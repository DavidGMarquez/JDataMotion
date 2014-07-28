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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import jdatamotion.Modelo.InstancesComparable;
import jdatamotion.Vista.TarefaProgreso;
import static jdatamotion.Vista.bundle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Drawable;
import weka.core.Attribute;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public final class ManexadorScatterPlots {

    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    public static final int FREEZE = 2;
    private final JSlider slider;
    private final JTextField textField;
    private final boolean[][] scatterPlotsVisibles;
    private final ArrayList<PropertyChangeListener> propertyChangeListeners;
    private TarefaPlay tarefaPlay;
    private final transient ArrayList<ArrayList<ScatterPlot>> matrizScatterPlots;
    private int visualizados;
    private final int numSeries;
    private final int numItems;

    public int getNumSeries() {
        return numSeries;
    }

    public int getNumItems() {
        return numItems;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeListeners.add(propertyChangeListener);
    }

    public int getEstado() {
        return tarefaPlay.getEstado();
    }

    public void pecharJFramesChartPanel() {
        matrizScatterPlots.stream().filter((alsp) -> (alsp != null)).forEach((alsp) -> {
            alsp.stream().filter((sp) -> (sp != null)).forEach((sp) -> {
                sp.getjFrameAmpliado().dispose();
            });
        });
    }

    public boolean nadaVisualizado() {
        return visualizados == 0;
    }

    public boolean todoVisualizado() {
        return visualizados >= numItems;
    }

    public void goTo(int to) {
        if (to > visualizados) {
            for (int j = matrizScatterPlots.size() - 1; j >= 0; j--) {
                for (int k = 0; k < matrizScatterPlots.get(j).size(); k++) {
                    if (scatterPlotsVisibles[j][k]) {
                        ((XYDatasetModelo) matrizScatterPlots.get(j).get(k).getChartPanelCela().getChart().getXYPlot().getDataset()).visualizarItems(to - visualizados);
                    }
                }
            }
        } else if (to < visualizados) {
            for (int j = matrizScatterPlots.size() - 1; j >= 0; j--) {
                for (int k = 0; k < matrizScatterPlots.get(j).size(); k++) {
                    if (scatterPlotsVisibles[j][k]) {
                        ((XYDatasetModelo) matrizScatterPlots.get(j).get(k).getChartPanelCela().getChart().getXYPlot().getDataset()).agocharItems(visualizados - to);
                    }
                }
            }
        }
        visualizados = to;
        slider.setValue((int) 1.0 * slider.getMaximum() * to / numItems);
        textField.setText(String.valueOf(to));
        textField.setBackground(Color.white);
    }

    public void freeze() {
        tarefaPlay.freeze();
    }

    final class TarefaPlay extends SwingWorker<Void, Void> {

        private int estado;

        public TarefaPlay(ArrayList<ArrayList<ScatterPlot>> alsp, boolean[][] scatterPlotsVisibles) {
            super();
            setEstado(PAUSE);
        }

        public void setEstado(int estado) {
            firePropertyChange("estadoReproductor", this.estado, estado);
            this.estado = estado;
        }

        @Override
        protected Void doInBackground() {
            try {
                while (!todoVisualizado() && estado == PLAY) {
                    for (int j = matrizScatterPlots.size() - 1; j >= 0; j--) {
                        for (int k = 0; k < matrizScatterPlots.get(j).size(); k++) {
                            if (scatterPlotsVisibles[j][k]) {
                                ((XYDatasetModelo) matrizScatterPlots.get(j).get(k).getChartPanelCela().getChart().getXYPlot().getDataset()).visualizarItems(1);
                            }
                        }
                    }
                    visualizados++;
                    slider.setValue((int) 1.0 * slider.getMaximum() * visualizados / numItems);
                    textField.setText(String.valueOf(visualizados));
                    textField.setBackground(Color.white);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                }
                if (!todoVisualizado()) {
                    setEstado(FREEZE);
                } else {
                    setEstado(PAUSE);
                }
            } catch (Exception e) {
                Logger.getLogger(ManexadorScatterPlots.class.getName()).log(Level.SEVERE, null, e);
                throw e;
            }
            return null;
        }

        public void pause() {
            setEstado(PAUSE);
        }

        public void freeze() {
            setEstado(FREEZE);
        }

        public int getEstado() {
            return estado;
        }

        public void play() {
            setEstado(PLAY);
            execute();
        }
    }

    public void pause() {
        tarefaPlay.pause();
    }

    public void play() {
        tarefaPlay = new TarefaPlay(matrizScatterPlots, scatterPlotsVisibles);
        propertyChangeListeners.stream().forEach((p) -> {
            tarefaPlay.addPropertyChangeListener(p);
        });
        tarefaPlay.play();
    }

    public int getVisualizados() {
        return visualizados;
    }

    public ManexadorScatterPlots(InstancesComparable instances, int atributoColor, boolean[][] scatterPlotsVisibles, JSlider slider, JTextField textField) {
        int numAtributosNumericos = scatterPlotsVisibles.length;
        this.matrizScatterPlots = new ArrayList<>(numAtributosNumericos);
        this.numItems = instances.numInstances();
        if (atributoColor == -1) {
            this.numSeries = 1;
        } else {
            this.numSeries = instances.attribute(atributoColor).numValues();
        }
        this.scatterPlotsVisibles = scatterPlotsVisibles;
        for (int in = 0; in < numAtributosNumericos; in++) {
            ArrayList<ScatterPlot> alsp = new ArrayList<>();
            for (int in2 = 0; in2 < numAtributosNumericos; in2++) {
                alsp.add(null);
            }
            this.matrizScatterPlots.add(alsp);
        }
        this.propertyChangeListeners = new ArrayList<>();
        this.tarefaPlay = new TarefaPlay(matrizScatterPlots, scatterPlotsVisibles);
        tarefaPlay.setEstado(PAUSE);
        this.visualizados = 0;
        this.slider = slider;
        this.textField = textField;
    }

    public ArrayList<ArrayList<ScatterPlot>> getMatrizScatterPlots() {
        return matrizScatterPlots;

    }

    public class JFrameChartPanel extends JFrame {

        private final ChartPanel chartPanel;

        public ChartPanel getChartPanel() {
            return chartPanel;
        }

        public JFrameChartPanel(String title, ChartPanel chartPanel, int indiceI, int indiceJ) {
            super();
            this.chartPanel = chartPanel;
            setIconImage(new ImageIcon(getClass().getResource("imaxes/faviconGrafica.png")).getImage());
            setContentPane(chartPanel);
            setSize(600, 400);
        }
    }

    private class PuntoConNominal {

        private final int atributoNominal;
        private final double valorX;
        private final double valorY;

        public int getAtributoNominal() {
            return atributoNominal;
        }

        public double getValorX() {
            return valorX;
        }

        public double getValorY() {
            return valorY;
        }

        public PuntoConNominal(int atributoNominal, double valorX, double valorY) {
            this.atributoNominal = atributoNominal;
            this.valorX = valorX;
            this.valorY = valorY;
        }
    }

    class XYDatasetModelo extends XYSeriesCollection {

        private Number domainMin;
        private Number domainMax;
        private Number rangeMin;
        private Number rangeMax;
        private Range domainRange;
        private Range range;
        private final int atributoY;
        private final int atributoX;
        private final int atributoColor;
        private final PuntoConNominal puntos[];
        private final InstancesComparable atributos;

        public int getAtributoY() {
            return atributoY;
        }

        public int getAtributoX() {
            return atributoX;
        }

        public int getAtributoColor() {
            return atributoColor;
        }

        public PuntoConNominal[] getPuntos() {
            return puntos;
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

        @Override
        public Comparable<String> getSeriesKey(int i) {
            if (atributoColor < 0) {
                return "";
            }
            return i > 0 ? atributos.attribute(atributoColor).value(i - 1) : bundle.getString("senDefinir");
        }

        public Number getRangeMax() {
            return rangeMax;
        }

        public Range getRange() {
            return range;
        }

        public XYDatasetModelo(InstancesComparable atributos, int atributoX, int atributoY, int atributoColor) {
            super();
            this.atributos = atributos;
            this.atributoX = atributoX;
            this.atributoY = atributoY;
            this.atributoColor = atributoColor;
            int numeroInstancias = atributos.numInstances();
            double d = (1.0D / 0.0D);
            double d1 = (-1.0D / 0.0D);
            double d2 = (1.0D / 0.0D);
            double d3 = (-1.0D / 0.0D);
            addSeries(new XYSeries(-1, false));
            if (atributoColor > -1) {
                for (int i = 0; i < atributos.attribute(atributoColor).numValues(); i++) {
                    addSeries(new XYSeries(i, false));
                }
            }
            puntos = new PuntoConNominal[numeroInstancias];
            for (int j = 0; j < numeroInstancias; j++) {
                Double d4 = atributos.instance(j).value(atributoX);
                if (d4 < d) {
                    d = d4;
                }
                if (d4 > d1) {
                    d1 = d4;
                }
                Double d5 = atributos.instance(j).value(atributoY);
                if (d5 < d2) {
                    d2 = d5;
                }
                if (d5 > d3) {
                    d3 = d5;
                }
                puntos[j] = new PuntoConNominal(atributoColor > -1 && Double.compare(atributos.instance(j).value(atributoColor), Double.NaN) != 0 ? (int) atributos.instance(j).value(atributoColor) : -1, d4, d5);
                getSeries((Comparable) puntos[j].getAtributoNominal()).add(puntos[j].getValorX(), puntos[j].getValorY(), false);
                visualizados = numItems;
            }
            try {
                domainMin = d;
                domainMax = d1;
                domainRange = new Range(d, d1);
                rangeMin = d2;
                rangeMax = d3;
                range = new Range(d2, d3);
                fireDatasetChanged();
            } catch (IllegalArgumentException e) {
            }
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

        public double getRangeUpperBound() {
            return rangeMax.doubleValue();
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

        public synchronized void visualizarItems(int i) {
            for (int a = 0; a < i; a++) {
                PuntoConNominal p = puntos[visualizados + a];
                getSeries((Comparable) p.getAtributoNominal()).add(p.getValorX(), p.getValorY(), false);
            }
            fireDatasetChanged();
        }

        public synchronized void agocharItems(int i) {
            setNotify(false);
            for (int a = 0; a < i; a++) {
                PuntoConNominal xv = puntos[visualizados - a - 1];
                XYSeries xys = getSeries((Comparable) xv.getAtributoNominal());
                xys.remove(xys.getItemCount() - 1);
            }
            setNotify(true);
        }

    }

    public void anularSeleccions(XYPlot xyPlot) {
        List annotations = xyPlot.getAnnotations();
        for (Iterator it = annotations.iterator(); it.hasNext();) {
            XYAnnotation annotation = (XYAnnotation) it.next();
            xyPlot.removeAnnotation(annotation);
        }
    }

    public void procesarSeleccion(InstancesComparable instances, ArrayList<Integer> indicesInstances) {
        double radioInicial = 15.0;
        double grosorInicial = 1.0;
        double pasoGrosor = 0.0;
        double pasoRadio = 1;
        for (int i = 0; i < getMatrizScatterPlots().size(); i++) {
            for (int j = 0; j < getMatrizScatterPlots().get(i).size(); j++) {
                if (getMatrizScatterPlots().get(i).get(j) != null) {
                    ScatterPlot sp = getMatrizScatterPlots().get(i).get(j);
                    anularSeleccions(sp.getjFrameAmpliado().getChartPanel().getChart().getXYPlot());
                    anularSeleccions(sp.getChartPanelCela().getChart().getXYPlot());
                    for (int k = 0; k < indicesInstances.size(); k++) {
                        if (getVisualizados() > indicesInstances.get(k)) {
                            Color color = obterCoresHSB(k, indicesInstances.size());
                            sp.getChartPanelCela().getChart().getXYPlot().addAnnotation(new XYDrawableAnnotation(instances.get(indicesInstances.get(k)).value(sp.getIndiceAtributoX()), instances.get(indicesInstances.get(k)).value(sp.getIndiceAtributoY()), radioInicial + k * pasoRadio, radioInicial + k * pasoRadio, new CircleDrawer(color, new BasicStroke((float) (grosorInicial + k * pasoGrosor)), null)));
                            sp.getjFrameAmpliado().getChartPanel().getChart().getXYPlot().addAnnotation(new XYDrawableAnnotation(instances.get(indicesInstances.get(k)).value(sp.getIndiceAtributoX()), instances.get(indicesInstances.get(k)).value(sp.getIndiceAtributoY()), radioInicial + k * pasoRadio, radioInicial + k * pasoRadio, new CircleDrawer(color, new BasicStroke((float) (grosorInicial + k * pasoGrosor)), null)));
                        }
                    }
                }
            }
        }
    }

    private static Color obterCoresHSB(int cor, int totalCores) {
        return Color.getHSBColor((float) cor / totalCores, (float) 0.9, (float) 0.9);
    }

    public ScatterPlot scatterPlotFactory(final InstancesComparable instances, final int indiceAtributoX, final int indiceAtributoY, int indiceAtributoColor, TarefaProgreso task, Vista vista) {
        return new ScatterPlot(instances, indiceAtributoX, indiceAtributoY, indiceAtributoColor, task, vista);

    }

    public class ScatterPlot implements Serializable {

        private final int indiceAtributoX;
        private final int indiceAtributoY;
        private final int indiceAtributoColor;
        private ChartPanel chartPanelCela;
        private final JFrameChartPanel jFrameAmpliado;

        public void setChartPanelCela(ChartPanel meuChartPanel) {
            this.chartPanelCela = meuChartPanel;
        }

        public JFrameChartPanel getjFrameAmpliado() {
            return jFrameAmpliado;
        }

        public int getIndiceAtributoColor() {
            return indiceAtributoColor;
        }

        public ScatterPlot(final InstancesComparable instances, final int indiceAtributoX, final int indiceAtributoY, int indiceAtributoColor, TarefaProgreso task, Vista vista) {
            this.indiceAtributoX = indiceAtributoX;
            this.indiceAtributoColor = indiceAtributoColor;
            this.indiceAtributoY = indiceAtributoY;
            XYDatasetModelo xydm = new XYDatasetModelo(instances, indiceAtributoX, indiceAtributoY, indiceAtributoColor);
            JFreeChart jfreechart1 = createChart(instances, xydm, task);
            JFreeChart jfreechart2 = createChart(instances, xydm, task);
            if (indiceAtributoColor < 0) {
                jfreechart1.removeLegend();
                jfreechart2.removeLegend();
            }
            ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());
            ChartUtilities.applyCurrentTheme(jfreechart1);
            ChartUtilities.applyCurrentTheme(jfreechart2);
            this.chartPanelCela = new ChartPanel(jfreechart1);
            task.acumulate(35);
            ChartPanel chartPanelAmpliado = new ChartPanel(jfreechart2);
            task.acumulate(35);
            this.chartPanelCela.getChart().setTitle((String) null);
            configurarChartPanel(this.chartPanelCela, instances, vista);
            configurarChartPanel(chartPanelAmpliado, instances, vista);
            this.jFrameAmpliado = new JFrameChartPanel("'" + instances.attribute(indiceAtributoY).name() + "' " + bundle.getString("fronteA") + " '" + instances.attribute(indiceAtributoX).name() + "'", chartPanelAmpliado, indiceAtributoX, indiceAtributoY);
            anularSeleccions(this.chartPanelCela.getChart().getXYPlot());
            anularSeleccions(this.jFrameAmpliado.getChartPanel().getChart().getXYPlot());
        }

        private void configurarChartPanel(ChartPanel chartpanel, InstancesComparable instances, Vista vista) {
            chartpanel.setMouseWheelEnabled(true);
            chartpanel.setPreferredSize(new Dimension(340, 210));
            JPopupMenu jpopup = vista.getjPopupMenu1();
            chartpanel.addChartMouseListener(new ChartMouseListener() {
                @Override
                public void chartMouseClicked(final ChartMouseEvent event) {
                    java.awt.EventQueue.invokeLater(() -> {
                        if (SwingUtilities.isLeftMouseButton(event.getTrigger()) && event.getTrigger().getClickCount() == 1) {
                            XYPlot xyp = event.getChart().getXYPlot();
                            double domainCrosshairValue = xyp.getDomainCrosshairValue();
                            double rangeCrosshairValue = xyp.getRangeCrosshairValue();
                            ArrayList<Integer> indicesInstancesPuntos = new ArrayList<>();
                            Enumeration e = instances.enumerateInstances();
                            for (int i = 0; e.hasMoreElements(); i++) {
                                Instance di = (Instance) e.nextElement();
                                if (di.value(indiceAtributoX) == domainCrosshairValue && di.value(indiceAtributoY) == rangeCrosshairValue) {
                                    indicesInstancesPuntos.add(i);
                                }
                            }
                            if (!indicesInstancesPuntos.isEmpty()) {
                                jpopup.removeAll();
                                jpopup.setLayout(new BoxLayout(jpopup, BoxLayout.X_AXIS));
                                for (int i = 0; i < indicesInstancesPuntos.size(); i++) {
                                    if (getVisualizados() > indicesInstancesPuntos.get(i)) {
                                        if (i != 0) {
                                            jpopup.add(new JSeparator(SwingConstants.VERTICAL));
                                        }
                                        int p = indicesInstancesPuntos.get(i);
                                        JPanel pa = new JPanel();
                                        pa.setBorder(new LineBorder(obterCoresHSB(i, indicesInstancesPuntos.size()), 2));
                                        pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
                                        Enumeration en = instances.enumerateAttributes();
                                        while (en.hasMoreElements()) {
                                            Attribute a = (Attribute) en.nextElement();
                                            boolean represented = a.index() == indiceAtributoX || a.index() == indiceAtributoY;
                                            pa.add(new JLabel((represented ? "<html><strong>" : "") + a.name() + ": " + (a.type() == Attribute.NUMERIC ? Double.compare(instances.instance(p).value(a), Double.NaN) != 0 ? instances.instance(p).value(a) : "?" : instances.instance(p).stringValue(a)) + (represented ? "</strong></html>" : "")));
                                        }
                                        jpopup.add(pa);
                                    }
                                }
                                jpopup.show(event.getTrigger().getComponent(), event.getTrigger().getX(), event.getTrigger().getY());
                                procesarSeleccion(instances, indicesInstancesPuntos);

                            }
                        }
                    });
                }

                @Override
                public void chartMouseMoved(ChartMouseEvent event) {
                }
            });
            chartpanel.addMouseWheelListener((MouseWheelEvent e) -> {
                jpopup.setVisible(false);
            });
            chartpanel.addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    jpopup.setVisible(false);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    jpopup.setVisible(false);
                }
            });
        }

        private JFreeChart createChart(InstancesComparable instances, XYDataset xydataset, TarefaProgreso task) {
            JFreeChart jfreechart = ChartFactory.createScatterPlot("'" + instances.attribute(indiceAtributoX).name() + "' " + Vista.getBundle().getString("fronteA") + " '" + instances.attribute(indiceAtributoY).name() + "'", instances.attribute(indiceAtributoX).name(), instances.attribute(indiceAtributoY).name(), xydataset, PlotOrientation.VERTICAL, true, true, false);
            task.acumulate(15);
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

        public ChartPanel getChartPanelCela() {
            return chartPanelCela;
        }
    }

    class CircleDrawer implements Drawable {

        private final Paint outlinePaint;
        private final Stroke outlineStroke;
        private final Paint fillPaint;

        public CircleDrawer(Paint paint, Stroke stroke, Paint paint1) {
            outlinePaint = paint;
            outlineStroke = stroke;
            fillPaint = paint1;
        }

        @Override
        public void draw(Graphics2D graphics2d, Rectangle2D rectangle2d) {
            java.awt.geom.Ellipse2D.Double double1 = new java.awt.geom.Ellipse2D.Double(
                    rectangle2d.getX(), rectangle2d.getY(), rectangle2d.getWidth(),
                    rectangle2d.getHeight());
            if (fillPaint != null) {
                graphics2d.setPaint(fillPaint);
                graphics2d.fill(double1);
            }
            if (outlinePaint != null && outlineStroke != null) {
                graphics2d.setPaint(outlinePaint);
                graphics2d.setStroke(outlineStroke);
                graphics2d.draw(double1);
            }
            graphics2d.setPaint(Color.black);
            graphics2d.setStroke(new BasicStroke(1.0F));
            java.awt.geom.Line2D.Double double2 = new java.awt.geom.Line2D.Double(
                    rectangle2d.getCenterX(), rectangle2d.getMinY(),
                    rectangle2d.getCenterX(), rectangle2d.getMaxY());
            java.awt.geom.Line2D.Double double3 = new java.awt.geom.Line2D.Double(
                    rectangle2d.getMinX(), rectangle2d.getCenterY(),
                    rectangle2d.getMaxX(), rectangle2d.getCenterY());
            graphics2d.draw(double2);
            graphics2d.draw(double3);
        }
    }

}
