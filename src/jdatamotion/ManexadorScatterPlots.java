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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import static jdatamotion.Vista.bundle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Drawable;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

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
    private final int numSeries;
    private final NodeList<InstancesSimultaneas> eixoTemporal;
    private final InstancesComparable instances;
    private final int paso;
    private int t;
    private Nodo<InstancesSimultaneas> nodoActual;
    private int msInstances[];
    private final int lonxitudeEstela;

    public int getTInicial() {
        if (eixoTemporal.isEmpty()) {
            return 0;
        }
        return eixoTemporal.get(0).getObject().getMs();
    }

    public int getTActual() {
        return t;
    }

    public int getTFinal() {
        if (eixoTemporal.isEmpty()) {
            return 0;
        }
        return eixoTemporal.getLast().getObject().getMs();
    }

    public int getNumSeries() {
        return numSeries;
    }

    public int getNumItems() {
        return instances.numInstances();
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

    public int[] getMsInstances() {
        return msInstances;
    }

    public synchronized void goTo(int toMs) {
        goTo(1.0 * toMs / getTFinal());
    }

    public synchronized void goTo(double to) {
        int toMs = (int) Math.round(to * (getTFinal() - getTInicial()) + getTInicial());
        Nodo<InstancesSimultaneas> nodo = nodoActual;
        int numeroItems = 0;
        if (toMs > t) {
            numeroItems = numeroItemsAVisualizar(toMs);
            for (int j = matrizScatterPlots.size() - 1; j >= 0; j--) {
                for (int k = 0; k < matrizScatterPlots.get(j).size(); k++) {
                    if (scatterPlotsVisibles[j][k]) {
                        ScatterPlot sp = matrizScatterPlots.get(j).get(k);
                        XYDatasetModelo xyd = (XYDatasetModelo) sp.getChartPanelCela().getChart().getXYPlot().getDataset();
                        nodo = xyd.visualizarItems(sp, numeroItems);
                    }
                }
            }
        } else if (toMs < t) {
            numeroItems = -numeroItemsAAgochar(toMs);
            for (int j = matrizScatterPlots.size() - 1; j >= 0; j--) {
                for (int k = 0; k < matrizScatterPlots.get(j).size(); k++) {
                    if (scatterPlotsVisibles[j][k]) {
                        ScatterPlot sp = matrizScatterPlots.get(j).get(k);
                        XYDatasetModelo xyd = (XYDatasetModelo) sp.getChartPanelCela().getChart().getXYPlot().getDataset();
                        nodo = xyd.agocharItems(sp, -numeroItems);
                    }
                }
            }
        }
        textField.setText(String.valueOf(Integer.parseInt(textField.getText()) + numeroItems));
        slider.setValue((int) Math.round(to * slider.getMaximum()));
        nodoActual = nodo;
        t = (int) Math.round(getTInicial() + to * (getTFinal() - getTInicial()));
    }

    private int numeroItemsAVisualizar(int toMs) {
        int n = 0;
        Nodo<InstancesSimultaneas> nodo = nodoActual.getNext();
        while (nodo != null && nodo.getObject().getMs() <= toMs) {
            n += nodo.getObject().size();
            nodo = nodo.getNext();
        }
        return n;
    }

    private int numeroItemsAAgochar(int toMs) {
        int n = 0;
        Nodo<InstancesSimultaneas> nodo = nodoActual;
        while (nodo != null && nodo.getObject().getMs() > toMs) {
            n += nodo.getObject().size();
            nodo = nodo.getBack();
        }
        return n;
    }

    public void freeze() {
        tarefaPlay.freeze();
    }

    void goToNext() {
        goTo(nodoActual.getNext().getObject().getMs());
    }

    void goToBefore() {
        goTo(nodoActual.getBack().getObject().getMs());
    }

    class Nodo<E> {

        private Nodo<E> next;
        private Nodo<E> back;
        private final E object;

        public Nodo<E> getNext() {
            return next;
        }

        public Nodo<E> getBack() {
            return back;
        }

        public E getObject() {
            return object;
        }

        public Nodo(E object) {
            this.object = object;
        }
    }

    private class NodeList<E> extends ArrayList<Nodo<E>> {

        private Nodo<E> first;
        private Nodo<E> last;

        public Nodo<E> getFirst() {
            return first;
        }

        public Nodo<E> getLast() {
            return last;
        }

        public NodeList() {
            super();
        }

        public void addElement(E e) {
            add(new Nodo<>(e));
        }

        @Override
        public boolean add(Nodo<E> e) {
            if (isEmpty()) {
                e.back = null;
                first = e;
            } else {
                last.next = e;
                e.back = last;
            }
            e.next = null;
            last = e;
            return super.add(e);
        }
    }

    class InstancesSimultaneas extends ArrayList<Instance> {

        private Integer ms;

        public Integer getMs() {
            return this.ms;
        }

        public InstancesSimultaneas(Integer ms) {
            super();
            this.ms = ms;
        }

        public void setMs(Integer ms) {
            this.ms = ms;
        }
    }

    private NodeList<InstancesSimultaneas> fabricarEixo(InstancesComparable instances, int indiceTemporal, int ordeVisualizacion, int lonxitudeEstela) {
        NodeList<InstancesSimultaneas> eixo = new NodeList<>();
        msInstances = new int[instances.numInstances()];
        Instances ins = instances;
        Double ultimoIndice;
        switch (ordeVisualizacion) {
            case Vista.ORDE_MODELO:
                for (int i = 0; i < ins.numInstances(); i++) {
                    Instance in = (Instance) ins.instance(i);
                    eixo.addElement(new InstancesSimultaneas((i + 1) * paso));
                    msInstances[i] = (i + 1) * paso;
                    eixo.get(eixo.size() - 1).getObject().add(in);
                }
                break;
            case Vista.ORDE_INDICE_TEMPORAL_NUMERICO:
                ArrayList<Attribute> atributos = new ArrayList<>();
                atributos.add(new Attribute("indiceInstancesOrixinal"));
                atributos.add(new Attribute("indiceTemporal"));
                Instances insts = new Instances("", atributos, ins.numInstances());
                for (int i = 0; i < ins.numInstances(); i++) {
                    insts.add(new DenseInstance(1.0, new double[]{i, ins.get(i).value(indiceTemporal)}));
                }
                insts.sort(1);
                ultimoIndice = null;
                int j = 0;
                for (int i = 0; i < insts.numInstances(); i++) {
                    Instance in = (Instance) insts.instance(i);
                    if (!Objects.equals(in.value(1), ultimoIndice)) {
                        j++;
                        eixo.addElement(new InstancesSimultaneas(j * paso));
                        ultimoIndice = in.value(1);
                    }
                    eixo.get(eixo.size() - 1).getObject().add(instances.instance((int) in.value(0)));
                    msInstances[(int) in.value(0)] = j * paso;
                }
                break;
            case Vista.ORDE_INDICE_TEMPORAL_NUMERICO_PONDERADO:
                atributos = new ArrayList<>();
                atributos.add(new Attribute("indiceInstancesOrixinal"));
                atributos.add(new Attribute("indiceTemporal"));
                insts = new Instances("", atributos, ins.numInstances());
                double max = 0.0;
                for (int i = 0; i < ins.numInstances(); i++) {
                    insts.add(new DenseInstance(1.0, new double[]{i, ins.get(i).value(indiceTemporal)}));
                    int indiceTemporalMs = Math.round((float) ins.get(i).value(indiceTemporal) * paso);
                    if (indiceTemporalMs > max) {
                        max = indiceTemporalMs;
                    }
                }
                insts.sort(1);
                ultimoIndice = null;
                for (int i = 0; i < insts.numInstances(); i++) {
                    Instance in = (Instance) insts.instance(i);
                    msInstances[(int) in.value(0)] = (int) (!in.isMissing(1) ? Math.round((float) in.value(1) * paso) : max + paso);
                    if (!Objects.equals(in.value(1), ultimoIndice)) {
                        int ms = msInstances[(int) in.value(0)];
                        eixo.addElement(new InstancesSimultaneas(ms != 0 ? ms : 0));
                        ultimoIndice = in.value(1);
                    }
                    eixo.get(eixo.size() - 1).getObject().add(instances.instance((int) in.value(0)));
                }
                break;
        }
        return eixo;
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
        @SuppressWarnings("SleepWhileInLoop")
        protected Void doInBackground() {
            try {
                while (estado == PLAY && getTActual() < getTFinal()) {
                    try {
                        Thread.sleep(paso);
                    } catch (InterruptedException ex) {
                    }
                    synchronized (ManexadorScatterPlots.this) {
                        if (estado == PLAY && getTActual() < getTFinal()) {
                            int items = numeroItemsAVisualizar(t + paso);
                            Nodo<InstancesSimultaneas> nodo = nodoActual;
                            for (int j = matrizScatterPlots.size() - 1; j >= 0; j--) {
                                for (int k = 0; k < matrizScatterPlots.get(j).size(); k++) {
                                    if (scatterPlotsVisibles[j][k]) {
                                        ScatterPlot sp = matrizScatterPlots.get(j).get(k);
                                        XYDatasetModelo xyd = (XYDatasetModelo) sp.getChartPanelCela().getChart().getXYPlot().getDataset();
                                        nodo = xyd.visualizarItems(sp, items);
                                    }
                                }
                            }
                            nodoActual = nodo;
                            t = Math.min(t + paso, getTFinal());
                            slider.setValue(slider.getMaximum() * (getTActual() - getTInicial()) / (getTFinal() - getTInicial()));
                            textField.setText(String.valueOf(Integer.parseInt(textField.getText()) + items));
                        }
                    }
                }
                if (getTActual() < getTFinal()) {
                    setEstado(FREEZE);
                } else {
                    setEstado(PAUSE);
                }
            } catch (NumberFormatException e) {
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

    public ManexadorScatterPlots(InstancesComparable instances, int atributoColor, int indiceTemporal, boolean[][] scatterPlotsVisibles, JSlider slider, JTextField textField, int ordeVisualizacion, int paso, int lonxitudeEstela) {
        int numAtributosNumericos = scatterPlotsVisibles.length;
        this.matrizScatterPlots = new ArrayList<>(numAtributosNumericos);
        this.instances = instances;
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
        this.tarefaPlay.setEstado(PAUSE);
        this.slider = slider;
        this.lonxitudeEstela = lonxitudeEstela;
        this.paso = paso;
        this.textField = textField;
        this.eixoTemporal = fabricarEixo(instances, indiceTemporal, ordeVisualizacion, lonxitudeEstela);
        this.t = getTInicial();
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

        public int obterItemsTotais() {
            int r = 0;
            for (int j = 0; j < getSeriesCount(); j++) {
                r += getSeries(j).getItemCount();
            }
            return r;
        }

        public XYDatasetModelo(InstancesComparable atributos, int atributoX, int atributoY, int atributoColor) {
            super();
            this.atributos = atributos;
            this.atributoX = atributoX;
            this.atributoY = atributoY;
            this.atributoColor = atributoColor;
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
            Nodo<InstancesSimultaneas> nodo = eixoTemporal.getFirst();
            while (nodo != null) {
                for (int i = 0; i < nodo.getObject().size(); i++) {
                    Double d4 = nodo.getObject().get(i).value(atributoX);
                    if (d4 < d) {
                        d = d4;
                    }
                    if (d4 > d1) {
                        d1 = d4;
                    }
                    Double d5 = nodo.getObject().get(i).value(atributoY);
                    if (d5 < d2) {
                        d2 = d5;
                    }
                    if (d5 > d3) {
                        d3 = d5;
                    }
                    getSeries((Comparable) (atributoColor > -1 && Double.compare(nodo.getObject().get(i).value(atributoColor), Double.NaN) != 0 ? (int) nodo.getObject().get(i).value(atributoColor) : -1)).add(d4, d5, false);
                }
                nodo = nodo.getNext();
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

        public synchronized Nodo<InstancesSimultaneas> visualizarItems(ScatterPlot sp, int numeroItems) {
            sp.eliminarAnotacions(XYAnnotation.class);
            int e = numeroItems;
            Nodo<InstancesSimultaneas> nodo = nodoActual;
            while (e > 0) {
                nodo = nodo.getNext();
                for (int i = 0; i < nodo.getObject().size(); i++) {
                    XYSeries xys = getSeries((Comparable) (atributoColor > -1 && Double.compare(nodo.getObject().get(i).value(atributoColor), Double.NaN) != 0 ? (int) nodo.getObject().get(i).value(atributoColor) : -1));
                    xys.add(nodo.getObject().get(i).value(atributoX), nodo.getObject().get(i).value(atributoY), false);
                    e--;
                }
            }
            sp.pintarEstela(nodo, lonxitudeEstela);
            fireDatasetChanged();
            return nodo;
        }

        public synchronized Nodo<InstancesSimultaneas> agocharItems(ScatterPlot sp, int numeroItems) {
            setNotify(false);
            sp.eliminarAnotacions(XYAnnotation.class);
            int e = numeroItems;
            Nodo<InstancesSimultaneas> nodo = nodoActual;
            while (e > 0) {
                for (int i = 0; i < nodo.getObject().size(); i++) {
                    XYSeries xys = getSeries((Comparable) (atributoColor > -1 && Double.compare(nodo.getObject().get(i).value(atributoColor), Double.NaN) != 0 ? (int) nodo.getObject().get(i).value(atributoColor) : -1));
                    xys.remove(xys.getItemCount() - 1);
                    e--;
                }
                nodo = nodo.getBack();
            }
            sp.pintarEstela(nodo, lonxitudeEstela);
            setNotify(true);
            return nodo;
        }
    }

    public void procesarSeleccion(InstancesComparable instances, ArrayList<Integer> indicesInstances) {
        double radioInicial = 15.0;
        double grosorInicial = 1.0;
        double pasoGrosor = 0.0;
        double pasoRadio = 1;
        matrizScatterPlots.stream().forEach((matrizScatterPlot) -> {
            matrizScatterPlot.stream().filter((matrizScatterPlot1) -> (matrizScatterPlot1 != null)).map((matrizScatterPlot1) -> matrizScatterPlot1).map((sp) -> {
                sp.eliminarAnotacions(XYDrawableAnnotation.class);
                return sp;
            }).map((sp) -> {
                sp.getChartPanelCela().getChart().getXYPlot().setNotify(false);
                return sp;
            }).map((sp) -> {
                for (int k = 0; k < indicesInstances.size(); k++) {
                    if (msInstances[indicesInstances.get(k)] <= getTActual()) {
                        sp.engadirAnotacion(new XYDrawableAnnotation(instances.get(indicesInstances.get(k)).value(sp.getIndiceAtributoX()), instances.get(indicesInstances.get(k)).value(sp.getIndiceAtributoY()), radioInicial + k * pasoRadio, radioInicial + k * pasoRadio, new CircleDrawer(obterCoresHSB(k, indicesInstances.size()), new BasicStroke((float) (grosorInicial + k * pasoGrosor)), null)));
                    }
                }
                return sp;
            }).forEach((sp) -> {
                sp.getChartPanelCela().getChart().getXYPlot().setNotify(true);
            });
        });
    }

    public int numColumnasNonVacias() {
        int n = 0;
        for (int i = 0; i < matrizScatterPlots.size(); i++) {
            if (!columnaScatterPlotsVacia(i)) {
                n++;
            }
        }
        return n;
    }

    public int numFilasNonVacias() {
        int n = 0;
        for (int i = 0; i < matrizScatterPlots.size(); i++) {
            if (!filaScatterPlotsVacia(i)) {
                n++;
            }
        }
        return n;
    }

    public boolean columnaScatterPlotsVacia(int columna) {
        for (boolean[] bb : scatterPlotsVisibles) {
            if (bb[columna]) {
                return false;
            }
        }
        return true;
    }

    public boolean filaScatterPlotsVacia(int fila) {
        for (boolean b : scatterPlotsVisibles[fila]) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    public ScatterPlot getScatterPlot(int i, int j) {
        return matrizScatterPlots.get(i).get(j);
    }

    public void cubrirConScatterPlot(int i, int j, List<Integer> indices, int indiceAtributoNominal) {
        if (scatterPlotsVisibles[i][j]) {
            matrizScatterPlots.get(i).set(j, new ScatterPlot(instances, indices.get(j), indices.get(i), indiceAtributoNominal));
        }
        t = getTFinal();
        nodoActual = eixoTemporal.getLast();
        slider.setValue(100);
    }

    public static Color obterCoresHSB(int cor, int totalCores) {
        return Color.getHSBColor((float) cor / totalCores, (float) 0.9, (float) 0.9);
    }

    public static Color obterCorIntermedia(int cor, int totalCores, Color cor1, Color cor2) {
        int r1 = cor1.getRed(), g1 = cor1.getGreen(), b1 = cor1.getBlue(), r2 = cor2.getRed(), g2 = cor2.getGreen(), b2 = cor2.getBlue();
        return new Color((int) Math.round(1.0 * r1 + (r2 - r1) * cor / totalCores), (int) Math.round(1.0 * g1 + (g2 - g1) * cor / totalCores), (int) Math.round(1.0 * b1 + (b2 - b1) * cor / totalCores));
    }

    public int contarJFramesVisibles() {
        int c = 0;
        for (ArrayList<ScatterPlot> alsp : matrizScatterPlots) {
            c = alsp.stream().filter((jf) -> (jf != null && jf.getjFrameAmpliado().isVisible())).map((_item) -> 1).reduce(c, Integer::sum);
        }
        return c;
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

        private class ChartPanelParcheado extends ChartPanel {

            private int intentos;
            private final int maxIntentos = 5;

            public ChartPanelParcheado(JFreeChart chart) {
                super(chart);
            }

            private void tryToPaintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                } catch (ConcurrentModificationException e) {
                    if (intentos < maxIntentos) {
                        intentos++;
                        tryToPaintComponent(g);
                    }
                }
            }

            @Override
            public void paintComponent(Graphics g) {
                intentos = 0;
                tryToPaintComponent(g);
            }
        }

        public void pintarEstela() {
            pintarEstela(nodoActual, lonxitudeEstela);
        }

        private synchronized void pintarEstela(Nodo<InstancesSimultaneas> nodo, int lonxitudeEstela) {
            Color corEstela = Color.white, corBackgroundChartPanel = (Color) chartPanelCela.getChart().getPlot().getBackgroundPaint(), corBackgroundJFrameAmpliar = (Color) jFrameAmpliado.getChartPanel().getChart().getPlot().getBackgroundPaint();
            Nodo<InstancesSimultaneas> nAnterior, nActual = nodo;
            for (int i = 0; i < lonxitudeEstela; i++) {
                if (nActual != null) {
                    nAnterior = nActual.getBack();
                    if (nAnterior == null) {
                        break;
                    }
                    for (int j = 0; j < nActual.getObject().size(); j++) {
                        double xAct = nActual.getObject().get(j).value(indiceAtributoX), yAct = nActual.getObject().get(j).value(indiceAtributoY);
                        for (int k = 0; k < nAnterior.getObject().size(); k++) {
                            double xAnt = nAnterior.getObject().get(k).value(indiceAtributoX), yAnt = nAnterior.getObject().get(k).value(indiceAtributoY);
                            chartPanelCela.getChart().getXYPlot().addAnnotation(new XYLineAnnotation(xAnt, yAnt, xAct, yAct, new BasicStroke(2.0f), obterCorIntermedia(i, lonxitudeEstela, corEstela, corBackgroundChartPanel)), false);
                            jFrameAmpliado.getChartPanel().getChart().getXYPlot().addAnnotation(new XYLineAnnotation(xAnt, yAnt, xAct, yAct, new BasicStroke(2.0f), obterCorIntermedia(i, lonxitudeEstela, corEstela, corBackgroundJFrameAmpliar)), false);
                        }
                    }
                    nActual = nAnterior;
                }
            }
        }

        public ScatterPlot(final InstancesComparable instances, final int indiceAtributoX, final int indiceAtributoY, int indiceAtributoColor) {
            this.indiceAtributoX = indiceAtributoX;
            this.indiceAtributoColor = indiceAtributoColor;
            this.indiceAtributoY = indiceAtributoY;
            XYDatasetModelo xydm = new XYDatasetModelo(instances, indiceAtributoX, indiceAtributoY, indiceAtributoColor);
            JFreeChart jfreechart1 = createChart(instances, xydm), jfreechart2 = createChart(instances, xydm);
            if (indiceAtributoColor < 0) {
                jfreechart1.removeLegend();
                jfreechart2.removeLegend();
            }
            ChartTheme ct = StandardChartTheme.createDarknessTheme();
            ct.apply(jfreechart1);
            ct.apply(jfreechart2);
            this.chartPanelCela = new ChartPanelParcheado(jfreechart1);
            ChartPanel chartPanelAmpliado = new ChartPanelParcheado(jfreechart2);
            this.chartPanelCela.getChart().setTitle((String) null);
            this.jFrameAmpliado = new JFrameChartPanel("'" + instances.attribute(indiceAtributoY).name() + "' " + bundle.getString("fronteA") + " '" + instances.attribute(indiceAtributoX).name() + "'", chartPanelAmpliado, indiceAtributoX, indiceAtributoY);
        }

        public void eliminarAnotacions(Class claseAnotacion) {
            XYPlot xyp1 = chartPanelCela.getChart().getXYPlot(), xyp2 = jFrameAmpliado.getChartPanel().getChart().getXYPlot();
            List annotations = xyp1.getAnnotations();
            Iterator it = annotations.iterator();
            while (it.hasNext()) {
                XYAnnotation a = (XYAnnotation) it.next();
                if (claseAnotacion.isInstance(a)) {
                    xyp1.removeAnnotation(a, false);
                    xyp2.removeAnnotation(a, false);
                }
            }
        }

        public void engadirAnotacion(XYAnnotation a) {
            chartPanelCela.getChart().getXYPlot().addAnnotation(a, false);
            jFrameAmpliado.getChartPanel().getChart().getXYPlot().addAnnotation(a, false);
        }

        private JFreeChart createChart(InstancesComparable instances, XYDatasetModelo xydataset) {
            JFreeChart jfreechart = ChartFactory.createScatterPlot("'" + instances.attribute(indiceAtributoX).name() + "' " + Vista.getBundle().getString("fronteA") + " '" + instances.attribute(indiceAtributoY).name() + "'", instances.attribute(indiceAtributoX).name(), instances.attribute(indiceAtributoY).name(), xydataset, PlotOrientation.VERTICAL, true, false, false);
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
