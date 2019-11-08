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
package com.agiletec.plugins.jacms.aps.system.services.resource.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class MockResourceDataBean implements ResourceDataBean {

    private String _resourceType;
    private String _descr;
    private String _mainGroup;
    private File _file;
    private List<Category> _categories = new ArrayList<Category>();
    private String _mimeType;
    private Map<String,String> metadata = new HashMap<String, String>();
    private String owner;
    public String getResourceId() {
        return null;
    }

    /**
     * Restituisce il tipo di risorsa.
     *
     * @return Il tipo di risorsa.
     */
    public String getResourceType() {
        return _resourceType;
    }

    /**
     * Setta il tipo di risorsa.
     *
     * @param type Il tipo di risorsa.
     */
    public void setResourceType(String type) {
        this._resourceType = type;
    }

    /**
     * Setta la descrizione della risorsa.
     *
     * @param descr La descrizione della risorsa.
     */
    public void setDescr(String descr) {
        this._descr = descr;
    }

    /**
     * Restituisce la descrizione della risorsa.
     *
     * @return La descrizione della risorsa.
     */
    public String getDescr() {
        return _descr;
    }

    /**
     * Setta il file relativo alla risorsa.
     *
     * @param file Il file relativo alla risorsa.
     */
    public void setFile(File file) {
        this._file = file;
    }

    public String getMainGroup() {
        return _mainGroup;
    }

    public void setMainGroup(String mainGroup) {
        this._mainGroup = mainGroup;
    }

    public List<Category> getCategories() {
        return _categories;
    }

    public void setCategories(List<Category> categories) {
        this._categories = categories;
    }

    public int getFileSize() {
        return (int) this._file.length() / 1000;
    }

    public InputStream getInputStream() throws Throwable {
        return new FileInputStream(this._file);
    }

    public String getFileName() {
        String filename = _file.getName().substring(_file.getName().lastIndexOf('/') + 1).trim();
        return filename;
    }

    public String getMimeType() {
        return _mimeType;
    }

    public void setMimeType(String mimetype) {
        this._mimeType = mimetype;
    }

    @Override
    public File getFile() {
        return _file;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(Map<String,String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
