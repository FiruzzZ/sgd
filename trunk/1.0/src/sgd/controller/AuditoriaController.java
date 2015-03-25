package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMAuditoriaPanel;
import sgd.gui.panel.BuscadorAuditoriaPanel;
import sgd.jpa.controller.AuditoriaJpaController;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.Archivo;
import sgd.jpa.model.Auditoria;
import sgd.jpa.model.AuditoriaDetalle;
import sgd.jpa.model.AuditoriaPrecinto;
import sgd.jpa.model.Institucion;
import sgd.jpa.model.Recibo;
import sgd.jpa.model.SectorUI;
import sgd.jpa.model.SubTipoDocumento;
import sgd.jpa.model.TipoDocumento;
import sgd.jpa.model.UsuarioSector;
import utilities.general.UTIL;
import utilities.gui.SwingUtil;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class AuditoriaController implements ActionListener {

    private CustomABMJDialog abmDialog;
    private JDBuscador buscador;
    private ABMAuditoriaPanel abmPanel;
    private BuscadorAuditoriaPanel buscadorPanel;
    private Auditoria entity;
    private final AuditoriaJpaController jpaController = new AuditoriaJpaController();
    private static final Logger LOG = Logger.getLogger(AuditoriaController.class.getSimpleName());
    public static final SectorUI sectorUI = SectorUI.AUDITORIA;

    public AuditoriaController() {
    }

    CustomABMJDialog getAbm(Window owner) throws MessageException {
        List<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        List<ComboBoxWrapper<TipoDocumento>> ltd = new ArrayList<>();
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, ltd, sectorUI);
        if (ltd.isEmpty()) {
            throw new MessageException("Aún no se ha definido ningún Tipo de Documento para el sector."
                    + "\nPuedo definirlos en menú Configuración > ABM Tipo de Documentos");
        }
        abmPanel = new ABMAuditoriaPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0); // --> entity Instance
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        new TipoDocumentoComboListener(abmPanel.getCbTipoDocumento(), abmPanel.getCbSubTipoDocumento());
        UTIL.loadComboBox(abmPanel.getCbTipoDocumento(), ltd, false, "<Sin Tipo de Documento>");
        abmPanel.getCbInstitucion().setSelectedIndex(0);
        loadPrestadores();
        abmPanel.addButtonsActionListener(this);
        abmDialog = new CustomABMJDialog(owner, abmPanel, "ABM " + sectorUI, true, null);
        abmDialog.setPanelComponentsEnabled(false);
        abmDialog.addBottomButtonsActionListener(this);
        abmDialog.addToolBarButtonsActionListener(this);
        abmDialog.setEnabledBottomButtons(false);
        return abmDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (abmDialog != null && abmDialog.isActive()) {
                if (button.equals(abmDialog.getBtnAceptar())) {
                    crearArchivo(false);
                } else if (button.equals(abmDialog.getBtnExtraBottom())) {
                    crearArchivo(true);
                } else if (button.equals(abmDialog.getBtnCancelar())) {
                    abmDialog.setPanelComponentsEnabled(false);
                    abmDialog.setEnabledToolBarButtons(true);
                    SwingUtil.resetJComponets(abmPanel.getComponents());
                    entity = null;
                } else if (button.equals(abmDialog.getBtnNuevo())) {
                    btnNuevoAction();
                } else if (button.equals(abmDialog.getBtnEditar())) {
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
                        abmDialog.setPanelComponentsEnabled(true);
                        abmDialog.setEnabledBottomButtons(true);
                        abmPanel.getCbInstitucion().setEnabled(false);
                        loadPrestadores();
                    } catch (MessageException ex) {
                        ex.displayMessage(abmDialog);
                    }
                } else if (button.equals(abmDialog.getBtnBuscar())) {
                    initBuscador();
                    if (buscador.isEligio()) {
                        abmDialog.setPanelComponentsEnabled(false);
                        abmDialog.setEnabledBottomButtons(false);
                        setPanelABM(entity);
                    }
                } else if (button.equals(abmDialog.getBtnBorrar())) {
                    abmDialog.showMessage("¡No implementado aún!", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (button.equals(abmPanel.getBtnAgregar())) {
                    try {
                        AuditoriaDetalle detalle = getDetalle();
                        checkConstraints(detalle);
                        entity.getDetalle().add(detalle);
                        cargarTablaDetalle(detalle);
                        abmPanel.resetUI(false);
                        abmPanel.getCbTipoDocumento().requestFocusInWindow();
                    } catch (MessageException ex) {
                        abmDialog.showMessage(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (button.equals(abmPanel.getBtnQuitar())) {
                    borrarDetalle();
                } else if (button.equals(abmPanel.getBtnPrecintos())) {
                    if (!entity.getPrecintos().isEmpty()) {
                        SGDUtilities.initPrecintosUI(abmDialog, entity.getPrecintos());
                    } else {
                        abmDialog.showMessage(SGD.getResources().getString("unclosedEntityPrecintos"), "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (buscador != null && buscador.isActive()) {
                if (button.equals(buscador.getbBuscar())) {
                    String jpql = getBuscadorQuery();
                    List<AuditoriaDetalle> list = jpaController.findAuditoriaDetalleByJPQL(jpql);
                    cargarTablaBuscador(list);
                }
            }
        }
    }

    private void setEntity(boolean cerrarYPrecintar) throws MessageException {
        if (entity.getDetalle() == null || entity.getDetalle().isEmpty()) {
            throw new MessageException("Debe contener al menos un detalle (No vas a mandar la caja vacía..)");
        }
        if (cerrarYPrecintar) {
            List<String> codigoPrecintos = SGDUtilities.initPrecintosUI(abmDialog);
            if (codigoPrecintos.isEmpty()) {
                throw new MessageException(SGD.getResources().getString("cargaprecintoscancelada"));
            }
            for (String codigo : codigoPrecintos) {
                entity.getPrecintos().add(new AuditoriaPrecinto(null, codigo, entity));
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

    private AuditoriaDetalle getDetalle() throws MessageException {
        Map<String, Object> data = abmPanel.getData();
        TipoDocumento td;
        SubTipoDocumento std;
        String prestador;
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

        prestador = (String) data.get("prestador");
        prestador = prestador.toUpperCase().trim();
        if (prestador.isEmpty()) {
            throw new MessageException("Prestador no válido, elija uno de los existenes o escriba el que desea agregar");
        }
        documentoFecha = (Date) data.get("documentoFecha");
        if (documentoFecha == null) {
            throw new MessageException("Fecha de documento no válida");
        }
        if (!data.get("observacion").toString().isEmpty()) {
            observacion = (String) data.get("observacion");
        }

        AuditoriaDetalle detalle = new AuditoriaDetalle(null, getNextOrderIndex(entity), td, std, documentoFecha, prestador, observacion, entity);
        return detalle;
    }

    private void checkConstraints(AuditoriaDetalle toAdd) throws MessageException {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (int row = 0; row < dtm.getRowCount(); row++) {
            AuditoriaDetalle old = (AuditoriaDetalle) dtm.getValueAt(row, 0);
            if (Objects.equals(old.getTipoDocumento(), toAdd.getTipoDocumento())
                    && Objects.equals(old.getSubTipoDocumento(), toAdd.getSubTipoDocumento())
                    && Objects.equals(old.getPrestador(), toAdd.getPrestador())
                    && UTIL.compararIgnorandoTimeFields(old.getDocumentoFecha(), toAdd.getDocumentoFecha()) == 0) {
                throw new MessageException("Ya existe un detalle con los mismos datos:"
                        + "\nTipo de Documento: " + old.getTipoDocumento().getNombre()
                        + (old.getSubTipoDocumento() == null ? "" : "\nSub-Tipo de Documento: " + old.getSubTipoDocumento().getNombre())
                        + "\nPrestador:" + old.getPrestador()
                        + "\nFecha:" + UTIL.DATE_FORMAT.format(old.getDocumentoFecha())
                );
            }
        }
        List<AuditoriaDetalle> old = jpaController.findDetalle(toAdd.getTipoDocumento(), toAdd.getSubTipoDocumento(), toAdd.getPrestador(), toAdd.getDocumentoFecha());
        //excluyendo los resultados de la misma caja
        for (Iterator<AuditoriaDetalle> it = old.iterator(); it.hasNext();) {
            AuditoriaDetalle detalle = it.next();
            if (Objects.equals(detalle.getAuditoria().getId(), entity.getId())) {
                it.remove();
            }
        }
        if (!old.isEmpty()) {
            String warn = "Existen registros de archivos con la misma información en las siguientes cajas:";
            for (AuditoriaDetalle detalle : old) {
                warn += "\n" + SGDUtilities.getBarcode(detalle.getAuditoria());
            }
            JOptionPane.showMessageDialog(abmDialog, warn, "Posible duplicación de carga de archivos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarTablaDetalle(AuditoriaDetalle detalle) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getTipoDocumento().getNombre(),
            detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
            detalle.getPrestador(),
            UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
            detalle.getObservacion()
        });
    }

    private void borrarDetalle() {
        int[] selectedRows = abmPanel.getjTable1().getSelectedRows();
        for (int ii = 0; ii < selectedRows.length; ii++) {
            int selectedRow = selectedRows[ii];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
            AuditoriaDetalle candidate = (AuditoriaDetalle) dtm.getValueAt(selectedRow, 0);
            AuditoriaDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                AuditoriaDetalle ad = entity.getDetalle().get(i);
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
        buscadorPanel = new BuscadorAuditoriaPanel();
        SGDUtilities.volcar(abmPanel.getCbInstitucion(), buscadorPanel.getCbInstitucion(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbTipoDocumento(), buscadorPanel.getCbTipoDocumento(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbSubTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), "<Elegir>");
        buscadorPanel.getCbInstitucion().setSelectedIndex(0);
        buscadorPanel.getCbTipoDocumento().setSelectedIndex(0);
        buscadorPanel.getCbSubTipoDocumento().setSelectedIndex(0);
        new TipoDocumentoComboListener(buscadorPanel.getCbTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), true);
        buscador = new JDBuscador(abmDialog, true, buscadorPanel, "Buscador de Auditoriaes");
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
                    entity = (Auditoria) UTIL.getSelectedValue(buscador.getjTable1(), 0);
                    buscador.setEligio(true);
                    buscador.dispose();
                }
            }
        });

        buscador.addListener(this);
        buscador.setLocationRelativeTo(abmDialog);
        buscador.setVisible(true);
    }

    private String getBuscadorQuery() {
        StringBuilder sb = new StringBuilder("SELECT o FROM " + AuditoriaDetalle.class.getSimpleName() + " o "
                + " WHERE o.id is not null ");
        Map<String, Object> data = buscadorPanel.getData();

        if (data.get("td") != null) {
            sb.append("AND o.auditoria.baja = ").append(data.get("baja"));
        }
        if (data.get("i") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) data.get("i");
            sb.append(" AND o.auditoria.institucion.id = ").append(cb.getEntity().getInstitucion().getId());
        } else {
            List<Institucion> institucionesList = new UsuarioSectorJPAController().findInstituciones(UsuarioController.getCurrentUser());
            Iterator<Institucion> it = institucionesList.iterator();
            sb.append(" AND (");
            while (it.hasNext()) {
                Institucion institucion = it.next();
                sb.append(" o.auditoria.institucion.id = ").append(institucion.getId());
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
        if (data.get("prestador").toString().length() > 0) {
            sb.append(" AND UPPER(o.prestador) LIKE UPPER('%").append(data.get("prestador")).append("%')");
        }
        if (data.get("observacion").toString().length() > 0) {
            sb.append(" AND UPPER(o.observacion) LIKE UPPER('%").append(data.get("observacion")).append("%')");
        }
        if (!data.get("caja").toString().isEmpty()) {
            sb.append(" AND o.auditoria.codigo=").append(data.get("caja"));
        }
        Logger.getLogger(this.getClass()).trace(sb.toString());
        return sb.toString();
    }

    private void cargarTablaBuscador(List<AuditoriaDetalle> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (AuditoriaDetalle detalle : list) {
            dtm.addRow(new Object[]{
                detalle.getAuditoria(),
                detalle.getTipoDocumento().getNombre(),
                detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
                detalle.getPrestador(),
                UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
                detalle.getObservacion(),
                detalle.getAuditoria().getBarcode(),
                detalle.getAuditoria().getPrecintos().isEmpty() ? "No" : "Si " + detalle.getAuditoria().getPrecintos().size(),
                detalle.getAuditoria().getRecibo() != null ? detalle.getAuditoria().getRecibo().getNumero() : null
            });
        }
    }

    private void setPanelABM(Auditoria o) {
        UTIL.setSelectedItem(abmPanel.getCbInstitucion(), o.getInstitucion().getNombre());
        abmPanel.getBtnPrecintos().setEnabled(!o.getPrecintos().isEmpty());
        abmPanel.setBarcode(SGDUtilities.getBarcode(o));
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        for (AuditoriaDetalle detalle : o.getDetalle()) {
            cargarTablaDetalle(detalle);
        }
    }

    private void doReport(Auditoria o) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.codigobarra"), "Archivo " + o.getClass().getSimpleName() + " N" + o.getBarcode());
        r.addParameter("TABLA", o.getClass().getSimpleName());
        r.addParameter("ID_TABLA", o.getId());
       r.viewReport();
    }

    private void btnNuevoAction() {
        entity = new Auditoria();
        abmDialog.setEnabledToolBarButtons(false);
        abmDialog.setPanelComponentsEnabled(true);
        abmDialog.setEnabledBottomButtons(true);
        SwingUtil.resetJComponets(abmPanel.getComponents());
        loadPrestadores();
        abmPanel.getCbInstitucion().requestFocusInWindow();
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
        UsuarioSector us = cbw.getEntity();
        abmPanel.setBarcode(us.getInstitucion().getId() + "-" + us.getSector().getSectorUI().getCode() + "-xxxxxx");
    }

    CustomABMJDialog getAbmRecibos(Window owner) {
        return new ReciboController().getAbm(owner, sectorUI);
    }

    CustomABMJDialog getAbmRecepcion(Window owner) {
        return new RecepcionController().getAbm(owner, sectorUI);
    }

    CustomABMJDialog viewArchivo(Auditoria o) {
        abmPanel = new ABMAuditoriaPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        abmPanel.getCbInstitucion().addItem(o.getInstitucion().getNombre());
        setPanelABM(o);
        CustomABMJDialog ccustomABMJDialog = new CustomABMJDialog(null, abmPanel, "Archivo " + o.getClass().getSimpleName() + " " + o.getBarcode(), true, null);
        ccustomABMJDialog.setToolBarVisible(false);
        ccustomABMJDialog.setBottomButtonsVisible(false);
        ccustomABMJDialog.setPanelComponentsEnabled(false);
        return ccustomABMJDialog;
    }

    private void removePrecintos(Auditoria entity) {
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
            abmDialog.showMessage(msj, null, 1);
            if (nuevo || SGDUtilities.reImprimirCodigoBarraArchivo()) {
                doReport(entity);
            }
            abmDialog.setPanelComponentsEnabled(false);
            SwingUtil.resetJComponets(abmPanel.getComponents());
            entity = null;
            abmDialog.setEnabledToolBarButtons(true);
            abmDialog.getBtnNuevo().requestFocusInWindow();
        } catch (IllegalArgumentException | MessageException ex) {
            abmDialog.showMessage(ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            abmDialog.showMessage(ex.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(this.getClass()).error(ex.getLocalizedMessage(), ex);
        }
    }

    void enviado(Integer archivoId, Recibo recibo) {
        Auditoria o = jpaController.find(archivoId);
        o.setRecibo(recibo);
        jpaController.merge(o);
    }

    void recepcionar(Integer archivoId) {
        Auditoria o = jpaController.find(archivoId);
        o.setRecibo(null);
        jpaController.merge(o);
    }

    private Integer getNextOrderIndex(Auditoria o) {
        if (o.getDetalle() == null) {
            o.setDetalle(new ArrayList<AuditoriaDetalle>());
        }
        int orderIndex = 0;
        for (AuditoriaDetalle d : o.getDetalle()) {
            if (d.getOrderIndex() > orderIndex) {
                orderIndex = d.getOrderIndex();
            }
        }
        return orderIndex + 1;
    }

    /**
     * Carga la lista de prestadores para el AutoComplete del JTextfield de Prestadores en el panel
     * ABM.
     */
    private void loadPrestadores() {
        UTIL.loadComboBox(abmPanel.getCbPrestador(), jpaController.findPrestadores(), false);
        AutoCompleteDecorator.decorate(abmPanel.getCbPrestador());
    }
    
      Archivo getArchivo(Integer archivoId) {
       return jpaController.find(archivoId);
    }
}
