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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model;

import java.util.Date;

/**
 *
 * @author D.Cherchi
 *
 */
public class CommentSearchBean implements ICommentSearchBean {

	@Override
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	@Override
	public Date getCreationFROMDate() {
		return _creationFROMDate;
	}
	public void setCreationFROMDate(Date creationFROMDate) {
		this._creationFROMDate = creationFROMDate;
	}

	@Override
	public String getComment() {
		return _comment;
	}
	public void setComment(String comment) {
		this._comment = comment;
	}

	@Override
	public int getStatus() {
		return _status;
	}
	public void setStatus(int status) {
		this._status = status;
	}

	@Override
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public void setCreationTODate(Date creationTODate) {
		this._creationTODate = creationTODate;
	}

	@Override
	public Date getCreationTODate() {
		return _creationTODate;
	}

	@Override
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String _contentId;
	public Date _creationFROMDate;
	private Date _creationTODate;
	public String _comment;
	public int _status = 0;
	private String _username;
	private String sort;

}