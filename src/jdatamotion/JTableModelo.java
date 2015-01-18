///*
// * The MIT License
// *
// * Copyright 2015 usuario.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package jdatamotion;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import javax.swing.BorderFactory;
//import javax.swing.DefaultCellEditor;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JRadioButtonMenuItem;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import static javax.swing.JTable.AUTO_RESIZE_OFF;
//import javax.swing.JViewport;
//import javax.swing.SwingConstants;
//import javax.swing.SwingUtilities;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.TableColumnModelEvent;
//import javax.swing.event.TableColumnModelListener;
//import javax.swing.event.TableModelEvent;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellEditor;
//import javax.swing.table.TableColumn;
//import static jdatamotion.Vista.bundle;
//import weka.core.Attribute;
//
///**
// *
// * @author usuario
// */
//    class JTableModelo extends JTable {
//
//        private final JPanel columnaNumerais;
//        private final Vista.JPanelActualizable panelDetallarAtributo;
//        private final JScrollPane taboa;
//        private final Modelo modelo;
//        private final boolean editable;
//        private int columnaTaboaSeleccionada;
//
//        public JPanel getColumnaNumerais() {
//            return columnaNumerais;
//        }
//
//        public JScrollPane getTaboa() {
//            return taboa;
//        }
//
//        public Modelo getModelo() {
//            return modelo;
//        }
//
//        public boolean isEditable() {
//            return editable;
//        }
//
//        public int getColumnaTaboaSeleccionada() {
//            return columnaTaboaSeleccionada;
//        }
//
//        public void finalizarEdicions() {
//            TableCellEditor ce = getCellEditor();
//            if (ce != null) {
//                ce.stopCellEditing();
//            }
//            for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
//                TableColumn columna = getColumnModel().getColumn(i);
//                if (columna.getCellEditor() != null) {
//                    columna.getCellEditor().cancelCellEditing();
//                }
//            }
//        }
//
//        public void configurarColumnaNominal(TableColumn column, ArrayList<String> valoresNominais) {
//            JComboBox<String> comboBox = new JComboBox<>();
//            valoresNominais.stream().forEach((v) -> {
//                comboBox.addItem(v);
//            });
//            DefaultCellEditor defaultCellEditor = new DefaultCellEditor(comboBox);
//            defaultCellEditor.setClickCountToStart(2);
//            column.setCellEditor(defaultCellEditor);
//            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//            column.setCellRenderer(renderer);
//        }
//
//        private void actualizarTaboaConModelo() {
//            int numFilasTaboa = ((Vista.TableModelPanelModelo) getModel()).getRowCount(), numFilasModelo = modelo.obterNumInstancias(), numColumnasTaboa = getColumnModel().getColumnCount(), numColumnasModelo = modelo.obterNumAtributos();
//            if (numFilasTaboa > numFilasModelo) {
//                for (int i = numFilasTaboa - 1; i >= numFilasModelo; i--) {
//                    ((Vista.TableModelPanelModelo) getModel()).removeRow(i);
//                }
//            } else if (numFilasTaboa < numFilasModelo) {
//                for (int i = numFilasModelo - 1; i >= numFilasTaboa; i--) {
//                    ArrayList<Object> arrayList = new ArrayList<>();
//                    for (int j = 0; j < modelo.obterNumAtributos(); j++) {
//                        arrayList.add("");
//                    }
//                    ((Vista.TableModelPanelModelo) getModel()).addRow(arrayList);
//                }
//            }
//            if (numColumnasTaboa > numColumnasModelo) {
//                int indiceModeloEliminar = -1, indiceTaboaEliminar = -1;
//                for (int j = 0; j < getColumnModel().getColumnCount(); j++) {
//                    boolean disponible = false;
//                    for (int i = 0; !disponible && i < modelo.getInstancesComparable().numAttributes(); i++) {
//                        if (modelo.getInstancesComparable().attribute(i).name() == getColumnModel().getColumn(j).getHeaderValue()) {
//                            disponible = true;
//                            break;
//                        }
//                    }
//                    if (disponible == false) {
//                        indiceTaboaEliminar = j;
//                        indiceModeloEliminar = getColumnModel().getColumn(j).getModelIndex();
//                        break;
//                    }
//                }
//                getColumnModel().removeColumn(getColumnModel().getColumn(indiceTaboaEliminar));
//                for (int j = 0; j < numColumnasModelo; j++) {
//                    TableColumn tcaux = getColumnModel().getColumn(j);
//                    if (tcaux.getModelIndex() > indiceModeloEliminar) {
//                        tcaux.setModelIndex(tcaux.getModelIndex() - 1);
//                    }
//                }
//            } else if (numColumnasTaboa < numColumnasModelo) {
//                for (int i = numColumnasModelo - 1; i >= numColumnasTaboa; i--) {
//                    ((Vista.TableModelPanelModelo) getModel()).getAtributos().add(modelo.getInstancesComparable().attribute(i).name());
//                    ((Vista.TableModelPanelModelo) getModel()).getDatos().stream().forEach((alo) -> {
//                        alo.add("");
//                    });
//                    getColumnModel().addColumn(new TableColumn(i));
//                }
//            }
//        }
//
//        public JTableModelo(JPanel columnaNumerais, JScrollPane taboa, Modelo modelo, boolean editable, Vista.JPanelActualizable panelDetallarAtributo) {
//            super();
//            this.columnaNumerais = columnaNumerais;
//            this.taboa = taboa;
//            this.modelo = modelo;
//            this.editable = editable;
//            this.panelDetallarAtributo = panelDetallarAtributo;
//        }
//
//        public void actualizar() {
//            resaltarColumnaSeleccionada();
//            actualizarTaboaConModelo();
//            columnaNumerais.removeAll();
//            String data[][] = new String[modelo.obterNumInstancias()][1];
//            for (int i = 0; i < data.length; i++) {
//                data[i][0] = String.valueOf(i + 1);
//            }
//            JTable tablaIndices = new JTable(null);
//            tablaIndices.setModel(new DefaultTableModel(data, new String[]{""}) {
//                @Override
//                public boolean isCellEditable(int row, int col) {
//                    tablaIndices.setFocusable(false);
//                    tablaIndices.setRowSelectionAllowed(false);
//                    return false;
//                }
//            });
//            tablaIndices.setFillsViewportHeight(true);
//            columnaNumerais.add(tablaIndices);
//            tablaIndices.getColumnModel().getColumn(0).setPreferredWidth(0);
//            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//            renderer.setHorizontalAlignment(SwingConstants.CENTER);
//            tablaIndices.setDefaultRenderer(tablaIndices.getColumnClass(0), renderer);
//            tablaIndices.setBackground(Color.LIGHT_GRAY);
//            for (int j = 0; j < getColumnModel().getColumnCount(); j++) {
//                int columnaModelo = getColumnModel().getColumn(j).getModelIndex();
//                String nomeCabeceira = modelo.obterNomeAtributo(columnaModelo);
//                if (modelo.getIndiceTemporal() == columnaModelo) {
//                    nomeCabeceira = "<html><b style='white-space:nowrap;overflow:hidden;'>" + nomeCabeceira + " (" + bundle.getString("IT") + ")</b></html>";
//                }
//                getColumnModel().getColumn(j).setHeaderValue(nomeCabeceira);
//                for (int i = 0; i < getRowCount(); i++) {
//                    ((Vista.TableModelPanelModelo) getModel()).setValueNoFiring(modelo.obterStringDato(i, columnaModelo, false), i, columnaModelo);
//                }
//                if (modelo.obterArrayListAtributos().get(columnaModelo).type() == Attribute.NOMINAL) {
//                    ArrayList<String> valoresNominais = new ArrayList<>();
//                    Enumeration e = modelo.getInstancesComparable().attribute(columnaModelo).enumerateValues();
//                    while (e.hasMoreElements()) {
//                        String el = (String) e.nextElement();
//                        valoresNominais.add(el);
//                    }
//                    valoresNominais.add("");
//                    configurarColumnaNominal(getColumnModel().getColumn(j), valoresNominais);
//                } else {
//                    restablecerConfiguracionColumna(getColumnModel().getColumn(j));
//                }
//            }
//            revalidate();
//            repaint();
//        }
//
//        public void resaltarColumnaSeleccionada() {
//            for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
//                if (columnaTaboaSeleccionada != i) {
//                    getColumnModel().getColumn(i).setHeaderRenderer(null);
//                }
//            }
//            if (columnaTaboaSeleccionada > -1) {
//                final DefaultTableCellRenderer hr = (DefaultTableCellRenderer) getTableHeader().getDefaultRenderer();
//                getColumnModel().getColumn(columnaTaboaSeleccionada).setHeaderRenderer(new DefaultTableCellRenderer() {
//                    @Override
//                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                        JLabel lbl = (JLabel) hr.getTableCellRendererComponent(table, value, true, true, row, column);
//                        lbl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.RED, 2, false), new EmptyBorder(4, 3, 2, 3)));
//                        return lbl;
//                    }
//                });
//            }
//            activarBorrarAtributo(columnaTaboaSeleccionada > -1);
//            activarRenomearAtributo(columnaTaboaSeleccionada > -1);
//            panelDetallarAtributo.actualizar();
//        }
//
//        public void restablecerConfiguracionColumna(TableColumn columna) {
//            for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
//                TableColumn columnaux = getColumnModel().getColumn(i);
//                if (columnaux.getCellEditor() != null) {
//                    columnaux.getCellEditor().cancelCellEditing();
//                }
//            }
//            columna.setCellEditor(null);
//            columna.setCellRenderer(null);
//        }
//
//        @Override
//        public boolean getScrollableTracksViewportWidth() {
//            boolean response = false;
//            if (autoResizeMode != AUTO_RESIZE_OFF) {
//                if (getParent() instanceof JViewport) {
//                    response = ((JViewport) getParent()).getWidth() > getPreferredSize().width;
//                }
//            }
//            filler2.setPreferredSize(response ? filler2.getMinimumSize() : filler2.getMaximumSize());
//            return response;
//        }
//
//        public void inicializar() {
//            removeAll();
//            setModel(new Vista.TableModelPanelModelo(modelo.obterArrayListNomesAtributos(), modelo.obterArrayListStringDatos(false)));
//            setFillsViewportHeight(true);
//            getColumnModel().addColumnModelListener(new TableColumnModelListener() {
//                @Override
//                public void columnAdded(TableColumnModelEvent e) {
//                }
//
//                @Override
//                public void columnRemoved(TableColumnModelEvent e) {
//                }
//
//                @Override
//                public void columnMoved(TableColumnModelEvent e) {
//                    if (e.getFromIndex() == columnaTaboaSeleccionada) {
//                        columnaTaboaSeleccionada = e.getToIndex();
//                    }
//                }
//
//                @Override
//                public void columnMarginChanged(ChangeEvent e) {
//                }
//
//                @Override
//                public void columnSelectionChanged(ListSelectionEvent e) {
//                }
//            });
//            getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
//                activarBorrarDatos(!jTableModelo.getSelectionModel().isSelectionEmpty());
//            });
//            taboa.setViewportView(this);
//            if (editable) {
//                getModel().addTableModelListener((TableModelEvent e) -> {
//                    int fila = e.getFirstRow(), columna = e.getColumn();
//                    Object o = getModel().getValueAt(fila, columna);
//                    meuControlador.manexarEvento(Controlador.MUDAR_DATO, new Object[]{fila, columna, o});
//                });
//            }
//            if (editable) {
//                getTableHeader().addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        columnaTaboaSeleccionada = columnAtPoint(e.getPoint());
//                        resaltarColumnaSeleccionada();
//                        if (SwingUtilities.isRightMouseButton(e)) {
//                            botonIndiceTemporal.setSelected(modelo.getIndiceTemporal() == getColumnaModeloSeleccionada());
//                            popupConfigurarAtributo.show(e.getComponent(), e.getX(), e.getY());
//                            botonOcultarColumna.setSelected(!getTableHeader().isVisible());
//                            for (Component c : menuTipo.getMenuComponents()) {
//                                ((JRadioButtonMenuItem) c).setSelected(false);
//                            }
//                            switch (modelo.obterTipoAtributo(getColumnaModeloSeleccionada())) {
//                                case "numérico":
//                                    botonNumerico.setSelected(true);
//                                    break;
//                                case "nominal":
//                                    botonNominal.setSelected(true);
//                                    break;
//                                case "string":
//                                    botonString.setSelected(true);
//                                    break;
//                                case "data":
//                                    botonData.setSelected(true);
//                                    break;
//                            }
//                        }
//                    }
//                });
//            }
//            actualizar();
//        }
//    }
