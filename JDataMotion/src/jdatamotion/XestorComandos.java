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
package jdatamotion;

import jdatamotion.excepcions.ExcepcionLeve;
import jdatamotion.comandos.ComandoDesfacible;
import jdatamotion.comandos.Comando;
import java.io.Serializable;
import java.util.Objects;
import java.util.Stack;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author usuario
 */
public class XestorComandos implements Serializable {
    
    private final Stack<ComandoDesfacible> pilaDesfacer;
    private final Stack<ComandoDesfacible> pilaRefacer;
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof XestorComandos == false) {
            return false;
        }
        XestorComandos m = (XestorComandos) o;
        return m.getPilaDesfacer().equals(getPilaDesfacer()) && m.getPilaRefacer().equals(getPilaRefacer());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.pilaDesfacer);
        hash = 47 * hash + Objects.hashCode(this.pilaRefacer);
        return hash;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
    public XestorComandos() {
        pilaDesfacer = new Stack<>();
        pilaRefacer = new Stack<>();
    }
    
    public Stack<ComandoDesfacible> getPilaDesfacer() {
        return pilaDesfacer;
    }
    
    public Stack<ComandoDesfacible> getPilaRefacer() {
        return pilaRefacer;
    }
    
    public void ExecutarComando(Comando cmd) throws Exception {
        ExcepcionLeve excepcionLeve = null;
        try {
            cmd.Executar();
        } catch (ExcepcionLeve e) {
            excepcionLeve = e;
        }
        if (cmd instanceof ComandoDesfacible) {
            pilaDesfacer.push((ComandoDesfacible) cmd);
            vaciarPilaRefacer();
        }
        if (excepcionLeve != null) {
            throw excepcionLeve;
        }
    }
    
    public void Reverter() throws Exception {
        for (int i = pilaDesfacer.size() - 1; i >= 0; i--) {
            pilaDesfacer.get(i).Desfacer();
        }
    }
    
    public boolean pilaDesfacerVacia() {
        return pilaDesfacer.empty();
    }
    
    public void vaciarPilaRefacer() {
        pilaRefacer.removeAllElements();
    }
    
    public void vaciarPilaDesfacer() {
        pilaDesfacer.removeAllElements();
    }
    
    public boolean pilaRefacerVacia() {
        return pilaRefacer.empty();
    }
    
    public void Desfacer() throws Exception {
        if (!pilaDesfacer.empty()) {
            ComandoDesfacible cmd = (ComandoDesfacible) pilaDesfacer.peek();
            cmd.Desfacer();
            pilaRefacer.push(pilaDesfacer.pop());
        }
    }
    
    public void Refacer() throws Exception {
        if (!pilaRefacer.empty()) {
            ComandoDesfacible cmd = (ComandoDesfacible) pilaRefacer.peek();
            cmd.Executar();
            pilaDesfacer.push(pilaRefacer.pop());
        }
    }
}
