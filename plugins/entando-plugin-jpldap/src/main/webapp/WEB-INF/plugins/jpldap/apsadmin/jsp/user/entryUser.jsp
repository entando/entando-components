<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set var="targetNS" value="%{'/do/User'}" />
<h1><s:text name="title.userManagement" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">

<s:if test="getStrutsAction() == 1">
	<h2 class="margin-more-bottom"><s:text name="title.userManagement.userNew" /></h2>
</s:if>
<s:if test="getStrutsAction() == 2">
	<h2 class="margin-more-bottom"><s:text name="title.userManagement.userEdit" /></h2>	
</s:if>

<s:form action="save">
	<s:if test="hasFieldErrors()">
<div class="message message_error">	
<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
</div>
	</s:if>
	<s:if test="hasActionErrors()">
<div class="message message_error">	
<h3><s:text name="message.title.ActionErrors" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
</div>
	</s:if>
	
<p class="noscreen">
	<wpsf:hidden name="strutsAction" />
</p>

<s:if test="getStrutsAction() == 2">
<p class="noscreen">
	<wpsf:hidden name="username" />
	<wpsf:hidden name="remoteUser" />
</p>
</s:if>

<fieldset><legend><s:text name="label.info" /></legend>
<p>
	<label for="username" class="basic-mint-label"><s:text name="username" />:</label>
	<wpsf:textfield useTabindexAutoIncrement="true" name="username" id="username" disabled="%{getStrutsAction() == 2}" cssClass="text" />
</p>

<p>
	<label for="password" class="basic-mint-label"><s:text name="password" />:</label>
	<wpsf:password useTabindexAutoIncrement="true" name="password" id="password" cssClass="text" />
</p>

<p>
	<label for="passwordConfirm" class="basic-mint-label"><s:text name="passwordConfirm" />:</label>
	<wpsf:password useTabindexAutoIncrement="true" name="passwordConfirm" id="passwordConfirm" cssClass="text" />
</p>

<s:if test="(strutsAction == 1 && !isWriteUserEnable()) || (strutsAction == 2 && !isRemoteUser())">
<p>
	<wpsf:checkbox useTabindexAutoIncrement="true" name="active" id="active" cssClass="radiocheck" /><label for="active"><s:text name="note.userStatus.active" /></label>
</p>
</s:if>
</fieldset>

<s:if test="strutsAction == 2 && !isRemoteUser()">
<fieldset><legend><s:text name="label.state" /></legend>
<dl class="table-display">
<dt><s:text name="label.date.registration" /></dt>
	<dd><s:date name="user.creationDate" format="dd/MM/yyyy" /></dd>
<dt><s:text name="label.date.lastLogin" /></dt>
	<dd>
	<s:if test="user.lastAccess != null">
	<s:date name="user.lastAccess" format="dd/MM/yyyy" />
	<s:if test="!user.accountNotExpired">&#32;<span class="important">(<s:text name="note.userStatus.expiredAccount" />)</span></s:if>
	</s:if>
	<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
	</dd>
<dt><s:text name="label.date.lastPasswordChange" /></dt>
	<dd>
	<s:if test="user.lastPasswordChange != null">
	<s:date name="user.lastPasswordChange" format="dd/MM/yyyy" /><s:if test="!user.credentialsNotExpired">&#32;<span class="important">(<s:text name="note.userStatus.expiredPassword" />)</span></s:if>
	</s:if>
	<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
	</dd>
</dl>	
<p>
	<wpsf:checkbox useTabindexAutoIncrement="true" name="reset" id="reset" cssClass="radiocheck" /><label for="reset"><s:text name="note.userStatus.reset" /></label>
</p>

</fieldset>
</s:if>

<p class="centerText">
	<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
</p>

</s:form>

</div>