package sgd.jpa.model;

import java.io.Serializable;
import java.util.Objects;
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
public class DiscapacidadPrecinto extends Precinto implements Serializable {

    private static final long serialVersionUID = 1123L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String codigo;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Discapacidad discapacidad;

    public DiscapacidadPrecinto() {
    }

    public DiscapacidadPrecinto(String codigo, Discapacidad discapacidad) {
        this.codigo = codigo;
        this.discapacidad = discapacidad;
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

    public Discapacidad getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Discapacidad discapacidad) {
        this.discapacidad = discapacidad;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiscapacidadPrecinto other = (DiscapacidadPrecinto) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
