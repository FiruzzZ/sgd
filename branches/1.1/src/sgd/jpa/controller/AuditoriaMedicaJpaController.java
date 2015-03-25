package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import sgd.jpa.model.AuditoriaMedica;
import sgd.jpa.model.AuditoriaMedicaDetalle;
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

}
