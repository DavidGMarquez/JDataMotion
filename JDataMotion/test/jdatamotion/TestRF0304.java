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

import java.io.File;
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
public class TestRF0304 extends TestCase {

    public void test() {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        vista.inicializar(modelo, false);
        Controlador.setDebug(true);
        String resource = "example01.arff";
        String archivoSesion = "temp01.jdms";
        try {
            String pathEntrada = new URI(getClass().getResource(resource).toString()).getPath();
            ComparableInstances is1 = ValidFileLoading.loadARFF(pathEntrada);
            modelo.setComparableInstances(new ComparableInstances(is1));
            modelo.setDireccionAoFicheiro(pathEntrada);
            modelo.setHashCodeFicheiro(Modelo.resumirFicheiroSHA1(new File(pathEntrada)));
            String pathSaida = new URI(getClass().getResource(".").toString()).getPath() + archivoSesion;
            /* se modificamos un dato manualmente, a sesion non o rexistra
             e ao restaurar a sesion o arquivo segue sendo igual ao inicial */
            modelo.getComparableInstances().instance(5).setValue(5, 666);
            /* se lanzamos un comando, a sesión rexistrao e entonces a proba fallaria
             porque se volveria a executar o comando ao restaurar a sesion, e xa non seria igual ao inicial */
            //vista.getControlador().manexarEvento(Controlador.MUDAR_DATO, new Object[]{5, 5, 666});
            vista.getControlador().manexarEvento(Controlador.GARDAR_SESION, pathSaida);
            vista.getControlador().manexarEvento(Controlador.ABRIR_SESION, pathSaida);
            ComparableInstances is2 = modelo.getComparableInstances();
            assertEquals(is1, is2);
        } catch (Exception ex) {
            Logger.getLogger(TestRF0304.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Test suite() {
        return new TestSuite(TestRF0304.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }

}
