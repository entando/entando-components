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