package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import sgd.gui.panel.ABMPsicofisicoPanel;
import sgd.gui.panel.BuscadorPsicofisicoPanel;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.PsicofisicoJPAController;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class PsicofisicoController implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private JDBuscador buscador;
    private ABMPsicofisicoPanel abmPanel;
    private BuscadorPsicofisicoPanel buscadorPanel;
    private Psicofisico entity;
    private final PsicofisicoJPAController jpaController;
    private static final Logger LOG = Logger.getLogger(PsicofisicoController.class.getName());
    public static final SectorUI sectorUI = SectorUI.PSICOFISICO;

    public PsicofisicoController() {
        jpaController = new PsicofisicoJPAController();
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new ABMPsicofisicoPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0); // --> PsicofisicoDetalle instance
        ArrayList<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        List<ComboBoxWrapper<TipoDocumento>> ltd = new ArrayList<>();
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, ltd, sectorUI);
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        new TipoDocumentoComboListener(abmPanel.getCbTipoDocumento(), abmPanel.getCbSubTipoDocumento());
        UTIL.loadComboBox(abmPanel.getCbTipoDocumento(), ltd, false, "<Sin Tipo de Documento>");
        abmPanel.getCbInstitucion().setSelectedIndex(0);
        abmPanel.addButtonsActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM " + sectorUI, true, null);
        customABMJDialog.setPanelComponentsEnabled(false);
        customABMJDialog.addBottomButtonsActionListener(this);
        customABMJDialog.addToolBarButtonsActionListener(this);
        return customABMJDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (customABMJDialog != null && customABMJDialog.isActive()) {

            if (e.getSource().getClass().equals(JButton.class)) {
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
                            throw new MessageException(SGD.getResources().getString("recibonotnull").replaceAll("<Sector>",
                                    sectorUI.toString()));
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
                        PsicofisicoDetalle detalle = getDetalle();
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
                    if (entity != null) {
                        if (!entity.getPrecintos().isEmpty()) {
                            SGDUtilities.initPrecintosUI(customABMJDialog, entity.getPrecintos());
                        } else {
                            customABMJDialog.showMessage(SGD.getResources().getString("unclosedEntityPrecintos"), "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        } else if (buscador != null && buscador.isActive()) {
            if (e.getSource().equals(buscador.getbBuscar())) {
                String jpql = armarQuery();
                List<PsicofisicoDetalle> list = jpaController.findDetalleByJPQL(jpql);
                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(buscador, "La busqueda no produjo ningún resultado");
                    return;
                }
                cargarTablaBuscador(list);
            }
        }
    }

    private void setEntity(boolean cerrarYPrecintar) throws MessageException {
        if (entity.getDetalle() == null || entity.getDetalle().isEmpty()) {
            throw new MessageException("Debe contener al menos un detalle (No vas a mandar la caja vacía..)");
        }
        if (cerrarYPrecintar) {
            List<String> codigoPrecintos = SGDUtilities.initPrecintosUI(customABMJDialog);
            if (codigoPrecintos.isEmpty()) {
                throw new MessageException(SGD.getResources().getString("cargaprecintoscancelada"));
            }
            for (String codigo : codigoPrecintos) {
                entity.getPrecintos().add(new PsicofisicoPrecinto(codigo, entity));
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
            jpaController.create(entity);
        } else {
            jpaController.merge(entity);
        }
    }

    private PsicofisicoDetalle getDetalle() throws MessageException {
        Map<String, Object> data = abmPanel.getData();
        TipoDocumento td;
        SubTipoDocumento std;
        Long numeroDocumento;
        Integer numeroCarpeta;
        String apellido;
        String nombre;
        Date documentoFecha;
        String observacion;

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
            numeroDocumento = Long.valueOf(data.get("documentonumero").toString());
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de documento no válido (ingrese solo números)");
        }
        try {
            numeroCarpeta = Integer.valueOf(data.get("carpeta").toString());
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de carpeta no válido (ingrese solo números)");
        }
        apellido = (String) data.get("apellido");
        if (apellido.isEmpty()) {
            apellido = null;
        }
        nombre = (String) data.get("nombre");
        if (nombre.isEmpty()) {
            nombre = null;
        }
        documentoFecha = (Date) data.get("documentofecha");
        observacion = (String) data.get("observacion");
        if (observacion.isEmpty()) {
            observacion = null;
        }
        PsicofisicoDetalle detalle = new PsicofisicoDetalle(entity.getNextOrderIndex(), td, std,
                numeroDocumento, apellido, nombre, documentoFecha, observacion, entity,
                numeroCarpeta);
        return detalle;
    }

    private void checkConstraints(PsicofisicoDetalle toAdd) throws MessageException {
        if (toAdd.getNumeroDocumento() == null) {
            throw new MessageException("Número de documento no válida");
        }
        if (toAdd.getNumeroCarpeta() == null) {
            throw new MessageException("Número de carpeta no válida");
        }
        if (toAdd.getDocumentoFecha() == null) {
            throw new MessageException("Fecha de documento no válida");
        }
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (int row = 0; row < dtm.getRowCount(); row++) {
            PsicofisicoDetalle oldPsicofisicoDetalle = (PsicofisicoDetalle) dtm.getValueAt(row, 0);
            if (oldPsicofisicoDetalle.getTipoDocumento().equals(toAdd.getTipoDocumento())
                    && Objects.equals(oldPsicofisicoDetalle.getSubTipoDocumento(), toAdd.getSubTipoDocumento())
                    && Objects.equals(oldPsicofisicoDetalle.getNumeroDocumento(), toAdd.getNumeroDocumento())
                    && Objects.equals(oldPsicofisicoDetalle.getNumeroCarpeta(), toAdd.getNumeroCarpeta())) {
                throw new MessageException("Ya existe un detalle con los mismos datos:"
                        + "\nTipo de Documento: " + toAdd.getTipoDocumento().getNombre()
                        + "\nSub-Tipo de Documento: "
                        + (toAdd.getSubTipoDocumento() == null ? "<Sin Sub-Tipo>"
                                : toAdd.getSubTipoDocumento().getNombre())
                        + "\nN° Documento: " + toAdd.getNumeroDocumento()
                        + "\nN° Carpeta: " + toAdd.getNumeroCarpeta()
                );
            }
        }
    }

    private void cargarTablaDetalle(PsicofisicoDetalle detalle) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getTipoDocumento().getNombre(),
            detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
            detalle.getNumeroDocumento(),
            UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
            (detalle.getApellido() == null ? "" : detalle.getApellido()) + " " + (detalle.getNombre() == null ? "" : detalle.getNombre()),
            detalle.getNumeroCarpeta(),
            detalle.getObservacion()
        });
    }

    private void borrarDetalle() {
        int[] selectedRows = abmPanel.getjTable1().getSelectedRows();
        for (int ii = 0; ii < selectedRows.length; ii++) {
            int selectedRow = selectedRows[ii];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
            PsicofisicoDetalle candidate = (PsicofisicoDetalle) dtm.getValueAt(selectedRow, 0);
            PsicofisicoDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                PsicofisicoDetalle ad = entity.getDetalle().get(i);
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

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    private void initBuscador() {
        buscadorPanel = new BuscadorPsicofisicoPanel();
        SGDUtilities.volcar(abmPanel.getCbInstitucion(), buscadorPanel.getCbInstitucion(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbTipoDocumento(), buscadorPanel.getCbTipoDocumento(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbSubTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), "<Elegir>");
        buscadorPanel.getCbInstitucion().setSelectedIndex(0);
        buscadorPanel.getCbTipoDocumento().setSelectedIndex(0);
        buscadorPanel.getCbSubTipoDocumento().setSelectedIndex(0);
        new TipoDocumentoComboListener(buscadorPanel.getCbTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), true);
        buscador = new JDBuscador(customABMJDialog, true, buscadorPanel, "Buscador de Psicofisicoes");
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
        UTIL.hideColumnTable(buscador.getjTable1(), 0); // --> PsicofisicoDetalle instance
        buscador.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    entity = (Psicofisico) UTIL.getSelectedValue(buscador.getjTable1(), 0);
                    buscador.setEligio(true);
                    buscador.dispose();
                }
            }
        });

        buscador.addListener(this);
        buscador.setLocationRelativeTo(customABMJDialog);
        buscador.setVisible(true);
    }

    private String armarQuery() {
        StringBuilder sb = new StringBuilder("SELECT o FROM " + PsicofisicoDetalle.class.getSimpleName() + " o "
                + " WHERE o.id is not null ");
        Map<String, Object> data = buscadorPanel.getData();
        sb.append("AND o.psicofisico.baja = ").append(data.get("baja"));
        if (data.get("i") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) data.get("i");
            sb.append(" AND o.psicofisico.institucion.id = ").append(cb.getEntity().getInstitucion().getId());
        } else {
            List<Institucion> institucionesList = new UsuarioSectorJPAController().findInstituciones(UsuarioController.getCurrentUser());
            Iterator<Institucion> it = institucionesList.iterator();
            sb.append(" AND (");
            while (it.hasNext()) {
                Institucion institucion = it.next();
                sb.append(" o.psicofisico.institucion.id = ").append(institucion.getId());
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
        if (data.get("desde") != null) {
            sb.append(" AND o.documentoFecha >='").append(UTIL.yyyy_MM_dd.format((Date) data.get("desde"))).append("'");
        }
        if (data.get("hasta") != null) {
            sb.append(" AND o.documentoFecha <='").append(UTIL.yyyy_MM_dd.format(data.get("hasta"))).append("'");
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
        if (data.get("documentonumero").toString().length() > 0) {
            Long nf = Long.valueOf(data.get("documentonumero").toString());
            sb.append(" AND o.").append(PsicofisicoDetalle_.numeroDocumento.getName()).append(" = ").append(nf);
        }
        if (data.get("carpeta").toString().length() > 0) {
            Integer nf = Integer.valueOf(data.get("carpeta").toString());
            sb.append(" AND o.").append(PsicofisicoDetalle_.numeroCarpeta.getName()).append(" = ").append(nf);
        }
        if (!data.get("caja").toString().isEmpty()) {
            sb.append(" AND o.psicofisico.codigo=").append(data.get("caja"));
        }
        Logger.getLogger(this.getClass()).trace(sb.toString());
        return sb.toString();
    }

    private void cargarTablaBuscador(List<PsicofisicoDetalle> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (PsicofisicoDetalle detalle : list) {
            dtm.addRow(new Object[]{
                detalle.getPsicofisico(),
                detalle.getTipoDocumento().getNombre(),
                detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
                detalle.getNumeroDocumento(),
                UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
                detalle.getApellido() + ", " + detalle.getNombre(),
                detalle.getNumeroCarpeta(),
                detalle.getObservacion(),
                detalle.getPsicofisico().getBarcode(),
                detalle.getPsicofisico().getPrecintos().isEmpty() ? "No" : "Si " + detalle.getPsicofisico().getPrecintos().size(),
                detalle.getPsicofisico().getRecibo() != null ? detalle.getPsicofisico().getRecibo().getNumero() : null
            });
        }
    }

    private void setPanelABM(Psicofisico archivo) {
        abmPanel.getBtnPrecintos().setEnabled(!archivo.getPrecintos().isEmpty());
        UTIL.setSelectedItem(abmPanel.getCbInstitucion(), archivo.getInstitucion().getNombre());
        abmPanel.setBarcode(SGDUtilities.getBarcode(archivo));
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        for (PsicofisicoDetalle afiliacionDetalle : archivo.getDetalle()) {
            cargarTablaDetalle(afiliacionDetalle);
        }
    }

    private void doReport(Psicofisico o) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.codigobarra"), "Archivo " + o.getClass().getSimpleName() + " N" + o.getBarcode());
        r.addParameter("TABLA", o.getClass().getSimpleName());
        r.addParameter("ID_TABLA", o.getId());
       r.viewReport();
    }

    private void btnNuevoAction() {
        entity = new Psicofisico();
        customABMJDialog.setPanelComponentsEnabled(true);
        abmPanel.resetUI(true);
        abmPanel.getCbInstitucion().requestFocusInWindow();
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
        UsuarioSector us = cbw.getEntity();
        abmPanel.getjBarcodeBean1().setCode(us.getInstitucion().getId() + "-" + us.getSector().getSectorUI().getCode() + "-xxxxxx");
    }

    CustomABMJDialog viewArchivo(Psicofisico o) {
        abmPanel = new ABMPsicofisicoPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        abmPanel.getCbInstitucion().addItem(o.getInstitucion().getNombre());
        setPanelABM(o);
        CustomABMJDialog ccustomABMJDialog = new CustomABMJDialog((JDialog) null, abmPanel, "Archivo " + o.getClass().getSimpleName() + " " + o.getBarcode(), true, null);
        ccustomABMJDialog.setToolBarVisible(false);
        ccustomABMJDialog.setBottomButtonsVisible(false);
        ccustomABMJDialog.setPanelComponentsEnabled(false);
        return ccustomABMJDialog;
    }

    private void removePrecintos(Psicofisico entity) {
        jpaController.removePrecintos(entity);
    }

    private void crearArchivo(boolean cerrarYPrecintar) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Seleccione \"Nuevo\" para crear un Usuario nuevo o \"Editar\" para uno existente.");
            }
            String msj = entity.getId() == null ? "Registrado.." : "Modificado..";
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

    void enviado(Integer archivoId, Recibo recibo) {
        Psicofisico o = jpaController.find(archivoId);
        o.setRecibo(recibo);
        jpaController.merge(o);
    }

    void recepcionar(Integer archivoId) {
        Psicofisico o = jpaController.find(archivoId);
        o.setRecibo(null);
        jpaController.merge(o);
    }
    
 
    Archivo getArchivo(Integer archivoId) {
       return jpaController.find(archivoId);
    }
}
