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

/**
 *
 * @author usuario
 */
public class Test {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java JarClassLoader "
                    + "<jar file name> <class name>");
            System.exit(1);
        }
        /*
         * Create the jar class loader and use the first argument
         * passed in from the command line as the jar file to use.
         */
        JarClassLoader jarLoader = new JarClassLoader(args[0]);
        /* Load the class from the jar file and resolve it. */
        Class c = jarLoader.loadClass(args[1], true);
        /*
         * Create an instance of the class.
         *
         * Note that created object's constructor-taking-no-arguments
         * will be called as part of the object's creation.
         */
        Object o = c.newInstance();
        /* Are we using a class we specifically know about? */
        /*if (o instanceof TestClass) {
            TestClass tc = (TestClass) o;
            tc.doSomething();
        }*/
    }
}   // End of nested Class Test.
