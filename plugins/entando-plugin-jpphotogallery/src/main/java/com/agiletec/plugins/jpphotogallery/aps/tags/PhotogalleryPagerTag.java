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
package com.agiletec.plugins.jpphotogallery.aps.tags;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.tags.PagerTag;
import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.aps.tags.util.PagerTagHelper;
import com.agiletec.plugins.jpphotogallery.aps.tags.util.PhotogalleryPagerTagHelper;

public class PhotogalleryPagerTag extends PagerTag {

	private static final Logger _logger = LoggerFactory.getLogger(PhotogalleryPagerTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			Collection object = (Collection) this.pageContext.getAttribute(this.getListName());
			if (object == null) {
				_logger.error("No list in request");
			} else {
				PagerTagHelper helper = new PhotogalleryPagerTagHelper();
				IPagerVO pagerVo = helper.getPagerVO(object, this.getPagerId(), 
						this.isPagerIdFromFrame(), this.getMax(),  this.isAdvanced(), this.getOffset(), request);
				this.pageContext.setAttribute(this.getObjectName(), pagerVo);
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return EVAL_BODY_INCLUDE;
	}	
}