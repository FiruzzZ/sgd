package sgd.controller;

import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import sgd.controller.exception.MessageException;
import sgd.controller.exception.UnauthorizedModuleAccessException;
import sgd.jpa.model.SectorUI;

/**
 * Encargada de controlar el acceso a las GUI's del sistema según los permisos del Usuario.
 *
 * @author FiruzzZ
 */
public class UIAccessManager {

    //singleton
    private static UIAccessManager uIAccessManager;

    private UIAccessManager() {
    }

    /**
     * Retorna la instancia del administrador de pantallas
     *
     * @return
     */
    public static UIAccessManager getInstance() {
        if (uIAccessManager == null) {
            uIAccessManager = new UIAccessManager();
        }
        return uIAccessManager;
    }

    /**
     *
     * @param sectorUI
     * @return 1 = LECTURA (VIEW), 2 = TODO (CRUD)
     * @throws UnauthorizedModuleAccessException Si no tiene permisos de acceso al móduclo
     */
    private int checkPermiso(SectorUI sectorUI) throws UnauthorizedModuleAccessException {
        Integer permisos = null;
        //Cuando se intenta acceder al ABM de Usuarios
        if (sectorUI.equals(SectorUI.USUARIOS)) {
            if (UsuarioController.administraUsuarios()) {
                permisos = 2;
            }
        } else {
            permisos = UsuarioController.getPermisoByCodigoPantalla(sectorUI.getCode());
        }

        if (permisos == null) {
            //podría ser lanzado por getPermisoByCodigoPantalla, pero...
            throw new UnauthorizedModuleAccessException(sectorUI.getNombre());
        }
        return permisos;
    }

    private void setUISegunPermiso(CustomABMJDialog jd, int permiso) {
        if (permiso == 1) {
            jd.getBtnNuevo().setEnabled(false);
            jd.getBtnEditar().setEnabled(false);
            jd.getBtnBorrar().setEnabled(false);
            jd.setEnabledBottomButtons(false);
            jd.setTitle(jd.getTitle() + " (MODO VISTA)");
        }
    }

    public JDialog getCambiarPassword(JFrame owner) {
        JDialog jd = new UsuarioController().initCambiarPass(owner);
        return jd;
    }

    public String getLoggin(JFrame aThis) {
        UsuarioController.setCurrentUser(null);
        new UsuarioController().initLogin(aThis);
        return UsuarioController.getCurrentUser() != null ? UsuarioController.getCurrentUser().getNombre() : null;
    }

    public JDialog getABMUsuarios(Window owner) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(SectorUI.USUARIOS);
        CustomABMJDialog jd = new UsuarioController().getABMUsuarioUI(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMAfiliaciones(Window owner) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(SectorUI.AFILIACION);
        CustomABMJDialog jd = new AfiliacionController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMInstituciones(Window owner) {
        int permiso = (UsuarioController.getCurrentUser().getId() == 1) ? 2 : 1;
        CustomABMJDialog jd = new InstitucionController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMTipoDocumento(Window owner) throws UnauthorizedModuleAccessException {
        if (!UsuarioController.administraAlgunSector()) {
            throw new UnauthorizedModuleAccessException("Tipo de Documentos");
        }
        CustomABMJDialog jd = new TipoDocumentoController().getAbm(owner);
        setUISegunPermiso(jd, 2);
        return jd;
    }

    public JDialog getABMSubTipoDocumento(Window owner) throws UnauthorizedModuleAccessException {
        if (!UsuarioController.administraAlgunSector()) {
            throw new UnauthorizedModuleAccessException("Sub-Tipo de Documentos");
        }
        CustomABMJDialog jd = new SubTipoDocumentoController().getAbm(owner);
        setUISegunPermiso(jd, 2);
        return jd;
    }

    public JDialog getABMApe(Window owner) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(SectorUI.APE);
        CustomABMJDialog jd = new ApeController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMContable(Window owner) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(SectorUI.CONTABLE);
        CustomABMJDialog jd = new ContableController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMEnviosAfiliacion(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.AFILIACION);
    }

    public JDialog getABMEnviosContable(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.CONTABLE);
    }

