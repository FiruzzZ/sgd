package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class ContableDetalle implements Serializable {

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
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    @Column
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Contable contable;

    public ContableDetalle() {
    }

    public ContableDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Date documentoFecha, String observacion, Contable contable) {
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.documentoFecha = documentoFecha;
        this.observacion = observacion;
        this.contable = contable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Contable getContable() {
        return contable;
    }

    public void setContable(Contable contable) {
        this.contable = contable;
    }

    public Date getDocumentoFecha() {
        return documentoFecha;
    }

    public void setDocumentoFecha(Date documentoFecha) {
        this.documentoFecha = documentoFecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public SubTipoDocumento getSubTipoDocumento() {
        return subTipoDocumento;
    }

    public void setSubTipoDocumento(SubTipoDocumento subTipoDocumento) {
        this.subTipoDocumento = subTipoDocumento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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
        final ContableDetalle other = (ContableDetalle) obj;
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
        return true;
    }


    @Override
    public String toString() {
        return "ContableDetalle{" + "id=" + id + ", orderIndex=" + orderIndex + ", tipoDocumento=" + tipoDocumento + ", subTipoDocumento=" + subTipoDocumento + ", documentoFecha=" + documentoFecha + ", observacion=" + observacion + ", contable=" + contable.getId() + '}';
    }

    
}
