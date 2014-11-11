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
package org.entando.entando.plugins.jpsolrclient.aps.system.services.config;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.entando.entando.plugins.jpsolrclient.aps.system.services.config.model.SolrConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author S.Loru
 */
public class SolrConfigDOM {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SolrConfigDOM.class);
	
	public static String marshalConfig(SolrConfig solrConfig) throws ApsSystemException {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(SolrConfig.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT , Boolean.TRUE);
			marshaller.marshal(solrConfig, writer);
		} catch (Throwable t) {
			_logger.error("Error binding object", t);
			throw new ApsSystemException("Error binding object", t);
		}
		return writer.toString();
	}
	
	public static SolrConfig unmarshalConfig(String xml) throws ApsSystemException {
		SolrConfig bodyObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(SolrConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
			bodyObject = (SolrConfig) unmarshaller.unmarshal(is);
		} catch (Throwable t) {
			_logger.error("Error unmarshalling activity stream info config. xml:{}", xml, t);
			throw new ApsSystemException("Error unmarshalling activity stream info config", t);
		}
		return bodyObject;
	}

}
