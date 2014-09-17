<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<s:set name="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
<wpsf:checkbox useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="%{#attributeTracer.getFormFieldName(#attribute)+#random}" value="#checkedValue" />&#32;
<label for="<s:property value="#attributeTracer.getFormFieldName(#attribute)+#random" />"><s:text name="label.true" /></label>