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
package org.entando.entando.plugins.jpcontentfeedback.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Comment.TABLE_NAME)
public class Comment {
	
	public Comment() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false)
	private String _contentid;
	
	@DatabaseField(columnName = "creationdate", 
			dataType = DataType.DATE)
	private Date _creationDate;
	
	@DatabaseField(columnName = "usercomment", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _userComment;
	
	@DatabaseField(columnName = "status", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _status;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _username;
	
	public static final String TABLE_NAME = "jpcontentfeedback_comments";
	
}
/*
CREATE TABLE jpcontentfeedback_comments
(
  id integer NOT NULL,
  contentid character varying(16) NOT NULL,
  creationdate timestamp without time zone,
  usercomment character varying NOT NULL,
  status integer NOT NULL,
  username character varying(40) NOT NULL,
  CONSTRAINT jpcontentfeedback_comments_pkey PRIMARY KEY (id)
);
 */
