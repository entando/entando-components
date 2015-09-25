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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;


@XmlRootElement(name = "idea")
@XmlType(propOrder = {"id", "title", "descr", "pubDate", "username", "votePositive", "voteNegative", "status", "instanceCode", "tags", "commentsToApprove", "commentsNotApproved","commentsApproved"}) //"comments"
public class JAXBIdea {

    public JAXBIdea() {
        super();
    }

    public JAXBIdea(IIdea idea) {
		this.setId(idea.getId());
		this.setTitle(idea.getTitle());
		this.setDescr(idea.getDescr());
		this.setPubDate(idea.getPubDate());
		this.setUsername(idea.getUsername());
		this.setVotePositive(idea.getVotePositive());
		this.setVoteNegative(idea.getVoteNegative());
		this.setStatus(idea.getStatus());
		this.setInstanceCode(idea.getInstanceCode());
		this.setTags(idea.getTags());
		this.setCommentsApproved(idea.getCommentsApproved());
		this.setCommentsToApprove(idea.getCommentsToApprove());
		this.setCommentsNotApproved(idea.getCommentsNotApproved());
    }
    
    public Idea getIdea() {
    	Idea idea = new Idea();
		idea.setId(this.getId());
		idea.setTitle(this.getTitle());
		idea.setDescr(this.getDescr());
		idea.setPubDate(this.getPubDate());
		idea.setUsername(this.getUsername());
		idea.setVotePositive(this.getVotePositive());
		idea.setVoteNegative(this.getVoteNegative());
		idea.setStatus(this.getStatus());
		idea.setInstanceCode(this.getInstanceCode());
		idea.setTags(this.getTags());
//		idea.setComments(this.getComments());
    	return idea;
    }

	/*
	@XmlElement(name = "code", required = true)
	public String getCode() {
		return _code;
	}
*/

	@XmlElement(name = "id")
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}

	@XmlElement(name = "title", required = true)
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}

	@XmlElement(name = "descr", required = true)
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}

	@XmlElement(name = "pubDate")
	public Date getPubDate() {
		return _pubDate;
	}
	public void setPubDate(Date pubDate) {
		this._pubDate = pubDate;
	}

	@XmlElement(name = "username", required=true)
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	@XmlElement(name = "status", required = true)
	public int getStatus() {
		return _status;
	}
	public void setStatus(int status) {
		this._status = status;
	}

	@XmlElementWrapper(name="tags", required = true )
	@XmlElement (name="tag" )
	public List<String> getTags() {
		return _tags;
	}
	public void setTags(List<String> tags) {
		this._tags = tags;
	}

	@XmlElement(name = "votePositive")
	public int getVotePositive() {
		return _votePositive;
	}
	public void setVotePositive(int votePositive) {
		this._votePositive = votePositive;
	}

	@XmlElement(name = "voteNegative")
	public int getVoteNegative() {
		return _voteNegative;
	}
	public void setVoteNegative(int voteNegative) {
		this._voteNegative = voteNegative;
	}

	@XmlElement(name = "instanceCode", required = true)
	public String getInstanceCode() {
		return _instanceCode;
	}
	public void setInstanceCode(String instanceCode) {
		this._instanceCode = instanceCode;
	}

	@XmlElementWrapper(name="commentsToApprove")
	@XmlElement(name="commentid")
	public List<Integer> getCommentsToApprove() {
		return _commentsToApprove;
	}
	public void setCommentsToApprove(List<Integer> commentsToApprove) {
		this._commentsToApprove = commentsToApprove;
	}
	
	@XmlElementWrapper(name="commentsNotApproved")
	@XmlElement(name="commentid")
	public List<Integer> getCommentsNotApproved() {
		return _commentsNotApproved;
	}
	public void setCommentsNotApproved(List<Integer> commentsNotApproved) {
		this._commentsNotApproved = commentsNotApproved;
	}

	@XmlElementWrapper(name="commentsApproved")
	@XmlElement(name="commentid")
	public List<Integer> getCommentsApproved() {
		return _commentsApproved;
	}
	public void setCommentsApproved(List<Integer> commentsApproved) {
		this._commentsApproved = commentsApproved;
	}

	private String _id;
	private String _title;
	private String _descr;
	private Date _pubDate;
	private String _username;
	private int _votePositive;
	private int _voteNegative;
	private int _status;
	private String _instanceCode;
	
	private List<String> _tags = new ArrayList<String>();

	private List<Integer> _commentsToApprove;
	private List<Integer> _commentsNotApproved;
	private List<Integer> _commentsApproved;
}


