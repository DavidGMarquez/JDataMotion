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
package jdatamotion.comandos;

import java.util.List;
import jdatamotion.Modelo;
import jdatamotion.Vista;
import jdatamotion.filtros.FilterHandler;
import jdatamotioncommon.ComparableInstances;

/**
 *
 * @author usuario
 */
public class ComandoEliminarAtributo extends ComandoDesfacible {

    private int indiceAtributoTemporalAntigo;
    private final int indiceAtributoNoModelo;
    private ComparableInstances modeloAntigo;
    private List<FilterHandler> filtrosAntigos;

    public ComandoEliminarAtributo(Modelo modelo, int indiceAtributoNoModelo) {
        super(modelo, Vista.bundle.getString("Vista.jMenuItem19.text"));
        this.indiceAtributoNoModelo = indiceAtributoNoModelo;
    }

    @Override
    public void desfacer() throws Exception {
        ((Modelo) getObxectivo()).setIndiceTemporal(indiceAtributoTemporalAntigo);
        ((Modelo) getObxectivo()).setFiltros(filtrosAntigos);
        ((Modelo) getObxectivo()).setComparableInstances(modeloAntigo);
    }

    @Override
    public void executar() throws Exception {
        this.modeloAntigo = new ComparableInstances(((Modelo) getObxectivo()).getComparableInstances());
        this.indiceAtributoTemporalAntigo = ((Modelo) getObxectivo()).getIndiceTemporal();
        this.filtrosAntigos = ((Modelo) getObxectivo()).getFiltros();
        if (indiceAtributoNoModelo == ((Modelo) getObxectivo()).getIndiceTemporal()) {
            ((Modelo) getObxectivo()).setIndiceTemporal(-1);
        }
        ((Modelo) getObxectivo()).eliminarAtributo(indiceAtributoNoModelo);
        ((Modelo) getObxectivo()).desconfigurarAtributoFiltros();
    }
}
