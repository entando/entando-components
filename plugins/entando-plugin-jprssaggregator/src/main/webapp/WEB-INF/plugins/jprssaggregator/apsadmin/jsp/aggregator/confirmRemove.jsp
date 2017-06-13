<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li>
        <s:text name="breadcrumb.integrations.components"/>
    </li>
    <li>
        <a href="<s:url namespace="/do/jprssaggregator/Aggregator" action="list" />">
            <s:text name="jprssaggregator.title.rssAggregator.rssManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="label.remove" />
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="label.remove" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="doDelete">
        <p class="sr-only">
            <wpsf:hidden name="code" />
        </p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="jprssaggregator.title.rssAggregator.remove" /></p>
        <p  class="esclamation-underline-text">
            <s:text name="message.are.you.sure.to.delete"/>&#32;<s:property value="%{code}" />?
        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" />">
                <s:text name="label.back"/>
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete" />
            </wpsf:submit>


        </div>
    </s:form>
</div>
