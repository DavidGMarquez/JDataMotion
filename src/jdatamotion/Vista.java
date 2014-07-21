package jdatamotion;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import jdatamotion.ManexadorScatterPlots.JFrameChartPanel;
import jdatamotion.ManexadorScatterPlots.ScatterPlot;
import jdatamotion.sesions.Sesion;
import jdatamotion.sesions.SesionVista;
import jdatamotion.sesions.Sesionizable;
import org.jfree.chart.ChartPanel;
import org.jsoup.Jsoup;
import weka.core.Attribute;

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
    private static final int EXPLORADOR_ABRIR_FICHEIRO = 0;
    private static final int EXPLORADOR_ABRIR_SESION = 1;
    private static final int EXPLORADOR_GARDAR_SESION = 2;
    private static final int EXPLORADOR_GARDAR_FICHEIRO = 3;
    private transient Modelo meuModelo;
    private final TarefaProgreso task;
    private final ExecutorService cachedPool;
    private ManexadorScatterPlots mansp;
    private transient Controlador meuControlador;
    private int ultimaColumnaModeloSeleccionada;
    private boolean scatterPlotsVisibles[][];
    private boolean pulsarSlider;
    private int ultimoEstadoReproductor;
    public static ResourceBundle bundle = ResourceBundle.getBundle("jdatamotion/idiomas/Bundle", new Locale("en"));
    private static final String ficheiroConfiguracion = "configuracion.properties";

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public final void reset() {
        this.ultimaColumnaModeloSeleccionada = -1;
        this.scatterPlotsVisibles = new boolean[0][0];
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("estadoReproductor")) {
            int newValue = (int) evt.getNewValue();
            if (newValue != ManexadorScatterPlots.FREEZE) {
                for (ActionListener al : jButton8.getActionListeners()) {
                    jButton8.removeActionListener(al);
                }
                switch (newValue) {
                    case ManexadorScatterPlots.PLAY:
                        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/pause.png")));
                        jButton8.addActionListener((ActionEvent evt1) -> {
                            mansp.pause();
                        });
                        break;
                    case ManexadorScatterPlots.PAUSE:
                        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/play.png")));
                        jButton8.addActionListener((ActionEvent evt1) -> {
                            mansp.play();
                        });
                        break;
                }
            }
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
        Properties configFile = new Properties();
        try {
            configFile.load(new FileInputStream(ficheiroConfiguracion));
            if (configFile.getProperty("locale") != null) {
                Locale.setDefault(new Locale(configFile.getProperty("locale")));
            }
        } catch (FileNotFoundException ex1) {
            try {
                new File(ficheiroConfiguracion).createNewFile();
                FileInputStream is = new FileInputStream(ficheiroConfiguracion);
                configFile.load(is);
                configFile.setProperty("locale", Locale.getDefault().toString());
                configFile.store(new FileOutputStream(ficheiroConfiguracion), null);
            } catch (IOException ex) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            if (Controlador.debug) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        reset();
        initComponents();
        this.pulsarSlider = false;
        this.jPanel8.setVisible(false);
        this.task = new TarefaProgreso(jProgressBar1);
        this.cachedPool = Executors.newCachedThreadPool();
    }

    private int contarJFramesVisibles() {
        int c = 0;
        for (ArrayList<ScatterPlot> alsp : mansp.getMatrizScatterPlots()) {
            c = alsp.stream().filter((jf) -> (jf != null && jf.getjFrameAmpliado().isVisible())).map((_item) -> 1).reduce(c, Integer::sum);
        }
        return c;
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

    public Modelo getMeuModelo() {
        return meuModelo;
    }

    public Controlador getMeuControlador() {
        return meuControlador;
    }

    public JPanel getjPanel7() {
        return jPanel7;
    }

    public JPanel getjPanel4() {
        return jPanel4;
    }

    public int getUltimaColumnaModeloSeleccionada() {
        return ultimaColumnaModeloSeleccionada;
    }

    @Override
    public Sesion obterSesion() {
        SesionVista s = new SesionVista();
        s.setScatterPlotsVisibles(getScatterPlotsVisibles());
        s.setManexadorScatterPlots(s.getManexadorScatterPlots());
        return s;
    }

    public boolean[][] getScatterPlotsVisibles() {
        return scatterPlotsVisibles;
    }

    @Override
    public void aplicarSesion(Sesion sesion) throws Exception {
        SesionVista s = (SesionVista) sesion;
        scatterPlotsVisibles = s.getScatterPlotsVisibles();
        mansp = s.getManexadorScatterPlots();
    }

    public void crearControlador() {
        meuControlador = new Controlador();
    }

    public void activar() {
        this.setEnabled(true);
    }

    public void visualizar() {
        this.setVisible(true);
    }

    private void pintarMenuVisualizacion() {
        if (mansp != null) {
            mansp.pecharJFramesChartPanel();
            mansp.pause();
        }
        ArrayList<Integer> indices = meuModelo.obterIndicesAtributosNumericosNoModelo();
        int numAtributosNumericos = indices.size();
        mansp = new ManexadorScatterPlots(meuModelo.getAtributos(), meuModelo.getIndiceAtributoNominal(), scatterPlotsVisibles, jSlider1);
        mansp.addPropertyChangeListener(this);
        actualizarScatterPlotsVisibles(numAtributosNumericos);
        jPopupMenu1.setVisible(false);
        jSlider1.setValue(0);
        jPanel4.removeAll();
        int numCols = numColumnasNonVacias();
        int numFilas = numFilasNonVacias();
        if (numCols * numFilas != 0) {
            int max = Math.max(numFilas, numCols);
            double cols[] = new double[numCols];
            double filas[] = new double[numFilas];
            for (int i = 0; i < max; i++) {
                if (i < numCols) {
                    cols[i] = TableLayout.FILL;
                }
                if (i < numFilas) {
                    filas[i] = TableLayout.FILL;
                }
            }
            jPanel4.setLayout(new TableLayout(cols, filas));
            int filasVacias = 0;
            task.start();
            task.setEnd(numCols * numFilas * 100);
            for (int i = numAtributosNumericos - 1; i >= 0; i--) {
                if (!filaScatterPlotsVacia(i)) {
                    cachedPool.submit(new FioFilas(indices, i, filasVacias, meuModelo.getIndiceAtributoNominal()));
                } else {
                    filasVacias++;
                }
            }
        }
    }

    public int contarScatterplotsVisibles() {
        int c = 0;
        for (boolean bb[] : scatterPlotsVisibles) {
            for (boolean b : bb) {
                if (b) {
                    c++;
                }
            }
        }
        return c;
    }

    private int numColumnasNonVacias() {
        int n = 0;
        for (int i = 0; i < mansp.getMatrizScatterPlots().size(); i++) {
            if (!columnaScatterPlotsVacia(i)) {
                n++;
            }
        }
        return n;
    }

    private int numFilasNonVacias() {
        int n = 0;
        for (int i = 0; i < mansp.getMatrizScatterPlots().size(); i++) {
            if (!filaScatterPlotsVacia(i)) {
                n++;
            }
        }
        return n;
    }

    private boolean columnaScatterPlotsVacia(int columna) {
        for (boolean[] bb : scatterPlotsVisibles) {
            if (bb[columna]) {
                return false;
            }
        }
        return true;
    }

    private boolean filaScatterPlotsVacia(int fila) {
        for (boolean b : scatterPlotsVisibles[fila]) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    public class TarefaProgreso extends SwingWorker<Void, Void> {

        private final JProgressBar bar;
        private int end;
        private int acumulated;
        private boolean finished;

        public void setEnd(int end) {
            this.end = end;
        }

        public JProgressBar getBar() {
            return bar;
        }

        public int getEnd() {
            return end;
        }

        public int getAcumulated() {
            return acumulated;
        }

        private void reset() {
            acumulated = 0;
            try {
                doInBackground();
            } catch (Exception ex) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void start() {
            finished = false;
            jLabel3.setText(bundle.getString("procesando") + "...");
            reset();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }

        public void finish() {
            setCursor(Cursor.getDefaultCursor());
            reset();
            jLabel3.setText("");
        }

        public synchronized void acumulate(int fraction) {
            acumulated += fraction;
            try {
                doInBackground();
            } catch (Exception ex) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (acumulated >= end) {
                finished = true;
            }
        }

        public TarefaProgreso(JProgressBar bar) {
            super();
            this.bar = bar;
            this.end = 0;
            this.acumulated = 0;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int percent = Math.round((float) (end != 0 ? bar.getMaximum() * acumulated / end : 0));
            bar.setValue(percent);
            return null;
        }

        private synchronized boolean lockedTestFinished() {
            if (finished == false) {
                return false;
            } else {
                finished = false;
                return true;
            }
        }

    }

    class FioColumnas extends SwingWorker<Void, Void> implements Runnable {

        private final ArrayList<Integer> indices;
        private final int j;
        private final int i;
        private final int indiceNominal;
        private final int filasVacias;
        private final int columnasVacias;

        public FioColumnas(ArrayList<Integer> indices, int j, int i, int filasVacias, int columnasVacias, int indiceNominal) {
            this.indices = indices;
            this.j = j;
            this.i = i;
            this.indiceNominal = indiceNominal;
            this.filasVacias = filasVacias;
            this.columnasVacias = columnasVacias;
        }

        @Override
        public Void doInBackground() {
            if (!scatterPlotsVisibles[i][j]) {
                task.acumulate(50);
                jPanel4.add(new JLabel(), new TableLayoutConstraints(j - columnasVacias, indices.size() - 1 - i - filasVacias));
                task.acumulate(50);
            } else {
                int indiceI = indices.get(i);
                int indiceJ = indices.get(j);
                ScatterPlot spmatrix;
                spmatrix = mansp.new ScatterPlot(meuModelo.getAtributos(), indiceJ, indiceI, indiceNominal, task, Vista.this);
                mansp.getMatrizScatterPlots().get(i).set(j, spmatrix);
                ChartPanel chartPanelCela = spmatrix.getChartPanelCela();
                chartPanelCela.setLayout(new GridBagLayout());
                JButton clickmeButton = new JButton();
                final int a = i;
                final int b = j;
                clickmeButton.setAction(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrameChartPanel jfa = mansp.getMatrizScatterPlots().get(a).get(b).getjFrameAmpliado();
                        if (!jfa.isVisible()) {
                            jfa.setLocation(getX() + 20 * (contarJFramesVisibles() + 1), getY() + 20 * (contarJFramesVisibles() + 1));
                            jfa.setVisible(true);
                        } else {
                            jfa.toFront();
                        }
                    }
                });
                clickmeButton.setIcon(new ImageIcon(getClass().getResource("imaxes/ampliar.png")));
                clickmeButton.setPreferredSize(new Dimension(19, 19));
                GridBagConstraints c = new GridBagConstraints();
                c.anchor = GridBagConstraints.SOUTHEAST;
                c.weightx = 1;
                c.weighty = 1;
                chartPanelCela.add(clickmeButton, c);
                jPanel4.add(chartPanelCela, new TableLayoutConstraints(j - columnasVacias, indices.size() - 1 - i - filasVacias));
                chartPanelCela.revalidate();
                chartPanelCela.repaint();
            }
            return null;
        }

        @Override
        public void done() {
            boolean isFinished = task.lockedTestFinished();
            if (isFinished) {
                task.finish();
                jPanel4.revalidate();
                jPanel4.repaint();
            }
        }
    }

    class FioFilas extends SwingWorker<Void, Void> implements Runnable {

        private final ArrayList<Integer> indices;
        private final int i;
        private final int indiceNominal;
        private final int filasVacias;

        public FioFilas(ArrayList<Integer> indices, int i, int filasVacias, int indiceNominal) {
            this.indices = indices;
            this.i = i;
            this.filasVacias = filasVacias;
            this.indiceNominal = indiceNominal;
        }

        @Override
        public Void doInBackground() {
            int numAtributosNumericos = indices.size();
            int columnasVacias = 0;
            for (int j = 0; j < numAtributosNumericos; j++) {
                if (!columnaScatterPlotsVacia(j)) {
                    cachedPool.submit(new FioColumnas(indices, j, i, filasVacias, columnasVacias, indiceNominal));
                } else {
                    columnasVacias++;
                }
            }
            return null;
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
        if (((JPanelModelo) panelModelo).esVacio()) {
            inicializarPaneis();
        } else {
            actualizarPaneis();
        }
        jLabel2.setText(meuModelo.getAtributos().relationName());
    }

    private void pintarMenuFiltros() {

    }

    private void pintarMenus() {
        pintarMenuModelo();
        pintarMenuFiltros();
        pintarMenuVisualizacion();
    }

    public void actualizarPilas() {
        if (jTabbedPane1.isEnabled()) {
            if (!meuControlador.getXestorComandos().pilaDesfacerVacia()) {
                jMenuItem7.setEnabled(true);
                jMenuItem7.setText(bundle.getString("Vista.jMenuItem7.text") + " " + meuControlador.getXestorComandos().getPilaDesfacer().peek().getNome());
            } else {
                jMenuItem7.setEnabled(false);
                jMenuItem7.setText(bundle.getString("Vista.jMenuItem7.text"));
            }
            if (!meuControlador.getXestorComandos().pilaRefacerVacia()) {
                jMenuItem8.setEnabled(true);
                jMenuItem8.setText(bundle.getString("Vista.jMenuItem8.text") + " " + meuControlador.getXestorComandos().getPilaRefacer().peek().getNome());
            } else {
                jMenuItem8.setEnabled(false);
                jMenuItem8.setText(bundle.getString("Vista.jMenuItem8.text"));
            }
        }
    }

    @Override
    public void update(final Observable o, Object arg) {
        if (!jTabbedPane1.isEnabled() && o != null && !((Modelo) o).getAtributos().isEmpty()) {
            jMenuItem2.setEnabled(true);
            jMenuItem4.setEnabled(true);
            jMenuItem10.setEnabled(true);
            jMenuItem13.setEnabled(true);
            jMenuItem15.setEnabled(true);
            jMenuItem16.setEnabled(true);
            jMenuItem17.setEnabled(true);
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
    @SuppressWarnings("unchecked")
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
        jComboBox1 = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel8 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelModelo = new JPanelModelo();
        jScrollPane7 = new javax.swing.JScrollPane();
        panelDetallarAtributo = new JPanelActualizable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
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
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
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

        jDialog2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog2.setTitle(bundle.getString("Vista.jMenuItem16.text")); // NOI18N
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

        jButton6.setText(bundle.getString("Aceptar")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JDataMotion");
        setIconImage(new ImageIcon(getClass().getResource("imaxes/favicon.png")).getImage());
        setMinimumSize(new java.awt.Dimension(600, 300));
        setName("Form"); // NOI18N

        jProgressBar1.setName("jProgressBar1"); // NOI18N

        jLabel3.setName("jLabel3"); // NOI18N

        jLayeredPane1.setName("jLayeredPane1"); // NOI18N

        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setOpaque(false);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/play.png"))); // NOI18N
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jSlider1.setValue(0);
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
                    mansp.goTo(s.getValue());
                }
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/goToEnd.png"))); // NOI18N
        jButton9.setName("jButton9"); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jdatamotion/imaxes/goToStart.png"))); // NOI18N
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(92, 69));
        jTabbedPane1.setName(""); // NOI18N
        jTabbedPane1.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(255, 232, 198));
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        jPanel9.setName("jPanel9"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane6.setViewportView(jPanel9);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        panelModelo.setMinimumSize(new java.awt.Dimension(250, 0));
        panelModelo.setName("panelModelo"); // NOI18N
        panelModelo.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane3.setViewportView(panelModelo);

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        panelDetallarAtributo.setName("panelDetallarAtributo"); // NOI18N
        panelDetallarAtributo.setLayout(new javax.swing.BoxLayout(panelDetallarAtributo, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane7.setViewportView(panelDetallarAtributo);

        jLabel1.setText(bundle.getString("Vista.jLabel1.text").concat(":")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane5.getVerticalScrollBar().setModel(jScrollPane3.getVerticalScrollBar().getModel());
        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane5.setHorizontalScrollBar(null);
        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jPanel10.setName("jPanel10"); // NOI18N
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane5.setViewportView(jPanel10);

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
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane5)))
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Vista.jMenu4.text"), jPanel1); // NOI18N

        jPanel2.setBackground(new java.awt.Color(209, 254, 209));
        jPanel2.setName("jPanel2"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 352, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addGap(37, 37, 37))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 394, Short.MAX_VALUE)))
        );
        jLayeredPane1.setLayer(jPanel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jTabbedPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

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

        jMenuItem15.setText(bundle.getString("Vista.jMenuItem15.text")); // NOI18N
        jMenuItem15.setEnabled(false);
        jMenuItem15.setName("jMenuItem15"); // NOI18N
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem15);

        jMenuBar1.add(jMenu4);

        jMenu7.setText(bundle.getString("Vista.jMenu7.text")); // NOI18N
        jMenu7.setName("jMenu7"); // NOI18N
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JPopupMenu getjPopupMenu1() {
        return jPopupMenu1;
    }

    class ComponenteImaxe extends JComponent {

        Image imaxe;

        public ComponenteImaxe(Image i) {
            this.imaxe = i;
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(imaxe, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_ABRIR_FICHEIRO);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        abrirExploradorFicheiros(EXPLORADOR_GARDAR_FICHEIRO);
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

    public void pechar() {
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
        meuControlador.manexarEvento(Controlador.MUDAR_INDICE_TEMPORAL, ultimaColumnaModeloSeleccionada == meuModelo.getIndiceTemporal() ? -1 : ultimaColumnaModeloSeleccionada);
    }//GEN-LAST:event_botonIndiceTemporalActionPerformed

    public JMenuItem getjMenuItem14() {
        return jMenuItem14;
    }

    private void botonNumericoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNumericoActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{ultimaColumnaModeloSeleccionada, Attribute.NUMERIC});
    }//GEN-LAST:event_botonNumericoActionPerformed

    private void botonStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonStringActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{ultimaColumnaModeloSeleccionada, Attribute.STRING});
    }//GEN-LAST:event_botonStringActionPerformed

    private void botonNominalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNominalActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{ultimaColumnaModeloSeleccionada, Attribute.NOMINAL});
    }//GEN-LAST:event_botonNominalActionPerformed

    private void botonDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDataActionPerformed
        meuControlador.manexarEvento(Controlador.MUDAR_TIPO, new Object[]{ultimaColumnaModeloSeleccionada, Attribute.DATE});
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
        ((JPanelModelo) panelModelo).finalizarEdicions();
        meuControlador.manexarEvento(Controlador.ENGADIR_DATOS, null);
        jScrollPane3.getVerticalScrollBar().setValue(jScrollPane3.getVerticalScrollBar().getMaximum());
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        ((JPanelModelo) panelModelo).finalizarEdicions();
        Integer[] indices = new Integer[((JPanelModelo) panelModelo).getMinaTaboa().getSelectedRowCount()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = ((JPanelModelo) panelModelo).getMinaTaboa().getSelectedRows()[i];
        }
        ((JPanelModelo) panelModelo).getMinaTaboa().getSelectionModel().clearSelection();
        meuControlador.manexarEvento(Controlador.ELIMINAR_DATOS, indices);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        String novoNomeRelacion = (String) JOptionPane.showInputDialog(this, bundle.getString("mudarNomeRelacionMensaxe"), bundle.getString("Vista.jMenuItem15.text"), JOptionPane.QUESTION_MESSAGE, null, null, meuModelo.getAtributos().relationName());
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
        jSlider1.setValue(0);
        mansp.pause();
        mansp.goTo(0);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jSlider1.setValue(100);
        mansp.pause();
        mansp.goTo(100);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        if (ultimoEstadoReproductor == ManexadorScatterPlots.PLAY) {
            mansp.play();
        }
        pulsarSlider = false;
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jSlider1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MousePressed
        pulsarSlider = true;
        ultimoEstadoReproductor = mansp.getEstado();
        mansp.freeze();
    }//GEN-LAST:event_jSlider1MousePressed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        mansp.play();
    }//GEN-LAST:event_jButton8ActionPerformed

    public JSlider getjSlider1() {
        return jSlider1;
    }

    @SuppressWarnings("unchecked")
    private void configurarEstablecerAtributoNominalRepresentado() {
        jDialog3.setLocation((getWidth() - jDialog3.getPreferredSize().width) / 2, (getHeight() - jDialog3.getPreferredSize().height) / 2);
        jComboBox1.removeAllItems();
        jComboBox1.addItem((String) "- " + bundle.getString("ningun") + " -");
        meuModelo.obterIndicesAtributosNominaisNoModelo().stream().forEach((a) -> {
            jComboBox1.addItem(meuModelo.obterNomeAtributo(a));
        });
        if (meuModelo.getIndiceAtributoNominal() == -1) {
            jComboBox1.setSelectedIndex(0);
        } else {
            jComboBox1.setSelectedItem(meuModelo.obterNomeAtributo(meuModelo.getIndiceAtributoNominal()));
        }
        jButton6.addActionListener((ActionEvent e) -> {
            if (meuModelo.getIndiceAtributoNominal() != meuModelo.obterArrayListNomesAtributos().indexOf(jComboBox1.getSelectedItem())) {
                if (jComboBox1.getSelectedIndex() == 0) {
                    meuModelo.setIndiceAtributoNominal(-1);
                } else {
                    meuModelo.setIndiceAtributoNominal(meuModelo.obterArrayListNomesAtributos().indexOf(jComboBox1.getSelectedItem()));
                }
                pintarMenuVisualizacion();
            }
            jDialog3.dispose();
        });
        jButton5.addActionListener((ActionEvent e) -> {
            jDialog3.dispose();
        });
        jDialog3.pack();
        jDialog3.setVisible(true);
    }

    private void configurarJDialogEngadirOuEliminarScatterplots() {
        jDialog2.setLocation((getWidth() - jDialog2.getPreferredSize().width) / 2, (getHeight() - jDialog2.getPreferredSize().height) / 2);
        jPanel5.removeAll();
        jPanel6.removeAll();
        jPanel7.removeAll();
        int anchoCela = 40;
        int altoCela = 40;
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
        ArrayList<Integer> atributosNumericos = meuModelo.obterIndicesAtributosNumericosNoModelo();
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
                    l.setToolTipText(meuModelo.obterNomeAtributo(atributosNumericos.get(j)) + " " + bundle.getString("fronteA") + " " + meuModelo.obterNomeAtributo(atributosNumericos.get(k)));
                    l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    l.setOpaque(true);
                    l.setPreferredSize(new Dimension(anchoCela, altoCela));
                    if (scatterPlotsVisibles.length > 0 && scatterPlotsVisiblesAux[j][k]) {
                        l.setBackground(Color.YELLOW);
                    }
                    l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    final int a = j;
                    final int b = k;
                    l.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
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
        jDialog2.pack();
        jDialog2.setVisible(true);
    }

    public void reiniciarAplicacion() {
        if (mansp != null) {
            mansp.pecharJFramesChartPanel();
        }
        dispose();
        JDataMotion.main(null);
    }

    static class VerticalLabelUI extends BasicLabelUI {

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

        private static final Rectangle paintIconR = new Rectangle();
        private static final Rectangle paintTextR = new Rectangle();
        private static final Rectangle paintViewR = new Rectangle();
        private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

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

            // Use inverted height &amp; width
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
                int textX = paintTextR.x;
                int textY = paintTextR.y + fm.getAscent();

                if (label.isEnabled()) {
                    paintEnabledText(label, g, clippedText, textX, textY);
                } else {
                    paintDisabledText(label, g, clippedText, textX, textY);
                }
            }
            g2.setTransform(tr);
        }

    }

    private void mudarIdioma(String locale) {
        if (JOptionPane.showConfirmDialog(this, bundle.getString("confirmacionMudarIdioma"), bundle.getString("confirmacionMudarIdiomaTitulo"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                Properties configFile = new Properties();
                FileInputStream is = new FileInputStream(ficheiroConfiguracion);
                configFile.load(is);
                configFile.setProperty("locale", locale);
                configFile.store(new FileOutputStream(ficheiroConfiguracion), null);
                reiniciarAplicacion();
            } catch (IOException ex) {
                if (Controlador.debug) {
                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void amosarAcercaDe() {
        try {
            JOptionPane.showMessageDialog(this, bundle.getString("AcercaDe"), bundle.getString("Vista.jMenuItem6.text"), INFORMATION_MESSAGE, new ImageIcon(ImageIO.read(getClass().getResource("imaxes/favicon.png")).getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
        } catch (IOException ex) {
            if (Controlador.debug) {
                Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void abrirExploradorFicheiros(int opcion) {
        switch (opcion) {
            case EXPLORADOR_ABRIR_SESION:
                ConfigurarJFileChooserAbrir(Controlador.ABRIR_SESION);
                break;
            case EXPLORADOR_ABRIR_FICHEIRO:
                ConfigurarJFileChooserAbrir(Controlador.IMPORTAR_FICHEIRO);
                break;
            case EXPLORADOR_GARDAR_FICHEIRO:
                ConfigurarJFileChooserGardar(Controlador.EXPORTAR_FICHEIRO);
                break;
            case EXPLORADOR_GARDAR_SESION:
                ConfigurarJFileChooserGardar(Controlador.GARDAR_SESION);
                break;
        }
        jDialog1.setVisible(true);
    }

    public JDialog getjDialog1() {
        return jDialog1;
    }

    public JFileChooser getjFileChooser1() {
        return jFileChooser1;
    }

    class FiltroExtension implements Serializable {

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

    private void ConfigurarJFileChooserAbrir(final int eventoParaControlador) {
        ArrayList<FiltroExtension> ext = new ArrayList<>();
        for (ActionListener al : getjFileChooser1().getActionListeners()) {
            getjFileChooser1().removeActionListener(al);
        }
        for (PropertyChangeListener al : getjFileChooser1().getPropertyChangeListeners()) {
            getjFileChooser1().removePropertyChangeListener("fileFilterChanged", al);
        }
        getjFileChooser1().setDialogType(JFileChooser.OPEN_DIALOG);
        switch (eventoParaControlador) {
            case Controlador.ABRIR_SESION:
                ext.add(new FiltroExtension(bundle.getString("formatoSesion") + " (*.jdm)", new String[]{"jdm"}));
                ext.add(new FiltroExtension(bundle.getString("todosOsFicheiros") + " (*.*)", null));
                definirExtensions(ext);
                jDialog1.setTitle(bundle.getString("Vista.jMenuItem3.text"));
                getjFileChooser1().setApproveButtonText(bundle.getString("abrir"));
                break;
            case Controlador.IMPORTAR_FICHEIRO:
                ext.add(new FiltroExtension(bundle.getString("formatosImportarFicheiro") + " (*.csv;*.arff)", new String[]{"csv", "arff"}));
                ext.add(new FiltroExtension(bundle.getString("todosOsFicheiros") + " (*.*)", null));
                definirExtensions(ext);
                jDialog1.setTitle(bundle.getString("Vista.jMenuItem1.text"));
                getjFileChooser1().setApproveButtonText(bundle.getString("importar"));
                break;
        }
        getjFileChooser1().setSelectedFile(new File(""));
        getjFileChooser1().addActionListener((ActionEvent e) -> {
            jDialog1.dispose();
            if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                ((JPanelModelo) panelModelo).vaciar();
                meuControlador.manexarEvento(
                        eventoParaControlador,
                        getjFileChooser1().getSelectedFile().getAbsolutePath());
            }
        });
        getjFileChooser1().revalidate();
        getjFileChooser1().repaint();
    }

    private void definirExtensions(ArrayList<FiltroExtension> ext) {
        for (FileFilter e : jFileChooser1.getChoosableFileFilters()) {
            jFileChooser1.removeChoosableFileFilter(e);
        }
        UIManager.put("FileChooser.acceptAllFileFilterText", bundle.getString("todosOsFicheiros"));
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

    private void ConfigurarJFileChooserGardar(final int eventoParaControlador) {
        ArrayList<FiltroExtension> ext = new ArrayList<>();
        for (ActionListener al : getjFileChooser1().getActionListeners()) {
            getjFileChooser1().removeActionListener(al);
        }
        for (PropertyChangeListener al : getjFileChooser1().getPropertyChangeListeners()) {
            getjFileChooser1().removePropertyChangeListener("fileFilterChanged", al);
        }
        getjFileChooser1().setDialogType(JFileChooser.SAVE_DIALOG);
        switch (eventoParaControlador) {
            case Controlador.GARDAR_SESION:
                ext.add(new FiltroExtension(bundle.getString("formatoSesion") + " (*.jdm)", new String[]{"jdm"}));
                definirExtensions(ext);
                jDialog1.setTitle(bundle.getString("Vista.jMenuItem4.text"));
                getjFileChooser1().setSelectedFile(new File(bundle.getString("SenTitulo") + ".jdm"));
                getjFileChooser1().addActionListener((ActionEvent e) -> {
                    boolean pecharEGardar = true;
                    File f = getjFileChooser1().getSelectedFile();
                    if (f.exists() && getjFileChooser1().getDialogType() == javax.swing.JFileChooser.SAVE_DIALOG && "ApproveSelection".equals(e.getActionCommand())) {
                        int result = JOptionPane.showConfirmDialog(jDialog1, bundle.getString("confirmacionSobrescritura"), bundle.getString("errorFicheiroXaExistenteTitulo"), JOptionPane.YES_NO_CANCEL_OPTION);
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
                                    getjFileChooser1().getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                getjFileChooser1().setApproveButtonText(bundle.getString("gardar"));
                break;
            case Controlador.EXPORTAR_FICHEIRO:
                FiltroExtension def = new FiltroExtension(bundle.getString("formatoExportarFicheiro1") + " (*.arff)", new String[]{"arff"});
                ext.add(def);
                ext.add(new FiltroExtension(bundle.getString("formatoExportarFicheiro2") + " (*.csv)", new String[]{"csv"}));
                definirExtensions(ext);
                jDialog1.setTitle(bundle.getString("Vista.jMenuItem2.text"));
                getjFileChooser1().addPropertyChangeListener("fileFilterChanged", (PropertyChangeEvent evt) -> {
                    actualizarNomeFicheiro();
                });
                actualizarNomeFicheiro();
                getjFileChooser1().addActionListener((ActionEvent e) -> {
                    switch (e.getActionCommand()) {
                        case "ApproveSelection":
                            File f = getjFileChooser1().getSelectedFile();
                            if (f.exists()) {
                                JOptionPane.showMessageDialog(jDialog1, bundle.getString("errorFicheiroXaExistente"), bundle.getString("errorFicheiroXaExistenteTitulo"), INFORMATION_MESSAGE);
                            } else {
                                String extension = "";
                                try {
                                    Field campos = getjFileChooser1().getFileFilter().getClass().getDeclaredField("extensions");
                                    campos.setAccessible(true);
                                    extension = ((String[]) campos.get(getjFileChooser1().getFileFilter()))[0];
                                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                                    if (Controlador.debug) {
                                        Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                                    meuControlador.manexarEvento(
                                            Controlador.EXPORTAR_FICHEIRO,
                                            new Object[]{extension, getjFileChooser1().getSelectedFile().getAbsolutePath()});
                                }
                                jDialog1.dispose();
                            }
                            break;
                        case "CancelSelection":
                            jDialog1.dispose();
                            break;
                    }
                });
                getjFileChooser1().setApproveButtonText(bundle.getString("exportar"));
                break;
        }
        getjFileChooser1().revalidate();
        getjFileChooser1().repaint();
    }

    private void actualizarNomeFicheiro() {
        int numero = 0;
        String base = getjFileChooser1().getCurrentDirectory() + "\\";
        String path = base + bundle.getString("SenTitulo") + "." + ((FileNameExtensionFilter) getjFileChooser1().getFileFilter()).getExtensions()[0];
        File f;
        if (new File(path).exists()) {
            do {
                numero++;
                path = base + bundle.getString("SenTitulo") + " (" + numero + ")." + ((FileNameExtensionFilter) getjFileChooser1().getFileFilter()).getExtensions()[0];
                f = new File(path);
            } while (f.exists());
        }
        getjFileChooser1().setSelectedFile(new File(path));
    }

    public void amosarDialogo(String mensaxe, int tipo) {
        String titulo;
        String nomeAplicacion = JDataMotion.class.getSimpleName();
        switch (tipo) {
            case ERROR_MESSAGE:
                titulo = nomeAplicacion.concat(" erro");
                break;
            case WARNING_MESSAGE:
                titulo = nomeAplicacion + " " + bundle.getString("aviso");
                break;
            case INFORMATION_MESSAGE:
                titulo = nomeAplicacion + " " + bundle.getString("informacion");
                break;
            case QUESTION_MESSAGE:
                titulo = nomeAplicacion + " " + bundle.getString("pregunta");
                break;
            case PLAIN_MESSAGE:
                titulo = nomeAplicacion + " " + bundle.getString("mensaxe");
                break;
            default:
                titulo = "";
        }
        JOptionPane.showMessageDialog(this, mensaxe, titulo, tipo);
    }

    class JPanelModelo extends JPanel {

        private JTable minaTaboa;

        public int indiceModeloAIndiceTaboa(int indiceModelo) {

            if (indiceModelo > -1) {
                String nomeColumna = minaTaboa.getModel().getColumnName(indiceModelo);
                for (int i = 0; i < minaTaboa.getColumnCount(); i++) {
                    if (minaTaboa.getColumnName(i).equals(nomeColumna)) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public JTable getMinaTaboa() {
            return this.minaTaboa;
        }

        public void setMinaTaboa(JTable minaTaboa) {
            this.minaTaboa = minaTaboa;
        }

        public void finalizarEdicions() {
            TableCellEditor cellEditor = getMinaTaboa().getCellEditor();
            if (cellEditor != null) {
                cellEditor.stopCellEditing();
            }
            for (int i = 0; i < getMinaTaboa().getColumnModel().getColumnCount(); i++) {
                TableColumn columna = getMinaTaboa().getColumnModel().getColumn(i);
                if (columna.getCellEditor() != null) {
                    columna.getCellEditor().cancelCellEditing();
                }
            }
        }

        public boolean esVacio() {
            return (getMinaTaboa() == null);
        }

        public void vaciar() {
            setMinaTaboa(null);
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
            int numFilasTaboa = ((TaboaConcreta) getMinaTaboa().getModel()).getRowCount();
            int numFilasModelo = meuModelo.obterNumInstancias();
            if (numFilasTaboa > numFilasModelo) {
                for (int i = numFilasTaboa - 1; i >= numFilasModelo; i--) {
                    ((TaboaConcreta) getMinaTaboa().getModel()).removeRow(i);
                }
            } else if (numFilasTaboa < numFilasModelo) {
                for (int i = numFilasModelo - 1; i >= numFilasTaboa; i--) {
                    ArrayList<Object> arrayList = new ArrayList<>();
                    for (int j = 0; j < meuModelo.obterNumAtributos(); j++) {
                        arrayList.add("");
                    }
                    ((TaboaConcreta) getMinaTaboa().getModel()).addRow(arrayList);
                }
            }
        }

        public JPanelModelo() {
            super();
        }

        public void actualizar() {
            actualizarTaboaConModelo();
            jPanel10.removeAll();
            for (int j = 0; j < meuModelo.obterNumAtributos(); j++) {
                int indiceModelo = getMinaTaboa().getColumnModel().getColumn(j).getModelIndex();
                int indiceTaboa = indiceModeloAIndiceTaboa(j);
                String nomeCabeceira = meuModelo.obterNomeAtributo(indiceModelo);
                if (meuModelo.getIndiceTemporal() == indiceModelo) {
                    nomeCabeceira = "<html><b style='white-space:nowrap;overflow:hidden;'>" + nomeCabeceira + " (" + bundle.getString("IT") + ")</b></html>";
                }
                getMinaTaboa().getColumnModel().getColumn(j).setHeaderValue(nomeCabeceira);
                for (int i = 0; i < getMinaTaboa().getRowCount(); i++) {
                    if (j == 0) {
                        JLabel l = new JLabel("<html><body style='height:12px;'>" + String.valueOf(i + 1) + "</body></html>", SwingConstants.CENTER);
                        l.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
                        jPanel10.add(l);
                    }
                    ((TaboaConcreta) getMinaTaboa().getModel()).setValueNoFiring(meuModelo.obterStringDato(i, j, false), i, j);
                }
                if (meuModelo.obterArrayListAtributos().get(j).type() == Attribute.NOMINAL) {
                    ArrayList<String> valoresNominais = new ArrayList<>();
                    Enumeration e = meuModelo.getAtributos().attribute(j).enumerateValues();
                    while (e.hasMoreElements()) {
                        String el = (String) e.nextElement();
                        valoresNominais.add(el);
                    }
                    valoresNominais.add("");
                    configurarColumnaNominal(getMinaTaboa().getColumnModel().getColumn(indiceTaboa), valoresNominais);
                } else {
                    restablecerConfiguracionColumna(getMinaTaboa().getColumnModel().getColumn(indiceTaboa));
                }
            }
            getMinaTaboa().revalidate();
            getMinaTaboa().repaint();
        }

        public void establecerColumnaCabeceraResaltada(final int columna) {
            for (int i = 0; i < getMinaTaboa().getColumnModel().getColumnCount(); i++) {
                if (columna != i) {
                    getMinaTaboa().getColumnModel().getColumn(i).setHeaderRenderer(null);
                }
            }
            if (columna > -1) {
                final DefaultTableCellRenderer hr = (DefaultTableCellRenderer) getMinaTaboa().getTableHeader().getDefaultRenderer();
                getMinaTaboa().getColumnModel().getColumn(columna).setHeaderRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        JLabel lbl = (JLabel) hr.getTableCellRendererComponent(table, value, true, true, row, column);
                        lbl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.RED, 2, false), lbl.getBorder()));
                        return lbl;
                    }
                });
            }
        }

        public void restablecerConfiguracionColumna(TableColumn columna) {
            for (int i = 0; i < getMinaTaboa().getColumnModel().getColumnCount(); i++) {
                TableColumn columnaux = getMinaTaboa().getColumnModel().getColumn(i);
                if (columnaux.getCellEditor() != null) {
                    columnaux.getCellEditor().cancelCellEditing();
                }
            }
            columna.setCellEditor(null);
            columna.setCellRenderer(null);
        }

        public void inicializar() {
            removeAll();
            setMinaTaboa(new JTableParcheada(null));
            getMinaTaboa().setModel(new TaboaConcreta(meuModelo.obterArrayListNomesAtributos(), meuModelo.obterArrayListStringDatos(false)));
            getMinaTaboa().setFillsViewportHeight(true);
            configurarColumnas(getMinaTaboa());
            getMinaTaboa().getPreferredSize().width = meuModelo.obterNumAtributos() * 50;
            jScrollPane3.setViewportView(getMinaTaboa());
            getMinaTaboa().getModel().addTableModelListener((TableModelEvent e) -> {
                int fila = e.getFirstRow();
                int columna = e.getColumn();
                Object o = getMinaTaboa().getModel().getValueAt(fila, columna);
                meuControlador.manexarEvento(Controlador.MUDAR_DATO, new Object[]{fila, columna, o});
            });
            getMinaTaboa().getTableHeader().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ultimaColumnaModeloSeleccionada = getMinaTaboa().getColumnModel().getColumn(getMinaTaboa().columnAtPoint(e.getPoint())).getModelIndex();
                    if (SwingUtilities.isRightMouseButton(e)) {
                        botonIndiceTemporal.setSelected(meuModelo.getIndiceTemporal() == ultimaColumnaModeloSeleccionada);
                        popupConfigurarAtributo.show(e.getComponent(), e.getX(), e.getY());
                        for (Component c : menuTipo.getMenuComponents()) {
                            ((JRadioButtonMenuItem) c).setSelected(false);
                        }
                        switch (meuModelo.obterTipoAtributo(ultimaColumnaModeloSeleccionada)) {
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
                    establecerColumnaCabeceraResaltada(indiceModeloAIndiceTaboa(ultimaColumnaModeloSeleccionada));
                    ((JPanelActualizable) panelDetallarAtributo).actualizar();
                }
            });
            actualizar();
        }

        public void configurarColumnas(JTable table) {
            getMinaTaboa().getColumnModel().addColumnModelListener(new TableColumnModelListener() {
                @Override
                public void columnAdded(TableColumnModelEvent e) {
                }

                @Override
                public void columnRemoved(TableColumnModelEvent e) {
                }

                @Override
                public void columnMoved(TableColumnModelEvent e) {
                }

                @Override
                public void columnMarginChanged(ChangeEvent e) {
                }

                @Override
                public void columnSelectionChanged(ListSelectionEvent e) {
                }
            });
            getMinaTaboa().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                activarBorrarDatos(!((JPanelModelo) panelModelo).getMinaTaboa().getSelectionModel().isSelectionEmpty());
            });
        }
    }

    private void activarBorrarDatos(boolean activar) {
        jMenuItem14.setEnabled(activar);
    }

    class TaboaConcreta extends AbstractTableModel {

        private final ArrayList<String> atributos;
        private final ArrayList<ArrayList<Object>> datos;

        public ArrayList<String> getAtributos() {
            return atributos;
        }

        public ArrayList<ArrayList<Object>> getDatos() {
            return datos;
        }

        TaboaConcreta(ArrayList<String> columnNames, ArrayList<ArrayList<Object>> data) {
            super();
            this.atributos = columnNames;
            this.datos = data;
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
            return true;
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

    class JTableParcheada extends JTable {

        public JTableParcheada(TableModel tm) {
            super(tm);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            if (autoResizeMode != AUTO_RESIZE_OFF) {
                if (getParent() instanceof JViewport) {
                    return (((JViewport) getParent()).getWidth() > getPreferredSize().width);
                }
            }
            return false;
        }
    }

    public void inicializarPaneis() {
        ((JPanelModelo) panelModelo).inicializar();
    }

    public void actualizarPaneis() {
        ((JPanelModelo) panelModelo).actualizar();
        ((JPanelActualizable) panelDetallarAtributo).actualizar();
    }

    class JPanelActualizable extends JPanel {

        public void actualizar() {
            panelDetallarAtributo.removeAll();
            if (ultimaColumnaModeloSeleccionada > -1) {
                panelDetallarAtributo.add(new JLabel(bundle.getString("nomeAtributo") + ": " + meuModelo.obterNomeAtributo(ultimaColumnaModeloSeleccionada)));
                panelDetallarAtributo.add(new JLabel(bundle.getString("tipo") + ": " + bundle.getString(meuModelo.obterTipoAtributo(ultimaColumnaModeloSeleccionada))));
                panelDetallarAtributo.add(new JLabel(bundle.getString("indiceTemporal") + ": " + (meuModelo.getIndiceTemporal() == ultimaColumnaModeloSeleccionada ? bundle.getString("si") : bundle.getString("non"))));
                switch (meuModelo.obterTipoAtributo(ultimaColumnaModeloSeleccionada)) {
                    case "numérico":
                        int numInstancesNonNaN = 0;
                        Double dato,
                         min = null,
                         max = null,
                         desvTipica = null,
                         media = null;
                        for (int i = 0; i < meuModelo.getAtributos().numInstances(); i++) {
                            dato = (Double) meuModelo.obterDato(i, ultimaColumnaModeloSeleccionada);
                            if (Double.compare(dato, Double.NaN) != 0) {
                                numInstancesNonNaN++;
                                if (max == null) {
                                    max = dato;
                                    min = dato;
                                    media = dato;
                                } else {
                                    if (dato > max) {
                                        max = dato;
                                    }
                                    if (dato < min) {
                                        min = dato;
                                    }
                                    media += dato;
                                }
                            }
                        }
                        if (media != null) {
                            media /= numInstancesNonNaN;
                            numInstancesNonNaN = 0;
                            desvTipica = 0.0;
                            for (int i = 0; i < meuModelo.getAtributos().numInstances(); i++) {
                                dato = (Double) meuModelo.obterDato(i, ultimaColumnaModeloSeleccionada);
                                if (Double.compare(dato, Double.NaN) != 0) {
                                    numInstancesNonNaN++;
                                    desvTipica += Math.pow(dato - media, 2.0);
                                }
                            }
                            desvTipica /= (numInstancesNonNaN - 1);
                        }
                        panelDetallarAtributo.add(new JLabel(bundle.getString("maximo") + ": " + (max != null ? max : "-")));
                        panelDetallarAtributo.add(new JLabel(bundle.getString("minimo") + ": " + (min != null ? min : "-")));
                        panelDetallarAtributo.add(new JLabel(bundle.getString("media") + ": " + (media != null ? media : "-")));
                        panelDetallarAtributo.add(new JLabel(bundle.getString("desviacionTipica") + ": " + (desvTipica != null && Double.compare(desvTipica, Double.NaN) != 0 ? desvTipica : "-")));
                        break;
                    case "nominal":
                        ArrayList<Integer> coincidencias = new ArrayList<>();
                        for (int i = 0; i < meuModelo.getAtributos().attribute(ultimaColumnaModeloSeleccionada).numValues() + 1; i++) {
                            coincidencias.add(0);
                        }
                        for (int i = 0; i < meuModelo.getAtributos().numInstances(); i++) {
                            int index = meuModelo.getAtributos().instance(i).isMissing(ultimaColumnaModeloSeleccionada) ? 0 : (int) meuModelo.getAtributos().instance(i).value(ultimaColumnaModeloSeleccionada) + 1;
                            coincidencias.set(index, coincidencias.get(index) + 1);
                        }
                        panelDetallarAtributo.add(new JLabel(bundle.getString("valores") + ": "));
                        panelDetallarAtributo.add(new JLabel("  " + bundle.getString("senDefinir") + " (" + coincidencias.get(0) + ")"));
                        for (int i = 1; i < coincidencias.size(); i++) {
                            panelDetallarAtributo.add(new JLabel("  " + meuModelo.getAtributos().attribute(ultimaColumnaModeloSeleccionada).value(i - 1) + " (" + coincidencias.get(i) + ")"));
                        }
                        break;
                    case "string":
                        break;
                    case "data":
                        break;
                }
            }
            panelDetallarAtributo.revalidate();
            panelDetallarAtributo.repaint();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JRadioButtonMenuItem botonData;
    javax.swing.JRadioButtonMenuItem botonIndiceTemporal;
    javax.swing.JRadioButtonMenuItem botonNominal;
    javax.swing.JRadioButtonMenuItem botonNumerico;
    javax.swing.JRadioButtonMenuItem botonString;
    javax.swing.JButton jButton1;
    javax.swing.JButton jButton10;
    javax.swing.JButton jButton2;
    javax.swing.JButton jButton3;
    javax.swing.JButton jButton4;
    javax.swing.JButton jButton5;
    javax.swing.JButton jButton6;
    javax.swing.JButton jButton8;
    javax.swing.JButton jButton9;
    javax.swing.JComboBox jComboBox1;
    javax.swing.JDialog jDialog1;
    javax.swing.JDialog jDialog2;
    javax.swing.JDialog jDialog3;
    javax.swing.JFileChooser jFileChooser1;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    javax.swing.JLayeredPane jLayeredPane1;
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
    javax.swing.JMenuItem jMenuItem2;
    javax.swing.JMenuItem jMenuItem3;
    javax.swing.JMenuItem jMenuItem4;
    javax.swing.JMenuItem jMenuItem5;
    javax.swing.JMenuItem jMenuItem6;
    javax.swing.JMenuItem jMenuItem7;
    javax.swing.JMenuItem jMenuItem8;
    javax.swing.JMenuItem jMenuItem9;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel10;
    javax.swing.JPanel jPanel2;
    javax.swing.JPanel jPanel4;
    javax.swing.JPanel jPanel5;
    javax.swing.JPanel jPanel6;
    javax.swing.JPanel jPanel7;
    javax.swing.JPanel jPanel8;
    javax.swing.JPanel jPanel9;
    javax.swing.JPopupMenu jPopupMenu1;
    javax.swing.JProgressBar jProgressBar1;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JScrollPane jScrollPane3;
    javax.swing.JScrollPane jScrollPane5;
    javax.swing.JScrollPane jScrollPane6;
    javax.swing.JScrollPane jScrollPane7;
    javax.swing.JScrollPane jScrollPane8;
    javax.swing.JScrollPane jScrollPane9;
    javax.swing.JPopupMenu.Separator jSeparator1;
    javax.swing.JPopupMenu.Separator jSeparator2;
    javax.swing.JPopupMenu.Separator jSeparator3;
    javax.swing.JPopupMenu.Separator jSeparator4;
    javax.swing.JSlider jSlider1;
    javax.swing.JTabbedPane jTabbedPane1;
    javax.swing.JMenu menuTipo;
    javax.swing.JPanel panelDetallarAtributo;
    javax.swing.JPanel panelModelo;
    javax.swing.JPopupMenu popupConfigurarAtributo;
    // End of variables declaration//GEN-END:variables
}
