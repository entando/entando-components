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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import junit.framework.TestCase;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

/**
 *
 * @author Entando
 */
public class TestKieContainer extends TestCase {

//    @Override
//    public void setUp() throws Exception {
//
//    }

//    @Override
//    public void tearDown() throws Exception {
//
//    }


    public void testKieContainer() throws Throwable {
        testContainer(kieContainerJson);
    }


    public void testKieContainers() throws Throwable {
        testContainerList(kieContainersListJson);
    }

    public void testKieContainersResult() throws Throwable {
        testContainers(kieContainersJson);
    }

    public void testKieContainersQueryResult() throws Throwable {
        String result;
        KieContainersQueryResult kcqr = (KieContainersQueryResult) JAXBHelper
                .unmarshall(kieContainersQueryResultJson, KieContainersQueryResult.class, false, true);
        KieContainers kieContainers;

        assertNotNull(kcqr);
        assertEquals("SUCCESS", kcqr.getType());
        assertEquals("List of created containers", kcqr.getMsg());
        kieContainers = kcqr.getContainers();
        assertNotNull(kieContainers);
        result = JAXBHelper.marshall(kieContainers, false, true);
        testContainers(result);
    }

    private void testContainers(String json) throws Throwable {
        String jsonConteiners;
        KieContainers kcr = (KieContainers) JAXBHelper
                .unmarshall(json, KieContainers.class, false, true);

        assertNotNull(kcr);
        KieContainerList kieContainerList = kcr.getKieContainerList();
        assertNotNull(kieContainerList);

        jsonConteiners = JAXBHelper.marshall(kieContainerList, true, true);
        testContainerList(jsonConteiners);
    }

    private void testContainerList(String json) throws Throwable {
        String jsonCont;
        KieContainerList kcl = (KieContainerList) JAXBHelper
                .unmarshall(json, KieContainerList.class, true, true);

        assertNotNull(kcl);
        assertFalse(kcl.getList().isEmpty());
        assertEquals(3, kcl.getList().size());

        KieContainer kontainer = kcl.getList().get(1);
        jsonCont = JAXBHelper.marshall(kontainer, false, true);
        testContainer(jsonCont);
    }

    private void testContainer(String json) throws Throwable {
        KieContainer kc = (KieContainer) JAXBHelper
                .unmarshall(json, KieContainer.class, false, true);
        assertNotNull(kc);
        assertNotNull(kc.getConfigItems());
        assertFalse(kc.getConfigItems().isEmpty());
        assertEquals(4, kc.getConfigItems().size());
        assertEquals("KBase",
                kc.getConfigItems().get(0).getItemName());
        assertEquals("",
                kc.getConfigItems().get(0).getItemValue());
        assertEquals("BPM",
                kc.getConfigItems().get(0).getItemType());
        assertEquals("MortgageDemo", kc.getContainerId());
        assertEquals("mortgage",
                kc.getReleaseId().getArtifactId());
        assertEquals("com.redhat.bpms.examples",
                kc.getReleaseId().getGroupId());
        assertEquals("1",
                kc.getReleaseId().getVersion());
        assertEquals("BIG-MORTGAGE",
                kc.getResolvedReleaseId().getArtifactId());
        assertEquals("COM.REDHAT.BPMS.EXAMPLES",
                kc.getResolvedReleaseId().getGroupId());
        assertEquals("2",
                kc.getResolvedReleaseId().getVersion());
        assertEquals("STARTED", kc.getStatus());
        assertEquals("DISPOSED",
                kc.getScanner().getStatus());
        assertEquals(null,
                kc.getScanner().getPollInterval());
        assertNotNull(kc.getMessages());
        assertFalse(kc.getMessages().isEmpty());
        assertEquals(1,
                kc.getMessages().size());
        assertEquals("INFO",
                kc.getMessages().get(0).getSeverity());
        assertEquals((Long)1493575730624L,
                kc.getMessages().get(0).getTimestamp());
        assertNotNull(kc.getMessages().get(0).getContent());
        assertEquals(1,
                kc.getMessages().get(0).getContent().size());
        assertEquals("Container MortgageDemo successfully created with module com.redhat.bpms.examples:mortgage:1.",
                kc.getMessages().get(0).getContent().get(0));
    }

