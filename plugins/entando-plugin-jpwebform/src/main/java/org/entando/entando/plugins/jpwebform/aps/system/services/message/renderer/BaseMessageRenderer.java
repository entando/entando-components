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
package org.entando.entando.plugins.jpwebform.aps.system.services.message.renderer;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.renderer.BaseEntityRenderer;
import com.agiletec.aps.system.common.renderer.EntityWrapper;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;

/**
 * Implementation for Entity Renderer Service
 * Provide the rendering functionalities for generic entities.
 * @author E.Mezzano - M.Diana - W.Ambu - E.Santoboni
 */
public class BaseMessageRenderer extends BaseEntityRenderer {
	
	@Override
	protected EntityWrapper getEntityWrapper(IApsEntity entity) {
		return new EntityWrapper((Message) entity);
	}
	
	@Override
	protected String getEntityWrapperContextName() {
		return "message";
	}
	
}