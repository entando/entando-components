<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set name="currentResource" value="#attribute.resources[#lang.code]"></s:set>
<s:set name="defaultResource" value="#attribute.resource"></s:set>

<span class="noscreen"><s:text name="note.attachContent" /></span>

<s:if test="#lang.default">
<%-- Lingua di DEFAULT --%>
	<s:if test="#currentResource != null">
		<%-- Lingua di default - Risorsa VALORIZZATA --%>
		<%-- IMMAGINE E LINK + TESTO + PULSANTE RIMUOVI --%>
		<%-- IMMAGINE E LINK --%> 
		<a class="noborder" href="<s:property value="#defaultResource.attachPath" />"><img class="noborder" src="<wp:resourceURL/>administration/common/img/icons/resourceTypes/22x22/<s:property value="%{getIconFile(#defaultResource.instance.fileName)}"/>" alt="<s:property value="%{#defaultResource.instance.fileName}"/>" title="<s:property value="%{#defaultResource.instance.fileName}"/>&#32;<s:property value="%{#defaultResource.instance.fileLength}"/>" /></a>

		<%-- CAMPO DI TESTO --%>
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
		
		<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
			<%-- PULSANTE DI RIMOZIONE RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/removeResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Attach</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:param>
			</s:include>
		</s:if>
	</s:if>
	<s:else>
		<%-- Lingua di default - Risorsa NON VALORIZZATA --%>
		
		<%-- PULSANTE DI RICERCA RISORSA --%>
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
			<s:param name="resourceTypeCode">Attach</s:param>
			<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/attachment.png</s:param>
		</s:include>
		
	</s:else>
</s:if>
<s:else>
<%-- Lingua NON di DEFAULT --%>
	<s:if test="#defaultResource == null">
		<%-- Risorsa lingua di DEFAULT NON VALORIZZATA --%>
		<s:text name="note.editContent.doThisInTheDefaultLanguage" />.
	</s:if>
	<s:else>
		<%-- Risorsa lingua di DEFAULT VALORIZZATA --%>
		<s:if test="#currentResource == null">
			<%-- Risorsa lingua corrente NON VALORIZZATA --%>
			<%-- IMMAGINE DI DEFAULT + PULSANTE SCEGLI RISORSA --%> 
			
			<%-- RISORSA DI DEFAULT --%> 
			<a class="noborder" href="<s:property value="#defaultResource.attachPath" />">
				<img src="<wp:resourceURL/>administration/common/img/icons/resourceTypes/22x22/<s:property value="%{getIconFile(#defaultResource.instance.fileName)}"/>" alt="<s:property value="#defaultResource.descr"/>" title="<s:property value="%{#defaultResource.instance.fileName}"/>&#32;<s:property value="%{#defaultResource.instance.fileLength}"/>" /></a>
			
			<%-- PULSANTE DI RICERCA RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Attach</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/attachment.png</s:param>
			</s:include>
			
		</s:if>
		<s:else>
			<%-- IMMAGINE LINGUA CORRENTE CON LINK + PULSANTE RIMUOVI --%>
			 
			<%-- IMMAGINE LINGUA CORRENTE CON LINK  --%> 
			<a class="noborder" href="<s:property value="#currentResource.attachPath" />">
				<img src="<wp:resourceURL/>administration/common/img/icons/resourceTypes/22x22/<s:property value="%{getIconFile(#currentResource.instance.fileName)}"/>" alt="<s:property value="#currentResource.descr"/>" title="<s:property value="%{#currentResource.instance.fileName}"/>&#32;<s:property value="%{#currentResource.instance.fileLength}"/>" /></a> 
			
			<%-- PULSANTE DI RICERCA RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Attach</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/attachment.png</s:param>
			</s:include>
			
			<%-- PULSANTE DI RIMOZIONE RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/removeResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Attach</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:param>
			</s:include>
			
		</s:else>
		<%-- CAMPO DI TESTO --%>
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
	</s:else>
</s:else>
