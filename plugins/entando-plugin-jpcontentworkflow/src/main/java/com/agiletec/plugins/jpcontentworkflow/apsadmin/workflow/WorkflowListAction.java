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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow;

import java.util.List;

import com.agiletec.aps.system.services.role.Role;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class WorkflowListAction extends AbstractWorkflowAction {

	private static final Logger _logger = LoggerFactory.getLogger(WorkflowListAction.class);

	public String saveRoles() {
		try {
			List<SmallContentType> types = this.getContentTypes();
			for (int i = 0; i < types.size(); i++) {
				SmallContentType smallContentType = types.get(i);
				String roleNameParam = this.getRequest().getParameter(smallContentType.getCode() + "_authority");
				String roleName = (null != this.getRoleManager().getRole(roleNameParam)) ? roleNameParam : null;
				this.getWorkflowManager().updateRole(smallContentType.getCode(), roleName);
			}
			this.addActionMessage(this.getText("jpcontentworkflow.config.updated"));
		} catch (Throwable t) {
			_logger.error("error in saveRoles", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}

	public Role getRole(String typeCode) {
		Role role = null;
		String roleName = this.getWorkflowManager().getRole(typeCode);
		if (roleName != null) {
			role = this.getRoleManager().getRole(roleName);
		}
		return role;
	}

}
