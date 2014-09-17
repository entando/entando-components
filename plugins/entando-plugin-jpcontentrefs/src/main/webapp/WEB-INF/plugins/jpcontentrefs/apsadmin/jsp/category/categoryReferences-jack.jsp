<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<h3><s:text name="jpcontentrefs.title.contentTypeReferenced" /></h3>

<s:if test="null != references['jpcontentrefsContentCategoryRefManagerUtilizers']">
<wpsa:subset source="references['jpcontentrefsContentCategoryRefManagerUtilizers']" count="10" objectName="contentTypeReferences" advanced="true" offset="5" pagerId="contentCatTypeRefsManagerReferences">
<s:set name="group" value="#contentTypeReferences" />

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<table class="generic" id="contentListTable" summary="<s:text name="jpcontentrefs.note.referencedContentTypes.summary" />">
<caption><span><s:text name="title.contentList" /></span></caption>
	<tr>
		<th>
			<s:text name="label.code" />
		</th>
		<th>
			<s:text name="label.description" />
		</th>
	</tr>
	<s:iterator var="currentContentTypeVar" >
		<tr>
			<td>
				<s:property value="#currentContentTypeVar.code"/>
			</td>
			<td>
				<s:property value="#currentContentTypeVar.descr"/>
			</td>
		</tr>
	</s:iterator>
</table>

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>
</s:if>
<s:else>
<p><s:text name="jpcontentrefs.note.referencedContentTypes.empty" /></p>
</s:else>