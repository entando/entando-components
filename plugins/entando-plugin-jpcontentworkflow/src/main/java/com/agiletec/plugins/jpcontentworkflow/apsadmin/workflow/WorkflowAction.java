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