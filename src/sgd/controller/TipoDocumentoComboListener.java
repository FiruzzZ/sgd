package sgd.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import org.apache.log4j.Logger;
import sgd.jpa.controller.TipoDocumentoJPAController;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
class TipoDocumentoComboListener implements ActionListener {
    
    private JComboBox tipoDocumentoComboBox;
    private JComboBox std;
    private TipoDocumento preSelected = null;
    private final boolean subTipoIsElegible;
    
    TipoDocumentoComboListener(JComboBox cbTipoDocumento, JComboBox cbSubTipoDocumento) {
        this(cbTipoDocumento, cbSubTipoDocumento, false);
    }
    
    TipoDocumentoComboListener(JComboBox cbTipoDocumento, JComboBox cbSubTipoDocumento, boolean subTipoIsElegible) {
        this.tipoDocumentoComboBox = cbTipoDocumento;
        this.std = cbSubTipoDocumento;
        this.subTipoIsElegible = subTipoIsElegible;
        addSelf();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(tipoDocumentoComboBox)) {
            //cuando selecciona un Tipo de Documento
            try {
                @SuppressWarnings("unchecked")
                ComboBoxWrapper<TipoDocumento> c = (ComboBoxWrapper<TipoDocumento>) tipoDocumentoComboBox.getSelectedItem();
                TipoDocumento selected = c.getEntity();
                if (preSelected == null || !preSelected.equals(selected)) {
                    selected = new TipoDocumentoJPAController().find(selected.getId());
                    preSelected = selected;
                    List<ComboBoxWrapper<SubTipoDocumento>> l = new ArrayList<>(preSelected.getSubTipoDocumentos().size());
                    for (SubTipoDocumento subTipoDocumento : preSelected.getSubTipoDocumentos()) {
                        l.add(new ComboBoxWrapper<>(subTipoDocumento, subTipoDocumento.getId(), subTipoDocumento.getNombre()));
                    }
                    UTIL.loadComboBox(std, l, subTipoIsElegible, "<Sin Sub-Tipos>");
                }
            } catch (ClassCastException ex) {
                Logger.getLogger(TipoDocumentoComboListener.class).trace(ex.getLocalizedMessage());
                UTIL.loadComboBox(std, null, subTipoIsElegible, "<Sin Sub-Tipos>");
            }
        }
    }
    
    private void addSelf() {
        tipoDocumentoComboBox.addActionListener(this);
    }
}
