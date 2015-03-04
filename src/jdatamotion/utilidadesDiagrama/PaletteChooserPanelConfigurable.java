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

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.jfree.chart.editor.PaletteSample;
import org.jfree.chart.plot.ColorPalette;
import org.jfree.chart.plot.RainbowPalette;

/**
 *
 * @author usuario
 */
class PaletteChooserPanelConfigurable extends JPanel {

    /**
     * A combo for selecting the stroke.
     */
    private JComboBox selector;

    /**
     * Constructor.
     *
     * @param current the current palette sample.
     * @param available an array of 'available' palette samples.
     */
    public PaletteChooserPanelConfigurable(PaletteSample current,
            PaletteSample[] available) {
        setLayout(new BorderLayout());
        this.selector = new JComboBox(available);
        this.selector.setSelectedItem(current);
        this.selector.setRenderer(new PaletteSample(new RainbowPalette()));
        add(this.selector);
    }

    /**
     * Returns the selected palette.
     *
     * @return The selected palette.
     */
    public ColorPalette getSelectedPalette() {
        PaletteSample sample = (PaletteSample) this.selector.getSelectedItem();
        return sample.getPalette();
    }
}
