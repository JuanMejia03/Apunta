package com.juanm.backendapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "respuestas_usuario")
public class RespuestaUsuario {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reto_id", nullable = false)
    private Reto reto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta pregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcion_id", nullable = false)
    private Opcion opcion;

    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta;

    @Column(name = "orden_pregunta", nullable = false)
    private int ordenPregunta;

    // Constructors, Getters, and Setters
    public RespuestaUsuario() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Reto getReto() { return reto; }
    public void setReto(Reto reto) { this.reto = reto; }
    public Pregunta getPregunta() { return pregunta; }
    public void setPregunta(Pregunta pregunta) { this.pregunta = pregunta; }
    public Opcion getOpcion() { return opcion; }
    public void setOpcion(Opcion opcion) { this.opcion = opcion; }
    public boolean isEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
    public int getOrdenPregunta() { return ordenPregunta; }
    public void setOrdenPregunta(int ordenPregunta) { this.ordenPregunta = ordenPregunta; }
}