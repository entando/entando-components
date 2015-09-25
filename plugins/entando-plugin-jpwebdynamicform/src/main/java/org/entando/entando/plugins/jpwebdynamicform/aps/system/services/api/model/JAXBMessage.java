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
package org.entando.entando.plugins.jpwebdynamicform.aps.system.services.api.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.JAXBEntity;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "message")
@XmlType(propOrder = {"creationDate", "langCode", "username", "answers"})
@XmlSeeAlso({JAXBAnswer.class})
public class JAXBMessage extends JAXBEntity {
    
    public JAXBMessage() {}
    
    public JAXBMessage(IApsEntity mainEntity, String langCode) {
        super(mainEntity, langCode);
        Message message = (Message) mainEntity;
        this.setCreationDate(message.getCreationDate());
		this.setLangCode(message.getLangCode());
		this.setUsername(message.getUsername());
    }
    
    public IApsEntity buildEntity(IApsEntity prototype, ICategoryManager categoryManager) {
        Message message = (Message) super.buildEntity(prototype, categoryManager);
        //this.setCreationDate(message.getCreationDate());
		message.setLangCode(this.getLangCode());
		message.setUsername(this.getUsername());
		return message;
    }
    
	@XmlElement(name = "username", required = false)
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	@XmlElement(name = "creationDate", required = false)
	public Date getCreationDate() {
		return _creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	@XmlElement(name = "langCode", required = false)
	public String getLangCode() {
		return _langCode;
	}
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
    @XmlElement(name = "answer", required = false)
    @XmlElementWrapper(name = "answers", required = false)
    public List<JAXBAnswer> getAnswers() {
        return this._answers;
    }
    
    public void addAnswers(List<Answer> mainAnswers) {
        if (null == mainAnswers || mainAnswers.isEmpty()) {
            return;
        }
		this._answers = new ArrayList<JAXBAnswer>();
		for (int i = 0; i < mainAnswers.size(); i++) {
			Answer answer = mainAnswers.get(i);
			JAXBAnswer jaxbAnswer = new JAXBAnswer(answer);
			this._answers.add(jaxbAnswer);
		}
    }
    
	private String _username;
	private Date _creationDate;
	private String _langCode;
	
    private List<JAXBAnswer> _answers;
	
}