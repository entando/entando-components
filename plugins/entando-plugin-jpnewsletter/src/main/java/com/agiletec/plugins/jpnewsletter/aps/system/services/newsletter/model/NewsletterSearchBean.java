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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model;

/**
 * @author E.Santoboni
 */
public class NewsletterSearchBean {
	
	public Boolean getInQueue() {
		return _inQueue;
	}
	public void setInQueue(Boolean inQueue) {
		this._inQueue = inQueue;
	}
	
	public Boolean getSent() {
		return _sent;
	}
	public void setSent(Boolean sent) {
		this._sent = sent;
	}
	
	private Boolean _inQueue;
	private Boolean _sent;
	
}