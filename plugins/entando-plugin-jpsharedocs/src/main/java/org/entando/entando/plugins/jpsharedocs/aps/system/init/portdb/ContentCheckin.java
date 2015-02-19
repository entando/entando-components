/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.system.init.portdb;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Mezzano
 */
@DatabaseTable(tableName = ContentCheckin.TABLE_NAME)
public class ContentCheckin {
	
	public ContentCheckin() {}
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false, id = true)
	private String _contentId;
	
	@DatabaseField(columnName = "checkin_date", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _checkinDate;
	
	@DatabaseField(columnName = "checkin_editor", 
			dataType = DataType.STRING, 
			width = 150, 
			canBeNull = false)
	private String _checkinEditor;
	
	public static final String TABLE_NAME = "jpsharedocs_checkin";
	
}
/*
CREATE TABLE jpsharedocs_checkin
(
  contentid character varying(16) NOT NULL,
  checkin_date timestamp without time zone NOT NULL,
  checkin_editor character varying(150) NOT NULL,
  CONSTRAINT jpcontentcheckin_checkin_pkey PRIMARY KEY (contentid)
)
 */