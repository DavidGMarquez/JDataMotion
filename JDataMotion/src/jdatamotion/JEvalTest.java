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

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

/**
 *
 * @author usuario
 */
public class JEvalTest {

    public static void main(String[] args) {
        String regex = "^.*\\$[12]\\.(`[a-zA-Z0-9]*)?$";
        String test = "$1.`asd";
        System.out.println("MATCH? " + test.matches(regex));
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(test);
        if (m.matches()) {
            System.out.println("MATCHES: " + m.group(1));
        }
        /*try {
         Evaluator mEvaluator = new Evaluator();
         String r2 = mEvaluator.evaluate("sqrt(2)");
         System.out.println(r2);
         } catch (EvaluationException ex) {
         Logger.getLogger(JEvalTest.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }
}
