<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li class="page-title-container">
        <s:text name="jpcmstagcloud.name"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpcmstagcloud.name"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="jpcmstagcloud.name.help"/>" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<br>

<div id="main" role="main">
    <s:form action="updateSystemParams">
        <fieldset class="form-horizontal">
            <div class="form-group">
                <s:set var="paramName" value="'jpldap_active'"/>
                <label class="col-sm-2 control-label" for="maxMonthsSinceLastAccess">
                    <s:text name="jpcmstagcloud.hookpoint.configSystemParams.categoryRoot"/>
                </label>
                <div class="col-sm-10">
                    <s:set var="jpcmstagcloud_paramName" value="'jpcmstagcloud_categoryRoot'"/>
                    <s:if test="null == systemParams.get(#jpcmstagcloud_paramName) || systemParams.get(#jpcmstagcloud_paramName).trim().length() == 0">
                        <s:set var="jpcmstagcloud_categoryRootValueVar" value="'jptagcloud_categoryRoot'"/>
                    </s:if>
                    <s:else>
                        <s:set var="jpcmstagcloud_categoryRootValueVar"
                               value="systemParams.get(#jpcmstagcloud_paramName)"/>
                    </s:else>
                    <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpcmstagcloud_paramName}"
                                    id="%{#jpcmstagcloud_paramName}" value="%{#jpcmstagcloud_categoryRootValueVar}"
                                    cssClass="form-control"/>
                    <wpsf:hidden name="%{#jpcmstagcloud_paramName + externalParamMarker}" value="true"/>
                </div>
            </div>
            <div class="form-group">
                <label for="maxMonthsSinceLastAccess" class="col-sm-2 control-label">
                    <s:text name="jpcmstagcloud.hookpoint.configSystemParams.delayDays"/>:
                </label>
                <div class="col-sm-10">
                    <s:set var="jpcmstagcloud_paramName" value="'jpcmstagcloud_delayDays'"/>
                    <s:if test="null == systemParams.get(#jpcmstagcloud_paramName)">
                        <s:set var="jpcmstagcloud_delayDaysValueVar" value="'-1'"/>
                    </s:if>
                    <s:else>
                        <s:set var="jpcmstagcloud_delayDaysValueVar"
                               value="systemParams.get(#jpcmstagcloud_paramName)"/>
                    </s:else>
                    <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpcmstagcloud_paramName}"
                                    id="%{#jpcmstagcloud_paramName}" value="%{#jpcmstagcloud_delayDaysValueVar}"
                                    cssClass="form-control"/>
                    <wpsf:hidden name="%{#jpcmstagcloud_paramName + externalParamMarker}" value="true"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.save"/>
                    </wpsf:submit>
                </div>
            </div>
        </fieldset>
    </s:form>
</div>
