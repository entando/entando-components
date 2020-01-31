/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jacms.aps.system.services.content.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * @author E.Santoboni
 */
public class RowContentListHelper {
	
	public static List<Properties> fromParameterToContents(String contentsParameter) {
		List<Properties> contents = new ArrayList<Properties>();
		if (StringUtils.isBlank(contentsParameter)) {
			return contents;
		}
		String parse1 = (contentsParameter.startsWith("[")) ? contentsParameter.substring(1) : contentsParameter;
		String parse2 = (parse1.endsWith("]")) ? parse1.substring(0, parse1.length()-1) : parse1;
		if (!StringUtils.isBlank(parse2)) {
			String parse11 = (parse2.startsWith("{")) ? parse2.substring(1) : parse2;
			String parse22 = (parse11.endsWith("}")) ? parse11.substring(0, parse11.length()-1) : parse11;
			String[] split = parse22.split(Pattern.quote("},{"));
			for (int i = 0; i < split.length; i++) {
				String[] keysAndValues = split[i].split(",");
				if (keysAndValues.length > 0) {
					Properties properties = new Properties();
					for (int j = 0; j < keysAndValues.length; j++) {
						String[] entry = keysAndValues[j].split("=");
						if (entry.length == 2) {
							properties.put(entry[0].trim(), entry[1].trim());
						}
					}
					contents.add(properties);
				}
			}
		}
		return contents;
	}
	
	public static String fromContentsToParameter(List<Properties> contents) {
		if (null == contents || contents.isEmpty()) {
			return "";
		}
		return contents.toString();
	}
	
}
