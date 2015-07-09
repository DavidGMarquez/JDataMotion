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
import java.util.logging.Level;
import java.util.logging.Logger;
import jdatamotioncommon.ComparableInstances;
import junit.framework.Test;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import junit.framework.TestSuite;
import weka.core.Attribute;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 *
 * @author usuario
 */
public class TestRF09 extends TestCase {

    public void test() {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        vista.inicializar(modelo, false);
        Controlador.setDebug(true);
        String resource = "213_N3.csv";
        int columna = 39;
        int novoDato = Attribute.NOMINAL;
        try {
            String pathEntrada = new URI(getClass().getResource(resource).toString()).getPath();
            ComparableInstances is1 = ValidFileLoading.loadCSV(pathEntrada);
            modelo.setComparableInstances(new ComparableInstances(is1));
            vista.getControlador().manexarEvento(Controlador.MUDAR_TIPO, new Object[]{columna, novoDato});
            NumericToNominal filtro = new NumericToNominal();
            filtro.setOptions(new String[]{"-R", String.valueOf(columna + 1)});
            filtro.setInputFormat(is1);
            is1 = new ComparableInstances(Filter.useFilter(is1, filtro));
            ComparableInstances is2 = modelo.getComparableInstances();
            is1.setRelationName("");
            is2.setRelationName("");
            assertEquals(is1, is2);
        } catch (Exception ex) {
            Logger.getLogger(TestRF09.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Test suite() {
        return new TestSuite(TestRF09.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }

}
