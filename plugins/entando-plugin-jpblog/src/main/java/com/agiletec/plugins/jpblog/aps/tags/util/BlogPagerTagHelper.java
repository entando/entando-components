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
package com.agiletec.plugins.jpblog.aps.tags.util;

import java.util.Collection;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.aps.tags.util.PagerTagHelper;

/**
 * Acts like the pager tag except:
 * 	<li>when the url contains a content id present in the pager collection, this pager will have always MAX_ELEMENTS == 1</li>
 *  <li>when no specific position is present in url parameters (getItemNumber), the position will be the position of the contentId inside the pager collection (and MAX_ELEMENTS == 1)</li>
 *
 */
public class BlogPagerTagHelper extends PagerTagHelper {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogPagerTagHelper.class);
	
	@Override
	public IPagerVO getPagerVO(Collection collection, String pagerId, boolean pagerIdFromFrame,
			int max, boolean isAdvanced, int offset, ServletRequest request) throws ApsSystemException {
		IPagerVO pagerVo = null;
		try {
			String truePagerId = this.getPagerId(pagerId, pagerIdFromFrame, request);
			String contentId = request.getParameter("contentId");
			this.setContentId(contentId);
			int item = this.getItemNumber(truePagerId, request);
			int maxElement = this.getMaxElementForItem(max, request);
			if (this.isThereBlogContentIdParam(request, collection)) {
				item = this.getItemParamBlogContentIdParam(item,contentId, request, collection);
				maxElement = 1;
			}
			pagerVo = this.buildPageVO(collection, item, maxElement, truePagerId, isAdvanced, offset);
		} catch (Throwable t) {
			_logger.error("Error while preparing the pagerVo object", t);
			throw new ApsSystemException("Error while preparing the pagerVo object", t);
		}
		return pagerVo;
	}

	/**
	 * Return true when the pager collection contains a content Id specified as parameter
	 * @param request
	 * @param collection
	 * @return
	 */
	public boolean isThereBlogContentIdParam(ServletRequest request, Collection collection) {
		if (StringUtils.isNotBlank(_contentId)) {
			if (collection instanceof List && collection.contains(this._contentId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the pagerItem for a specific contentId, OLNY when there is no other position specified
	 * @param currentItem
	 * @param contentId
	 * @param request
	 * @param collection
	 * @return
	 */
	protected int getItemParamBlogContentIdParam(int currentItem, String contentId, ServletRequest request, Collection<String> collection) {
		int pagerVal = currentItem;
		if (pagerVal == 0) {
			pagerVal = ((List<String>)collection).indexOf(contentId) + 1;
		}
		return pagerVal;
	}
	
	private int getItemNumber(String truePagerId, ServletRequest request) {
		String stringItem = null;
		if (null != truePagerId) {
			stringItem = request.getParameter(truePagerId+"_item");
		} else {
			stringItem = request.getParameter("item");
		}
		int item = 0;
		if (stringItem != null) {
			try {
				item = Integer.parseInt(stringItem);
			} catch (NumberFormatException e) {
				_logger.error("Error while parsing the stringItem {}", stringItem);
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

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	private String _contentId;
	
}
