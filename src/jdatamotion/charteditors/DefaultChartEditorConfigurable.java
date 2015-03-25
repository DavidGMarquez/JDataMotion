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
package jdatamotion.charteditors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import jdatamotion.Vista;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.ChartEditor;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.title.Title;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.layout.LCBLayout;
import org.jfree.ui.PaintSample;

/**
 *
 * @author usuario
 */
public class DefaultChartEditorConfigurable extends JPanel implements ActionListener {

    /**
     * A panel for displaying/editing the properties of the title.
     */
    private DefaultTitleEditorConfigurable titleEditor;

    /**
     * A panel for displaying/editing the properties of the plot.
     */
    private DefaultPlotEditorConfigurable plotEditor;

    /**
     * A checkbox indicating whether or not the chart is drawn with
     * anti-aliasing.
     */
    private JCheckBox antialias;

    /**
     * The chart background color.
     */
    private PaintSample background;

    /**
     * The resourceBundle for the localization.
     */
    protected static ResourceBundle localizationResources
            // = ResourceBundleWrapper.getBundle(
            //      "org.jfree.chart.editor.LocalizationBundle");
            = Vista.bundle;

    /**
     * Standard constructor - the property panel is made up of a number of
     * sub-panels that are displayed in the tabbed pane.
     *
     * @param chart the chart, whichs properties should be changed.
     */
    public DefaultChartEditorConfigurable() {
        setLayout(new BorderLayout());

        JPanel other = new JPanel(new BorderLayout());
        other.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel general = new JPanel(new BorderLayout());
        general.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                localizationResources.getString("General")));

        JPanel interior = new JPanel(new LCBLayout(6));
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        this.antialias = new JCheckBox(localizationResources.getString(
                "Draw_anti-aliased"));
        this.antialias.setSelected(Vista.GraphicConfigurationManager.readBooleanProperty("anti-aliased"));
        interior.add(this.antialias);
        interior.add(new JLabel(""));
        interior.add(new JLabel(""));
        interior.add(new JLabel(localizationResources.getString(
                "Background_paint")));
        this.background = new PaintSample(Vista.GraphicConfigurationManager.readColorProperty("chart_background_paint"));
        interior.add(this.background);
        JButton button = new JButton(localizationResources.getString(
                "Select..."));
        button.setActionCommand("BackgroundPaint");
        button.addActionListener(this);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Series_Paint")));
        JTextField info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info);
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Series_Stroke")));
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info);
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Paint")));
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info);
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Stroke")));
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info);
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button);

        general.add(interior, BorderLayout.NORTH);
        other.add(general, BorderLayout.NORTH);

        JPanel parts = new JPanel(new BorderLayout());

//        Title title = chart.getTitle();
//        Plot plot = chart.getPlot();
        JTabbedPane tabs = new JTabbedPane();

//        this.titleEditor = new DefaultTitleEditorConfigurable(title);
        this.titleEditor = new DefaultTitleEditorConfigurable();
        this.titleEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Title"), this.titleEditor);

//        if (plot instanceof PolarPlot) {
//            this.plotEditor = new DefaultPolarPlotEditorConfigurable((PolarPlot) plot);
//        } else {
//            this.plotEditor = new DefaultPlotEditorConfigurable(plot);
//        }
        this.plotEditor = new DefaultPolarPlotEditorConfigurable();
        this.plotEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Plot"), this.plotEditor);

        tabs.add(localizationResources.getString("Other"), other);
        parts.add(tabs, BorderLayout.NORTH);
        add(parts);
    }

    /**
     * Returns a reference to the title editor.
     *
     * @return A panel for editing the title.
     */
    public DefaultTitleEditorConfigurable getTitleEditor() {
        return this.titleEditor;
    }

    /**
     * Returns a reference to the plot property sub-panel.
     *
     * @return A panel for editing the plot properties.
     */
    public DefaultPlotEditorConfigurable getPlotEditor() {
        return this.plotEditor;
    }

    /**
     * Returns the current setting of the anti-alias flag.
     *
     * @return <code>true</code> if anti-aliasing is enabled.
     */
    public boolean getAntiAlias() {
        return this.antialias.isSelected();
    }

    /**
     * Returns the current background paint.
     *
     * @return The current background paint.
     */
    public Paint getBackgroundPaint() {
        return this.background.getPaint();
    }

    /**
     * Handles user interactions with the panel.
     *
     * @param event a BackgroundPaint action.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("BackgroundPaint")) {
            attemptModifyBackgroundPaint();
        }
    }

    /**
     * Allows the user the opportunity to select a new background paint. Uses
     * JColorChooser, so we are only allowing a subset of all Paint objects to
     * be selected (fix later).
     */
    private void attemptModifyBackgroundPaint() {
        Color c;
        c = JColorChooser.showDialog(this, localizationResources.getString(
                "Background_Color"), Color.blue);
        if (c != null) {
            this.background.setPaint(c);
        }
    }

}
