package com.juanm.backendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "xp_total", nullable = false)
    private int xpTotal = 0;

    @Column(name = "meta_xp", nullable = false)
    private int metaXp = 100;

    @Column(name = "dia_racha", nullable = false)
    private int diaRacha = 0;

    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documento> documentos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reto> retos;

    // Constructors, Getters, and Setters
    public Usuario() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public int getXpTotal() { return xpTotal; }
    public void setXpTotal(int xpTotal) { this.xpTotal = xpTotal; }
    public int getMetaXp() { return metaXp; }
    public void setMetaXp(int metaXp) { this.metaXp = metaXp; }
    public int getDiaRacha() { return diaRacha; }
    public void setDiaRacha(int diaRacha) { this.diaRacha = diaRacha; }
    public LocalDateTime getUltimaActividad() { return ultimaActividad; }
    public void setUltimaActividad(LocalDateTime ultimaActividad) { this.ultimaActividad = ultimaActividad; }
}