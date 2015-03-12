/*
 * JDContenedor.java
 *
 * Created on 19/11/2009, 17:02:03
 */
package sgd.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrador
 */
public class JDContenedor extends javax.swing.JDialog {

    private boolean modoBuscador;

    @Deprecated
    public JDContenedor(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
        this.setLocationRelativeTo(owner);
    }

    public JDContenedor(java.awt.Frame owner, boolean modal, String title) {
        super(owner, modal);
        initComponents();
        this.setLocationRelativeTo(owner);
        this.setTitle(title);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bNuevo = new javax.swing.JButton();
        bModificar = new javax.swing.JButton();
        bBorrar = new javax.swing.JButton();
        bSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bImprimir = new javax.swing.JButton();
        labelMensaje = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfFiltro = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("..::poneme un título::..");
        setMinimumSize(new java.awt.Dimension(612, 364));

        bNuevo.setMnemonic('n');
        bNuevo.setText("Nuevo");
        bNuevo.setName("new"); // NOI18N

        bModificar.setMnemonic('m');
        bModificar.setText("Modificar");
        bModificar.setName("edit"); // NOI18N
        bModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bModificarActionPerformed(evt);
            }
        });

        bBorrar.setMnemonic('b');
        bBorrar.setText("Borrar");
        bBorrar.setName("del"); // NOI18N

        bSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/32px-Crystal_Clear_action_editdelete.png"))); // NOI18N
        bSalir.setText("Salir");
        bSalir.setName("exit"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel());
        jTable1.setName("jTableContenedor"); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        bImprimir.setMnemonic('i');
        bImprimir.setText("Imprimir");
        bImprimir.setName("print"); // NOI18N

        labelMensaje.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel2.setText("Filtrar:");

        tfFiltro.setName("tfFiltro"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bSalir)
                            .addComponent(bImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(tfFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(labelMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBorrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bImprimir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(bSalir))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bModificarActionPerformed
    }//GEN-LAST:event_bModificarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBorrar;
    private javax.swing.JButton bImprimir;
    private javax.swing.JButton bModificar;
    private javax.swing.JButton bNuevo;
    private javax.swing.JButton bSalir;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelMensaje;
    private javax.swing.JTextField tfFiltro;
    // End of variables declaration//GEN-END:variables

    public void setDTM(javax.swing.table.DefaultTableModel dtmNEW) {
        jTable1.setModel(dtmNEW);
    }

    /**
     * Agrega un escuchador a el textField, la botonera de la derecha y la tabla
     * @param o A object whom implements:
     * <br>{@link ActionListener} for {@code bNuevo, bModificar, bBorrar, bImprimir, bSalir}.
     * <br>{@link KeyListener} for {@code tfFiltro}.
     * <br>{@link MouseListener} for {@code jTable1}.
     * @throws ClassCastException if the object does not implement {@link ActionListener}
     */
    public void setListener(Object o) {
        bNuevo.addActionListener((ActionListener) o);
        bModificar.addActionListener((ActionListener) o);
        bBorrar.addActionListener((ActionListener) o);
        bImprimir.addActionListener((ActionListener) o);
        bSalir.addActionListener((ActionListener) o);
        try {
            jTable1.addMouseListener((MouseListener) o);
        } catch (ClassCastException ex) {
            System.out.println(ex.getClass() + " NO implementa MouseListener en " + o.getClass());
        }

        try {
            tfFiltro.addKeyListener((KeyListener) o);
        } catch (ClassCastException ex) {
            System.out.println(ex.getClass() + ": NO implementa KeyListener en " + o.getClass());
        }
    }

    public JTable getjTable1() {
        return jTable1;
    }

    public Object getSelectedValue(int fila, int columna) {
        return jTable1.getModel().getValueAt(fila, columna);
    }

    /**
     * Devuele el Objeto de la celda que permita identificar una única Entity
     * de la Class
     * @param columna, nº de la columna en la que está ese objeto ( >=0 )
     * @return
     */
    public Object getSelectedValue(int columna) {
        return jTable1.getModel().getValueAt(jTable1.getSelectedRow(), columna);
    }

    /**
     * Setea un mensaje de información.
     * Si @param messageType < -1 || >3 no hace NADA.
     * @param msg,.. mensaje.
     * @param titulo (puede tener título o no).
     * @param messageType =3 (sin título), sino -1=PLAIN,0=ERROR, 1=INFO,2=WARRNING.
     */
    public void showMessage(String msg, String titulo, int messageType) {
        if (messageType < -1 || messageType > 3) {
            return;
        }
        if (messageType == 3) {
            JOptionPane.showMessageDialog(this, msg);
        } else {
            JOptionPane.showMessageDialog(this, msg, titulo, messageType);
        }

    }

    public boolean isModoBuscador() {
        return modoBuscador;
    }

    public void setModoBuscador(boolean isModoBuscador) {
        modoBuscador = isModoBuscador;
    }

    public int confirmarBorrado(String msj, String titulo) {
        return JOptionPane.showOptionDialog(this, msj, titulo, JOptionPane.YES_NO_OPTION, 2, null, null, null);
    }

    public DefaultTableModel getDTM() {
        return (DefaultTableModel) jTable1.getModel();
    }

    public void hideBtmEliminar() {
        bBorrar.setVisible(false);
    }

    public void hideBtmImprimir() {
        bImprimir.setVisible(false);
    }

    public JTextField getTfFiltro() {
        return tfFiltro;
    }

    public JLabel getLabelMensaje() {
        return labelMensaje;
    }

    public JButton getbBorrar() {
        return bBorrar;
    }

    public JButton getbImprimir() {
        return bImprimir;
    }

    public JButton getbModificar() {
        return bModificar;
    }

    public JButton getbNuevo() {
        return bNuevo;
    }

    public JButton getbSalir() {
        return bSalir;
    }

    public void setButtonsVisible(boolean visible) {
        bNuevo.setVisible(visible);
        bModificar.setVisible(visible);
        bBorrar.setVisible(visible);
        bImprimir.setVisible(visible);
        bSalir.setVisible(visible);
    }
}
