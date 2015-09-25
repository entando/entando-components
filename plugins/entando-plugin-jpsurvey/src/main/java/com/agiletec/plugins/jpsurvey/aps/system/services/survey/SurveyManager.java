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
package com.agiletec.plugins.jpsurvey.aps.system.services.survey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceUtilizer;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;

public class SurveyManager extends AbstractService implements ISurveyManager, GroupUtilizer, ResourceUtilizer {

	private static final Logger _logger = LoggerFactory.getLogger(SurveyManager.class);
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().debug(this.getClass().getName() + ": initiated ");
	}
	
	@Override
	public Survey loadSurvey(int id) throws ApsSystemException {
		try {
			return this.getSurveyDAO().loadSurvey(id);
		} catch (Throwable t) {
			_logger.error("Error while loading a complete survey", t);
			throw new ApsSystemException("Error while loading a complete survey", t);
		}
	}
	
	@Override
	public void saveSurvey(Survey survey) throws ApsSystemException {
		try {
			this.getSurveyDAO().saveSurvey(survey);
		} catch (Throwable t) {
			_logger.error("Error while saving a complete survey", t);
			throw new ApsSystemException("Error while saving a complete survey", t);
		}
	}
	
	@Override
	public void deleteSurvey(int id) throws ApsSystemException {
		try {
			this.getSurveyDAO().deleteSurvey(id);
		} catch (Throwable t) {
			_logger.error("Error while deleting a complete survey", t);
			throw new ApsSystemException("Error while deleting a complete survey", t);
		}
	}
	
	@Override
	public Question loadQuestion(int id) throws ApsSystemException {
		try {
			return this.getQuestionDAO().loadQuestion(id);
		} catch (Throwable t) {
			_logger.error("Error while loading a question with its choices", t);
			throw new ApsSystemException("Error while loading a question with its choices", t);
		}
	}
	
	@Override
	public Choice loadChoice(int id) throws ApsSystemException {
		try {
			return this.getChoiceDAO().loadChoice(id);
		} catch (Throwable t) {
			_logger.error("Error while loading a choice", t);
			throw new ApsSystemException("Error while loading a choice", t);
		}
	}
	
	@Override
	public void saveChoice(Choice choice) throws ApsSystemException {
		try {
			this.getChoiceDAO().saveChoice(choice);
		} catch (Throwable t) {
			_logger.error("Error while saving a choice", t);
			throw new ApsSystemException("Error while saving a choice", t);
		}
	}
	
	@Override
	public void saveChoiceInSortedPosition(Choice choice) throws ApsSystemException {
		try {
			this.getChoiceDAO().saveChoiceInSortedPosition(choice);
		} catch (Throwable t) {
			_logger.error("Error while saving a choice in sorted position", t);
			throw new ApsSystemException("Error while saving a choice in sorted position", t);
		}
	}
	
	@Override
	public void deleteChoice(int id) throws ApsSystemException {
		try {
			this.getChoiceDAO().deleteChoice(id);
		} catch (Throwable t) {
			_logger.error("Error while deleting the choice", t);
			throw new ApsSystemException("Error while deleting the choice", t);
		}
	}
	
	@Override
	public List<Choice> getQuestionChoices(int id) throws ApsSystemException {
		List<Choice> choices = null;
		try {
			choices = this.getQuestionDAO().getQuestionChoices(id);
		} catch (Throwable t) {
			_logger.error("Error while getting the choices of a question", t);
			throw new ApsSystemException("Error while getting the choices of a question", t);
		}
		return choices;
	}
	
	@Override
	public List<Question> getSurveyQuestions(int id) throws ApsSystemException {
		List<Question> questions = null;
		try {
			questions = this.getSurveyDAO().getSurveyQuestions(id);
		} catch (Throwable t) {
			_logger.error("Error while getting the questions of a survey", t);
			throw new ApsSystemException("Error while getting the questions of a survey", t);
		}
		return questions;
	}
	
	@Override
	public void saveQuestion(Question question) throws ApsSystemException {
		try {
			this.getQuestionDAO().saveQuestion(question);
		} catch (Throwable t) {
			_logger.error("Error while saving a question", t);
			throw new ApsSystemException("Error while saving a question", t);
		}
	}
	
	@Override
	public void deleteQuestion(int id) throws ApsSystemException{
		try {
			this.getQuestionDAO().deleteQuestion(id);
		} catch (Throwable t) {
			_logger.error("Error while deleting a question", t);
			throw new ApsSystemException("Error while deleting a question", t);
		}
	}
	
	@Override
	public void deleteChoiceByQuestionId(int id) throws ApsSystemException{
		try {
			this.getChoiceDAO().deleteChoiceByQuestionId(id);
		} catch (Throwable t) {
			_logger.error("Error while deleting choices by their question ID", t);
			throw new ApsSystemException("Error while deleting choices by their question ID ", t);
		}
	}
	
	@Override
	public void updateChoice(Choice choice) throws ApsSystemException {
		try {
			this.getChoiceDAO().updateChoice(choice);
		} catch (Throwable t) {
			_logger.error("Error while updating a choice", t);
			throw new ApsSystemException("Error while updating a choice", t);
		}
	}
	
	@Override
	public void updateQuestion(Question question) throws ApsSystemException {
		try {
			this.getQuestionDAO().updateQuestion(question);
		} catch (Throwable t) {
			_logger.error("Error while updating a question", t);
			throw new ApsSystemException("Error while updating a question", t);
		}
	}
	
	@Override
	public void deleteQuestionBySurveyId(int id) throws ApsSystemException {
		try {
			this.getQuestionDAO().deleteQuestionBySurveyId(id);
		} catch (Throwable t) {
			_logger.error("Error while deleting the questions of a survey", t);
			throw new ApsSystemException("Error while deleting the questions of a survey", t);
		}
	}
	
	@Override
	public void updateSurvey(Survey survey) throws ApsSystemException {
		try {
			this.getSurveyDAO().updateSurvey(survey);
		} catch (Throwable t) {
			_logger.error("Error while deleting the questions of a survey", t);
			throw new ApsSystemException("Error while deleting the questions of a survey", t);
		}
	}
	
	@Override
	public void swapQuestionPosition(int id, boolean isUp) throws ApsSystemException {
		try {
			Question targetQuestion = this.getQuestionDAO().loadQuestion(id);
			if (null == targetQuestion) {
				return;
			}
			Survey survey = this.getSurveyDAO().loadSurvey(targetQuestion.getSurveyId());
			if (null == survey) {
				return;
			}
			List<Question> questions = survey.getQuestions();
			this.getQuestionDAO().swapQuestionPosition(targetQuestion, questions, isUp);
		} catch (Throwable t) {
			_logger.error("Error while swapping two choices in a question", t);
			throw new ApsSystemException("Error while swapping two choices in a question", t);
		}
	}
	
	@Override
	public void swapChoicePosition(int id, boolean isUp) throws ApsSystemException {
		try {
			Choice choice = this.getChoiceDAO().loadChoice(id);
			if (null == choice) {
				return;
			}
			List<Choice> choices = this.getQuestionDAO().getQuestionChoices(choice.getQuestionId());
			//this.getChoiceDAO().swapChoicePosition(id, isUp);
			this.getChoiceDAO().swapChoicePosition(choice, choices, isUp);
		} catch (Throwable t) {
			_logger.error("Error while swapping two choices in a question", t);
			throw new ApsSystemException("Error while swapping two choices in a question", t);
		}
	}
	
	@Override
	public void saveQuestionInSortedPosition(Question question) throws ApsSystemException {
		try {
			this.getQuestionDAO().saveQuestionInSortedPosition(question);
		} catch (Throwable t) {
			_logger.error("Error while saving a question in a sorted position", t);
			throw new ApsSystemException("Error while saving a question in a sorted position", t);
		}
	}
	
	@Override
	public List<Integer> searchSurvey(Integer id, String description,
			Collection<String> groups, Boolean isActive,
			Boolean isQuestionnaire, String title, Boolean isPublic)
			throws ApsSystemException {
		List<Integer> result = new ArrayList<Integer>();
		try {
			result = this.getSurveyDAO().searchSurvey(id, description, groups, isActive, 
					isQuestionnaire, title, isPublic);
		} catch (Throwable t) {
			_logger.error("Error while serching surveys", t);
			throw new ApsSystemException("Error while serching surveys", t);
		}
		return result;
	}
	
	@Override
	public List<Integer> getActiveSurveyByUser(UserDetails userdetails, Boolean isQuestionnaire, Boolean archive) throws ApsSystemException {
		if (null == userdetails) {
			return null;
		}
		Set<String> groups = new HashSet<String>();
		groups.add(Group.FREE_GROUP_NAME);
		List<Group> userGroups = this.getAuthorizationManager().getGroupsOfUser(userdetails);
		for (int i = 0; i < userGroups.size(); i++) {
			groups.add(userGroups.get(i).getName());
		}
		if (groups.contains(Group.ADMINS_GROUP_NAME)) {
			groups = null;
		}
		List<Integer> result = new ArrayList<Integer>();
		try {
			List<Integer> list = this.getSurveyDAO().searchSurvey(null, null, groups, true, isQuestionnaire, null, null);
			if (null != list) {
				Iterator<Integer> itr = list.iterator();
				while (itr.hasNext()) {
					Integer currentId = itr.next();
					SurveyRecord survey = this.getSurveyDAO().loadSurvey(currentId);
					if ((survey.isArchive() && archive) || (survey.isOpen() && !archive)) {
						result.add(currentId);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore loading the survey list available for user {}", userdetails.getUsername(), t);
			throw new ApsSystemException("Errore loading the survey list available for user " + userdetails.getUsername(), t);
		}
		return result;
	}
	
	@Override
	public List<Integer> getActiveSurveyList() throws ApsSystemException {
		List<Integer> list = null;
		try {
			list = this.getSurveyDAO().searchSurvey(null, null, null, true, null, null, null);
		} catch (Throwable t) {
			_logger.error("Error loading the active surveys list", t);
			throw new ApsSystemException("Error loading the active surveys list", t);
		}
		return list;
	}
	
	@Override
	public List<Integer> getSurveyList() throws ApsSystemException {
		List<Integer> list = null;
		try {
			list = this.getSurveyDAO().searchSurvey(null, null, null, null, null, null, null);
		} catch (Throwable t) {
			_logger.error("Error loading the active surveys list", t);
			throw new ApsSystemException("Error loading the active surveys list", t);
		}
		return list;
	}
	
	@Override
	public List getGroupUtilizers(String groupName) throws ApsSystemException {
		List<Survey> surveys = null;
		try {
			Collection<String> groups = new ArrayList<String>();
			groups.add(groupName);
			List<Integer> surveyIds = this.getSurveyDAO().searchSurvey(null, null, groups, null, null, null, null);
			if (null == surveyIds || surveyIds.isEmpty()) {
				return null;
			}
			surveys = new ArrayList<Survey>(surveyIds.size());
			for (int i = 0; i < surveyIds.size(); i++) {
				Integer id = surveyIds.get(i);
				surveys.add(this.loadSurvey(id));
			}
		} catch (Throwable t) {
			_logger.error("Error loading surveys by group {}",groupName, t);
			throw new ApsSystemException("Error loading surveys by group " + groupName, t);
		}
		return surveys;
	}
	
	@Override
	public List getResourceUtilizers(String resourceId) throws ApsSystemException {
		List<Survey> surveys = null;
		try {
			List<Integer> surveyIds = this.getSurveyDAO().loadResourceUtilizers(resourceId);
			if (null == surveyIds || surveyIds.isEmpty()) {
				return null;
			}
			surveys = new ArrayList<Survey>(surveyIds.size());
			for (int i = 0; i < surveyIds.size(); i++) {
				Integer id = surveyIds.get(i);
				surveys.add(this.loadSurvey(id));
			}
		} catch (Throwable t) {
			_logger.error("Error loading surveys by resource {}", resourceId, t);
			throw new ApsSystemException("Error loading surveys by resource " + resourceId, t);
		}
		return surveys;
	}
	
	public void setSurveyDAO(ISurveyDAO surveyDAO) {
		this._surveyDAO = surveyDAO;
	}
	protected ISurveyDAO getSurveyDAO() {
		return _surveyDAO;
	}
	
	public void setQuestionDAO(IQuestionDAO questionDAO) {
		this._questionDAO = questionDAO;
	}
	protected IQuestionDAO getQuestionDAO() {
		return _questionDAO;
	}
	
	public void setChoiceDAO(IChoiceDAO choiceDAO) {
		this._choiceDAO = choiceDAO;
	}
	protected IChoiceDAO getChoiceDAO() {
		return _choiceDAO;
	}
	
	public void setResponseDAO(IResponseDAO responseDAO) {
		this._responseDAO = responseDAO;
	}
	protected IResponseDAO getResponseDAO() {
		return _responseDAO;
	}
	
	public void setVoterDAO(IVoterDAO voterDAO) {
		this._voterDAO = voterDAO;
	}
	protected IVoterDAO getVoterDAO() {
		return _voterDAO;
	}
	
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	
	private ISurveyDAO _surveyDAO;
	private IQuestionDAO _questionDAO;
	private IChoiceDAO _choiceDAO;
	private IResponseDAO _responseDAO;
	private IVoterDAO _voterDAO;
	
	private IAuthorizationManager _authorizationManager;
	
}