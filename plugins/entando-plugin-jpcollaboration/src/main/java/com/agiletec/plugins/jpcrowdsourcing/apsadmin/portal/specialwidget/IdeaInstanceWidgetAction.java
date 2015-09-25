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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.portal.specialwidget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;

public class IdeaInstanceWidgetAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaInstanceWidgetAction.class);

	public String configInstance() {
		try {
			Widget showlet = super.createNewShowlet();
			showlet.getConfig().setProperty(IdeaInstanceWidgetAction.WIDGET_PARAM_IDEA_INSTANCE, this.getInstanceCode());
			this.setShowlet(showlet);
		} catch (Throwable t) {
			_logger.error("error in configInstance", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String changeInstance() {
		try {
			Widget showlet = super.createNewShowlet();
			this.setShowlet(showlet);
		} catch (Throwable t) {
			_logger.error("error in changeInstance", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public List<IdeaInstance> getIdeaInstances() {
		List<IdeaInstance> ideaInstances = new ArrayList<IdeaInstance>();
		try {
			IPage page = this.getPage(this.getPageCode());
			Set<String> pageGroups = new HashSet<String>();
			pageGroups.add(page.getGroup());
			if (null != page.getExtraGroups() && !page.getExtraGroups().isEmpty()) {
				pageGroups.addAll(page.getExtraGroups());
			}
			if (!pageGroups.contains(Group.FREE_GROUP_NAME)) pageGroups.add(Group.FREE_GROUP_NAME);
			List<String> codes = this.getIdeaInstanceManager().getIdeaInstances(pageGroups, null);
			if (null != codes && !codes.isEmpty()) {
				Iterator<String> it = codes.iterator();
				while (it.hasNext()) {
					String code = it.next();
					IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(code);
					if (null != ideaInstance) {
						ideaInstances.add(ideaInstance);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getIdeaInstances for page {}", this.getPageCode(), t);
			throw new RuntimeException("error in getIdeaInstances for page " + this.getPageCode());
		}
		return ideaInstances;
	}


	public String getInstanceCode() {
		return _instanceCode;
	}
	public void setInstanceCode(String instanceCode) {
		this._instanceCode = instanceCode;
	}

	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}
	private IIdeaInstanceManager _ideaInstanceManager;

	private String _instanceCode;

	public static final String WIDGET_PARAM_IDEA_INSTANCE = "instanceCode";
	public static final String WIDGET_CODE = "jpcollaboration_ideaInstance";

	/**
	 * @deprecated Use {@link #WIDGET_CODE} instead
	 */
	public static final String SHOWLET_CODE= WIDGET_CODE;

	/**
	 * @deprecated Use {@link #WIDGET_PARAM_IDEA_INSTANCE} instead
	 */
	public static final String SHOWLET_PARAM_IDEA_INSTANCE = WIDGET_PARAM_IDEA_INSTANCE;
}
