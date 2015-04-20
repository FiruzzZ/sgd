package sgd.gui.panel;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import utilities.gui.SwingUtil;

/**
 *
 * @author FiruzzZ
 */
public class BuscadorAfiliacionPanel extends javax.swing.JPanel {

    /**
     * Creates new form BuscadorAfiliacionPanel
     */
    public BuscadorAfiliacionPanel() {
        initComponents();
        SwingUtil.addDigitsInputListener(tfNumeroCaja, 6);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        cbInstitucion = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cbTipoDocumento = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cbSubTipoDocumento = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        tfAfiliadoNumero = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfApellido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfNumeroCaja = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        dcFechaDesde = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        dcFechaHasta = new com.toedter.calendar.JDateChooser();
        checkBaja = new javax.swing.JCheckBox();

        jLabel8.setText("Institución");

        jLabel1.setText("Tipo Documento");

        jLabel2.setText("SubTipo Documento");

        jLabel3.setText("N° Afiliado");

        jLabel4.setText("Apellido/Nombre");

        jLabel5.setText("N° Caja");

        tfNumeroCaja.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setText("Fecha del Doc:");

        jLabel7.setText("Desde");

        jLabel9.setText("Hasta");

        checkBaja.setText("Incluir Archivos de Baja");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbInstitucion, 0, 299, Short.MAX_VALUE)
                            .addComponent(cbSubTipoDocumento, 0, 299, Short.MAX_VALUE)
                            .addComponent(cbTipoDocumento, 0, 299, Short.MAX_VALUE)
                            .addComponent(tfApellido)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBaja)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfAfiliadoNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfNumeroCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(tfNumeroCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfAfiliadoNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbSubTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(dcFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(dcFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(checkBaja))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbInstitucion;
    private javax.swing.JComboBox cbSubTipoDocumento;
    private javax.swing.JComboBox cbTipoDocumento;
    private javax.swing.JCheckBox checkBaja;
    private com.toedter.calendar.JDateChooser dcFechaDesde;
    private com.toedter.calendar.JDateChooser dcFechaHasta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField tfAfiliadoNumero;
    private javax.swing.JTextField tfApellido;
    private javax.swing.JTextField tfNumeroCaja;
    // End of variables declaration//GEN-END:variables

    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put("i", cbInstitucion.getSelectedIndex() > 0 ? cbInstitucion.getSelectedItem() : null);
        data.put("td", cbTipoDocumento.getSelectedIndex() > 0 ? cbTipoDocumento.getSelectedItem() : null);
        data.put("std", cbSubTipoDocumento.getSelectedIndex() > 0 ? cbSubTipoDocumento.getSelectedItem() : null);
        data.put("afiliado", tfAfiliadoNumero.getText().trim());
        data.put("apenom", tfApellido.getText().trim());
        data.put("caja", tfNumeroCaja.getText().trim());
        data.put("desde", dcFechaDesde.getDate());
        data.put("hasta", dcFechaHasta.getDate());
        data.put("baja", checkBaja.isSelected());
        return data;
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
}