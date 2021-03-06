/*
 * The MIT License
 *
 * Copyright 2015 usuario.
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdatamotioncommon.ComparableInstances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

/**
 *
 * @author usuario
 */
public class ValidFileLoading {

    public static ComparableInstances loadARFF(String resourcePath) {
        ComparableInstances comparableInstances = null;
        try {
            ArffLoader loaderARFF = new ArffLoader();
            loaderARFF.setFile(new File(resourcePath));
            comparableInstances = new ComparableInstances(loaderARFF.getDataSet());
        } catch (IOException ex) {
            Logger.getLogger(ValidFileLoading.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comparableInstances;
    }

    public static ComparableInstances loadCSV(String resourcePath) {
        ComparableInstances comparableInstances = null;
        try {
            CSVLoader loaderCSV = new CSVLoader();
            loaderCSV.setSource(new File(resourcePath));
            comparableInstances = new ComparableInstances(loaderCSV.getDataSet());

        } catch (IOException ex) {
            Logger.getLogger(ValidFileLoading.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comparableInstances;
    }

}
