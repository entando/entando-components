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
package com.agiletec.plugins.jpphotogallery.apsadmin.portal.specialwidget.photogallery;

import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.ContentListViewerWidgetAction;

public class PhotogalleryWidgetAction extends ContentListViewerWidgetAction {
	
	public String getModelIdMaster() {
		return _modelIdMaster;
	}
	public void setModelIdMaster(String modelIdMaster) {
		this._modelIdMaster = modelIdMaster;
	}
	
	public String getModelIdPreview() {
		return _modelIdPreview;
	}
	public void setModelIdPreview(String modelIdPreview) {
		this._modelIdPreview = modelIdPreview;
	}
	
	// Parametri showlet jpphotogallery
	private String _modelIdMaster;
	private String _modelIdPreview;
	
}