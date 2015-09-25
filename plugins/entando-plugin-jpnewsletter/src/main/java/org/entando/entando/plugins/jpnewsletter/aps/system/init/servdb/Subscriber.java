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
package org.entando.entando.plugins.jpnewsletter.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Subscriber.TABLE_NAME)
public class Subscriber {
	
	public Subscriber() {}
	
	@DatabaseField(columnName = "mailaddress", 
			dataType = DataType.STRING,
			width = 100, 
			canBeNull = false, id = true)
	private String _mailAddress;
	
	@DatabaseField(columnName = "subscription_date", 
			dataType = DataType.DATE)
	private Date _subscriptionDate;
	
	@DatabaseField(columnName = "active", 
			dataType = DataType.SHORT)
	private short _active;
	
	public static final String TABLE_NAME = "jpnewsletter_subscribers";
	
}
/*
CREATE TABLE jpnewsletter_subscribers
(
  mailaddress character varying(100) NOT NULL,
  subscription_date date,
  active smallint,
  CONSTRAINT jpnewsletter_subscribers_pkey PRIMARY KEY (mailaddress)
)
 */