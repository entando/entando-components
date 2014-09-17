<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>

<span class="noscreen"><s:text name="note.linkContent" /></span>

<s:if test="#attribute.symbolicLink != null">
	
	<s:if test="#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4">
		<s:set name="linkedPage" value="%{getPage(#attribute.symbolicLink.pageDest)}"></s:set>
	</s:if>
	<s:if test="#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4">
		<s:set name="linkedContent" value="%{getContentVo(#attribute.symbolicLink.contentDest)}"></s:set>
	</s:if>
	<s:set name="validLink" value="true"></s:set>
	<s:if test="(#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4) && #linkedPage == null">
	<s:text name="LinkAttribute.fieldError.linkToPage.voidPage" />
	<s:set name="validLink" value="false"></s:set>
	</s:if>
	<s:if test="(#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4) && (#linkedContent == null || !#linkedContent.onLine)">
	<s:text name="LinkAttribute.fieldError.linkToContent" />
	<s:set name="validLink" value="false"></s:set>
	</s:if>
	
	<s:if test="#validLink">
	<%-- LINK VALORIZZATO CORRETTAMENTE --%>
	
		<s:if test="#attribute.symbolicLink.destType == 1">
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-url.png</s:set>
			<s:set name="linkDestination" value="%{getText('note.URLLinkTo') + ': ' + #attribute.symbolicLink.urlDest}" />
		</s:if>
		
		<s:if test="#attribute.symbolicLink.destType == 2">
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-page.png</s:set>
			<s:set name="linkDestination" value="%{getText('note.pageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
		</s:if>
		
		<s:if test="#attribute.symbolicLink.destType == 3">
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-content.png</s:set>
			<s:set name="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr}" />
		</s:if>
		
		<s:if test="#attribute.symbolicLink.destType == 4">
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-contentOnPage.png</s:set>
			<s:set name="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr + ', ' + getText('note.contentOnPageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
		</s:if>
	
		<img src="<s:property value="iconImagePath" />" alt="<s:property value="linkDestination" />" title="<s:property value="linkDestination" />" />
		<%-- CAMPO DI TESTO --%>
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
	</s:if>
</s:if>

<s:if test="#lang.default">
<%-- Lingua di DEFAULT --%>
	
	
	<wpfssa:actionParam action="chooseLink" var="chooseLinkActionNameVar" >
		<wpfssa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
		<wpfssa:actionSubParam name="attributeName" value="%{#attribute.name}" />
		<wpfssa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
		<wpfssa:actionSubParam name="langCode" value="%{#lang.code}" />
	</wpfssa:actionParam>
	<s:url var="chooseLinkActionVar" action="%{#chooseLinkActionNameVar}" />
	<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link.png</s:set>
	<sj:submit type="image" targets="form-container" value="%{getText('label.configure')}" title="%{#attribute.name + ': ' + getText('label.configure')}" 
			   button="true" href="%{#chooseLinkActionVar}" src="%{#iconImagePath}" />
	
	<%--
	<wpsa:actionParam action="chooseLink" var="chooseLinkActionName" >
		<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
		<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
		<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
		<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
	</wpsa:actionParam>
	<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link.png</s:set>	
	<wpsf:submit useTabindexAutoIncrement="true" action="%{#chooseLinkActionName}" type="image"
		value="%{getText('label.configure')}" title="%{#attribute.name + ': ' + getText('label.configure')}" src="%{#iconImagePath}" />
	--%>
	
	
	<s:if test="#attribute.symbolicLink != null && ((!#attributeTracer.monoListElement) || (#attributeTracer.monoListElement && #attributeTracer.compositeElement))">
	<%-- LINK VALORIZZATO --%>
		<%-- PULSANTE RIMUOVI --%>
		<wpsa:actionParam action="removeLink" var="removeLinkActionName" >
			<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
			<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
			<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
			<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
		</wpsa:actionParam>

		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/delete.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" type="image" action="%{#removeLinkActionName}" 
			value="%{getText('label.remove')}" title="%{getText('label.remove')}"  src="%{#iconImagePath}" />
	</s:if>
</s:if>
<s:else><s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.</s:else>