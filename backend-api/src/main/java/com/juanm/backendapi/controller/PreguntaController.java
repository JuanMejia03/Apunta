package com.juanm.backendapi.controller;

import com.juanm.backendapi.dto.pregunta.PreguntaDto;
import com.juanm.backendapi.service.PreguntaService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documentos/{documentoId}/preguntas")
public class PreguntaController {
    private final PreguntaService preguntaService;

    public PreguntaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    @GetMapping
    public List<PreguntaDto> listar(
            @PathVariable @NotBlank @Size(max = 36) String documentoId,
            @RequestParam @NotBlank @Size(max = 36) String usuarioId) {
        return preguntaService.listarPorDocumento(documentoId, usuarioId);
    }
}
