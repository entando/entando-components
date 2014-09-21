<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:set name="titleKey">jpwebdynamicform_TITLE_<s:property value="typeCode"/></s:set>
<s:set name="subtitleKey">jpwebdynamicform_SUBTITLE_<s:property value="typeCode"/></s:set>
<s:set name="typeCodeKey" value="typeCode" />

<s:set name="myCurrentPage"><wp:currentPage param="code"/></s:set>

<h2 class="title-divider"><span><wp:i18n key="${titleKey}" /></span>
<small><wp:i18n key="${subtitleKey}" /></small></h2>

<form action="<wp:action path="/ExtStr2/do/jpwebdynamicform/Message/User/captchaConfirm.action"/>" method="post" id="formContact" >
	<s:if test="hasFieldErrors()">
	<div class="alert alert-error">
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
	<div class="alert alert-error">
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
	</s:if>
	
<s:if test="recaptchaAfterEnabled">
<script type="text/javascript" src="http://api.recaptcha.net/challenge?k=<wp:info key="systemParam" paramName="jpwebdynamicform_recaptcha_publickey" />"></script>
<noscript>
<iframe src="http://api.recaptcha.net/noscript?k=<wp:info key="systemParam" paramName="jpwebdynamicform_recaptcha_publickey" />"
 height="300" width="500" frameborder="0"></iframe><br>
 <s:textarea name="recaptcha_challenge_field" rows="3" cols="40"/>
 <s:hidden name="recaptcha_response_field" value="manual_challenge"/>
</noscript>
</s:if>

<p>
	<s:set name="labelSubmit"><wp:i18n key="jpwebdynamicform_INVIA" /></s:set>
	<wpsf:submit useTabindexAutoIncrement="true" value="%{#labelSubmit}" cssClass="btn btn-inverse"/>
</p>
</form>
