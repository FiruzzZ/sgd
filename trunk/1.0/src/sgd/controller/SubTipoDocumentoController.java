package sgd.controller;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.NoResultException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import sgd.controller.exception.MessageException;
import sgd.gui.panel.MiniABMPanel;
import sgd.jpa.controller.SubTipoDocumentoJPAController;
import sgd.jpa.controller.TipoDocumentoJPAController;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.Sector;
import sgd.jpa.model.SubTipoDocumento;
import sgd.jpa.model.TipoDocumento;
import sgd.jpa.model.UsuarioSector;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class SubTipoDocumentoController implements ActionListener {

    private MiniABMPanel abmPanel;
    private CustomABMJDialog customABMJDialog;
    private SubTipoDocumento entity;
    private static final String INFO_ABM_TEXT =
            "<html>Los Sub-Tipos de Documentos son elementos que complementan al Tipo de Documento."
            + "<br>Estos representan una descripción mas específica de un Documento.</html>";

    public SubTipoDocumentoController() {
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new MiniABMPanel();
        abmPanel.setVisibleInstitucion(false);
        abmPanel.setVisibleSubTipoDoc(false);
        //<editor-fold defaultstate="collapsed" desc="carga los sectores en los cuales tiene permiso de administrar">
        List<UsuarioSector> findByUsuario = new UsuarioSectorJPAController().findByUsuario(UsuarioController.getCurrentUser());
        List<ComboBoxWrapper<Sector>> sectoresList = new ArrayList<>();
        for (UsuarioSector usuarioSector : findByUsuario) {
            if (usuarioSector.getPermiso() == 2) {
                Sector sector = usuarioSector.getSector();
                ComboBoxWrapper<Sector> cbw = new ComboBoxWrapper<>(sector, sector.getSectorUI().getCode(), sector.getSectorUI().getNombre());
                if (!sectoresList.contains(cbw)) {
                    sectoresList.add(cbw);
                }
            }
        }
        UTIL.loadComboBox(abmPanel.getCbSector(), sectoresList, false);

        //</editor-fold>
        UTIL.getDefaultTableModel(abmPanel.getjTable1(), new String[]{"entity", "Sector", "Tipo Documento", "Sub-Tipo Documento"});
        UTIL.hideColumnTable(abmPanel.getjTable1(), 0);

        abmPanel.getCbSector().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTipoDocumentos();
            }
        });
        abmPanel.getCbTipoDocumento().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (abmPanel.getCbTipoDocumento().getSelectedIndex() > -1) {
                    cargarTablaABM();
                }
            }
        });
        abmPanel.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                entity = (SubTipoDocumento) abmPanel.getjTable1().getModel().getValueAt(abmPanel.getjTable1().getSelectedRow(), 0);
                setPanelABM();
                customABMJDialog.setPanelComponentsEnabled(true);
                abmPanel.getCbSector().setEnabled(false);
                abmPanel.getCbTipoDocumento().setEnabled(false);
            }
        });

        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM Sub-Tipo Documento", true, INFO_ABM_TEXT);
        customABMJDialog.getBtnExtraBottom().setVisible(false);
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
                    customABMJDialog.setPanelComponentsEnabled(true);
                    abmPanel.resetUI();
                    entity = new SubTipoDocumento();
                    abmPanel.getCbSector().requestFocusInWindow();
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    if (entity != null && entity.getId() != null) {
//                        new SubTipoDocumentoJPAController().findByTipoDocumento(entity);
//                        if (institucionCount > 0) {
//                            customABMJDialog.showMessage("No se puede eliminar esta Institución porque hay registros ("
//                                    + institucionCount + ") que depende de ella.", "Restricción", JOptionPane.WARNING_MESSAGE);
//                        } else {
//                            new TipoDocumentoJPAController().remove(entity);
//                            //hacer lo que haría el botón CANCELAR
//                            actionPerformed(new ActionEvent(customABMJDialog.getBtnCancelar(), 0, null));
//                        }
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
//                if (e.getSource().equals(abmPanel.getCbSector())) {
//                } else if (e.getSource().equals(abmPanel.getCbTipoDocumento())) {
//                }
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
        if (entity.getTipoDocumento() == null) {
            try {
                @SuppressWarnings("unchecked")
                ComboBoxWrapper<TipoDocumento> cbw = (ComboBoxWrapper<TipoDocumento>) abmPanel.getCbTipoDocumento().getSelectedItem();
                TipoDocumento tipoDocumento = cbw.getEntity();
                entity.setTipoDocumento(tipoDocumento);
            } catch (ClassCastException e) {
                throw new MessageException("Tipo de Documento no válido");
            }
        }
        entity.setNombre(nombre.toUpperCase());
    }

    private void persistEntity() throws MessageException {
        SubTipoDocumentoJPAController jPAController = new SubTipoDocumentoJPAController();
        try {
            SubTipoDocumento findByNombre = jPAController.findByNombre(entity.getTipoDocumento(), entity.getNombre());
            if (entity.getId() != null && !entity.getId().equals(findByNombre.getId())) {
                throw new MessageException("Ya existe un Sub-Tipo de Documento con el nombre " + entity.getNombre()
                        + " para el Tipo Documento " + entity.getTipoDocumento().getNombre() + ".");
            }
        } catch (NoResultException noResultException) {
            //ignored...
        }
        if (entity.getId() == null) {
            jPAController.create(entity);
        } else {
            jPAController.merge(entity);
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
            ComboBoxWrapper<TipoDocumento> cbw = (ComboBoxWrapper<TipoDocumento>) abmPanel.getCbTipoDocumento().getSelectedItem();
            TipoDocumento tipoDocumento = cbw.getEntity();
            List<SubTipoDocumento> findBy = new SubTipoDocumentoJPAController().findByTipoDocumento(tipoDocumento);
            for (SubTipoDocumento subTipoDocumento : findBy) {
                dtm.addRow(new Object[]{
                            subTipoDocumento,
                            tipoDocumento.getSector().getSectorUI().getNombre(),
                            tipoDocumento.getNombre(),
                            subTipoDocumento.getNombre()
                        });
            }
        } catch (ClassCastException ex) {
            //ignored...
        }
    }

    private void cargarTipoDocumentos() {
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<Sector> cbw = (ComboBoxWrapper<Sector>) abmPanel.getCbSector().getSelectedItem();
        Sector sector = cbw.getEntity();
        List<TipoDocumento> tipoDocumentos = sector.getTipoDocumentos();
        List<ComboBoxWrapper<TipoDocumento>> l = new ArrayList<>(tipoDocumentos.size());
        for (TipoDocumento tipoDocumento : tipoDocumentos) {
            ComboBoxWrapper<TipoDocumento> cbw2 = new ComboBoxWrapper<>(tipoDocumento, tipoDocumento.getId(), tipoDocumento.getNombre());
            l.add(cbw2);
        }
        UTIL.loadComboBox(abmPanel.getCbTipoDocumento(), l, false, "<Sin Tipo Documentos>");
    }
}
