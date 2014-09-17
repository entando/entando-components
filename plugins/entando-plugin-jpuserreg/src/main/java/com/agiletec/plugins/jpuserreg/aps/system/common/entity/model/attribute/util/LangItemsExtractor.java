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
package com.agiletec.plugins.jpuserreg.aps.system.common.entity.model.attribute.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.attribute.util.EnumeratorAttributeItemsExtractor;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

public class LangItemsExtractor implements EnumeratorAttributeItemsExtractor {

	@Override
	public List<String> getItems() {
		List<Lang> langs = this.getLangManager().getLangs();
		List<String> langItems = new ArrayList<String>(langs.size());
		Iterator<Lang> langsIter = langs.iterator();
		while (langsIter.hasNext()) {
			String langCode = langsIter.next().getCode();
			langItems.add(langCode);
		}
		return langItems;
	}

	public ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	private ILangManager _langManager;

}