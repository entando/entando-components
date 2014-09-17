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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * @author E.Santoboni
 */
public class ContentWorkflowDAO extends AbstractDAO implements IContentWorkflowDAO {

	private static final Logger _logger = LoggerFactory.getLogger(ContentWorkflowDAO.class);
	
	@Override
	public List<String> searchUsedSteps(String typeCode) {
		List<String> steps = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_STEPS);
			stat.setString(1, typeCode);
			res = stat.executeQuery();
			while (res.next()) {
				steps.add(res.getString(1));
			}
		} catch (Throwable t) {
			_logger.error("Error searching steps for type {}", typeCode,  t);
			throw new RuntimeException("Error searching steps by type ", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return steps;
	}
	
	private final String SEARCH_STEPS = 
		"SELECT status FROM contents WHERE contenttype = ? GROUP BY status ";
	
}