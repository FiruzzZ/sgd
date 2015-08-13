package sgd.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author FiruzzZ
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"barcode"}),
    @UniqueConstraint(columnNames = {"institucion_id", "sector_id", "codigo"})
})
public class Discapacidad extends Archivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false)
    private boolean baja;
    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String barcode;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer codigo;
    @Basic(optional = false)
    @Column(nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;
    @Column(length = 200)
    private String observacion;
    @ManyToOne(optional = false)
    private Institucion institucion;
    @ManyToOne(optional = false)
    private Sector sector;
    @ManyToOne
    private Recibo recibo;
    @ManyToOne(optional = false)
    private Usuario usuario;
    @OrderBy(value = "orderIndex")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discapacidad")
    private List<DiscapacidadDetalle> detalle;
    @OneToMany(mappedBy = "discapacidad", cascade = {CascadeType.ALL})
    private List<DiscapacidadPrecinto> precintos;

    public Discapacidad() {
        detalle = new ArrayList<>();
        precintos = new ArrayList<>();
    }

    public Discapacidad(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DiscapacidadDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DiscapacidadDetalle> detalle) {
        this.detalle = detalle;
    }

    public void setPrecintos(List<DiscapacidadPrecinto> precintos) {
        this.precintos = precintos;
    }

    public List<DiscapacidadPrecinto> getPrecintos() {
        return precintos;
    }

    public Integer getNextOrderIndex() {
        if (detalle == null) {
            detalle = new ArrayList<>();
        }
        int orderIndex = 0;
        for (DiscapacidadDetalle o : detalle) {
            if (o.getOrderIndex() > orderIndex) {
                orderIndex = o.getOrderIndex();
            }
        }
        return orderIndex + 1;
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
        if (!(object instanceof Discapacidad)) {
            return false;
        }
        Discapacidad other = (Discapacidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sgd.jpa.model.Discapacidad[ id=" + id + " ]";
    }

}