    private final static String kieContainerJson = "{\n" +
            "        \"status\" : \"STARTED\",\n" +
            "        \"scanner\" : {\n" +
            "          \"status\" : \"DISPOSED\",\n" +
            "          \"poll-interval\" : null\n" +
            "        },\n" +
            "        \"messages\" : [ {\n" +
            "          \"severity\" : \"INFO\",\n" +
            "          \"timestamp\" : 1493575730624,\n" +
            "          \"content\" : [ \"Container MortgageDemo successfully created with module com.redhat.bpms.examples:mortgage:1.\" ]\n" +
            "        } ],\n" +
            "        \"container-id\" : \"MortgageDemo\",\n" +
            "        \"release-id\" : {\n" +
            "          \"version\" : \"1\",\n" +
            "          \"group-id\" : \"com.redhat.bpms.examples\",\n" +
            "          \"artifact-id\" : \"mortgage\"\n" +
            "        },\n" +
            "        \"resolved-release-id\" : {\n" +
            "          \"version\" : \"2\",\n" +
            "          \"group-id\" : \"COM.REDHAT.BPMS.EXAMPLES\",\n" +
            "          \"artifact-id\" : \"BIG-MORTGAGE\"\n" +
            "        },\n" +
            "        \"config-items\" : [ {\n" +
            "          \"itemName\" : \"KBase\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"KSession\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"MergeMode\",\n" +
            "          \"itemValue\" : \"MERGE_COLLECTIONS\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"RuntimeStrategy\",\n" +
            "          \"itemValue\" : \"SINGLETON\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        } ]\n" +
            "      }";


    private final static String kieContainersListJson = "{  \n" +
            "   \"kie-containers\":{  \n" +
            "      \"kie-container\":[  \n" +
            "         {  \n" +
            "            \"status\":\"STARTED\",\n" +
            "            \"scanner\":{  \n" +
            "               \"status\":\"DISPOSED\",\n" +
            "               \"poll-interval\":null\n" +
            "            },\n" +
            "            \"messages\":[  \n" +
            "\n" +
            "            ],\n" +
            "            \"container-id\":\"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"resolved-release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"config-items\":[  \n" +
            "               {  \n" +
            "                  \"itemName\":\"KBase\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"KSession\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"MergeMode\",\n" +
            "                  \"itemValue\":\"MERGE_COLLECTIONS\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"RuntimeStrategy\",\n" +
            "                  \"itemValue\":\"SINGLETON\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               }\n" +
            "            ]\n" +
            "         },\n" +
            "         {  \n" +
            "            \"status\":\"STARTED\",\n" +
            "            \"scanner\":{  \n" +
            "               \"status\":\"DISPOSED\",\n" +
            "               \"poll-interval\":null\n" +
            "            },\n" +
            "            \"messages\":[  \n" +
            "               {  \n" +
            "                  \"severity\":\"INFO\",\n" +
            "                  \"timestamp\":1493575730624,\n" +
            "                  \"content\":[  \n" +
            "                     \"Container MortgageDemo successfully created with module com.redhat.bpms.examples:mortgage:1.\"\n" +
            "                  ]\n" +
            "               }\n" +
            "            ],\n" +
            "            \"container-id\":\"MortgageDemo\",\n" +
            "            \"release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"resolved-release-id\":{  \n" +
            "               \"version\":\"2\",\n" +
            "               \"group-id\":\"COM.REDHAT.BPMS.EXAMPLES\",\n" +
            "               \"artifact-id\":\"BIG-MORTGAGE\"\n" +
            "            },\n" +
            "            \"config-items\":[  \n" +
            "               {  \n" +
            "                  \"itemName\":\"KBase\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"KSession\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"MergeMode\",\n" +
            "                  \"itemValue\":\"MERGE_COLLECTIONS\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"RuntimeStrategy\",\n" +
            "                  \"itemValue\":\"SINGLETON\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               }\n" +
            "            ]\n" +
            "         },\n" +
            "         {  \n" +
            "            \"status\":\"STARTED\",\n" +
            "            \"scanner\":{  \n" +
            "               \"status\":\"DISPOSED\",\n" +
            "               \"poll-interval\":null\n" +
            "            },\n" +
            "            \"messages\":[  \n" +
            "\n" +
            "            ],\n" +
            "            \"container-id\":\"Mortgage2\",\n" +
            "            \"release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"resolved-release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"config-items\":[  \n" +
            "               {  \n" +
            "                  \"itemName\":\"KBase\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"KSession\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"MergeMode\",\n" +
            "                  \"itemValue\":\"MERGE_COLLECTIONS\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"RuntimeStrategy\",\n" +
            "                  \"itemValue\":\"SINGLETON\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               }\n" +
            "            ]\n" +
            "         }\n" +
            "      ]\n" +
            "   }\n" +
            "}";

