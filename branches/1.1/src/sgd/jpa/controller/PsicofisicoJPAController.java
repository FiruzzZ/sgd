package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import sgd.jpa.model.*;

/**
 *
 * @author FiruzzZ
 */
public class PsicofisicoJPAController extends SGDJpaImp<Psicofisico, Integer> {

    public List<PsicofisicoDetalle> findDetalleByJPQL(String jpql) {
        return getEntityManager().createQuery(jpql, PsicofisicoDetalle.class).getResultList();
    }

    public void removePrecintos(Psicofisico entity) {
        getEntityManager().getTransaction().begin();
        for (PsicofisicoPrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<PsicofisicoPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }
}
