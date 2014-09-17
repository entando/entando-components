<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="commentVar" value="comment" />
<jacmswpsa:content contentId="%{#commentVar.contentId}" var="contentVar" />
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />"><s:text name="jpcontentfeedback.title.commentsManager" /></a>
        &#32;/&#32;<s:text name="jpcontentfeedback.label.delete.comment.confirm" />
    </span>
</h1>
<div id="main">

    <table class="table table-bordered">
        <tr>
            <th class="text-right"><s:text name="jpcontentfeedback.author" /></th>
            <td><code><s:property value="#commentVar.username"/></code></td>          
        </tr>
        <tr>           
            <th class="text-right"><s:text name="jpcontentfeedback.date.creation" /></th>
            <td><code><s:date name="#commentVar.creationDate" format="dd/MM/yyyy HH:mm" /></code></td>
        </tr>
        <tr>           
            <th class="text-right"><s:text name="jpcontentfeedback.comment" /></th>
            <td><s:property value="#commentVar.comment"/></td>
        </tr>
        <tr>          
            <th class="text-right"><s:text name="jpcontentfeedback.status" /></th>
            <td><s:property value="getAllStatus().get(#commentVar.status)" /></td>
        </tr>
        <tr>           
            <th class="text-right"><s:text name="jpcontentfeedback.content.id" /></th>
            <td><code><s:property value="#commentVar.contentId" /></code></td>
        </tr>
        <tr>            
            <th class="text-right"><s:text name="jpcontentfeedback.content.description" /></th>
            <td><s:property value="#contentVar.descr" /></td>
        </tr>
        <tr>          
            <th class="text-right"><s:text name="jpcontentfeedback.content.type" /></th>
            <td><s:property value="#contentVar.typeDescr" /> (<s:property value="#contentVar.typeCode" />)</td>
        </tr>
    </table>

    <s:form action="delete">

        <s:if test="hasActionMessages()">
            <div class="alert alert-info alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionMessages">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property/></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        
    <div class="alert alert-warning">
            <wpsf:hidden name="selectedComment" />
            <wpsf:hidden name="strutsAction" />
        <s:text name="jpcontentfeedback.label.delete.comment.confirm" />
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
