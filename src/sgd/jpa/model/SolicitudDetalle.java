
package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FiruzzZ
 */
@Entity
@Table(catalog = "sgd", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudDetalle.findAll", query = "SELECT s FROM SolicitudDetalle s")})
public class SolicitudDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer archivoId;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String barcode;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer orderIndex;
    @JoinColumn(name = "solicitud_id", nullable = false)
    @ManyToOne(optional = false)
    private Solicitud solicitud;

    public SolicitudDetalle() {
    }

    public SolicitudDetalle(Integer id) {
        this.id = id;
    }

    public SolicitudDetalle(Integer orderIndex, Integer archivoId, String barcode, Solicitud solicitud) {
        this.orderIndex = orderIndex;
        this.archivoId = archivoId;
        this.barcode = barcode;
        this.solicitud = solicitud;
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

    public void setArchivoId(Integer archivoid) {
        this.archivoId = archivoid;
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

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
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
        if (!(object instanceof SolicitudDetalle)) {
            return false;
        }
        SolicitudDetalle other = (SolicitudDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sgd.jpa.model.SolicitudDetalle[ id=" + id + " ]";
    }

}
