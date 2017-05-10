<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.jpmail"/></li>
    <li><s:text name="title.eMailManagement.sendersConfig"/></li>
    <li class="page-title-container"><s:text name="title.eMailManagement.deleteSender"/></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.eMailManagement.deleteSender"/>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="TO be inserted" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="deleteSender">
        <p class="sr-only"><wpsf:hidden name="code"/></p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="title.eMailManagement.sendersConfig.trashSender"/></p>
        <p>
            <s:text name="note.sendersConfig.trash"/>&#32;
            <s:property value="code" />
        </p>
        <div class="text-center margin-large-top">
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete"/>
            </wpsf:submit>
        </div>
        <div class="text-center margin-large-top">
            <a class="btn btn-default"
               href="<s:url action="viewSenders"/>"/> <s:text name="title.eMailManagement.sendersConfig" />
            </a>
        </div>
    </s:form>
</div>