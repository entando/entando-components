/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jptokenapi.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserToken.TABLE_NAME)
public class UserToken {
	
	public UserToken() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _username;
	
	@DatabaseField(columnName = "token", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _token;
	
	public static final String TABLE_NAME = "jptokenapi_usertokens";
	
}
/*
CREATE TABLE jptokenapi_usertokens (
  username character varying(40) NOT NULL,
  token character varying(100) NOT NULL,
  CONSTRAINT jptokenapi_usertokens_pkey PRIMARY KEY (username)
);
 */