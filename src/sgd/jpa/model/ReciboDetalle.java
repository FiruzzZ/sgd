package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class ReciboDetalle implements Serializable {

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
    private Recibo recibo;

    public ReciboDetalle() {
        //ORM
    }

    public ReciboDetalle(Recibo recibo, Integer orderIndex, Integer archivoId, String barcode) {
        this.recibo = recibo;
        this.orderIndex = orderIndex;
        this.archivoId = archivoId;
        this.barcode = barcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public Integer getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(Integer archivoId) {
        this.archivoId = archivoId;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
        if (!(object instanceof ReciboDetalle)) {
            return false;
        }
        ReciboDetalle other = (ReciboDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReciboDetalle{" + "id=" + id + ", orderIndex=" + orderIndex + ", archivoId=" + archivoId + ", barcode=" + barcode + ", recibo=" + recibo.getId() + '}';
    }
}
