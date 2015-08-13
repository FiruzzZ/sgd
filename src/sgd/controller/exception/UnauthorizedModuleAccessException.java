package sgd.controller.exception;

/**
 * Thrown when a user try to access to UI which has no permissions.
 * <br>DEFAULT MESSAGE for constructor without args:
 * <br><i>"No tiene autorización para ingresar al módulo"</i>
 * @author FiruzzZ
 */
public class UnauthorizedModuleAccessException extends Exception {

    public UnauthorizedModuleAccessException() {
        super("No tiene permisos para ingresar al módulo");
    }

    public UnauthorizedModuleAccessException(String message) {
        super("No tiene permisos para ingresar al módulo " + message);
    }
    
    
}
