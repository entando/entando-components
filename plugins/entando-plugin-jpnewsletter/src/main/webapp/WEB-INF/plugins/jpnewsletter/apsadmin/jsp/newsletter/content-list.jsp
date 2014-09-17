<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1><s:text name="jpnewsletter.title.newsletterManagement" /></h1>

<div id="main">
<h2><s:text name="title.contentList" /></h2>

<p><s:text name="jpnewsletter.note.intro" /></p>
<p><s:text name="jpnewsletter.note.intro.bis" /></p>

<s:form action="search" >
<p class="noscreen">
	<input type="hidden" name="lastGroupBy"/>
	<input type="hidden" name="lastOrder"/>
</p>

<p><label for="text" class="basic-mint-label label-search"><s:text name="label.description"/>:</label>
<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" /></p>

<fieldset><legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
	<div class="accordion_element">
	
	<p>
	<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" 
		list="contentTypes" listKey="code" listValue="descr" 
		headerKey="" headerValue="%{getText('label.all')}" cssClass="text"></wpsf:select>
	</p>
	
	<p>
	<label for="inQueue" class="basic-mint-label"><s:text name="label.state"/>:</label>
	<select name="inQueue" id="inQueue" class="text" tabindex="<wpsa:counter />">
		<option value=""><s:text name="label.all" /></option>
		<option value="1" <s:if test="inQueue==\"1\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.inQueue" /></option>
		<option value="2" <s:if test="inQueue==\"2\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.notInQueue" /></option>
	</select>
	</p>
	
	<p>
	<label for="sent" class="basic-mint-label"><s:text name="jpnewsletter.label.sent"/>:</label>
	<select name="sent" id="sent" class="text" tabindex="<wpsa:counter />">
		<option value=""><s:text name="label.all" /></option>
		<option value="1" <s:if test="sent==\"1\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.sent.yes" /></option>
		<option value="2" <s:if test="sent==\"2\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.sent.no" /></option>
	</select>
	</p>
	</div>
</fieldset>

<fieldset><legend class="accordion_toggler"><s:text name="title.searchResultOptions" /></legend>
	<div class="accordion_element">
		<ul class="noBullet radiocheck">
			<li><wpsf:checkbox useTabindexAutoIncrement="true" name="viewCode" id="viewCode"></wpsf:checkbox><label for="viewCode"><s:text name="label.code"/></label></li>
			<li><wpsf:checkbox useTabindexAutoIncrement="true" name="viewTypeDescr" id="viewTypeDescr"></wpsf:checkbox><label for="viewTypeDescr"><s:text name="name.contentType"/></label></li>
			<li><wpsf:checkbox useTabindexAutoIncrement="true" name="viewGroup" id="viewGroup"></wpsf:checkbox><label for="viewGroup"><s:text name="label.group"/></label></li>
			<li><wpsf:checkbox useTabindexAutoIncrement="true" name="viewCreationDate" id="viewCreationDate"></wpsf:checkbox><label for="viewCreationDate"><s:text name="label.creationDate"/></label></li>
		</ul>
	</div>
</fieldset>

<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" /></p>

</s:form>

<div class="subsection-light">

<s:form action="search" >
<p class="noscreen">
	<wpsf:hidden name="text" />
	<wpsf:hidden name="contentType" />
	<wpsf:hidden name="inQueue" />
	<wpsf:hidden name="sent" />
	<wpsf:hidden name="viewCode" />
	<wpsf:hidden name="viewTypeDescr" />
	<wpsf:hidden name="viewGroup" />
	<wpsf:hidden name="viewCreationDate" />
	<wpsf:hidden name="lastGroupBy" />
	<wpsf:hidden name="lastOrder" />
</p>

<s:if test="hasActionErrors()">
	<div class="message message_error">
	<h3><s:text name="message.title.ActionErrors" /></h3>	
		<ul>
		<s:iterator value="ActionErrors">
			<li><s:property/></li>
		</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasActionMessages()">
	<div class="message message_confirm">
	<h3><s:text name="messages.confirm" /></h3>	
	<ul>
		<s:iterator value="actionMessages">
			<li><s:property/></li>
		</s:iterator>
	</ul>
	</div>
