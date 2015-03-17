package sgd.jpa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.controller.SGDUtilities;
import sgd.jpa.model.Archivo;
import sgd.jpa.model.Institucion;
import sgd.jpa.model.Precinto;
import sgd.jpa.model.Recibo;
import sgd.jpa.model.Sector;

/**
 *
 * @author FiruzzZ
 * @param <T>
 * @param <ID>
 */
public abstract class SGDJpaImp<T extends Archivo, ID extends Serializable> extends AbstractDAO<T, Serializable> {

    protected EntityManager entityManager;

    /**
     * Para cuando se necesita mantener el contexto de persistencia abierto.
     * <br>Ej: recuperar objectos en LAZY load; manipular varios JPAControllers simultaneamente;
     */
    private boolean keepSessionClosed = true;

    public SGDJpaImp() {
        getEntityManager();
    }

    public SGDJpaImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = DAO.getEntityManager();
        }
        return entityManager;
    }

    public final boolean keepSessionClosed() {
        return keepSessionClosed;
    }

    public final void setKeepSessionClosed(boolean keepSessionClosed) {
        this.keepSessionClosed = keepSessionClosed;
    }

    @Override
    public void create(T entity) {
        entity.setCodigo(getNextCodigo(entity.getInstitucion(), entity.getSector()));
        entity.setBarcode(SGDUtilities.getBarcode(entity));
        super.create(entity);
    }

    /**
     * Recupera los archivos de la institución que estén relacionados a un {@link Recibo} (de envío)
     *
     * @param institucion cannot be null
     * @return
     */
    public List<T> findEnviados(Institucion institucion) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getEntityClass());
        Root<T> from = query.from(getEntityClass());
        query.select(from).
                where(cb.and(cb.equal(from.get("institucion"), institucion),
                                cb.isNotNull(from.get("recibo"))
                        ));
        return getEntityManager().createQuery(query).getResultList();
    }

    public List<T> findByEstado(Institucion institucion, boolean cerrada) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getEntityClass());
        Root<T> from = query.from(getEntityClass());
        query.select(from).
                where(cb.and(cb.equal(from.get("institucion"), institucion),
                                cb.equal(from.get("cerrada"), cerrada)));
        return getEntityManager().createQuery(query).getResultList();
    }

    /**
     * Recupera los archivos del sector cerrados pero NO relacionados a un {@link Recibo}.
     *
     * @param institucion
     * @return
     */
    public List<T> findToArchivar(Institucion institucion) {
        List<T> resultList = findByEstado(institucion, true);
        List<T> toArchivar = new ArrayList<>(resultList.size());
        for (T archivo : resultList) {
            if (archivo.getRecibo() == null) {
                toArchivar.add(archivo);
            }
        }
        return toArchivar;
    }

    public int getNextCodigo(Institucion institucion, Sector sector) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Number> query = cb.createQuery(Number.class);
        Root<T> from = query.from(getEntityClass());
        query.select(cb.greatest(from.get("codigo").as(Integer.class))).
                where(cb.and(
                                cb.equal(from.get("institucion"), institucion),
                                cb.equal(from.get("sector"), sector)));
        Number next = getEntityManager().createQuery(query).getSingleResult();
        return next == null ? 1 : next.intValue() + 1;
    }
}
