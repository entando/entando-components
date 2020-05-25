<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="ibox float-e-margins">
    <div class="jpcrowdsourcing jpcrowdsourcing_fastEntryIdea">
        <div class="ibox-title">
            <h5><wp:i18n key="jpcollaboration_NEW_IDEA_TITLE" /></h5>
        </div>
        <jpcrwsrc:currentPageWidget param="config" configParam="instanceCode" widget="collaboration_ideaInstance" var="instanceVar"/>
        <div class="ibox-content">
            <c:choose>
                <c:when test="${sessionScope.currentUser != 'guest'}">
                    <jpcrwsrc:pageWithWidget var="entryIdea_page" widgetTypeCode="jpcollaboration_entryIdea" listResult="false" />
                    <form action="<wp:url page="${entryIdea_page.code}" />" method="post" accept-charset="UTF-8">
                        <s:hidden name="_csrf" value="%{csrfToken}"/>
                        <c:if test="${null != instanceVar}">
                            <input type="hidden" name="jpcrowdsourcing_fastInstanceCode" value="<c:out value="${instanceVar}" />" />
                        </c:if>
                        <p>
                            <label for="jpcrowdsourcing_fastDescr" class="control-label"><wp:i18n key="jpcollaboration_LABEL_DESCR" /></label>
                            <textarea rows="5" cols="40" name="jpcrowdsourcing_fastDescr" id="jpcrowdsourcing_fastDescr" class="form-control"></textarea>
                        </p>
                        <p>
                            <input type="submit" value="<wp:i18n key="jpcollaboration_SUBMIT_IDEA" />" class="btn btn-default pull-right" />
                        </p><br><br>
                    </form>
                </c:when>
                <c:otherwise>
                    <p class="alert alert-danger"><wp:i18n key="jpcollaboration_DO_LOGIN" /></p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
