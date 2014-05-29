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

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public final class ScatterPlot implements Serializable {

    private Instances instances;
    private final int indiceAtributoX;
    private final int indiceAtributoY;
    private final int indiceAtributoColor;
    private transient Vista minaVista;
    private final ChartPanel meuChartPanel;

    public int getIndiceAtributoColor() {
        return indiceAtributoColor;
    }

    public void setMinaVista(Vista minaVista) {
        this.minaVista = minaVista;
    }

    public Vista getMinaVista() {
        return minaVista;
    }

    public void setInstances(Instances instances) {
        this.instances = instances;
    }

    public Instances getInstances() {
        return instances;
    }

    public ScatterPlot(final Instances instances, final int indiceAtributoX, final int indiceAtributoY, int indiceAtributoColor, Vista vista, boolean celdaMatriz) {
        this.indiceAtributoX = indiceAtributoX;
        this.indiceAtributoColor = indiceAtributoColor;
        this.minaVista = vista;
        this.indiceAtributoY = indiceAtributoY;
        this.instances = instances;
        JFreeChart jfreechart = createChart(new XYDatasetModelo(instances, indiceAtributoX, indiceAtributoY, indiceAtributoColor));
        if (celdaMatriz) {
            jfreechart.setTitle((String) null);
        }
        if (jfreechart.getXYPlot().getSeriesCount() == 1) {
            jfreechart.removeLegend();
        }
        ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());
        ChartUtilities.applyCurrentTheme(jfreechart);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        chartpanel.setPreferredSize(new Dimension(340, 210));
        chartpanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(final ChartMouseEvent event) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        double domainCrosshairValue = meuChartPanel.getChart().getXYPlot().getDomainCrosshairValue();
                        double rangeCrosshairValue = meuChartPanel.getChart().getXYPlot().getRangeCrosshairValue();
                        ArrayList<Integer> indicesInstancesPuntos = new ArrayList<>();
                        Enumeration e = instances.enumerateInstances();
                        for (int i = 0; e.hasMoreElements(); i++) {
                            Instance di = (Instance) e.nextElement();
                            if (di.value(indiceAtributoX) == domainCrosshairValue && di.value(indiceAtributoY) == rangeCrosshairValue) {
                                indicesInstancesPuntos.add(i);
                            }
                        }
                        if (event.getTrigger().getClickCount() == 1) {
                            minaVista.getjPopupMenu1().removeAll();
                            minaVista.getjPopupMenu1().setLayout(new BoxLayout(minaVista.getjPopupMenu1(), BoxLayout.X_AXIS));
                            for (int i = 0; i < indicesInstancesPuntos.size(); i++) {
                                if (i != 0) {
                                    minaVista.getjPopupMenu1().add(new JSeparator(SwingConstants.VERTICAL));
                                }
                                int p = indicesInstancesPuntos.get(i);
                                JPanel pa = new JPanel();
                                pa.setBorder(new LineBorder(obterCoresHSB(i, indicesInstancesPuntos.size()), 2));
                                pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
                                Enumeration en = instances.enumerateAttributes();
                                while (en.hasMoreElements()) {
                                    Attribute a = (Attribute) en.nextElement();
                                    pa.add(new JLabel(a.name() + ": " + (a.type() == Attribute.NUMERIC ? instances.instance(p).value(a) : instances.instance(p).stringValue(a))));
                                }
                                minaVista.getjPopupMenu1().add(pa);
                            }
                            minaVista.getjPopupMenu1().show(event.getTrigger().getComponent(), event.getTrigger().getX(), event.getTrigger().getY());
                            minaVista.procesarSeleccion(indicesInstancesPuntos);
                        }
                    }
                }
                );
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
            }
        });
        this.meuChartPanel = chartpanel;
    }

    private JFreeChart createChart(XYDataset xydataset) {
        JFreeChart jfreechart = ChartFactory.createScatterPlot("'" + instances.attribute(indiceAtributoX).name() + "' " + minaVista.getBundle().getString("fronteA") + " '" + instances.attribute(indiceAtributoY).name() + "'", instances.attribute(indiceAtributoX).name(), instances.attribute(indiceAtributoY).name(), xydataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setDomainCrosshairLockedOnData(true);
        xyplot.setRangeCrosshairVisible(true);
        xyplot.setRangeCrosshairLockedOnData(true);
        xyplot.setDomainZeroBaselineVisible(true);
        xyplot.setRangeZeroBaselineVisible(true);
        xyplot.setDomainPannable(true);
        xyplot.setRangePannable(true);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        return jfreechart;
    }

    public int getIndiceAtributoX() {
        return indiceAtributoX;
    }

    public int getIndiceAtributoY() {
        return indiceAtributoY;
    }

    public ChartPanel getMeuChartPanel() {
        return meuChartPanel;
    }

    private XYDataset dataset;

    public void setDataset(XYDataset dataset) {
        this.dataset = dataset;
    }

    public XYDataset getDataset() {
        return dataset;
    }

    public static Color obterCoresHSB(int cor, int totalCores) {
        return Color.getHSBColor((float) cor / totalCores, (float) 0.9, (float) 0.9);
    }

}
