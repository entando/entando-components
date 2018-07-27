/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = FriendlyCode.TABLE_NAME)
public class FriendlyCode {
	
	public FriendlyCode() {}
	
	@DatabaseField(columnName = "friendlycode", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false, id = true)
	private String _friendlyCode;
	
	@DatabaseField(columnName = "pagecode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = true)
	private String _pageCode;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = true)
	private String _contentId;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 2, 
			canBeNull = true)
	private String _langcode;
	
	public static final String TABLE_NAME = "jpseo_friendlycode";
	
}
/*
CREATE TABLE jpseo_friendlycode
(
  friendlycode character varying(100) NOT NULL,
  pagecode character varying(30),
  contentid character varying(16),
  langcode character varying(2),
  CONSTRAINT jpseo_friendlycode_pkey PRIMARY KEY (friendlycode),
--  CONSTRAINT jpseo_friendlycode_contentid_fkey FOREIGN KEY (contentid)
--      REFERENCES contents (contentid) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION,
--  CONSTRAINT jpseo_friendlycode_pagecode_fkey FOREIGN KEY (pagecode)
--      REFERENCES pages (code) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */