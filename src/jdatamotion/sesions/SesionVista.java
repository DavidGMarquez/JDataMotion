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

import jdatamotion.ManexadorScatterPlots;

/**
 *
 * @author usuariosy
 */
public class SesionVista extends Sesion {

    private boolean scatterPlotsVisibles[][];
    private ManexadorScatterPlots manexadorScatterPlots;
    private int ordeVisualizacion;

    public int getOrdeVisualizacion() {
        return ordeVisualizacion;
    }

    public void setOrdeVisualizacion(int ordeVisualizacion) {
        this.ordeVisualizacion = ordeVisualizacion;
    }

    public void setManexadorScatterPlots(ManexadorScatterPlots manexadorScatterPlots) {
        this.manexadorScatterPlots = manexadorScatterPlots;
    }

    public void setScatterPlotsVisibles(boolean[][] scatterPlotsVisibles) {
        this.scatterPlotsVisibles = scatterPlotsVisibles;
    }

    public boolean[][] getScatterPlotsVisibles() {
        return scatterPlotsVisibles;
    }

    public ManexadorScatterPlots getManexadorScatterPlots() {
        return manexadorScatterPlots;
    }

    public SesionVista() {
        super();
    }

}
