/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.jpa.controller;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import sgd.jpa.model.Usuario;

/**
 *
 * @author FiruzzZ
 */
public class UsuarioJPAController extends AbstractDAO<Usuario, Integer> {

    private EntityManager entityManager;

    public UsuarioJPAController() {
    }

    @Override
    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    @Override
    public List<Usuario> findRange(int first, int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Usuario> findByNamedQueryAndNamedParams(String queryName, Map<String, ? extends Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Usuario find(String nick, String pwd) {
        TypedQuery<Usuario> query
                = getEntityManager().createQuery("SELECT c FROM " + getEntityClass().getSimpleName() + " c"
                + " WHERE UPPER(c.nombre)='" + nick.toUpperCase() + "' AND c.pwd='" + pwd + "'", getEntityClass());
        return query.getSingleResult();
    }

}
