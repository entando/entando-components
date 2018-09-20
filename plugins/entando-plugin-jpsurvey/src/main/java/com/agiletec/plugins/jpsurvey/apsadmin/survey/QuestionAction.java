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
package com.agiletec.plugins.jpsurvey.apsadmin.survey;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;

public class QuestionAction extends AbstractSurveyAction implements IQuestionAction {
	
	@Override
	public void validate() {
		super.validate();
		this.checkExistingIds();
		this.fetchLocalizedFields();
	}
	
	private void checkExistingIds() {
		if (null == this.getStrutsAction()) { 
			this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"strutsAction"}));
		} else {
			if (null == this.getQuestionId() && this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"questionId"}));
			}
			if (null == this.getSingleChoice() && this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"isSingleChoice"}));
			}
		}
	}
	
//	private boolean hasResponseNumberFieldErrors() {
//		List<String> minRespErrors = (List<String>) this.getFieldErrors().get("minResponseNumber");
//		List<String> maxRespErrors = (List<String>) this.getFieldErrors().get("maxResponseNumber");
//		return ((minRespErrors != null && (minRespErrors).size() > 0) ||
//				(maxRespErrors != null && maxRespErrors.size() > 0));
//	}
//	
//	
//	private void checkResponseNumberFields() {
//		List<Choice> list = new ArrayList<Choice>();
//		Question question = null;
//		int currentChoicesNumber = 0;
//		if (this.hasActionErrors()) return;
//		if (this.hasResponseNumberFieldErrors()) {
//			return; // Ci sono errori sui size che vanno corretti prima 
//		}
//		// abort response size checks if the override is present or when creating new questions
//		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD || 
//				(null != this.getOverrideResponseNumberFieldsCheck() && this.getOverrideResponseNumberFieldsCheck())) return;
//		try {
//			if (null == this.getMinResponseNumber()) setMinResponseNumber(0);
//			if (null == this.getMaxResponseNumber()) setMaxResponseNumber(0);
//			int min = this.getMinResponseNumber();
//			int max = this.getMaxResponseNumber();
//			if (this.getSingleChoice() == SINGLE_CHOICE_ID) return;
//			question = this.getSurveyManager().loadQuestion(this.getQuestionId());
//			if (null != question) {
//				list = question.getChoices();
//				this.setQuestion(question);
//			}
//			if (null != list) {
//				currentChoicesNumber = list.size();
//				// if we have no choices then these checks are useless
//				if (min == 0 && max == 0 && currentChoicesNumber == 0) return;
//				if (min == 0 && currentChoicesNumber > 0) {
//					this.addFieldError(this.getText("jpsurvey_minResponseNumber"),this.getText("message.questionAction.zeroValue",new String[]{this.getText("jpsurvey_minResponseNumber")}));
//				}
//				// check for errors condition in multiple-choice questions 
//				if (min > currentChoicesNumber && currentChoicesNumber > 0) {
//					this.addActionError(this.getText("message.questionAction.outOfBounds",new String[]{this.getText("minResponseNumber")}));
//				}
//				if (max > currentChoicesNumber && currentChoicesNumber > 0) {
//					this.addActionError(this.getText("message.questionAction.outOfBounds",new String[]{this.getText("maxResponseNumber")}));
//				}
//			} else {
//				// check for improper values in response numbers
//				if (min > 0 || max > 0) {
//					this.addActionError(this.getText("message.questionAction.TypeNotAllowed"));
//				}
//			}
//		} catch (Throwable t) {
//			ApsSystemUtils.logThrowable(t, this, "checkResponseNumberFields");
//			throw new RuntimeException("Error in valition answers allowed",t);
//		}
//	}

	/**
	 * Used to get all the localized field coming from the form
	 */
	private void fetchLocalizedFields() {
		Iterator<Lang> itr = this.getLangManager().getLangs().iterator();
		while (itr.hasNext()) {
			Lang currentLang = itr.next();
			Lang defaultLanguage = this.getLangManager().getDefaultLang();
			String currentLangCode = currentLang.getCode();
			String questionKey = "question-" + currentLangCode;
			String question = this.getRequest().getParameter(questionKey);
			if (null != question && question.trim().length() > 0) {
				this.getQuestions().put(currentLangCode, question.trim());
			} else {
				if (currentLang.getCode().equals(defaultLanguage.getCode())) {
					this.addActionError(this.getText("message.jpsurvey.defaultLangRequired", new String[]{defaultLanguage.getDescr(),this.getText("question")}));
				}
			}
		}
	}
	
	public boolean isEditable(Integer surveyId) {
		boolean res = false;
		try {
			if (null == surveyId) return true;
			Survey survey = this.getSurveyManager().loadSurvey(surveyId);
			res =! survey.isElegibleForVoting(null);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "isEditable");
			throw new RuntimeException("Error checking the \"editable\" state of the survey with id " + surveyId, t);
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.ISurveyAction#getResponseOccurences()
	 */
	public int getResponseOccurences(Integer choiceId) {
		List<SingleQuestionResponse> list = null;
		try {
			if (choiceId == null) return 0;
			list = this.getResponseManager().aggregateResponseByIds(null, null, choiceId, null);
			if (null != list) {
				return list.size();
			} else {
				return 0;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getResponseOccurences");
			throw new RuntimeException("Error loading statistics related to choice ID "+choiceId,t);
		} 
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#addNewQuestion()
	 */
	public String addNewQuestion() {
		SurveyRecord survey = null;
		Question fakeQuestion = new Question();
		try {
			if (this.getQuestionId() == null ) {
				setStrutsAction(ApsAdminSystemConstants.ADD);
				// we have no choice but to load the titles from the survey
				survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
				if (null != survey) {
					fakeQuestion.setId(-1);
					fakeQuestion.setSurveyId(this.getSurveyId());
					fakeQuestion.setExtraInfo(survey.isQuestionnaire(), survey.getTitles());
					this.setQuestion(fakeQuestion);
				}
			} else {
				if (this.getActionErrors().isEmpty()) return this.editSingleQuestion();
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getResponseOccurences");
			throw new RuntimeException("Error adding a new question. ",t);
		}
		return SUCCESS;
	}
	
	@Override
	public String editSingleQuestion() {
		Question question = null;
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			if (null != this.getQuestionId()) {
				question = this.getSurveyManager().loadQuestion(this.getQuestionId());
				if (null == question) {
					this.addActionError(this.getText("message.questionAction.nullquestion", new String[]{String.valueOf(this.getQuestionId())}));
					return INPUT;
				} else {
					this.setQuestion(question);
					this.setTitles(question.getSurveyTitles());
					this.setQuestionId(question.getId());
					this.setSurveyId(question.getSurveyId());
					this.setQuestions(question.getQuestions());
					this.setPos(question.getPos());
					this.setSingleChoice(question.isSingleChoice() ? SINGLE_CHOICE_ID : MULTIPLE_CHOICE_ID);
					if (!question.isSingleChoice()) {
						this.setMinResponseNumber(question.getMinResponseNumber());
						this.setMaxResponseNumber(question.getMaxResponseNumber());
					}
					Map<Integer, Integer> stats = getResponseManager().loadQuestionStatistics(this.getQuestionId());
					// load statistics for the current question
					this.setChoiceStats(stats);
				}
			} else {
				this.addActionError(this.getText("message.surveyAction.cannotProceed")); 
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "editSingleQuestion");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#saveQuestion()
	 */
	public String saveQuestion() {
		Question question = null;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				question = new Question();
				question.setSurveyId(this.getSurveyId());
				question.setSingleChoice(true);
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				question = this.getSurveyManager().loadQuestion(this.getQuestionId());
				question.setSingleChoice(this.getSingleChoice() == SINGLE_CHOICE_ID);
				if (!question.isSingleChoice()) {
					question.setMaxResponseNumber(this.getMaxResponseNumber());
					question.setMinResponseNumber(this.getMinResponseNumber());
					// THIS WILL NOT PRESERVE THE SINGLE/MULTIPLE CHOICE OPTION 
					// check if the min and max answer are present
					if (question.getChoices().size() < this.getMinResponseNumber()) {
						// the choices are fewer than those mandatory
						this.addActionError(this.getText("message.question.tooFewChoices",
								new String[]{String.valueOf(question.getChoices().size()), String.valueOf(this.getMinResponseNumber())}));
						return "wrongAnswerNumber";
					}
					if (this.getMinResponseNumber() > this.getMaxResponseNumber()) {
						// the number of mandatory choices is smaller than the number of the allowed ones.
						this.addActionError(this.getText("message.question.incorrectChoicesRange",
								new String[]{String.valueOf(this.getMinResponseNumber()), String.valueOf(this.getMaxResponseNumber())}));
						return "wrongAnswerNumber";
					}
					if (question.getChoices().size() < this.getMaxResponseNumber()) {
						// the number of choices is smaller than the one a user can pick
						this.addActionError(this.getText("message.question.fewChoices",
								new String[]{String.valueOf(question.getChoices().size()), String.valueOf(this.getMaxResponseNumber())}));
						return "wrongAnswerNumber";
					}
				} else {
					question.setMaxResponseNumber(0);
					question.setMinResponseNumber(0);
				}
			}
			question.setQuestions(this.getQuestions());
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getSurveyManager().saveQuestionInSortedPosition(question);
				this.setQuestionId(question.getId());
				this.setStrutsAction(ApsAdminSystemConstants.EDIT);
				return "editSingleQuestion";
			} 
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getSurveyManager().updateQuestion(question);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveQuestion");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#deleteQuestion()
	 */
	public String deleteQuestion() {
		try {
			if (null != this.getQuestionId()) {
				this.getResponseManager().deleteResponseByQuestionId(this.getQuestionId());
				this.getSurveyManager().deleteQuestion(this.getQuestionId());
			} else {
				this.addActionError(this.getText("message.surveyAction.cannotProceed"));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteQuestion");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#moveQuestionUp()
	 */
	public String moveQuestionUp() {
		return this.moveQuestion(true);
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#moveQuestionDown()
	 */
	public String moveQuestionDown() {
		return this.moveQuestion(false);
	}
	
	private String moveQuestion(boolean up) {
		try {
			if (null != getQuestionId()) {
				this.getSurveyManager().swapQuestionPosition(this.getQuestionId(), up);
			} else {
				this.addActionError(this.getText("message.surveyAction.cannotProceed"));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "moveQuestion");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#trashQuestion()
	 */
	public String trashQuestion() {
		Question question = null;
		try {
			if (null != this.getQuestionId()) {
				question = this.getSurveyManager().loadQuestion(this.getQuestionId());
			} else {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"questionId"}));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "trashQuestion");
			return FAILURE;
		}
		this.setQuestion(question);
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#addFreeText()
	 */
	public String addFreeText() {
		List<Choice> choiceList = null;
		String result = this.saveQuestion();
		if (result.equals(FAILURE)) return FAILURE;
		if (result.equals(INPUT)) return INPUT;
		try {
			if (null != this.getQuestionId()) {
				choiceList = getSurveyManager().getQuestionChoices(this.getQuestionId());
				if (null!= choiceList) {
					Iterator<Choice> itr = choiceList.iterator();
					while (itr.hasNext()) {
						Choice current = itr.next();
						if (current.isFreeText()) {
							this.addActionError(this.getText("message.questionAction.noMoreFreeText"));
							return "noMoreFreeText";
						}
					}
				}	
			} else {
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addFreeText");
			return FAILURE;
		}
		return SUCCESS;
	}	
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#addChoice()
	 */
	public String addChoice() {
		String result = this.saveQuestion();
		if (result.equals(FAILURE)) return FAILURE;
		if (result.equals(INPUT)) return INPUT;
		if (this.getStrutsAction() != ApsAdminSystemConstants.EDIT) {
			this.addActionError("jpsurvey: wrong struts action (" + this.getStrutsAction() + ") detected in 'addChoice', only EDIT allowed");
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * Trampoline to the list of the submitted free text 
	 * @return
	 */
	public String freeTextListEntry() {
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IQuestionAction#editChoice()
	 */
	public String editChoice() {
		return SUCCESS;
	}

	public String getDefaultLangCode() {
		return this.getLangManager().getDefaultLang().getCode();
	}
	
	public void setQuestionId(Integer questionId) {
		this._questionId = questionId;
	}
	public Integer getQuestionId() {
		return _questionId;
	}
	
	public void setStrutsAction(Integer strutsAction) {
		this._strutsAction = strutsAction;
	}
	public Integer getStrutsAction() {
		return _strutsAction;
	}
	
	public void setChoices(List<Choice> choices) {
		this._choices = choices;
	}
	public List<Choice> getChoices() {
		return _choices;
	}
	
	public void setSurveyId(Integer surveyId) {
		this._surveyId = surveyId;
	}
	public Integer getSurveyId() {
		return _surveyId;
	}
	
	public void setPos(Integer pos) {
		this._pos = pos;
	}
	public Integer getPos() {
		return _pos;
	}
	
	public void setMinResponseNumber(Integer minResponseNumber) {
		this._minResponseNumber = minResponseNumber;
	}
	public Integer getMinResponseNumber() {
		return _minResponseNumber;
	}
	
	public void setMaxResponseNumber(Integer maxResponseNumber) {
		this._maxResponseNumber = maxResponseNumber;
	}
	public Integer getMaxResponseNumber() {
		return _maxResponseNumber;
	}
	
	public void setQuestions(ApsProperties questions) {
		this._questions = questions;
	}
	public ApsProperties getQuestions() {
		return _questions;
	}
	
	public void setSingleChoice(Integer singleChoice) {
		this._singleChoice = singleChoice;
	}
	public Integer getSingleChoice() {
		return _singleChoice;
	}
	
	public void setChoiceStats(Map<Integer, Integer> choiceStats) {
		this._choiceStats = choiceStats;
	}
	public Map<Integer, Integer> getChoiceStats() {
		return _choiceStats;
	}
	
	public void setOverrideResponseNumberFieldsCheck(
			Boolean overrideResponseNumberFieldsCheck) {
		this._overrideResponseNumberFieldsCheck = overrideResponseNumberFieldsCheck;
	}
	public Boolean getOverrideResponseNumberFieldsCheck() {
		return _overrideResponseNumberFieldsCheck;
	}
	
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	protected IResponseManager getResponseManager() {
		return _responseManager;
	}
	
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}
	public ApsProperties getTitles() {
		return _titles;
	}
	
	public void setQuestion(Question question) {
		this._question = question;
	}
	public Question getQuestion() {
		return _question;
	}

	// management variables
	private Integer _strutsAction;
	private Boolean _overrideResponseNumberFieldsCheck;
	private Question _question;
	private ApsProperties _titles = new ApsProperties(); // used when adding a new question
	private ApsProperties _questions = new ApsProperties();
	
	// field variables
	private Integer _questionId;
	private Integer _surveyId;
	private Integer _pos;
	private Integer _singleChoice;
	private Integer _minResponseNumber;
	private Integer _maxResponseNumber;
	private List<Choice> _choices;
	private Map<Integer, Integer> _choiceStats;
	
	// managers
	private IResponseManager _responseManager;
	
}
