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