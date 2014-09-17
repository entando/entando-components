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
package com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.helper;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpfastcontentedit.aps.system.JpFastContentEditSystemConstants;

import java.util.List;

/**
 * @author E.Santoboni
 */
public class ContentActionHelper extends com.agiletec.plugins.jacms.apsadmin.content.helper.ContentActionHelper implements IContentActionHelper {

	@Override
	public void updateEntity(IApsEntity content, HttpServletRequest request) {
		super.updateEntity(content, request);
		try {
            if (null == content) return;
        	String authorAttrName = this.getAuthorAttributeName(request);
    		if (authorAttrName != null) {
    			ITextAttribute authorAttribute = (ITextAttribute) content.getAttribute(authorAttrName);
    			if (null != authorAttribute) {
    				UserDetails user =  (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
    				Lang defaultLang = super.getLangManager().getDefaultLang();
    				authorAttribute.setText(user.getUsername(), defaultLang.getCode());
    			}
    		}
        } catch (Throwable t) {
        	ApsSystemUtils.logThrowable(t, this, "updateEntity");
        	throw new RuntimeException("Errore on updateEntity", t);
        }
	}

	@Override
	@Deprecated
	public String extractAuthor(HttpServletRequest request) {
		return this.getAuthorAttributeName(request);
	}

	@Override
	public String getAuthor(Content content, HttpServletRequest request) {
		String authorAttrName = this.getAuthorAttributeName(request);
		ITextAttribute authorAttribute = (ITextAttribute) content.getAttribute(authorAttrName);
		if (null != authorAttribute && null != authorAttribute.getText()) {
			return authorAttribute.getText();
		} else {
			return content.getLastEditor();
		}
	}

	@Override
	public String getAuthorAttributeName(HttpServletRequest request) {
		String authorAttributeName = this.extractShowletParam(request, JpFastContentEditSystemConstants.AUTHOR_ATTRIBUTE_SHOWLET_PARAM_NAME);
		if (authorAttributeName == null) {
			authorAttributeName = this.getDefaultAuthor();
		}
		return authorAttributeName;
	}

	@Override
	public String extractShowletParam(HttpServletRequest request, String paramName) {
		String paramValue = null;
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		if (reqCtx!=null) {
			Widget widget = (com.agiletec.aps.system.services.page.Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (widget!=null) {
				ApsProperties config = widget.getConfig();
				if (null != config) {
					String showletParam = config.getProperty(paramName);
					if (showletParam!=null && showletParam.trim().length()>0) {
						paramValue = showletParam.trim();
					}
				}
			}
		}
		return paramValue;
	}

	@Override
	public boolean isUserAllowed(Content content, UserDetails currentUser) {
		return super.getAuthorizationManager().isAuthOnGroup(currentUser, content.getMainGroup())
			|| super.getAuthorizationManager().isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME);
	}

	@Override
	public void checkTypeLabels(Content content) {
		if (null == content) {
			return;
		}
		try {
			List<AttributeInterface> attributes = content.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				//jpfastcontentedit_${contentType}_${attributeName}
				String attributeLabelKey = "jpfastcontentedit_" + content.getTypeCode() + "_" + attribute.getName();
				if (null == this.getI18nManager().getLabelGroup(attributeLabelKey)) {
					String attributeDescription = attribute.getDescription();
					String value = (null != attributeDescription && attributeDescription.trim().length() > 0) ?
							attributeDescription :
							attribute.getName();
					this.addLabelGroups(attributeLabelKey, value);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "checkTypeLables");
			throw new RuntimeException("Error checking label types", t);
		}
	}

	protected void addLabelGroups(String key, String defaultValue) throws ApsSystemException {
		try {
			ApsProperties properties = new ApsProperties();
			Lang defaultLang = super.getLangManager().getDefaultLang();
			properties.put(defaultLang.getCode(), defaultValue);
			this.getI18nManager().addLabelGroup(key, properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addLabelGroups");
			throw new RuntimeException("Error adding label groups - key '" + key + "'", t);
		}
	}

	protected String getDefaultAuthor() {
		return _defaultAuthor;
	}
	public void setDefaultAuthor(String defaultAuthor) {
		this._defaultAuthor = defaultAuthor;
	}

	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}

	private String _defaultAuthor;

	private II18nManager _i18nManager;

}