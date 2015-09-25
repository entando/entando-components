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
