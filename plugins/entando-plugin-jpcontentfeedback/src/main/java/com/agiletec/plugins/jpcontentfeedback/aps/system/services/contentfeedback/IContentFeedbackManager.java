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
