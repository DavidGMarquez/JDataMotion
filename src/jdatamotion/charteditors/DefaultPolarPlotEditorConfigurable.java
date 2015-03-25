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

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import jdatamotion.Vista;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.layout.LCBLayout;

/**
 *
 * @author usuario
 */
public class DefaultPolarPlotEditorConfigurable extends DefaultPlotEditorConfigurable
        implements FocusListener {

    /**
     * A text field to enter a manual TickUnit.
     */
    private JTextField manualTickUnit;

    /**
     * A text field to enter the angleOffset.
     */
    private JTextField angleOffset;

    /**
     * The size for the manual TickUnit.
     */
    private double manualTickUnitValue;

    /**
     * The value for the plot's angle offset.
     */
    private double angleOffsetValue;

    /**
     * Standard constructor - constructs a panel for editing the properties of
     * the specified plot.
     *
     * @param plot the plot, which should be changed.
     */
    public DefaultPolarPlotEditorConfigurable() {
        super();
        //this.angleOffsetValue = plot.getAngleOffset();
        this.angleOffsetValue = Vista.GraphicConfigurationManager.readDoubleProperty("angle-offset");
        this.angleOffset.setText(Double.toString(this.angleOffsetValue));
        //this.manualTickUnitValue = plot.getAngleTickUnit().getSize();
        this.manualTickUnitValue = Vista.GraphicConfigurationManager.readDoubleProperty("manual-tick-unit");
        this.manualTickUnit.setText(Double.toString(this.manualTickUnitValue));
    }

    /**
     * Creates a tabbed pane for editing the plot attributes.
     *
     * @param plot the plot.
     *
     * @return A tabbed pane.
     */
    @Override
    protected JTabbedPane createPlotTabs() {
        JTabbedPane tabs = super.createPlotTabs();
        // TODO find a better localization key
        tabs.insertTab(localizationResources.getString("General1"), null,
                createPolarPlotPanel(), null, 0);
        tabs.setSelectedIndex(0);
        return tabs;
    }

    public double getManualTickUnitValue() {
        return manualTickUnitValue;
    }

    public double getAngleOffsetValue() {
        return angleOffsetValue;
    }

    protected JPanel createPolarPlotPanel() {
        JPanel plotPanel = new JPanel(new LCBLayout(3));
        plotPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        plotPanel.add(new JLabel(localizationResources.getString(
                "AngleOffset")));
        this.angleOffset = new JTextField(Double.toString(
                this.angleOffsetValue));
        this.angleOffset.setActionCommand("AngleOffsetValue");
        this.angleOffset.addActionListener(this);
        this.angleOffset.addFocusListener(this);
        plotPanel.add(this.angleOffset);
        plotPanel.add(new JPanel());

        plotPanel.add(new JLabel(localizationResources.getString(
                "Manual_TickUnit_value")));
        this.manualTickUnit = new JTextField(Double.toString(
                this.manualTickUnitValue));
        this.manualTickUnit.setActionCommand("TickUnitValue");
        this.manualTickUnit.addActionListener(this);
        this.manualTickUnit.addFocusListener(this);
        plotPanel.add(this.manualTickUnit);
        plotPanel.add(new JPanel());

        return plotPanel;
    }

    /**
     * Does nothing.
     *
     * @param event the event.
     */
    @Override
    public void focusGained(FocusEvent event) {
        // don't need to do anything
    }

    /**
     * Revalidates minimum/maximum range.
     *
     * @param event the event.
     */
    @Override
    public void focusLost(FocusEvent event) {
        if (event.getSource() == this.angleOffset) {
            validateAngleOffset();
        } else if (event.getSource() == this.manualTickUnit) {
            validateTickUnit();
        }
    }

    /**
     * Handles actions from within the property panel.
     *
     * @param event an event.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("AngleOffsetValue")) {
            validateAngleOffset();
        } else if (command.equals("TickUnitValue")) {
            validateTickUnit();
        }
    }

    /**
     * Validates the angle offset entered by the user.
     */
    public void validateAngleOffset() {
        double newOffset;
        try {
            newOffset = Double.parseDouble(this.angleOffset.getText());
        } catch (NumberFormatException e) {
            newOffset = this.angleOffsetValue;
        }
        this.angleOffsetValue = newOffset;
        this.angleOffset.setText(Double.toString(this.angleOffsetValue));
    }

    /**
     * Validates the tick unit entered by the user.
     */
    public void validateTickUnit() {
        double newTickUnit;
        try {
            newTickUnit = Double.parseDouble(this.manualTickUnit.getText());
        } catch (NumberFormatException e) {
            newTickUnit = this.manualTickUnitValue;
        }

        if (newTickUnit > 0.0 && newTickUnit < 360.0) {
            this.manualTickUnitValue = newTickUnit;
        }
        this.manualTickUnit.setText(Double.toString(this.manualTickUnitValue));
    }

    @Override
    public void updatePlotProperties(Plot plot) {
        super.updatePlotProperties(plot);
        PolarPlot pp = (PolarPlot) plot;
        pp.setAngleTickUnit(new NumberTickUnit(this.manualTickUnitValue));
        pp.setAngleOffset(this.angleOffsetValue);
    }
}
