<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="margin-none"><s:text name="jpcontentrefs.title.contentTypeReferenced" /></h3>
	</div>
	<div class="panel-body">
		<s:if test="null != references['jpcontentrefsContentCategoryRefManagerUtilizers']">
			<wpsa:subset source="references['jpcontentrefsContentCategoryRefManagerUtilizers']" count="10" objectName="contentTypeReferences" advanced="true" offset="5" pagerId="contentCatTypeRefsManagerReferences">
				<s:set name="group" value="#contentTypeReferences" />

				<div class="text-center">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>

				<table class="table table-bordered">
					<caption class="sr-only"><span><s:text name="title.contentList" /></span></caption>
					<thead>
						<tr>
							<th>
								<s:text name="label.description" />
							</th>
							<th>
								<s:text name="label.code" />
							</th>
						</tr>
					</thead>
					<s:iterator var="currentContentTypeVar" >
						<tr>
							<td>
								<a href="<s:url namespace="/do/jacms/Content" action="edit">
								<s:param name="contentId" value="#currentContentTypeVar.code" /></s:url>">
									<s:property value="#currentContentTypeVar.descr"/>
								</a>
							</td>
							<td>
								<code><s:property value="#currentContentTypeVar.code"/></code>
							</td>
						</tr>
					</s:iterator>
				</table>

				<div class="text-center">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
			</wpsa:subset>
		</s:if>
		<s:else>
			<p class="margin-none"><s:text name="jpcontentrefs.note.referencedContentTypes.empty" /></p>
		</s:else>
	</div>
</div>

