package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMContablePanel;
import sgd.gui.panel.BuscadorContablePanel;
import sgd.jpa.controller.ContableJPAController;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class ContableController extends ArchivoController<Contable> implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private ABMContablePanel abmPanel;
    private BuscadorContablePanel buscadorPanel;
    private Contable entity;
    private final ContableJPAController jPAController;
    private static final Logger LOG = Logger.getLogger(ContableController.class.getName());
    public static final SectorUI sectorUI = SectorUI.CONTABLE;

    public ContableController() {
        jPAController = new ContableJPAController();
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new ABMContablePanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0); // --> entity..
        ArrayList<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        List<ComboBoxWrapper<TipoDocumento>> ltd = new ArrayList<>();
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, ltd, sectorUI);
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        TipoDocumentoComboListener tipoDocumentoComboListener = new TipoDocumentoComboListener(abmPanel.getCbTipoDocumento(), abmPanel.getCbSubTipoDocumento());
        UTIL.loadComboBox(abmPanel.getCbTipoDocumento(), ltd, false, "<Sin Tipo de Documento>");
        abmPanel.getCbInstitucion().setSelectedIndex(0);
        abmPanel.setButtonsActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM " + sectorUI, true, null);
        customABMJDialog.setPanelComponentsEnabled(false);
        customABMJDialog.addBottomButtonsActionListener(this);
        customABMJDialog.addToolBarButtonsActionListener(this);
        customABMJDialog.setEnabledBottomButtons(false);
        return customABMJDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (customABMJDialog != null && customABMJDialog.isActive()) {
            if (e.getSource() instanceof JButton) {
                if (e.getSource().equals(customABMJDialog.getBtnAceptar())) {
                    crearArchivo(false);
                } else if (e.getSource().equals(customABMJDialog.getBtnExtraBottom())) {
                    crearArchivo(true);
                } else if (e.getSource().equals(customABMJDialog.getBtnCancelar())) {
                    customABMJDialog.setPanelComponentsEnabled(false);
                    abmPanel.resetUI(true);
                    entity = null;
                } else if (e.getSource().equals(customABMJDialog.getBtnNuevo())) {
                    btnNuevoAction();
                } else if (e.getSource().equals(customABMJDialog.getBtnEditar())) {
                    try {
                        if (entity == null || entity.getId() == null) {
                            throw new MessageException("No a seleccionado ninguna caja"
                                    + "\nUtilice el buscador para seleccionar la que desea modificar.");
                        }
                        if (entity.getRecibo() != null) {
                            throw new MessageException(SGD.getResources().getString("recibonotnull").replaceAll("<Sector>", sectorUI.toString()));
                        }
                        if (!entity.getPrecintos().isEmpty()) {
                            if (SGDUtilities.confirmarReAperturaDeArchivo()) {
                                removePrecintos(entity);
                            } else {
                                return;
                            }
                        }
                        customABMJDialog.setPanelComponentsEnabled(true);
                        customABMJDialog.setEnabledBottomButtons(true);
                        abmPanel.getCbInstitucion().setEnabled(false);
                    } catch (MessageException ex) {
                        ex.displayMessage(customABMJDialog);
                    }
                } else if (e.getSource().equals(customABMJDialog.getBtnBuscar())) {
                    initBuscador();
                    if (buscador.isEligio()) {
                        customABMJDialog.setPanelComponentsEnabled(false);
                        customABMJDialog.setEnabledBottomButtons(false);
                        setPanelABM(entity);
                    }
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    customABMJDialog.showMessage("¡No implementado aún!", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (e.getSource().equals(abmPanel.getBtnAgregar())) {
                    try {
                        ContableDetalle detalle = getDetalle();
                        checkConstraints(detalle);
                        entity.getDetalle().add(detalle);
                        cargarTablaDetalle(detalle);
                        abmPanel.resetUI(false);
                        abmPanel.getCbTipoDocumento().requestFocusInWindow();
                    } catch (MessageException ex) {
                        customABMJDialog.showMessage(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (e.getSource().equals(abmPanel.getBtnQuitar())) {
                    borrarDetalle();
                } else if (e.getSource().equals(abmPanel.getBtnPrecintos())) {
                    if (!entity.getPrecintos().isEmpty()) {
                        SGDUtilities.initPrecintosUI(customABMJDialog, entity.getPrecintos());
                    } else {
                        customABMJDialog.showMessage(SGD.getResources().getString("unclosedEntityPrecintos"), "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } else if (buscador != null && buscador.isActive()) {
            if (e.getSource() instanceof JButton) {
                if (e.getSource().equals(buscador.getbBuscar())) {
                    String query = getBuscadorQuery();
                    List<ContableDetalle> list = jPAController.findDetalleByQuery(query);
                    if (list.isEmpty()) {
                        JOptionPane.showMessageDialog(buscador, "La busqueda no produjo ningún resultado");
                        return;
                    }
                    cargarTablaBuscador(list);
                }
            }
        }
    }

    private void setEntity(boolean cerrarYPrecintar) throws MessageException {
        if (entity.getDetalle() == null || entity.getDetalle().isEmpty()) {
            throw new MessageException("Debe contener al menos un detalle"
                    + " (No vas a mandar la caja vacía..)");
        }
        if (cerrarYPrecintar) {
            List<String> codigoPrecintos = SGDUtilities.initPrecintosUI(customABMJDialog);
            if (codigoPrecintos.isEmpty()) {
                throw new MessageException(SGD.getResources().getString("cargaprecintoscancelada"));
            }
            for (String codigo : codigoPrecintos) {
                entity.getPrecintos().add(new ContablePrecinto(codigo, entity));
            }
        }
        if (entity.getId() == null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
            entity.setInstitucion(cb.getEntity().getInstitucion());
            entity.setSector(cb.getEntity().getSector());
            entity.setUsuario(UsuarioController.getCurrentUser());
        }
    }

    private void persistEntity() {
        if (entity.getId() == null) {
            jPAController.create(entity);
        } else {
            jPAController.merge(entity);
        }
    }

    private ContableDetalle getDetalle() throws MessageException {
        Map<String, Object> data = abmPanel.getData();
        TipoDocumento td;
        SubTipoDocumento std;
        Date documentoFecha;
        String observacion = null;

        try {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<TipoDocumento> cb = (ComboBoxWrapper<TipoDocumento>) data.get("td");
            td = cb.getEntity();
        } catch (Exception e) {
            throw new MessageException("Tipo de Documento no válido");
        }
        try {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<SubTipoDocumento> cbs = (ComboBoxWrapper<SubTipoDocumento>) data.get("std");
            std = cbs.getEntity();
        } catch (ClassCastException e) {
            std = null;
        }

        documentoFecha = (Date) data.get("documentoFecha");
        if (documentoFecha == null) {
            throw new MessageException("Fecha de documento no válida");
        }
        if (!data.get("observacion").toString().isEmpty()) {
            observacion = (String) data.get("observacion");
        }
        ContableDetalle detalle = new ContableDetalle(entity.getNextOrderIndex(), td, std, documentoFecha, observacion, entity);
        return detalle;
    }

    private void checkConstraints(ContableDetalle toAdd) throws MessageException {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (int row = 0; row < dtm.getRowCount(); row++) {
            ContableDetalle old = (ContableDetalle) dtm.getValueAt(row, 0);
            if (old.getTipoDocumento().equals(toAdd.getTipoDocumento())) {
                if (Objects.equals(old.getSubTipoDocumento(), toAdd.getSubTipoDocumento())) {
                    if (Objects.equals(old.getDocumentoFecha(), toAdd.getDocumentoFecha())) {
                        throw new MessageException("Ya existe un detalle con los mismos datos:"
                                + "\nTipo de Documento: " + old.getTipoDocumento().getNombre()
                                + "\nSub-Tipo de Documento: " + old.getSubTipoDocumento() == null
                                        ? "<Sin Sub-Tipo>" : old.getSubTipoDocumento().getNombre()
                                        + "\nFecha Documento: " + (old.getDocumentoFecha() == null ? "" : UTIL.DATE_FORMAT.format(old.getDocumentoFecha())));
                    }
                }
            }
        }
    }

    private void cargarTablaDetalle(ContableDetalle detalle) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getTipoDocumento().getNombre(),
            detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
            UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()).substring(3), //no se muetra el dia
            detalle.getObservacion()
        });
    }

    private void borrarDetalle() {
        int[] selectedRows = abmPanel.getjTable1().getSelectedRows();
        for (int arrayIndex = selectedRows.length - 1; arrayIndex >= 0; arrayIndex--) {
            int selectedRow = selectedRows[arrayIndex];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
            ContableDetalle candidate = (ContableDetalle) dtm.getValueAt(selectedRow, 0);
            ContableDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                ContableDetalle ad = entity.getDetalle().get(i);
                if (candidate.getId() != null && candidate.getId().equals(ad.getId())) {
                    removed = entity.getDetalle().remove(i);
                } else if (candidate.getId() == null && ad.getId() == null
                        && candidate.getOrderIndex().equals(ad.getOrderIndex())) {
                    removed = entity.getDetalle().remove(i);
                }
            }
            LOG.debug("borrando.. " + selectedRow + ", " + removed);
            dtm.removeRow(selectedRow);
        }
    }

    private void initBuscador() {
        buscadorPanel = new BuscadorContablePanel();
        SGDUtilities.volcar(abmPanel.getCbInstitucion(), buscadorPanel.getCbInstitucion(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbTipoDocumento(), buscadorPanel.getCbTipoDocumento(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbSubTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), "<Elegir>");
        buscadorPanel.getCbInstitucion().setSelectedIndex(0);
        buscadorPanel.getCbTipoDocumento().setSelectedIndex(0);
        buscadorPanel.getCbSubTipoDocumento().setSelectedIndex(0);
        new TipoDocumentoComboListener(buscadorPanel.getCbTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), true);
        buscador = new JDBuscador(customABMJDialog, true, buscadorPanel, "Buscador de Contables");
        buscador.getbImprimir().setEnabled(false);
        buscador.getbLimpiar().setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) abmPanel.getjTable1().getModel();
        List<String> columNames = new ArrayList<>(model.getColumnCount());
        for (int i = 0; i < model.getColumnCount(); i++) {
            columNames.add(model.getColumnName(i));
        }
        columNames.add("Código");
        columNames.add("Precintada");
        columNames.add("Enviada");
        UTIL.getDefaultTableModel(buscador.getjTable1(), columNames.toArray(new String[columNames.size()]));
        UTIL.hideColumnTable(buscador.getjTable1(), 0); // --> entity instance
        buscador.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    entity = (Contable) UTIL.getSelectedValue(buscador.getjTable1(), 0);
                    buscador.setEligio(true);
                    buscador.dispose();
                }
            }
        });
        buscador.addListener(this);
        buscador.setLocationRelativeTo(customABMJDialog);
        buscador.setVisible(true);
    }

    private String getBuscadorQuery() {
        StringBuilder sb = new StringBuilder("SELECT o FROM " + ContableDetalle.class.getSimpleName() + " o "
                + " WHERE ");
        Map<String, Object> data = buscadorPanel.getData();
        sb.append(" o.contable.baja = ").append(data.get("baja"));
        if (data.get("i") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) data.get("i");
            sb.append(" AND o.contable.institucion.id = ").append(cb.getEntity().getInstitucion().getId());
        } else {
            List<Institucion> institucionesList = new UsuarioSectorJPAController().findInstituciones(UsuarioController.getCurrentUser());
            Iterator<Institucion> it = institucionesList.iterator();
            sb.append(" AND (");
            while (it.hasNext()) {
                Institucion institucion = it.next();
                sb.append(" o.contable.institucion.id = ").append(institucion.getId());
                if (it.hasNext()) {
                    sb.append(" OR ");
                }
            }
            sb.append(") ");
        }
        if (data.get("td") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<TipoDocumento> cb = (ComboBoxWrapper<TipoDocumento>) data.get("td");
            sb.append(" AND o.tipoDocumento.id = ").append(cb.getEntity().getId());
        }
        if (data.get("std") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<SubTipoDocumento> cb = (ComboBoxWrapper<SubTipoDocumento>) data.get("std");
            sb.append(" AND o.subTipoDocumento.id = ").append(cb.getEntity().getId());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (data.get("desde") != null && data.get("hasta") != null) {
            Date date = (Date) data.get("desde");
            Date hasta = (Date) data.get("hasta");
            sb.append(" AND o.documentoFecha BETWEEN '").append(sdf.format(date)).
                    append("'" + " AND '").append(sdf.format(hasta)).append("'");
        } else {
            if (data.get("desde") != null) {
                Date date = (Date) data.get("desde");
                sb.append(" AND o.documentoFecha >='").append(sdf.format(date)).append("'");
            }
            if (data.get("hasta") != null) {
                Date date = (Date) data.get("hasta");
                sb.append(" AND o.documentoFecha <='").append(sdf.format(date)).append("'");
            }
        }
        if (!data.get("caja").toString().isEmpty()) {
            sb.append(" AND o.contable.codigo=").append(data.get("caja"));
        }
        LOG.trace("JPQL: " + sb.toString());
        return sb.toString();
    }

    private void cargarTablaBuscador(List<ContableDetalle> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (ContableDetalle detalle : list) {
            dtm.addRow(new Object[]{
                detalle.getContable(),
                detalle.getTipoDocumento().getNombre(),
                detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
                UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()).substring(3), //no se muestra el dia
                detalle.getObservacion(),
                detalle.getContable().getBarcode(),
                detalle.getContable().getPrecintos().isEmpty() ? "No" : "Si " + detalle.getContable().getPrecintos().size(),
                detalle.getContable().getRecibo() != null ? detalle.getContable().getRecibo().getNumero() : null
            });
        }
    }

    private void setPanelABM(Contable o) {
        UTIL.setSelectedItem(abmPanel.getCbInstitucion(), o.getInstitucion().getNombre());
        abmPanel.getBtnPrecintos().setEnabled(!o.getPrecintos().isEmpty());
        abmPanel.setBarcode(SGDUtilities.getBarcode(o));
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        LOG.trace(o.toString());
        LOG.debug(o.getDetalle().size());
        for (ContableDetalle afiliacionDetalle : o.getDetalle()) {
            cargarTablaDetalle(afiliacionDetalle);
        }
    }

    private void doReport(Contable o) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.codigobarra"), "Archivo " + o.getClass().getSimpleName() + " N" + o.getBarcode());
        r.addParameter("TABLA", o.getClass().getSimpleName());
        r.addParameter("ID_TABLA", o.getId());
        r.viewReport();
    }

    private void removePrecintos(Contable entity) {
        jPAController.removePrecintos(entity);
        entity.setPrecintos(null);
    }

    CustomABMJDialog viewArchivo(Contable instance) {
        abmPanel = new ABMContablePanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        UTIL.hideColumnTable(abmPanel.getjTable1(), abmPanel.getjTable1().getColumnCount() - 1);
        abmPanel.getCbInstitucion().addItem(instance.getInstitucion().getNombre());
        setPanelABM(instance);
        CustomABMJDialog customABMJDial = new CustomABMJDialog(null, abmPanel, "Archivo " + instance.getClass().getSimpleName() + " " + instance.getBarcode(), true, null);
        customABMJDial.setToolBarVisible(false);
        customABMJDial.setBottomButtonsVisible(false);
        customABMJDial.setPanelComponentsEnabled(false);
        return customABMJDial;
    }

    private void crearArchivo(boolean cerrarYPrecintar) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Seleccione \"Nuevo\" para comenzar un nuevo Archivo (Caja) o \"Modificar\" para continuar con uno existente.");
            }
            boolean nuevo = entity.getId() == null;
            setEntity(cerrarYPrecintar);
            persistEntity();
            customABMJDialog.showMessage(nuevo ? "Registrado.." : "Modificado..", null, 1);
            if (nuevo || SGDUtilities.reImprimirCodigoBarraArchivo()) {
                doReport(entity);
            }
            customABMJDialog.setPanelComponentsEnabled(false);
            abmPanel.resetUI(true);
            entity = null;
            customABMJDialog.getBtnNuevo().requestFocusInWindow();
        } catch (IllegalArgumentException ex) {
            customABMJDialog.showMessage(ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
        } catch (MessageException ex) {
            customABMJDialog.showMessage(ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            customABMJDialog.showMessage(ex.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(this.getClass()).error(ex.getLocalizedMessage(), ex);
        }
    }

    private void btnNuevoAction() {
        entity = new Contable();
        customABMJDialog.setPanelComponentsEnabled(true);
        customABMJDialog.setEnabledBottomButtons(true);
        abmPanel.resetUI(true);
        abmPanel.getCbTipoDocumento().setSelectedIndex(0);
        abmPanel.getCbInstitucion().requestFocusInWindow();
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
        UsuarioSector us = cbw.getEntity();
        abmPanel.setBarcode(us.getInstitucion().getId() + "-" + us.getSector().getSectorUI().getCode() + "-xxxxxx");
    }

    void enviado(Integer archivoId, Recibo recibo) {
        Contable o = jPAController.find(archivoId);
        o.setRecibo(recibo);
        jPAController.merge(o);
    }

    void recepcionar(Integer archivoId) {
        Contable o = jPAController.find(archivoId);
        o.setRecibo(null);
        jPAController.merge(o);
    }

    Contable find(Integer archivoId) {
        return jPAController.find(archivoId);
    }
}
