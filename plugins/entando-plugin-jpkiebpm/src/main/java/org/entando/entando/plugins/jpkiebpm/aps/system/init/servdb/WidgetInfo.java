/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = WidgetInfo.TABLE_NAME)
public class WidgetInfo {

	public WidgetInfo() {
	}

	@DatabaseField(columnName = "id", dataType = DataType.INTEGER, canBeNull = false, id = true)
	private int _id;

	@DatabaseField(columnName = "pagecode", width = 30, canBeNull = false)
	private String _pagecode;

	@DatabaseField(columnName = "framepos_online", dataType = DataType.INTEGER, canBeNull = true)
	private int _framePosOnline;

	@DatabaseField(columnName = "framepos_draft", dataType = DataType.INTEGER, canBeNull = true)
	private int _framePosDraft;

	@DatabaseField(columnName = "widget_type", dataType = DataType.LONG_STRING, canBeNull = false)
	private String _widget_type;

	@DatabaseField(columnName = "information_online", dataType = DataType.LONG_STRING, canBeNull = true)
	private String _informationOnline;

	@DatabaseField(columnName = "information_draft", dataType = DataType.LONG_STRING, canBeNull = true)
	private String _information_draft;

	public static final String TABLE_NAME = "jpkiebpm_widgetinfo";
}
