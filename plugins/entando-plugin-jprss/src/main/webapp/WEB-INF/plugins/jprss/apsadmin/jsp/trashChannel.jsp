<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jprss/Rss" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="jprss.title.rssManagement" />">
            <s:text name="jprss.title.rssManagement" />
        </a>
    </li>
    <li class="page-title-container"><s:text name="label.delete"/></li>
</ol>

<h1 class="page-title-container">
    <s:text name="label.delete"/>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="delete" namespace="/do/jprss/Rss" cssClass="form-horizontal">
        <p class="sr-only"><wpsf:hidden name="id"/></p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="note.deleteChannel.areYouSure"/></p>
        <p class="esclamation-underline-text">
            <s:property value="id" />?
        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default"
               href="<s:url namespace="/do/jprss/Rss" action="list"/>"/> <s:text name="label.back" />
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete"/>
            </wpsf:submit>
        </div>

    </s:form>
</div>
