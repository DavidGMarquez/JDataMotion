/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdatamotioncommon;

import java.util.Iterator;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author usuario
 */
public class Utils {

    public static Object obterDato(ComparableInstances instancesComparable, int fila, int columna) {
        Object valor = null;
        switch (instancesComparable.attribute(columna).type()) {
            case Attribute.DATE:
            case Attribute.RELATIONAL:
            case Attribute.STRING:
                valor = instancesComparable.get(fila).attribute(columna).value((int) instancesComparable.get(fila).value(columna));
                break;
            case Attribute.NOMINAL:
                valor = (Double.isNaN(instancesComparable.get(fila).value(columna)) ? "" : instancesComparable.get(fila).attribute(columna).value((int) instancesComparable.get(fila).value(columna)));
                break;
            case Attribute.NUMERIC:
                valor = instancesComparable.get(fila).value(columna);
                break;
        }
        return valor;
    }

    public static int getNumeroObservaciones(ComparableInstances instancesComparable, int indiceAtributo) {
        int r = 0;
        Iterator<Instance> it = instancesComparable.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            if (!instance.isMissing(indiceAtributo)) {
                r++;
            }
        }
        return r;
    }

    public static Double getValorPercentil(ComparableInstances instancesComparable, int percentil, int indiceAtributo) {
        if (percentil > 100) {
            return instancesComparable.get(instancesComparable.numInstances() - 1).value(indiceAtributo);
        }
        Instances ins = new ComparableInstances(instancesComparable);
        Iterator<Instance> it = ins.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            if (instance.isMissing(indiceAtributo)) {
                it.remove();
            }
        }
        if (ins.isEmpty()) {
            return null;
        }
        ins.sort(indiceAtributo);
        Double p, x, d;
        int e;
        x = 1.0 * ins.numInstances() * percentil / 100;
        d = x % 1;
        e = (int) Math.round(x - d);
        p = d != 0.0 ? ins.instance(e).value(indiceAtributo) : (ins.instance(e - 1).value(indiceAtributo) + ins.instance(e).value(indiceAtributo)) / 2;
        return p;
    }

    public static Double getMedia(ComparableInstances instancesComparable, int indiceAtributo) {
        int numInstancesNonNaN = 0;
        Double dato,
                media = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) Utils.obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                numInstancesNonNaN++;
                if (media == null) {
                    media = dato;
                } else {
                    media += dato;
                }
            }
        }
        if (media != null) {
            media /= numInstancesNonNaN;
        }
        return media;
    }

    public static Double getMinimo(ComparableInstances instancesComparable, int indiceAtributo) {
        Double dato,
                min = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) Utils.obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                if (min == null) {
                    min = dato;
                } else if (dato < min) {
                    min = dato;
                }
            }
        }
        return min;
    }

    public static Double getVarianza(ComparableInstances instancesComparable, int indiceAtributo) {
        int numInstancesNonNaN = 0;
        Double dato,
                varianza = null,
                media = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) Utils.obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                numInstancesNonNaN++;
                if (media == null) {
                    media = dato;
                } else {
                    media += dato;
                }
            }
        }
        if (media != null) {
            media /= numInstancesNonNaN;
            numInstancesNonNaN = 0;
            varianza = 0.0;
            for (int i = 0; i < instancesComparable.numInstances(); i++) {
                dato = (Double) Utils.obterDato(instancesComparable, i, indiceAtributo);
                if (Double.compare(dato, Double.NaN) != 0) {
                    numInstancesNonNaN++;
                    varianza += Math.pow(dato - media, 2.0);
                }
            }
            varianza /= (numInstancesNonNaN - 1);
        }
        return varianza;
    }

    public static Double getDesviacionTipica(ComparableInstances comparableInstances, int indiceAtributo) {
        Double varianza = getVarianza(comparableInstances, indiceAtributo);
        return varianza != null ? Math.sqrt(varianza) : null;
    }

    public static Double getMaximo(ComparableInstances instancesComparable, int indiceAtributo) {
        Double dato, max = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                if (max == null) {
                    max = dato;
                } else if (dato > max) {
                    max = dato;
                }
            }
        }
        return max;
    }
}
