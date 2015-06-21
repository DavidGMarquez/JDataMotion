/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdatamotioncommon.filtros;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import jdatamotioncommon.ComparableInstances;

/**
 *
 * @author usuario
 */
public interface IFilter extends Serializable {

    public static boolean isEverythingConfigured(Integer filteredAttributeIndex, Map<String, Parameter> parameters) {
        return isConfiguredFilteredAttribute(filteredAttributeIndex) && areParametersConfigured(parameters);
    }

    public static boolean isConfiguredFilteredAttribute(Integer filteredAttributeIndex) {
        return filteredAttributeIndex != null;
    }

    public static boolean areParametersConfigured(Map<String, Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            Iterator<Entry<String, Parameter>> it = parameters.entrySet().iterator();
            while (it.hasNext()) {
                if (it.next().getValue().getValue() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public ComparableInstances filter(ComparableInstances comparableInstances, Integer filteredAttributeIndex, Map<String, Parameter> parameters);

    public abstract String getName();

    public abstract Map<String, Parameter> getParametersNeeded();

}
