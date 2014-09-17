<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.avatarManagement" />
    </span>
</h1>

<div id="main">
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>	
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
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
    <s:if test="hasActionMessages()">
        <div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>	
            <ul class="margin-base-vertical">
                <s:iterator value="actionMessages">
                    <li><s:property/></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="style" />

    <c:choose>
        <c:when test="${style == 'gravatar'}">
            <p>
                <s:text name="jpavatar.label.current.avatar" />
            </p> 		
            <img src="<c:out value="${currentAvatar}" />"/>
        </c:when>
        <c:when test="${style == 'local'}">
            <p>
                <s:text name="jpavatar.label.current.avatar" />
            </p> 		
            <img src="<c:out value="${currentAvatar}" />"/>
            <s:if test="null == avatarResource">
                <s:form cssClass="form-horizontal" action="save" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <div class="col-xs-12">
                            <label for="jpavatar_file"><s:text name="label.avatarImage" /></label> 
                            <s:set var="fileTabIndex"><wpsa:counter /></s:set>
                            <s:file name="avatar" tabindex="%{#fileTabIndex}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                                <s:text name="%{getText('label.ok')}" />
                            </wpsf:submit>
                        </div>
                    </div>
                </s:form>
            </s:if>
            <s:else>
                <s:form action="bin" cssClass="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                                <s:text name="%{getText('label.remove')}" />
                            </wpsf:submit>
                        </div>
                    </div>
                </s:form>
            </s:else>
        </c:when>
        <c:otherwise>
            style <c:out value="${style}" />
        </c:otherwise>
    </c:choose>

</div>