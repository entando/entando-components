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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        Set removedPluginsSubMenuSet = (Set<String>) this.getServletContext().getAttribute("removedPluginsSubMenuSet");
        if (contexts != null) {
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {

                String[] beanNamesTemp = classPathXmlApplicationContext.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
                beanNames = (String[]) ArrayUtils.addAll(beanNames, beanNamesTemp);
            }
        }
        Set hs = new HashSet();
        for (int i = 0; i < beanNames.length; i++) {
            String beanName = beanNames[i];
            if (removedPluginsSubMenuSet != null && removedPluginsSubMenuSet.contains(beanName)) {
                continue;
            }
            hs.add(beanName);
        }
        beanNames = (String[]) hs.toArray(new String[0]);
        Arrays.sort(beanNames);
        return beanNames;

    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        String[] beanNames = super.getBeanNamesForType(type);
        List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) this.getServletContext().getAttribute("pluginsContextsList");
        Set removedPluginsSubMenuSet = (Set<String>) this.getServletContext().getAttribute("removedPluginsSubMenuSet");
        if (contexts != null) {
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
                String[] beanNamesTemp = classPathXmlApplicationContext.getBeanNamesForType(type);
                beanNames = (String[]) ArrayUtils.addAll(beanNames, beanNamesTemp);
                HashSet hs = new HashSet();
                for (int i = 0; i < beanNames.length; i++) {
                    String beanName = beanNames[i];
                    hs.add(beanName);
                }
                beanNames = (String[]) hs.toArray(new String[0]);
                Arrays.sort(beanNames);
            }
        }
        Set hs = new HashSet();
        for (int i = 0; i < beanNames.length; i++) {
            String beanName = beanNames[i];
            if (removedPluginsSubMenuSet != null && removedPluginsSubMenuSet.contains(beanName)) {
                continue;
            }
            hs.add(beanName);
        }
        beanNames = (String[]) hs.toArray(new String[0]);
        Arrays.sort(beanNames);
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
