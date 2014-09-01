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
package jdatamotion.filtros;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import jdatamotion.InstancesComparable;
import jdatamotion.Modelo;
import jdatamotion.Vista;
import weka.core.Attribute;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class FiltroLimite implements IFilter {

    public static final String LIMITE_VALOR = Vista.bundle.getString("limiteValor");
    public static final String LIMITE_PERCENTIL = Vista.bundle.getString("limitePercentil");

    public static final String COTA_SUPERIOR = Vista.bundle.getString("cotaSuperior");
    public static final String COTA_INFERIOR = Vista.bundle.getString("cotaInferior");

    private Integer indiceAtributoFiltrado;
    private Parameter valor;
    private String nomeAtributoFiltrado;
    private final InstancesComparable atributos;
    private Parameter tipoLimite;
    private Parameter tipoCota;

    public FiltroLimite(InstancesComparable atributos) {
        this.atributos = atributos;
        this.indiceAtributoFiltrado = null;
        this.valor = null;
        this.nomeAtributoFiltrado = null;
        this.tipoLimite = new Parameter(Vista.bundle.getString("tipoLimite"), LIMITE_VALOR);
        this.tipoCota = new Parameter(Vista.bundle.getString("tipoCota"), COTA_SUPERIOR);
    }

    @Override
    public InstancesComparable filter(InstancesComparable instancesComparable) {
        if (indiceAtributoFiltrado == null || valor == null) {
            return instancesComparable;
        }
        InstancesComparable ins = new InstancesComparable(instancesComparable);
        Double valorNumerico = 0.0;
        if (tipoLimite.getValue().equals(LIMITE_VALOR)) {
            valorNumerico = (Double) valor.getValue();
        } else if (tipoLimite.getValue().equals(LIMITE_PERCENTIL)) {
            valorNumerico = Modelo.getValorPercentil(instancesComparable, ((Double) valor.getValue()).intValue(), indiceAtributoFiltrado);
        }
        Iterator<Instance> it = ins.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            Double v = instance.isMissing(indiceAtributoFiltrado) ? null : instance.value(indiceAtributoFiltrado);
            if (v != null && valorNumerico != null && ((tipoCota.getValue().equals(COTA_SUPERIOR) && v > valorNumerico) || (tipoCota.getValue().equals(COTA_INFERIOR) && v < valorNumerico))) {
                instance.setValue(indiceAtributoFiltrado, valorNumerico);
            }
        }
        return ins;
    }

    @Override
    public String toString() {
        return "Filtro de límite";
    }

    @Override
    public void configure() {
        List<Integer> indicesAtributosNumericos = new ArrayList<>();
        List<String> nomesAtributos = new ArrayList<>();
        nomesAtributos.add(Vista.bundle.getString("ningun"));
        for (int i = 0; i < atributos.numAttributes(); i++) {
            Attribute at = atributos.attribute(i);
            if (at.isNumeric()) {
                nomesAtributos.add(at.name());
                indicesAtributosNumericos.add(i);
            }
        }
        ButtonGroup groupTipoLimite = new ButtonGroup();
        JRadioButton botonLimiteValor = new JRadioButton(Vista.bundle.getString("limiteValor"));
        JRadioButton botonLimitePercentil = new JRadioButton(Vista.bundle.getString("limitePercentil"));
        if (tipoLimite.getValue().equals(LIMITE_VALOR)) {
            botonLimiteValor.setSelected(true);
        } else if (tipoLimite.getValue().equals(LIMITE_PERCENTIL)) {
            botonLimitePercentil.setSelected(true);
        }
        groupTipoLimite.add(botonLimiteValor);
        groupTipoLimite.add(botonLimitePercentil);
        JPanel tipoLimitePanel = new JPanel(new GridLayout(0, 1));
        tipoLimitePanel.add(botonLimiteValor);
        tipoLimitePanel.add(botonLimitePercentil);
        ButtonGroup groupTipoCota = new ButtonGroup();
        JRadioButton botonCotaSuperior = new JRadioButton(Vista.bundle.getString("cotaSuperior"));
        JRadioButton botonCotaInferior = new JRadioButton(Vista.bundle.getString("cotaInferior"));
        if (tipoCota.getValue().equals(COTA_SUPERIOR)) {
            botonCotaSuperior.setSelected(true);
        } else if (tipoCota.getValue().equals(COTA_INFERIOR)) {
            botonCotaInferior.setSelected(true);
        }
        groupTipoCota.add(botonCotaSuperior);
        groupTipoCota.add(botonCotaInferior);
        JPanel tipoCotaPanel = new JPanel(new GridLayout(0, 1));
        tipoCotaPanel.add(botonCotaSuperior);
        tipoCotaPanel.add(botonCotaInferior);
        JTextField tf = new JTextField(valor != null ? String.valueOf(valor.getValue()) : "", 5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel(Vista.bundle.getString("nomeAtributoFiltrado")));
        JComboBox cb = new JComboBox<>(nomesAtributos.toArray());
        cb.setSelectedItem(nomeAtributoFiltrado != null ? nomeAtributoFiltrado : 0);
        myPanel.add(cb);
        myPanel.add(new JLabel(Vista.bundle.getString("valor") + ": "));
        myPanel.add(tf);
        myPanel.add(tipoLimitePanel);
        myPanel.add(tipoCotaPanel);
        int result = JOptionPane.showConfirmDialog(null, myPanel, Vista.bundle.getString("configurar") + " " + toString(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            indiceAtributoFiltrado = cb.getSelectedIndex() > 0 ? indicesAtributosNumericos.get(cb.getSelectedIndex() - 1) : null;
            if (indiceAtributoFiltrado != null) {
                nomeAtributoFiltrado = atributos.attribute(indiceAtributoFiltrado).name();
            } else {
                nomeAtributoFiltrado = null;
            }
            if (botonCotaInferior.isSelected()) {
                tipoCota = new Parameter(Vista.bundle.getString("tipoCota"), COTA_INFERIOR);
            } else if (botonCotaSuperior.isSelected()) {
                tipoCota = new Parameter(Vista.bundle.getString("tipoCota"), COTA_SUPERIOR);
            }
            if (botonLimiteValor.isSelected()) {
                tipoLimite = new Parameter(Vista.bundle.getString("tipoLimite"), LIMITE_VALOR);
            } else if (botonLimitePercentil.isSelected()) {
                tipoLimite = new Parameter(Vista.bundle.getString("tipoLimite"), LIMITE_PERCENTIL);
            }
            try {
                valor = new Parameter(Vista.bundle.getString("valor"), Double.parseDouble(tf.getText()));
            } catch (NumberFormatException e) {
                valor = null;
            }
        }
    }

    @Override
    public Parameter[] getParameters() {
        List<Parameter> params = new ArrayList<>();
        if (tipoCota != null) {
            params.add(tipoCota);
        }
        if (nomeAtributoFiltrado != null) {
            params.add(new Parameter(Vista.bundle.getString("nomeAtributoFiltrado"), nomeAtributoFiltrado));
        }
        if (tipoLimite != null) {
            params.add(tipoLimite);
        }
        if (valor != null) {
            params.add(valor);
        }
        return params.toArray(new Parameter[params.size()]);
    }

    @Override
    public IFilter clone() throws CloneNotSupportedException {
        FiltroLimite f = new FiltroLimite(atributos);
        f.indiceAtributoFiltrado = indiceAtributoFiltrado;
        f.nomeAtributoFiltrado = nomeAtributoFiltrado;
        f.tipoCota = tipoCota;
        f.tipoLimite = tipoLimite;
        f.valor = valor;
        return f;
    }

}