<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li>
        <s:text name="jpcrowdsourcing.admin.title" />
    </li>
    <li>
        <s:text name="jpcrowdsourcing.title.comments" />
    </li>
    <li class="page-title-container">
        <s:text name="jpcrowdsourcing.title.comments.delete" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpcrowdsourcing.admin.title" />
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>


<div class="mb-20">
    <s:form action="delete">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <p class="noscreen">
            <wpsf:hidden name="strutsAction" value="%{strutsAction}" />
            <wpsf:hidden name="commentId" value="%{commentId}" />
        </p>
        <s:set var="comment" value="%{getComment(commentId)}" />
        <div class="table-responsive">
            <table class="table table-bordered no-mb">
                <tr>
                    <th class="text-right table-w-20">
                        <s:text name="jpcrowdsourcing.label.description" />
                    </th>
                    <td>
                        <s:property value="#comment.comment" />
                    </td>
                </tr>
                <tr>
                    <th class="text-right table-w-20">
                        <s:text name="jpcrowdsourcing.label.date" />
                    </th>
                    <td>
                        <s:date name="#comment.creationDate" format="dd/MM/yyyy HH:mm" />
                    </td>
                </tr>
                <tr>
                    <th class="text-right table-w-20">
                        <s:text name="label.state" />
                    </th>
                    <td>
                        <s:property value="#comment.status" />
                    </td>
                </tr>
                <tr>
                    <th class="text-right table-w-20">
                        <s:text name="jpcrowdsourcing.label.author" />
                    </th>
                    <td>
                        <s:property value="#comment.username" />
                    </td>
                </tr>
            </table>
        </div>
        <div class="text-center mt-20">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline">
                <s:text name="label.delete" />
            </p>
            <p class="esclamation-underline-text">
                <s:text name="jpcrowdsourcing.note.areYouSureDelete" />
            </p>
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" />">
                <s:text name="%{getText('label.back')}" />
            </a>
            <s:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="%{getText('label.remove')}" />
            </s:submit>
        </div>
    </s:form>
</div>
