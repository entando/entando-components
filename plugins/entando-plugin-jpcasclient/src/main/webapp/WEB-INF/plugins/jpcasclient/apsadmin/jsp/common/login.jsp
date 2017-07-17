<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpcc" uri="/jpcasclient" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
    <head>
        <title>Entando - Log in</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta charset="utf-8" />
        <link rel="shortcut icon" href="<wp:resourceURL />administration/img/favicon-entando.png" />
        <jsp:include page="/WEB-INF/apsadmin/jsp/common/inc/header-include.jsp" />
    </head>

    <body id="background-full" class="display-table ">
        <div class="display-cell ">
            <div class="col-md-6 v-align ">
                <div class="center1">
                    <img class="logo-entando-login" src="<wp:resourceURL />administration/img/entando-logo.svg" />
                    <p class="ux_brand"><strong>THE DXP PLATFORM</strong></p>
                    <p class="ux_brand_subtitle"> FOR UX CONVERGENCE</p>
                    <div class="spacer-login"></div>
                    <div class="entando-intro">
                        Entando is the lightest, open source Digital Experience Platform (DXP) for
                        modern applications. Entando harmonizes customer experience across the
                        omnichannel applying the techniques of modern software practices to
                        enterprise applications. Learn quickly, develop easily, deploy rapidly.
                    </div>
                    <div class="copyright-entando">Copyright 2017 <span class="entando-sm-write">Entando</span></div>
                </div>
            </div>
            <div class="col-md-6 v-align ">
                <div class="center2">
                    <s:form action="doLogin" cssClass="form-horizontal" >
                        <s:if test="hasActionErrors()">
                            <div id="actionErrorsBox" class="message message_error">
                                <div class="alert alert-danger alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                                        <span class="pficon pficon-close"></span>
                                    </button>
                                    <span class="pficon pficon-error-circle-o"></span>
                                    <strong><s:text name="message.title.ActionErrors" /></strong>
                                    <ul>
                                        <s:iterator value="actionErrors">
                                            <li>
                                                <s:property />
                                            </li>
                                        </s:iterator>
                                    </ul>
                                </div>
                            </div>  
                        </s:if>

                        <s:if test="hasFieldErrors()">
                            <div id="fieldErrorsBox">
                                <div class="alert alert-danger alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                                        <span class="pficon pficon-close"></span>
                                    </button>
                                    <span class="pficon pficon-error-circle-o"></span>
                                    <strong><s:text name="message.title.FieldErrors" /></strong>	
                                    <ul>
                                        <s:iterator value="fieldErrors">
                                            <s:iterator value="value">
                                                <li>
                                                    <s:property escapeHtml="false"/>
                                                </li>
                                            </s:iterator>
                                        </s:iterator>
                                    </ul>
                                </div>
                            </div>
                        </s:if>

                        <s:if test="#session.currentUser != null && #session.currentUser.username != 'guest'">
                            <div class="whiteBox">
                                <p>
                                    <em><s:text name="note.userbar.welcome"/></em>,
                                    <strong> <s:property value="#session.currentUser" /></strong>!
                                </p>

                                <s:if test="!#session.currentUser.credentialsNotExpired">
                                    <p>
                                        <s:text name="note.login.expiredPassword.intro" />&#32;<a href="<s:url action="editPassword" />" ><s:text name="note.login.expiredPassword.outro" /></a>.
                                    </p>
                                </s:if>
                                <s:else>
                                    <wp:ifauthorized permission="enterBackend" var="checkEnterBackend" />
                                    <c:choose>
                                        <c:when test="${checkEnterBackend}">
                                            <p>
                                                <s:text name="note.login.yetLogged" />,<br />
                                                <a href="<s:url action="main" />" ><s:text name="note.goToMain" /></a> | <a href="<s:url action="logout" namespace="/do" />" ><s:text name="menu.exit"/></a>
                                            </p>
                                        </c:when>
                                        <c:otherwise>
                                            <p>
                                                <s:text name="note.login.notAllowed" />, <a href="<s:url action="logout" namespace="/do" />" ><s:text name="menu.exit"/></a>
                                            </p>

                                        </c:otherwise>
                                    </c:choose>
                                </s:else>
                            </div>
                        </s:if>

                        <s:else>
                            <div class="form-group">
                                <label for="username" class="control-label control-label-entando">
                                    <s:text name="label.username" />
                                </label>
                                <div>
                                    <wpsf:textfield useTabindexAutoIncrement="true" name="username" id="username" cssClass="entando-input" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password"class="control-label control-label-entando">
                                    <s:text name="label.password" />
                                </label>
                                <div>
                                    <wpsf:password useTabindexAutoIncrement="true" name="password" id="password" cssClass="entando-input" />
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="login-buttons">
                                    <div data-toggle="buttons">
                                        <label class="btn btn-custom-login-cas active">
                                            <input type="radio" name="request_locale" value="en" checked="checked" /> English
                                        </label>
                                        <label class="btn btn-custom-login-cas ">
                                            <input type="radio" name="request_locale" value="it" /> Italiano
                                        </label>
                                    </div>

                                    <wpsf:submit type="button" cssClass="btn btn-login pull-right">
                                        <s:text name="label.signin" />
                                    </wpsf:submit>
                                    <jpcc:CasConfigParamTag var="jpcasclient_is_active" param="active" />
                                    <c:if test="${jpcasclient_is_active}">
                                        <a class="btn btn-login-cas" href="<jpcc:CasConfigParamTag param="casLoginURL" />?service=<wp:url paramRepeat="true"  />"><s:text name="label.signin.cas" /></a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </s:else>
            </s:form>
        </div>
    </body>
</html>
                    