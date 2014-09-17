<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#attribute.symbolicLink != null">
	
	<s:if test="#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4">
		<s:set var="linkedPage" value="%{getPage(#attribute.symbolicLink.pageDest)}"></s:set>
	</s:if>
	<s:if test="#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4">
		<s:set var="linkedContent" value="%{getContentVo(#attribute.symbolicLink.contentDest)}"></s:set>
	</s:if>
	<s:set var="validLink" value="true"></s:set>
	<s:if test="(#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4) && #linkedPage == null">
	<!-- MESSAGGIO LINK SU PAGINA BUCATO -->
	<s:set var="validLink" value="false"></s:set>
	</s:if>
	<s:if test="(#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4) && (#linkedContent == null || !#linkedContent.onLine)">
	<!-- MESSAGGIO LINK SU CONTENUTO BUCATO -->
	<s:set var="validLink" value="false"></s:set>
	</s:if>
	
	<s:if test="#validLink">
	<%-- LINK VALORIZZATO CORRETTAMENTE --%>
	
		<s:if test="#attribute.symbolicLink.destType == 1">
			<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-url.png</s:set>
			<s:set var="linkIntro" ><wp:i18n key="jpfastcontentedit_LINK_TO_URL"/></s:set>
			<s:set var="linkDestination" value="%{#attribute.symbolicLink.urlDest}" />
		</s:if>
		
		<s:elseif test="#attribute.symbolicLink.destType == 2">
			<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-page.png</s:set>
			<s:set var="linkIntro" ><wp:i18n key="jpfastcontentedit_LINK_TO_PAGE"/></s:set>
			<s:set var="linkDestination" value="%{#linkedPage.titles[currentLang.code]}" />
		</s:elseif>
		
		<s:elseif test="#attribute.symbolicLink.destType == 3">
			<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-content.png</s:set>
			<s:set var="linkIntro" ><wp:i18n key="jpfastcontentedit_LINK_TO_CONTENT"/></s:set>
			<s:set var="linkDestination" value="%{#attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr}" />
		</s:elseif>
		
		<s:elseif test="#attribute.symbolicLink.destType == 4">
			<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-contentOnPage.png</s:set>
			<s:set var="linkIntro" ><wp:i18n key="jpfastcontentedit_LINK_TO_CONTENT"/></s:set>
			<s:set var="linkContentOnPage" ><wp:i18n key="jpfastcontentedit_LINK_TO_CONTENT_ON_PAGE"/></s:set>
			<s:set var="linkDestination" value="%{#attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr + ', ' + #linkContentOnPage + ': ' + #linkedPage.titles[currentLang.code]}" />
		</s:elseif>
		
		<img src="<s:property value="iconImagePath" />" alt="<s:property value="%{#linkIntro + ': ' + #linkDestination}" />" title="<s:property value="linkDestination" />" />
		<%-- CAMPO DI TESTO --%>
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
	</s:if>
</s:if>

<s:if test="#lang.default">
<%-- Lingua di DEFAULT --%>
	<wpsa:actionParam action="chooseLink" var="chooseLinkActionName" >
		<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
		<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
		<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
		<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
	</wpsa:actionParam>
	<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link.png</s:set>
	<s:set var="configure_label" ><wp:i18n key="jpfastcontentedit_CONFIGURE"/></s:set>
	<s:set var="remove_label" ><wp:i18n key="jpfastcontentedit_REMOVE"/></s:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="%{#chooseLinkActionName}" type="image"
		value="%{#configure_label}" title="%{#attribute.name + ': ' + #configure_label}" src="%{#iconImagePath}" />
	<s:if test="#attribute.symbolicLink != null && ((!#attributeTracer.monoListElement) || (#attributeTracer.monoListElement && #attributeTracer.compositeElement))">
	<%-- LINK VALORIZZATO --%>
		<%-- PULSANTE RIMUOVI --%>
		<wpsa:actionParam action="removeLink" var="removeLinkActionName" >
			<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
			<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
			<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
			<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
		</wpsa:actionParam>
		
		<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" type="image" action="%{#removeLinkActionName}" 
			value="%{#remove_label}" title="%{#remove_label}"  src="%{#iconImagePath}" />
	</s:if>
</s:if>
<s:else><wp:i18n key="jpfastcontentedit_DO_THIS_ON_DEFAULT_LANG"/>.</s:else>