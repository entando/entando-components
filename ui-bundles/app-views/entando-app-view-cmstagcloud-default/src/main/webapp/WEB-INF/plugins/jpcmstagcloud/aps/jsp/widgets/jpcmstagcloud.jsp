<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jptc" uri="/jpcmstagcloud-aps-core" %>
<wp:headInfo type="CSS" info="../../plugins/jpcmstagcloud/static/css/jpcmstagcloud.css"/>
<jptc:tagCloudBuilder occurrencesVar="occurrences" cloudBeansVar="cloudBeans" />
<wp:pageWithWidget var="jptagcloud_listPage" widgetTypeCode="jpcmstagcloud_viewer_list" />
<div class="jpcmstagcloud">
    <h1><wp:i18n key="jpcmstagcloud_TITLE" /></h1>
    <c:if test="${!empty cloudBeans}">
        <ul class="cloud_list">
            <c:forEach var="cloudBean" items="${cloudBeans}">
                <li>
                    <span class="icon fa fa-tag"></span>&nbsp;
                    <a title="<wp:i18n key="jpcmstagcloud_GOTO" />&#32;<c:out value="${cloudBean.title}" />" 
                       href="<wp:url page="${jptagcloud_listPage.code}" >
                           <wp:parameter name="tagCategoryCode">
                               <c:out value="${cloudBean.cloudNode.code}" />
                           </wp:parameter>
                       </wp:url>" class="badge tag<c:out value="${cloudBean.classId}" />"><c:out value="${cloudBean.title}" />
                    </a>
                    </span>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</div>
