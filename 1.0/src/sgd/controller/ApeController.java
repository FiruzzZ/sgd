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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMAPEProtesisPanel;
import sgd.gui.panel.BuscadorApePanel;
import sgd.jpa.controller.ApeJPAController;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class ApeController implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private JDBuscador buscador;
    private ABMAPEProtesisPanel abmPanel;
    private Ape entity;
    private BuscadorApePanel buscadorPanel;
    private final ApeJPAController jPAController;
    private static final Logger LOG = Logger.getLogger(ContableController.class.getName());
    public static final SectorUI sectorUI = SectorUI.APE;

    public ApeController() {
        jPAController = new ApeJPAController();
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new ABMAPEProtesisPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0); // --> AfiliacionDetalle instance
        ArrayList<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>();
        List<ComboBoxWrapper<TipoDocumento>> ltd = new ArrayList<>();
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, ltd, sectorUI);
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        UTIL.loadComboBox(abmPanel.getCbTipoDocumento(), ltd, false, "<Sin Tipo de Documento>");
        UTIL.loadComboBox(abmPanel.getCbSubTipoDocumento(), null, false, "<Sin Sub-Tipo de Documento>");
        TipoDocumentoComboListener tipoDocumentoComboListener = new TipoDocumentoComboListener(abmPanel.getCbTipoDocumento(), abmPanel.getCbSubTipoDocumento());
        abmPanel.addButtonActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM " + sectorUI, true, null);
        customABMJDialog.setPanelComponentsEnabled(false);
        customABMJDialog.addBottomButtonsActionListener(this);
        customABMJDialog.addToolBarButtonsActionListener(this);
        return customABMJDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
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
                                throw new MessageException(SGD.getResources().getString("recibonotnull")
                                        .replaceAll("<Recibo.id>", entity.getRecibo().getNumero().toString())
                                        .replaceAll("<Sector>", sectorUI.toString()));
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
                            setPanelABM(entity);
                        } catch (MessageException ex) {
                            ex.displayMessage(customABMJDialog);
                        }
                    } else if (e.getSource().equals(customABMJDialog.getBtnBuscar())) {
                        initBuscador();
                        if (buscador.isEligio()) {
                            customABMJDialog.setEnabledBottomButtons(false);
                            setPanelABM(entity);
                        }
                    } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                        customABMJDialog.showMessage("¡No implementado aún!", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    } else if (e.getSource().equals(abmPanel.getBtnAgregar())) {
                        try {
                            ApeDetalle detalle = getDetalle();
                            checkConstraints(detalle);
                            entity.getDetalle().add(detalle);
                            cargarTablaDetalle(detalle);
                            abmPanel.resetUI(false);
                            abmPanel.getCbTipoDocumento().requestFocusInWindow();
                        } catch (MessageException ex) {
                            customABMJDialog.showMessage(ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
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
                if (e.getSource().getClass().equals(JButton.class)) {
                    if (e.getSource().equals(buscador.getbBuscar())) {
                        String query = getBuscadorQuery();
                        List<ApeDetalle> list = jPAController.findDetalleByQuery(query);
                        if (list.isEmpty()) {
                            JOptionPane.showMessageDialog(buscador, "La busqueda no produjo ningún resultado");
                            return;
                        }
                        cargarTablaBuscador(list);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Global Catch:", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                entity.getPrecintos().add(new ApePrecinto(codigo, entity));
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

    private ApeDetalle getDetalle() throws MessageException {
        Map<String, Object> data = abmPanel.getData();
        TipoDocumento td;
        SubTipoDocumento std;
        Long numeroAfiliado;
        Date documentoFecha;
        String apellido = null, nombre = null, observacion = null;

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

        try {
            numeroAfiliado = Long.valueOf(data.get("afiliado").toString());
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de afiliado no válido (ingrese solo números)");
        }
        documentoFecha = (Date) data.get("documentoFecha");

        if (!data.get("apellido").toString().isEmpty()) {
            apellido = data.get("apellido").toString().toUpperCase();
        }
        if (!data.get("nombre").toString().isEmpty()) {
            nombre = data.get("nombre").toString().toUpperCase();
        }
        if (!data.get("observacion").toString().isEmpty()) {
            observacion = (String) data.get("observacion");
            observacion.toUpperCase();
        }
        ApeDetalle detalle = new ApeDetalle(entity.getNextOrderIndex(), td, std, numeroAfiliado, documentoFecha, observacion, entity, apellido, nombre);
        return detalle;
    }

    private void checkConstraints(ApeDetalle toAdd) throws MessageException {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (int row = 0; row < dtm.getRowCount(); row++) {
            ApeDetalle old = (ApeDetalle) dtm.getValueAt(row, 0);
            if (old.getTipoDocumento().equals(toAdd.getTipoDocumento())) {
                if (Objects.equals(toAdd.getSubTipoDocumento(), old.getSubTipoDocumento())) {
                    if (Objects.equals(old.getNumeroAfiliado(), toAdd.getNumeroAfiliado())
                            && Objects.equals(old.getDocumentoFecha(), toAdd.getDocumentoFecha())
                            && UTIL.compararIgnorandoTimeFields(old.getDocumentoFecha(), toAdd.getDocumentoFecha()) == 0) {
                        throw new MessageException("Ya existe un detalle con los mismos datos:"
                                + "\nTipo de Documento: " + old.getTipoDocumento().getNombre()
                                + (old.getSubTipoDocumento() == null ? "" : "\nSub-Tipo de Documento: " + old.getSubTipoDocumento().getNombre())
                                + "\nN° Afiliado: " + old.getNumeroAfiliado()
                                + "\nFecha de Doc.: " + (old.getDocumentoFecha() == null ? "" : UTIL.DATE_FORMAT.format(old.getDocumentoFecha()))
                        );
                    }
                }
            }
        }
    }

    private void cargarTablaDetalle(ApeDetalle detalle) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getTipoDocumento().getNombre(),
            detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
            detalle.getNumeroAfiliado(),
            (detalle.getApellido() + ", " + detalle.getNombre()),
            detalle.getDocumentoFecha() == null ? null : UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
            detalle.getObservacion()
        });
    }

    private void borrarDetalle() {
        int[] selectedRows = abmPanel.getjTable1().getSelectedRows();
        for (int arrayIndex = selectedRows.length - 1; arrayIndex >= 0; arrayIndex--) {
            int selectedRow = selectedRows[arrayIndex];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
            ApeDetalle candidate = (ApeDetalle) dtm.getValueAt(selectedRow, 0);
            ApeDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                ApeDetalle ad = entity.getDetalle().get(i);
                if (candidate.getId() != null && candidate.equals(ad)) {
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
        buscadorPanel = new BuscadorApePanel();
        SGDUtilities.volcar(abmPanel.getCbInstitucion(), buscadorPanel.getCbInstitucion(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbTipoDocumento(), buscadorPanel.getCbTipoDocumento(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbSubTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), "<Elegir>");
        buscadorPanel.getCbInstitucion().setSelectedIndex(0);
        buscadorPanel.getCbTipoDocumento().setSelectedIndex(0);
        buscadorPanel.getCbSubTipoDocumento().setSelectedIndex(0);
        new TipoDocumentoComboListener(buscadorPanel.getCbTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), true);
        buscador = new JDBuscador(customABMJDialog, true, buscadorPanel, "Buscador " + sectorUI);
        buscador.getbLimpiar().setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) abmPanel.getjTable1().getModel();
        List<String> columNames = new ArrayList<>(model.getRowCount());
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
                    entity = (Ape) UTIL.getSelectedValue(buscador.getjTable1(), 0);
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
        StringBuilder sb = new StringBuilder("SELECT o FROM " + ApeDetalle.class.getSimpleName() + " o"
                + " WHERE ");
        Map<String, Object> data = buscadorPanel.getData();
        sb.append(" o.ape.baja = ").append(data.get("baja"));
        if (data.get("i") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) data.get("i");
            sb.append(" AND o.ape.institucion.id = ").append(cb.getEntity().getInstitucion().getId());
        } else {
            List<Institucion> institucionesList = new UsuarioSectorJPAController().findInstituciones(UsuarioController.getCurrentUser());
            Iterator<Institucion> it = institucionesList.iterator();
            sb.append(" AND (");
            while (it.hasNext()) {
                Institucion institucion = it.next();
                sb.append(" o.ape.institucion.id = ").append(institucion.getId());
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
            sb.append(" AND d.subTipoDocumento.id = ").append(cb.getEntity().getId());
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
        if (data.get("apenom").toString().length() > 0) {
            String[] split = data.get("apenom").toString().toUpperCase().split(" ");
            sb.append(" AND ( UPPER(CONCAT(o.apellido, o.nombre)) like '%").append(split[0]).append("%'");
            if (split.length > 1) {
                split = Arrays.copyOfRange(split, 1, split.length);
                for (String c : split) {
                    sb.append(" AND (UPPER(CONCAT(o.apellido, o.nombre)) like '%").append(c).append("%') ");
                }
            }
            sb.append(")");
        }
        if (data.get("caja").toString().length() > 0) {
            sb.append(" AND o.ape.codigo =").append(data.get("caja"));
        }
        if (!data.get("observacion").toString().isEmpty()) {
            sb.append(" AND o.observacion like '%").append(data.get("observacion")).append("%'");
        }
        if (data.get("afiliado").toString().length() > 0) {
            Long nf = Long.valueOf(data.get("afiliado").toString());
            sb.append(" AND o.numeroAfiliado = ").append(nf);
        }
        LOG.trace("JPQL: " + sb.toString());
        return sb.toString();
    }

    private void cargarTablaBuscador(List<ApeDetalle> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (ApeDetalle detalle : list) {
            dtm.addRow(new Object[]{
                detalle.getApe(),
                detalle.getTipoDocumento().getNombre(),
                detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
                detalle.getNumeroAfiliado(),
                detalle.getDocumentoFecha() == null ? null : UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
                (detalle.getApellido() == null ? "" : detalle.getApellido()) + ", " + (detalle.getNombre() == null ? "" : detalle.getNombre()),
                detalle.getObservacion(),
                detalle.getApe().getBarcode(),
                detalle.getApe().getPrecintos().isEmpty() ? "No" : "Si " + detalle.getApe().getPrecintos().size(),
                detalle.getApe().getRecibo() != null ? detalle.getApe().getRecibo().getNumero() : null
            });
        }
    }

    private void setPanelABM(Ape o) {
        UTIL.setSelectedItem(abmPanel.getCbInstitucion(), o.getInstitucion().getNombre());
        abmPanel.getBtnPrecintos().setEnabled(!entity.getPrecintos().isEmpty());
        abmPanel.setBarcode(SGDUtilities.getBarcode(o));
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        for (ApeDetalle detalle : o.getDetalle()) {
            cargarTablaDetalle(detalle);
        }
        abmPanel.getBtnPrecintos().setEnabled(!o.getPrecintos().isEmpty());
    }

    private void doReport(Ape o) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.codigobarra"), "Archivo " + o.getClass().getSimpleName() + " N" + o.getBarcode());
        r.addParameter("TABLA", o.getClass().getSimpleName());
        r.addParameter("ID_TABLA", o.getId());
       r.viewReport();
    }

    CustomABMJDialog getAbmRecibos(JFrame owner) {
        return new ReciboController().getAbm(owner, sectorUI);
    }

    private void removePrecintos(Ape entity) {
        jPAController.removePrecintos(entity);
        entity.setPrecintos(null);
    }

    JDialog viewArchivo(Ape o) {
        abmPanel = new ABMAPEProtesisPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        abmPanel.getCbInstitucion().addItem(o.getInstitucion().getNombre());
        setPanelABM(o);
        CustomABMJDialog ccustomABMJDialog = new CustomABMJDialog((JDialog) null, abmPanel, "Archivo " + o.getClass().getSimpleName() + " " + o.getBarcode(), true, null);
        ccustomABMJDialog.setToolBarVisible(false);
        ccustomABMJDialog.setBottomButtonsVisible(false);
        ccustomABMJDialog.setPanelComponentsEnabled(false);
        return ccustomABMJDialog;
    }

    private void crearArchivo(boolean cerrarYPrecintar) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Seleccione \"Nuevo\" para crear un Archivo nuevo o \"Editar\" para uno existente.");
            }
            String msj = entity.getId() == null ? "Registrado.." : "Modificado..";
            if (!entity.getPrecintos().isEmpty()) {
                msj += " (Re apertura)";
            }
            boolean nuevo = entity.getId() == null;
            setEntity(cerrarYPrecintar);
            persistEntity();

            customABMJDialog.showMessage(msj, null, 1);
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
        entity = new Ape();
        customABMJDialog.setPanelComponentsEnabled(true);
        abmPanel.resetUI(true);
        abmPanel.getCbTipoDocumento().setSelectedIndex(0);
        abmPanel.getCbInstitucion().requestFocusInWindow();
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
        UsuarioSector us = cbw.getEntity();
        abmPanel.setBarcode(us.getInstitucion().getId() + "-" + us.getSector().getSectorUI().getCode() + "-xxxxxx");
    }

    void enviado(Integer archivoId, Recibo recibo) {
        Ape o = jPAController.find(archivoId);
        o.setRecibo(recibo);
        jPAController.merge(o);
    }

    void recepcionar(Integer archivoId) {
        Ape o = jPAController.find(archivoId);
        o.setRecibo(null);
        jPAController.merge(o);
    }
}
