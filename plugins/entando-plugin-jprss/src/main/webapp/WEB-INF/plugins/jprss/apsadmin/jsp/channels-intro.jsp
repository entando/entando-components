<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="targetNS" value="%{'/do/jprss/Rss'}" />
<h1><s:text name="jprss.title.rssManagement" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">
	<div class="intro jprss">
		<!-- 
			For the rss icon image:
			Social Network Icon Pack by Komodo Media, Rogie King is licensed under a 
			Creative Commons Attribution-Share Alike 3.0 Unported License.
			(http://creativecommons.org/licenses/by-sa/3.0/)
			Based on a work at www.komodomedia.com.
		 -->
		<s:property value="%{getText('jprss.intro.html')}" escape="false"/>
	</div>
</div>