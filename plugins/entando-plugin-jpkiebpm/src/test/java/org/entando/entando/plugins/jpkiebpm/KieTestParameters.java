/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm;

/**
 *
 * @author Entando
 */
public interface KieTestParameters {

    public final static Boolean TEST_ENABLED = true;

    // THE VALUES BELOW MUST EXIST ON THE BPM !!!
    public final static String USERNAME = "bpmsAdmin";
    public final static String PASSWORD = "bpmsuite1!";

    public final static String HOSTNAME = "entando-jbpms.sorint.it";
    public final static String SCHEMA = "http";
    public final static String WEBAPP = "kie-server";
    public final static Integer PORT = 80;

    public final static String TARGET_PROCESS_ID = "com.redhat.bpms.examples.mortgage.MortgageApplication";
    public final static String TARGET_CONTAINER_ID = "com.redhat.bpms.examples:mortgage:1";
    public final static String TARGET_PROCESS_INSTANCE_ID = "155";
}
