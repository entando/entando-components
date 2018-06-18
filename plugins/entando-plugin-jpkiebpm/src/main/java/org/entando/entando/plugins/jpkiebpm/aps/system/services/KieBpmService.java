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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.model.DatatableWidgetConfigDto;
import org.entando.entando.plugins.jpkiebpm.web.model.DatatableWidgetConfigRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author paddeo
 */
public class KieBpmService implements IKieBpmService {
    
    private final String PROP_BPM_GROUP = "groups";
    private final String PREFIX_BPM_GROUP = "bpm_";
    private final String PREFIX_FIELD = "field_";
    private final String PREFIX_VISIBLE = "visible_";
    private final String PREFIX_OVERRIDE = "override_";
    private final String PREFIX_POSITION = "position_";
    
    public static final String PROP_NAME_WIDGET_INFO_ID = KieBpmSystemConstants.WIDGET_PARAM_INFO_ID;//"widgetInfoId";

    protected final String PROP_PROCESS_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_PROCESS_ID;
    protected final String PROP_CONTAINER_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_CONTAINER_ID;
    protected final String PROP_OVERRIDE_ID = KieBpmSystemConstants.WIDGET_INFO_PROP_OVERRIDE_ID;
    
    private String DEMO_USER = "taskUser";
    
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    
    @Autowired
    @Qualifier("jpkiebpmsCaseManager")
    private IKieFormManager formManager;
    
    @Autowired
    private IDtoBuilder<BpmWidgetInfo, DatatableWidgetConfigDto> dtoBuilder;
    
    public IBpmWidgetInfoManager getBpmWidgetInfoManager() {
        return bpmWidgetInfoManager;
    }
    
    public void setBpmWidgetInfoManager(IBpmWidgetInfoManager bpmWidgetInfoManager) {
        this.bpmWidgetInfoManager = bpmWidgetInfoManager;
    }
    
