/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SubjectTerm;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebmail.apsadmin.util.AttachmentInfo;
import com.agiletec.plugins.jpwebmail.apsadmin.util.CurrentFolderMessagesInfo;
import com.agiletec.plugins.jpwebmail.apsadmin.util.WebMailHelper;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.AbstractWebmailBaseAction;

/**
 * Classe action astratta base per la gestione dei mesaggi singoli.
 * La classe fornisce i metodi base per la visualizzazione dei messaggi singoli e per 
 * la creazione di nuovi messaggi come reply (o forward) di messaggi singoli preesistenti.
 * @author E.Santoboni
 */
public class AbstractMessageAction extends AbstractWebmailBaseAction {
	
	/**
	 * Restituisce un messaggio.
	 * Il messaggio viene estratto in base alla posizione (proprietà messageIndex) 
	 * nella cartella corrente. Restituisce null nel caso che nessun messaggio sia stato selezionato, 
	 * o i parametri recuperati non consentano l'individuazione di un messaggio.
	 * @return Il messaggio selezionato.
	 * @throws ApsSystemException In caso di errore nel recupero del messaggio selezionato.
	 */
	protected Message getSelectedMessage() throws ApsSystemException {
		List<Message> messages = this.getCurrentFolderMessages();
		if (null == messages || messages.isEmpty()) {
			return null;
		}
		Message message = null;
		try {
			Message smallMessage = messages.get(this.getMessageIndex());
			if (null == smallMessage) return null; 
			message = this.searchCompleteMessage(smallMessage);
		} catch (Throwable t) {
			throw new ApsSystemException("Errore in estrazione messaggio", t);
		}
		return message;
	}
	
	/**
	 * Restituisce un messaggio completo corrispondente al messaggio "small" specificato.
	 * Il messaggio specificato nel parametro "smallMessage" è corrispondente ad un messaggio in lista 
	 * e presenta solo gli attributi necessari per la visualizzazione degli elementi/messaggi in lista 
	 * (destinatari, data spedizione, oggetto).
	 * Nel caso non sia stato trovato nessun messaggio completo corrispondente 
	 * al messaggio small specificato, viene restituito null.
	 * @param smallMessage Il messaggio in forma "small".
	 * @return Il messaggio completo.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected Message searchCompleteMessage(Message smallMessage) throws ApsSystemException {
		try {
			SubjectTerm term = new SubjectTerm(smallMessage.getSubject());
			Folder folder = this.getCurrentFolder();
			folder.open(Folder.READ_WRITE);
			this.setOpenedFolder(folder);
			Message[] messages = folder.search(term);
			if (null != messages) {
				if (messages.length==1) {
					return messages[0]; 
				} else {
					for (int i=0; i<messages.length; i++) {
						if (this.isSame(smallMessage, messages[i])) {
							return messages[i];
						}
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchCompleteMessage");
			throw new ApsSystemException("Errore in estrazione messaggio", t);
		}
		return null;
	}
	
	/**
	 * Verifica la corrispondenza tra un messaggio in forma small ed un messaggio completo.
	 * Il confronto viene effettuato in base alla data di spedizione, alla rata di ricezione ed al soggetto della mail
	 * @param smallMessage Il messaggio in forma small.
	 * @param message Il messaggio completo.
	 * @return true se i due messaggi sono corrispondenti, false in caso contrario.
	 * @throws Throwable In caso di errore.
	 */
	protected boolean isSame(Message smallMessage, Message message) throws Throwable {
		return (smallMessage.getSentDate().equals(message.getSentDate()) 
				&& smallMessage.getReceivedDate().equals(message.getReceivedDate()) 
				&& smallMessage.getSubject().equals(message.getSubject()));
	}
	
