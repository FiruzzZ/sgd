package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
public class GremialesDetalle implements Serializable {

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
    private Long documentoNumero;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    /**
     * A concatenation of CUIT_nombre
     */
    @Column(length = 150)
    private String empresa;
    /**
     * A concatenation of CUIL_nombre
     */
    @Column(length = 150)
    private String empleado;
    @Column(length = 255)
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Gremiales gremiales;

    public GremialesDetalle() {
    }

    public GremialesDetalle(Integer id, Integer orderIndex, TipoDocumento tipoDocumento,
            SubTipoDocumento subTipoDocumento, Long documentoNumero, Date documentoFecha,
            String empresa, String empleado, String observacion, Gremiales archivo) {
        this.id = id;
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.documentoNumero = documentoNumero;
        this.documentoFecha = documentoFecha;
        this.empresa = empresa;
        this.empleado = empleado;
        this.observacion = observacion;
        this.gremiales = archivo;
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

    public Long getDocumentoNumero() {
        return documentoNumero;
    }

    public void setDocumentoNumero(Long documentoNumero) {
        this.documentoNumero = documentoNumero;
    }

    public Date getDocumentoFecha() {
        return documentoFecha;
    }

    public void setDocumentoFecha(Date documentoFecha) {
        this.documentoFecha = documentoFecha;
    }

    /**
     * CUIT_nombre
     *
     * @return
     */
    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    /**
     * CUIL_nombre
     *
     * @return
     */
    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Gremiales getGremiales() {
        return gremiales;
    }

    public void setGremiales(Gremiales gremiales) {
        this.gremiales = gremiales;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final GremialesDetalle other = (GremialesDetalle) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GremialesDetalle{" + "id=" + id + ", orderIndex=" + orderIndex
                + ", tipoDocumento=" + tipoDocumento + ", subTipoDocumento=" + subTipoDocumento
                + ", documentoNumero=" + documentoNumero + ", documentoFecha=" + documentoFecha
                + ", empresa=" + empresa + ", empleado=" + empleado + ", observacion=" + observacion
                + ", archivo.id=" + (gremiales != null ? gremiales.getId() : null) + '}';
    }

}
