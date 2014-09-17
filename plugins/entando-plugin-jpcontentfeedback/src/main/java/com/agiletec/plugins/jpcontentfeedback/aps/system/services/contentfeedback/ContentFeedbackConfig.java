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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "contentFeedbackConfig")
public class ContentFeedbackConfig implements IContentFeedbackConfig {

    public ContentFeedbackConfig() {
        super();
    }

    public ContentFeedbackConfig(String xml) throws Throwable {
        JAXBContext context = JAXBContext.newInstance(ContentFeedbackConfig.class);
        ContentFeedbackConfig config = (ContentFeedbackConfig) context.createUnmarshaller().unmarshal(new StringReader(xml));
        this.setComment(config.getComment());
        this.setAnonymousComment(config.getAnonymousComment());
        this.setModeratedComment(config.getModeratedComment());
        this.setRateContent(config.getRateContent());
        this.setRateComment(config.getRateComment());
    }

    public String toXML() throws Throwable {
        JAXBContext context = JAXBContext.newInstance(ContentFeedbackConfig.class);
        StringWriter sw = new StringWriter();
        context.createMarshaller().marshal(this, sw);
        return sw.toString();
    }

    public String getComment() {
        return _comment;
    }
    public void setComment(String comment) {
        this._comment = comment;
    }

    public String getAnonymousComment() {
        return _anonymousComment;
    }
    public void setAnonymousComment(String anonymousComment) {
        this._anonymousComment = anonymousComment;
    }

    public String getModeratedComment() {
		return _moderatedComment;
	}
	public void setModeratedComment(String moderatedComment) {
		this._moderatedComment = moderatedComment;
	}


	public String getRateContent() {
		return _rateContent;
	}
	public void setRateContent(String rateContent) {
		this._rateContent = rateContent;
	}

    public String getRateComment() {
        return _rateComment;
    }
    public void setRateComment(String rateComment) {
        this._rateComment = rateComment;
    }

	private String _comment;
    private String _anonymousComment;
    private String _moderatedComment;
    private String _rateContent;
    private String _rateComment;

}
