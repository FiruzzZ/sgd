/*
 * ABMRecepcionPanel.java
 *
 * Created on 08/05/2012, 08:29:35
 */
package sgd.gui.panel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FiruzzZ
 */
public class ABMRecepcionPanel extends javax.swing.JPanel {

    /**
     * Creates new form ABMRecepcionPanel
     */
    public ABMRecepcionPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTableBuscador = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDetalle = new javax.swing.JTable();
        btnVerArchivo = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        tfNumero = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cbInstitucion = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        dcRecepcion = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jTableBuscador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Entity", "Código", "Precintos", "N° Envío", "Fecha Envío"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableBuscador.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableBuscador);
        if (jTableBuscador.getColumnModel().getColumnCount() > 0) {
            jTableBuscador.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTableBuscador.getColumnModel().getColumn(1).setResizable(false);
            jTableBuscador.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTableBuscador.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTableBuscador.getColumnModel().getColumn(3).setResizable(false);
            jTableBuscador.getColumnModel().getColumn(3).setPreferredWidth(20);
            jTableBuscador.getColumnModel().getColumn(4).setResizable(false);
            jTableBuscador.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/16px_add_circular.png"))); // NOI18N
        btnAgregar.setMnemonic('g');
        btnAgregar.setText("Agregar");

        jTableDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Entity", "Código", "Precintos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDetalle.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableDetalle);
        if (jTableDetalle.getColumnModel().getColumnCount() > 0) {
            jTableDetalle.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTableDetalle.getColumnModel().getColumn(1).setResizable(false);
            jTableDetalle.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTableDetalle.getColumnModel().getColumn(2).setPreferredWidth(250);
        }

        btnVerArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/folder_32.png"))); // NOI18N
        btnVerArchivo.setText("Ver Archivo");

        btnQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/16px_delete_circular.png"))); // NOI18N
        btnQuitar.setMnemonic('q');
        btnQuitar.setText("Quitar");

        tfNumero.setEditable(false);
        tfNumero.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tfNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel8.setText("Institución");

        jLabel2.setText("Fecha de Recepción");

        jLabel3.setText("N°");

        jLabel1.setText("Lista de Archivos Recibidos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuitar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnVerArchivo))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dcRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(dcRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnQuitar)
                        .addComponent(btnAgregar))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnVerArchivo;
    private javax.swing.JComboBox cbInstitucion;
    private com.toedter.calendar.JDateChooser dcRecepcion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableBuscador;
    private javax.swing.JTable jTableDetalle;
    private javax.swing.JTextField tfNumero;
    // End of variables declaration//GEN-END:variables

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnQuitar() {
        return btnQuitar;
    }

    public JButton getBtnVerArchivo() {
        return btnVerArchivo;
    }

    public JComboBox getCbInstitucion() {
        return cbInstitucion;
    }

    public JTable getjTableBuscador() {
        return jTableBuscador;
    }

    public JTable getjTableDetalle() {
        return jTableDetalle;
    }

    public JTextField getTfNumero() {
        return tfNumero;
    }

    public void resetUI(boolean limpiarTablas) {
        if (limpiarTablas) {
            DefaultTableModel dtm = (DefaultTableModel) jTableBuscador.getModel();
            dtm.setRowCount(0);
            DefaultTableModel dtm1 = (DefaultTableModel) jTableDetalle.getModel();
            dtm1.setRowCount(0);
        }
        tfNumero.setText(null);
        cbInstitucion.setSelectedIndex(0);
    }
}
