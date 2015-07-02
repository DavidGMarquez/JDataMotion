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
package jdatamotion.excepcions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import jdatamotion.Modelo;
import jdatamotioncommon.ComparableInstances;
import weka.core.Instance;

/**
 *
 * @author usuario
 */
public class ExcepcionFormatoIdentificacionTemporal extends Exception {

    private final int indiceTemporal;
    private final ComparableInstances comparableInstances;

    public ExcepcionFormatoIdentificacionTemporal(int indiceTemporal, ComparableInstances comparableInstances) {
        super();
        this.indiceTemporal = indiceTemporal;
        this.comparableInstances = comparableInstances;
    }

    @Override
    public String getMessage() {
        if (comparableInstances.attribute(indiceTemporal).isNumeric()) {
            int index = 0;
            for (int i = 0; i < comparableInstances.numInstances(); i++) {
                if (comparableInstances.get(i).value(indiceTemporal) < 0.0) {
                    index = i;
                    break;
                }
            }
            return "O atributo " + comparableInstances.attribute(indiceTemporal).name() + " é negativo na entrada número " + (index + 1) + ".";
        } else if (comparableInstances.attribute(indiceTemporal).isString()) {
            int index = 0;
            for (int i = 0; i < comparableInstances.numInstances(); i++) {
                SimpleDateFormat timeFormat = new SimpleDateFormat(Modelo.formatoTimeIdentificadorTemporal, Locale.getDefault());
                try {
                    timeFormat.parse(Modelo.normalizarTime(comparableInstances.get(i).stringValue(indiceTemporal)));
                } catch (ParseException pe) {
                    index = i;
                    break;
                }
            }
            return "O atributo '" + comparableInstances.attribute(indiceTemporal).name() + "' non segue o formato '" + Modelo.formatoTimeIdentificadorTemporal + "' na entrada número " + (index + 1) + ".";
        } else {
            return "O atributo '" + comparableInstances.attribute(indiceTemporal).name() + "' non é nin de tipo numérico nin de tipo string.";
        }
    }

}
