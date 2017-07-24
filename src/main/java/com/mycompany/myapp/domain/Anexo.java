package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Anexo.
 */
@Entity
@Table(name = "anexo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Anexo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "jhi_link", nullable = false)
    private String link;

    @ManyToOne
    private Peticion peticion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Anexo descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLink() {
        return link;
    }

    public Anexo link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Peticion getPeticion() {
        return peticion;
    }

    public Anexo peticion(Peticion peticion) {
        this.peticion = peticion;
        return this;
    }

    public void setPeticion(Peticion peticion) {
        this.peticion = peticion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Anexo anexo = (Anexo) o;
        if (anexo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), anexo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Anexo{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", link='" + getLink() + "'" +
            "}";
    }
}
