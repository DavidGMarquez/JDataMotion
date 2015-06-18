/*
 * The MIT License
 *
 * Copyright 2014 usuario.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, mergebundle, publish, distribute, sublicense, and/or sell
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
import jdatamotioncommon.filtros.DoubleParameter;
import java.util.Iterator;
import java.util.Map;
import jdatamotion.Modelo;
import jdatamotion.Vista;
import jdatamotioncommon.ComparableInstances;
import jdatamotioncommon.filtros.IFilter;
import jdatamotioncommon.filtros.Parameter;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class FiltroEliminacionOutliers implements IFilter {

    private static final String NUMERO_DE_DTS = Vista.bundle.getString("numeroDeDTs");

    @Override
    public HashMap<String, Parameter> getParameters() {
        HashMap<String, Parameter> p = new HashMap<>();
        p.put(NUMERO_DE_DTS, new DoubleParameter());
        return p;
    }

    @Override
    public ComparableInstances filter(ComparableInstances comparableInstances, Integer filteredAttributeIndex, Map<String, Parameter> parameters) {
        if (!IFilter.isEverythingConfigured(filteredAttributeIndex, parameters) || !comparableInstances.attribute(filteredAttributeIndex).isNumeric()) {
            return comparableInstances;
        }
        ComparableInstances ins = new ComparableInstances(comparableInstances);
        Double desvTipica = Modelo.getDesviacionTipica(ins, filteredAttributeIndex), media = Modelo.getMedia(ins, filteredAttributeIndex);
        Double numDTs = (Double) parameters.get(NUMERO_DE_DTS).getValue();
        Iterator<Instance> it = ins.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            Double v = instance.isMissing(filteredAttributeIndex) ? null : instance.value(filteredAttributeIndex);
            if (v != null && (v < media - numDTs * desvTipica || v > media + numDTs * desvTipica)) {
                it.remove();
            }
        }
        return ins;
    }

    @Override
    public String toString() {
        return "Filtro de eliminación de outliers";
    }
}
