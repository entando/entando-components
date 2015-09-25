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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance;

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IIdeaInstanceManager {

	public IdeaInstance getIdeaInstance(String code) throws ApsSystemException;
	
	/**
	 * 
	 * @param code
	 * @param status optional. collection of status to filter the idea results
	 * @return the idea instance.
	 * @throws ApsSystemException
	 */
	public IdeaInstance getIdeaInstance(String code, Collection<Integer> status) throws ApsSystemException;

	public List<String> getIdeaInstances() throws ApsSystemException;

	/**
	 *
	 * @param groupNames OR
	 * @param code can be null, like search
	 * @return
	 * @throws ApsSystemException
	 */
	public List<String> getIdeaInstances(Collection<String> groupNames, String code) throws ApsSystemException;

	public void addIdeaInstance(IdeaInstance ideainstance) throws ApsSystemException;

	public void updateIdeaInstance(IdeaInstance ideainstance) throws ApsSystemException;

	public void deleteIdeaInstance(String code) throws ApsSystemException;


}