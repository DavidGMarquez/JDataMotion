package jdatamotion;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;
import javax.swing.*;

@SuppressWarnings("serial")
public class DialogWithScrollPane2 extends JFrame {

   public DialogWithScrollPane2() {
      super();

      //setResizable(false);
      final JPanel pane = (JPanel) getContentPane();

      Vector<Object> listOfStuff = new Vector<Object>();
      for (int i = 0; i < 100; i++) {
         listOfStuff.add(Integer.toString(i));
      }

      final JScrollPane scrollPane = new JScrollPane();
      JList list = new JList(listOfStuff);

      scrollPane.setViewportView(list);

      final JPanel blueRectPanel = new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.blue);
            g.fillRect(20, 50, 100, 200);
         }
      };
      blueRectPanel.setOpaque(false);

      final JLayeredPane layeredPane = new JLayeredPane();
      layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
      layeredPane.add(blueRectPanel, JLayeredPane.PALETTE_LAYER);

      layeredPane.addComponentListener(new ComponentAdapter() {

         private void resizeLayers() {
            final JViewport viewport = scrollPane.getViewport();
            scrollPane.setBounds(layeredPane.getBounds());
            blueRectPanel.setBounds(viewport.getBounds());
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  blueRectPanel.setBounds(viewport.getBounds());
               }
            });
         }

         @Override
         public void componentShown(ComponentEvent e) {
            resizeLayers();
         }

         @Override
         public void componentResized(ComponentEvent e) {
            resizeLayers();
         }
      });

      pane.add(layeredPane);
      setPreferredSize(new Dimension(300, 300));
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();
      setLocation(500, 250);
      setVisible(true);
   }

   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new DialogWithScrollPane2();
         }
      });
   }
}