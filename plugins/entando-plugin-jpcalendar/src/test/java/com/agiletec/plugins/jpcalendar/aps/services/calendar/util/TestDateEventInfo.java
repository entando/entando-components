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
package com.agiletec.plugins.jpcalendar.aps.services.calendar.util;

import java.util.Calendar;
import java.util.Date;

import com.agiletec.plugins.jpcalendar.aps.ApsPluginBaseTestCase;

import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.DateEventInfo;

public class TestDateEventInfo extends ApsPluginBaseTestCase {
	
	public void testIsValidEvent_MoveEnd_1() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		Calendar startEvent = Calendar.getInstance();
		startEvent.set(Calendar.YEAR, 1999);
		startEvent.set(Calendar.MONTH, Calendar.JANUARY);
		startEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setStart(startEvent.getTime());

		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.FEBRUARY);
		endEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setEnd(endEvent.getTime());

		assertFalse(info.isValidEvent());

		endEvent.set(Calendar.MONTH, Calendar.MAY);
		endEvent.set(Calendar.DAY_OF_MONTH, 1);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(1, info.getEndDay());

		endEvent.set(Calendar.MONTH, Calendar.MAY);
		endEvent.set(Calendar.DAY_OF_MONTH, 8);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(8, info.getEndDay());

		endEvent.set(Calendar.MONTH, Calendar.SEPTEMBER);
		endEvent.set(Calendar.DAY_OF_MONTH, 8);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());

		endEvent.set(Calendar.MONTH, Calendar.JANUARY);
		endEvent.set(Calendar.DAY_OF_MONTH, 8);
		endEvent.set(Calendar.YEAR, 2000);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());
	}

	public void testIsValidEvent_MoveEnd_2() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.APRIL);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		Calendar startEvent = Calendar.getInstance();
		startEvent.set(Calendar.YEAR, 1999);
		startEvent.set(Calendar.MONTH, Calendar.APRIL);
		startEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setStart(startEvent.getTime());

		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.APRIL);
		endEvent.set(Calendar.DAY_OF_MONTH, 29);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(12, info.getStartDay());
		assertEquals(29, info.getEndDay());

		endEvent.set(Calendar.MONTH, Calendar.APRIL);
		endEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(12, info.getStartDay());
		assertEquals(12, info.getEndDay());

		endEvent.set(Calendar.MONTH, Calendar.APRIL);
		endEvent.set(Calendar.DAY_OF_MONTH, 8);
		info.setEnd(endEvent.getTime());

		assertFalse(info.isValidEvent());

		endEvent.set(Calendar.MONTH, Calendar.JANUARY);
		endEvent.set(Calendar.DAY_OF_MONTH, 8);
		info.setEnd(endEvent.getTime());

		assertFalse(info.isValidEvent());

	}

	public void testIsValidEvent_NullEnd_2() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		Calendar startEvent = Calendar.getInstance();
		startEvent.set(Calendar.YEAR, 1999);
		startEvent.set(Calendar.MONTH, Calendar.JANUARY);
		startEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setStart(startEvent.getTime());

		info.setEnd(null);
		assertFalse(info.isValidEvent());
	}

	public void testIsValidEvent_NullEnd_3() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		Calendar startEvent = Calendar.getInstance();
		startEvent.set(Calendar.YEAR, 1999);
		startEvent.set(Calendar.MONTH, Calendar.MAY);
		startEvent.set(Calendar.DAY_OF_MONTH, 1);
		info.setStart(startEvent.getTime());

		info.setEnd(null);
		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(1, info.getEndDay());
	}

	public void testIsValidEvent_NullEnd_4() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		Calendar startEvent = Calendar.getInstance();
		startEvent.set(Calendar.YEAR, 1999);
		startEvent.set(Calendar.MONTH, Calendar.MAY);
		startEvent.set(Calendar.DAY_OF_MONTH, startEvent.getActualMaximum(Calendar.DAY_OF_MONTH));
		info.setStart(startEvent.getTime());

		info.setEnd(null);
		assertTrue(info.isValidEvent());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());
	}

	public void testIsValidEvent_NullEnd_5() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		Calendar startEvent = Calendar.getInstance();
		startEvent.set(Calendar.YEAR, 1999);
		startEvent.set(Calendar.MONTH, Calendar.SEPTEMBER);
		startEvent.set(Calendar.DAY_OF_MONTH, 25);
		info.setStart(startEvent.getTime());

		info.setEnd(null);
		assertFalse(info.isValidEvent());
	}

	public void testIsValidEvent_MoveStart_6() {
		Calendar requiredMonth = this.getRequiredMonth(1998, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		Calendar startEvent = Calendar.getInstance();
		startEvent.set(Calendar.YEAR, 1999);
		startEvent.set(Calendar.MONTH, Calendar.JANUARY);
		startEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setStart(startEvent.getTime());

		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.FEBRUARY);
		endEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setEnd(endEvent.getTime());

		assertFalse(info.isValidEvent());

		startEvent.set(Calendar.YEAR, 1998);
		startEvent.set(Calendar.MONTH, Calendar.JUNE);
		startEvent.set(Calendar.DAY_OF_MONTH, 1);
		info.setStart(startEvent.getTime());

		assertFalse(info.isValidEvent());

		startEvent.set(Calendar.YEAR, 1998);
		startEvent.set(Calendar.MONTH, Calendar.MAY);
		startEvent.set(Calendar.DAY_OF_MONTH, startEvent.getActualMaximum(Calendar.DAY_OF_MONTH));
		info.setStart(startEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());

		startEvent.set(Calendar.YEAR, 1998);
		startEvent.set(Calendar.MONTH, Calendar.MAY);
		startEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setStart(startEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(12, info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());

		startEvent.set(Calendar.YEAR, 1998);
		startEvent.set(Calendar.MONTH, Calendar.MAY);
		startEvent.set(Calendar.DAY_OF_MONTH, 1);
		info.setStart(startEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());

		startEvent.set(Calendar.YEAR, 1998);
		startEvent.set(Calendar.MONTH, Calendar.APRIL);
		startEvent.set(Calendar.DAY_OF_MONTH, startEvent.getActualMaximum(Calendar.DAY_OF_MONTH));
		info.setStart(startEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());

		startEvent.set(Calendar.YEAR, 1997);
		startEvent.set(Calendar.MONTH, Calendar.SEPTEMBER);
		startEvent.set(Calendar.DAY_OF_MONTH, 23);
		info.setStart(startEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());
	}

	public void testIsValidEvent_NullStart_7() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		info.setStart(null);

		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.JANUARY);
		endEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setEnd(endEvent.getTime());

		assertFalse(info.isValidEvent());
	}
	
	public void testIsValidEvent_NullStart_8() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		info.setStart(null);
		
		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.DAY_OF_MONTH, endEvent.getActualMinimum(Calendar.DAY_OF_MONTH));
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.APRIL);
		endEvent.set(Calendar.DAY_OF_MONTH, endEvent.getActualMaximum(Calendar.DAY_OF_MONTH));
		info.setEnd(endEvent.getTime());

		assertFalse(info.isValidEvent());
	}
	
	public void testIsValidEvent_NullStart_9() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		info.setStart(null);

		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.MAY);
		endEvent.set(Calendar.DAY_OF_MONTH, 1);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(1, info.getStartDay());
		assertEquals(1, info.getEndDay());
	}

	public void testIsValidEvent_NullStart_10() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.MAY);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		info.setStart(null);

		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.MAY);
		endEvent.set(Calendar.DAY_OF_MONTH, 15);
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(15, info.getStartDay());
		assertEquals(15, info.getEndDay());
	}
	
	public void testIsValidEvent_NullStart_11() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.APRIL);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);
		
		info.setStart(null);
		
		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.DAY_OF_MONTH, endEvent.getActualMinimum(Calendar.DAY_OF_MONTH));
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.APRIL);
		endEvent.set(Calendar.DAY_OF_MONTH, endEvent.getActualMaximum(Calendar.DAY_OF_MONTH));
		info.setEnd(endEvent.getTime());

		assertTrue(info.isValidEvent());
		assertEquals(endEvent.getActualMaximum(Calendar.DAY_OF_MONTH), info.getStartDay());
		assertEquals(endEvent.getActualMaximum(Calendar.DAY_OF_MONTH), info.getEndDay());
	}
	
	public void testIsValidEvent_NullStart_12() {
		Calendar requiredMonth = this.getRequiredMonth(1999, Calendar.APRIL);
		DateEventInfo info = this.getDateEventInfoForTest(requiredMonth);

		info.setStart(null);

		Calendar endEvent = Calendar.getInstance();
		endEvent.set(Calendar.YEAR, 1999);
		endEvent.set(Calendar.MONTH, Calendar.MAY);
		endEvent.set(Calendar.DAY_OF_MONTH, 12);
		info.setEnd(endEvent.getTime());

		assertFalse(info.isValidEvent());
	}
	
	private DateEventInfo getDateEventInfoForTest(Calendar requiredMonth) {
		Date firstDayOfReqMonth = this.getFirstDay(requiredMonth);
		Date lastDayOfReqMonth = this.getLastDay(requiredMonth);

		DateEventInfo info = new DateEventInfo(firstDayOfReqMonth, lastDayOfReqMonth);
		return info;
	}

	private Calendar getRequiredMonth(int year, int month) {
		Calendar requiredMonth = Calendar.getInstance();
		requiredMonth.set(Calendar.YEAR, year);
		requiredMonth.set(Calendar.MONTH, month);
		return requiredMonth;
	}
	
	private Date getFirstDay(Calendar requiredMonth) {
		Calendar first = (Calendar) requiredMonth.clone();
		first.set(Calendar.DAY_OF_MONTH, requiredMonth.getActualMinimum(Calendar.DAY_OF_MONTH));
		return first.getTime();
	}
	
	private Date getLastDay(Calendar requiredMonth) {
		Calendar last = (Calendar) requiredMonth.clone();
		last.set(Calendar.DAY_OF_MONTH, requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
		return last.getTime();
	}
	
}