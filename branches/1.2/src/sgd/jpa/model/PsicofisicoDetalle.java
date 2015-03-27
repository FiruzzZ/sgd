package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class PsicofisicoDetalle implements Serializable {

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
    private Long numeroDocumento;
    @Column(length = 50)
    private String apellido;
    @Column(length = 50)
    private String nombre;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    @Column
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Psicofisico psicofisico;
    @Column(nullable = false)
    private Integer numeroCarpeta;

    public PsicofisicoDetalle() {
    }

    public PsicofisicoDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Long numeroDocumento, String apellido, String nombre, Date documentoFecha, String observacion, Psicofisico psicofisico, Integer numeroCarpeta) {
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.apellido = apellido;
        this.nombre = nombre;
        this.documentoFecha = documentoFecha;
        this.observacion = observacion;
        this.psicofisico = psicofisico;
        this.numeroCarpeta = numeroCarpeta;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Integer getNumeroCarpeta() {
        return numeroCarpeta;
    }

    public void setNumeroCarpeta(Integer numeroCarpeta) {
        this.numeroCarpeta = numeroCarpeta;
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

    public Psicofisico getPsicofisico() {
        return psicofisico;
    }

    public void setPsicofisico(Psicofisico psicofisico) {
        this.psicofisico = psicofisico;
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PsicofisicoDetalle other = (PsicofisicoDetalle) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "PsicofisicoDetalle{" + "id=" + id + ", orderIndex=" + orderIndex
                + ", tipoDocumento=" + tipoDocumento + ", subTipoDocumento=" + subTipoDocumento
                + ", numeroDocumento=" + numeroDocumento + ", apellido=" + apellido
                + ", numeroCarpeta=" + numeroCarpeta
                + ", nombre=" + nombre + ", documentoFecha=" + documentoFecha
                + ", observacion=" + observacion
                + ", psicofisico=" + psicofisico.getId() + '}';
    }

}
