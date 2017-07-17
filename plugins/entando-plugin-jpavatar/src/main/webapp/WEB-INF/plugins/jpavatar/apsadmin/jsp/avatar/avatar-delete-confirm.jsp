<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpavatar.admin.menu.integration"/></li>
    <li>
        <s:text name="jpavatar.admin.menu.uxcomponents"/>
    </li>
    <li>
        <a href='<s:url action="management" namespace="/do/jpavatar/Config" />'>
            <s:text name="title.avatarManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="label.delete"/>
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="label.delete"/>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="delete">
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="jpavatar.label.confirm.delete"/></p>
        <p class="esclamation-underline-text">

            <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  />
            <c:if test="${null != currentAvatar}"><img src="<c:out value="${currentAvatar}" />"/></c:if>
            </p>

            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width"
                   href='<s:url action="management" namespace="/do/jpavatar/Config" />'>
                <s:text name="label.back"/>
            </a>
            <s:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete"/>
            </s:submit>
        </div>
    </s:form>
</div>
