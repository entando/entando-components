<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set var="mykey" value="'description_lang'+#lang.code" />
<s:set var="fieldErrorsVar" value="%{fieldErrors[#mykey]}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <label class="col-sm-2 control-label" for="description_lang<s:property value="code" />">
        <s:text name="jpseo.label.pageDescription" />
    </label>
    <div class="col-sm-10">
        <wpsf:textfield name="%{'description_lang'+#lang.code}" id="%{'description_lang'+code}" value="%{#attr[#mykey]}" cssClass="form-control" />
        <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
            </span>
        </s:if>
    </div>
</div>

<s:if test="%{#lang.default}" >
<hr />

<div class="form-group">
    <label class="col-sm-2 control-label" for="new_metatag">
        ADD Metatags
    </label>
    <div class="col-sm-10">
        
        <div class="col-sm-5">
            <s:set var="fieldErrorsVar" value="%{fieldErrors['new_metatag_key']}" />
            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
            <div class="form-group<s:property value="#controlGroupErrorClass" />">
                <label class="col-sm-2 control-label" for="new_metatag">
                    Key
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield name="new_metatag_key" id="new_metatag" cssClass="form-control" />
                    <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                    </s:if>
                </div>
            </div>
        </div>
        
        <div class="col-sm-5">
            <s:set var="fieldErrorsVar" value="%{fieldErrors['new_metatag_value']}" />
            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
            <div class="form-group<s:property value="#controlGroupErrorClass" />">
                <label class="col-sm-2 control-label" for="new_metatag">
                    Value
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield name="new_metatag_value" cssClass="form-control" />
                    <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                    </s:if>
                </div>
            </div>
        </div>
        
        <div class="col-sm-2"> 
            <wpsf:submit name="/do/jpseo/Page" action="addMetatag" type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.add" />
            </wpsf:submit>
        </div>
        
    </div>
</div>
</s:if>
