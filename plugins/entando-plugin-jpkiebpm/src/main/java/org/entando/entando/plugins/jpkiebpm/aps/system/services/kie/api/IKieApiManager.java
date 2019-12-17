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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model.JAXBProcessInstanceList;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiInputForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiSignal;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessInstance;

import java.util.List;
import java.util.Properties;

public interface IKieApiManager {

    KieApiForm getBpmForm(Properties properties) throws Throwable;

    void postBpmForm(KieBpmConfig bpmConfig, KieApiInputForm form) throws Throwable;

    List<KieProcessInstance> getInstanceProcessesList(Properties properties) throws Throwable;

    void postSignal(KieBpmConfig bpmConfig, KieApiSignal signal) throws Throwable;

    void startNewProcess(KieBpmConfig bpmConfig, KieApiProcessStart process) throws Throwable;

    public List<KieProcessInstance> processList(Properties properties) throws Throwable;

    public JAXBProcessInstanceList processInstancesDataTable(Properties properties) throws Throwable;
}
