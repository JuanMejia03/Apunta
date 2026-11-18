package com.juanm.backendapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "opciones")
public class Opcion {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta pregunta;

    @Column(nullable = false, length = 1)
    private char literal;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta = false;

    public Opcion() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Pregunta getPregunta() { return pregunta; }
    public void setPregunta(Pregunta pregunta) { this.pregunta = pregunta; }
    public char getLiteral() { return literal; }
    public void setLiteral(char literal) { this.literal = literal; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public boolean isEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
}