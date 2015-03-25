package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class AfiliacionDetalle implements Serializable {

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
    private Long numeroAfiliado;
    @Column(nullable = false)
    private Integer familiarNumero;
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
    private Afiliacion afiliacion;

    public AfiliacionDetalle() {
    }

    public AfiliacionDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Long numeroAfiliado, Integer familiarNumero, String apellido, String nombre, Date documentoFecha, String observacion, Afiliacion afiliacion) {
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.numeroAfiliado = numeroAfiliado;
        this.familiarNumero = familiarNumero;
        this.apellido = apellido;
        this.nombre = nombre;
        this.documentoFecha = documentoFecha;
        this.observacion = observacion;
        this.afiliacion = afiliacion;
    }

    public Afiliacion getAfiliacion() {
        return afiliacion;
    }

    public void setAfiliacion(Afiliacion afiliacion) {
        this.afiliacion = afiliacion;
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

    public Integer getFamiliarNumero() {
        return familiarNumero;
    }

    public void setFamiliarNumero(Integer familiarNumero) {
        this.familiarNumero = familiarNumero;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AfiliacionDetalle other = (AfiliacionDetalle) obj;
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
        return "AfiliacionDetalle{" + "id=" + id + ", orderIndex=" + orderIndex 
                + ", tipoDocumento=" + tipoDocumento + ", subTipoDocumento=" + subTipoDocumento 
                + ", numeroAfiliado=" + numeroAfiliado + ", familiarNumero=" + familiarNumero
                + ", apellido=" + apellido + ", nombre=" + nombre + ", documentacionFecha=" + documentoFecha + ", observacion=" + observacion + ", afiliacion=" + afiliacion.getId() + '}';
    }
}
