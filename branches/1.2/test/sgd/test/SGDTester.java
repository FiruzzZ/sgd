package sgd.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.PropertyConfigurator;
import sgd.SGD;
import sgd.controller.*;
import sgd.jpa.controller.*;

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
    }
}
