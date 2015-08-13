/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.jpa.controller;

import java.io.Serializable;
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
public class InstitucionJPAController extends AbstractDAO<Institucion, Integer> implements Serializable {

    private static final long serialVersionUID = 11234123412341234L;

    public InstitucionJPAController() {
    }
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    @Override
    public List<Institucion> findRange(int first, int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Institucion> findByNamedQueryAndNamedParams(String queryName, Map<String, ? extends Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Institucion findByNombre(String nombre) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        //que va retornar..
        CriteriaQuery<Institucion> cq = cb.createQuery(getEntityClass());
        //donde se va buscar..
        Root<Institucion> from = cq.from(getEntityClass());
        cq.select(from).
                where(cb.equal(from.get(Institucion_.nombre), nombre));
        return getEntityManager().createQuery(cq).getSingleResult();
    }
}
