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
package com.agiletec.plugins.jpcmstagcloud.aps.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcmstagcloud.aps.system.JpcmstagcloudSystemConstants;
import com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud.ITagCloudManager;
import com.agiletec.plugins.jpcmstagcloud.aps.tags.util.CloudInfoBean;

/**
 * @author E.Santoboni
 */
public class TagCloudBuilderTag extends TagSupport {
	
	private static final Logger _logger =  LoggerFactory.getLogger(TagCloudBuilderTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ITagCloudManager tagCloudManager = (ITagCloudManager) ApsWebApplicationUtils.getBean(JpcmstagcloudSystemConstants.TAG_CLOUD_MANAGER, this.pageContext);
		try {
			UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			Map<ITreeNode, Integer> occurrences = tagCloudManager.getCloudInfos(currentUser);
			if (null != this.getOccurrencesVar()) {
				this.pageContext.setAttribute(this.getOccurrencesVar(), occurrences);
			}
			List<CloudInfoBean> cloudBeans = this.buildCloudsInfoBeans(occurrences);
			this.pageContext.setAttribute(this.getCloudBeansVar(), cloudBeans);
		} catch (Throwable t) {
			_logger.error("Error in doStartTag", t);
			throw new JspException("Error in doStartTag", t);
		}
		return super.doStartTag();
	}
	
	private List<CloudInfoBean> buildCloudsInfoBeans(Map<ITreeNode, Integer> occurrences) throws Throwable {
		List<CloudInfoBean> beans = new ArrayList<CloudInfoBean>();
		try {
			ServletRequest request = this.pageContext.getRequest();
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			int[] minMax = this.extractMinMax(occurrences);
			int interval = (minMax[1] - minMax[0]);
			double delta = ((double)interval)/9d;
			Iterator<ITreeNode> iter = occurrences.keySet().iterator();
			while (iter.hasNext()) {
				ITreeNode treeNode = iter.next();
				int occurrence = occurrences.get(treeNode).intValue();
				double position = ((double) (occurrence-minMax[0]))/delta;
				long positionInt = Math.round(position);
				String classId = String.valueOf(((int)positionInt+1));
				CloudInfoBean bean = new CloudInfoBean(treeNode, occurrence, classId, currentLang);
				beans.add(bean);
			}
			BeanComparator comparator = new BeanComparator("title");
			Collections.sort(beans, comparator);
		} catch (Throwable t) {
			_logger.error("Error building Cloud info", t);
			throw new ApsSystemException("Error building Cloud info", t);
		}
		return beans;
	}

	private int[] extractMinMax(Map<ITreeNode, Integer> occurrences) {
		int[] minMax = new int[2];
		if (null == occurrences || occurrences.isEmpty()) {
			return minMax;
		}
		minMax[0] = -1;
		List<Integer> list = new ArrayList<Integer>(occurrences.values());
		for (int i=0; i<list.size(); i++) {
			int integer = list.get(i).intValue();
			if (minMax[0] > integer || minMax[0] == -1) {
				minMax[0] = integer;
			}
			if (minMax[1] < integer) {
				minMax[1] = integer;
			}
		}
		return minMax;
	}
	
	@Override
	public void release() {
		super.release();
		this.setOccurrencesVar(null);
		this.setCloudBeansVar(null);
	}
	
	public String getOccurrencesVar() {
		return _occurrencesVar;
	}
	public void setOccurrencesVar(String occurrencesVar) {
		this._occurrencesVar = occurrencesVar;
	}
	
	public String getCloudBeansVar() {
		return _cloudBeansVar;
	}
	public void setCloudBeansVar(String cloudBeansVar) {
		this._cloudBeansVar = cloudBeansVar;
	}
	
	private String _occurrencesVar;
	private String _cloudBeansVar;
	
}
