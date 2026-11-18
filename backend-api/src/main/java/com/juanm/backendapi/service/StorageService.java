package com.juanm.backendapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation;

    public StorageService(@Value("${app.storage.location:uploads}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
    }

    public String guardarArchivo(MultipartFile file, String usuarioId) {
        if (file.isEmpty()) {
            throw new RuntimeException("Error: No se puede guardar un archivo vacío.");
        }

        String nombreOriginal = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (nombreOriginal.contains("..")) {
            throw new RuntimeException("Nombre de archivo inválido: " + nombreOriginal);
        }

        try {
            Path directorioUsuario = this.rootLocation.resolve(usuarioId).normalize();
            Files.createDirectories(directorioUsuario);

            String nombreUnico = UUID.randomUUID() + "_" + nombreOriginal;
            Path destinoArchivo = directorioUsuario.resolve(nombreUnico);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinoArchivo, StandardCopyOption.REPLACE_EXISTING);
            }

            return Paths.get("uploads", usuarioId, nombreUnico).toString().replace("\\", "/");

        } catch (IOException e) {
            throw new RuntimeException("Fallo al guardar el archivo en el disco: " + nombreOriginal, e);
        }
    }
}