<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:ifauthorized permission="superuser" var="isSuperuser"/>
<c:choose>
    <c:when test="${isSuperuser}">
        <li class="margin-large-bottom"><span class="h5"><s:text name="jpwebform.menu.admin" />
                <ul class="nav nav-pills nav-stacked">
                    <li><a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">jpwebformFormManager</s:param></s:url>" ><s:text name="jpwebform.menu.formTypeAdmin" /></a></li>
                    <li><a href="<s:url namespace="/do/jpwebform/Message/Operator" action="list" />" ><s:text name="jpwebform.menu.messages"/></a></li>
                    <li><a href="<s:url namespace="/do/jpwebform/Message/Config" action="list" />" ><s:text name="jpwebform.menu.config" /></a></li>
                </ul>
        </li>
    </c:when>
    <c:otherwise>
        <wp:ifauthorized permission="jpwebformOperator">
            <li class="margin-large-bottom"><a href="<s:url action="list" namespace="/do/jpwebform/Message/Operator" />" ><s:text name="jpwebform.menu.messages"/></a></li>
        </wp:ifauthorized>
    </c:otherwise>
</c:choose>