    private final static String kieContainersJson = "{\n" +
            "   \"kie-containers\":{  \n" +
            "      \"kie-container\":[  \n" +
            "         {  \n" +
            "            \"status\":\"STARTED\",\n" +
            "            \"scanner\":{  \n" +
            "               \"status\":\"DISPOSED\",\n" +
            "               \"poll-interval\":null\n" +
            "            },\n" +
            "            \"messages\":[  \n" +
            "\n" +
            "            ],\n" +
            "            \"container-id\":\"com.redhat.bpms.examples:mortgage:1\",\n" +
            "            \"release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"resolved-release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"config-items\":[  \n" +
            "               {  \n" +
            "                  \"itemName\":\"KBase\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"KSession\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"MergeMode\",\n" +
            "                  \"itemValue\":\"MERGE_COLLECTIONS\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"RuntimeStrategy\",\n" +
            "                  \"itemValue\":\"SINGLETON\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               }\n" +
            "            ]\n" +
            "         },\n" +
            "         {  \n" +
            "            \"status\":\"STARTED\",\n" +
            "            \"scanner\":{  \n" +
            "               \"status\":\"DISPOSED\",\n" +
            "               \"poll-interval\":null\n" +
            "            },\n" +
            "            \"messages\":[  \n" +
            "               {  \n" +
            "                  \"severity\":\"INFO\",\n" +
            "                  \"timestamp\":1493575730624,\n" +
            "                  \"content\":[  \n" +
            "                     \"Container MortgageDemo successfully created with module com.redhat.bpms.examples:mortgage:1.\"\n" +
            "                  ]\n" +
            "               }\n" +
            "            ],\n" +
            "            \"container-id\":\"MortgageDemo\",\n" +
            "            \"release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"resolved-release-id\":{  \n" +
            "               \"version\":\"2\",\n" +
            "               \"group-id\":\"COM.REDHAT.BPMS.EXAMPLES\",\n" +
            "               \"artifact-id\":\"BIG-MORTGAGE\"\n" +
            "            },\n" +
            "            \"config-items\":[  \n" +
            "               {  \n" +
            "                  \"itemName\":\"KBase\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"KSession\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"MergeMode\",\n" +
            "                  \"itemValue\":\"MERGE_COLLECTIONS\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"RuntimeStrategy\",\n" +
            "                  \"itemValue\":\"SINGLETON\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               }\n" +
            "            ]\n" +
            "         },\n" +
            "         {  \n" +
            "            \"status\":\"STARTED\",\n" +
            "            \"scanner\":{  \n" +
            "               \"status\":\"DISPOSED\",\n" +
            "               \"poll-interval\":null\n" +
            "            },\n" +
            "            \"messages\":[  \n" +
            "\n" +
            "            ],\n" +
            "            \"container-id\":\"Mortgage2\",\n" +
            "            \"release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"resolved-release-id\":{  \n" +
            "               \"version\":\"1\",\n" +
            "               \"group-id\":\"com.redhat.bpms.examples\",\n" +
            "               \"artifact-id\":\"mortgage\"\n" +
            "            },\n" +
            "            \"config-items\":[  \n" +
            "               {  \n" +
            "                  \"itemName\":\"KBase\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"KSession\",\n" +
            "                  \"itemValue\":\"\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"MergeMode\",\n" +
            "                  \"itemValue\":\"MERGE_COLLECTIONS\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               },\n" +
            "               {  \n" +
            "                  \"itemName\":\"RuntimeStrategy\",\n" +
            "                  \"itemValue\":\"SINGLETON\",\n" +
            "                  \"itemType\":\"BPM\"\n" +
            "               }\n" +
            "            ]\n" +
            "         }\n" +
            "      ]\n" +
            "   }\n" +
            "}";

