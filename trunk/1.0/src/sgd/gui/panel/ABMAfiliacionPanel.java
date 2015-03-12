package sgd.gui.panel;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Code128;
import utilities.gui.SwingUtil;

/**
 *
 * @author FiruzzZ
 */
public class ABMAfiliacionPanel extends javax.swing.JPanel {

    /**
     * Creates new form AfiliacionPanel
     */
    public ABMAfiliacionPanel() {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbTipoDocumento = new javax.swing.JComboBox();
        cbSubTipoDocumento = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        tfAfiliadoNumero = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfApellido = new javax.swing.JTextField();
        tfNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        dcFecha = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        tfObservacion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cbInstitucion = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jBarcodeBean1 = new net.sourceforge.jbarcodebean.JBarcodeBean();
        btnPrecintos = new javax.swing.JButton();
        tfFamiliarNumero = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Entity", "Tipo Documento", "Sub-Tipo Documento", "N° Afiliado", "Fecha", "Apellido, Nombre", "Observ."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/16px_add_circular.png"))); // NOI18N
        btnAgregar.setMnemonic('g');
        btnAgregar.setText("Agregar");

        btnQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sgd/rsc/icons/16px_delete_circular.png"))); // NOI18N
        btnQuitar.setMnemonic('q');
        btnQuitar.setText("Quitar");

        jLabel1.setText("Tipo Documento");

        jLabel2.setText("SubTipo Documento");

        jLabel3.setText("N° Afiliado");

        tfAfiliadoNumero.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfAfiliadoNumeroFocusGained(evt);
            }
        });

        jLabel4.setText("Apellido");

        jLabel5.setText("Nombre");

        tfApellido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfApellidoFocusGained(evt);
            }
        });

        tfNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfNombreFocusGained(evt);
            }
        });

        jLabel6.setText("Fecha del Doc.");

        jLabel7.setText("Observación");

        jLabel8.setText("Institución");

        jBarcodeBean1.setBarcodeHeight(20);
        jBarcodeBean1.setCode("1-SEC-000001");
        jBarcodeBean1.setCodeType(new Code128());

        javax.swing.GroupLayout jBarcodeBean1Layout = new javax.swing.GroupLayout(jBarcodeBean1);
        jBarcodeBean1.setLayout(jBarcodeBean1Layout);
        jBarcodeBean1Layout.setHorizontalGroup(
            jBarcodeBean1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );
        jBarcodeBean1Layout.setVerticalGroup(
            jBarcodeBean1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        btnPrecintos.setText("Ver Precintos");
        btnPrecintos.setFocusable(false);
        btnPrecintos.setRequestFocusEnabled(false);

        tfFamiliarNumero.setColumns(2);
        tfFamiliarNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfFamiliarNumero.setText("0");
        tfFamiliarNumero.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfFamiliarNumeroFocusGained(evt);
            }
        });
        tfFamiliarNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfFamiliarNumeroKeyTyped(evt);
            }
        });

        jLabel9.setText("/");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1)
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnQuitar)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 786, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(tfAfiliadoNumero)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tfFamiliarNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tfApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(152, 152, 152))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                    .addComponent(jBarcodeBean1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnPrecintos))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbSubTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 378, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBarcodeBean1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrecintos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbSubTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfAfiliadoNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(tfApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(tfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfFamiliarNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(dcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuitar)
                    .addComponent(btnAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tfAfiliadoNumeroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfAfiliadoNumeroFocusGained
        SwingUtil.setSelectedAll((JTextComponent) evt.getSource());
    }//GEN-LAST:event_tfAfiliadoNumeroFocusGained

    private void tfFamiliarNumeroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfFamiliarNumeroFocusGained
        SwingUtil.setSelectedAll((JTextComponent) evt.getSource());
    }//GEN-LAST:event_tfFamiliarNumeroFocusGained

    private void tfApellidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfApellidoFocusGained
        SwingUtil.setSelectedAll((JTextComponent) evt.getSource());
    }//GEN-LAST:event_tfApellidoFocusGained

    private void tfNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfNombreFocusGained
        SwingUtil.setSelectedAll((JTextComponent) evt.getSource());
    }//GEN-LAST:event_tfNombreFocusGained

    private void tfFamiliarNumeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfFamiliarNumeroKeyTyped
        SwingUtil.checkInputDigit(evt, false, 2);
    }//GEN-LAST:event_tfFamiliarNumeroKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnPrecintos;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JComboBox cbInstitucion;
    private javax.swing.JComboBox cbSubTipoDocumento;
    private javax.swing.JComboBox cbTipoDocumento;
    private com.toedter.calendar.JDateChooser dcFecha;
    private net.sourceforge.jbarcodebean.JBarcodeBean jBarcodeBean1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfAfiliadoNumero;
    private javax.swing.JTextField tfApellido;
    private javax.swing.JTextField tfFamiliarNumero;
    private javax.swing.JTextField tfNombre;
    private javax.swing.JTextField tfObservacion;
    // End of variables declaration//GEN-END:variables

    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>(8);
        data.put("afiliado", tfAfiliadoNumero.getText().trim());
        data.put("familiar", tfFamiliarNumero.getText().trim());
        data.put("apellido", tfApellido.getText().trim());
        data.put("nombre", tfNombre.getText().trim());
        data.put("observacion", tfObservacion.getText().trim());
        data.put("documentoFecha", dcFecha.getDate());
        data.put("td", cbTipoDocumento.getSelectedItem());
        data.put("std", cbSubTipoDocumento.getSelectedItem());
        return data;
    }

    public void resetUI(boolean limpiarTabla) {
        tfAfiliadoNumero.setText(null);
        tfApellido.setText(null);
        tfNombre.setText(null);
        tfObservacion.setText(null);
        dcFecha.setDate(null);
        if (limpiarTabla) {
            ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
        }
    }

    public JBarcodeBean getjBarcodeBean1() {
        return jBarcodeBean1;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnQuitar() {
        return btnQuitar;
    }

    public JComboBox getCbInstitucion() {
        return cbInstitucion;
    }

    public JComboBox getCbSubTipoDocumento() {
        return cbSubTipoDocumento;
    }

    public JComboBox getCbTipoDocumento() {
        return cbTipoDocumento;
    }

    public JTable getjTable1() {
        return jTable1;
    }

    public void setBarcode(String barcode) {
        jBarcodeBean1.setCode(barcode);
    }

    public JButton getBtnPrecintos() {
        return btnPrecintos;
    }

    public void addButtonsActionListener(ActionListener actionListener) {
        btnAgregar.addActionListener(actionListener);
        btnQuitar.addActionListener(actionListener);
        btnPrecintos.addActionListener(actionListener);
    }
}
