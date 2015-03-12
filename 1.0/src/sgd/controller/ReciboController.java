package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMReciboPanel;
import sgd.gui.panel.BuscadorReciboPanel;
import sgd.jpa.controller.*;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author Administrador
 */
public class ReciboController implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private JDBuscador buscador;
    private BuscadorReciboPanel buscadorPanel;
    private ABMReciboPanel abmPanel;
    private Recibo entity;
    private final ReciboJPAController jPAController;
    private SectorUI sectorUI;

    public ReciboController() {
        jPAController = new ReciboJPAController();
    }

    CustomABMJDialog getAbm(Window owner, SectorUI sector) {
        this.sectorUI = sector;
        abmPanel = new ABMReciboPanel();
        UTIL.hideColumnTable(abmPanel.getjTableBuscador(), 0);
        UTIL.hideColumnTable(abmPanel.getjTableDetalle(), 0);
        ArrayList<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, null, sectorUI);
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        SGDUtilities.setCargadorDeArchivosParaReciboListener(abmPanel);
        abmPanel.getBtnAgregar().addActionListener(this);
        abmPanel.getBtnQuitar().addActionListener(this);
        abmPanel.getBtnVerArchivo().addActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM Envíos - " + sectorUI, true, null);
        customABMJDialog.setMessageText(SGD.getResources().getString("envio.ui.message"));
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
                            throw new IllegalArgumentException("Seleccione \"Nuevo\" para crear un nuevo Envío.");
                        }
                        String msj = entity.getId() == null ? "Registrado.." : "Modificado..";
                        setEntity();
                        persistEntity();
                        updateArchivosEnviados();
                        customABMJDialog.showMessage(msj, null, 1);
                        doReport(entity);
                        customABMJDialog.setPanelComponentsEnabled(false);
                        abmPanel.resetUI(true);
                        entity = null;
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
                    entity = new Recibo();
                    customABMJDialog.setPanelComponentsEnabled(true);
                    abmPanel.resetUI(true);
                    abmPanel.getCbInstitucion().setSelectedIndex(0); // para que cargue la tabla buscador
                    abmPanel.getCbInstitucion().requestFocusInWindow();
                } else if (e.getSource().equals(customABMJDialog.getBtnEditar())) {
                    customABMJDialog.showMessage("No se puede editar un Envío, debe crearse uno nuevo en cualquier caso.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } else if (e.getSource().equals(customABMJDialog.getBtnBuscar())) {
                    initBuscador();
                    if (buscador.isEligio()) {
                        setPanelABM(entity);
                        customABMJDialog.setPanelComponentsEnabled(false);
                    }
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    customABMJDialog.showMessage(SGD.getResources().getString("unimplemented"), "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (e.getSource().equals(abmPanel.getBtnAgregar())) {
                    agregarArchivoToReciboAction();
                } else if (e.getSource().equals(abmPanel.getBtnQuitar())) {
                    btnQuitarArchivoAction();
                } else if (e.getSource().equals(abmPanel.getBtnVerArchivo())) {
                    if (abmPanel.getjTableBuscador().getSelectedRow() != -1) {
                        Archivo o = (Archivo) UTIL.getSelectedValue(abmPanel.getjTableBuscador(), 0);
                        SGDUtilities.viewArchivo(o);
                    } else {
                        customABMJDialog.showMessage(SGD.getResources().getString("info.selectviewarchive"), null, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            //</editor-fold>
        } else if (buscador != null && buscador.isActive()) {
            if (e.getSource().getClass().equals(JButton.class)) {
                if (e.getSource().equals(buscador.getbBuscar())) {
                    String jpql = armarQuery();
                    List<Recibo> list = jPAController.findByQuery(jpql);
                    cargarTablaBuscador(list);
                }
            }
        }
    }

    private void setEntity() throws MessageException {
        if (entity.getDetalle() == null || entity.getDetalle().isEmpty()) {
            throw new MessageException(SGD.getResources().getString("warn.emptyenvio"));
        }
        if (entity.getId() == null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
            entity.setSector(cb.getEntity().getSector());
            entity.setUsuario(UsuarioController.getCurrentUser());
            if (entity.getNumero() == null) {
                entity.setNumero(jPAController.countBySector(entity.getSector()) + 1);
            }
        }
    }

    private void persistEntity() {
        if (entity.getId() == null) {
            jPAController.create(entity);
        }
    }

    private void updateArchivosEnviados() {
        for (ReciboDetalle reciboDetalle : entity.getDetalle()) {
            switch (sectorUI) {
                case AFILIACION: {
                    new AfiliacionController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case APE: {
                    new ApeController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case AUDITORIA: {
                    new AuditoriaController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case CONTABLE: {
                    new ContableController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case FACTURACION: {
                    new FacturacionController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case PSICOFISICO: {
                    new PsicofisicoController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case GREMIALES: {
                    new GremialesController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case CRONICO: {
                    new CronicoController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                case DISCAPACIDAD: {
                    new DiscapacidadController().enviado(reciboDetalle.getArchivoId(), entity);
                    break;
                }
                default:
                    throw new IllegalArgumentException(SGD.getResources().getString("undefinedsectorimplentation") + ": " + sectorUI);
            }
        }
    }

    private ReciboDetalle getDetalle(int selectedRow) throws MessageException {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTableBuscador().getModel();
        Archivo o = (Archivo) dtm.getValueAt(selectedRow, 0);
        Integer archivoID = o.getId();
        String barcode = o.getBarcode();
        ReciboDetalle detalle = new ReciboDetalle(entity, entity.getNextOrderIndex(), archivoID, barcode);
        return detalle;
    }

    private void checkConstraints(ReciboDetalle toAdd) throws MessageException {
        for (ReciboDetalle reciboDetalle : entity.getDetalle()) {
            if (reciboDetalle.getArchivoId() == toAdd.getArchivoId()) {
                throw new MessageException("Ya se ha agregado el Archivo código: " + toAdd.getBarcode());
            }
        }
    }

    private void cargarTablaDetalle(ReciboDetalle detalle, String precintos) {
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
            ReciboDetalle candidate = (ReciboDetalle) dtm.getValueAt(selectedRow, 0);
            ReciboDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                ReciboDetalle ad = entity.getDetalle().get(i);
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

    private void doReport(Recibo entity) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.recibido"), "Nota de Envío N° " + entity.getNumero());
        r.addParameter("RECIBO_ID", entity.getId());
        r.addParameter("TABLA", sectorUI.getNombre());
        r.printReport(true);
    }

    private void initBuscador() {
        buscadorPanel = new BuscadorReciboPanel();
        buscador = new JDBuscador(customABMJDialog, true, buscadorPanel, "Buscador de Envíos");
        buscador.getbImprimir().setEnabled(false);
        buscador.getbLimpiar().setEnabled(false);
        UTIL.getDefaultTableModel(buscador.getjTable1(),
                new String[]{"entity.Recibo", "N° Envío", "Fecha"});
        UTIL.hideColumnTable(buscador.getjTable1(), 0);
        UTIL.limpiarDtm(buscador.getjTable1());
        buscador.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    entity = (Recibo) UTIL.getSelectedValue(buscador.getjTable1(), 0);
                    buscador.setEligio(true);
                    buscador.dispose();
                }
            }
        });
        buscador.addListener(this);
        buscador.setLocationRelativeTo(customABMJDialog);
        buscador.setVisible(true);
    }

    private void setPanelABM(Recibo entity) {
        abmPanel.getTfNumero().setText(entity.getNumero().toString());
        for (ReciboDetalle reciboDetalle : entity.getDetalle()) {
            reciboDetalle.getArchivoId();

        }
    }

    private String armarQuery() {
        StringBuilder sb = new StringBuilder("SELECT o FROM " + Recibo.class
                .getSimpleName() + " o "
                + " WHERE o.id is not null ");
        if (buscadorPanel.getjTextField1()
                .length() > 0) {
            sb.append(" AND o.numero=").append(Long.valueOf(buscadorPanel.getjTextField1()));
        }

        if (buscadorPanel.getDcFechaDesde()
                != null) {
            sb.append(" AND o.creation >= '").append(UTIL.yyyy_MM_dd.format(buscadorPanel.getDcFechaDesde())).append("'");
        }

        if (buscadorPanel.getDcFechaHasta()
                != null) {
            sb.append(" AND o.creation <= '").append(UTIL.yyyy_MM_dd.format(buscadorPanel.getDcFechaDesde())).append("'");
        }

        return sb.toString();
    }

    private void cargarTablaBuscador(List<Recibo> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (Recibo recibo : list) {
            dtm.addRow(new Object[]{
                recibo,
                recibo.getId(),
                UTIL.DATE_FORMAT.format(recibo.getCreation())
            });
        }
    }

    private void cargarTablaArchivos(Object entity, String barcode, String precintos) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTableBuscador().getModel();
        dtm.addRow(new Object[]{entity, barcode, precintos});
    }

    private void agregarArchivoToReciboAction() {
        try {
            if (abmPanel.getjTableBuscador().getSelectedRowCount() > 0) {
                for (int selectedRow : abmPanel.getjTableBuscador().getSelectedRows()) {
                    ReciboDetalle detalle = getDetalle(selectedRow);
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
