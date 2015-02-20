<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jpwebform" uri="/jpwebform-aps-core"  %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"  %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"  %>
<jpwebform:showFormList listName="formListVar" titleVar="titleVar" pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" />

<c:if test="${!(empty titleVar)}">
	<h2><c:out value="${titleVar}" /></h2>
</c:if>
	
<c:choose>
	<c:when test="${!(empty formListVar)}">
		<wp:pager pagerId="jpwebformListPager" listName="formListVar" objectName="groupForm" advanced="true" offset="5">
			<c:set var="group" value="${groupForm}" scope="request" />
			<ul>
				<c:forEach items="${formListVar}" var="formId" begin="${groupForm.begin}" end="${groupForm.end}">
    				<jpwebform:form formId="${formId}" info="status" var="completed" />
					<li>
						<h3><jpwebform:form formId="${formId}" info="description"/> - <c:out value="${formId}" /></h3>
						<p>
						<strong><wp:i18n key="jpwebform_COMPLETED" /></strong>: <wp:i18n key="jpwebform_COMPLETED_${completed?'YES':'NO'}" />
						 <c:if test="${completed == false}">
							<jpwebform:page formId="${formId}" var="pageVar"/>
							<a href="<wp:url page="${pageVar}" />">
								<wp:i18n key="jpwebform_RESUME_FORM" />
							</a>

						</c:if>						
						</p>
							<%--
							<dt><wp:i18n key="jpwebform_REPEATABLE" /></dt>
								<jpwebform:form formId="${formId}" info="repeatable" var="repeatable" />
								<dd><wp:i18n key="jpwebform_REPEATABLE_${repeatable?'YES':'NO'}" /></dd>
							--%>
						<p>
						<strong><wp:i18n key="jpwebform_SEND_DATE" /></strong>:
							<jpwebform:form formId="${formId}" info="sendDate" var="sendDate" />
							<wp:info key="currentLang" var="currentLang" />
							<fmt:setLocale value="${currentLang}" />
							<fmt:formatDate value="${sendDate}" pattern="EEEE dd/MMM/yyyy HH:mm" />
						</p>
					</li>
				</c:forEach>	
			</ul>
			<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
		</wp:pager>
	</c:when>
	<c:otherwise>
			<p><wp:i18n key="jpwebform_LIST_VIEWER_EMPTY" /></p>
	</c:otherwise>
</c:choose>