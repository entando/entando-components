<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpta" uri="/jptokenapi-core" %>

<wpta:myToken var="myTokenVar" />

<legend><s:text name="title.jptokenapi.myToken" /></legend>

<div class="row form-horizontal">
    <label class="col-sm-2 control-label">
        <s:text name="label.jptokenapi.myToken" />
    </label>
    <div class="col-sm-10">
        <c:choose>
            <c:when test="${null != myTokenVar}"><code><c:out value="${myTokenVar}"/></code></c:when>
            <c:otherwise><s:text name="jptokenapi.nullToken" /></c:otherwise>
        </c:choose>
    </div>
</div>