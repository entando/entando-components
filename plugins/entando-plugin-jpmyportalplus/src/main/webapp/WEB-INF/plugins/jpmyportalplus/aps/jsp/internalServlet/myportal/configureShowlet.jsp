<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:i18n key="jpmyportal_BOX_CONFIGURE_SHOWLET_TITLE" />

<div class="configureShowlet">
	<h3><wp:i18n key="jpmyportal_SELECTED_SHOWLET" />:</h3>
	
	<p><wp:i18n key="jpmyportal_BOX" />: <s:property value="frameSource" /></p>
	<p><wp:i18n key="jpmyportal_PAGEMODEL" />: <c:out value="${param.currentPageDescr}" /></p>
	<form action="<wp:action path="/ExtStr2/do/Front/jpmyportal/MyPortal/selectFrame.action" />" method="post" >
		<p class="noscreen">
			<wpsf:hidden name="currentPage" />
			<wpsf:hidden name="frameSource" />
		</p>
		<p>
		<label for="choosenShowlet"><wp:i18n key="jpmyportal_SHOWLET" /></label>:<br />
		<s:set name="labelSubmit"><wp:i18n key="jpmyportal_CHOOSE" /></s:set>
		<wpsf:select name="choosenShowlet" id="choosenShowlet" headerKey="unassigned" list="customizableShowlets" listKey="code" listValue="%{getTitle(code, titles)}" />
		<wpsf:submit action="selectFrame" value="%{#labelSubmit}" title="%{#labelSubmit}" cssClass="button" />
		</p>
	</form>
	
	<s:set name="defaultShowlet" value="%{getDefaultShowlet(currentPage, frameSource)}" />
	
	<s:if test="null != #defaultShowlet">
		<form action="<wp:action path="/ExtStr2/do/Front/jpmyportal/MyPortal/resetFrame.action" />" method="post" >
			<p class="noscreen">
				<wpsf:hidden name="currentPage" />
				<wpsf:hidden name="frameSource" />
			</p>
			
			<s:set name="labelSubmit"><wp:i18n key="jpmyportal_CHOOSE" /></s:set>
			<p><wp:i18n key="jpmyportal_RESET_TO_DEFAULT" />:<br />
			<em><s:property value="#defaultShowlet.type.descr" /></em>
			<wpsf:submit action="resetFrame" value="%{#labelSubmit}" title="%{#labelSubmit}" cssClass="button" /></p>
		</form>
	</s:if>
</div>