package sgd.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Es la nota de envío (culpa de alguien se terminó llamando recibo!)
 * @author Administrador
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"numero", "sector_id"})
})
public class Recibo implements Serializable {

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
    @OneToMany(mappedBy = "recibo", cascade = CascadeType.ALL)
    private List<ReciboDetalle> detalle;

    public Recibo() {
        //ORM
    }

    public Recibo(Sector sector, Integer numero, Usuario usuario, List<ReciboDetalle> detalle) {
        this.sector = sector;
        this.numero = numero;
        this.usuario = usuario;
        this.detalle = detalle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public List<ReciboDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<ReciboDetalle> detalle) {
        this.detalle = detalle;
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
            detalle = new ArrayList<ReciboDetalle>();
        }
        int orderIndex = 0;
        for (ReciboDetalle o : detalle) {
            if (o.getOrderIndex() > orderIndex) {
                orderIndex = o.getOrderIndex();
            }
        }
        return orderIndex + 1;
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
        if (!(object instanceof Recibo)) {
            return false;
        }
        Recibo other = (Recibo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Recibo{" + "id=" + id + ", sector=" + sector + ", numero=" + numero + ", usuario=" + usuario + ", creation=" + creation + ", detalle=" + detalle + '}';
    }
}
