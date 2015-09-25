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
package com.agiletec.plugins.jpsurvey.apsadmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jpsurvey.PluginConfigTestUtils;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class ApsAdminPluginBaseTestCase extends ApsAdminBaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	/**
	 * Create a survey object to perform the test. This object prior its usage must be saved in database
	 * and then reloaded to update the internal ID's
	 * @param isQuestionnaire
	 * @param isActive
	 * @param isFreeText
	 * @return the survey object just saved in the database
	 * @throws Throwable in case of error
	 */
	protected Survey createFakeSurveyForTest(boolean isQuestionnaire, boolean isActive, boolean isFreeText) throws Throwable {
		Survey survey = this.getFakeActiveSurvey("testingAction", "testingAction");
		survey.setQuestionnaire(isQuestionnaire);
		survey.setActive(isActive);
		survey.getQuestions().get(0).getChoices().get(0).setFreeText(isFreeText);
		survey.getQuestions().get(0).getChoices().get(1).setFreeText(false);
		survey.getQuestions().get(1).getChoices().get(0).setFreeText(isFreeText);
		survey.getQuestions().get(1).getChoices().get(1).setFreeText(false);
		return survey;
	}
	
	/**
	 * Create on the fly a survey object to perform tests routines
	 * @return a publishable survey object
	 * @throws Throwable in case of any error
	 */
	protected Survey getFakeActiveSurvey() throws Throwable {
		Survey survey = new Survey();
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><properties><property key=\"test\">me</property><property key=\"test2\">me too</property></properties>";
		String xmlImageDescr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><properties><property key=\"en\">You should NOT see THIS in the DB!</property><property key=\"it\">NON dovresti vedere QUESTO nel database!</property></properties>";
		Question question = this.getFakeQuestion();
		Question question2 = this.getFakeQuestion();
		question2.setPos(1);
		List<Question> questions = new ArrayList<Question>();
		questions.add(question);
		questions.add(question2);
		Date date = new Date();
		ApsProperties prop = new ApsProperties();
		prop.loadFromXml(xml);
		survey.setDescriptions(prop);
		survey.setGroupName("fake");
		survey.setStartDate(date);
		survey.setEndDate(null);
		survey.setActive(true);
		survey.setPublicPartialResult(false);
		survey.setPublicResult(true);
		survey.setQuestionnaire(false);
		survey.setGatherUserInfo(true);
		survey.setRestricted(true);
		survey.setTitles(prop);
		survey.setImageId("IMGXXX");
		prop = new ApsProperties();
		prop.loadFromXml(xmlImageDescr);
		survey.setImageDescriptions(prop);
		survey.setQuestions(questions);
		return survey;
	}
	
	/**
	 * This creates a fake active survey with the given title and description.
	 * @param description the text string inserted in the XML portion for the description
	 * @param title the text string inserted in the XML portion for the title
	 * @return the requested survey object
	 * @throws Throwable
	 */
	protected Survey getFakeActiveSurvey(String description, String title) throws Throwable {
		Survey survey = new Survey();
		String xmlDescription="<?xml version=\"1.0\" encoding=\"UTF-8\"?><properties><property key=\"en\">"+description+"_en</property><property key=\"it\">"+description+"_it</property></properties>";
		String xmlTitle="<?xml version=\"1.0\" encoding=\"UTF-8\"?><properties><property key=\"en\">"+title+"_en</property><property key=\"it\">"+title+"_it+</property></properties>";
		Question question = this.getFakeQuestion();
		question.setPos(1);
		Question question2 = this.getFakeQuestion();
		question2.setPos(2);
		List<Question> questions = new ArrayList<Question>();
		questions.add(question);
		questions.add(question2);
		Date date = new Date();
		ApsProperties prop = new ApsProperties();
		prop.loadFromXml(xmlDescription);
		survey.setDescriptions(prop);
		survey.setGroupName("fake");
		survey.setStartDate(date);
		survey.setEndDate(null);
		survey.setActive(true);
		survey.setPublicPartialResult(false);
		survey.setPublicResult(true);
		survey.setQuestionnaire(false);
		survey.setGatherUserInfo(true);
		survey.setRestricted(true);
		prop = new ApsProperties();
		prop.loadFromXml(xmlTitle);
		survey.setTitles(prop);
		survey.setImageId("IMGXXX");
		prop = new ApsProperties();
		prop.loadFromXml(xmlDescription);
		survey.setImageDescriptions(prop);
		survey.setQuestions(questions);
		return survey;
	}
	
	/**
	 * This will assign the fake question to the survey ID 1
	 * @return a question object not existing in the database
	 * @throws Throwable
	 */
	protected Question getFakeQuestion() throws Throwable {
		Question question = new Question();
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><properties><property key=\"test\">me</property><property key=\"test2\">me too</property></properties>";
		ApsProperties prop = new ApsProperties();
		Choice choice1 = this.getFakeChoice();
		Choice choice2 = this.getFakeChoice();
		List<Choice> choiceList = new ArrayList<Choice>();
		try {
			choiceList.add(choice1);
			choiceList.add(choice2);
			prop.loadFromXml(xml);
			question.setQuestions(prop);
			question.setId(2); // this will be changed when saving...
			question.setMaxResponseNumber(2677);
			question.setMinResponseNumber(1977);
			question.setPos(0);
			question.setSingleChoice(false);
			question.setSurveyId(1);
			choice1.setId(2); // will be changed upon saving
			choice1.setPos(0);
			choice1.setQuestionId(question.getId());
			choice1.setId(2); // will be changed upon saving
			choice2.setPos(10); // this is set on purpose to test save in sorted position method
			choice2.setQuestionId(question.getId());
			question.setChoices(choiceList);
		} catch (Throwable t) {
			throw t;
		}
		return question;
	}
	
	protected Choice getFakeChoice() throws Throwable {
		Choice choice = new Choice();
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><properties><property key=\"test\">me</property><property key=\"test2\">me too</property></properties>";
		ApsProperties prop = new ApsProperties();
		try {
			prop.loadFromXml(xml);
			choice.setChoices(prop);
			choice.setFreeText(true);
			choice.setPos(96);
			choice.setQuestionId(2);
		} catch (Throwable t) {
			throw t;
		}
		return choice;
	}
	
}
