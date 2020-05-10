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

import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.PageAction;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.Action;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.Metatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.PageMetatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageExtraConfigDOM;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PageActionAspect {

    private static final Logger _logger = LoggerFactory.getLogger(PageActionAspect.class);

    public static final String PARAM_FRIENDLY_CODE = "friendlyCode";
    public static final String PARAM_METATAGS = "pageMetatags";
    public static final String PARAM_METATAG_ATTRIBUTE_NAMES = "pageMetatagAttributeName";
    public static final String PARAM_DESCRIPTION_PREFIX = "description_lang_";
    public static final String PARAM_DESCRIPTION_USE_DEFAULT_PREFIX = "description_useDefaultLang_";
    public static final String PARAM_KEYWORDS_PREFIX = "keywords_lang_";
    public static final String PARAM_KEYWORDS_USE_DEFAULT_PREFIX = "keywords_useDefaultLang_";
    public static final String PARAM_USE_EXTRA_DESCRIPTIONS = "useExtraDescriptions";

    private ILangManager langManager;
    private ISeoMappingManager seoMappingManager;
    private IPageManager pageManager;
    
    @Before("execution(* com.agiletec.apsadmin.portal.PageAction.validate())")
    public void executeExtraValidationParent(JoinPoint joinPoint) {
        this.executeExtraValidation(joinPoint);
    }

    @Before("execution(* com.agiletec.plugins.jacms.apsadmin.portal.PageAction.validate())")
    public void executeExtraValidation(JoinPoint joinPoint) {
        PageAction action = (PageAction) joinPoint.getTarget();
        this.checkFriendlyCode(action);
        this.extractAndSetSeoFields();
    }

    @Before("execution(* com.agiletec.apsadmin.portal.PageAction.joinExtraGroup())")
    public void executeExtraJoinExtraGroup(JoinPoint joinPoint) {
        this.extractAndSetSeoFields();
    }

    @Before("execution(* com.agiletec.apsadmin.portal.PageAction.removeExtraGroup())")
    public void executeExtraRemoveExtraGroup(JoinPoint joinPoint) {
        this.extractAndSetSeoFields();
    }

    @After("execution(* com.agiletec.apsadmin.portal.PageAction.edit())")
    public void executeExtraValueFormForEdit(JoinPoint joinPoint) {
        HttpServletRequest request = ServletActionContext.getRequest();
        PageAction action = (PageAction) joinPoint.getTarget();
        String pageCode = action.getSelectedNode();
        IPage page = action.getPage(pageCode);
        if (null != page && page.getMetadata() instanceof SeoPageMetadata) {
            SeoPageMetadata pageMetadata = (SeoPageMetadata) page.getMetadata();
            request.setAttribute(PARAM_FRIENDLY_CODE, pageMetadata.getFriendlyCode());
            request.setAttribute(PARAM_USE_EXTRA_DESCRIPTIONS, pageMetadata.isUseExtraDescriptions());
            ApsProperties props = pageMetadata.getDescriptions();
            if (null != props) {
                Iterator<Object> iter = props.keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    PageMetatag metatag = (PageMetatag) props.get(key);
                    request.setAttribute(PARAM_DESCRIPTION_PREFIX + key, metatag.getValue());
                    request.setAttribute(PARAM_DESCRIPTION_USE_DEFAULT_PREFIX + key, metatag.isUseDefaultLangValue());
                }
            }
            ApsProperties keywords = pageMetadata.getKeywords();
            if (null != keywords) {
                Iterator<Object> iter = keywords.keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    PageMetatag metatag = (PageMetatag) keywords.get(key);
                    request.setAttribute(PARAM_KEYWORDS_PREFIX + key, metatag.getValue());
                    request.setAttribute(PARAM_KEYWORDS_USE_DEFAULT_PREFIX + key, metatag.isUseDefaultLangValue());
                }
            }
            Map<String, Map<String, PageMetatag>> seoParameters = pageMetadata.getComplexParameters();
            if (null != seoParameters) {
                Lang defaultLang = this.getLangManager().getDefaultLang();
                Map<String, Map<String, PageMetatag>> metas = SeoPageExtraConfigDOM.extractRightParams(seoParameters, defaultLang);
                request.setAttribute(PARAM_METATAGS, metas);
            }
            request.setAttribute(PARAM_METATAG_ATTRIBUTE_NAMES, Metatag.getAttributeNames());
        }
    }
    
    private void extractAndSetSeoFields() {
        HttpServletRequest request = ServletActionContext.getRequest();
        SeoPageActionUtils.extractAndSetDescriptionAndKeywords(request);
        SeoPageActionUtils.extractAndSetFriendlyCode(request);
        SeoPageActionUtils.extractAndSetSeoParameters(request);
        String param = request.getParameter(PARAM_USE_EXTRA_DESCRIPTIONS);
        request.setAttribute(PARAM_USE_EXTRA_DESCRIPTIONS, param);
    }

    private void checkFriendlyCode(PageAction action) {
        HttpServletRequest request = ServletActionContext.getRequest();
        String code = request.getParameter(PARAM_FRIENDLY_CODE);
        if (null != code && code.trim().length() > 100) {
            String[] args = {"100"};
            action.addFieldError(PARAM_FRIENDLY_CODE, action.getText("jpseo.error.friendlyCode.stringlength", args));
        }
        if (null != code && code.trim().length() > 0) {
            Pattern pattern = Pattern.compile("([a-z0-9_])+");
            Matcher matcher = pattern.matcher(code);
            if (!matcher.matches()) {
                action.addFieldError(PARAM_FRIENDLY_CODE, action.getText("jpseo.error.friendlyCode.wrongCharacters"));
            }
        }
        if (null != code && code.trim().length() > 0) {
            FriendlyCodeVO vo = this.getSeoMappingManager().getReference(code);
            if (null != vo && (vo.getPageCode() == null || !vo.getPageCode().equals(action.getPageCode()))) {
                String[] args = {code};
                action.addFieldError(PARAM_FRIENDLY_CODE, action.getText("jpseo.error.page.duplicateFriendlyCode", args));
            }
        }
        request.setAttribute(PARAM_FRIENDLY_CODE, code);
    }

    @Around("execution(* com.agiletec.apsadmin.portal.PageAction.saveAndConfigure())")
    public Object executeUpdateAfterSaveAndConfigure(ProceedingJoinPoint joinPoint) {
        return this.executeUpdateAfterSave(joinPoint);
    }

    @Around("execution(* com.agiletec.apsadmin.portal.PageAction.save())")
    public Object executeUpdateAfterSave(ProceedingJoinPoint joinPoint) {
        Object result = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            _logger.error("error saving page for seo", t);
        }
        try {
            //se il salvataggio va a buon fine, aggiorna l'oggetto
            if (null != result && result instanceof String) {
                String resultStr = (String) result;
                if (resultStr.equalsIgnoreCase(Action.SUCCESS)) {
                    PageAction action = (PageAction) joinPoint.getTarget();
                    IPage seoPage = this.buildPageMetadataForUpdate(request, action);
                    if (null != seoPage) {
                        this.getPageManager().updatePage(seoPage);
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("error updating page for seo", t);
            return BaseAction.FAILURE;
        }
        return result;
    }

    protected IPage buildPageMetadataForUpdate(HttpServletRequest request, PageAction action) {
        IPage seoPage = null;
        String pagecode = action.getPageCode();
        ApsProperties descriptions = new ApsProperties();
        ApsProperties langKeywordsKey = new ApsProperties();
        String friendlyCode = request.getParameter(PARAM_FRIENDLY_CODE);
        Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
        while (langsIter.hasNext()) {
            Lang lang = (Lang) langsIter.next();
            String titleKey = PARAM_DESCRIPTION_PREFIX + lang.getCode();
            String title = request.getParameter(titleKey);
            if (null != title) {
                PageMetatag meta = new PageMetatag(lang.getCode(), "description", title.trim());
                String useDefaultLangKey = PARAM_DESCRIPTION_USE_DEFAULT_PREFIX + lang.getCode();
                String useDefaultLang = request.getParameter(useDefaultLangKey);
                meta.setUseDefaultLangValue(!lang.isDefault() && Boolean.parseBoolean(useDefaultLang));
                descriptions.put(lang.getCode(), meta);
            }
            String keywordsKey = PARAM_KEYWORDS_PREFIX + lang.getCode();
            String keywords = request.getParameter(keywordsKey);
            if (null != keywords) {
                PageMetatag meta = new PageMetatag(lang.getCode(), "keywords", keywords.trim());
                String useDefaultLangKey = PARAM_KEYWORDS_USE_DEFAULT_PREFIX + lang.getCode();
                String useDefaultLang = request.getParameter(useDefaultLangKey);
                meta.setUseDefaultLangValue(!lang.isDefault() && Boolean.parseBoolean(useDefaultLang));
                langKeywordsKey.put(lang.getCode(), meta);
            }
        }
        IPage page = this.getPageManager().getDraftPage(pagecode);
        if (null != page) {
            SeoPageMetadata pageMetadata = null;
            if (page.getMetadata() instanceof SeoPageMetadata) {
                pageMetadata = (SeoPageMetadata) page.getMetadata();
            } else {
                pageMetadata = new SeoPageMetadata(page.getMetadata());
            }
            seoPage = page;
            pageMetadata.setFriendlyCode(friendlyCode);
            pageMetadata.setDescriptions(descriptions);
            pageMetadata.setKeywords(langKeywordsKey);
            pageMetadata.setComplexParameters(SeoPageActionUtils.extractSeoParameters(request));
            pageMetadata.setUpdatedAt(new Date());
            pageMetadata.setUseExtraDescriptions(null != request.getParameter(PARAM_USE_EXTRA_DESCRIPTIONS) && request.getParameter(PARAM_USE_EXTRA_DESCRIPTIONS).equalsIgnoreCase("true"));
            seoPage.setMetadata(pageMetadata);
        }
        return seoPage;
    }
    
    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected ISeoMappingManager getSeoMappingManager() {
        return seoMappingManager;
    }

    public void setSeoMappingManager(ISeoMappingManager seoMappingManager) {
        this.seoMappingManager = seoMappingManager;
    }

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

}
