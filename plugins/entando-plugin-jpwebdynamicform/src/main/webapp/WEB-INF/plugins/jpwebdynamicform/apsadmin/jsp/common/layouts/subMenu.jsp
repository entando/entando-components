<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:ifauthorized permission="superuser" var="isSuperuser"/>
<c:choose>
    <c:when test="${isSuperuser}">
        <li class="list-group-item">
            <a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />">
                <span class="list-group-item-value">
                    <s:text name="jpwebdynamicform.menu.admin"/>
                </span>
            </a>
        </li>
    </c:when>
    <c:otherwise>
        <wp:ifauthorized permission="jpwebdynamicform_manageForms">
            <li class="list-group-item">
                <a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />">
                    <span class="list-group-item-value">
                            <s:text name="jpwebdynamicform.menu.messages"/>
                    </span>
                </a>
            </li>
        </wp:ifauthorized>
    </c:otherwise>
</c:choose>
