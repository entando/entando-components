<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpsharedocs/static/css/jpsharedocs.css" />

<h3><wp:i18n key="jpsharedocs_EDIT_STEP4" /></h3>

<p><wp:i18n key="jpsharedocs_EDIT_CONFIRM_DATA" /></p>

<form action="<wp:action path="/ExtStr2/do/jpsharedocs/Document/save" />" method="post" enctype="multipart/form-data" >
	<p class="noscreen">
		<wpsf:hidden name="step" value="4" />
		<s:if test="%{isEdit()}"><wpsf:hidden name="contentId" /></s:if>
		<wpsf:hidden name="title" />
		<wpsf:hidden name="author" />
		<wpsf:hidden name="description" />
		<wpsf:hidden name="docDescription" />
		<wpsf:hidden name="documentContentType" />
		<wpsf:hidden name="documentFileName" />
		<wpsf:hidden name="tmpFileName" />
		<s:iterator value="categories" id="categoryCode" status="rowstatus">
			<wpsf:hidden name="categories" value="%{#categoryCode}" id="categories-%{#rowstatus.index}"/>
		</s:iterator>
		<s:iterator value="extraGroups" id="groupName" status="rowstatus">
			<wpsf:hidden name="extraGroups" value="%{#groupName}" id="extraGroups-%{#rowstatus.index}"/>
		</s:iterator>
		<wpsf:hidden name="mainGroup" />
	</p>
	
	<fieldset>
		<legend><wp:i18n key="jpsharedocs_EDIT_INFO" /></legend> 
		<dl>
			<dt><wp:i18n key="jpsharedocs_DOC_TITLE" /></dt><dd><s:property value="title" /></dd>
			<dt><wp:i18n key="jpsharedocs_DOC_DESCRIPTION" /></dt><dd><s:property value="description"/></dd>
			<dt><wp:i18n key="jpsharedocs_DOC_AUTHOR" /></dt><dd><s:property value="author"/></dd>
			
		<s:if test='%{documentFileName!=null && documentFileName!=""}'> 
			<s:if test='%{isEdit()}'> 
				<dt><wp:i18n key="jpsharedocs_DOC_NEW_VERSION" /></dt><dd><s:property value="documentFileName"/></dd>
				<dt><wp:i18n key="jpsharedocs_DOC_NEWVERSION_DESCR" /></dt><dd><s:property value="docDescription"/></dd>
			</s:if>
			<s:else>
				<dt><wp:i18n key="jpsharedocs_DOC_DOCUMENT" /></dt><dd><s:property value="documentFileName"/></dd>
				<dt><wp:i18n key="jpsharedocs_DOC_VERSION_DESCR" /></dt><dd><s:property value="docDescription"/></dd>
			</s:else>
		</s:if>
		<s:elseif test="%{isEdit()}">
				<dt><wp:i18n key="jpsharedocs_DOC_NEW_VERSION" /></dt><dd><wp:i18n key="jpsharedocs_DOC_VERSION_UNCHANGED" /></dd>
		</s:elseif>
		</dl>
		<p>
			<s:set name="backLabel" ><wp:i18n key="jpsharedocs_GOTO_EDITINFO" /></s:set> 
			<wpsf:submit action="compileFields" cssClass="button" value="%{#backLabel}" />
		</p>
	</fieldset>
	
	<fieldset>
		<legend><wp:i18n key="jpsharedocs_EDIT_CATEGORIES"/></legend>
		<dl>
			<dt><wp:i18n key="jpsharedocs_CATEGORIES_TABLE_SUMMARY"/></dt>
			<dd>
				<ul>
					<s:iterator value="categories" id="categoryCode">
						<s:set name="category" value="%{getCategory(#categoryCode)}" />
						<li><s:property value="#category.defaultFullTitle"/></li>
					</s:iterator>
				</ul>
			</dd>
		</dl>
		<p>
			<s:set name="backLabel" ><wp:i18n key="jpsharedocs_GOTO_EDITCATEGORIES" /></s:set> 
			<wpsf:submit action="compileCategories" cssClass="button" value="%{#backLabel}" />
		</p>
	</fieldset>
	
	<fieldset>
		<legend><wp:i18n key="jpsharedocs_EDIT_GROUPS" /></legend>
		<dl>
			<dt><wp:i18n key="jpsharedocs_DOC_MAINGROUP" /></dt>
				<dd><s:if test="%{isEdit()}"><s:property value="%{getGroup(content.mainGroup).getDescr()}"/></s:if>
				<s:else><s:property value="%{getGroup(mainGroup).getDescr()}"/></s:else></dd>
			
			<dt><wp:i18n key="jpsharedocs_DOC_EXTRA_GROUPS"/></dt>
			<dd>
				<s:if test="extraGroups.size == 0"><wp:i18n key="jpsharedocs_DOC_NO_EXTRA_GROUPS" /></s:if>
				<s:if test="extraGroups.size != 0">
					<ul>
						<s:iterator value="extraGroups" id="groupName">
							<li><s:property value="%{getGroup(#groupName).getDescr()}"/></li> 
						</s:iterator>
					</ul>
				</s:if>
			</dd>
		</dl>
		<p>
			<s:set name="backLabel" ><wp:i18n key="jpsharedocs_GOTO_EDITGROUPS" /></s:set> 
			<wpsf:submit action="compileGroups" cssClass="button" value="%{#backLabel}" />
		</p>
	</fieldset>		
			
	<p>
		<s:set name="submitLabel" ><wp:i18n key="jpsharedocs_SAVE" /></s:set> 
		<wpsf:submit cssClass="button" value="%{#submitLabel}" />
	</p>
	<p>
	
		<wpsa:actionParam action="save" var="saveAndCheckoutAction">
			<wpsa:actionSubParam name="checkin" value="false" />
		</wpsa:actionParam>
		
		<s:if test="%{checkedIn}" >
			<s:set name="submitLabel" ><wp:i18n key="jpsharedocs_CHECK_OUT" /></s:set>
			<wpsf:submit cssClass="button" action="%{#saveAndCheckoutAction}" value="%{#submitLabel}"></wpsf:submit>
		</s:if>
	
	</p>
	
</form>