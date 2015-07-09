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
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdatamotion.Vista.recursosIdioma;
import jdatamotion.filtros.FiltroLimite;
import jdatamotioncommon.ComparableInstances;
import jdatamotioncommon.filtros.DoubleParameter;
import jdatamotioncommon.filtros.IFilter;
import jdatamotioncommon.filtros.StringParameter;
import junit.framework.Test;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import junit.framework.TestSuite;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class TestRF2628 extends TestCase {

    public void test() {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        vista.inicializar(modelo, false);
        Controlador.setDebug(true);
        String resource = "example01.arff";
        IFilter filtro = new FiltroLimite();
        Double maximo = 15.0;
        int columna = 1;
        StringParameter sp1 = new StringParameter(), sp2 = new StringParameter();
        DoubleParameter sp3 = new DoubleParameter();
        sp1.setValue(recursosIdioma.getString("limiteValor"));
        sp2.setValue(recursosIdioma.getString("cotaSuperior"));
        sp3.setValue(maximo);
        try {
            String pathEntrada = new URI(getClass().getResource(resource).toString()).getPath();
            ComparableInstances is1 = ValidFileLoading.loadARFF(pathEntrada);
            modelo.setComparableInstances(new ComparableInstances(is1));
            vista.getControlador().manexarEvento(Controlador.ENGADIR_FILTRO, new Object[]{0, filtro});
            modelo.getFiltro(0).getParameters().put(recursosIdioma.getString("tipoLimite"), sp1);
            modelo.getFiltro(0).getParameters().put(recursosIdioma.getString("tipoCota"), sp2);
            modelo.getFiltro(0).getParameters().put(recursosIdioma.getString("valor"), sp3);
            modelo.getFiltro(0).setIndiceAtributoFiltrado(columna);
            boolean todosMenores = true;
            Enumeration<Instance> e = modelo.getComparableInstancesFiltradas().enumerateInstances();
            while (e.hasMoreElements()) {
                Instance i = e.nextElement();
                if (i.value(columna) > maximo) {
                    todosMenores = false;
                }
            }
            assertEquals(todosMenores, true);
        } catch (URISyntaxException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Test suite() {
        return new TestSuite(TestRF2628.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }

}
