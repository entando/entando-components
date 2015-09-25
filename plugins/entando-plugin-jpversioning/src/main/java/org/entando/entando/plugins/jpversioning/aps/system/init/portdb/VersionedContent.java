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
package org.entando.entando.plugins.jpversioning.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.IDatabaseManager;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = VersionedContent.TABLE_NAME)
public class VersionedContent implements ExtendedColumnDefinition {
	
	public VersionedContent() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = false)
	private String _contentId;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _contentType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _description;
	
	/*
  id integer NOT NULL,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
	 */
	
	@DatabaseField(columnName = "status", 
			dataType = DataType.STRING, 
			width = 12, canBeNull = false)
	private String _status;
	
	@DatabaseField(columnName = "contentxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _contentXml;
	
	@DatabaseField(columnName = "versiondate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _versionDate;
	
	@DatabaseField(columnName = "versioncode", 
			dataType = DataType.STRING, 
			width = 7, canBeNull = false)
	private String _versionCode;
	
	/*
  status character varying(12) NOT NULL,
  contentxml character varying NOT NULL,
  versiondate timestamp without time zone NOT NULL,
  versioncode character varying(7) NOT NULL,
	 */
	
	@DatabaseField(columnName = "onlineversion", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _onLineVersion;
	
	@DatabaseField(columnName = "approved", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _approved;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40)
	private String _username;
	
	/*
  onlineversion integer NOT NULL,
  approved smallint NOT NULL,
  username character varying(40),
	 */
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		return new String[]{"ALTER TABLE " + TABLE_NAME + " " 
				+ "ADD CONSTRAINT jpvers_contentvers_key UNIQUE (contentid, versioncode)"};
	}
	
	public static final String TABLE_NAME = "jpversioning_versionedcontents";
	
}
/*
CREATE TABLE jpversioning_versionedcontents
(
  id integer NOT NULL,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  * 
  status character varying(12) NOT NULL,
  contentxml character varying NOT NULL,
  versiondate timestamp without time zone NOT NULL,
  versioncode character varying(7) NOT NULL,
  * 
  onlineversion integer NOT NULL,
  approved smallint NOT NULL,
  username character varying(40),
  CONSTRAINT jpversioning_versionedcontents_pkey PRIMARY KEY (id),
  CONSTRAINT jpversioning_versionedcontents_contentid_key UNIQUE (contentid, versioncode)
);
*/