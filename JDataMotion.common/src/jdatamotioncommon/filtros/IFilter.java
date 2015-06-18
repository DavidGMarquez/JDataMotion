/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdatamotioncommon.filtros;

import java.util.Map;
import jdatamotioncommon.ComparableInstances;

/**
 *
 * @author usuario
 */
public interface IFilter {

    public static boolean isEverythingConfigured(Integer filteredAttributeIndex, Map<String, Parameter> parameters) {
        return isConfiguredFilteredAttribute(filteredAttributeIndex) && areParametersConfigured(parameters);
    }

    public static boolean isConfiguredFilteredAttribute(Integer filteredAttributeIndex) {
        return filteredAttributeIndex != null;
    }

    public static boolean areParametersConfigured(Map<String, Parameter> parameters) {
        return parameters.values().stream().noneMatch((p) -> (p == null));
    }

    public ComparableInstances filter(ComparableInstances comparableInstances, Integer filteredAttributeIndex, Map<String, Parameter> parameters);

    @Override
    public abstract String toString();

    public abstract Map<String, Parameter> getParameters();

}
