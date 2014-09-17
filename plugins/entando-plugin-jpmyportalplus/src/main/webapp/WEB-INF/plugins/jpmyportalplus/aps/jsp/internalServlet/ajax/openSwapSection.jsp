<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<c:set var="showletId"><s:property value="showletCodeOnOpeningSection" />_<s:property value="%{frameWhereOpenSection}" /></c:set>
<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/swapFrames.action" method="post" class="margin-medium-all">
	<p class="noscreen">
		<%-- il nome del parametro startFramePos è referenziato in jpmyportalplus_javascript_variables.jsp  --%>
		<input type="hidden" name="currentPageCode" value="<s:property value="currentPageCode" />" />
		<input type="hidden" name="startFramePos" value="<s:property value="%{frameWhereOpenSection}" />" />
	</p>

	<p>
		<label for="scambia_<c:out value="${showletId}" />"><s:text name="jpmyportalplus.movethiswidget" /></label>
		<select id="scambia_<c:out value="${showletId}" />" name="targetFramePos">
			<s:iterator value="selectItems" var="selectItem" >
				<option value="<s:property value="#selectItem.frameId" />">
				<s:if test="#selectItem.sameColumn"><s:text name="jpmyportalplus.swapitwith" /> <s:property value="#selectItem.title" /></s:if>
				<s:else><s:text name="jpmyportalplus.insertintocolumn" /> <s:property value="#selectItem.columnIdDest" /></s:else>
				</option>
			</s:iterator>
		</select>
	</p>
	<p>
		<input type="submit" class="btn btn-small btn-primary" value="<s:text name="jpmyportalplus.move" />" />
	</p>
</form>