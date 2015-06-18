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

/**
 *
 * @author usuario
 */
public class IntegerParameter extends Parameter<Integer> {

    private final Integer max;
    private final Integer min;

    public IntegerParameter(Integer value, Integer max, Integer min) {
        super(value);
        this.max = max;
        this.min = min;
    }

    public IntegerParameter() {
        this(null, null, null);
    }

    public IntegerParameter(Integer value) {
        this(value, null, null);
    }

    @Override
    public boolean isValid(Integer value) {
        return value == null || ((max == null || value >= min) && (min == null || value <= max));
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

}
