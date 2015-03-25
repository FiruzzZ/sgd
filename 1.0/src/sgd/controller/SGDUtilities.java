package sgd.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import sgd.SGD;
import sgd.gui.panel.ABMRecepcionPanel;
import sgd.gui.panel.ABMReciboPanel;
import sgd.gui.panel.AddPrecintosPanel;
import sgd.jpa.controller.*;
import sgd.jpa.model.*;
import utilities.general.UTIL;
import utilities.swing.components.ComboBoxWrapper;

/**
 *
 * @author FiruzzZ
 */
public class SGDUtilities {

    private static AddPrecintosPanel panelPrecintos;
    private static List<String> precintosList;

    static JDialog getArchivoViewer(JPanel archivoViewer) {
        CustomABMJDialog customABMJDialog = new CustomABMJDialog(null, archivoViewer, null, true, null);
        customABMJDialog.setPanelComponentsEnabled(false);
        customABMJDialog.setToolBarVisible(false);
        customABMJDialog.setBottomButtonsVisible(false);
        return customABMJDialog;
    }

    /**
     * Si la lista de instituciones wrapper contiene la instancia.
     *
     * @param wrappedList
     * @param candidate
     * @return <code>true</code> if {@code candidate} is an element on
     * {@code wrappedList}
     */
    static boolean isContained(List<ComboBoxWrapper<UsuarioSector>> wrappedList, Institucion candidate) {
        for (ComboBoxWrapper<UsuarioSector> comboBoxWrapper : wrappedList) {
            UsuarioSector us = comboBoxWrapper.getEntity();
            if (us.getInstitucion().equals(candidate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Concatena los precintos, separandolos con ", "
     *
     * @param precintos
     * @return un String
     */
    static String precintosToString(List<? extends Precinto> precintos) {
        String precintosString = "";
        for (Precinto precinto : precintos) {
            precintosString += precinto.getCodigo() + ", ";
        }
        precintosString = precintosString.substring(0, precintosString.length() - 2);
        return precintosString;
    }

    public static void viewArchivo(Archivo archivo) {
        JDialog viewArchivo = null;
        SectorUI sectorUI = archivo.getSector().getSectorUI();
        if (sectorUI.equals(SectorUI.AFILIACION)) {
            viewArchivo = new AfiliacionController().viewArchivo((Afiliacion) archivo);
        } else if (sectorUI.equals(SectorUI.APE)) {
            viewArchivo = new ApeController().viewArchivo((Ape) archivo);
        } else if (sectorUI.equals(SectorUI.CONTABLE)) {
            viewArchivo = new ContableController().viewArchivo((Contable) archivo);
        } else if (sectorUI.equals(SectorUI.PSICOFISICO)) {
            viewArchivo = new PsicofisicoController().viewArchivo((Psicofisico) archivo);
        } else if (sectorUI.equals(SectorUI.FACTURACION)) {
            viewArchivo = new FacturacionController().viewArchivo((Facturacion) archivo);
        } else if (sectorUI.equals(SectorUI.AUDITORIA)) {
            viewArchivo = new AuditoriaController().viewArchivo((Auditoria) archivo);
        } else if (sectorUI.equals(SectorUI.GREMIALES)) {
            viewArchivo = new GremialesController().viewArchivo((Gremiales) archivo);
        } else if (sectorUI.equals(SectorUI.CRONICO)) {
            viewArchivo = new CronicoController().viewArchivo((Cronico) archivo);
        } else if (sectorUI.equals(SectorUI.DISCAPACIDAD)) {
            viewArchivo = new DiscapacidadController().viewArchivo((Discapacidad) archivo);
        } else if (sectorUI.equals(SectorUI.AUDITORIAMEDICA)) {
            viewArchivo = new AuditoriaMedicaController().viewArchivo((AuditoriaMedica) archivo);
        } else {
            throw new IllegalArgumentException(SGD.getResources().getString("undefinedsectorimplementation") + ": " + sectorUI);
        }
        viewArchivo.setLocationRelativeTo(null);
        viewArchivo.setVisible(true);
    }

    private SGDUtilities() {
    }

    static void volcar(JComboBox cbInstitucion, JComboBox cbInstitucion0, String primerItem) {
        cbInstitucion0.removeAllItems();
        if (primerItem != null) {
            cbInstitucion0.addItem(primerItem);
        }
        for (int i = 0; i < cbInstitucion.getItemCount(); i++) {
            cbInstitucion0.addItem(cbInstitucion.getItemAt(i));

        }
    }

    /**
     * Carga una lista con las Instituciones que tiene habilitadas para usar en
     * el sectorUI indicado
     *
     * @param l donde
     * @param sectorUI
     */
    static void cargarInstitucionesYTipoDocumentoSegunSector(List<ComboBoxWrapper<UsuarioSector>> l,
            List<ComboBoxWrapper<TipoDocumento>> tipoDocumentoList, SectorUI sectorUI) {
        /**
         * s va ser null si el usuario no tiene ningún permiso de acceso al
         * sector especificado PD: eso hay que controlar antes de iniciar el ABM
         */
        Sector s = null;
        for (UsuarioSector us : UsuarioController.getCurrentUser().getUsuarioSectores()) {
            //si el permiso pertenece al sector indicato
            if (us.getSector().getSectorUI().equals(sectorUI)) {
                s = us.getSector();
                boolean repetido = false;
                //checkea si en la lista ya se agregó la Institucion
                for (ComboBoxWrapper<UsuarioSector> existentes : l) {
                    if (existentes.getEntity().getInstitucion().equals(us.getInstitucion())) {
                        repetido = true;
                        break;
                    }
                }
                if (!repetido) {
                    l.add(new ComboBoxWrapper<>(us, us.getId(), us.getInstitucion().getNombre()));
                }
            }
        }
        if (tipoDocumentoList != null && s != null) {
            List<TipoDocumento> tdList = new TipoDocumentoJPAController().findBy(s);
            for (TipoDocumento tipoDocumento : tdList) {
                tipoDocumentoList.add(new ComboBoxWrapper<>(tipoDocumento, tipoDocumento.getId(), tipoDocumento.getNombre()));
            }
        }
    }

    /**
     *
     * @return true if JOptionPane.YES_OPTION
     */
    static boolean reImprimirCodigoBarraArchivo() {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, SGD.getResources().getObject("reimprimircodigo"), "Confirmar", JOptionPane.YES_NO_OPTION);
    }

    public static String getBarcode(Archivo o) {
        return getBarcode(o.getInstitucion(), o.getSector(), o.getCodigo());
    }

    /**
     *
     * @param institucion
     * @param sector
     * @param codigo
     * @return #-###-######
     */
    static String getBarcode(Institucion institucion, Sector sector, Integer codigo) {
        return institucion.getId().toString() + "-" + sector.getSectorUI().getCode() + "-" + UTIL.AGREGAR_CEROS(codigo, 6);
    }

    /**
     *
     * @return true si ACEPTA
     */
    static boolean confirmarReAperturaDeArchivo() {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
                "El Archivo que va modificar se encuentra clasificado como \"Cerrado\"."
                + "\nLas modificaciones implican un cambio en su estado y una reasignación de números de precinto."
                + "\n¿Desea continuar?", "Re apertura de Archivo", JOptionPane.YES_NO_OPTION);
    }

    /**
     * Visualiza una GUI en la cual se cargarán hasta 4 códigos de precintos.
     *
     * @param owner
     * @return Una lista vacía si no ingresó ningún precinto, o hasta 4.
     */
    static List<String> initPrecintosUI(JDialog owner) {
        precintosList = new ArrayList<>(4);
        panelPrecintos = new AddPrecintosPanel();
        final CustomABMJDialog customABMJDialog = new CustomABMJDialog(owner, panelPrecintos, "Carga de códigos de Precintos", true, null);
        customABMJDialog.setMessageText("Ingrese el/los código(s) de precinto(s) que formarán parte de la caja.");
        customABMJDialog.getBtnExtraBottom().setVisible(false);
        customABMJDialog.getjToolBar().setVisible(false);
        customABMJDialog.getBtnAceptar().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    precintosList = panelPrecintos.getCodigoPrecintos();
                    if (precintosList.isEmpty()) {
                        throw new IllegalArgumentException("Debe ingresar al menos un código de precinto");
                    }
                    customABMJDialog.dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(customABMJDialog, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        customABMJDialog.getBtnCancelar().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                customABMJDialog.dispose();
            }
        });
        customABMJDialog.setLocationRelativeTo(owner);
        customABMJDialog.setVisible(true);
        return precintosList;
    }

