<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<s:include value="/WEB-INF/apsadmin/jsp/content/modules/include/linkAttribute/linkAttributeConfigIntro.jsp"></s:include>
<h3><s:text name="title.configureLinkAttribute" /><!-- Infamous White Space Hack --> <!--// Infamous White Space Hack  -->(<s:text name="title.step1of2" />)</h3>
<s:include value="/WEB-INF/apsadmin/jsp/content/modules/include/linkAttribute/linkAttributeConfigReminder.jsp"></s:include>
<p>
	<s:text name="note.chooseLinkType" />:
</p>

<s:form action="configLink">
<wpsf:hidden name="contentOnSessionMarker" />

<s:if test="hasFieldErrors()">
<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
            <li><s:property/></li>
		</s:iterator>
	</s:iterator>
</ul>
</s:if>
<ul>
<s:iterator id="typeId" value="linkDestinations">
<s:if test="#typeId != 4">
	
	<s:if test="#typeId == 1">
		<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/img/icons/link-url.png</wpsa:set>
		<s:set name="linkDestination" value="%{getText('note.URLLinkTo')}" />
	</s:if>
	
	<s:if test="#typeId == 2">
		<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/img/icons/link-page.png</wpsa:set>
		<s:set name="linkDestination" value="%{getText('note.pageLinkTo')}" />
	</s:if>
	
	<s:if test="#typeId == 3">
		<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/img/icons/link-content.png</wpsa:set>
		<s:set name="linkDestination" value="%{getText('note.contentLinkTo')}" />
	</s:if>
	
	<li><input type="radio" <s:if test="#typeId == symbolicLink.destType">checked="checked"</s:if> name="linkType" id="linkType_<s:property value="#typeId"/>" value="<s:property value="#typeId"/>" /><label for="linkType_<s:property value="#typeId"/>"><img src="<s:property value="iconImagePath" />" alt=" " /> <s:property value="linkDestination" /></label></li>
	
</s:if>	
</s:iterator>
</ul>

<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.continue')}" title="%{getText('label.continue')}" cssClass="button" /></p>

</s:form>