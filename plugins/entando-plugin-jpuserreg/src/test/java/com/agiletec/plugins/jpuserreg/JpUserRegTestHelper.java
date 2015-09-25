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