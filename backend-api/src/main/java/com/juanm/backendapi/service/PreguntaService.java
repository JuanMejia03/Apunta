package com.juanm.backendapi.service;

import com.juanm.backendapi.dto.pregunta.OpcionDto;
import com.juanm.backendapi.dto.pregunta.PreguntaDto;
import com.juanm.backendapi.dto.pregunta.PreguntaGeneradaDto;
import com.juanm.backendapi.dto.pregunta.ResultadoGeneracionDto;
import com.juanm.backendapi.integration.gemini.GeminiPreguntasClient;
import com.juanm.backendapi.model.Documento;
import com.juanm.backendapi.model.Opcion;
import com.juanm.backendapi.model.Pregunta;
import com.juanm.backendapi.repository.DocumentoRepository;
import com.juanm.backendapi.repository.PreguntaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class PreguntaService {
    private final DocumentoRepository documentos;
    private final PreguntaRepository preguntas;
    private final GeminiPreguntasClient gemini;
    private final EntityManager entityManager;
    private final TransactionTemplate transaccion;

    PreguntaService(DocumentoRepository documentos, PreguntaRepository preguntas,
                    GeminiPreguntasClient gemini, EntityManager entityManager,
                    PlatformTransactionManager transactionManager) {
        this.documentos = documentos;
        this.preguntas = preguntas;
        this.gemini = gemini;
        this.entityManager = entityManager;
        this.transaccion = new TransactionTemplate(transactionManager);
    }

    @Transactional(readOnly = true)
    public List<PreguntaDto> listarPorDocumento(String documentoId, String usuarioId) {
        if (!documentos.existsByIdAndUsuarioId(documentoId, usuarioId)) {
            throw documentoNoEncontrado();
        }

        return preguntas.buscarConOpciones(documentoId, usuarioId).stream()
                .map(pregunta -> new PreguntaDto(pregunta.getId(), pregunta.getEnunciado(), pregunta.getTema(),
                        pregunta.getOpciones().stream()
                                .sorted(Comparator.comparingInt(Opcion::getLiteral).thenComparing(Opcion::getId))
                                .map(opcion -> new OpcionDto(opcion.getId(), opcion.getLiteral(), opcion.getTexto()))
                                .toList()))
                .toList();
    }

    public ResultadoGeneracionDto generar(String documentoId, String usuarioId) {
        Documento documento = documentos.findByIdAndUsuarioId(documentoId, usuarioId)
                .orElseThrow(PreguntaService::documentoNoEncontrado);
        long existentes = preguntas.countByDocumentoId(documentoId);
        if (existentes > 0) {
            return new ResultadoGeneracionDto(documentoId, existentes, false);
        }
        String texto = documento.getTextoExtraido();
        if (texto == null || texto.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "El documento no contiene texto para generar preguntas.");
        }

        List<PreguntaGeneradaDto> generadas = gemini.generar(texto);
        if (generadas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "El texto no permite generar preguntas de repaso.");
        }

        // Serializar la escritura por documento sin mantener un bloqueo durante la llamada HTTP.
        return transaccion.execute(status -> {
            Documento documentoBloqueado = documentos.findForUpdateByIdAndUsuarioId(documentoId, usuarioId)
                    .orElseThrow(PreguntaService::documentoNoEncontrado);
            long cantidadGuardada = preguntas.countByDocumentoId(documentoId);
            if (cantidadGuardada > 0) {
                return new ResultadoGeneracionDto(documentoId, cantidadGuardada, false);
            }
            for (var generada : generadas) {
                guardarPregunta(documentoBloqueado, generada);
            }
            return new ResultadoGeneracionDto(documentoId, generadas.size(), true);
        });
    }

    private void guardarPregunta(Documento documento, PreguntaGeneradaDto generada) {
        Pregunta pregunta = new Pregunta();
        pregunta.setId(UUID.randomUUID().toString());
        pregunta.setDocumento(documento);
        pregunta.setEnunciado(generada.enunciado().strip());
        pregunta.setTema(generada.tema().strip());
        pregunta.setRetroalimentacion(generada.retroalimentacion().strip());
        entityManager.persist(pregunta);

        for (int indice = 0; indice < generada.opciones().size(); indice++) {
            Opcion opcion = new Opcion();
            opcion.setId(UUID.randomUUID().toString());
            opcion.setPregunta(pregunta);
            opcion.setLiteral((char) ('A' + indice));
            opcion.setTexto(generada.opciones().get(indice).strip());
            opcion.setEsCorrecta(indice + 1 == generada.correcta());
            entityManager.persist(opcion);
        }
    }

    private static ResponseStatusException documentoNoEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado.");
    }

}
