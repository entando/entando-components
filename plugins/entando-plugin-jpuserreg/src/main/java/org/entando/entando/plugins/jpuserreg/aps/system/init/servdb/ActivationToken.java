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
package org.entando.entando.plugins.jpuserreg.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ActivationToken.TABLE_NAME)
public class ActivationToken {
	
	public ActivationToken() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _username;
	
	@DatabaseField(columnName = "token", 
			dataType = DataType.STRING, 
			width = 250, 
			canBeNull = false)
	private String _token;
	
	@DatabaseField(columnName = "regtime", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _registrationTime;
	
	@DatabaseField(columnName = "tokentype", 
			dataType = DataType.STRING, 
			width = 25, 
			canBeNull = false)
	private String _tokentype;
	
	public static final String TABLE_NAME = "jpuserreg_activationtokens";
	
}
/*
CREATE TABLE jpuserreg_activationtokens
(
  username character varying(40) NOT NULL,
  token character varying NOT NULL,
  regtime timestamp without time zone NOT NULL,
  tokentype character varying(25) NOT NULL,
  CONSTRAINT jpuserreg_activationtokens_pkey PRIMARY KEY (username)
);
 */