/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.jpa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import sgd.jpa.model.*;

/**
 *
 * @author FiruzzZ
 */
public class UsuarioSectorJPAController extends AbstractDAO<UsuarioSector, Integer> {

    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    @Override
    public List<UsuarioSector> findRange(int first, int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UsuarioSector> findByNamedQueryAndNamedParams(String queryName, Map<String, ? extends Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UsuarioSector find(Institucion i, Sector s) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UsuarioSector> query = builder.createQuery(getEntityClass());
        Root<UsuarioSector> root = query.from(getEntityClass());
        query.select(root);
        query.where(builder.and(builder.equal(root.get(UsuarioSector_.sector), s),
                builder.equal(root.get(UsuarioSector_.institucion), i)));
        return getEntityManager().createQuery(query).getSingleResult();
    }

    /**
     * Recupera todas las {@link Institucion} que tiene asignado el Usuario en diferentes sectores.
     *
     * @param u
     * @return
     */
    public List<Institucion> findInstituciones(Usuario u) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Institucion> query = builder.createQuery(Institucion.class);
        Root<UsuarioSector> root = query.from(getEntityClass());
        query.select(root.get(UsuarioSector_.institucion)).
                where(builder.equal(root.get(UsuarioSector_.usuario), u)).
                groupBy(root.get(UsuarioSector_.institucion));

        //hay que hacer toda esta verdura porque no funciona groupBy de Institucion
        List<Institucion> l = getEntityManager().createQuery(query).getResultList();
        List<Institucion> l2 = new ArrayList<Institucion>(l.size());
        for (Institucion institucion : l) {
            if (!l2.contains(institucion)) {
                l2.add(institucion);
            }
        }
        return l2;
    }

    /**
     * Retrieve all {@link Sector} accessables for an {@link Usuario} AND an {@link Institucion}.
     *
     * @param u
     * @param i
     * @return
     */
    public List<Sector> find(Usuario u, Institucion i) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sector> query = builder.createQuery(Sector.class);
        Root<UsuarioSector> root = query.from(getEntityClass());
        query.select(root.get(UsuarioSector_.sector));
        query.where(builder.and(builder.equal(root.get(UsuarioSector_.usuario), u),
                builder.equal(root.get(UsuarioSector_.institucion), i)));
        return getEntityManager().createQuery(query).getResultList();
    }

    public int findByInstitucion(Institucion institucion) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Institucion> query = builder.createQuery(Institucion.class);
        Root<UsuarioSector> root = query.from(getEntityClass());
        query.select(root.get(UsuarioSector_.institucion));
        query.where(builder.equal(root.get(UsuarioSector_.institucion), institucion));
        return getEntityManager().createQuery(query).getResultList().size();
    }

    public List<UsuarioSector> findByUsuario(Usuario u) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UsuarioSector> query = builder.createQuery(getEntityClass());
        Root<UsuarioSector> root = query.from(getEntityClass());
        query.select(root);
        query.where(builder.equal(root.get(UsuarioSector_.usuario), u));
//        cq.groupBy(from.get(UsuarioSector_.sector));
        return getEntityManager().createQuery(query).getResultList();
    }

    public boolean updateAdmin(Institucion entity) {
        Usuario admin = new UsuarioJPAController().find(1);
        for (Sector sector : new SectorJPAController().findAll()) {
            UsuarioSector usuarioSector = new UsuarioSector(null, admin, entity, sector, 2, true, true);
            create(usuarioSector);
        }
        return true;
    }

    /**
     * Retorna los sectores en los cuales el usuario tiene permiso de admninUsuario == true o TODOS
     * si id == 1
     *
     * @param usuario
     * @param institucion
     * @return
     */
    public List<Sector> findByAdminUsuarios(Usuario usuario, Institucion institucion) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sector> query = builder.createQuery(Sector.class);
        Root<UsuarioSector> root = query.from(getEntityClass());
        query.select(root.get(UsuarioSector_.sector));
        query.where(
                //                builder.or(builder.equal(root.get(UsuarioSector_.usuario).get(Usuario_.id), 1),
                builder.and(
                        builder.equal(root.get(UsuarioSector_.usuario), usuario),
                        builder.equal(root.get(UsuarioSector_.institucion), institucion),
                        builder.equal(root.get(UsuarioSector_.administraUsuarios), true))
        //        )
        );
        return getEntityManager().createQuery(query).getResultList();
    }
}
