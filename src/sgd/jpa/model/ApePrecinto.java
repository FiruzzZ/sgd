package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
public class ApePrecinto extends Precinto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String codigo;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Ape ape;

    public ApePrecinto() {
        //orm
    }

    public ApePrecinto(String codigo, Ape ape) {
        this.codigo = codigo;
        this.ape = ape;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Ape getApe() {
        return ape;
    }

    public void setApe(Ape ape) {
        this.ape = ape;
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        if (!(object instanceof ApePrecinto)) {
            return false;
        }
        ApePrecinto other = (ApePrecinto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ApePrecinto{" + "id=" + id + ", codigo=" + codigo + ", ape.id=" + ape.getId() + '}';
    }

}
