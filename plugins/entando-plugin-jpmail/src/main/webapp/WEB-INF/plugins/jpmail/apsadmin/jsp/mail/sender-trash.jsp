<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="breadcrumb.jpmail"/></li>
    <li> <a href="<s:url namespace="/do/jpmail/MailConfig" action="viewSenders" />"><s:text name="title.eMailManagement.sendersConfig"/></a></li>
    <li class="page-title-container"><s:text name="title.eMailManagement.deleteSender"/></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.eMailManagement.deleteSender"/>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="deleteSender">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <p class="sr-only"><wpsf:hidden name="code"/></p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="title.eMailManagement.sendersConfig.trashSender"/></p>
        <p class="esclamation-underline-text">
            <s:text name="note.sendersConfig.trash"/>&#32;-
            <s:property value="code" />?
        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width" href="<s:url action="viewSenders"/>"/>
            <s:text name="label.back" />
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete"/>
            </wpsf:submit>
        </div>
    </s:form>
</div>
