/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.apsadmin.util;

import javax.mail.Address;

import com.agiletec.aps.util.HtmlHandler;

/**
 * @author E.Santoboni
 */
public class WebMailHelper {
	
	public static String joinAddress(Address[] address) {
		String s = "";
		if (address != null) {
			for (int i = 0; i < address.length; i++) {
				s += address[i].toString() + ", ";
			}
			if (s.length() >= 2) {
				s = s.substring(0, s.length() - 2);
			}
		} else {
			s = "";
		}
		return s;
	}
	
	public static boolean isHtmlContent(String content) {
		if (null == content) return false; 
		int indexStart = content.indexOf("<HTML");
		if (indexStart>=0) {
			int indexEnd = content.indexOf("</HTML");
			if (indexEnd>indexStart) return true;
		} else {
			indexStart = content.indexOf("<html");
			if (indexStart>=0) {
				int indexEnd = content.indexOf("</html");
				if (indexEnd>indexStart) return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public static String getCleanedHtmlContent(String content) {
		//System.out.println("PURGIARE CONTENUTO " + content);
		int indexStart = content.indexOf("<BODY");
		if (indexStart>=0) {
			int indexEnd = content.indexOf("</BODY", indexStart);
			return getCleanedHtmlContent(indexStart, indexEnd, content);
		} else {
			indexStart = content.indexOf("<body");
			if (indexStart>=0) {
				int indexEnd = content.indexOf("</body", indexStart);
				return getCleanedHtmlContent(indexStart, indexEnd, content);
			} else {
				return "WRONG HTML CONTENT";
			}
		}
	}
	
	private static String getCleanedHtmlContent(int indexStart, int indexEnd, String content) {
		int realIndexStart = content.indexOf(">", indexStart);
		String body = content.substring(realIndexStart+1, indexEnd);
		//System.out.println("*********************************************");
		//System.out.println(body);
		//System.out.println("*********************************************");
		HtmlHandler htmlHandler = new HtmlHandler();
		return htmlHandler.getParsedText(body);
	}
	
}
