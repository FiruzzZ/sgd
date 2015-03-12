/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.config.QueryHints;
import sgd.jpa.model.*;

/**
 *
 * @author FiruzzZ
 */
public class TipoDocumentoJPAController extends AbstractDAO<TipoDocumento, Integer> {

    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    @Override
    public void create(TipoDocumento entity) {
        if (entity.getSubTipoDocumentos() == null) {
            entity.setSubTipoDocumentos(new ArrayList<SubTipoDocumento>(0));
        }
        super.create(entity);
    }

    @Override
    public List<TipoDocumento> findRange(int first, int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TipoDocumento> findByNamedQueryAndNamedParams(String queryName, Map<String, ? extends Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TipoDocumento findByNombre(Sector sector, String nombre) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        //que va retornar..
        CriteriaQuery<TipoDocumento> cq = cb.createQuery(getEntityClass());
        //donde se va buscar..
        Root<TipoDocumento> from = cq.from(getEntityClass());
        cq.select(from).
                where(cb.and(cb.equal(from.get(TipoDocumento_.nombre), nombre),
                cb.equal(from.get(TipoDocumento_.sector), sector)));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public List<TipoDocumento> findBy(Sector sector) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TipoDocumento> cq = cb.createQuery(getEntityClass());
        Root<TipoDocumento> from = cq.from(getEntityClass());
        cq.select(from).
                where(cb.equal(from.get(TipoDocumento_.sector), sector));
        return getEntityManager().createQuery(cq).setHint(QueryHints.REFRESH, true).getResultList();
    }

    public List<TipoDocumento> findUsoTipoDocumento(String jpqlQuery, TipoDocumento sector) {
        return getEntityManager().createQuery(jpqlQuery, getEntityClass()).getResultList();
    }
}
