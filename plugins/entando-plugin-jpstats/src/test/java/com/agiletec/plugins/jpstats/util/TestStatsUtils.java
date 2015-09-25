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
package com.agiletec.plugins.jpstats.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;

import javax.sql.DataSource;

import com.agiletec.plugins.jpstats.aps.system.services.stats.IStatsManager;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsManager;
import com.agiletec.plugins.jpstats.aps.util.CalendarConverter;

public class TestStatsUtils {
	
	public static void cleanDB(String ip, DataSource dataSource) throws Throwable {
		String DELETE = "DELETE FROM jpstats_statistics WHERE ip = ? ";
		PreparedStatement prepStat = null;
		Connection conn = dataSource.getConnection();
		prepStat = conn.prepareStatement(DELETE);
		try {
			prepStat.setString(1, ip);
			prepStat.executeUpdate();
		} catch (Throwable t) {
			throw t;
		} finally {
			if (prepStat != null) prepStat.close();
			if (conn != null) conn.close();
		}
	}
	
	public static void cleanTestEnvironment(IStatsManager statsManager) {
		try {
			Calendar start = CalendarConverter.getCalendarDay("01/01/2008", 0, 0, 0, 0);
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_YEAR, 1);
			statsManager.deleteStatsRecord(start.getTime(), end.getTime());
		} catch (Throwable t) {
			throw new RuntimeException("Error cleaning test environment", t);
		}
	}
	
	public static void waitStatsThread() throws InterruptedException {
		Thread[] threads = new Thread[20];
	    Thread.enumerate(threads);
	    for (int i=0; i<threads.length; i++) {
	    	Thread currentThread = threads[i];
	    	if (currentThread != null && 
	    			currentThread.getName().startsWith(StatsManager.RECORDER_THREAD_PREFIX)) {
	    		currentThread.join();
	    	}
	    }
	}
	
}