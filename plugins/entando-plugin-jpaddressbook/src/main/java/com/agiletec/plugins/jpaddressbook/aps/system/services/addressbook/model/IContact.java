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
package com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public interface IContact {
	
	public String getId();
	
	public String getOwner();
	
	public boolean isPublicContact();
	
	public IUserProfile getContactInfo();
	
	public Object getValue(String key);
	
	public Object getAttribute(String key);
	
	public List<AttributeInterface> getAttributes();
	
}
