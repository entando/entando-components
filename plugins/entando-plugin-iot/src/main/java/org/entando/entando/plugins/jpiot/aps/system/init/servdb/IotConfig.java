/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = IotConfig.TABLE_NAME)
public class IotConfig {
	
	public IotConfig() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "name", 
		dataType = DataType.STRING, 
		width=50,  canBeNull=false)
	private String _name;
	
	@DatabaseField(columnName = "hostname", 
		dataType = DataType.STRING, 
		width=50,  canBeNull=false)
	private String _hostname;
	
	@DatabaseField(columnName = "port", 
		dataType = DataType.INTEGER, 
		 canBeNull= true)
	private int _port;
	
	@DatabaseField(columnName = "webapp", 
		dataType = DataType.STRING, 
		width=50,  canBeNull=false)
	private String _webapp;
	
	@DatabaseField(columnName = "username", 
		dataType = DataType.STRING, 
		width=50,  canBeNull= true)
	private String _username;
	
	@DatabaseField(columnName = "password", 
		dataType = DataType.STRING, 
		width=50,  canBeNull= true)
	private String _password;
	
	@DatabaseField(columnName = "token", 
		dataType = DataType.STRING, 
		width=255,  canBeNull= true)
	private String _token;
	

public static final String TABLE_NAME = "jpiot_iotconfig";
}
