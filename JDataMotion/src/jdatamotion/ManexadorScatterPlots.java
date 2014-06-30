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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import jdatamotion.Vista.ScatterPlot;

/**
 *
 * @author usuario
 */
public class ManexadorScatterPlots {

    private final ExecutorService cachedPool;

    public void pecharJFramesChartPanel() {
        matrizScatterPlots.stream().filter((alsp) -> (alsp != null)).forEach((alsp) -> {
            alsp.stream().filter((sp) -> (sp != null)).forEach((sp) -> {
                sp.getjFrameAmpliado().dispose();
            });
        });
    }

    class TarefaPlay extends SwingWorker<Void, Void> {

        private final ArrayList<ArrayList<ScatterPlot>> alalsp;
        private final boolean[][] scatterPlotsVisibles;

        public TarefaPlay(ArrayList<ArrayList<Vista.ScatterPlot>> alsp,boolean[][] scatterPlotsVisibles) {
            super();
            this.alalsp = alsp;
            this.scatterPlotsVisibles=scatterPlotsVisibles;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Vista.XYDatasetModelo d = (Vista.XYDatasetModelo) alalsp.get(0).get(0).getChartPanelCela().getChart().getXYPlot().getDataset();
            int i, numI = d.getSeriesCount();
            for (i = 0; i < numI; i++) {
                while (!d.todoVisualizado()) {
                    synchronized (this) {
                        for (int j = alalsp.size() - 1; j >= 0; j--) {
                            for (int k = 0; k < alalsp.get(j).size(); k++) {
                                if (scatterPlotsVisibles[j][k]) {
                                    ((Vista.XYDatasetModelo) alalsp.get(j).get(k).getChartPanelCela().getChart().getXYPlot().getDataset()).visualizarItems(1);
                                }
                            }
                        }
                        visualizados++;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

    }

    public void play() {
        cachedPool.submit(tarefaPlay);
    }

    private final TarefaPlay tarefaPlay;
    private final transient ArrayList<ArrayList<Vista.ScatterPlot>> matrizScatterPlots;
    private int visualizados;

    public int getVisualizados() {
        return visualizados;
    }

    public ManexadorScatterPlots(int numAtributosNumericos,boolean[][] scatterPlotsVisibles) {
        cachedPool = Executors.newCachedThreadPool();
        matrizScatterPlots = new ArrayList<>(numAtributosNumericos);
        for (int in = 0; in < numAtributosNumericos; in++) {
            ArrayList<Vista.ScatterPlot> alsp = new ArrayList<>();
            for (int in2 = 0; in2 < numAtributosNumericos; in2++) {
                alsp.add(null);
            }
            matrizScatterPlots.add(alsp);
        }
        tarefaPlay = new TarefaPlay(matrizScatterPlots,scatterPlotsVisibles);
        visualizados = 0;
    }

    public ArrayList<ArrayList<Vista.ScatterPlot>> getMatrizScatterPlots() {
        return matrizScatterPlots;
    }
}
