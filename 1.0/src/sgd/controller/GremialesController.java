package sgd.controller;

import controller.exceptions.MissingReportException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMGremialesPanel;
import sgd.gui.panel.BuscadorGremialesPanel;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.GremialesJpaController;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.Gremiales;
import sgd.jpa.model.GremialesDetalle;
import sgd.jpa.model.GremialesDetalle_;
import sgd.jpa.model.GremialesPrecinto;
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
public class GremialesController implements ActionListener {

    private CustomABMJDialog abmDialog;
    private JDBuscador buscador;
    private ABMGremialesPanel abmPanel;
    private BuscadorGremialesPanel buscadorPanel;
    private Gremiales entity;
    private final GremialesJpaController jpaController;
    private static final Logger LOG = Logger.getLogger(GremialesController.class.getName());
    private Map<String, String> empresasList;
    private Map<String, String> empleadosList;
    public static final SectorUI sectorUI = SectorUI.GREMIALES;

    public GremialesController() {
        jpaController = new GremialesJpaController();
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new ABMGremialesPanel();
        abmPanel.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && abmPanel.getjTable1().getSelectedRow() > -1) {
                    GremialesDetalle gd = (GremialesDetalle) UTIL.getSelectedValueFromModel(abmPanel.getjTable1(), 0);
                    Map<String, Object> data = new HashMap<>(10);
                    data.put("td", gd.getTipoDocumento());
                    data.put("std", gd.getSubTipoDocumento());
                    data.put("documentonumero", gd.getDocumentoNumero());
                    data.put("documentofecha", gd.getDocumentoFecha());
                    data.put("empresacuit", gd.getEmpresa() == null ? null : gd.getEmpresa().split("_")[0]);
                    data.put("empresanombre", gd.getEmpresa() == null ? null : gd.getEmpresa().split("_")[1]);
                    data.put("empleadocuil", gd.getEmpleado() == null ? null : gd.getEmpleado().split("_")[0]);
                    data.put("empleadonombre", gd.getEmpleado() == null ? null : gd.getEmpleado().split("_")[1]);
                    data.put("observacion", gd.getObservacion());
                    abmPanel.setData(data);

                }
            }

            private void setDataPanelABM(GremialesDetalle gd) {

            }

        });
        //<editor-fold defaultstate="collapsed" desc="autocomplete Empresa/Empleado">
        abmPanel.getTfEmpresaCUIT().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setPreloadedEmpresaData();
                }
            }

        });
        abmPanel.getTfEmpresaCUIT().addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                if (!abmPanel.getTfEmpresaCUIT().getText().isEmpty()) {
                    setPreloadedEmpresaData();
                }
            }

        });
        abmPanel.getTfEmpleadoCUIL().addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                if (!abmPanel.getTfEmpleadoCUIL().getText().isEmpty()) {
                    setPreloadedEmpleadoData();
                }
            }

        });
        abmPanel.getTfEmpleadoCUIL().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setPreloadedEmpleadoData();
                }
            }

        });
        //</editor-fold>
        empresasList = new HashMap<>(0);
        empleadosList = new HashMap<>(0);
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0); // --> GremialesDetalle instance
        ArrayList<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        List<ComboBoxWrapper<TipoDocumento>> tipoDocumentosList = new ArrayList<>();
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, tipoDocumentosList, sectorUI);
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), l, false);
        new TipoDocumentoComboListener(abmPanel.getCbTipoDocumento(), abmPanel.getCbSubTipoDocumento());
        UTIL.loadComboBox(abmPanel.getCbTipoDocumento(), tipoDocumentosList, false, "<Sin Tipo de Documento>");
        abmPanel.getCbInstitucion().setSelectedIndex(0);
        abmPanel.addButtonsActionListener(this);
        abmDialog = new CustomABMJDialog(owner, abmPanel, "ABM " + sectorUI, true, null);
        abmDialog.setPanelComponentsEnabled(false);
        abmDialog.addBottomButtonsActionListener(this);
        abmDialog.addToolBarButtonsActionListener(this);
        return abmDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (abmDialog != null && abmDialog.isActive()) {

            if (e.getSource().getClass().equals(JButton.class)) {
                if (e.getSource().equals(abmDialog.getBtnAceptar())) {
                    crearArchivo(false);
                } else if (e.getSource().equals(abmDialog.getBtnExtraBottom())) {
                    crearArchivo(true);
                } else if (e.getSource().equals(abmDialog.getBtnCancelar())) {
                    abmDialog.setPanelComponentsEnabled(false);
                    abmDialog.setEnabledToolBarButtons(true);
                    SwingUtil.resetJComponets(abmPanel.getComponents());
                    entity = null;
                } else if (e.getSource().equals(abmDialog.getBtnNuevo())) {
                    btnNuevoAction();
                } else if (e.getSource().equals(abmDialog.getBtnEditar())) {
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
                        loadEmpresasEmpleados();
                        abmDialog.setEnabledToolBarButtons(false);
                        abmDialog.setPanelComponentsEnabled(true);
                        abmDialog.setEnabledBottomButtons(true);
                        abmPanel.getCbInstitucion().setEnabled(false);//no se puede editar la institucion!
                        abmPanel.getCbTipoDocumento().requestFocusInWindow();
                    } catch (MessageException ex) {
                        ex.displayMessage(abmDialog);
                    }
                } else if (e.getSource().equals(abmDialog.getBtnBuscar())) {
                    initBuscador();
                    if (buscador.isEligio()) {
                        abmDialog.setPanelComponentsEnabled(false);
                        abmDialog.setEnabledBottomButtons(false);
                        setPanelABM(entity);
                    }
                } else if (e.getSource().equals(abmDialog.getBtnBorrar())) {
                    abmDialog.showMessage("¡No implementado aún!", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (e.getSource().equals(abmPanel.getBtnAgregar())) {
                    try {
                        GremialesDetalle detalle = getDetalle();
                        checkConstraints(detalle);
                        entity.getDetalle().add(detalle);
                        cargarTablaDetalle(detalle);
                        updateEmpresasEmpleadosPredictiveList(detalle);
                        SwingUtil.resetJComponets(abmPanel.getComponents(), JTable.class);
                        abmPanel.getCbTipoDocumento().requestFocusInWindow();
                    } catch (MessageException ex) {
                        abmDialog.showMessage(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (e.getSource().equals(abmPanel.getBtnQuitar())) {
                    borrarDetalle();
                } else if (e.getSource().equals(abmPanel.getBtnPrecintos())) {
                    if (entity != null) {
                        if (!entity.getPrecintos().isEmpty()) {
                            SGDUtilities.initPrecintosUI(abmDialog, entity.getPrecintos());
                        } else {
                            abmDialog.showMessage(SGD.getResources().getString("unclosedEntityPrecintos"), "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        } else if (buscador != null && buscador.isActive()) {
            if (e.getSource().equals(buscador.getbBuscar())) {
                String jpql = getBuscadorQuery();
                List<GremialesDetalle> list = jpaController.findDetalleByQuery(jpql);
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
            throw new MessageException(SGD.getResources().getString("warn.emptydetailbox"));
        }
        if (cerrarYPrecintar) {
            List<String> codigoPrecintos = SGDUtilities.initPrecintosUI(abmDialog);
            if (codigoPrecintos.isEmpty()) {
                throw new MessageException(SGD.getResources().getString("cargaprecintoscancelada"));
            }
            for (String codigo : codigoPrecintos) {
                entity.getPrecintos().add(new GremialesPrecinto(codigo, entity));
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

    private GremialesDetalle getDetalle() throws MessageException {
        Map<String, Object> data = abmPanel.getData();
        TipoDocumento td;
        SubTipoDocumento std;
        Long numeroDocumento;
        String empresaCUITNombre;
        String empleadoCUILNombre;
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
        String eNombre = data.get("empresacuit").toString().replaceAll("_", "");
        String eCU = data.get("empresanombre").toString().replaceAll("_", "");
        if (eNombre.isEmpty() != eCU.isEmpty()) {
            throw new MessageException("Debe ingresar ambos datos de la empresa (Nombre y CUIT) o ninguno si no corresponde");
        }
        empresaCUITNombre = eNombre + "_" + eCU.toUpperCase();
        if (empresaCUITNombre.length() > 150) {
            throw new MessageException("Los datos de empresa (nombre y cuit) no puede superar los 150 caracteres."
                    + "\nTamaño actual: " + empresaCUITNombre.length());
        }
        if (empresaCUITNombre.length() < 2) {
            empresaCUITNombre = null;
        }
        eNombre = data.get("empleadocuil").toString().replaceAll("_", "");
        eCU = data.get("empleadonombre").toString().replaceAll("_", "");
        if (eNombre.isEmpty() != eCU.isEmpty()) {
            throw new MessageException("Debe ingresar ambos datos del Empleado (Nombre y CUIT) o ninguno si no corresponde");
        }
        empleadoCUILNombre = eNombre + "_" + eCU.toUpperCase();
        if (empleadoCUILNombre.length() > 150) {
            throw new MessageException("Los datos de empleado (nombre y cuit) no puede superar los 150 caracteres."
                    + "\nTamaño actual: " + empleadoCUILNombre.length());
        }
        if (empleadoCUILNombre.length() < 2) {
            empleadoCUILNombre = null;
        }
        documentoFecha = (Date) data.get("documentofecha");
        observacion = (String) data.get("observacion");
        if (observacion.isEmpty()) {
            observacion = null;
        }
        GremialesDetalle detalle = new GremialesDetalle(null, entity.getNextOrderIndex(), td, std, numeroDocumento, documentoFecha, empresaCUITNombre, empleadoCUILNombre, observacion, entity);
        return detalle;
    }

    private void checkConstraints(GremialesDetalle toAdd) throws MessageException {
        if (toAdd.getDocumentoNumero() == null) {
            throw new MessageException("Número de documento no válida");
        }
        if (toAdd.getDocumentoFecha() == null) {
            throw new MessageException("Fecha de documento no válida");
        }
//        if (toAdd.getCuit()== null) {
//            throw new MessageException("CUIT empresa no válida");
//        }
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (int row = 0; row < dtm.getRowCount(); row++) {
            GremialesDetalle oldGremialesDetalle = (GremialesDetalle) dtm.getValueAt(row, 0);
            if (oldGremialesDetalle.getTipoDocumento().equals(toAdd.getTipoDocumento())
                    && Objects.equals(oldGremialesDetalle.getSubTipoDocumento(), toAdd.getSubTipoDocumento())
                    && Objects.equals(oldGremialesDetalle.getDocumentoNumero(), toAdd.getDocumentoNumero())
                    && UTIL.compararIgnorandoTimeFields(oldGremialesDetalle.getDocumentoFecha(), toAdd.getDocumentoFecha()) == 0
                    && Objects.equals(oldGremialesDetalle.getEmpresa(), toAdd.getEmpresa())
                    && Objects.equals(oldGremialesDetalle.getEmpleado(), toAdd.getEmpleado())) {
                throw new MessageException("Ya existe un detalle en este Archivo con los mismos datos:"
                        + "\nTipo de Documento: " + toAdd.getTipoDocumento().getNombre()
                        + "\nSub-Tipo de Documento: "
                        + (toAdd.getSubTipoDocumento() == null ? "<Sin Sub-Tipo>"
                                : toAdd.getSubTipoDocumento().getNombre())
                        + "\nN° Documento: " + toAdd.getDocumentoNumero()
                        + "\nFecha Doc.: " + UTIL.DATE_FORMAT.format(toAdd.getDocumentoFecha())
                        + "\nEmpresa: " + Objects.toString(toAdd.getEmpresa(), "").replaceAll("_", " ")
                        + "\nEmpleado: " + Objects.toString(toAdd.getEmpleado(), "").replaceAll("_", " ")
                );
            }
        }
    }

    private void cargarTablaDetalle(GremialesDetalle detalle) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getTipoDocumento().getNombre(),
            detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
            detalle.getDocumentoNumero(),
            UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
            Objects.toString(detalle.getEmpresa(), "").replaceAll("_", " "),
            Objects.toString(detalle.getEmpleado(), "").replaceAll("_", " "),
            detalle.getObservacion()
        });
    }

    private void borrarDetalle() {
        int[] selectedRows = abmPanel.getjTable1().getSelectedRows();
        for (int ii = 0; ii < selectedRows.length; ii++) {
            int selectedRow = selectedRows[ii];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
            GremialesDetalle candidate = (GremialesDetalle) dtm.getValueAt(selectedRow, 0);
            GremialesDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                GremialesDetalle ad = entity.getDetalle().get(i);
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
        buscadorPanel = new BuscadorGremialesPanel();
        SGDUtilities.volcar(abmPanel.getCbInstitucion(), buscadorPanel.getCbInstitucion(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbTipoDocumento(), buscadorPanel.getCbTipoDocumento(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbSubTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), "<Elegir>");
        buscadorPanel.getCbInstitucion().setSelectedIndex(0);
        buscadorPanel.getCbTipoDocumento().setSelectedIndex(0);
        buscadorPanel.getCbSubTipoDocumento().setSelectedIndex(0);
        new TipoDocumentoComboListener(buscadorPanel.getCbTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), true);
        buscador = new JDBuscador(abmDialog, true, buscadorPanel, "Buscador de Gremiales");
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
        buscador.getjTable1().getColumnModel().getColumn(columNames.size() - 1).setPreferredWidth(70);
        UTIL.hideColumnTable(buscador.getjTable1(), 0); // --> GremialesDetalle instance
        buscador.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    entity = (Gremiales) UTIL.getSelectedValue(buscador.getjTable1(), 0);
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
        StringBuilder sb = new StringBuilder("SELECT o FROM " + GremialesDetalle.class.getSimpleName() + " o "
                + " WHERE o.id is not null ");
        Map<String, Object> data = buscadorPanel.getData();
        sb.append("AND o.").append(GremialesDetalle_.gremiales.getName()).append(".baja = ").append(data.get("baja"));
        if (data.get("i") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) data.get("i");
            sb.append(" AND o.").append(GremialesDetalle_.gremiales.getName()).append(".institucion.id = ").append(cb.getEntity().getInstitucion().getId());
        } else {
            List<Institucion> institucionesList = new UsuarioSectorJPAController().findInstituciones(UsuarioController.getCurrentUser());
            Iterator<Institucion> it = institucionesList.iterator();
            sb.append(" AND (");
            while (it.hasNext()) {
                Institucion institucion = it.next();
                sb.append(" o.").append(GremialesDetalle_.gremiales.getName()).append(".institucion.id = ").append(institucion.getId());
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
            sb.append(" AND o.").append(GremialesDetalle_.documentoFecha.getName()).append(" >='").append(UTIL.yyyy_MM_dd.format((Date) data.get("desde"))).append("'");
        }
        if (data.get("hasta") != null) {
            sb.append(" AND o.").append(GremialesDetalle_.documentoFecha.getName())
                    .append(" <='").append(UTIL.yyyy_MM_dd.format(data.get("hasta"))).append("'");
        }
        if (data.get("documentonumero").toString().length() > 0) {
            Long nf = Long.valueOf(data.get("documentonumero").toString());
            sb.append(" AND o.").append(GremialesDetalle_.documentoNumero.getName()).append(" = ").append(nf);
        }
        if (data.get("empresa").toString().length() > 0) {
            sb.append(" AND UPPER(o.").append(GremialesDetalle_.empresa.getName()).append(")"
                    + " like '%").append(data.get("empresa").toString().toUpperCase()).append("%' ");
        }
        if (data.get("empleado").toString().length() > 0) {
            sb.append(" AND UPPER(o.").append(GremialesDetalle_.empleado.getName()).append(")"
                    + " like '%").append(data.get("empleado").toString().toUpperCase()).append("%' ");
        }
        if (!data.get("archivo").toString().isEmpty()) {
            sb.append(" AND o.").append(GremialesDetalle_.gremiales.getName()).append(".codigo=").append(data.get("archivo"));
        }
        Logger.getLogger(this.getClass()).trace(sb.toString());
        return sb.toString();
    }

    private void cargarTablaBuscador(List<GremialesDetalle> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (GremialesDetalle detalle : list) {
            dtm.addRow(new Object[]{
                detalle.getGremiales(),
                detalle.getTipoDocumento().getNombre(),
                detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
                detalle.getDocumentoNumero(),
                UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
                Objects.toString(detalle.getEmpresa(), "").replaceAll("_", " "),
                Objects.toString(detalle.getEmpleado(), "").replaceAll("_", " "),
                detalle.getObservacion(),
                detalle.getGremiales().getBarcode(),
                detalle.getGremiales().getPrecintos().isEmpty() ? "No" : "Si " + detalle.getGremiales().getPrecintos().size(),
                detalle.getGremiales().getRecibo() != null ? detalle.getGremiales().getRecibo().getNumero() : null
            });
        }
    }

    private void setPanelABM(Gremiales o) {
        UTIL.setSelectedItem(abmPanel.getCbInstitucion(), o.getInstitucion().getNombre());
        abmPanel.setBarcode(SGDUtilities.getBarcode(o));
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        for (GremialesDetalle afiliacionDetalle : o.getDetalle()) {
            cargarTablaDetalle(afiliacionDetalle);
        }
        abmPanel.getBtnPrecintos().setEnabled(!o.getPrecintos().isEmpty());
    }

    private void doReport(Gremiales o) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.codigobarra"), "Archivo " + o.getClass().getSimpleName() + " N" + o.getBarcode());
        r.addParameter("TABLA", o.getClass().getSimpleName());
        r.addParameter("ID_TABLA", o.getId());
       r.viewReport();
    }

    private void btnNuevoAction() {
        entity = new Gremiales();
        abmDialog.setEnabledToolBarButtons(false);
        abmDialog.setPanelComponentsEnabled(true);
        SwingUtil.resetJComponets(abmPanel.getComponents());
        loadEmpresasEmpleados();
        abmPanel.getCbInstitucion().requestFocusInWindow();
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
        UsuarioSector us = cbw.getEntity();
        abmPanel.setBarcode(us.getInstitucion().getId() + "-" + us.getSector().getSectorUI().getCode() + "-xxxxxx");
    }

    CustomABMJDialog viewArchivo(Gremiales o) {
        abmPanel = new ABMGremialesPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        abmPanel.getCbInstitucion().addItem(o.getInstitucion().getNombre());
        setPanelABM(o);
        CustomABMJDialog ccustomABMJDialog = new CustomABMJDialog(null, abmPanel, "Archivo " + o.getClass().getSimpleName() + " " + o.getBarcode(), true, null);
        ccustomABMJDialog.setToolBarVisible(false);
        ccustomABMJDialog.setBottomButtonsVisible(false);
        ccustomABMJDialog.setPanelComponentsEnabled(false);
        return ccustomABMJDialog;
    }

    private void removePrecintos(Gremiales entity) {
        jpaController.removePrecintos(entity);
    }

    private void crearArchivo(boolean cerrarYPrecintar) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Seleccione \"Nuevo\" para crear un Archivo (Caja) nueva o \"Editar\" para modificar una existente.");
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
            Logger.getLogger(this.getClass()).error(ex.getLocalizedMessage(), ex);
            abmDialog.showMessage(ex.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    void enviado(Integer archivoId, Recibo recibo) {
        Gremiales o = jpaController.find(archivoId);
        o.setRecibo(recibo);
        jpaController.merge(o);
    }

    void recepcionar(Integer archivoId) {
        Gremiales o = jpaController.find(archivoId);
        o.setRecibo(null);
        jpaController.merge(o);
    }

    private void loadEmpresasEmpleados() {
        empresasList = jpaController.findEmpresas();
        AutoCompleteDecorator.decorate(abmPanel.getTfEmpresaCUIT(), new ArrayList<>(empresasList.keySet()), false);
        AutoCompleteDecorator.decorate(abmPanel.getTfEmpresaNombre(), new ArrayList<>(empresasList.values()), false);

        //EMPLEADOS...
        empleadosList = jpaController.findEmpleados();
        AutoCompleteDecorator.decorate(abmPanel.getTfEmpleadoCUIL(), new ArrayList<>(empleadosList.keySet()), false);
        AutoCompleteDecorator.decorate(abmPanel.getTfEmpleadoNombre(), new ArrayList<>(empleadosList.values()), false);

    }

    private void setPreloadedEmpleadoData() {
        if (empleadosList.containsKey(abmPanel.getTfEmpleadoCUIL().getText())) {
            String nombre = empleadosList.get(abmPanel.getTfEmpleadoCUIL().getText());
            abmPanel.getTfEmpleadoNombre().setText(nombre);
            abmPanel.getTfEmpleadoNombre().setEnabled(false);
            abmPanel.getTfObservacion().requestFocusInWindow();
        } else {
            abmPanel.getTfEmpleadoNombre().setText(null);
            abmPanel.getTfEmpleadoNombre().setEnabled(true);
            abmPanel.getTfEmpleadoNombre().requestFocusInWindow();
        }
    }

    private void setPreloadedEmpresaData() {
        if (empresasList.containsKey(abmPanel.getTfEmpresaCUIT().getText())) {
            String nombre = empresasList.get(abmPanel.getTfEmpresaCUIT().getText());
            abmPanel.getTfEmpresaNombre().setText(nombre);
            abmPanel.getTfEmpresaNombre().setEnabled(false);
            abmPanel.getTfEmpleadoCUIL().requestFocusInWindow();
        } else {
            //cuando no existe la cuit, habilita la carga del nombre de la empresa
            abmPanel.getTfEmpresaNombre().setText(null);
            abmPanel.getTfEmpresaNombre().setEnabled(true);
            abmPanel.getTfEmpresaNombre().requestFocusInWindow();
        }
    }

    private void updateEmpresasEmpleadosPredictiveList(GremialesDetalle detalle) {
        if (detalle.getEmpresa() != null) {
            String[] s = detalle.getEmpresa().split("_");
            String cu = s[0];
            String no = s[1];
            if (!empresasList.containsValue(cu)) {
                empresasList.put(no, cu);
                AutoCompleteDecorator.decorate(abmPanel.getTfEmpresaCUIT(), new ArrayList<>(empresasList.values()), false);
                //empresas (son las KEY) nombres...
                AutoCompleteDecorator.decorate(abmPanel.getTfEmpresaNombre(), new ArrayList<>(empresasList.keySet()), false);
            } else {
                String oldCU = empresasList.get(no);
            }
        }
        if (detalle.getEmpleado() != null) {
            String[] s = detalle.getEmpleado().split("_");
            if (!empleadosList.containsValue(s[0])) {
                empleadosList.put(s[1], s[0]);
                //combo con las CUIL
                AutoCompleteDecorator.decorate(abmPanel.getTfEmpleadoCUIL(), new ArrayList<>(empleadosList.values()), false);
                //nombres...
                AutoCompleteDecorator.decorate(abmPanel.getTfEmpleadoNombre(), new ArrayList<>(empleadosList.keySet()), false);
            }
        }
    }
}
