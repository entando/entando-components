<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<div id="main">
    
    <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="style" />
    <p><s:text name="jpavatar.label.current.avatar" /></p>
    <img src="<s:url action="avatarStream" namespace="/do/currentuser/avatar"><s:param name="gravatarSize">34</s:param></s:url>"/>
    <c:if test="${style == 'local'}">
        <c:choose>
            <c:when test="${null == currentAvatar}">
                <s:form cssClass="form-horizontal" namespace="/do/jpavatar/Avatar" action="save" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <div class="col-xs-12">
                            <label for="jpavatar_file"><s:text name="label.avatarImage" /></label> 
                            <s:file name="avatar" />
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                                <span class="icon fa fa-floppy-o"></span>&#32;
                                <s:text name="label.save" />
                            </wpsf:submit>
                        </div>
                    </div>
                </s:form>
            </c:when>
            <c:otherwise>
                <s:form namespace="/do/jpavatar/Avatar" action="bin" cssClass="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                                <s:text name="%{getText('label.remove')}" />
                            </wpsf:submit>
                        </div>
                    </div>
                </s:form>
            </c:otherwise>
        </c:choose>
    </c:if>
</div>