/*
 * CustomABMJDialog.java
 *
 * Created on 15/08/2009, 00:46:05
 */
package sgd.controller;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Métodos interantes. <br>{@linkplain #addListenerToBottomButtons(java.awt.event.ActionListener)} <br>{@linkplain CustomABMJDialog#addToolBarButtonsListener(java.awt.event.ActionListener)} <br>{@linkplain CustomABMJDialog#setBottomButtonsVisible(boolean)
 * }
 * <br>{@linkplain #setEnabledBottomButtons(boolean) }
 * <br>{@linkplain #setEnabledToolBarButtons(boolean) }
 * <br>{@linkplain #setPanelEnabled(boolean) }
 * <br>{@linkplain #setPermisos(int)}
 *
 * @author FiruzzZ
 */
public class CustomABMJDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JToolBar jToolBar;
    private JButton btnNuevo;
    private JButton btnBorrar;
    private JButton btnBuscar;
    private JButton btnEditar;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JButton btnExtraBottom;
    private JPanel bottomButtonPane;
    private JPanel topPanel;
    private JLabel messageLabel;
    private static final boolean customTextPosition = false;
    public static final int HORIZONTAL_TEXT_POSITION = SwingUtilities.CENTER;
    public static final int VERTICAL_TEXT_POSITION = SwingUtilities.BOTTOM;
    public static final String RESOURCE_FOLDER_PATH = "/sgd/rsc/icons/";
    private String htmlFormattedMessage;
    private Color topButtonForegroundColor = Color.WHITE;
    private Color topButtonBackgroundColor = Color.BLACK;

    public CustomABMJDialog(Window owner, JPanel panel, String title, boolean modal, String messageText) {
        super(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
        if (panel == null) {
            throw new IllegalArgumentException("No te olvides del PANEL capooooooo!!");
        }
        this.panel = panel;
        this.htmlFormattedMessage = getHtmlText(messageText);
        initComponents();
    }

    public void setToolBarVisible(boolean visible) {
        jToolBar.setVisible(visible);
    }

    public void setBottomButtonsVisible(boolean visible) {
        bottomButtonPane.setVisible(visible);
    }

    /**
     * Set a actionListener for the 4 buttons in the tool bar. Commonly used it
     * as: New, Remove, Edit, Search.
     *
     * @param o
     */
    public void addToolBarButtonsActionListener(ActionListener o) {
        for (Component component : jToolBar.getComponents()) {
            if (component instanceof JButton) {
                JButton b = (JButton) component;
                b.addActionListener(o);
            }
        }
    }

    /**
     * Add an
     * <code>ActionListener</code> to all buttons in {@link #bottomButtonPane}
     *
     * @param o the ActionListener to be added
     */
    public void addBottomButtonsActionListener(ActionListener o) {
        for (Component component : bottomButtonPane.getComponents()) {
            if (component instanceof JButton) {
                JButton b = (JButton) component;
                b.addActionListener(o);
            }
        }
    }

    /**
     * Enables or disables all the buttons into {@link #bottomButtonPane}
     *
     * @param enable
     */
    public void setEnabledBottomButtons(boolean enable) {
        for (Component component : bottomButtonPane.getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(enable);
            }
        }
    }

    public void setEnabledToolBarButtons(boolean enable) {
        for (Component component : jToolBar.getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(enable);
            }
        }
    }

    /**
     * Set enable to FALSE to all componenets in the panel. For visibility
     * reasons, this methond doesn't affect to {@link JScrollPane}, {@link JLabel}
     * and {@link JSeparator}
     *
     * @param enable
     * @see #setComponentsEnabled(java.awt.Component[], boolean)
     * @see #setEnableDependingOfType(java.awt.Component, boolean)
     */
    public void setPanelComponentsEnabled(boolean enable) {
        setComponentsEnabled(panel.getComponents(), enable);
    }

    private void setComponentsEnabled(Component[] components, boolean enable) {
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel subPanel = (JPanel) component;
                setComponentsEnabled(subPanel.getComponents(), enable);
            } else {
                setEnableDependingOfType(component, enable);
            }
        }
    }

    protected void setEnableDependingOfType(Component component, boolean enable) {
        if (!(component instanceof JScrollPane)
                && !(component instanceof JLabel)
                && !(component instanceof JSeparator)) {
            component.setEnabled(enable);
        }
    }

    public void setPermisos(int uno_o_dos) {
        if (uno_o_dos == 1) {
            setEnabledToolBarButtons(false);
            setEnabledBottomButtons(false);
            btnBuscar.setEnabled(true);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getters components">
    public JPanel getBottomButtonPane() {
        return bottomButtonPane;
    }

    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    public JButton getBtnNuevo() {
        return btnNuevo;
    }

    public JButton getBtnBorrar() {
        return btnBorrar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnExtraBottom() {
        return btnExtraBottom;
    }

    public JToolBar getjToolBar() {
        return jToolBar;
    }

    public JPanel getPanel() {
        return panel;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="init componentes......que loco!">
    private void initComponents() {
        Insets insets = new Insets(2, 1, 2, 1);
        jToolBar = new JToolBar();
        btnNuevo = new JButton();
        btnBorrar = new JButton();
        btnEditar = new JButton();
        btnBuscar = new JButton();
        btnAceptar = new JButton();
        btnCancelar = new JButton();
        btnExtraBottom = new JButton();

        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        messageLabel.setForeground(new Color(255, 255, 255));
        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        messageLabel.setText(htmlFormattedMessage);
        messageLabel.setVisible(htmlFormattedMessage != null);
        messageLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(Color.BLACK);
        messagePanel.setLayout(new GridLayout(1, 1));
        messagePanel.add(messageLabel);

        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setBackground(Color.BLACK);
        toolBarPanel.setLayout(new GridLayout(1, 1));
        toolBarPanel.add(jToolBar);

        //<editor-fold defaultstate="collapsed" desc="Buttons settings">
        btnNuevo.setIcon(new ImageIcon(getClass().getResource(RESOURCE_FOLDER_PATH + "32px_action_db_add.png")));
        btnNuevo.setMnemonic('n');
        btnNuevo.setText("Nuevo");
        btnNuevo.setFocusable(false);
        btnNuevo.setMargin(insets);

        btnBorrar.setIcon(new ImageIcon(getClass().getResource(RESOURCE_FOLDER_PATH + "32px_action_db_remove.png")));
        btnBorrar.setMnemonic('e');
        btnBorrar.setText("Eliminar");
        btnBorrar.setFocusable(false);
        btnBorrar.setMargin(insets);

        btnEditar.setIcon(new ImageIcon(getClass().getResource(RESOURCE_FOLDER_PATH + "32px_action_configure.png")));
        btnEditar.setMnemonic('m');
        btnEditar.setText("Modificar");
        btnEditar.setFocusable(false);
        btnEditar.setMargin(insets);

        btnBuscar.setIcon(new ImageIcon(getClass().getResource(RESOURCE_FOLDER_PATH + "32px_action_demo.png")));
        btnBuscar.setMnemonic('b');
        btnBuscar.setText("Buscar");
        btnBuscar.setFocusable(false);

        btnExtraBottom.setIcon(new ImageIcon(getClass().getResource(RESOURCE_FOLDER_PATH + "24px_package.png")));
        btnExtraBottom.setText("Cerrar y precintar ");
        btnExtraBottom.setMargin(insets);

        btnAceptar.setIcon(new ImageIcon(getClass().getResource(RESOURCE_FOLDER_PATH + "24px_accept.png")));
        btnAceptar.setMnemonic('a');
        btnAceptar.setText("Aceptar");
        btnAceptar.setMargin(insets);

        btnCancelar.setIcon(new ImageIcon(getClass().getResource(RESOURCE_FOLDER_PATH + "24px_cancel.png")));
        btnCancelar.setMnemonic('c');
        btnCancelar.setText("Cancelar");
        btnCancelar.setMargin(insets);
        //</editor-fold>

        jToolBar.setFloatable(false);
        jToolBar.add(btnNuevo);
        jToolBar.add(btnBorrar);
        jToolBar.add(btnEditar);
        jToolBar.add(btnBuscar);
        for (Component component : jToolBar.getComponents()) {
            if (component instanceof JButton) {
                JButton b = (JButton) component;
                b.setForeground(topButtonForegroundColor);
                b.setBackground(topButtonBackgroundColor);
            }
        }

        bottomButtonPane = new JPanel();
        bottomButtonPane.setLayout(new BoxLayout(bottomButtonPane, BoxLayout.LINE_AXIS));
        bottomButtonPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 10));
        bottomButtonPane.add(Box.createHorizontalGlue());
        bottomButtonPane.add(btnExtraBottom);
        bottomButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomButtonPane.add(btnAceptar);
        bottomButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomButtonPane.add(btnCancelar);

        topPanel = new JPanel();
        topPanel.setBackground(topButtonBackgroundColor);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        topPanel.add(messagePanel);
        topPanel.add(toolBarPanel);

        getContentPane().add(topPanel, BorderLayout.PAGE_START);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bottomButtonPane, BorderLayout.PAGE_END);

        if (customTextPosition) {
            setCustomTextPositions();
        }
        pack();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PSVM">
    public static void main(String[] args) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //</editor-fold>
        JPanel pane = new JPanel();
        pane.setSize(800, 600);
        CustomABMJDialog jd = new CustomABMJDialog((JDialog) null, pane, null, true, "");
        jd.setMessageText("assaffsdafsdfasdafsdaafsdafsdsdfsdfaasdfafsdfdssfd"
                + "<br>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaafffff"
                + "<br>afwefefaafwefewfweafwefwewefwefwefwf"
                + "<br>12341234ñ123j4o123ij4ño123ij4ño123ij4oi21     123 4231 4123");
        jd.setVisible(true);
        System.exit(0);
    }
    //</editor-fold>

    public void showMessage(String msj, String title, int messageType) {
        JOptionPane.showMessageDialog(this, msj, title, messageType);
    }

    private void setCustomTextPositions() {
        for (Component component : jToolBar.getComponents()) {
            if (component instanceof JButton) {
                JButton btn = (JButton) component;
                btn.setHorizontalTextPosition(HORIZONTAL_TEXT_POSITION);
                btn.setVerticalTextPosition(VERTICAL_TEXT_POSITION);
            }
        }
    }

    /**
     * Set a message at the top of the GUI. This will be formatted with HTML
     * TAG's like: Enclosed by &lt HTML> .. &lt/HTML>, "\n" replaced by &lt br>
     *
     * @param messageText
     */
    public void setMessageText(String messageText) {
        htmlFormattedMessage = getHtmlText(messageText);
        messageLabel.setVisible(htmlFormattedMessage != null);
        messageLabel.setText(htmlFormattedMessage);
        this.pack();
    }

    public JLabel getMessageLabel() {
        return messageLabel;
    }

    private String getHtmlText(String messageText1) {
        String insentiveCaseRegEx = "(?ui)";
        if (messageText1 != null) {
            String htmlFormatted;
            htmlFormatted = messageText1.replaceAll(insentiveCaseRegEx + "<html>", "").replaceAll(insentiveCaseRegEx + "</html>", "");
            htmlFormatted = htmlFormatted.replaceAll(insentiveCaseRegEx + "\\n", "<br>");
            return "<HTML>" + htmlFormatted + "</HTML>";
        }
        return null;
    }
}
