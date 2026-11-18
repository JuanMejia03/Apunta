package com.juanm.backendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipo;

    @Column(nullable = false)
    private Long tamano;

    @Column(name = "ruta_almacenamiento", nullable = false, length = 500)
    private String rutaAlmacenamiento;

    @Lob
    @Column(name = "texto_extraido", columnDefinition = "LONGTEXT")
    private String textoExtraido;

    @Column(name = "fecha_subido", nullable = false, updatable = false)
    private LocalDateTime fechaSubido = LocalDateTime.now();

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pregunta> preguntas;

    @OneToMany(mappedBy = "documento")
    private List<Reto> retos;

    // Constructors, Getters, and Setters
    public Documento() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    public TipoDocumento getTipo() { return tipo; }
    public void setTipo(TipoDocumento tipo) { this.tipo = tipo; }
    public Long getTamano() { return tamano; }
    public void setTamano(Long tamano) { this.tamano = tamano; }
    public String getRutaAlmacenamiento() { return rutaAlmacenamiento; }
    public void setRutaAlmacenamiento(String rutaAlmacenamiento) { this.rutaAlmacenamiento = rutaAlmacenamiento; }
    public String getTextoExtraido() { return textoExtraido; }
    public void setTextoExtraido(String textoExtraido) { this.textoExtraido = textoExtraido; }
    public LocalDateTime getFechaSubido() { return fechaSubido; }
    public void setFechaSubido(LocalDateTime fechaSubido) { this.fechaSubido = fechaSubido; }
}