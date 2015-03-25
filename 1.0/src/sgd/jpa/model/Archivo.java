package sgd.jpa.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author FiruzzZ
 */
//@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("ClassMayBeInterface")
public abstract class Archivo {

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
    
    public abstract List<?> getDetalle();
}
