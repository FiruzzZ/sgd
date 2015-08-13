package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
public class ContablePrecinto extends Precinto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String codigo;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Contable contable;

    public ContablePrecinto() {
    }

    public ContablePrecinto(String codigo, Contable contable) {
        this.codigo = codigo;
        this.contable = contable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Contable getContable() {
        return contable;
    }

    public void setContable(Contable contable) {
        this.contable = contable;
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
        if (!(object instanceof ContablePrecinto)) {
            return false;
        }
        ContablePrecinto other = (ContablePrecinto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sgd.jpa.model.ContablePrecinto[ id=" + id + " ]";
    }
}
