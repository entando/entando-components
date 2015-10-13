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
package org.entando.entando.plugins.jprss.aps.system.services.rss;

/**
 * This object holds the configuration for mapping a feed with a content type
 * @author S.Puddu
 */
public class RssContentMapping {
	
	@Override
	public RssContentMapping clone() {
		RssContentMapping clone = new RssContentMapping();
		clone.setContentType(this.getContentType());
		clone.setDescriptionAttributeName(this.getDescriptionAttributeName());
		clone.setTitleAttributeName(this.getTitleAttributeName());
		return clone;
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public String getTitleAttributeName() {
		return _titleAttributeName;
	}
	public void setTitleAttributeName(String titleAttributeName) {
		this._titleAttributeName = titleAttributeName;
	}
	
	public String getDescriptionAttributeName() {
		return _descriptionAttributeName;
	}
	public void setDescriptionAttributeName(String descriptionAttributeName) {
		this._descriptionAttributeName = descriptionAttributeName;
	}
	
	private String _contentType;
	private String _titleAttributeName;
	private String _descriptionAttributeName;
	
}