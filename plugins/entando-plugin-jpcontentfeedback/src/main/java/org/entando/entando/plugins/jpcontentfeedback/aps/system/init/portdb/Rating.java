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
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Rating.TABLE_NAME)
public class Rating implements ExtendedColumnDefinition {
	
	public Rating() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(foreign = true, columnName = "commentid", 
			canBeNull = true)
	private Comment _comment;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16)
	private String _contentid;
	
	@DatabaseField(columnName = "voters", 
			dataType = DataType.INTEGER)
	private int _voters;
	
	@DatabaseField(columnName = "sumvote", 
			dataType = DataType.INTEGER)
	private int _sumvote;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String commentTableName = Comment.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			commentTableName = "`" + commentTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT jpcontentfeedback_rat_id_fkey FOREIGN KEY (commentid) "
				+ "REFERENCES " + commentTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpcontentfeedback_rating";
	
}
/*
CREATE TABLE jpcontentfeedback_rating
(
  id integer NOT NULL,
  commentid integer,
  contentid character varying(16),
  voters integer,
  sumvote integer,
  CONSTRAINT jpcontentfeedback_rating_pkey PRIMARY KEY (id),
  CONSTRAINT jpcontentfeedback_rating_refcommentid_fkey FOREIGN KEY (commentid)
      REFERENCES jpcontentfeedback_comments (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 */
