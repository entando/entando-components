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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author entando
 */
public class FSIDemoHelper {

    protected static JSONObject getJsonForBpm() {
        return new JSONObject("{\n"
                + "   \"client\":{\n"
                + "      \"com.redhat.bpms.demo.fsi.onboarding.model.Client\":{\n"
                + "         \"id\":null,\n"
                + "         \"name\":\"Giovanni\",\n"
                + "         \"country\":\"IT\",\n"
                + "         \"type\":\"BIG_BUSINESS\",\n"
                + "         \"bic\":\"998899888\",\n"
                + "         \"relatedParties\":[\n"
                + "            {\n"
                + "               \"com.redhat.bpms.demo.fsi.onboarding.model.RelatedParty\":{\n"
                + "                  \"id\":null,\n"
                + "                  \"relationship\":\"Consultant\",\n"
                + "                  \"party\":{\n"
                + "                     \"com.redhat.bpms.demo.fsi.onboarding.model.Party\":{\n"
                + "                        \"id\":null,\n"
                + "                        \"name\":\"Paco\",\n"
                + "                        \"surname\":\"Add\",\n"
                + "                        \"dateOfBirth\":1506590295001,\n"
                + "                        \"ssn\":\"987654321\",\n"
                + "                        \"email\": \"p.addeo@entando.com\"\n"
                + "                     }\n"
                + "                  }\n"
                + "               }\n"
                + "            }\n"
                + "         ]\n"
                + "      }\n"
                + "   },\n"
                + "   \"accountManager\": \"prakash\"\n"
                + "}");
    }

    protected static JSONObject replaceValuesFromProcess(JSONObject json, KieApiProcessStart process) {
        JSONObject client = json.getJSONObject("client").getJSONObject("com.redhat.bpms.demo.fsi.onboarding.model.Client");
        JSONObject party = client.getJSONArray("relatedParties").getJSONObject(0);
        JsonHelper.replaceKey(party, "name", process.getPname());
        JsonHelper.replaceKey(party, "surname", process.getPsurname());
        JsonHelper.replaceKey(party, "dateOfBirth", Long.valueOf(process.getPdateOfBirth()));
        JsonHelper.replaceKey(party, "ssn", "123456789");//Hardcoded
        JsonHelper.replaceKey(party, "email", process.getPemail());
        JsonHelper.replaceKey(party, "relationship", process.getPrelationship());
        JsonHelper.replaceKey(client, "name", process.getCname());
        JsonHelper.replaceKey(client, "country", process.getState());
        JsonHelper.replaceKey(client, "type", "BIG_BUSINESS");//Hardcoded
        JsonHelper.replaceKey(client, "bic", "987654321");//Hardcoded
        JsonHelper.replaceKey(json, "accountManager", process.getAccountManager());
        client.getJSONArray("relatedParties").put(0, party);
        json.getJSONObject("client").put("com.redhat.bpms.demo.fsi.onboarding.model.Client", client);
        return json;
    }

