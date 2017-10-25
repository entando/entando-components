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

import java.util.HashMap;

import org.apache.http.HttpStatus;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_CONTAINERS_LIST;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_HUMAN_TASK_LIST;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_PROCESS_DEFINITIONS_LIST;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_PROCESS_INSTANCES_LIST;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_PROCESS_DEFINITION;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_PROCESS_DIAGRAM;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_POST_PROCESS_START;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_DATA_HUMAN_TASK;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_GET_TASK_FORM_DEFINITION;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.API_PUT_HUMAN_TASK;

import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.entando.entando.plugins.jprestapi.aps.core.IEndpoint;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;

/**
 * @author Entando
 */
public class KieEndpointDictionary {

    private static HashMap<String, Endpoint> endpoints;

    private KieEndpointDictionary(){}

    private static void init() {
        endpoints = new HashMap<>();
        endpoints.put(API_GET_CONTAINERS_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers", HttpStatus.SC_OK));
        endpoints.put(API_GET_PROCESS_DEFINITIONS_LIST, new Endpoint(IEndpoint.httpVerb.GET, "services/rest/server/queries/processes/definitions", HttpStatus.SC_OK));
        endpoints.put(API_GET_PROCESS_INSTANCES_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/processes/%s/instances?page=%d&pageSize=%d", HttpStatus.SC_OK));
        endpoints.put(API_GET_HUMAN_TASK_LIST, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/queries/tasks/instances/pot-owners?page=%d&pageSize=%d", HttpStatus.SC_OK));
        endpoints.put(API_GET_TASK_FORM_DEFINITION, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/forms/tasks/%s?filter=true", HttpStatus.SC_OK));
        endpoints.put(API_GET_PROCESS_DEFINITION, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/forms/processes/%s?filter=true", HttpStatus.SC_OK, true));
        endpoints.put(API_POST_PROCESS_START, new Endpoint(IEndpoint.httpVerb.POST, "/services/rest/server/containers/%s/processes/%s/instances", HttpStatus.SC_CREATED, true));
        endpoints.put(API_GET_PROCESS_DIAGRAM, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/images/processes/instances/%s", HttpStatus.SC_OK, true));
        endpoints.put(API_GET_DATA_HUMAN_TASK, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/tasks/%s?withInputData=true&withOutputData=true", HttpStatus.SC_OK, true));
        endpoints.put(API_GET_DATA_HUMAN_TASK_DETAIL, new Endpoint(IEndpoint.httpVerb.GET, "/services/rest/server/containers/%s/tasks/%s?withAssignments=true", HttpStatus.SC_OK, true));
        endpoints.put(API_PUT_HUMAN_TASK, new Endpoint(IEndpoint.httpVerb.PUT, "/services/rest/server/containers/%s/tasks/%s/states/completed?auto-progress=true", HttpStatus.SC_CREATED, true));
    }

    public static HashMap<String, Endpoint> create() {
        init();
        return endpoints;
    }

}
