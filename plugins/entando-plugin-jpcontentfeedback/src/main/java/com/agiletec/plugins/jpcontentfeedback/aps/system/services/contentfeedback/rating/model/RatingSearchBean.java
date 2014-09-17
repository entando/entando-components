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


public class RatingSearchBean implements IRatingSearchBean {

	@Override
	public int getCommentId() {
		return _commentId;
	}
	public void setCommentId(int commentId) {
		this._commentId = commentId;
	}
	@Override
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	@Override
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	private int _commentId;
	private String _contentId;
	private int _id;

}
