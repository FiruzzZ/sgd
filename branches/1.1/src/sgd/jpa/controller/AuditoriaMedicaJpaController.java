package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.AuditoriaMedica;
import sgd.jpa.model.AuditoriaMedicaDetalle;
import sgd.jpa.model.AuditoriaMedicaDetalle_;
import sgd.jpa.model.AuditoriaMedicaPrecinto;

/**
 *
 * @author FiruzzZ
 */
public class AuditoriaMedicaJpaController extends SGDJpaImp<AuditoriaMedica, Integer> {

    public AuditoriaMedicaJpaController() {
    }

    public List<AuditoriaMedicaDetalle> findDetalleByJPQL(String jpql) {
        return getEntityManager().createQuery(jpql, AuditoriaMedicaDetalle.class).getResultList();
    }

    public void removePrecintos(AuditoriaMedica entity) {
        getEntityManager().getTransaction().begin();
        for (AuditoriaMedicaPrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<AuditoriaMedicaPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }
    
    
      public List<String> findDelegaciones() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<AuditoriaMedicaDetalle> root = query.from(AuditoriaMedicaDetalle.class);
        query.select(cb.upper(root.get(AuditoriaMedicaDetalle_.delegacion))).distinct(true);
        query.orderBy(cb.asc(cb.upper(root.get(AuditoriaMedicaDetalle_.delegacion))));
        return getEntityManager().createQuery(query).getResultList();
    }

}
