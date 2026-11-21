package com.juanm.backendapi.dto.pregunta;

import java.util.List;

public record PreguntaDto(String id, String enunciado, String tema, List<OpcionDto> opciones) {}
