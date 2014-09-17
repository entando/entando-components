<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-contentFinding">
	<h3><s:text name="title.configureLinkAttribute" />&#32;(<s:text name="title.step2of2" />)</h3>
	<s:include value="linkAttributeConfigReminder.jsp" />
	<s:form id="formform" action="joinPageLink" namespace="/do/jpfrontshortcut/Content/Link" theme="simple">
		<s:if test="hasFieldErrors()">
			<div class="alert">
				<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<p class="noscreen">
			<wpsf:hidden name="contentOnSessionMarker" />
			<s:if test="contentId == null">
				<wpsf:hidden name="linkType" value="2"/>
			</s:if>
			<s:else>
				<wpsf:hidden name="contentId"/>
				<wpsf:hidden name="linkType" value="4"/>
			</s:else>
			<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
				<wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/>
			</s:iterator>
		</p>
		<s:set name="currentRoot" value="allowedTreeRootNode" />
		<p>
			<label for="selectedNode_<s:property value="#random" />"><s:text name="title.pageTree" /></label><br />
			<select name="selectedNode" id="selectedNode_<s:property value="#random" />">
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/linkAttribute/inc/page-selectItem.jsp" />
			</select>
		</p>
		<p class="centerText">
			<s:url var="joinLinkActionVar" action="joinPageLink" />
			<sj:submit targets="form-container" value="%{getText('label.join')}" title="%{getText('label.join')}" 
					   button="true" href="%{#joinLinkActionVar}" cssClass="button" />
		</p>
		<p class="lower-actions">
			<s:url var="entryContentActionVar" action="backToEntryContent" />
			<sj:submit value="Cancel, Back to Edit Content" href="%{#entryContentActionVar}" button="true" targets="form-container" />
		</p>
</s:form>