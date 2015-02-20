<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:if test="#attribute.type == 'Author'">
    <c:set var="username" scope="request"><s:property value="#attribute.text"/></c:set>
    <c:if test="${null != username}">
        <wp:userProfileAttribute username="${username}" attributeRoleName="userprofile:firstname" var="profileName"/>
        <wp:userProfileAttribute username="${username}" attributeRoleName="userprofile:surname" var="profileSurname"/>
        <c:set var="hasProfileAuthorAttrs" value="${not empty profileName || not empty profileSurname}" />
    </c:if>
    <%-- <span class="important"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></span> --%>
    <label for="author">
        <s:property value="#attribute.name" />
    </label>
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />        
    <c:choose>
        <c:when test="${hasProfileAuthorAttrs}">
            <s:set var="author">	
                <c:out value="${profileName} ${profileSurname}" />
            </s:set>
        </c:when>
        <c:otherwise>
            <s:set var="author"><c:out value="${username}" /></s:set>
        </c:otherwise>
    </c:choose>
    <s:textfield id="author" readonly="true" value="%{author}" cssClass="form-control"/>
</s:if>
