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
package com.agiletec.plugins.jpfastcontentedit.apsadmin.portal.specialwidget.fastcontentedit;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;

import org.slf4j.Logger;

public class FastContentEditWidgetConfigAction extends SimpleWidgetConfigAction {

	@Override
	public void validate() {
		super.validate();
		if (null == this.getFieldErrors().get("contentType")) {
			if (null == this.getContentManager().createContentType(this.getContentType())) {
				//TODO DA RIVEDERE LABEL
				this.addFieldError("contentType", this.getText("INVALID_CONTENT_TYPE", new String[]{this.getContentType()}));
			}
		}
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			try {
				Widget widget = super.createNewShowlet();
				this.setShowlet(widget);
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}
	}

	@Override
	public String save() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			log.trace("Salvataggio showlet - code = " + this.getWidget().getType().getCode() +
					", pageCode = " + this.getPageCode() + ", frame = " + this.getFrame());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return "configure";
	}

	/**
	 * Restituisce la lista di contenuti (in forma small) definiti nel sistema.
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire
	 * una corretta visualizzazione della pagina.
	 * @return La lista di tipi di contenuto (in forma small) definiti nel sistema.
	 */
	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}

	public List<AttributeInterface> getSelectableAttributes() {
		String typeCode = this.getWidget().getConfig().getProperty("typeCode");
		List<AttributeInterface> attributesForSelect = new ArrayList<AttributeInterface>();
		if (null != typeCode && typeCode.length() > 0) {
			IApsEntity  contentType =  getContentManager().getEntityPrototype(typeCode);
			List<AttributeInterface> attributes =  contentType.getAttributeList();
			for (AttributeInterface attribute : attributes) {
				if (attribute.isTextAttribute()) {
					attributesForSelect.add(attribute);
				}
			}
		}
		return attributesForSelect;
	}

	public String configContentType() {
		try {
			Widget widget = super.createNewShowlet();
			widget.getConfig().setProperty("typeCode", this.getContentType());
			this.setShowlet(widget);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "init");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String changeContentType() {
		try {
			Widget widget = super.createNewShowlet();
			this.setShowlet(widget);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "changeContentType");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	public IContentManager getContentManager() {
		return _contentManager;
	}

	public void setAuthorAttribute(String authorAttribute) {
		this._authorAttribute = authorAttribute;
	}
	public String getAuthorAttribute() {
		return _authorAttribute;
	}

	private String _contentType;
	private IContentManager _contentManager;
	private String _authorAttribute;

}
