package sgd.controller;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import sgd.controller.exception.MessageException;
import sgd.gui.panel.MiniABMPanel;
import sgd.jpa.controller.*;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class TipoDocumentoController implements ActionListener {

    private MiniABMPanel abmPanel;
    private CustomABMJDialog customABMJDialog;
    private TipoDocumento entity;
    private TipoDocumentoJPAController jPAController;
    private static final String INFO_ABM_TEXT
            = "Los Tipos de Documentos definen criterios de agrupación de la documentación utilizada."
            + "\nEstos a su vez pueden o no tener \"Sub-Tipos\" para una mejor clasificación y división de los mismos.";

    public TipoDocumentoController() {
        jPAController = new TipoDocumentoJPAController();
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new MiniABMPanel();
        abmPanel.setVisibleInstitucion(false);
//        abmPanel.setVisibleSector(false);
        abmPanel.setVisibleTipoDoc(false);
        abmPanel.setVisibleSubTipoDoc(false);
        List<UsuarioSector> permisos = new UsuarioSectorJPAController().findByUsuario(UsuarioController.getCurrentUser());
        List<ComboBoxWrapper<Sector>> sectoresList = new ArrayList<>();
        for (UsuarioSector permiso : permisos) {
            if (permiso.getPermiso() == 2) {
                Sector sector = permiso.getSector();
                ComboBoxWrapper<Sector> cbw = new ComboBoxWrapper<>(sector, sector.getSectorUI().getCode(), sector.getSectorUI().getNombre());
                if (!sectoresList.contains(cbw)) {
                    sectoresList.add(cbw);
                }
            }
        }
        //carga los sectores en los cuales tiene permiso de administrador
        UTIL.loadComboBox(abmPanel.getCbSector(), sectoresList, false);
        UTIL.getDefaultTableModel(abmPanel.getjTable1(), new String[]{"entity", "Tipo Documento", "Sub-Tipo cant"});
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);
        cargarTablaABM();
        abmPanel.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                entity = (TipoDocumento) abmPanel.getjTable1().getModel().getValueAt(abmPanel.getjTable1().getSelectedRow(), 0);
                setPanelABM();
                customABMJDialog.setPanelComponentsEnabled(true);
                abmPanel.getCbSector().setEnabled(false);
            }
        });
        abmPanel.getCbSector().addActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM Tipo Documento", true, INFO_ABM_TEXT);
        customABMJDialog.getBtnExtraBottom().setText("Ver Sub Tipos");
        customABMJDialog.getBtnExtraBottom().setIcon(new ImageIcon(getClass().getResource(CustomABMJDialog.RESOURCE_FOLDER_PATH + "32px_view_detail.png")));
        customABMJDialog.getBtnExtraBottom().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (entity != null) {
                    if (entity.getSubTipoDocumentos() == null || entity.getSubTipoDocumentos().isEmpty()) {
                        JOptionPane.showMessageDialog(customABMJDialog, "No posee Sub tipos de documentos asociados");
                    } else {
                        String s = "";
                        int i = 0;
                        for (SubTipoDocumento std : entity.getSubTipoDocumentos()) {
                            i++;
                            s += "\n" + i + " " + std.getNombre();
                        }
                        JOptionPane.showMessageDialog(customABMJDialog, s, "Sub Tipos de documentos relacionados", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        customABMJDialog.setPanelComponentsEnabled(false);
        customABMJDialog.addBottomButtonsActionListener(this);
        customABMJDialog.addToolBarButtonsActionListener(this);
        return customABMJDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (customABMJDialog != null && customABMJDialog.isActive()) {
            //<editor-fold defaultstate="collapsed" desc="Button actions">
            if (e.getSource().getClass().equals(JButton.class)) {
                if (e.getSource().equals(customABMJDialog.getBtnAceptar())) {
                    //<editor-fold defaultstate="collapsed" desc="aceptar action">
                    try {
                        if (entity == null) {
                            throw new IllegalArgumentException("Seleccione Agregar para crear una Institución nueva o Modificar seleccione una de la tabla inferior para una existente.");
                        }
                        String msj = entity.getId() == null ? "Registrado.." : "Modificado..";
                        setEntity();
                        persistEntity();
                        cargarTablaABM();
                        customABMJDialog.showMessage(msj, null, 1);
                        customABMJDialog.setPanelComponentsEnabled(false);
                        abmPanel.resetUI();
                        entity = null;
                    } catch (IllegalArgumentException ex) {
                        customABMJDialog.showMessage(ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
                    } catch (MessageException ex) {
                        customABMJDialog.showMessage(ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
                    } catch (Exception ex) {
                        customABMJDialog.showMessage(ex.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger(this.getClass()).error(ex.getLocalizedMessage(), ex);
                    }
                    //</editor-fold>
                } else if (e.getSource().equals(customABMJDialog.getBtnCancelar())) {
                    customABMJDialog.setPanelComponentsEnabled(false);
                    abmPanel.resetUI();
                    entity = null;
                } else if (e.getSource().equals(customABMJDialog.getBtnNuevo())) {
                    entity = new TipoDocumento();
                    abmPanel.resetUI();
                    customABMJDialog.setPanelComponentsEnabled(true);
                    abmPanel.getCbSector().requestFocusInWindow();
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    if (entity != null && entity.getId() != null) {
                        List<SubTipoDocumento> subTiposList = new SubTipoDocumentoJPAController().findByTipoDocumento(entity);
                        boolean usado = isUsadoPorUnDetalleArchivo(entity);
                        if (!usado) {
                            String extra = "";
                            if (!subTiposList.isEmpty()) {
                                extra = "Este Tipo de Documento contiene " + subTiposList.size() + "Sub-Tipo(s).\n";
                            }
                            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(customABMJDialog, extra
                                    + "Confirmar eliminación del Tipo de Documento: " + entity.getNombre(), "Confirmación", JOptionPane.YES_NO_OPTION)) {
                                jPAController.remove(entity);
                                abmPanel.resetUI();
                                customABMJDialog.setPanelComponentsEnabled(false);
                                entity = null;
                                cargarTablaABM();
                            }
                        } else {
                            customABMJDialog.showMessage("No se puede eliminar el registro porque está relaciona a Archivos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        customABMJDialog.showMessage("naranja por borrar....", "Aún..", 0);
                    }
                } else if (e.getSource().equals(customABMJDialog.getBtnBuscar())) {
                    customABMJDialog.showMessage("Para editar un Tipo de Documento, debe seleccionar uno de tabla", null, JOptionPane.INFORMATION_MESSAGE);
                } else if (e.getSource().equals(customABMJDialog.getBtnEditar())) {
                    customABMJDialog.showMessage("Para editar un Tipo de Documento, debe seleccionar uno de tabla", null, JOptionPane.INFORMATION_MESSAGE);
                }
            } //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ComboBox actions">
            else if (e.getSource().getClass().equals(JComboBox.class)) {
                if (e.getSource().equals(abmPanel.getCbSector())) {
                    cargarTablaABM();
                }
            }
            //</editor-fold>
        }
    }

    private void setEntity() throws MessageException {
        String nombre = abmPanel.getTfNombre().getText().trim();
        if (nombre.length() < 1) {
            throw new MessageException("Nombre no válido");
        } else if (nombre.length() > 50) {
            throw new MessageException("Nombre ridiculisisisimamente largo! (Máximo 50 caracteres)");
        }
        entity.setNombre(nombre.toUpperCase());
        if (entity.getSector() == null) {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<Sector> cbw = (ComboBoxWrapper<Sector>) abmPanel.getCbSector().getSelectedItem();
            Sector sector = cbw.getEntity();
            entity.setSector(sector);
        }
    }

    private void persistEntity() throws MessageException {
        TipoDocumentoJPAController jPAcontroller = new TipoDocumentoJPAController();
        try {
            TipoDocumento findByNombre = jPAcontroller.findByNombre(entity.getSector(), entity.getNombre());
            if (entity.getId() != null && !entity.getId().equals(findByNombre.getId())) {
                throw new MessageException("Ya existe un Tipo de Documento con el nombre " + entity.getNombre());
            }
        } catch (NoResultException noResultException) {
            //ignored...
        }
        if (entity.getId() == null) {
            jPAcontroller.create(entity);
        } else {
            jPAcontroller.merge(entity);
        }
    }

    private void setPanelABM() {
        abmPanel.getTfNombre().setText(entity.getNombre());
    }

    private void cargarTablaABM() {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        try {
            @SuppressWarnings("unchecked")
            ComboBoxWrapper<Sector> cbw = (ComboBoxWrapper<Sector>) abmPanel.getCbSector().getSelectedItem();
            Sector sector = cbw.getEntity();
            List<TipoDocumento> findBy = new TipoDocumentoJPAController().findBy(sector);
            for (TipoDocumento tipoDocumento : findBy) {
                dtm.addRow(new Object[]{
                    tipoDocumento,
                    tipoDocumento.getNombre(),
                    tipoDocumento.getSubTipoDocumentos().size()
                });
            }
        } catch (ClassCastException e) {
        }
    }

    private boolean isUsadoPorUnDetalleArchivo(TipoDocumento td) {
        List<TipoDocumento> l;
        SectorUI sectorUI = td.getSector().getSectorUI();
        Class<?> detalle = null;
        if (sectorUI.equals(SectorUI.AFILIACION)) {
            detalle = AfiliacionDetalle.class;
        } else if (sectorUI.equals(SectorUI.APE)) {
            detalle = ApeDetalle.class;
        } else if (sectorUI.equals(SectorUI.CONTABLE)) {
            detalle = ContableDetalle.class;
        } else if (sectorUI.equals(SectorUI.FACTURACION)) {
            detalle = FacturacionDetalle.class;
        } else if (sectorUI.equals(SectorUI.GREMIALES)) {
            detalle = GremialesDetalle.class;
        } else if (sectorUI.equals(SectorUI.AUDITORIA)) {
            detalle = AuditoriaDetalle.class;
        } else if (sectorUI.equals(SectorUI.PSICOFISICO)) {
            detalle = PsicofisicoDetalle.class;
        } else if (sectorUI.equals(SectorUI.CRONICO)) {
            detalle = CronicoDetalle.class;
        } else if (sectorUI.equals(SectorUI.DISCAPACIDAD)) {
            detalle = DiscapacidadDetalle.class;
        }
         else if (sectorUI.equals(SectorUI.AUDITORIAMEDICA)) {
            detalle = AuditoriaMedicaDetalle.class;
        }
        String query = "SELECT d.tipoDocumento FROM " + detalle.getSimpleName() + " d WHERE d.tipoDocumento.id = " + td.getId();
        l = jPAController.findUsoTipoDocumento(query, td);
        return !l.isEmpty();
    }
}
