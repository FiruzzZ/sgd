
package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMRecepcionPanel;
import sgd.gui.panel.BuscadorReciboPanel;
import sgd.jpa.controller.AfiliacionJPAController;
import sgd.jpa.controller.ApeJPAController;
import sgd.jpa.controller.AuditoriaJpaController;
import sgd.jpa.controller.AuditoriaMedicaJpaController;
import sgd.jpa.controller.ContableJPAController;
import sgd.jpa.controller.CronicoJpaController;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.DiscapacidadJpaController;
import sgd.jpa.controller.FacturacionJpaController;
import sgd.jpa.controller.GremialesJpaController;
import sgd.jpa.controller.PsicofisicoJPAController;
import sgd.jpa.controller.SolicitudJpaController;
import sgd.jpa.model.Archivo;
import sgd.jpa.model.Precinto;
import sgd.jpa.model.SectorUI;
import sgd.jpa.model.Solicitud;
import sgd.jpa.model.SolicitudDetalle;
import sgd.jpa.model.UsuarioSector;
import utilities.general.UTIL;
import utilities.gui.SwingUtil;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class SolicitudController implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private JDBuscador buscador;
    private Solicitud entity;
    private final SolicitudJpaController jpaController;
    private SectorUI sectorUI;
    private ABMRecepcionPanel abmPanel;
    private BuscadorReciboPanel buscadorPanel;

    public SolicitudController() {
        jpaController = new SolicitudJpaController();
    }

    CustomABMJDialog getAbm(Window owner, SectorUI sector) {
        this.sectorUI = sector;
        abmPanel = new ABMRecepcionPanel();
        UTIL.hideColumnTable(abmPanel.getjTableBuscador(), 0);
        UTIL.hideColumnTable(abmPanel.getjTableDetalle(), 0);
        ArrayList<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, null, sectorUI);
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        SGDUtilities.setCargadorDeArchivosParaSolicitudListener(abmPanel.getCbInstitucion(), abmPanel.getjTableBuscador());
        abmPanel.getBtnAgregar().addActionListener(this);
        abmPanel.getBtnQuitar().addActionListener(this);
        abmPanel.getBtnVerArchivo().addActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "Solicitud - " + sectorUI, true, null);
        customABMJDialog.setMessageText(SGD.getResources().getString("solicitud.ui.message"));
        customABMJDialog.getBtnExtraBottom().setVisible(false);
        customABMJDialog.getBtnEditar().setEnabled(false);
        customABMJDialog.getBtnBorrar().setEnabled(false);
        customABMJDialog.setPanelComponentsEnabled(false);
        customABMJDialog.addBottomButtonsActionListener(this);
        customABMJDialog.addToolBarButtonsActionListener(this);
        return customABMJDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (customABMJDialog != null && customABMJDialog.isActive()) {
            //<editor-fold defaultstate="collapsed" desc="Buttons">
            if (e.getSource().getClass().equals(JButton.class)) {
                if (e.getSource().equals(customABMJDialog.getBtnAceptar())) {
                    //<editor-fold defaultstate="collapsed" desc="aceptar action">
                    try {
                        if (entity == null) {
                            throw new IllegalArgumentException("Seleccione \"Nuevo\" para crear una nueva Recepción.");
                        }
                        String msj = entity.getId() == null ? "Registrado.." : "Modificado..";
                        setEntity();
                        persistEntity();
                        customABMJDialog.showMessage(msj, null, 1);
                        doReport(entity);
                        entity = null;
                        SwingUtil.resetJComponets(abmPanel.getComponents());
                        customABMJDialog.setPanelComponentsEnabled(false);
                        customABMJDialog.setEnabledBottomButtons(false);
                        customABMJDialog.getBtnNuevo().requestFocusInWindow();
                    } catch (IllegalArgumentException | MessageException ex) {
                        customABMJDialog.showMessage(ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
                    } catch (Exception ex) {
                        customABMJDialog.showMessage(ex.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger(this.getClass()).error(ex.getLocalizedMessage(), ex);
                    }
                    //</editor-fold>
                } else if (e.getSource().equals(customABMJDialog.getBtnCancelar())) {
                    customABMJDialog.setPanelComponentsEnabled(false);
                    abmPanel.resetUI(true);
                    entity = null;
                } else if (e.getSource().equals(customABMJDialog.getBtnNuevo())) {
                    entity = new Solicitud();
                    customABMJDialog.setPanelComponentsEnabled(true);
                    abmPanel.resetUI(true);
                    abmPanel.getCbInstitucion().setSelectedIndex(0); // para que cargue la tabla buscador
                    abmPanel.getCbInstitucion().requestFocusInWindow();
                } else if (e.getSource().equals(customABMJDialog.getBtnEditar())) {
                    customABMJDialog.showMessage("No se puede editar una Recepción, debe crearse una nueva en cualquier caso.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } else if (e.getSource().equals(customABMJDialog.getBtnBuscar())) {
                    initBuscador();
                    if (buscador.isEligio()) {
                        setPanelABM(entity);
                        customABMJDialog.setPanelComponentsEnabled(false);
                    }
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    customABMJDialog.showMessage(SGD.getResources().getString("unimplemented"), "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (e.getSource().equals(abmPanel.getBtnAgregar())) {
                    btnAgregarArchivoAction();
                } else if (e.getSource().equals(abmPanel.getBtnQuitar())) {
                    btnQuitarArchivoAction();
                } else if (e.getSource().equals(abmPanel.getBtnVerArchivo())) {
                    if (abmPanel.getjTableBuscador().getSelectedRow() != -1) {
                        Object o = UTIL.getSelectedValue(abmPanel.getjTableBuscador(), 0);
                        SGDUtilities.viewArchivo((Archivo) o);
                    } else {
                        customABMJDialog.showMessage(SGD.getResources().getString("info.selectviewarchive"), null, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            //</editor-fold>
        } else if (buscador != null && buscador.isActive()) {
            if (e.getSource().getClass().equals(JButton.class)) {
                if (e.getSource().equals(buscador.getbBuscar())) {
                    String jpql = getBuscadorQuery();
                    List<Solicitud> list = jpaController.findByQuery(jpql);
                    cargarTablaBuscador(list);
                }
            }
        }
    }

    private void setEntity() throws MessageException {
        if (entity.getDetalle().isEmpty()) {
            throw new MessageException("No se ha agregado ningún Archivo a la recepción");
        }
        if (entity.getId() == null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
            entity.setSector(cb.getEntity().getSector());
            entity.setUsuario(UsuarioController.getCurrentUser());
            if (entity.getNumero() == null) {
                entity.setNumero(jpaController.countBySector(entity.getSector()) + 1);
            }
        }
    }

    private void persistEntity() {
        if (entity.getId() == null) {
            jpaController.create(entity);
        }
    }

    private void checkConstraints(SolicitudDetalle toAdd) throws MessageException {
        for (SolicitudDetalle reciboDetalle : entity.getDetalle()) {
            if (Objects.equals(reciboDetalle.getArchivoId(), toAdd.getArchivoId())) {
                throw new MessageException("Ya se ha agregado el Archivo código: " + toAdd.getBarcode());
            }
        }
    }

    private void cargarTablaDetalle(SolicitudDetalle detalle, String precintos) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTableDetalle().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getBarcode(),
            precintos
        });
    }

    private void btnQuitarArchivoAction() {
        int[] selectedRows = abmPanel.getjTableDetalle().getSelectedRows();
        for (int ii = selectedRows.length - 1; ii >= 0; ii--) {
            int selectedRow = selectedRows[ii];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTableDetalle().getModel();
            SolicitudDetalle candidate = (SolicitudDetalle) dtm.getValueAt(selectedRow, 0);
            SolicitudDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                SolicitudDetalle ad = entity.getDetalle().get(i);
                if (candidate.getId() != null && candidate.getId().equals(ad.getId())) {
                    removed = entity.getDetalle().remove(i);
                } else if (candidate.getId() == null && ad.getId() == null
                        && candidate.getOrderIndex().equals(ad.getOrderIndex())) {
                    removed = entity.getDetalle().remove(i);

                }
            }
            Logger.getLogger(this.getClass()).debug("borrando.. " + selectedRow + ", " + removed);
            dtm.removeRow(selectedRow);
        }
    }

    private void doReport(Solicitud entity) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), "sgd_solicitud_archivos.jasper", "Solicitud N° " + entity.getNumero());
        r.addParameter("ID", entity.getId());
        r.addParameter("TABLA", sectorUI.getNombre());
       r.viewReport();
    }

    private void initBuscador() {
        buscadorPanel = new BuscadorReciboPanel();
        buscador = new JDBuscador(customABMJDialog, true, buscadorPanel, "Buscador de Recepciones");
        buscador.getbImprimir().setEnabled(false);
        buscador.getbLimpiar().setEnabled(false);
        UTIL.getDefaultTableModel(buscador.getjTable1(),
                new String[]{"entity", "N° Recepción", "Fecha"});
        UTIL.hideColumnTable(buscador.getjTable1(), 0);
        UTIL.limpiarDtm(buscador.getjTable1());
        buscador.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    entity = (Solicitud) UTIL.getSelectedValue(buscador.getjTable1(), 0);
                    buscador.setEligio(true);
                    buscador.dispose();
                }
            }
        });
        buscador.addListener(this);
        buscador.setLocationRelativeTo(customABMJDialog);
        buscador.setVisible(true);
    }

    private void setPanelABM(Solicitud entity) {
        abmPanel.getTfNumero().setText(entity.getNumero().toString());
        for (SolicitudDetalle detalle : entity.getDetalle()) {
            Archivo o;
            Integer archivoId = detalle.getArchivoId();
            switch (entity.getSector().getSectorUI()) {
                case AFILIACION: {
                    o = new AfiliacionJPAController().find(archivoId);
                    break;
                }
                case APE: {
                    o = new ApeJPAController().find(archivoId);
                    break;
                }
                case AUDITORIA: {
                    o = new AuditoriaJpaController().find(archivoId);
                    break;
                }
                case CONTABLE: {
                    o = new ContableJPAController().find(archivoId);
                    break;
                }
                case FACTURACION: {
                    o = new FacturacionJpaController().find(archivoId);
                    break;
                }
                case PSICOFISICO: {
                    o = new PsicofisicoJPAController().find(archivoId);
                    break;
                }
                case GREMIALES: {
                    o = new GremialesJpaController().find(archivoId);
                    break;
                }
                case CRONICO: {
                    o = new CronicoJpaController().find(archivoId);
                    break;
                }
                case DISCAPACIDAD: {
                    o = new DiscapacidadJpaController().find(archivoId);
                    break;
                }
                case AUDITORIAMEDICA: {
                    o = new AuditoriaMedicaJpaController().find(archivoId);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("sectorUI no definido: " + entity.getSector());
                }
            }
            String precintosString = "";
            for (Precinto precinto : o.getPrecintos()) {
                precintosString += precinto.getCodigo() + ", ";
            }
            precintosString = precintosString.substring(0, precintosString.length() - 2);
            System.out.println(o.toString());
            ((DefaultTableModel) abmPanel.getjTableDetalle().getModel())
                    .addRow(new Object[]{o, o.getBarcode(), precintosString});
        }
    }

    private String getBuscadorQuery() {
        StringBuilder sb = new StringBuilder("SELECT o FROM " + jpaController.getEntityClass().getSimpleName() + " o "
                + " WHERE o.id is not null ");
        if (buscadorPanel.getjTextField1().length() > 0) {
            sb.append(" AND o.numero=").append(Long.valueOf(buscadorPanel.getjTextField1()));
        }
        if (buscadorPanel.getDcFechaDesde() != null) {
            sb.append(" AND o.creation >= '").append(buscadorPanel.getDcFechaDesde()).append("'");
        }
        if (buscadorPanel.getDcFechaHasta() != null) {
            sb.append(" AND o.creation <= '").append(buscadorPanel.getDcFechaDesde()).append("'");
        }
        return sb.toString();
    }

    private void cargarTablaBuscador(List<Solicitud> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (Solicitud o : list) {
            dtm.addRow(new Object[]{
                o,
                o.getNumero(),
                UTIL.DATE_FORMAT.format(o.getCreation())
            });
        }
    }

    private void cargarTablaArchivos(Object entity, String barcode, String precintos) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTableBuscador().getModel();
        dtm.addRow(new Object[]{entity, barcode, precintos});
    }

    private void btnAgregarArchivoAction() {
        try {
            if (abmPanel.getjTableBuscador().getSelectedRowCount() > 0) {
                for (int selectedRow : abmPanel.getjTableBuscador().getSelectedRows()) {
                    DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTableBuscador().getModel();
                    Archivo o = (Archivo) dtm.getValueAt(selectedRow, 0);
                    SolicitudDetalle detalle = new SolicitudDetalle(entity.getNextOrderIndex(), o.getId(), o.getBarcode(), entity);
                    checkConstraints(detalle);
                    entity.getDetalle().add(detalle);
                    cargarTablaDetalle(detalle, (String) abmPanel.getjTableBuscador().getModel().getValueAt(selectedRow, 2));
//                    abmPanel.resetUI(false);
                }
            } else {
                throw new MessageException("Debe seleccionar al menos un archivo de la tabla superior");
            }
        } catch (MessageException ex) {
            customABMJDialog.showMessage(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

}
