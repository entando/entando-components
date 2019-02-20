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

import org.apache.http.HttpStatus;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.entando.entando.plugins.jprestapi.aps.core.IEndpoint;

import java.util.HashMap;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;

/**
 * @author Entando
 */
public class KieEndpointDictionary {

    private static HashMap<String, Endpoint> endpoints;

    private KieEndpointDictionary() {
    }

    private static void init() {
        endpoints = new HashMap<>();
        endpoints.put(API_GET_CONTAINERS_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers", HttpStatus.SC_OK));
        endpoints.put(API_GET_PROCESS_DEFINITIONS_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/processes/definitions", HttpStatus.SC_OK));
        endpoints.put(API_GET_PROCESS_INSTANCES_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/processes/%s/instances?page=%d&pageSize=%d", HttpStatus.SC_OK));
        endpoints.put(API_GET_HUMAN_TASK_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/tasks/instances/pot-owners", HttpStatus.SC_OK));
        endpoints.put(API_GET_TASK_FORM_DEFINITION, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/forms/tasks/%s?filter=true", HttpStatus.SC_OK));
        endpoints.put(API_GET_PROCESS_DEFINITION, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/forms/processes/%s?filter=true", HttpStatus.SC_OK, true));
        endpoints.put(API_POST_PROCESS_START, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/containers/%s/processes/%s/instances/correlation/%s", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_PROCESS_DIAGRAM_BPM6, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/images/processes/instances/%s", HttpStatus.SC_OK, true));   
        endpoints.put(API_GET_PROCESS_DIAGRAM, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/images/processes/%s", HttpStatus.SC_OK, true));
        endpoints.put(API_GET_PROCESS_INSTANCE_DIAGRAM, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/images/processes/instances/%s", HttpStatus.SC_OK, true));

        endpoints.put(API_GET_DATA_HUMAN_TASK, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/tasks/%s?withInputData=true&withOutputData=true", HttpStatus.SC_OK, true));
        endpoints.put(API_GET_DATA_HUMAN_TASK_DETAIL, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/tasks/%s?withAssignments=true", HttpStatus.SC_OK, true));
        endpoints.put(API_PUT_HUMAN_TASK, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/completed?auto-progress=true", HttpStatus.SC_CREATED, true));
        endpoints.put(API_POST_SIGNAL, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/containers/%s/processes/instances/%s/signal/%s", HttpStatus.SC_OK, true));
        endpoints.put(API_DELETE_PROCESS, new Endpoint(IEndpoint.httpVerb.DELETE, "/services/rest/server/containers/%s/processes/instances/%s", HttpStatus.SC_NO_CONTENT, true));
        endpoints.put(API_GET_ALL_PROCESS_INSTANCES_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/processes/instances", HttpStatus.SC_OK));
        endpoints.put(API_PUT_HUMAN_TASK_STATE, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/%s", HttpStatus.SC_CREATED, true));
        endpoints.put(API_PUT_SET_TASK_STATE, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/%s", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_ALL_TASK_LIST_ADMIN, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/tasks/instances/admins", HttpStatus.SC_OK, true));
        endpoints.put(API_POST_ALL_PROCESS_INSTANCES_W_CLIENT_DATA, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/queries/definitions/getProcessInstancesWithClient/filtered-data", HttpStatus.SC_OK, true));


        endpoints.put(API_GET_CASES_DEFINITIONS, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/cases/definitions", HttpStatus.SC_OK));
        endpoints.put(API_GET_CASES_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/cases/instances", HttpStatus.SC_OK));
        endpoints.put(API_GET_CASES_DETAILS, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/cases/instances/%s", HttpStatus.SC_OK));
        endpoints.put(API_GET_MILESTONES_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/cases/instances/%s/milestones", HttpStatus.SC_OK));
        endpoints.put(API_GET_SERVER_STATUS, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server", HttpStatus.SC_OK));
        endpoints.put(API_GET_COMMENTS_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/cases/instances/%s/comments", HttpStatus.SC_OK));
        endpoints.put(API_POST_COMMENTS, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/containers/%s/cases/instances/%s/comments", HttpStatus.SC_CREATED, true));
        endpoints.put(API_PUT_UPDATE_COMMENTS, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/cases/instances/%s/comments/%s", HttpStatus.SC_CREATED, true));
        endpoints.put(API_DELETE_COMMENTS, new Endpoint(IEndpoint.httpVerb.DELETE, "/services/rest/server/containers/%s/cases/instances/%s/comments/%s", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_ROLE, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/cases/instances/%s/roles", HttpStatus.SC_OK));
        endpoints.put(API_PUT_ROLE, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/cases/instances/%s/roles/%s", HttpStatus.SC_CREATED, true));
        endpoints.put(API_DELETE_ROLE, new Endpoint(IEndpoint.httpVerb.DELETE, "/services/rest/server/containers/%s/cases/instances/%s/roles/%s", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_CASE_FILE, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/cases/instances/%s/caseFile", HttpStatus.SC_OK));
        endpoints.put(API_POST_CASE_FILE, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/containers/%s/cases/instances/%s/caseFile", HttpStatus.SC_CREATED, true));
        endpoints.put(API_DELETE_CASE_FILE, new Endpoint(IEndpoint.httpVerb.DELETE, "/services/rest/server/containers/%s/cases/instances/%s/caseFile", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_PROCESS_INSTANCE, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/processes/instance/correlation/%s", HttpStatus.SC_OK));
        
        endpoints.put(API_PUT_COMPLETE_ENRICHMENT_DOCUMENT_APPROVAL_TASK, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/%s", HttpStatus.SC_CREATED, true));
        // /services/rest/server/containers/%s/tasks/%s/states/%s?user=legalWorker&auto-progress=true

        endpoints.put(API_GET_HUMAN_TASK_LIST_ADMIN, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/tasks/instances/admins", HttpStatus.SC_OK));
        endpoints.put(API_PUT_HUMAN_TASK_START, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/started", HttpStatus.SC_CREATED, true));
        endpoints.put(API_PUT_HUMAN_TASK_OUTPUT, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/contents/output", HttpStatus.SC_CREATED, true));
        endpoints.put(API_PUT_HUMAN_TASK_COMPLETE, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/completed", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_HUMAN_TASK_DETAILS, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/tasks/instances/%s", HttpStatus.SC_OK));
        endpoints.put(API_ADMIN_GET_CASES, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/admin/cases/instances?sortOrder=false", HttpStatus.SC_OK));
        endpoints.put(API_POST_RUN_ADDITIONAL_INFO_RULES, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/containers/instances/%s", HttpStatus.SC_OK, true));
        endpoints.put(API_POST_START_CASE, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/containers/%s/cases/%s/instances", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_PROCESS_VARIABLES, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/processes/definitions/%s/variables", HttpStatus.SC_OK, true));
        endpoints.put(API_GET_PROCESS_VARIABLE_INSTANCES, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/processes/instances/%s/variables/instances", HttpStatus.SC_OK, true));
        endpoints.put(API_PUT_HUMAN_TASK_CLAIMED, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/claimed?user=%s", HttpStatus.SC_CREATED, true));


    }

    public static HashMap<String, Endpoint> create() {
        init();
        return endpoints;
    }

}
