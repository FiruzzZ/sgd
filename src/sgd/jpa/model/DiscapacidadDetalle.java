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
public class DiscapacidadDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    private Long documentoNumero;
    @Column(name = "periodo_year")
    private Integer periodoYear;
    @Column(length = 50)
    private String apellido;
    @Column(length = 50)
    private String nombre;
    @Column(length = 255)
    private String observacion;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer orderIndex;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @ManyToOne
    private SubTipoDocumento subTipoDocumento;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Discapacidad discapacidad;

    public DiscapacidadDetalle() {
    }

    public DiscapacidadDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Discapacidad discapacidad, Long documentoNumero, Date documentofecha, Integer periodoYear, String apellido, String nombre, String observacion) {
        this.orderIndex = orderIndex;
        this.documentoFecha = documentofecha;
        this.documentoNumero = documentoNumero;
        this.periodoYear = periodoYear;
        this.apellido = apellido;
        this.nombre = nombre;
        this.observacion = observacion;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.discapacidad = discapacidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDocumentoFecha() {
        return documentoFecha;
    }

    public void setDocumentoFecha(Date documentoFecha) {
        this.documentoFecha = documentoFecha;
    }

    public Long getDocumentoNumero() {
        return documentoNumero;
    }

    public void setDocumentoNumero(Long documentoNumero) {
        this.documentoNumero = documentoNumero;
    }

    public Integer getPeriodoYear() {
        return periodoYear;
    }

    public void setPeriodoYear(Integer periodoYear) {
        this.periodoYear = periodoYear;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public void setOrderIndex(int orderIndex) {
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

    public Discapacidad getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Discapacidad discapacidad) {
        this.discapacidad = discapacidad;
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
        if (!(object instanceof DiscapacidadDetalle)) {
            return false;
        }
        DiscapacidadDetalle other = (DiscapacidadDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DiscapacidadDetalle{" + "id=" + id + ", documentofecha=" + documentoFecha 
                + ", documentoNumero=" + documentoNumero + ", periodoYear=" + periodoYear 
                + ", apellido=" + apellido + ", nombre=" + nombre + ", observacion=" + observacion 
                + ", orderIndex=" + orderIndex + ", tipoDocumento=" + tipoDocumento 
                + ", subTipoDocumento=" + subTipoDocumento + '}';
    }
}
