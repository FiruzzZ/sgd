package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class FacturacionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, updatable = false)
    private Integer orderIndex;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @ManyToOne
    private SubTipoDocumento subTipoDocumento;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    @Column(length = 50)
    private String prestador;
    @Column(length = 20)
    private String expediente;
    @Column(length = 200)
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Facturacion facturacion;

    public FacturacionDetalle() {
    }

    public FacturacionDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Date documentoFecha, String prestador, String expediente, String observacion, Facturacion facturacion) {
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.documentoFecha = documentoFecha;
        this.prestador = prestador;
        this.expediente = expediente;
        this.observacion = observacion;
        this.facturacion = facturacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public SubTipoDocumento getSubTipoDocumento() {
        return subTipoDocumento;
    }

    public void setSubTipoDocumento(SubTipoDocumento subTipoDocumento) {
        this.subTipoDocumento = subTipoDocumento;
    }

    public Date getDocumentoFecha() {
        return documentoFecha;
    }

    public void setDocumentoFecha(Date documentoFecha) {
        this.documentoFecha = documentoFecha;
    }

    public String getPrestador() {
        return prestador;
    }

    public void setPrestador(String prestador) {
        this.prestador = prestador;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Facturacion getFacturacion() {
        return facturacion;
    }

    public void setFacturacion(Facturacion facturacion) {
        this.facturacion = facturacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FacturacionDetalle other = (FacturacionDetalle) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.tipoDocumento, other.tipoDocumento)) {
            return false;
        }
        if (!Objects.equals(this.subTipoDocumento, other.subTipoDocumento)) {
            return false;
        }
        if (!Objects.equals(this.documentoFecha, other.documentoFecha)) {
            return false;
        }
        if (!Objects.equals(this.prestador, other.prestador)) {
            return false;
        }
        if (!Objects.equals(this.expediente, other.expediente)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FacturacionDetalle{" + "id=" + id + ", orderIndex=" + orderIndex + ", tipoDocumento=" + tipoDocumento + ", subTipoDocumento=" + subTipoDocumento + ", documentoFecha=" + documentoFecha + ", prestador=" + prestador + ", expediente=" + expediente + ", observacion=" + observacion + '}';
    }

}
