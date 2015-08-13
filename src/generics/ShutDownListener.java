package generics;

import generics.WaitingDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

/**
 *
 * @author FiruzzZ
 */
public abstract class ShutDownListener {

    private boolean closeFuerzaBrutaThread = false;
    private boolean activeConnection = false;
    private boolean shutDownSystem = false;
    private Connection connection;
    private static final Logger LOG = Logger.getLogger(ShutDownListener.class.getName());
    private static final String connectionErrorIcon = "/sgd/rsc/icons/32px_action_configure.png";
    private String message;
    private WaitingDialog lostConnectionDialog;
    private final Thread fuerzaBrutaShutDown = new Thread(new Runnable() {
        @Override
        public void run() {
            LOG.trace("Iniciando bucle fuerzaBruta!!!");
            while (!closeFuerzaBrutaThread && !shutDownSystem) {
                try {
                    shutDownSystem = hasToShutdown();
                    if (shutDownSystem) {
                        shutDownAction();
                    }
                    activeConnection = true;
                    if (lostConnectionDialog != null && lostConnectionDialog.isVisible()) {
                        lostConnectionDialog.dispose();
                    }
                } catch (InterruptedException ex) {
                    LOG.trace("shutDownThread sleeping INTERRUPTED!!");
                    break;
//                } catch (SQLException ex) {
                } catch (Exception ex) {
                    LOG.warn("shutDownThread Exception!! " + ex.getLocalizedMessage());
                    try {
                        LOG.trace("Cerrando connection fallida (porque la poronga de c3p0 no lo hace solo como EclipseLink)");
                        connection.close();
                        connection = null;
                    } catch (SQLException ex1) {
                        LOG.error("Error cerrando connection fallida:" + ex1.getLocalizedMessage());
                    }
                    activeConnection = false;
                    displayLostConnectionUI(ex.getLocalizedMessage());
                }
            }
            LOG.trace("finishing Thread > fuerzaBrutaShutDown");
        }
    });

    public abstract Connection getConnection();

    public abstract void shutDownAction();

    public void close() throws SQLException {
        closeFuerzaBrutaThread = true;
        if (!connection.isClosed()) {
            connection.close();
        }
    }

    public boolean isShutDownSystem() {
        return shutDownSystem;
    }

    public final Thread getFuerzaBrutaShutDown() {
        return fuerzaBrutaShutDown;
    }

    public boolean hasToShutdown() throws SQLException, InterruptedException {
        boolean cerrar;
        try (ResultSet rs = getActiveConnection().createStatement().executeQuery("SELECT shutdown from sistema")) {
            rs.next();
            cerrar = rs.getBoolean(1);
        }
        if (cerrar) {
            message = getShutDownMessage();
        } else {
            Thread.sleep(3000);
        }
        return cerrar;
    }

    private Connection getActiveConnection() throws SQLException {
//        LOG.trace("getActive = " + (connection == null || connection.isClosed()));
        if (connection == null || connection.isClosed()) {
            connection = getConnection();
        }
        return connection;
    }

    public final String getMessage() {
        return message;
    }

    public String getShutDownMessage() throws SQLException {
        ResultSet rs = getActiveConnection().createStatement().executeQuery("SELECT shutdown_message, shutdown_time from sistema");
        if (rs.next()) {
            return rs.getString(1).replaceAll(" ", "_");
        } else {
            return "No_message_from_server";
        }
    }

    private void displayLostConnectionUI(String message) {
        LOG.trace("display lost connection");
        if (lostConnectionDialog == null) {
            lostConnectionDialog = new WaitingDialog(null, "Error de conexión", true,
                    "<html>Error de conexión con la base de datos:"
                    + "<br>Esta ventana desaparecerá cuando la conexión sea re-establecida."
                    + "<br>Si cierra la ventana finalizará la aplicación"
                    + "<br>" + message
                    + "<br>"
                    + "<br>");
            lostConnectionDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("closing lostConnectionDialog");
                    System.exit(0);
                }
            });
            if (connectionErrorIcon != null) {
                lostConnectionDialog.getLabelMessage().setIcon(new ImageIcon(getClass().getResource(connectionErrorIcon)));
            }
        }
        if (!lostConnectionDialog.isVisible()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lostConnectionDialog.setVisible(true);
                }
            }).start();
        } else {
            if (lostConnectionDialog.isVisible()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}
