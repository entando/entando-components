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
package org.entando.entando.plugins.jpnewsletter.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ContentQueueElement.TABLE_NAME)
public class ContentQueueElement {
	
	public ContentQueueElement() {}
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = false, id = true)
	private String _contentId;
	
	public static final String TABLE_NAME = "jpnewsletter_contentqueue";
	
}
/*
CREATE TABLE jpnewsletter_contentqueue
(
  contentid character varying(16) NOT NULL,
  CONSTRAINT jpnewsletter_contentqueue_pkey PRIMARY KEY (contentid)
);
 */