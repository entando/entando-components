/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.model;

import java.util.Map;

public class WttConfig {
	
	public Map<Integer, InterventionType> getInterventionTypes() {
		return _interventionTypes;
	}
	public void setInterventionTypes(Map<Integer, InterventionType> interventionTypes) {
		this._interventionTypes = interventionTypes;
	}
	public InterventionType getInterventionType(Integer id) {
		return _interventionTypes.get(id);
	}
	
	public Map<Integer, String> getPriorities() {
		return _priorities;
	}
	public void setPriorities(Map<Integer, String> priorities) {
		this._priorities = priorities;
	}
	
	private Map<Integer, InterventionType> _interventionTypes;
	private Map<Integer, String> _priorities;
	
}