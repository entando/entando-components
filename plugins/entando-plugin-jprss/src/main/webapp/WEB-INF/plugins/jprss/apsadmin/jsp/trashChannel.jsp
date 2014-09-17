<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="targetNS" value="%{'/do/jprss/Rss'}" />
<h1><s:text name="jprss.title.rssManagement" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">
	<s:form action="delete">
		<p>
			<wpsf:hidden name="id" />
			<s:text name="note.deleteChannel.areYouSure" />&#32;<em class="important"><s:property value="title"/></em>?
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.remove')}" cssClass="button" />
		</p>
	</s:form>
</div>