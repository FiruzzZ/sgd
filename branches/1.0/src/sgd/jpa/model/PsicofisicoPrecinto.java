package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
public class PsicofisicoPrecinto extends Precinto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String codigo;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Psicofisico psicofisico;

    public PsicofisicoPrecinto() {
    }

    public PsicofisicoPrecinto(String codigo, Psicofisico psicofisico) {
        this.codigo = codigo;
        this.psicofisico = psicofisico;
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

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Psicofisico getPsicofisico() {
        return psicofisico;
    }

    public void setPsicofisico(Psicofisico psicofisico) {
        this.psicofisico = psicofisico;
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
        if (!(object instanceof PsicofisicoPrecinto)) {
            return false;
        }
        PsicofisicoPrecinto other = (PsicofisicoPrecinto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sgd.jpa.model.PsicofisicoPrecinto[ id=" + id + " ]";
    }
}