	/**
	 * Restituisce il contenuto del messaggio specificato.
	 * @param message Il messaggio cui restituire il contenuto.
	 * @return Il contenuto del messaggio.
	 */
	public String getContent(Message message) {
		try {
			this.checkFolder(message.getFolder());
			Object content = message.getContent();
			if (content instanceof String) {
				boolean isHtmlText = WebMailHelper.isHtmlContent((String) content);
				if (isHtmlText) {
					return WebMailHelper.getCleanedHtmlContent((String) content);
				} else {
					return content.toString();
				}
			} else if (content instanceof MimeMultipart) {
				MimeMultipart multipart = (MimeMultipart) content;
				StringBuffer buffer = new StringBuffer();
				for (int x = 0; x < multipart.getCount(); x++) {
	                BodyPart bodyPart = multipart.getBodyPart(x);
	                /*
	                System.out.println("bodyPart.getContentType() " + x + " - " + bodyPart.getContentType());
	                System.out.println("bodyPart.getDescription() " + x + " - " + bodyPart.getDescription());
	                System.out.println("bodyPart.getDisposition() " + x + " - " + bodyPart.getDisposition());
	                System.out.println("bodyPart.getFileName() " + x + " - " + bodyPart.getFileName());
	                System.out.println("bodyPart.getSize() " + x + " - " + bodyPart.getSize());
	                System.out.println("bodyPart.getAllHeaders() " + x + " - " + bodyPart.getAllHeaders());
	                System.out.println("bodyPart.getContentType() " + x + " - " + bodyPart.getDataHandler().getContentType());
	                System.out.println("bodyPart.getName() " + x + " - " + bodyPart.getDataHandler().getName());
	                */
	                if (!this.isAttachment(bodyPart)) {
	                	//System.out.println(x + " - BODY PART DA EROGARE");
	                	//System.out.println("DEVE ENTRATE A NAVIGARE DENTRO LA SOTTOPARTE");
		    			this.flushBodyPart(bodyPart, buffer, true);
		    			break;
	                }
	            }
				return buffer.toString();
			}
		} catch (Throwable t) {
			String subject = "Mail Object";
			try {
				subject = message.getSubject();
			} catch (MessagingException e) {
				ApsSystemUtils.logThrowable(t, this, "getContent", "Errore in estrazione soggetto mail ");
			}
			ApsSystemUtils.logThrowable(t, this, "getContent", "Errore in estrazione corpo testo di mail '" + subject + "'");
		}
		return "";
	}
	
