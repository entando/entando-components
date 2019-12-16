/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import java.util.Date;

import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;

/**
 * @author E.Santoboni
 */
public class ContentRecordVO extends ApsEntityRecord {
	
	private String description;
	private String status;
	private Date create;
	private Date modify;
	private Date publish;
	private boolean onLine;
	private boolean sync;
	private String xmlOnLine;
	
	private String mainGroupCode;
    
	private String version;
	private String firstEditor;
	private String lastEditor;
    private String restriction;
	
	public String getDescription() {
		return description;
	}
	@Deprecated
	public String getDescr() {
		return this.getDescription();
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	@Deprecated
	public void setDescr(String description) {
		this.setDescription(description);
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getCreate() {
		return create;
	}
	public void setCreate(Date create) {
		this.create = create;
	}
	
	public Date getModify() {
		return modify;
	}
	public void setModify(Date modify) {
		this.modify = modify;
	}

    public Date getPublish() {
        return publish;
    }
    public void setPublish(Date publish) {
        this.publish = publish;
    }
	
	public String getXmlWork() {
		return super.getXml();
	}
	public void setXmlWork(String xmlWork) {
		super.setXml(xmlWork);
	}
	
	public boolean isOnLine() {
		return onLine;
	}
	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}
	
	public boolean isSync() {
		return sync;
	}
	public void setSync(boolean sync) {
		this.sync = sync;
	}
	
	public String getXmlOnLine() {
		return xmlOnLine;
	}
	public void setXmlOnLine(String xmlOnLine) {
		this.xmlOnLine = xmlOnLine;
	}
	
	public String getMainGroupCode() {
		return mainGroupCode;
	}
	public void setMainGroupCode(String mainGroupCode) {
		this.mainGroupCode = mainGroupCode;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getFirstEditor() {
		return firstEditor;
	}
	public void setFirstEditor(String firstEditor) {
		this.firstEditor = firstEditor;
	}
	
	public String getLastEditor() {
		return lastEditor;
	}
	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}

	public String getRestriction() {
		return restriction;
	}
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}
	
}
