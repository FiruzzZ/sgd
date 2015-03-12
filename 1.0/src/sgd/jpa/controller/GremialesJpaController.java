package sgd.jpa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sgd.jpa.model.Gremiales;
import sgd.jpa.model.GremialesDetalle;
import sgd.jpa.model.GremialesDetalle_;
import sgd.jpa.model.GremialesPrecinto;
import sgd.jpa.model.Precinto;

/**
 *
 * @author FiruzzZ
 */
public class GremialesJpaController extends SGDJpaImp<Gremiales, Integer> {

    public List<GremialesDetalle> findDetalleByQuery(String jpql) {
        return getEntityManager().createQuery(jpql).getResultList();
    }

    public void removePrecintos(Gremiales entity) {
        getEntityManager().getTransaction().begin();
        for (Precinto precinto : entity.getPrecintos()) {
            getEntityManager().remove(precinto);
            entity.setPrecintos(new ArrayList<GremialesPrecinto>(0));
        }
        getEntityManager().getTransaction().commit();
    }

    /**
     *
     * @return key = CUIT, value = nombre
     */
    public Map<String, String> findEmpresas() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<GremialesDetalle> root = query.from(GremialesDetalle.class);
        query.select(cb.upper(root.get(GremialesDetalle_.empresa))).distinct(true)
                .where(cb.isNotNull(root.get(GremialesDetalle_.empresa)))
                .orderBy(cb.asc(cb.upper(root.get(GremialesDetalle_.empresa))));
        List<String> resultList = getEntityManager().createQuery(query).getResultList();
        Map<String, String> l = new HashMap<>(resultList.size());
        for (String string : resultList) {
            String[] x = string.split("_");
            if (x.length > 1) {
                l.put(x[0], x[1]);
            }
        }
        return l;
    }

    /**
     *
     * @return key = CUIL, value= Nombre
     */
    public Map<String, String> findEmpleados() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<GremialesDetalle> root = query.from(GremialesDetalle.class);
        query.select(cb.upper(root.get(GremialesDetalle_.empleado))).distinct(true)
                .where(cb.isNotNull(root.get(GremialesDetalle_.empleado)))
                .orderBy(cb.asc(cb.upper(root.get(GremialesDetalle_.empleado))));
        List<String> resultList = getEntityManager().createQuery(query).getResultList();
        Map<String, String> l = new HashMap<>(resultList.size());
        for (String string : resultList) {
            String[] x = string.split("_");
            if (x.length > 1) {
                l.put(x[0], x[1]);
            }
        }
        return l;
    }
}
