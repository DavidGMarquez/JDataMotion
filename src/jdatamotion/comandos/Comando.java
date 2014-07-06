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

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author usuario
 */
public abstract class Comando implements Serializable {

    private Object obxectivo;
    private final String nome;

    public Comando(Object obxectivo, String nome) {
        this.obxectivo = obxectivo;
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Comando == false) {
            return false;
        }
        Comando c = (Comando) o;
        return EqualsBuilder.reflectionEquals(this, c);
    }

    public String getNome() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.obxectivo);
        hash = 59 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public abstract void Executar() throws Exception;

    public void setObxectivo(Object obxectivo) {
        this.obxectivo = obxectivo;
    }

    public Object getObxectivo() {
        return obxectivo;
    }

}
