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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jdatamotion.InstancesComparable;
import jdatamotion.Vista;
import weka.core.Attribute;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class FiltroRecheo implements IFilter {

    private Integer indiceAtributoFiltrado;
    private Parameter valor;
    private String nomeAtributoFiltrado;
    private final InstancesComparable atributos;

    public FiltroRecheo(InstancesComparable atributos) {
        this.atributos = atributos;
        this.indiceAtributoFiltrado = null;
        this.valor = null;
        this.nomeAtributoFiltrado = null;
    }

    @Override
    public InstancesComparable filter(InstancesComparable instancesComparable) {
        if (indiceAtributoFiltrado == null || valor == null) {
            return instancesComparable;
        }
        InstancesComparable ins = new InstancesComparable(instancesComparable);
        Iterator<Instance> it = ins.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            if (instance.isMissing(indiceAtributoFiltrado)) {
                instance.setValue(indiceAtributoFiltrado, (double) valor.getValue());
            }
        }
        return ins;
    }

    @Override
    public String toString() {
        return "Filtro de recheo";
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
        JTextField tf = new JTextField(valor != null ? String.valueOf(valor.getValue()) : "", 5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel(Vista.bundle.getString("nomeAtributoFiltrado")));
        JComboBox cb = new JComboBox<>(nomesAtributos.toArray());
        cb.setSelectedItem(nomeAtributoFiltrado != null ? nomeAtributoFiltrado : 0);
        myPanel.add(cb);
        myPanel.add(new JLabel(Vista.bundle.getString("valor") + ": "));
        myPanel.add(tf);
        int result = JOptionPane.showConfirmDialog(null, myPanel, Vista.bundle.getString("configurar") + " " + toString(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            indiceAtributoFiltrado = cb.getSelectedIndex() > 0 ? indicesAtributosNumericos.get(cb.getSelectedIndex() - 1) : null;
            if (indiceAtributoFiltrado != null) {
                nomeAtributoFiltrado = atributos.attribute(indiceAtributoFiltrado).name();
            } else {
                nomeAtributoFiltrado = null;
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
        if (nomeAtributoFiltrado != null) {
            params.add(new Parameter(Vista.bundle.getString("nomeAtributoFiltrado"), nomeAtributoFiltrado));
        }
        if (valor != null) {
            params.add(valor);
        }
        return params.toArray(new Parameter[params.size()]);
    }

    @Override
    public IFilter clone() throws CloneNotSupportedException {
        FiltroRecheo f = new FiltroRecheo(atributos);
        f.indiceAtributoFiltrado = indiceAtributoFiltrado;
        f.nomeAtributoFiltrado = nomeAtributoFiltrado;
        f.valor = valor;
        return f;
    }

}
