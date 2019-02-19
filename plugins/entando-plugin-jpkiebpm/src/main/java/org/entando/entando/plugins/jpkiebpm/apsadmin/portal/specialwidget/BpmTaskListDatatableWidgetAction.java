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
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;

public class BpmTaskListDatatableWidgetAction extends BpmDatatableWidgetAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmProcessDatatableWidgetAction.class);

    private String DEMO_USER = "taskUser";
    private String redirectDetailsPage;
    private boolean redirectOnClickRow;
    private boolean showClaimButton;
    private boolean showCompleteButton;

    public String getRedirectDetailsPage() {
        return redirectDetailsPage;
    }

    public void setRedirectDetailsPage(String redirectPage) {
        this.redirectDetailsPage = redirectPage;
    }

    public boolean isRedirectOnClickRow() {
        return redirectOnClickRow;
    }

    public void setRedirectOnClickRow(boolean redirectOnClickRow) {
        this.redirectOnClickRow = redirectOnClickRow;
    }

    public boolean isShowClaimButton() {
        return showClaimButton;
    }

    public void setShowClaimButton(boolean showClaimButton) {
        this.showClaimButton = showClaimButton;
    }

    public boolean isShowCompleteButton() {
        return showCompleteButton;
    }

    public void setShowCompleteButton(boolean showCompleteButton) {
        this.showCompleteButton = showCompleteButton;
    }

   
    @Override
    protected void loadFieldIntoDatatableFromBpm(String containerId, String procId) throws ApsSystemException {
        HashMap<String, String> opt = new HashMap<>();
        opt.put("user", DEMO_USER);
        KieBpmConfig config = super.getFormManager().getKieServerConfigurations().get(this.getKnowledgeSourcePath());
        List<KieTask> task = super.getFormManager().getHumanTaskList(config, null, opt);

        Set<String> variableFields = super.getFormManager().getProcessVariables(config, containerId, procId);
        //JPW -- Assign process variables for datatable
        //        /server/queries/processes/instances/{pInstanceId}/variables/instances
        //        /server/containers/{id}/processes/definitions/{pId}/variables
        //JPW -- Assog tasl varoab;es

        List<String> fields = new ArrayList<>();
        fields.addAll(variableFields);

        //Horrible hack. To be replaced by an actual call.
        if (!task.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(task.get(0).toString(), ",");
            while (tokenizer.hasMoreTokens()) {
                final String name = tokenizer.nextToken().trim();
                fields.add(name);
            }
        }

        loadDataIntoFieldDatatable(fields);
    }

    @Override
    protected ApsProperties addAdditionalPropertiesIntoWidgetInfo() {

        ApsProperties properties = new ApsProperties();
        properties.put(KieBpmSystemConstants.WIDGET_PARAM_REDIRECT_ONCLICK_ROW, Boolean.toString(isRedirectOnClickRow()));
        if (isRedirectOnClickRow()) {
            properties.put(KieBpmSystemConstants.WIDGET_PARAM_REDIRECT_ONCLICK_ROW_DETAILS_PAGE, redirectDetailsPage);
        } else {
            properties.put(KieBpmSystemConstants.WIDGET_PARAM_REDIRECT_ONCLICK_ROW_DETAILS_PAGE, "");
        }
        properties.put(KieBpmSystemConstants.WIDGET_PARAM_SHOW_CLAIM_BUTTON, Boolean.toString(isShowClaimButton()));
        properties.put(KieBpmSystemConstants.WIDGET_PARAM_SHOW_COMPLETE_BUTTON, Boolean.toString(isShowCompleteButton()));

        return properties;
    }

    @Override
    protected void loadWidgetInfo() {
        super.loadWidgetInfo();
        
        final String widgetInfoId = this.getWidget().getConfig().getProperty(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID);

        if (StringUtils.isNotBlank(widgetInfoId)) {

            try {
                BpmWidgetInfo widgetInfo = this.getBpmWidgetInfoManager().getBpmWidgetInfo(Integer.valueOf(widgetInfoId));

                String propertyOnClickRow = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_PARAM_REDIRECT_ONCLICK_ROW);
                if (StringUtils.isNotBlank(propertyOnClickRow)) {
                    this.setRedirectOnClickRow(Boolean.valueOf(propertyOnClickRow));
                    this.setRedirectDetailsPage(widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_PARAM_REDIRECT_ONCLICK_ROW_DETAILS_PAGE));
                } else {
                    this.setRedirectOnClickRow(false);
                    this.setRedirectDetailsPage("");
                }
                
                
                String propertyShowClaimButton = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_PARAM_SHOW_CLAIM_BUTTON);
                if (StringUtils.isNotBlank(propertyShowClaimButton)) {
                    this.setShowClaimButton(Boolean.valueOf(propertyShowClaimButton));
                }else
                {
                    this.setShowClaimButton(false);
                }
                
                String propertyShowCompleteButton = widgetInfo.getConfigDraft().getProperty(KieBpmSystemConstants.WIDGET_PARAM_SHOW_COMPLETE_BUTTON);
                if (StringUtils.isNotBlank(propertyShowCompleteButton)) {
                    this.setShowCompleteButton(Boolean.valueOf(propertyShowCompleteButton));
                } else {
                    this.setShowCompleteButton(false);
                }


            } catch (ApsSystemException e) {
                logger.error("Error loading WidgetInfo", e);
            }
        }
    }
    
    /**
    * Return a plain list of the free pages in the portal.
    *
    * @return the list of the free pages of the portal.
    */
    public List<IPage> getFreePages() {
        IPage root = this.getPageManager().getOnlineRoot();
        List<IPage> pages = new ArrayList<>();
        this.addFreePublicPages(root, pages);
        return pages;
    }

    private void addFreePublicPages(IPage page, List<IPage> pages) {
        if (null == page) {
            return;
        }
        if (page.getGroup().equals(Group.FREE_GROUP_NAME)) {
            pages.add(page);
        }
        String[] children = page.getChildrenCodes();
        for (int i = 0; i < children.length; i++) {
            IPage child = this.getPageManager().getOnlinePage(children[i]);
            this.addFreePublicPages(child, pages);
        }
    }


}
