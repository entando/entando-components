<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.avatarManagement" />
    </span>
</h1>
<div id="main">
    <s:form action="delete" class="form-horizontal">            
        <div class="alert alert-warning">
            <s:text name="jpavatar.label.confirm.delete" />&#32;
            <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  />
            <c:if test="${null != currentAvatar}"><img src="<c:out value="${currentAvatar}" />"/></c:if>
            <div class="text-center margin-large-top">
                <s:submit type="button" cssClass="btn btn-warning btn-lg" >
                    <span class="icon fa fa-times-circle"></span>&#32;
                    <s:text name="%{getText('label.remove')}" />
                </s:submit>
            </div>    
            <p class="text-center margin-small-top">
                <a class="btn btn-link" href="<s:url action="edit"/>">
                    <s:text name="label.back" />
                </a>
            </p>
        </div>
    </s:form>
</div>