	private void flushBodyPart(BodyPart bodyPart, StringBuffer buffer, boolean first) throws Throwable {
		Object content = bodyPart.getContent();
		if (content instanceof String) {
			boolean isHtmlText = WebMailHelper.isHtmlContent((String) content);
			buffer.setLength(0);
			if (isHtmlText) {
				buffer.append(WebMailHelper.getCleanedHtmlContent((String) content));
				return;
			} else {
				buffer.append((String) content);
				return;
			}
			//System.out.println("bodyPart isHtmlText " + isHtmlText);
			//buffer = new StringBuffer((String) content);
			//return true;
		} else if (content instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) content;
			for (int x = 0; x < multipart.getCount(); x++) {
				BodyPart otherBodyPart = multipart.getBodyPart(x);
                if (this.isAttachment(otherBodyPart)) {
                	DataHandler handler = bodyPart.getDataHandler();
                	//System.out.println("otherBodyPartContent HA UN ALLEGATO " + handler.getName());
                	continue;
                }
                Object otherBodyPartContent = otherBodyPart.getContent();
				if (otherBodyPartContent instanceof String) {
    				boolean isHtmlText = WebMailHelper.isHtmlContent((String) otherBodyPartContent);
    				//System.out.println("otherBodyPartContent isHtmlText " + isHtmlText);
    				//System.out.println("******************** " + otherBodyPartContent + " ********************");
    				buffer.setLength(0);
    				if (isHtmlText) {
    					buffer.append(WebMailHelper.getCleanedHtmlContent(otherBodyPartContent.toString()));
    					return;
    				} else {
    					buffer.append(otherBodyPartContent.toString());
    					return;
    				}
    				/*
    				System.out.println("******************** " + otherBodyPartContent + " ********************");
    				buffer = new StringBuffer("****otherBodyPartContent***"+otherBodyPartContent.toString()+"****otherBodyPartContent***");
    				return true;
    				*/
    			}
            }
		}
	}
	
	/**
	 * Verifica se il bodypart specificato rappresenta un attachment.
	 * @param bodyPart Il bodypart da analizzare.
	 * @return true se il bodypart è un attachment, false in caso contrario.
	 * @throws Throwable In caso di errore.
	 */
	protected boolean isAttachment(BodyPart bodyPart) throws Throwable {
		String disposition = bodyPart.getDisposition();
        return (disposition != null && bodyPart.getFileName() != null 
        		&& (disposition.equalsIgnoreCase(BodyPart.ATTACHMENT) || disposition.equalsIgnoreCase(BodyPart.INLINE)));
	}
	
	public String getTo(Message message) {
		return this.getRecipients(message, Message.RecipientType.TO);
	}
	
	public String getCc(Message message) {
		return this.getRecipients(message, Message.RecipientType.CC);
	}
	
	public String getBcc(Message message) {
		return this.getRecipients(message, Message.RecipientType.BCC);
	}
	
	protected String getRecipients(Message message, Message.RecipientType type) {
		try {
			return MimeUtility.decodeText(WebMailHelper.joinAddress(message.getRecipients(type)));
		} catch (Throwable t) {
			String subject = "Mail Object";
			try {
				subject = message.getSubject();
			} catch (Throwable tt) {
				ApsSystemUtils.logThrowable(tt, this, "getContent", "Errore in estrazione soggetto mail ");
			}
			ApsSystemUtils.logThrowable(t, this, "getContent", "Errore in estrazione Recipients " + type + " di mail '" + subject + "'");
		}
		return "";
	}
	
	public List<AttachmentInfo> getAttachmentInfos(Message message) {
		List<AttachmentInfo> infos = new ArrayList<AttachmentInfo>();
		try {
			this.checkFolder(message.getFolder());
			Object content = message.getContent();
			if (content instanceof MimeMultipart) {
				MimeMultipart multipart = (MimeMultipart) content;
				for (int x = 0; x < multipart.getCount(); x++) {
	                BodyPart bodyPart = multipart.getBodyPart(x);
	                /*
	                System.out.println("bodyPart.getContentType() " + x + " - " + bodyPart.getContentType());
	                System.out.println("bodyPart.getDescription() " + x + " - " + bodyPart.getDescription());
	                System.out.println("bodyPart.getDisposition() " + x + " - " + bodyPart.getDisposition());
	                System.out.println("bodyPart.getFileName() " + x + " - " + bodyPart.getFileName());
	                System.out.println("bodyPart.getSize() " + x + " - " + bodyPart.getSize());
	                System.out.println("bodyPart.getAllHeaders() " + x + " - " + bodyPart.getAllHeaders());
	                System.out.println("bodyPart.getContentType() " + x + " - " + bodyPart.getDataHandler().getContentType());
	                System.out.println("bodyPart.getName() " + x + " - " + bodyPart.getDataHandler().getName());
	                */
	                if (this.isAttachment(bodyPart)) {
	                	AttachmentInfo info = new AttachmentInfo(x, bodyPart);
	                	infos.add(info);
	                	continue;
	                }
	                Object bodyPartContent = bodyPart.getContent();
	                if (bodyPartContent instanceof MimeMultipart) {
	                	this.searchAttachments((MimeMultipart) bodyPartContent, infos, x);
	                }
	            }
			}
		} catch (Throwable t) {
			String subject = "Mail Object";
			try {
				subject = message.getSubject();
			} catch (MessagingException e) {
				ApsSystemUtils.logThrowable(t, this, "getAttachmentInfos", "Errore in estrazione soggetto mail ");
			}
			ApsSystemUtils.logThrowable(t, this, "getAttachmentInfos", "Errore in estrazione attachments di mail '" + subject + "'");
		}
		return infos;
	}
	
	private void searchAttachments(MimeMultipart multipart, List<AttachmentInfo> infos, int partNumber) throws Throwable {
		for (int x = 0; x < multipart.getCount(); x++) {
			BodyPart otherBodyPart = multipart.getBodyPart(x);
			if (this.isAttachment(otherBodyPart)) {
				AttachmentInfo info = new AttachmentInfo(partNumber, otherBodyPart, x);
               	infos.add(info);
			}
		}
	}
	
	public List<Message> getCurrentFolderMessages() {
		CurrentFolderMessagesInfo folderInfos = (CurrentFolderMessagesInfo) this.getRequest().getSession().getAttribute(CurrentFolderMessagesInfo.CURRENT_FOLDER_MESSAGES);
		if (folderInfos == null) {
			throw new RuntimeException("Informazioni nulle su cartella corrente " + this.getCurrentFolderName());
		}
		if (!folderInfos.getFolderName().equals(this.getCurrentFolderName())) {
			throw new RuntimeException("Informazioni richieste su cartella " + folderInfos.getFolderName() 
					+ " non compatibili con cartella corrente " + this.getCurrentFolderName());
		}
		return folderInfos.getMessages();
	}
	
	public int getMessageIndex() {
		return _messageIndex;
	}
	public void setMessageIndex(int messageIndex) {
		this._messageIndex = messageIndex;
	}
	
	private int _messageIndex = -1;
	
}
