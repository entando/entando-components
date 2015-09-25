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

/**
 * @author E.Santoboni
 */
public class WorkflowAction extends AbstractWorkflowAction implements IWorkflowAction {
	
	@Override
	public void validate() {
		super.validate();
		this.checkWorkflow();
	}
	
	protected void checkWorkflow() {
		String roleName = this.getRole();
		if (roleName!=null && roleName.length()>0) {
			if (this.getRoleManager().getRole(roleName)==null) {
				String[] args = { roleName };
				this.addFieldError("descr", this.getText("message.save.unknowRole", args));
			}
		}
	}
	
	@Override
	public String edit() {
		try {
			String typeCode = this.getTypeCode();
			String role = this.getWorkflowManager().getRole(typeCode);
			this.setRole(role);
		} catch(Exception e) {
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			String typeCode = this.getTypeCode();
			String role = this.getRole();
			this.getWorkflowManager().updateRole(typeCode, role);
		} catch(Exception e) {
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public SmallContentType getContentType() {
		if (this._contentType==null) {
			String typeCode = this.getTypeCode();
			this._contentType = (SmallContentType) this.getContentManager().getSmallContentTypesMap().get(typeCode);
		}
		return this._contentType;
	}
	
	public List<Role> getRoles() {
		return this.getRoleManager().getRoles();
	}
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	public String getRole() {
		return _role;
	}
	public void setRole(String role) {
		this._role = role;
	}
	
	private String _typeCode;
	private String _role;
	
	private SmallContentType _contentType;
	
}