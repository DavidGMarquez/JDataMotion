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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import jdatamotion.Vista.ScatterPlot;
import jdatamotion.Vista.XYDatasetModelo;

/**
 *
 * @author usuario
 */
public final class ManexadorScatterPlots {

    private final JSlider slider;
    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    private final boolean[][] scatterPlotsVisibles;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private final PropertyChangeListener propertyChangeListener;

    public int getEstado() {
        if (tarefaPlay == null) {
            return PAUSE;
        } else {
            return tarefaPlay.getEstado();
        }
    }

    public void pecharJFramesChartPanel() {
        matrizScatterPlots.stream().filter((alsp) -> (alsp != null)).forEach((alsp) -> {
            alsp.stream().filter((sp) -> (sp != null)).forEach((sp) -> {
                sp.getjFrameAmpliado().dispose();
            });
        });
    }

    final class TarefaPlay extends SwingWorker<Void, Void> {

        private final ArrayList<ArrayList<ScatterPlot>> alalsp;
        private final boolean[][] scatterPlotsVisibles;
        private int estado;

        public TarefaPlay(ArrayList<ArrayList<ScatterPlot>> alsp, boolean[][] scatterPlotsVisibles) {
            super();
            this.alalsp = alsp;
            this.scatterPlotsVisibles = scatterPlotsVisibles;
            setEstado(PAUSE);
        }

        public void setEstado(int estado) {
            firePropertyChange("estadoReproductor", this.estado, estado);
            this.estado = estado;
        }

        @Override
        protected Void doInBackground() throws Exception {
            XYDatasetModelo d = (XYDatasetModelo) alalsp.get(0).get(0).getChartPanelCela().getChart().getXYPlot().getDataset();
            int numI = d.getSeriesCount();
            for (int i = 0; i < numI; i++) {
                while (!d.todoVisualizado() && estado == PLAY) {
                    synchronized (this) {
                        for (int j = alalsp.size() - 1; j >= 0; j--) {
                            for (int k = 0; k < alalsp.get(j).size(); k++) {
                                if (scatterPlotsVisibles[j][k]) {
                                    ((XYDatasetModelo) alalsp.get(j).get(k).getChartPanelCela().getChart().getXYPlot().getDataset()).visualizarItems(1);
                                }
                            }
                        }
                        visualizados++;
                        slider.setValue((int) 100.0 * visualizados / d.getItemCount());
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                }
            }
            return null;
        }

        private void pause() {
            setEstado(PAUSE);
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
        this.tarefaPlay = new TarefaPlay(matrizScatterPlots, scatterPlotsVisibles);
        this.tarefaPlay.addPropertyChangeListener(propertyChangeListener);
        this.tarefaPlay.play();
    }

    private TarefaPlay tarefaPlay;
    private final transient ArrayList<ArrayList<ScatterPlot>> matrizScatterPlots;
    private int visualizados;

    public int getVisualizados() {
        return visualizados;
    }

    public ManexadorScatterPlots(int numAtributosNumericos, boolean[][] scatterPlotsVisibles, JSlider slider, PropertyChangeListener propertyChangeListener) {
        this.matrizScatterPlots = new ArrayList<>(numAtributosNumericos);
        this.propertyChangeListener = propertyChangeListener;
        this.scatterPlotsVisibles = scatterPlotsVisibles;
        for (int in = 0; in < numAtributosNumericos; in++) {
            ArrayList<ScatterPlot> alsp = new ArrayList<>();
            for (int in2 = 0; in2 < numAtributosNumericos; in2++) {
                alsp.add(null);
            }
            this.matrizScatterPlots.add(alsp);
        }
        this.tarefaPlay = null;
        this.visualizados = 0;
        this.slider = slider;
    }

    public ArrayList<ArrayList<ScatterPlot>> getMatrizScatterPlots() {
        return matrizScatterPlots;
    }
}
