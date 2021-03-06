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
package jdatamotion.sesions;

import jdatamotioncommon.ComparableInstances;

/**
 *
 * @author usuariosy
 */
public class SesionModelo extends Sesion {

    private String direccionAoFicheiro;
    private ComparableInstances cabeceiras;
    private int indiceTemporal;
    private byte[] hash;
    private String nomeRelacion;
    private int indiceAtributoNominal;

    public void setIndiceAtributoNominal(int indiceAtributoNominal) {
        this.indiceAtributoNominal = indiceAtributoNominal;
    }

    public int getIndiceAtributoNominal() {
        return indiceAtributoNominal;
    }

    public void setNomeRelacion(String nomeRelacion) {
        this.nomeRelacion = nomeRelacion;
    }

    public String getNomeRelacion() {
        return nomeRelacion;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setIndiceTemporal(int indiceTemporal) {
        this.indiceTemporal = indiceTemporal;
    }

    public int getIndiceTemporal() {
        return indiceTemporal;
    }

    public void setDireccionAoFicheiro(String direccionAoFicheiro) {
        this.direccionAoFicheiro = direccionAoFicheiro;
    }

    public void setCabeceiras(ComparableInstances cabeceiras) {
        this.cabeceiras = cabeceiras;
    }

    public SesionModelo() {
        super();
    }

    public String getDireccionAoFicheiro() {
        return direccionAoFicheiro;
    }

    public ComparableInstances getCabeceiras() {
        return cabeceiras;
    }

}
