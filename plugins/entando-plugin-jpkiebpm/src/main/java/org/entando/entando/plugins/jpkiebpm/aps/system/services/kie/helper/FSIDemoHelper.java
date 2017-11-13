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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.json.JSONObject;

/**
 *
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
        JsonHelper.replaceKey(party, "dateOfBirth", process.getPdateOfBirth());
        JsonHelper.replaceKey(party, "ssn", process.getPssn());
        JsonHelper.replaceKey(party, "email", process.getPemail());
        JsonHelper.replaceKey(party, "relationship", process.getPrelationship());
        JsonHelper.replaceKey(client, "name", process.getCname());
        JsonHelper.replaceKey(client, "country", process.getCountry());
        JsonHelper.replaceKey(client, "type", process.getType());
        JsonHelper.replaceKey(client, "bic", process.getBic());
        JsonHelper.replaceKey(json, "accountManager", process.getAccountManager());
        client.getJSONArray("relatedParties").put(0, party);
        json.getJSONObject("client").put("com.redhat.bpms.demo.fsi.onboarding.model.Client", client);
        return json;
    }

    public static KieApiProcessStart replaceValuesFromJson(JSONObject json, KieApiProcessStart process) {
        JSONObject client = json.getJSONObject("task-input-data").getJSONObject("htClient")
                .getJSONObject("com.redhat.bpms.demo.fsi.onboarding.model.Client");
        JSONObject party = client.getJSONArray("relatedParties").getJSONObject(0);
        process.setPname(party.getString("name"));
        process.setPsurname(party.getString("surname"));
        process.setPdateOfBirth(party.getString("dateOfBirth"));
        process.setPssn(party.getString("ssn"));
        process.setPemail(party.getString("email"));
        process.setPrelationship(party.getString("relationship"));
        process.setCname(client.getString("name"));
        process.setCountry(client.getString("country"));
        process.setType(client.getString("type"));
        process.setBic(client.getString("bic"));
        process.setAccountManager(json.getString("accountManager"));
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
        process.setCountry((String) input.get("client_country"));
        process.setType((String) input.get("client_type"));
        process.setPrelationship("Consultant");
        process.setPemail((String) input.get("client_email"));
        process.setPdateOfBirth("1506590295001");
        process.setCorrelation(processId + "_" + process.getBic());
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

}
