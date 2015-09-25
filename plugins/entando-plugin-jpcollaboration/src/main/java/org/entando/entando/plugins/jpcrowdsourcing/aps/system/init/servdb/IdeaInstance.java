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
package org.entando.entando.plugins.jpcrowdsourcing.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * 
 */
@DatabaseTable(tableName = IdeaInstance.TABLE_NAME)
public class IdeaInstance {
	
	public IdeaInstance() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.STRING, 
			width = 80, 
			canBeNull = false, id = true)
	private String _code;
	
	@DatabaseField(columnName = "createdat", 
			dataType = DataType.DATE,
			canBeNull= true)
	private Date _createdat;
	
	public static final String TABLE_NAME = "jpcollaboration_ideainstance";
	
/*
CREATE TABLE collaboration_ideainstance
(
  code character varying(80) NOT NULL,
  createdat timestamp without time zone,
  CONSTRAINT collaboration_ideainstance_pkey PRIMARY KEY (code)
);
 */
}
