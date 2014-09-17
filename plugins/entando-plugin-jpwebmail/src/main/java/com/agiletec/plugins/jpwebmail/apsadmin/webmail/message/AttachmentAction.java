/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper.FileHelper;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper.INewMessageActionHelper;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class AttachmentAction extends AbstractMessageAction implements IAttachmentAction {
	
	public void validate() {
		super.validate();
		if (null == this._filename || this._filename.trim().length() == 0) {
			try {
				this.getNewMessageHelper().updateMessageOnSession(this.getRequest());
			} catch (Throwable t) {
				throw new RuntimeException("Errore in aggiornamento mail", t);
			}
			this.addFieldError("upload", this.getText("Resource.fileRequired"));
		}
	}
	
	@Override
	public String addAttachment() {
		try {
			MimeMessage message = this.getNewMessageHelper().updateMessageOnSession(this.getRequest());
			this.addNewBodyPart((Multipart) message.getContent());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addAttachment");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeAttachment() {
		try {
			MimeMessage message = this.getNewMessageHelper().updateMessageOnSession(this.getRequest());
			if (null == message) return "intro";
			Multipart multiPart = (MimeMultipart) message.getContent();
			multiPart.removeBodyPart(this.getAttachmentNumber());
			this.removeTempBodyPartFolder(this.getAttachmentNumber());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeAttachment");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void removeTempBodyPartFolder(int number) throws ApsSystemException {
		try {
			String bodypartFolder = this.getNewMessageHelper().getUserWebMailDiskRootFolder(this.getCurrentUser()) + File.separator + number;
			File dir = new File(bodypartFolder);
			FileHelper.deleteDir(dir, bodypartFolder);
	    } catch (Throwable t) {
	    	throw new ApsSystemException("Errore in preparazione attachments in reply", t);
	    }
	}
	
	private void addNewBodyPart(Multipart multiPart) throws ApsSystemException {
		try {
			String bodyPartFileName = this._filename;
			String bodypartFolder = this.getMultipartFolder(multiPart);
			String fileName = bodypartFolder + File.separator + bodyPartFileName;
			InputStream inputStream = new FileInputStream(this._file);
			FileHelper.saveFile(fileName, inputStream);
		    BodyPart messageBodyPart = new MimeBodyPart();
    		DataSource source = new FileDataSource(fileName);
    		messageBodyPart.setDataHandler(new DataHandler(source));
    		messageBodyPart.setFileName(bodyPartFileName);
    		messageBodyPart.setDisposition(Part.ATTACHMENT);
    		multiPart.addBodyPart(messageBodyPart);
	    } catch (Throwable t) {
	    	throw new ApsSystemException("Errore in preparazione attachments in reply", t);
	    }
	}
	
	/**
	 * Crea una cartella apposita per il nuovo attachment da inserire.
	 * @param multiPart Il nuovo multipart.
	 * @return La nuova cartella del nuovo miltipart.
	 * @throws Throwable In caso di errore.
	 */
	private String getMultipartFolder(Multipart multiPart) throws Throwable {
		int index = multiPart.getCount();
		String mainFolder = this.getNewMessageHelper().getUserWebMailDiskRootFolder(this.getCurrentUser()) + File.separator;
		File dir = null;
		String bodypartFolder = null;
		do {
			bodypartFolder = mainFolder + index;
			dir = new File(bodypartFolder);
			index++;
		} while (dir.exists());
		dir.mkdirs();
		return bodypartFolder;
	}
	
	
    public void setUpload(File file) {
       this._file = file;
    }
    public File getUpload() {
		return this._file;
	}

    public void setUploadContentType(String contentType) {
       this._contentType = contentType;
    }

    public void setUploadFileName(String filename) {
       this._filename = filename;
    }
    
    public int getAttachmentNumber() {
		return _attachmentNumber;
	}
	public void setAttachmentNumber(int attachmentNumber) {
		this._attachmentNumber = attachmentNumber;
	}
	
    protected INewMessageActionHelper getNewMessageHelper() {
		return _newMessageHelper;
	}
	public void setNewMessageHelper(INewMessageActionHelper newMessageHelper) {
		this._newMessageHelper = newMessageHelper;
	}
	
	private File _file;
    private String _contentType;
    private String _filename;
    
    private int _attachmentNumber;
    
	private INewMessageActionHelper _newMessageHelper;
	
}
