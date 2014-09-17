<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-entryPage"> 
	<s:if test="strutsAction == 1">
		<h2><s:text name="title.newPage" /> - Parent ''<s:property value="%{getPage(parentPageCode).getTitle(currentLang.getCode())}" />''</h2>
	</s:if>
	<s:elseif test="strutsAction == 2">
		<h2><s:text name="title.editPage" /></h2>
	</s:elseif>
	<s:elseif test="strutsAction == 3">
		<%-- copying page... --%>
		<h2><s:text name="title.pastePage" /> - Parent ''<s:property value="%{getPage(parentPageCode).getTitle(currentLang.getCode())}" />''</h2>
	</s:elseif>
	<div class="subsection">
		<s:form action="save" id="formform" theme="simple">
			<s:if test="hasActionErrors()">
				<div class="message message_error">
				<h3><s:text name="message.title.ActionErrors" /></h3>	
					<ul>
						<s:iterator value="actionErrors">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<s:if test="hasFieldErrors()">
				<div class="message message_error">
					<h3><s:text name="message.title.FieldErrors" /></h3>	
					<ul>
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<p class="noscreen">
				<wpsf:hidden name="langCode" />
				<wpsf:hidden name="selectedNode" />
				<wpsf:hidden name="strutsAction" />
				<wpsf:hidden name="copyPageCode" />
				<wpsf:hidden name="parentPageCode" />
				<wpsf:hidden name="groupSelectLock" />
				<s:if test="strutsAction == 2">
					<wpsf:hidden name="pageCode" />
				</s:if>
				<s:iterator value="extraGroups" id="groupName"><s:hidden name="extraGroups" value="%{#groupName}" /></s:iterator>
				<s:if test="strutsAction == 3">
					<wpsf:hidden name="group" />
					<wpsf:hidden name="model" />
					<wpsf:hidden name="defaultShowlet" />
					<wpsf:hidden name="showable" />
					<wpsf:hidden name="useExtraTitles" />
				</s:if>
			</p>
			<fieldset>
				<legend><s:text name="label.info" /></legend>
				<p>
					<label for="<c:out value="pageCode${random}" />" class="basic-mint-label"><s:text name="name.pageCode" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="pageCode" id="%{'pageCode'+#random}" disabled="%{getStrutsAction() == 2}" cssClass="text" />
				</p>
				<s:iterator value="langs">
					<p>
						<label for="<c:out value="lang${random}" /><s:property value="code" />" class="basic-mint-label"><span class="monospace">(<s:property value="code" />)</span> <s:text name="name.pageTitle" />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" name="%{'lang'+code}" id="%{'lang'+#random+code}" value="%{titles.get(code)}" cssClass="text" />
					</p>
				</s:iterator>
				<s:if test="strutsAction == 3">
					</fieldset>
				</s:if>
				<s:if test="strutsAction != 3">
					<p>
						<label for="<c:out value="group${random}" />" class="basic-mint-label"><s:text name="label.ownerGroup" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="group" id="%{'group'+#random}" list="allowedGroups" listKey="name" listValue="descr" disabled="%{groupSelectLock}" cssClass="text"></wpsf:select>
						<s:if test="groupSelectLock"><wpsf:hidden name="group" /></s:if>
					</p>
				</fieldset>
				<fieldset>
					<legend><s:text name="label.extraGroups" /></legend>
					<p>
						<label for="<c:out value="extraGroups${random}" />" class="basic-mint-label"><s:text name="label.join" />&#32;<s:text name="label.group" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="extraGroupName" id="%{'extraGroups'+#random}" list="groups" 
							listKey="name" listValue="descr" cssClass="text" />
						<s:url var="joinExtraGroupUrlVar" action="joinExtraGroup" />
						<sj:submit targets="form-container" value="%{getText('label.join')}" button="true" href="%{joinExtraGroupUrlVar}" />
					</p>
					<s:if test="extraGroups.size() != 0">
						<ul class="noBullet unstyled">
							<s:iterator value="extraGroups" var="groupName">
								<li>
									<wpfssa:actionParam action="removeExtraGroup" var="removeExtraGroupSubmitVar" >
										<wpfssa:actionSubParam name="extraGroupName" value="%{#groupName}" />
									</wpfssa:actionParam>
									<s:url var="removeExtraGroupVar" action="%{#removeExtraGroupSubmitVar}" />
									<sj:submit targets="form-container" value="%{getText('label.remove')}" button="true" href="%{#removeExtraGroupVar}" />
									<s:property value="%{getSystemGroups()[#groupName].getDescr()}"/> 
								</li>
							</s:iterator>
						</ul>
					</s:if>
				</fieldset>
				<fieldset>
					<legend><s:text name="label.settings" /></legend>
					<p>
						<label for="model" class="basic-mint-label"><s:text name="name.pageModel" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="model" id="model" list="pageModels" listKey="code" listValue="descr" cssClass="text"></wpsf:select>
					</p>
					<ul class="noBullet unstyled">
						<li>
							<label class="checkbox" for="<c:out value="defaultShowlet${random}" />"><wpsf:checkbox useTabindexAutoIncrement="true" name="defaultShowlet" id="%{'defaultShowlet'+#random}" cssClass="radiocheck" /><s:text name="name.hasDefaultWidgets" /></label>
						</li>
						<li>
							<label class="checkbox" for="<c:out value="viewerPage${random}" />"><wpsf:checkbox useTabindexAutoIncrement="true" name="viewerPage" id="%{'viewerPage'+#random}" cssClass="radiocheck" /><s:text name="name.isViewerPage" /></label></li>
						<li>
							<label class="checkbox" for="<c:out value="showable${random}" />"><wpsf:checkbox useTabindexAutoIncrement="true" name="showable" id="%{'showable'+#random}" cssClass="radiocheck" /><s:text name="name.isShowablePage" /></label></li>
						<li>
							<label class="checkbox" for="<c:out value="useExtraTitles${random}" />"><wpsf:checkbox useTabindexAutoIncrement="true" name="useExtraTitles" id="%{'useExtraTitles'+#random}" cssClass="radiocheck" /><abbr lang="en" title="<s:text name="name.SEO.full" />"><s:text name="name.SEO.short" /></abbr>:&#32;<s:text name="name.useBetterTitles" /></label></li>
					</ul>
				</fieldset>
			</s:if>
			<s:set var="saveLabel"><wp:i18n key="SAVE" /></s:set>
			<p class="save">
				<sj:submit targets="form-container" value="%{#saveLabel}" indicator="indicator"button="true" />
			</p>
		</s:form>
	</div>
</div>