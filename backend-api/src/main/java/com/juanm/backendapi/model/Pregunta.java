package com.juanm.backendapi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(nullable = false)
    private String tema;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String retroalimentacion;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opcion> opciones;

    // Constructors, Getters, and Setters
    public Pregunta() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Documento getDocumento() { return documento; }
    public void setDocumento(Documento documento) { this.documento = documento; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }
}