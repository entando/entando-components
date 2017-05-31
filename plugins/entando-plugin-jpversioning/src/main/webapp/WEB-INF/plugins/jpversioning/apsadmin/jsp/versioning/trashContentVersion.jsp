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
                       data-content="<s:text name="jpversioning.admin.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>

<div id="main" class="text-center">
    <s:set var="contentVersion" value="%{getContentVersion(versionId)}" />
    <s:form action="delete">
        <p class="sr-only">
            <s:hidden name="versionId" />
            <s:hidden name="contentId" />
            <s:hidden name="backId" />
        </p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="title.versioning.delete.confirm"/></p>
        <p>
            <s:text name="message.jpversioning.confirmDelete" />&#32;<code>
            <s:property value="%{#contentVersion.version}"/>&#32;
            <s:property value="#contentVersion.contentId" /></code>?
        </p>
        <%--<div class="text-center margin-large-top">--%>
            <%--<wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">--%>
                <%--<s:text name="label.remove"/>--%>
            <%--</wpsf:submit>--%>
        <%--</div>--%>
        <%--<div class="text-center margin-large-top">--%>
            <%--<a class="btn btn-default" href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"--%>
               <%--title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">--%>
                <%--<s:text name="title.jpversioning.content" /></a>--%>
        <%--</div>--%>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width"
               href="<s:url action="list" namespace="do/jpversioning/Content/Versioning"/>  ">
                <s:text name="label.back"/>
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.remove"/>
            </wpsf:submit>
        </div>
    </s:form>
</div>
