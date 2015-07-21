package sgd.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.apache.log4j.PropertyConfigurator;
import sgd.SGD;
import sgd.controller.*;
import sgd.jpa.controller.*;
import sgd.jpa.model.Psicofisico;
import sgd.jpa.model.PsicofisicoDetalle;
import sgd.jpa.model.PsicofisicoPrecinto;

/**
 *
 * @author FiruzzZ
 */
public class SGDTester {

    private static ResourceBundle resources;

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("log4j.properties");
        resources = ResourceBundle.getBundle("sgd.properties");
        Properties properties = SGD.load(new File(SGD.propertiesFile));
        DAO.setProperties(properties);
        DAO.getEntityManager();
        UsuarioController.setCurrentUser(new UsuarioJPAController().find(1));
        List<Psicofisico> psicos = new PsicofisicoJPAController().findAll();
        System.out.println("Psicos=" + psicos.size());
//        properties.setProperty("database", "sgd2");
        properties.setProperty("server", "192.168.1.254");
        DAO.setProperties(properties);
        EntityManager em = DAO.getEntityManager();
        em.getTransaction().begin();
        for (Psicofisico p : psicos) {
            p.setId(null);
            for (PsicofisicoDetalle d : p.getDetalle()) {
                d.setId(null);
            }
            for (PsicofisicoPrecinto pp : p.getPrecintos()) {
                pp.setId(null);
            }
            em.persist(p);
            System.out.println(p.getId());
        }
        em.getTransaction().commit();
    }
}
