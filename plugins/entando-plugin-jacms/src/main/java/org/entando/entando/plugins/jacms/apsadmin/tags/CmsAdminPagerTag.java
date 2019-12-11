/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jacms.apsadmin.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.apsadmin.tags.util.AdminPagerTagHelper;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.Arrays;

/**
 * @author E.Santoboni
 */
public class CmsAdminPagerTag extends com.agiletec.apsadmin.tags.AdminPagerTag {

    private static final Logger logger = LoggerFactory.getLogger(CmsAdminPagerTag.class);

    private String total;
    private String maxSize;

    @Override
    public int doStartTag() throws JspException {
        ServletRequest request = this.pageContext.getRequest();
        try {
            Object realPagerId = this.findValue(this.getPagerId());
            Integer realTotal = Integer.parseInt(this.findValue(this.getTotal()).toString());
            Integer realMaxSize = Integer.parseInt(this.findValue(this.getMaxSize()).toString());
            ValueStack stack = this.getStack();
            AdminPagerTagHelper helper = this.getPagerHelper();
            IPagerVO pagerVo = helper.getPagerVO(Arrays.asList(new String[realTotal]),
                    realPagerId.toString(), realMaxSize, true, this.getOffset(), request);
            stack.getContext().put(this.getObjectName(), pagerVo);
            stack.setValue("#attr['" + this.getObjectName() + "']", pagerVo, false);
        } catch (Exception e) {
            logger.error("Error creating the pager", e);
            throw new JspException("Error creating the pager", e);
        }
        return EVAL_BODY_INCLUDE;
    }
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

}
