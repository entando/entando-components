<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<div id="main">

<h2><s:text name="jpcontentrefs.title.contentTypes" /></h2>
<p><s:text name="jpcontentrefs.note.intro" /></p>

<table class="generic" summary="<s:text name="jpcontentrefs.note.contentTypes.summary" />">
<caption><span><s:text name="jpcontentrefs.title.contentTypes.caption" /></span></caption>
<tr>
	<th><s:text name="jpcontentrefs.label.code" /></th>
	<th><s:text name="jpcontentrefs.label.description" /></th>	
</tr>
<s:iterator id="contentType" value="contentTypes">
<tr>
	<td class="icon"><span class="monospace"><s:property value="#contentType.code" /></span></td>
 	<td><a href="<s:url action="edit"><s:param name="contentTypeCode" value="#contentType.code"/></s:url>" title="<s:text name="jpcontentrefs.note.gotoCategories" />" ><s:property value="#contentType.descr" /></a></td>
</tr>
</s:iterator>
</table>
</div>