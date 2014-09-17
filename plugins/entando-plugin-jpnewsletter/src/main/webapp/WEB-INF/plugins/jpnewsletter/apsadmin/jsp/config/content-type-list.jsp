<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><s:text name="jpnewsletter.title.newsletterManagement" /></h1> 
<div id="main">
	<h2 class="margin-more-bottom" ><s:text name="jpnewsletter.title.newsletterConfig" /></h2>
	<s:form action="addContentTypeConfig">
		<s:if test="hasFieldErrors()">
			<div class="message message_error">	
			<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
				            <li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
	
		<fieldset>
			<legend><s:text name="label.info" /></legend>
			<p class="noscreen"><wpsf:hidden name="newsletterContentType.contentTypeCode" /></p>
		
			<p><s:text name="jpnewsletter.contenttypeConfig.intro" />: <s:property value="getSmallContentType(contentTypeCode).descr" />.</p>
		
			<p>
				<label for="jpnewsletter_text_model" class="basic-mint-label"><s:text name="jpnewsletter.plainModel" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_text_model" name="newsletterContentType.simpleTextModel" list="getContentModelByType(newsletterContentType.contentTypeCode)" listKey="id" listValue="description" ></wpsf:select>
			</p>
			
			<p>
				<label for="jpnewsletter_html_model" class="basic-mint-label"><s:text name="jpnewsletter.htmlModel" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_html_model" name="newsletterContentType.htmlModel" list="getContentModelByType(newsletterContentType.contentTypeCode)" 
					headerKey="" headerValue="" listKey="id" listValue="description" ></wpsf:select>
			</p>
		</fieldset>
	 
		<p> 
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.add')}" cssClass="button" />
		</p>
	</s:form>
</div>