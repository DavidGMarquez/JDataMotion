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
import jdatamotioncommon.filtros.DoubleParameter;
import jdatamotioncommon.filtros.StringParameter;
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
public class FiltroLimite implements IFilter {

    private static final String TIPO_LIMITE = Vista.bundle.getString("tipoLimite");
    private static final String LIMITE_VALOR = Vista.bundle.getString("limiteValor");
    private static final String LIMITE_PERCENTIL = Vista.bundle.getString("limitePercentil");

    private static final String TIPO_COTA = Vista.bundle.getString("tipoCota");
    private static final String COTA_SUPERIOR = Vista.bundle.getString("cotaSuperior");
    private static final String COTA_INFERIOR = Vista.bundle.getString("cotaInferior");

    private static final String VALOR = Vista.bundle.getString("valor");

    private final HashMap<String, Parameter> parameters = new HashMap<>();

    @Override
    public HashMap<String, Parameter> getParameters() {
        return parameters;
    }

    public FiltroLimite() {
        this.parameters.put(TIPO_LIMITE, new StringParameter(new String[]{LIMITE_VALOR, LIMITE_PERCENTIL}));
        this.parameters.put(TIPO_COTA, new StringParameter(new String[]{COTA_SUPERIOR, COTA_INFERIOR}));
        this.parameters.put(VALOR, new DoubleParameter());
    }

    @Override
    public ComparableInstances filter(ComparableInstances comparableInstances, Integer filteredAttributeIndex, Map<String, Parameter> parameters) {
        if (!IFilter.isEverythingConfigured(filteredAttributeIndex, parameters) || !comparableInstances.attribute(filteredAttributeIndex).isNumeric()) {
            return comparableInstances;
        }
        ComparableInstances ins = new ComparableInstances(comparableInstances);
        Double valorNumerico = 0.0;
        if (parameters.get(TIPO_LIMITE).getValue().equals(LIMITE_VALOR)) {
            valorNumerico = (Double) parameters.get(VALOR).getValue();
        } else if (parameters.get(TIPO_LIMITE).getValue().equals(LIMITE_PERCENTIL)) {
            valorNumerico = Modelo.getValorPercentil(comparableInstances, ((Double) parameters.get(VALOR).getValue()).intValue(), filteredAttributeIndex);
        }
        Iterator<Instance> it = ins.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            Double v = instance.isMissing(filteredAttributeIndex) ? null : instance.value(filteredAttributeIndex);
            if (v != null && valorNumerico != null && ((parameters.get(TIPO_COTA).getValue().equals(COTA_SUPERIOR) && v > valorNumerico) || (parameters.get(TIPO_COTA).getValue().equals(COTA_INFERIOR) && v < valorNumerico))) {
                instance.setValue(filteredAttributeIndex, valorNumerico);
            }
        }
        return ins;
    }

    @Override
    public String toString() {
        return "Filtro de límite";
    }

}
