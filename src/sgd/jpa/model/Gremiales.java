package sgd.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @UniqueConstraint(columnNames = {"institucion_id", "sector_id", "codigo"})
})
public class Gremiales extends Archivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    private Institucion institucion;
    @ManyToOne(optional = false)
    private Sector sector;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer codigo;
    @ManyToOne(optional = false)
    private Usuario usuario;
    private boolean baja;
    @Column(length = 200)
    private String observacion;
    @Basic(optional = false)
    @Column(insertable = false, updatable = false, nullable = false, columnDefinition = "timestamp with time zone NOT NULL DEFAULT now()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;
    @OrderBy(value = "orderIndex")
    @OneToMany(mappedBy = "gremiales", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GremialesDetalle> detalle;
    @Basic(optional = false)
    @Column(updatable = false, nullable = false, length = 50, unique = true)
    private String barcode;
    @OneToMany(mappedBy = "gremiales", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<GremialesPrecinto> precintos;
    @ManyToOne
    private Recibo recibo;

    public Gremiales() {
        detalle = new ArrayList<>();
        precintos = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public List<GremialesDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<GremialesDetalle> detalle) {
        this.detalle = detalle;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<GremialesPrecinto> getPrecintos() {
        return precintos;
    }

    public void setPrecintos(List<GremialesPrecinto> precintos) {
        this.precintos = precintos;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }
    public Integer getNextOrderIndex() {
        if (detalle == null) {
            detalle = new ArrayList<>();
        }
        int orderIndex = 0;
        for (GremialesDetalle o : detalle) {
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
        if (!(object instanceof Gremiales)) {
            return false;
        }
        Gremiales other = (Gremiales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sgd.jpa.model.Gremiales[ id=" + id + " ]";
    }

}
