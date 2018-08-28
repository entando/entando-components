/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.model.DatatableWidgetConfigDto;
import org.entando.entando.plugins.jpkiebpm.web.model.DatatableWidgetConfigRequest;

/**
 *
 * @author paddeo
 */
public interface IKieBpmService {

    String BEAN_NAME = "KieBpmService";

    public DatatableWidgetConfigDto getDataTableWidgetConfig(int configId);

    public DatatableWidgetConfigDto updateDataTableWidgetConfig(DatatableWidgetConfigRequest req);

    public DatatableWidgetConfigDto deleteDataTableWidgetConfig(int configId);

    public DatatableWidgetConfigDto chooseForm(KieBpmConfig config);

    public DatatableWidgetConfigDto chooseProcessForm(KieBpmConfig config);

    public DatatableWidgetConfigDto updateDataTypeWidgetConfig(KieBpmConfig config, DatatableWidgetConfigRequest req);
}
