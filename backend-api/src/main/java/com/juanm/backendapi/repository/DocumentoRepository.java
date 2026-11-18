package com.juanm.backendapi.repository;

import com.juanm.backendapi.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, String> {

    List<Documento> findByUsuarioId(String usuarioId);
}