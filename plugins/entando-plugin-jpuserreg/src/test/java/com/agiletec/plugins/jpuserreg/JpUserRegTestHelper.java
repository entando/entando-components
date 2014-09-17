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
package com.agiletec.plugins.jpuserreg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.UserRegDAO;

/**
 * @author G.Cocco
 * */
public class JpUserRegTestHelper extends UserRegDAO {
	
	public String getTokenFromUsername(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String token = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_TOKEN_FROM_USERNAME);
			stat.setString(1,username);
			res = stat.executeQuery();
			if (res.next()) {
				token = res.getString("token");
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting token from Username", "getUsernameFromToken");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return token;
	}
	
	private static final String GET_TOKEN_FROM_USERNAME = "SELECT token FROM jpuserreg_activationtokens WHERE username = ?";
	
	//	Config with a valid email address
	public static final String EMAIL = "agiletectest@gmail.com";
	
}