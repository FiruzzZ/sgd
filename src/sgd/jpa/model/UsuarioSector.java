/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * Es la combinación que da permiso a los usuarios a un {@link Sector} para cierta {@link Institucion}
 * @author FiruzzZ
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "institucion_id", "sector_id"})
})
@Cacheable(false)
public class UsuarioSector implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Institucion institucion;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Sector sector;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false, columnDefinition = "timestamp with time zone NOT NULL DEFAULT now()")
    private Date creacion;
    /**
     * 1=lectura, 2 =todo
     */
    @Column(nullable = false)
    private Integer permiso;
    @Column(nullable = false)
    private boolean administraSector;
    @Column(nullable = false)
    private boolean administraUsuarios;

    public UsuarioSector() {
    }

    public UsuarioSector(Integer id, Usuario usuario, Institucion institucion, Sector sector, Integer permiso, boolean administraSector, boolean administraUsuarios) {
        this.id = id;
        this.usuario = usuario;
        this.institucion = institucion;
        this.sector = sector;
        this.permiso = permiso;
        this.administraSector = administraSector;
        this.administraUsuarios = administraUsuarios;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getCreacion() {
        return creacion;
    }

    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }

    /**
     * 1 = lectura, 2 = todo
     *
     * @return
     */
    public Integer getPermiso() {
        return permiso;
    }

    public void setPermiso(Integer permiso) {
        this.permiso = permiso;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public boolean getAdministraSector() {
        return administraSector;
    }

    public void setAdministraSector(boolean administraSector) {
        this.administraSector = administraSector;
    }

    public boolean getAdministraUsuarios() {
        return administraUsuarios;
    }

    public void setAdministraUsuarios(boolean administraUsuarios) {
        this.administraUsuarios = administraUsuarios;
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
        if (!(object instanceof UsuarioSector)) {
            return false;
        }
        UsuarioSector other = (UsuarioSector) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsuarioSector{" + "id=" + id + ", usuario=" + usuario + ", institucion=" + institucion + ", sector=" + sector + ", creacion=" + creacion + ", permiso=" + permiso + ", administraSector=" + administraSector + ", administraUsuarios=" + administraUsuarios + '}';
    }
}
