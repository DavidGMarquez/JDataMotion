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

import jdatamotion.Modelo;
import jdatamotion.Vista;
import jdatamotion.filtros.IFilter;

/**
 *
 * @author usuario
 */
public class ComandoEliminarFiltro extends ComandoDesfacible {

    private final int index;
    private int indexAntigo;
    private IFilter filtroAntigo;

    public ComandoEliminarFiltro(Modelo modelo, int index) {
        super(modelo, Vista.bundle.getString("comandoEliminarFiltro"));
        this.index = index;
    }

    @Override
    public void Desfacer() throws Exception {
        ((Modelo) getObxectivo()).engadirFiltro(indexAntigo, filtroAntigo);
    }

    @Override
    public void Executar() throws Exception {
        indexAntigo = index;
        filtroAntigo = ((Modelo) getObxectivo()).getFiltro(index);
        ((Modelo) getObxectivo()).eliminarFiltro(index);
    }
}