    public static KieApiProcessStart replaceValuesFromJson(JSONObject json, KieApiProcessStart process) {
        try {

            JSONObject client = json.getJSONObject("task-input-data");
            if (client.has("htClient")) {
                client = client.getJSONObject("htClient")
                        .getJSONObject("com.redhat.bpms.demo.fsi.onboarding.model.Client");
                JSONObject party = client.getJSONArray("relatedParties").getJSONObject(0)
                        .getJSONObject("com.redhat.bpms.demo.fsi.onboarding.model.RelatedParty");
                JSONObject innerparty = party.getJSONObject("party")
                        .getJSONObject("com.redhat.bpms.demo.fsi.onboarding.model.Party");
                process.setPname(innerparty.getString("name"));
                process.setPsurname(innerparty.getString("surname"));
                process.setPdateOfBirth(String.valueOf(innerparty.getLong("dateOfBirth")));
                process.setPssn(String.valueOf(innerparty.isNull("ssn") ? null : innerparty.getInt("ssn")));
                process.setPemail(innerparty.getString("email"));
                process.setPrelationship(party.getString("relationship"));
                process.setCname(client.getString("name"));
                process.setState(client.isNull("country") ? null : client.getString("country"));
                process.setType(client.isNull("type") ? null : client.getString("type"));
                process.setBic(String.valueOf(innerparty.isNull("bic") ? null : client.getLong("bic")));
                process.setAccountManager(json.isNull("accountManager") ? null : json.getString("accountManager"));
            }
//            process.setAccountManager(json.getString("accountManager"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return process;
    }

    public static String createStartProcessPayload(KieApiProcessStart process) {
        String res = null;

        try {
            JSONObject json = FSIDemoHelper.getJsonForBpm();
            json = FSIDemoHelper.replaceValuesFromProcess(json, process);
            res = json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static KieApiProcessStart createStartProcessPayload(String containerId, String processId, Map<String, Object> input) {
        KieApiProcessStart process = new KieApiProcessStart();
        process.setContainerId(containerId);
        process.setProcessId(processId);
        process.setAccountManager((String) input.get("accountManager"));
        process.setBic((String) input.get("client_bic"));
        process.setCname((String) input.get("client_name"));
        process.setState((String) input.get("client_country"));
        process.setType((String) input.get("client_type"));
        process.setPrelationship("Consultant");
        process.setPemail((String) input.get("client_email"));
        process.setPdateOfBirth("1506590295001");
        Random fRandom = new Random();
        process.setCorrelation(String.valueOf(fRandom.nextGaussian()));
        process.setPssn((String) input.get("client_creditScore"));
        process.setPname((String) input.get("party_name"));
        process.setPsurname((String) input.get("party_surname"));
        return process;
    }

    public static KieProcessFormQueryResult addMissinFields(KieProcessFormQueryResult kpfr) {
        // CLIENT EMAIL
        KieProcessFormField email = new KieProcessFormField();
        email.setName("client_email");
        email.setId(1111l);
        email.setPosition(12);
        email.setType("InputText");
        List<KieProcessProperty> propsEmail = new ArrayList<>();
        KieProcessProperty req = new KieProcessProperty();
        req.setName("fieldRequired");
        req.setValue("true");
        propsEmail.add(req);
        KieProcessProperty lab = new KieProcessProperty();
        lab.setName("label");
        lab.setValue("email (client)");
        propsEmail.add(lab);
        KieProcessProperty read = new KieProcessProperty();
        read.setName("readonly");
        read.setValue("false");
        propsEmail.add(read);
        KieProcessProperty out = new KieProcessProperty();
        out.setName("outputBinding");
        out.setValue("client/email");
        propsEmail.add(out);
        KieProcessProperty fclass = new KieProcessProperty();
        fclass.setName("fieldClass");
        fclass.setValue("java.lang.String");
        propsEmail.add(fclass);
        email.setProperties(propsEmail);

        //PARTY NAME
        KieProcessFormField pname = new KieProcessFormField();
        pname.setName("party_name");
        pname.setId(1222l);
        pname.setPosition(13);
        pname.setType("InputText");
        List<KieProcessProperty> propsPname = new ArrayList<>();
        propsPname.add(req);
        lab = new KieProcessProperty();
        lab.setName("label");
        lab.setValue("name (consultant)");
        propsPname.add(lab);
        propsPname.add(read);
        out = new KieProcessProperty();
        out.setName("outputBinding");
        out.setValue("party/name");
        propsPname.add(out);
        propsPname.add(fclass);
        pname.setProperties(propsPname);

        //PARTY SURNAME
        KieProcessFormField psurname = new KieProcessFormField();
        psurname.setName("party_surname");
        psurname.setId(1333l);
        psurname.setPosition(14);
        psurname.setType("InputText");
        List<KieProcessProperty> propsPsurname = new ArrayList<>();
        propsPsurname.add(req);
        lab = new KieProcessProperty();
        lab.setName("label");
        lab.setValue("surname (consultant)");
        propsPsurname.add(lab);
        propsPsurname.add(read);
        out = new KieProcessProperty();
        out.setName("outputBinding");
        out.setValue("party/surname");
        propsPsurname.add(out);
        propsPsurname.add(fclass);
        psurname.setProperties(propsPsurname);

        kpfr.getFields().add(email);
        kpfr.getFields().add(pname);
        kpfr.getFields().add(psurname);

        return kpfr;
    }

    public static String getPayloadForCompleteEnrichDocument(Map<String, Object> input) throws IOException {
        JSONObject payload = new JSONObject(PAYLOAD_ENRICHMENT);
        Object docObj = JsonHelper.findKey(payload, "org.jbpm.document.service.impl.DocumentImpl");

        if (docObj instanceof JSONObject) {
            JSONObject doc = (JSONObject) docObj;

            if (input.containsKey("identifier")) {
                JsonHelper.replaceKey(doc, "identifier", input.get("identifier"));
            }
            if (input.containsKey("name")) {
                JsonHelper.replaceKey(doc, "name", input.get("name"));
            }
            if (input.containsKey("link")) {
                JsonHelper.replaceKey(doc, "link", input.get("link"));
            }
            if (input.containsKey("size")) {
                Long val = (Long) input.get("size");

                JsonHelper.replaceKey(doc, "size", val);
            }
            if (input.containsKey("lastModified")) {
                Long val = (Long) input.get("lastModified");

                JsonHelper.replaceKey(doc, "lastModified", val);
            }
            if (input.containsKey("content")) {
                JsonHelper.replaceKey(doc, "content", input.get("content"));
            }

            if (input.containsKey("attributes")
                    && input.get("attributes") instanceof Map) {
                Object attr = JsonHelper.findKey(doc, "attributes");

                if (attr instanceof JSONObject) {
                    JsonHelper.replaceKey(doc, "attributes", input.get("attributes"));
                }
            }

        }

        return payload.toString();
    }

    public static String getPayloadForProcessInstancesWithClient(Map<String, String> input) {
        String jsonString = PAYLOAD_INSTANCES_W_DATA;
        if (input != null && input.containsKey("processInstanceId")) {
            String piid = input.get("processInstanceId");
            jsonString = jsonString.replace("#PLACEHOLDER#", QUERY_PLACEHOLDER).replace("666", piid);
        } else {
            jsonString = jsonString.replace("#PLACEHOLDER#", "");
        }
        System.out.println("json:" + jsonString);
        JSONObject json = new JSONObject(jsonString);
        // TODO process dynamic data here
        return json.toString();
    }

    public static String getPayloadForCompleteEnrichmentDocumentApproval(String review) {
        JSONObject json = new JSONObject(PAYLOAD_COMPLE_ENRICHMENT_DOCUMENT_APPROVAL_TASK);

        JsonHelper.replaceKey(json, "htApprovalStatus", review);
        return json.toString();
    }

    public static String getPayloadForAdditionalClientDetailTask(Map<String, Object> input) {
        JSONObject json = new JSONObject(PAYLOAD_ADDITIONAL_CLIENT_DETAIL_TASK);
        JSONObject clientModel = (JSONObject) JsonHelper.findKey(json, "com.redhat.bpms.demo.fsi.onboarding.model.Client");
        JSONObject address = (JSONObject) JsonHelper.findKey(clientModel, "address");
        JSONArray relatedParties = (JSONArray) JsonHelper.findKey(json, "relatedParties");

        if (null == input) {
            // return empty JSON
            return "{}";
        }
        if (clientModel instanceof JSONObject
                && address instanceof JSONObject
                && relatedParties instanceof JSONArray) {

            if (input.containsKey("name")) {
                JsonHelper.replaceKey(clientModel, "name", input.get("name"));
            }
            if (input.containsKey("country")) {
                JsonHelper.replaceKey(clientModel, "country", input.get("country"));
            }
            if (input.containsKey("type")) {
                JsonHelper.replaceKey(clientModel, "type", input.get("type"));
            }
            if (input.containsKey("bic")) {
                JsonHelper.replaceKey(clientModel, "bic", input.get("bic"));
            }

            if (input.containsKey("street")) {
                JsonHelper.replaceKey(address, "street", input.get("street"));
            }
            if (input.containsKey("zipcode")) {
                JsonHelper.replaceKey(address, "zipcode", input.get("zipcode"));
            }
            if (input.containsKey("state")) {
                JsonHelper.replaceKey(address, "state", input.get("state"));
            }
            if (input.containsKey("address_country")) {
                JsonHelper.replaceKey(address, "country", input.get("address_country"));
            }

            JSONObject relatePartiesEntry = (JSONObject) relatedParties.get(0);

            if (input.containsKey("relationship")) {
                JsonHelper.replaceKey(relatePartiesEntry, "relationship", input.get("relationship"));
            }
            if (input.containsKey("party_name")) {
                JsonHelper.replaceKey(relatePartiesEntry, "name", input.get("party_name"));
            }
            if (input.containsKey("surname")) {
                JsonHelper.replaceKey(relatePartiesEntry, "surname", input.get("surname"));
            }
            if (input.containsKey("dateOfBirth")) {
                Long val = (Long) input.get("dateOfBirth");

                JsonHelper.replaceKey(relatePartiesEntry, "dateOfBirth", val);
            }
            if (input.containsKey("ssn")) {
                JsonHelper.replaceKey(relatePartiesEntry, "ssn", input.get("ssn"));
            }
            if (input.containsKey("email")) {
                JsonHelper.replaceKey(relatePartiesEntry, "email", input.get("email"));
            }

        }
        return json.toString();
    }

    public final static String PAYLOAD_ENRICHMENT
            = "{\n"
            + "  \"htUploadedDocument\" : {\n"
            + "  	\"org.jbpm.document.service.impl.DocumentImpl\":{\n"
            + "      \"identifier\" : \"myCoolIdentifier\",\n"
            + "      \"name\" : \"My Cool Document.\",\n"
            + "      \"link\" : \"my-cool-link\",\n"
            + "      \"size\" : 1200,\n"
            + "      \"lastModified\" : 1507840764549,\n"
            + "      \"content\" : \"VkdocGN5QnBjeUIwYUdVZ1ptbHNaU0IxYzJWa0lHWnZjaUIwWlhOMGFXNW4=\",\n"
            + "      \"attributes\" : {\n"
            + "        \"testKey\" : \"testValue\"\n"
            + "      }\n"
            + "  	}\n"
            + "  }\n"
            + "}";

    public final static String PAYLOAD_INSTANCES_W_DATA = "{\n"
            + "  \"order-asc\" : false,\n"
            + "#PLACEHOLDER#"
            + "  \"result-column-mapping\" : {\n"
            + "    \"clientid\" : \"integer\",\n"
            + "    \"name\" : \"string\",\n"
            + "    \"status\": \"string\",\n"
            + "    \"bic\":\"string\",\n"
            + "    \"type\": \"string\",\n"
            + "    \"country\":\"string\",\n"
            + "    \"phonenumber\":\"string\",\n"
            + "    \"creditscore\":\"number\",\n"
            + "    \"relationship\":\"string\",\n"
            + "    \"email\":\"string\",\n"
            + "    \"dateofbirth\":\"date\",\n"
            + "    \"pname\":\"string\",\n"
            + "    \"surname\":\"string\",\n"
            + "    \"ssn\":\"string\"\n"
            + "  }\n"
            + "}";

    public final static String QUERY_PLACEHOLDER
            = "    \"query-params\" : [ {\n"
            + "    \"cond-column\" : \"processinstanceid\",\n"
            + "    \"cond-operator\" : \"EQUALS_TO\",\n"
            + "    \"cond-values\" : [ \"666\" ]\n"
            + "  } ],\n";

    public final static String PAYLOAD_ADDITIONAL_CLIENT_DETAIL_TASK = "{\n"
            + "  \"htClient\" : {\n"
            + "      \"com.redhat.bpms.demo.fsi.onboarding.model.Client\":{\n"
            + "         \"id\":null,\n"
            + "         \"name\":\"Red Hat\",\n"
            + "         \"country\":\"IT\",\n"
            + "         \"type\":\"BIG_BUSINESS\",\n"
            + "         \"bic\":\"123456789\",\n"
            + "         \"address\": {\n"
            + "         	\"street\":\"314 Littleton Rd\",\n"
            + "         	\"zipcode\":\"01886\",\n"
            + "         	\"state\":\"MA\",\n"
            + "         	\"country\":\"USA\"\n"
            + "         },\n"
            + "         \"relatedParties\":[\n"
            + "            {\n"
            + "               \"com.redhat.bpms.demo.fsi.onboarding.model.RelatedParty\":{\n"
            + "                  \"id\":null,\n"
            + "                  \"relationship\":\"Consultant\",\n"
            + "                  \"party\":{\n"
            + "                     \"com.redhat.bpms.demo.fsi.onboarding.model.Party\":{\n"
            + "                        \"id\":null,\n"
            + "                        \"name\":\"Duncan\",\n"
            + "                        \"surname\":\"Doyle\",\n"
            + "                        \"dateOfBirth\":1506590295001,\n"
            + "                        \"ssn\":\"987654321\",\n"
            + "                        \"email\": \"mail@localdomain.com\"\n"
            + "                     }\n"
            + "                  }\n"
            + "               }\n"
            + "            }\n"
            + "         ]\n"
            + "      }\n"
            + "   }\n"
            + "}";

    public final static String PAYLOAD_COMPLE_ENRICHMENT_DOCUMENT_APPROVAL_TASK = "{\n"
            + "  \"htApprovalStatus\" : \"Approved because of this and this.\",\n"
            + "  \"htDocumentApproved\" : true\n"
            + "}";

}
