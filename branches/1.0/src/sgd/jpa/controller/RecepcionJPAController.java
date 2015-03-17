package sgd.jpa.controller;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.Recepcion;
import sgd.jpa.model.Recepcion_;
import sgd.jpa.model.Sector;

/**
 *
 * @author Administrador
 */
public class RecepcionJPAController extends AbstractDAO<Recepcion, Integer> {

    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    public int countBySector(Sector sector) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root root = cq.from(getEntityClass());
        cq.select(cb.count(root));
        cq.where(cb.equal(root.get(Recepcion_.sector), sector));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
