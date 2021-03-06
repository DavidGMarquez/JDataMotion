package jdatamotion;

import jdatamotion.filtros.FilterHandler;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.JTabbedPane;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import jdatamotion.ManexadorScatterPlots.ChartPanelConfigurable;
import jdatamotion.ManexadorScatterPlots.JFrameChartPanel;
import jdatamotion.ManexadorScatterPlots.ScatterPlot;
import jdatamotion.Modelo.JarClassLoader;
import jdatamotion.charteditors.ChartEditorManagerConfigurable;
import jdatamotioncommon.filtros.DoubleParameter;
import jdatamotioncommon.filtros.StringParameter;
import jdatamotion.sesions.Sesion;
import jdatamotion.sesions.SesionVista;
import jdatamotion.sesions.Sesionizable;
import jdatamotion.charteditors.DefaultChartEditorConfigurable;
import jdatamotion.charteditors.RegularPolygon;
import jdatamotion.charteditors.StarPolygon;
import jdatamotioncommon.ComparableInstances;
import jdatamotioncommon.Utils;
import jdatamotioncommon.filtros.IFilter;
import jdatamotioncommon.filtros.IntegerParameter;
import jdatamotioncommon.filtros.Parameter;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import org.apache.commons.lang3.ArrayUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jsoup.Jsoup;
import weka.core.Attribute;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class Vista extends JFrame implements Observer, Sesionizable, PropertyChangeListener {

    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;

    public static final int ORDE_MODELO = 0;
    public static final int ORDE_INDICE_TEMPORAL_NUMERICO = 1;
    public static final int ORDE_INDICE_TEMPORAL_NUMERICO_PONDERADO = 2;
    public static final int ORDE_INDICE_TEMPORAL_HORA = 3;

    private static final int EXPLORADOR_IMPORTAR_FICHEIRO = 0;
    private static final int EXPLORADOR_ABRIR_SESION = 1;
    private static final int EXPLORADOR_GARDAR_SESION = 2;
    private static final int EXPLORADOR_EXPORTAR_FICHEIRO = 3;
    private static final int EXPLORADOR_IMPORTAR_FILTROS = 4;
    private static final int EXPLORADOR_EXPORTAR_FILTROS = 5;
    private static final int EXPLORADOR_IMPORTAR_FILTRO_DESDE_JAR = 6;

    private transient Modelo meuModelo;
    private int ordeVisualizacion;
    private int paso;
    private static ManexadorScatterPlots mansp;
    private final TarefaProgreso task;
    private transient Controlador meuControlador;
    private boolean scatterPlotsVisibles[][];
    private boolean pulsarSlider;
    private JTableModeloMenu jTableModelo;
    private int ultimoEstadoReprodutor;
    private int lonxitudeEstela;
    private Color corEstela;
    public static ResourceBundle recursosIdioma;
    private static final String ficheiroConfiguracion = "configuracion.properties";
    private static final String ficheiroConfiguracionPorDefecto = "/jdatamotion/default_config.properties";
    public static Properties propiedades;
    private boolean visualizacionDesactualizada;
    private String distanceFormula;
    private Instance puntoA;
    private Instance puntoB;
    private Character buscaPunto = null;

    public static ResourceBundle getRecursosIdioma() {
        return recursosIdioma;
    }

    public final void reset() {
        this.pulsarSlider = false;
        this.ordeVisualizacion = ORDE_MODELO;
        this.paso = 100;
        this.lonxitudeEstela = 5;
        this.corEstela = Color.WHITE;
        this.jTableModelo = null;
        this.scatterPlotsVisibles = new boolean[0][0];
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "estadoReprodutor":
                int newValue = (int) evt.getNewValue();
                if (newValue != ManexadorScatterPlots.FREEZE) {
                    for (ActionListener al : jButton8.getActionListeners()) {
                        jButton8.removeActionListener(al);
                    }
                    switch (newValue) {
                        case ManexadorScatterPlots.PLAY:
                            jButton8.addActionListener((ActionEvent evt1) -> {
                                mansp.pause();
                            });
                            jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/pause.png")));
                            break;
                        case ManexadorScatterPlots.PAUSE:
                            jButton8.addActionListener((ActionEvent evt1) -> {
                                jButton8ActionPerformed(evt1);
                            });
                            jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/play.png")));
                            break;
                    }
                }
                break;
        }
    }

    public Vista() {
        super();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //NOI18N
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            if (Controlador.debug) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); //NOI18N
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex1) {
                if (Controlador.debug) {
                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        propiedades = lerPropiedades(ficheiroConfiguracionPorDefecto);
        try {
            anadirPropiedades(propiedades, ficheiroConfiguracion);
        } catch (FileNotFoundException ex) {
            escribirPropiedades(new Properties(), ficheiroConfiguracion);
        }
        Locale.setDefault(new Locale(propiedades.getProperty("locale")));
        recursosIdioma = ResourceBundle.getBundle("jdatamotion/idiomas/Bundle", Locale.getDefault());
        reset();
        initComponents();
        this.task = new TarefaProgreso(jProgressBar1);
        this.visualizacionDesactualizada = false;
        this.jPanel8.setVisible(false);
        this.distanceFormula = "sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2))";
    }

    @SuppressWarnings("null")
    private static synchronized void anadirPropiedades(Properties propiedades, String url) throws FileNotFoundException {
        InputStream input = new FileInputStream(url);
        try {
            propiedades.load(input);
        } catch (IOException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static synchronized void escribirPropiedades(Properties propiedades, String url) {
        OutputStream outputStream = null;
        try {
            propiedades.store(new FileOutputStream(new File(url)), null);
        } catch (IOException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private String interpretarFormula(Instance puntoA, Instance puntoB, String formula) throws EvaluationException {
        Matcher m = Pattern.compile("\\$([12])\\.`(.*?)`").matcher(formula);
        String s = formula;
        while (m.find()) {
            Attribute atributo = meuModelo.getComparableInstances().attribute(m.group(2));
            if (atributo != null) {
                s = s.replaceAll("\\$" + m.group(1) + "\\.`" + m.group(2) + "`", String.valueOf((m.group(1).equals("1") ? puntoA : puntoB).value(atributo)));
            }
        }
        return new Evaluator().evaluate(s);
    }

    public static class GraphicConfigurationManager {

        public static Boolean readBooleanProperty(String propiedad) {
            return propiedades.getProperty(propiedad).equals("y");
        }

        public static boolean hasProperty(String propiedad) {
            return propiedades.getProperty(propiedad) != null;
        }

        public static void writeBooleanProperty(String propiedad, Boolean b) {
            gravarEscribirPropiedade(propiedades, propiedad, b ? "y" : "n", ficheiroConfiguracion);
        }

        GraphicConfigurationManager() {
        }

        public static Color readColorProperty(String propiedad) {
            return new Color(Integer.valueOf(propiedades.getProperty(propiedad).split(",")[0]), Integer.valueOf(propiedades.getProperty(propiedad).split(",")[1]), Integer.valueOf(propiedades.getProperty(propiedad).split(",")[2]));
        }

        public static List<Shape> readListStrokeProperty(String propiedad) {
            List<Shape> l = new ArrayList<>();
            for (String serieShape : propiedades.getProperty(propiedad).split("\\|")) {
                if (!serieShape.isEmpty()) {
                    Shape s = null;
                    int v = Integer.valueOf(serieShape.split(",")[1]);
                    switch (serieShape.split(",")[0]) {
                        case "regular":
                            s = new RegularPolygon(0, 0, 4, v, -Math.PI / 2);
                            break;
                        case "star":
                            s = new StarPolygon(0, 0, 4, 2, v, -Math.PI / 2);
                            break;
                    }
                    if (s != null) {
                        l.add(s);
                    }
                }
            }
            return l;
        }

        public static List<Color> readListColorProperty(String propiedad) {
            List<Color> l = new ArrayList<>();
            for (String serieColor : propiedades.getProperty(propiedad).split("\\|")) {
                if (!serieColor.isEmpty()) {
                    l.add(new Color(Integer.valueOf(serieColor.split(",")[0]), Integer.valueOf(serieColor.split(",")[1]), Integer.valueOf(serieColor.split(",")[2])));
                }
            }
            return l;
        }

        public static void writeColorProperty(String propiedad, Color color) {
            gravarEscribirPropiedade(propiedades, propiedad, color.getRed() + "," + color.getGreen() + "," + color.getBlue(), ficheiroConfiguracion);
        }

        public static void writeListColorProperty(String propiedad, List<Color> colorList) {
            String colorListString = "";
            colorListString = colorList.stream().map((c) -> c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "|").reduce(colorListString, String::concat);
            if (!colorListString.isEmpty()) {
                colorListString = colorListString.substring(0, colorListString.length() - 1);
            }
            gravarEscribirPropiedade(propiedades, propiedad, colorListString, ficheiroConfiguracion);
        }

        public static void writeListShapeProperty(String propiedad, List<Shape> shapeList) {
            String shapeListString = "";
            for (Shape s : shapeList) {
                if (s instanceof RegularPolygon) {
                    shapeListString += "regular," + ((RegularPolygon) s).getVertexCount();
                } else if (s instanceof StarPolygon) {
                    shapeListString += "star," + ((StarPolygon) s).getVertexCount();
                }
                shapeListString += "|";
            }
            if (!shapeListString.isEmpty()) {
                shapeListString = shapeListString.substring(0, shapeListString.length() - 1);
            }
            gravarEscribirPropiedade(propiedades, propiedad, shapeListString, ficheiroConfiguracion);
        }

        public static Stroke readStrokeProperty(String propiedad) {
            Float f = Float.parseFloat(propiedades.getProperty(propiedad));
            return f != 0.0 ? new BasicStroke() : null;
        }

        public static void writeStrokeProperty(String propiedad, Stroke s) {
            gravarEscribirPropiedade(propiedades, propiedad, String.valueOf(s != null ? ((BasicStroke) s).getLineWidth() : 0.0), ficheiroConfiguracion);
        }

        public static Font readFontProperty(String propiedad) {
            return new Font(propiedades.getProperty(propiedad).split("\\.")[0], stringToStyle(propiedades.getProperty(propiedad).split("\\.")[1].split(",")[0]), Integer.parseInt(propiedades.getProperty(propiedad).split("\\.")[1].split(",")[1]));
        }

        public static void writeFontProperty(String propiedad, Font f) {
            gravarEscribirPropiedade(propiedades, propiedad, f.getFamily() + "." + styleToString(f.getStyle()) + "," + f.getSize(), ficheiroConfiguracion);
        }

        public static Double readDoubleProperty(String propiedad) {
            return Double.parseDouble(propiedades.getProperty(propiedad));
        }

        public static void writeDoubleProperty(String propiedad, Double d) {
            gravarEscribirPropiedade(propiedades, propiedad, d.toString(), ficheiroConfiguracion);
        }

        private static String styleToString(int f) {
            switch (f) {
                case Font.BOLD | Font.ITALIC:
                    return "BOLD+ITALIC";
                case Font.BOLD:
                    return "BOLD";
                case Font.ITALIC:
                    return "ITALIC";
                default:
                    return "PLAIN";
            }
        }

        private static int stringToStyle(String s) {
            switch (s.toUpperCase()) {
                case "BOLD+ITALIC":
                    return Font.BOLD | Font.ITALIC;
                case "BOLD":
                    return Font.BOLD;
                case "ITALIC":
                    return Font.ITALIC;
                default:
                    return Font.PLAIN;
            }
        }

    }

    private static synchronized Properties lerPropiedades(String url) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            prop.load(url.charAt(0) != '/' ? new FileInputStream(url) : Vista.class.getResourceAsStream(url));
        } catch (IOException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return prop;
    }

    public void inicializar(Modelo modelo, boolean visualizar) {
        meuModelo = modelo;
        meuModelo.addObserver(this);
        crearControlador();
        meuControlador.inicializar(modelo, this);
        if (visualizar) {
            visualizar();
        }
    }

    @Override
    public Sesion obterSesion() {
        SesionVista s = new SesionVista();
        s.setOrdeVisualizacion(ordeVisualizacion);
        s.setScatterPlotsVisibles(scatterPlotsVisibles);
        s.setLonxitudeEstela(lonxitudeEstela);
        s.setPaso(paso);
        s.setCorEstela(corEstela);
        s.setDistanceFormula(distanceFormula);
        return s;
    }

    @Override
    public void aplicarSesion(Sesion sesion) throws Exception {
        SesionVista s = (SesionVista) sesion;
        scatterPlotsVisibles = s.getScatterPlotsVisibles();
        ordeVisualizacion = s.getOrdeVisualizacion();
        paso = s.getPaso();
        corEstela = s.getCorEstela();
        lonxitudeEstela = s.getLonxitudeEstela();
        distanceFormula = s.getDistanceFormula();
    }

    private void crearControlador() {
        meuControlador = new Controlador();
    }

    private void visualizar() {
        this.setVisible(true);
    }

    private void activarPanelReproduccion(boolean activar) {
        for (Component comp : jPanel8.getComponents()) {
            comp.setEnabled(activar);
        }
    }

    private class mouseAdapterPropertiesMenu extends MouseAdapter {

        private final Component c;

        public mouseAdapterPropertiesMenu(Component c) {
            this.c = c;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                ManexadorScatterPlots.createPropertiesPopupMenu().show(c, e.getX(), e.getY());
            }
        }
    }

    private void pintarMenuVisualizacion() {
        visualizacionDesactualizada = true;
        if (jTabbedPane1.getSelectedIndex() == 2) {
            final long startTime = System.currentTimeMillis();;
            activarPanelReproduccion(false);
            if (mansp != null) {
                mansp.pecharJFramesChartPanel();
                mansp.pause();
            }
            ComparableInstances instanciasFiltradas = meuModelo.getComparableInstancesFiltradas();
            ArrayList<Integer> indices = new ArrayList<>();
            for (int i = 0; i < instanciasFiltradas.numAttributes(); i++) {
                if (instanciasFiltradas.attribute(i).isNumeric()) {
                    indices.add(i);
                }
            }
            int numAtributosNumericos = indices.size();
            actualizarScatterPlotsVisibles(numAtributosNumericos);
            mansp = new ManexadorScatterPlots(this, instanciasFiltradas, meuModelo.getIndiceAtributoNominal(), meuModelo.getIndiceTemporal(), scatterPlotsVisibles, jSlider1, jTextField1, ordeVisualizacion, paso, lonxitudeEstela, corEstela);
            mansp.addPropertyChangeListener(this);
            jPopupMenu1.setVisible(false);
            jPanel4.removeAll();
            setScatterPlotsBackground();
            int numCols = mansp.numColumnasNonBaleiras(), numFilas = mansp.numFilasNonBaleiras();
            if (numCols * numFilas != 0) {
                int max = Math.max(numFilas, numCols);
                double cols[] = new double[numCols], filas[] = new double[numFilas];
                for (int i = 0; i < max; i++) {
                    if (i < numCols) {
                        cols[i] = TableLayout.FILL;
                    }
                    if (i < numFilas) {
                        filas[i] = TableLayout.FILL;
                    }
                }
                jPanel4.setLayout(new TableLayout(cols, filas));
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                jLabel3.setText(recursosIdioma.getString("procesando") + "...");
                new Thread(() -> {
                    task.reset();
                    task.setEnd(numCols * numFilas);
                    int filasVacias = 0;
                    for (int i = 0; i < numAtributosNumericos; i++) {
                        JScrollBar scrollVertical = jScrollPane2.getVerticalScrollBar();
                        scrollVertical.setValue(scrollVertical.getMaximum());
                        if (!mansp.filaScatterPlotsVacia(i)) {
                            int columnasVacias = 0;
                            for (int j = 0; j < numAtributosNumericos; j++) {
                                if (!mansp.columnaScatterPlotsVacia(j)) {
                                    mansp.cubrirConScatterPlot(i, j, indices, meuModelo.getIndiceAtributoNominal());
                                    ScatterPlot sp = mansp.getScatterPlot(i, j);
                                    int xPos = numFilas + filasVacias - 1 - i, yPos = j - columnasVacias;
                                    if (sp != null) {
                                        configureMatrixScatterPlot(sp, instanciasFiltradas);
                                        ChartPanel chartPanelCela = sp.getChartPanelCela();
                                        jPanel4.add(chartPanelCela, new TableLayoutConstraints(yPos, xPos));
                                        chartPanelCela.revalidate();
                                        chartPanelCela.repaint();
                                    } else {
                                        JLabel l = new JLabel();
                                        l.addMouseListener(new mouseAdapterPropertiesMenu(l));
                                        jPanel4.add(l, new TableLayoutConstraints(yPos, xPos));
                                    }
                                    task.acumulate(1);
                                } else {
                                    columnasVacias++;
                                }
                            }
                        } else {
                            filasVacias++;
                        }
                    }
                    setCursor(Cursor.getDefaultCursor());
                    jLabel3.setText("");
                    activarPanelReproduccion(true);
                    task.reset();
                    if (Controlador.debug) {
                        System.out.println("Menú Visualización pintado en " + (System.currentTimeMillis() - startTime) / 1000.0 + " segundos.");
                    }
                }).start();
            } else {
                jPanel4.setLayout(new GridBagLayout());
                JPanel p = new JPanel();
                p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
                JLabel l = new JLabel("<html><body align='center'>" + recursosIdioma.getString("matrizScatterplotsBaleira") + " " + recursosIdioma.getString("botonComezarVisualizacion") + ".</body></html>");
                l.setAlignmentX(Component.CENTER_ALIGNMENT);
                l.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
                p.add(l);
                JButton b = new JButton(recursosIdioma.getString("botonComezarVisualizacion"));
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.addActionListener((ActionEvent e) -> {
                    configurarJDialogEngadirOuEliminarScatterplots();
                });
                p.add(b);
                p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                jPanel4.add(p);
                jPanel4.addMouseListener(new mouseAdapterPropertiesMenu(jPanel4));
            }
            jLabel4.setText("/" + String.valueOf(instanciasFiltradas.numInstances()));
            jTextField1.setText(String.valueOf(instanciasFiltradas.numInstances()));
            visualizacionDesactualizada = false;
            jPanel4.revalidate();
            jPanel4.repaint();
        }
    }

    static void doEditProperties(Component c) {
        try {
            DefaultChartEditorConfigurable editor = (DefaultChartEditorConfigurable) ChartEditorManagerConfigurable.getDefaultChartEditorConfigurable();
            if (JOptionPane.showConfirmDialog(c, editor, recursosIdioma.getString("Chart_Properties"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                grabarEscribirConfiguracionGraficaScatterPlots(editor);
                mansp.aplicarConfiguracionGraficaScatterPlots();
                mansp.pause();
                mansp.goTo(1.0);
            }
        } catch (java.lang.ExceptionInInitializerError e) {
            Logger.getLogger(ManexadorScatterPlots.class.getName()).log(Level.SEVERE, null, e.getException());
        }
    }

    private Integer getColumnaModeloSeleccionada(JTableModelo jtm) {
        return jtm.columnaTaboaSeleccionada < 0 ? 0 : jtm.getColumnModel().getColumn(jtm.columnaTaboaSeleccionada).getModelIndex();
    }

    private int getColumnaModeloSeleccionada() {
        return jTableModelo.getColumnModel().getColumn(jTableModelo.columnaTaboaSeleccionada).getModelIndex();
    }

    private HashMap<String, Parameter> obterConfiguracionFiltro(int indiceFiltro) {
        Map<String, Parameter> configuracion = meuModelo.getFiltro(indiceFiltro).getParameters();
        HashMap<String, Parameter> resultado = new HashMap<>();
        int numP = configuracion.size();
        JLabel[] jLabels = new JLabel[numP];
        JComponent[] jComponents = new JComponent[numP];
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Iterator<Map.Entry<String, Parameter>> it = configuracion.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<String, Parameter> pair = it.next();
            Parameter p = pair.getValue();
            jLabels[i] = new JLabel(pair.getKey() + ": ");
            if (p instanceof DoubleParameter) {
                DoubleParameter dp = (DoubleParameter) p;
                jComponents[i] = new JTextField(dp.getValue() != null ? Double.toString(dp.getValue()) : "", 5);
            } else if (p instanceof StringParameter) {
                StringParameter sp = (StringParameter) p;
                JComboBox jcb = new JComboBox<>(new DefaultComboBoxModel<>(ArrayUtils.addAll(new String[]{null}, sp.getOptions())));
                jcb.setSelectedItem(sp.getValue());
                jComponents[i] = jcb;
            }
            jPanel.add(jLabels[i]);
            jPanel.add(jComponents[i]);
            jPanel.add(Box.createHorizontalStrut(15));
            i++;
        }
        jPanel.add(new JLabel(recursosIdioma.getString("atributo") + " :"));
        JComboBox jcb = new JComboBox<>(new DefaultComboBoxModel<>(ArrayUtils.addAll(new String[]{null}, meuModelo.getNomesAtributos())));
        jcb.setSelectedItem(meuModelo.getFiltro(indiceFiltro).getIndiceAtributoFiltrado() != null ? meuModelo.getComparableInstances().attribute(meuModelo.getFiltro(indiceFiltro).getIndiceAtributoFiltrado()).name() : null);
        jPanel.add(jcb);
        int result = JOptionPane.showConfirmDialog(this, jPanel, recursosIdioma.getString("configuracionFiltro") + " - " + meuModelo.getFiltro(indiceFiltro).toString(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            it = configuracion.entrySet().iterator();
            i = 0;
            while (it.hasNext()) {
                Map.Entry<String, Parameter> pair = it.next();
                Parameter p = pair.getValue();
                Parameter newParameter = null;
                try {
                    if (p instanceof DoubleParameter) {
                        DoubleParameter dp = (DoubleParameter) p;
                        newParameter = new DoubleParameter(dp.getValue(), dp.getMax(), dp.getMin());
                        String t = ((JTextField) jComponents[i]).getText();
                        newParameter.setValue(!"".equals(t) ? Double.parseDouble(t) : null);
                    } else if (p instanceof StringParameter) {
                        StringParameter sp = (StringParameter) p;
                        newParameter = new StringParameter(sp.getOptions());
                        newParameter.setValue((String) ((JComboBox) jComponents[i]).getSelectedItem());
                    }
                    resultado.put(pair.getKey(), newParameter);
                } catch (NumberFormatException e) {
                }
                i++;
            }
            IntegerParameter indiceAtributo = new IntegerParameter();
            indiceAtributo.setValue(jcb.getSelectedItem() != null ? meuModelo.getIndiceAtributo((String) jcb.getSelectedItem()) : null);
            resultado.put("indiceAtributo", indiceAtributo);
        } else {
            return null;
        }
        return resultado;
    }

    protected Controlador getControlador() {
        return meuControlador;
    }

    void setScatterPlotsBackground() {
        jPanel4.setBackground(GraphicConfigurationManager.readColorProperty("chart_background_paint"));
    }

    private static void grabarEscribirConfiguracionGraficaScatterPlots(DefaultChartEditorConfigurable editor) {
        GraphicConfigurationManager.writeColorProperty("chart_background_paint", (Color) editor.getBackgroundPaint());
        GraphicConfigurationManager.writeColorProperty("scatterplot_background_paint", (Color) editor.getPlotEditor().getBackgroundPaint());
        GraphicConfigurationManager.writeColorProperty("outline_paint", (Color) editor.getPlotEditor().getOutlinePaint());
        GraphicConfigurationManager.writeStrokeProperty("outline_stroke", editor.getPlotEditor().getOutlineStroke());
        GraphicConfigurationManager.writeColorProperty("title_paint", (Color) editor.getTitleEditor().getTitlePaint());
        GraphicConfigurationManager.writeColorProperty("domain_axis_paint", (Color) editor.getPlotEditor().getDomainAxisPropertyEditPanel().getLabelPaint());
        GraphicConfigurationManager.writeColorProperty("range_axis_paint", (Color) editor.getPlotEditor().getRangeAxisPropertyEditPanel().getLabelPaint());
        GraphicConfigurationManager.writeFontProperty("domain_axis_font", editor.getPlotEditor().getDomainAxisPropertyEditPanel().getLabelFont());
        GraphicConfigurationManager.writeFontProperty("range_axis_font", editor.getPlotEditor().getRangeAxisPropertyEditPanel().getLabelFont());
        GraphicConfigurationManager.writeBooleanProperty("anti-aliased", editor.getAntiAlias());
        GraphicConfigurationManager.writeFontProperty("title_font", editor.getTitleEditor().getTitleFont());
        GraphicConfigurationManager.writeBooleanProperty("range_tick_labels", editor.getPlotEditor().getRangeAxisPropertyEditPanel().isTickLabelsVisible());
        GraphicConfigurationManager.writeBooleanProperty("range_tick_marks", editor.getPlotEditor().getRangeAxisPropertyEditPanel().isTickMarksVisible());
        GraphicConfigurationManager.writeBooleanProperty("domain_tick_labels", editor.getPlotEditor().getDomainAxisPropertyEditPanel().isTickLabelsVisible());
        GraphicConfigurationManager.writeBooleanProperty("domain_tick_marks", editor.getPlotEditor().getDomainAxisPropertyEditPanel().isTickMarksVisible());
        GraphicConfigurationManager.writeFontProperty("domain_tick_labels_font", editor.getPlotEditor().getDomainAxisPropertyEditPanel().getTickLabelFont());
        GraphicConfigurationManager.writeFontProperty("range_tick_labels_font", editor.getPlotEditor().getRangeAxisPropertyEditPanel().getTickLabelFont());
        GraphicConfigurationManager.writeListColorProperty("series_paint", editor.getSeriesPaint());
        GraphicConfigurationManager.writeListShapeProperty("series_shape", editor.getSeriesShape());
    }

    private class TarefaProgreso extends SwingWorker<Void, Void> {

        private int end;
        private int acumulated;
        private final JProgressBar jProgressBar;

        public void setEnd(int end) {
            this.end = end;
        }

        public int getEnd() {
            return end;
        }

        public int getAcumulated() {
            return acumulated;
        }

        public void reset() {
            acumulated = 0;
            try {
                doInBackground();
            } catch (Exception ex) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public TarefaProgreso(JProgressBar jProgressBar) {
            super();
            this.jProgressBar = jProgressBar;
            this.end = 0;
            this.acumulated = 0;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int value = (int) (end != 0 ? Math.round(1.0 * jProgressBar.getMaximum() * acumulated / end) : 0);
            jProgressBar.setValue(value);
            return null;
        }

        public void acumulate(int cantidade) {
            acumulated += cantidade;
            try {
                doInBackground();
            } catch (Exception ex) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void configureMatrixScatterPlot(ScatterPlot sp, ComparableInstances instances) {
        configureScatterPlot(sp, instances);
        ChartPanel chartPanelCela = sp.getChartPanelCela();
        chartPanelCela.setLayout(new GridBagLayout());
        JButton botonAmpliar = new JButton();
        final JFrameChartPanel jfa = sp.getJFrameAmpliado();
        botonAmpliar.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jfa.isVisible()) {
                    jfa.setLocation(getX() + 20 * (mansp.contarJFramesVisibles() + 1), getY() + 20 * (mansp.contarJFramesVisibles() + 1));
                    jfa.setVisible(true);
                } else {
                    jfa.toFront();
                }
            }
        });
        botonAmpliar.setOpaque(false);
        botonAmpliar.setContentAreaFilled(false);
        botonAmpliar.setBorderPainted(false);
        botonAmpliar.setFocusPainted(false);
        botonAmpliar.setIcon(new ImageIcon(getClass().getResource("imaxes/ampliar.png")));
        botonAmpliar.setPreferredSize(new Dimension(19, 19));
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.weightx = 1;
        c.weighty = 1;
        chartPanelCela.add(botonAmpliar, c);
        sp.pintarEstela();
    }

    private static void configureChartPanel(ChartPanel chartpanel, int width, int height) {
        chartpanel.setMouseWheelEnabled(true);
        chartpanel.setPreferredSize(new Dimension(width, height));
        Plot pl = chartpanel.getChart().getPlot();
        if (pl instanceof XYPlot) {
            XYPlot xpl = (XYPlot) pl;
            xpl.setDomainPannable(true);
            xpl.setRangePannable(true);
        }
    }

    private void configureScatterPlot(ScatterPlot sp, ComparableInstances instances) {
        int indiceAtributoX = sp.getIndiceAtributoX(), indiceAtributoY = sp.getIndiceAtributoY();
        for (ChartPanel chartpanel : new ChartPanel[]{sp.getChartPanelCela(), sp.getJFrameAmpliado().getChartPanel()}) {
            configureChartPanel(chartpanel, 340, 210);
            chartpanel.addChartMouseListener(new ChartMouseListener() {
                @Override
                public void chartMouseClicked(final ChartMouseEvent event) {
                    java.awt.EventQueue.invokeLater(() -> {
                        if (mansp.getEstado() != ManexadorScatterPlots.PLAY && SwingUtilities.isLeftMouseButton(event.getTrigger()) && event.getTrigger().getClickCount() == 1) {
                            XYPlot xyp = event.getChart().getXYPlot();
                            double domainCrosshairValue = xyp.getDomainCrosshairValue();
                            double rangeCrosshairValue = xyp.getRangeCrosshairValue();
                            ArrayList<Integer> indicesInstancesPuntos = new ArrayList<>();
                            Enumeration e = instances.enumerateInstances();
                            for (int i = 0; e.hasMoreElements(); i++) {
                                Instance di = (Instance) e.nextElement();
                                if (di.value(indiceAtributoX) == domainCrosshairValue && di.value(indiceAtributoY) == rangeCrosshairValue) {
                                    indicesInstancesPuntos.add(i);
                                }
                            }
                            if (!indicesInstancesPuntos.isEmpty()) {
                                jPopupMenu1.removeAll();
                                jPopupMenu1.setLayout(new BoxLayout(jPopupMenu1, BoxLayout.X_AXIS));
                                for (int i = 0; i < indicesInstancesPuntos.size(); i++) {
                                    int p = indicesInstancesPuntos.get(i);
                                    if (mansp.getMsInstances()[p] <= mansp.getTActual()) {
                                        if (i != 0) {
                                            jPopupMenu1.add(new JSeparator(SwingConstants.VERTICAL));
                                        }
                                        JPanel pa = new JPanel();
                                        pa.setBorder(new LineBorder(ManexadorScatterPlots.obterCoresHSB(i, indicesInstancesPuntos.size()), 2));
                                        pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
                                        Enumeration en = instances.enumerateAttributes();
                                        while (en.hasMoreElements()) {
                                            Attribute a = (Attribute) en.nextElement();
                                            boolean represented = a.index() == indiceAtributoX || a.index() == indiceAtributoY;
                                            pa.add(new JLabel((represented ? "<html><strong>" : "") + a.name() + ": " + (a.type() == Attribute.NUMERIC ? Double.compare(instances.instance(p).value(a), Double.NaN) != 0 ? instances.instance(p).value(a) : "?" : instances.instance(p).stringValue(a)) + (represented ? "</strong></html>" : "")));
                                        }
                                        if (buscaPunto != null) {
                                            pa.addMouseListener(new MouseAdapter() {
                                                @Override
                                                public void mouseClicked(MouseEvent e) {
                                                    switch (buscaPunto) {
                                                        case 'A':
                                                            puntoA = instances.instance(p);
                                                            jLabel11.setText(instances.instance(p).toString());
                                                            break;
                                                        case 'B':
                                                            puntoB = instances.instance(p);
                                                            jLabel13.setText(instances.instance(p).toString());
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    buscaPunto = null;
                                                    jPopupMenu1.setVisible(false);
                                                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                                    jFrame1.toFront();
                                                }
                                            });
                                            jPopupMenu1.setCursor(new Cursor(Cursor.HAND_CURSOR));
                                        } else {
                                            jPopupMenu1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                        }
                                        jPopupMenu1.add(pa);
                                    }
                                }
                                jPopupMenu1.show(event.getTrigger().getComponent(), event.getTrigger().getX(), event.getTrigger().getY());
                                mansp.procesarSeleccion(instances, indicesInstancesPuntos);
                            }
                        }
                    });
                }

                @Override
                public void chartMouseMoved(ChartMouseEvent event) {
                }
            });
            chartpanel.addMouseWheelListener((MouseWheelEvent e) -> {
                if (buscaPunto == null) {
                    jPopupMenu1.setVisible(false);
                }
            });
            chartpanel.addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    jPopupMenu1.setVisible(false);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    jPopupMenu1.setVisible(false);
                }
            });
        }
    }

    private void actualizarScatterPlotsVisibles(int numAtributosNumericos) {
        if (numAtributosNumericos > scatterPlotsVisibles.length) {
            boolean temp[][] = new boolean[numAtributosNumericos][numAtributosNumericos];
            for (boolean[] row : temp) {
                Arrays.fill(row, false);
            }
            for (int i = 0; i < scatterPlotsVisibles.length; i++) {
                System.arraycopy(scatterPlotsVisibles[i], 0, temp[i], 0, scatterPlotsVisibles[i].length);
            }
            scatterPlotsVisibles = temp;
        } else if (numAtributosNumericos < scatterPlotsVisibles.length) {
            boolean temp[][] = new boolean[numAtributosNumericos][numAtributosNumericos];
            for (boolean[] row : temp) {
                Arrays.fill(row, false);
            }
            for (int i = 0; i < numAtributosNumericos; i++) {
                System.arraycopy(scatterPlotsVisibles[i], 0, temp[i], 0, numAtributosNumericos);
            }
            scatterPlotsVisibles = temp;
        }
    }

    private void pintarMenuModelo() {
        if (jTableModelo == null) {
            inicializarPaneis();
        } else {
            actualizarPaneis();
        }
        jLabel2.setText(meuModelo.getComparableInstances().relationName());
    }

    private static class ClassEnumerator {

        private static void processDirectory(File directory, String pkgname, ArrayList<String> classes) {
            String[] files = directory.list();
            for (String fileName : files) {
                String className = null;
                if (fileName.endsWith(".class")) {
                    className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
                }
                if (className != null) {
                    classes.add(className);
                }
                File subdir = new File(directory, fileName);
                if (subdir.isDirectory()) {
                    processDirectory(subdir, pkgname + '.' + fileName, classes);
                }
            }
        }

        private static ArrayList<String> getClassesForPackage(Package pkg) {
            ArrayList<String> classes = new ArrayList<>();
            String pkgname = pkg.getName();
            String relPath = pkgname.replace('.', '/');
            processDirectory(new File("build\\classes\\" + relPath), pkgname, classes);
            return classes;
        }
    }

    private void pintarMenuFiltros() {
        try {
            jFrameModeloParcial.dispose();
            jPanel3.removeAll();
            jList1.setModel(new DefaultListModel<>());
            DefaultListModel<IFilter> dlm = (DefaultListModel<IFilter>) jList1.getModel();
            ArrayList<String> discoveredClasses = ClassEnumerator.getClassesForPackage(FilterHandler.class.getPackage());
            for (String line : discoveredClasses) {
                if (!line.isEmpty()) {
                    Class<?> c = Class.forName(line);
                    if (!c.equals(FilterHandler.class)) {
                        Object object = null;
                        try {
                            object = c.getDeclaredConstructor(ComparableInstances.class).newInstance(meuModelo.getComparableInstances());
                        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            try {
                                object = c.getDeclaredConstructor().newInstance();
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex1) {
                                amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
                                if (Controlador.debug) {
                                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex1);
                                }
                            }
                        }
                        if (object instanceof IFilter) {
                            IFilter filtro = (IFilter) object;
                            String nomeFiltro = filtro.toString();
                            boolean valido = true;
                            for (int i = 0; i < dlm.getSize(); i++) {
                                if (nomeFiltro.equals(dlm.get(i).toString())) {
                                    valido = false;
                                    break;
                                }
                            }
                            if (valido) {
                                dlm.addElement(filtro);
                            }
                        }
                    }
                }
            }
            File[] listOfFiles = new File("filters").listFiles();
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile() && listOfFile.getName().endsWith(".jar")) {
                    JarClassLoader jarLoader = new JarClassLoader(listOfFile.getPath());
                    JarFile jarFile = new JarFile(listOfFile.getPath());
                    Enumeration ee = jarFile.entries();
                    while (ee.hasMoreElements()) {
                        JarEntry entry = (JarEntry) ee.nextElement();
                        String name = entry.getName();
                        if (name.endsWith(".class")) {
                            Class c = jarLoader.loadClass(name.substring(0, name.lastIndexOf('.')).replaceAll("/", "."), true);
                            Object o = c.newInstance();
                            if (o instanceof IFilter) {
                                IFilter filtro = (IFilter) o;
                                boolean valido = true;
                                for (int i = 0; i < dlm.getSize(); i++) {
                                    if (filtro.toString().equals(dlm.get(i).toString())) {
                                        valido = false;
                                        break;
                                    }
                                }
                                if (valido) {
                                    dlm.addElement(filtro);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IOException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        }
        int gap = 8, padding = 16;
        int n = meuModelo.contarFiltros();
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalStrut(padding));
        horizontalBox.add(Box.createGlue());
        JButton b = new JButton();
        b.addActionListener((ActionEvent e) -> {
            JTableModelo jtm = new JTableModelo(jPanel12, jScrollPane13, meuModelo, (JPanelActualizable) panelDetallarAtributo, false, 0, filler4);
            jtm.inicializar();
            jFrameModeloParcial.setTitle(meuModelo.getComparableInstances().relationName() + " - " + recursosIdioma.getString("modeloInicial"));
            jFrameModeloParcial.pack();
            jFrameModeloParcial.setVisible(true);
        });
        b.setIcon(new ImageIcon(getClass().getResource("imaxes/instanciasOrixinais.jpg")));
        horizontalBox.add(b);
        for (int i = 0; i < n; i++) {
            horizontalBox.add(Box.createHorizontalStrut(gap));
            if (i > 0) {
                JPanel p = new JPanel();
                p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
                JLabel l = new JLabel();
                l.setIcon(new ImageIcon(getClass().getResource("imaxes/frechaFiltros.png")));
                p.add(l);
                b = new JButton();
                final int a = i;
                b.addActionListener((ActionEvent e) -> {
                    ArrayList<FilterHandler> filtrosParciais = new ArrayList<>();
                    for (int j = 0; j < a; j++) {
                        filtrosParciais.add(meuModelo.getFiltro(j));
                    }
                    Modelo modeloParcial = new Modelo(meuModelo);
                    modeloParcial.setFiltros(filtrosParciais);
                    modeloParcial.setComparableInstances(modeloParcial.getComparableInstancesFiltradas());
                    JTableModelo jtm = new JTableModelo(jPanel12, jScrollPane13, modeloParcial, (JPanelActualizable) panelDetallarAtributo, false, 0, filler4);
                    jtm.inicializar();
                    jFrameModeloParcial.setTitle(meuModelo.getComparableInstances().relationName() + " - " + recursosIdioma.getString("modeloParcial") + " (" + a + "/" + n + ")");
                    jFrameModeloParcial.pack();
                    jFrameModeloParcial.setVisible(true);
                });
                b.setPreferredSize(new Dimension(30, 20));
                b.setIcon(imageToIcon(new ImageIcon(getClass().getResource("imaxes/instanciasFiltradas.jpg")).getImage().getScaledInstance(b.getPreferredSize().width, b.getPreferredSize().height, Image.SCALE_SMOOTH)));
                p.add(b);
                horizontalBox.add(p);
            } else {
                JLabel l = new JLabel();
                l.setIcon(new ImageIcon(getClass().getResource("imaxes/frechaFiltros.png")));
                horizontalBox.add(l);
            }
            horizontalBox.add(Box.createHorizontalStrut(gap));
            FilterHandler f = meuModelo.getFiltro(i);
            PanelFiltro pf = new PanelFiltro(i);
            if (!f.estaTodoConfigurado()) {
                pf.setIcon("/jdatamotion/imaxes/filtroAviso.png");
                pf.setToolTipText(recursosIdioma.getString("filtroAviso"));
            }
            pf.setNomeFiltro(f.toString());
            pf.setFiltroSeleccionado(f.isSeleccionado());
            pf.jCheckBox1.addActionListener((ActionEvent e) -> {
                f.setSeleccionado(pf.jCheckBox1.isSelected());
                jMenuItem24.setEnabled(false);
                for (int i1 = 0; i1 < n; i1++) {
                    if (meuModelo.getFiltro(i1).isSeleccionado()) {
                        jMenuItem24.setEnabled(true);
                        break;
                    }
                }
            });
            Iterator<Map.Entry<String, Parameter>> it = f.getParameters().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Parameter> pair = it.next();
                pf.addParameter(pair.getKey(), pair.getValue());
            }
            jMenuItem24.setEnabled(false);
            for (int i1 = 0; i1 < n; i1++) {
                if (meuModelo.getFiltro(i1).isSeleccionado()) {
                    jMenuItem24.setEnabled(true);
                    break;
                }
            }
            pf.addAtributoFiltrado(f.getIndiceAtributoFiltrado() != null ? meuModelo.getComparableInstances().attribute(f.getIndiceAtributoFiltrado()) : null);
            horizontalBox.add(pf);
            if (i == n - 1) {
                horizontalBox.add(Box.createHorizontalStrut(gap));
                JLabel l = new JLabel();
                l.setIcon(new ImageIcon(getClass().getResource("imaxes/frechaFiltros.png")));
                horizontalBox.add(l);
                horizontalBox.add(Box.createHorizontalStrut(gap));
                b = new JButton();
                b.addActionListener((ActionEvent e) -> {
                    Modelo modeloParcial = new Modelo(meuModelo);
                    modeloParcial.setComparableInstances(modeloParcial.getComparableInstancesFiltradas());
                    JTableModelo jtm = new JTableModelo(jPanel12, jScrollPane13, modeloParcial, (JPanelActualizable) panelDetallarAtributo, false, 0, filler4);
                    jtm.inicializar();
                    jFrameModeloParcial.setTitle(meuModelo.getComparableInstances().relationName() + " - " + recursosIdioma.getString("modeloFinal"));
                    jFrameModeloParcial.pack();
                    jFrameModeloParcial.setVisible(true);
                });
                b.setIcon(new ImageIcon(getClass().getResource("imaxes/instanciasFiltradas.jpg")));
                horizontalBox.add(b);
            }
        }
        horizontalBox.add(Box.createGlue());
        horizontalBox.add(Box.createHorizontalStrut(padding));
        jPanel3.add(horizontalBox);
    }

    private void pintarMenus() {
        long startTime = 0;
        if (Controlador.debug) {
            startTime = System.currentTimeMillis();
        }
        pintarMenuModelo();
        if (Controlador.debug) {
            System.out.println("Menú Modelo pintado en " + (System.currentTimeMillis() - startTime) / 1000.0 + " segundos.");
            startTime = System.currentTimeMillis();
        }
        pintarMenuFiltros();
        if (Controlador.debug) {
            System.out.println("Menú Filtros pintado en " + (System.currentTimeMillis() - startTime) / 1000.0 + " segundos.");
        }
        pintarMenuVisualizacion();
    }

    private Icon imageToIcon(Image image) {
        ImageIcon imgIcon = new ImageIcon(image);
        Icon iconReturn = (Icon) imgIcon;
        return iconReturn;
    }

    private void actualizarPilas() {
        if (jTabbedPane1.isEnabled()) {
            if (!meuControlador.getXestorComandos().pilaDesfacerVacia()) {
                jMenuItem7.setEnabled(true);
                jMenuItem7.setText(recursosIdioma.getString("Vista.jMenuItem7.text") + " " + meuControlador.getXestorComandos().getPilaDesfacer().peek().getNome());
            } else {
                jMenuItem7.setEnabled(false);
                jMenuItem7.setText(recursosIdioma.getString("Vista.jMenuItem7.text"));
            }
            if (!meuControlador.getXestorComandos().pilaRefacerVacia()) {
                jMenuItem8.setEnabled(true);
                jMenuItem8.setText(recursosIdioma.getString("Vista.jMenuItem8.text") + " " + meuControlador.getXestorComandos().getPilaRefacer().peek().getNome());
            } else {
                jMenuItem8.setEnabled(false);
                jMenuItem8.setText(recursosIdioma.getString("Vista.jMenuItem8.text"));
            }
        }
    }

    @Override
    public void update(final Observable o, Object arg) {
        if (!jTabbedPane1.isEnabled() && o != null && !((Modelo) o).getComparableInstances().isEmpty()) {
            jMenuItem2.setEnabled(true);
            jMenuItem4.setEnabled(true);
            jMenuItem10.setEnabled(true);
            jMenuItem13.setEnabled(true);
            jMenuItem15.setEnabled(true);
            jMenuItem16.setEnabled(true);
            jMenuItem17.setEnabled(true);
            jMenuItem18.setEnabled(true);
            jMenuItem21.setEnabled(true);
            jMenuItem22.setEnabled(true);
            jMenuItem23.setEnabled(true);
            jMenuItem25.setEnabled(true);
            jMenuItem26.setEnabled(true);
            jMenu4.setEnabled(true);
            jTabbedPane1.setEnabled(true);
            jTabbedPane1.setSelectedIndex(0);
        }
        if (jTabbedPane1.isEnabled()) {
            pintarMenus();
            SwingUtilities.invokeLater(() -> {
                actualizarPilas();
                revalidate();
                repaint();
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jFileChooser1 = new javax.swing.JFileChooser();
        popupConfigurarAtributo = new javax.swing.JPopupMenu();
        menuTipo = new javax.swing.JMenu();
        botonNumerico = new javax.swing.JRadioButtonMenuItem();
        botonNominal = new javax.swing.JRadioButtonMenuItem();
        botonString = new javax.swing.JRadioButtonMenuItem();
        botonData = new javax.swing.JRadioButtonMenuItem();
        botonIndiceTemporal = new javax.swing.JRadioButtonMenuItem();
        botonOcultarColumna = new javax.swing.JRadioButtonMenuItem();
        jDialog2 = new javax.swing.JDialog();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jDialog3 = new javax.swing.JDialog();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jDialog4 = new javax.swing.JDialog();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jFrameModeloParcial = new javax.swing.JFrame();
        panelDetallarAtributo = new JPanelActualizable();
        jScrollPane13 = new javax.swing.JScrollPane();
        jScrollPane14 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 25), new java.awt.Dimension(0, 25), new java.awt.Dimension(0, 25));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 18));
        jFrame1 = new javax.swing.JFrame();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel8 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel15 = new JPanelActualizable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane5.getVerticalScrollBar().setModel(jScrollPane3.getVerticalScrollBar().getModel());
        jPanel10 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 25), new java.awt.Dimension(0, 25), new java.awt.Dimension(0, 25));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 18));
        jPanel2 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jButton14 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        jDialog1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog1.setModal(true);
        jDialog1.setName("jDialog1"); // NOI18N

        jFileChooser1.setName("jFileChooser1"); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 582, Short.MAX_VALUE)
            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 397, Short.MAX_VALUE)
            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        jDialog1.pack();

        popupConfigurarAtributo.setName("popupConfigurarAtributo"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jdatamotion/idiomas/Bundle"); // NOI18N
        menuTipo.setText(bundle.getString("Vista.menuTipo.text")); // NOI18N
        menuTipo.setName("menuTipo"); // NOI18N

        botonNumerico.setText(bundle.getString("numérico")); // NOI18N
        botonNumerico.setName("botonNumerico"); // NOI18N
        botonNumerico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNumericoActionPerformed(evt);
            }
        });
        menuTipo.add(botonNumerico);

        botonNominal.setText(bundle.getString("nominal")); // NOI18N
        botonNominal.setName("botonNominal"); // NOI18N
        botonNominal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNominalActionPerformed(evt);
            }
        });
        menuTipo.add(botonNominal);

        botonString.setText(bundle.getString("string")); // NOI18N
        botonString.setName("botonString"); // NOI18N
        botonString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonStringActionPerformed(evt);
            }
        });
        menuTipo.add(botonString);

        botonData.setText(bundle.getString("data")); // NOI18N
        botonData.setName("botonData"); // NOI18N
        botonData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDataActionPerformed(evt);
            }
        });
        menuTipo.add(botonData);

        popupConfigurarAtributo.add(menuTipo);

        botonIndiceTemporal.setText(bundle.getString("Vista.botonIndiceTemporal.text")); // NOI18N
        botonIndiceTemporal.setName("botonIndiceTemporal"); // NOI18N
        botonIndiceTemporal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonIndiceTemporalActionPerformed(evt);
            }
        });
        popupConfigurarAtributo.add(botonIndiceTemporal);

        botonOcultarColumna.setText(bundle.getString("Vista.botonOcultarColumna.text")); // NOI18N
        botonOcultarColumna.setName("botonOcultarColumna"); // NOI18N
        botonOcultarColumna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOcultarColumnaActionPerformed(evt);
            }
        });
        popupConfigurarAtributo.add(botonOcultarColumna);

        jDialog2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog2.setTitle(bundle.getString("Vista.jMenuItem16.text")); // NOI18N
        jDialog2.setMinimumSize(new java.awt.Dimension(500, 500));
        jDialog2.setModal(true);
        jDialog2.setName("jDialog2"); // NOI18N
        jDialog2.setPreferredSize(new java.awt.Dimension(500, 500));

        jButton1.setText(bundle.getString("Aceptar")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setText(bundle.getString("Cancelar")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jPanel5.setMaximumSize(null);
        jPanel5.setName("jPanel5"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel5);

        jScrollPane8.getHorizontalScrollBar().setModel(jScrollPane1.getHorizontalScrollBar().getModel());
        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane8.setName("jScrollPane8"); // NOI18N

        jPanel7.setMaximumSize(null);
        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setLayout(new java.awt.GridLayout(1, 0, 2, 2));
        jScrollPane8.setViewportView(jPanel7);

        jScrollPane9.getVerticalScrollBar().setModel(jScrollPane1.getVerticalScrollBar().getModel());
        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane9.setName("jScrollPane9"); // NOI18N

        jPanel6.setMaximumSize(null);
        jPanel6.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.GridLayout(0, 1, 2, 2));
        jScrollPane9.setViewportView(jPanel6);

        jButton3.setText(bundle.getString("Vista.jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        jButton4.setText(bundle.getString("Vista.jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jDialog2Layout.createSequentialGroup()
                                .addComponent(jScrollPane8)
                                .addGap(17, 17, 17))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)))))
                .addContainerGap())
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addComponent(jScrollPane9)
                        .addGap(17, 17, 17))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(6, 6, 6)))
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jDialog3.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog3.setTitle(bundle.getString("Vista.jMenuItem17.text")); // NOI18N
        jDialog3.setModal(true);
        jDialog3.setName("jDialog3"); // NOI18N

        jComboBox1.setName("jComboBox1"); // NOI18N

        jButton5.setText(bundle.getString("Cancelar")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText(bundle.getString("Aceptar")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap())
        );

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        jDialog4.setTitle(bundle.getString("Vista.jMenuItem22.text")); // NOI18N
        jDialog4.setName("jDialog4"); // NOI18N

        buttonGroup2.add(jRadioButton2);
        jRadioButton2.setText(bundle.getString("Vista.jRadioButton2.text")); // NOI18N
        jRadioButton2.setName("jRadioButton2"); // NOI18N

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText(bundle.getString("Vista.jRadioButton3.text")); // NOI18N
        jRadioButton3.setName("jRadioButton3"); // NOI18N

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText(bundle.getString("Vista.jRadioButton4.text")); // NOI18N
        jRadioButton4.setName("jRadioButton4"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel5.setText(bundle.getString("Vista.jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(bundle.getString("Vista.jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jButton7.setText(bundle.getString("Aceptar")); // NOI18N
        jButton7.setName("jButton7"); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton13.setText(bundle.getString("Cancelar")); // NOI18N
        jButton13.setName("jButton13"); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel7.setText(bundle.getString("Vista.jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jTextField3.setName("jTextField3"); // NOI18N

        jLabel14.setText(bundle.getString("Vista.jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setOpaque(true);
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog4Layout.createSequentialGroup()
                        .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialog4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13))
                            .addGroup(jDialog4Layout.createSequentialGroup()
                                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton2)
                                    .addComponent(jRadioButton3)
                                    .addComponent(jRadioButton4)
                                    .addGroup(jDialog4Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel6))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDialog4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDialog4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton13))
                .addContainerGap())
        );

        jFrameModeloParcial.setLocationByPlatform(true);
        jFrameModeloParcial.setMinimumSize(new java.awt.Dimension(600, 350));
        jFrameModeloParcial.setName("jFrameModeloParcial"); // NOI18N

        panelDetallarAtributo.setName("panelDetallarAtributo"); // NOI18N
        panelDetallarAtributo.setLayout(new javax.swing.BoxLayout(panelDetallarAtributo, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane13.setName("jScrollPane13"); // NOI18N

        jScrollPane14.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane14.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane14.setHorizontalScrollBar(null);
        jScrollPane14.setName("jScrollPane14"); // NOI18N
        jScrollPane14.getVerticalScrollBar().setModel(jScrollPane13.getVerticalScrollBar().getModel());

        jPanel12.setMinimumSize(new java.awt.Dimension(250, 0));
        jPanel12.setName("jPanel12"); // NOI18N
        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane14.setViewportView(jPanel12);

        filler3.setName("filler3"); // NOI18N

        filler4.setName("filler4"); // NOI18N

        javax.swing.GroupLayout jFrameModeloParcialLayout = new javax.swing.GroupLayout(jFrameModeloParcial.getContentPane());
        jFrameModeloParcial.getContentPane().setLayout(jFrameModeloParcialLayout);
        jFrameModeloParcialLayout.setHorizontalGroup(
            jFrameModeloParcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameModeloParcialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameModeloParcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDetallarAtributo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jFrameModeloParcialLayout.setVerticalGroup(
            jFrameModeloParcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameModeloParcialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameModeloParcialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13)
                    .addGroup(jFrameModeloParcialLayout.createSequentialGroup()
                        .addComponent(panelDetallarAtributo, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameModeloParcialLayout.createSequentialGroup()
                        .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(filler4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFrame1.setTitle(bundle.getString("Vista.jMenuItem25.text")); // NOI18N
        jFrame1.setName("jFrame1"); // NOI18N
        jFrame1.setResizable(false);
        jFrame1.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                jFrame1WindowClosed(evt);
            }
        });

        jLabel8.setText(bundle.getString("Vista.jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel12.setText(bundle.getString("Vista.jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jButton16.setText(bundle.getString("Cancelar")); // NOI18N
        jButton16.setName("jButton16"); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel11.setName("jLabel11"); // NOI18N

        jTextField6.setName("jTextField6"); // NOI18N

        jButton15.setText(bundle.getString("Aceptar")); // NOI18N
        jButton15.setName("jButton15"); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton18.setText(bundle.getString("definir")); // NOI18N
        jButton18.setName("jButton18"); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton17.setText(bundle.getString("definir")); // NOI18N
        jButton17.setName("jButton17"); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel13.setName("jLabel13"); // NOI18N

        jLabel10.setText(bundle.getString("Vista.jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton18))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton17)))
                .addContainerGap())
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton17)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jButton18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jButton16))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JDataMotion");
        setIconImage(new ImageIcon(getClass().getResource("imaxes/favicon.png")).getImage());
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(600, 300));
        setName("Form"); // NOI18N

        jProgressBar1.setName("jProgressBar1"); // NOI18N

        jLabel3.setName("jLabel3"); // NOI18N

        jLayeredPane1.setName("jLayeredPane1"); // NOI18N
        jLayeredPane1.setLayout(new javax.swing.OverlayLayout(jLayeredPane1));

        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setOpaque(false);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/play.png"))); // NOI18N
        jButton8.setToolTipText(bundle.getString("Vista.jButton8.toolTipText")); // NOI18N
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jSlider1.setValue(100);
        jSlider1.setName("jSlider1"); // NOI18N
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jSlider1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });
        jSlider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(pulsarSlider && e.getSource() instanceof JSlider) {
                    JSlider s=(JSlider)e.getSource();
                    mansp.goTo(1.0*s.getValue()/s.getMaximum());
                }
            }
        });

        jSlider1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                double percent = p.x / ((double) jSlider1.getWidth());
                int range = jSlider1.getMaximum() - jSlider1.getMinimum();
                double newVal = range * percent;
                int result = (int)(jSlider1.getMinimum() + newVal);
                jSlider1.setValue(result);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/goToEnd.png"))); // NOI18N
        jButton9.setToolTipText(bundle.getString("Vista.jButton9.toolTipText")); // NOI18N
        jButton9.setName("jButton9"); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/goToStart.png"))); // NOI18N
        jButton10.setToolTipText(bundle.getString("Vista.jButton10.toolTipText")); // NOI18N
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/stepForward.png"))); // NOI18N
        jButton11.setToolTipText(bundle.getString("Vista.jButton11.toolTipText")); // NOI18N
        jButton11.setName("jButton11"); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/stepBackward.png"))); // NOI18N
        jButton12.setToolTipText(bundle.getString("Vista.jButton12.toolTipText")); // NOI18N
        jButton12.setName("jButton12"); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel4.setName("jLabel4"); // NOI18N

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setName("jTextField1"); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(394, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(533, Short.MAX_VALUE))
        );

        jLayeredPane1.add(jPanel8);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(92, 69));
        jTabbedPane1.setName(""); // NOI18N
        jTabbedPane1.setOpaque(true);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 232, 198));
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        jPanel15.setName("jPanel15"); // NOI18N
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane7.setViewportView(jPanel15);

        jLabel1.setText(bundle.getString("Vista.jLabel1.text").concat(":")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane5.setHorizontalScrollBar(null);
        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jPanel10.setMinimumSize(new java.awt.Dimension(250, 0));
        jPanel10.setName("jPanel10"); // NOI18N
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane5.setViewportView(jPanel10);

        filler1.setName("filler1"); // NOI18N

        filler2.setName("filler2"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane7))
                .addContainerGap())
        );

        jScrollPane4.setViewportView(jPanel1);

        jTabbedPane1.addTab(bundle.getString("Vista.jMenu4.text"), jScrollPane4); // NOI18N

        jPanel2.setBackground(new java.awt.Color(209, 254, 209));
        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane10.setName("jScrollPane10"); // NOI18N

        jList1.setName("jList1"); // NOI18N
        jScrollPane10.setViewportView(jList1);
        jList1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                jButton14.setEnabled(!jList1.isSelectionEmpty());
            }
        });

        jList1.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof IFilter) {
                    ((JLabel) renderer).setText(((IFilter) value).getName());
                }
                return renderer;
            }
        });

        jScrollPane11.setMinimumSize(new java.awt.Dimension(400, 100));
        jScrollPane11.setName("jScrollPane11"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));
        jScrollPane11.setViewportView(jPanel3);

        jButton14.setText(bundle.getString("Vista.jButton14.text")); // NOI18N
        jButton14.setEnabled(false);
        jButton14.setName("jButton14"); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton14)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Vista.jMenu7.text"), jPanel2); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setName("jPanel4"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel4);

        jTabbedPane1.addTab(bundle.getString("Vista.jMenu8.text"), jScrollPane2); // NOI18N

        String nomeLabel = "";
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                nomeLabel = bundle.getString("Vista.jMenu4.text");
                break;
                case 1:
                nomeLabel = bundle.getString("Vista.jMenu7.text");
                break;
                case 2:
                nomeLabel = bundle.getString("Vista.jMenu8.text");
                break;
            }
            final JLabel label = new JLabel("<html><body style='width:80px;text-align:center;margin-bottom:4px;'>" + nomeLabel + "</body></html>", JLabel.CENTER);
            label.setFont(new Font("Calibri", 1, 16));
            label.addPropertyChangeListener("enabled", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (!(boolean) evt.getNewValue()) {
                        Jsoup.parse(label.getText());
                    }
                }
            });
            jTabbedPane1.setTabComponentAt(i, label);
        }

        jTabbedPane1.addPropertyChangeListener(
            "enabled", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt
                ) {
                    for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
                        ((JLabel) jTabbedPane1.getTabComponentAt(i)).setEnabled((boolean) (evt.getNewValue()));
                    }
                }
            }
        );
        jTabbedPane1.setEnabled(false);
        jTabbedPane1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                SwingUtilities.invokeLater(new Runnable () {
                    public void run() {
                        jPanel8.setVisible(jTabbedPane1.getSelectedIndex() == 2);
                    }
                });
            }
        });

        jLayeredPane1.add(jTabbedPane1);
        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText(bundle.getString("Vista.jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText(bundle.getString("Vista.jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText(bundle.getString("Vista.jMenuItem2.text")); // NOI18N
        jMenuItem2.setEnabled(false);
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jMenu1.add(jSeparator1);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText(bundle.getString("Vista.jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText(bundle.getString("Vista.jMenuItem4.text")); // NOI18N
        jMenuItem4.setEnabled(false);
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jMenu1.add(jSeparator2);

        jMenuItem10.setText(bundle.getString("Vista.jMenuItem10.text")); // NOI18N
        jMenuItem10.setEnabled(false);
        jMenuItem10.setName("jMenuItem10"); // NOI18N
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jMenu1.add(jSeparator3);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem5.setText(bundle.getString("Vista.jMenuItem5.text")); // NOI18N
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(bundle.getString("Vista.jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText(bundle.getString("Vista.jMenuItem7.text")); // NOI18N
        jMenuItem7.setEnabled(false);
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText(bundle.getString("Vista.jMenuItem8.text")); // NOI18N
        jMenuItem8.setEnabled(false);
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu4.setText(bundle.getString("Vista.jMenu4.text")); // NOI18N
        jMenu4.setName("jMenu4"); // NOI18N

        jMenuItem13.setText(bundle.getString("Vista.jMenuItem13.text")); // NOI18N
        jMenuItem13.setEnabled(false);
        jMenuItem13.setName("jMenuItem13"); // NOI18N
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem13);

        jMenuItem14.setText(bundle.getString("Vista.jMenuItem14.text")); // NOI18N
        jMenuItem14.setEnabled(false);
        jMenuItem14.setName("jMenuItem14"); // NOI18N
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem14);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jMenu4.add(jSeparator4);

        jMenuItem18.setText(bundle.getString("Vista.jMenuItem18.text")); // NOI18N
        jMenuItem18.setEnabled(false);
        jMenuItem18.setName("jMenuItem18"); // NOI18N
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem18);

        jMenuItem19.setText(bundle.getString("Vista.jMenuItem19.text")); // NOI18N
        jMenuItem19.setEnabled(false);
        jMenuItem19.setName("jMenuItem19"); // NOI18N
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem19);

        jMenuItem20.setText(bundle.getString("Vista.jMenuItem20.text")); // NOI18N
        jMenuItem20.setEnabled(false);
        jMenuItem20.setName("jMenuItem20"); // NOI18N
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem20);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jMenu4.add(jSeparator5);

        jMenuItem15.setText(bundle.getString("Vista.jMenuItem15.text")); // NOI18N
        jMenuItem15.setEnabled(false);
        jMenuItem15.setName("jMenuItem15"); // NOI18N
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem15);

        jSeparator6.setName("jSeparator6"); // NOI18N
        jMenu4.add(jSeparator6);

        jMenuItem21.setText(bundle.getString("Vista.jMenuItem21.text")); // NOI18N
        jMenuItem21.setEnabled(false);
        jMenuItem21.setName("jMenuItem21"); // NOI18N
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem21);

        jMenuBar1.add(jMenu4);

        jMenu7.setText(bundle.getString("Vista.jMenu7.text")); // NOI18N
        jMenu7.setName("jMenu7"); // NOI18N

        jMenuItem23.setText(bundle.getString("Vista.jMenuItem23.text")); // NOI18N
        jMenuItem23.setEnabled(false);
        jMenuItem23.setName("jMenuItem23"); // NOI18N
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem23);

        jMenuItem24.setText(bundle.getString("Vista.jMenuItem24.text")); // NOI18N
        jMenuItem24.setEnabled(false);
        jMenuItem24.setName("jMenuItem24"); // NOI18N
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem24);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jMenu7.add(jSeparator7);

        jMenuItem26.setText(bundle.getString("Vista.jMenuItem26.text")); // NOI18N
        jMenuItem26.setEnabled(false);
        jMenuItem26.setName("jMenuItem26"); // NOI18N
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem26);

        jMenuBar1.add(jMenu7);

        jMenu8.setText(bundle.getString("Vista.jMenu8.text")); // NOI18N
        jMenu8.setName("jMenu8"); // NOI18N

        jMenuItem16.setText(bundle.getString("Vista.jMenuItem16.text")); // NOI18N
        jMenuItem16.setEnabled(false);
        jMenuItem16.setName("jMenuItem16"); // NOI18N
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem16);

        jMenuItem17.setText(bundle.getString("Vista.jMenuItem17.text")); // NOI18N
        jMenuItem17.setEnabled(false);
        jMenuItem17.setName("jMenuItem17"); // NOI18N
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem17);

        jMenuItem22.setText(bundle.getString("Vista.jMenuItem22.text")); // NOI18N
        jMenuItem22.setEnabled(false);
        jMenuItem22.setName("jMenuItem22"); // NOI18N
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem22);

        jSeparator8.setName("jSeparator8"); // NOI18N
        jMenu8.add(jSeparator8);

        jMenuItem25.setText(bundle.getString("Vista.jMenuItem25.text")); // NOI18N
        jMenuItem25.setEnabled(false);
        jMenuItem25.setName("jMenuItem25"); // NOI18N
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem25);

        jMenuBar1.add(jMenu8);

        jMenu5.setText(bundle.getString("Vista.jMenu5.text")); // NOI18N
        jMenu5.setName("jMenu5"); // NOI18N

        jMenu6.setText(bundle.getString("Vista.jMenu6.text")); // NOI18N
        jMenu6.setName("jMenu6"); // NOI18N

        jMenuItem9.setText(bundle.getString("Vista.jMenuItem9.text")); // NOI18N
        jMenuItem9.setName("jMenuItem9"); // NOI18N
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem9);

        jMenuItem11.setText(bundle.getString("Vista.jMenuItem11.text")); // NOI18N
        jMenuItem11.setName("jMenuItem11"); // NOI18N
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem11);

        jMenuItem12.setText(bundle.getString("Vista.jMenuItem12.text")); // NOI18N
        jMenuItem12.setName("jMenuItem12"); // NOI18N
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem12);

        jMenu5.add(jMenu6);

        jMenuBar1.add(jMenu5);

        jMenu3.setText(bundle.getString("Vista.jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N

        jMenuItem6.setText(bundle.getString("Vista.jMenuItem6.text")); // NOI18N
        jMenuItem6.setName("jMenuItem6"); // NOI18N
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(314, 314, 314)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(624, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(576, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLayeredPane1)
                    .addGap(38, 38, 38)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_IMPORTAR_FICHEIRO);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_EXPORTAR_FICHEIRO);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_ABRIR_SESION);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_GARDAR_SESION);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        pechar();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void pechar() {
        System.exit(0);
    }

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        amosarAcercaDe();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        meuControlador.manexarEvento(Controlador.DESFACER, null);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        meuControlador.manexarEvento(Controlador.REFACER, null);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        meuControlador.manexarEvento(Controlador.RESTAURAR, null);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void botonIndiceTemporalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIndiceTemporalActionPerformed
        if (getColumnaModeloSeleccionada() == meuModelo.getIndiceTemporal() || !meuModelo.getComparableInstances().attribute(getColumnaModeloSeleccionada()).isNumeric()) {
            ordeVisualizacion = ORDE_MODELO;
        }
        meuControlador.manexarEvento(Controlador.MUDAR_INDICE_TEMPORAL, getColumnaModeloSeleccionada() == meuModelo.getIndiceTemporal() ? -1 : getColumnaModeloSeleccionada());
    }//GEN-LAST:event_botonIndiceTemporalActionPerformed

    private void botonNumericoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNumericoActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{getColumnaModeloSeleccionada(), Attribute.NUMERIC});
    }//GEN-LAST:event_botonNumericoActionPerformed

    private void botonStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonStringActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{getColumnaModeloSeleccionada(), Attribute.STRING});
    }//GEN-LAST:event_botonStringActionPerformed

    private void botonNominalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNominalActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{getColumnaModeloSeleccionada(), Attribute.NOMINAL});
    }//GEN-LAST:event_botonNominalActionPerformed

    private void botonDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDataActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{getColumnaModeloSeleccionada(), Attribute.DATE});
    }//GEN-LAST:event_botonDataActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        mudarIdioma("en");
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        mudarIdioma("gl");
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        mudarIdioma("es");
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        jTableModelo.finalizarEdicions();
        meuControlador.manexarEvento(Controlador.ENGADIR_DATOS, null);
        jScrollPane3.getVerticalScrollBar().setValue(jScrollPane3.getVerticalScrollBar().getMaximum());
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        jTableModelo.finalizarEdicions();
        Integer[] indices = new Integer[jTableModelo.getSelectedRowCount()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = jTableModelo.getSelectedRows()[i];
        }
        jTableModelo.getSelectionModel().clearSelection();
        meuControlador.manexarEvento(Controlador.ELIMINAR_DATOS, indices);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        String novoNomeRelacion = (String) JOptionPane.showInputDialog(this, recursosIdioma.getString("mudarNomeRelacionMensaxe"), recursosIdioma.getString("Vista.jMenuItem15.text"), JOptionPane.QUESTION_MESSAGE, null, null, meuModelo.getComparableInstances().relationName());
        if (novoNomeRelacion != null) {
            meuControlador.manexarEvento(Controlador.MUDAR_NOME_RELACION, novoNomeRelacion);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        configurarJDialogEngadirOuEliminarScatterplots();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        configurarEstablecerAtributoNominalRepresentado();
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        mansp.pause();
        mansp.goTo(0.0);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        mansp.pause();
        mansp.goTo(1.0);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        if (ultimoEstadoReprodutor == ManexadorScatterPlots.PLAY) {
            mansp.play();
        }
        pulsarSlider = false;
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jSlider1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MousePressed
        pulsarSlider = true;
        ultimoEstadoReprodutor = mansp.getEstado();
        mansp.freeze();
    }//GEN-LAST:event_jSlider1MousePressed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (mansp.getTActual() >= mansp.getTFinal()) {
            mansp.goTo(0.0);
        }
        mansp.play();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (mansp.getTActual() < mansp.getTFinal()) {
            mansp.goToNext();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (mansp.getTActual() > mansp.getTInicial()) {
            mansp.goToPrevious();
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void botonOcultarColumnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonOcultarColumnaActionPerformed
        TableColumn tc = jTableModelo.getColumnModel().getColumn(jTableModelo.columnaTaboaSeleccionada);
        tc.setMinWidth(0);
        tc.setMaxWidth(0);
    }//GEN-LAST:event_botonOcultarColumnaActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        int columnaModelo = getColumnaModeloSeleccionada();
        jTableModelo.columnaTaboaSeleccionada = -1;
        meuControlador.manexarEvento(Controlador.ELIMINAR_ATRIBUTO, columnaModelo);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        meuControlador.manexarEvento(Controlador.ENGADIR_ATRIBUTO, null);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        String novoNomeAtributo = (String) JOptionPane.showInputDialog(this, recursosIdioma.getString("mudarNomeAtributoMensaxe"), recursosIdioma.getString("Vista.jMenuItem20.text"), JOptionPane.QUESTION_MESSAGE, null, null, meuModelo.getComparableInstances().attribute(getColumnaModeloSeleccionada()).name());
        if (novoNomeAtributo != null) {
            meuControlador.manexarEvento(Controlador.RENOMEAR_ATRIBUTO, new Object[]{getColumnaModeloSeleccionada(), novoNomeAtributo});
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        for (int i = 0; i < jTableModelo.getColumnModel().getColumnCount(); i++) {
            TableColumn tc = jTableModelo.getColumnModel().getColumn(i);
            if (jTableModelo.getColumnModel().getColumn(i).getWidth() == 0) {
                tc.setMaxWidth(Integer.MAX_VALUE);
                tc.setMinWidth(15);
                tc.setPreferredWidth(75);
            }
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        switch (ordeVisualizacion) {
            case ORDE_MODELO:
                buttonGroup2.setSelected(jRadioButton2.getModel(), true);
                break;
            case ORDE_INDICE_TEMPORAL_NUMERICO:
                buttonGroup2.setSelected(jRadioButton3.getModel(), true);
                break;
            case ORDE_INDICE_TEMPORAL_NUMERICO_PONDERADO:
                buttonGroup2.setSelected(jRadioButton4.getModel(), true);
                break;
        }
        boolean ordeNumericoDisponible = meuModelo.getIndiceTemporal() > -1;
        jRadioButton3.setEnabled(ordeNumericoDisponible);
        jRadioButton4.setEnabled(ordeNumericoDisponible);
        jTextField2.setText(String.valueOf(paso));
        jTextField3.setText(String.valueOf(lonxitudeEstela));
        jLabel15.setBackground(corEstela);
        jDialog4.setLocation((getWidth() - jDialog4.getPreferredSize().width) / 2, (getHeight() - jDialog4.getPreferredSize().height) / 2);
        jDialog4.pack();
        jDialog4.setVisible(true);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        ButtonModel mb = buttonGroup2.getSelection();
        if (jRadioButton2.getModel().equals(mb)) {
            ordeVisualizacion = ORDE_MODELO;
        } else if (jRadioButton3.getModel().equals(mb)) {
            ordeVisualizacion = ORDE_INDICE_TEMPORAL_NUMERICO;
        } else if (jRadioButton4.getModel().equals(mb)) {
            ordeVisualizacion = ORDE_INDICE_TEMPORAL_NUMERICO_PONDERADO;
        }
        try {
            paso = Integer.parseInt(jTextField2.getText());
            lonxitudeEstela = Integer.parseInt(jTextField3.getText());
        } catch (NumberFormatException e) {
        }
        corEstela = jLabel15.getBackground();
        pintarMenuVisualizacion();
        jDialog4.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        char vChar = evt.getKeyChar();
        if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jDialog3.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (meuModelo.getIndiceAtributoNominal() != meuModelo.obterListaNomesAtributos().indexOf(jComboBox1.getSelectedItem())) {
            if (jComboBox1.getSelectedIndex() == 0) {
                meuModelo.setIndiceAtributoNominal(-1);
            } else {
                meuModelo.setIndiceAtributoNominal(meuModelo.obterListaNomesAtributos().indexOf(jComboBox1.getSelectedItem()));
            }
            pintarMenus();
        }
        jDialog3.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        jDialog4.dispose();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        meuControlador.manexarEvento(Controlador.ENGADIR_FILTRO, new Object[]{meuModelo.contarFiltros(), jList1.getSelectedValuesList().get(0)});
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_IMPORTAR_FILTROS);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_EXPORTAR_FILTROS);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if (visualizacionDesactualizada && ((JTabbedPane) evt.getSource()).getSelectedIndex() == 2) {
            pintarMenuVisualizacion();
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_IMPORTAR_FILTRO_DESDE_JAR);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        jTextField6.setText(distanceFormula);
        jLabel11.setText(puntoA != null ? puntoA.toString() : "");
        jLabel13.setText(puntoB != null ? puntoB.toString() : "");
        jFrame1.pack();
        jFrame1.setLocationRelativeTo(this);
        jFrame1.setVisible(true);

        ArrayList<String> words = new ArrayList<>();
        Enumeration<Attribute> en = meuModelo.getComparableInstances().enumerateAttributes();
        while (en.hasMoreElements()) {
            Attribute at = en.nextElement();
            words.add("`" + at.name() + "`");
        }

        new AutoSuggestor(jTextField6, jFrame1, words, Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f) {
            @Override
            boolean wordTyped(String typedWord) {
                return super.wordTyped(getTextField().getText().substring(0, Math.min(getTextField().getCaretPosition() + 1, getTextField().getText().length())));
            }
        };

    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        jFrame1.dispose();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        if (puntoA == null || puntoB == null || jTextField6.getText().isEmpty()) {
        } else {
            distanceFormula = jTextField6.getText();
            try {
                JOptionPane.showMessageDialog(this, "La distancia es " + interpretarFormula(puntoA, puntoB, jTextField6.getText()) + ".");
            } catch (EvaluationException ex) {
                JOptionPane.showMessageDialog(jFrame1, recursosIdioma.getString("formulaDistanciaErronea"));
            }
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        buscaPunto = 'A';
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        buscaPunto = 'B';
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jFrame1WindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jFrame1WindowClosed
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        buscaPunto = null;
        puntoA = null;
        puntoB = null;
    }//GEN-LAST:event_jFrame1WindowClosed

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        Color c = JColorChooser.showDialog(jDialog4, recursosIdioma.getString("Vista.jLabel14.text"), jLabel15.getBackground());
        if (c != null) {
            jLabel15.setBackground(c);
        }
    }//GEN-LAST:event_jLabel15MouseClicked

    private void configurarEstablecerAtributoNominalRepresentado() {
        jDialog3.setLocation((getWidth() - jDialog3.getPreferredSize().width) / 2, (getHeight() - jDialog3.getPreferredSize().height) / 2);
        jComboBox1.removeAllItems();
        jComboBox1.addItem("- " + recursosIdioma.getString("ningun") + " -");
        meuModelo.obterIndicesAtributosNominais().stream().forEach((a) -> {
            jComboBox1.addItem(meuModelo.obterNomeAtributo(a));
        });
        if (meuModelo.getIndiceAtributoNominal() == -1) {
            jComboBox1.setSelectedIndex(0);
        } else {
            jComboBox1.setSelectedItem(meuModelo.obterNomeAtributo(meuModelo.getIndiceAtributoNominal()));
        }
        jDialog3.pack();
        jDialog3.setVisible(true);
    }

    private void configurarJDialogEngadirOuEliminarScatterplots() {
        jDialog2.setLocation((getWidth() - jDialog2.getPreferredSize().width) / 2, (getHeight() - jDialog2.getPreferredSize().height) / 2);
        jPanel5.removeAll();
        jPanel6.removeAll();
        jPanel7.removeAll();
        int anchoCela = 40, altoCela = 40;
        for (ActionListener al : jButton1.getActionListeners()) {
            jButton1.removeActionListener(al);
        }
        for (ActionListener al : jButton2.getActionListeners()) {
            jButton2.removeActionListener(al);
        }
        final boolean[][] scatterPlotsVisiblesAux = new boolean[scatterPlotsVisibles.length][scatterPlotsVisibles.length];
        for (int i = 0; i < scatterPlotsVisibles.length; i++) {
            System.arraycopy(scatterPlotsVisibles[i], 0, scatterPlotsVisiblesAux[i], 0, scatterPlotsVisibles[i].length);
        }
        ArrayList<Integer> atributosNumericos = meuModelo.obterIndicesAtributosNumericos();
        if (!atributosNumericos.isEmpty()) {
            jPanel5.setLayout(new GridLayout(0, atributosNumericos.size(), 2, 2));
            for (int j = atributosNumericos.size() - 1; j >= 0; j--) {
                JLabel lh = new JLabel(meuModelo.obterNomeAtributo(atributosNumericos.get(j)), JLabel.CENTER);
                lh.setPreferredSize(new Dimension(100, altoCela));
                lh.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                jPanel6.add(lh);
                for (int k = 0; k < atributosNumericos.size(); k++) {
                    if (j == 0) {
                        JLabel lv = new JLabel(meuModelo.obterNomeAtributo(atributosNumericos.get(k)), JLabel.CENTER);
                        lv.setPreferredSize(new Dimension(anchoCela, 100));
                        lv.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        lv.setUI(new VerticalLabelUI(false));
                        jPanel7.add(lv);
                    }
                    JLabel l = new JLabel();
                    l.setToolTipText(meuModelo.obterNomeAtributo(atributosNumericos.get(j)) + " " + recursosIdioma.getString("fronteA") + " " + meuModelo.obterNomeAtributo(atributosNumericos.get(k)));
                    l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    l.setOpaque(true);
                    l.setPreferredSize(new Dimension(anchoCela, altoCela));
                    if (scatterPlotsVisibles.length > 0 && scatterPlotsVisiblesAux[j][k]) {
                        l.setBackground(Color.YELLOW);
                    }
                    l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    final int a = j, b = k;
                    l.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                if (scatterPlotsVisiblesAux[a][b]) {
                                    ((JLabel) e.getSource()).setBackground(null);
                                } else {
                                    ((JLabel) e.getSource()).setBackground(Color.YELLOW);
                                }
                                scatterPlotsVisiblesAux[a][b] = !scatterPlotsVisiblesAux[a][b];
                            }
                        }
                    });
                    jPanel5.add(l);
                }
            }
        }
        jButton1.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < scatterPlotsVisiblesAux.length; i++) {
                System.arraycopy(scatterPlotsVisiblesAux[i], 0, scatterPlotsVisibles[i], 0, scatterPlotsVisiblesAux[i].length);
            }
            pintarMenuVisualizacion();
            jDialog2.dispose();
        });
        jButton2.addActionListener((ActionEvent e) -> {
            jDialog2.dispose();
        });
        jButton4.addActionListener((ActionEvent e) -> {
            for (Component c : jPanel5.getComponents()) {
                if (c instanceof JLabel) {
                    c.setBackground(Color.YELLOW);
                }
            }
            for (boolean[] scatterPlotsVisiblesAux1 : scatterPlotsVisiblesAux) {
                for (int j = 0; j < scatterPlotsVisiblesAux1.length; j++) {
                    scatterPlotsVisiblesAux1[j] = true;
                }
            }
        });
        jButton3.addActionListener((ActionEvent e) -> {
            for (Component c : jPanel5.getComponents()) {
                if (c instanceof JLabel) {
                    c.setBackground(null);
                }
            }
            for (boolean[] scatterPlotsVisiblesAux1 : scatterPlotsVisiblesAux) {
                for (int j = 0; j < scatterPlotsVisiblesAux1.length; j++) {
                    scatterPlotsVisiblesAux1[j] = false;
                }
            }
        });
        SwingUtilities.invokeLater(() -> {
            JScrollBar scrollVertical = jScrollPane1.getVerticalScrollBar();
            scrollVertical.setValue(scrollVertical.getMaximum());
        });
        jDialog2.pack();
        jDialog2.setVisible(true);
    }

    public void reiniciarAplicacion() {
        jFrameModeloParcial.dispose();
        if (mansp != null) {
            mansp.pecharJFramesChartPanel();
        }
        dispose();
        JDataMotion.main(null);
    }

    private static class VerticalLabelUI extends BasicLabelUI {

        static {
            labelUI = new VerticalLabelUI(false);
        }

        protected boolean clockwise;

        public VerticalLabelUI(boolean clockwise) {
            super();
            this.clockwise = clockwise;
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            Dimension dim = super.getPreferredSize(c);
            return new Dimension(dim.height, dim.width);
        }

        private final Rectangle paintIconR = new Rectangle();
        private final Rectangle paintTextR = new Rectangle();
        private final Rectangle paintViewR = new Rectangle();
        private Insets paintViewInsets = new Insets(0, 0, 0, 0);

        @Override
        public void paint(Graphics g, JComponent c) {
            JLabel label = (JLabel) c;
            String text = label.getText();
            Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
            if ((icon == null) && (text == null)) {
                return;
            }
            FontMetrics fm = g.getFontMetrics();
            paintViewInsets = c.getInsets(paintViewInsets);
            paintViewR.x = paintViewInsets.left;
            paintViewR.y = paintViewInsets.top;
            paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
            paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
            paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
            paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
            String clippedText = layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);
            Graphics2D g2 = (Graphics2D) g;
            AffineTransform tr = g2.getTransform();
            if (clockwise) {
                g2.rotate(Math.PI / 2);
                g2.translate(0, -c.getWidth());
            } else {
                g2.rotate(-Math.PI / 2);
                g2.translate(-c.getHeight(), 0);
            }
            if (icon != null) {
                icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
            }
            if (text != null) {
                int textX = paintTextR.x, textY = paintTextR.y + fm.getAscent();
                if (label.isEnabled()) {
                    paintEnabledText(label, g, clippedText, textX, textY);
                } else {
                    paintDisabledText(label, g, clippedText, textX, textY);
                }
            }
            g2.setTransform(tr);
        }
    }

    private static void gravarPropiedade(String nomePropiedade, String valorPropiedade, String url) {
        Properties p = lerPropiedades(url);
        p.put(nomePropiedade, valorPropiedade);
        escribirPropiedades(p, ficheiroConfiguracion);
    }

    private static void gravarEscribirPropiedade(Properties propiedades, String nomePropiedade, String valorPropiedade, String url) {
        propiedades.put(nomePropiedade, valorPropiedade);
        gravarPropiedade(nomePropiedade, valorPropiedade, url);
    }

    private void mudarIdioma(String locale) {
        if (JOptionPane.showConfirmDialog(this, recursosIdioma.getString("confirmacionMudarIdioma"), recursosIdioma.getString("confirmacionMudarIdiomaTitulo"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            gravarPropiedade("locale", locale, ficheiroConfiguracion);
            reiniciarAplicacion();
        }
    }

    private void amosarAcercaDe() {
        try {
            JOptionPane.showMessageDialog(this, recursosIdioma.getString("AcercaDe"), recursosIdioma.getString("Vista.jMenuItem6.text"), INFORMATION_MESSAGE, new ImageIcon(ImageIO.read(getClass().getResource("imaxes/favicon.png")).getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
        } catch (IOException ex) {
            if (Controlador.debug) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void abrirExploradorFicheiros(int opcion) {
        switch (opcion) {
            case EXPLORADOR_IMPORTAR_FILTRO_DESDE_JAR:
                configurarJFileChooserAbrir(Controlador.IMPORTAR_FILTRO_DENDE_JAR);
                break;
            case EXPLORADOR_ABRIR_SESION:
                configurarJFileChooserAbrir(Controlador.ABRIR_SESION);
                break;
            case EXPLORADOR_IMPORTAR_FICHEIRO:
                configurarJFileChooserAbrir(Controlador.IMPORTAR_FICHEIRO);
                break;
            case EXPLORADOR_EXPORTAR_FICHEIRO:
                configurarJFileChooserGardar(Controlador.EXPORTAR_FICHEIRO);
                break;
            case EXPLORADOR_GARDAR_SESION:
                configurarJFileChooserGardar(Controlador.GARDAR_SESION);
                break;
            case EXPLORADOR_EXPORTAR_FILTROS:
                configurarJFileChooserGardar(Controlador.EXPORTAR_FILTROS);
                break;
            case EXPLORADOR_IMPORTAR_FILTROS:
                configurarJFileChooserAbrir(Controlador.IMPORTAR_FILTROS);
                break;
        }
        jDialog1.setVisible(true);
    }

    private class FiltroExtension implements Serializable {

        public String nome;
        public String[] extensions;

        public FiltroExtension(String nome, String[] extensions) {
            this.nome = nome;
            this.extensions = extensions;
        }

        public String getNome() {
            return nome;
        }

        public String[] getExtensions() {
            return extensions;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setExtensions(String[] extensions) {
            this.extensions = extensions;
        }
    }

    private void configurarJFileChooserAbrir(final int eventoParaControlador) {
        ArrayList<FiltroExtension> ext = new ArrayList<>();
        for (ActionListener al : jFileChooser1.getActionListeners()) {
            jFileChooser1.removeActionListener(al);
        }
        for (PropertyChangeListener al : jFileChooser1.getPropertyChangeListeners()) {
            jFileChooser1.removePropertyChangeListener("fileFilterChanged", al);
        }
        jFileChooser1.setCurrentDirectory(new File("."));
        jFileChooser1.setDialogType(JFileChooser.OPEN_DIALOG);
        switch (eventoParaControlador) {
            case Controlador.ABRIR_SESION:
                ext.add(new FiltroExtension(recursosIdioma.getString("formatoSesion") + " (*.jdms)", new String[]{"jdms"}));
                ext.add(new FiltroExtension(recursosIdioma.getString("todosOsFicheiros") + " (*.*)", null));
                definirExtensions(ext);
                jDialog1.setTitle(recursosIdioma.getString("Vista.jMenuItem3.text"));
                jFileChooser1.setApproveButtonText(recursosIdioma.getString("abrir"));
                break;
            case Controlador.IMPORTAR_FICHEIRO:
                ext.add(new FiltroExtension(recursosIdioma.getString("formatosImportarFicheiro") + " (*.csv;*.arff)", new String[]{"csv", "arff"}));
                ext.add(new FiltroExtension(recursosIdioma.getString("todosOsFicheiros") + " (*.*)", null));
                definirExtensions(ext);
                jDialog1.setTitle(recursosIdioma.getString("Vista.jMenuItem1.text"));
                jFileChooser1.setApproveButtonText(recursosIdioma.getString("importar"));
                break;
            case Controlador.IMPORTAR_FILTROS:
                ext.add(new FiltroExtension(recursosIdioma.getString("formatoFiltros") + " (*.jdmf)", new String[]{"jdmf"}));
                ext.add(new FiltroExtension(recursosIdioma.getString("todosOsFicheiros") + " (*.*)", null));
                definirExtensions(ext);
                jDialog1.setTitle(recursosIdioma.getString("Vista.jMenuItem23.text"));
                jFileChooser1.setApproveButtonText(recursosIdioma.getString("importar"));
                break;
            case Controlador.IMPORTAR_FILTRO_DENDE_JAR:
                ext.add(new FiltroExtension(recursosIdioma.getString("formatoJAR") + " (*.jar)", new String[]{"jar"}));
                ext.add(new FiltroExtension(recursosIdioma.getString("todosOsFicheiros") + " (*.*)", null));
                definirExtensions(ext);
                jDialog1.setTitle(recursosIdioma.getString("Vista.jMenuItem26.text"));
                jFileChooser1.setApproveButtonText(recursosIdioma.getString("importar"));
                break;
        }
        jFileChooser1.setSelectedFile(new File(""));
        jFileChooser1.addActionListener((ActionEvent e) -> {
            jDialog1.dispose();
            if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                meuControlador.manexarEvento(
                        eventoParaControlador,
                        jFileChooser1.getSelectedFile().getAbsolutePath());
            }
        });
        jFileChooser1.revalidate();
        jFileChooser1.repaint();
    }

    private void definirExtensions(ArrayList<FiltroExtension> ext) {
        for (FileFilter e : jFileChooser1.getChoosableFileFilters()) {
            jFileChooser1.removeChoosableFileFilter(e);
        }
        UIManager.put("FileChooser.acceptAllFileFilterText", recursosIdioma.getString("todosOsFicheiros"));
        jFileChooser1.setAcceptAllFileFilterUsed(false);
        ext.stream().forEach((e) -> {
            if (e.getExtensions() == null) {
                jFileChooser1.setAcceptAllFileFilterUsed(true);
            } else {
                FileNameExtensionFilter filter = new FileNameExtensionFilter(e.getNome(), e.getExtensions());
                this.jFileChooser1.addChoosableFileFilter(filter);
            }
        });
    }

    private void configurarJFileChooserGardar(final int eventoParaControlador) {
        ArrayList<FiltroExtension> ext = new ArrayList<>();
        for (ActionListener al : jFileChooser1.getActionListeners()) {
            jFileChooser1.removeActionListener(al);
        }
        for (PropertyChangeListener al : jFileChooser1.getPropertyChangeListeners()) {
            jFileChooser1.removePropertyChangeListener("fileFilterChanged", al);
        }
        jFileChooser1.setDialogType(JFileChooser.SAVE_DIALOG);
        switch (eventoParaControlador) {
            case Controlador.GARDAR_SESION:
                ext.add(new FiltroExtension(recursosIdioma.getString("formatoSesion") + " (*.jdms)", new String[]{"jdms"}));
                definirExtensions(ext);
                jDialog1.setTitle(recursosIdioma.getString("Vista.jMenuItem4.text"));
                jFileChooser1.setSelectedFile(new File(recursosIdioma.getString("SenTitulo") + ".jdms"));
                jFileChooser1.addActionListener((ActionEvent e) -> {
                    boolean pecharEGardar = true;
                    File f = jFileChooser1.getSelectedFile();
                    if (f.exists() && jFileChooser1.getDialogType() == javax.swing.JFileChooser.SAVE_DIALOG && "ApproveSelection".equals(e.getActionCommand())) {
                        int result = JOptionPane.showConfirmDialog(jDialog1, recursosIdioma.getString("confirmacionSobrescritura"), recursosIdioma.getString("errorFicheiroXaExistenteTitulo"), JOptionPane.YES_NO_CANCEL_OPTION);
                        switch (result) {
                            case JOptionPane.YES_OPTION:
                                break;
                            case JOptionPane.CANCEL_OPTION:
                            case JOptionPane.NO_OPTION:
                            case JOptionPane.CLOSED_OPTION:
                                pecharEGardar = false;
                                break;
                        }
                    }
                    if (pecharEGardar) {
                        jDialog1.dispose();
                        if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                            meuControlador.manexarEvento(
                                    Controlador.GARDAR_SESION,
                                    jFileChooser1.getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                jFileChooser1.setApproveButtonText(recursosIdioma.getString("gardar"));
                break;
            case Controlador.EXPORTAR_FICHEIRO:
                FiltroExtension def = new FiltroExtension(recursosIdioma.getString("formatoExportarFicheiro1") + " (*.arff)", new String[]{"arff"});
                ext.add(def);
                ext.add(new FiltroExtension(recursosIdioma.getString("formatoExportarFicheiro2") + " (*.csv)", new String[]{"csv"}));
                definirExtensions(ext);
                jDialog1.setTitle(recursosIdioma.getString("Vista.jMenuItem2.text"));
                jFileChooser1.addPropertyChangeListener("fileFilterChanged", (PropertyChangeEvent evt) -> {
                    actualizarNomeFicheiro();
                });
                actualizarNomeFicheiro();
                jFileChooser1.addActionListener((ActionEvent e) -> {
                    switch (e.getActionCommand()) {
                        case "ApproveSelection":
                            File f = jFileChooser1.getSelectedFile();
                            if (f.exists()) {
                                JOptionPane.showMessageDialog(jDialog1, recursosIdioma.getString("errorFicheiroXaExistente"), recursosIdioma.getString("errorFicheiroXaExistenteTitulo"), INFORMATION_MESSAGE);
                            } else {
                                String extension = "";
                                try {
                                    Field campos = jFileChooser1.getFileFilter().getClass().getDeclaredField("extensions");
                                    campos.setAccessible(true);
                                    extension = ((String[]) campos.get(jFileChooser1.getFileFilter()))[0];
                                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                                    if (Controlador.debug) {
                                        Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                                    meuControlador.manexarEvento(
                                            Controlador.EXPORTAR_FICHEIRO,
                                            new Object[]{extension, jFileChooser1.getSelectedFile().getAbsolutePath()});
                                }
                                jDialog1.dispose();
                            }
                            break;
                        case "CancelSelection":
                            jDialog1.dispose();
                            break;
                    }
                });
                jFileChooser1.setApproveButtonText(recursosIdioma.getString("exportar"));
                break;
            case Controlador.EXPORTAR_FILTROS:
                ext.add(new FiltroExtension(recursosIdioma.getString("formatoFiltros") + " (*.jdmf)", new String[]{"jdmf"}));
                definirExtensions(ext);
                jDialog1.setTitle(recursosIdioma.getString("Vista.jMenuItem24.text"));
                jFileChooser1.setSelectedFile(new File(recursosIdioma.getString("SenTitulo") + ".jdmf"));
                jFileChooser1.addActionListener((ActionEvent e) -> {
                    boolean pecharEGardar = true;
                    File f = jFileChooser1.getSelectedFile();
                    if (f.exists() && jFileChooser1.getDialogType() == javax.swing.JFileChooser.SAVE_DIALOG && "ApproveSelection".equals(e.getActionCommand())) {
                        int result = JOptionPane.showConfirmDialog(jDialog1, recursosIdioma.getString("confirmacionSobrescritura"), recursosIdioma.getString("errorFicheiroXaExistenteTitulo"), JOptionPane.YES_NO_CANCEL_OPTION);
                        switch (result) {
                            case JOptionPane.YES_OPTION:
                                break;
                            case JOptionPane.CANCEL_OPTION:
                            case JOptionPane.NO_OPTION:
                            case JOptionPane.CLOSED_OPTION:
                                pecharEGardar = false;
                                break;
                        }
                    }
                    if (pecharEGardar) {
                        jDialog1.dispose();
                        List<Integer> indicesFiltros = new ArrayList<>();
                        for (int i = 0; i < meuModelo.getFiltros().size(); i++) {
                            if (meuModelo.getFiltros().get(i).isSeleccionado()) {
                                indicesFiltros.add(i);
                            }
                        }
                        if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                            meuControlador.manexarEvento(
                                    Controlador.EXPORTAR_FILTROS,
                                    new Object[]{
                                        jFileChooser1.getSelectedFile().getAbsolutePath(),
                                        indicesFiltros.toArray(new Integer[indicesFiltros.size()])});
                        }
                    }
                });
                jFileChooser1.setApproveButtonText(recursosIdioma.getString("exportar"));
                break;
        }
        jFileChooser1.revalidate();
        jFileChooser1.repaint();
    }

    private void actualizarNomeFicheiro() {
        int numero = 0;
        String base = jFileChooser1.getCurrentDirectory() + "\\";
        String path = base + recursosIdioma.getString("SenTitulo") + "." + ((FileNameExtensionFilter) jFileChooser1.getFileFilter()).getExtensions()[0];
        File f;
        if (new File(path).exists()) {
            do {
                numero++;
                path = base + recursosIdioma.getString("SenTitulo") + " (" + numero + ")." + ((FileNameExtensionFilter) jFileChooser1.getFileFilter()).getExtensions()[0];
                f = new File(path);
            } while (f.exists());
        }
        jFileChooser1.setSelectedFile(new File(path));
    }

    public void amosarDialogo(String mensaxe, int tipo) {
        String titulo;
        String nomeAplicacion = JDataMotion.class.getSimpleName();
        switch (tipo) {
            case ERROR_MESSAGE:
                titulo = nomeAplicacion.concat(" erro");
                break;
            case WARNING_MESSAGE:
                titulo = nomeAplicacion + " " + recursosIdioma.getString("aviso");
                break;
            case INFORMATION_MESSAGE:
                titulo = nomeAplicacion + " " + recursosIdioma.getString("informacion");
                break;
            case QUESTION_MESSAGE:
                titulo = nomeAplicacion + " " + recursosIdioma.getString("pregunta");
                break;
            case PLAIN_MESSAGE:
                titulo = nomeAplicacion + " " + recursosIdioma.getString("mensaxe");
                break;
            default:
                titulo = "";
        }
        JOptionPane.showMessageDialog(this, mensaxe, titulo, tipo);
    }

    private class JTableModelo extends JTable {

        final JPanel columnaNumerais;
        final boolean editable;
        final JPanelActualizable panelDetallarAtributo;
        final JScrollPane taboa;
        final Modelo modelo;
        int columnaTaboaSeleccionada;
        Filler filler;

        public JPanel getColumnaNumerais() {
            return columnaNumerais;
        }

        public JScrollPane getTaboa() {
            return taboa;
        }

        public Modelo getModelo() {
            return modelo;
        }

        public int getColumnaTaboaSeleccionada() {
            return columnaTaboaSeleccionada;
        }

        public void finalizarEdicions() {
            TableCellEditor ce = getCellEditor();
            if (ce != null) {
                ce.stopCellEditing();
            }
            for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
                TableColumn columna = getColumnModel().getColumn(i);
                if (columna.getCellEditor() != null) {
                    columna.getCellEditor().cancelCellEditing();
                }
            }
        }

        public void configurarColumnaNominal(TableColumn column, ArrayList<String> valoresNominais) {
            JComboBox<String> comboBox = new JComboBox<>();
            valoresNominais.stream().forEach((v) -> {
                comboBox.addItem(v);
            });
            DefaultCellEditor defaultCellEditor = new DefaultCellEditor(comboBox);
            defaultCellEditor.setClickCountToStart(2);
            column.setCellEditor(defaultCellEditor);
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            column.setCellRenderer(renderer);
        }

        private void actualizarTaboaConModelo() {
            int numFilasTaboa = ((TableModelPanelModelo) getModel()).getRowCount(), numFilasModelo = modelo.obterNumInstancias(), numColumnasTaboa = getColumnModel().getColumnCount(), numColumnasModelo = modelo.obterNumAtributos();
            if (numFilasTaboa > numFilasModelo) {
                for (int i = numFilasTaboa - 1; i >= numFilasModelo; i--) {
                    ((TableModelPanelModelo) getModel()).removeRow(i);
                }
            } else if (numFilasTaboa < numFilasModelo) {
                for (int i = numFilasModelo - 1; i >= numFilasTaboa; i--) {
                    ArrayList<Object> arrayList = new ArrayList<>();
                    for (int j = 0; j < modelo.obterNumAtributos(); j++) {
                        arrayList.add("");
                    }
                    ((TableModelPanelModelo) getModel()).addRow(arrayList);
                }
            }
            if (numColumnasTaboa > numColumnasModelo) {
                int indiceModeloEliminar = -1, indiceTaboaEliminar = -1;
                for (int j = 0; j < getColumnModel().getColumnCount(); j++) {
                    boolean disponible = false;
                    for (int i = 0; !disponible && i < modelo.getComparableInstances().numAttributes(); i++) {
                        if (modelo.getComparableInstances().attribute(i).name() == getColumnModel().getColumn(j).getHeaderValue()) {
                            disponible = true;
                            break;
                        }
                    }
                    if (disponible == false) {
                        indiceTaboaEliminar = j;
                        indiceModeloEliminar = getColumnModel().getColumn(j).getModelIndex();
                        break;
                    }
                }
                getColumnModel().removeColumn(getColumnModel().getColumn(indiceTaboaEliminar));
                for (int j = 0; j < numColumnasModelo; j++) {
                    TableColumn tcaux = getColumnModel().getColumn(j);
                    if (tcaux.getModelIndex() > indiceModeloEliminar) {
                        tcaux.setModelIndex(tcaux.getModelIndex() - 1);
                    }
                }
            } else if (numColumnasTaboa < numColumnasModelo) {
                for (int i = numColumnasModelo - 1; i >= numColumnasTaboa; i--) {
                    ((TableModelPanelModelo) getModel()).getAtributos().add(modelo.getComparableInstances().attribute(i).name());
                    ((TableModelPanelModelo) getModel()).getDatos().stream().forEach((alo) -> {
                        alo.add("");
                    });
                    getColumnModel().addColumn(new TableColumn(i));
                }
            }
        }

        public JTableModelo(JPanel columnaNumerais, JScrollPane taboa, Modelo modelo, JPanelActualizable panelDetallarAtributo, boolean editable, int columnaTaboaSeleccionada, Filler filler) {
            super();
            this.columnaNumerais = columnaNumerais;
            this.filler = filler;
            this.taboa = taboa;
            this.modelo = modelo;
            this.panelDetallarAtributo = panelDetallarAtributo;
            this.editable = editable;
            this.columnaTaboaSeleccionada = columnaTaboaSeleccionada;
        }

        public void actualizar() {
            resaltarColumnaSeleccionada();
            actualizarTaboaConModelo();
            columnaNumerais.removeAll();
            String data[][] = new String[modelo.obterNumInstancias()][1];
            for (int i = 0; i < data.length; i++) {
                data[i][0] = "<html><body style='white-space:nowrap;'>" + String.valueOf(i + 1) + "</body></html>";
            }
            JTable tablaIndices = new JTable(null);
            tablaIndices.setModel(new DefaultTableModel(data, new String[]{""}) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    tablaIndices.setFocusable(false);
                    tablaIndices.setRowSelectionAllowed(false);
                    return false;
                }
            });
            tablaIndices.setFillsViewportHeight(true);
            columnaNumerais.add(tablaIndices);
            tablaIndices.getColumnModel().getColumn(0).setPreferredWidth(0);
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            tablaIndices.setDefaultRenderer(tablaIndices.getColumnClass(0), renderer);
            tablaIndices.setBackground(Color.LIGHT_GRAY);
            for (int j = 0; j < getColumnModel().getColumnCount(); j++) {
                int columnaModelo = getColumnModel().getColumn(j).getModelIndex();
                String nomeCabeceira = modelo.obterNomeAtributo(columnaModelo);
                if (modelo.getIndiceTemporal() == columnaModelo) {
                    nomeCabeceira = "<html><b style='white-space:nowrap;overflow:hidden;'>" + nomeCabeceira + " (" + recursosIdioma.getString("IT") + ")</b></html>";
                }
                getColumnModel().getColumn(j).setHeaderValue(nomeCabeceira);
                for (int i = 0; i < getRowCount(); i++) {
                    ((TableModelPanelModelo) getModel()).setValueNoFiring(modelo.obterStringDato(i, columnaModelo, false), i, columnaModelo);
                }
                if (modelo.obterListaAtributos().get(columnaModelo).type() == Attribute.NOMINAL) {
                    ArrayList<String> valoresNominais = new ArrayList<>();
                    Enumeration e = modelo.getComparableInstances().attribute(columnaModelo).enumerateValues();
                    while (e.hasMoreElements()) {
                        String el = (String) e.nextElement();
                        valoresNominais.add(el);
                    }
                    valoresNominais.add("");
                    configurarColumnaNominal(getColumnModel().getColumn(j), valoresNominais);
                } else {
                    restablecerConfiguracionColumna(getColumnModel().getColumn(j));
                }
            }
            revalidate();
            repaint();
        }

        public void resaltarColumnaSeleccionada() {
            if (columnaTaboaSeleccionada < 0) {
                columnaTaboaSeleccionada = 0;
            }
            for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
                if (columnaTaboaSeleccionada != i) {
                    getColumnModel().getColumn(i).setHeaderRenderer(null);
                }
            }
            final DefaultTableCellRenderer hr = (DefaultTableCellRenderer) getTableHeader().getDefaultRenderer();
            getColumnModel().getColumn(columnaTaboaSeleccionada).setHeaderRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel lbl = (JLabel) hr.getTableCellRendererComponent(table, value, true, true, row, column);
                    lbl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.RED, 2, false), new EmptyBorder(4, 3, 2, 3)));
                    return lbl;
                }
            });
            panelDetallarAtributo.actualizar(getColumnaModeloSeleccionada(this), modelo);
        }

        private class TableModelPanelModelo extends AbstractTableModel {

            private final List<String> atributos;
            private final List<List<Object>> datos;
            private final boolean editable;

            public List<String> getAtributos() {
                return atributos;
            }

            public List<List<Object>> getDatos() {
                return datos;
            }

            TableModelPanelModelo(List<String> columnNames, List<List<Object>> data, boolean editable) {
                super();
                this.atributos = columnNames;
                this.datos = data;
                this.editable = editable;
            }

            @Override
            public int getColumnCount() {
                return atributos.size();
            }

            @Override
            public int getRowCount() {
                return datos.size();
            }

            @Override
            public String getColumnName(int col) {
                return atributos.get(col);
            }

            @Override
            public Object getValueAt(int row, int col) {
                return datos.get(row).get(col);
            }

            public void addRow(ArrayList<Object> row) {
                datos.add(row);
            }

            public void removeRow(int index) {
                datos.remove(index);
            }

            @Override
            public Class getColumnClass(int c) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return this.editable;
            }

            public void setValueNoFiring(Object value, int row, int col) {
                datos.get(row).set(col, value);
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                setValueNoFiring(value, row, col);
                fireTableCellUpdated(row, col);
            }
        }

        public void restablecerConfiguracionColumna(TableColumn columna) {
            for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
                TableColumn columnaux = getColumnModel().getColumn(i);
                if (columnaux.getCellEditor() != null) {
                    columnaux.getCellEditor().cancelCellEditing();
                }
            }
            columna.setCellEditor(null);
            columna.setCellRenderer(null);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            boolean response = false;
            if (autoResizeMode != AUTO_RESIZE_OFF) {
                if (getParent() instanceof JViewport) {
                    response = ((JViewport) getParent()).getWidth() > getPreferredSize().width;
                }
            }
            filler.setPreferredSize(response ? filler.getMinimumSize() : filler.getMaximumSize());
            return response;
        }

        public void inicializar() {
            removeAll();
            setModel(new TableModelPanelModelo(modelo.obterListaNomesAtributos(), modelo.obterListaDatos(false), editable));
            setFillsViewportHeight(true);
            getColumnModel().addColumnModelListener(new TableColumnModelListener() {
                @Override
                public void columnAdded(TableColumnModelEvent e) {
                }

                @Override
                public void columnRemoved(TableColumnModelEvent e) {
                }

                @Override
                public void columnMoved(TableColumnModelEvent e) {
                    if (e.getFromIndex() == columnaTaboaSeleccionada) {
                        columnaTaboaSeleccionada = e.getToIndex();
                    }
                }

                @Override
                public void columnMarginChanged(ChangeEvent e) {
                }

                @Override
                public void columnSelectionChanged(ListSelectionEvent e) {
                }
            });
            taboa.setViewportView(this);
            getTableHeader().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    columnaTaboaSeleccionada = columnAtPoint(e.getPoint());
                    actualizar();
                }
            });
            actualizar();
        }
    }

    private class JTableModeloMenu extends JTableModelo {

        public JTableModeloMenu(JPanel columnaNumerais, JScrollPane taboa, Modelo modelo, JPanelActualizable panelDetallarAtributo, int columnaTaboaSeleccionada, Filler filler) {
            super(columnaNumerais, taboa, modelo, panelDetallarAtributo, true, columnaTaboaSeleccionada, filler);
        }

        @Override
        public void resaltarColumnaSeleccionada() {
            super.resaltarColumnaSeleccionada();
            activarBorrarAtributo(columnaTaboaSeleccionada > -1);
            activarRenomearAtributo(columnaTaboaSeleccionada > -1);
        }

        @Override
        public void inicializar() {
            super.inicializar();
            getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                activarBorrarDatos(!jTableModelo.getSelectionModel().isSelectionEmpty());
            });
            getModel().addTableModelListener((TableModelEvent e) -> {
                int fila = e.getFirstRow(), columna = e.getColumn();
                Object o = getModel().getValueAt(fila, columna);
                meuControlador.manexarEvento(Controlador.MUDAR_DATO, new Object[]{fila, columna, o});
            });
            getTableHeader().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        botonIndiceTemporal.setSelected(modelo.getIndiceTemporal() == getColumnaModeloSeleccionada());
                        popupConfigurarAtributo.show(e.getComponent(), e.getX(), e.getY());
                        botonOcultarColumna.setSelected(!getTableHeader().isVisible());
                        for (Component c : menuTipo.getMenuComponents()) {
                            ((JRadioButtonMenuItem) c).setSelected(false);
                        }
                        switch (modelo.obterTipoAtributo(getColumnaModeloSeleccionada())) {
                            case "numérico":
                                botonNumerico.setSelected(true);
                                break;
                            case "nominal":
                                botonNominal.setSelected(true);
                                break;
                            case "string":
                                botonString.setSelected(true);
                                break;
                            case "data":
                                botonData.setSelected(true);
                                break;
                        }
                    }
                }
            });
            actualizar();
        }

    }

    private void activarBorrarDatos(boolean activar) {
        jMenuItem14.setEnabled(activar);
    }

    private void activarBorrarAtributo(boolean activar) {
        jMenuItem19.setEnabled(activar);
    }

    private void activarRenomearAtributo(boolean activar) {
        jMenuItem20.setEnabled(activar);
    }

    private class PanelFiltro extends javax.swing.JPanel {

        private String stringParameters;
        private final int indiceFiltro;

        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton15;
        private javax.swing.JButton jButton16;
        private javax.swing.JButton jButton2;
        javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel12;

        public void setNomeFiltro(String nomeFiltro) {
            jLabel9.setText("<html><p style='text-align:center;'>" + nomeFiltro + "</p></html>");
        }

        public boolean isFiltroSeleccionado() {
            return jCheckBox1.isSelected();
        }

        public void setFiltroSeleccionado(boolean seleccionado) {
            jCheckBox1.setSelected(seleccionado);
        }

        public void addParameter(String name, Parameter p) {
            stringParameters += "<p style='line-height:0.5;'>- " + name + ": " + (p.getValue() != null ? p.getValue().toString() : "?") + "</p>";
            jLabel10.setText("<html>" + stringParameters + "</html>");
        }

        public void addAtributoFiltrado(Attribute atributo) {
            addParameter(Vista.recursosIdioma.getString("atributo"), new StringParameter(atributo != null ? atributo.name() : "?"));
        }

        public void setIcon(String url) {
            jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource(url)));
        }

        public void removeParameters() {
            stringParameters = "";
            jLabel10.setText("");
        }

        public PanelFiltro(int indiceFiltro) {
            this.indiceFiltro = indiceFiltro;
            this.stringParameters = "";
            initComponents();
            jButton2.setVisible(indiceFiltro > 0);
            jButton1.setVisible(indiceFiltro < meuModelo.contarFiltros() - 1);
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
            java.awt.GridBagConstraints gridBagConstraints;

            jLabel8 = new javax.swing.JLabel();
            jLabel9 = new javax.swing.JLabel();
            jLabel10 = new javax.swing.JLabel();
            jPanel12 = new javax.swing.JPanel();
            jButton15 = new javax.swing.JButton();
            jButton16 = new javax.swing.JButton();
            jButton1 = new javax.swing.JButton();
            jButton2 = new javax.swing.JButton();
            jCheckBox1 = new javax.swing.JCheckBox();

            setMaximumSize(new java.awt.Dimension(150, 2147483647));
            setMinimumSize(new java.awt.Dimension(150, 250));
            setPreferredSize(new java.awt.Dimension(150, 250));
            setLayout(new java.awt.GridBagLayout());

            jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/filtro.png"))); // NOI18N
            jLabel8.setMaximumSize(new java.awt.Dimension(100, 50));
            jLabel8.setMinimumSize(new java.awt.Dimension(100, 50));
            jLabel8.setPreferredSize(new java.awt.Dimension(100, 50));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            add(jLabel8, gridBagConstraints);

            jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel9.setText(" ");
            jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
            jLabel9.setMaximumSize(new java.awt.Dimension(100, 90));
            jLabel9.setMinimumSize(new java.awt.Dimension(100, 90));
            jLabel9.setPreferredSize(new java.awt.Dimension(100, 90));
            jLabel9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            add(jLabel9, gridBagConstraints);

            jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
            jLabel10.setText(" ");
            jLabel10.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            jLabel10.setMaximumSize(new java.awt.Dimension(100, 90));
            jLabel10.setMinimumSize(new java.awt.Dimension(100, 90));
            jLabel10.setPreferredSize(new java.awt.Dimension(100, 90));
            jLabel10.setRequestFocusEnabled(false);
            jLabel10.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            add(jLabel10, gridBagConstraints);

            jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/configurarFiltro.jpg"))); // NOI18N
            jButton15.setPreferredSize(new java.awt.Dimension(20, 20));
            jButton15.addActionListener((java.awt.event.ActionEvent evt) -> {
                jButton15ActionPerformed(evt);
            });

            jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/eliminarFiltro.jpg"))); // NOI18N
            jButton16.setPreferredSize(new java.awt.Dimension(20, 20));
            jButton16.addActionListener((java.awt.event.ActionEvent evt) -> {
                jButton16ActionPerformed(evt);
            });

            javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
            jPanel12.setLayout(jPanel12Layout);
            jPanel12Layout.setHorizontalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
            jPanel12Layout.setVerticalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            add(jPanel12, gridBagConstraints);

            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/intercambiarDereita.gif"))); // NOI18N
            jButton1.setMaximumSize(new java.awt.Dimension(25, 25));
            jButton1.setMinimumSize(new java.awt.Dimension(25, 25));
            jButton1.setPreferredSize(new java.awt.Dimension(25, 25));
            jButton1.addActionListener((java.awt.event.ActionEvent evt) -> {
                jButton1ActionPerformed(evt);
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridy = 1;
            add(jButton1, gridBagConstraints);

            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/intercambiarEsquerda.gif"))); // NOI18N
            jButton2.setMaximumSize(new java.awt.Dimension(25, 25));
            jButton2.setMinimumSize(new java.awt.Dimension(25, 25));
            jButton2.setPreferredSize(new java.awt.Dimension(25, 25));
            jButton2.addActionListener((java.awt.event.ActionEvent evt) -> {
                jButton2ActionPerformed(evt);
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            add(jButton2, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 4;
            add(jCheckBox1, gridBagConstraints);
        }// </editor-fold>                        

        private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {
            Map<String, Parameter> config = obterConfiguracionFiltro(indiceFiltro);
            if (config != null) {
                meuControlador.manexarEvento(Controlador.CONFIGURAR_FILTRO, new Object[]{indiceFiltro, config});
            }
        }

        private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {
            meuControlador.manexarEvento(Controlador.ELIMINAR_FILTRO, indiceFiltro);
        }

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            meuControlador.manexarEvento(Controlador.INTERCAMBIAR_FILTROS, new Object[]{indiceFiltro, indiceFiltro + 1});
        }

        private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
            meuControlador.manexarEvento(Controlador.INTERCAMBIAR_FILTROS, new Object[]{indiceFiltro, indiceFiltro - 1});
        }
    }

    private void inicializarPaneis() {
        jTableModelo = new JTableModeloMenu(jPanel10, jScrollPane3, meuModelo, (JPanelActualizable) jPanel15, 0, filler2);
        jTableModelo.inicializar();
    }

    private void actualizarPaneis() {
        jTableModelo.actualizar();
        ((JPanelActualizable) jPanel15).actualizar(getColumnaModeloSeleccionada(jTableModelo), meuModelo);
    }

    private class JPanelActualizable extends JPanel {

        private JFreeChart createChartAtributoNumerico(ComparableInstances instancias, int indiceAtributoNumerico, int indiceAtributoNominal) {
            JFreeChart chart = null;
            Attribute atributo = instancias.attribute(indiceAtributoNumerico);
            if (atributo.isNumeric()) {
                if (indiceAtributoNominal > -1) {
                    HistogramDataset histogramdataset = new HistogramDataset();
                    List<List<Instance>> ad = new ArrayList<>();
                    for (int j = 0; j < instancias.attribute(indiceAtributoNominal).numValues() + 1; j++) {
                        ad.add(new ArrayList<>());
                    }
                    for (int j = 0; j < instancias.numInstances(); j++) {
                        if (instancias.instance(j).isMissing(indiceAtributoNominal)) {
                            ad.get(0).add(instancias.instance(j));
                        } else {
                            ad.get((int) (instancias.instance(j).value(indiceAtributoNominal) + 1.0)).add(instancias.instance(j));
                        }
                    }
                    for (int i = 0; i < ad.size(); i++) {
                        double[] target = new double[ad.get(i).size()];
                        for (int j = 0; j < target.length; j++) {
                            target[j] = ad.get(i).get(j).value(indiceAtributoNumerico);
                        }
                        ComparableInstances aux = new ComparableInstances(instancias);
                        for (int j = aux.numInstances() - 1; j >= 0; j--) {
                            aux.delete(j);
                        }
                        for (int j = 0; j < ad.get(i).size(); j++) {
                            aux.add(ad.get(i).get(j));
                        }
                        int bin = getFreedmanDiaconisNumberOfBins(aux, indiceAtributoNumerico);
                        Double max = Utils.getMaximo(aux, indiceAtributoNumerico),
                                min = Utils.getMinimo(aux, indiceAtributoNumerico);
                        min = min != null ? min : 0.0;
                        max = max != null ? max : 0.0;
                        histogramdataset.addSeries(i == 0 ? recursosIdioma.getString("senDefinir") : instancias.attribute(indiceAtributoNominal).value(i - 1), target, bin, min, max);
                    }
                    chart = ChartFactory.createHistogram(null, null, null, histogramdataset, PlotOrientation.VERTICAL, true, true, false);
                    XYPlot xyplot = (XYPlot) chart.getPlot();
                    xyplot.setForegroundAlpha(0.85F);
                    XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
                    xybarrenderer.setDrawBarOutline(false);
                } else {
                    HistogramDataset dataset = new HistogramDataset();
                    List<Double> v = new ArrayList<>();
                    for (int j = 0; j < instancias.numInstances(); j++) {
                        if (!instancias.instance(j).isMissing(atributo)) {
                            v.add(instancias.instance(j).value(atributo));
                        }
                    }
                    int bin = getFreedmanDiaconisNumberOfBins(instancias, indiceAtributoNumerico);
                    Double max = Utils.getMaximo(instancias, indiceAtributoNumerico),
                            min = Utils.getMinimo(instancias, indiceAtributoNumerico);
                    min = min != null ? min : 0.0;
                    max = max != null ? max : 0.0;
                    dataset.addSeries(-1, ArrayUtils.toPrimitive(v.toArray(new Double[v.size()])), bin, min, max);
                    chart = ChartFactory.createHistogram(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
                }
                chart.setBackgroundPaint(new Color(240, 240, 240));
                XYPlot xyplot = (XYPlot) chart.getPlot();
                xyplot.setForegroundAlpha(0.7F);
                xyplot.setBackgroundPaint(Color.WHITE);
                xyplot.setDomainGridlinePaint(new Color(150, 150, 150));
                xyplot.setRangeGridlinePaint(new Color(150, 150, 150));
                XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
                xybarrenderer.setShadowVisible(false);
                xybarrenderer.setBarPainter(new StandardXYBarPainter());
            }
            return chart;
        }

        private JFreeChart createChartAtributoNominal(ComparableInstances instancias, int indiceAtributoNominal) {
            JFreeChart chart = null;
            Attribute atributo = instancias.attribute(indiceAtributoNominal);
            if (atributo.isNominal()) {
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                int[] reconto = new int[atributo.numValues() + 1];
                for (int i = 0; i < reconto.length; i++) {
                    reconto[i] = 0;
                }
                for (int i = 0; i < instancias.numInstances(); i++) {
                    Instance ins = instancias.instance(i);
                    if (ins.isMissing(atributo)) {
                        reconto[0]++;
                    } else {
                        reconto[(int) ins.value(atributo) + 1]++;
                    }
                }
                for (int i = 0; i < reconto.length; i++) {
                    dataset.addValue((Number) reconto[i], -1, i > 0 ? atributo.value(i - 1) : recursosIdioma.getString("senDefinir"));
                }
                chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
                chart.setBackgroundPaint(new Color(240, 240, 240));
                CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
                categoryPlot.setForegroundAlpha(0.7F);
                categoryPlot.setBackgroundPaint(Color.WHITE);
                categoryPlot.setDomainGridlinePaint(new Color(150, 150, 150));
                categoryPlot.setRangeGridlinePaint(new Color(150, 150, 150));
                BarRenderer xybarrenderer = (BarRenderer) categoryPlot.getRenderer();
                xybarrenderer.setShadowVisible(false);
                xybarrenderer.setBarPainter(new StandardBarPainter());
                chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
            }
            return chart;
        }

        public void actualizar(int columnaModeloSeleccionada, Modelo modelo) {
            removeAll();
            if (columnaModeloSeleccionada > -1 && modelo.obterNumAtributos() > columnaModeloSeleccionada) {
                add(new JLabel(recursosIdioma.getString("nomeAtributo") + ": " + modelo.obterNomeAtributo(columnaModeloSeleccionada)));
                add(new JLabel(recursosIdioma.getString("tipo") + ": " + recursosIdioma.getString(modelo.obterTipoAtributo(columnaModeloSeleccionada))));
                add(new JLabel(recursosIdioma.getString("indiceTemporal") + ": " + (modelo.getIndiceTemporal() == columnaModeloSeleccionada ? recursosIdioma.getString("si") : recursosIdioma.getString("non"))));
                switch (modelo.obterTipoAtributo(columnaModeloSeleccionada)) {
                    case "numérico":
                        Double max = Utils.getMaximo(modelo.getComparableInstances(), columnaModeloSeleccionada),
                         min = Utils.getMinimo(modelo.getComparableInstances(), columnaModeloSeleccionada),
                         media = Utils.getMedia(modelo.getComparableInstances(), columnaModeloSeleccionada),
                         desvTipica = Utils.getDesviacionTipica(modelo.getComparableInstances(), columnaModeloSeleccionada),
                         varianza = Utils.getVarianza(modelo.getComparableInstances(), columnaModeloSeleccionada);
                        add(new JLabel(recursosIdioma.getString("maximo") + ": " + (max != null ? max : "-")));
                        add(new JLabel(recursosIdioma.getString("minimo") + ": " + (min != null ? min : "-")));
                        add(new JLabel(recursosIdioma.getString("media") + ": " + (media != null ? media : "-")));
                        add(new JLabel(recursosIdioma.getString("varianza") + ": " + (varianza != null && Double.compare(varianza, Double.NaN) != 0 ? varianza : "-")));
                        add(new JLabel(recursosIdioma.getString("desviacionTipica") + ": " + (desvTipica != null && Double.compare(desvTipica, Double.NaN) != 0 ? desvTipica : "-")));
                        ChartPanel cp = new ChartPanel(createChartAtributoNumerico(modelo.getComparableInstances(), columnaModeloSeleccionada, modelo.getIndiceAtributoNominal()), false, true, true, true, true);
                        configureChartPanel(cp, 0, (int) Math.round(getPreferredSize().getWidth() * cp.getPreferredSize().getHeight() / cp.getPreferredSize().getWidth()));
                        add(cp);
                        break;
                    case "nominal":
                        ArrayList<Integer> coincidencias = new ArrayList<>();
                        for (int i = 0; i < modelo.getComparableInstances().attribute(columnaModeloSeleccionada).numValues() + 1; i++) {
                            coincidencias.add(0);
                        }
                        for (int i = 0; i < modelo.getComparableInstances().numInstances(); i++) {
                            int index = modelo.getComparableInstances().instance(i).isMissing(columnaModeloSeleccionada) ? 0 : (int) modelo.getComparableInstances().instance(i).value(columnaModeloSeleccionada) + 1;
                            coincidencias.set(index, coincidencias.get(index) + 1);
                        }
                        add(new JLabel(recursosIdioma.getString("valores") + ": "));
                        add(new JLabel("  " + recursosIdioma.getString("senDefinir") + " (" + coincidencias.get(0) + ")"));
                        for (int i = 1; i < coincidencias.size(); i++) {
                            add(new JLabel("  " + modelo.getComparableInstances().attribute(columnaModeloSeleccionada).value(i - 1) + " (" + coincidencias.get(i) + ")"));
                        }
                        cp = new ChartPanelConfigurable(createChartAtributoNominal(modelo.getComparableInstances(), columnaModeloSeleccionada), false, true, true, true, true);
                        configureChartPanel(cp, 0, (int) Math.round(getPreferredSize().getWidth() * cp.getPreferredSize().getHeight() / cp.getPreferredSize().getWidth()));
                        add(cp);
                        break;
                    case "string":
                        break;
                    case "data":
                        break;
                }
            }
            revalidate();
            repaint();
        }
    }

    private static int getFreedmanDiaconisNumberOfBins(ComparableInstances comparableInstances, int indice) {
        int n = Utils.getNumeroObservaciones(comparableInstances, indice);
        if (n == 0) {
            return 1;
        }
        return Math.min(n, Math.max(1, Math.round((float) Math.ceil(1.0 * Math.pow(n, 1.0 / 3) * (Utils.getMaximo(comparableInstances, indice) - Utils.getMinimo(comparableInstances, indice)) / (2 * (Utils.getValorPercentil(comparableInstances, 75, indice) - Utils.getValorPercentil(comparableInstances, 25, indice)))))));
    }

    private class AutoSuggestor {

        private final JTextComponent textComp;
        private final Window container;
        private final JPanel suggestionsPanel;
        private final JWindow autoSuggestionPopUpWindow;
        private String typedWord;
        private final ArrayList<String> dictionary = new ArrayList<>();
        private int currentIndexOfSpace, tW, tH;
        private final DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                checkForAndShowSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                checkForAndShowSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                checkForAndShowSuggestions();
            }
        };
        private final Color suggestionsTextColor;
        private final Color suggestionFocusedColor;

        public AutoSuggestor(JTextComponent textComp, Window mainWindow, ArrayList<String> words, Color popUpBackground, Color textColor, Color suggestionFocusedColor, float opacity) {
            this.textComp = textComp;
            this.suggestionsTextColor = textColor;
            this.container = mainWindow;
            this.suggestionFocusedColor = suggestionFocusedColor;
            this.textComp.getDocument().addDocumentListener(documentListener);
            setDictionary(words);
            typedWord = "";
            currentIndexOfSpace = 0;
            tW = 0;
            tH = 0;
            autoSuggestionPopUpWindow = new JWindow(mainWindow);
            autoSuggestionPopUpWindow.setOpacity(opacity);
            suggestionsPanel = new JPanel();
            suggestionsPanel.setLayout(new GridLayout(0, 1));
            suggestionsPanel.setBackground(popUpBackground);
            addKeyBindingToRequestFocusInPopUpWindow();
        }

        private void addKeyBindingToRequestFocusInPopUpWindow() {
            textComp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
            textComp.getActionMap().put("Down released", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {//focuses the first label on popwindow
                    for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
                        if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
                            ((SuggestionLabel) suggestionsPanel.getComponent(i)).setFocused(true);
                            autoSuggestionPopUpWindow.toFront();
                            autoSuggestionPopUpWindow.requestFocusInWindow();
                            suggestionsPanel.requestFocusInWindow();
                            suggestionsPanel.getComponent(i).requestFocusInWindow();
                            break;
                        }
                    }
                }
            });
            suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
            suggestionsPanel.getActionMap().put("Down released", new AbstractAction() {
                int lastFocusableIndex = 0;

                @Override
                public void actionPerformed(ActionEvent ae) {//allows scrolling of labels in pop window (I know very hacky for now :))

                    ArrayList<SuggestionLabel> sls = getAddedSuggestionLabels();
                    int max = sls.size();

                    if (max > 1) {//more than 1 suggestion
                        for (int i = 0; i < max; i++) {
                            SuggestionLabel sl = sls.get(i);
                            if (sl.isFocused()) {
                                if (lastFocusableIndex == max - 1) {
                                    lastFocusableIndex = 0;
                                    sl.setFocused(false);
                                    autoSuggestionPopUpWindow.setVisible(false);
                                    setFocusToTextField();
                                    checkForAndShowSuggestions();//fire method as if document listener change occured and fired it

                                } else {
                                    sl.setFocused(false);
                                    lastFocusableIndex = i;
                                }
                            } else if (lastFocusableIndex <= i) {
                                if (i < max) {
                                    sl.setFocused(true);
                                    autoSuggestionPopUpWindow.toFront();
                                    autoSuggestionPopUpWindow.requestFocusInWindow();
                                    suggestionsPanel.requestFocusInWindow();
                                    suggestionsPanel.getComponent(i).requestFocusInWindow();
                                    lastFocusableIndex = i;
                                    break;
                                }
                            }
                        }
                    } else {//only a single suggestion was given
                        autoSuggestionPopUpWindow.setVisible(false);
                        setFocusToTextField();
                        checkForAndShowSuggestions();//fire method as if document listener change occured and fired it
                    }
                }
            });
        }

        private void setFocusToTextField() {
            container.toFront();
            container.requestFocusInWindow();
            textComp.requestFocusInWindow();
        }

        public ArrayList<SuggestionLabel> getAddedSuggestionLabels() {
            ArrayList<SuggestionLabel> sls = new ArrayList<>();
            for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
                if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
                    SuggestionLabel sl = (SuggestionLabel) suggestionsPanel.getComponent(i);
                    sls.add(sl);
                }
            }
            return sls;
        }

        private void checkForAndShowSuggestions() {
            typedWord = getCurrentlyTypedWord();

            suggestionsPanel.removeAll();//remove previos words/jlabels that were added

            //used to calcualte size of JWindow as new Jlabels are added
            tW = 0;
            tH = 0;

            boolean added = wordTyped(typedWord);

            if (!added) {
                if (autoSuggestionPopUpWindow.isVisible()) {
                    autoSuggestionPopUpWindow.setVisible(false);
                }
            } else {
                showPopUpWindow();
                setFocusToTextField();
            }
        }

        protected void addWordToSuggestions(String word) {
            SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this);

            calculatePopUpWindowSize(suggestionLabel);

            suggestionsPanel.add(suggestionLabel);
        }

        public String getCurrentlyTypedWord() {//get newest word after last white spaceif any or the first word if no white spaces
            String word = null;
            Pattern p = Pattern.compile("^.*\\$[12]\\.(`[a-zA-Z0-9]*)?$");
            Matcher m = p.matcher(getTextField().getText().substring(0, Math.min(getTextField().getCaretPosition(), getTextField().getText().length())));
            if (m.matches()) {
                word = m.group(1) != null ? m.group(1) : "";
            }
            return word;
        }

        private void calculatePopUpWindowSize(JLabel label) {
            //so we can size the JWindow correctly
            if (tW < label.getPreferredSize().width) {
                tW = label.getPreferredSize().width;
            }
            tH += label.getPreferredSize().height;
        }

        private void showPopUpWindow() {
            autoSuggestionPopUpWindow.getContentPane().add(suggestionsPanel);
            autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textComp.getWidth(), 30));
            autoSuggestionPopUpWindow.setSize(tW, tH);
            autoSuggestionPopUpWindow.setVisible(true);

            Integer windowX = null;
            Integer windowY = null;

            if (textComp instanceof JTextField) {//calculate x and y for JWindow at bottom of JTextField
                windowX = container.getX() + textComp.getX() + 5;
                if (suggestionsPanel.getHeight() > autoSuggestionPopUpWindow.getMinimumSize().height) {
                    windowY = container.getY() + textComp.getY() + textComp.getHeight() + autoSuggestionPopUpWindow.getMinimumSize().height;
                } else {
                    windowY = container.getY() + textComp.getY() + textComp.getHeight() + autoSuggestionPopUpWindow.getHeight();
                }
            } else {//calculate x and y for JWindow on any JTextComponent using the carets position
                Rectangle rect = null;
                try {
                    rect = textComp.getUI().modelToView(textComp, textComp.getCaret().getDot());//get carets position
                } catch (BadLocationException ex) {
                }

                if (rect != null) {
                    windowX = (int) (rect.getX() + 15);
                    windowY = (int) (rect.getY() + (rect.getHeight() * 3));
                }
            }

            if (windowX != null && windowY != null) {
                autoSuggestionPopUpWindow.setLocation(windowX, windowY);
            }
            autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textComp.getWidth(), 30));
            autoSuggestionPopUpWindow.revalidate();
            autoSuggestionPopUpWindow.repaint();

        }

        private void setDictionary(ArrayList<String> words) {
            dictionary.clear();
            if (words == null) {
                return;//so we can call constructor with null value for dictionary without exception thrown
            }
            words.stream().forEach((word) -> {
                dictionary.add(word);
            });
        }

        public JWindow getAutoSuggestionPopUpWindow() {
            return autoSuggestionPopUpWindow;
        }

        public Window getContainer() {
            return container;
        }

        public JTextComponent getTextField() {
            return textComp;
        }

        public void addToDictionary(String word) {
            dictionary.add(word);
        }

        boolean wordTyped(String typedWord) {

            if (typedWord.isEmpty()) {
                return false;
            }

            boolean suggestionAdded = false;
            for (String word : dictionary) {//get words in the dictionary which we added
                boolean fullymatches = true;
                for (int i = 0; i < typedWord.length(); i++) {//each string in the word
                    if (!typedWord.toLowerCase().matches("^.*\\$[12]\\.(`[a-zA-Z0-9]*)?$") || !word.toLowerCase().matches("^" + typedWord.substring(typedWord.lastIndexOf(".") + 1) + ".*$")) {//check for match
                        fullymatches = false;
                        break;
                    }
                }
                if (fullymatches) {
                    addWordToSuggestions(word);
                    suggestionAdded = true;
                }
            }
            return suggestionAdded;
        }
    }

    class SuggestionLabel extends JLabel {

        private boolean focused = false;
        private final JWindow autoSuggestionsPopUpWindow;
        private final JTextComponent textComponent;
        private final AutoSuggestor autoSuggestor;
        private final Color suggestionsTextColor, suggestionBorderColor;

        public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, AutoSuggestor autoSuggestor) {
            super(string);

            this.suggestionsTextColor = suggestionsTextColor;
            this.autoSuggestor = autoSuggestor;
            this.textComponent = autoSuggestor.getTextField();
            this.suggestionBorderColor = borderColor;
            this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();

            initComponent();
        }

        private void initComponent() {
            setFocusable(true);
            setForeground(suggestionsTextColor);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    super.mouseClicked(me);

                    replaceWithSuggestedText();

                    autoSuggestionsPopUpWindow.setVisible(false);
                }
            });

            getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
            getActionMap().put("Enter released", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    replaceWithSuggestedText();
                    autoSuggestionsPopUpWindow.setVisible(false);
                }
            });
        }

        public void setFocused(boolean focused) {
            if (focused) {
                setBorder(new LineBorder(suggestionBorderColor));
            } else {
                setBorder(null);
            }
            repaint();
            this.focused = focused;
        }

        public boolean isFocused() {
            return focused;
        }

        private void replaceWithSuggestedText() {
            String suggestedWord = getText();
            String text = textComponent.getText().substring(0, Math.min(textComponent.getCaretPosition(), textComponent.getText().length()));
            String typedWord = autoSuggestor.getCurrentlyTypedWord();
            String t = text.substring(0, text.lastIndexOf(typedWord));
            String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
            textComponent.setText(tmp.concat(textComponent.getText().substring(textComponent.getCaretPosition())));
            textComponent.setCaretPosition(tmp.length());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JRadioButtonMenuItem botonData;
    javax.swing.JRadioButtonMenuItem botonIndiceTemporal;
    javax.swing.JRadioButtonMenuItem botonNominal;
    javax.swing.JRadioButtonMenuItem botonNumerico;
    javax.swing.JRadioButtonMenuItem botonOcultarColumna;
    javax.swing.JRadioButtonMenuItem botonString;
    javax.swing.ButtonGroup buttonGroup2;
    javax.swing.Box.Filler filler1;
    javax.swing.Box.Filler filler2;
    javax.swing.Box.Filler filler3;
    javax.swing.Box.Filler filler4;
    javax.swing.JButton jButton1;
    javax.swing.JButton jButton10;
    javax.swing.JButton jButton11;
    javax.swing.JButton jButton12;
    javax.swing.JButton jButton13;
    javax.swing.JButton jButton14;
    javax.swing.JButton jButton15;
    javax.swing.JButton jButton16;
    javax.swing.JButton jButton17;
    javax.swing.JButton jButton18;
    javax.swing.JButton jButton2;
    javax.swing.JButton jButton3;
    javax.swing.JButton jButton4;
    javax.swing.JButton jButton5;
    javax.swing.JButton jButton6;
    javax.swing.JButton jButton7;
    javax.swing.JButton jButton8;
    javax.swing.JButton jButton9;
    javax.swing.JComboBox<String> jComboBox1;
    javax.swing.JDialog jDialog1;
    javax.swing.JDialog jDialog2;
    javax.swing.JDialog jDialog3;
    javax.swing.JDialog jDialog4;
    javax.swing.JFileChooser jFileChooser1;
    javax.swing.JFrame jFrame1;
    javax.swing.JFrame jFrameModeloParcial;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel10;
    javax.swing.JLabel jLabel11;
    javax.swing.JLabel jLabel12;
    javax.swing.JLabel jLabel13;
    javax.swing.JLabel jLabel14;
    javax.swing.JLabel jLabel15;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    javax.swing.JLabel jLabel4;
    javax.swing.JLabel jLabel5;
    javax.swing.JLabel jLabel6;
    javax.swing.JLabel jLabel7;
    javax.swing.JLabel jLabel8;
    javax.swing.JLayeredPane jLayeredPane1;
    javax.swing.JList<IFilter> jList1;
    javax.swing.JMenu jMenu1;
    javax.swing.JMenu jMenu2;
    javax.swing.JMenu jMenu3;
    javax.swing.JMenu jMenu4;
    javax.swing.JMenu jMenu5;
    javax.swing.JMenu jMenu6;
    javax.swing.JMenu jMenu7;
    javax.swing.JMenu jMenu8;
    javax.swing.JMenuBar jMenuBar1;
    javax.swing.JMenuItem jMenuItem1;
    javax.swing.JMenuItem jMenuItem10;
    javax.swing.JMenuItem jMenuItem11;
    javax.swing.JMenuItem jMenuItem12;
    javax.swing.JMenuItem jMenuItem13;
    javax.swing.JMenuItem jMenuItem14;
    javax.swing.JMenuItem jMenuItem15;
    javax.swing.JMenuItem jMenuItem16;
    javax.swing.JMenuItem jMenuItem17;
    javax.swing.JMenuItem jMenuItem18;
    javax.swing.JMenuItem jMenuItem19;
    javax.swing.JMenuItem jMenuItem2;
    javax.swing.JMenuItem jMenuItem20;
    javax.swing.JMenuItem jMenuItem21;
    javax.swing.JMenuItem jMenuItem22;
    javax.swing.JMenuItem jMenuItem23;
    javax.swing.JMenuItem jMenuItem24;
    javax.swing.JMenuItem jMenuItem25;
    javax.swing.JMenuItem jMenuItem26;
    javax.swing.JMenuItem jMenuItem3;
    javax.swing.JMenuItem jMenuItem4;
    javax.swing.JMenuItem jMenuItem5;
    javax.swing.JMenuItem jMenuItem6;
    javax.swing.JMenuItem jMenuItem7;
    javax.swing.JMenuItem jMenuItem8;
    javax.swing.JMenuItem jMenuItem9;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel10;
    javax.swing.JPanel jPanel12;
    javax.swing.JPanel jPanel15;
    javax.swing.JPanel jPanel2;
    javax.swing.JPanel jPanel3;
    javax.swing.JPanel jPanel4;
    javax.swing.JPanel jPanel5;
    javax.swing.JPanel jPanel6;
    javax.swing.JPanel jPanel7;
    javax.swing.JPanel jPanel8;
    javax.swing.JPopupMenu jPopupMenu1;
    javax.swing.JProgressBar jProgressBar1;
    javax.swing.JRadioButton jRadioButton2;
    javax.swing.JRadioButton jRadioButton3;
    javax.swing.JRadioButton jRadioButton4;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane10;
    javax.swing.JScrollPane jScrollPane11;
    javax.swing.JScrollPane jScrollPane13;
    javax.swing.JScrollPane jScrollPane14;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JScrollPane jScrollPane3;
    javax.swing.JScrollPane jScrollPane4;
    javax.swing.JScrollPane jScrollPane5;
    javax.swing.JScrollPane jScrollPane7;
    javax.swing.JScrollPane jScrollPane8;
    javax.swing.JScrollPane jScrollPane9;
    javax.swing.JPopupMenu.Separator jSeparator1;
    javax.swing.JPopupMenu.Separator jSeparator2;
    javax.swing.JPopupMenu.Separator jSeparator3;
    javax.swing.JPopupMenu.Separator jSeparator4;
    javax.swing.JPopupMenu.Separator jSeparator5;
    javax.swing.JPopupMenu.Separator jSeparator6;
    javax.swing.JPopupMenu.Separator jSeparator7;
    javax.swing.JPopupMenu.Separator jSeparator8;
    javax.swing.JSlider jSlider1;
    javax.swing.JTabbedPane jTabbedPane1;
    javax.swing.JTextField jTextField1;
    javax.swing.JTextField jTextField2;
    javax.swing.JTextField jTextField3;
    javax.swing.JTextField jTextField6;
    javax.swing.JMenu menuTipo;
    javax.swing.JPanel panelDetallarAtributo;
    javax.swing.JPopupMenu popupConfigurarAtributo;
    // End of variables declaration//GEN-END:variables
}
