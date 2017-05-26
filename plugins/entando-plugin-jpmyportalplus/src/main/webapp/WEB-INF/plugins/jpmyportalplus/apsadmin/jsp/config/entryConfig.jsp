<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ page contentType="charset=UTF-8" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li class="page-title-container">
        <s:text name="jpmyportalplus.menu.config"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.jpmyportalplus"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="title.jpmyportalplus.help"/>" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br/>
<br/>


<div id="main">
    <s:form action="save">

        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>

        <s:set var="showletTypeCodes" value="showletTypeCodes"/>
        <p class="noscreen">
            <s:iterator var="showletCode" value="showletTypeCodes" status="rowstatus">
                <wpsf:hidden id="showlets-%{#rowstatus.index}" name="showletTypeCodes" value="%{#showletCode}"/>
            </s:iterator>
        </p>

        <fieldset class="form-horizontal">
            <legend><s:text name="jpmyportalplus.label.configWidget"/></legend>

            <div class="form-group">
                <label class="col-xs-2 control-label" for="showletCode"><s:text
                        name="jpmyportalplus.label.addWidget"/></label>
                <div class="col-xs-10">
                    <div class="input-group">
                        <select name="showletCode" tabindex="<wpsa:counter />" id="showletCode"
                                class="form-control">
                            <s:iterator var="showletFlavour" value="showletFlavours">
                                <wpsa:set var="tmpShowletType">tmpShowletTypeValue</wpsa:set>
                                <s:iterator var="showletType" value="#showletFlavour">
                                    <s:if test="#showletType.optgroup != #tmpShowletType">
                                        <s:if test="#showletType.optgroup == 'stockShowletCode'">
                                            <wpsa:set var="optgroupLabel">
                                                <s:text name="title.widgetManagement.widgets.stock"/>
                                            </wpsa:set>
                                        </s:if>
                                        <s:elseif test="#showletType.optgroup == 'customShowletCode'">
                                            <wpsa:set var="optgroupLabel">
                                                <s:text name="title.widgetManagement.widgets.custom"/>
                                            </wpsa:set>
                                        </s:elseif>
                                        <s:elseif test="#showletType.optgroup == 'userShowletCode'">
                                            <wpsa:set var="optgroupLabel">
                                                <s:text name="title.widgetManagement.widgets.user"/>
                                            </wpsa:set>
                                        </s:elseif>
                                        <s:else>
                                            <wpsa:set var="pluginPropertyName"
                                                      value="%{getText(#showletType.optgroup + '.name')}"/>
                                            <wpsa:set var="pluginPropertyCode"
                                                      value="%{getText(#showletType.optgroup + '.code')}"/>
                                            <wpsa:set var="optgroupLabel">
                                                <s:text name="#pluginPropertyName"/>
                                            </wpsa:set>
                                        </s:else>
                                        <optgroup label="<s:property value="#optgroupLabel"/>">
                                    </s:if>
                                    <s:if test="!#showletTypeCodes.contains(#showletType.key)">
                                        <option value="<s:property value="#showletType.key" />">
                                            <s:property value="#showletType.value"/></option>
                                    </s:if>
                                    <wpsa:set var="tmpShowletType">
                                        <s:property value="#showletType.optgroup"/>
                                    </wpsa:set>
                                </s:iterator>
                                </optgroup>
                            </s:iterator>
                        </select>
                        <span class="input-group-btn">
                            <wpsf:submit action="addWidget" value="%{getText('label.add')}" cssClass="btn btn-primary"/>
                        </span>
                    </div>
                </div>
            </div>

            <s:if test="%{showletTypeCodes.size > 0}">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <th scope="col-xs-11"><s:text name="label.widget"/></th>
                            <th class="text-center col-xs-1" scope="col">
                                <s:text name="label.remove"/>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator var="widgetTypeItemVar" value="widgetTypeItems" status="rowstatus">
                            <tr>
                                <td>
                                    <s:property value="#widgetTypeItemVar.value"/>
                                </td>
                                <td class="text-center">
                                    <wpsa:actionParam action="removeWidget" var="actionName">
                                        <wpsa:actionSubParam name="widgetCode" value="%{#widgetTypeItemVar.key}"/>
                                    </wpsa:actionParam>
                                    <wpsf:submit type="button" src="%{#iconImagePath}" action="%{#actionName}"
                                                 value="%{getText('label.remove')}"
                                                 title="%{getText('label.remove') + ': ' + #widgetTypeItemVar.value}"
                                                 cssClass="btn btn-link">
                                        <span class="fa fa-trash-o fa-lg"></span>
                                    </wpsf:submit>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </s:if>
        </fieldset>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:submit type="button" value="%{getText('label.save')}" cssClass="btn btn-primary pull-right">
                        <s:text name="label.save"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
