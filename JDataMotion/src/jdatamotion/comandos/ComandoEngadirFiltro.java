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
import jdatamotion.filtros.FilterHandler;
import jdatamotioncommon.filtros.IFilter;

/**
 *
 * @author usuario
 */
public class ComandoEngadirFiltro extends ComandoDesfacible {

    private final int index;
    private final IFilter filtro;

    public ComandoEngadirFiltro(Modelo modelo, int index, IFilter filtro) {
        super(modelo, Vista.recursosIdioma.getString("Vista.jButton14.text"));
        this.index = index;
        this.filtro = filtro;
    }

    @Override
    public void desfacer() throws Exception {
        ((Modelo) getObxectivo()).eliminarFiltro(index);
    }

    @Override
    public void executar() throws Exception {
        ((Modelo) getObxectivo()).engadirFiltro(index, filtro);
    }
}
