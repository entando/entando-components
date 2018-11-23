/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.internalservlet;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class TestBpmCaseInstanceActionBase extends TestCase{

    @Mock
    private KieBpmConfig kieBpmConfig;

    @Mock
    private BaseConfigManager configManager;

    @Mock
    private KieFormManager formManager;

    @InjectMocks
    private BpmCaseInstanceChartAction bpmCaseInstanceActionBase = new BpmCaseInstanceChartAction();

    private static String KNOWLEDGE_SOURCE_ID = "knowlege-source-id";
    private static String CONTAINER_ID = "container-id";
    private static String CASE_PATH = "case-path";
    private static String CHANNEL_PATH = "channel-path";

    private static String xmlConf = "";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        String filePath = "src/test/resources/examples/xml/kie-bpm-configuration-test.xml";
        xmlConf = new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Test
    public void testIsKieServerConfigurationNotValid() throws ApsSystemException {
        when(formManager.getKieServerConfigurations()).thenReturn(new HashMap<String, KieBpmConfig>());
        boolean valid = bpmCaseInstanceActionBase.isKieServerConfigurationValid();
        assertFalse(valid);
    }

    @Test
    public void testIsKieServerConfigurationValid() throws ApsSystemException {
        HashMap<String, KieBpmConfig> serverConfigurations = new HashMap();
        bpmCaseInstanceActionBase.setKnowledgeSourceId(KNOWLEDGE_SOURCE_ID);
        serverConfigurations.put(KNOWLEDGE_SOURCE_ID, kieBpmConfig);
        when(formManager.getKieServerConfigurations()).thenReturn(serverConfigurations);
        when(configManager.getConfigItem(anyString())).thenReturn(xmlConf);
        when(formManager.getConfigManager()).thenReturn(configManager);
         boolean valid = bpmCaseInstanceActionBase.isKieServerConfigurationValid();
        assertTrue(valid);
    }

}
