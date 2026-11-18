package com.juanm.backendapi.dto.documento;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public record SubirDocumentoDto(
        @NotNull(message = "El archivo es obligatorio") MultipartFile file,
        @NotBlank @Size(max = 36) @Pattern(regexp = "(?!\\.{1,2}$)[^/\\\\:]+") String usuarioId) {

    @AssertTrue(message = "El archivo debe ser un PDF con contenido")
    public boolean isPdfValido() {
        return file == null || (!file.isEmpty() && MediaType.APPLICATION_PDF_VALUE.equals(file.getContentType()));
    }

    @AssertTrue(message = "El nombre del archivo no es válido")
    public boolean isNombreArchivoValido() {
        if (file == null) {
            return true;
        }
        String nombre = file.getOriginalFilename();
        return nombre != null && !nombre.isBlank() && nombre.length() <= 255
                && !nombre.contains("..") && nombre.indexOf('/') < 0 && nombre.indexOf('\\') < 0;
    }
}
