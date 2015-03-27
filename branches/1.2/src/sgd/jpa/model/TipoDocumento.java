/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class TipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 40)
    @OrderBy
    private String nombre;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false, columnDefinition = "timestamp with time zone NOT NULL DEFAULT now()")
    private Date creacion;
    @OneToMany(mappedBy = "tipoDocumento", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<SubTipoDocumento> subTipoDocumentos;
    @ManyToOne(optional = false)
    private Sector sector;

    public TipoDocumento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getCreacion() {
        return creacion;
    }

    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }

    public List<SubTipoDocumento> getSubTipoDocumentos() {
        return subTipoDocumentos;
    }

    public void setSubTipoDocumentos(List<SubTipoDocumento> subTipoDocumentos) {
        this.subTipoDocumentos = subTipoDocumentos;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDocumento)) {
            return false;
        }
        TipoDocumento other = (TipoDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoDocumento{" + "id=" + id + ", nombre=" + nombre + ", creacion=" + creacion + ", subTipoDocumentos=" + subTipoDocumentos.size() + ", sector=" + sector + '}';
    }
}
