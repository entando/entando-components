<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpcs" uri="/jpcrowdsourcing-aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<div class="ibox float-e-margins">
    <div class="jpcrowdsourcing jpcrowdsourcing_statistics">
        <div class="ibox-title">
            <h5><wp:i18n key="jpcollaboration_STATISTIC_TITLE" /></h5>
        </div>
        <div class="ibox-content">
            <wpcs:currentPageWidget param="config" configParam="instanceCode" widget="collaboration_ideaInstance" var="instanceVar"/>
            <wpcs:statistic var="stats" instanceCode="${instanceVar}" />
            <c:choose>
                <c:when test="${null != stats}">
                    <ul class="unstyled">
                        <li class="text-right">
                            <wp:i18n key="jpcollaboration_PROPOSED_IDEAS" />&#32;<span class="badge badge-info"><c:out value="${stats.ideas}" /></span>
                        </li>
                        <li class="text-right">
                            <wp:i18n key="jpcollaboration_VOTES" />&#32;<span class="badge badge-info"><c:out value="${stats.votes}" /></span>
                        </li>
                        <li class="text-right">
                            <wp:i18n key="jpcollaboration_IDEA_COMMENTS" />&#32;<span class="badge badge-info"><c:out value="${stats.comments}" /></span>
                        </li>
                        <li class="text-right">
                            <wp:i18n key="jpcollaboration_USERS" />&#32;<span class="badge badge-info"><c:out value="${stats.users}" /></span>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <p class="alert alert-warning"><wp:i18n key="jpcollaboration_STATISTICS_EMPTY" /></p>
                    </c:otherwise>
                </c:choose>
        </div>
    </div>
</div>

