package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
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
public class CronicoDetalle implements Serializable {

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
    private Integer numeroAfiliado;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer numeroFamiliar;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer numeroDocumento;
    private Integer numeroFormulario;
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    @Column(length = 200)
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Cronico cronico;

    public CronicoDetalle() {
    }

    public CronicoDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Integer numeroAfiliado, Integer numeroFamiliar, Integer numeroDocumento, Integer numeroFormulario, Date documentoFecha, String observacion, Cronico cronico) {
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.numeroAfiliado = numeroAfiliado;
        this.numeroFamiliar = numeroFamiliar;
        this.numeroDocumento = numeroDocumento;
        this.numeroFormulario = numeroFormulario;
        this.documentoFecha = documentoFecha;
        this.observacion = observacion;
        this.cronico = cronico;
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

    public Integer getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(Integer numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
    }

    public Integer getNumeroFamiliar() {
        return numeroFamiliar;
    }

    public void setNumeroFamiliar(Integer numeroFamiliar) {
        this.numeroFamiliar = numeroFamiliar;
    }

    public Date getDocumentoFecha() {
        return documentoFecha;
    }

    public void setDocumentoFecha(Date documentoFecha) {
        this.documentoFecha = documentoFecha;
    }

    public Integer getNumeroFormulario() {
        return numeroFormulario;
    }

    public void setNumeroFormulario(Integer numeroFormulario) {
        this.numeroFormulario = numeroFormulario;
    }

    public Integer getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Integer numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Cronico getCronico() {
        return cronico;
    }

    public void setCronico(Cronico cronico) {
        this.cronico = cronico;
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
        if (!(object instanceof CronicoDetalle)) {
            return false;
        }
        CronicoDetalle other = (CronicoDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
