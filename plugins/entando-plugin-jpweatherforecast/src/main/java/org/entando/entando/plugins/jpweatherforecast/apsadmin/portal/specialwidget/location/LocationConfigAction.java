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
package org.entando.entando.plugins.jpweatherforecast.apsadmin.portal.specialwidget.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jpweatherforecast.aps.system.services.geoservice.IGeoReverseManager;
import org.entando.entando.plugins.jpweatherforecast.apsadmin.WeatherForecastSystemConstants;
import org.geonames.Toponym;
import org.slf4j.Logger;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

public class LocationConfigAction extends SimpleWidgetConfigAction {
	
	@Override
	public String init() {
		String result = super.init();
		
		if (result != null 
				&& result.equals(SUCCESS)) {
			Widget widget = this.getWidget();
			
			// load existing configuration
			ApsProperties args = widget.getConfig();
			// restore needed params
			this.setWidgetTypeCode(widget.getType().getCode());
			if (null != args 
					&& !args.isEmpty()) {
				
				String country = args.getProperty(WeatherForecastSystemConstants.WIDGET_PARAM_COUNTRY_CODE);
				String city = args.getProperty(WeatherForecastSystemConstants.WIDGET_PARAM_CITY);
				String latitude = args.getProperty(WeatherForecastSystemConstants.WIDGET_PARAM_LATITUDE);
				String longitude = args.getProperty(WeatherForecastSystemConstants.WIDGET_PARAM_LONGITUDE);
				
				setCountry(country);
				setCity(city);
				setLatitude(latitude);
				setLongitude(longitude);
				
				setStrutsAction(ApsAdminSystemConstants.EDIT);
			} else {
				setStrutsAction(ApsAdminSystemConstants.ADD);
			}
		}
		return result;
	}
	
	/**
	 * Pinpoint location using geoservices
	 * @return
	 */
	public String localize() {
		Toponym coord = null;

		try {
			coord = getGeoManager().localize(getCity(), getCountry());
			if (null != coord) {
				String latitude = String.valueOf(coord.getLatitude());
				String longitude = String.valueOf(coord.getLongitude());
				
				setLatitude(latitude);
				setLongitude(longitude);
			} else {
				ApsSystemUtils.getLogger().debug("Could not pinpoint location " + getCity());
				return "manual";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "localize");
			return "manual";
		}
		return save();
	}
	
	@Override
	public String save() {
		Logger log = ApsSystemUtils.getLogger();
		
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			IPage page = this.getPage(this.getPageCode());
			int strutsAction = (null != page.getWidgets()[this.getFrame()]) ? ApsAdminSystemConstants.ADD : ApsAdminSystemConstants.EDIT;
			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			log.debug("Saving Widget - code = " + this.getWidget().getType().getCode() + 
					", pageCode = " + this.getPageCode() + ", frame = " + this.getFrame());
			this.addActivityStreamInfo(strutsAction, true);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return "configure";
	}
	
	protected void createValuedShowlet() throws Exception {
		Widget widget = this.createNewShowlet();
		List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();

		for (int i=0; i<parameters.size(); i++) {
			WidgetTypeParameter param = parameters.get(i);
			String paramName = param.getName();
			String value = this.getRequest().getParameter(paramName);

			if (value != null 
					&& value.trim().length()>0) {
				widget.getConfig().setProperty(paramName, value);
			}
		}
		
		// add coordinates
		String longitude = getLongitude();
		String latitude = getLatitude();
		
		if (null != longitude
				&& null != latitude
				&& !"".equals(longitude.trim())
				&& !"".equals(latitude.trim())) {
			widget.getConfig().setProperty(WeatherForecastSystemConstants.WIDGET_PARAM_LATITUDE, latitude);
			widget.getConfig().setProperty(WeatherForecastSystemConstants.WIDGET_PARAM_LONGITUDE, longitude);
		}
		this.setShowlet(widget);
	}
	
	/**
	 * Used as a trampoline for manual inseertion
	 * @return
	 */
	public String manualInsertion() {
		return SUCCESS;
	}
	
	public Map<String, String> getAvailableLocales() {
		Map<String, String> langs = new HashMap<String, String>();
		Locale[] locales = Locale.getAvailableLocales();

		for (Locale locale: locales) {
			String code = locale.getCountry();

			if (null != code 
					&& code.length() == 2
					&& !"".equals(code.trim())) {
				langs.put(locale.getCountry(), locale.getDisplayCountry());
//				System.out.println("+ " + locale.getCountry() + " : " + locale.getDisplayCountry());
			}
		}

//		System.out.println("#2");
//		for (Lang lang: list) {
//			langs.put(lang.getCode(), lang.getDescr());
//		}

		return sortLocales(langs);
	}
	
	
	private LinkedHashMap<String, String> sortLocales(Map<String, String> locales) {
		List<String> valueList = new ArrayList<String>(locales.values());
		List<String> keyList = new ArrayList<String>(locales.keySet());
		Collections.sort(valueList);
		Collections.sort(keyList);
		LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();

		Iterator<String> valueItr = valueList.iterator();
		while (valueItr.hasNext()) {
			String curVal = valueItr.next();
			Iterator<String> keyItr = keyList.iterator();

			while (keyItr.hasNext()) {
				String curKey = keyItr.next();
				String comp1 = locales.get(curKey);
				String comp2 = curVal.toString();

				if (comp1.equals(comp2)){
					locales.remove(curKey);
					keyList.remove(curKey);
					sortedMap.put(curKey, curVal);
					break;
				}
			}
		}
		return sortedMap;
	}
	
	public String getCity() {
		return _city;
	}
	
	public void setCity(String city) {
		this._city = city;
	}
	
	public String getCountry() {
		return _country;
	}
	
	public void setCountry(String country) {
		this._country = country;
	}
	
	public String getLatitude() {
		return _latitude;
	}
	public void setLatitude(String latitude) {
		this._latitude = latitude;
	}
	
	public String getLongitude() {
		return _longitude;
	}
	
	public void setLongitude(String longitude) {
		this._longitude = longitude;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}
	
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public IGeoReverseManager getGeoManager() {
		return _geoManager;
	}

	public void setGeoManager(IGeoReverseManager geoManager) {
		this._geoManager = geoManager;
	}
	
	public ILangManager getLangManager() {
		return _langManager;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}


	private String _city;
	private String _country;
	private String _latitude;
	private String _longitude;
	private int _strutsAction;
	
	/* TODO useful to take location from the profile of the current user
	IUserProfileManager profileManager;
	*/
	private IGeoReverseManager _geoManager;
	private ILangManager _langManager;
}
