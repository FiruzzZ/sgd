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
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"institucion_id", "sector_id", "codigo"})
})
public class Afiliacion extends Archivo implements Serializable {

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
    @OneToMany(mappedBy = "afiliacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy(value = "orderIndex")
    private List<AfiliacionDetalle> detalle;
    @Basic(optional = false)
    @Column(updatable = false, nullable = false, length = 50, unique = true)
    private String barcode;
    @OneToMany(mappedBy = "afiliacion", cascade = {CascadeType.ALL})
    private List<AfiliacionPrecinto> precintos;
    @ManyToOne
    private Recibo recibo;

    public Afiliacion() {
        detalle = new ArrayList<>();
        precintos = new ArrayList<>();
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

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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

    public List<AfiliacionDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<AfiliacionDetalle> afiliacionDetalle) {
        this.detalle = afiliacionDetalle;
    }

    @Override
    public String getBarcode() {
        return barcode;
    }

    @Override
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public List<AfiliacionPrecinto> getPrecintos() {
        return precintos;
    }

    public void setPrecintos(List<AfiliacionPrecinto> precintos) {
        this.precintos = precintos;
    }

    @Override
    public Recibo getRecibo() {
        return recibo;
    }

    @Override
    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Afiliacion other = (Afiliacion) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Afiliacion{" + "id=" + id + ", institucion=" + institucion + ", sector=" + sector + ", codigo=" + codigo + ", usuario=" + usuario + ", baja=" + baja + ", observacion=" + observacion + ", creation=" + creation + ", detalle=" + detalle + ", barcode=" + barcode + ", precintos=" + precintos + ", recibo=" + recibo + '}';
    }

}
