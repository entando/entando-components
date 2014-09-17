<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<fieldset class="col-xs-12">
    <legend><s:text name="jpfrontshortcut.name" /></legend>
    <div class="form-group">
        <s:set name="jpfrontshortcut_paramName" value="'jpfrontshortcut_activePageFrontEndEditing'" />
        <div class="checkbox">
            <input type="checkbox" class="radiocheck" id="<s:property value="#jpfrontshortcut_paramName" />" name="<s:property value="#jpfrontshortcut_paramName" />" value="true" <s:if test="systemParams.get(#jpfrontshortcut_paramName)">checked="checked"</s:if> />
            <wpsf:hidden name="%{#jpfrontshortcut_paramName + externalParamMarker}" value="true" />
            <label for="<s:property value="#jpfrontshortcut_paramName" />"><s:text name="jpfrontshortcut.label.activePageFrontEndEditing" /></label>
        </div>
    </div>
    <div class="form-group">
        <s:set name="jpfrontshortcut_paramName" value="'jpfrontshortcut_activeFrameFrontEndEditing'" />
        <div class="checkbox">
            <input type="checkbox" class="radiocheck" id="<s:property value="#jpfrontshortcut_paramName" />" name="<s:property value="#jpfrontshortcut_paramName" />" value="true" <s:if test="systemParams.get(#jpfrontshortcut_paramName)">checked="checked"</s:if> />
            <wpsf:hidden name="%{#jpfrontshortcut_paramName + externalParamMarker}" value="true" />
            <label for="<s:property value="#jpfrontshortcut_paramName" />"><s:text name="jpfrontshortcut.label.activeFrameFrontEndEditing" /></label>
        </div>
    </div>
    <div class="form-group">
        <s:set name="jpfrontshortcut_paramName" value="'jpfrontshortcut_activeContentFrontEndEditing'" />
        <div class="checkbox">
            <input type="checkbox" class="radiocheck" id="<s:property value="#jpfrontshortcut_paramName" />" name="<s:property value="#jpfrontshortcut_paramName" />" value="true" <s:if test="systemParams.get(#jpfrontshortcut_paramName)">checked="checked"</s:if> />
            <wpsf:hidden name="%{#jpfrontshortcut_paramName + externalParamMarker}" value="true" />
            <label for="<s:property value="#jpfrontshortcut_paramName" />"><s:text name="jpfrontshortcut.label.activeContentFrontEndEditing" /></label>
        </div>
    </div>
    <%--
    <p>
            <s:text name="jpfrontshortcut.hookpoint.configSystemParams.reloadConfigurationInfo" />
    </p>
    --%>
</fieldset>