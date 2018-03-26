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
package org.entando.entando.plugins.jpkiebpm.aps.system;

/**
 *
 * @author Entando
 */
public interface KieBpmSystemConstants {

    public final static String WIDGET_CODE_BPM_FORM = "bpm-form";
    public static final String WIDGET_PROP_NAME_OVERRIDE_ID = "overrides";

    public final static String DEFAULT_SCHEMA = "http";
    public final static int DEFAULT_POSRT = 80;

    // TODO check if this is a constant somewhere in java.net or arg.apache.httpd
    public final static String HEADER_KEY_ACCEPT = "Accept";
    public final static String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    public final static String HEADER_VALUE_JSON = "application/json";
    
    public final static String HEADER_TRUE = "true";
    public final static String HEADER_FALSE = "false";
   
    //Mileston parameters
    public final static String HEADER_MILESTONES_ACHIEVEDONLY_PARM = "achievedOnly";
   
    //Case instance details parameters
    public final static String HEADER_CASE_DETAILS_WITHDATA_PARM = "withData";
    public final static String HEADER_CASE_DETAILS_WITHROLES_PARM = "withRoles";
    public final static String HEADER_CASE_DETAILS_WITHMILESTONES_PARM = "withMilestones";
    public final static String HEADER_CASE_DETAILS_WITHSTAGES_PARM = "withStages";
    
    //Case instance roles parameters
    public final static String HEADER_CASE_ROLES_USER_PARM = "user";
    public final static String HEADER_CASE_ROLES_GROUP_PARM = "group";

    // configuration parameter
    public final static String KIE_BPM_CONFIG_ITEM = "jpkiebpm_config";

    // misc
    public final static String SUCCESS = "SUCCESS";

    // API mnemonics
    public final static String API_GET_CONTAINERS_LIST = "API_GET_CONTAINERS_LIST";
    public final static String API_GET_PROCESS_DEFINITIONS_LIST = "API_GET_PROCESS_DEFINITIONS_LIST";
    public final static String API_GET_PROCESS_INSTANCES_LIST = "API_GET_PROCESS_INSTANCES_LIST";
    public final static String API_GET_HUMAN_TASK_LIST = "API_GET_HUMAN_TASK_LIST";
    public final static String API_GET_TASK_FORM_DEFINITION = "API_GET_TASK_FORM_DEFINITION";
    public final static String API_GET_PROCESS_DEFINITION = "API_GET_PROCESS_DEFINITION";
    public final static String API_POST_PROCESS_START = "API_POST_PROCESS_START";
    public final static String API_GET_PROCESS_DIAGRAM = "API_GET_PROCESS_DIAGRAM";
    public final static String API_GET_DATA_HUMAN_TASK = "API_GET_DATA_HUMAN_TASK";
    public final static String API_GET_DATA_HUMAN_TASK_DETAIL = "API_GET_DATA_HUMAN_TASK_DETAIL";
    public final static String API_PUT_HUMAN_TASK = "API_PUT_HUMAN_TASK";
    public final static String API_POST_SIGNAL = "API_POST_SIGNAL";
    public final static String API_DELETE_PROCESS = "API_DELETE_PROCESS";
    public final static String API_GET_ALL_PROCESS_INSTANCES_LIST = "API_GET_ALL_PROCESS_INSTANCES_LIST";
    public final static String API_PUT_HUMAN_TASK_STATE = "API_PUT_HUMAN_TASK_STATE";
    public final static String API_PUT_SET_TASK_STATE = "API_PUT_SET_TASK_STATE";
    public final static String API_GET_ALL_TASK_LIST_ADMIN = "API_GET_ALL_TASK_LIST_ADMIN";
    public final static String API_POST_ALL_PROCESS_INSTANCES_W_CLIENT_DATA = "API_POST_ALL_PROCESS_INSTANCES_W_CLIENT_DATA";
    public final static String API_PUT_COMPLETE_ENRICHMENT_DOCUMENT_APPROVAL_TASK = "API_PUT_COMPLETE_ENRICHMENT_DOCUMENT_APPROVAL_TASK";
    public final static String API_GET_CASES_DEFINITIONS = "API_GET_CASES_DEFINITIONS";
    public final static String API_GET_CASES_LIST = "API_GET_CASES_LIST";
    public final static String API_GET_CASES_DETAILS = "API_GET_CASES_DETAILS";
    public final static String API_GET_MILESTONES_LIST = "API_GET_MILESTONES_LIST";
    public final static String API_GET_SERVER_STATUS = "API_GET_SERVER_STATUS";
    public final static String API_GET_COMMENTS_LIST = "API_GET_COMMENTS_LIST";
    public final static String API_POST_COMMENTS = "API_POST_COMMENTS";
    public final static String API_PUT_UPDATE_COMMENTS = "API_PUT_UPDATE_COMMENTS";
    public final static String API_DELETE_COMMENTS = "API_DELETE_COMMENTS";
    public final static String API_GET_ROLE = "API_GET_ROLE";
    public final static String API_PUT_ROLE = "API_PUT_ROLE";
    public final static String API_DELETE_ROLE = "API_DELETE_ROLE";
    
    

    public static final String WIDGET_PARAM_DATA_TYPE_CODE = "dataTypeCode";
    public static final String WIDGET_PARAM_DATA_UX_ID = "dataUxId";
    public static final String WIDGET_PARAM_INFO_ID = "widgetInfoId";

    public static final String WIDGET_INFO_PROP_PROCESS_ID = "processId";
    public static final String WIDGET_INFO_PROP_CONTAINER_ID = "containerId";
    public static final String WIDGET_INFO_PROP_OVERRIDE_ID = "overrides";

}
