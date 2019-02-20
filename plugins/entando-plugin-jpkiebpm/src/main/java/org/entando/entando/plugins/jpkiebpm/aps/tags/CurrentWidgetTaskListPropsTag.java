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
package org.entando.entando.plugins.jpkiebpm.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.entando.entando.aps.tags.ExtendedTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;

@SuppressWarnings("serial")
public class CurrentWidgetTaskListPropsTag extends ExtendedTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(CurrentWidgetTaskListPropsTag.class);

    private String redirectOnClickRowVar;
    private String redirectDetailsPageVar;
    private String showCompleteButtonVar;
    private String showClaimButtonVar;
    
    @Override
    public int doStartTag() throws JspException {
        try {
            Widget widget = this.extractShowlet();
            String widgetInfoId = widget.getConfig().getProperty("widgetInfoId");
            IBpmWidgetInfoManager bpmWidgetInfoManager = (IBpmWidgetInfoManager) ApsWebApplicationUtils.getBean("jpkiebpmBpmWidgetInfoManager", this.pageContext);
            BpmWidgetInfo bpmWidgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(Integer.valueOf(widgetInfoId));
            String redirectOnClickRow = (String) bpmWidgetInfo.getConfigOnline().get(KieBpmSystemConstants.WIDGET_PARAM_REDIRECT_ONCLICK_ROW);
            String redirectDetailsPage = (String) bpmWidgetInfo.getConfigOnline().get(KieBpmSystemConstants.WIDGET_PARAM_REDIRECT_ONCLICK_ROW_DETAILS_PAGE);
            String showClaimButton = (String) bpmWidgetInfo.getConfigOnline().get(KieBpmSystemConstants.WIDGET_PARAM_SHOW_CLAIM_BUTTON);
            String showCompleteButton = (String) bpmWidgetInfo.getConfigOnline().get(KieBpmSystemConstants.WIDGET_PARAM_SHOW_COMPLETE_BUTTON);

 
            if (null != redirectOnClickRowVar) {
                this.pageContext.setAttribute(this.getRedirectOnClickRowVar(), redirectOnClickRow);
            }
            if (null != this.redirectDetailsPageVar) {
                this.pageContext.setAttribute(this.getRedirectDetailsPageVar(), redirectDetailsPage);
            }
            if (null != showClaimButtonVar) {
                this.pageContext.setAttribute(this.getShowClaimButtonVar(), showClaimButton);
            }
            if (null != showCompleteButtonVar) {
                this.pageContext.setAttribute(this.getShowCompleteButtonVar(), showCompleteButton);
            }

        } catch (Throwable t) {
            String msg = "Error detected during showlet preprocessing";
            logger.error(msg, t);
            throw new JspException(msg, t);
        }
        return super.doStartTag();
    }

    private Widget extractShowlet() {
        ServletRequest req = this.pageContext.getRequest();
        RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
        Widget widget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
        return widget;
    }

    @Override
    public void release() {
        super.release();
        this.redirectOnClickRowVar = null;
        this.redirectDetailsPageVar = null;
        this.showCompleteButtonVar = null;
        this.showClaimButtonVar = null;

    }

    public String getRedirectDetailsPageVar() {
        return redirectDetailsPageVar;
    }

    public void setRedirectDetailsPageVar(String redirectDetailsPageVar) {
        this.redirectDetailsPageVar = redirectDetailsPageVar;
    }

    public String getRedirectOnClickRowVar() {
        return redirectOnClickRowVar;
    }

    public void setRedirectOnClickRowVar(String redirectOnClickRowVar) {
        this.redirectOnClickRowVar = redirectOnClickRowVar;
    }

    public String getShowCompleteButtonVar() {
        return showCompleteButtonVar;
    }

    public void setShowCompleteButtonVar(String showCompleteButtonVar) {
        this.showCompleteButtonVar = showCompleteButtonVar;
    }

    public String getShowClaimButtonVar() {
        return showClaimButtonVar;
    }

    public void setShowClaimButtonVar(String showClaimButtonVar) {
        this.showClaimButtonVar = showClaimButtonVar;
    }

}
