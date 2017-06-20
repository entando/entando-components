<%@ taglib prefix="jptc" uri="/jpcmstagcloud-aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<jptc:contentList listName="contentList" />
<jptc:tagCloudBuilder occurrencesVar="occurrences" cloudBeansVar="cloudBeans" />
<wp:headInfo type="CSS" info="../../plugins/jpcmstagcloud/static/css/jpcmstagcloud.css"/>

<h1><wp:i18n key="jpcmstagcloud_TAGGEDCONTENTS"></wp:i18n> </h1>
<jacms:searcher listName="result" />
<p><wp:i18n key="jpcmstagcloud_TAG" />:&#32;

<c:forEach items="${cloudBeans}" var="tag">
    <span class="cloud_list">
        <c:if test="${tag.cloudNode.code == tagCategoryCode}">
            <span class="badge2"><c:out value="${tag.title}" /></span>
        </c:if>
    </span>
</c:forEach>
</p>
<c:if test="${!empty contentList}">
    <wp:pager listName="contentList" objectName="groupContent" pagerIdFromFrame="true" max="10" advanced="true" offset="5" >
        <c:set var="group" value="${groupContent}" scope="request" />
        <p><em><wp:i18n key="jpcmstagcloud_INTRO" />&#32;<c:out value="${groupContent.size}" />&#32;<wp:i18n key="jpcmstagcloud_OUTRO" />&#32;[<c:out value="${groupContent.currItem}" />/<c:out value="${groupContent.maxItem}" />]:</em></p>
        <c:import url="/WEB-INF/plugins/jpcmstagcloud/aps/jsp/widgets/inc/pagerBlock.jsp" />
        <ul class="text-decoration-none">
            <c:forEach var="contentId" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
                <li class="li-custom-tag"><jacms:content contentId="${contentId}" modelId="list" /></li>
            </c:forEach>
        </ul>
        <c:import url="/WEB-INF/plugins/jpcmstagcloud/aps/jsp/widgets/inc/pagerBlock.jsp" />
    </wp:pager>
</c:if>
