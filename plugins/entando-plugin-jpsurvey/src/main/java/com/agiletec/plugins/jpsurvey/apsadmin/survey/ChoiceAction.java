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

import java.util.ArrayList;
import java.util.HashMap;
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

public class ChoiceAction extends AbstractSurveyAction implements IChoiceAction {
	
	@Override
	public void validate() {
		super.validate();
		this.checkExistingIds();
		this.fetchLocalizedFields();
	}
	
	/**
	 * Check for the correct imports from chained actions
	 */
	private void checkExistingIds() {
		if (null == this.getStrutsAction()) this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"strutsAction"}));
		else {
			if (null == this.getQuestionId() && this.getStrutsAction()==ApsAdminSystemConstants.EDIT) {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"questionId"}));
			}
			if (null == this.getChoiceId() && this.getStrutsAction()==ApsAdminSystemConstants.EDIT) {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"choiceId"}));
			}
		}
	}

	/**
	 * Inspect the request so find all references of strings in the form of 'titles-xy' and 'descriptions-xy' where 'xy' is the
	 * language code 
	 */
	private void fetchLocalizedFields() {
		Iterator<Lang> itr = this.getLangManager().getLangs().iterator();
		while (itr.hasNext()) {
			Lang currentLang = itr.next();
			Lang defaultLanguage = this.getLangManager().getDefaultLang();
			String currentLangCode = currentLang.getCode();
			String choiceKey = "choice-" + currentLangCode;
			String choice = this.getRequest().getParameter(choiceKey);
			if (null != choice && choice.trim().length() > 0) {
				this.getChoices().put(currentLangCode, choice.trim());
			} else {
				if (currentLang.getCode().equals(defaultLanguage.getCode())) {
					this.addActionError(this.getText("message.jpsurvey.defaultLangRequired", new String[]{defaultLanguage.getDescr(),this.getText("choice")}));
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IChoiceAction#listChoice()
	 */
	public String editSingleChoice() {
		Choice choice = null;
		try {
			if (null == this.getChoiceId()) {
				this.addActionError(this.getText("message.surveyAction.cannotProceed")); 
				return INPUT;
			}
			choice = this.getSurveyManager().loadChoice(this.getChoiceId());
			this.setChoice(choice);
			if (null != choice) {
				this.setChoices(choice.getChoices());
			} else {
				this.addActionError(this.getText("message.surveyAction.cannotProceed")); 
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "editSingleChoice");
			return FAILURE;
		}
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IChoiceAction#saveChoice()
	 */
	public String saveChoice() {
		Choice choice = null;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				choice = this.getSurveyManager().loadChoice(this.getChoiceId());
			} 
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				choice = new Choice();
				choice.setQuestionId(this.getQuestionId());
				choice.setFreeText(false);
			}
			choice.setChoices(_choices);
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getSurveyManager().updateChoice(choice);
			} 
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getSurveyManager().saveChoiceInSortedPosition(choice);
				this.setChoiceId(choice.getId());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveChoice");
			return FAILURE;
		}
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IChoiceAction#addNewFreeText()
	 */
	public String addNewFreeText() {
		Choice choice = null;
		// fake XML common for every free text option record
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><properties><property key=\"ERROR\">YOU SHOULD _NOT_ SEE THIS</property></properties>";		
		ApsProperties prop = new ApsProperties();
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		try {
			if (null == this.getQuestionId() || null == this.getStrutsAction()) {
				this.addActionError(this.getText("message.surveyAction.cannotProceed"));
				return INPUT;
			}
			prop.loadFromXml(xml);
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.addActionError(this.getText("message.choiceAction.unexpextedAction", new String[]{this.getText("_choice"), this.getText("freeText")}));
				return INPUT;
			}
			choice = new Choice();
			choice.setChoices(prop);
			choice.setQuestionId(this.getQuestionId());
			choice.setFreeText(true);
			this.getSurveyManager().saveChoiceInSortedPosition(choice);
			this.setChoiceId(choice.getId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addNewFreeText");
			return FAILURE;
		}
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IChoiceAction#addNewChoice()
	 */
	public String addNewChoice() {
		Question question = null;
		Choice fakeChoice = new Choice(); // this is a dummy used only to contain extra infos!
		try {
			question = this.getSurveyManager().loadQuestion(this.getQuestionId());
			if (null != question) {
				fakeChoice.setExtraInfo(question.getSurveyId(), question.isQuestionnaire(), question.getSurveyTitles(), question.getQuestions());
			}
			fakeChoice.setId(-1);
			fakeChoice.setQuestionId(this.getQuestionId());
			this.setChoice(fakeChoice);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addNewChoice");
			return FAILURE;
		}
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IChoiceAction#deleteChoice()
	 */
	public String deleteChoice() {
		try {
			if (null == this.getChoiceId()) {
				this.addActionError(this.getText("message.surveyAction.cannotProceed")); 
				return INPUT;
			}
			this.getResponseManager().deleteResponseByChoiceId(_choiceId);
			this.getSurveyManager().deleteChoice(_choiceId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteChoice");
			return FAILURE;
		}
		return SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IChoiceAction#moveChoiceUp()
	 */
	public String moveChoiceUp() {
		return moveChoice(true);
	}

	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.apsadmin.survey.IChoiceAction#moveChoiceDown()
	 */
	public String moveChoiceDown() {
		return moveChoice(false);
	}
	
	private String moveChoice(boolean up) {
		try {
			if (null == this.getChoiceId()) {
				this.addActionError(this.getText("message.surveyAction.cannotProceed"));
				return INPUT;
			}
			this.getSurveyManager().swapChoicePosition(this.getChoiceId(), up);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "moveChoice");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String getDefaultLangCode() {
		return this.getLangManager().getDefaultLang().getCode();
	}	

	public String trashChoice() throws Throwable {
		Choice choice = null;
		try {
			if (null != this.getChoiceId()) {
				choice = this.getSurveyManager().loadChoice(this.getChoiceId());
				if (null != choice) {
					if (choice.isFreeText()) {
						choice.getChoices().clear();
					}
				} 
			} else {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"choiceId"}));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "trashChoice");
			return FAILURE;
		}
		this.setChoice(choice);
		return SUCCESS;
	}
	
	/**
	 * This look for free text answer trying to aggregate them whenever is possible.
	 */
	public String freeTextList() {
		List<String> list = new ArrayList<String>();
		Map<String, Integer> aggregateResult = new HashMap<String, Integer>();
		List<SingleQuestionResponse> responses = null;
		Iterator<SingleQuestionResponse> itr = null;
		try {
			if (null == this.getQuestionId() || null == this.getChoiceId()) {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"questionId"}));
				return INPUT;
			}

			// collect all the answers
			responses = getResponseManager().aggregateResponseByIds(null, null, this.getChoiceId(), null);
			if (null != responses) {
				itr = responses.iterator();
				while (itr.hasNext()) {
					SingleQuestionResponse current = itr.next();
					list.add(current.getFreeText());
				}
				// aggregate them
				for (int scan = 0; scan < list.size(); scan++) {
					String currentFreeTextKey = list.get(scan).toLowerCase().trim();
					int currentFreeTextValue = 1;
					if (aggregateResult.containsKey(currentFreeTextKey)) {
						currentFreeTextValue = aggregateResult.get(currentFreeTextKey) + 1;
					}
					// add new voice in the map
					aggregateResult.put(currentFreeTextKey, currentFreeTextValue);
				}
			}
			this.setFreeTextMap(aggregateResult);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getFreeTextOptions");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public void setChoiceId(Integer choiceId) {
		this._choiceId = choiceId;
	}
	public Integer getChoiceId() {
		return _choiceId;
	}
	
	public void setQuestionId(Integer questionId) {
		this._questionId = questionId;
	}
	public Integer getQuestionId() {
		return _questionId;
	}
	
	public void setChoices(ApsProperties choices) {
		this._choices = choices;
	}
	public ApsProperties getChoices() {
		return _choices;
	}
	
	public void setStrutsAction(Integer strutsAction) {
		this._strutsAction = strutsAction;
	}
	public Integer getStrutsAction() {
		return _strutsAction;
	}
	
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	public IResponseManager getResponseManager() {
		return _responseManager;
	}
	
	public void setFreeTextMap(Map<String, Integer> freeTextMap) {
		this._freeTextMap = freeTextMap;
	}
	public Map<String, Integer> getFreeTextMap() {
		return _freeTextMap;
	}
	
	public void setChoice(Choice choice) {
		this._choice = choice;
	}
	public Choice getChoice() {
		return _choice;
	}

	// management variables
	private Integer _strutsAction;
	private Choice _choice;

	// field variables
	private Integer _choiceId;
	private Integer _questionId;
	private ApsProperties _choices = new ApsProperties();
	private Map<String, Integer> _freeTextMap;

	// Managers
	private IResponseManager _responseManager;
}
