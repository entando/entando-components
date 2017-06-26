/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.apsadmin.config;

import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.IContentSchedulerManager;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class ContentThreadConfigEmailAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentThreadConfigEmailAction.class);

	/**
	 * entrypoint
	 * 
	 * @return
	 */
	public String viewEmail() {
		try {

			ContentThreadConfig config = this.getContentSchedulerManager().getConfig();

			this.setSenderCode(config.getSenderCode());
			this.setMailAttrName(config.getMailAttrName());
			this.setAlsoHtml(config.isAlsoHtml());

			this.setSubject(config.getSubject());

			this.setHtmlFooter(config.getHtmlFooter());
			this.setHtmlHeader(config.getHtmlHeader());
			this.setHtmlSeparator(config.getHtmlSeparator());

			this.setTextFooter(config.getTextFooter());
			this.setTextHeader(config.getTextHeader());
			this.setTextSeparator(config.getTextSeparator());

			this.setHtmlFooterMove(config.getHtmlFooterMove());
			this.setHtmlHeaderMove(config.getHtmlHeaderMove());
			this.setHtmlSeparatorMove(config.getHtmlSeparatorMove());

			this.setTextFooterMove(config.getTextFooterMove());
			this.setTextHeaderMove(config.getTextHeaderMove());
			this.setTextSeparatorMove(config.getTextSeparatorMove());

		} catch (Throwable t) {
			_logger.error("Error in viewEmail", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String saveItem() {
		try {

			ContentThreadConfig config = this.getContentSchedulerManager().getConfig();

			config.setSenderCode(this.getSenderCode());
			config.setMailAttrName(this.getMailAttrName());
			config.setAlsoHtml(this.isAlsoHtml());

			config.setSubject(this.getSubject());

			config.setHtmlFooter(this.getHtmlFooter());
			config.setHtmlHeader(this.getHtmlHeader());
			config.setHtmlSeparator(this.getHtmlSeparator());

			config.setTextFooter(this.getTextFooter());
			config.setTextHeader(this.getTextHeader());
			config.setTextSeparator(this.getTextSeparator());

			config.setHtmlFooterMove(this.getHtmlFooterMove());
			config.setHtmlHeaderMove(this.getHtmlHeaderMove());
			config.setHtmlSeparatorMove(this.getHtmlSeparatorMove());

			config.setTextFooterMove(this.getTextFooterMove());
			config.setTextHeaderMove(this.getTextHeaderMove());
			config.setTextSeparatorMove(this.getTextSeparatorMove());

			this.getContentSchedulerManager().updateConfig(config);

			this.addActionMessage(this.getText("jpcontentscheduler.saveItem.success"));
		} catch (Throwable t) {
			_logger.error("Error saving item", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public void setBaseConfigManager(ConfigInterface baseConfigManager) {
		this._baseConfigManager = baseConfigManager;
	}

	public ConfigInterface getBaseConfigManager() {
		return _baseConfigManager;
	}

	public IContentSchedulerManager getContentSchedulerManager() {
		return _contentSchedulerManager;
	}

	public void setContentSchedulerManager(IContentSchedulerManager contentSchedulerManager) {
		this._contentSchedulerManager = contentSchedulerManager;
	}

	public IContentManager getContentManager() {
		return contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this.contentManager = contentManager;
	}

	public String getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	public String getMailAttrName() {
		return mailAttrName;
	}

	public void setMailAttrName(String mailAttrName) {
		this.mailAttrName = mailAttrName;
	}

	public boolean isAlsoHtml() {
		return alsoHtml;
	}

	public void setAlsoHtml(boolean alsoHtml) {
		this.alsoHtml = alsoHtml;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHtmlHeader() {
		return htmlHeader;
	}

	public void setHtmlHeader(String htmlHeader) {
		this.htmlHeader = htmlHeader;
	}

	public String getHtmlFooter() {
		return htmlFooter;
	}

	public void setHtmlFooter(String htmlFooter) {
		this.htmlFooter = htmlFooter;
	}

	public String getHtmlSeparator() {
		return htmlSeparator;
	}

	public void setHtmlSeparator(String htmlSeparator) {
		this.htmlSeparator = htmlSeparator;
	}

	public String getTextHeader() {
		return textHeader;
	}

	public void setTextHeader(String textHeader) {
		this.textHeader = textHeader;
	}

	public String getTextFooter() {
		return textFooter;
	}

	public void setTextFooter(String textFooter) {
		this.textFooter = textFooter;
	}

	public String getTextSeparator() {
		return textSeparator;
	}

	public void setTextSeparator(String textSeparator) {
		this.textSeparator = textSeparator;
	}

	public String getHtmlHeaderMove() {
		return htmlHeaderMove;
	}

	public void setHtmlHeaderMove(String htmlHeaderMove) {
		this.htmlHeaderMove = htmlHeaderMove;
	}

	public String getHtmlFooterMove() {
		return htmlFooterMove;
	}

	public void setHtmlFooterMove(String htmlFooterMove) {
		this.htmlFooterMove = htmlFooterMove;
	}

	public String getHtmlSeparatorMove() {
		return htmlSeparatorMove;
	}

	public void setHtmlSeparatorMove(String htmlSeparatorMove) {
		this.htmlSeparatorMove = htmlSeparatorMove;
	}

	public String getTextHeaderMove() {
		return textHeaderMove;
	}

	public void setTextHeaderMove(String textHeaderMove) {
		this.textHeaderMove = textHeaderMove;
	}

	public String getTextFooterMove() {
		return textFooterMove;
	}

	public void setTextFooterMove(String textFooterMove) {
		this.textFooterMove = textFooterMove;
	}

	public String getTextSeparatorMove() {
		return textSeparatorMove;
	}

	public void setTextSeparatorMove(String textSeparatorMove) {
		this.textSeparatorMove = textSeparatorMove;
	}

	private ConfigInterface _baseConfigManager;
	private IContentSchedulerManager _contentSchedulerManager;
	private IContentManager contentManager;

	private String senderCode;
	private String mailAttrName;
	private boolean alsoHtml;

	private String subject;
	private String htmlHeader;
	private String htmlFooter;
	private String htmlSeparator;

	private String textHeader;
	private String textFooter;
	private String textSeparator;

	private String htmlHeaderMove;
	private String htmlFooterMove;
	private String htmlSeparatorMove;

	private String textHeaderMove;
	private String textFooterMove;
	private String textSeparatorMove;

}
