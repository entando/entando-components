/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.apsadmin.portal;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.portal.PageSettingsAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

/**
 * @author E.Santoboni
 */
public class PageSettingsActionAspectTest {

    private static final String CONFIG_PARAMETER
            = "<Params><Param name=\"param_1\">value_1</Param><Param name=\"param_2\">value_2</Param></Params>";

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Mock
    private ConfigInterface configManager;

    @Mock
    private IStorageManager storageManager;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private TextProvider textProvider;

    @InjectMocks
    private PageSettingsAction pageSettingsAction;

    @InjectMocks
    private PageSettingsActionAspect actionAspect;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ActionContext actionContext = Mockito.mock(ActionContext.class);
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        request.setSession(new MockHttpSession(servletContext));
        when(actionContext.getLocale()).thenReturn(Locale.ENGLISH);
        ValueStack valueStack = Mockito.mock(ValueStack.class);
        Map<String, Object> context = new HashMap<>();
        Container container = Mockito.mock(Container.class);
        XWorkConverter conv = Mockito.mock(XWorkConverter.class);
        when(container.getInstance(XWorkConverter.class)).thenReturn(conv);
        when(conv.convertValue(Mockito.any(Map.class), Mockito.any(Object.class), Mockito.any(Class.class))).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "VALUE";
            }
        });
        context.put(ActionContext.CONTAINER, container);
        when(valueStack.getContext()).thenReturn(context);
        when(actionContext.getValueStack()).thenReturn(valueStack);
        when(actionContext.get(ServletActionContext.HTTP_REQUEST)).thenReturn(request);
        ServletActionContext.setContext(actionContext);
        when(joinPoint.getTarget()).thenReturn(pageSettingsAction);
        when(textProvider.getText(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn("text");
    }

    @After
    public void tearDown() throws Exception {
        this.request.removeAllParameters();
        String path = System.getProperty("java.io.tmpdir") + File.separator + "robot.txt";
        File tempFile = new File(path);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void executeInitConfig_1() throws ApsSystemException {
        String path = System.getProperty("java.io.tmpdir") + File.separator + "robot.txt";
        when(configManager.getParam(JpseoSystemConstants.ROBOT_ALTERNATIVE_PATH_PARAM_NAME)).thenReturn(path);
        actionAspect.executeInitConfig(joinPoint);
        Mockito.verify(storageManager, Mockito.times(0)).exists(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertTrue(pageSettingsAction.hasFieldErrors());
        Assert.assertEquals(1, pageSettingsAction.getFieldErrors().get(PageSettingsActionAspect.PARAM_ROBOT_CONTENT_CODE).size());
    }

    @Test
    public void executeInitConfig_2() throws ApsSystemException {
        String path = System.getProperty("java.io.tmpdir") + File.separator + "meta-inf" + File.separator + "robot.txt";
        when(configManager.getParam(JpseoSystemConstants.ROBOT_ALTERNATIVE_PATH_PARAM_NAME)).thenReturn(path);
        actionAspect.executeInitConfig(joinPoint);
        Mockito.verify(storageManager, Mockito.times(0)).exists(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertTrue(pageSettingsAction.hasFieldErrors());
        Assert.assertEquals(1, pageSettingsAction.getFieldErrors().get(PageSettingsActionAspect.PARAM_ROBOT_ALTERNATIVE_PATH_CODE).size());
    }

    @Test
    public void executeInitConfig_3() throws ApsSystemException {
        this.request.getSession().setAttribute(PageSettingsActionAspect.SESSION_PARAM_ROBOT_ALTERNATIVE_PATH_CODE_ERROR, "Message");
        String path = System.getProperty("java.io.tmpdir") + File.separator + "robot.txt";
        when(configManager.getParam(JpseoSystemConstants.ROBOT_ALTERNATIVE_PATH_PARAM_NAME)).thenReturn(path);
        actionAspect.executeInitConfig(joinPoint);
        Mockito.verify(storageManager, Mockito.times(0)).exists(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertTrue(pageSettingsAction.hasFieldErrors());
        Assert.assertEquals(1, pageSettingsAction.getFieldErrors().get(PageSettingsActionAspect.PARAM_ROBOT_ALTERNATIVE_PATH_CODE).size());
        Assert.assertEquals("Message", pageSettingsAction.getFieldErrors().get(PageSettingsActionAspect.PARAM_ROBOT_ALTERNATIVE_PATH_CODE).get(0));
    }

    @Test
    public void executeUpdateSystemParams_1() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        actionAspect.executeUpdateSystemParams(joinPoint);
        Mockito.verify(storageManager, Mockito.times(0)).saveFile(Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(InputStream.class));
        Mockito.verify(storageManager, Mockito.times(1)).deleteFile(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertFalse(pageSettingsAction.hasFieldErrors());
        Mockito.verify(configManager, Mockito.times(1)).getConfigItem(ArgumentMatchers.anyString());
    }

    @Test
    public void executeUpdateSystemParams_2() throws Exception {
        this.request.setParameter(PageSettingsActionAspect.PARAM_ROBOT_CONTENT_CODE, "Robot content");
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        actionAspect.executeUpdateSystemParams(joinPoint);
        Mockito.verify(storageManager, Mockito.times(1)).saveFile(Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(InputStream.class));
        Mockito.verify(storageManager, Mockito.times(0)).deleteFile(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertFalse(pageSettingsAction.hasFieldErrors());
        Mockito.verify(configManager, Mockito.times(1)).getConfigItem(ArgumentMatchers.anyString());
    }

    @Test
    public void executeUpdateSystemParams_3() throws Exception {
        String path = System.getProperty("java.io.tmpdir") + File.separator + "robot.txt";
        this.request.setParameter(PageSettingsActionAspect.PARAM_ROBOT_ALTERNATIVE_PATH_CODE, path);
        this.request.setParameter(PageSettingsActionAspect.PARAM_ROBOT_CONTENT_CODE, "Robot content");
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        actionAspect.executeUpdateSystemParams(joinPoint);
        Mockito.verify(storageManager, Mockito.times(0)).saveFile(Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(InputStream.class));
        Mockito.verify(storageManager, Mockito.times(0)).deleteFile(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertFalse(pageSettingsAction.hasFieldErrors());
        Mockito.verify(configManager, Mockito.times(1)).getConfigItem(ArgumentMatchers.anyString());

    }

    @Test
    public void executeUpdateSystemParams_4() throws Exception {
        String path = System.getProperty("java.io.tmpdir") + File.separator + "meta-inf" + File.separator + "robot.txt";
        this.request.setParameter(PageSettingsActionAspect.PARAM_ROBOT_ALTERNATIVE_PATH_CODE, path);
        this.request.setParameter(PageSettingsActionAspect.PARAM_ROBOT_CONTENT_CODE, "Robot content");
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        actionAspect.executeUpdateSystemParams(joinPoint);
        Mockito.verify(storageManager, Mockito.times(0)).saveFile(Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(InputStream.class));
        Mockito.verify(storageManager, Mockito.times(0)).deleteFile(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertTrue(pageSettingsAction.hasFieldErrors());
        Assert.assertEquals(1, pageSettingsAction.getFieldErrors().get(PageSettingsActionAspect.PARAM_ROBOT_ALTERNATIVE_PATH_CODE).size());
        Mockito.verify(configManager, Mockito.times(1)).updateConfigItem(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void executeUpdateSystemParams_9() throws ApsSystemException {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        Mockito.doThrow(ApsSystemException.class).when(configManager).updateConfigItem(Mockito.anyString(), Mockito.anyString());
        actionAspect.executeUpdateSystemParams(joinPoint);
        Mockito.verify(storageManager, Mockito.times(1)).deleteFile(Mockito.anyString(), Mockito.anyBoolean());
        Assert.assertFalse(pageSettingsAction.hasFieldErrors());
        Assert.assertTrue(pageSettingsAction.hasActionErrors());
    }

}
