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
package org.entando.entando.plugins.jpfrontshortcut.apsadmin.util;

import com.agiletec.apsadmin.util.ApsRequestParamsUtil;
import java.util.Enumeration;

import java.util.Iterator;
import java.util.Properties;
import javax.servlet.ServletRequest;

/**
 * @author E.Santoboni
 */
public class ApsAjaxActionParamsUtil extends ApsRequestParamsUtil {
	
	public static String[] getApsParams(String paramPrefix, String separator, ServletRequest request) {
		String[] apsParams = null;
		Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
        	String pname = (String) params.nextElement();
        	if (pname.startsWith("action:")) {
        		pname = pname.substring("action:".length());
        	}
        	if (pname.startsWith(paramPrefix)) {
        		apsParams = splitParam(pname, separator);
        		break;
        	}
        }
		Enumeration attributes = request.getAttributeNames();
		while (attributes.hasMoreElements()) {
			String pname = (String) attributes.nextElement();
			if (pname.startsWith(paramPrefix)) {
        		apsParams = splitParam(pname, separator);
        		break;
        	}
		}
        return apsParams;
	}
	
	public static String createApsActionParam(String action, Properties params) {
		StringBuilder buffer = new StringBuilder(action);
		if (params.size() > 0) {
			buffer.append("@");
			Iterator keys = params.keySet().iterator();
			boolean first = true;
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = params.getProperty(key);
				if (!first) {
					buffer.append(";");
				}
				buffer.append(key).append("=").append(value);
				first = false;
			}
		}
		return buffer.toString();
	}
	
}
