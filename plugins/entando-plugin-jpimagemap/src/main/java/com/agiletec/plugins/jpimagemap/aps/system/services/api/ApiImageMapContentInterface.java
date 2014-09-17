/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpimagemap.aps.system.services.api;

import java.util.Properties;

import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpimagemap.aps.system.services.api.model.JAXBImageMapContent;
import org.entando.entando.plugins.jacms.aps.system.services.api.ApiContentInterface;

/**
 * @author E.Santoboni
 */
public class ApiImageMapContentInterface extends ApiContentInterface {
	
    public JAXBImageMapContent getContent(Properties properties) throws ApiException, Throwable {
        return (JAXBImageMapContent) super.getContent(properties);
    }
	
	public String getContentToHtml(Properties properties) throws Throwable {
		return super.getContentToHtml(properties);
	}
	
	protected JAXBImageMapContent getJAXBContentInstance(Content mainContent, String langCode) {
		return new JAXBImageMapContent(mainContent, langCode);
	}
	
    public StringApiResponse addContent(JAXBImageMapContent jaxbContent, Properties properties) throws Throwable {
        return super.addContent(jaxbContent, properties);
    }
	
    public StringApiResponse updateContent(JAXBImageMapContent jaxbContent, Properties properties) throws Throwable {
        return super.updateContent(jaxbContent, properties);
    }
	
	public StringApiResponse deleteContent(Properties properties) throws Throwable {
		return super.deleteContent(properties);
	}
	
}