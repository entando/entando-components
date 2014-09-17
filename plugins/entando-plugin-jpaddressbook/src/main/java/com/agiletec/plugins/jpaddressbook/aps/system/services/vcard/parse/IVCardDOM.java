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
package com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.parse;

import java.util.List;

import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.model.VCardContactField;


/**
 * @author A.Cocco
 */
public interface IVCardDOM {

	/**
	 * Build vcard-mapping document.
	 * @param vcards
	 */
	public void buildVcardMappingDOM(List<VCardContactField> vcards) ;

	/**
	 * Returns a VCard list
	 * @return vcard list
	 */
	public List<VCardContactField> parseVcardMapping();

	/**
	 * Read vcard mapping
	 * @param vcard mapping
	 */
	public void readVcardMapping(String vcardMapping);

	/**
	 * Returns vcard mapping in XML format
	 * @return vcard mapping in XML format
	 */	
	public String getXMLDocument();

}
