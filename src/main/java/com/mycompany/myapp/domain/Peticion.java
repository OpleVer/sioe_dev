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

    @Column(name = "tipo_evaluacion")
    private Integer tipo_evaluacion;

    @Column(name = "numero_acta")
    private String numero_acta;

    @Column(name = "acta")
    private String acta;

    @Column(name = "acuerdo")
    private String acuerdo;

    @Column(name = "cedula")
    private String cedula;

    @Column(name = "descripcion_anexo")
    private String descripcion_anexo;

    @Column(name = "link_anexo")
    private String link_anexo;

    @Column(name = "completada")
    private Boolean completada;

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

    public Integer getTipo_evaluacion() {
        return tipo_evaluacion;
    }

    public Peticion tipo_evaluacion(Integer tipo_evaluacion) {
        this.tipo_evaluacion = tipo_evaluacion;
        return this;
    }

    public void setTipo_evaluacion(Integer tipo_evaluacion) {
        this.tipo_evaluacion = tipo_evaluacion;
    }

    public String getNumero_acta() {
        return numero_acta;
    }

    public Peticion numero_acta(String numero_acta) {
        this.numero_acta = numero_acta;
        return this;
    }

    public void setNumero_acta(String numero_acta) {
        this.numero_acta = numero_acta;
    }

    public String getActa() {
        return acta;
    }

    public Peticion acta(String acta) {
        this.acta = acta;
        return this;
    }

    public void setActa(String acta) {
        this.acta = acta;
    }

    public String getAcuerdo() {
        return acuerdo;
    }

    public Peticion acuerdo(String acuerdo) {
        this.acuerdo = acuerdo;
        return this;
    }

    public void setAcuerdo(String acuerdo) {
        this.acuerdo = acuerdo;
    }

    public String getCedula() {
        return cedula;
    }

    public Peticion cedula(String cedula) {
        this.cedula = cedula;
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDescripcion_anexo() {
        return descripcion_anexo;
    }

    public Peticion descripcion_anexo(String descripcion_anexo) {
        this.descripcion_anexo = descripcion_anexo;
        return this;
    }

    public void setDescripcion_anexo(String descripcion_anexo) {
        this.descripcion_anexo = descripcion_anexo;
    }

    public String getLink_anexo() {
        return link_anexo;
    }

    public Peticion link_anexo(String link_anexo) {
        this.link_anexo = link_anexo;
        return this;
    }

    public void setLink_anexo(String link_anexo) {
        this.link_anexo = link_anexo;
    }

    public Boolean isCompletada() {
        return completada;
    }

    public Peticion completada(Boolean completada) {
        this.completada = completada;
        return this;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
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
            ", tipo_evaluacion='" + getTipo_evaluacion() + "'" +
            ", numero_acta='" + getNumero_acta() + "'" +
            ", acta='" + getActa() + "'" +
            ", acuerdo='" + getAcuerdo() + "'" +
            ", cedula='" + getCedula() + "'" +
            ", descripcion_anexo='" + getDescripcion_anexo() + "'" +
            ", link_anexo='" + getLink_anexo() + "'" +
            ", completada='" + isCompletada() + "'" +
            "}";
    }
}
