/*
 * BuscadorGremialesPanel.java
 *
 * Created on 30/06/2014, 10:42:07
 */
package sgd.gui.panel;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;

/**
 *
 * @author FiruzzZ
 */
public class BuscadorGremialesPanel extends javax.swing.JPanel {

    /**
     * Creates new form BuscadorGremialesPanel
     */
    public BuscadorGremialesPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfEmpresa = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfNumeroArchivo = new javax.swing.JTextField();
        checkBaja = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        dcFechaHasta = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfEmpleado = new javax.swing.JTextField();
        tfDocumentoNumero = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbTipoDocumento = new javax.swing.JComboBox();
        dcFechaDesde = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbInstitucion = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbSubTipoDocumento = new javax.swing.JComboBox();

        jLabel4.setText("Empresa (Nombre/CUIT)");

        tfNumeroArchivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfNumeroArchivo.setToolTipText("Archivo / Caja");

        checkBaja.setText("Cajas de Baja");

        jLabel10.setText("N° Caja");

        jLabel8.setText("Institución");

        jLabel5.setText("Empleado (Nombre/CUIL)");

        tfEmpleado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        tfDocumentoNumero.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel2.setText("SubTipo Documento");

        jLabel6.setText("Fecha del Doc:");

        jLabel1.setText("Tipo Documento");

        jLabel7.setText("Desde");

        jLabel9.setText("Hasta");

        jLabel3.setText("N° Documento");

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
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbInstitucion, 0, 299, Short.MAX_VALUE)
                            .addComponent(cbSubTipoDocumento, 0, 299, Short.MAX_VALUE)
                            .addComponent(cbTipoDocumento, 0, 299, Short.MAX_VALUE)
                            .addComponent(tfEmpresa)
                            .addComponent(tfEmpleado))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tfDocumentoNumero, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfNumeroArchivo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBaja)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(tfNumeroArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfDocumentoNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbSubTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(dcFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(checkBaja)
                    .addComponent(dcFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField tfDocumentoNumero;
    private javax.swing.JTextField tfEmpleado;
    private javax.swing.JTextField tfEmpresa;
    private javax.swing.JTextField tfNumeroArchivo;
    // End of variables declaration//GEN-END:variables

    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        if (cbInstitucion.getSelectedIndex() > 0) {
            data.put("i", cbInstitucion.getSelectedItem());
        } else {
            data.put("i", null);
        }
        if (cbTipoDocumento.getSelectedIndex() > 0) {
            data.put("td", cbTipoDocumento.getSelectedItem());
        } else {
            data.put("td", null);
        }
        if (cbSubTipoDocumento.getSelectedIndex() > 0) {
            data.put("std", cbSubTipoDocumento.getSelectedItem());
        } else {
            data.put("std", null);
        }
        data.put("documentonumero", tfDocumentoNumero.getText().trim());
        data.put("archivo", tfNumeroArchivo.getText().trim());
        data.put("empresa", tfEmpresa.getText().trim());
        data.put("empleado", tfEmpleado.getText().trim());
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
