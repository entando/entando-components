<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<legend><s:text name="jpseo.label.config" /></legend>

<%-- friendlyCode --%>
<s:set var="fieldErrorsVar" value="%{fieldErrors['friendlyCode']}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <label class="col-sm-2 control-label" for="friendlyCode">
        <s:text name="jpseo.label.friendlyCode" />
        <%-- &nbsp;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i> --%>
        <%--
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.code" />" data-placement="right"><span class="fa fa-info-circle"></span></a>
        --%>
    </label>
    <div class="col-sm-10">
        <wpsf:textfield name="friendlyCode" id="friendlyCode" value="%{#attr.friendlyCode}" cssClass="form-control" />
        <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
            </span>
        </s:if>
    </div>
</div>
        
<s:if test="strutsAction != 3">
    <%-- friendlyCode --%>
    <s:set var="fieldErrorsVar" value="%{fieldErrors['xmlConfig']}" />
    <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <label class="col-sm-2 control-label" for="xmlConfig">
            <s:text name="jpseo.label.xmlConfig" />
            <%-- &nbsp;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i> --%>
            <%--
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.code" />" data-placement="right"><span class="fa fa-info-circle"></span></a>
            --%>
        </label>
        <div class="col-sm-10">
            <wpsf:textarea cols="50" rows="5" name="xmlConfig" id="xmlConfig" value="%{#attr.xmlConfig}" cssClass="form-control" />
            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                </span>
            </s:if>
            <br />
            <s:set var="seoParametersExampleVar" >
                <s:text name="jpseo.label.example" />:
                <seoparameters>
                    <parameter key="key1"><![CDATA[VALUE_1]]></parameter>
                    <parameter key="key2">VALUE_2</parameter>
                    <parameter key="key3">
                        <property key="en">VALUE_3 EN</property>
                        <property key="it">VALUE_3 IT</property>
                    </parameter>
                    <parameter key="key4">VALUE_4</parameter>
                    <parameter key="key5">
                        <property key="en">VALUE_5 EN</property>
                        <property key="it"><![CDATA[VALUE_5 IT]]></property>
                        <property key="fr">VALUE_5 FR</property>
                    </parameter>
                    <parameter key="key6"><![CDATA[VALUE_6]]></parameter>
                </seoparameters>
            </s:set>
            <pre>
                <s:property value="#seoParametersExampleVar" escapeXml="true" />
            </pre>
        </div>
    </div>
</s:if>

<legend><s:text name="jpseo.label.descriptions" /></legend>
<s:iterator value="langs" var="lang">
    <%-- lang --%>
    <s:set var="mykey" value="'description_lang'+#lang.code" />
    <s:set var="fieldErrorsVar" value="%{fieldErrors[#mykey]}" />
    <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <label class="col-sm-2 control-label" for="description_lang<s:property value="code" />">
            <code class="label label-info" ><s:property value="#lang.code" /></code></abbr>&#32;<s:text name="jpseo.label.pageDescription" />
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
</s:iterator>

<div class="form-checkbox form-group<s:property value="#controlGroupErrorClass" />">
    <div class="col-sm-3 control-label">
        <label class="display-block" for="useExtraDescriptions"><s:text name="jpseo.label.useBetterDescriptions" />
            <%--
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="jpseo.label.useBetterDescriptions" />" data-placement="left">
                <span class="fa fa-info-circle"></span>
            </a>
            --%>
        </label>
    </div>
    <div class="col-sm-4">
        <wpsf:checkbox name="useExtraDescriptions" id="useExtraDescriptions" value="#attr.useExtraDescriptions" cssClass="bootstrap-switch" />
    </div>
</div>
