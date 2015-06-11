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
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import jdatamotion.Vista;
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
        interior.add(new JLabel());
        button = new JButton(localizationResources.getString("Edit..."));
        button.setActionCommand("SeriesPaint");
        button.addActionListener(this);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Series_Stroke")));
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(true);
        interior.add(new JLabel());
        button = new JButton(localizationResources.getString("Edit..."));
        button.setActionCommand("SeriesStroke");
        button.addActionListener(this);
        button.setEnabled(true);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Paint")));
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(true);
        interior.add(new JLabel());
        button = new JButton(localizationResources.getString("Edit..."));
        button.setActionCommand("SeriesOutlinePaint");
        button.addActionListener(this);
        button.setEnabled(true);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Stroke")));
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(true);
        interior.add(new JLabel());
        button = new JButton(localizationResources.getString("Edit..."));
        button.setActionCommand("SeriesOutlineStroke");
        button.addActionListener(this);
        button.setEnabled(true);
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
        switch (command) {
            case "BackgroundPaint":
                attemptModifyBackgroundPaint();
                break;
            case "SeriesPaint":
                attemptModifySeriesPaint();
                break;
            case "SeriesStroke":
                //attemptModifyBackgroundPaint();
                break;
            case "SeriesOutlinePaint":
                //attemptModifyBackgroundPaint();
                break;
            case "SeriesOutlineStroke":
                //attemptModifyBackgroundPaint();
                break;
        }
    }

    /**
     * Allows the user the opportunity to select a new background paint. Uses
     * JColorChooser, so we are only allowing a subset of all Paint objects to
     * be selected (fix later).
     */
    private void attemptModifyBackgroundPaint() {
        Color c;
        c = JColorChooser.showDialog(this, localizationResources.getString("Background_Color"), Vista.GraphicConfigurationManager.readColorProperty("chart_background_paint"));
        if (c != null) {
            this.background.setPaint(c);
        }
    }

    class MuestraColor extends PaintSample {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(20, 20);
        }

        public MuestraColor(Paint paint) {
            super(paint);
        }
    }

    private void attemptModifySeriesPaint() {
        JPanel c = new JPanel();
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JScrollPane js = new JScrollPane(p);
        js.setMaximumSize(new Dimension(400, 200));
        js.setPreferredSize(new Dimension(400, 200));
        c.add(js);
        List<MuestraColor> colors = new ArrayList<>();
        List<Color> readListColorProperty = Vista.GraphicConfigurationManager.readListColorProperty("series_paint");
        readListColorProperty.stream().forEach((co) -> {
            colors.add(new MuestraColor(co));
        });
        pintarLineas(p, colors);
        if (JOptionPane.showConfirmDialog(null, c, localizationResources.getString("Series_Paint"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            List<Color> colorlist = new ArrayList<>();
            colors.stream().forEach((ps) -> {
                colorlist.add((Color) ps.getPaint());
            });
            Vista.GraphicConfigurationManager.writeListColorProperty("series_paint", colorlist);
        }
    }

    private void addLinea(JPanel p, MuestraColor c, List<MuestraColor> colors) {
        JPanel linea = new JPanel();
        linea.add(Box.createHorizontalStrut(10));
        linea.setLayout(new BoxLayout(linea, BoxLayout.X_AXIS));
        JLabel etiqueta = new JLabel(localizationResources.getString("corSeriesNumero") + " " + String.valueOf(p.getComponentCount()) + " " + (p.getComponentCount() == 0 ? "(" + Vista.bundle.getString("senDefinir") + ")" : "") + ": ");
        linea.add(etiqueta);
        linea.add(Box.createHorizontalGlue());
        MuestraColor ps = c != null ? c : new MuestraColor(null);
        ps.setMaximumSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        ps.setMinimumSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        ps.setPreferredSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        ps.setSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        linea.add(ps);
        linea.setMaximumSize(new Dimension(400, 30));
        linea.setPreferredSize(new Dimension(400, 30));
        linea.add(Box.createHorizontalStrut(20));
        final JColorChooser jcc = new JColorChooser(c != null ? (Color) c.getPaint() : Color.BLACK);
        JButton boton1 = new JButton(ps.getPaint() != null ? Vista.bundle.getString("modificar") : Vista.bundle.getString("engadir"));
        ActionListener okActionListener1 = (ActionEvent e1) -> {
            if (ps.getPaint() == null) {
                colors.add(ps);
            }
            ps.setPaint(jcc.getColor());
            pintarLineas(p, colors);
        };
        boton1.addActionListener((ActionEvent e) -> {
            final JDialog jccd = JColorChooser.createDialog(null, "Change Button Background", true, jcc, okActionListener1, (ActionEvent e1) -> {
            });
            jccd.setVisible(true);
        });
        linea.add(boton1);
        linea.add(Box.createHorizontalStrut(10));
        if (ps.getPaint() != null) {
            JButton boton2 = new JButton(Vista.bundle.getString("eliminar"));
            boton2.addActionListener((ActionEvent e1) -> {
                colors.remove(ps);
                pintarLineas(p, colors);
            });
            linea.add(boton2);
            linea.add(Box.createHorizontalStrut(10));
        }
        p.add(linea);
    }

    private void pintarLineas(JPanel p, List<MuestraColor> colors) {
        p.removeAll();
        colors.stream().forEach((co) -> {
            addLinea(p, co, colors);
        });
        addLinea(p, null, colors);
        p.revalidate();
        p.repaint();
    }

}
