package sgd.controller;

import controller.exceptions.MissingReportException;
import generics.WaitingDialog;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import sgd.jpa.controller.DAO;

/**
 *
 * @author FiruzzZ
 */
public class Reportes implements Runnable {

    private Map<String, Object> parameters;
    private final String pathReport;
    private final String tituloReporte;
    /**
     * sería algo así "./reportes/"
     */
    public static final String FOLDER_REPORTES = "." + System.getProperty("file.separator") + "reportes" + System.getProperty("file.separator");
    private Boolean isViewerReport = null;
    /**
     * Para saber si la ventana de impresión ya sucedió. (No si aceptó o
     * canceló)
     */
    private boolean reporteFinalizado;
    /**
     * Si aparece el PrintDialog Default = true
     */
    private boolean withPrintDialog;
    private Thread reportThread;
    private final Connection connection;
    private JDialog jd;

    /**
     *
     * @param pathReport ruta absoluta del archivo .JASPER o solo el nombre del
     * archivo .JASPER si se encuentra en {@link Reportes#FOLDER_REPORTES} +
     * pathReport
     * @param title Título de la ventana del reporte
     * @throws MissingReportException
     */
    public Reportes(String pathReport, String title) throws MissingReportException {

        if (pathReport == null) {
            throw new IllegalArgumentException("pathReport CAN'T BE NULL....no es válida!");
        }

        if (!new File(pathReport).exists()) {
            if (!new File(FOLDER_REPORTES + pathReport).exists()) {
                throw new MissingReportException("No se encontró el archivo del reporte: " + pathReport
                        + "\n" + FOLDER_REPORTES + pathReport);
            }
            pathReport = FOLDER_REPORTES + pathReport;
        }
        parameters = new HashMap<>();
        this.pathReport = pathReport;
        tituloReporte = title;
        reporteFinalizado = false;
        withPrintDialog = true;
        connection = DAO.getJDBCConnection();
    }

    /**
     *
     * @param jdbcConnection a java.sql.Connection
     * @param pathReport ruta absoluta del archivo .JASPER o solo el nombre del
     * archivo .JASPER si se encuentra en {@link Reportes#FOLDER_REPORTES} +
     * pathReport
     * @param title Título de la ventana del reporte
     * @throws MissingReportException
     * @throws IllegalArgumentException if pathReport is null
     */
    public Reportes(Connection jdbcConnection, String pathReport, String title) throws MissingReportException {
        connection = jdbcConnection;
        if (pathReport == null) {
            throw new IllegalArgumentException("pathReport CAN'T BE NULL....no es válida!");
        }

        if (!new File(pathReport).exists()) {
            if (!new File(FOLDER_REPORTES + pathReport).exists()) {
                throw new MissingReportException("No se encontró el archivo del reporte: " + pathReport
                        + "\n" + FOLDER_REPORTES + pathReport);
            }
            pathReport = FOLDER_REPORTES + pathReport;
        }
        parameters = new HashMap<>();
        this.pathReport = pathReport;
        tituloReporte = title;
        reporteFinalizado = false;
        withPrintDialog = true;

    }

    public void viewReport() throws JRException {
        isViewerReport = true;
        reportThread = new Thread(this, pathReport);
        reportThread.start();
    }

    /**
     * Bla bla...
     *
     * @param withPrintDialog si aparece el PrintDialog o imprime directamente.
     * @return
     * @throws JRException
     */
    public boolean printReport(boolean withPrintDialog) throws JRException {
        jd = new WaitingDialog((JDialog) null, "Preparando impresion", false, "Preparando archivo para impresión");
        jd.setAlwaysOnTop(true);
        jd.setVisible(true);
        isViewerReport = false;
        this.withPrintDialog = withPrintDialog;
        reportThread = new Thread(this, pathReport);
        reportThread.start();
        reporteFinalizado = true;
        return reporteFinalizado;
    }

    public void printReport() throws JRException {
        isViewerReport = false;
        reportThread = new Thread(this, pathReport);
        reportThread.start();
    }

    public void exportPDF(String filePathSafer) throws JRException {
        JasperPrint jprint;
        jprint = JasperFillManager.fillReport(pathReport, parameters, connection);
        JasperExportManager.exportReportToPdfFile(jprint, filePathSafer);
    }

    public void addParameter(String key, Object parametro) {
        parameters.put(key, parametro);
    }

    public void setParameterMap(HashMap<String, Object> map) {
        parameters = map;
    }

    /**
     * Add a parameter ENTIDAD al reporte. Es decir el Nombre de la empresa, si
     * existe
     *
     * @throws IOException
     */
    public void addEntidad() throws IOException {
//      addParameter("ENTIDAD", datosEmpresaController.getNombre());
    }

    /**
     * Add a parameter CURRENT_USER to the report
     */
    public void addCurrent_User() {
        addParameter("CURRENT_USER", UsuarioController.getCurrentUser().getNombre());
    }

    @Override
    public void run() {
        Logger.getLogger(Reportes.class).trace("Initializing Thread Reportes: " + reportThread.getName());
        try {
            doReport();
        } catch (Exception ex) {
            jd.dispose();
            Logger.getLogger(Reportes.class).trace("Error en Report, trying to close JDBC Connection..", ex);
            try {
                connection.close();
            } catch (SQLException sQLException) {
                Logger.getLogger(Reportes.class).error("Error intentando cerrar conexión JDBC", sQLException);
            }
            JOptionPane.showMessageDialog(null, "Error generando reporte:" + ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        Logger.getLogger(Reportes.class).trace("Finished Thread Reportes: " + reportThread.getName());

    }

    private synchronized void doReport() {
        Logger.getLogger(Reportes.class).trace("Running doReport()..");
        JasperPrint jPrint;
        try {
            jPrint = JasperFillManager.fillReport(pathReport, parameters, connection);
            if (isViewerReport) {
                JasperViewer jViewer = new JasperViewer(jPrint, false);
                jViewer.setTitle(tituloReporte);
                jViewer.setExtendedState(JasperViewer.NORMAL);
                jViewer.setAlwaysOnTop(true);
                jd.dispose();
                jViewer.setVisible(true);
            } else {
                jd.dispose();
                JasperPrintManager.printReport(jPrint, withPrintDialog);
            }
        } catch (JRException ex) {
            Logger.getLogger(Reportes.class).log(Level.ERROR, "Se pudrió todo con el reporte", ex);
        }
        Logger.getLogger(Reportes.class).trace("Finished doReport()");
    }

    public boolean isReporteFinalizado() {
        return reporteFinalizado;
    }
}
