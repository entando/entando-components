<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"  %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"  %>
<s:set var="targetNS" value="%{'/do/jpaddressbook/AddressBook'}" />
<h1 class="panel panel-default title-page">
  <span class="panel-body display-block"><a href="/testdevelop/do/jpaddressbook/AddressBook/list.action"><s:text name="title.contactManagement" /></a>&#32;/&#32;<s:text name="jpaddressbook.title.trash" />
  </span>
</h1>
<div id="main">
  <%--<legend class="accordion_toggler"><s:text name="jpaddressbook.title.trash" /></legend>--%>
	<s:set var="contact" value="%{getContact(entityId)}" />
	<s:set value="#contact.contactInfo.firstNameAttributeName" var="nameKey"/>
	<s:set value="#contact.contactInfo.surnameAttributeName" var="surnameKey"/>
	<s:set value="#contact.contactInfo.getAttribute(#nameKey).getText()" var="name" />
	<s:set value="#contact.contactInfo.getAttribute(#surnameKey).getText()" var="surname" />
	<s:form action="delete">
		<div class="alert alert-warning">
			<wpsf:hidden name="entityId"/>
			<s:text name="jpaddressbook.admin.confirmRemove" />&#32;
			<code>
        <em class="important"><s:property value="#name" />&#32;<s:property value="#surname" /></em>&#32;
      	(<s:property value="#contact.id"/>)?&#32;     
      </code>			
      <div class="text-center margin-large-top"> 
        <s:submit useTabindexAutoIncrement="true"  type="button" action="delete" cssClass="btn btn-warning btn-lg">
          <span class="icon fa fa-times-circle"></span>&#32;
          <s:text name="label.remove" />
        </s:submit>
      </div>		
    </div>
		<%--<p><s:text name="jpaddressbook.admin.confirmRemoveGoBack" />&#32;<a href="<s:url namespace="/do/jpaddressbook/AddressBook" action="list" />"><s:text name="jpaddressbook.title.addressbook" /></a></p>--%>
	</s:form>
</div>