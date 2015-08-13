package sgd.jpa.controller;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.Sector;
import sgd.jpa.model.Solicitud;
import sgd.jpa.model.Solicitud_;

/**
 *
 * @author FiruzzZ
 */
public class SolicitudJpaController extends AbstractDAO<Solicitud, Integer> {

    private EntityManager entityManager;

    public SolicitudJpaController() {
    }

    @Override
    public EntityManager getEntityManager() {
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
        cq.where(cb.equal(root.get(Solicitud_.sector), sector));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
