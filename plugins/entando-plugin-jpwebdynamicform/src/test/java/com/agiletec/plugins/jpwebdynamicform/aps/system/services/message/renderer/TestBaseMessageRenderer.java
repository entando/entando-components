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