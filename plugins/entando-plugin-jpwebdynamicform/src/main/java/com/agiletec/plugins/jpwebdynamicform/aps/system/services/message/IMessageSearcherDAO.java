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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message;

import java.util.List;

import com.agiletec.aps.system.common.entity.IEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interface for Data Access Object delegated for the Message searching operations.
 * @author E.Mezzano
 */
public interface IMessageSearcherDAO extends IEntitySearcherDAO {
	
	/**
	 * Searches the message identifiers according with the given filters and according to the answered flag.
	 * @param filters The entity search filters.
	 * @param answered If true filters only the answered messages, otherwise only the not answered messages.
	 * @return The messages matching the given filters.
	 * @throws ApsSystemException
	 */
	public List<String> searchId(EntitySearchFilter[] filters, boolean answered) throws ApsSystemException;
	
	@Deprecated
	public static final String USERNAME_FILTER_KEY = IMessageManager.USERNAME_FILTER_KEY;
	
	@Deprecated
	public static final String CREATION_DATE_FILTER_KEY = IMessageManager.CREATION_DATE_FILTER_KEY;
	
}