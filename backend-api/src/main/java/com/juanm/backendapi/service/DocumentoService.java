package com.juanm.backendapi.service;

import com.juanm.backendapi.model.Documento;
import com.juanm.backendapi.model.TipoDocumento;
import com.juanm.backendapi.model.Usuario;
import com.juanm.backendapi.repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DocumentoService {

    private final StorageService storageService;
    private final PdfService pdfService;
    private final DocumentoRepository documentoRepository;

    public DocumentoService(
            StorageService storageService,
            PdfService pdfService,
            DocumentoRepository documentoRepository) {
        this.storageService = storageService;
        this.pdfService = pdfService;
        this.documentoRepository = documentoRepository;
    }

    @Transactional
    public Documento procesarYGuardarDocumento(MultipartFile file, String usuarioId) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new RuntimeException("Solo se permiten archivos PDF.");
        }

        String rutaGuardada = storageService.guardarArchivo(file, usuarioId);

        String textoExtraido = pdfService.extraerTextoDePdf(file);

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(usuarioId);

        Documento documento = new Documento();
        documento.setId(UUID.randomUUID().toString());
        documento.setUsuario(usuarioMock);
        documento.setNombreArchivo(file.getOriginalFilename());
        documento.setTipo(TipoDocumento.PDF);
        documento.setTamano(file.getSize());
        documento.setRutaAlmacenamiento(rutaGuardada);
        documento.setTextoExtraido(textoExtraido);

        return documentoRepository.save(documento);
    }
}