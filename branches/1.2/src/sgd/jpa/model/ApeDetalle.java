package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class ApeDetalle implements Serializable {

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
    @Temporal(TemporalType.DATE)
    private Date documentoFecha;
    @Column(length = 50)
    private String apellido;
    @Column(length = 50)
    private String nombre;
    @Column
    private String observacion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Ape ape;

    public ApeDetalle() {
    }

    public ApeDetalle(Integer orderIndex, TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, Long numeroAfiliado, Date documentoFecha, String observacion, Ape ape, String apellido, String nombre) {
        this.orderIndex = orderIndex;
        this.tipoDocumento = tipoDocumento;
        this.subTipoDocumento = subTipoDocumento;
        this.numeroAfiliado = numeroAfiliado;
        this.documentoFecha = documentoFecha;
        this.observacion = observacion;
        this.ape = ape;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ape getApe() {
        return ape;
    }

    public void setApe(Ape ape) {
        this.ape = ape;
    }

    public Date getDocumentoFecha() {
        return documentoFecha;
    }

    public void setDocumentoFecha(Date documentoFecha) {
        this.documentoFecha = documentoFecha;
    }

    public Long getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(Long numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApeDetalle)) {
            return false;
        }
        ApeDetalle other = (ApeDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ApeDetalle{" + "id=" + id + ", orderIndex=" + orderIndex + ", tipoDocumento=" + tipoDocumento
                + ", subTipoDocumento=" + subTipoDocumento + ", numeroAfiliado=" + numeroAfiliado + ", documentoFecha="
                + documentoFecha
                + ", apellido=" + apellido + ", nombre=" + nombre + ", observacion=" + observacion
                + ", ape=" + (ape != null ? ape.getId() : null) + '}';
    }

}
