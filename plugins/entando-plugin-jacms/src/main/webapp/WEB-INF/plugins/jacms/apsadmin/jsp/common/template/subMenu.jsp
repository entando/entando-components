<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser" var="isSuperUser" />
<wp:ifauthorized permission="editContents" var="isEditContents" />
<wp:ifauthorized permission="manageResources" var="isManageResources" />

<c:if test="${isEditContents || isManageResources}">
    <li class="list-group-item tertiary-nav-item-pf" data-target="apps-cms-tertiary">
        <a>
            <span class="list-group-item-value"><s:text name="menu.APPS.CMS" /></span>
        </a>
        <div id="apps-cms-tertiary" class="nav-pf-tertiary-nav">
            <div class="nav-item-pf-header">
                <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                <span><s:text name="menu.APPS.CMS" /></span>
            </div>
            <ul class="list-group">
                <c:if test="${isEditContents}">
                    <li class="list-group-item">
                        <a href="<s:url action="list" namespace="/do/jacms/Content" />">
                           <span class="list-group-item-value"><s:text name="menu.APPS.CMS.contents" /></span>
                        </a>
                    </li>
                </c:if>
                <c:if test="${isManageResources}">
                    <li class="list-group-item">
                        <a href="<s:url action="list" namespace="/do/jacms/Resource" ><s:param name="resourceTypeCode" >Image</s:param></s:url>">
                            <span class="list-group-item-value"><s:text name="menu.APPS.CMS.digitalAssets" /></span>
                        </a>
                    </li>
                </c:if>
                <c:if test="${isSuperUser}">
                    <li class="list-group-item">
                        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName">jacmsContentManager</s:param></s:url>">
                            <span class="list-group-item-value"><s:text name="menu.APPS.CMS.contentTypes" /></span>
                        </a>
                    </li>
                    <li class="list-group-item">
                        <a href="<s:url action="list" namespace="/do/jacms/ContentModel" />">
                           <span class="list-group-item-value"><s:text name="menu.APPS.CMS.contentModels" /></span>
                        </a>
                    </li>
                    <li class="list-group-item">
                        <a href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />">
                           <span class="list-group-item-value"><s:text name="menu.APPS.CMS.contentSettings" /></span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>
    </li>
</c:if>