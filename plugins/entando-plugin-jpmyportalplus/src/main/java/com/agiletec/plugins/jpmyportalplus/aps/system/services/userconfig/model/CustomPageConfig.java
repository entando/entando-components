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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.Frame;

import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
public class CustomPageConfig {

	private static final Logger _logger = LoggerFactory.getLogger(CustomPageConfig.class);
	
	public CustomPageConfig(String pageCode, int frames) {
		this.setPageCode(pageCode);
		this.setConfig(new Widget[frames]);
		this.setStatus(new Integer[frames]);
	}
	
	public CustomPageConfig(Cookie cookie, IPage page, 
			IWidgetTypeManager showletTypeManager, IMyPortalPageModelManager myPortalPageModelManager, 
			Set<String> allowedShowlets, String voidShowletCode) throws ApsSystemException {
		String value;
		try {
			value = URLDecoder.decode(cookie.getValue(),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			value=null;
			_logger.error("Error decoding cookie ", e1);
			//ApsSystemUtils.logThrowable(e1, this, "CustomPageConfig", "Error decoding cookie");
		}
		// FIX temporaneo :: problema escape sui doppi apici
		String tmp = value.replace("\\", "");
		value=tmp;
		try {
			//Frame[] frames = ((MyPortalPageModel) page.getModel()).getFrameConfigs();
			Frame[] frames = page.getModel().getConfiguration();
			String modelCode = page.getModel().getCode();
			Map<Integer, MyPortalFrameConfig> myPortalConfig = myPortalPageModelManager.getPageModelConfig(modelCode);
			int frameNumber = frames.length;
			JSONParser parser = new JSONParser();
			JSONObject positions = (JSONObject) parser.parse(value);
			this.setPageCode(page.getCode());
			this.setConfig(new Widget[frameNumber]);
			this.setStatus(new Integer[frameNumber]);
			for (int i = 0; i < frameNumber; i++) {
				//Frame frame = frames[i];
				//boolean isMyPortalFrame = (frame instanceof MyPortalFrame);
				MyPortalFrameConfig frameConfig = (null != myPortalConfig) ? myPortalConfig.get(i) : null;
				if (null == frameConfig || frameConfig.isLocked()) continue;
				JSONObject frame = (JSONObject) positions.get(String.valueOf(i));
				if (null == frame) continue;
				Object showletCode = frame.get("code");
				if (null == showletCode) continue;
				WidgetType type = showletTypeManager.getWidgetType(showletCode.toString());
				if (null == type) continue;
				Widget showlet = null;
				if (showletCode.equals(voidShowletCode) || allowedShowlets.contains(showletCode) || this.isViewerType(type)) {
					showlet = new Widget();
					showlet.setType(type);
                    /*
					JSONObject showletConfig = (JSONObject) frame.get("config");
					if (null != showletConfig) {
						ApsProperties properties = new ApsProperties();
						Set<String> keys = showletConfig.keySet();
						Iterator<String> keysiter = keys.iterator();
						while (keysiter.hasNext()) {
							String configkey = keysiter.next();
							Object configvalue = showletConfig.get(configkey);
							if (null != configvalue) {
								properties.put(configkey, configvalue.toString());
							}
						}
						showlet.setConfig(properties);
					}
                    */
				}
				if (null != showlet) {
					this.getConfig()[i] = showlet;
					Object status = frame.get("status");
					if (null != status) {
						int statusInt = 0;
						try {
							statusInt = Integer.parseInt(status.toString());
						} catch (NumberFormatException e) {
							//nothing to do
						}
						this.getStatus()[i] = statusInt;
					}
				}
			}
		} catch (ParseException e) {
			_logger.error("Invalid cockie value: {} ", value, e);
		} catch (Throwable t) {
			_logger.error("Error reading the configuration of guest user", t);
			throw new ApsSystemException("Error reading the configuration of guest user", t);
		}
	}

	protected boolean isViewerType(WidgetType type) {
		String action = type.getAction();
		if (null == action || !action.equals("viewerConfig")) return false;
		List<WidgetTypeParameter> params = type.getTypeParameters();
		if (null == params || params.isEmpty()) return false;
		for (int i=0; i<params.size(); i++) {
			WidgetTypeParameter WidgetTypeParameter = params.get(i);
			if (WidgetTypeParameter.getName().equals(SystemConstants.K_CONTENT_ID_PARAM)) {
				return true;
			}
		}
		return false;
	}

	public void update(WidgetUpdateInfoBean[] updateInfos) {
		for (int i = 0; i < updateInfos.length; i++) {
			WidgetUpdateInfoBean infoBean = updateInfos[i];
			int pos = infoBean.getFramePos();
			this.getConfig()[pos] = infoBean.getShowlet();
			this.getStatus()[pos] = infoBean.getStatus();
		}
	}

	public Cookie toCookie(String coockieName) throws Throwable {
		String value = null;
		try {
			JSONObject frames = new JSONObject();
			Widget[] showlets = this.getConfig();
			for (int i = 0; i < showlets.length; i++) {
				Widget showlet = showlets[i];
				if (null == showlet) continue;
				JSONObject frame = new JSONObject();
				frame.put("code", showlet.getType().getCode());
                                /*
				ApsProperties properties = showlet.getConfig();
				if (properties != null && !properties.isEmpty()) {
					JSONObject configFrame = new JSONObject();
					Set<Object> keys = properties.keySet();
					Iterator<Object> keysIter = keys.iterator();
					while (keysIter.hasNext()) {
						Object configkey = keysIter.next();
						Object configValue = properties.get(configkey);
						if (null != configValue) {
							configFrame.put(configkey.toString(), configValue.toString());
						}
					}
					frame.put("config", configFrame);
				} else {
                                */
					frame.put("config", null);
				/*
                                }
                                */
				Integer status = this.getStatus()[i];
				if (status != null && status.intValue()==1) {
					frame.put("status", 1);
				} else {
					frame.put("status", 0);
				}
				frames.put(i, frame);
			}
			value = frames.toJSONString();
			//value = "{\"23\": {\"status\": 1,\"config\": {\"contentType\": \"NEW\"},\"code\": \"latest_news\"},\"23\": {\"status\": 1,\"config\": {\"contentType\": \"NEW\"},\"code\": \"latest_news\"},\"23\": {\"status\": 1,\"config\": {\"contentType\": \"NEW\"},\"code\": \"latest_news\"},\"23\": {\"status\": 1,\"config\": {\"contentType\": \"NEW\"},\"code\": \"latest_news\"},\"23\": {\"status\": 1,\"config\": {\"contentType\": \"NEW\"},\"code\": \"latest_news\"}}";
		} catch (Throwable t) {
			_logger.error("Error building the showlet array to render", t);
			throw new ApsSystemException("Error building the showlet array to render", t);
		}
		Cookie cookie = null;
		if (null != value) {
			cookie = new Cookie(coockieName,  URLEncoder.encode(value, "UTF-8"));
			cookie.setMaxAge(365*24*60*60);//one year
			cookie.setPath("/");
		}
		return cookie;
	}

	public String getPageCode() {
		return _pageCode;
	}
	protected void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}

	public Widget[] getConfig() {
		return _config;
	}
	protected void setConfig(Widget[] config) {
		this._config = config;
	}

	public Integer[] getStatus() {
		return _status;
	}
	protected void setStatus(Integer[] status) {
		this._status = status;
	}

	private String _pageCode;

	private Widget[] _config;
	private Integer[] _status;

}
