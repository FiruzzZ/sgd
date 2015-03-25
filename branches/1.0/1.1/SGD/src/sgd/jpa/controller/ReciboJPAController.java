package sgd.jpa.controller;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.*;

/**
 *
 * @author Administrador
 */
public class ReciboJPAController extends AbstractDAO<Recibo, Integer> {

    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    @Override
    public List<Recibo> findRange(int first, int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Recibo> findByNamedQueryAndNamedParams(String queryName, Map<String, ? extends Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int countBySector(Sector sector) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root root = cq.from(getEntityClass());
        cq.select(cb.count(root));
        cq.where(cb.equal(root.get(Recibo_.sector), sector));
        Query q = getEntityManager().createQuery(cq);
        return ((Number) q.getSingleResult()).intValue();
    }
}
