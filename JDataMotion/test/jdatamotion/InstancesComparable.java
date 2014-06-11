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

import java.util.Enumeration;
import java.util.NoSuchElementException;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.InstanceComparator;
import weka.core.Instances;

/**
 *
 * @author usuario
 */
public class InstancesComparable extends Instances {

    public InstancesComparable(Instances i) {
        super(i);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Instances == false) {
            return false;
        }
        Instances is = (Instances) o;
        Enumeration e1 = is.enumerateInstances();
        Enumeration e2 = enumerateInstances();
        InstanceComparator a = new InstanceComparator();
        if (!relationName().equals(is.relationName())) {
            return false;
        }
        while (e1.hasMoreElements() || e1.hasMoreElements()) {
            try {
                Instance i1 = (Instance) e1.nextElement();
                Instance i2 = (Instance) e2.nextElement();
                if (a.compare(i1, i2) != 0) {
                    return false;
                }
            } catch (NoSuchElementException e) {
                return false;
            }
        }
        e1 = is.enumerateAttributes();
        e2 = enumerateAttributes();
        while (e1.hasMoreElements() || e1.hasMoreElements()) {
            try {
                Attribute a1 = (Attribute) e1.nextElement();
                Attribute a2 = (Attribute) e2.nextElement();
                if (!a1.equals(a2)) {
                    return false;
                }
            } catch (NoSuchElementException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
}