    public JDialog getABMEnviosApe(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.APE);
    }

    public JDialog getABMEnviosPsicofisico(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.PSICOFISICO);
    }

    public JDialog getABMEnviosAuditoria(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.AUDITORIA);
    }
    
    public JDialog getABMEnviosAuditoriaMedica(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.AUDITORIAMEDICA);
    }

    public JDialog getABMEnviosFacturacion(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.FACTURACION);
    }

    public JDialog getABMEnviosGremiales(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.GREMIALES);
    }

    public JDialog getABMCronico(Window owner) throws UnauthorizedModuleAccessException, MessageException {
        int permiso = checkPermiso(SectorUI.CRONICO);
        CustomABMJDialog jd = new CronicoController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMEnviosCronico(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.CRONICO);
    }

    public JDialog getABMRecepcionCronico(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.CRONICO);
    }

    public JDialog getABMPsicofisico(Window owner) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(SectorUI.PSICOFISICO);
        CustomABMJDialog jd = new PsicofisicoController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMFacturacion(Window owner) throws UnauthorizedModuleAccessException, MessageException {
        int permiso = checkPermiso(SectorUI.FACTURACION);
        CustomABMJDialog jd = new FacturacionController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMAuditoria(Window owner) throws UnauthorizedModuleAccessException, MessageException {
        int permiso = checkPermiso(SectorUI.AUDITORIA);
        CustomABMJDialog jd = new AuditoriaController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }
    
     public JDialog getABMAuditoriaMedica(Window owner) throws UnauthorizedModuleAccessException, MessageException {
        int permiso = checkPermiso(SectorUI.AUDITORIAMEDICA);
        CustomABMJDialog jd = new AuditoriaMedicaController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMGremiales(Window owner) throws UnauthorizedModuleAccessException, MessageException {
        int permiso = checkPermiso(SectorUI.GREMIALES);
        CustomABMJDialog jd = new GremialesController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMRecepcionAfiliacion(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.AFILIACION);
    }

    public JDialog getABMRecepcionApe(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.APE);
    }

    public JDialog getABMRecepcionAuditoria(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.AUDITORIA);
    }
    
    public JDialog getABMRecepcionAuditoriaMedica(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.AUDITORIAMEDICA);
    }

    public JDialog getABMRecepcionContable(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.CONTABLE);
    }

    public JDialog getABMRecepcionFacturacion(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.FACTURACION);
    }

    public JDialog getABMRecepcionPsicofisico(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.PSICOFISICO);
    }

    public JDialog getABMRecepcionGremiales(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.GREMIALES);
    }

    public JDialog getABMSolicitudAfiliacion(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.AFILIACION);
    }

    public JDialog getABMSolicitudApe(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.APE);
    }

    public JDialog getABMSolicitudAuditoria(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.AUDITORIA);
    }
    
     public JDialog getABMSolicitudAuditoriaMedica(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.AUDITORIAMEDICA);
    }

    public JDialog getABMSolicitudContable(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.CONTABLE);
    }

    public JDialog getABMSolicitudFacturacion(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.FACTURACION);
    }

    public JDialog getABMSolicitudPsicofisico(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.PSICOFISICO);
    }

    public JDialog getABMSolicitudGremiales(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.GREMIALES);
    }

    public JDialog getABMSolicitudCronico(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.CRONICO);
    }

    public JDialog getABMDiscapacidad(Window owner) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(SectorUI.DISCAPACIDAD);
        CustomABMJDialog jd = new DiscapacidadController().getAbm(owner);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    public JDialog getABMEnviosDiscapacidad(Window owner) throws UnauthorizedModuleAccessException {
        return getABMEnvios(owner, SectorUI.DISCAPACIDAD);
    }

    public JDialog getABMRecepcionDiscapacidad(Window owner) throws UnauthorizedModuleAccessException {
        return getABMRecepcion(owner, SectorUI.DISCAPACIDAD);
    }

    public JDialog getABMSolicitudDiscapacidad(Window owner) throws UnauthorizedModuleAccessException {
        return getABMSolicitud(owner, SectorUI.DISCAPACIDAD);
    }

    private JDialog getABMEnvios(Window owner, SectorUI sector) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(sector);
        CustomABMJDialog jd = new ReciboController().getAbm(owner, sector);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    private JDialog getABMSolicitud(Window owner, SectorUI sector) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(sector);
        CustomABMJDialog jd = new SolicitudController().getAbm(owner, sector);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

    private JDialog getABMRecepcion(Window owner, SectorUI sector) throws UnauthorizedModuleAccessException {
        int permiso = checkPermiso(sector);
        CustomABMJDialog jd = new RecepcionController().getAbm(owner, sector);
        setUISegunPermiso(jd, permiso);
        return jd;
    }

}
