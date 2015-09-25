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
package com.agiletec.plugins.jpblog.aps.tags;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.tags.PagerTag;
import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.plugins.jpblog.aps.tags.util.BlogPagerTagHelper;

/**
 * Acts like PagerTag, except that
 * 	<li>the returned object (pagerVo) is created by a specific helper (BlogPagerTagHelper)</li>
 * 	<li>when the pager focuses on a specific content id, pushes a request attribute "[pagerId]_singleContent", used by the widget to activate specific behaviors</li>
 *
 */
public class BlogPagerTag extends PagerTag {
	
	private static final Logger _logger =  LoggerFactory.getLogger(BlogPagerTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			Collection<String> object = (Collection) this.pageContext.getAttribute(this.getListName());
			if (object == null) {
				_logger.error("There is no list in the request");
			} else {
				BlogPagerTagHelper helper = new BlogPagerTagHelper();
				IPagerVO pagerVo = helper.getPagerVO(object, this.getPagerId(), this.isPagerIdFromFrame(), this.getMax(),  this.isAdvanced(), this.getOffset(), request);
				this.pageContext.setAttribute(this.getObjectName(), pagerVo);
				if (helper.isThereBlogContentIdParam(request, object)) {
					String name = pagerVo.getPagerId() + "_singleContent";
					this.pageContext.setAttribute(name, "true");
				}
			}
		} catch (Throwable t) {
			_logger.error("Error detected during tag initialization", t);
			throw new JspException("Error detected during tag initialization", t);
		}
		return EVAL_BODY_INCLUDE;
	}
	
}
