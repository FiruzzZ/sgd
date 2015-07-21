package sgd.jpa.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"institucion_id", "sector_id", "codigo"})
})
public class Psicofisico extends Archivo implements Serializable {

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
    @Column(nullable = false)
    private boolean baja;
    @Column(length = 200)
    private String observacion;
    @Basic(optional = false)
    @Column(insertable = false, updatable = false, nullable = false, columnDefinition = "timestamp with time zone NOT NULL DEFAULT now()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;
    @OrderBy(value = "orderIndex")
    @OneToMany(mappedBy = "psicofisico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PsicofisicoDetalle> detalle;
    @Basic(optional = false)
    @Column(updatable = false, nullable = false, length = 50, unique = true)
    private String barcode;
    @OneToMany(mappedBy = "psicofisico", cascade = {CascadeType.ALL})
    private List<PsicofisicoPrecinto> precintos;
    @ManyToOne
    private Recibo recibo;
    @Version
    private java.sql.Timestamp version;

    public Psicofisico() {
        detalle = new ArrayList<>();
        precintos = new ArrayList<>();
    }

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
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

    public List<PsicofisicoDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<PsicofisicoDetalle> detalle) {
        this.detalle = detalle;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<PsicofisicoPrecinto> getPrecintos() {
        return precintos;
    }

    public void setPrecintos(List<PsicofisicoPrecinto> precintos) {
        this.precintos = precintos;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getNextOrderIndex() {
        if (detalle == null) {
            detalle = new ArrayList<>();
        }
        int orderIndex = 0;
        for (PsicofisicoDetalle o : detalle) {
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
        if (!(object instanceof Psicofisico)) {
            return false;
        }
        Psicofisico other = (Psicofisico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Psicofisico{" + "id=" + id + ", institucion=" + institucion + ", sector=" + sector + ", codigo=" + codigo + ", usuario=" + usuario + ", baja=" + baja + ", observacion=" + observacion + ", creation=" + creation + ", detalle=" + detalle.size() + ", barcode=" + barcode + ", precintos=" + precintos.size() + ", recibo=" + recibo + '}';
    }
}
