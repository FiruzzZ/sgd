package sgd.jpa.controller;

import java.util.List;
import org.eclipse.persistence.config.QueryHints;
import sgd.jpa.model.Contable;
import sgd.jpa.model.ContableDetalle;
import sgd.jpa.model.ContablePrecinto;

/**
 *
 * @author FiruzzZ
 */
public class ContableJPAController extends SGDJpaImp<Contable, Integer> {

    public ContableJPAController() {
    }

    @SuppressWarnings("unchecked")
    public List<ContableDetalle> findContableDetalleByNativeSQL(String jpql) {
        return getEntityManager().createNativeQuery(jpql).
                setHint(QueryHints.REFRESH, Boolean.TRUE).
                getResultList();
    }

    public List<ContableDetalle> findDetalleByQuery(String jpql) {
        return getEntityManager().createQuery(jpql).getResultList();
    }

    public void removePrecintos(Contable entity) {
        getEntityManager().getTransaction().begin();
        for (ContablePrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
        }
        getEntityManager().refresh(entity);
        getEntityManager().getTransaction().commit();
        entity.getPrecintos().clear();
    }

}
