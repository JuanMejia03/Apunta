package com.juanm.backendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "retos")
public class Reto {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = true)
    private Documento documento;

    @Column(name = "fecha_inicio", nullable = false, updatable = false)
    private LocalDateTime fechaInicio = LocalDateTime.now();

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "duracion_segundos", nullable = false)
    private int duracionSegundos = 0;

    @Column(name = "total_preguntas", nullable = false)
    private int totalPreguntas = 0;

    @Column(name = "respuestas_acertadas", nullable = false)
    private int respuestasAcertadas = 0;

    @Column(name = "xp_ganado", nullable = false)
    private int xpGanado = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReto estado = EstadoReto.EN_PROCESO;

    @OneToMany(mappedBy = "reto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaUsuario> respuestasUsuario;

    // Constructors, Getters, and Setters
    public Reto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Documento getDocumento() { return documento; }
    public void setDocumento(Documento documento) { this.documento = documento; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public int getDuracionSegundos() { return duracionSegundos; }
    public void setDuracionSegundos(int duracionSegundos) { this.duracionSegundos = duracionSegundos; }
    public int getTotalPreguntas() { return totalPreguntas; }
    public void setTotalPreguntas(int totalPreguntas) { this.totalPreguntas = totalPreguntas; }
    public int getRespuestasAcertadas() { return respuestasAcertadas; }
    public void setRespuestasAcertadas(int respuestasAcertadas) { this.respuestasAcertadas = respuestasAcertadas; }
    public int getXpGanado() { return xpGanado; }
    public void setXpGanado(int xpGanado) { this.xpGanado = xpGanado; }
    public EstadoReto getEstado() { return estado; }
    public void setEstado(EstadoReto estado) { this.estado = estado; }
}