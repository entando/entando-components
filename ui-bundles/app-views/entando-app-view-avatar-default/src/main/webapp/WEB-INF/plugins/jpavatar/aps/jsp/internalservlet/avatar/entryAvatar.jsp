<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="avatarResourceVar" value="avatarResource" scope="page" />
<h1><wp:i18n key="jpavatar_TITLE" /></h1>
<c:choose>
    <c:when test="${sessionScope.currentUser != 'guest'}">
        <s:if test="hasActionErrors()">
            <div class="alert alert-error">
                <h2><s:text name="message.title.ActionErrors" /></h2>
                <ul class="unstyled">
                    <s:iterator value="actionErrors">
                        <li><s:property escape="false" /></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-error">
                <h2><s:text name="message.title.FieldErrors" /></h2>
                <ul class="unstyled">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-warning">
                <h2><s:text name="messages.confirm" /></h2>
                <ul class="unstyled">
                    <s:iterator value="actionMessages">
                        <li><s:property /></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="style" />
        <div class="media">
            <span class="pull-left">
                <img class="media-object img-polaroid" src="<s:url action="avatarStream" namespace="/do/currentuser/avatar"><s:param name="gravatarSize">34</s:param></s:url>" />
            </span>
            <div class="media-body">
                <c:choose>
                    <c:when test="${style == 'gravatar'}">
                        <wp:i18n key="jpavatar_CURRENT_AVATAR" />
                    </c:when>
                    <c:when test="${style == 'local' && empty avatarResourceVar}">
                        <p class="media-heading">
                            <span class="text-warning"><wp:i18n key="jpavatar_YOU_HAVE_NO_AVATAR" /></span>
                        </p>
                        <form action="<wp:action path="/ExtStr2/do/jpavatar/Front/Avatar/save.action" />" method="post" enctype="multipart/form-data">
                              <p>
                                <label for="jpavatar_upload"><wp:i18n key="jpavatar_UPLOAD" /></label>
                            <s:file name="avatar" id="jpavatar_upload" />
                            </p>
                            <p>
                            <s:submit type="button" cssClass="btn btn-primary">
                                <wp:i18n key="jpavatar_GO_UPLOAD" />
                            </s:submit>
                            </p>
                        </form>
                    </c:when>
                    <c:when test="${style == 'local' && !(empty avatarResourceVar)}">
                        <p class="media-heading">
                        <wp:i18n key="jpavatar_CURRENT_AVATAR" />
                        </p>
                        <form action="<wp:action path="/ExtStr2/do/jpavatar/Front/Avatar/bin.action" />" method="post">
                              <p>
                            <s:submit cssClass="btn btn-warning" type="button">
                                <wp:i18n key="jpavatar_DELETE" />
                            </s:submit>
                            </p>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="media-heading">style <c:out value="${style}" /></p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:when>
</c:choose>