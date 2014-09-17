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