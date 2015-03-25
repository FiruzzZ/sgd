package sgd.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import sgd.controller.exception.MessageException;
import sgd.gui.*;
import sgd.gui.panel.ABMUsuariosPanel;
import sgd.gui.panel.BuscadorUsuarioPanel;
import sgd.jpa.controller.UsuarioJPAController;
import sgd.jpa.controller.UsuarioSectorJPAController;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 * Administra al usuario "actual"
 *
 * @author FiruzzZ
 */
public class UsuarioController implements ActionListener, KeyListener {

    private static Usuario CURRENT_USER;
    private JDBuscador buscador = null;
    private CustomABMJDialog customABMJDialog;
    private JDLogin jDLogin;
    private final String[] colsName = {"Entity.instance", "Nombre", "Bloqueado", "Fecha Alta"};
    private final int[] colsWidth = {15, 50, 30, 50};
    private ABMUsuariosPanel abmPanel;
    private BuscadorUsuarioPanel buscadorPanel;
    private JDcambiarPass jdCambiarPass;
    //global mutables
    private Usuario entity;
    private final UsuarioJPAController jPAController;

    public UsuarioController() {
        jPAController = new UsuarioJPAController();
    }

    /**
     * Retorna el usuario actualmente logea.
     *
     * @return
     */
    public static Usuario getCurrentUser() {
        if (CURRENT_USER != null) {
            CURRENT_USER = new UsuarioJPAController().find(CURRENT_USER.getId());
        }
        return CURRENT_USER;
    }

    public static void setCurrentUser(Usuario usuario) {
        CURRENT_USER = usuario;
    }

    /**
     *
     * @param codigoUI
     * @return 1 = LECTURA (VIEW), 2 = TODO (CRUD) or <code>null</code>
     */
    static Integer getPermisoByCodigoPantalla(Integer codigoUI) {
        Integer permiso = null;
        try {
            new UsuarioJPAController().refresh(CURRENT_USER);
        } catch (IllegalArgumentException ex) {
            //when the entity is not managed
            CURRENT_USER = new UsuarioJPAController().find(CURRENT_USER.getId());
        }
        for (UsuarioSector usuarioSector : CURRENT_USER.getUsuarioSectores()) {
            if (usuarioSector.getSector().getSectorUI().getCode().equals(codigoUI)) {
                permiso = usuarioSector.getPermiso();
            }
        }
        return permiso;
    }

