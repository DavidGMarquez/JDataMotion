package jdatamotion;

/**
 *
 * @author Pablo Pérez Romaní
 */
public class JDataMotion {

    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        vista.inicializar(modelo, true);
    }
}
