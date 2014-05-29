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
package jdatamotion;

import java.util.ArrayList;
import java.util.Enumeration;
import org.jfree.data.DomainInfo;
import org.jfree.data.Range;
import org.jfree.data.RangeInfo;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;
import weka.core.Instances;

/**
 *
 * @author usuario
 */
public class XYDatasetModelo extends AbstractXYDataset implements XYDataset,
        DomainInfo, RangeInfo {

    private static final long serialVersionUID = 1L;
    private final Double xValues[][];
    private final Double yValues[][];
    private final int seriesCount;
    private final int itemCount;
    private Number domainMin;
    private Number domainMax;
    private Number rangeMin;
    private Number rangeMax;
    private Range domainRange;
    private Range range;
    private final Instances atributos;
    private final int atributoColor;
    private final int atributoY;
    private final int atributoX;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double[][] getxValues() {
        return xValues;
    }

    public Double[][] getyValues() {
        return yValues;
    }

    public int getItemCount() {
        return itemCount;
    }

    public Number getDomainMin() {
        return domainMin;
    }

    public Number getDomainMax() {
        return domainMax;
    }

    public Number getRangeMin() {
        return rangeMin;
    }

    public Number getRangeMax() {
        return rangeMax;
    }

    public Range getRange() {
        return range;
    }

    public Instances getAtributos() {
        return atributos;
    }

    public XYDatasetModelo(Instances atributos, int atributoX, int atributoY, int atributoColor) {
        this.atributos = atributos;
        this.atributoX = atributoX;
        this.atributoY = atributoY;
        this.atributoColor = atributoColor;
        int numeroInstancias = atributos.numInstances();
        double d = (1.0D / 0.0D);
        double d1 = (-1.0D / 0.0D);
        double d2 = (1.0D / 0.0D);
        double d3 = (-1.0D / 0.0D);
        if (atributoColor > -1) {
            Enumeration en = atributos.attribute(atributoColor).enumerateValues();
            int numeroValoresAtributoNominal = 0;
            while (en.hasMoreElements()) {
                en.nextElement();
                numeroValoresAtributoNominal++;
            }
            ArrayList<ArrayList<Double>> xv = new ArrayList<>();
            ArrayList<ArrayList<Double>> yv = new ArrayList<>();
            for (int i = 0; i < numeroValoresAtributoNominal; i++) {
                xv.add(new ArrayList<Double>());
                yv.add(new ArrayList<Double>());
            }
            for (int l = 0; l < numeroInstancias; l++) {
                xv.get((int) atributos.instance(l).value(atributoColor)).add(atributos.instance(l).value(atributoX));
                yv.get((int) atributos.instance(l).value(atributoColor)).add(atributos.instance(l).value(atributoY));
            }
            int maxInstanciasNominal = 0;
            for (int i = 0; i < numeroValoresAtributoNominal; i++) {
                int size = xv.get(i).size();
                if (size > maxInstanciasNominal) {
                    maxInstanciasNominal = size;
                }
            }
            seriesCount = numeroValoresAtributoNominal;
            itemCount = maxInstanciasNominal;
            xValues = new Double[numeroValoresAtributoNominal][maxInstanciasNominal];
            yValues = new Double[numeroValoresAtributoNominal][maxInstanciasNominal];
            for (int i = 0; i < numeroValoresAtributoNominal; i++) {
                for (int j = 0; j < xv.get(i).size(); j++) {
                    Double d4 = xv.get(i).get(j);
                    if (d4 < d) {
                        d = d4;
                    }
                    if (d4 > d1) {
                        d1 = d4;
                    }
                    xValues[i][j] = d4;
                    Double d5 = yv.get(i).get(j);
                    if (d5 < d2) {
                        d2 = d5;
                    }
                    if (d5 > d3) {
                        d3 = d5;
                    }
                    yValues[i][j] = d5;
                }
            }
        } else {
            seriesCount = 1;
            itemCount = numeroInstancias;
            xValues = new Double[1][numeroInstancias];
            yValues = new Double[1][numeroInstancias];
            for (int l = 0; l < numeroInstancias; l++) {
                double d4 = atributos.instance(l).value(atributoX);
                xValues[0][l] = d4;
                if (d4 < d) {
                    d = d4;
                }
                if (d4 > d1) {
                    d1 = d4;
                }
                double d5 = atributos.instance(l).value(atributoY);
                yValues[0][l] = d5;
                if (d5 < d2) {
                    d2 = d5;
                }
                if (d5 > d3) {
                    d3 = d5;
                }
            }
        }
        try {
            domainMin = d;
            domainMax = d1;
            domainRange = new Range(d, d1);
            rangeMin = d2;
            rangeMax = d3;
            range = new Range(d2, d3);
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public Number getX(int i, int j) {
        return xValues[i][j];
    }

    @Override
    public Number getY(int i, int j) {
        return yValues[i][j];
    }

    @Override
    public int getSeriesCount() {
        return seriesCount;
    }

    @Override
    public Comparable<String> getSeriesKey(int i) {
        return atributoColor > -1 ? atributos.attribute(atributoColor).value(i) : "";
    }

    @Override
    public int getItemCount(int i) {
        return itemCount;
    }

    public double getDomainLowerBound() {
        return domainMin.doubleValue();
    }

    @Override
    public double getDomainLowerBound(boolean flag) {
        return domainMin.doubleValue();
    }

    public double getDomainUpperBound() {
        return domainMax.doubleValue();
    }

    @Override
    public double getDomainUpperBound(boolean flag) {
        return domainMax.doubleValue();
    }

    public Range getDomainBounds() {
        return domainRange;
    }

    @Override
    public Range getDomainBounds(boolean flag) {
        return domainRange;
    }

    public Range getDomainRange() {
        return domainRange;
    }

    public double getRangeLowerBound() {
        return rangeMin.doubleValue();
    }

    @Override
    public double getRangeLowerBound(boolean flag) {
        return rangeMin.doubleValue();
    }

    public double getRangeUpperBound() {
        return rangeMax.doubleValue();
    }

    @Override
    public double getRangeUpperBound(boolean flag) {
        return rangeMax.doubleValue();
    }

    @Override
    public Range getRangeBounds(boolean flag) {
        return range;
    }

    public Range getValueRange() {
        return range;
    }

    public Number getMinimumDomainValue() {
        return domainMin;
    }

    public Number getMaximumDomainValue() {
        return domainMax;
    }

    public Number getMinimumRangeValue() {
        return domainMin;
    }

    public Number getMaximumRangeValue() {
        return domainMax;
    }
}
