/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DashboardPieChart.TABLE_NAME)
public class DashboardPieChart {
	
	public DashboardPieChart() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "widgetid", 
		dataType = DataType.STRING, 
		width=50,  canBeNull=false)
	private String _widgetId;
	

public static final String TABLE_NAME = "dashboard_dashboardpiechart";
}
