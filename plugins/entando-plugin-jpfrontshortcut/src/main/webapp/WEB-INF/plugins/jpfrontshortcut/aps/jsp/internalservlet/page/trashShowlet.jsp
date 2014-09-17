<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<h2>
	<s:text name="title.widgetManagement.position.clear" />&#32;
	<s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/>
</h2>
<div id="form-container" class="widget_form"> 
	<s:form action="deleteWidgetFromPage" id="formform" theme="simple">
		<p>
			<wpsf:hidden name="pageCode"/>
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" />
			<s:text name="note.clearPosition.areYouSure.position" />&#32;<em class="important">&#32;<s:property value="frame" />&#32;&ndash;&#32;<s:property value="%{getPage(pageCode).model.getFrames()[frame]}"/></em>
			<s:text name="note.clearPosition.areYouSure.page" />&#32;<em class="important"><s:property value="%{getPage(pageCode).getTitle(currentLang.getCode())}" /></em>
			<s:set var="showletTypeVar" value="%{showlet.type}"></s:set>
			<s:if test="null != #showletTypeVar">
				<s:text name="note.clearPosition.areYouSure.widget" />&#32;<em class="important"><s:property value="%{getTitle(#showletTypeVar.getCode(), #showletTypeVar.getTitles())}" /></em>
			</s:if>	
			?
		</p>
		<p>
			<sj:submit targets="form-container" value="%{getText('label.clear')}" indicator="indicator" button="true" />
		</p>
	</s:form>
</div>