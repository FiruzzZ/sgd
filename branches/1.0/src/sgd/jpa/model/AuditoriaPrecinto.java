package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author FiruzzZ
 */
@Entity
public class AuditoriaPrecinto extends Precinto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String codigo;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Auditoria auditoria;

    public AuditoriaPrecinto() {
    }

    public AuditoriaPrecinto(Integer id, String codigo, Auditoria auditoria) {
        this.id = id;
        this.codigo = codigo;
        this.auditoria = auditoria;
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
    public String getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
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
        if (!(object instanceof AuditoriaPrecinto)) {
            return false;
        }
        AuditoriaPrecinto other = (AuditoriaPrecinto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditoriaPrecinto{" + "id=" + id + ", codigo=" + codigo + ", auditoria=" + auditoria + '}';
    }

}
