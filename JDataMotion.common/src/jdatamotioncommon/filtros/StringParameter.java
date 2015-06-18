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
package jdatamotioncommon.filtros;

import java.util.Arrays;

/**
 *
 * @author usuario
 */
public class StringParameter extends Parameter<String> {

    private final String[] options;

    public StringParameter(String value, String[] options) {
        super(value);
        this.options = options;
    }

    public StringParameter(String[] options) {
        this(null, options);
    }

    public String[] getOptions() {
        return options;
    }

    public StringParameter() {
        this(null, null);
    }

    public StringParameter(String value) {
        this(value, null);
    }

    @Override
    public boolean isValid(String value) {
        return value == null || options == null || Arrays.asList(options).contains(value);
    }

}
