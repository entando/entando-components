/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BpmProcessDatatableWidgetAction extends BpmDatatableWidgetAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmProcessDatatableWidgetAction.class);

    @Override
    protected void loadFieldIntoDatatableFromBpm(String containerId, String processId) throws ApsSystemException {
        KieBpmConfig config = getFormManager().getKieServerConfigurations().get(this.getKnowledgeSourcePath());
        List<KieProcess> processes = getFormManager().getProcessDefinitionsList(config);
        if (!processes.isEmpty()) {
            //Horrible hack. To be replaced by an actual call.
            List<String> fields = new ArrayList<>();

            StringTokenizer tokenizer = new StringTokenizer(processes.get(0).toString(), ",");
            Byte position = 1;
            while (tokenizer.hasMoreTokens()) {
                final String name = tokenizer.nextToken().trim();
                fields.add(name);
            }
            super.loadDataIntoFieldDatatable(fields);
        }

        Map<String, String> columns = new HashMap<>();
        this.loadDataIntoFieldDatatable(columns);
    }

    private void loadDataIntoFieldDatatable(Map<String, String> fields) {

        Byte position = 1;

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            final FieldDatatable fd = new FieldDatatable(entry.getValue());
            fd.setField(PREFIX_FIELD + entry.getKey());
            fd.setPosition(position++);
            fd.setVisible(true);
            fd.setOverride("");
            this.fieldsDatatable.add(fd);
        }
    }
}
