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
package com.agiletec.plugins.jpgeoref.aps.tags.helper;

public class GeorefInfoBean {

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public double getX() {
		return _x;
	}
	public void setX(double x) {
		this._x = x;
	}
	
	public double getY() {
		return _y;
	}
	public void setY(double y) {
		this._y = y;
	}
	
	public double getZ() {
		return _z;
	}
	public void setZ(double z) {
		this._z = z;
	}
	
	private String _contentId;
	private double _x;
	private double _y;
	private double _z;
}
