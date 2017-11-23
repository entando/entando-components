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

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.json.JSONObject;

/**
 *
 * @author Entando
 */
public class TestFSIDemoHelper extends TestCase {


    public void testGetPayloadForCompleteEnrichDocument() throws Throwable {
        Map<String, Object> input = new HashMap<>();
        Map <String, String> attr = new HashMap<String, String>();
        Date now = new Date();

        attr.put("keyTest", "valueTest");
        try {
            input.put("identifier", "anyIdentifier");
            input.put("name", "anyName");
            input.put("link", "anyLink");
            input.put("content", "anyBase64Content");

            input.put("size", new Long(1234567));
            input.put("lastModified", now.getTime());
            input.put("attributes", attr);

            String res = FSIDemoHelper.getPayloadForCompleteEnrichDocument(input);
            assertNotNull(res);

            JSONObject json = new JSONObject(res);
            Object obj = JsonHelper.findKey(json, "identifier");
            assertTrue(obj instanceof String);
            assertEquals("anyIdentifier", (String)obj);

            obj = JsonHelper.findKey(json, "name");
            assertTrue(obj instanceof String);
            assertEquals("anyName", (String)obj);

            obj = JsonHelper.findKey(json, "link");
            assertTrue(obj instanceof String);
            assertEquals("anyLink", (String)obj);

            obj = JsonHelper.findKey(json, "size");
            assertTrue(obj instanceof Integer);
            assertEquals((Integer)1234567, (Integer)obj);

            obj = JsonHelper.findKey(json, "lastModified");
            assertTrue(obj instanceof Long);
            assertEquals((Long)now.getTime(), (Long)obj);

            obj = JsonHelper.findKey(json, "content");
            assertTrue(obj instanceof String);
            assertEquals("anyBase64Content", (String)obj);

            obj = JsonHelper.findKey(json, "attributes");
            assertTrue(obj instanceof JSONObject);

            assertTrue(((JSONObject)obj).has("keyTest"));
            assertTrue(Arrays.asList(
                    JSONObject.getNames((JSONObject)obj)).contains("keyTest"));
            assertEquals("valueTest",
                    (String) ((JSONObject)obj).get("keyTest"));

        } catch (Throwable t) {
            throw t;
        }
    }

    public void testGetPayloadForAdditionalClientDetailTask() {
        Map<String, Object> input = new HashMap<>();
        Date now = new Date();

        input.put("party_name", "anyPartyName");
        input.put("surname", "anyPartySurname");
        input.put("type", "anyType");
        input.put("name", "anyName");
        input.put("country", "anyCountry");
        input.put("dateOfBirth", now.getTime());
        input.put("ssn", new Long(2677));
        input.put("email", "anyemail@yanydomain.com");
        input.put("bic", new Long(23812677));
        input.put("street", "anyStreet");
        input.put("zipcode", 12345);
        input.put("state", "anyState");
        input.put("address_country", "anyAddressCountry");


        try {
            String payload = FSIDemoHelper.getPayloadForAdditionalClientDetailTask(input);
            assertNotNull(payload);

            JSONObject json = new JSONObject(payload);

            // CLIENT
            Object obj = JsonHelper.findKey(json, "com.redhat.bpms.demo.fsi.onboarding.model.Client");
            assertTrue(obj instanceof JSONObject);

            assertTrue(((JSONObject)obj).has("name"));
            assertEquals("anyName",
                    (String)((JSONObject)obj).get("name"));

            assertTrue(((JSONObject)obj).has("country"));
            assertEquals("anyCountry",
                    (String)((JSONObject)obj).get("country"));

            assertTrue(((JSONObject)obj).has("type"));
            assertEquals("anyType",
                    (String)((JSONObject)obj).get("type"));

            assertTrue(((JSONObject)obj).has("bic"));
            assertEquals(Long.valueOf(23812677),
                    Long.valueOf((Integer)((JSONObject)obj).get("bic")));

            // ADDRESS
            obj = JsonHelper.findKey(json, "address");

            assertTrue(((JSONObject)obj).has("street"));
            assertEquals("anyStreet",
                    (String)((JSONObject)obj).get("street"));

            assertTrue(((JSONObject)obj).has("zipcode"));
            assertEquals((Integer)12345,
                    (Integer)((JSONObject)obj).get("zipcode"));

            assertTrue(((JSONObject)obj).has("state"));
            assertEquals("anyState",
                    (String)((JSONObject)obj).get("state"));

            assertTrue(((JSONObject)obj).has("country"));
            assertEquals("anyAddressCountry",
                    (String)((JSONObject)obj).get("country"));

            // PARTY
            obj = JsonHelper.findKey(json, "com.redhat.bpms.demo.fsi.onboarding.model.Party");

            assertTrue(obj instanceof JSONObject);
            assertTrue(((JSONObject)obj).has("name"));
            assertEquals("anyPartyName",
                    (String)((JSONObject)obj).get("name"));

            assertTrue(((JSONObject)obj).has("surname"));
            assertEquals("anyPartySurname",
                    (String)((JSONObject)obj).get("surname"));

            assertTrue(((JSONObject)obj).has("dateOfBirth"));
            assertEquals((Long)now.getTime(),
                    (Long)((JSONObject)obj).get("dateOfBirth"));

            assertTrue(((JSONObject)obj).has("ssn"));
            assertEquals(Long.valueOf(2677),
                    Long.valueOf((Integer)((JSONObject)obj).get("ssn")));

            assertTrue(((JSONObject)obj).has("email"));
            assertEquals("anyemail@yanydomain.com",
                    (String)((JSONObject)obj).get("email"));

        } catch (Throwable t) {
            throw t;
        }
    }
}
