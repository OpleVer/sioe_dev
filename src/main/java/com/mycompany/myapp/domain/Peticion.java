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
            "}";
    }
}
