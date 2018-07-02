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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.Metatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.PageMetatag;

public class SeoPageActionUtils {
    
    protected static Map<String, Map<String, PageMetatag>> extractSeoParameters(HttpServletRequest request) {
        Map<String, Map<String, PageMetatag>> seoParameters = new HashMap<>();
        List<Lang> langs = getLangManager(request).getLangs();
        for (Lang lang : langs) {
            Map<String, PageMetatag> langMetas = new HashMap<>();
            String mainPrefix = "pageMetataKey_" + lang.getCode() + "_";
            int index = 0;
            while (!StringUtils.isBlank(request.getParameter(mainPrefix+index))) {
                String key = request.getParameter(mainPrefix+index);
                String attributeName = request.getParameter("pageMetataAttribute_" + lang.getCode() + "_" + index);
                String value = request.getParameter("pageMetataValue_" + lang.getCode() + "_" + index);
                PageMetatag meta = new PageMetatag(lang.getCode(), key, value);
                meta.setKeyAttribute(attributeName);
                langMetas.put(key, meta);
                index++;
            }
            if (!langMetas.isEmpty()) {
                seoParameters.put(lang.getCode(), langMetas);
            }
        }
        return seoParameters;
    }
    
    protected static void extractAndSetSeoParameters(HttpServletRequest request) {
        Map<String, Map<String, PageMetatag>> seoParameters = extractSeoParameters(request);
        setSeoParameters(seoParameters, request);
    }
    
    protected static void setSeoParameters(Map<String, Map<String, PageMetatag>> seoParameters, HttpServletRequest request) {
        request.setAttribute(PageActionAspect.PARAM_METATAGS, seoParameters);
        request.setAttribute(PageActionAspect.PARAM_METATAG_ATTRIBUTE_NAMES, Metatag.getAttributeNames());
    }
    
    protected static void extractAndSetDescriptions(HttpServletRequest request) {
        Iterator<Lang> langsIter = getLangManager(request).getLangs().iterator();
        while (langsIter.hasNext()) {
            Lang lang = (Lang) langsIter.next();
            String titleKey = PageActionAspect.PARAM_DESCRIPTION_PREFIX + lang.getCode();
            String title = request.getParameter(titleKey);
            if (null != title) {
                request.setAttribute(titleKey, title);
            }
        }
    }
    
    protected static void extractAndSetFriendlyCode(HttpServletRequest request) {
        String code = request.getParameter(PageActionAspect.PARAM_FRIENDLY_CODE);
        request.setAttribute(PageActionAspect.PARAM_FRIENDLY_CODE, code);
    }

    private static ILangManager getLangManager(HttpServletRequest request) {
        return (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, request);
    }
    
}
