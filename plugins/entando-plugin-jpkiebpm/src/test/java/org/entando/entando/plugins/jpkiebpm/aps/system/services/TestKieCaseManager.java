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

import java.util.List;
import static org.entando.entando.plugins.jpkiebpm.KieTestParameters.TEST_ENABLED;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author own_strong
 */
public class TestKieCaseManager extends TestKieFormManager {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        _caseManager = (CaseManager) this.getService("jpkiebpmsCaseManager");
    }

    public void testGetCasesDefinitions() throws Throwable {
        KieBpmConfig current = _caseManager.getConfig();

        try {
            // update configuration to reflect test configuration
            _caseManager.updateConfig(super.getConfigForTests());
            // invoke the manager
            JSONObject _casesDefinitions = _caseManager.getCasesDefinitions(_containers.get(0).getContainerId());
            assertNotNull(_casesDefinitions);
            if (TEST_ENABLED) {
                assertFalse(_casesDefinitions.length() == 0);
            } else {
                assertTrue(_casesDefinitions.length() == 0);
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _caseManager.updateConfig(current);
        }
    }

    public void testGetCaseInstancesList() throws Throwable {
        KieBpmConfig current = _caseManager.getConfig();

        try {
            // update configuration to reflect test configuration
            _caseManager.updateConfig(super.getConfigForTests());
            // invoke the manager
            _caseInstancesList = _caseManager.getCaseInstancesList(_containers.get(0).getContainerId());
            assertNotNull(_caseInstancesList);
            if (TEST_ENABLED) {
                assertFalse(_caseInstancesList.isEmpty());
            } else {
                assertTrue(_caseInstancesList.isEmpty());
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _caseManager.updateConfig(current);
        }
    }

    public void testGetMilestonesList() throws Throwable {
        KieBpmConfig current = _caseManager.getConfig();

        try {
            // update configuration to reflect test configuration
            _caseManager.updateConfig(super.getConfigForTests());
            // invoke the manager
            JSONArray _MilestonesList = _caseManager.getMilestonesList(_containers.get(0).getContainerId(), _caseInstancesList.get(0));
            assertNotNull(_MilestonesList);
            if (TEST_ENABLED) {
                assertFalse(_MilestonesList == null);
            } else {
                assertTrue(_MilestonesList == null);
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _caseManager.updateConfig(current);
        }
    }

    protected List<String> _caseInstancesList;
    protected CaseManager _caseManager;
}
