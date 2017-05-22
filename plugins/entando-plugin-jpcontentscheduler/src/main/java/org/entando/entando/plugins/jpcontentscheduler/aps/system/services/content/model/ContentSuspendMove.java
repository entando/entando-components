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

import java.util.List;

public class ContentSuspendMove {

	private String _suspend;
	private List<String> _idsContents;
	private String _typeContent;
	private String _globalCat;
	private List<String> catType;
	private String fieldNameContentReplace;
	private String fieldNameModelContentReplace;
	private String contentIdReplace;
	private String contentModelReplace;

	public ContentSuspendMove() {
	}

	public ContentSuspendMove(String suspend, List<String> idsContents, String typeContent, String globalCat,
			List<String> catType, String fieldNameContentReplace, String fieldNameModelContentReplace) {
		this._suspend = suspend;
		this._idsContents = idsContents;
		this._typeContent = typeContent;
		this._globalCat = globalCat;
		this.catType = catType;
		this.fieldNameContentReplace = fieldNameContentReplace;
		this.fieldNameModelContentReplace = fieldNameModelContentReplace;
	}

	public String getSuspend() {
		return _suspend;
	}

	public void setSuspend(String suspend) {
		this._suspend = suspend;
	}

	public List<String> getIdsContents() {
		return _idsContents;
	}

	public void setIdsContents(List<String> idsContents) {
		this._idsContents = idsContents;
	}

	public String getTypeContent() {
		return _typeContent;
	}

	public void setTypeContent(String typeContent) {
		this._typeContent = typeContent;
	}

	public String getGlobalCat() {
		return _globalCat;
	}

	public void setGlobalCat(String globalCat) {
		this._globalCat = globalCat;
	}

	public List<String> getCatType() {
		return catType;
	}

	public void setCatType(List<String> catType) {
		this.catType = catType;
	}

	public String getFieldNameContentReplace() {
		return fieldNameContentReplace;
	}

	public void setFieldNameContentReplace(String fieldNameContentReplace) {
		this.fieldNameContentReplace = fieldNameContentReplace;
	}

	public String getContentIdReplace() {
		return contentIdReplace;
	}

	public void setContentIdReplace(String contentIdReplace) {
		this.contentIdReplace = contentIdReplace;
	}

	public String getContentModelReplace() {
		return contentModelReplace;
	}

	public void setContentModelReplace(String contentModelReplace) {
		this.contentModelReplace = contentModelReplace;
	}

	public String getFieldNameModelContentReplace() {
		return fieldNameModelContentReplace;
	}

	public void setFieldNameModelContentReplace(String fieldNameModelContentReplace) {
		this.fieldNameModelContentReplace = fieldNameModelContentReplace;
	}

}
