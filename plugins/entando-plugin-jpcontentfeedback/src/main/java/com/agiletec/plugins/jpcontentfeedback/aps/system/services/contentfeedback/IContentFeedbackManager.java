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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IContentFeedbackManager {

	public IContentFeedbackConfig getConfig();

	/**
	 * When true, contents can be commented
	 */
	public abstract boolean isCommentActive();

	/**
	 * If contents can be commented, when true, also guest users can add comment
	 * @return
	 */
	public abstract boolean allowAnonymousComment();

	/**
	 * when <b>true</b> comments should pass through a back office validation before get online
	 * @return
	 */
	public abstract boolean isCommentModerationActive();
	
	
	/**
	 * when <b>true</b> contents can be rated
	 * @return
	 */
	public abstract boolean isRateContentActive();


	/**
	 * when <b>true</b> comments on contents can be rated
	 * @return
	 */
	public abstract boolean isRateCommentActive();


	public void updateConfig(IContentFeedbackConfig config) throws ApsSystemException;

}
