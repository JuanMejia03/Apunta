package com.juanm.backendapi.controller;

import com.juanm.backendapi.dto.documento.SubirDocumentoDto;
import com.juanm.backendapi.dto.pregunta.ResultadoGeneracionDto;
import com.juanm.backendapi.service.DocumentoService;
import com.juanm.backendapi.service.PreguntaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;
    private final PreguntaService preguntaService;

    public DocumentoController(DocumentoService documentoService, PreguntaService preguntaService) {
        this.documentoService = documentoService;
        this.preguntaService = preguntaService;
    }

    @PostMapping("/{documentoId}/preguntas/generar")
    public ResponseEntity<ResultadoGeneracionDto> generarPreguntas(
            @PathVariable @NotBlank @Size(max = 36) String documentoId,
            @RequestParam @NotBlank @Size(max = 36) String usuarioId) {
        var resultado = preguntaService.generar(documentoId, usuarioId);
        return ResponseEntity.status(resultado.generadas() ? HttpStatus.CREATED : HttpStatus.OK).body(resultado);
    }

    @PostMapping("/subir")
    public ResponseEntity<?> subirDocumento(@Valid @ModelAttribute SubirDocumentoDto solicitud) {
        try {
            var documentoGuardado = documentoService.procesarYGuardarDocumento(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body(documentoGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al procesar el documento."));
        }
    }
}
