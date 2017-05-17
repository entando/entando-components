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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContentThreadConfig {

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		this._active = active;
	}

	public String getGlobalCat() {
		return _globalCat;
	}

	public void setGlobalCat(String _globalCat) {
		this._globalCat = _globalCat;
	}

	public String getContentIdRepl() {
		return contentIdRepl;
	}

	public void setContentIdRepl(String contentIdRepl) {
		this.contentIdRepl = contentIdRepl;
	}

	public String getContentModelRepl() {
		return contentModelRepl;
	}

	public void setContentModelRepl(String contentModelRepl) {
		this.contentModelRepl = contentModelRepl;
	}

	public String getSenderCode() {
		return _senderCode;
	}

	public void setSenderCode(String senderCode) {
		this._senderCode = senderCode;
	}

	public String getMailAttrName() {
		return _mailAttrName;
	}

	public void setMailAttrName(String mailAttrName) {
		this._mailAttrName = mailAttrName;
	}

	public boolean isAlsoHtml() {
		return _alsoHtml;
	}

	public void setAlsoHtml(boolean alsoHtml) {
		this._alsoHtml = alsoHtml;
	}

	public String getSubject() {
		return _subject;
	}

	public void setSubject(String subject) {
		this._subject = subject;
	}

	public String getHtmlHeader() {
		return _htmlHeader;
	}

	public void setHtmlHeader(String htmlHeader) {
		this._htmlHeader = htmlHeader;
	}

	public String getHtmlFooter() {
		return _htmlFooter;
	}

	public void setHtmlFooter(String htmlFooter) {
		this._htmlFooter = htmlFooter;
	}

	public String getHtmlSeparator() {
		return _htmlSeparator;
	}

	public void setHtmlSeparator(String htmlSeparator) {
		this._htmlSeparator = htmlSeparator;
	}

	public String getHtmlHeaderMove() {
		return _htmlHeaderMove;
	}

	public void setHtmlHeaderMove(String _htmlHeaderMove) {
		this._htmlHeaderMove = _htmlHeaderMove;
	}

	public String getHtmlFooterMove() {
		return _htmlFooterMove;
	}

	public void setHtmlFooterMove(String _htmlFooterMove) {
		this._htmlFooterMove = _htmlFooterMove;
	}

	public String getHtmlSeparatorMove() {
		return _htmlSeparatorMove;
	}

	public void setHtmlSeparatorMove(String _htmlSeparatorMove) {
		this._htmlSeparatorMove = _htmlSeparatorMove;
	}

	public String getTextHeaderMove() {
		return _textHeaderMove;
	}

	public void setTextHeaderMove(String _textHeaderMove) {
		this._textHeaderMove = _textHeaderMove;
	}

	public String getTextFooterMove() {
		return _textFooterMove;
	}

	public void setTextFooterMove(String _textFooterMove) {
		this._textFooterMove = _textFooterMove;
	}

	public String getTextSeparatorMove() {
		return _textSeparatorMove;
	}

	public void setTextSeparatorMove(String _textSeparatorMove) {
		this._textSeparatorMove = _textSeparatorMove;
	}

	public String getTextHeader() {
		return _textHeader;
	}

	/**
	 * Imposta l'header della mail in testo semplice.
	 * 
	 * @param textHeader
	 *            L'header della mail in testo semplice.
	 */
	public void setTextHeader(String textHeader) {
		this._textHeader = textHeader;
	}

	/**
	 * Restituisce il footer della mail in testo semplice.
	 * 
	 * @return Il footer della mail in testo semplice.
	 */
	public String getTextFooter() {
		return _textFooter;
	}

	/**
	 * Imposta il footer della mail in testo semplice.
	 * 
	 * @param textFooter
	 *            Il footer della mail in testo semplice.
	 */
	public void setTextFooter(String textFooter) {
		this._textFooter = textFooter;
	}

	public String getTextSeparator() {
		return _textSeparator;
	}

	public void setTextSeparator(String textSeparator) {
		this._textSeparator = textSeparator;
	}

	/**
	 * Imposta il codice dell'istanza abilitata all'invio del job di
	 * pubblicazione/sospensione automatica
	 *
	 * @param sitecode
	 */
	public void setSitecode(String sitecode) {
		this._sitecode = sitecode;
	}

	/**
	 * Restituisce il codice dell'istanza abilitata all'invio del job di
	 * pubblicazione/sospensione automatica
	 *
	 * @return
	 */
	public String getSitecode() {
		return _sitecode;
	}

	public Map<String, List<String>> getGroupsContentType() {
		return groupsContentType;
	}

	public void setGroupsContentType(Map<String, List<String>> groupsContentType) {
		this.groupsContentType = groupsContentType;
	}

	public Map<String, List<String>> getUsersContentType() {
		return usersContentType;
	}

	public void setUsersContentType(Map<String, List<String>> usersContentType) {
		this.usersContentType = usersContentType;
	}

	public List<ContentTypeElem> getTypesList() {
		return _typesList;
	}

	public void setTypesList(List<ContentTypeElem> list) {
		_typesList = list;
	}

	// scheduler
	private boolean _active = false;

	// cat globale
	private String _globalCat;

	// contenuto sostitutivo
	private String contentIdRepl;
	private String contentModelRepl;

	// gruppi
	Map<String, List<String>> groupsContentType;
	// utenti
	Map<String, List<String>> usersContentType;

	// mail config
	private String _senderCode;
	private String _mailAttrName;
	private boolean _alsoHtml;

	// template
	private String _subject;
	private String _htmlHeader = "";
	private String _htmlFooter = "";
	private String _htmlSeparator = "";

	private String _textHeader = "";
	private String _textFooter = "";
	private String _textSeparator = "";

	private String _htmlHeaderMove = "";
	private String _htmlFooterMove = "";
	private String _htmlSeparatorMove = "";

	private String _textHeaderMove = "";
	private String _textFooterMove = "";
	private String _textSeparatorMove = "";

	private String _sitecode;

	// tipi di contenuto da utilizzare
	private List<ContentTypeElem> _typesList = new ArrayList<ContentTypeElem>();

}
