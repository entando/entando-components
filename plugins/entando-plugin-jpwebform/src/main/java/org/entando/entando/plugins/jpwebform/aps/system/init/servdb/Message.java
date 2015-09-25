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
package org.entando.entando.plugins.jpwebform.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Message.TABLE_NAME)
public class Message {
	
	public Message() {}
	
	@DatabaseField(columnName = "messageid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false, id = true)
	private String _messageTd;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40)
	private String _username;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 3, 
			canBeNull = false)
	private String _langCode;
	
	@DatabaseField(columnName = "messagetype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _messageType;
	
	@DatabaseField(columnName = "creationdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _creationDate;
	
	@DatabaseField(columnName = "senddate", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _sendDate;
	
	@DatabaseField(columnName = "messagexml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _messageXml;
	
	@DatabaseField(columnName = "versiontype", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _versionType;
	
	@DatabaseField(columnName = "done", 
			dataType = DataType.SHORT, 
			canBeNull = true)
	private short _done;
	
	public static final String TABLE_NAME = "jpwebform_messages";
	
}
/*
CREATE TABLE jpwebform_messages
(
  messageid character varying(16) NOT NULL,
  username character varying(40),
  langcode character varying(2) NOT NULL,
  messagetype character varying(30) NOT NULL,
  creationdate timestamp without time zone NOT NULL,
  senddate timestamp without time zone,
  messagexml character varying NOT NULL,
  versiontype integer NOT NULL,
  done smallint,
  CONSTRAINT jpwebform_messages_pkey PRIMARY KEY (messageid )
)
 */