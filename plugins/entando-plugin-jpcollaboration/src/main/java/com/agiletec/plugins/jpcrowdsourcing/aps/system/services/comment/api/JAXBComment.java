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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IIdeaComment;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IdeaComment;


@XmlRootElement(name = "comment")
@XmlType(propOrder = {"id", "ideaId", "creationDate", "commentText", "status", "username"})
public class JAXBComment {

    public JAXBComment() {
        super();
    }

    public JAXBComment(IIdeaComment comment) {
    	this.setId(comment.getId());
    	this.setIdeaId(comment.getIdeaId());
    	this.setCreationDate(comment.getCreationDate());
    	this.setCommentText(comment.getComment());
    	this.setStatus(comment.getStatus());
    	this.setUsername(comment.getUsername());
    }
    
    public IdeaComment getComment() {
    	IdeaComment comment = new IdeaComment();
    	comment.setId(this.getId());
    	comment.setIdeaId(this.getIdeaId());
    	comment.setCreationDate(this.getCreationDate());
    	comment.setComment(this.getCommentText());
    	comment.setStatus(this.getStatus());
    	comment.setUsername(this.getUsername());
    	return comment;
    }

	@XmlElement(name = "id")
	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}
        
	@XmlElement(name = "ideaid", required = true)
	public String getIdeaId() {
		return _ideaId;
	}
	public void setIdeaId(String ideaId) {
		this._ideaId = ideaId;
	}
	
	
	@XmlElement(name = "creationDate")
	public Date getCreationDate() {
		return _creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	@XmlElement(name = "commentText", required = true)
	public String getCommentText() {
		return _commentText;
	}
	public void setCommentText(String commentText) {
		this._commentText = commentText;
	}
	
	@XmlElement(name = "status", required=true)
	public int getStatus() {
		return _status;
	}
	public void setStatus(int status) {
		this._status = status;
	}
	
	@XmlElement(name = "username")
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	private int _id;
	private String _ideaId;
	private Date _creationDate;
	private String _commentText;
	private int _status;
	private String _username;
}


