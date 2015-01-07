/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcomponentinstaller.aps;

import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.spring.SpringObjectFactory;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.spring.ClassReloadingXMLWebApplicationContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 *
 * @author casari
 */
public class StrutsSpringObjectFactory extends SpringObjectFactory {

    private static final Logger LOG = LoggerFactory.getLogger(StrutsSpringObjectFactory.class);

    //@Inject
    //public StrutsSpringObjectFactory(
    //        @Inject(value=StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_AUTOWIRE,required=false) String autoWire,
    //        @Inject(value=StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_USE_CLASS_CACHE,required=false) String useClassCacheStr,
    //        @Inject ServletContext servletContext) {
    //    this(autoWire, "false", useClassCacheStr, servletContext);
    //}
    /**
     * Constructs the spring object factory
     *
     * @param autoWire The type of autowiring to use
     * @param alwaysAutoWire Whether to always respect the autowiring or not
     * @param useClassCacheStr Whether to use the class cache or not
     * @param servletContext The servlet context
     * @since 2.1.3
     */
    @Inject
    public StrutsSpringObjectFactory(
            @Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_AUTOWIRE, required = false) String autoWire,
            @Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_AUTOWIRE_ALWAYS_RESPECT, required = false) String alwaysAutoWire,
            @Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_USE_CLASS_CACHE, required = false) String useClassCacheStr,
            @Inject ServletContext servletContext,
            @Inject(StrutsConstants.STRUTS_DEVMODE) String devMode,
            @Inject Container container) {

        super();
        boolean useClassCache = "true".equals(useClassCacheStr);
        if (LOG.isInfoEnabled()) {
            LOG.info("Initializing Struts-Spring integration...");
        }

        Object rootWebApplicationContext = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        if (rootWebApplicationContext instanceof RuntimeException) {
            RuntimeException runtimeException = (RuntimeException) rootWebApplicationContext;
            LOG.fatal(runtimeException.getMessage());
            return;
        }

        ApplicationContext appContext = (ApplicationContext) rootWebApplicationContext;
        if (appContext == null) {
            // uh oh! looks like the lifecycle listener wasn't installed. Let's inform the user
            String message = "********** FATAL ERROR STARTING UP STRUTS-SPRING INTEGRATION **********\n"
                    + "Looks like the Spring listener was not configured for your web app! \n"
                    + "Nothing will work until WebApplicationContextUtils returns a valid ApplicationContext.\n"
                    + "You might need to add the following to web.xml: \n"
                    + "    <listener>\n"
                    + "        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>\n"
                    + "    </listener>";
            LOG.fatal(message);
            return;
        }

        String watchList = container.getInstance(String.class, "struts.class.reloading.watchList");
        String acceptClasses = container.getInstance(String.class, "struts.class.reloading.acceptClasses");
        String reloadConfig = container.getInstance(String.class, "struts.class.reloading.reloadConfig");

        if ("true".equals(devMode)
                && StringUtils.isNotBlank(watchList)
                && appContext instanceof ClassReloadingXMLWebApplicationContext) {
            //prevent class caching
            useClassCache = false;

            ClassReloadingXMLWebApplicationContext reloadingContext = (ClassReloadingXMLWebApplicationContext) appContext;
            reloadingContext.setupReloading(watchList.split(","), acceptClasses, servletContext, "true".equals(reloadConfig));
            if (LOG.isInfoEnabled()) {
                LOG.info("Class reloading is enabled. Make sure this is not used on a production environment!", watchList);
            }

            setClassLoader(reloadingContext.getReloadingClassLoader());

            //we need to reload the context, so our isntance of the factory is picked up
            reloadingContext.refresh();
        }

        this.setApplicationContext(appContext);

        int type = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;   // default
        if ("name".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;
        } else if ("type".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
        } else if ("auto".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;
        } else if ("constructor".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;
        } else if ("no".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_NO;
        }
        this.setAutowireStrategy(type);

        this.setUseClassCache(useClassCache);

        this.setAlwaysRespectAutowireStrategy("true".equalsIgnoreCase(alwaysAutoWire));

        if (LOG.isInfoEnabled()) {
            LOG.info("... initialized Struts-Spring integration successfully");
        }
    }

    @Override
    public Object buildBean(String beanName, Map<String, Object> extraContext, boolean injectInternal) throws Exception {

        XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) appContext;
        List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) xmlWebApplicationContext.getServletContext().getAttribute("pluginsContextsList");

        Object o = null;
        if (appContext.containsBean(beanName)) {
            o = appContext.getBean(beanName);
        } else {
            if (contexts != null) {
                for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                    if (classPathXmlApplicationContext.containsBean(beanName)) {
                        try {
                            o = classPathXmlApplicationContext.getBean(beanName);
                            return o;
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }

        if (o == null) {
            Class beanClazz = getClassInstance(beanName);
            o = buildBean(beanClazz, extraContext);
        }
        if (injectInternal) {
            injectInternalBeans(o);
        }

        return o;
    }

}
