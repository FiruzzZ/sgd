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
public class Auditoria extends Archivo implements Serializable {

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
    @OneToMany(mappedBy = "auditoria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AuditoriaDetalle> detalle;
    @Basic(optional = false)
    @Column(updatable = false, nullable = false, length = 50, unique = true)
    private String barcode;
    @OneToMany(mappedBy = "auditoria", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<AuditoriaPrecinto> precintos;
    @ManyToOne
    private Recibo recibo;

    public Auditoria() {
        detalle = new ArrayList<>();
        precintos = new ArrayList<>();
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

    @Override
    public Sector getSector() {
        return sector;
    }

    @Override
    public void setSector(Sector sector) {
        this.sector = sector;
    }

    @Override
    public Integer getCodigo() {
        return codigo;
    }

    @Override
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

    @Override
    public Date getCreation() {
        return creation;
    }

    @Override
    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public List<AuditoriaDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<AuditoriaDetalle> detalle) {
        this.detalle = detalle;
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
    public List<AuditoriaPrecinto> getPrecintos() {
        return precintos;
    }

    public void setPrecintos(List<AuditoriaPrecinto> precintos) {
        this.precintos = precintos;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
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
        if (!(object instanceof Auditoria)) {
            return false;
        }
        Auditoria other = (Auditoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass() + "{" + "id=" + id + ", institucion=" + institucion + ", sector=" + sector + ", codigo=" + codigo + ", usuario=" + usuario + ", baja=" + baja + ", observacion=" + observacion + ", creation=" + creation + ", detalle=" + detalle + ", barcode=" + barcode + ", precintos=" + precintos + ", recibo=" + recibo + '}';
    }

}
