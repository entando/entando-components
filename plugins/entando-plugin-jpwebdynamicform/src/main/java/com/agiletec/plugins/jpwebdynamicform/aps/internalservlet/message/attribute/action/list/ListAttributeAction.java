/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message.attribute.action.list;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.IEntityActionHelper;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.common.INewMessageAction;


/**
 * Action class delegate to manage operations on List Attributes.<br />
 * This is an implementation for Message entities.
 * @author E.Santoboni, E.Mezzano
 */
public class ListAttributeAction extends com.agiletec.apsadmin.system.entity.attribute.action.list.ListAttributeAction {

	@Override
	protected IApsEntity getCurrentApsEntity() {
		Message message = this.updateMessageOnSession();
		return message;
	}

	/**
	 * Update the Message entity with the field parameters and instert it into the session.
	 * @return The updated message.
	 */
	protected Message updateMessageOnSession() {
		Message message = (Message) this.getRequest().getSession().getAttribute(INewMessageAction.SESSION_PARAM_NAME_CURRENT_MESSAGE + this.getTypeCode());
		if (null != message) {
			this.getEntityActionHelper().updateEntity(message, this.getRequest());
		}
		return message;
	}

	/**
	 * Extract the typeCode from the current showlet.
	 * @return The type code extracted from the showlet.
	 */
	protected String extractTypeCode() {
		String typeCode = null;
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		if (reqCtx!=null) {
			Widget showlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (showlet!=null) {
				ApsProperties config = showlet.getConfig();
				if (null!=config) {
					String showletTypeCode = config.getProperty(JpwebdynamicformSystemConstants.TYPECODE_SHOWLET_PARAM);
					if (showletTypeCode!=null && showletTypeCode.trim().length()>0) {
						typeCode = showletTypeCode.trim();
					}
				}
			}
		}
		return typeCode;
	}

	/**
	 * Returns the message type search filter.
	 * @return The message type search filter.
	 */
	public String getTypeCode() {
		if (null==this._typeCode) {
			this._typeCode = this.extractTypeCode();
		}
		return _typeCode;
	}
	/**
	 * Sets the message type search filter. If the current showlet contains a typeCode parameter gives it priority.
	 * @param typeCode The message type search filter.
	 */
	public void setTypeCode(String typeCode) {
		String showletTypeCode = this.extractTypeCode();
		this._typeCode = (null==showletTypeCode) ? typeCode : showletTypeCode;
	}

	protected IEntityActionHelper getEntityActionHelper() {
		return _entityActionHelper;
	}
	public void setEntityActionHelper(IEntityActionHelper entityActionHelper) {
		this._entityActionHelper = entityActionHelper;
	}

	private String _typeCode;
	private IEntityActionHelper _entityActionHelper;

}