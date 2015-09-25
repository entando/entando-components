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
package com.agiletec.plugins.jpstats.apsadmin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpstats.aps.system.services.stats.IStatsManager;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsRecord;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.VisitsStat;
import com.agiletec.plugins.jpstats.aps.util.HitsTDP;
import com.agiletec.plugins.jpstats.aps.util.StatsDataBean;
import com.agiletec.plugins.jpstats.aps.util.TopContentsCDP;
import com.agiletec.plugins.jpstats.aps.util.TopPagesCDP;

import de.laures.cewolf.DatasetProducer;

public class StatisticsAction extends BaseAction implements IStatisticsAction {
	
	@Override
	public String viewStatistics() {
		if (this.getSelectedTypes() == null || this.getSelectedTypes().size() == 0) {
			this.addActionError(this.getText("jpstats.graphicType.required"));
			return INPUT;
		}
		this.getStartDate();
		this.getEndDate();
		if (this.getStartDate().compareTo(this.getEndDate())>0) {
			this.addFieldError("start", this.getText("jpstats.Message.invalidDateRange"));
			return INPUT;
		}
		return SUCCESS;
	}
	
	@Override
	public String trash() {
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			this.getStatsManager().deleteStatsRecord(this.getStartDate(), this.getEndDate());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String report() {
		return SUCCESS;
	}
	
	public List<StatsRecord> getStatsRecords() {
		List<StatsRecord> records = new ArrayList<StatsRecord>();
		try {
			records = this.getStatsManager().loadStatsRecord(this.getStartDate(), this.getEndDate());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getStatsRecords");
			throw new RuntimeException("Errore in estrazione Records", t);
		}
		return records;
	}
	
	public DatasetProducer getHitsTimeData() {
		return new HitsTDP(this.getStatsManager(), this.getStatsDataBean());
	}
	
	public DatasetProducer getMostVisitedPagestimeData() {
		return new TopPagesCDP(this.getStatsManager(), this.getStatsDataBean());
	}
	
	public DatasetProducer getTopContentsDataset() {
		return new TopContentsCDP(this.getStatsManager(), this.getStatsDataBean());
	}
	
	public int getIpByDateInterval() {
		try {
			return this.getStatsManager().getIPByDateInterval(this.getStartDay(), this.getEndDay());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getIpByDateInterval");
			throw new RuntimeException("Error loading number of ip addresses in interval", t);
		}
	}
	
	public String getAverageTimePage() {
		try {
			return this.getStatsManager().getAverageTimePage(this.getStartDay(), this.getEndDay());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAverageTimePage");
			throw new RuntimeException("Error loading average time in pages", t);
		}
	}
	
	public String getAverageTimeSite() {
		try {
			return this.getStatsManager().getAverageTimeSite(this.getStartDay(), this.getEndDay());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAverageTimeSite");
			throw new RuntimeException("Error loading average time in site", t);
		}
	}
	
	public int getNumPageSession() {
		try {
			return this.getStatsManager().getNumPageSession(this.getStartDay(), this.getEndDay());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getNumPageSession");
			throw new RuntimeException("Error loading number of pages for session", t);
		}
	}
	
	public List<VisitsStat> getVisitsForDate() {
		try {
			List<VisitsStat> statistics = this.getStatsManager().searchVisitsForDate(this.getStartDate(), this.getEndDate());
			Collections.sort(statistics);
			return statistics;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVisitsForDate");
			throw new RuntimeException("Error loading visits for date", t);
		}
	}
	
	public List<VisitsStat> getVisitsForPages() {
		try {
			List<VisitsStat> statistics = this.getStatsManager().searchVisitsForPages(this.getStartDate(), this.getEndDate());
			Collections.sort(statistics);
			return statistics;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVisitsForPages");
			throw new RuntimeException("Error loading visits for pages", t);
		}
	}
	
	public List<VisitsStat> getVisitsForContents() {
		try {
			List<VisitsStat> statistics = this.getStatsManager().searchVisitsForContents(this.getStartDate(), this.getEndDate());
			Collections.sort(statistics);
			return statistics;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVisitsForContents");
			throw new RuntimeException("Error loading visits for contents", t);
		}
	}
	
	public String getBaseDirectory() {
		String baseDirectory = this.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/plugins/jpstats/report/").concat("/");
		return baseDirectory;
	}
	
	public Calendar getEndDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.getEndDate());
		return calendar;
	}
	
	public Calendar getStartDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.getStartDate());
		return calendar;
	}
	
	public JRDataSource getDataSource() {
		return new JREmptyDataSource();
//		List l = new ArrayList();
//		l.add("");
//		return l;
	}
	
	private StatsDataBean getStatsDataBean() {
		if (null==this._statsDataBean) {
			this._statsDataBean = new StatsDataBean();
			this._statsDataBean.setStart(this.getStartDay());
			this._statsDataBean.setEnd(this.getEndDay());
		}
		return this._statsDataBean;
	}
	
	private void prepareEndDate() {
		if (this._endDate==null) {
			Date date = null;
			if (this._end!=null && this._end.trim().length()>0) {
				date = DateConverter.parseDate(this._end.trim(), "dd/MM/yyyy");
			}
			if (date==null) {
				date = new Date();
				this._end = DateConverter.getFormattedDate(date, "dd/MM/yyyy");
			} else {
				date = new Date(date.getTime()+86400000-1);
			}
			this._endDate = date;
		}
	}
	
	private void prepareStartDate() {
		if (this._startDate==null) {
			Date date = null;
			if (this._start!=null && this._start.trim().length()>0) {
				date = DateConverter.parseDate(this._start.trim(), "dd/MM/yyyy");
			}
			if (date==null) {
				date = new Date(new Date().getTime() - 604800000l);
				this._start = DateConverter.getFormattedDate(date, "dd/MM/yyyy");
				date = DateConverter.parseDate(this._start.trim(), "dd/MM/yyyy");
			}
			this._startDate = date;
		}
	}
	
	public String getStart() {
		this.prepareStartDate();
		return _start;
	}
	public void setStart(String start) {
		this._start= start;
	}
	public Date getStartDate() {
		this.prepareStartDate();
		return _startDate;
	}
	
	public String getEnd() {
		this.prepareEndDate();
		return _end;
	}
	public void setEnd(String end) {
		this._end = end;
	}
	public Date getEndDate() {
		this.prepareEndDate();
		return _endDate;
	}
	
	public IStatsManager getStatsManager() {
		return _statsManager;
	}
	public void setStatsManager(IStatsManager statsManager) {
		this._statsManager = statsManager;
	}
	
	public void setGraphicType(Map<String, String> graphicType) {
		this._graphicType = graphicType;
	}
	public Map<String, String> getGraphicType() {
		return _graphicType;
	}

	public void setSelectedTypes(List<String> selectedTypes) {
		this._selectedTypes = selectedTypes;
	}
	public List<String> getSelectedTypes() {
		return _selectedTypes;
	}

	private Map<String, String> _graphicType;
	private List<String> _selectedTypes;
	
	private String _end;
	private String _start;
	
	private Date _startDate;
	private Date _endDate;
	
	private IStatsManager _statsManager;
	
	private StatsDataBean _statsDataBean;
	
}