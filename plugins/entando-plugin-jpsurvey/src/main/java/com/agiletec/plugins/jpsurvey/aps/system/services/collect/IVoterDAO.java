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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;

public interface IVoterDAO {
	
	public List<Integer> searchVotersId(FieldSearchFilter[] filters);
	
	public Voter getVoter(String username, String ipAddress, int surveyId);
	
	/**
	 * Get a voter given its ID
	 * @param id the unique identifier of the voter
	 * @return the obeject which describes the voter
	 */
	public Voter getVoterById(int id);
	
	/**
	 * Get a voter given its ID.<br />The connection is not closed after an exception 
	 * @param conn a previously opened connetion to the database
	 * @param id the unique identifier of the voter
	 * @return the obeject which describes the voter
	 */
	public Voter getVoterById(Connection conn, int id);
	
	/**
	 * Save the given voter to the database.
	 * @param voter
	 */
	public void saveVoter(Voter voter);
	
	/**
	 * Save the given voter to the database
	 * @param conn a previously opened connetion to the database
	 * @param voter to 'voter' object to save
	 */
	public void saveVoter(Connection conn, Voter voter);
	
	/**
	 * Delete a voter identified by the given ID
	 * @param id
	 */
	public void deleteVoterById(int id);
	
	/**
	 * Delete all the voters which answered the given survey 
	 * @param surveyId
	 */
	public void deleteVoterBySurveyId(int surveyId);
	
	/**
	 * Delete all the voters which answered the given survey 
	 * @param surveyId
	 */
	public void deleteVoterBySurveyId(Connection conn, int surveyId);
	
	/**
	 * Search the users matching the given criteria
	 * @param id
	 * @param age
	 * @param country
	 * @param sex
	 * @param date
	 * @param surveyId
	 * @param ipAddress
	 * @return a list containing the ID of the users found
	 */
	public List<Integer> searchVotersByIds(Integer id, Integer age, String country, Character sex, Date date, Integer surveyId, String ipAddress);
	
}