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
package jdatamotion.filtros;

import java.util.Iterator;
import jdatamotion.InstancesComparable;
import jdatamotion.Modelo;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class FiltroNormalizacion extends AbstractFilter {

    public FiltroNormalizacion(InstancesComparable atributos) {
        super(atributos);
    }

    @Override
    public InstancesComparable filter(InstancesComparable instancesComparable) {
        if (getIndiceAtributoFiltrado() == null) {
            return instancesComparable;
        }
        InstancesComparable ins = new InstancesComparable(instancesComparable);
        Double desvTipica = Modelo.getDesviacionTipica(ins, getIndiceAtributoFiltrado()), media = Modelo.getMedia(instancesComparable, getIndiceAtributoFiltrado());
        Iterator<Instance> it = ins.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            Double v = instance.isMissing(getIndiceAtributoFiltrado()) ? null : instance.value(getIndiceAtributoFiltrado());
            if (v != null) {
                Double vInstance = instance.value(getIndiceAtributoFiltrado());
                instance.setValue(getIndiceAtributoFiltrado(), (vInstance - media) / desvTipica);
            }
        }
        return ins;
    }

    @Override
    public String toString() {
        return "Filtro de normalización";
    }

}
