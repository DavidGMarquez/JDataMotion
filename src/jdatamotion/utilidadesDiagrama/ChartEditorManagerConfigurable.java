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
package jdatamotion.utilidadesDiagrama;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.ChartEditor;
import org.jfree.chart.editor.ChartEditorFactory;
import org.jfree.chart.util.ParamChecks;

/**
 *
 * @author usuario
 */
public class ChartEditorManagerConfigurable {

    /**
     * This factory creates new {@link ChartEditor} instances as required.
     */
    static ChartEditorFactory factory = new DefaultChartEditorFactoryConfigurable();

    /**
     * Private constructor prevents instantiation.
     */
    private ChartEditorManagerConfigurable() {
        // nothing to do
    }

    /**
     * Returns the current factory.
     *
     * @return The current factory (never <code>null</code>).
     */
    public static ChartEditorFactory getChartEditorFactory() {
        return factory;
    }

    /**
     * Sets the chart editor factory.
     *
     * @param f the new factory (<code>null</code> not permitted).
     */
    public static void setChartEditorFactory(ChartEditorFactory f) {
        ParamChecks.nullNotPermitted(f, "f");
        factory = f;
    }

    /**
     * Returns a component that can be used to edit the given chart.
     *
     * @param chart the chart.
     *
     * @return The chart editor.
     */
    public static ChartEditor getChartEditor(JFreeChart chart) {
        return factory.createEditor(chart);
    }
}