</s:if>

<wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
<s:set name="group" value="#groupContent" />

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<table class="generic" id="contentListTable" summary="<s:text name="jpnewsletter.note.contentList.summary" />">
<caption><span><s:text name="jpnewsletter.title.contentList.caption" /></span></caption>
<tr>
	<th><a href="
	<s:url action="changeOrder" anchor="contentListTable">
		<s:param name="text">
			<s:property value="text"/>
		</s:param>
		<s:param name="contentType">
			<s:property value="contentType"/>
		</s:param>
		<s:param name="inQueue">
			<s:property value="inQueue"/>
		</s:param>
		<s:param name="sent">
			<s:property value="sent"/>
		</s:param>
		<s:param name="viewCode">
			<s:property value="viewCode"/>
		</s:param>
		<s:param name="viewTypeDescr">
			<s:property value="viewTypeDescr"/>
		</s:param>
		<s:param name="viewGroup">
			<s:property value="viewGroup"/>
		</s:param>
		<s:param name="viewCreationDate">
			<s:property value="viewCreationDate"/>
		</s:param>
		<s:param name="pagerItem">
			<s:property value="#groupContent.currItem"/>
		</s:param>
		<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
		<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
		<s:param name="groupBy">descr</s:param>
	</s:url>
"><s:text name="label.description" /></a></th>
	
	<s:if test="viewCode">
	<th><a href="
	<s:url action="changeOrder" anchor="contentListTable">
		<s:param name="text">
			<s:property value="text"/>
		</s:param>
		<s:param name="contentType">
			<s:property value="contentType"/>
		</s:param>
		<s:param name="inQueue">
			<s:property value="inQueue"/>
		</s:param>
		<s:param name="sent">
			<s:property value="sent"/>
		</s:param>
		<s:param name="viewCode">
			<s:property value="viewCode"/>
		</s:param>
		<s:param name="viewTypeDescr">
			<s:property value="viewTypeDescr"/>
		</s:param>
		<s:param name="viewGroup">
			<s:property value="viewGroup"/>
		</s:param>
		<s:param name="viewCreationDate">
			<s:property value="viewCreationDate"/>
		</s:param>
		<s:param name="pagerItem">
			<s:property value="#groupContent.currItem"/>
		</s:param>
		<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
		<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
		<s:param name="groupBy">code</s:param>
	</s:url>
"><s:text name="label.code" /></a></th>
	</s:if>
	<s:if test="viewTypeDescr"><th><s:text name="label.type" /></th></s:if>
	<s:if test="viewGroup"><th><s:text name="label.group" /></th></s:if>
	<s:if test="viewCreationDate">
	<th><a href="
	<s:url action="changeOrder" anchor="contentListTable">
		<s:param name="text">
			<s:property value="text"/>
		</s:param>
		<s:param name="contentType">
			<s:property value="contentType"/>
		</s:param>
		<s:param name="inQueue">
			<s:property value="inQueue"/>
		</s:param>
		<s:param name="sent">
			<s:property value="sent"/>
		</s:param>
		<s:param name="viewCode">
			<s:property value="viewCode"/>
		</s:param>
		<s:param name="viewTypeDescr">
			<s:property value="viewTypeDescr"/>
		</s:param>
		<s:param name="viewGroup">
			<s:property value="viewGroup"/>
		</s:param>
		<s:param name="viewCreationDate">
			<s:property value="viewCreationDate"/>
		</s:param>
		<s:param name="pagerItem">
			<s:property value="#groupContent.currItem"/>
		</s:param>
		<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
		<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
		<s:param name="groupBy">created</s:param>
	</s:url>