    public IKieFormManager getFormManager() {
        return formManager;
    }
    
    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }
    
    public IDtoBuilder<BpmWidgetInfo, DatatableWidgetConfigDto> getDtoBuilder() {
        return dtoBuilder;
    }
    
    public void setDtoBuilder(IDtoBuilder<BpmWidgetInfo, DatatableWidgetConfigDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }
    
    @Override
    public DatatableWidgetConfigDto getDataTableWIdgetConfig(int configId) {
        try {
            BpmWidgetInfo info = this.getBpmWidgetInfoManager().getBpmWidgetInfo(configId);
            return this.getDtoBuilder().convert(info);
        } catch (Throwable t) {
            logger.error("error in loading widget config", t);
            throw new RestServerError("error in loading widget config", t);
        }
    }
    
    @Override
    public DatatableWidgetConfigDto createDataTableWIdgetConfig(DatatableWidgetConfigRequest req) {
        try {
            BpmWidgetInfo info = this.storeWidgetInfo(req);
            return this.getDtoBuilder().convert(info);
        } catch (Throwable t) {
            logger.error("error in creating widget config", t);
            throw new RestServerError("error in creating widget config", t);
        }
    }
    
    @Override
    public DatatableWidgetConfigDto updateDataTableWIdgetConfig(DatatableWidgetConfigRequest req) {
        try {
            BpmWidgetInfo info = this.storeWidgetInfo(req);
            return this.getDtoBuilder().convert(info);
        } catch (Throwable t) {
            logger.error("error in updating widget config", t);
            throw new RestServerError("error in updating widget config", t);
        }
    }
    
    @Override
    public DatatableWidgetConfigDto deleteDataTableWIdgetConfig(int configId) {
        try {
            BpmWidgetInfo info = this.getBpmWidgetInfoManager().getBpmWidgetInfo(configId);
            this.getBpmWidgetInfoManager().deleteBpmWidgetInfo(configId);
            return this.getDtoBuilder().convert(info);
        } catch (Throwable t) {
            logger.error("error in config delete", t);
            throw new RestServerError("error in config delete", t);
        }
    }
    
    @Override
    public DatatableWidgetConfigDto chooseForm() {
        try {
            return this.loadFieldIntoDatatableFromBpm();
        } catch (Throwable t) {
            logger.error("error in choose form", t);
            throw new RestServerError("error in chose form", t);
        }
    }
    
    private BpmWidgetInfo storeWidgetInfo(DatatableWidgetConfigRequest req) {
        BpmWidgetInfo widgetInfo = null;
        try {
            widgetInfo = new BpmWidgetInfo();
            widgetInfo.setFramePosDraft(req.getFramePosDraft());
            widgetInfo.setPageCode(req.getPageCode());
            
            widgetInfo.setWidgetType(req.getWidgetType());
            ApsProperties properties = new ApsProperties();
            this.setPropertiesIntoWidgetInfo(properties, req);
            widgetInfo.setInformationDraft(properties.toXml());
            widgetInfo.setId(req.getId());
            this.updateConfigWidget(widgetInfo);
        } catch (Exception e) {
            logger.error("Error in save WidgetInfo", e);
            
        }
        return widgetInfo;
    }
    
    private void setPropertiesIntoWidgetInfo(ApsProperties properties, DatatableWidgetConfigRequest req) {
        String procId = req.getProcessId();
        String contId = req.getContainerId();
        String sourceId = req.getKnowledgeSourceId();
        properties.put(PROP_PROCESS_ID, procId);
        properties.put(PROP_CONTAINER_ID, contId);
        if (sourceId != null) {
            properties.put(KieBpmSystemConstants.WIDGET_INFO_PROP_KIE_SOURCE_ID, sourceId);
        }
        if (req.getGroup() != null) {
            properties.put(PROP_BPM_GROUP, req.getGroup());
        }
        req.getInformationDraft().forEach(fieldMap -> {
            String field = (String) fieldMap.get("field") != null ? (String) fieldMap.get("field") : "";
            String visible = (String) fieldMap.get("visible") != null ? (String) fieldMap.get("visible") : "";
            String override = (String) fieldMap.get("override") != null ? (String) fieldMap.get("override") : "";
            String position = (String) fieldMap.get("position") != null ? (String) fieldMap.get("position") : "";
            properties.put(PREFIX_FIELD + field, PREFIX_FIELD + field);
            properties.put(PREFIX_VISIBLE + field, visible);
            properties.put(PREFIX_OVERRIDE + field, override);
            properties.put(PREFIX_POSITION + field, position);
        });
    }
    
    private void updateConfigWidget(BpmWidgetInfo widgetInfo) throws ApsSystemException {
        if (widgetInfo.getId() == 0) {
            this.getBpmWidgetInfoManager().deleteBpmWidgetInfo(widgetInfo);
            this.getBpmWidgetInfoManager().addBpmWidgetInfo(widgetInfo);
        } else {
            this.getBpmWidgetInfoManager().updateBpmWidgetInfo(widgetInfo);
        }
    }
    
    private DatatableWidgetConfigDto loadFieldIntoDatatableFromBpm() throws ApsSystemException {
        DatatableWidgetConfigDto info = null;
        HashMap<String, String> opt = new HashMap<>();
        opt.put("user", DEMO_USER);
        List<KieTask> task = formManager.getHumanTaskList(null, opt);
        if (!task.isEmpty()) {
            info = this.loadDataIntoFieldDatatable(task);
        }
        return info;
    }
    
    private DatatableWidgetConfigDto loadDataIntoFieldDatatable(List element) {
        DatatableWidgetConfigDto info = new DatatableWidgetConfigDto();
        List<Map<String, String>> props = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(element.get(0).toString(), ",");
        Byte position = 1;
        while (tokenizer.hasMoreTokens()) {
            Map<String, String> prop = new HashMap<>();
            String name = tokenizer.nextToken().trim();
            prop.put("field", name);
            prop.put("position", position++ + "");
            prop.put("visible", "true");
            prop.put("override", "");
            props.add(prop);
        }
        info.setInformationDraft(props);
        return info;
    }
}
