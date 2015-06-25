/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdatamotion.filter.sample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jdatamotioncommon.ComparableInstances;
import jdatamotioncommon.filtros.DoubleParameter;
import jdatamotioncommon.filtros.IFilter;
import jdatamotioncommon.filtros.Parameter;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class MiFiltro2 implements IFilter {

    private static final String VALOR = "valor";

    @Override
    public ComparableInstances filter(ComparableInstances comparableInstances, Integer filteredAttributeIndex, Map<String, Parameter> parameters) {
        if (!IFilter.isEverythingConfigured(filteredAttributeIndex, parameters) || !comparableInstances.attribute(filteredAttributeIndex).isNumeric()) {
            return comparableInstances;
        }
        Iterator<Instance> it = comparableInstances.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            Double v = instance.isMissing(filteredAttributeIndex) ? null : instance.value(filteredAttributeIndex);
            instance.setValue(filteredAttributeIndex, -(double) parameters.get(VALOR).getValue() + v);
        }
        return comparableInstances;
    }

    @Override
    public Map<String, Parameter> getParametersNeeded() {
        Map<String, Parameter> p = new HashMap<>();
        p.put(VALOR, new DoubleParameter());
        return p;
    }

    @Override
    public String getName() {
        return "Mi filtro2";
    }

}
