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
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
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

    private List<Color> seriesPaint;
    private List<Shape> seriesOutlineStroke;
    private List<Shape> seriesShape;
    private List<Color> seriesOutlinePaint;

    public List getSeriesPaint() {
        return seriesPaint;
    }

    public List getSeriesOutlineStroke() {
        return seriesOutlineStroke;
    }

    public List getSeriesShape() {
        return seriesShape;
    }

    public List getSeriesOutlinePaint() {
        return seriesOutlinePaint;
    }

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
                "Series_Shape")));
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(true);
        interior.add(new JLabel());
        button = new JButton(localizationResources.getString("Edit..."));
        button.setActionCommand("SeriesShape");
        button.addActionListener(this);
        button.setEnabled(true);
        interior.add(button);

        /*interior.add(new JLabel(localizationResources.getString(
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
         interior.add(button);*/
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
        this.seriesPaint = Vista.GraphicConfigurationManager.readListColorProperty("series_paint");
        this.seriesShape = Vista.GraphicConfigurationManager.readListStrokeProperty("series_shape");
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
            case "SeriesShape":
                attemptModifySeriesShape();
                break;
            /*case "SeriesOutlinePaint":
             attemptModifySeriesOutlinePaint();
             break;
             case "SeriesOutlineStroke":
             attemptModifySeriesOutlineStroke();
             break;*/
        }
    }

    private void attemptModifyBackgroundPaint() {
        Color c;
        c = JColorChooser.showDialog(this, localizationResources.getString("Background_Color"), Vista.GraphicConfigurationManager.readColorProperty("chart_background_paint"));
        if (c != null) {
            this.background.setPaint(c);
        }
    }

    /*private void attemptModifySeriesOutlinePaint() {
     showColorListSelector(this.seriesOutlinePaint);
     }*/
    private void attemptModifySeriesShape() {
        showStrokeListSelector(this.seriesShape);
    }

    /*private void attemptModifySeriesOutlineStroke() {
     showStrokeListSelector(this.seriesOutlineStroke);
     }*/
    private void attemptModifySeriesPaint() {
        showColorListSelector(this.seriesPaint);
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

    class MuestraForma extends JPanel {

        private Shape forma;

        public void setForma(Shape forma) {
            this.forma = forma;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(20, 20);
        }

        public Integer getNumeroVertices() {
            if (forma instanceof RegularPolygon) {
                return ((RegularPolygon) forma).getVertexCount();
            } else if (forma instanceof StarPolygon) {
                return ((StarPolygon) forma).getVertexCount();
            } else {
                return null;
            }
        }

        public Shape getForma() {
            return forma;
        }

        public MuestraForma(Shape forma) {
            this.forma = forma;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            if (forma != null) {
                ((Graphics2D) g).translate(10, 10);
                ((Graphics2D) g).setPaint(Color.black);
                ((Graphics2D) g).draw(forma);
            }
        }
    }

    private void showColorListSelector(List<Color> initialList) {
        JPanel c = new JPanel();
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JScrollPane js = new JScrollPane(p);
        js.setMaximumSize(new Dimension(400, 200));
        js.setPreferredSize(new Dimension(400, 200));
        c.add(js);
        List<MuestraColor> colors = new ArrayList<>();
        initialList.stream().forEach((co) -> {
            colors.add(new MuestraColor(co));
        });
        pintarLineasColor(p, colors);
        if (JOptionPane.showConfirmDialog(null, c, localizationResources.getString("seleccionarListaCores"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            List<Color> colorlist = new ArrayList<>();
            colors.stream().forEach((ps) -> {
                colorlist.add((Color) ps.getPaint());
            });
            initialList.clear();
            colorlist.stream().forEach((color) -> {
                initialList.add(color);
            });
        }
    }

    private void showStrokeListSelector(List<Shape> initialList) {
        JPanel c = new JPanel();
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JScrollPane js = new JScrollPane(p);
        js.setMaximumSize(new Dimension(400, 200));
        js.setPreferredSize(new Dimension(400, 200));
        c.add(js);
        List<MuestraForma> shapes = new ArrayList<>();
        initialList.stream().forEach((co) -> {
            shapes.add(new MuestraForma(co));
        });
        pintarLineasForma(p, shapes);
        if (JOptionPane.showConfirmDialog(null, c, localizationResources.getString("seleccionarListaFormas"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            List<Shape> shapelist = new ArrayList<>();
            shapes.stream().forEach((ps) -> {
                shapelist.add(ps.getForma());
            });
            initialList.clear();
            shapelist.stream().forEach((shape) -> {
                initialList.add(shape);
            });
        }
    }

    private void addLineaColor(JPanel p, MuestraColor c, List<MuestraColor> colors) {
        JPanel linea = new JPanel();
        linea.add(Box.createHorizontalStrut(10));
        linea.setLayout(new BoxLayout(linea, BoxLayout.X_AXIS));
        JLabel etiqueta = new JLabel(localizationResources.getString("corNumero") + " " + String.valueOf(p.getComponentCount()) + " " + (p.getComponentCount() == 0 ? "(" + Vista.bundle.getString("senDefinir") + ")" : "") + ": ");
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
            pintarLineasColor(p, colors);
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
                pintarLineasColor(p, colors);
            });
            linea.add(boton2);
            linea.add(Box.createHorizontalStrut(10));
        }
        p.add(linea);
    }

    private void pintarLineasColor(JPanel p, List<MuestraColor> colors) {
        p.removeAll();
        colors.stream().forEach((co) -> {
            addLineaColor(p, co, colors);
        });
        addLineaColor(p, null, colors);
        p.revalidate();
        p.repaint();
    }

    private void pintarLineasForma(JPanel p, List<MuestraForma> shapes) {
        p.removeAll();
        shapes.stream().forEach((co) -> {
            addLineaForma(p, co, shapes);
        });
        addLineaForma(p, null, shapes);
        p.revalidate();
        p.repaint();
    }

    private void addLineaForma(JPanel p, MuestraForma c, List<MuestraForma> shapes) {
        JPanel linea = new JPanel();
        linea.add(Box.createHorizontalStrut(10));
        linea.setLayout(new BoxLayout(linea, BoxLayout.X_AXIS));
        JLabel etiqueta = new JLabel(localizationResources.getString("formaNumero") + " " + String.valueOf(p.getComponentCount()) + " " + (p.getComponentCount() == 0 ? "(" + Vista.bundle.getString("senDefinir") + ")" : "") + ": ");
        linea.add(etiqueta);
        linea.add(Box.createHorizontalGlue());
        MuestraForma ps = c != null ? c : new MuestraForma(null);
        ps.setMaximumSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        ps.setMinimumSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        ps.setPreferredSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        ps.setSize(c != null ? new Dimension(20, 20) : new Dimension(0, 0));
        linea.add(ps);
        linea.setMaximumSize(new Dimension(400, 30));
        linea.setPreferredSize(new Dimension(400, 30));
        linea.add(Box.createHorizontalStrut(20));
        JPanel co = new JPanel(new FlowLayout());
        JComboBox<String> jcb = new JComboBox(new String[]{Vista.bundle.getString("ningun"), Vista.bundle.getString("estrela"), Vista.bundle.getString("poligono")});
        if (c != null) {
            if (c.getForma() instanceof StarPolygon) {
                jcb.setSelectedIndex(1);
            }
            if (c.getForma() instanceof RegularPolygon) {
                jcb.setSelectedIndex(2);
            }
        }
        co.add(jcb);
        JSpinner js = new JSpinner(new SpinnerNumberModel(c != null ? c.getNumeroVertices() : 3, 3, 10, 1));
        co.add(js);
        JButton boton1 = new JButton(ps.getForma() != null ? Vista.bundle.getString("modificar") : Vista.bundle.getString("engadir"));
        boton1.addActionListener((ActionEvent e) -> {
            if (JOptionPane.showConfirmDialog(null, co, localizationResources.getString("seleccionarListaFormas"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                if (jcb.getSelectedIndex() != 0 && ps.getForma() == null) {
                    shapes.add(ps);
                }
                if (jcb.getSelectedIndex() != 0) {
                    Shape s = null;
                    int v = (int) js.getModel().getValue();
                    switch (jcb.getSelectedIndex()) {
                        case 1:
                            s = new StarPolygon(0, 0, 4, 2, v, -Math.PI / 2);
                            break;
                        case 2:
                            s = new RegularPolygon(0, 0, 4, v, -Math.PI / 2);
                            break;
                    }
                    ps.setForma(s);
                }
                pintarLineasForma(p, shapes);
            }
        });
        linea.add(boton1);
        linea.add(Box.createHorizontalStrut(10));
        if (ps.getForma() != null) {
            JButton boton2 = new JButton(Vista.bundle.getString("eliminar"));
            boton2.addActionListener((ActionEvent e1) -> {
                shapes.remove(ps);
                pintarLineasForma(p, shapes);
            });
            linea.add(boton2);
            linea.add(Box.createHorizontalStrut(10));
        }
        p.add(linea);
    }
}
