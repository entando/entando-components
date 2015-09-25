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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Idea implements IIdea {
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	public Date getPubDate() {
		return _pubDate;
	}
	public void setPubDate(Date pubDate) {
		this._pubDate = pubDate;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public int getStatus() {
		return _status;
	}
	public void setStatus(int status) {
		this._status = status;
	}
	
	public List<String> getTags() {
		return _tags;
	}
	public void setTags(List<String> tags) {
		this._tags = tags;
	}

	public void setVotePositive(int votePositive) {
		this._votePositive = votePositive;
	}
	public int getVotePositive() {
		return _votePositive;
	}

	public void setVoteNegative(int voteNegative) {
		this._voteNegative = voteNegative;
	}
	public int getVoteNegative() {
		return _voteNegative;
	}
	
	public String getInstanceCode() {
		return _instanceCode;
	}
	public void setInstanceCode(String instanceCode) {
		this._instanceCode = instanceCode;
	}
	
	public void setComments(Map<Integer, List<Integer>> comments) {
		this._comments = comments;
	}
	public Map<Integer, List<Integer>> getComments() {
		return _comments;
	}

	@Override
	public List<Integer> getCommentsToApprove() {
		int key = IIdea.STATUS_TO_APPROVE;
		List<Integer> comments = new ArrayList<Integer>();
		if (null != this.getComments() && this.getComments().containsKey(key)) {
			comments = this.getComments().get(key);
		}
		return comments;
	}
	@Override
	public List<Integer> getCommentsNotApproved() {
		int key = IIdea.STATUS_NOT_APPROVED;
		List<Integer> comments = new ArrayList<Integer>();
		if (null != this.getComments() && this.getComments().containsKey(key)) {
			comments = this.getComments().get(key);
		}
		return comments;
	}
	
	@Override
	public List<Integer> getCommentsApproved() {
		int key = IIdea.STATUS_APPROVED;
		List<Integer> comments = new ArrayList<Integer>();
		if (null != this.getComments() && this.getComments().containsKey(key)) {
			comments = this.getComments().get(key);
		}
		return comments;
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
	private Map<Integer, List<Integer>> _comments = new HashMap<Integer, List<Integer>>();
	
}
