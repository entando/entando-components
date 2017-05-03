<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpta" uri="/jptokenapi-core" %>

<wpta:myToken var="myTokenVar" />

<h2 class="margin-more-bottom"><s:text name="title.jptokenapi.myToken" /></h2>

<div class="col-xs-12 margin-large-top">
    <legend><s:text name="label.jptokenapi.myToken" /></legend>
    <div class="form-group">
        <div class="row">
            <c:choose>
                <c:when test="${null != myTokenVar}"><code><c:out value="${myTokenVar}"/></code></c:when>
                <c:otherwise><s:text name="jptokenapi.nullToken" /></c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
