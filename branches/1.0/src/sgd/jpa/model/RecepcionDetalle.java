package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"reception_id, barcode"})
})
public class RecepcionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, updatable = false)
    private Integer orderIndex;
    @Basic(optional = false)
    @Column(nullable = false, updatable = false)
    private Integer archivoId;
    @Basic(optional = false)
    @Column(nullable = false, updatable = false)
    private String barcode;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Recepcion recepcion;

    public RecepcionDetalle() {
        //ORM
    }

    public RecepcionDetalle(Integer orderIndex, Integer archivoId, String barcode, Recepcion recepcion) {
        this.orderIndex = orderIndex;
        this.archivoId = archivoId;
        this.barcode = barcode;
        this.recepcion = recepcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(Integer archivoId) {
        this.archivoId = archivoId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Recepcion getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(Recepcion recepcion) {
        this.recepcion = recepcion;
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
        if (!(object instanceof RecepcionDetalle)) {
            return false;
        }
        RecepcionDetalle other = (RecepcionDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RecepcionDetalle{" + "id=" + id + ", orderIndex=" + orderIndex + ", archivoId=" + archivoId + ", barcode=" + barcode + ", recepcion=" + (recepcion == null ? null : recepcion.getId()) + '}';
    }
}
