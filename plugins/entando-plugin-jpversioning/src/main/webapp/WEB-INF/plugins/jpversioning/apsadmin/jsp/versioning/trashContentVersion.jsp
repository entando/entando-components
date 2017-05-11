<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpversioning.admin.menu" /></li>
    <li>
        <a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
            <s:text name="title.jpversioning.content" /></a>
    </li>
    <li class="page-title-container"><s:text name="title.jpversioning.delete.confirm" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="page-title-container">
                <s:text name="jpversioning.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="TO be inserted" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>

<div id="main">
    <s:set var="contentVersion" value="%{getContentVersion(versionId)}" />
    <s:form action="delete">
        <p class="sr-only">
            <s:hidden name="versionId" />
            <s:hidden name="contentId" />
            <s:hidden name="backId" />
        </p>
        <div class="alert alert-warning">
            <s:text name="message.jpversioning.confirmDelete" />&#32;<code><s:property value="%{#contentVersion.version}"/>&#32;<s:property value="#contentVersion.contentId" /></code>?
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" >
                    <s:text name="label.remove" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
