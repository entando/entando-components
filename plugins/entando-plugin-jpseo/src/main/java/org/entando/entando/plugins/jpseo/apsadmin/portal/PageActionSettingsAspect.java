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

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.portal.PageSettingsAction;
import com.agiletec.apsadmin.system.BaseAction;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PageActionSettingsAspect {

    private static final Logger logger = LoggerFactory.getLogger(PageActionSettingsAspect.class);

    public static final String PARAM_ROBOT_CONTENT_CODE = "robotContent";

    public static final String PARAM_ROBOT_ALTERNATIVE_PATH_CODE = "robotPath";
    
    private ConfigInterface configManager;

    private IStorageManager storageManager;

    @After("execution(* com.agiletec.apsadmin.admin.BaseAdminAction.configSystemParams())")
    public void executeIntiConfig(JoinPoint joinPoint) {
        if (!(joinPoint.getTarget() instanceof PageSettingsAction)) {
            return;
        }
        PageSettingsAction action = (PageSettingsAction) joinPoint.getTarget();
        try {
            String robotContent = "";
            String alternativePath = this.getConfigManager().getParam(JpseoSystemConstants.ROBOT_ALTERNATIVE_PATH_PARAM_NAME);
            if (StringUtils.isEmpty(alternativePath)) {
                if (this.getStorageManager().exists("robot.txt", false)) {
                    robotContent = this.getStorageManager().readFile("robot.txt", false);
                }
            } else {
                robotContent = this.readFile(alternativePath, action);
            }
            HttpServletRequest request = ServletActionContext.getRequest();
            if (null != robotContent) {
                request.setAttribute(PARAM_ROBOT_CONTENT_CODE, robotContent);
            }
            if (null != alternativePath) {
                request.setAttribute(PARAM_ROBOT_ALTERNATIVE_PATH_CODE, alternativePath);
            }
        } catch (Exception e) {
            logger.error("Error extracting robot.txt file content", e);
        }
    }
    
    protected String readFile(String path, PageSettingsAction action) {
		try {
            File file = new File(path);
            if (file.exists()) {
                return FileUtils.readFileToString(file, CharEncoding.UTF_8);
            } else {
                String message = "File '" + path + "' does not exists";
                action.addFieldError(PARAM_ROBOT_CONTENT_CODE, message);
            }
		} catch (Throwable t) {
			logger.error("Error reading File with path {}", path, t);
            String message = "Error reading File with path " + path + " - " + t.getMessage();
            action.addFieldError(PARAM_ROBOT_CONTENT_CODE, message);
		}
        return "";
	}
    
    @Around("execution(* com.agiletec.apsadmin.portal.PageSettingsAction.updateSystemParams())")
    public Object executeUpdateSystemParamsForAjax(ProceedingJoinPoint joinPoint) {
        return executeUpdateSystemParams(joinPoint);
    }
    
    @Around("execution(* com.agiletec.apsadmin.portal.PageSettingsAction.updateSystemParamsForAjax())")
    public Object executeUpdateSystemParams(ProceedingJoinPoint joinPoint) {
        Object result = null;
        System.out.println("USCITO DA EDIT");
        HttpServletRequest request = ServletActionContext.getRequest();
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            logger.error("error saving page for seo", t);
        }
        try {
            //se il salvataggio va a buon fine, aggiorna l'oggetto
            if (null != result && result instanceof String) {
                // TODO
            }
        } catch (Throwable t) {
            logger.error("error updating page settings for seo", t);
            return BaseAction.FAILURE;
        }
        return result;
    }

    protected ConfigInterface getConfigManager() {
        return configManager;
    }
    
    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    protected IStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
}
