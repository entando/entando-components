/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.parse;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailgunConfig;
import org.jdom.output.Format;

/*
<?xml version="1.0" encoding="UTF-8"?>
<mailgun>
<apikey>key-1efx6dc2-1fnyf8-s13ud7yisnibdqo6</apikey>
<domain>entadevelop.com</domain>
</mailgun>
*/



/**
 * Provides read and update operations for the jpmailgun plugin xml configuration.
 * 
 * @author Alberto Piras
 */
public class MailgunConfigDOM {
  /**
   * Extracts the jpmailgun configuration from an xml.
   * @param xml The xml containing the configuration.
   * @return The jpmail configuration.
   * @throws ApsSystemException In case of parsing errors.
   */
  public MailgunConfig extractConfig(String xml) throws ApsSystemException {
    MailgunConfig config = new MailgunConfig();
    Element root = this.getRootElement(xml);
    extractXML(root, config);
    return config;
  }
  
  /**
   * Extracts the apikey and domain from the xml element and save it into the MailgunConfig object.
   * @param root The xml root element containing the apikey and domain configuration.
   * @param config The configuration.
   */
  private void extractXML(Element root, MailgunConfig config) {
    Element elementAPI = root.getChild(ELEMENT_APIKEY);
    if (null != elementAPI) {
      String APIContent = elementAPI.getText();
      config.setApiKey(APIContent);
    }
    Element elementDomain = root.getChild(ELEMENT_DOMAIN);
    if (null != elementDomain) {
      String DomainContent = elementDomain.getText();
      config.setDomain(DomainContent);
    }
  }
    
    /**
   * Returns the Xml element from a given text.
   * @param xmlText The text containing an Xml.
   * @return The Xml element from a given text.
   * @throws ApsSystemException In case of parsing exceptions.
   */
  private Element getRootElement(String xmlText) throws ApsSystemException {
    SAXBuilder builder = new SAXBuilder();
    builder.setValidation(false);
    StringReader reader = new StringReader(xmlText);
    Document	_doc =new Document();
    try {
      _doc = builder.build(reader);
    } catch (Throwable t) {
      System.out.println("Error while parsing xml : {}"+ xmlText+t);
      throw new ApsSystemException("Error detected while parsing the XML", t);
    }
    return _doc.getRootElement();
  }
  
  /**
   * Creates an xml containing the jpmail configuration.
   * @param config The jpmail configuration.
   * @return The xml containing the configuration.
   * @throws ApsSystemException In case of errors.
   */
	public String createConfigXml(MailgunConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
  
  /**
   * Extracts the smtp configuration from the xml element and save it into the MailConfig object.
   * @param root The xml root element containing the smtp configuration.
   * @param config The configuration.
   */
	private Element createConfigElement(MailgunConfig config) { 
		Element configElem = new Element(ROOT);
		Element apiElem = this.createAPIKeyElement(config);
		configElem.addContent(apiElem);
		Element domainElem = this.createDomainElement(config);
		configElem.addContent(domainElem);
		return configElem;
	}

	/**
	 * Create the senders element starting from the given MailConfig.
	 * @param config The configuration.
	 */
	private Element createAPIKeyElement(MailgunConfig config) {
		Element apiElem = new Element(ELEMENT_APIKEY);
		apiElem.setText(String.valueOf(config.getApiKey()));
		return apiElem;
	}
 
 	/**
	 * Create the senders element starting from the given MailConfig.
	 * @param config The configuration.
	 */
	private Element createDomainElement(MailgunConfig config) {
		Element domainElem = new Element(ELEMENT_DOMAIN);
		domainElem.setText(String.valueOf(config.getDomain()));
		return domainElem;
	}
 
  private final String ROOT = "mailgun";
  
  private final String PROTO_SSL = "ssl";
  private final String PROTO_TLS = "tls";
  private final String PROTO_STD = "std";

  private static final String ELEMENT_MAILGUN = "mailgun";
  private static final String ELEMENT_APIKEY = "apikey";
  private static final String ELEMENT_DOMAIN = "domain";
}
