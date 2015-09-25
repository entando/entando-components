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
package org.entando.entando.plugins.jpweatherforecast.aps.system.services.geoservice;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpweatherforecast.apsadmin.WeatherForecastSystemConstants;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

public class GeoReverseManager extends AbstractService implements IGeoReverseManager {

	@Override
	public void init() throws Exception {
		try {
			LoadConfiguration();
			ApsSystemUtils.getLogger().info(GeoReverseManager.class.getName() + " loaded successfully");
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "init");
		}
	}
	
	@Override
	public Toponym localize(String city, String countryCode) throws ApsSystemException {
		Toponym res = null;
		try {
			WebService.setUserName(this.getUsername());
			ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
			searchCriteria.setQ(city);
			searchCriteria.setCountryCode(countryCode);
			searchCriteria.setMaxRows(1);
			ToponymSearchResult searchResult = WebService.search(searchCriteria);
			if (null != searchResult
					&& null != searchResult.getToponyms()
					&& !searchResult.getToponyms().isEmpty()) {
				res = searchResult.getToponyms().get(0);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "localize", "Error reversing coordinates for '"+ city +"'");
			throw new ApsSystemException("Error reversing coordinates for '"+ city +"'", t);
		}
		return res;
	}
	
	@Override
	public List<Toponym> localizeAll(String city, String countryCode) throws ApsSystemException {
		List<Toponym> list = new ArrayList<Toponym>();
		try {
			WebService.setUserName(this.getUsername());
			ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
			searchCriteria.setQ(city);
			searchCriteria.setCountryCode(countryCode);
			searchCriteria.setMaxRows(1);
			ToponymSearchResult searchResult = WebService.search(searchCriteria);
			if (null != searchResult
					&& null != searchResult.getToponyms()
					&& !searchResult.getToponyms().isEmpty()) {
				list = searchResult.getToponyms();
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "localizeAll", "Error reversing coordinates for '"+ city +"'");
			throw new ApsSystemException("Error reversing coordinates for '"+ city +"'", t);
		}
		return list;
	}
	
	private void LoadConfiguration() {
		String username = this.getConfigManager().getParam(WeatherForecastSystemConstants.PARAM_USERNAME);
		setUsername(username);
		ApsSystemUtils.getLogger().debug("Accessing GeoNames services with user " + username);
	}
	
	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	public String getUsername() {
		return _username;
	}

	public void setUsername(String username) {
		this._username = username;
	}
	
	private ConfigInterface _configManager;
	
	private String _username;
}
