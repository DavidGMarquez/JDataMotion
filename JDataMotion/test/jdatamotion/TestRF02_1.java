/*
 * The MIT License
 *
 * Copyright 2015 usuario.
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdatamotioncommon.ComparableInstances;
import junit.framework.Test;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import junit.framework.TestSuite;

/**
 *
 * @author usuario
 */
public class TestRF02_1 extends TestCase {

    public void test() {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        vista.inicializar(modelo, false);
        Controlador.setDebug(true);
        String resource = "example01.arff";
        String tempSaida = "temp01.csv";
        try {
            String pathEntrada = new URI(getClass().getResource(resource).toString()).getPath();
            ComparableInstances is1 = ValidFileLoading.loadARFF(pathEntrada);
            modelo.setComparableInstances(new ComparableInstances(is1));
            String pathSaida = new URI(getClass().getResource(".").toString()).getPath() + tempSaida;
            vista.getControlador().manexarEvento(Controlador.EXPORTAR_FICHEIRO, new Object[]{"csv", pathSaida});
            ComparableInstances is2 = ValidFileLoading.loadCSV(pathSaida);
            is1.setRelationName("");
            is2.setRelationName("");
            assertEquals(is1, is2);
        } catch (URISyntaxException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Test suite() {
        return new TestSuite(TestRF02_1.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }

}
