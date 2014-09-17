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
