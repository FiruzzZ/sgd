package sgd.jpa.controller;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.*;

/**
 *
 * @author FiruzzZ
 */
public class SubTipoDocumentoJPAController extends AbstractDAO<SubTipoDocumento, Integer> {

    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    @Override
    public List<SubTipoDocumento> findRange(int first, int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<SubTipoDocumento> findByNamedQueryAndNamedParams(String queryName, Map<String, ? extends Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SubTipoDocumento findByNombre(TipoDocumento tipoDocumento, String nombre) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SubTipoDocumento> cq = cb.createQuery(getEntityClass());
        Root<SubTipoDocumento> from = cq.from(getEntityClass());
        cq.select(from).
                where(cb.and(cb.equal(from.get(SubTipoDocumento_.nombre), nombre),
                cb.equal(from.get(SubTipoDocumento_.tipoDocumento), tipoDocumento)));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public List<SubTipoDocumento> findByTipoDocumento(TipoDocumento tipoDocumento) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SubTipoDocumento> cq = cb.createQuery(getEntityClass());
        Root<SubTipoDocumento> from = cq.from(getEntityClass());
        cq.select(from).
                where(cb.equal(from.get(SubTipoDocumento_.tipoDocumento), tipoDocumento));
        return getEntityManager().createQuery(cq).getResultList();
    }
}
