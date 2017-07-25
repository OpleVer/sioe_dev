package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Peticion.
 */
@Entity
@Table(name = "peticion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Peticion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "numero_peticion", length = 32, nullable = false)
    private String numero_peticion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "acto_certificar", nullable = false)
    private String acto_certificar;

    @NotNull
    @Size(max = 60)
    @Column(name = "responsable", length = 60, nullable = false)
    private String responsable;

    @NotNull
    @Size(max = 65)
    @Column(name = "solicitante", length = 65, nullable = false)
    private String solicitante;

    @NotNull
    @Size(max = 200)
    @Column(name = "direccion", length = 200, nullable = false)
    private String direccion;

    @Column(name = "oficio")
    private String oficio;

    @NotNull
    @Size(max = 40)
    @Column(name = "cargo_solicitante", length = 40, nullable = false)
    private String cargo_solicitante;

    @ManyToOne(optional = false)
    @NotNull
    private Peticionario peticionarios;

    @ManyToOne(optional = false)
    @NotNull
    private Responsable responsables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero_peticion() {
        return numero_peticion;
    }

    public Peticion numero_peticion(String numero_peticion) {
        this.numero_peticion = numero_peticion;
        return this;
    }

    public void setNumero_peticion(String numero_peticion) {
        this.numero_peticion = numero_peticion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Peticion fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getActo_certificar() {
        return acto_certificar;
    }

    public Peticion acto_certificar(String acto_certificar) {
        this.acto_certificar = acto_certificar;
        return this;
    }

    public void setActo_certificar(String acto_certificar) {
        this.acto_certificar = acto_certificar;
    }

    public String getResponsable() {
        return responsable;
    }

    public Peticion responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public Peticion solicitante(String solicitante) {
        this.solicitante = solicitante;
        return this;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getDireccion() {
        return direccion;
    }

    public Peticion direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getOficio() {
        return oficio;
    }

    public Peticion oficio(String oficio) {
        this.oficio = oficio;
        return this;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public String getCargo_solicitante() {
        return cargo_solicitante;
    }

    public Peticion cargo_solicitante(String cargo_solicitante) {
        this.cargo_solicitante = cargo_solicitante;
        return this;
    }

    public void setCargo_solicitante(String cargo_solicitante) {
        this.cargo_solicitante = cargo_solicitante;
    }

    public Peticionario getPeticionarios() {
        return peticionarios;
    }

    public Peticion peticionarios(Peticionario peticionario) {
        this.peticionarios = peticionario;
        return this;
    }

    public void setPeticionarios(Peticionario peticionario) {
        this.peticionarios = peticionario;
    }

    public Responsable getResponsables() {
        return responsables;
    }

    public Peticion responsables(Responsable responsable) {
        this.responsables = responsable;
        return this;
    }

    public void setResponsables(Responsable responsable) {
        this.responsables = responsable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Peticion peticion = (Peticion) o;
        if (peticion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), peticion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Peticion{" +
            "id=" + getId() +
            ", numero_peticion='" + getNumero_peticion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", acto_certificar='" + getActo_certificar() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", solicitante='" + getSolicitante() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", oficio='" + getOficio() + "'" +
            ", cargo_solicitante='" + getCargo_solicitante() + "'" +
            "}";
    }
}
