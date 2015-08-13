package sgd.jpa.controller;

import java.util.List;
import sgd.jpa.model.*;

/**
 *
 * @author FiruzzZ
 */
public class ApeJPAController extends SGDJpaImp<Ape, Integer> {

    public ApeJPAController() {
    }

    public void removePrecintos(Ape entity) {
        getEntityManager().getTransaction().begin();
        for (ApePrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
        }
        getEntityManager().refresh(entity);
        getEntityManager().getTransaction().commit();
    }

    public List<ApeDetalle> findDetalleByQuery(String query) {
        return getEntityManager().createQuery(query).getResultList();
    }
}
