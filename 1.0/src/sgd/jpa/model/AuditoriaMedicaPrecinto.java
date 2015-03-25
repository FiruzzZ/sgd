package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Representa los códigos de los precintos que protegen un Archivo cuando este
 * es cerrado para su envio a un algún lado.
 *
 * @author nsteinhilber
 */
@Entity
public class AuditoriaMedicaPrecinto extends Precinto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String codigo;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private AuditoriaMedica auditoriaMedica;

    public AuditoriaMedicaPrecinto() {
        //ORM
    }

    public AuditoriaMedicaPrecinto(String codigo, AuditoriaMedica auditoriaMedica) {
        this.codigo = codigo;
        this.auditoriaMedica = auditoriaMedica;
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

    public AuditoriaMedica getAuditoriaMedica() {
        return auditoriaMedica;
    }

    public void setAuditoriaMedica(AuditoriaMedica auditoriaMedica) {
        this.auditoriaMedica = auditoriaMedica;
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
        if (!(object instanceof AuditoriaMedicaPrecinto)) {
            return false;
        }
        AuditoriaMedicaPrecinto other = (AuditoriaMedicaPrecinto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditoriaMedicaPrecinto{" + "id=" + id + ", codigo=" + codigo + '}';
    }
}
