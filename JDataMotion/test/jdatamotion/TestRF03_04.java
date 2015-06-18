/*
 * The MIT License
 *
 * Copyright 2014 usuario.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jdatamotion;

/**
 *
 * @author usuario
 */
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import junit.framework.TestSuite;

public class TestRF03_04 extends TestCase {

    public void test() {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        vista.inicializar(modelo, false);
        try {
            String pathEntrada = new URI(getClass().getResource("example01.arff").toString()).getPath();
            String pathSaida = new URI(getClass().getResource(".").toString()).getPath() + "temp01.jdms";
            vista.getControlador().manexarEvento(Controlador.IMPORTAR_FICHEIRO, pathEntrada);
            vista.getControlador().manexarEvento(Controlador.MUDAR_DATO, new Object[]{5, 5, 45.0});
            vista.getControlador().manexarEvento(Controlador.ELIMINAR_DATOS, new Integer[]{2, 4, 12});
            Controlador c1 = vista.getControlador();
            vista.getControlador().manexarEvento(Controlador.GARDAR_SESION, pathSaida);
            modelo = new Modelo();
            vista = new Vista();
            vista.inicializar(modelo, false);
            vista.getControlador().manexarEvento(Controlador.ABRIR_SESION, pathSaida);
            vista.getControlador().manexarEvento(Controlador.DESFACER, null);
            vista.getControlador().manexarEvento(Controlador.REFACER, null);

            //Altera o valor do quinto valor da quinta instancia
            //modelo.getAtributos().instance(5).setValue(5, 9.0);
            //Altera o nome do quinto atributo
            //modelo.getAtributos().renameAttribute(5, "OTRO NOMBRE ATRIBUTO");
            //Altera o nome da relación
            //modelo.getAtributos().setRelationName("OTRO NOMBRE RELACION");
            //Altera a direccion ao ficheiro
            //modelo.setDireccionAoFicheiro("OTRA DIRECCION AL FICHERO");
            //Altera o hashcode
            //modelo.setHashCode(new byte[]{2,2,2});
            //Altera o índice temporal
            /*try {
             modelo.setIndiceTemporal(-4);
             } catch (ExcepcionFormatoIdentificacionTemporal ex) {
             Logger.getLogger(TestRF03_04.class.getName()).log(Level.SEVERE, null, ex);
             }*/
            //Altera o índice do atributo nominal
            //modelo.setIndiceAtributoNominal(-4);
            Controlador c2 = vista.getControlador();
            assertEquals(c1, c2);
        } catch (URISyntaxException ex) {
            Logger.getLogger(TestRF03_04.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Test suite() {
        return new TestSuite(TestRF03_04.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }

}
