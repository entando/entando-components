/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.ResourceBundleTextProvider;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.ValueStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Default TextProvider implementation.
 *
 * @author Jason Carreira
 * @author Rainer Hermanns
 */
public class TextProviderSupport implements ResourceBundleTextProvider {

    private Class clazz;
    private LocaleProvider localeProvider;
    private ResourceBundle bundle;
    public static ServletContext servletContext;

    /**
     * Default constructor
     */
    public TextProviderSupport() {
    }

    /**
     * Constructor.
     *
     * @param clazz a clazz to use for reading the resource bundle.
     * @param provider a locale provider.
     */
    public TextProviderSupport(Class clazz, LocaleProvider provider) {
        this.clazz = clazz;
        this.localeProvider = provider;
    }

    /**
     * Constructor.
     *
     * @param bundle the resource bundle.
     * @param provider a locale provider.
     */
    public TextProviderSupport(ResourceBundle bundle, LocaleProvider provider) {
        this.bundle = bundle;
        this.localeProvider = provider;
    }


    /**
     * @param bundle the resource bundle.
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * @param clazz a clazz to use for reading the resource bundle.
     */
    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * @param localeProvider a locale provider.
     */
    @Inject
    public void setLocaleProvider(LocaleProvider localeProvider) {
        this.localeProvider = localeProvider;
    }

    /**
     * Checks if a key is available in the resource bundles associated with this
     * action. The resource bundles are searched, starting with the one
     * associated with this particular action, and testing all its superclasses'
     * bundles. It will stop once a bundle is found that contains the given
     * text. This gives a cascading style that allow global texts to be defined
     * for an application base class.
     */
    public boolean hasKey(String key) {
        String message;
        if (clazz != null) {
            message = LocalizedTextUtil.findText(clazz, key, getLocale(), null, new Object[0]);
        } else {
            message = LocalizedTextUtil.findText(bundle, key, getLocale(), null, new Object[0]);
        }

        if (message == null && servletContext != null) {
            List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : ctxList) {
                Thread.currentThread().setContextClassLoader(classPathXmlApplicationContext.getClassLoader());
                if (clazz != null) {
                    message = LocalizedTextUtil.findText(clazz, key, getLocale(), null, new Object[0]);
                } else {
                    message = LocalizedTextUtil.findText(bundle, key, getLocale(), null, new Object[0]);
                }
            }
            Thread.currentThread().setContextClassLoader(cl);
        }

