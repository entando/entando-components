/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.system.services.checkin;

import java.util.Date;

import org.entando.entando.plugins.jpsharedocs.aps.system.JpSharedocsSystemConstants;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class ContentCheckinManager extends AbstractService implements IContentCheckinManager, PublicContentChangedObserver {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().info(this.getClass().getName() + " ready");
	}
	
	@Override
	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
		try {
			int operationCode = event.getOperationCode();
			Content content = event.getContent();
			if (operationCode == PublicContentChangedEvent.REMOVE_OPERATION_CODE) {
				this.deleteContentCheckin(content.getId());
			} else if (operationCode == PublicContentChangedEvent.INSERT_OPERATION_CODE 
					|| operationCode == PublicContentChangedEvent.UPDATE_OPERATION_CODE) {
				//SOLO SE IL CONTENUTO HA L'ATTRIBUTO ed è true
				AttributeInterface checkinAttribute = content.getAttributeByRole(JpSharedocsSystemConstants.ATTRIBUTE_ROLE_CHECK_IN);
				//è true solo se il contenuto ha l'attributo
				//l'attributo è boolean
				//ed è true
				Boolean checkinValue = null;
				if (null != checkinAttribute) {
					boolean isBoolAttr = checkinAttribute instanceof BooleanAttribute;
					if (isBoolAttr) {
						checkinValue = ((BooleanAttribute)checkinAttribute).getValue();
					}
				}
				if (null != checkinValue && checkinValue == true) {
					ContentCheckin checkin = this.getContentCheckin(content.getId());
					if (null == checkin) {
						checkin = new ContentCheckin();
						checkin.setContentId(content.getId());
						checkin.setCheckinDate(new Date());
						String lastEditor = content.getLastEditor();
						if (null == lastEditor) lastEditor = "NULL";
						checkin.setCheckinUser(lastEditor);
						this.getContentCheckinDAO().insertContentChekin(checkin);
					} else {
						//IL DOC è gia in stato checkin... nulla da fare qui
					}
				} else {
					//LO RIMUOVO
					this.deleteContentCheckin(content.getId());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateFromPublicContentChanged", 	"Error notifing event " + PublicContentChangedEvent.class.getName());
		}
	}
	
	public void deleteContentCheckin(String contentId)	throws ApsSystemException {
		try {
			this.getContentCheckinDAO().deleteContentCheckin(contentId);
		} catch (Throwable t) {
			String msg = "Error deleting the contentCheckin record for content " + contentId;
			ApsSystemUtils.logThrowable(t, this, "deleteContentCheckin", msg);
			throw new ApsSystemException(msg, t);
		}
	}
	
	@Override
	public ContentCheckin getContentCheckin(String contentId) throws ApsSystemException {
		ContentCheckin checkin = null;
		try {
			checkin = this.getContentCheckinDAO().loadContentChekin(contentId);
		} catch (Throwable t) {
			String msg = "Error loading the contentCheckin record for content " + contentId;
			ApsSystemUtils.logThrowable(t, this, "getContentCheckin", msg);
			throw new ApsSystemException(msg, t);
		}
		return checkin;
	}
	
	protected IContentCheckinDAO getContentCheckinDAO() {
		return _contentCheckinDAO;
	}
	public void setContentCheckinDAO(IContentCheckinDAO contentCheckinDAO) {
		this._contentCheckinDAO = contentCheckinDAO;
	}
	
	private IContentCheckinDAO _contentCheckinDAO;
	
}