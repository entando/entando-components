/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.ContentThreadConstants;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentState;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class Utils {

	/**
	 * restituisce la data nel formato dell'xml dei contenuti
	 *
	 * @param date
	 * @return
	 */
	public static String dataConverter(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(ContentThreadConstants.DATE_PATTERN);
		return format.format(date);
	}

	public static String printTimeStamp(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(ContentThreadConstants.PRINT_DATE_PATTERN);
		return format.format(date);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, List> getReferencingObjects(Content content, ApplicationContext appCtx)
			throws ApsSystemException {
		Map<String, List> references = new HashMap<String, List>();
		try {
			// String[] defNames =
			// ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(ContentUtilizer.class);
			String[] defNames = appCtx.getBeanNamesForType(ContentUtilizer.class);
			for (int i = 0; i < defNames.length; i++) {
				Object service = null;
				try {
					service = appCtx.getBean(defNames[i]);
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, null, "hasReferencingObject");
					service = null;
				}
				if (service != null) {
					ContentUtilizer contentUtilizer = (ContentUtilizer) service;
					List utilizers = contentUtilizer.getContentUtilizers(content.getId());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(contentUtilizer.getName() + "Utilizers", utilizers);
					}
				}
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Errore in hasReferencingObject", t);
		}
		return references;
	}

	private static StringBuilder fromListToString(List<ContentState> list) {
		StringBuilder ans = new StringBuilder("");
		ans.append("\n\nContenuti pubblicati:\n");
		for (Iterator<ContentState> i = list.iterator(); i.hasNext();) {
			ContentState currElem = i.next();
			ans.append(currElem.getContentid()).append(" - ").append(currElem.getDesc()).append(" - ")
					.append(currElem.getResult()).append("\n");
		}
		return ans;
	}

	private static StringBuilder fromListToStringSuspendContent(List<ContentState> list) {
		StringBuilder ans = new StringBuilder("");
		ans.append("\n\nContenuti sospesi:\n");
		for (Iterator<ContentState> i = list.iterator(); i.hasNext();) {
			ContentState currElem = i.next();
			ans.append(currElem.getContentid()).append(" - ").append(currElem.getDesc()).append(" - ")
					.append(currElem.getResult()).append("\n");
		}
		return ans;
	}

	private static StringBuilder fromListToStringMoveContent(List<ContentState> list) {
		StringBuilder ans = new StringBuilder("");
		ans = ans.append("\n\nContenuti spostati in archivio online:\n");
		for (Iterator<ContentState> i = list.iterator(); i.hasNext();) {
			ContentState currElem = i.next();
			ans.append(currElem.getContentid()).append(" - ").append(currElem.getDesc()).append(" - ")
					.append(currElem.getResult()).append("\n");
		}
		return ans;
	}

	private static StringBuilder fromListToHtmlString(List<ContentState> list, String applicationBaseURL) {
		StringBuilder ans = new StringBuilder("");
		if (list != null && list.size() > 0) {
			ans.append("<ul>");
		}
		for (Iterator<ContentState> i = list.iterator(); i.hasNext();) {
			ContentState currElem = i.next();
			if (currElem.getResult().equals(ContentThreadConstants.ACTION_SUCCESS)) {
				ans.append("<li style=\"color:#3D8A41\">");
			} else {
				ans.append("<li style=\"color:#B8311B\">");
			}
			ans.append("<a href=\"").append(applicationBaseURL).append("/do/Content/edit.action?contentId=")
					.append(currElem.getContentid()).append("\">");
			ans.append(currElem.getContentid()).append("</a> :: ").append("&nbsp;-&nbsp;").append(currElem.getDesc())
					.append(" :: ").append(currElem.getResult()).append("</li>");
			if (!i.hasNext()) {
				ans.append("</ul>");
			}
		}
		return ans;
	}

	public static String prepareMailHtml(List<ContentState> contentPList, List<ContentState> contentSList,
			List<ContentState> contentMList, ContentThreadConfig config, Date startJobDate, Date endJobDate,
			String applicationBaseUrl) {
		StringBuilder ans = new StringBuilder("");
		ans.append(config.getHtmlHeader());
		if (contentPList != null && contentPList.size() > 0) {
			StringBuilder published = fromListToHtmlString(contentPList, applicationBaseUrl);
			ans.append(published);
		}
		if (contentSList != null && contentSList.size() > 0) {
			StringBuilder suspended = fromListToHtmlString(contentSList, applicationBaseUrl);
			ans.append(config.getHtmlSeparator()).append(suspended);
		}
		if (contentMList != null && contentMList.size() > 0) {
			StringBuilder moved = fromListToHtmlString(contentMList, applicationBaseUrl);
			ans.append(config.getHtmlSeparatorMove()).append(moved);
		}
		ans.append("<p>Inizio Job: " + Utils.printTimeStamp(startJobDate) + "<br />")
				.append("Fine Job: " + Utils.printTimeStamp(endJobDate) + "</p>").append(config.getHtmlFooter());
		return ans.toString();
	}

	public static String prepareMailText(List<ContentState> contentPList, List<ContentState> contentSList,
			List<ContentState> contentMList, ContentThreadConfig config, Date startJobDate, Date endJobDate) {
		StringBuilder ans = new StringBuilder("");
		ans.append(config.getTextHeader());
		if (contentPList != null && contentPList.size() > 0) {
			StringBuilder published = fromListToString(contentPList);
			ans.append(published);
		}
		if (contentSList != null && contentSList.size() > 0) {
			StringBuilder suspended = fromListToStringSuspendContent(contentSList);
			ans.append(config.getTextSeparator()).append(suspended);
		}
		if (contentMList != null && contentMList.size() > 0) {
			StringBuilder moved = fromListToStringMoveContent(contentMList);
			ans.append(config.getTextSeparator()).append(moved);
		}
		ans.append("\n\nInizio Job: " + Utils.printTimeStamp(startJobDate) + "\n")
				.append("Fine Job: " + Utils.printTimeStamp(endJobDate) + "\n\n").append(config.getTextFooter());
		return ans.toString();
	}

	public static Date getYesterdayDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public static Date getTomorrowDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		return cal.getTime();
	}

}
