package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.Cronico;
import sgd.jpa.model.CronicoDetalle;
import sgd.jpa.model.CronicoDetalle_;
import sgd.jpa.model.CronicoPrecinto;

/**
 *
 * @author FiruzzZ
 */
public class CronicoJpaController extends SGDJpaImp<Cronico, Integer> {
 @SuppressWarnings("unchecked")
    public List<CronicoDetalle> findCronicoDetalleByNativeQuery(String jpql) {
        return getEntityManager().createNativeQuery(jpql, CronicoDetalle.class).getResultList();
    }

    public List<CronicoDetalle> findCronicoDetalleByJPQL(String jpql) {
        return getEntityManager().createQuery(jpql, CronicoDetalle.class).getResultList();
    }

    public void removePrecintos(Cronico entity) {
        getEntityManager().getTransaction().begin();
        for (CronicoPrecinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<CronicoPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }

//    public List<String> findPrestadores() {
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<String> query = cb.createQuery(String.class);
//        Root<CronicoDetalle> root = query.from(CronicoDetalle.class);
//        query.select(cb.upper(root.get(CronicoDetalle_.prestador))).distinct(true);
//        query.orderBy(cb.asc(cb.upper(root.get(CronicoDetalle_.prestador))));
//        return getEntityManager().createQuery(query).getResultList();
//    }

    public List<CronicoDetalle> findIdenticalDetalle(CronicoDetalle o) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CronicoDetalle> query = cb.createQuery(CronicoDetalle.class);
        Root<CronicoDetalle> root = query.from(CronicoDetalle.class);
        query.where(cb.and(
                cb.equal(root.get(CronicoDetalle_.tipoDocumento), o.getTipoDocumento()),
                cb.equal(root.get(CronicoDetalle_.subTipoDocumento), o.getSubTipoDocumento()),
                cb.equal(root.get(CronicoDetalle_.numeroAfiliado), o.getNumeroAfiliado()),
                cb.equal(root.get(CronicoDetalle_.numeroFamiliar), o.getNumeroFamiliar()),
                cb.equal(root.get(CronicoDetalle_.numeroDocumento), o.getNumeroDocumento()),
                cb.equal(root.get(CronicoDetalle_.numeroFormulario), o.getNumeroFormulario()),
                cb.equal(root.get(CronicoDetalle_.documentoFecha), o.getDocumentoFecha()))
        );
        return getEntityManager().createQuery(query).getResultList();
    }
}
