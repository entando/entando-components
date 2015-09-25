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
package org.entando.entando.plugins.jpwebform.apsadmin.form;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.IEntityActionHelper;

import org.entando.entando.plugins.jpfileattribute.apsadmin.entity.attribute.FileAttributeAction;
import org.entando.entando.plugins.jpfileattribute.apsadmin.entity.attribute.FileAttributeActionHelper;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class WebformFileAttributeAction extends FileAttributeAction {

	private static final Logger _logger =  LoggerFactory.getLogger(WebformFileAttributeAction.class);

	public String backToEntryMessage() {
		FileAttributeActionHelper.removeSessionParams(this.getRequest().getSession());
		return SUCCESS;
	}
	
	@Override
	protected void updateEntity() throws ApsException{
		if(this.getMessage() == null){
			throw new ApsException(this.getText("error.null.message"));
		}
		this.getEntityActionHelper().updateEntity(this.getMessage(), this.getRequest());
	}
	
	@Override
	protected IApsEntity getEntity() {
		return this.getMessage();
	}
	
	/**
	 * Restituisce il messaggio in sesione.
	 * @return Il messaggio in sesione.
	 */
	public Message getMessage() {
		return (Message) this.getRequest().getSession().getAttribute(JpwebformSystemConstants.SESSION_PARAM_NAME_CURRENT_FORM + this.getTypeCode());
	}
	
	/**
	 * Extract the typeCode from the current showlet.
	 * @return The type code extracted from the showlet.
	 */
	protected String extractTypeCode() {
		String typeCode = null;
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		if (reqCtx != null) {
			Widget showlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (showlet != null) {
				ApsProperties config = showlet.getConfig();
				if (null != config) {
					String showletTypeCode = config.getProperty(JpwebformSystemConstants.WIDGET_PARAM_TYPECODE);
					if (showletTypeCode != null && showletTypeCode.trim().length() > 0) {
						typeCode = showletTypeCode.trim();
					}
				}
			}
		}
		return typeCode;
	}
	
	protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
		this._entryContentAnchorDest = entryContentAnchorDest;
	}

	public IEntityActionHelper getEntityActionHelper() {
		return _entityActionHelper;
	}

	public void setEntityActionHelper(IEntityActionHelper entityActionHelper) {
		this._entityActionHelper = entityActionHelper;
	}
	
	public String getTypeCode() {
		if (null == this._typeCode) {
			this._typeCode = this.extractTypeCode();
		}
		return _typeCode;
	}
	
	public void setTypeCode(String typeCode) {
		if(null == typeCode){
			this._typeCode = this.extractTypeCode();
		} else {
			this._typeCode = typeCode;
		}
	}

	public String getCurrentStepCode() {
		return _currentStepCode;
	}

	public void setCurrentStepCode(String currentStepCode) {
		this._currentStepCode = currentStepCode;
	}
	
	private String _entryContentAnchorDest;
	
	private IEntityActionHelper _entityActionHelper;
	
	private String _typeCode;
	
	private String _currentStepCode;
	
	
}
