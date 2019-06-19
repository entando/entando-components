/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.DashboardTable;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.IDashboardTableManager;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IotDefaultConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IotDefaultConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DefaultServerConfigurationTag extends TagSupport {

  private static final Logger _logger =  LoggerFactory.getLogger(DefaultServerConfigurationTag.class);

  @Override
  public int doStartTag() throws JspException {
    IotDefaultConfigManager iotDefaultConfigManager = (IotDefaultConfigManager) ApsWebApplicationUtils.getBean("IotDefaultConfigManager", this.pageContext);
    try {
      this.pageContext.setAttribute(this.getVar(), iotDefaultConfigManager.getIotTag());
    } catch (Throwable t) {
      _logger.error("Error in doStartTag", t);
      throw new JspException("Error in DefaultServerConfigurationTag doStartTag", t);
    }
    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    this.release();
    return super.doEndTag();
  }

  @Override
  public void release() {
    this.setVar(null);
    this.setKey(null);
  }

  private Widget extractWidget(RequestContext reqCtx) {
    Widget widget = null;
    widget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
    return widget;
  }

  protected String extractWidgetParameter(String parameterName, ApsProperties widgetConfig, RequestContext reqCtx) {
    return (String) widgetConfig.get(parameterName);
  }

  public String getVar() {
    return _var;
  }
  public void setVar(String var) {
    this._var = var;
  }

  public Integer getKey() {
    return _key;
  }
  public void setKey(Integer key) {
    this._key = key;
  }

  private String _var;
  private Integer _key;
}
