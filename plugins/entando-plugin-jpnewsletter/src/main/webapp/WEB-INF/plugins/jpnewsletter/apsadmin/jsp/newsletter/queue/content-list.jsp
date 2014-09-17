<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1><s:text name="jpnewsletter.title.newsletterManagement" /></h1>
<div id="main">
<h2><s:text name="jpnewsletter.title.newsletterQueue" /></h2>

<s:if test="%{contentIds.size() == 0}" >
	<p><s:text name="jpnewsletter.label.newsletterQueue.empty" /></p>
</s:if>
<s:else>

<s:if test="newsletterConfig.active">
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.intro" />
	</p>
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.intro.bis" />
	</p>
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.startInfo" >
			<s:param ><em class="important"><s:date name="newsletterConfig.nextTaskTime" format="dd/MM/yyyy" /></em></s:param>
			<s:param ><em class="important"><s:date name="newsletterConfig.nextTaskTime" format="HH:mm" /></em></s:param>
			<s:param value="hoursDelay" />
			<s:param value="minutesDelay" />
		</s:text>
	</p>
</s:if>
<s:else>
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.notActive" />
	</p>
</s:else>


<s:form action="search" >

<s:if test="hasActionErrors()">
<div class="message message_error">
<h2><s:text name="message.title.ActionErrors" /></h2>
	<ul>
	<s:iterator value="ActionErrors">
		<li><s:property/></li>
	</s:iterator>
	</ul>
</div>
</s:if>
<s:if test="hasActionMessages()">
<div class="message message_confirm">
<h2><s:text name="messages.confirm" /></h2>	
<ul>
	<s:iterator value="actionMessages">
		<li><s:property/></li>
	</s:iterator>
</ul>
</div>
</s:if>

<wpsa:subset source="contentIds" count="10" objectName="groupContent" advanced="true" offset="5">
<s:set name="group" value="#groupContent" />

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<table class="generic" id="contentListTable" summary="<s:text name="jpnewsletter.note.queue.summary" />">
<caption><span><s:text name="jpnewsletter.title.queue.caption" /></span></caption>
<tr>
	<th><s:text name="label.description" /></th>
	<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
</tr>


<s:iterator id="contentId" >
<s:set name="content" value="%{getContentVo(#contentId)}"></s:set>
<tr>
	<td>
		<a href="<s:url action="view" ><s:param name="contentId" value="#content.id" /></s:url>" title="<s:text name="label.view" />" >
			<s:property value="#content.descr" />
		</a>
	</td>
	<td class="icon">
		<a href="<s:url action="removeFromQueue" ><s:param name="contentId" value="#content.id" /></s:url>" title="<s:text name="label.remove" />" ><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.remove" />" /></a>
	</td>
</tr>
</s:iterator>
</table>


<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>


<s:if test="newsletterConfig.active">
<p>
<wpsf:submit useTabindexAutoIncrement="true" action="send" value="%{getText('jpnewsletter.label.sendNow')}" cssClass="button" />
</p>
</s:if>

</s:form>
</s:else>
</div>