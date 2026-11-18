package com.juanm.backendapi.controller;

import com.juanm.backendapi.model.Documento;
import com.juanm.backendapi.service.DocumentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping("/subir")
    public ResponseEntity<?> subirDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("usuarioId") String usuarioId) {
        try {
            Documento documentoGuardado = documentoService.procesarYGuardarDocumento(file, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(documentoGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al procesar el documento."));
        }
    }
}