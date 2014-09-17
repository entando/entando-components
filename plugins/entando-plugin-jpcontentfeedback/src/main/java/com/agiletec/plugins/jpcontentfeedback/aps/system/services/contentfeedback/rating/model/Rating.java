/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model;

/**
 * Oggetto rappresentante il rating.
 * @author d.cherchi
 *
 */
public class Rating implements IRating {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	public int getCommentId() {
		return _commentId;
	}
	public void setCommentId(int commentId) {
		this._commentId = commentId;
	}
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	public int getVoters() {
		return _voters;
	}
	private void setVoters(int voters) {
		this._voters = voters;
	}
	public int getSumvote() {
		return _sumvote;
	}
	private void setSumvote(int sumvote) {
		this._sumvote = sumvote;
	}

	private void setAverage(double average) {
		this._average = average;
	}
	public double getAverage() {
		return _average;
	}

	public void setVote(int voters, int sumvote){
		this.setSumvote(sumvote);
		this.setVoters(voters);
		this.setAverage((double)sumvote/voters);
	}

	private int _id;
	private int _commentId;
	private String _contentId;
	private int _voters;
	private int _sumvote;
	private double _average;

}
