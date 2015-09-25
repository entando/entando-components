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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.plugins.jpsurvey.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;

public class TestSurveyManager extends ApsPluginBaseTestCase {
	
	public void testLoadSurvey() throws Throwable {
		Survey survey = null;
		ApsProperties prop = null;
		try {
			survey = this.getSurveyManager().loadSurvey(0);
			assertNull(survey);
			survey = this.getSurveyManager().loadSurvey(1);
			assertNotNull(survey);
			assertEquals("ignored", survey.getGroupName());
			assertNotNull(survey.getDescriptions());
			assertTrue(survey.isActive());
			assertTrue(survey.isGatherUserInfo());
			assertTrue(survey.isPublicResult());
			assertFalse(survey.isPublicPartialResult());
			assertTrue(survey.isQuestionnaire());
			assertNotNull(survey.getTitles());
			assertFalse(survey.isRestricted());
			assertNotNull(survey.getQuestions());
			assertFalse(survey.getQuestions().isEmpty());
			assertEquals(2, survey.getQuestions().size());
			assertEquals("IMG001", survey.getImageId());
			prop = survey.getImageDescriptions();
			assertEquals("Barrali di notte", prop.getProperty("it"));
			// questions must belong to the same survey
			assertTrue(survey.getQuestions().get(0).getSurveyId() == survey.getId());
			assertTrue(survey.getQuestions().get(0).getSurveyId() == survey.getQuestions().get(1).getSurveyId());
			assertNotNull(survey.getQuestions().get(0).getChoices());
			// check choices size
			List<Choice> list = survey.getQuestions().get(0).getChoices();
			assertNotNull(list);
			assertEquals(4, list.size());
			assertTrue(list.get(3).isFreeText());
			assertEquals(4, list.get(3).getPos());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testLoadQuestion() throws Throwable {
		Question question = null;
		int[] choiceOrder = {5, 4, 7, 6};
		try {
			question = this.getSurveyManager().loadQuestion(2);
			assertNotNull(question);
			assertEquals(2, question.getId());
			assertNotNull(question.getQuestions());
			assertEquals(1, question.getPos());
			assertFalse(question.isSingleChoice());
			assertEquals(1, question.getMinResponseNumber());
			assertEquals(2, question.getMaxResponseNumber());
			List<Choice> choices = question.getChoices();
			assertNotNull(choices);
			assertEquals(4, choices.size());
			assertEquals(true, question.isQuestionnaire());
			assertEquals(true, choices.get(0).isQuestionnaire());
			assertEquals("Titolo-1", question.getSurveyTitles().get("it"));
			for (int i = 0; i < choices.size(); i++) {
				Choice choice = choices.get(i);
				assertEquals(choiceOrder[i], choice.getId());
				assertEquals(i + 1, choice.getPos());
			}
			// load unknown question
			question = this.getSurveyManager().loadQuestion(22);
			assertNull(question);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSaveQuestion() throws Throwable {
		Question question = this.getFakeQuestion();
		Question question2 = this.getFakeQuestion();
		Question actual = null;
		try {
			assertEquals(1, question.getSurveyId());
			// save a complete question
			this.getSurveyManager().saveQuestion(question);
			// save question with no choices
			question2.setChoices(null);
			this.getSurveyManager().saveQuestion(question2);
			// save a standard question
			actual = this.getSurveyManager().loadQuestion(question.getId());
			assertNotNull(actual);
			assertEquals(question.getSurveyId(), actual.getSurveyId());
			assertEquals(question.getPos(), actual.getPos());
			assertEquals(question.getMinResponseNumber(), actual.getMinResponseNumber());
			assertEquals(question.getMaxResponseNumber(), actual.getMaxResponseNumber());
			assertTrue(!actual.getChoices().isEmpty());
			assertEquals(question.getChoices().size(), actual.getChoices().size());
			// save a question with no choices
			actual = this.getSurveyManager().loadQuestion(question2.getId());
			assertNotNull(actual);
			assertEquals(question2.getId(), actual.getId());
			assertEquals(question2.getSurveyId(), actual.getSurveyId());
			assertEquals(question2.getPos(), actual.getPos());
			assertEquals(question2.getMinResponseNumber(), actual.getMinResponseNumber());
			assertEquals(question2.getMaxResponseNumber(), actual.getMaxResponseNumber());
			assertTrue(actual.getChoices().isEmpty());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteQuestion(question.getId());
			this.getSurveyManager().deleteQuestion(question2.getId());
		}
	}
	
	public void testLoadChoice() throws Throwable {
		Choice choice=null;
		try {
			choice = this.getSurveyManager().loadChoice(669);
			assertNull(choice);
			choice = this.getSurveyManager().loadChoice(6);
			assertNotNull(choice.getChoices());
			assertTrue(choice.isFreeText());
			assertEquals("Opzione TESTO LIBERO", choice.getChoices().get("it"));
			assertEquals(2, choice.getQuestionId());
			assertEquals(4, choice.getPos());
			assertEquals(1, choice.getSurveyId());
			assertEquals(true, choice.isQuestionnaire());
			assertEquals("Question 1-2", choice.getQuestions().get("en"));
			assertEquals("Titolo-1", choice.getSurveyTitles().get("it"));
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testGetChoiceByQuestionId() throws Throwable {
		List<Choice> list = null;
		try {
			list = this.getSurveyManager().getQuestionChoices(0);
			assertNotNull(list);
			assertTrue(list.isEmpty());
			list = this.getSurveyManager().getQuestionChoices(1);
			assertNotNull(list);
			assertEquals(3, list.size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSaveChoice() throws Throwable {
		Choice choice = null;
		List<Choice> list = null;
		int scan;
		try {
			choice = this.getFakeChoice();
			this.getSurveyManager().saveChoice(choice);
			list = this.getSurveyManager().getQuestionChoices(2);
			assertNotNull(list);
			for (scan=0; scan < list.size() && list.get(scan).getId() != choice.getId(); scan++);
			choice = list.get(scan);
			assertNotNull(choice.getChoices());
			assertEquals(96, choice.getPos());
			assertTrue(choice.isFreeText());
			assertEquals(2, choice.getQuestionId());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteChoice(choice.getId());
		}
	}
	
	public void testDeleteQuestion() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		Survey actual = null;
		Question question = null;
		try {
			// delete an unknown question, no exceptions!
			question = this.getSurveyManager().loadQuestion(222);
			assertNull(question);
			this.getSurveyManager().deleteQuestion(222);
			// save a survey with a single question
			survey.getQuestions().remove(0);
			assertEquals(1, survey.getQuestions().size());
			this.getSurveyManager().saveSurvey(survey);
			this.getSurveyManager().deleteQuestion(survey.getQuestions().get(0).getId());
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertNotNull(actual.getQuestions());
			assertTrue(actual.getQuestions().isEmpty());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testDeleteSurvey() throws Throwable {
		Survey survey = null;
		try {
			// delete an unknown survey
			survey = this.getSurveyManager().loadSurvey(555);
			assertNull(survey);
			this.getSurveyManager().deleteSurvey(555);
			// the deletion of existing survey is performed in other tests!
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSaveSurvey() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		Survey actual = null;
		try {
			DateFormat formatter=null; 
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			String startDateString = formatter.format(survey.getStartDate());
			Date startDate = (Date)formatter.parse(startDateString);
			// test a complete survey
			this.getSurveyManager().saveSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertEquals(survey.getId(), actual.getId());
			assertEquals(survey.getGroupName(), actual.getGroupName());
			assertEquals(survey.getImageId(), actual.getImageId());
			assertEquals(survey.getDescriptions(), actual.getDescriptions());
			assertEquals(survey.getEndDate(), actual.getEndDate());
			assertEquals(survey.getImageDescriptions(), actual.getImageDescriptions());
			assertEquals(startDate, actual.getStartDate());
			assertEquals(survey.getTitles(), actual.getTitles());
			assertEquals(survey.isActive(), actual.isActive());
			assertEquals(survey.isCheckCookie(), actual.isCheckCookie());
			assertEquals(survey.isCheckIpAddress(), actual.isCheckIpAddress());
			assertEquals(survey.getQuestions().size(), actual.getQuestions().size());
			for (Question currentQuestion: survey.getQuestions()) {
				Question actualQuestion = actual.getQuestion(currentQuestion.getId());
				assertNotNull(actualQuestion);
				assertEquals(currentQuestion.getPos(), actualQuestion.getPos());
				assertEquals(currentQuestion.isSingleChoice(), actualQuestion.isSingleChoice());
				assertEquals(currentQuestion.getMaxResponseNumber(), actualQuestion.getMaxResponseNumber());
				assertEquals(currentQuestion.getMinResponseNumber(), actualQuestion.getMinResponseNumber());
				assertEquals(currentQuestion.getQuestions(), actualQuestion.getQuestions());
				for (Choice currentChoice: currentQuestion.getChoices()) {
					Choice actualChoice = actual.getQuestion(currentQuestion.getId()).getChoice(currentChoice.getId());
					assertNotNull(actualChoice);
					assertEquals(currentChoice.isFreeText(), actualChoice.isFreeText());
					assertEquals(currentChoice.getPos(), actualChoice.getPos());
					assertEquals(currentChoice.getChoices(), actualChoice.getChoices());
//					System.out.println(" QUID "+currentQuestion.getId()+" CID "+currentChoice.getId());
				}
			}
			// test a survey with no choices
			this.getSurveyManager().deleteSurvey(survey.getId());
			for (Question i: survey.getQuestions()) {
				i.setChoices(new ArrayList<Choice>());
			}
			this.getSurveyManager().saveSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertEquals(survey.getId(), actual.getId());
			assertEquals(survey.getGroupName(), actual.getGroupName());
			assertEquals(survey.getImageId(), actual.getImageId());
			assertEquals(survey.getDescriptions(), actual.getDescriptions());
			assertEquals(survey.getEndDate(), actual.getEndDate());
			assertEquals(survey.getImageDescriptions(), actual.getImageDescriptions());
			assertEquals(startDate, actual.getStartDate());
			assertEquals(survey.getTitles(), actual.getTitles());
			assertEquals(survey.isActive(), actual.isActive());
			assertEquals(survey.isCheckCookie(), actual.isCheckCookie());
			assertEquals(survey.isCheckIpAddress(), actual.isCheckIpAddress());
			assertEquals(survey.getQuestions().size(), actual.getQuestions().size());
			for (Question currentQuestion: survey.getQuestions()) {
				Question actualQuestion = actual.getQuestion(currentQuestion.getId());
				assertNotNull(actualQuestion);
				assertEquals(currentQuestion.getPos(), actualQuestion.getPos());
				assertEquals(currentQuestion.isSingleChoice(), actualQuestion.isSingleChoice());
				assertEquals(currentQuestion.getMaxResponseNumber(), actualQuestion.getMaxResponseNumber());
				assertEquals(currentQuestion.getMinResponseNumber(), actualQuestion.getMinResponseNumber());
				assertEquals(currentQuestion.getQuestions(), actualQuestion.getQuestions());
				assertNotNull(currentQuestion.getChoices());
				assertEquals(currentQuestion.getChoices().isEmpty(), actualQuestion.getChoices().isEmpty());
				// System.out.println(" QUID "+currentQuestion.getId());
			}
			// test a survey with no questions
			this.getSurveyManager().deleteSurvey(survey.getId());
			survey.getQuestions().clear();
			this.getSurveyManager().saveSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertEquals(survey.getId(), actual.getId());
			assertEquals(survey.getGroupName(), actual.getGroupName());
			assertEquals(survey.getImageId(), actual.getImageId());
			assertEquals(survey.getDescriptions(), actual.getDescriptions());
			assertEquals(survey.getEndDate(), actual.getEndDate());
			assertEquals(survey.getImageDescriptions(), actual.getImageDescriptions());
			assertEquals(startDate, actual.getStartDate());
			assertEquals(survey.getTitles(), actual.getTitles());
			assertEquals(survey.isActive(), actual.isActive());
			assertEquals(survey.isCheckCookie(), actual.isCheckCookie());
			assertEquals(survey.isCheckIpAddress(), actual.isCheckIpAddress());
			assertNotNull(actual.getQuestions());
			assertEquals(0, actual.getQuestions().size());
			assertEquals(survey.getQuestions().size(), actual.getQuestions().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testSaveChoiceInSortedPosition() throws Throwable {
		Survey survey = null;
		Choice choice = null;
		try {
			survey = this.getFakeActiveSurvey();
			survey.getQuestions().remove(1);
			assertEquals(1, survey.getQuestions().size());
			this.getSurveyManager().saveSurvey(survey);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey.getQuestions());
			choice = this.getFakeChoice();
			choice.setId(999);
			assertNotNull(survey.getQuestions());
			choice.setQuestionId(survey.getQuestions().get(0).getId());
			this.getSurveyManager().saveChoiceInSortedPosition(choice);
			survey = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(survey);
			assertNotNull(survey.getQuestions());
			assertNotNull(survey.getQuestions().get(0).getChoices());
			assertEquals(3, survey.getQuestions().get(0).getChoices().size());
			choice = survey.getQuestions().get(0).getChoices().get(2);
			assertEquals(11, choice.getPos());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testUpdateChoice() throws Throwable {
		Choice choice = getFakeChoice();
		try {
			this.getSurveyManager().saveChoice(choice);
			choice = this.getSurveyManager().loadChoice(choice.getId());
			assertNotNull(choice);
			choice.setPos(2677);
			choice.setFreeText(false);
			this.getSurveyManager().updateChoice(choice);
			choice = this.getSurveyManager().loadChoice(choice.getId());
			assertNotNull(choice);
			assertEquals(2677, choice.getPos());
			assertFalse(choice.isFreeText());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteChoice(choice.getId());
		}
	}
	
	public void testDeleteChoiceByQuestionid() throws Throwable {
		Survey survey = null;
		Question actualQuestion = null;
		Survey actual = null;
		try {
			survey = this.getFakeActiveSurvey();
			survey.getQuestions().remove(0);
			assertEquals(1, survey.getQuestions().size());
			this.getSurveyManager().saveSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertNotNull(actual.getQuestions());
			assertEquals(1, actual.getQuestions().size());
			assertEquals(2, actual.getQuestions().get(0).getChoices().size());
			int questionId = survey.getQuestions().get(0).getId();
			this.getSurveyManager().deleteChoiceByQuestionId(questionId);
			actualQuestion = this.getSurveyManager().loadQuestion(questionId);
			assertNotNull(actualQuestion);
			assertNotNull(actualQuestion.getChoices());
			assertEquals(0, actualQuestion.getChoices().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testUpdateQuestion() throws Throwable {
		Question question = this.getFakeQuestion();
		try {
			// standard question with choices
			this.getSurveyManager().saveQuestion(question);
			question = this.getSurveyManager().loadQuestion(question.getId());
			assertNotNull(question);
			assertFalse(question.isSingleChoice());
			question.setMinResponseNumber(2677);
			question.setMaxResponseNumber(1977);
			question.setPos(38);
			question.setSingleChoice(true);
			question.setSurveyId(2);
			assertNotNull(question.getChoices());
			assertTrue(!question.getChoices().isEmpty());
			question.getChoices().get(0).setPos(1976); // this choice will be placed in last position!!!
			this.getSurveyManager().updateQuestion(question);
			question = this.getSurveyManager().loadQuestion(question.getId());
			assertNotNull(question);
			assertTrue(question.isSingleChoice());
			assertNotNull(question.getQuestions());
			assertEquals(38, question.getPos());
			assertEquals(1977, question.getMaxResponseNumber());
			assertEquals(2677, question.getMinResponseNumber());
			assertEquals(2, question.getSurveyId());
			assertNotNull(question.getChoices());
			assertTrue(!question.getChoices().isEmpty());
			assertEquals(2, question.getChoices().size());
			assertEquals(1976, question.getChoices().get(1).getPos());
			// delete one choice
			question.getChoices().remove(0);
			question.getChoices().get(0).setPos(123);
			question.setMinResponseNumber(1);
			this.getSurveyManager().updateQuestion(question);
			question = this.getSurveyManager().loadQuestion(question.getId());
			assertNotNull(question);
			assertEquals(1, question.getMinResponseNumber());
			assertEquals(1, question.getChoices().size());
			assertEquals(123, question.getChoices().get(0).getPos());
			// delete the remaining choice
			question.getChoices().clear();
			question.setMinResponseNumber(123);
			question.setMaxResponseNumber(321);
			this.getSurveyManager().updateQuestion(question);
			question = this.getSurveyManager().loadQuestion(question.getId());
			assertNotNull(question);
			assertEquals(123, question.getMinResponseNumber());
			assertEquals(321, question.getMaxResponseNumber());
			assertNotNull(question.getChoices());
			assertTrue(question.getChoices().isEmpty());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteQuestion(question.getId());
		}
	}
	
	public void testDeleteQuestionBySurveyId() throws Throwable {
		Survey survey = getFakeActiveSurvey();
		Survey actual = null;
		try {
			this.getSurveyManager().saveSurvey(survey);
			this.getSurveyManager().deleteQuestionBySurveyId(survey.getId());
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertNotNull(actual.getQuestions());
			assertEquals(survey.getId(), actual.getId()); // not really needed :)
			assertEquals(survey.getImageId(), actual.getImageId());
			assertEquals(survey.isActive(), actual.isActive());
			assertEquals(survey.getDescriptions(), actual.getDescriptions());
			assertTrue(actual.getQuestions().isEmpty());
			for (Question question: survey.getQuestions()) {
				Question actualQuestion = this.getSurveyManager().loadQuestion(question.getId());
				assertNull(actualQuestion);
				for (Choice choice: question.getChoices()) {
					Choice actualChoice = this.getSurveyManager().loadChoice(choice.getId());
					assertNull(actualChoice);
				}
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testUpdateSurvey() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		Survey actual = null;
		try {
			this.getSurveyManager().saveSurvey(survey);
			assertEquals(false, survey.getQuestions().get(0).isQuestionnaire());
			assertEquals(false, survey.getQuestions().get(0).getChoices().get(0).isQuestionnaire());
			assertEquals(survey.getId(), survey.getQuestions().get(0).getChoices().get(0).getSurveyId());
			// modify a complete survey with its question and choices
			survey.setActive(false);
			survey.setPublicPartialResult(true);
			survey.setPublicResult(false);
			survey.setQuestionnaire(true);
			assertEquals(survey.getId(), survey.getQuestions().get(0).getChoices().get(0).getSurveyId());
			survey.setGatherUserInfo(false);
			survey.setRestricted(true);
			survey.getQuestions().get(0).setPos(2); // this will swap position when loading from database
			survey.getQuestions().get(0).setSingleChoice(true);
			survey.getQuestions().get(0).setMinResponseNumber(789);
			survey.getQuestions().get(0).setMaxResponseNumber(1011);
			survey.getQuestions().get(0).setSingleChoice(true);
			survey.getQuestions().get(1).getChoices().get(0).setPos(11); // this will swap position when loading from database
			survey.getQuestions().get(1).getChoices().get(1).setFreeText(false);
			this.getSurveyManager().updateSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertEquals(false, actual.isActive());
			assertEquals(true, actual.isPublicPartialResult());
			assertEquals(false, actual.isPublicResult());
			assertEquals(true, actual.isQuestionnaire());
			assertEquals(false, actual.isGatherUserInfo());
			assertEquals(true, actual.isRestricted());
			assertNotNull(actual.getQuestions());
			assertEquals(2, actual.getQuestions().size());
			assertNotNull(actual.getQuestions().get(0).getChoices());
			assertNotNull(actual.getQuestions().get(1).getChoices());
			assertEquals(2, actual.getQuestions().get(0).getChoices().size());
			assertEquals(2, actual.getQuestions().get(1).getChoices().size());
			assertEquals(2, actual.getQuestions().get(1).getPos());
			assertEquals(true, actual.getQuestions().get(1).isSingleChoice());
			assertEquals(789, actual.getQuestions().get(1).getMinResponseNumber());
			assertEquals(1011, actual.getQuestions().get(1).getMaxResponseNumber());
			assertEquals(true, actual.getQuestions().get(1).isSingleChoice());
			assertEquals(1, actual.getQuestions().get(0).getPos());
			assertEquals(false, actual.getQuestions().get(0).isSingleChoice());
			assertEquals(false, actual.getQuestions().get(0).getChoices().get(0).isFreeText());
			assertEquals(11, actual.getQuestions().get(0).getChoices().get(1).getPos());
			assertEquals(10, actual.getQuestions().get(0).getChoices().get(0).getPos());
			assertTrue(survey.getQuestions().get(0).isQuestionnaire());
			assertTrue(survey.getQuestions().get(0).getChoices().get(0).isQuestionnaire());
			// check extra info
			assertTrue(survey.getQuestions().get(0).isQuestionnaire());
			assertTrue(survey.getQuestions().get(0).getChoices().get(0).isQuestionnaire());
			assertEquals(survey.getId(), survey.getQuestions().get(0).getChoices().get(0).getSurveyId());
			survey = this.getSurveyManager().loadSurvey(actual.getId());
			// update survey with no choices and one question
			assertNotNull(survey);
			survey.getQuestions().remove(1);
			survey.getQuestions().get(0).getChoices().clear();
//			survey.getQuestions().get(0).setChoices(null); // is the same of the line above
			survey.setActive(true);
			survey.setPublicPartialResult(false);
			survey.getQuestions().get(0).setSingleChoice(true);
			survey.getQuestions().get(0).setPos(11);
			this.getSurveyManager().updateSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertNotNull(actual.getQuestions());
			assertEquals(1, actual.getQuestions().size());
			assertEquals(true, actual.isActive());
			assertEquals(false, actual.isPublicPartialResult());
			assertEquals(true, actual.getQuestions().get(0).isSingleChoice());
			assertEquals(11, actual.getQuestions().get(0).getPos());
			// alternate question checks, load directly from the DB
			Question question = this.getSurveyManager().loadQuestion(actual.getQuestions().get(0).getId());
			assertNotNull(question);
			assertNotNull(question.getChoices());
			assertTrue(question.getChoices().isEmpty());
			// update a survey with no question
			survey = this.getSurveyManager().loadSurvey(actual.getId());
			survey.getQuestions().clear();
			survey.setActive(false);
			survey.setPublicPartialResult(false);
			survey.setPublicResult(true);
			survey.setQuestionnaire(true);
			survey.setGatherUserInfo(false);
			survey.setRestricted(true);
			this.getSurveyManager().updateSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertNotNull(actual.getQuestions());
			assertTrue(actual.getQuestions().isEmpty());
			assertEquals(false, actual.isActive());
			assertEquals(false, actual.isPublicPartialResult());
			assertEquals(true, actual.isPublicResult());
			assertEquals(true, actual.isQuestionnaire());
			assertEquals(false, actual.isGatherUserInfo());
			assertEquals(true, actual.isRestricted());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testChangeQuestionPosition() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		Survey actual = null;
		Survey expected = null;
		try {
			// prepare a known survey for swapping questions
			survey.getQuestions().get(0).setSingleChoice(true);
			survey.getQuestions().get(0).setMinResponseNumber(1);
			survey.getQuestions().get(0).setMaxResponseNumber(5);
			assertEquals(0, survey.getQuestions().get(0).getPos());
			survey.getQuestions().get(1).setMinResponseNumber(6);
			survey.getQuestions().get(1).setMaxResponseNumber(10);
			survey.getQuestions().get(1).setSingleChoice(false);
			assertEquals(1, survey.getQuestions().get(1).getPos());
			this.getSurveyManager().saveSurvey(survey);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			// Move the first question down
			this.getSurveyManager().swapQuestionPosition(actual.getQuestions().get(0).getId(), false);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertEquals(survey.getQuestions().get(0).getId(), actual.getQuestions().get(1).getId());
			assertEquals(true, actual.getQuestions().get(1).isSingleChoice());
			assertEquals(1, actual.getQuestions().get(1).getMinResponseNumber());
			assertEquals(5, actual.getQuestions().get(1).getMaxResponseNumber());
			assertEquals(survey.getQuestions().get(1).getId(), actual.getQuestions().get(0).getId());
			assertEquals(false, actual.getQuestions().get(0).isSingleChoice());
			assertEquals(6, actual.getQuestions().get(0).getMinResponseNumber());
			assertEquals(10, actual.getQuestions().get(0).getMaxResponseNumber());
			
			// move the last question down does nothing
			expected = this.getSurveyManager().loadSurvey(survey.getId());
			this.getSurveyManager().swapQuestionPosition(actual.getQuestions().get(1).getId(), false);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertEquals(expected.getQuestions().get(0).getId(), actual.getQuestions().get(0).getId());
			assertEquals(true, actual.getQuestions().get(1).isSingleChoice());
			assertEquals(1, actual.getQuestions().get(1).getMinResponseNumber());
			assertEquals(5, actual.getQuestions().get(1).getMaxResponseNumber());
			assertEquals(expected.getQuestions().get(1).getId(), actual.getQuestions().get(1).getId());
			assertEquals(false, actual.getQuestions().get(0).isSingleChoice());
			assertEquals(6, actual.getQuestions().get(0).getMinResponseNumber());
			assertEquals(10, actual.getQuestions().get(0).getMaxResponseNumber());
			
			// move the first question up does nothing
			expected = this.getSurveyManager().loadSurvey(survey.getId());
			this.getSurveyManager().swapQuestionPosition(actual.getQuestions().get(0).getId(), true);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertEquals(expected.getQuestions().get(0).getId(), actual.getQuestions().get(0).getId());
			assertEquals(true, actual.getQuestions().get(1).isSingleChoice());
			assertEquals(1, actual.getQuestions().get(1).getMinResponseNumber());
			assertEquals(5, actual.getQuestions().get(1).getMaxResponseNumber());
			assertEquals(expected.getQuestions().get(1).getId(), actual.getQuestions().get(1).getId());
			assertEquals(false, actual.getQuestions().get(0).isSingleChoice());
			assertEquals(6, actual.getQuestions().get(0).getMinResponseNumber());
			assertEquals(10, actual.getQuestions().get(0).getMaxResponseNumber());
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testSwapChoicePosition() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		Survey actual = null;
		Survey expected = null;
		try {
			// prepare a known survey for swapping questions
			survey.getQuestions().remove(1);
			survey.getQuestions().get(0).getChoices().get(0).setPos(1);
			survey.getQuestions().get(0).getChoices().get(0).setFreeText(false);
			survey.getQuestions().get(0).getChoices().get(1).setPos(2);
			survey.getQuestions().get(0).getChoices().get(1).setFreeText(true);
			this.getSurveyManager().saveSurvey(survey);
			expected = this.getSurveyManager().loadSurvey(survey.getId());
			// swap the first choice downwards
			this.getSurveyManager().swapChoicePosition(expected.getQuestions().get(0).getChoices().get(0).getId(), false);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertNotNull(actual.getQuestions());
			assertNotNull(actual.getQuestions().get(0).getChoices());
			assertEquals(expected.getQuestions().get(0).getChoices().get(1).getId(), actual.getQuestions().get(0).getChoices().get(0).getId());
			assertEquals(true, actual.getQuestions().get(0).getChoices().get(0).isFreeText());
			assertEquals(expected.getQuestions().get(0).getChoices().get(0).getId(), actual.getQuestions().get(0).getChoices().get(1).getId());
			assertEquals(false, actual.getQuestions().get(0).getChoices().get(1).isFreeText());
			// swap the first choice upwards does nothing
			expected = this.getSurveyManager().loadSurvey(actual.getId());
			this.getSurveyManager().swapChoicePosition(actual.getQuestions().get(0).getChoices().get(0).getId(), true);
			assertEquals(expected.getQuestions().get(0).getChoices().get(0).getId(), actual.getQuestions().get(0).getChoices().get(0).getId());
			assertEquals(true, actual.getQuestions().get(0).getChoices().get(0).isFreeText());
			assertEquals(expected.getQuestions().get(0).getChoices().get(1).getId(), actual.getQuestions().get(0).getChoices().get(1).getId());
			assertEquals(false, actual.getQuestions().get(0).getChoices().get(1).isFreeText());
			// swap the first choice downwards does nothing
			expected = this.getSurveyManager().loadSurvey(actual.getId());
			this.getSurveyManager().swapChoicePosition(actual.getQuestions().get(0).getChoices().get(1).getId(), false);
			assertEquals(expected.getQuestions().get(0).getChoices().get(0).getId(), actual.getQuestions().get(0).getChoices().get(0).getId());
			assertEquals(true, actual.getQuestions().get(0).getChoices().get(0).isFreeText());
			assertEquals(expected.getQuestions().get(0).getChoices().get(1).getId(), actual.getQuestions().get(0).getChoices().get(1).getId());
			assertEquals(false, actual.getQuestions().get(0).getChoices().get(1).isFreeText());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testSaveQuestionInSortedPosition() throws Throwable {
		Survey survey = this.getFakeActiveSurvey();
		Survey actual = null;
		Question question = getFakeQuestion();
		try {
			// prepare a known survey for swapping questions
			survey.getQuestions().get(0).setPos(3);
			survey.getQuestions().get(0).setSingleChoice(true);
			survey.getQuestions().get(0).setMinResponseNumber(1);
			survey.getQuestions().get(0).setMaxResponseNumber(5);
			survey.getQuestions().get(1).setPos(9);
			survey.getQuestions().get(1).setSingleChoice(false);
			survey.getQuestions().get(1).setMinResponseNumber(6);
			survey.getQuestions().get(1).setMaxResponseNumber(10);
			this.getSurveyManager().saveSurvey(survey);
			// add the new question
			question.setSurveyId(survey.getId());
			question.setPos(1); // this will be ignored when saving
			question.setMaxResponseNumber(888);
			question.setMinResponseNumber(777);
			this.getSurveyManager().saveQuestionInSortedPosition(question);
			actual = this.getSurveyManager().loadSurvey(survey.getId());
			assertNotNull(actual);
			assertNotNull(actual.getQuestions());
			assertEquals(3, actual.getQuestions().size());
			assertEquals(888, actual.getQuestions().get(2).getMaxResponseNumber());
			assertEquals(777, actual.getQuestions().get(2).getMinResponseNumber());
			assertEquals(10, actual.getQuestions().get(2).getPos());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testSearchSurvey() throws Throwable {
		List<Integer> result = null;
		SurveyRecord survey = null;
		Set<String> col1 = new HashSet<String>();
		col1.add("ignoredToo");
		Set<String> col2 = new HashSet<String>();
		col2.add("ignoREd");
		try {
			// test by ID
//			searchSurveyByIds(id, description, group, isActive, isQuestionnaire, title, isPublic)
			result = this.getSurveyManager().searchSurvey(1, null, null, null, null, null, null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals("ignored", survey.getGroupName().trim());
			// test by descr
			result = this.getSurveyManager().searchSurvey(null, "n-1", null, null, null, null, null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals("ignored", survey.getGroupName().trim());
//			l=this.getSurveyManager().searchSurveyByIds(null, "<survey>", null, null, null, null, null);
//			assertNull(l);
			// test by group
			result = this.getSurveyManager().searchSurvey(null, null, col1, null, null, null, null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals("ignoredToo", survey.getGroupName().trim());
			// test by isActive
			result = this.getSurveyManager().searchSurvey(null, null, null, false, null, null, null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals(2, survey.getId());
			// test by isActive (2)
			result = this.getSurveyManager().searchSurvey(null, null, null, true, null, null, null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals(1, survey.getId());
			// test by isQuestionnaire			
			result = this.getSurveyManager().searchSurvey(null, null, null, null, false, null, null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals(2, survey.getId());
			// test by isQuestionnaire (2)
			result = this.getSurveyManager().searchSurvey(null, null, null, null, true, null, null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals(1, survey.getId());
			// test by profileUser
			result = this.getSurveyManager().searchSurvey(null, null, null, null, null, "lo-1", null);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals(1, survey.getId());			
			result = this.getSurveyManager().searchSurvey(null, null, null, null, null, "mucci", null);
			assertNotNull(result);
			assertTrue(result.isEmpty());
			// test by isRestricted
			result = this.getSurveyManager().searchSurvey(null, null, null, null, null, null, true);			
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			// test by isRestricted (2)
			result = this.getSurveyManager().searchSurvey(null, null, null, null, null, null, false);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals(1, survey.getId());
			// test all fields
			result = this.getSurveyManager().searchSurvey(1, "ne-1", col2, true, true, "le-1", false);
			assertNotNull(result);
			assertEquals(1, result.size());
			survey = this.getSurveyManager().loadSurvey(result.get(0));
			assertEquals(1, survey.getId());
			// This should match all the surveys!
			result = this.getSurveyManager().searchSurvey(null, null, null, null, null, null, null);
			assertNotNull(result);
			assertEquals(2, result.size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSearchSurveyXMLsafe() throws Throwable {
		Survey survey = this.getFakeActiveSurvey("key=\"it\"","propertie");
		List<Integer> result = null;
		try {
			// search for XML statements in the existing surveys
			result = this.getSurveyManager().searchSurvey(null, "key=\"en\"", null, null, null, null, null);
			assertNotNull(result);
			assertEquals(0, result.size());
			result = this.getSurveyManager().searchSurvey(null, null, null, null, null, "key=\"en\"", null);
			assertNotNull(result);
			assertEquals(0, result.size());
			// save a survey with XML statement-like strings 
			survey.getQuestions().clear();
			this.getSurveyManager().saveSurvey(survey);
			result = this.getSurveyManager().searchSurvey(null, "key=", null, null, null, null, null);
			assertEquals(1, result.size());
			result = this.getSurveyManager().searchSurvey(null, null, null, null, null, "prop", null);
			assertEquals(1, result.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	public void testGetSurveyList() throws Throwable {
		Survey survey1 = getFakeActiveSurvey();
		Survey survey2 = getFakeActiveSurvey();
		List<Integer> list = null;
		try {
			this.getSurveyManager().saveSurvey(survey1);
			this.getSurveyManager().saveSurvey(survey2);
			// get all surveys
			list = this.getSurveyManager().getSurveyList();
			assertNotNull(list);
			assertEquals(4, list.size());
			// get active surveys list
			list = this.getSurveyManager().getActiveSurveyList();
			assertNotNull(list);
			assertEquals(3, list.size());
			// minus one!
			survey1.setActive(false);
			this.getSurveyManager().updateSurvey(survey1);
			list = this.getSurveyManager().getActiveSurveyList();
			assertNotNull(list);
			assertEquals(2, list.size());
			// get all surveys again :)
			list = this.getSurveyManager().getSurveyList();
			assertNotNull(list);
			assertEquals(4, list.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getSurveyManager().deleteSurvey(survey1.getId());
			this.getSurveyManager().deleteSurvey(survey2.getId());
		}
	}
	
//	public void testGetSurveysByUser() throws Throwable {
//		Survey s1 = this.getFakeActiveSurvey();
//		try {
//			DateFormat formatter=null; 
//			Date date = null;
//			formatter = new SimpleDateFormat("dd-MM-yyyy");
//			date = (Date)formatter.parse("02-06-2077"); 
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(date);
//			s1.setStartDate(cal.getTime());
//			date = (Date)formatter.parse("02-06-2077"); 
//			cal.setTime(date);
//			s1.setEndDate(date);
//		} catch (Throwable t) {
//			throw t;
//		} finally {
//			getSurveyManager().deleteSurvey(s1.getId());
//		}
//	}
	
}

