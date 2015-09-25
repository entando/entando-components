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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.activation;

/**
 * Interface for Struts Action to manage account activation
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 * */
public interface IUserActivationAction {
	
	/**
	 * Initialize the funtionality and redirect user to portal homepage if he has 
	 * activated already, or to an error page if token is consumed or wrong
	 * @return The action result.
	 */
	public String initActivation();
	
	/**
	 * Active account with information provided, if token is valid.
	 * Load also default roles and groups defined in the config.
	 * @return The action result.
	 */
	public String activate();
	
	/**
	 * Reactive account with information provided, if token is valid.
	 * @return The action result.
	 */
	public String reactivate();

	/**
	 * Initialize the funtionality and redirect user to portal homepage if he has 
	 * reactivated already, or to an error page if token is consumed or wrong
	 * @return The action result.
	 */
	public String initReactivation();
	
}