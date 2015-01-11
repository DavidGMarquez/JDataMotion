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
import jdatamotion.InstancesComparable;

/**
 *
 * @author usuario
 */
public abstract class AbstractFilter implements Serializable {

    private Integer indiceAtributoFiltrado;
    private HashMap<String, Parameter> parametersMap;

    public AbstractFilter(Integer indiceAtributoFiltrado, Parameter[] parameters) {
        parametersMap = new HashMap<>();
        this.indiceAtributoFiltrado = indiceAtributoFiltrado;
        setParameters(parameters);
    }

    public boolean estaConfiguradoIndiceAtributoFiltrado() {
        return indiceAtributoFiltrado != null;
    }

    public boolean estanConfiguradosParametros() {
        return parametersMap.values().stream().noneMatch((p) -> (p.getValue() == null));
    }

    public boolean estaTodoConfigurado() {
        return estaConfiguradoIndiceAtributoFiltrado() && estanConfiguradosParametros();
    }

    public AbstractFilter(InstancesComparable instancias) {
        this(null, null);
    }

    public AbstractFilter(Parameter[] parameters) {
        this(null, parameters);
    }

    public final void setIndiceAtributoFiltrado(Integer indiceAtributoFiltrado) {
        this.indiceAtributoFiltrado = indiceAtributoFiltrado;
    }

    public final void setParameters(Parameter[] parameters) {
        if (parameters != null) {
            for (Parameter p : parameters) {
                this.parametersMap.put(p.getName(), p);
            }
        }
    }

    public final Integer getIndiceAtributoFiltrado() {
        return indiceAtributoFiltrado;
    }

    public final Parameter[] getParameters() {
        return parametersMap.values().toArray(new Parameter[0]);
    }

    public final Parameter getParameter(String parameterName) {
        return parametersMap.get(parameterName);
    }

    public abstract InstancesComparable filter(InstancesComparable instancesComparable);

    @Override
    public abstract String toString();
}
