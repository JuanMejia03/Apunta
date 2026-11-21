package com.juanm.backendapi.repository;

import com.juanm.backendapi.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, String> {
    long countByDocumentoId(String documentoId);

    @Query("""
            select distinct p from Pregunta p
            left join fetch p.opciones
            where p.documento.id = :documentoId and p.documento.usuario.id = :usuarioId
            order by p.id
            """)
    List<Pregunta> buscarConOpciones(String documentoId, String usuarioId);
}
