package com.juanm.backendapi.repository;

import com.juanm.backendapi.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreguntaRepository extends JpaRepository<Pregunta, String> {
    long countByDocumentoId(String documentoId);
}
