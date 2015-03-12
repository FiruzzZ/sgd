package sgd.jpa.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author FiruzzZ
 */
//@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Archivo {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected Integer id;
//    @ManyToOne(optional = false)
//    protected Institucion institucion;
//    @ManyToOne(optional = false)
//    protected Sector sector;
//    @Basic(optional = false)
//    @Column(nullable = false)
//    protected Integer codigo;
//    @ManyToOne(optional = false)
//    protected Usuario usuario;
//    protected boolean baja;
//    @Column(length = 200)
//    protected String observacion;
//    @Basic(optional = false)
//    @Column(insertable = false, updatable = false, nullable = false, columnDefinition = "timestamp with time zone NOT NULL DEFAULT now()")
//    @Temporal(TemporalType.TIMESTAMP)
//    protected Date creation;
    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract Institucion getInstitucion();

    public abstract void setInstitucion(Institucion institucion);

    public abstract Sector getSector();

    public abstract void setSector(Sector sector);

    public abstract String getBarcode();

    public abstract void setBarcode(String barcode);

    public abstract Date getCreation();

    public abstract void setCreation(Date creation);

    public abstract void setCodigo(Integer codigo);

    public abstract Integer getCodigo();

    public abstract List<? extends Precinto> getPrecintos();

    public abstract void setRecibo(Recibo recibo);

    public abstract Recibo getRecibo();
}
