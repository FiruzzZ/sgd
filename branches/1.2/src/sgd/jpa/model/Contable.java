package sgd.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class Contable extends Archivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false, nullable = false)
    private Institucion institucion;
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false, nullable = false)
    private Sector sector;
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer codigo;
    @JoinColumn(updatable = false, nullable = false)
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
    @OneToMany(mappedBy = "contable", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ContableDetalle> detalle;
    @Basic(optional = false)
    @Column(updatable = false, nullable = false, unique = true)
    private String barcode;
    @OneToMany(mappedBy = "contable", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<ContablePrecinto> precintos;
    @ManyToOne
    private Recibo recibo;

    public Contable() {
        detalle = new ArrayList<>();
        precintos = new ArrayList<>();
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    @Override
    public Integer getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Override
    public Date getCreation() {
        return creation;
    }

    @Override
    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public List<ContableDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<ContableDetalle> detalle) {
        this.detalle = detalle;
    }

    @Override
    public Institucion getInstitucion() {
        return institucion;
    }

    @Override
    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public Sector getSector() {
        return sector;
    }

    @Override
    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String getBarcode() {
        return barcode;
    }

    @Override
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<ContablePrecinto> getPrecintos() {
        return precintos;
    }

    public void setPrecintos(List<ContablePrecinto> precintos) {
        this.precintos = precintos;
    }

    public Integer getNextOrderIndex() {
        if (detalle == null) {
            detalle = new ArrayList<>();
        }
        int orderIndex = 0;
        for (ContableDetalle d : detalle) {
            if (d.getOrderIndex() > orderIndex) {
                orderIndex = d.getOrderIndex();
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
        if (!(object instanceof Contable)) {
            return false;
        }
        Contable other = (Contable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Contable{" + "id=" + id + ", institucion=" + institucion + ", sector=" + sector + ", codigo=" + codigo + ", usuario=" + usuario + ", baja=" + baja + ", observacion=" + observacion + ", creation=" + creation + ", detalle=" + detalle + ", barcode=" + barcode + ", precintos=" + precintos + ", recibo=" + recibo + '}';
    }
}
