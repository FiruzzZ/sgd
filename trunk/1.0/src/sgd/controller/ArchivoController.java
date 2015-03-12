package sgd.controller;

import sgd.jpa.model.Recibo;

/**
 * Viendo aún como implementar esto..
 *
 * @author FiruzzZ
 */
public abstract class ArchivoController {

    public ArchivoController() {
        //algún dia... se va terminar el controlador por polimorfismo
    }

    abstract void enviado(Integer archivoId, Recibo recibo);
}
