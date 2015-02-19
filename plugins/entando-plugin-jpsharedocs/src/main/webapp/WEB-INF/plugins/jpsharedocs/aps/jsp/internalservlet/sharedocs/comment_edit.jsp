<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<wp:headInfo type="CSS" info="../../plugins/jpsharedocs/static/css/jpsharedocs.css" />

<p><wp:i18n key="jpsharedocs_COMMENT_ADDINFO" /></p>

<form action="<wp:action path="/ExtStr2/do/jpsharedocs/Document/saveComment" ><wp:parameter name="contentId" ><s:property value="contentId" /></wp:parameter></wp:action>" method="post" enctype="multipart/form-data" >
	<s:if test="hasActionErrors()">
		<h4><wp:i18n key="ERRORS" /></h4>
		<ul>
			<s:iterator value="ActionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</s:if>
	
	<s:if test="hasFieldErrors()">
		<h4><wp:i18n key="ERRORS" /></h4>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</s:if>
	
	<p>
		<label for="jpsharedocs_comment_text"><wp:i18n key="jpsharedocs_COMMENT_TEXT" /></label><br />
		<wpsf:textarea cols="15" rows="5" cssClass="text" id="jpsharedocs_comment_text" name="commento" />
	</p>
	
	<p>
		<s:set name="submitLabel" ><wp:i18n key="jpsharedocs_SAVE" /></s:set> 
		<wpsf:submit cssClass="button" value="%{#submitLabel}" />
	</p>			
	
</form>

<jacms:content contentId="${contentId}" modelId="20094"/>