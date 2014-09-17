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
package com.agiletec.plugins.jpcontentrefs.aps.system.services.category;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations.AbstractContentRelactionDOM;

/**
 * Classe JDOM per la lettura e scrittura 
 * dei tipi contenuto compatibili con la categoria considerata in xml.
<contenttypes>
	<contenttype typecode="MEN">
		<category code="cat1"/>
		<category code="cat2"/>
	</contenttype>
	<contenttype typecode="EVN">
		<category code="cat1"/>
	</contenttype>
</contenttypes>
 * @author E.Santoboni
 */
public class ContentCategoryRefDOM extends AbstractContentRelactionDOM {
	
	public ContentCategoryRefDOM() throws ApsSystemException {
		super();
	}
	
	public ContentCategoryRefDOM(String xml) throws ApsSystemException {
		super(xml);
	}
	
	@Override
	protected String getReferenceElementName() {
		return "category";
	}
	
}