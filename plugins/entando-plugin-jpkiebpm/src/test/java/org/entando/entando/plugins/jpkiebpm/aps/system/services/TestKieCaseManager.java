/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TestKieCaseManager extends TestKieFormManager {


    protected List<String> caseInstancesList;
    protected CaseManager caseManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        caseManager = (CaseManager) this.getService("jpkiebpmsCaseManager");
    }

    public void testGetCasesDefinitions() throws Throwable {

        KieBpmConfig current = super.getConfigForTests();
        try {
            // invoke the manager
            JSONObject _casesDefinitions = caseManager.getCasesDefinitions(current, containers.get(0).getContainerId());
            assertNotNull(_casesDefinitions);
            if (TEST_ENABLED) {
                assertFalse(_casesDefinitions.length() == 0);
            } else {
                assertTrue(_casesDefinitions.length() == 0);
            }
        } catch (Throwable t) {
            throw t;
        } finally {

        }
    }

    public void testGetCaseInstancesList() throws Throwable {

        KieBpmConfig current = super.getConfigForTests();

        try {
            // invoke the manager
            caseInstancesList = caseManager.getCaseInstancesList(current, containers.get(0).getContainerId());
            assertNotNull(caseInstancesList);
            if (TEST_ENABLED) {
                assertFalse(caseInstancesList.isEmpty());
            } else {
                assertTrue(caseInstancesList.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
        }
    }

    public void testGetMilestonesList() throws Throwable {
        KieBpmConfig current = super.getConfigForTests();

        try {
            // invoke the manager
            JSONArray _MilestonesList = caseManager.getMilestonesList(current, containers.get(0).getContainerId(), caseInstancesList.get(0));
            assertNotNull(_MilestonesList);
            if (TEST_ENABLED) {
                assertFalse(_MilestonesList == null);
            } else {
                assertTrue(_MilestonesList == null);
            }
        } catch (Throwable t) {
            throw t;
        } finally {
        }
    }

}
