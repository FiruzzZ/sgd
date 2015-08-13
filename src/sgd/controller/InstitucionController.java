/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgd.controller;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import sgd.controller.exception.MessageException;
import sgd.gui.panel.MiniABMPanel;
import sgd.jpa.controller.InstitucionJPAController;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.Institucion;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
class InstitucionController implements ActionListener {

    private MiniABMPanel abmPanel;
    private CustomABMJDialog customABMJDialog;
    private Institucion entity;
    private static final String INFO_ABM_TEXT = "<html>Las instituciones representan a quien pertenece la documentación utilizada por los distintos sectores</html>";

    public InstitucionController() {
    }

    CustomABMJDialog getAbm(Window owner) {
        abmPanel = new MiniABMPanel();
        abmPanel.setVisibleInstitucion(false);
        abmPanel.setVisibleSector(false);
        abmPanel.setVisibleTipoDoc(false);
        abmPanel.setVisibleSubTipoDoc(false);
        cargarTablaABM();
        abmPanel.getjTable1().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                @SuppressWarnings("unchecked")
                ComboBoxWrapper<Institucion> cbw = (ComboBoxWrapper<Institucion>) abmPanel.getjTable1().getModel().getValueAt(abmPanel.getjTable1().getSelectedRow(), 0);
                entity = cbw.getEntity();
                setPanelABM(entity);
                customABMJDialog.setPanelComponentsEnabled(true);
            }
        });
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM Instituciones", true, INFO_ABM_TEXT);
        customABMJDialog.getBtnExtraBottom().setVisible(false);
        customABMJDialog.getBtnBuscar().setEnabled(false);
        customABMJDialog.getBtnEditar().setEnabled(false);
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
                    entity = new Institucion();
                    abmPanel.getTfNombre().requestFocus();
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    if (entity != null && entity.getId() != null) {
                        int institucionCount = new UsuarioSectorJPAController().findByInstitucion(entity);
                        if (institucionCount > 0) {
                            customABMJDialog.showMessage("No se puede eliminar esta Institución porque hay registros ("
                                    + institucionCount + ") que depende de ella.", "Restricción", JOptionPane.WARNING_MESSAGE);
                        } else {
                            new InstitucionJPAController().remove(entity);
                            //hacer lo que haría el botón CANCELAR
                            customABMJDialog.showMessage("Eliminado.. (" + entity.getId() + ")", null, JOptionPane.INFORMATION_MESSAGE);
                            actionPerformed(new ActionEvent(customABMJDialog.getBtnCancelar(), 0, null));
                            cargarTablaABM();
                        }
                    } else {
                        customABMJDialog.showMessage("naranja por borrar....", "Aún..", 0);
                    }
                }
            }
        }
    }

    private void setEntity() throws MessageException {
        String nombre = abmPanel.getTfNombre().getText().trim();
        if (nombre.length() < 1) {
            throw new MessageException("Nombre no válido");
        } else if (nombre.length() > 50) {
            throw new MessageException("Nombre ridiculisisisimamente largo!");
        }
        entity.setNombre(nombre.toUpperCase());
    }

    private void persistEntity() throws MessageException {
        InstitucionJPAController jPAcontroller = new InstitucionJPAController();
        try {
            Institucion findByNombre = jPAcontroller.findByNombre(entity.getNombre());
            if (entity.getId() != null && !entity.getId().equals(findByNombre.getId())) {
                throw new MessageException("Ya existe una institución con el nombre " + entity.getNombre());
            }
        } catch (NoResultException e) {
            //ignored...
        }
        if (entity.getId() == null) {
            jPAcontroller.create(entity);
            new UsuarioSectorJPAController().updateAdmin(entity); // OJO CON ESTE!!
        } else {
            jPAcontroller.merge(entity);
        }
    }

    private void setPanelABM(Institucion entity) {
        abmPanel.getTfNombre().setText(entity.getNombre());
    }

    private void cargarTablaABM() {
        List<Institucion> findAll = new InstitucionJPAController().findAll();
        List<ComboBoxWrapper<Institucion>> l = new ArrayList<>(findAll.size());
        for (Institucion institucion : findAll) {
            l.add(new ComboBoxWrapper<>(institucion, institucion.getId(), institucion.getNombre()));
        }
        UTIL.getDefaultTableModel(abmPanel.getjTable1(), new String[]{"Institución", "Código"});
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        for (ComboBoxWrapper<Institucion> comboBoxWrapper : l) {
            dtm.addRow(new Object[]{comboBoxWrapper, comboBoxWrapper.getEntity().getId()});
        }
    }
}
