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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.TestFormToBpmHelper.TASK_DATA_JSON;
import org.json.JSONObject;

/**
 *
 * @author entando
 */
public class TestJsonHelper extends TestCase {

    public void testSearchKey() throws Throwable {
        JSONObject data = new JSONObject(TASK_DATA_JSON);

        assertEquals(null,
                JsonHelper.findKey(new JSONObject("{}"),
                        "any"));
        assertEquals(null,
                JsonHelper.findKey(null,
                        "any"));

        assertEquals(1234567890,
                JsonHelper.findKey(data, "ssn"));
        assertEquals(JSONObject.NULL,
                JsonHelper.findKey(data, "creditScore"));
        assertEquals("via San Giuseppe",
                JsonHelper.findKey(data, "address"));
        Object json =
                JsonHelper.findKey(data, "com.redhat.bpms.examples.mortgage.Property");
        assertNotNull(json);
        assertTrue(json instanceof JSONObject);
        assertTrue(((JSONObject)json).has("price"));
        assertTrue(((JSONObject)json).has("address"));
        assertEquals("via San Giuseppe",
                ((JSONObject)json).get("address"));
        assertEquals(240000,
                ((JSONObject)json).get("price"));
    }

    public void testReplaceKey() throws Throwable {
        JSONObject data = new JSONObject(TASK_DATA_JSON);

        assertFalse(
                JsonHelper.replaceKey(new JSONObject("{}"),
                        "any", null));

        assertFalse(
                JsonHelper.replaceKey(null, "any", null));

        assertTrue(JsonHelper.replaceKey(data, "ssn", 987654321));
        assertEquals(987654321,
                JsonHelper.findKey(data, "ssn"));

        assertTrue(JsonHelper.replaceKey(data, "price", 240001));
        assertEquals(240001,
                JsonHelper.findKey(data, "price"));

        assertTrue(
                JsonHelper.replaceKey(data,
                        "com.redhat.bpms.examples.mortgage.Property",
                        "entando"));
        assertEquals("entando",
                JsonHelper.findKey(data,
                        "com.redhat.bpms.examples.mortgage.Property"));
        assertFalse(
                JsonHelper.replaceKey(data,
                        "unknown-key",
                        "anyvalue"));
    }

}
