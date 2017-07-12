<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"  %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"  %>
<s:set var="targetNS" value="%{'/do/jpaddressbook/AddressBook'}" />

<jsp:include page="../common/template/breadcrumb.jsp" />
<jsp:include page="../common/template/tabMenu.jsp" />

<div id="main">

    <s:set var="contact" value="%{getContact(entityId)}" />
    <s:set value="#contact.contactInfo.firstNameAttributeName" var="nameKey"/>
    <s:set value="#contact.contactInfo.surnameAttributeName" var="surnameKey"/>
    <s:set value="#contact.contactInfo.getAttribute(#nameKey).getText()" var="name" />
    <s:set value="#contact.contactInfo.getAttribute(#surnameKey).getText()" var="surname" />
    <s:form action="delete">
        <wpsf:hidden name="entityId"/>


        <div class="text-center mt-20">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline">
                <s:text name="label.delete" />
            </p>
            <p>
                <s:text name="jpaddressbook.admin.confirmRemove" />&#32;
                <code>
                    <em class="important"><s:property value="#name" />&#32;<s:property value="#surname" /></em>&#32;
                    (<s:property value="#contact.id"/>)?&#32;     
                </code>
            </p>
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" />">
                <s:text name="%{getText('label.back')}" />
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <span class="icon fa fa-times-circle"></span>&#32;
                <s:text name="%{getText('label.remove')}" />
            </wpsf:submit>
        </div>    

    </s:form>
</div>