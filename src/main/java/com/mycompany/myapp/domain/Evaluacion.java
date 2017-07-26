package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Evaluacion.
 */
@Entity
@Table(name = "evaluacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Evaluacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    @Column(name = "numero_acta")
    private String numero_acta;

    @Column(name = "acta")
    private String acta;

    @Column(name = "acuerdo")
    private String acuerdo;

    @Column(name = "cedula")
    private String cedula;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTipo() {
        return tipo;
    }

    public Evaluacion tipo(Integer tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getNumero_acta() {
        return numero_acta;
    }

    public Evaluacion numero_acta(String numero_acta) {
        this.numero_acta = numero_acta;
        return this;
    }

    public void setNumero_acta(String numero_acta) {
        this.numero_acta = numero_acta;
    }

    public String getActa() {
        return acta;
    }

    public Evaluacion acta(String acta) {
        this.acta = acta;
        return this;
    }

    public void setActa(String acta) {
        this.acta = acta;
    }

    public String getAcuerdo() {
        return acuerdo;
    }

    public Evaluacion acuerdo(String acuerdo) {
        this.acuerdo = acuerdo;
        return this;
    }

    public void setAcuerdo(String acuerdo) {
        this.acuerdo = acuerdo;
    }

    public String getCedula() {
        return cedula;
    }

    public Evaluacion cedula(String cedula) {
        this.cedula = cedula;
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Evaluacion evaluacion = (Evaluacion) o;
        if (evaluacion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), evaluacion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Evaluacion{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", numero_acta='" + getNumero_acta() + "'" +
            ", acta='" + getActa() + "'" +
            ", acuerdo='" + getAcuerdo() + "'" +
            ", cedula='" + getCedula() + "'" +
            "}";
    }
}
