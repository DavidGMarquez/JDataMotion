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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jdatamotion.Modelo;
import jdatamotioncommon.ComparableInstances;
import jdatamotioncommon.filtros.DoubleParameter;
import jdatamotioncommon.filtros.IFilter;
import jdatamotioncommon.filtros.Parameter;
import jdatamotioncommon.filtros.StringParameter;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class FiltroNormalizacion implements IFilter {

    @Override
    public Map<String, Parameter> getParametersNeeded() {
        Map<String, Parameter> p = new HashMap<>();
        return p;
    }

    public FiltroNormalizacion() {
    }

    @Override
    public ComparableInstances filter(ComparableInstances comparableInstances, Integer filteredAttributeIndex, Map<String, Parameter> parameters) {
        if (!IFilter.isEverythingConfigured(filteredAttributeIndex, parameters) || !comparableInstances.attribute(filteredAttributeIndex).isNumeric()) {
            return comparableInstances;
        }
        Double desvTipica = Modelo.getDesviacionTipica(comparableInstances, filteredAttributeIndex), media = Modelo.getMedia(comparableInstances, filteredAttributeIndex);
        Iterator<Instance> it = comparableInstances.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            Double v = instance.isMissing(filteredAttributeIndex) ? null : instance.value(filteredAttributeIndex);
            if (v != null) {
                Double vInstance = instance.value(filteredAttributeIndex);
                instance.setValue(filteredAttributeIndex, (vInstance - media) / desvTipica);
            }
        }
        return comparableInstances;
    }

    @Override
    public String getName() {
        return "Filtro de normalización";
    }

}
