/*
 * The MIT License
 *
 * Copyright 2014 usuario.
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

import javax.swing.*;
import javax.swing.table.*;

public class BadCursor {

  public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              createJDialog();
              createJFrame();
          }
      });
  }

  public static void createJDialog() {
        // This will create a Dialog with a JTable that does not correctly show
        // a resize cursor.
        JTable table = new JTable(new DefaultTableModel(new String[] {"a", "b"},
            10));
        JFrame parent = new JFrame();
        JDialog dialog = new JDialog(parent, "No Resize Cursor");
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.getContentPane().add(scrollPane);
        dialog.pack();
        dialog.setVisible(true);
  }

  public static void createJFrame() {
        // This will create a Dialog with a JTable that correctly shows
        // a resize cursor.
        JTable table = new JTable(new DefaultTableModel(new String[] {"a", "b"},
            10));
        JFrame frame = new JFrame("Resize Cursor");
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
  }
}