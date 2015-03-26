package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author nsteinhilber
 */
@Entity
public class AuditoriaMedicaDetalle implements Serializable {

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
    @Column
    private Long numeroAfiliado;
    @Basic(optional = false)
    @Column
    private Integer numeroDocumento;
    @Column
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    @Column
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private AuditoriaMedica auditoriaMedica;
     @Column(length = 200)
    private String delegacion;

    public AuditoriaMedicaDetalle() {
    }

    public AuditoriaMedicaDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Long numeroAfiliado, Integer numeroDocumento, Date documentoFecha, String observacion, AuditoriaMedica auditoriaMedica, String delegacion) {
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.numeroAfiliado = numeroAfiliado;
        this.documentoFecha = documentoFecha;
        this.observacion = observacion;
        this.auditoriaMedica = auditoriaMedica;
        this.numeroDocumento = numeroDocumento;
        this.delegacion = delegacion;
    }

    public AuditoriaMedica getAuditoriaMedica() {
        return auditoriaMedica;
    }

    public void setAuditoriaMedica(AuditoriaMedica auditoriaMedica) {
        this.auditoriaMedica = auditoriaMedica;
    }

    public Integer getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Integer numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }



    public Date getDocumentoFecha() {
        return documentoFecha;
    }

    public void setDocumentoFecha(Date documentoFecha) {
        this.documentoFecha = documentoFecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

  
    public Long getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(Long numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
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

    public String getDelegacion() {
        return delegacion;
    }

    public void setDelegacion(String delegacion) {
        this.delegacion = delegacion;
    }

   
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuditoriaMedicaDetalle other = (AuditoriaMedicaDetalle) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "AuditoriaMedicaDetalle{" + "id=" + id + ", orderIndex=" + orderIndex 
                + ", tipoDocumento=" + tipoDocumento + ", subTipoDocumento=" + subTipoDocumento 
                + ", numeroAfiliado=" + numeroAfiliado + ", numeroDocumento=" + numeroDocumento
                + ", documentacionFecha=" + documentoFecha + ", observacion=" + observacion + ", auditoriaMedica=" + auditoriaMedica.getId() + '}';
    }
}
