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

public interface IContentFeedbackConfig {

    /**
     * when <b>true</b> contents can be commented
     */
	public abstract String getComment();

    /**
     * when <b>true</b> contents can be commented
     */
	public abstract void setComment(String comment);

    /**
     * when <b>true</b>, and only when the param <i>comment</i> is <i>true</i>, contents contents can be commented by guest users
     */
	public abstract String getAnonymousComment();
	
    /**
     * when <b>true</b>, and only when the param <i>comment</i> is <i>true</i>, contents contents can be commented by guest users
     */
	public abstract void setAnonymousComment(String anonymousComment);
	

    /**
     * when <b>true</b> comments should pass through a back office validation before get online
     */
    public String getModeratedComment();
	
    /**
     * when <b>true</b> comments should pass through a back office validation before get online
     */
    public void setModeratedComment(String moderatedComment);	
	
	/**
	 * when <b>true</b> contents can be rated
	 */
	public abstract String getRateContent();

	/**
	 * when <b>true</b> contents can be rated
	 */
	public abstract void setRateContent(String rateContent);
	
	/**
	 * when <b>true</b> comments can be rated
	 */
	public abstract String getRateComment();
	
	/**
	 * when <b>true</b> comments can be rated
	 */
	public abstract void setRateComment(String rateComment);


	public String toXML() throws Throwable;

}