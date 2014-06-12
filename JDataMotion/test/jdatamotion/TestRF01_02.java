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
import junit.framework.TestSuite;

public class TestRF01_02 extends TestCase {

    public void test() {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        vista.inicializar(modelo, false);
        try {
            String pathEntrada = new URI(getClass().getResource("example01.arff").toString()).getPath();
            String pathSaida = new URI(getClass().getResource(".").toString()).getPath() + "temp01.arff";
            vista.getMeuControlador().manexarEvento(Controlador.IMPORTAR_FICHEIRO, pathEntrada);
            InstancesComparable is1 = modelo.getAtributos();
            vista.getMeuControlador().manexarEvento(Controlador.EXPORTAR_FICHEIRO, new Object[]{"arff", pathSaida});
            vista.getMeuControlador().manexarEvento(Controlador.IMPORTAR_FICHEIRO, pathSaida);

            //Altera o valor do quinto valor da quinta instancia
            //modelo.getAtributos().instance(5).setValue(5, 9.0);
            //Altera o nome do quinto atributo
            //modelo.getAtributos().renameAttribute(5, "OTRO NOMBRE ATRIBUTO");
            //Altera o nome da relación
            //modelo.getAtributos().setRelationName("OTRO NOMBRE RELACION");
            
            InstancesComparable is2 = modelo.getAtributos();
            assertEquals(is1, is2);
        } catch (URISyntaxException ex) {
            Logger.getLogger(TestRF01_02.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Test suite() {
        return new TestSuite(TestRF01_02.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }

}
