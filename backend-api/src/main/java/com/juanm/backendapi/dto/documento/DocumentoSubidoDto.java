package com.juanm.backendapi.dto.documento;

import com.juanm.backendapi.model.TipoDocumento;

import java.time.LocalDateTime;

public record DocumentoSubidoDto(
        String id,
        String usuarioId,
        String nombreArchivo,
        TipoDocumento tipo,
        long tamano,
        LocalDateTime fechaSubido) {}