    CustomABMJDialog getABMUsuarioUI(Window owner) {
        abmPanel = new ABMUsuariosPanel();
        List<UsuarioSector> usuarioSectores = UsuarioController.getCurrentUser().getUsuarioSectores();
        List<ComboBoxWrapper<UsuarioSector>> ll = new ArrayList<>(5);
        for (UsuarioSector us : usuarioSectores) {
            if (us.getAdministraUsuarios()) {
                if (!SGDUtilities.isContained(ll, us.getInstitucion())) {
                    ComboBoxWrapper<UsuarioSector> cbw = new ComboBoxWrapper<>(us, us.getId(), us.getInstitucion().getNombre());
                    ll.add(cbw);
                }
            }
        }
        UTIL.loadComboBox(abmPanel.getCbInstitucion(), ll, false);
        cargarSectores();
        abmPanel.getCbInstitucion().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cargarSectores();
            }
        });
        abmPanel.getBtnAgregar().addActionListener(this);
        abmPanel.getBtnQuitar().addActionListener(this);
        customABMJDialog = new CustomABMJDialog(owner, abmPanel, "ABM - USUARIOS", true, null);
        customABMJDialog.getBtnExtraBottom().setVisible(false);
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
                            throw new IllegalArgumentException("Seleccione Agregar para crear un Usuario nuevo o Modificar para uno existente.");
                        }
                        String msj = entity.getId() == null ? "Registrado.." : "Modificado..";
                        setEntity();
                        persistEntity();
                        customABMJDialog.showMessage(msj, null, 1);
                        customABMJDialog.setPanelComponentsEnabled(false);
                        abmPanel.resetUI();
                        entity = null;
                    } catch (IllegalArgumentException ex) {
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
                    entity = new Usuario();
                    entity.setUsuarioSectores(new ArrayList<UsuarioSector>(3));
                } else if (e.getSource().equals(customABMJDialog.getBtnBorrar())) {
                    if (entity != null && entity.getId() != null) {
                        jPAController.remove(entity);
                        //hacer lo que haría el botón CANCELAR
                        actionPerformed(new ActionEvent(customABMJDialog.getBtnCancelar(), 0, null));
                    } else {
                        customABMJDialog.showMessage("naranja por borrar....", "Aún..", 0);
                    }
                } else if (e.getSource().equals(customABMJDialog.getBtnBuscar())) {
                    initBuscador();
                    customABMJDialog.setPanelComponentsEnabled(false);
                } else if (e.getSource().equals(customABMJDialog.getBtnEditar())) {
                    if (entity == null) {
                        JOptionPane.showMessageDialog(customABMJDialog, "Debe seleccionar un usuario");
                        return;
                    }
                    customABMJDialog.setPanelComponentsEnabled(true);
                } else if (e.getSource().equals(abmPanel.getBtnAgregar())) {
                    btnAgregarPermisoAction();
                    refreshTablaPermisos(entity);
                    abmPanel.getCheckAdminSector().setSelected(false);
                    abmPanel.getCheckAdminUser().setSelected(false);
                } else if (e.getSource().equals(abmPanel.getBtnQuitar())) {
                    btnQuitarPermisoAction();
                    refreshTablaPermisos(entity);
                }
            }
        }

    }

    /**
     *
     * @param nick
     * @param pwd
     * @return
     * @throws MessageException
     * @throws Exception
     */
    public Usuario checkLoginUser(String nick, String pwd) throws MessageException, Exception {
        try {
            CURRENT_USER = new UsuarioJPAController().find(nick, pwd);
            if (CURRENT_USER != null && CURRENT_USER.getBlocked()) {
                CURRENT_USER = null;
                throw new MessageException("Usuario deshabilitado");
            }
        } catch (NoResultException ex) {
            throw new MessageException("Usuario/Contraseña no válido");
        }
        return CURRENT_USER;
    }

    public JDialog initCambiarPass(java.awt.Frame frame) {
        jdCambiarPass = new JDcambiarPass(frame, true);
        jdCambiarPass.getLabelUsuario().setText(CURRENT_USER.getNombre());
        jdCambiarPass.setListener(this);
        return jdCambiarPass;
    }

    public void initLogin(JFrame frame) {
        jDLogin = new JDLogin(frame, true);
        jDLogin.setTitle("SGD - Identificación de Usuario");
        jDLogin.setListener(this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jDLogin.setLocation((screenSize.width - jDLogin.getWidth()) / 2, (screenSize.height - jDLogin.getHeight()) / 2);
        jDLogin.setAlwaysOnTop(true);
        jDLogin.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getComponent().getClass().equals(javax.swing.JTextField.class)) {
            JTextField tf = (JTextField) e.getComponent();
            // <editor-fold defaultstate="collapsed" desc="JDLogin">
            if (tf.getName().equalsIgnoreCase("ulogin") && e.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    CURRENT_USER = checkLoginUser(jDLogin.getTfU(), jDLogin.getPass());
                    if (CURRENT_USER != null) {
                        jDLogin.dispose();
                    }
                } catch (MessageException ex) {
                    jDLogin.getjLabel3().setText(ex.getMessage());
                } catch (Exception ex) {
                    jDLogin.showMessage(ex.getMessage(), "Error Login usuario", 0);
                }
            }// </editor-fold>
        } else if (e.getComponent() instanceof JPasswordField) {
            // <editor-fold defaultstate="collapsed" desc="JDLogin">
            if (jDLogin.isActive() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    CURRENT_USER = checkLoginUser(jDLogin.getTfU(), jDLogin.getPass());
                    if (CURRENT_USER != null) {
                        System.out.println("Usuario " + CURRENT_USER + "logeado.");
                        jDLogin.dispose();
                    }
                } catch (MessageException ex) {
                    System.out.println("Messageexcep.......");
                    jDLogin.getjLabel3().setText(ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Excep.......");
                    jDLogin.showMessage(ex.getMessage(), "Error Login usuario", 0);
                }
            }// </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="JDCambiarPass">
            else if (jdCambiarPass != null && jdCambiarPass.isActive() && (e.getKeyCode() == 10)) {
                try {
                    cambiarPwd();
                } catch (MessageException ex) {
                    jdCambiarPass.getLabelMsj().setText(ex.getMessage());
                }

            }// </editor-fold>
        }
    }

    private void cambiarPwd() throws MessageException {
        if (jdCambiarPass.getPass1().equals(CURRENT_USER.getPwd())) {
            if (jdCambiarPass.getPass2().equals(jdCambiarPass.getPass3())) {
                if (jdCambiarPass.getPass2().length() >= 5) {
                    CURRENT_USER.setPwd(jdCambiarPass.getPass2());
                    jPAController.merge(CURRENT_USER);
                    jdCambiarPass.getLabelMsj().setText("Contraseña actualizada");
                } else {
                    throw new MessageException("<html>La contraseña debe tener al menos<br> 5 caracteres</html>");
                }
            } else {
                throw new MessageException("Las nuevas contraseñas no coinciden");
            }
        } else {
            throw new MessageException("La contraseña actual no es correcta");
        }
    }

    private void setPanelABM(Usuario u) {
        abmPanel.getjTextField1().setText(u.getNombre());
        abmPanel.getjPasswordField1().setText(u.getPwd());
        abmPanel.getCheckBlocked().setSelected(u.getBlocked());
        refreshTablaPermisos(u);
    }

    private void setEntity() {
        List<String> errores = new ArrayList<>(2);
        Map<String, Object> data = abmPanel.getData();
        String nombre = (String) data.get("nombre");
        if (nombre == null || nombre.length() < 1) {
            errores.add("Nombre de usuario no válido");
        }
        String pwd = (String) data.get("pwd");
        if (pwd == null || pwd.length() <= 4) {
            errores.add("Contraseña no válida, debe tener al menos 5 caracteres");
        }

        if (!errores.isEmpty()) {
            StringBuilder sb = new StringBuilder(50);
            for (String string : errores) {
                sb.append(string).append("\n");
            }
            throw new IllegalArgumentException(sb.toString());
        }

        //set of Entity........................
        entity.setNombre(nombre);
        entity.setPwd(pwd);
        entity.setBlocked((Boolean) data.get("blockeado"));
    }

    private void persistEntity() {
        if (entity.getId() == null) {
            jPAController.create(entity);
        } else {
            entity = jPAController.merge(entity);
        }
    }

    private void modificarPermiso(Institucion i, Sector s, int permiso, boolean adminSector, boolean adminUsuario) {
        for (UsuarioSector usuarioSector : entity.getUsuarioSectores()) {
            if (usuarioSector.getInstitucion().equals(i) && usuarioSector.getSector().equals(s)) {
                usuarioSector.setPermiso(permiso);
                usuarioSector.setAdministraSector(adminSector);
                usuarioSector.setAdministraUsuarios(adminUsuario);
                break;
            }
        }
    }

    private JDialog getBuscador(JDialog owner, boolean enableDisposeOnMouseDoubleClick) {
        buscadorPanel = new BuscadorUsuarioPanel();
        buscador = new JDBuscador(owner, true, buscadorPanel, "Buscador de Usuarios");
        UTIL.getDefaultTableModel(buscador.getjTable1(), colsName, colsWidth, new Class<?>[]{null, null, Boolean.class, null});
        UTIL.hideColumnTable(buscador.getjTable1(), 0);
        List<Usuario> l = jPAController.findAll();
        DefaultTableModel dtm = buscador.getDtm();
        for (Usuario usuario : l) {
//            if (usuario.getId() != 1) {
            dtm.addRow(new Object[]{
                usuario,
                usuario.getNombre(),
                usuario.getBlocked(),
                UTIL.DATE_FORMAT.format(usuario.getCreacion())});
//            }
        }
        buscador.hideImprimir();
        buscador.hideLimpiar();
        if (enableDisposeOnMouseDoubleClick) {
            buscador.getjTable1().addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        buscador.dispose();
                    }
                }
            });
        }
        return buscador;
    }

    private boolean initBuscador() {
        getBuscador(customABMJDialog, true);
        buscador.setVisible(true);
        if (buscador.getjTable1().getSelectedRow() > -1) {
            entity = (Usuario) buscador.getDtm().getValueAt(buscador.getjTable1().getSelectedRow(), 0);
            setPanelABM(entity);
        }
        return buscador.getjTable1().getSelectedRow() > -1;
    }

    private void cargarSectores() {
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem();
        List<Sector> ls = new UsuarioSectorJPAController().findByAdminUsuarios(UsuarioController.getCurrentUser(), cbw.getEntity().getInstitucion());
        ArrayList<ComboBoxWrapper<Sector>> lls = new ArrayList<>(5);
        for (Sector sector : ls) {
            lls.add(new ComboBoxWrapper<>(sector, sector.getId(), sector.getSectorUI().getNombre()));
        }
        UTIL.loadComboBox(abmPanel.getCbSector(), lls, false, "<SIN PERMISO DE ACCESO>");
    }

    /**
     * Chequea si el usuario actual tiene permiso para administrar la documentación de algun sector.
     *
     * @return si si o si no..
     */
    static boolean administraAlgunSector() {
        try {
            new UsuarioJPAController().refresh(CURRENT_USER);
        } catch (IllegalArgumentException ex) {
            CURRENT_USER = new UsuarioJPAController().find(CURRENT_USER.getId());
        }
        for (UsuarioSector us : CURRENT_USER.getUsuarioSectores()) {
            if (us.getAdministraSector()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checkea si el usuario actualmente logeado tiene permiso de administración de usuarios en
     * algún sector.
     *
     * @return true si administra Usuarios de algún sector
     */
    static boolean administraUsuarios() {
        try {
            new UsuarioJPAController().refresh(CURRENT_USER);
        } catch (IllegalArgumentException ex) {
            //when the entity is not managed YET!
            CURRENT_USER = new UsuarioJPAController().find(CURRENT_USER.getId());
        }
        for (UsuarioSector us : CURRENT_USER.getUsuarioSectores()) {
            if (us.getAdministraUsuarios()) {
                return true;
            }
        }
        return false;
    }

    private void btnAgregarPermisoAction() {
        @SuppressWarnings("unchecked")
        Institucion institucionToAdd = ((ComboBoxWrapper<UsuarioSector>) abmPanel.getCbInstitucion().getSelectedItem()).getEntity().getInstitucion();
        @SuppressWarnings("unchecked")
        Sector sectorToAdd = ((ComboBoxWrapper<Sector>) abmPanel.getCbSector().getSelectedItem()).getEntity();
        boolean permisoLectura = abmPanel.getjRadioButton1().isSelected(); //LECTURA
        boolean adminSector = abmPanel.getCheckAdminSector().isSelected();
        boolean adminUsuario = abmPanel.getCheckAdminUser().isSelected();
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        boolean found = false;
        for (int row = 0; row < dtm.getRowCount(); row++) {
            Institucion i = (Institucion) dtm.getValueAt(row, 0);
            Sector s = (Sector) dtm.getValueAt(row, 1);
            if (institucionToAdd.equals(i) && sectorToAdd.equals(s)) {
                found = true;
                if (confirmaModificacionDePermisos(i.getNombre(), s.getNombre())) {
                    modificarPermiso(institucionToAdd, sectorToAdd, permisoLectura ? 1 : 2, adminSector, adminUsuario);
                    break;
                }
            }
        }
        if (!found) {
            entity.getUsuarioSectores().add(new UsuarioSector(
                    null, entity, institucionToAdd, sectorToAdd,
                    (permisoLectura ? 1 : 2),
                    adminSector,
                    adminUsuario));
        }
    }

    private boolean confirmaModificacionDePermisos(String i, String s) {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(customABMJDialog,
                "El usuario ya tiene asignado un permiso en:"
                + "\nInstitución: " + i
                + "\nSector: " + s
                + "\n¿Está seguro que desea modificar los permisos actuales?",
                "Modificación de permisos", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    private void refreshTablaPermisos(Usuario entity) {
        DefaultTableModel dtm = (DefaultTableModel) abmPanel.getjTable1().getModel();
        dtm.setRowCount(0);
        for (UsuarioSector us : entity.getUsuarioSectores()) {
            dtm.addRow(new Object[]{
                us.getInstitucion(),
                us.getSector(),
                us.getPermiso() == 2,
                us.getAdministraSector(),
                us.getAdministraUsuarios()
            });
        }
    }

    private void btnQuitarPermisoAction() {
        if (entity != null && abmPanel.getjTable1().getSelectedRow() > -1) {
            Institucion toDelete = (Institucion) UTIL.getSelectedValue(abmPanel.getjTable1(), 0);
            Sector secToDelete = (Sector) UTIL.getSelectedValue(abmPanel.getjTable1(), 1);
            for (int i = 0; i < entity.getUsuarioSectores().size(); i++) {
                UsuarioSector usuarioSector = entity.getUsuarioSectores().get(i);
                if (usuarioSector.getInstitucion().equals(toDelete) && usuarioSector.getSector().equals(secToDelete)) {
                    entity.getUsuarioSectores().remove(i);
                    break;
                }
            }

        }
    }
}
