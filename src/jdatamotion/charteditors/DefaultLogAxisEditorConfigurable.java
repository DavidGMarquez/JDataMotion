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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberTickUnit;

/**
 *
 * @author usuario
 */
public class DefaultLogAxisEditorConfigurable extends DefaultValueAxisEditorConfigurable {

    private double manualTickUnitValue;

    private JTextField manualTickUnit;

    /**
     * Standard constructor: builds a property panel for the specified axis.
     *
     * @param axis the axis, which should be changed.
     */
    public DefaultLogAxisEditorConfigurable(LogAxis axis) {
        super(axis);
        this.manualTickUnitValue = axis.getTickUnit().getSize();
        manualTickUnit.setText(Double.toString(this.manualTickUnitValue));
    }

    /**
     * Creates a panel for editing the tick unit.
     *
     * @return A panel.
     */
    @Override
    protected JPanel createTickUnitPanel() {
        JPanel tickUnitPanel = super.createTickUnitPanel();

        tickUnitPanel.add(new JLabel(localizationResources.getString(
                "Manual_TickUnit_value")));
        this.manualTickUnit = new JTextField(Double.toString(
                this.manualTickUnitValue));
        this.manualTickUnit.setEnabled(!isAutoTickUnitSelection());
        this.manualTickUnit.setActionCommand("TickUnitValue");
        this.manualTickUnit.addActionListener(this);
        this.manualTickUnit.addFocusListener(this);
        tickUnitPanel.add(this.manualTickUnit);
        tickUnitPanel.add(new JPanel());

        return tickUnitPanel;
    }

    /**
     * Handles actions from within the property panel.
     *
     * @param event an event.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("TickUnitValue")) {
            validateTickUnit();
        } else {
            // pass to the super-class for handling
            super.actionPerformed(event);
        }
    }

    @Override
    public void focusLost(FocusEvent event) {
        super.focusLost(event);
        if (event.getSource() == this.manualTickUnit) {
            validateTickUnit();
        }
    }

    /**
     * Toggles the auto-tick-unit setting.
     */
    @Override
    public void toggleAutoTick() {
        super.toggleAutoTick();
        if (isAutoTickUnitSelection()) {
            this.manualTickUnit.setText(Double.toString(this.manualTickUnitValue));
            this.manualTickUnit.setEnabled(false);
        } else {
            this.manualTickUnit.setEnabled(true);
        }
    }

    /**
     * Validates the tick unit entered.
     */
    public void validateTickUnit() {
        double newTickUnit;
        try {
            newTickUnit = Double.parseDouble(this.manualTickUnit.getText());
        } catch (NumberFormatException e) {
            newTickUnit = this.manualTickUnitValue;
        }

        if (newTickUnit > 0.0) {
            this.manualTickUnitValue = newTickUnit;
        }
        this.manualTickUnit.setText(Double.toString(this.manualTickUnitValue));
    }

    /**
     * Sets the properties of the specified axis to match the properties defined
     * on this panel.
     *
     * @param axis the axis.
     */
    @Override
    public void setAxisProperties(Axis axis) {
        super.setAxisProperties(axis);
        LogAxis logAxis = (LogAxis) axis;
        if (!isAutoTickUnitSelection()) {
            logAxis.setTickUnit(new NumberTickUnit(manualTickUnitValue));
        }
    }
}
