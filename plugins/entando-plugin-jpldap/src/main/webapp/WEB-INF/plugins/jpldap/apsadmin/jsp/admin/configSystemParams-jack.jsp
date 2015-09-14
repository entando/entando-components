<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset class="col-xs-12 margin-large-top"><legend><s:text name="jpldap.name" /></legend>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_active'" />
        <div class="checkbox-inline">
            <label>
                <s:include value="/WEB-INF/apsadmin/jsp/admin/simpleCheckboxParamBlock.jsp" />
                &#32;<s:text name="label.active" />
                <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
            </label>
        </div>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_providerUrl'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.providerUrl" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_securityConnectionType'" />
        <label class="display-block"><s:text name="jpldap.hookpoint.configSystemParams.securityConnectionType" /></label>
        <div class="btn-group" data-toggle="buttons">
            <s:set name="paramValueVar" value="%{systemParams[#paramName]}" />
            <s:set name="selectedOptionVar" value="#paramValueVar == null || #paramValueVar == 'none'" />
            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_none" 
                       name="<s:property value="#paramName" />" value="none" <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                <s:text name="label.none" />
            </label>
            <s:set name="selectedOptionVar" value="#paramValueVar == 'ssl'" />
            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_ssl" 
                       name="<s:property value="#paramName" />" value="none" <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                <s:text name="jpldap.label.securityConnectionType.ssl" />
            </label>
            <s:set name="selectedOptionVar" value="#paramValueVar == 'tls_free'" />
            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_tls_free" 
                       name="<s:property value="#paramName" />" value="tls_free" <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                <s:text name="jpldap.label.securityConnectionType.tls_free" />
            </label>
            <s:set name="selectedOptionVar" value="#paramValueVar == 'tls'" />
            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_tls" 
                       name="<s:property value="#paramName" />" value="tls" <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                <s:text name="jpldap.label.securityConnectionType.tls" />
            </label>
            <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
        </div>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_securityPrincipal'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.securityPrincipal" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_securityCredentials'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.securityCredentials" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userObjectClass'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.userObjectClass" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userIdAttributeName'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.userIdAttributeName" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_filterGroup'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.filterGroup" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_filterGroupAttributeName'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.filterGroupAttributeName" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_searchResultMaxSize'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.searchResultMaxSize" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userEditingActive'" />
        <div class="checkbox-inline">
            <label>
                <s:include value="/WEB-INF/apsadmin/jsp/admin/simpleCheckboxParamBlock.jsp" />
                &#32;<s:text name="jpldap.hookpoint.configSystemParams.userEditingActive" />
                <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
            </label>
        </div>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userBaseDN'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.userBaseDN" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userObjectClassesCSV'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.userObjectClassesCSV" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_ouObjectClassesCSV'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.ouObjectClassesCSV" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userRealAttributeName'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.userRealAttributeName" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userPasswordAttributeName'" />
        <label for="admin-settings-area-<s:property value="#paramName"/>"><s:text name="jpldap.hookpoint.configSystemParams.userPasswordAttributeName" /></label>
        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp" />
        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
    </div>
    
    <div class="form-group">
        <s:set name="paramName" value="'jpldap_userPasswordAlgorithm'" />
        <label class="display-block"><s:text name="jpldap.hookpoint.configSystemParams.userPasswordAlgorithm" /></label>
        <div class="btn-group" data-toggle="buttons">
            <s:set name="paramValueVar" value="%{systemParams[#paramName]}" />
            <s:set name="selectedOptionVar" value="#paramValueVar == null || #paramValueVar == 'none'" />
            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                <input type="radio" class="radiocheck" id="jpldap_userPasswordAlgorithm_plain" 
                       name="<s:property value="#paramName" />" value="none" <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                <s:text name="jpldap.label.userPasswordAlgorithm.plain" />
            </label>
            <s:set name="selectedOptionVar" value="#paramValueVar == 'md5'" />
            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                <input type="radio" class="radiocheck" id="jpldap_userPasswordAlgorithm_md5" 
                       name="<s:property value="#paramName" />" value="md5" <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                <s:text name="jpldap.label.userPasswordAlgorithm.md5" />
            </label>
            <s:set name="selectedOptionVar" value="#paramValueVar == 'sha'" />
            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                <input type="radio" class="radiocheck" id="jpldap_userPasswordAlgorithm_sha" 
                       name="<s:property value="#paramName" />" value="sha" <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                <s:text name="jpldap.label.userPasswordAlgorithm.sha" />
            </label>
            <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
        </div>
    </div>
    
</fieldset>