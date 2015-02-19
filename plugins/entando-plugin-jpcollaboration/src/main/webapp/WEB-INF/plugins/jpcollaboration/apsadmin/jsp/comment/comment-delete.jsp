<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.admin.title" />&#32;/&#32;<s:text name="jpcrowdsourcing.title.comments.delete" /></h1>

<div id="main">

<s:form action="delete" class="form-horizontal">
<p class="noscreen">
	<wpsf:hidden name="strutsAction" value="%{strutsAction}" />
	<wpsf:hidden name="commentId" value="%{commentId}" />
</p>

<s:set var="comment" value="%{getComment(commentId)}" />


<table class="table table-bordered">
    <tr>
	<th class="text-right"><s:text name="jpcrowdsourcing.label.description" /></th>
	<td><s:property value="#comment.comment"/></td>
    </tr>
    <tr>
        <th class="text-right"><s:text name="jpcrowdsourcing.label.date" /></th>
        <td><code><s:property value="#comment.creationDate"/></code></td>
    </tr>
    <tr>
	<th class="text-right"><s:text name="label.state" /></th>
        <td><s:property value="#comment.status"/></td>
    </tr>
    <tr>
        <th class="text-right"><s:text name="jpcrowdsourcing.label.author" /></th>
        <td><code><s:property value="#comment.username"/></code></td>
    </tr>
</table>

    <div class="alert alert-warning">
        <s:text name="jpcrowdsourcing.note.areYouSureDelete" />
        <div class="text-center margin-large-top">
            <s:submit type="button" cssClass="btn btn-warning btn-lg" >
                <span class="icon fa fa-times-circle"></span>&#32;
                <s:text name="%{getText('label.remove')}" />
            </s:submit>
        </div>    
        <p class="text-center margin-small-top">
            <a class="btn btn-link" href="<s:url action="list"/>">
                <s:text name="%{getText('label.back')}"/>
            </a>
        </p>
    </div>
</s:form>
</div>