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
package org.entando.entando.plugins.jpsurvey.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Survey.TABLE_NAME)
public class Survey {
	
	public Survey() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "description", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20)
	private String _mainGroup;
	
	@DatabaseField(columnName = "startdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _startDate;
	
	@DatabaseField(columnName = "enddate", 
			dataType = DataType.DATE)
	private Date _endDate;
	
	@DatabaseField(columnName = "active", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _active;
	
	@DatabaseField(columnName = "publicpartialresult", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _publicPartialResult;
	
	@DatabaseField(columnName = "publicresult", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _publicResult;
	
	@DatabaseField(columnName = "questionnaire", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _questionnaire;
	
	@DatabaseField(columnName = "gatheruserinfo", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _gatherUserInfo;
	
	@DatabaseField(columnName = "title", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _title;
	
	@DatabaseField(columnName = "restrictedaccess", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _restrictedAccess;
	
	@DatabaseField(columnName = "checkcookie", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _checkCookie;
	
	@DatabaseField(columnName = "checkipaddress", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _checkIpAddress;

	@DatabaseField(columnName = "checkusername", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _checkUsername;
	
	@DatabaseField(columnName = "imageid", 
			dataType = DataType.STRING, 
			width = 16)
	private String _imageid;
	
	@DatabaseField(columnName = "imagedescr", 
			dataType = DataType.LONG_STRING)
	private String _imageDescription;
	
	public static final String TABLE_NAME = "jpsurvey";
	
}
/*

-------------- mercoledì, 24 aprile 2013------------------------
ALTER TABLE jpsurvey ADD COLUMN checkusername smallint;
update jpsurvey SET checkusername = 0;
ALTER TABLE jpsurvey ALTER COLUMN checkusername SET NOT NULL;
----------------------------------------------------------------

CREATE TABLE jpsurvey
(
  id integer NOT NULL,
  description character varying NOT NULL,
  maingroup character varying(20) NOT NULL,
  startdate date NOT NULL,
  enddate date,
  active smallint NOT NULL,
  publicpartialresult smallint NOT NULL,
  publicresult smallint NOT NULL,
  questionnaire smallint NOT NULL,
  gatheruserinfo smallint NOT NULL,
  title character varying NOT NULL,
  restrictedaccess smallint NOT NULL,
  checkcookie smallint NOT NULL,
  checkipaddress smallint NOT NULL,
  checkusername smallint NOT NULL,
  imageid character varying(16),
  imagedescr character varying,
  CONSTRAINT jpsurvey_pkey PRIMARY KEY (id)
) ;
 */