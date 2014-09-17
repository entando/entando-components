<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="jpnewsletter.title.newsletterManagement" /></h1>

<div id="main">
<h2 class="margin-more-bottom"><s:text name="jpnewsletter.title.newsletterConfig" /></h2>

<s:form>
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

	<fieldset><legend><s:text name="jpnewsletter.title.basic" /></legend>
	
		<p>
			<wpsf:checkbox useTabindexAutoIncrement="true" id="jpnewsletter_active" name="activeService" cssClass="radiocheck" />
			<label for="jpnewsletter_active"><s:text name="jpnewsletter.active" /></label> 
		</p>
			
		<p>
			<label for="jpnewsletter_day_cal" class="basic-mint-label"><s:text name="jpnewsletter.startday" />:</label>
			<s:set var="startSchedulerDateValue" ><s:if test="null != newsletterConfig.startScheduler"><s:date name="newsletterConfig.startScheduler" format="dd/MM/yyyy" /></s:if></s:set>
			<wpsf:textfield useTabindexAutoIncrement="true" id="jpnewsletter_day_cal" name="newsletterConfig.startScheduler" value="%{#startSchedulerDateValue}" maxlength="254" cssClass="text" />
			<span class="inlineNote"><s:text name="jpnewsletter.time.format" /></span>
		</p>
		
		<p>
			<label for="jpnewsletter_hour" class="basic-mint-label"><s:text name="jpnewsletter.hour" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_hour" name="startSchedulerHour" list="#{0:0,1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,11:11,12:12,13:13,14:14,15:15,16:16,17:17,18:18,19:19,20:20,21:21,22:22,23:23}" cssClass="text width4em" />
			<label for="jpnewsletter_min"><s:text name="jpnewsletter.minute" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_min" name="startSchedulerMinute" list="#{0:00,5:05,10:10,15:15,20:20,25:25,30:30,35:35,40:40,45:45,50:50,55:55}" cssClass="text width4em" />
			<label for="jpnewsletter_delay" ><s:text name="jpnewsletter.delay" />:</label> 
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_delay" name="newsletterConfig.hoursDelay" list="#{
				24: getText('delay.Oneday'),
				48: getText('delay.Two.days'),
				96: getText('delay.Four.days'),
				168: getText('delay.One.Week'),
				336: getText('delay.Two.weeks'),
				672: getText('delay.Four.weeks')
			}" cssClass="text width6em"/>
		</p>
       
      <s:if test="%{newsletterConfig.contentTypes.values().size() > 0}">  
			<table class="generic" summary="<s:text name="jpnewsletter.title.contentTypes.list" />">
				<caption><span><s:text name="jpnewsletter.configuredContentTypes" /></span></caption>
				<tr>
					<th><s:text name="jpnewsletter.contentType" /></th>
					<th><s:text name="jpnewsletter.plainModel" /></th>
					<th><s:text name="jpnewsletter.htmlModel" /></th>
					<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
				</tr>
				<s:iterator value="newsletterConfig.contentTypes.values()" id="contentTypeConfig" >
					<tr>
						<td><s:property value="getSmallContentType(#contentTypeConfig.contentTypeCode).descr"  /></td> 
						<td><s:property value="getContentModel(#contentTypeConfig.simpleTextModel).description"  /></td> 
						<td><s:property value="getContentModel(#contentTypeConfig.htmlModel).description"  /></td>
						<td class="icon">
							<s:set name="removeIcon"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
							<wpsa:actionParam action="removeContentType" var="actionName">
								<wpsa:actionSubParam name="contentTypeCode" value="%{#contentTypeConfig.contentTypeCode}" />
							</wpsa:actionParam> 
							<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#removeIcon}" action="%{#actionName}" value="%{getText('label.remove')}: %{getSmallContentType(#contentTypeConfig.contentTypeCode).descr}" title="%{getText('label.remove')}: %{getSmallContentType(#contentTypeConfig.contentTypeCode).descr}"  />
						</td>
					</tr>
				</s:iterator>
			</table>
		</s:if>
		<s:else>
			<p><s:text name="jpnewsletter.contentTypes.empty" /></p>
		</s:else>
		
		<p>
			<label for="jpnewsletter_addcontenttype" class="basic-mint-label"><s:text name="jpnewsletter.addContentType" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_addcontenttype" name="contentTypeCode" list="contentTypes" listKey="code" listValue="descr" />
			<wpsf:submit useTabindexAutoIncrement="true" action="addContentType" value="%{getText('label.add')}" cssClass="button" />
		</p>
		
	</fieldset>
	
	<fieldset id="categories">
	<legend class="accordion_toggler"><s:text name="jpnewsletter.config.categories" /></legend>
	<div class="accordion_element">
		<s:set var="categoryMappingKeys" value="newsletterConfig.subscriptions.keySet()" /> 
		<s:if test="%{#categoryMappingKeys.size() > 0}">
			<table class="generic" summary="jpnewsletter.note.categories.list">
				<caption><span><s:text name="jpnewsletter.configured.categories.caption" /></span></caption>
				<tr>
					<th><s:text name="jpnewsletter.description" /></th>	
					<th><s:text name="jpnewsletter.code" /></th>	
					<th><s:text name="jpnewsletter.profileAttribute" /></th>	
					<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr> </th>	
				</tr>
				<s:iterator value="#categoryMappingKeys" id="categoryMappingKey" >
					<tr>
						<td><s:property value="getTitle(#categoryMappingKey, getCategory(#categoryMappingKey).titles)" /></td>
						<td><s:property value="#categoryMappingKey" /></td>
						<td><s:property value="%{newsletterConfig.subscriptions[#categoryMappingKey]}" /></td>
						<td class="icon">
							<s:set name="removeIcon"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
							<wpsa:actionParam action="removeCategoryMapping" var="actionName" >
								<wpsa:actionSubParam name="categoryCode" value="%{#categoryMappingKey}" />
							</wpsa:actionParam>
							<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#removeIcon}" action="%{#actionName}" value="%{getText('label.remove')}: %{#categoryMappingKey}"  title="%{getText('label.remove')}: %{#categoryMappingKey}"/>
						</td>
					</tr>
				</s:iterator>
			</table>
		</s:if>
		<s:else>
			<p><s:text name="jpnewsletter.category.empty" /></p>
		</s:else>
		<p>
			<label for="jpnewsletter_catlist" class="basic-mint-label"><s:text name="jpnewsletter.category" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_catlist" name="categoryCode" list="categories" listKey="code" listValue="getFullTitle(currentLang.code)" cssClass="text" />
		</p>
		<p>
			<label for="jpnewsletter_catattr" class="basic-mint-label"><s:text name="jpnewsletter.category.profileAttribute" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_catattr" name="attributeName" list="booleanProfileAttributes" listKey="name" listValue="name" />
		</p>
		<p>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.add')}" action="addCategoryMapping" cssClass="button" />
		</p>
	</div>
	</fieldset>
	
	<fieldset class="margin-more-top">
	<legend><s:text name="jpnewsletter.title.email" /></legend>
	<p><s:text name="jpnewsletter.config.help" /></p>		
		<p>
			<label for="jpnewsletter_sender" class="basic-mint-label"><s:text name="jpnewsletter.email.sender" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_sender" name="newsletterConfig.senderCode" list="mailSenders" listKey="key" listValue="value" />
		</p>
		<p>
			<label for="jpnewsletter_subject" class="basic-mint-label"><s:text name="jpnewsletter.email.subject" />:</label>  
			<wpsf:textfield useTabindexAutoIncrement="true" id="jpnewsletter_subject" name="newsletterConfig.subject" cssClass="text" />
		</p>
		<p>
			<label for="jpnewsletter_textheader" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.textheader" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_textheader" name="newsletterConfig.textHeader" cols="50" rows="3" />
		</p>
		<p>
			<label for="jpnewsletter_textfooter" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.textfooter" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_textfooter" name="newsletterConfig.textFooter" cols="50" rows="3" />
		</p>
		<p>
			<s:if test="#myClient == 'advanced'">
				<wpsf:checkbox useTabindexAutoIncrement="true" id="jpnewsletter_alsoHtml" name="alsoHtml" cssClass="radiocheck" onclick="disable(this, this.form.jpnewsletter_htmlheader) & disable(this, this.form.jpnewsletter_htmlfooter)"/>
			</s:if>
			<s:else>
				<wpsf:checkbox useTabindexAutoIncrement="true" id="jpnewsletter_alsoHtml" name="alsoHtml" cssClass="radiocheck"/>
			</s:else>
			<label for="jpnewsletter_alsoHtml"><s:text name="jpnewsletter.email.alsoHtml" /></label> 
		</p>
		<s:if test="#myClient == 'advanced'"><s:set var="disableText" value="%{!alsoHtml}" /></s:if>
		<p>
			<label for="jpnewsletter_htmlheader" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.htmlheader" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_htmlheader" name="newsletterConfig.htmlHeader" cols="50" rows="3" disabled="#disableText"/>
		</p>
		<p>
			<label for="jpnewsletter_htmlfooter" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.htmlfooter" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_htmlfooter" name="newsletterConfig.htmlFooter" cols="50" rows="3" disabled="#disableText"/>
		</p>
	</fieldset>
	
	<fieldset id="subscription" class="margin-more-top">
		<legend class="accordion_toggler"><s:text name="jpnewsletter.title.signup" /></legend>
		<div class="accordion_element">
		<p><s:text name="jpnewsletter.subscription.help" /></p>
		
		<h3><s:text name="jpnewsletter.subscription.users" /></h3>
		<p><s:text name="jpnewsletter.subscription.help1" /></p>
		
		<p>
			<label for="jpnewsletter_emailmapping" class="basic-mint-label"><s:text name="jpnewsletter.mapping.email.profile" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_emailmapping" name="newsletterConfig.mailAttrName" list="defaultProfile.attributeList" listKey="name" listValue="name" />
		</p>
		
		<p>
			<label for="jpnewsletter_subscription_attr" class="basic-mint-label"><s:text name="jpnewsletter.mapping.subscription.preference" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="jpnewsletter_subscription_attr" name="newsletterConfig.allContentsAttributeName" list="booleanProfileAttributes" listKey="name" listValue="name" />
		</p>
		
		<div class="subsection-light">
		<h3><s:text name="jpnewsletter.subscription.guests" /></h3>
		
		 <h4><s:text name="jpnewsletter.title.signup" /></h4>

		<p>
			<label for="newsletterConfig_subscriptionPageCode" class="basic-mint-label"><s:text name="jpnewsletter.email.page" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" list="pages" name="newsletterConfig.subscriptionPageCode" id="newsletterConfig_subscriptionPageCode" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.none')}" />
		</p>
		
		<p>
			<label for="jpnewsletter.subscriptors_confirm_tokenValidity" class="basic-mint-label"><s:text name="jpnewsletter.email.tokenValidity" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" id="jpnewsletter.subscriptors_confirm_tokenValidity" name="newsletterConfig.subscriptionTokenValidityDays" cssClass="text" />
		</p>
		<p>
			<label for="jpnewsletter.subscriptors_confirm_subject" class="basic-mint-label"><s:text name="jpnewsletter.email.subject" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" id="jpnewsletter.subscriptors_confirm_subject" name="newsletterConfig.subscriptionSubject" cssClass="text" />
		</p>
		<p><s:text name="jpnewsletter.subscription.link.help" /></p>
		<p>
			<label for="jpnewsletter_subscription_confirm_body_text" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.body.text" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_subscription_confirm_body_text" name="newsletterConfig.subscriptionTextBody" cssClass="text" cols="50" rows="4" />
		</p>
		<p>
			<label for="jpnewsletter_subscription_confirm_body_html" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.body.html" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_subscription_confirm_body_html" name="newsletterConfig.subscriptionHtmlBody" cssClass="text" cols="50" rows="4" />
		</p>
			
		<h4><s:text name="jpnewsletter.title.cancellation" /></h4>
		
		<p>
			<label for="newsletterConfig_unsubscriptionPageCode" class="basic-mint-label" ><s:text name="jpnewsletter.email.page" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" list="pages" name="newsletterConfig.unsubscriptionPageCode" id="newsletterConfig_unsubscriptionPageCode" listKey="code" listValue="getShortFullTitle(currentLang.code)" 
					headerKey="" headerValue="%{getText('label.none')}" />
		</p>
		
		<p><s:text name="jpnewsletter.cancellation.link.help" /></p>
		<p>
			<label for="jpnewsletter_subscribers_label_footer_text" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.textfooter" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_subscribers_label_footer_text" name="newsletterConfig.subscribersTextFooter" cols="50" rows="3" ></wpsf:textarea>
		</p>
		
		<p>
			<label for="jpnewsletter_subscribers_label_footer_html" class="basic-mint-label alignTop"><s:text name="jpnewsletter.email.htmlfooter" />:</label>
			<wpsf:textarea useTabindexAutoIncrement="true" id="jpnewsletter_subscribers_label_footer_html" name="newsletterConfig.subscribersHtmlFooter" cols="50" rows="3" ></wpsf:textarea>
		</p>
		</div>
		</div>
	</fieldset> 

	<p class="centerText">
		<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" action="save" cssClass="button" />
	</p>
</s:form>
</div>