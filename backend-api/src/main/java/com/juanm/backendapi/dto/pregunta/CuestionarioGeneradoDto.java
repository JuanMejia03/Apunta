package com.juanm.backendapi.dto.pregunta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public record CuestionarioGeneradoDto(
        @NotNull @Size(max = MAX_PREGUNTAS) List<@NotNull @Valid PreguntaGeneradaDto> preguntas) {

    public static final int MAX_PREGUNTAS = 5;

    @AssertTrue(message = "Los enunciados deben ser distintos")
    public boolean isEnunciadosDistintos() {
        return preguntas == null || preguntas.stream().filter(Objects::nonNull)
                .map(PreguntaGeneradaDto::enunciado).filter(Objects::nonNull)
                .map(enunciado -> enunciado.strip().toLowerCase(Locale.ROOT))
                .distinct().count() == preguntas.size();
    }
}
