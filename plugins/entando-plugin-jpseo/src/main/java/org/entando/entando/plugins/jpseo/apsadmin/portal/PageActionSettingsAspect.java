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

import com.agiletec.apsadmin.portal.PageSettingsAction;
import com.agiletec.apsadmin.system.BaseAction;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PageActionSettingsAspect {

    private static final Logger _logger = LoggerFactory.getLogger(PageActionSettingsAspect.class);

    public static final String PARAM_ROBOT_CONTENT_CODE = "robotContent";

    @After("execution(* com.agiletec.apsadmin.admin.BaseAdminAction.configSystemParams())")
    public void executeIntiConfig(JoinPoint joinPoint) {
        if (!(joinPoint.getTarget() instanceof PageSettingsAction)) {
            return;
        }
        HttpServletRequest request = ServletActionContext.getRequest();
        PageSettingsAction action = (PageSettingsAction) joinPoint.getTarget();
        // TODO
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
            _logger.error("error saving page for seo", t);
        }
        try {
            //se il salvataggio va a buon fine, aggiorna l'oggetto
            if (null != result && result instanceof String) {
                // TODO
            }
        } catch (Throwable t) {
            _logger.error("error updating page configuration for seo", t);
            return BaseAction.FAILURE;
        }
        return result;
    }
    
}
