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
