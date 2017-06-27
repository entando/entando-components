<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<s:set var="inputCode"><c:out value="${param.inputCode}"/></s:set>

<s:if test="%{null != #inputCode}">
    <s:set var="inputFieldErrorsVar" value="%{fieldErrors[#inputCode]}" />
    <s:set var="inputHasFieldErrorVar" value="#inputFieldErrorsVar != null && !#inputFieldErrorsVar.isEmpty()" />
    <s:set var="inputErrorClassVar" value="''" />
    <s:if test="#inputHasFieldErrorVar">
        <s:set var="inputErrorClassVar" value="' input-with-feedback'" />
    </s:if>
</s:if>