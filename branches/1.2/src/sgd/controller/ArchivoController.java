package sgd.controller;

import sgd.gui.JDBuscador;
import sgd.jpa.model.Archivo;
import sgd.jpa.model.Recibo;

/**
 * Viendo aún como implementar esto..
 *
 * @author FiruzzZ
 * @param <E>
 */
public abstract class ArchivoController<E extends Archivo> {

    protected JDBuscador buscador;
//    protected SGDJpaImp<E,Serializable> jpaController;

    public ArchivoController() {
        //algún dia... se va terminar el controlador por polimorfismo
    }

    abstract void enviado(Integer archivoId, Recibo recibo);

    abstract E find(Integer archivoId);

    abstract CustomABMJDialog viewArchivo(E archivo);

    abstract void recepcionar(Integer archivoId);
}
