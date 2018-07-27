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
package com.agiletec.plugins.jacms.apsadmin.resource;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleResourceAction extends ResourceAction {

    private static final Logger logger = LoggerFactory.getLogger(MultipleResourceAction.class);
    
    @Override
    public void validate() {
       
        if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {

            if (null == getFileDescriptions()) {
                this.addFieldError(DESCR_FIELD + 0, getText("error.resource.file.descrEmpty"));
            }
            if (null == getFileUpload()) {
                this.addFieldError(FILE_UPLOAD_FIELD + 0, this.getText("error.resource.file.fileEmpty"));
            }

        } else {
            try {
                if (null == getFileUploadInputStream()) {
                    this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.void"));
                }

            } catch (Throwable ex) {
                java.util.logging.Logger.getLogger(MultipleResourceAction.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (null != getFileUpload()) {

                fetchFileDescriptions();
                validateFileDescriptions();

                ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
                this.getFileUploadFileName().forEach(fileName
                        -> checkRightFileType(resourcePrototype, fileName));

            }
        }
    }

    public String edit() {
        try {
            ResourceInterface resource = this.loadResource(this.getResourceId());
            this.setResourceTypeCode(resource.getType());
            List fileDescr = new ArrayList<String>();
            fileDescr.add(resource.getDescription());
            setFileDescriptions(fileDescr);
            List<Category> resCategories = resource.getCategories();
            for (int i = 0; i < resCategories.size(); i++) {
                Category resCat = resCategories.get(i);
                this.getCategoryCodes().add(resCat.getCode());
            }
            this.setMainGroup(resource.getMainGroup());
            this.setStrutsAction(ApsAdminSystemConstants.EDIT);
        } catch (Throwable t) {
            logger.error("error in edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected void validateFileDescriptions() {
        int i = 0;
        for (File file : this.getFileUpload()) {
            if (null != fileDescriptions) {
                if (!(fileDescriptions.get(i).length() > 0)) {
                    this.addFieldError(DESCR_FIELD + i, this.getText("error.resource.file.descrEmpty"));
                }
            } else {
                this.addFieldError(DESCR_FIELD + i, this.getText("error.resource.file.descrEmpty"));
            }
            i++;
        }
    }

    public String joinCategory() {
        fetchFileDescriptions();
        return super.joinCategory();
    }

    public String removeCategory() {
        fetchFileDescriptions();
        return super.removeCategory();
    }

    protected void checkRightFileType(ResourceInterface resourcePrototype, String fileName) {
        boolean isRight = false;
        if (fileName.length() > 0) {
            String docType = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
            String[] types = resourcePrototype.getAllowedFileTypes();
            isRight = isValidType(docType, types);
        } else {
            isRight = true;
        }
        if (!isRight) {
            this.addFieldError("upload", this.getText("error.resource.file.wrongFormat"));
        }
    }

    protected boolean isValidType(String docType, String[] rightTypes) {
        boolean isValid = false;
        if (rightTypes.length > 0) {
            for (int i = 0; i < rightTypes.length; i++) {
                if (docType.toLowerCase().equals(rightTypes[i])) {
                    isValid = true;
                    break;
                }
            }
        } else {
            isValid = true;
        }
        return isValid;
    }

    public String increaseCount() {
        fetchFileDescriptions();
        fieldCount++;
        return SUCCESS;
    }

    public String decreaseCount() {
        fetchFileDescriptions();
        fieldCount--;
        return SUCCESS;
    }

    /**
     * Executes the specific action to modify an existing resource.
     *
     * @return The result code.
     */
    @Override
    public String save() {
        int index = 0;

        try {

            fetchFileDescriptions();
            for (String fileDescription : getFileDescriptions()) {

                if ((fileDescription.length() > 0) && (null != getFile(index))) {
                    File file = getFile(index);
                    BaseResourceDataBean resourceFile = new BaseResourceDataBean(file);

                    resourceFile.setFileName(getFileUploadFileName().get(index));
                    resourceFile.setMimeType(getFileUploadContentType().get(index));

                    resourceFile.setDescr(fileDescription);
                    resourceFile.setMainGroup(getMainGroup());
                    resourceFile.setResourceType(this.getResourceType());
                    resourceFile.setCategories(getCategories());

                    List<BaseResourceDataBean> baseResourceDataBeanList = new ArrayList<BaseResourceDataBean>();
                    baseResourceDataBeanList.add(resourceFile);

                    try {
                        if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
                            this.getResourceManager().addResources(baseResourceDataBeanList);
                        } else if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
                            resourceFile.setResourceId(super.getResourceId());
                            this.getResourceManager().updateResource(resourceFile);
                        }
                        this.addActionMessage(this.getText("message.resource.filename.uploaded",
                                new String[]{fileUploadFileName.get(index)}));
                    } catch (ApsSystemException ex) {
                        java.util.logging.Logger.getLogger(ResourceAction.class.getName()).log(Level.SEVERE, null, ex);
                        // add action error
                        this.addFieldError(String.valueOf(index), this.getText("error.resource.filename.uploadError",
                                new String[]{fileUploadFileName.get(index)}));
                    }
                }
                index++;
            }

        } catch (Throwable t) {
            logger.error("error in save", t);
            return FAILURE;
        }

        return SUCCESS;
    }

    public String getFileDescription(int i) {
        if (null != fileDescriptions
                && !fileDescriptions.isEmpty()) {
            return fileDescriptions.get(i);
        }
        return "";
    }

    protected void fetchFileDescriptions() {

        if (null == fileDescriptions) {
            fileDescriptions = new ArrayList<>();
        }
        fileDescriptions.clear();
        for (int i = 0; i <= fieldCount; i++) {
            String desc = this.getRequest().getParameter(DESCR_FIELD + i);
            fileDescriptions.add(i, desc);
        }
    }

    public File getFile(int index) {
        return fileUpload.get(index);
    }

    public InputStream getFileUploadInputStream() throws Throwable {
        if (null == this.getFileUpload()) {
            return null;
        }
        return new FileInputStream(this.getFile(0));
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public List<String> getFileDescriptions() {
        return fileDescriptions;
    }

    public void setFileDescriptions(List<String> fileDescriptions) {
        this.fileDescriptions = fileDescriptions;
    }

    public List<File> getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(List<File> fileUpload) {
        this.fileUpload = fileUpload;
    }

    public List<String> getFileUploadContentType() {
        return fileUploadContentType;
    }

    public void setFileUploadContentType(List<String> fileUploadContentType) {
        this.fileUploadContentType = fileUploadContentType;
    }

    public List<String> getFileUploadFileName() {
        return fileUploadFileName;
    }

    public String getFileUploadFileName(int i) {
        if (null != fileUploadFileName) {
            return fileUploadFileName.get(i);
        }
        return "";
    }

    public void setFileUploadFileName(List<String> fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }

    private int fieldCount = 0;
    private List<String> fileDescriptions;

    private List<File> fileUpload = new ArrayList<File>();
    private List<String> fileUploadContentType = new ArrayList<String>();
    private List<String> fileUploadFileName = new ArrayList<String>();

    public final static String DESCR_FIELD = "descr_";
    public final static String FILE_UPLOAD_FIELD = "fileUpload_";

}
