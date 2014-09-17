<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<p><s:text name="jpnewsletter.title.newsletterEntry.info" />: <em><s:property value="contentVo.descr"/></em>.</p>
<s:set var="contentReport" value="contentReport" />

<s:if test="%{#contentReport==null}">
<p><s:text name="Message.newsletter.notSent" />.</p>
</s:if>

<s:else>
<p><s:text name="Message.newsletter.sent" />&#32;<s:date name="%{#contentReport.sendDate}" format="dd/MM/yyyy" />.</p>
	
	<dl class="table-display">		
		<dt><s:text name="jpnewsletter.label.subject" /></dt>
			<dd><s:property value="%{#contentReport.subject}"/></dd>
		<dt><s:text name="jpnewsletter.label.simpleTextBody" /></dt>
			<dd><s:property value="%{#contentReport.textBody}" /></dd>
	</dl>
<%--<wpsf:textarea useTabindexAutoIncrement="true" disabled="true" id="body" value="%{#contentReport.textBody}" cols="60" rows="20" cssClass="text"/>
	<s:if test="%{#contentReport.htmlBody!=null && #contentReport.htmlBody!=''}">
<p><a href="<s:url action="viewHtmlBody" >
	<s:param name="contentId">
		<s:property value="contentId"/>
	</s:param>
</s:url>" ><s:text name="label.viewHtmlBody"/></a></p>
</s:if>
--%>

<s:if test="%{#contentReport.recipients != null && #contentReport.recipients.size() > 0}">
	<table class="generic" summary="<s:text name="jpnewsletter.note.newsletter.dest.summary" />">
	<caption><span><s:text name="jpnewsletter.title.newsletter.dest.caption" /></span></caption>
		<tr>
			<th><s:text name="label.username"/></th>
			<th><s:text name="label.eMail"/></th>
		</tr>
	<s:iterator value="%{#contentReport.recipients.entrySet()}" var="recipient">
		<tr>
			<td><s:property value="#recipient.key"/></td>
			<td><s:property value="#recipient.value"/></td>
		</tr>
	</s:iterator>
	</table>
</s:if>
</s:else>