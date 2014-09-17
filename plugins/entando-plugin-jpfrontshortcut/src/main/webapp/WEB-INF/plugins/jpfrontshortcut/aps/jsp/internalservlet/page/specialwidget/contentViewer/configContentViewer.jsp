<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-configContentViewer">
	<h2><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h2>
	<div class="subsection-light">
		<h3 class="margin-more-top margin-bit-bottom">
			<s:text name="name.widget" />:&#32;
			<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
		</h3>
		<s:form namespace="/do/jpfrontshortcut/Page/SpecialWidget/Viewer" action="saveViewerConfig" id="formform" theme="simple">
			<p class="noscreen">
				<wpsf:hidden name="pageCode" />
				<wpsf:hidden name="frame" />
				<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
			</p>
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
			<s:set name="showletParams" value="showlet.type.parameter" />
			<s:property value="#showletParams['contentId'].descr" />
			<s:if test="showlet.config['contentId'] != null">
				<s:set name="content" value="%{getContentVo(showlet.config['contentId'])}"></s:set>
				<p>
					<s:text name="title.configContentViewer.settings" />&#32;
					<s:property value="#content.descr" /> (<s:property value="#content.id" />)
				</p>
				<p class="margin-more-bottom">
					<wpsf:hidden name="contentId" value="%{getShowlet().getConfig().get('contentId')}" />
					<s:url var="searchContentsUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Viewer" action="searchContents" />
					<sj:submit targets="form-container" href="%{searchContentsUrlVar}" value="%{getText('label.change')}" indicator="indicator" button="true" cssClass="button" />
				</p>
				<fieldset>
					<legend><s:text name="title.publishingOptions" /></legend>
					<p>
						<label for="modelId" clasS="basic-mint-label"><s:text name="label.contentModel" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" id="modelId" name="modelId" value="%{getShowlet().getConfig().get('modelId')}" 
						list="%{getModelsForContent(showlet.config['contentId'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="text" />
					</p>
				</fieldset>
				<s:set var="showletTypeParameters" value="showlet.type.typeParameters"></s:set>
				<s:if test="#showletTypeParameters.size()>2">
					<fieldset>
						<legend><s:text name="label.otherSettings" /></legend>
						<s:iterator value="#showletTypeParameters" id="showletParam" >
							<s:if test="!#showletParam.name.equals('contentId') && !#showletParam.name.equals('modelId')">
								<p>
									<label for="fagianoParam_<s:property value="#showletParam.name" />"><s:property value="#showletParam.descr" />:</label><br />
									<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{'fagianoParam_'+#showletParam.name}" name="%{#showletParam.name}" value="%{showlet.config[#showletParam.name]}" />
								</p>
							</s:if>
						</s:iterator>
					</fieldset>
				</s:if>
			</s:if>
			<s:else>
				<p>
					<s:text name="note.noContentSet" />
				</p>
				<p>
					<s:url var="searchContentsUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Viewer" action="searchContents" />
					<sj:submit targets="form-container" href="%{searchContentsUrlVar}" value="%{getText('label.choose')}" indicator="indicator" button="true" cssClass="button" />
				</p>
			</s:else>
			<p class="centerText">
				<sj:submit targets="form-container" value="%{getText('label.confirm')}" indicator="indicator" button="true" cssClass="button" />
			</p>
		</s:form>
	</div>
</div>