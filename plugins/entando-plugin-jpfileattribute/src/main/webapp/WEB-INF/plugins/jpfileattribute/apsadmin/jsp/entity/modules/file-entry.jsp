<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<s:url action="results" namespace="/do/jacms/Content" var="contentListURL"/>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="${contentListURL}" title="${contentListURLTitle}">
            <s:text name="breadcrumb.jacms.content.list" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="jacms.menu.contentAdmin" />
        <%--<s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" />--%>
    </li>
</ol>
<!-- Page Title -->

<h1 class="page-title-container">
    <s:text name="jacms.menu.contentAdmin" />
    <span class="pull-right">
    </span>
</h1>
<!-- Default separator -->
<div class="form-group-separator"></div>
<br>
<div>
    <div class="col-xs-12">
        <legend class="margin-more-bottom">
            <s:text name="title.contentEditing" />
        </legend>
        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/include/snippet-content.jsp" />
    </div>

    <s:form action="save" method="post" enctype="multipart/form-data" cssClass="form-horizontal">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <wpsf:hidden name="contentOnSessionMarker" />
        <s:if test="hasFieldErrors()">
            <div class="col-xs-12">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="message.title.FieldErrors" /></strong>
                    <ul>
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                                <li><s:property escapeHtml="false" /></li>
                                </s:iterator>
                            </s:iterator>
                    </ul>
                </div>
            </div>
        </s:if>

        <div class="col-xs-12">
            <fieldset>
                <legend><s:text name="jpfileattribute.title.uploadFile" /></legend>
                <label for="upload" class="basic-mint-label">
                    <s:text name="label.file" />:
                </label>
                <s:file name="upload" id="upload" label="label.file"/>
            </fieldset>
        </div>
        <div class="col-xs-12">
            <div class="pull-right">
                <wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="btn btn-primary" />
                <wpsf:submit useTabindexAutoIncrement="true" action="backToEntryContent" value="%{getText('label.delete')}" cssClass="btn btn-danger" />
            </div>
        </div>
    </s:form>
</div>
