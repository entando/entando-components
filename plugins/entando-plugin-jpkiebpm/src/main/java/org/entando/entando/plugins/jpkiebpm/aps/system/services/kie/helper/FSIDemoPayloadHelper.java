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

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form.KieApiProcessStart;
import org.json.JSONObject;

/**
 *
 * @author entando
 */
public class FSIDemoPayloadHelper {

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


    public static String createStartProcessPayload(KieApiProcessStart process) {
        String res = null;

        try {
            JSONObject json = FSIDemoPayloadHelper.getJsonForBpm();
            json = FSIDemoPayloadHelper.replaceValuesFromProcess(json, process);
            res = json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



}
