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
import jdatamotion.excepcions.ExcepcionLeve;
import jdatamotion.filtros.FilterHandler;
import jdatamotioncommon.ComparableInstances;

/**
 *
 * @author usuario
 */
public class ComandoMudarTipo extends ComandoDesfacible {

    private final int novoTipo;
    private final int columnaModelo;
    private ComparableInstances modeloAntigo;
    private int indiceAtributoNominalAntigo;
    private List<FilterHandler> filtrosAntigos;

    public ComandoMudarTipo(Modelo modelo, int columnaModelo, int novoTipo) {
        super(modelo, Vista.recursosIdioma.getString("comandoMudarTipo"));
        this.novoTipo = novoTipo;
        this.columnaModelo = columnaModelo;
    }

    @Override
    public void desfacer() throws Exception {
        ((Modelo) getObxectivo()).setIndiceAtributoNominal(indiceAtributoNominalAntigo);
        ((Modelo) getObxectivo()).setComparableInstances(modeloAntigo);
        ((Modelo) getObxectivo()).setFiltros(filtrosAntigos);
    }

    @Override
    public void executar() throws Exception {
        this.indiceAtributoNominalAntigo = ((Modelo) getObxectivo()).getIndiceAtributoNominal();
        this.modeloAntigo = new ComparableInstances(((Modelo) getObxectivo()).getComparableInstances());
        this.filtrosAntigos = ((Modelo) getObxectivo()).getFiltros();
        try {
            ((Modelo) getObxectivo()).mudarTipo(columnaModelo, novoTipo);
        } catch (ExcepcionLeve e) {
        }
        ((Modelo) getObxectivo()).desconfigurarAtributoFiltros();
    }

}
