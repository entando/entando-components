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
package com.agiletec.plugins.jpblog.aps.system.services.blog;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "blogConfig")
public class BlogConfig implements IBlogConfig {

	public BlogConfig() {}

	public BlogConfig(String xml) throws Throwable {
		JAXBContext context = JAXBContext.newInstance(BlogConfig.class);
		BlogConfig config = (BlogConfig) context.createUnmarshaller().unmarshal(new StringReader(xml));
		List<String> categories = config.getCategories();
		if (null != categories) {
			for (int i = 0; i < categories.size(); i++) {
				String cat = categories.get(i);
				if (cat.trim().length() > 0) {
					this.addCategory(cat);
				}
			}
		}
	}

	public String toXML() throws Throwable {
		JAXBContext context = JAXBContext.newInstance(BlogConfig.class);
		StringWriter sw = new StringWriter();
		context.createMarshaller().marshal(this, sw);
		return sw.toString();
	}

	public void addCategory(String code) {
		this.getCategories().add(code);
	}

	public void removeCategory(String code) {
		this.getCategories().remove(code);
	}

	public List<String> getCategories() {
		return _categories;
	}

	public void setCategories(List<String> categories) {
		this._categories = categories;
	}
	private List<String> _categories = new ArrayList<String>();


}