        return message != null;
    }

    /**
     * Get a text from the resource bundles associated with this action. The
     * resource bundles are searched, starting with the one associated with this
     * particular action, and testing all its superclasses' bundles. It will
     * stop once a bundle is found that contains the given text. This gives a
     * cascading style that allow global texts to be defined for an application
     * base class.
     *
     * @param key name of text to be found
     * @return value of named text or the provided key if no value is found
     */
    public String getText(String key) {
        return getText(key, key, Collections.emptyList());
    }

    /**
     * Get a text from the resource bundles associated with this action. The
     * resource bundles are searched, starting with the one associated with this
     * particular action, and testing all its superclasses' bundles. It will
     * stop once a bundle is found that contains the given text. This gives a
     * cascading style that allow global texts to be defined for an application
     * base class. If no text is found for this text name, the default value is
     * returned.
     *
     * @param key name of text to be found
     * @param defaultValue the default value which will be returned if no text
     * is found
     * @return value of named text or the provided defaultValue if no value is
     * found
     */
    public String getText(String key, String defaultValue) {
        return getText(key, defaultValue, Collections.emptyList());
    }

    /**
     * Get a text from the resource bundles associated with this action. The
     * resource bundles are searched, starting with the one associated with this
     * particular action, and testing all its superclasses' bundles. It will
     * stop once a bundle is found that contains the given text. This gives a
     * cascading style that allow global texts to be defined for an application
     * base class. If no text is found for this text name, the default value is
     * returned.
     *
     * @param key name of text to be found
     * @param defaultValue the default value which will be returned if no text
     * is found
     * @return value of named text or the provided defaultValue if no value is
     * found
     */
    public String getText(String key, String defaultValue, String arg) {
        List<Object> args = new ArrayList<Object>();
        args.add(arg);
        return getText(key, defaultValue, args);
    }

    /**
     * Get a text from the resource bundles associated with this action. The
     * resource bundles are searched, starting with the one associated with this
     * particular action, and testing all its superclasses' bundles. It will
     * stop once a bundle is found that contains the given text. This gives a
     * cascading style that allow global texts to be defined for an application
     * base class. If no text is found for this text name, the default value is
     * returned.
     *
     * @param key name of text to be found
     * @param args a List of args to be used in a MessageFormat message
     * @return value of named text or the provided key if no value is found
     */
    public String getText(String key, List<?> args) {
        return getText(key, key, args);
    }

    /**
     * Get a text from the resource bundles associated with this action. The
     * resource bundles are searched, starting with the one associated with this
     * particular action, and testing all its superclasses' bundles. It will
     * stop once a bundle is found that contains the given text. This gives a
     * cascading style that allow global texts to be defined for an application
     * base class. If no text is found for this text name, the default value is
     * returned.
     *
     * @param key name of text to be found
     * @param args an array of args to be used in a MessageFormat message
     * @return value of named text or the provided key if no value is found
     */
    public String getText(String key, String[] args) {
        return getText(key, key, args);
    }

    /**
     * Get a text from the resource bundles associated with this action. The
     * resource bundles are searched, starting with the one associated with this
     * particular action, and testing all its superclasses' bundles. It will
     * stop once a bundle is found that contains the given text. This gives a
     * cascading style that allow global texts to be defined for an application
     * base class. If no text is found for this text name, the default value is
     * returned.
     *
     * @param key name of text to be found
     * @param defaultValue the default value which will be returned if no text
     * is found
     * @param args a List of args to be used in a MessageFormat message
     * @return value of named text or the provided defaultValue if no value is
     * found
     */
    public String getText(String key, String defaultValue, List<?> args) {
        Object[] argsArray = ((args != null && !args.equals(Collections.emptyList())) ? args.toArray() : null);
        String result = null;
        if (clazz != null) {
            result = LocalizedTextUtil.findText(clazz, key, getLocale(), defaultValue, argsArray);
        } else {
            result = LocalizedTextUtil.findText(bundle, key, getLocale(), defaultValue, argsArray);
        }

        if (result == null  && servletContext != null) {
            List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : ctxList) {
                Thread.currentThread().setContextClassLoader(classPathXmlApplicationContext.getClassLoader());
                if (clazz != null) {
                    result = LocalizedTextUtil.findText(clazz, key, getLocale(), defaultValue, argsArray);
                } else {
                    result = LocalizedTextUtil.findText(bundle, key, getLocale(), defaultValue, argsArray);
                }
            }
            Thread.currentThread().setContextClassLoader(cl);
        }

        return result;
    }

    /**
     * Get a text from the resource bundles associated with this action. The
     * resource bundles are searched, starting with the one associated with this
     * particular action, and testing all its superclasses' bundles. It will
     * stop once a bundle is found that contains the given text. This gives a
     * cascading style that allow global texts to be defined for an application
     * base class. If no text is found for this text name, the default value is
     * returned.
     *
     * @param key name of text to be found
     * @param defaultValue the default value which will be returned if no text
     * is found
     * @param args an array of args to be used in a MessageFormat message
     * @return value of named text or the provided defaultValue if no value is
     * found
     */
    public String getText(String key, String defaultValue, String[] args) {

        String result = null;
        if (clazz != null) {
            result = LocalizedTextUtil.findText(clazz, key, getLocale(), defaultValue, args);
        } else {
            result = LocalizedTextUtil.findText(bundle, key, getLocale(), defaultValue, args);
        }

        if (result == null && servletContext != null) {
            List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : ctxList) {
                Thread.currentThread().setContextClassLoader(classPathXmlApplicationContext.getClassLoader());
                if (clazz != null) {
                    result = LocalizedTextUtil.findText(clazz, key, getLocale(), defaultValue, args);
                } else {
                    result = LocalizedTextUtil.findText(bundle, key, getLocale(), defaultValue, args);
                }
            }
            Thread.currentThread().setContextClassLoader(cl);
        }
        return result;
    }

    /**
     * Gets a message based on a key using the supplied args, as defined in
     * {@link java.text.MessageFormat}, or, if the message is not found, a
     * supplied default value is returned. Instead of using the value stack in
     * the ActionContext this version of the getText() method uses the provided
     * value stack.
     *
     * @param key the resource bundle key that is to be searched for
     * @param defaultValue the default value which will be returned if no
     * message is found
     * @param args a list args to be used in a {@link java.text.MessageFormat}
     * message
     * @param stack the value stack to use for finding the text
     * @return the message as found in the resource bundle, or defaultValue if
     * none is found
     */
    public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
        String result = null;
        if(key.equals("jpaddressbook.code")){
         String ddd = "";
        }
        Object[] argsArray = ((args != null) ? args.toArray() : null);
        Locale locale;
        if (stack == null) {
            locale = getLocale();
        } else {
            locale = (Locale) stack.getContext().get(ActionContext.LOCALE);
        }
        if (locale == null) {
            locale = getLocale();
        }
        if (clazz != null) {
            result = LocalizedTextUtil.findText(clazz, key, locale, defaultValue, argsArray, stack);
        } else {
            result = LocalizedTextUtil.findText(bundle, key, locale, defaultValue, argsArray, stack);
        }

        if (result == null && servletContext != null) {
            List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : ctxList) {
                Thread.currentThread().setContextClassLoader(classPathXmlApplicationContext.getClassLoader());
                //com\agiletec\plugins\jpaddressbook\apsadmin
                LocalizedTextUtil.addDefaultResourceBundle("com.agiletec.plugins.jpaddressbook.apsadmin.global-messages");
                LocalizedTextUtil.setDelegatedClassLoader(cl);
                if (clazz != null) {
                    result = LocalizedTextUtil.findText(clazz, key, locale, defaultValue, argsArray, stack);
                } else {
                    result = LocalizedTextUtil.findText(bundle, key, locale, defaultValue, argsArray, stack);
                }
            }
            Thread.currentThread().setContextClassLoader(cl);
        }

        return result;
    }

    /**
     * Gets a message based on a key using the supplied args, as defined in
     * {@link java.text.MessageFormat}, or, if the message is not found, a
     * supplied default value is returned. Instead of using the value stack in
     * the ActionContext this version of the getText() method uses the provided
     * value stack.
     *
     * @param key the resource bundle key that is to be searched for
     * @param defaultValue the default value which will be returned if no
     * message is found
     * @param args an array args to be used in a {@link java.text.MessageFormat}
     * message
     * @param stack the value stack to use for finding the text
     * @return the message as found in the resource bundle, or defaultValue if
     * none is found
     */
    public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
        String result = null;
        Locale locale;
        if (stack == null) {
            locale = getLocale();
        } else {
            locale = (Locale) stack.getContext().get(ActionContext.LOCALE);
        }
        if (locale == null) {
            locale = getLocale();
        }
        if (clazz != null) {
            result = LocalizedTextUtil.findText(clazz, key, locale, defaultValue, args, stack);
        } else {
            result = LocalizedTextUtil.findText(bundle, key, locale, defaultValue, args, stack);
        }

        if (result == null && servletContext != null) {
            List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            for (ClassPathXmlApplicationContext classPathXmlApplicationContext : ctxList) {
                Thread.currentThread().setContextClassLoader(classPathXmlApplicationContext.getClassLoader());
                if (clazz != null) {
                    result = LocalizedTextUtil.findText(clazz, key, locale, defaultValue, args, stack);
                } else {
                    result = LocalizedTextUtil.findText(bundle, key, locale, defaultValue, args, stack);
                }
            }
            Thread.currentThread().setContextClassLoader(cl);
        }

        return result;
    }

    /**
     * Get the named bundle.
     * <p/>
     * You can override the getLocale() methodName to change the behaviour of
     * how to choose locale for the bundles that are returned. Typically you
     * would use the TextProvider interface to get the users configured locale,
     * or use your own methodName to allow the user to select the locale and
     * store it in the session (by using the SessionAware interface).
     *
     * @param aBundleName bundle name
     * @return a resource bundle
     */
    public ResourceBundle getTexts(String aBundleName) {
        return LocalizedTextUtil.findResourceBundle(aBundleName, getLocale());
    }

    /**
     * Get the resource bundle associated with this action. This will be based
     * on the actual subclass that is used.
     *
     * @return resouce bundle
     */
    public ResourceBundle getTexts() {
        if (clazz != null) {
            return getTexts(clazz.getName());
        }
        return bundle;
    }

    /**
     * Get's the locale from the localeProvider.
     *
     * @return the locale from the localeProvider.
     */
    private Locale getLocale() {
        return localeProvider.getLocale();
    }

}
