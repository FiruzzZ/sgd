package sgd;

import generics.ShutDownListener;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sgd.controller.UsuarioController;
import sgd.gui.Principal;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.UsuarioJPAController;

/**
 *
 * @author FiruzzZ
 */
public class SGD {

    public static final String propertiesFile = "cfg.ini";
    private static ResourceBundle resources;
    private static boolean develop;
    public static final Logger LOG = Logger.getLogger(SGD.class);
    private Principal principal;

    private ShutDownListener sdl = new ShutDownListener() {
        @Override
        public Connection getConnection() {
            return DAO.getJDBCConnection();
        }

        @Override
        public void shutDownAction() {
            salir();
        }
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (String login : args) {
            if (login.equalsIgnoreCase("develop")) {
                develop = true;
                break;
            }
        }
        //<editor-fold defaultstate="collapsed" desc=" Look and feel Nimbus setting code">
        try {
            //If Nimbus (introduced in Java SE 6) is not available, stay with the
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            LOG.trace("not support Nimbus");
        }
        //</editor-fold>
        new SGD();
    }

    private SGD() {
        SplashScreen.getSplashScreen();
        PropertyConfigurator.configure("log4j.properties");
        resources = ResourceBundle.getBundle("sgd.properties");
        Properties properties = null;
        try {
            properties = load(new File(propertiesFile));
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No se encotró el archivo de configuración (cfg.ini)", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error en la lectura del archivo de configuracion de sistema", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        try {
            DAO.setProperties(properties);
            DAO.getEntityManager();
            sdl.getFuerzaBrutaShutDown().start();
            DAO.setDefaultData();
            if (develop) {
                UsuarioController.setCurrentUser(new UsuarioJPAController().find(1));
            }
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    principal = new Principal();
                    setAppIcons(principal);
                    principal.setVisible(true);
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error:\n" + ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LOG.fatal(ex, ex);
        }
    }

    public static ResourceBundle getResources() {
        return resources;
    }

    public static Properties load(File propsFile) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(propsFile)) {
            props.load(fis);
        }
        return props;
    }

    private void setAppIcons(JFrame jFrame) {
        List<Image> iconsList = new ArrayList<>(3);
        iconsList.add(Toolkit.getDefaultToolkit().createImage(getClass().getResource(SGD.getResources().getString("app.icon"))));
        iconsList.add(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/sgd/rsc/icons/16px_app.ico")));
        iconsList.add(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/sgd/rsc/icons/24px_app.ico")));
        iconsList.add(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/sgd/rsc/icons/32px_app.ico")));
        jFrame.setIconImages(iconsList);
    }

    private void salir() {
        if (principal != null) {
            principal.dispose();
        }
        try {
            sdl.close();
        } catch (SQLException ex) {
            LOG.fatal(ex, ex);
        }
        if (sdl.isShutDownSystem()) {
            try {
                String shutDownMessage = sdl.getMessage();
                if (new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "Messenger.jar").exists()) {
                    Process p = Runtime.getRuntime().exec("java -jar Messenger.jar " + shutDownMessage + " Cerrado_por_mantenimiento");
                }
            } catch (Exception ex) {
                LOG.trace("shutting Down Thread!!", ex);
            }
        }
        System.exit(0);
    }
}
