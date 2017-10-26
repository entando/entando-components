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
package org.entando.entando.plugins.jprestapi.aps.core.helper;

import java.util.List;
import org.entando.entando.plugins.jprestapi.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jprestapi.aps.core.helper.model.TestArray;
import org.entando.entando.plugins.jprestapi.aps.core.helper.model.TestArrayElement;
import org.entando.entando.plugins.jprestapi.aps.core.helper.model.TestObject;
import org.entando.entando.plugins.jprestapi.aps.core.helper.model.TestUser;

/**
 *
 * @author Entando
 */
public class TestJAXBHelper extends ApsPluginBaseTestCase {

    public void testMarshalling() {

        try {
            TestObject obj = new TestObject();

            obj.setAccess_token("accesstoken");
            obj.setExpires_in(Long.MAX_VALUE);
            obj.setToken_type("STANDARD");

            String src = JAXBHelper.marshall(obj, true, false);

            assertNotNull(src);
            assertTrue(src.startsWith("<?xml"));
//            System.out.println("XML:\n" + src);

            src = JAXBHelper.marshall(obj, true, true);

            assertNotNull(src);
            assertTrue(src.startsWith("{"));
            assertTrue(src.contains("token_type"));
//            System.out.println("JSON:\n" + src);

        } catch (Throwable o) {
            o.printStackTrace();
        }
    }

    public void testUnmarshalling() throws Throwable {
        TestUser eu  = null;
        TestObject to = null;

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<user xmlns=\"http://eurotech.com/edc/2.0\">\n" +
            "   <id>15201</id>\n" +
            "   <accountId>1803</accountId>\n" +
            "   <username>prova23</username>\n" +
            "   <createdOn>2017-05-23T09:10:32Z</createdOn>\n" +
            "   <createdBy>15076</createdBy>\n" +
            "   <modifiedOn>2017-05-23T09:10:32Z</modifiedOn>\n" +
            "   <modifiedBy>15076</modifiedBy>\n" +
            "   <status>ENABLED</status>\n" +
            "   <displayName>prova11</displayName>\n" +
            "   <email>prova11@prova.it</email>\n" +
            "   <phoneNumber>3945678935</phoneNumber>\n" +
            "   <loginAttempts>0</loginAttempts>\n" +
            "   <optlock>0</optlock>\n" +
            "</user>";

        try {
            eu = (TestUser) JAXBHelper.unmarshall(xml, TestUser.class, true, false);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        assertNotNull(eu);
        assertNotNull(eu.getId());
        assertNotNull(eu.getUsername());

        assertEquals("15201", eu.getId());
        assertEquals("1803", eu.getAccountId());
        assertEquals("prova23", eu.getUsername());
        assertEquals("ENABLED", eu.getStatus());

        String json = "{\n" +
                        "   \"testObject\" : {\n" +
                        "      \"custom-access-token\" : \"accesstoken\",\n" +
                        "      \"token_type\" : \"STANDARD\",\n" +
                        "      \"expires_in\" : 9223372036854775807\n" +
                        "   }\n" +
                        "}";

        try {
            to = (TestObject) JAXBHelper.unmarshall(json, TestObject.class, true, true);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        assertNotNull(to);
        assertEquals("STANDARD", to.getToken_type());
        assertEquals(new Long(9223372036854775807L), to.getExpires_in());
        assertEquals("accesstoken", to.getAccess_token());
    }

    public void testUnmarshallingArray() throws Throwable {

        final String json = "[ \n" +
                                " {\n" +
                                "  \"id\":\"one\",\n" +
                                "  \"payload\":\"payloadOne\"\n" +
                                " },\n" +
                                "  {\n" +
                                "  \"id\":\"two\",\n" +
                                "  \"payload\":\"payloadTwo\"\n" +
                                " }\n" +
                                "]";
        try {
            List<TestArrayElement>  ta = (List<TestArrayElement> ) JAXBHelper
                    .unmarshall(json, TestArray.class, false, true);
            assertNotNull(ta);
            assertFalse(ta.isEmpty());
            assertEquals(2, ta.size());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
