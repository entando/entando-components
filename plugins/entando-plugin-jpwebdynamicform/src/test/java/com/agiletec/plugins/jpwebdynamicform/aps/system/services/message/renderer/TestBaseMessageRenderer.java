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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.renderer;

import java.util.Date;

import com.agiletec.plugins.jpwebdynamicform.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwebdynamicform.util.JpwebdynamicformTestHelper;

import com.agiletec.aps.system.common.renderer.IEntityRenderer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

public class TestBaseMessageRenderer extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testGetRenderedEntity() {
		Message message = this._helper.createMessage("1", "admin", "it", new Date(), "Company", "Address", JpwebdynamicformTestHelper.EMAIL, "Note");
		String model = "#if($message.Company.text != \"\") $message.Address.text $i18n.getLabel(\"PAGE_MODEL\") #end";
		
		String rendereEntity = this._entityRenderer.render(message, model, "en", false);
		assertEquals(" Address page model ", rendereEntity);
	}
    
	private void init() throws Exception {
		try {
			this._entityRenderer = (IEntityRenderer) this.getService(JpwebdynamicformSystemConstants.BASE_ENTITY_RENDERER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	protected IEntityRenderer _entityRenderer;
	
}