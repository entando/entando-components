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
package com.agiletec.plugins.jpcontentfeedback.aps.system;

/**
 * This interceptor can make sure that back buttons and double clicks don't cause un-intended side affects.
 * For example, you can use this to prevent careless users who might double click on a "checkout" button at an online store.
 * This interceptor uses a fairly primitive technique for when an invalid token is found: it returns the result invalid.token.
 *
 * TypeMessages parameter to specify the type of message to associate at invalid.token result
 * The values of type are the following:
 * 	  error: return an action error message
 * 	  message: return an action message
 * 	  none: don't return message
 *
 * @author D.Cherchi
 * @deprecated use com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.system.TokenInterceptor
 */
public class TokenInterceptor extends com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.system.TokenInterceptor {
	
}
