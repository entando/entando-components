<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<p class="text-info"><s:text name="jpcontentrefs.note.intro" /></p>
<s:if test="%{contentTypes.size()>0}">
	<table class="table table-bordered table-condensed margin-base-vertical">
		<caption class="sr-only"><span><s:text name="jpcontentrefs.title.contentTypes.caption +jpcontentrefs.note.contentTypes.summary" /></span></caption>
		<thead>
			<tr>
				<th class="text-center"><s:text name="jpcontentrefs.label.code" /></th>
				<th><s:text name="jpcontentrefs.label.description" /></th>
			</tr>
		</thead>
		<s:iterator value="contentTypes" var="contentType">
			<tr>
				<td class="text-center"><code><s:property value="#contentType.code" /></code></td>
				<td><a href="<s:url action="edit"><s:param name="contentTypeCode" value="#contentType.code"/></s:url>" title="<s:text name="jpcontentrefs.note.gotoCategories" />" ><s:property value="#contentType.descr" /></a></td>
			</tr>
		</s:iterator>
	</table>
</s:if>
<s:else>
	<p>
		No ContenType configured. <a href="
			<s:url
				namespace="/do/Entity"
				action="initAddEntityType">
					<s:param name="entityManagerName" value="%{'jacmsContentManager'}" />
			</s:url>">Add One</a>
	</p>
</s:else>