"><s:text name="label.creationDate" /></a></th>
</s:if>
	<th><a href="
	<s:url action="changeOrder" anchor="contentListTable">
		<s:param name="text">
			<s:property value="text"/>
		</s:param>
		<s:param name="contentType">
			<s:property value="contentType"/>
		</s:param>
		<s:param name="inQueue">
			<s:property value="inQueue"/>
		</s:param>
		<s:param name="sent">
			<s:property value="sent"/>
		</s:param>
		<s:param name="viewCode">
			<s:property value="viewCode"/>
		</s:param>
		<s:param name="viewTypeDescr">
			<s:property value="viewTypeDescr"/>
		</s:param>
		<s:param name="viewGroup">
			<s:property value="viewGroup"/>
		</s:param>
		<s:param name="viewCreationDate">
			<s:property value="viewCreationDate"/>
		</s:param>
		<s:param name="pagerItem">
			<s:property value="#groupContent.currItem"/>
		</s:param>
		<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
		<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
		<s:param name="groupBy">lastModified</s:param>
	</s:url>
"><s:text name="label.lastEdit" /></a></th>
	<th><s:text name="jpnewsletter.label.sendDate" /></th>
	<th class="icon"><abbr title="<s:text name="label.state" />">S</abbr></th>
	<th class="icon"><abbr title="<s:text name="jpnewsletter.label.details" />">D</abbr></th>
</tr>
<s:iterator id="contentId">
<s:set name="content" value="%{getContentVo(#contentId)}" />
<s:set name="contentReport" value="%{getContentReport(#contentId)}" />
<tr>
<td>
	<input type="checkbox" name="contentIds" id="content_<s:property value="#content.id" />" value="<s:property value="#content.id" />" />
	<label for="content_<s:property value="#content.id" />"><s:property value="#content.descr" /></label>
</td>
<s:if test="viewCode"><td><span class="monospace"><s:property value="#content.id" /></span></td></s:if>
<s:if test="viewTypeDescr"><td><s:property value="%{getSmallContentType(#content.typeCode).descr}" /></td></s:if>
<s:if test="viewGroup"><td><s:property value="%{getGroup(#content.mainGroupCode).descr}" /></td></s:if>
<s:if test="viewCreationDate"><td class="centerText"><span class="monospace"><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></span></td></s:if>
<td class="icon monospace centerText"><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></td>
<td class="icon">
<s:if test="%{#contentReport!=null}"><span class="monospace"><s:date name="#contentReport.sendDate" format="dd/MM/yyyy HH:mm" /></span></s:if>
<s:else><abbr title="<s:text name="neverSentNewsletter" />">-</abbr></s:else>
</td>
<td class="centerText">
<s:if test="isContentInQueue(#contentId)">
	<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/content-isnotsynched.png</wpsa:set>
	<s:set name="isWaitingNewsletter" value="%{getText('jpnewsletter.label.inQueue')}" />
	<img src="<s:property value="iconImagePath" />" alt="<s:property value="#isWaitingNewsletter" />" title="<s:property value="#isWaitingNewsletter" />" />
</s:if>
<s:else>
	<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/content-isnotonline.png</wpsa:set>
	<s:set name="notWaitingNewsletter" value="%{getText('jpnewsletter.label.notInQueue')}" />
	<img src="<s:property value="iconImagePath" />" alt="<s:property value="#notWaitingNewsletter" />" title="<s:property value="#notWaitingNewsletter" />" />
</s:else>
</td>
<td class="icon">
<a href="<s:url action="entry" ><s:param name="contentId" value="#content.id" /></s:url>" title="<s:text name="jpnewsletter.label.detailOf" />: <s:property value="#content.descr" />">
	<img src="<wp:resourceURL />plugins/jpnewsletter/administration/img/icons/system-search.png" alt="<s:text name="label.view" />" />
</a>
</td>
</tr>
</s:iterator>
</table>

<p class="centerText">
<wpsf:submit useTabindexAutoIncrement="true" action="addToQueue" value="%{getText('jpnewsletter.label.insertInQueue')}" cssClass="button" />
</p>
<%--
Da reinserire se i button diventano più di uno.
<p class="buttons">
	<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />plugins/jpnewsletter/administration/img/icons/mail-send-receive.png</wpsa:set>	
	<wpsf:submit useTabindexAutoIncrement="true" action="addToQueue" type="image" src="%{#iconImagePath}" value="%{getText('label.insertInQueue')}" title="%{getText('label.insertInQueue')}" />
</p>

--%>

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</s:form>
</div>
</div>