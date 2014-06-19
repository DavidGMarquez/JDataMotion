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

/**
 *
 * @author usuario
 */
public class ComandoMudarDato extends ComandoDesfacible {

    private final int fila;
    private final int columna;
    private final Object novoValor;
    private final Object antigoValor;

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Object getNovoValor() {
        return novoValor;
    }

    public Object getAntigoValor() {
        return antigoValor;
    }

    public ComandoMudarDato(Modelo modelo, int fila, int columna, Object novoValor) {
        super(modelo, Vista.bundle.getString("comandoMudarDato"));
        this.fila = fila;
        this.columna = columna;
        this.novoValor = novoValor;
        this.antigoValor = modelo.obterDato(fila, columna);
    }

    @Override
    public void Desfacer() throws Exception {
        ((Modelo) getObxectivo()).mudarDato(fila, columna, antigoValor);
    }

    @Override
    public void Executar() throws Exception {
        ((Modelo) getObxectivo()).mudarDato(fila, columna, novoValor);
    }
}
