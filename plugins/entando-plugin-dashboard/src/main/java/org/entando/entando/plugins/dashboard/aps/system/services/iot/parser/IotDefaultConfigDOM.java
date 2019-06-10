package org.entando.entando.plugins.dashboard.aps.system.services.iot.parser;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IotDefaultConfig;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.StringReader;

public class IotDefaultConfigDOM {

  public static final String DATASOURCE_CODE = "datasourceCode";
  public static final String DASHBOARD_ID = "dashboardId";
  public static final String ROOT = "iot";

  public IotDefaultConfig extractConfig(String xml) throws ApsSystemException {
    IotDefaultConfig config = new IotDefaultConfig();
    Element root = this.getRootElement(xml);
    Element datasourceCode = root.getChild(DATASOURCE_CODE);
    if (datasourceCode != null) {
      String datasourceCodeText = datasourceCode.getText();
      config.setDatasourceCode(datasourceCodeText);
    }
    Element dashboardId = root.getChild(DASHBOARD_ID);
    if (dashboardId != null) {
      String dashboardIdText = dashboardId.getText();
      config.setDashboardId(Long.parseLong(dashboardIdText));
    }
    
    return config;
  }

  private Element getRootElement(String xmlText) throws ApsSystemException {
    SAXBuilder builder = new SAXBuilder();
    builder.setValidation(false);
    StringReader reader = new StringReader(xmlText);
    Element root = null;
    try {
      Document doc = builder.build(reader);
      root = doc.getRootElement();
    } catch (Throwable t) {
      ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
      throw new ApsSystemException("Error parsing xml", t);
    }
    return root;
  }

  private Element createConfigElement(IotDefaultConfig config) {
    Element configElem = new Element(ROOT);
    Element datasourceCode = new Element(DATASOURCE_CODE);
    datasourceCode.setText(config.getDatasourceCode());
    configElem.addContent(datasourceCode);
    Element dashboardId = new Element(DASHBOARD_ID);
    dashboardId.setText(config.getDashboardId().toString());
    configElem.addContent(dashboardId);
    return configElem;
  }

  public String createConfigXml(IotDefaultConfig config) throws ApsSystemException {
    Element root = this.createConfigElement(config);
    Document doc = new Document(root);
    XMLOutputter out = new XMLOutputter();
    Format format = Format.getPrettyFormat();
    format.setIndent("\t");
    out.setFormat(format);
    return out.outputString(doc);
  }
  
}
