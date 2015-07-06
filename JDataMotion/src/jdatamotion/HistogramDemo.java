/*
 * The MIT License
 *
 * Copyright 2015 usuario.
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

import java.awt.Dimension;
import java.io.IOException;
import java.util.Random;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class HistogramDemo extends ApplicationFrame {

    public HistogramDemo(String s) {
        super(s);
        JPanel jpanel = createDemoPanel();
        jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
    }

    private static IntervalXYDataset createDataset() {
        HistogramDataset histogramdataset = new HistogramDataset();
        double ad[] = new double[1000];
        Random random = new Random(0xbc614eL);
        for (int i = 0; i < 1000; i++) {
            ad[i] = random.nextGaussian() + 5D;
        }

        histogramdataset.addSeries("H1", ad, 100, 2D, 8D);
        ad = new double[1000];
        for (int j = 0; j < 1000; j++) {
            ad[j] = random.nextGaussian() + 7D;
        }

        histogramdataset.addSeries("H2", ad, 100, 4D, 10D);
        return histogramdataset;
    }

    private static JFreeChart createChart(IntervalXYDataset intervalxydataset) {
        JFreeChart jfreechart = ChartFactory.createHistogram("Histogram Demo 1", null, null, intervalxydataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setForegroundAlpha(0.85F);
        XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
        xybarrenderer.setDrawBarOutline(false);
        return jfreechart;
    }

    public static JPanel createDemoPanel() {
        JFreeChart jfreechart = createChart(createDataset());
        return new ChartPanel(jfreechart);
    }

    public static void main(String args[])
            throws IOException {
        HistogramDemo histogramdemo1 = new HistogramDemo("JFreeChart : HistogramDemo1");
        histogramdemo1.pack();
        RefineryUtilities.centerFrameOnScreen(histogramdemo1);
        histogramdemo1.setVisible(true);
    }
}
