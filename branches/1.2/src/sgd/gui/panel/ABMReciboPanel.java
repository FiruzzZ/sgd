/*
 * ABMReciboPanel.java
 *
 * Created on 29/02/2012, 10:31:12
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
public class ABMReciboPanel extends javax.swing.JPanel {

    /**
     * Creates new form ABMReciboPanel
     */
    public ABMReciboPanel() {
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

        btnAgregar = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDetalle = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableBuscador = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        tfNumero = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cbInstitucion = new javax.swing.JComboBox();
        btnVerArchivo = new javax.swing.JButton();

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/16px_add_circular.png"))); // NOI18N
        btnAgregar.setMnemonic('a');
        btnAgregar.setText("Agregar");

        btnQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/16px_delete_circular.png"))); // NOI18N
        btnQuitar.setMnemonic('q');
        btnQuitar.setText("Quitar");

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
            jTableDetalle.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTableDetalle.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTableDetalle.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        jTableBuscador.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableBuscador.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableBuscador);
        if (jTableBuscador.getColumnModel().getColumnCount() > 0) {
            jTableBuscador.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTableBuscador.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTableBuscador.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        jLabel1.setText("N° Envío");

        tfNumero.setEditable(false);
        tfNumero.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tfNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel8.setText("Institución");

        btnVerArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/folder_32.png"))); // NOI18N
        btnVerArchivo.setText("Ver Archivo");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuitar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(4, 4, 4)
                        .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
                        .addComponent(btnVerArchivo)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(tfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar)
                    .addComponent(btnQuitar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnVerArchivo;
    private javax.swing.JComboBox cbInstitucion;
    private javax.swing.JLabel jLabel1;
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

    public JTable getjTableBuscador() {
        return jTableBuscador;
    }

    public JTable getjTableDetalle() {
        return jTableDetalle;
    }

    public JComboBox getCbInstitucion() {
        return cbInstitucion;
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
