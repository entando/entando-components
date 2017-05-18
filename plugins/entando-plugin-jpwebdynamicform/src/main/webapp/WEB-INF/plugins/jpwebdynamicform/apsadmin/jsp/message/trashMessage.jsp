<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpwebdynamicform.menu.integration"/></li>
    <li>
        <s:text name="jpwebdynamicform.menu.uxcomponents"/>
    </li>
    <li>
        <a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />">
            <s:text name="title.messageManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.messageManagement.trash" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.messageManagement.trash" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="delete">
        <p class="noscreen"><wpsf:hidden name="id"/></p>
        <s:set var="id" value="message.id" />
        <s:set var="typeDescr" value="message.typeDescr" />
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="label.delete"/></p>
        <p>
            <s:text name="title.messageManagement.trash.info" />&#32;
            <s:text name="%{#id}"/>&#32;
            <s:text name="title.messageManagement.trash.info.type" />&#32;<em><s:property value="#typeDescr"/></em>&#63;
        </p>
        <div class="btn btn-danger button-fixed-width">
            <wpsf:submit type="button" cssClass="btn btn-menu-right">
                <s:text name="label.delete"/>
            </wpsf:submit>

        </div>
        <div class="text-center margin-large-top">
            <a class="btn btn-default" href="<s:url action="list" />">
                <s:text name="title.messageManagement" />
            </a>
        </div>
    </s:form>
</div>
