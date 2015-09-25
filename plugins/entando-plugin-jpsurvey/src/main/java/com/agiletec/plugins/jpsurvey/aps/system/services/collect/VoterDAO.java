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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;

public class VoterDAO extends AbstractSurveyDAO implements IVoterDAO {

	private static final Logger _logger = LoggerFactory.getLogger(VoterDAO.class);
	
	@Override
	public List<Integer> searchVotersId(FieldSearchFilter[] filters) {
		List recordsId = null;
		try {
			recordsId  = super.searchId(filters);
		} catch (Throwable t) {
			throw new RuntimeException("error in searchRecordsId", t);
		}
		return recordsId;
	}
	
	public Voter getVoter(String username, String ipAddress, int surveyId) {
		Voter voter = null;
		Connection conn = null;
		PreparedStatement stat=null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_VOTER_BY_SURVEY);
			stat.setString(1, username);
			stat.setString(2, ipAddress);
			stat.setInt(3, surveyId);
			res = stat.executeQuery();
			voter = this.buildVoterByResultSet(res);
		} catch (Throwable t) {
			_logger.error("Error extracting the voting user by IP {} and idSurvey {}", ipAddress, surveyId,  t);
			throw new RuntimeException("Error extracting the voting user by IP", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return voter;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterDAO#getVoterbyId(int)
	 */
	public Voter getVoterById(int id) {
		Voter voter = null;
		Connection conn = null;
		try {
			conn=this.getConnection();
			voter=this.getVoterById(conn, id);
		} catch (Throwable t) {
			_logger.error("Error getting the voter by ID {}", id,  t);
			throw new RuntimeException("Error getting the voter by ID", t);
		} finally {
			closeConnection(conn);
		}
		return voter;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterDAO#getVoterbyId(java.sql.Connection, int)
	 */
	public Voter getVoterById(Connection conn, int id) {
		Voter voter = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(GET_VOTER_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			voter = this.buildVoterByResultSet(res);
		} catch (Throwable t) {
			_logger.error("Error loading user voter from id {}", id, t);
			throw new RuntimeException("Error loading user voter", t);
		} finally {
			closeDaoResources(res, stat);
		}
		return voter;
	}
	
	private Voter buildVoterByResultSet(ResultSet res) {
		Voter voter = null;
		try {
			if (res.next()) {
				voter = new Voter();
				voter.setId(res.getInt(1)); // 1
				voter.setAge(res.getShort(2)); // 2
				voter.setCountry(res.getString(3)); // 3
				String sex = res.getString(4);
				if (null != sex && sex.length()>0) {
					voter.setSex(sex.charAt(0)); // 4
				}
				voter.setDate(res.getDate(5)); // 5
				voter.setSurveyid(res.getInt(6)); // 6
				voter.setUsername(res.getString(7)); // 7
				voter.setIpaddress(res.getString(8)); // 8	
			}
		} catch (Throwable t) {
			_logger.error("Error loading voting user",  t);
			throw new RuntimeException("Error loading voting user", t);
		}
		return voter;
	}


	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterDAO#saveVoter(com.agiletec.plugins.jpsurvey.aps.system.services.collect.Voter)
	 */
	public void saveVoter(Voter voter) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.saveVoter(conn, voter);
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error saving voting user",  t);
			throw new RuntimeException("Error saving voting user", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterDAO#saveVoter(java.sql.Connection, com.agiletec.plugins.jpsurvey.aps.system.services.collect.Voter)
	 */
	public void saveVoter(Connection conn, Voter voter) {
		PreparedStatement stat= null;
		try {
			String NEXT_ID = "SELECT MAX(id) FROM jpsurvey_voters";
			int selfGeneratedId = this.getUniqueId(NEXT_ID, conn);//this._keyGeneratorManager.getUniqueKeyCurrentValue();
			stat = conn.prepareStatement(SAVE_VOTER);
			voter.setId(selfGeneratedId); 
			stat.setInt(1, voter.getId()); // 1
			if (voter.getAge() > -1) {
				stat.setShort(2, voter.getAge()); // 2
			} else {
				stat.setNull(2, java.sql.Types.SMALLINT); // 2
			}
			if (null != voter.getCountry() && voter.getCountry().length() > 0) {
				stat.setString(3, voter.getCountry().substring(0,2)); // 3
			} else {
				stat.setNull(3, java.sql.Types.VARCHAR); // 3
			}
			
			if (null != voter.getSex()) {
				stat.setString(4, voter.getSex().toString()); // 4
			} else {
				stat.setNull(4, java.sql.Types.VARCHAR); // 4
			}
			stat.setDate(5, new java.sql.Date(voter.getDate().getTime())); // 5
			stat.setInt(6, voter.getSurveyid()); // 6
			stat.setString(7, voter.getUsername()); // 7
			stat.setString(8, voter.getIpaddress()); // 8
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error saving voter",  t);
			throw new RuntimeException("Error saving voter", t);
		} finally  {
			closeDaoResources(null, stat);
		}
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterDAO#deleteVoterById(int)
	 */
	public void deleteVoterById(int id) {
		Connection conn = null;
		PreparedStatement stat=null;
		try {
			conn=this.getConnection();
			stat=conn.prepareStatement(DELETE_VOTER_BY_ID);
			stat.setInt(1, id);
			stat.execute();
		} catch (Throwable t) {
			_logger.error("Errore nella cancellazione dell'utente votante",  t);
			throw new RuntimeException("Errore nella cancellazione dell'utente votante", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void deleteVoterBySurveyId(int surveyId) {
		Connection conn = null;
		try {
			conn=this.getConnection();
			deleteVoterBySurveyId(conn, surveyId);
		} catch (Throwable t) {
			_logger.error("Errore nella cancellazione degli utenti che hanno votato il survey {}", surveyId,  t);
			throw new RuntimeException("Errore nella cancellazione degli utenti che hanno votato il survey", t);
		} finally {
			closeConnection(conn);
		}
	}
	
	public void deleteVoterBySurveyId(Connection conn, int surveyId) {
		PreparedStatement stat=null;
		try {
			stat=conn.prepareStatement(DELETE_VOTER_BY_SURVEY);
			stat.setInt(1, surveyId);
			stat.execute();
		} catch (Throwable t) {
			_logger.error("Error deleting users associated to the survey",  t);
			throw new RuntimeException("Error deleting users associated to the survey", t);
		} finally {
			closeDaoResources(null, stat);			
		}
	}
	
	public List<Integer> searchVotersByIds(Integer id, Integer age, String country, Character sex, Date date, Integer surveyId, String ipAddress) {
		List<Integer> list = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		int idx = 1;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(this.createStatement(id, age, country, sex, date, surveyId, ipAddress));
			if (null != id) {
				stat.setInt(idx++, id); // 1
			}
			if (null != age) {
				stat.setShort(idx++, Integer.valueOf(age).shortValue()); // 2				
			}
			if (null != country && country.length()>0) {
				stat.setString(idx++, country);
			}
			if (null != sex) {
				stat.setString(idx++, String.valueOf(sex)); // 4
			}
			if (null != date) {
				stat.setDate(idx++, new java.sql.Date(date.getTime())); // 5
			}
			if (null != surveyId) {
				stat.setInt(idx++, surveyId); // 6
			}
			if (null != ipAddress && ipAddress.length()<16 && ipAddress.length()>6) {
				stat.setString(idx++, ipAddress); // 7
			}
			res = stat.executeQuery();
			while (res.next()) {
				list.add(res.getInt(1));
			}
		} catch (Throwable t) {
			_logger.error("Error building the list of voter users",  t);
			throw new RuntimeException("Error building the list of voter users", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		if (list.isEmpty()) return null;
		return list;
	}
	
	private String createStatement(Integer id, Integer age, String country, Character sex, Date date,
			Integer surveyId, String ipAddress) {
		StringBuffer query= new StringBuffer(SEARCH_VOTERS_IDS);
		boolean whereInserted = false;
		if (null != id) {
			if (!whereInserted) {
				query.append(" WHERE ");
				whereInserted = true;
			}
			query.append(" id = ? ");
		}
		if (null != age) {
			if (!whereInserted) {
				query.append(" WHERE ");
				whereInserted=true;
			} else {
				query.append(" AND ");
			}
			query.append("age = ?");
		}
		if (null != country) {
			if (!whereInserted) {
				query.append(" WHERE ");
				whereInserted = true;
			} else {
				query.append(" AND ");
			}
			query.append("LOWER(country) = LOWER(?) ");
		}
		if (null != sex) {
			if (!whereInserted) {
				query.append(" WHERE ");
				whereInserted = true;
			} else {
				query.append(" AND ");
			}
			query.append(" LOWER(sex) = LOWER(?) ");
		}
		if (null != date) {
			if (!whereInserted) {
				query.append(" WHERE ");
				whereInserted = true;
			} else {
				query.append(" AND ");
			}
			query.append(" votedate = ? ");
		}
		if (null != surveyId) {
			if (!whereInserted) {
				query.append(" WHERE ");
				whereInserted = true;
			} else {
				query.append(" AND ");
			}
			query.append(" surveyid = ? ");
		}
		if (null != ipAddress && ipAddress.length() < 16 && ipAddress.length() > 6) {
			if (!whereInserted) {
				query.append(" WHERE ");
				whereInserted = true;
			} else {
				query.append(" AND ");
			}
			query.append(" ipaddress = ? ");
		}
		query.append(" ORDER BY ID ");
		return query.toString();
	}
	
	private final String GET_VOTER_BASE_QUERY_BLOCK =
		"SELECT id, age, country, sex, votedate, surveyid, username, ipaddress FROM jpsurvey_voters ";
	
	private final String GET_VOTER_BY_SURVEY =
		GET_VOTER_BASE_QUERY_BLOCK + " WHERE username = ? AND ipaddress = ? AND surveyid = ? ";
	
	private final String GET_VOTER_BY_ID =
		GET_VOTER_BASE_QUERY_BLOCK + " WHERE id = ? ";
	
	private final String SAVE_VOTER =
		"INSERT INTO jpsurvey_voters (id, age, country, sex, votedate, surveyid, username, ipaddress) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? ) ";
	
	private final String DELETE_VOTER_BY_ID	 =
		"DELETE FROM jpsurvey_voters WHERE id = ? ";
	
	private final String DELETE_VOTER_BY_SURVEY	 =
		"DELETE FROM jpsurvey_voters WHERE surveyid = ? ";
	
	private final String SEARCH_VOTERS_IDS = "SELECT id FROM jpsurvey_voters ";
	
}
