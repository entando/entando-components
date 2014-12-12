/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpcomponentinstaller.aps;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author M.Casari
 */
public class XmlWebApplicationContext extends org.springframework.web.context.support.XmlWebApplicationContext {

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        Map<String, T> map = super.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
        if (contexts != null) {
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                try {
                    Map<String, T> tempmap = classPathXmlApplicationContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
                    map.putAll(tempmap);
                } catch (Exception ex) {
                }
            }
        }
        return map;
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> map = super.getBeansOfType(type);
        List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
        if (contexts != null) {
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                try {
                    Map<String, T> tempmap = classPathXmlApplicationContext.getBeansOfType(type);
                    map.putAll(tempmap);
                } catch (Exception ex) {
                }
            }
        }
        return map;
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        String[] beanNames = super.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
        if (contexts != null) {
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                try {
                    String[] beanNamesTemp = classPathXmlApplicationContext.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
                    beanNames = (String[]) ArrayUtils.addAll(beanNames, beanNamesTemp);
                } catch (Exception ex) {
                }
            }
        }
        return beanNames;

    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        String[] beanNames = super.getBeanNamesForType(type);
        List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
        if (contexts != null) {
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                try {
                    String[] beanNamesTemp = classPathXmlApplicationContext.getBeanNamesForType(type);
                    beanNames = (String[]) ArrayUtils.addAll(beanNames, beanNamesTemp);
                } catch (Exception ex) {
                }
            }
        }
        return beanNames;
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        Object bean = super.getBean(name, args);
        List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
        if (contexts != null) {
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                if (bean == null) {
                    try {
                        bean = classPathXmlApplicationContext.getBean(name, args);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return bean;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        T bean = null;
        try {
            bean = super.getBean(requiredType);
        } catch (Exception e) {
            List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
            if (contexts != null) {
                for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                    if (bean == null) {
                        try {
                            bean = classPathXmlApplicationContext.getBean(requiredType);
                            return bean;
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }
        return bean;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        T bean = null;
        try {
            bean = super.getBean(name, requiredType);
        } catch (Exception e) {
            List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
            if (contexts != null) {
                for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                    if (bean == null) {
                        try {
                            bean = classPathXmlApplicationContext.getBean(name, requiredType);
                            return bean;
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }
        return bean;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = null;
        try {
            bean = super.getBean(name);
        } catch (Exception e) {
            List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
            if (contexts != null) {
                for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                    if (bean == null) {
                        try {
                            bean = classPathXmlApplicationContext.getBean(name);
                            return bean;
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }
        return bean;
    }

}
