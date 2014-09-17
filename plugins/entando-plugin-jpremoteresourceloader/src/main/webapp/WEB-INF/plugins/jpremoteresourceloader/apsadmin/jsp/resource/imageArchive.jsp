<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="onEditContent">
<h1><s:text name="title.contentManagement" /></h1>
<h2><s:text name="title.contentEditing" /></h2>
<s:text name="contentDescription" />: <s:property value="content.descr" /> 

<h3><s:text name="title.chooseImage" /></h3>
</s:if>

<s:if test="!onEditContent">
	<h1><s:text name="title.resourceManagement" /></h1>
	<h2><s:text name="title.imageManagement" /></h2>
</s:if>

<s:form action="search">
<p class="noscreen">
	<wpsf:hidden name="resourceTypeCode" />
</p>
<p>
	<label for="text"><s:text name="label.search.for"/>&#32;<s:text name="label.description"/>:</label><br />
	<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" />
</p>
<fieldset><legend><s:text name="title.searchFilters" /></legend>
<p class="important">
	<s:text name="label.category" />:<br />
</p>

<ul id="categoryTree">
	<s:set name="inputFieldName" value="'categoryCode'" />
	<s:set name="selectedTreeNode" value="selectedNode" />
	<s:set name="liClassName" value="'category'" />
	<s:set name="currentRoot" value="categoryRoot" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
</ul>

</fieldset>
<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" /></p>

</s:form>

<wp:ifauthorized permission="manageResources">
	<p><a href="<s:url action="new" ><s:param name="resourceTypeCode" >Image</s:param></s:url>"><s:text name="label.new" />&#32;<s:text name="label.resource" /></a></p>
<s:if test="!onEditContent">
	<p><a href="<s:url action="new" namespace="/do/jpremoteresourceloader/Resource"><s:param name="resourceTypeCode" >Image</s:param></s:url>"><s:text name="label.new" />&#32;<s:text name="jpremoteresourceloader.label.remote" /></a></p>
</s:if>
<s:else>
	<p><a href="<s:url action="new" namespace="/do/jpremoteresourceloader/Content/Resource"><s:param name="resourceTypeCode" >Image</s:param></s:url>"><s:text name="label.new" />&#32;<s:text name="jpremoteresourceloader.label.remote" /></a></p>
</s:else>	

</wp:ifauthorized>

<s:form action="search">
<p class="noscreen">
	<wpsf:hidden name="text" />
	<wpsf:hidden name="categoryCode" />
	<wpsf:hidden name="resourceTypeCode" />
</p>

<wpsa:subset source="resources" count="10" objectName="groupResource" advanced="true" offset="5">
<s:set name="group" value="#groupResource" />

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<s:iterator id="resourceid">
<s:set name="resource" value="%{loadResource(#resourceid)}"></s:set>
<%-- http://www.maxdesign.com.au/presentation/definition/dl-image-gallery.htm --%>
<dl class="gallery">
	<dt class="image"><a href="<s:property value="%{#resource.getImagePath(0)}" />" ><img src="<s:property value="%{#resource.getImagePath(1)}"/>" alt=" " /></a></dt>
	<dt class="options">
		<s:if test="onEditContent">
			<a href="<s:url action="joinResource" namespace="/do/jacms/Content/Resource">
					<s:param name="resourceId" value="%{#resourceid}" />
				</s:url>#<s:property value="currentAttributeLang" />_tab" title="<s:text name="note.joinThisToThat" />: <s:property value="content.descr" />" ><s:text name="label.join" /></a>
		</s:if>
		<s:else>
			<a href="<s:url action="edit" ><s:param name="resourceId" value="%{#resourceid}" /></s:url>" ><img src="<wp:resourceURL />administration/img/icons/edit-content.png" alt="<s:text name="label.edit" />" title="<s:text name="label.edit" />" /></a><span class="noscreen">, </span><a href="<s:url action="trash" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{#resource.type}" /></s:url>" ><img src="<wp:resourceURL />administration/img/icons/delete.png" alt="<s:text name="label.remove" />" title="<s:text name="label.remove" />" /></a>
		</s:else>
	</dt>
	<dd>
		<p><s:property value="#resource.descr" /></p>
	</dd>
</dl>

</s:iterator>

<div class="pager clear">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</s:form>


<s:if test="onEditContent">

<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/backToContentButton.jsp" />

</s:if>