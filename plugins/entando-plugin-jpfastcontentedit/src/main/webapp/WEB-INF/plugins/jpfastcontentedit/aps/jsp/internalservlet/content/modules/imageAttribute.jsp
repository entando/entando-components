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
		<%-- Lingua di default - Risorsa VALORIZZATA --%>
		<%-- IMMAGINE E LINK + TESTO + PULSANTE RIMUOVI --%>
		<%-- IMMAGINE E LINK --%>
		<div class="control-group">
			<span class="control-label">
				<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
					<%-- PULSANTE DI RIMOZIONE RISORSA --%>
					<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/removeResourceSubmit.jsp">
						<s:param name="resourceTypeCode">Image</s:param>
						<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:param>
					</s:include>
				</s:if>
			</span>
			<div class="controls imageAttribute-img">
				<a href="<s:property value="#defaultResource.getImagePath('0')" />">
					<img class="alignTop" src="<s:property value="#defaultResource.getImagePath('1')"/>" 
					alt="<s:property value="#defaultResource.descr"/>" /></a>
			</div>
		</div>	
		<%-- CAMPO DI TESTO --%>
		<div class="control-group">
			<label class="control-label" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><abbr title="<s:text name="label.img.text.long" />"><s:text name="label.img.text.short" /></abbr></label>
			<div class="controls">
				<wpsf:textfield 
					useTabindexAutoIncrement="true" 
					id="%{#attributeTracer.getFormFieldName(#attribute)}" 
					name="%{#attributeTracer.getFormFieldName(#attribute)}" 
					value="%{#attribute.getTextForLang(#lang.code)}"
					maxlength="254" />
			</div>
		</div>
	</s:if>
	<s:else>
		<%-- Lingua di default - Risorsa NON VALORIZZATA --%>
		
		<%-- PULSANTE DI RICERCA RISORSA --%>
		<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
			<s:param name="resourceTypeCode">Image</s:param>
			<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/image.png</s:param>
		</s:include>
		
	</s:else>
</s:if>
<s:else>
<%-- Lingua NON di DEFAULT --%>
	<s:if test="#defaultResource == null">
		<%-- Risorsa lingua di DEFAULT NON VALORIZZATA --%>
		<s:text name="note.editContent.doThisInTheDefaultLanguage" />
	</s:if>
	<s:else>
		<s:if test="#currentResource == null">
			<%-- Risorsa lingua corrente NON VALORIZZATA --%>
			<%-- IMMAGINE DI DEFAULT + PULSANTE SCEGLI RISORSA --%> 
			<div class="control-group">
				<span class="control-label" for="">
					<%-- PULSANTE DI RICERCA RISORSA --%>
					<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
						<s:param name="resourceTypeCode">Image</s:param>
						<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/image.png</s:param>
					</s:include>
				</span>
				<div class="controls">
					<%-- IMMAGINE DI DEFAULT --%>
					<a href="<s:property value="#defaultResource.getImagePath('0')" />"><img class="alignTop" src="<s:property value="#defaultResource.getImagePath('1')"/>" 
						alt="<s:property value="#defaultResource.descr"/>" /></a>
				</div>
			</div>
		</s:if>

		<s:else>
			<div class="control-group">
				<div class="control-label" for="">
					<%-- PULSANTE DI RIMOZIONE RISORSA --%>
					<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/removeResourceSubmit.jsp">
						<s:param name="resourceTypeCode">Image</s:param>
						<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/clear.png</s:param>
					</s:include>			
					<%-- PULSANTE DI RICERCA RISORSA --%>
					<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/chooseResourceSubmit.jsp">
						<s:param name="resourceTypeCode">Image</s:param>
						<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/image.png</s:param>
					</s:include>
				</div>
				<div class="controls">
					<%-- IMMAGINE LINGUA CORRENTE CON LINK  --%> 
					<span class="imageAttribute-img">
					<a href="<s:property value="#currentResource.getImagePath('0')" />"><img class="alignTop" src="<s:property value="#currentResource.getImagePath('1')"/>" 
						alt="<s:property value="#currentResource.descr"/>" /></a>
					</span>
				</div>
			</div>
		</s:else>
		<%-- CAMPO DI TESTO --%>
		<%-- CAMPO DI TESTO - MODULARIZZARE --%>
		<div class="control-group">
			<label class="control-label" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><abbr title="<s:text name="label.img.text.long" />"><s:text name="label.img.text.short" /></abbr></label>
			<div class="controls">
				<wpsf:textfield 
					useTabindexAutoIncrement="true" 
					id="%{#attributeTracer.getFormFieldName(#attribute)}" 
					name="%{#attributeTracer.getFormFieldName(#attribute)}" 
					value="%{#attribute.getTextForLang(#lang.code)}"
					maxlength="254" />
			</div>
		</div>
	</span>	
	</s:else>
</s:else>
