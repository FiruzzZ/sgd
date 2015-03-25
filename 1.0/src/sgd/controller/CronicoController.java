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
import sgd.SGD;
import sgd.controller.exception.MessageException;
import sgd.gui.JDBuscador;
import sgd.gui.panel.ABMCronicoPanel;
import sgd.gui.panel.BuscadorCronicoPanel;
import sgd.jpa.controller.CronicoJpaController;
import sgd.jpa.controller.DAO;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.Cronico;
import sgd.jpa.model.CronicoDetalle;
import sgd.jpa.model.CronicoDetalle_;
import sgd.jpa.model.CronicoPrecinto;
import sgd.jpa.model.Institucion;
import sgd.jpa.model.Recibo;
import sgd.jpa.model.SectorUI;
import sgd.jpa.model.SubTipoDocumento;
import sgd.jpa.model.TipoDocumento;
import sgd.jpa.model.UsuarioSector;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class CronicoController implements ActionListener {

    private CustomABMJDialog customABMJDialog;
    private JDBuscador buscador;
    private ABMCronicoPanel abmPanel;
    private BuscadorCronicoPanel buscadorPanel;
    private Cronico entity;
    private final CronicoJpaController jpaController = new CronicoJpaController();
    private static final Logger LOG = Logger.getLogger(CronicoController.class.getSimpleName());
    public static final SectorUI sectorUI = SectorUI.CRONICO;

    public CronicoController() {
    }

    CustomABMJDialog getAbm(Window owner) throws MessageException {
        List<ComboBoxWrapper<UsuarioSector>> l = new ArrayList<>(5);
        List<ComboBoxWrapper<TipoDocumento>> ltd = new ArrayList<>();
        SGDUtilities.cargarInstitucionesYTipoDocumentoSegunSector(l, ltd, sectorUI);
        if (ltd.isEmpty()) {
            throw new MessageException("Aún no se ha definido ningún Tipo de Documento para el sector."
                    + "\nPuedo definirlos en menú Configuración > ABM Tipo de Documentos");
        }
        abmPanel = new ABMCronicoPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0); // --> entity Instance
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
                                    .replaceAll("envio.id", entity.getRecibo().getId() + "")
                                    .replaceAll("<Sector>", sectorUI.toString())
                            );
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
                    initBuscador(customABMJDialog);
                    if (buscador.isEligio()) {
                        customABMJDialog.setPanelComponentsEnabled(false);
                        customABMJDialog.setEnabledBottomButtons(false);
                        setPanelABM(entity);
                    }
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    customABMJDialog.showMessage("¡No implementado aún!", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (e.getSource().equals(abmPanel.getBtnAgregar())) {
                    try {
                        CronicoDetalle detalle = getDetalle();
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
                List<CronicoDetalle> list = jpaController.findCronicoDetalleByJPQL(jpql);
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
                entity.getPrecintos().add(new CronicoPrecinto(codigo, entity));
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

    private CronicoDetalle getDetalle() throws MessageException {
        Map<String, Object> data = abmPanel.getData();
        TipoDocumento td;
        SubTipoDocumento std;
        Integer afiliado;
        Integer familiar = 0;
        Integer formulario = null;
        Integer numeroDocumento;
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
            afiliado = Integer.valueOf(data.get("afiliado").toString());
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de afiliado no válido (ingrese solo números)");
        }
        try {
            if (!data.get("familiar").toString().isEmpty()) {
                familiar = Integer.valueOf(data.get("familiar").toString());
            }
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de Familiar no válido (ingrese solo números)");
        }
        try {
            numeroDocumento = Integer.valueOf(data.get("numerodocumento").toString());
        } catch (NumberFormatException numberFormatException) {
            throw new MessageException("Número de documento no válido (ingrese solo números)");
        }

        if (!data.get("formulario").toString().isEmpty()) {
            try {
                formulario = Integer.valueOf(data.get("formulario").toString());
            } catch (NumberFormatException numberFormatException) {
                throw new MessageException("Número de formulario no válido (ingrese solo números)");
            }
        }
        documentoFecha = (Date) data.get("documentoFecha");
//        if (documentoFecha == null) {
//            throw new MessageException("Fecha de documento no válida");
//        }
        if (!data.get("observacion").toString().isEmpty()) {
            observacion = (String) data.get("observacion");
        }

        CronicoDetalle detalle = new CronicoDetalle(getNextOrderIndex(entity), td, std, afiliado, familiar, numeroDocumento, formulario, documentoFecha, observacion, entity);
        return detalle;
    }

    private void checkConstraints(CronicoDetalle toAdd) throws MessageException {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (int row = 0; row < dtm.getRowCount(); row++) {
            CronicoDetalle old = (CronicoDetalle) dtm.getValueAt(row, 0);
            if (Objects.equals(old.getTipoDocumento(), toAdd.getTipoDocumento())
                    && Objects.equals(old.getSubTipoDocumento(), toAdd.getSubTipoDocumento())
                    && Objects.equals(old.getNumeroAfiliado(), toAdd.getNumeroAfiliado())
                    && Objects.equals(old.getNumeroFamiliar(), toAdd.getNumeroFamiliar())
                    && Objects.equals(old.getNumeroFormulario(), toAdd.getNumeroFormulario())
                    && Objects.equals(old.getNumeroDocumento(), toAdd.getNumeroDocumento())
                    && UTIL.compararIgnorandoTimeFields(old.getDocumentoFecha(), toAdd.getDocumentoFecha()) == 0) {
                throw new MessageException("Ya existe un detalle con los mismos datos:"
                        + "\nTipo de Documento: " + old.getTipoDocumento().getNombre()
                        + (old.getSubTipoDocumento() == null ? "" : "\nSub-Tipo de Documento: " + old.getSubTipoDocumento().getNombre())
                        + "\nN° Afiliado:" + old.getNumeroAfiliado() + "/" + old.getNumeroFamiliar()
                        + "\nN° Documento:" + old.getNumeroDocumento()
                        + "\nN° Formulario: " + old.getNumeroFormulario()
                        + (old.getDocumentoFecha() == null ? "" : "\nFecha:" + UTIL.DATE_FORMAT.format(old.getDocumentoFecha()))
                );
            }
        }
        List<CronicoDetalle> old = jpaController.findIdenticalDetalle(toAdd);
        //excluyendo los resultados de la misma caja
        for (Iterator<CronicoDetalle> it = old.iterator(); it.hasNext();) {
            CronicoDetalle detalle = it.next();
            if (Objects.equals(detalle.getCronico().getId(), entity.getId())) {
                it.remove();
            }
        }
        if (!old.isEmpty()) {
            String warn = "Existen registros de archivos con la misma información en las siguientes cajas:";
            for (CronicoDetalle detalle : old) {
                warn += "\n" + SGDUtilities.getBarcode(detalle.getCronico());
            }
            JOptionPane.showMessageDialog(customABMJDialog, warn, "Posible duplicación de carga de archivos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarTablaDetalle(CronicoDetalle detalle) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.addRow(new Object[]{
            detalle,
            detalle.getTipoDocumento().getNombre(),
            detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
            detalle.getNumeroAfiliado() + "/" + detalle.getNumeroFamiliar(),
            detalle.getNumeroDocumento(),
            detalle.getNumeroFormulario(),
            UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
            detalle.getObservacion()
        });
    }

    private void borrarDetalle() {
        int[] selectedRows = abmPanel.getjTable1().getSelectedRows();
        for (int ii = 0; ii < selectedRows.length; ii++) {
            int selectedRow = selectedRows[ii];
            DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
            CronicoDetalle candidate = (CronicoDetalle) dtm.getValueAt(selectedRow, 0);
            CronicoDetalle removed = null;
            for (int i = 0; i < entity.getDetalle().size(); i++) {
                CronicoDetalle ad = entity.getDetalle().get(i);
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

    private void initBuscador(Window owner) {
        buscadorPanel = new BuscadorCronicoPanel();
        SGDUtilities.volcar(abmPanel.getCbInstitucion(), buscadorPanel.getCbInstitucion(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbTipoDocumento(), buscadorPanel.getCbTipoDocumento(), "<Elegir>");
        SGDUtilities.volcar(abmPanel.getCbSubTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), "<Elegir>");
        buscadorPanel.getCbInstitucion().setSelectedIndex(0);
        buscadorPanel.getCbTipoDocumento().setSelectedIndex(0);
        buscadorPanel.getCbSubTipoDocumento().setSelectedIndex(0);
        new TipoDocumentoComboListener(buscadorPanel.getCbTipoDocumento(), buscadorPanel.getCbSubTipoDocumento(), true);
        buscador = new JDBuscador(owner, true, buscadorPanel, "Buscador de Crónicos");
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
                    entity = (Cronico) UTIL.getSelectedValue(buscador.getjTable1(), 0);
                    buscador.setEligio(true);
                    buscador.dispose();
                }
            }
        });

        buscador.addListener(this);
        buscador.setLocationRelativeTo(owner);
        buscador.setVisible(true);
    }

    private String getBuscadorQuery() {
        StringBuilder sb = new StringBuilder("SELECT o FROM " + CronicoDetalle.class.getSimpleName() + " o "
                + " WHERE o.id is not null ");

        Map<String, Object> data = buscadorPanel.getData();
        sb.append("AND o.archivo.baja = ").append(data.get("baja"));
        if (data.get("i") != null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<UsuarioSector> cb = (ComboBoxWrapper<UsuarioSector>) data.get("i");
            sb.append(" AND o.").append(CronicoDetalle_.cronico.getName()).append(".institucion.id = ").append(cb.getEntity().getInstitucion().getId());
        } else {
            List<Institucion> institucionesList = new UsuarioSectorJPAController().findInstituciones(UsuarioController.getCurrentUser());
            Iterator<Institucion> it = institucionesList.iterator();
            sb.append(" AND (");
            while (it.hasNext()) {
                Institucion institucion = it.next();
                sb.append(" o.").append(CronicoDetalle_.cronico.getName()).append(".institucion.id = ").append(institucion.getId());
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
        if (data.get("afiliado").toString().length() > 0) {
            try {
                String[] split = data.get("afiliado").toString().split("/");
                Integer na = Integer.valueOf(split[0]);
                sb.append(" AND o.numeroAfiliado = ").append(na);
                if (split.length > 1) {
                    Integer nf = Integer.valueOf(split[0]);
                    sb.append(" AND o.numeroFamiliar = ").append(nf);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Número de familiar no válido"
                        + "\nFormatos válidos: 1234, 001234, 1234/1, 1234/01, 001234/01");
            }
        }
        if (data.get("documento").toString().length() > 0) {
            try {
                Integer na = Integer.valueOf(data.get("documento").toString());
                sb.append(" AND o.numeroDocumento = ").append(na);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Número del documento no válido (ingrese solo números)");
            }
        }
        if (data.get("observacion").toString().length() > 0) {
            sb.append(" AND UPPER(o.observacion) LIKE UPPER('%").append(data.get("observacion")).append("%')");
        }
        if (!data.get("caja").toString().isEmpty()) {
            sb.append(" AND o.").append(CronicoDetalle_.cronico.getName()).append(".codigo=").append(data.get("caja"));
        }
        LOG.trace(sb.toString());
        return sb.toString();
    }

    private void cargarTablaBuscador(List<CronicoDetalle> list) {
        DefaultTableModel dtm = buscador.getDtm();
        dtm.setRowCount(0);
        for (CronicoDetalle detalle : list) {
            dtm.addRow(new Object[]{
                detalle.getCronico(),
                detalle.getTipoDocumento().getNombre(),
                detalle.getSubTipoDocumento() != null ? detalle.getSubTipoDocumento().getNombre() : null,
                detalle.getNumeroAfiliado() + "/" + detalle.getNumeroFamiliar(),
                detalle.getNumeroDocumento(),
                detalle.getNumeroFormulario(),
                UTIL.DATE_FORMAT.format(detalle.getDocumentoFecha()),
                detalle.getObservacion(),
                detalle.getCronico().getBarcode(),
                detalle.getCronico().getPrecintos().isEmpty() ? "No" : "Si " + detalle.getCronico().getPrecintos().size(),
                detalle.getCronico().getRecibo() != null ? detalle.getCronico().getRecibo().getNumero() : null
            });
        }
    }

    private void setPanelABM(Cronico o) {
        UTIL.setSelectedItem(abmPanel.getCbInstitucion(), o.getInstitucion().getNombre());
        abmPanel.getBtnPrecintos().setEnabled(!o.getPrecintos().isEmpty());
        abmPanel.setBarcode(SGDUtilities.getBarcode(o));
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        for (CronicoDetalle detalle : o.getDetalle()) {
            cargarTablaDetalle(detalle);
        }
    }

    private void doReport(Cronico o) throws MissingReportException, JRException {
        Reportes r = new Reportes(DAO.getJDBCConnection(), SGD.getResources().getString("report.codigobarra"), "Archivo " + o.getClass().getSimpleName() + " N" + o.getBarcode());
        r.addParameter("TABLA", o.getClass().getSimpleName());
        r.addParameter("ID_TABLA", o.getId());
       r.viewReport();
    }

    private void btnNuevoAction() {
        entity = new Cronico();
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

    CustomABMJDialog getAbmRecibos(Window owner) {
        return new ReciboController().getAbm(owner, sectorUI);
    }

    CustomABMJDialog getAbmRecepcion(Window owner) {
        return new RecepcionController().getAbm(owner, sectorUI);
    }

    CustomABMJDialog viewArchivo(Cronico o) {
        abmPanel = new ABMCronicoPanel();
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        abmPanel.getCbInstitucion().addItem(o.getInstitucion().getNombre());
        setPanelABM(o);
        CustomABMJDialog ccustomABMJDialog = new CustomABMJDialog(null, abmPanel, "Archivo " + o.getClass().getSimpleName() + " " + o.getBarcode(), true, null);
        ccustomABMJDialog.setToolBarVisible(false);
        ccustomABMJDialog.setBottomButtonsVisible(false);
        ccustomABMJDialog.setPanelComponentsEnabled(false);
        return ccustomABMJDialog;
    }

    private void removePrecintos(Cronico entity) {
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
            Logger.getLogger(this.getClass()).error(ex.getLocalizedMessage(), ex);
        }
    }

    void enviado(Integer archivoId, Recibo recibo) {
        Cronico o = jpaController.find(archivoId);
        o.setRecibo(recibo);
        jpaController.merge(o);
    }

    void recepcionar(Integer archivoId) {
        Cronico o = jpaController.find(archivoId);
        o.setRecibo(null);
        jpaController.merge(o);
    }

    private Integer getNextOrderIndex(Cronico o) {
        if (o.getDetalle() == null) {
            o.setDetalle(new ArrayList<CronicoDetalle>());
        }
        int orderIndex = 0;
        for (CronicoDetalle d : o.getDetalle()) {
            if (d.getOrderIndex() > orderIndex) {
                orderIndex = d.getOrderIndex();
            }
        }
        return orderIndex + 1;
    }
}
