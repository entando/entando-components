<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-editFrame"> 
	<s:if test="showlet != null">
		<%--
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
		--%>
		<h2><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h2>
		<h3><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h3>
		<p><s:text name="note.editFrame.noConfigNeeded" /></p>
	</s:if>
	<s:else>
		<h2><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h2>
		<%-- Error message handling --%>
		<s:if test="hasActionErrors()">
			<div class="alert">
				<p><strong><s:text name="message.title.ActionErrors" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<%--
		<p class="margin-more-bottom"><s:text name="note.editFrame.chooseAWidget" /></p>
		--%>
		<s:form action="joinWidget" id="formform" theme="simple" cssClass="form-inline">
			<p class="noscreen">
				<wpsf:hidden name="langCode" />
				<wpsf:hidden name="pageCode" />
				<wpsf:hidden name="frame" />
			</p>
			<p><s:text name="title.editFrame.chooseAWidget" /></p>
			<p>
				<label for="<c:out value="showletCode${random}" />" class="basic-mint-label"><s:text name="name.widget" /></label>
				<select name="widgetTypeCode" tabindex="<wpsa:counter />" id="<c:out value="showletCode${random}" />">
				<s:iterator var="showletFlavour" value="showletFlavours">

					<wpsa:set var="tmpShowletType">tmpShowletTypeValue</wpsa:set>
					
					<s:iterator var="showletType" value="#showletFlavour" >
								
						<s:if test="#showletType.optgroup != #tmpShowletType">
						
							<s:if test="#showletType.optgroup == 'stockShowletCode'">
								<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.stock" /></wpsa:set>
							</s:if>
							<s:elseif test="#showletType.optgroup == 'customShowletCode'">
								<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.custom" /></wpsa:set>
							</s:elseif>
							<s:elseif test="#showletType.optgroup == 'userShowletCode'">
								<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.user" /></wpsa:set>
							</s:elseif>
							<s:else>
								<wpsa:set var="pluginPropertyName" value="%{getText(#showletType.optgroup + '.name')}" />		
								<wpsa:set var="pluginPropertyCode" value="%{getText(#showletType.optgroup + '.code')}" />					
								<wpsa:set var="optgroupLabel"><s:text name="#pluginPropertyName" /></wpsa:set>
							</s:else>
									
						<optgroup label="<s:property value="#optgroupLabel" />">
						</s:if>
							<option value="<s:property value="#showletType.key" />"><s:property value="#showletType.value" /></option>
					
						<wpsa:set var="tmpShowletType"><s:property value="#showletType.optgroup" /></wpsa:set>
					
					</s:iterator>
						</optgroup>	
				</s:iterator>
				</select>
				</p>
			</fieldset>
			<p><sj:submit targets="form-container" value="%{getText('label.save')}" indicator="indicator" button="true" /></p>
		</s:form>
	</s:else>
</div>