    static void initPrecintosUI(JDialog owner, List<? extends Precinto> precintos) {
        panelPrecintos = new AddPrecintosPanel();
        List<String> p = new ArrayList<>(4);
        for (Precinto precinto : precintos) {
            p.add(precinto.getCodigo());
        }
        panelPrecintos.setCodigoPrecintos(p);
        JDialog jDialog = new JDialog(owner, "N° de precintos", true);
        jDialog.add(panelPrecintos);
        jDialog.pack();
        jDialog.setLocationRelativeTo(owner);
        jDialog.setVisible(true);
    }

    /**
     * Se encarga del evento del comboBox de Instituciones. De cargar los
     * archivos Determina el SectorUI por medio del valor seleccionado del
     * combobox el cual contiene
     * {@link ComboBoxWrapper}&lt{@link UsuarioSector}> como items
     *
     * @param panel
     */
    static void setCargadorDeArchivosParaReciboListener(final ABMReciboPanel panel) {
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) panel.getCbInstitucion().getSelectedItem();
        final SectorUI sectorUI = cbw.getEntity().getSector().getSectorUI();
        panel.getCbInstitucion().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) panel.getCbInstitucion().getSelectedItem();
                Institucion institucion = cbw.getEntity().getInstitucion();
                DefaultTableModel dtm = (DefaultTableModel) panel.getjTableBuscador().getModel();
                dtm.setRowCount(0);
                List<? extends Archivo> list;
                if (sectorUI == SectorUI.AFILIACION) {
                    list = new AfiliacionJPAController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.APE) {
                    list = new ApeJPAController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.CONTABLE) {
                    list = new ContableJPAController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.PSICOFISICO) {
                    list = new PsicofisicoJPAController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.FACTURACION) {
                    list = new FacturacionJpaController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.AUDITORIA) {
                    list = new AuditoriaJpaController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.GREMIALES) {
                    list = new GremialesJpaController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.CRONICO) {
                    list = new CronicoJpaController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.DISCAPACIDAD) {
                    list = new DiscapacidadJpaController().findToArchivar(institucion);
                } else if (sectorUI == SectorUI.AUDITORIAMEDICA) {
                    list = new AuditoriaMedicaJpaController().findToArchivar(institucion);
                } else {
                    throw new IllegalArgumentException(SGD.getResources().getString("undefinedsectorimplementation") + sectorUI);
                }
                for (Archivo o : list) {
                    dtm.addRow(new Object[]{o, o.getBarcode(), precintosToString(o.getPrecintos())});
                }
            }
        });
    }

    static void setCargadorDeArchivosParaRecepcionListener(final ABMRecepcionPanel panel) {
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) panel.getCbInstitucion().getSelectedItem();
        final SectorUI sectorUI = cbw.getEntity().getSector().getSectorUI();
        panel.getCbInstitucion().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) panel.getCbInstitucion().getSelectedItem();
                Institucion institucion = cbw.getEntity().getInstitucion();
                DefaultTableModel dtm = (DefaultTableModel) panel.getjTableBuscador().getModel();
                dtm.setRowCount(0);
                List<? extends Archivo> list;
                if (sectorUI == SectorUI.AFILIACION) {
                    list = new AfiliacionJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.APE) {
                    list = new ApeJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.CONTABLE) {
                    list = new ContableJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.PSICOFISICO) {
                    list = new PsicofisicoJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.FACTURACION) {
                    list = new FacturacionJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.AUDITORIA) {
                    list = new AuditoriaJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.GREMIALES) {
                    list = new GremialesJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.CRONICO) {
                    list = new CronicoJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.DISCAPACIDAD) {
                    list = new DiscapacidadJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.AUDITORIAMEDICA) {
                    list = new AuditoriaMedicaJpaController().findEnviados(institucion);
                } else {
                    throw new IllegalArgumentException(SGD.getResources().getString("undefinedsectorimplementation") + sectorUI);
                }
                for (Archivo o : list) {
                    String precintosString = "";
                    for (Precinto precinto : o.getPrecintos()) {
                        precintosString += precinto.getCodigo() + ", ";
                    }
                    precintosString = precintosString.substring(0, precintosString.length() - 2);
                    dtm.addRow(new Object[]{o, o.getBarcode(), precintosString, o.getRecibo().getNumero(), UTIL.DATE_FORMAT.format(o.getCreation())});
                }
            }
        });
    }

    static void setCargadorDeArchivosParaSolicitudListener(final JComboBox cbUsuarioSector, final JTable table) {
        @SuppressWarnings("unchecked")
        ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) cbUsuarioSector.getSelectedItem();
        final SectorUI sectorUI = cbw.getEntity().getSector().getSectorUI();
        cbUsuarioSector.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                ComboBoxWrapper<UsuarioSector> cbw = (ComboBoxWrapper<UsuarioSector>) cbUsuarioSector.getSelectedItem();
                Institucion institucion = cbw.getEntity().getInstitucion();
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                dtm.setRowCount(0);
                List<? extends Archivo> list;
                if (sectorUI == SectorUI.AFILIACION) {
                    list = new AfiliacionJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.APE) {
                    list = new ApeJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.CONTABLE) {
                    list = new ContableJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.PSICOFISICO) {
                    list = new PsicofisicoJPAController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.FACTURACION) {
                    list = new FacturacionJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.AUDITORIA) {
                    list = new AuditoriaJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.GREMIALES) {
                    list = new GremialesJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.CRONICO) {
                    list = new CronicoJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.DISCAPACIDAD) {
                    list = new DiscapacidadJpaController().findEnviados(institucion);
                } else if (sectorUI == SectorUI.AUDITORIAMEDICA) {
                    list = new AuditoriaMedicaJpaController().findEnviados(institucion);
                } else {
                    throw new IllegalArgumentException(SGD.getResources().getString("undefinedsectorimplementation") + sectorUI);
                }
                for (Archivo o : list) {
                    String precintosString = "";
                    for (Precinto precinto : o.getPrecintos()) {
                        precintosString += precinto.getCodigo() + ", ";
                    }
                    precintosString = precintosString.substring(0, precintosString.length() - 2);
                    dtm.addRow(new Object[]{o, o.getBarcode(), precintosString, o.getRecibo().getNumero(), UTIL.DATE_FORMAT.format(o.getCreation())});
                }
            }
        });
    }
}
