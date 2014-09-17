<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1><s:text name="jpnewsletter.title.newsletterManagement" /></h1>
<div id="main">
<h2><s:text name="title.jpnewsletter.subscribersManagement" /></h2>
<p><s:text name="jpnewsletter.note.subscribers.intro" /></p>
<s:form action="search">
	<p>
		<label for="search_mail_add" class="basic-mint-label label-search"><s:text name="jpnewsletter.label.search.mailaddress" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" id="search_mail_add" cssClass="text" name="insertedMailAddress" />
	</p>

<fieldset><legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
<div class="accordion_element">
<p>
	<wpsf:radio useTabindexAutoIncrement="true" checked="insertedActive == 1" name="insertedActive" value="1" id="jpnewsletter_search_active" cssClass="radiocheck" /><label for="jpnewsletter_search_active"><s:text name="jpnewsletter.label.search.active" /></label>
	<wpsf:radio useTabindexAutoIncrement="true" checked="insertedActive == 0" name="insertedActive" value="0" id="jpnewsletter_search_not_active" /><label for="jpnewsletter_search_not_active"><s:text name="jpnewsletter.label.search.notactive" /></label>
	<wpsf:radio useTabindexAutoIncrement="true" checked="insertedActive == null" name="insertedActive" value="" id="jpnewsletter_search_all"/><label for="jpnewsletter_search_all"><s:text name="jpnewsletter.label.search.all" /></label>
</p>
</div>
</fieldset>
	<p>
		<wpsf:submit useTabindexAutoIncrement="true" action="search" cssClass="button" value="%{getText('label.search')}"/>
	</p>
</s:form>
<div class="subsection-light">

<s:set var="subscribersVar" value="subscribers" />

<s:if test="#subscribersVar != null && #subscribersVar.size() > 0">
<table class="generic" summary="<s:text name="jpnewsletter.subscribersList.summary" />">
	<caption><span><s:text name="jpnewsletter.subscribersList.caption" /></span></caption>
	<tr>
		<th><s:text name="label.email" /> </th>
		<th><s:text name="label.subscribtionDate" /></th>
		<th class="icon"><abbr title="<s:text name="label.state.active.full" />"><s:text name="label.state.active.short" /></abbr></th>
		<th class="icon"><abbr title="<s:text name="label.remove.full" />">&ndash;</abbr></th>
	</tr>
	<s:iterator value="#subscribersVar" var="subscriber">
		<tr>
			<td><s:property value="#subscriber.mailAddress" /></td>
			<td class="monospace"><s:date name="#subscriber.subscriptionDate" format="dd/MM/yyyy" /></td> 
			<td class="icon">
				<s:if test="#subscriber.active == 1"><s:set var="newsletterUserStatus">true</s:set></s:if>
				<s:else><s:set var="newsletterUserStatus">false</s:set></s:else>
				<img src="<wp:resourceURL/>administration/common/img/icons/<s:property value="#newsletterUserStatus" />.png" alt="<s:property value='%{getText("label.state.active."+#newsletterUserStatus)}' />" title="<s:property value='%{getText("label.state.active."+#newsletterUserStatus)}' />"/>
			</td>
			<td class="icon">
				<s:url var="removeAction" action="trash.action"><s:param name="mailAddress" value="#subscriber.mailAddress" /></s:url>
				<a href="<s:property value="#removeAction" />" title="<s:text name="label.remove" />:&#32;<s:property value="#subscriber.mailAddress" />">
					<img src="<wp:resourceURL/>administration/common/img/icons/delete.png" alt="<s:text name="label.remove.full" />" />
				</a>
			</td>
		</tr>
	</s:iterator>
</table>
</s:if>
<s:else><s:text name="jpnewsletter.subscribers.empty" /></s:else>

</div>
</div>