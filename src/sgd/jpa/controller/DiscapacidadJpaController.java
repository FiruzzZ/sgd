package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import sgd.jpa.model.Discapacidad;
import sgd.jpa.model.DiscapacidadDetalle;
import sgd.jpa.model.DiscapacidadPrecinto;

/**
 *
 * @author FiruzzZ
 */
public class DiscapacidadJpaController extends SGDJpaImp<Discapacidad, Integer> {

    public DiscapacidadJpaController() {
    }

    public List<DiscapacidadDetalle> findDetalleByJPQL(String jpql) {
        return getEntityManager().createQuery(jpql, DiscapacidadDetalle.class).getResultList();
    }

    public void removePrecintos(Discapacidad entity) {
        getEntityManager().getTransaction().begin();
        for (DiscapacidadPrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<DiscapacidadPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }

}
