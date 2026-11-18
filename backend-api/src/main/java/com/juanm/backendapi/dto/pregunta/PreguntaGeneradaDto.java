package com.juanm.backendapi.dto.pregunta;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public record PreguntaGeneradaDto(
        @NotBlank String enunciado,
        @NotBlank @Size(max = 255) String tema,
        @NotBlank String retroalimentacion,
        @NotNull @Size(min = 4, max = 4) List<@NotBlank String> opciones,
        @NotNull @Min(1) @Max(4) Integer correcta) {

    @AssertTrue(message = "Las opciones deben ser distintas")
    public boolean isOpcionesDistintas() {
        return opciones == null || opciones.stream().filter(Objects::nonNull)
                .map(opcion -> opcion.strip().toLowerCase(Locale.ROOT))
                .distinct().count() == opciones.size();
    }
}
