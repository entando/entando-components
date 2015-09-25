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
package com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialwidget.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FacetNavWidgetHelper {
	
	/**
	 * Returns concatenated string.
	 * @param values
	 * @param separator
	 * @return concatenated string.
	 */
	public static String concatStrings(List<String> values, String separator) {
		StringBuffer concatedValues = new StringBuffer();
		Iterator<String> valuesIter = values.iterator();
		if (valuesIter.hasNext()) {
			concatedValues.append(valuesIter.next());
			while (valuesIter.hasNext()) {
				concatedValues.append(separator);
				concatedValues.append(valuesIter.next());
			}
		}
		return concatedValues.toString();
	}
	
	/**
	 * Extracts from a string, the substrings contained in it as a separator character.
	 * @param values The string containing the values concatenated.
	 * @param separator The separator character.
	 * @return The list of strings extracted from the parameter values.
	 */
	public static List<String> splitValues(String values, String separator) {
		List<String> valuesList = new ArrayList<String>();
		if (values!=null && values.length()>0) {
			String[] splittedValues = values.split(separator);
			for (int i=0; i<splittedValues.length; i++) {
				if (splittedValues[i].length()>0) {
					valuesList.add(splittedValues[i]);
				}
			}
		}
		return valuesList;
	}
	
}