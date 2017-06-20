<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--<s:set var="categoryTreeStyleVar"><wp:info key="systemParam" paramName="treeStyle_category"/></s:set>--%>
<%--<s:set var="targetNS" value="%{'/do/jacms/Resource'}"/>--%>
<%--<s:set var="targetParamName" value="%{'resourceTypeCode'}"/>--%>
<%--<s:set var="targetParamValue" value="resourceTypeCode"/>--%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpversioning.admin.menu"/></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >%{resourceTypeCode}</s:param></s:url>">
            <s:property value="%{getText('title.' + resourceTypeCode + 'Management')}"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.resourceManagement.resourceTrash"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpversioning.admin.menu"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="jpversioning.admin.help"/>" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<br>

<div class="text-center">
    <s:form action="search">
        <s:set var="resourceId" value="%{resourceId}"/>
        <s:set var="resource" value="%{getTrashedResource(#resourceId)}" />

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline-text">
            <s:text name="label.delete"/>
        </p>
        <p>
            <s:text name="note.deleteResource.areYouSure"/>&#32;
            <s:property value="#resource.descr" />&#63;
        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width"
               href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>   ">
                <s:text name="label.back"/>
            </a>
            <a class="btn btn-danger button-fixed-width"
               href="<s:url action="remove" ><s:param name="resourceId" value="%{resourceId}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>">
                <s:text name="label.delete"/>
            </a>
        </div>
    </s:form>
</div>

