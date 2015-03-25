package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Clase que persiste a {@link SectorUI}
 *
 * @author FiruzzZ
 */
@Entity
public class Sector implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    @OneToMany(mappedBy = "sector")
    private List<TipoDocumento> tipoDocumentos;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false, columnDefinition = "timestamp with time zone NOT NULL DEFAULT now()")
    private Date creacion;
    @Column(unique = true, nullable = false, updatable = false, length = 40)
    private String nombre;
    @Transient
    private transient SectorUI sectorUI;

    public Sector() {
    }

    public Sector(Integer id) {
        this.id = id;
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

    public List<TipoDocumento> getTipoDocumentos() {
        return tipoDocumentos;
    }

    public void setTipoDocumentos(List<TipoDocumento> tipoDocumentos) {
        this.tipoDocumentos = tipoDocumentos;
    }

    public SectorUI getSectorUI() {
        return sectorUI;
    }

    @PostLoad
    public void setSectorUI() {
        this.sectorUI = SectorUI.getByCode(id);
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
        if (!(object instanceof Sector)) {
            return false;
        }
        Sector other = (Sector) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return sectorUI.getNombre();
    }
}
