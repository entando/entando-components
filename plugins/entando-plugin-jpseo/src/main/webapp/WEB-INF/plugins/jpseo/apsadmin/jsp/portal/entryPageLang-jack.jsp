<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set var="mykey" value="'description_lang_'+#lang.code" />
<s:set var="mykeyUseDefault" value="'description_useDefaultLang_'+#lang.code" />
<s:set var="fieldErrorsVar" value="%{fieldErrors[#mykey]}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <label class="col-sm-2 control-label" for="description_lang<s:property value="#lang.code" />">
        <s:text name="jpseo.label.pageDescription" /> 
        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-placement="top" data-content="<s:text name="jpseo.pageEdit.description.help" />"
           data-original-title="">
            <span class="fa fa-info-circle"></span>
        </a>
    </label>
    <s:if test="%{#lang.default}" >
        <div class="col-sm-10">
        </s:if>
        <s:if test="%{!#lang.default}" >
            <div class="col-sm-6">
            </s:if>
            <wpsf:textfield name="%{'description_lang_'+#lang.code}" id="%{'description_lang_'+#lang.code}" value="%{#attr[#mykey]}" cssClass="form-control" />
            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
        <s:if test="%{!#lang.default}" >
            <label class="col-sm-2 control-label" for="description_useDefaultLang_<s:property value="#lang.code" />">
                <s:text name="jpseo.label.inheritFromDefaultLang" />
            </label>
            <div class="col-sm-2">
                <wpsf:checkbox
                    name="%{'description_useDefaultLang_'+#lang.code}" 
                    id="%{'description_useDefaultLang_'+#lang.code}"
                    value="%{#attr[#mykeyUseDefault]}" 
                    cssClass="radiocheck bootstrap-switch"
                    />
            </div>
        </s:if>
    </div>

    <s:set var="keywordskey" value="'keywords_lang_'+#lang.code" />
    <s:set var="keywordsUseDefault" value="'keywords_useDefaultLang_'+#lang.code" />
    <s:set var="fieldErrorsVar" value="%{fieldErrors[#keywordskey]}" />
    <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <label class="col-sm-2 control-label" for="keywords_lang<s:property value="#lang.code" />">
            <s:text name="jpseo.label.pageKeywords" />
            <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-placement="top" data-content="<s:text name="jpseo.pageEdit.keywords.help" />"
               data-original-title="">
                <span class="fa fa-info-circle"></span>
            </a>
        </label>
        <s:if test="%{#lang.default}" >
            <div class="col-sm-10">
            </s:if>
            <s:if test="%{!#lang.default}" >
                <div class="col-sm-6">
                </s:if>
                <wpsf:textfield name="%{'keywords_lang_'+#lang.code}" id="%{'keywords_lang_'+#lang.code}" value="%{#attr[#keywordskey]}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />&#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
            <s:if test="%{!#lang.default}" >
                <label class="col-sm-2 control-label" for="keywords_useDefaultLang_<s:property value="#lang.code" />">
                    <s:text name="jpseo.label.inheritFromDefaultLang" />
                </label>
                <div class="col-sm-2">
                    <wpsf:checkbox
                        name="%{'keywords_useDefaultLang_'+#lang.code}" 
                        id="%{'keywords_useDefaultLang_'+#lang.code}"
                        value="%{#attr[#keywordsUseDefault]}" 
                        cssClass="radiocheck bootstrap-switch"
                        />
                </div>
            </s:if>
        </div>

        <s:set var="pageMetatagsVar" value="#attr['pageMetatags']" /> 
        <s:if test="%{null != #pageMetatagsVar}">
            <s:set var="pageMetatagsLangVar" value="#pageMetatagsVar[#lang.code]" /> 
            <s:if test="%{null != #pageMetatagsLangVar}">
                <s:iterator value="#pageMetatagsLangVar" var="pageMetatagVar" status="rowStatus">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="metata_lang_<s:property value="#lang.code" />_<s:property value="#pageMetatagVar.key" />">
                            <wpsf:hidden name="%{'pageMetataKey_'+#lang.code+'_'+#rowStatus.index}" value="%{#pageMetatagVar.key}" />
                            <s:property value="#pageMetatagVar.key" />
                        </label>
                        <s:if test="%{#lang.default}" >
                            <div class="col-sm-4">
                            </s:if>
                            <s:if test="%{!#lang.default}" >
                                <div class="col-sm-3">
                                </s:if>
                                <wpsf:select 
                                    name="%{'pageMetataAttribute_'+#lang.code+'_'+#rowStatus.index}" 
                                    list="#attr['pageMetatagAttributeName']" value="%{#pageMetatagVar.value.keyAttribute}" 
                                    cssClass="form-control"
                                    />
                            </div>
                            <s:if test="%{#lang.default}" >
                                <div class="col-sm-4">
                                </s:if>
                                <s:if test="%{!#lang.default}" >
                                    <div class="col-sm-3">
                                    </s:if>
                                    <wpsf:textfield
                                        name="%{'pageMetataValue_'+#lang.code+'_'+#rowStatus.index}" 
                                        id="%{'metata_lang_'+#lang.code+'_'+#pageMetatagVar.key}" 
                                        value="%{#pageMetatagVar.value.value}" cssClass="form-control" 
                                        />
                                </div>
                                <s:if test="%{#lang.default}" >
                                    <div class="col-sm-12 btn-position">
                                        <wpsa:actionParam action="removeMetatag" var="actionNameVar" >
                                            <wpsa:actionSubParam name="metatagKey" value="%{#pageMetatagVar.key}" />
                                        </wpsa:actionParam>
                                        <wpsf:submit type="button" action="%{#actionNameVar}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-danger pull-right">
                                            <span class="pficon pficon-delete"></span>
                                        </wpsf:submit>
                                    </div>
                                </s:if>

                                <s:if test="%{!#lang.default}" >
                                    <label class="col-sm-2 control-label" for="pageMetataUseDefaultLang_<s:property value="#lang.code" />_<s:property value="#pageMetatagVar.key" />">
                                        <s:text name="jpseo.label.inheritFromDefaultLang" />
                                    </label>
                                    <div class="col-sm-2">
                                        <wpsf:checkbox
                                            name="%{'pageMetataUseDefaultLang_'+#lang.code+'_'+#rowStatus.index}" 
                                            id="%{'pageMetataUseDefaultLang_'+#lang.code+'_'+#pageMetatagVar.key}" 
                                            value="%{#pageMetatagVar.value.useDefaultLangValue}"
                                            cssClass="bootstrap-switch"
                                            />
                                    </div>
                                </s:if>
                            </div>

                        </s:iterator>
                    </s:if>
                </s:if>

                <!--METATAG SECTION-->
                <s:if test="%{#lang.default}" >
                    <div class="metadata-well">
                        <div class="separator"></div>

                        <div class="form-group">
                            <label class="col-sm-2 section-label" for="new_metatag">
                                <s:text name="jpseo.label.addMetatags" />
                                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                   data-placement="top" data-content="<s:text name="jpseo.pageEdit.addMetatags.help" />"
                                   data-original-title="">
                                    <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                        </div>
                        <br>

                        <div class="form-inline">
                            <s:set var="fieldErrorsVar" value="%{fieldErrors['new_metatag_key']}" />
                            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                            <div class="col-md-6 form-group<s:property value="#controlGroupErrorClass" />">
                                <label class="col-sm-4 control-label" for="new_metatag">
                                    <s:text name="label.key" />
                                </label>
                                <div class="col-sm-8">
                                    <wpsf:textfield name="metatagKey" id="new_metatag" cssClass="form-control custom-input" />
                                    <s:if test="#hasFieldErrorVar">
                                        <span class="help-block text-danger">
                                            <s:iterator value="%{#fieldErrorsVar}">
                                                <s:property />&#32;</s:iterator>
                                            </span>
                                    </s:if>
                                </div>
                            </div>
                            <s:set var="fieldErrorsVar" value="%{fieldErrors['new_metatag_value']}" />
                            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                            <div class="col-md-6 form-group<s:property value="#controlGroupErrorClass" />">
                                <label class="col-sm-3 control-label" for="new_metatag">
                                    <s:text name="label.value" />
                                </label>
                                <div class="col-sm-9">
                                    <wpsf:textfield name="metatagValue" cssClass="form-control custom-input" />
                                    <s:if test="#hasFieldErrorVar">
                                        <span class="help-block text-danger">
                                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                                            </span>
                                    </s:if>

                                </div>
                            </div>
                            <div class="col-sm-12"> 
                                <wpsf:submit action="addMetatag" type="button" cssClass="btn btn-primary pull-right btn-position">
                                    <s:text name="label.add" />
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>
                </s:if>
