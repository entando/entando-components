/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by josephwhite on 8/22/18.
 */
public interface ICaseManager {
    JSONObject getCasesDefinitions(KieBpmConfig config, String containerId) throws ApsSystemException;

    List<String> getCaseInstancesList(KieBpmConfig config, String containerId) throws ApsSystemException;

    JSONObject getCaseInstancesDetails(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    JSONArray getMilestonesList(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    JSONArray getCaseComments(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    boolean postCaseComments(KieBpmConfig config, String containerId, String caseID, String comment) throws ApsSystemException;

    boolean updateCaseComments(KieBpmConfig config, String containerId, String caseID, String caseCommentId, String comment) throws ApsSystemException;

    boolean deleteCaseComments(KieBpmConfig config, String containerId, String caseID, String caseCommentId) throws ApsSystemException;

    JSONObject getCaseRoles(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    boolean addCaseRoles(KieBpmConfig config, String containerId, String caseID, String caseRoleName, String user, String group) throws ApsSystemException;

    boolean deleteCaseRoles(KieBpmConfig config, String containerId, String caseID, String caseRoleName, String user, String group) throws ApsSystemException;

    JSONObject getCaseFile(KieBpmConfig config, String containerId, String caseID) throws ApsSystemException;

    boolean postCaseFile(KieBpmConfig config, String containerId, String caseID, String data) throws ApsSystemException;

    boolean deleteCaseFile(KieBpmConfig config, String containerId, String caseID, String dataId) throws ApsSystemException;

    JSONObject getProcessInstanceByCorrelationKey(KieBpmConfig config, String correlationKey) throws ApsSystemException;
}
