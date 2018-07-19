/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.PageDAO;
import com.agiletec.aps.system.services.page.PageExtraConfigDOM;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Data Access Object for the 'page' objects
 * @author E.Santoboni
 */
public class SeoPageDAO extends PageDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoPageDAO.class);

	@Override
	protected PageExtraConfigDOM getExtraConfigDOM() {
		return new SeoPageExtraConfigDOM();
	}
    
    @Override
	protected PageMetadata createPageMetadata(String code, ResultSet res, int startIndex) throws Throwable {
		SeoPageMetadata pageMetadata = new SeoPageMetadata();
		int index = startIndex;
		String titleText = res.getString(index++);
		ApsProperties titles = new ApsProperties();
		try {
			titles.loadFromXml(titleText);
		} catch (Throwable t) {
			_logger.error("IO error detected while parsing the titles of the page {}", code, t);
			String msg = "IO error detected while parsing the titles of the page '" + code + "'";
			throw new ApsSystemException(msg, t);
		}
		pageMetadata.setTitles(titles);
		pageMetadata.setModel(this.getPageModelManager().getPageModel(res.getString(index++)));
		Integer showable = res.getInt(index++);
		pageMetadata.setShowable(showable == 1);
		String extraConfig = res.getString(index++);
		if (null != extraConfig && extraConfig.trim().length() > 0) {
			try {
                SeoPageExtraConfigDOM configDom = new SeoPageExtraConfigDOM();
				configDom.addExtraConfig(pageMetadata, extraConfig);
			} catch (Throwable t) {
				_logger.error("IO error detected while parsing the extra config of the page {}", code, t);
				String msg = "IO error detected while parsing the extra config of the page '" + code + "'";
				throw new ApsSystemException(msg, t);
			}
		}
		Timestamp date = res.getTimestamp(index++);
		pageMetadata.setUpdatedAt(date != null ? new Date(date.getTime()) : null);
		return pageMetadata;
	}

}
