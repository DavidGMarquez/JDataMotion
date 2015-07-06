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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import jdatamotioncommon.ComparableInstances;
import jdatamotioncommon.filtros.IFilter;
import jdatamotioncommon.filtros.Parameter;

/**
 *
 * @author usuario
 */
public class FilterHandler implements Serializable {

    private Integer indiceAtributoFiltrado;
    private boolean seleccionado;
    private final IFilter filtro;
    private Map<String, Parameter> parameters = new HashMap<>();

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public FilterHandler(Integer indiceAtributoFiltrado, IFilter filtro) {
        this.indiceAtributoFiltrado = indiceAtributoFiltrado;
        this.seleccionado = false;
        this.filtro = filtro;
        this.parameters = filtro.getParametersNeeded();
    }

    public final void setIndiceAtributoFiltrado(Integer indiceAtributoFiltrado) {
        this.indiceAtributoFiltrado = indiceAtributoFiltrado;
    }

    public Map<String, Parameter> getParameters() {
        return this.parameters;
    }

    public final Integer getIndiceAtributoFiltrado() {
        return indiceAtributoFiltrado;
    }

    public final Parameter getParameter(String parameterName) {
        Iterator<Entry<String, Parameter>> it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Parameter> pair = it.next();
            if (pair.getKey().equals(parameterName)) {
                return pair.getValue();
            }
        }
        return null;
    }

    public ComparableInstances filtrar(ComparableInstances instancesComparable) {
        return filtro.filter(new ComparableInstances(instancesComparable), indiceAtributoFiltrado, parameters);
    }

    @Override
    public String toString() {
        return filtro.getName();
    }

    public final void setParameters(Map<String, Parameter> parametros) {
        this.parameters = parametros;
    }

    public boolean estaTodoConfigurado() {
        return IFilter.isEverythingConfigured(indiceAtributoFiltrado, parameters);
    }

    public IFilter getFiltro() {
        return this.filtro;
    }
}
