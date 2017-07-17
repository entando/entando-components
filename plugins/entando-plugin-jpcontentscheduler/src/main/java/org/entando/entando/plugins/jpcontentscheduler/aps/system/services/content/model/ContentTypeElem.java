/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model;

import java.io.Serializable;
import java.util.List;

public class ContentTypeElem implements Serializable {

	private static final long serialVersionUID = 6825571071130570645L;

	public ContentTypeElem() {

	}

	public ContentTypeElem(String contentType, String startDateAttr, String endDateAttro, String idContentReplace, String suspend, List<String> idsCategories,
			String modelIdContentReplace) {
		this.contentType = contentType;
		this.startDateAttr = startDateAttr;
		this.endDateAttro = endDateAttro;
		this.idContentReplace = idContentReplace;
		this.suspend = suspend;
		this.idsCategories = idsCategories;
		this.modelIdContentReplace = modelIdContentReplace;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getStartDateAttr() {
		return startDateAttr;
	}

	public void setStartDateAttr(String startDateAttr) {
		this.startDateAttr = startDateAttr;
	}

	public String getEndDateAttro() {
		return endDateAttro;
	}

	public void setEndDateAttro(String endDateAttro) {
		this.endDateAttro = endDateAttro;
	}

	public String getIdContentReplace() {
		return idContentReplace;
	}

	public void setIdContentReplace(String idContentReplace) {
		this.idContentReplace = idContentReplace;
	}

	public String getSuspend() {
		return suspend;
	}

	public void setSuspend(String suspend) {
		this.suspend = suspend;
	}

	public List<String> getIdsCategories() {
		return idsCategories;
	}

	public void setIdsCategories(List<String> idsCategories) {
		this.idsCategories = idsCategories;
	}

	public String getModelIdContentReplace() {
		return modelIdContentReplace;
	}

	public void setModelIdContentReplace(String modelIdContentReplace) {
		this.modelIdContentReplace = modelIdContentReplace;
	}

	private String contentType;
	private String startDateAttr;
	private String endDateAttro;
	private String idContentReplace;
	private String modelIdContentReplace;
	private String suspend;
	private List<String> idsCategories;

}
