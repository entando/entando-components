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
package com.agiletec.plugins.jpphotogallery.aps.tags.util;

import java.util.Collection;
import java.util.List;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.aps.tags.util.PagerTagHelper;
import com.agiletec.aps.util.ApsProperties;


public class PhotogalleryPagerTagHelper extends PagerTagHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PhotogalleryPagerTagHelper.class);
	
	/**
	 * Restituisce l'oggetto necessario per fornire gli elementi necessari
	 * a determinare l'item corrente.
	 * @param collection La collection degli elementi da paginare.
	 * @param pagerId L'identificativo (specificato nel tag) del paginatore. Può essere null.
	 * @param pagerIdFromFrame Determina se ricavare l'identificativo dall'id del frame
	 * dove è inserito il paginatore.
	 * @param max Il numero massimo (specificato nel tag) di elementi per item.
	 * Nel caso che sia 0 (o non sia stato specificato nel tag) il valore
	 * viene ricercato nei parametri di configurazione della showlet.
	 * @param isAdvanced Specifica se il pginatore è in modalità avanzata.
	 * @param offset Campo offset, considerato solo nel caso di paginatore avanzato.
	 * @param request La request.
	 * @return L'oggetto necessario per fornire gli elementi necessari a determinare l'item corrente.
	 * @throws ApsSystemException In caso di errori nella costruzione dell'oggetto richiesto.
	 */
	public IPagerVO getPagerVO(Collection collection, String pagerId, boolean pagerIdFromFrame,
			int max, boolean isAdvanced, int offset, ServletRequest request) throws ApsSystemException {
		IPagerVO pagerVo = null;
		try {
			String truePagerId = this.getPagerId(pagerId, pagerIdFromFrame, request);
			int maxElement = this.getMaxElementForItem(max, request);
			int item = this.getItemNumber(collection, maxElement, truePagerId, request);
			pagerVo = this.buildPageVO(collection, item, maxElement, truePagerId, isAdvanced, offset);
		} catch (Throwable t) {
			_logger.error("Error creating pagerVO", t);
			throw new ApsSystemException("Error creating pagerVO", t);
		}
		return pagerVo;
	}

	protected int getMaxElementForItem(int maxItems, ServletRequest request) {
		if (maxItems == 0) {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			if (reqCtx != null) {
				Widget showlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
				ApsProperties config = showlet.getConfig();
				String stringMax = (String) config.get("maxElemForItem");
				if (stringMax != null && stringMax.length() > 0) {
					maxItems = Integer.parseInt(stringMax);
				}
			}
		}
		return maxItems;
	}

	protected int getItemNumber(Collection collection, int maxElement, String truePagerId, ServletRequest request) {
		String stringItem = null;
		if (null != truePagerId) {
			stringItem = request.getParameter(truePagerId+"_item");
		} else {
			stringItem = request.getParameter("item");
		}
		if (null == stringItem) {
			stringItem = request.getParameter("pivotContent");
			if (stringItem != null) {
				//RICERCA NUMERO ITEM DA POSIZIONE dell'identificativo
				List<String> list = (List) collection;
				int indexSearched = -1;
				for (int i=0; i<list.size(); i++) {
					String contentId = list.get(i);
					if (stringItem.equals(contentId)) {
						indexSearched = i;
						break;
					}
				}
				if (indexSearched>=0) {
					int index = 0;
					if ((indexSearched+1)%maxElement==0) {
						index = ((indexSearched+1)/maxElement);
					} else {
						index = ((indexSearched+1)/maxElement)+1;
					}
					return index;
				}
			}
		}
		int item = 0;
		if (stringItem != null) {
			try {
				item = Integer.parseInt(stringItem);
			} catch (Throwable t) {
				_logger.error("Error parsing stringItem '{}'", stringItem, t);
			}
		}
		return item;
	}

	private String getPagerId(String pagerId, boolean pagerIdFromFrame, ServletRequest request) {
		String truePagerId = pagerId;
		if (null == truePagerId && pagerIdFromFrame) {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			if (reqCtx != null) {
				int currentFrame = this.getCurrentFrame(reqCtx);
				truePagerId = "frame" + currentFrame;
			}
		}
		return truePagerId;
	}

	private int getCurrentFrame(RequestContext reqCtx) {
		Integer frame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		int currentFrame = frame.intValue();
		return currentFrame;
	}

	public static final int DEFAULT_OFFSET = 10;

}