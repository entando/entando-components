/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import static java.util.Arrays.asList;

import java.util.List;

import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

/**
 *
 * @author own_strong
 */
public class BpmBpmProcessDiagramWidgetAction extends SimpleWidgetConfigAction {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BpmBpmCaseInstanceActionsWidgetAction.class);

	private String channel;
	private List<Integer> channels;
	private String frontEndCaseData;

	@Override
	public String init() {
		String result = super.init();
		return result;
	}

	@Override
	protected String extractInitConfig() {
		String result = super.extractInitConfig();

		Widget widget = this.getWidget();
		String frontEndCaseDatain;
		if (widget != null) {

			frontEndCaseDatain = widget.getConfig().getProperty("frontEndCaseData");
			String channel = widget.getConfig().getProperty("channel");
			this.setFrontEndCaseData(frontEndCaseDatain);
			this.setChannel(channel);
			this.setWidgetTypeCode(this.getWidget().getType().getCode());
		} else {
			logger.warn(" widget is null in extraction ");
		}

		return result;
	}

	public String getFrontEndCaseData() {
		return frontEndCaseData;
	}

	public void setFrontEndCaseData(String frontEndCaseData) {
		this.frontEndCaseData = frontEndCaseData;
	}

	public List<Integer> getChannels() {
		return channels = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}

	public void setChannels(List<Integer> channels) {
		this.channels = channels;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
