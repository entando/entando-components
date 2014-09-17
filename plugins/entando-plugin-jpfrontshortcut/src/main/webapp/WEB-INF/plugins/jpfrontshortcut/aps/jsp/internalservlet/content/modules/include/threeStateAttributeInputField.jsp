<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<input type="radio" name="<s:property value="#attributeTracer.getFormFieldName(#attribute)" />" id="true_<s:property value="#attributeTracer.getFormFieldName(#attribute)+#random" />" 
	   value="true" tabindex="<wpsa:counter />" 
	   <s:if test="#attribute.booleanValue != null && #attribute.booleanValue == true"> checked="checked" </s:if> />
<label for="true_<s:property value="#attributeTracer.getFormFieldName(#attribute)+#random" />"><s:text name="label.yes"/></label><span class="noscreen">&nbsp;,&nbsp;</span>
<input type="radio" name="<s:property value="#attributeTracer.getFormFieldName(#attribute)" />" id="false_<s:property value="#attributeTracer.getFormFieldName(#attribute)+#random" />" 
	   value="false" tabindex="<wpsa:counter />" 
	   <s:if test="#attribute.booleanValue != null && #attribute.booleanValue == false"> checked="checked" </s:if> />
<label for="false_<s:property value="#attributeTracer.getFormFieldName(#attribute)+#random" />"><s:text name="label.no"/></label><span class="noscreen">&nbsp;,&nbsp;</span>
<input type="radio" name="<s:property value="#attributeTracer.getFormFieldName(#attribute)" />" id="none_<s:property value="#attributeTracer.getFormFieldName(#attribute)+#random" />" 
	   value="" tabindex="<wpsa:counter />" 
	   <s:if test="#attribute.booleanValue == null"> checked="checked" </s:if> />
<label for="none_<s:property value="#attributeTracer.getFormFieldName(#attribute)+#random" />" ><s:text name="label.bothYesAndNo"/></label>
