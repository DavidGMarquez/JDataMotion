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
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jdatamotion.Vista;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.layout.LCBLayout;
import org.jfree.ui.FontChooserPanel;
import org.jfree.ui.FontDisplayField;
import org.jfree.ui.PaintSample;

/**
 *
 * @author usuario
 */
public class DefaultTitleEditorConfigurable extends JPanel implements ActionListener {

    /**
     * Whether or not to display the title on the chart.
     */
    private boolean showTitle;

    /**
     * The checkbox to indicate whether or not to display the title.
     */
    private JCheckBox showTitleCheckBox;

    /**
     * A field for displaying/editing the title text.
     */
    private JTextField titleField;

    /**
     * The font used to draw the title.
     */
    private Font titleFont;

    /**
     * A field for displaying a description of the title font.
     */
    private final JTextField fontfield;

    /**
     * The button to use to select a new title font.
     */
    private final JButton selectFontButton;

    /**
     * The paint (color) used to draw the title.
     */
    private final PaintSample titlePaint;

    /**
     * The button to use to select a new paint (color) to draw the title.
     */
    private final JButton selectPaintButton;

    /**
     * The resourceBundle for the localization.
     */
    protected static ResourceBundle localizationResources
            //            = ResourceBundleWrapper.getBundle(
            //                    "org.jfree.chart.editor.LocalizationBundle");
            = Vista.bundle;

    /**
     * Standard constructor: builds a panel for displaying/editing the
     * properties of the specified title.
     *
     * @param title the title, which should be changed.
     */
    public DefaultTitleEditorConfigurable() {

//        TextTitle t = (title != null ? (TextTitle) title
//                : new TextTitle(localizationResources.getString("Title")));
//        this.showTitle = (title != null);
        this.titleFont = Vista.GraphicConfigurationManager.readFontProperty("title_font");
//        this.titleField = new JTextField(t.getText());
        //this.titlePaint = new PaintSample(t.getPaint());
        this.titlePaint = new PaintSample(Vista.GraphicConfigurationManager.readColorProperty("title_paint"));

        setLayout(new BorderLayout());

        JPanel general = new JPanel(new BorderLayout());
        general.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        localizationResources.getString("General")
                )
        );

        JPanel interior = new JPanel(new LCBLayout(4));
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

//        interior.add(new JLabel(localizationResources.getString("Show_Title")));
//        this.showTitleCheckBox = new JCheckBox();
//        this.showTitleCheckBox.setSelected(this.showTitle);
//        this.showTitleCheckBox.setActionCommand("ShowTitle");
//        this.showTitleCheckBox.addActionListener(this);
//        interior.add(new JPanel());
//        interior.add(this.showTitleCheckBox);
//
//        JLabel titleLabel = new JLabel(localizationResources.getString("Text"));
//        interior.add(titleLabel);
//        interior.add(this.titleField);
//        interior.add(new JPanel());
        JLabel fontLabel = new JLabel(localizationResources.getString("Font"));
        this.fontfield = new FontDisplayField(this.titleFont);
        this.fontfield.setEnabled(true);
        this.fontfield.setEditable(false);
        this.selectFontButton = new JButton(
                localizationResources.getString("Select...")
        );
        this.selectFontButton.setActionCommand("SelectFont");
        this.selectFontButton.addActionListener(this);
        interior.add(fontLabel);
        interior.add(this.fontfield);
        interior.add(this.selectFontButton);

        JLabel colorLabel = new JLabel(
                localizationResources.getString("Color")
        );
        this.selectPaintButton = new JButton(
                localizationResources.getString("Select...")
        );
        this.selectPaintButton.setActionCommand("SelectPaint");
        this.selectPaintButton.addActionListener(this);
        interior.add(colorLabel);
        interior.add(this.titlePaint);
        interior.add(this.selectPaintButton);

        this.enableOrDisableControls();

        general.add(interior);
        add(general, BorderLayout.NORTH);
    }

    /**
     * Returns the title text entered in the panel.
     *
     * @return The title text entered in the panel.
     */
    public String getTitleText() {
        return this.titleField.getText();
    }

    /**
     * Returns the font selected in the panel.
     *
     * @return The font selected in the panel.
     */
    public Font getTitleFont() {
        return this.titleFont;
    }

    /**
     * Returns the paint selected in the panel.
     *
     * @return The paint selected in the panel.
     */
    public Paint getTitlePaint() {
        return this.titlePaint.getPaint();
    }

    /**
     * Handles button clicks by passing control to an appropriate handler
     * method.
     *
     * @param event the event
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        switch (command) {
            case "SelectFont":
                attemptFontSelection();
                break;
            case "SelectPaint":
                attemptPaintSelection();
                break;
            case "ShowTitle":
                attemptModifyShowTitle();
                break;
        }
    }

    /**
     * Presents a font selection dialog to the user.
     */
    public void attemptFontSelection() {

        FontChooserPanel panel = new FontChooserPanel(this.titleFont);
        int result
                = JOptionPane.showConfirmDialog(
                        this, panel, localizationResources.getString("Font_Selection"),
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                );

        if (result == JOptionPane.OK_OPTION) {
            this.titleFont = panel.getSelectedFont();
            this.fontfield.setText(
                    this.titleFont.getFontName() + " " + this.titleFont.getSize()
            );
        }
    }

    /**
     * Allow the user the opportunity to select a Paint object. For now, we just
     * use the standard color chooser - all colors are Paint objects, but not
     * all Paint objects are colors (later we can implement a more general Paint
     * chooser).
     */
    public void attemptPaintSelection() {
        Paint p = this.titlePaint.getPaint();
        Color defaultColor = (p instanceof Color ? (Color) p : Color.blue);
        Color c = JColorChooser.showDialog(
                this, localizationResources.getString("Title_Color"), defaultColor
        );
        if (c != null) {
            this.titlePaint.setPaint(c);
        }
    }

    /**
     * Allow the user the opportunity to change whether the title is displayed
     * on the chart or not.
     */
    private void attemptModifyShowTitle() {
        this.showTitle = this.showTitleCheckBox.isSelected();
        this.enableOrDisableControls();
    }

    /**
     * If we are supposed to show the title, the controls are enabled. If we are
     * not supposed to show the title, the controls are disabled.
     */
    private void enableOrDisableControls() {
    }

    /**
     * Sets the properties of the specified title to match the properties
     * defined on this panel.
     *
     * @param chart the chart whose title is to be modified.
     */
    public void setTitleProperties(JFreeChart chart) {
        if (this.showTitle) {
            TextTitle title = chart.getTitle();
            if (title == null) {
                title = new TextTitle();
                chart.setTitle(title);
            }
            title.setText(getTitleText());
            title.setFont(getTitleFont());
            title.setPaint(getTitlePaint());
        } else {
            chart.setTitle((TextTitle) null);
        }
    }

}
