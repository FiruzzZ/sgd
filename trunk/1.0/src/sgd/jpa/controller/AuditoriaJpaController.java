package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.Auditoria;
import sgd.jpa.model.AuditoriaDetalle;
import sgd.jpa.model.AuditoriaDetalle_;
import sgd.jpa.model.AuditoriaPrecinto;
import sgd.jpa.model.SubTipoDocumento;
import sgd.jpa.model.TipoDocumento;

/**
 *
 * @author FiruzzZ
 */
public class AuditoriaJpaController extends SGDJpaImp<Auditoria, Integer> {

    public AuditoriaJpaController() {
    }

    public List<AuditoriaDetalle> findAuditoriaDetalleByJPQL(String jpql) {
        return getEntityManager().createQuery(jpql, AuditoriaDetalle.class).getResultList();
    }

    public void removePrecintos(Auditoria entity) {
        getEntityManager().getTransaction().begin();
        for (AuditoriaPrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<AuditoriaPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }

    public List<String> findPrestadores() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<AuditoriaDetalle> root = query.from(AuditoriaDetalle.class);
        query.select(cb.upper(root.get(AuditoriaDetalle_.prestador))).distinct(true);
        query.orderBy(cb.asc(cb.upper(root.get(AuditoriaDetalle_.prestador))));
        return getEntityManager().createQuery(query).getResultList();
    }

    public List<AuditoriaDetalle> findDetalle(TipoDocumento tipoDocumento, SubTipoDocumento subTipoDocumento, String prestador, Date documentoFecha) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AuditoriaDetalle> query = cb.createQuery(AuditoriaDetalle.class);
        Root<AuditoriaDetalle> root = query.from(AuditoriaDetalle.class);
        query.where(cb.and(
                cb.equal(root.get(AuditoriaDetalle_.tipoDocumento), tipoDocumento),
                cb.equal(root.get(AuditoriaDetalle_.subTipoDocumento), subTipoDocumento),
                cb.equal(root.get(AuditoriaDetalle_.prestador), prestador),
                cb.equal(root.get(AuditoriaDetalle_.documentoFecha), documentoFecha))
        );
        return getEntityManager().createQuery(query).getResultList();
    }
}