    private final static String kieContainersQueryResultJson = "{\n" +
            "  \"type\" : \"SUCCESS\",\n" +
            "  \"msg\" : \"List of created containers\",\n" +
            "  \"result\" : {\n" +
            "    \"kie-containers\" : {\n" +
            "      \"kie-container\" : [ {\n" +
            "        \"status\" : \"STARTED\",\n" +
            "        \"scanner\" : {\n" +
            "          \"status\" : \"DISPOSED\",\n" +
            "          \"poll-interval\" : null\n" +
            "        },\n" +
            "        \"messages\" : [ ],\n" +
            "        \"container-id\" : \"com.redhat.bpms.examples:mortgage:1\",\n" +
            "        \"release-id\" : {\n" +
            "          \"version\" : \"1\",\n" +
            "          \"group-id\" : \"com.redhat.bpms.examples\",\n" +
            "          \"artifact-id\" : \"mortgage\"\n" +
            "        },\n" +
            "        \"resolved-release-id\" : {\n" +
            "          \"version\" : \"1\",\n" +
            "          \"group-id\" : \"com.redhat.bpms.examples\",\n" +
            "          \"artifact-id\" : \"mortgage\"\n" +
            "        },\n" +
            "        \"config-items\" : [ {\n" +
            "          \"itemName\" : \"KBase\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"KSession\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"MergeMode\",\n" +
            "          \"itemValue\" : \"MERGE_COLLECTIONS\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"RuntimeStrategy\",\n" +
            "          \"itemValue\" : \"SINGLETON\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        } ]\n" +
            "      }, {\n" +
            "        \"status\" : \"STARTED\",\n" +
            "        \"scanner\" : {\n" +
            "          \"status\" : \"DISPOSED\",\n" +
            "          \"poll-interval\" : null\n" +
            "        },\n" +
            "        \"messages\" : [ {\n" +
            "          \"severity\" : \"INFO\",\n" +
            "          \"timestamp\" : 1493575730624,\n" +
            "          \"content\" : [ \"Container MortgageDemo successfully created with module com.redhat.bpms.examples:mortgage:1.\" ]\n" +
            "        } ],\n" +
            "        \"container-id\" : \"MortgageDemo\",\n" +
            "        \"release-id\" : {\n" +
            "          \"version\" : \"1\",\n" +
            "          \"group-id\" : \"com.redhat.bpms.examples\",\n" +
            "          \"artifact-id\" : \"mortgage\"\n" +
            "        },\n" +
            "        \"resolved-release-id\" : {\n" +
            "          \"version\" : \"2\",\n" +
            "          \"group-id\" : \"COM.REDHAT.BPMS.EXAMPLES\",\n" +
            "          \"artifact-id\" : \"BIG-MORTGAGE\"\n" +
            "        },\n" +
            "        \"config-items\" : [ {\n" +
            "          \"itemName\" : \"KBase\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"KSession\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"MergeMode\",\n" +
            "          \"itemValue\" : \"MERGE_COLLECTIONS\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"RuntimeStrategy\",\n" +
            "          \"itemValue\" : \"SINGLETON\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        } ]\n" +
            "      }, {\n" +
            "        \"status\" : \"STARTED\",\n" +
            "        \"scanner\" : {\n" +
            "          \"status\" : \"DISPOSED\",\n" +
            "          \"poll-interval\" : null\n" +
            "        },\n" +
            "        \"messages\" : [ ],\n" +
            "        \"container-id\" : \"Mortgage2\",\n" +
            "        \"release-id\" : {\n" +
            "          \"version\" : \"1\",\n" +
            "          \"group-id\" : \"com.redhat.bpms.examples\",\n" +
            "          \"artifact-id\" : \"mortgage\"\n" +
            "        },\n" +
            "        \"resolved-release-id\" : {\n" +
            "          \"version\" : \"1\",\n" +
            "          \"group-id\" : \"com.redhat.bpms.examples\",\n" +
            "          \"artifact-id\" : \"mortgage\"\n" +
            "        },\n" +
            "        \"config-items\" : [ {\n" +
            "          \"itemName\" : \"KBase\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"KSession\",\n" +
            "          \"itemValue\" : \"\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"MergeMode\",\n" +
            "          \"itemValue\" : \"MERGE_COLLECTIONS\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        }, {\n" +
            "          \"itemName\" : \"RuntimeStrategy\",\n" +
            "          \"itemValue\" : \"SINGLETON\",\n" +
            "          \"itemType\" : \"BPM\"\n" +
            "        } ]\n" +
            "      } ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
