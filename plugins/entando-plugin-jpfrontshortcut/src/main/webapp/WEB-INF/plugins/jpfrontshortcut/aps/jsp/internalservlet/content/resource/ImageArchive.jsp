<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<h3><s:text name="title.chooseImage" /></h3>
<s:form id="formform" action="search" namespace="/do/jpfrontshortcut/Content/Resource" theme="simple">
	<fieldset>
		<legend><s:text name="title.searchFilters" /></legend>
		<p>
			<wpsf:hidden name="resourceTypeCode" />
			<wpsf:hidden name="contentOnSessionMarker" />
			<label for="text_<c:out value="${random}" />"><s:text name="label.description"/>:</label><br />
			<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="%{'text_'+#random}" cssClass="text" />
		</p>
		<p>
			<label for="category_<c:out value="${random}" />"><s:text name="label.category" />:</label><br />
			<s:set name="currentRoot" value="categoryRoot" />
			<select name="categoryCode" id="category_<c:out value="${random}" />" >
<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/resource/inc/category-selectItem.jsp" />
			</select>
		</p>
	</fieldset>

	<p>
		<s:url var="executeSearchUrlVar" namespace="/do/jpfrontshortcut/Content/Resource" action="search" />
		<sj:submit 
			targets="form-container" 
			indicator="indicator" 
			href="%{#executeSearchUrlVar}" 
			value="%{getText('label.search')}" 
			button="true" />
	</p>

	<wpfssa:subset source="resources" count="12" objectName="groupResource" advanced="true" offset="5">
		<s:set name="group" value="#groupResource" />
		<s:set var="pagerSubmitActionNameVar" value="'search'" />

		<div class="archive-pager">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/include/pager_formBlock.jsp" />
		</div>
		<ul class="archive-list">
		<s:iterator var="resourceid" status="archiveItemStatusVar">
			<s:set name="resource" value="%{loadResource(#resourceid)}"></s:set>
			<li class="archive-item n<s:property value="(#archiveItemStatusVar.count-1)%4" />">
				<p>
					<wpfssa:actionParam action="joinResource" var="joinResourceActionNameVar" >
						<wpfssa:actionSubParam name="resourceId" value="%{#resourceid}" />
					</wpfssa:actionParam>
					<s:url var="joinResourceActionVar" action="%{#joinResourceActionNameVar}" />
					<sj:submit targets="form-container" value="%{getText('label.join')}" 
							   button="true" href="%{#joinResourceActionVar}" />
					<img class="item-image" src="<s:property value="%{#resource.getImagePath(1)}"/>" alt=" " />
					<span class="description"><s:property value="#resource.descr" /></span>
				</p>
			</li>
		</s:iterator>
		</ul>
		<div class="archive-pager">
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/include/pager_formBlock.jsp" />
		</div>
	</wpfssa:subset>
	
	<p class="lower-actions">
		<s:url var="entryContentActionVar" action="backToEntryContent" />
		<sj:submit value="%{getText('jpfrontshortcut.dialog.back.to.entryContent')}" href="%{#entryContentActionVar}" button="true" targets="form-container" />
	</p>
</s:form>