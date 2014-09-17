/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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