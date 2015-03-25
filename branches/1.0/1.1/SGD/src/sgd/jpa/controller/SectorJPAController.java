/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.jpa.controller;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import sgd.jpa.model.Sector;

/**
 *
 * @author FiruzzZ
 */
public class SectorJPAController extends AbstractDAO<Sector, Integer> {

    private EntityManager entityManager;

    public SectorJPAController() {
    }

    @Override
    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    @Override
    public List<Sector> findRange(int first, int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Sector> findByNamedQueryAndNamedParams(String queryName, Map<String, ? extends Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
