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

import com.agiletec.plugins.jpsurvey.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.SurveySystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class TestVoterManager  extends ApsPluginBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this._voterManager = (IVoterManager) this.getService(SurveySystemConstants.SURVEY_VOTER_MANAGER);
	}
	
	public void testSaveLoadDelete() throws Throwable {
		Voter voter = this.getFakeVoter();
		try {
			this.getVoterManager().saveVoter(voter);
			voter = this.getVoterManager().getVoterById(voter.getId());
			assertFalse(voter.getId() == -1);
			assertEquals(31, voter.getAge());
			assertEquals("it", voter.getCountry());
			assertEquals("M", voter.getSex().toString());
			assertNotNull(voter.getDate());
			assertEquals(1, voter.getSurveyid());
			assertEquals("192.168.10.1", voter.getIpaddress());
			assertNotNull(voter);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getVoterManager().deleteVoterById(voter.getId());
		}
	}
	
	public void testSearchByIds() throws Throwable {
		List<Integer> list = null;
		Voter voter1 = this.getFakeVoter();
		Voter voter2 = this.getFakeVoter();
		Voter voter3 = this.getFakeVoter();
		Date date=new Date();
		try {
			voter1.setIpaddress("192.168.10.1");
			voter2.setIpaddress("192.168.10.2");
			voter3.setIpaddress("192.168.10.3");
			voter1.setSex('F');
			voter2.setSex('f');
			voter3.setSex('M');
			voter1.setCountry("jp");
			voter2.setCountry("it");
			voter3.setCountry("is");
			voter1.setAge(Integer.valueOf(31).shortValue());
			voter2.setAge(Integer.valueOf(32).shortValue());
			voter3.setAge(Integer.valueOf(31).shortValue());
			date.setTime(Long.valueOf("11111111111"));
			voter3.setDate(date);
			this.getVoterManager().saveVoter(voter1);
			this.getVoterManager().saveVoter(voter2);
			this.getVoterManager().saveVoter(voter3);			
			list=this.getVoterManager().searchVotersByIds(null, null, null, null, null, null, null);
			assertNotNull(list);
			assertEquals(4, list.size());
			list=this.getVoterManager().searchVotersByIds(-1, null, null, null, null, null, null);
			assertNull(list);
			list=this.getVoterManager().searchVotersByIds(null, 31, null, null, null, null, null);
			assertNotNull(list);
			assertEquals(2, list.size());
			list=this.getVoterManager().searchVotersByIds(null, null, "is", null, null, null, null);
			assertNotNull(list);
			assertEquals(1, list.size());
			list=this.getVoterManager().searchVotersByIds(null, null, null, 'f', null, null, null);
			assertNotNull(list);
			assertEquals(2, list.size());
			list=this.getVoterManager().searchVotersByIds(null, null, null, null, voter2.getDate(), null, null);
			assertNotNull(list);
			assertEquals(2, list.size());
			list=this.getVoterManager().searchVotersByIds(null, null, null, null, null, 1, null);
			assertNotNull(list);
			assertEquals(3, list.size());
			list=this.getVoterManager().searchVotersByIds(null, null, null, null, null, null, "192.168.10.2");
			assertNotNull(list);
			assertEquals(1, list.size());
			assertEquals((int) voter2.getId(), (int)list.get(0));
			list=this.getVoterManager().searchVotersByIds(voter2.getId(), 32, "IT", 'F', voter2.getDate(), 1, "192.168.10.2");
			assertNotNull(list);
			assertEquals(1, list.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getVoterManager().deleteVoterById(voter1.getId());
			this.getVoterManager().deleteVoterById(voter2.getId());
			this.getVoterManager().deleteVoterById(voter3.getId());
		}
	}
	
	public void testDeleteVoterBySurveyId() throws Throwable {
		Voter v1 = this.getFakeVoter();
		Voter v2 = this.getFakeVoter();
		Survey survey=null;
		List<Integer> list=null;
		try {
			survey = this.getFakeActiveSurvey();
			this.getSurveyManager().saveSurvey(survey);
			list = this.getVoterManager().searchVotersByIds(null, null, null, null, null, survey.getId(), null);
			assertNull(list);
			v1.setSurveyid(survey.getId());
			v2.setSurveyid(survey.getId());
			this.getVoterManager().saveVoter(v1);
			this.getVoterManager().saveVoter(v2);
			list = this.getVoterManager().searchVotersByIds(null, null, null, null, null, survey.getId(), null);
			assertNotNull(list);
			assertEquals(2, list.size());
			this.getVoterManager().deleteVoterBySurveyId(survey.getId());
			list = this.getVoterManager().searchVotersByIds(null, null, null, null, null, survey.getId(), null);
			assertNull(list);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.getVoterManager().deleteVoterById(v1.getId());
			this.getVoterManager().deleteVoterById(v2.getId());
			this.getSurveyManager().deleteSurvey(survey.getId());
		}
	}
	
	private Voter getFakeVoter() {
		Voter voter = new Voter();
		voter.setId(-1);
		voter.setAge(Integer.valueOf("31").shortValue());
		voter.setCountry("it");
		voter.setSex('M');
		voter.setDate(new Date());
		voter.setSurveyid(1);
		voter.setIpaddress("192.168.10.1");
		voter.setUsername(SystemConstants.GUEST_USER_NAME);
		return voter;
	}
	
	public void setVoterManager(IVoterManager voterManager) {
		this._voterManager = voterManager;
	}
	public IVoterManager getVoterManager() {
		return _voterManager;
	}

	private IVoterManager _voterManager;
	
}