package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
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
import sgd.jpa.controller.ContableJPAController;
import sgd.jpa.controller.CronicoJpaController;
import sgd.jpa.controller.DiscapacidadJpaController;
import sgd.jpa.controller.FacturacionJpaController;
import sgd.jpa.controller.GremialesJpaController;
import sgd.jpa.controller.PsicofisicoJPAController;
import sgd.jpa.controller.RecepcionJPAController;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author Administrador
 */
public class RecepcionController implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private JDBuscador buscador;
    private Recepcion entity;
    private final RecepcionJPAController jpaController;
    private SectorUI sectorUI;
    private ABMRecepcionPanel abmPanel;
    private BuscadorReciboPanel buscadorPanel;

    public RecepcionController() {
        jpaController = new RecepcionJPAController();

    }

    CustomABMJDialog getAbm(Window owner, SectorUI sector) {
        this.sectorUI = sector;
        abmPanel = new ABMRecepcionPanel();
        UTIL.hideColumnTable(abmPanel.getjTableBuscador(), 0);
        UTIL.hideColumnTable(abmPanel.getjTableDetalle(), 0);
        ArrayList<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, null, sectorUI);
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        SGDUtilities.setCargadorDeArchivosParaRecepcionListener(abmPanel);
        abmPanel.getBtnAgregar().addActionListener(this);
        abmPanel.getBtnQuitar().addActionListener(this);
        abmPanel.getBtnVerArchivo().addActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "Recepciones - " + sectorUI, true, null);
        customABMJDialog.setMessageText(SGD.getResources().getString("recepcion.ui.message"));
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
                        System.out.println(entity.toString());
                        persistEntity();
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
                    entity = new Recepcion();
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
                    List<Recepcion> list = jpaController.findByQuery(jpql);
                    cargarTablaBuscador(list);
                }
            }
        }
    }

    private void setEntity() throws MessageException {
        if (entity.getDetalle() == null || entity.getDetalle().isEmpty()) {
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
        for (RecepcionDetalle recepcionDetalle : entity.getDetalle()) {
            if (sectorUI.equals(SectorUI.AFILIACION)) {
                new AfiliacionController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.APE)) {
                new ApeController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.CONTABLE)) {
                new ContableController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.PSICOFISICO)) {
                new PsicofisicoController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.FACTURACION)) {
                new FacturacionController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.AUDITORIA)) {
                new AuditoriaController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.GREMIALES)) {
                new GremialesController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.CRONICO)) {
                new CronicoController().recepcionar(recepcionDetalle.getArchivoId());
            } else if (sectorUI.equals(SectorUI.DISCAPACIDAD)) {
                new DiscapacidadController().recepcionar(recepcionDetalle.getArchivoId());
            } else {
                throw new IllegalArgumentException("sectorUI no válido: " + sectorUI);
            }
        }
    }

    private void checkConstraints(RecepcionDetalle toAdd) throws MessageException {
        for (RecepcionDetalle reciboDetalle : entity.getDetalle()) {
            if (reciboDetalle.getArchivoId() == toAdd.getArchivoId()) {
                throw new MessageException("Ya se ha agregado el Archivo código: " + toAdd.getBarcode());
            }
        }
    }

    private void cargarTablaDetalle(RecepcionDetalle detalle, String precintos) {
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
            RecepcionDetalle candidate = (RecepcionDetalle) dtm.getValueAt(selectedRow, 0);
            RecepcionDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                RecepcionDetalle ad = entity.getDetalle().get(i);
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

    @Deprecated
    private void doReport(Recepcion entity) throws MissingReportException, JRException {
//        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.recibido"), "Recibo de Envío N " + entity.getNumero());
//        r.addParameter("RECIBO_ID", entity.getId());
//        r.addParameter("TABLA", sectorUI.getNombre());
//        r.printReport(true);
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
                    entity = (Recepcion) UTIL.getSelectedValue(buscador.getjTable1(), 0);
                    buscador.setEligio(true);
                    buscador.dispose();
                }
            }
        });
        buscador.addListener(this);
        buscador.setLocationRelativeTo(customABMJDialog);
        buscador.setVisible(true);
    }

    private void setPanelABM(Recepcion entity) {
        abmPanel.getTfNumero().setText(entity.getNumero().toString());
        for (RecepcionDetalle detalle : entity.getDetalle()) {
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
        StringBuilder sb = new StringBuilder("SELECT o FROM " + Recepcion.class.getSimpleName() + " o "
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

    private void cargarTablaBuscador(List<Recepcion> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (Recepcion o : list) {
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
                    RecepcionDetalle detalle = new RecepcionDetalle(entity.getNextOrderIndex(), o.getId(), o.getBarcode(), entity);
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
