package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.Facturacion;
import sgd.jpa.model.FacturacionDetalle;
import sgd.jpa.model.FacturacionDetalle_;
import sgd.jpa.model.FacturacionPrecinto;
import sgd.jpa.model.Facturacion_;
import sgd.jpa.model.Institucion;
import sgd.jpa.model.SubTipoDocumento;
import sgd.jpa.model.TipoDocumento;

/**
 *
 * @author FiruzzZ
 */
public class FacturacionJpaController extends SGDJpaImp<Facturacion, Integer> {

    public FacturacionJpaController() {
    }

    @SuppressWarnings("unchecked")
    public List<FacturacionDetalle> findFacturacionDetalleByNativeQuery(String jpql) {
        return getEntityManager().createNativeQuery(jpql, FacturacionDetalle.class).getResultList();
    }

    public List<FacturacionDetalle> findFacturacionDetalleByJPQL(String jpql) {
        return getEntityManager().createQuery(jpql, FacturacionDetalle.class).getResultList();
    }

    public void removePrecintos(Facturacion entity) {
        getEntityManager().getTransaction().begin();
        for (FacturacionPrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<FacturacionPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }

    public List<String> findPrestadores() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<FacturacionDetalle> root = query.from(FacturacionDetalle.class);
        query.select(cb.upper(root.get(FacturacionDetalle_.prestador))).distinct(true);
        query.orderBy(cb.asc(cb.upper(root.get(FacturacionDetalle_.prestador))));
        return getEntityManager().createQuery(query).getResultList();
    }

    public List<FacturacionDetalle> findDetalle(TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, String expediente, String prestador, Date documentoFecha) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FacturacionDetalle> query = cb.createQuery(FacturacionDetalle.class);
        Root<FacturacionDetalle> root = query.from(FacturacionDetalle.class);
        query.where(cb.and(
                cb.equal(root.get(FacturacionDetalle_.tipoDocumento), tipoDocumento),
                cb.equal(root.get(FacturacionDetalle_.subTipoDocumento), subTipoDocumento),
                cb.equal(root.get(FacturacionDetalle_.expediente), expediente),
                cb.equal(root.get(FacturacionDetalle_.prestador), prestador),
                cb.equal(root.get(FacturacionDetalle_.documentoFecha), documentoFecha))
        );
        return getEntityManager().createQuery(query).getResultList();
    }
}
