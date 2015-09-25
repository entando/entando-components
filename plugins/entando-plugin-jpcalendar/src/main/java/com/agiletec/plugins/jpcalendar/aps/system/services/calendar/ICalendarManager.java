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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

import java.util.Calendar;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.EventsOfDayDataBean;

public interface ICalendarManager {

	/**
	 * Restituisce un'array di interi caratterizzante 
	 * abilitati all'utente specificato.
	 * @param requiredMonth Il mese richiesto.
	 * @param user L'utente richiedente.
	 * @return
	 * @throws ApsSystemException 
	 */
	public int[] getEventsForMonth(Calendar requiredMonth, UserDetails user) throws ApsSystemException;
	
	/**
	 * Carica una lista di identificativi di contenuto in base ai valori 
	 * dei campi del bean specificato.
	 * Metodo riservato al EventsOfDayTag.
	 * @param bean Il bean Contenente i dati da erogare.
	 * @return La lista di identificativi di contenuto cercata. 
	 * @throws ApsSystemException
	 */
	public List loadEventsOfDayId(EventsOfDayDataBean bean) throws ApsSystemException;

	/**
	 * Restituisce il primo anno a partire dal quale ci sono eventi.
	 * @return Il primo anno.
	 */
	public int getFirstYear();
	
	public CalendarConfig getConfig();
	
	public void updateConfig(CalendarConfig config) throws ApsSystemException;
	
	public static final String REQUIRED_DATE_PATTERN = "yyyyMMdd";

}