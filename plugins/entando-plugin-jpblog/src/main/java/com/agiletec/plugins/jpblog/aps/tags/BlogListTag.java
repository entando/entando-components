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

import java.util.Calendar;
import java.util.Date;
import javax.servlet.jsp.JspException;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.tags.ContentListTag;
import com.agiletec.plugins.jpblog.aps.system.JpblogSystemConstants;

public class BlogListTag extends ContentListTag {

	@Override
	public int doStartTag() throws JspException {
		if (null != this.getMonth() && this.getMonth().trim().length() > 0) {
			Date start = DateConverter.parseDate(this.getMonth() + 01, "yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			int lastDate = cal.getActualMaximum(Calendar.DATE);
			cal.set(Calendar.DATE, lastDate);
			cal.set(Calendar.HOUR, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			EntitySearchFilter filter = EntitySearchFilter.createRoleFilter(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE, start, cal.getTime());
			this.addFilter(filter);
		}
		if (null != this.getCategory() && this.getCategory().trim().length() > 0) {
			this.addCategory(this.getCategory());
		}
		return super.doStartTag();
	}
	
	/**
	 * Return true if the system caching must involved in the search process.
	 * @return true if the system caching must involved in the search process.
	 */
	@Override
	public boolean isCacheable() {
		return false;
	}

	@Override
	public void release() {
		super.release();
	}


	public String getMonth() {
		return this.pageContext.getRequest().getParameter("jpblogmonth");
	}
	
	@Override
	public String getCategory() {
		return this.pageContext.getRequest().getParameter("jpblogcat");
	}
	
}
