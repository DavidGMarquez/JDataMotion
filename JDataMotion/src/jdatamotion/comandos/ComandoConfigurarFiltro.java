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

import java.util.HashMap;
import java.util.Map;
import jdatamotion.Modelo;
import jdatamotion.Vista;
import jdatamotioncommon.filtros.Parameter;

/**
 *
 * @author usuario
 */
public class ComandoConfigurarFiltro extends ComandoDesfacible {

    private final int index;
    private final Map<String, Parameter> parametrosAntigos;
    private final Map<String, Parameter> parametrosNovos;
    private final Integer indiceAtributoAntigo;
    private final Integer indiceAtributoNovo;

    public ComandoConfigurarFiltro(Modelo modelo, int index, HashMap<String, Parameter> parametrosNovos) throws CloneNotSupportedException {
        super(modelo, Vista.bundle.getString("comandoConfigurarFiltro"));
        this.index = index;
        HashMap<String, Parameter> parametrosNovosAux = (HashMap<String, Parameter>) parametrosNovos.clone();
        this.indiceAtributoNovo = parametrosNovosAux.containsKey("indiceAtributo") ? (Integer) parametrosNovosAux.remove("indiceAtributo").getValue() : null;
        this.parametrosNovos = parametrosNovosAux;
        this.parametrosAntigos = modelo.getFiltro(index).getParameters();
        this.indiceAtributoAntigo = modelo.getFiltro(index).getIndiceAtributoFiltrado();
    }

    @Override
    public void desfacer() throws Exception {
        ((Modelo) getObxectivo()).configurarFiltro(index, parametrosAntigos);
        ((Modelo) getObxectivo()).getFiltro(index).setIndiceAtributoFiltrado(indiceAtributoAntigo);
    }

    @Override
    public void executar() throws Exception {
        ((Modelo) getObxectivo()).configurarFiltro(index, parametrosNovos);
        ((Modelo) getObxectivo()).getFiltro(index).setIndiceAtributoFiltrado(indiceAtributoNovo);
    }
}
