<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations" /></li>
    <li><s:text name="breadcrumb.integrations.components" /></li>
    <!--<li><s:text name="menu.configure"/></li>-->
    <li class="page-title-container">
        <s:text name="jpldap.name"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpldap.name"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="jpldap.title.help" />" data-placement="left" data-original-title="">
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

<div class="mb-20">
    <s:form action="updateSystemParams">
        <s:if test="hasActionMessages()">
            <div class="alert alert-success">
                <span class="pficon pficon-ok"></span>
                <strong><s:text name="messages.confirm" /></strong>
                <s:iterator value="actionMessages">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </div>
        </s:if>
        <fieldset class="form-horizontal">
            <div class="col-xs-12">
                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_active'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>>">
                        <s:text name="label.active"/>
                    </label>
                    <div class="col-sm-9">
                        <input type="checkbox" id="admin-settings-area-<s:property value="#paramName"/>" value="true"
                               name="<s:property value="#paramName"/>" data-toggle="toggle"
                               class="radiocheck bootstrap-switch"
                               <s:if test="systemParams[#paramName]">checked="checked"</s:if> />
                        <%--<s:include value="/WEB-INF/apsadmin/jsp/admin/simpleCheckboxParamBlock.jsp"/>--%>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_providerUrl'"/>
                    <label class="col-sm-3 control-label" for="exampleInput">
                        <s:text name="jpldap.hookpoint.configSystemParams.providerUrl"/>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_securityConnectionType'"/>
                    <label class="col-sm-3 control-label">
                        <s:text name="jpldap.hookpoint.configSystemParams.securityConnectionType"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.securityConnectionType.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>

                    </label>
                    <div class="col-sm-9">
                        <div class="btn-group" data-toggle="buttons">
                            <s:set var="paramValueVar" value="%{systemParams[#paramName]}"/>
                            <s:set var="selectedOptionVar" value="#paramValueVar == null || #paramValueVar == 'none'"/>
                            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                                    <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_none"
                                           name="<s:property value="#paramName" />" value="none"
                                    <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                                <s:text name="label.none"/>
                            </label>
                            <s:set var="selectedOptionVar" value="#paramValueVar == 'ssl'"/>
                            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                                    <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_ssl"
                                           name="<s:property value="#paramName" />" value="none"
                                    <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                                <s:text name="jpldap.label.securityConnectionType.ssl"/>
                            </label>
                            <s:set var="selectedOptionVar" value="#paramValueVar == 'tls_free'"/>
                            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                                    <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_tls_free"
                                           name="<s:property value="#paramName" />" value="tls_free"
                                    <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                                <s:text name="jpldap.label.securityConnectionType.tls_free"/>
                            </label>
                            <s:set var="selectedOptionVar" value="#paramValueVar == 'tls'"/>
                            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                                    <input type="radio" class="radiocheck" id="jpldap_securityConnectionType_tls"
                                           name="<s:property value="#paramName" />" value="tls"
                                    <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                                <s:text name="jpldap.label.securityConnectionType.tls"/>
                            </label>
                            <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_securityPrincipal'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.securityPrincipal"/>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_securityCredentials'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.securityCredentials"/>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userObjectClass'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.userObjectClass"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.userObjectClass.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userIdAttributeName'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.userIdAttributeName"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.userIdAttributeName.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_filterGroup'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.filterGroup"/>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_filterGroupAttributeName'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.filterGroupAttributeName"/>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_searchResultMaxSize'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.searchResultMaxSize"/>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userEditingActive'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>>">
                        <s:text name="jpldap.hookpoint.configSystemParams.userEditingActive"/>
                    </label>
                    <div class="col-sm-9">
                        <input type="checkbox" id="admin-settings-area-<s:property value="#paramName"/>" value="true"
                               name="<s:property value="#paramName"/>" data-toggle="toggle"
                               class="radiocheck bootstrap-switch"
                               <s:if test="systemParams[#paramName]">checked="checked"</s:if> />
                        <%--<s:include value="/WEB-INF/apsadmin/jsp/admin/simpleCheckboxParamBlock.jsp"/>--%>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userBaseDN'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.userBaseDN"/>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userObjectClassesCSV'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.userObjectClassesCSV"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.userObjectClassesCSV.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_ouObjectClassesCSV'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.ouObjectClassesCSV"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.ouObjectClassesCSV.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userRealAttributeName'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.userRealAttributeName"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.userRealAttributeName.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userPasswordAttributeName'"/>
                    <label class="col-sm-3 control-label" for="admin-settings-area-<s:property value="#paramName"/>">
                        <s:text name="jpldap.hookpoint.configSystemParams.userPasswordAttributeName"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.userPasswordAttributeName.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </label>
                    <div class="col-sm-9">
                        <s:include value="/WEB-INF/apsadmin/jsp/admin/textParamBlock.jsp"/>
                        <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:set var="paramName" value="'jpldap_userPasswordAlgorithm'"/>
                    <label class="col-sm-3 control-label" for="exampleInput">
                        <s:text name="jpldap.hookpoint.configSystemParams.userPasswordAlgorithm"/>
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-placement="top"
                           data-content="<s:text name="jpldap.hookpoint.configSystemParams.userPasswordAlgorithm.tooltip"/>"
                           data-original-title="" >
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </label>
                    <div class="col-sm-9">
                        <div class="btn-group" data-toggle="buttons">
                            <s:set var="paramValueVar" value="%{systemParams[#paramName]}"/>
                            <s:set var="selectedOptionVar" value="#paramValueVar == null || #paramValueVar == 'none'"/>
                            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                                    <input type="radio" class="radiocheck" id="jpldap_userPasswordAlgorithm_plain"
                                           name="<s:property value="#paramName" />" value="none"
                                    <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                                <s:text name="jpldap.label.userPasswordAlgorithm.plain"/>
                            </label>
                            <s:set var="selectedOptionVar" value="#paramValueVar == 'md5'"/>
                            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                                    <input type="radio" class="radiocheck" id="jpldap_userPasswordAlgorithm_md5"
                                           name="<s:property value="#paramName" />" value="md5"
                                    <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                                <s:text name="jpldap.label.userPasswordAlgorithm.md5"/>
                            </label>
                            <s:set var="selectedOptionVar" value="#paramValueVar == 'sha'"/>
                            <label class="btn btn-default <s:if test="#selectedOptionVar"> active</s:if>">
                                    <input type="radio" class="radiocheck" id="jpldap_userPasswordAlgorithm_sha"
                                           name="<s:property value="#paramName" />" value="sha"
                                    <s:if test="#selectedOptionVar">checked="checked"</s:if> />
                                <s:text name="jpldap.label.userPasswordAlgorithm.sha"/>
                            </label>
                            <wpsf:hidden name="%{#paramName + externalParamMarker}" value="true"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save"/>
                </wpsf:submit>
            </div>
        </fieldset>
    </s:form>
</div>
