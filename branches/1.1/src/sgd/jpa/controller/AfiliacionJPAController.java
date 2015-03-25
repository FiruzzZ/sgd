package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import sgd.jpa.model.Afiliacion;
import sgd.jpa.model.AfiliacionDetalle;
import sgd.jpa.model.AfiliacionPrecinto;

/**
 *
 * @author FiruzzZ
 */
public class AfiliacionJPAController extends SGDJpaImp<Afiliacion, Integer> {

    public AfiliacionJPAController() {
    }

    public List<AfiliacionDetalle> findDetalleByJPQL(String jpql) {
        return getEntityManager().createQuery(jpql, AfiliacionDetalle.class).getResultList();
    }

    public void removePrecintos(Afiliacion entity) {
        getEntityManager().getTransaction().begin();
        for (AfiliacionPrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<AfiliacionPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }

}
