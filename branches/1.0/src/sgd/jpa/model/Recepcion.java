package sgd.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
public class Recepcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    private Sector sector;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer numero;
    @ManyToOne(optional = false)
    private Usuario usuario;
    @Column(insertable = false, updatable = false, nullable = false, columnDefinition = "timestamp with time zone NOT NULL DEFAULT now()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;
    @OrderBy(value = "orderIndex")
    @OneToMany(mappedBy = "recepcion", cascade = CascadeType.ALL)
    private List<RecepcionDetalle> detalle;

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public List<RecepcionDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<RecepcionDetalle> detalle) {
        this.detalle = detalle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getNextOrderIndex() {
        if (detalle == null) {
            detalle = new ArrayList<>();
        }
        int orderIndex = 0;
        for (RecepcionDetalle o : detalle) {
            if (o.getOrderIndex() > orderIndex) {
                orderIndex = o.getOrderIndex();
            }
        }
        return orderIndex + 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Recepcion other = (Recepcion) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    
    @Override
    public String toString() {
        return "Recepcion{" + "id=" + id + ", sector=" + sector.toString() + ", numero=" + numero + ", usuario=" + usuario + ", creation=" + creation + ", detalle=" + detalle + '}';
    }
    
    
}
