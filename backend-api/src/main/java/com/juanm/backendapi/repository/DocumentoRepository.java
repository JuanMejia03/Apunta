package com.juanm.backendapi.repository;

import com.juanm.backendapi.model.Documento;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, String> {

    List<Documento> findByUsuarioId(String usuarioId);

    Optional<Documento> findByIdAndUsuarioId(String id, String usuarioId);

    boolean existsByIdAndUsuarioId(String id, String usuarioId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Documento> findForUpdateByIdAndUsuarioId(String id, String usuarioId);
}
