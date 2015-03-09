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
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jfree.chart.axis.ColorBar;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.editor.PaletteSample;
import org.jfree.chart.plot.GreyPalette;
import org.jfree.chart.plot.RainbowPalette;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.layout.LCBLayout;

/**
 *
 * @author usuario
 */
class DefaultColorBarEditorConfigurable extends DefaultNumberAxisEditorConfigurable {

    /**
     * A checkbox that indicates whether or not the color indices should run
     * high to low.
     */
    private JCheckBox invertPaletteCheckBox;

    /**
     * Flag set by invertPaletteCheckBox.
     */
    private boolean invertPalette = false;

    /**
     * A checkbox that indicates whether the palette is stepped.
     */
    private JCheckBox stepPaletteCheckBox;

    /**
     * Flag set by stepPaletteCheckBox.
     */
    private boolean stepPalette = false;

    /**
     * The Palette Sample displaying the current Palette.
     */
    private PaletteSample currentPalette;

    /**
     * An array of availiable sample palettes.
     */
    private PaletteSample[] availablePaletteSamples;

    /**
     * The resourceBundle for the localization.
     */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    /**
     * Creates a new edit panel for a color bar.
     *
     * @param colorBar the color bar.
     */
    public DefaultColorBarEditorConfigurable(ColorBar colorBar) {
        super((NumberAxis) colorBar.getAxis());
        this.invertPalette = colorBar.getColorPalette().isInverse();
        this.stepPalette = colorBar.getColorPalette().isStepped();
        this.currentPalette = new PaletteSample(colorBar.getColorPalette());
        this.availablePaletteSamples = new PaletteSample[2];
        this.availablePaletteSamples[0]
                = new PaletteSample(new RainbowPalette());
        this.availablePaletteSamples[1]
                = new PaletteSample(new GreyPalette());

        JTabbedPane other = getOtherTabs();

        JPanel palettePanel = new JPanel(new LCBLayout(4));
        palettePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        palettePanel.add(new JPanel());
        this.invertPaletteCheckBox = new JCheckBox(
                localizationResources.getString("Invert_Palette"),
                this.invertPalette
        );
        this.invertPaletteCheckBox.setActionCommand("invertPalette");
        this.invertPaletteCheckBox.addActionListener(this);
        palettePanel.add(this.invertPaletteCheckBox);
        palettePanel.add(new JPanel());

        palettePanel.add(new JPanel());
        this.stepPaletteCheckBox = new JCheckBox(
                localizationResources.getString("Step_Palette"),
                this.stepPalette
        );
        this.stepPaletteCheckBox.setActionCommand("stepPalette");
        this.stepPaletteCheckBox.addActionListener(this);
        palettePanel.add(this.stepPaletteCheckBox);
        palettePanel.add(new JPanel());

        palettePanel.add(
                new JLabel(localizationResources.getString("Palette"))
        );
        JButton button
                = new JButton(localizationResources.getString("Set_palette..."));
        button.setActionCommand("PaletteChoice");
        button.addActionListener(this);
        palettePanel.add(this.currentPalette);
        palettePanel.add(button);

        other.add(localizationResources.getString("Palette"), palettePanel);

    }

    /**
     * Handles actions from within the property panel.
     *
     * @param event the event.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("PaletteChoice")) {
            attemptPaletteSelection();
        } else if (command.equals("invertPalette")) {
            this.invertPalette = this.invertPaletteCheckBox.isSelected();
        } else if (command.equals("stepPalette")) {
            this.stepPalette = this.stepPaletteCheckBox.isSelected();
        } else {
            super.actionPerformed(event);  // pass to super-class for handling
        }
    }

    /**
     * Handle a palette selection.
     */
    private void attemptPaletteSelection() {
        PaletteChooserPanelConfigurable panel = new PaletteChooserPanelConfigurable(null, this.availablePaletteSamples);
        int result = JOptionPane.showConfirmDialog(
                this, panel, localizationResources.getString("Palette_Selection"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            double zmin = this.currentPalette.getPalette().getMinZ();
            double zmax = this.currentPalette.getPalette().getMaxZ();
            this.currentPalette.setPalette(panel.getSelectedPalette());
            this.currentPalette.getPalette().setMinZ(zmin);
            this.currentPalette.getPalette().setMaxZ(zmax);
        }
    }

    /**
     * Sets the properties of the specified axis to match the properties defined
     * on this panel.
     *
     * @param colorBar the color bar.
     */
    public void setAxisProperties(ColorBar colorBar) {
        super.setAxisProperties(colorBar.getAxis());
        colorBar.setColorPalette(this.currentPalette.getPalette());
        colorBar.getColorPalette().setInverse(this.invertPalette); //dmo added
        colorBar.getColorPalette().setStepped(this.stepPalette); //dmo added
    }

    /**
     * A static method that returns a panel that is appropriate for the axis
     * type.
     *
     * @param colorBar the color bar.
     *
     * @return A panel or <code>null</code< if axis is <code>null</code>.
     */
    public static DefaultColorBarEditorConfigurable getInstance(ColorBar colorBar) {

        if (colorBar != null) {
            return new DefaultColorBarEditorConfigurable(colorBar);
        } else {
            return null;
        }

    }

}
