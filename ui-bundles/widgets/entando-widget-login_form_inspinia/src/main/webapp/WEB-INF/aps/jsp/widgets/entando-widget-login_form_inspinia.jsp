<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />
<wp:headInfo type="JS" info="entando-misc-bootstrap/bootstrap.min.js" />

<ul class="nav pull-right">
    <li class="span2 dropdown<c:if test="${accountExpired || wrongAccountCredential}"> open</c:if>">
    <c:choose>
        <c:when test="${sessionScope.currentUser != 'guest'}">
            <div class="btn-group">
                <button class="btn span2 text-left dropdown-toggle" data-toggle="dropdown">
                    <c:out value="${sessionScope.currentUser}"/>
                    <span class="caret pull-right"></span>
                </button>
                <ul class="dropdown-menu pull-right well-small">
                    <li class="padding-medium-vertical">
                    <wp:ifauthorized permission="enterBackend">
                        <p>
                            <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<wp:info key="currentLang" />&amp;backend_client_gui=advanced"><span class="icon-wrench"></span> <wp:i18n key="ESLF_ADMINISTRATION" /></a>
                        </p>
                    </wp:ifauthorized>
                    <div class="divider"></div>
                    <p class="help-block text-right">
                        <a class="btn" href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/logout.action"><wp:i18n key="ESLF_SIGNOUT" /></a>
                    </p>

                    <wp:pageWithWidget var="editProfilePageVar" widgetTypeCode="userprofile_editCurrentUser" />
                    <c:if test="${null != editProfilePageVar}" >
                        <p class="help-block text-right">
                            <a href="<wp:url page="${editProfilePageVar.code}" />" ><wp:i18n key="ESLF_PROFILE_CONFIGURATION" /></a>
                        </p>
                    </c:if>

                    </li>
                </ul>
            </div>
        </c:when>
        <c:otherwise>
            <a class="dropdown-toggle text-right" data-toggle="dropdown" href="#"><wp:i18n key="ESLF_SIGNIN" /> <span class="caret"></span></a>
            <ul class="dropdown-menu well-small">
                <li>
                    <form class="form-vertical" method="POST">
                        <c:if test="${accountExpired}">
                            <div class="alert alert-error">
                                <button class="close" data-dismiss="alert">x</button>
                                <wp:i18n key="ESLF_USER_STATUS_EXPIRED" />
                            </div>
                        </c:if>
                        <c:if test="${wrongAccountCredential}">
                            <div class="alert alert-error">
                                <button class="close" data-dismiss="alert">x</button>
                                <wp:i18n key="ESLF_USER_STATUS_CREDENTIALS_INVALID" />
                            </div>
                        </c:if>
                        <input type="text" name="username" class="input-large" placeholder="<wp:i18n key="ESLF_USERNAME" />">
                               <input type="password" name="password" class="input-large" placeholder="<wp:i18n key="ESLF_PASSWORD" />">
                               <p class="text-right">
                            <input type="submit" class="btn btn-primary" value="<wp:i18n key="ESLF_SIGNIN" />" />
                        </p>
                    </form>
                </li>
            </ul>
        </c:otherwise>
    </c:choose>
</li>
</ul>
