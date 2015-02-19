<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.admin.title" />&#32;/&#32;<s:text name="jpcrowdsourcing.title.idea.delete" /></span></h1>

<div id="main">

<s:form action="delete">
<p class="noscreen">
	<wpsf:hidden name="strutsAction" value="%{strutsAction}" />
	<wpsf:hidden name="ideaId" value="%{ideaId}" />
</p>

<s:set var="idea" value="%{getIdea(ideaId)}" />


<table class="table table-bordered">
    <tr>
	<th class="text-right"><s:text name="jpcrowdsourcing.label.title" /></th>
	<td><s:property value="#idea.title"/></td>
    </tr>
    <tr>
	<th class="text-right"><s:text name="jpcrowdsourcing.label.description" /></th>
	<td><s:property value="#idea.descr"/></td>        
    </tr>
    <tr>
	<th class="text-right"><s:text name="label.state" /></th>
		<s:if test="#idea.status == 3">
			<s:set name="iconImage" id="iconImage">icon fa fa-check text-success</s:set>
			<s:set name="isOnlineStatus" value="%{getText('label.yes')}" />
		</s:if>
		<s:if test="#idea.status == 2">
			<s:set name="iconImage" id="iconImage">icon fa fa-pause text-warning</s:set>
			<s:set name="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
		</s:if>
		<s:if test="#idea.status == 1">
			<s:set name="iconImage" id="iconImage">icon fa fa-pause text-warning</s:set>
			<s:set name="isOnlineStatus" value="%{getText('label.no')}" />
		</s:if>
                        <td><span class="<s:property value="iconImage" />" title="<s:property value="isOnlineStatus" />" /></td>
    </tr>            
    <tr>
        <th class="text-right"><s:text name="jpcrowdsourcing.label.author" /></th>
        <td><code><s:property value="#idea.username"/></code></td>                
    </tr>            
</table>

    <div class="alert alert-warning">
        <s:text name="jpcrowdsourcing.note.idea.areYouSureDelete" />
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