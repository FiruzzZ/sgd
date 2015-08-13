package sgd.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Representa los códigos de los precintos que protegen un Archivo cuando este
 * es cerrado para su envio a un algún lado.
 *
 * @author FiruzzZ
 */
@Entity
public class AfiliacionPrecinto extends Precinto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String codigo;
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Afiliacion afiliacion;

    public AfiliacionPrecinto() {
        //ORM
    }

    public AfiliacionPrecinto(String codigo, Afiliacion afiliacion) {
        this.codigo = codigo;
        this.afiliacion = afiliacion;
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

    public Afiliacion getAfiliacion() {
        return afiliacion;
    }

    public void setAfiliacion(Afiliacion afiliacion) {
        this.afiliacion = afiliacion;
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
        if (!(object instanceof AfiliacionPrecinto)) {
            return false;
        }
        AfiliacionPrecinto other = (AfiliacionPrecinto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AfiliacionPrecinto{" + "id=" + id + ", codigo=" + codigo + '}';
    }
}
