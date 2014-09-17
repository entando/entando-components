<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set name="currentResource" value="#attribute.resources[#lang.code]"></s:set>
<s:set name="defaultResource" value="#attribute.resource"></s:set>

<span class="noscreen"><s:text name="note.imageContent" /></span>
<s:if test="#lang.default">
<%-- Lingua di DEFAULT --%>
	<s:if test="#currentResource != null">
	<span class="imageAttribute">
		<%-- Lingua di default - Risorsa VALORIZZATA --%>
		<%-- IMMAGINE E LINK + TESTO + PULSANTE RIMUOVI --%>
		<%-- IMMAGINE E LINK --%>
		<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
			<%-- PULSANTE DI RIMOZIONE RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/removeResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Image</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:param>
			</s:include>
		</s:if>
		<span class="imageAttribute-img">
			<img src="<s:property value="#defaultResource.getImagePath('1')"/>" alt="<s:property value="#defaultResource.descr"/>" />
		</span>
		<%-- CAMPO DI TESTO --%>
		<span class="imageAttribute-text">
		<label class="basic-mint-label" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><abbr title="<s:text name="label.img.text.long" />"><s:text name="label.img.text.short" /></abbr>:</label>
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
		</span>
	</span>	
	</s:if>
	<s:else>
		<%-- Lingua di default - Risorsa NON VALORIZZATA --%>
		
		<%-- PULSANTE DI RICERCA RISORSA --%>
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
			<s:param name="resourceTypeCode">Image</s:param>
			<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/image.png</s:param>
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
		<span class="imageAttribute">
		<%-- Risorsa lingua di DEFAULT VALORIZZATA --%>
		<s:if test="#currentResource == null">
			<%-- Risorsa lingua corrente NON VALORIZZATA --%>
			<%-- IMMAGINE DI DEFAULT + PULSANTE SCEGLI RISORSA --%> 
			
			<%-- IMMAGINE DI DEFAULT --%>
			<span class="imageAttribute-img">
			<a href="<s:property value="#defaultResource.getImagePath('0')" />"><img class="alignTop" src="<s:property value="#defaultResource.getImagePath('1')"/>" 
				alt="<s:property value="#defaultResource.descr"/>" /></a>
			</span>
			<%-- PULSANTE DI RICERCA RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Image</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/image.png</s:param>
			</s:include>
		</s:if>
		<s:else>
			<%-- IMMAGINE LINGUA CORRENTE CON LINK + PULSANTE RIMUOVI --%>
			
			<%-- PULSANTE DI RIMOZIONE RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/removeResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Image</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/clear.png</s:param>
			</s:include>			
			
			<%-- IMMAGINE LINGUA CORRENTE CON LINK  --%> 
			<span class="imageAttribute-img">
			<a href="<s:property value="#currentResource.getImagePath('0')" />"><img class="alignTop" src="<s:property value="#currentResource.getImagePath('1')"/>" 
				alt="<s:property value="#currentResource.descr"/>" /></a>
			</span>
			
			<%-- PULSANTE DI RICERCA RISORSA --%>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Image</s:param>
				<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/image.png</s:param>
			</s:include>
		</s:else>
		<%-- CAMPO DI TESTO --%>
		<%-- CAMPO DI TESTO - MODULARIZZARE --%>
		<span class="imageAttribute-text">
		<label class="basic-mint-label" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><abbr title="testo per ciccio">testo</abbr>:</label>		
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
		</span>
	</span>	
	</s:else>
</s:else>
