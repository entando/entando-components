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

import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;

public interface IVoterManager {
	
	public String FIELD_USERNAME = "username";
	public String FIELD_SURVEY_ID = "surveyid";

	public Voter getVoter(String username, String ipAddress, int surveyId) throws ApsSystemException;

	public List<Integer> searchVoters(FieldSearchFilter[] filters) throws ApsSystemException;
	
	/**
	 * Get a voter given its ID
	 * @param id the unique identifier of the voter
	 * @return the obeject which describes the voter
	 */
	public Voter getVoterById(int id) throws ApsSystemException;

	/**
	 * Save the given voter to the database.
	 * @param voter the object to save
	 */
	public void saveVoter(Voter voter) throws ApsSystemException;

	/**
	 * Delete a voter identified by the given ID
	 * @param id
	 */
	public void deleteVoterById(int id) throws ApsSystemException;
	
	/**
	 * Delete all the voters which answered the given survey 
	 * @param surveyId
	 */
	public void deleteVoterBySurveyId(int surveyId) throws ApsSystemException;
	
	/**
	 * Search the users matching the given criteria
	 * 
	 * @param id
	 * @param age
	 * @param country
	 * @param sex
	 * @param date
	 * @param surveyId
	 * @param ipAddress
	 * @return a list containing the ID of the users found
	 */
	public List<Integer> searchVotersByIds(Integer id, Integer age, String country, Character sex, 
			Date date, Integer surveyId, String ipAddress) throws ApsSystemException;

}