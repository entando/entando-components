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
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
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

}
