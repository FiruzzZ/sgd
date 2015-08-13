package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMAfiliacionPanel;
import sgd.gui.panel.BuscadorAfiliacionPanel;
import sgd.jpa.controller.AfiliacionJPAController;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class AfiliacionController extends ArchivoController<Afiliacion> implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private ABMAfiliacionPanel abmPanel;
    private BuscadorAfiliacionPanel buscadorPanel;
    private Afiliacion entity;
    private final AfiliacionJPAController jpaController = new AfiliacionJPAController();
    private static final Logger LOG = Logger.getLogger(AfiliacionController.class.getSimpleName());
    public static final SectorUI sectorUI = SectorUI.AFILIACION;

    public AfiliacionController() {
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new ABMAfiliacionPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0); // --> entity Instance
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
        customABMJDialog.setEnabledBottomButtons(false);
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
                            AfiliacionDetalle detalle = getDetalle();
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
                if (e.getSource().equals(buscador.getbBuscar())) {
                    String jpql = getBuscadorQuery();
                    List<AfiliacionDetalle> list = jpaController.findDetalleByJPQL(jpql);
                    if (list.isEmpty()) {
                        JOptionPane.showMessageDialog(buscador, "La busqueda no produjo ningún resultado");
                        return;
                    }
                    cargarTablaBuscador(list);
                }
            }
        } catch (Exception ex) {
            LOG.error("Global Catch:", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                entity.getPrecintos().add(new AfiliacionPrecinto(codigo, entity));
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

    private AfiliacionDetalle getDetalle() throws MessageException {
        Map<String, Object> data = abmPanel.getData();
        TipoDocumento td;
        SubTipoDocumento std;
        Long numeroAfiliado;
        Integer familiarNumero = 0;
        String apellido = null;
        String nombre = null;
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

        try {
            numeroAfiliado = Long.valueOf(data.get("afiliado").toString());
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de afiliado no válido (ingrese solo números)");
        }
        try {
            if (!data.get("familiar").toString().isEmpty()) {
                familiarNumero = Integer.valueOf(data.get("familiar").toString());
            }
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de Familiar no válido (ingrese solo números o deje vació el campo)");
        }
        if (familiarNumero < 0 || familiarNumero > 99) {
            throw new MessageException("Número de Familiar no válido, no puede ser menor a CERO, ni mayor a 99");
        }
        if (!data.get("apellido").toString().isEmpty()) {
            apellido = (String) data.get("apellido");
        }
        if (!data.get("nombre").toString().isEmpty()) {
            nombre = (String) data.get("nombre");
        }
        documentoFecha = (Date) data.get("documentoFecha");
        if (documentoFecha == null) {
            throw new MessageException("Fecha de documento no válida");
        }
        if (!data.get("observacion").toString().isEmpty()) {
            observacion = (String) data.get("observacion");
        }

        AfiliacionDetalle detalle = new AfiliacionDetalle(getNextOrderIndex(entity), td, std, numeroAfiliado, familiarNumero, apellido, nombre, documentoFecha, observacion, entity);
        return detalle;
    }

    private void checkConstraints(AfiliacionDetalle toAdd) throws MessageException {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (int row = 0; row < dtm.getRowCount(); row++) {
            AfiliacionDetalle old = (AfiliacionDetalle) dtm.getValueAt(row, 0);
            if (Objects.equals(old.getTipoDocumento(), toAdd.getTipoDocumento())) {
                if (Objects.equals(old.getSubTipoDocumento(), toAdd.getSubTipoDocumento())) {
                    if (Objects.equals(old.getNumeroAfiliado(), toAdd.getNumeroAfiliado())
                            && Objects.equals(old.getFamiliarNumero(), toAdd.getFamiliarNumero())) {
                        throw new MessageException("Ya existe un detalle con los mismos datos:"
                                + "\nTipo de Documento: " + old.getTipoDocumento().getNombre()
                                + (old.getSubTipoDocumento() == null ? "" : "\nSub-Tipo de Documento: " + old.getSubTipoDocumento().getNombre())
                                + "\nN° Afiliado: " + old.getNumeroAfiliado() + "/" + old.getFamiliarNumero());
                    }
                }
            }
        }
    }

    private void cargarTablaDetalle(AfiliacionDetalle detalle) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getTipoDocumento().getNombre(),
            detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
            detalle.getNumeroAfiliado() + "/" + detalle.getFamiliarNumero(),
            UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
            detalle.getApellido() + ", " + detalle.getNombre(),
            detalle.getObservacion()
        });
    }

    private void borrarDetalle() {
        int[] selectedRows = abmPanel.getjTable1().getSelectedRows();
        for (int ii = 0; ii < selectedRows.length; ii++) {
            int selectedRow = selectedRows[ii];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
            AfiliacionDetalle candidate = (AfiliacionDetalle) dtm.getValueAt(selectedRow, 0);
            AfiliacionDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                AfiliacionDetalle ad = entity.getDetalle().get(i);
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
        buscadorPanel = new BuscadorAfiliacionPanel();
        SGDUtilities.volcar(abmPanel.getCbInstitucion(), buscadorPanel.getCbInstitucion(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbTipoDocumento(), buscadorPanel.getCbTipoDocumento(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbSubTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), "<Elegir>");
        buscadorPanel.getCbInstitucion().setSelectedIndex(0);
        buscadorPanel.getCbTipoDocumento().setSelectedIndex(0);
        buscadorPanel.getCbSubTipoDocumento().setSelectedIndex(0);
        new TipoDocumentoComboListener(buscadorPanel.getCbTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), true);
        buscador = new JDBuscador(customABMJDialog, true, buscadorPanel, "Buscador de Afiliaciones");
        buscador.getbImprimir().setEnabled(false);
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
                    entity = (Afiliacion) UTIL.getSelectedValue(buscador.getjTable1(), 0);
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
        StringBuilder sb = new StringBuilder("SELECT o FROM " + AfiliacionDetalle.class.getSimpleName() + " o "
                + " WHERE o.id is not null ");
        Map<String, Object> data = buscadorPanel.getData();

        if (data.get("td") != null) {
            sb.append("AND o.afiliacion.baja = ").append(data.get("baja"));
        }
        if (data.get("i") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) data.get("i");
            sb.append(" AND o.afiliacion.institucion.id = ").append(cb.getEntity().getInstitucion().getId());
        } else {
            List<Institucion> institucionesList = new UsuarioSectorJPAController().findInstituciones(UsuarioController.getCurrentUser());
            Iterator<Institucion> it = institucionesList.iterator();
            sb.append(" AND (");
            while (it.hasNext()) {
                Institucion institucion = it.next();
                sb.append(" o.afiliacion.institucion.id = ").append(institucion.getId());
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
        if (data.get("caja").toString().length() > 0) {
            sb.append(" AND o.afiliacion.codigo =").append(data.get("caja"));
        }
        if (data.get("afiliado").toString().length() > 0) {
            Long nf = Long.valueOf(data.get("afiliado").toString());
            sb.append(" AND o.numeroAfiliado = ").append(nf);
        }
        Logger.getLogger(this.getClass()).trace(sb.toString());
        return sb.toString();
    }

    private void cargarTablaBuscador(List<AfiliacionDetalle> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (AfiliacionDetalle detalle : list) {
            dtm.addRow(new Object[]{
                detalle.getAfiliacion(),
                detalle.getTipoDocumento().getNombre(),
                detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
                detalle.getNumeroAfiliado() + "/" + detalle.getFamiliarNumero(),
                UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
                detalle.getApellido() + ", " + detalle.getNombre(),
                detalle.getObservacion(),
                detalle.getAfiliacion().getBarcode(),
                detalle.getAfiliacion().getPrecintos().isEmpty() ? "No" : "Si " + detalle.getAfiliacion().getPrecintos().size(),
                detalle.getAfiliacion().getRecibo() != null ? detalle.getAfiliacion().getRecibo().getNumero() : null
            });
        }
    }

    private void setPanelABM(Afiliacion o) {
        UTIL.setSelectedItem(abmPanel.getCbInstitucion(), o.getInstitucion().getNombre());
        abmPanel.getBtnPrecintos().setEnabled(!o.getPrecintos().isEmpty());
        abmPanel.setBarcode(SGDUtilities.getBarcode(o));
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        for (AfiliacionDetalle afiliacionDetalle : o.getDetalle()) {
            cargarTablaDetalle(afiliacionDetalle);
        }
    }

    private void doReport(Afiliacion o) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.codigobarra"), "Archivo " + o.getClass().getSimpleName() + " N" + o.getBarcode());
        r.addParameter("TABLA", o.getClass().getSimpleName());
        r.addParameter("ID_TABLA", o.getId());
       r.viewReport();
    }

    private void btnNuevoAction() {
        entity = new Afiliacion();
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

    @Override
    CustomABMJDialog viewArchivo(Afiliacion o) {
        abmPanel = new ABMAfiliacionPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        abmPanel.getCbInstitucion().addItem(o.getInstitucion().getNombre());
        setPanelABM(o);
        CustomABMJDialog ccustomABMJDialog = new CustomABMJDialog(null, abmPanel, "Archivo " + o.getClass().getSimpleName() + " " + o.getBarcode(), true, null);
        ccustomABMJDialog.setToolBarVisible(false);
        ccustomABMJDialog.setBottomButtonsVisible(false);
        ccustomABMJDialog.setPanelComponentsEnabled(false);
        return ccustomABMJDialog;
    }

    private void removePrecintos(Afiliacion entity) {
        jpaController.removePrecintos(entity);
    }

    private void crearArchivo(boolean cerrarYPrecintar) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Seleccione \"Nuevo\" para comenzar un nuevo Archivo (Caja) o \"Modificar\" para continuar con uno existente.");
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
        } catch (IllegalArgumentException | MessageException ex) {
            customABMJDialog.showMessage(ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            customABMJDialog.showMessage(ex.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            LOG.error(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    void enviado(Integer archivoId, Recibo recibo) {
        Afiliacion o = jpaController.find(archivoId);
        o.setRecibo(recibo);
        jpaController.merge(o);
    }

    void recepcionar(Integer archivoId) {
        Afiliacion o = jpaController.find(archivoId);
        o.setRecibo(null);
        jpaController.merge(o);
    }

    public Integer getNextOrderIndex(Afiliacion o) {
        if (o.getDetalle() == null) {
            o.setDetalle(new ArrayList<AfiliacionDetalle>());
        }
        int orderIndex = 0;
        for (AfiliacionDetalle d : o.getDetalle()) {
            if (d.getOrderIndex() > orderIndex) {
                orderIndex = d.getOrderIndex();
            }
        }
        return orderIndex + 1;
    }

    @Override
    Afiliacion find(Integer archivoId) {
       return jpaController.find(archivoId);
    }

}
