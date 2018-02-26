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
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.kieProcess;

/**
 *
 * @author own_strong
 */
public class BpmCaseProgressWidgetAction extends BpmDatatableWidgetAction {

    private CaseManager caseManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    @Override
    protected void loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        List<kieProcess> processes = caseManager.getProcessDefinitionsList();
        if (!processes.isEmpty()) {
            super.loadDataIntoFieldDatatable(processes);
        }

        HashMap<String, String> columns = new HashMap<>();

        columns.put("Status Progress", "statusProgress");
        columns.put("Customer Name", "customerName");
        columns.put("partyName", "partyName");
        columns.put("status", "status");
        columns.put("Company", "company");
        columns.put("Case Due In", "dueDate");

        this.loadDataIntoFieldDatatable(columns);
    }

    private void loadDataIntoFieldDatatable(HashMap fields) {

        Byte position = 1;
        for (Iterator<Map.Entry> iter = fields.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, String> obj = iter.next();
            final String name = obj.getValue();
            final FieldDatatable fd = new FieldDatatable(name);
            fd.setField(PREFIX_FIELD + obj.getKey());
            fd.setPosition(position++);
            fd.setVisible(Boolean.valueOf(true));
            fd.setOverride("");
            this.fieldsDatatable.add(fd);

        }

    }

    @Override
    public IGroupManager getGroupManager() {
        return groupManager;
    }

    @Override
    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    @Override
    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }

    @Override
    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }


}
