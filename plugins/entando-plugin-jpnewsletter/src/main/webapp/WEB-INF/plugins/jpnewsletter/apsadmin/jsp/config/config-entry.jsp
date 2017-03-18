<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpnewsletter.title.newsletterManagement" />&#32;/&#32;<s:text name="jpnewsletter.title.newsletterConfig" />
	</span>
</h1>

<s:form>
	
	<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable fade in margin-base-top">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
		<ul class="margin-base-top">
		<s:iterator value="ActionErrors">
			<li><s:property escapeHtml="false" /></li>
		</s:iterator>
		</ul>
	</div>
	</s:if>
	
	<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable fade in margin-base-top">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
		<ul class="margin-base-top">
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
				<li><s:property escapeHtml="false" /></li>
			</s:iterator>
		</s:iterator>
		</ul>
	</div>
	</s:if>
	
	<fieldset class="margin-large-top"><legend><s:text name="jpnewsletter.title.basic" /></legend>
		<div class="form-group">
			<label class="checkbox" for="jpnewsletter_active">
				<wpsf:checkbox id="jpnewsletter_active" name="activeService" cssClass="radiocheck" />
				<s:text name="jpnewsletter.active" />
			</label> 
		</div>
		
		<div class="form-group">
			<label for="jpnewsletter_day_cal" class="basic-mint-label"><s:text name="jpnewsletter.startday" />:</label>
			<s:set var="startSchedulerDateValue" ><s:if test="null != newsletterConfig.startScheduler"><s:date name="newsletterConfig.startScheduler" format="dd/MM/yyyy" /></s:if></s:set>
			<wpsf:textfield id="jpnewsletter_day_cal" name="newsletterConfig.startScheduler" value="%{#startSchedulerDateValue}" maxlength="254" cssClass="text" />
			<span class="inlineNote"><s:text name="jpnewsletter.time.format" /></span>
		</div>
		
		<div class="form-group">
			<label for="jpnewsletter_hour" ><s:text name="jpnewsletter.hour" />:</label>
			<wpsf:select id="jpnewsletter_hour" name="startSchedulerHour" list="#{0:0,1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,11:11,12:12,13:13,14:14,15:15,16:16,17:17,18:18,19:19,20:20,21:21,22:22,23:23}" cssClass="text width4em" />
			<label for="jpnewsletter_min"><s:text name="jpnewsletter.minute" />:</label>
			<wpsf:select id="jpnewsletter_min" name="startSchedulerMinute" list="#{0:00,5:05,10:10,15:15,20:20,25:25,30:30,35:35,40:40,45:45,50:50,55:55}" cssClass="text width4em" />
			<label for="jpnewsletter_delay" ><s:text name="jpnewsletter.delay" />:</label> 
			<wpsf:select id="jpnewsletter_delay" name="newsletterConfig.hoursDelay" list="#{
				24: getText('delay.Oneday'),
				48: getText('delay.Two.days'),
				96: getText('delay.Four.days'),
				168: getText('delay.One.Week'),
				336: getText('delay.Two.weeks'),
				672: getText('delay.Four.weeks')
			}" cssClass="text width6em"/>
		</div>
		
		<div class="form-group">
			<label for="jpnewsletter_addcontenttype"><s:text name="jpnewsletter.addContentType" /></label>
			<div class="input-group">
				<wpsf:select id="jpnewsletter_addcontenttype" name="contentTypeCode" list="contentTypes" listKey="code" listValue="descr" cssClass="form-control" />
				<span class="input-group-btn">
					<wpsf:submit type="button" action="addContentType" cssClass="btn btn-info">
						<span class="icon fa fa-plus-square"></span>&#32;
						<s:text name="label.add" />
					</wpsf:submit>
				</span>
			</div>
		</div>
		
		<s:if test="%{newsletterConfig.contentTypes.values().size() > 0}">
		<div class="table-responsive">
			<table class="table table-bordered">
				<tr>
					<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
					<th><s:text name="jpnewsletter.contentType" /></th>
					<th><s:text name="jpnewsletter.plainModel" /></th>
					<th><s:text name="jpnewsletter.htmlModel" /></th>
				</tr>
				<s:iterator value="newsletterConfig.contentTypes.values()" var="contentTypeConfig" >
				<tr>
					<td class="text-center text-nowrap">
						<div class="btn-group btn-group-xs">
							<wpsa:actionParam action="removeContentType" var="actionName" >
								<wpsa:actionSubParam name="contentTypeCode" value="%{#contentTypeConfig.contentTypeCode}" />
							</wpsa:actionParam>
							<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}: %{getSmallContentType(#contentTypeConfig.contentTypeCode).descr}" cssClass="btn btn-warning">
								<span class="icon fa fa-times-circle-o"></span>
							</wpsf:submit>
						</div>
					</td>
					<td><s:property value="getSmallContentType(#contentTypeConfig.contentTypeCode).descr"  /></td> 
					<td><s:property value="getContentModel(#contentTypeConfig.simpleTextModel).description"  /></td> 
					<td>
						<s:set var="htmlModelVar" value="%{getContentModel(#contentTypeConfig.htmlModel).description}" />
						<s:if test="null != #htmlModelVar"><s:property value="#htmlModelVar" /></s:if><s:else>&ndash;</s:else>
					</td>
				</tr>
				</s:iterator>
			</table>
		</div>
		</s:if>
		<s:else>
			<p><s:text name="jpnewsletter.contentTypes.empty" /></p>
		</s:else>
		
	</fieldset>

	<fieldset class="margin-large-top">
		<legend data-toggle="collapse" data-target="#mail-configuration"><s:text name="jpnewsletter.title.email" />&#32;<span class="icon fa fa-chevron-down"></span></legend>
		<div class="collapse" id="mail-configuration">
		<p><s:text name="jpnewsletter.config.help" /></p>
		<div class="form-group">
			<label for="jpnewsletter_sender"><s:text name="jpnewsletter.email.sender" /></label>
			<wpsf:select id="jpnewsletter_sender" name="newsletterConfig.senderCode" list="mailSenders" listKey="key" listValue="value" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter_subject"><s:text name="jpnewsletter.email.subject" /></label>
			<wpsf:textfield id="jpnewsletter_subject" name="newsletterConfig.subject" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter_textheader" ><s:text name="jpnewsletter.email.textheader" /></label>
			<wpsf:textarea id="jpnewsletter_textheader" name="newsletterConfig.textHeader" cols="50" rows="3" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter_textfooter"><s:text name="jpnewsletter.email.textfooter" /></label>
			<wpsf:textarea id="jpnewsletter_textfooter" name="newsletterConfig.textFooter" cols="50" rows="3" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label class="checkbox" for="jpnewsletter_alsoHtml">
				<wpsf:checkbox id="jpnewsletter_alsoHtml" name="alsoHtml" cssClass="radiocheck"/>
				<s:text name="jpnewsletter.email.alsoHtml" />
			</label> 
		</div>
		<div class="form-group">
			<label for="jpnewsletter_htmlheader" ><s:text name="jpnewsletter.email.htmlheader" /></label>
			<wpsf:textarea id="jpnewsletter_htmlheader" name="newsletterConfig.htmlHeader" cols="50" rows="3" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter_htmlfooter" ><s:text name="jpnewsletter.email.htmlfooter" /></label>
			<wpsf:textarea id="jpnewsletter_htmlfooter" name="newsletterConfig.htmlFooter" cols="50" rows="3" cssClass="form-control" />
		</div>
		</div>
	</fieldset>
	
	<fieldset class="margin-large-top" id="subscription">
		<legend class="accordion_toggler"><s:text name="jpnewsletter.title.signup" /></legend>
		<div class="accordion_element">
		<p><s:text name="jpnewsletter.subscription.help" /></p>
		
		<h3><s:text name="jpnewsletter.subscription.users" /></h3>
		<p><s:text name="jpnewsletter.subscription.help1" /></p>
		
		<s:set var="booleanProfileAttributesVar" value="booleanProfileAttributes" />
		
		<div class="form-group">
			<label for="jpnewsletter_subscription_attr"><s:text name="jpnewsletter.mapping.subscription.preference" /></label>
			<wpsf:select id="jpnewsletter_subscription_attr" name="newsletterConfig.allContentsAttributeName" list="#booleanProfileAttributesVar" listKey="name" listValue="name" cssClass="form-control" />
		</div>
		
		<div data-toggle="collapse" data-target="#tematic-newsletter"><strong><s:text name="jpnewsletter.config.categories" /></strong>&#32;<span class="icon fa fa-chevron-down"></span></div>
		<p>Open this section to create tematic newsletter</p>
		<div class="collapse" id="tematic-newsletter">
			
			<div class="form-group">
				<label for="jpnewsletter_catlist"><s:text name="jpnewsletter.category" /></label>
				<wpsf:select id="jpnewsletter_catlist" name="categoryCode" list="categories" listKey="code" listValue="getFullTitle(currentLang.code)" cssClass="form-control" />
			</div>
			<div class="form-group">
				<label for="jpnewsletter_catattr" class="basic-mint-label"><s:text name="jpnewsletter.category.profileAttribute" /></label>
				<wpsf:select id="jpnewsletter_catattr" name="attributeName" list="booleanProfileAttributes" listKey="name" listValue="name" cssClass="form-control" />
			</div>
			<div class="form-group">
				<span class="input-group-btn">
					<wpsf:submit type="button" action="addCategoryMapping" cssClass="btn btn-info">
						<span class="icon fa fa-plus"></span>
						<s:text name="label.add"></s:text>
					</wpsf:submit>
				</span>
			</div>
			<s:set var="categoryMappingKeys" value="newsletterConfig.subscriptions.keySet()" /> 
			<s:if test="%{#categoryMappingKeys.size() > 0}">
				<div class="table-responsive">
					<table class="table table-bordered">
						<tr>
							<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
							<th><s:text name="jpnewsletter.description" /></th>
							<th><s:text name="jpnewsletter.code" /></th>
							<th><s:text name="jpnewsletter.profileAttribute" /></th>
						</tr>
						<s:iterator value="#categoryMappingKeys" var="categoryMappingKey" >
						<tr>
							<td class="text-center text-nowrap">
								<div class="btn-group btn-group-xs">
									<wpsa:actionParam action="removeCategoryMapping" var="actionName" >
										<wpsa:actionSubParam name="categoryCode" value="%{#categoryMappingKey}" />
									</wpsa:actionParam>
									<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
										<span class="icon fa fa-times-circle-o"></span>
									</wpsf:submit>
								</div>
							</td>
							<td><s:property value="getTitle(#categoryMappingKey, getCategory(#categoryMappingKey).titles)" /></td>
							<td><s:property value="#categoryMappingKey" /></td>
							<td><s:property value="%{newsletterConfig.subscriptions[#categoryMappingKey]}" /></td>
						</tr>
						</s:iterator>
					</table>
				</div>
			</s:if>
			<s:else>
				<p><s:text name="jpnewsletter.category.empty" /></p>
			</s:else>
		</div>
		<div class="subsection-light">
		<h3><s:text name="jpnewsletter.subscription.guests" /></h3>
		<h4><s:text name="jpnewsletter.title.signup" /></h4>

		<div class="form-group">
			<label for="newsletterConfig_subscriptionPageCode"><s:text name="jpnewsletter.email.page" /></label>
			<wpsf:select list="confirmSubscriptionPages" name="newsletterConfig.subscriptionPageCode" id="newsletterConfig_subscriptionPageCode" 
						 listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.none')}" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter.subscriptors_confirm_tokenValidity"><s:text name="jpnewsletter.email.tokenValidity" /></label>
			<wpsf:select id="jpnewsletter.subscriptors_confirm_tokenValidity" name="newsletterConfig.subscriptionTokenValidityDays"
							list="#{1:1,2:2,5:5,10:10,15:15,20:20}" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter.subscriptors_confirm_subject"><s:text name="jpnewsletter.email.subject" /></label>
			<wpsf:textfield id="jpnewsletter.subscriptors_confirm_subject" name="newsletterConfig.subscriptionSubject" cssClass="form-control" />
		</div>
		<p><s:text name="jpnewsletter.subscription.link.help" /></p>
		<div class="form-group">
			<label for="jpnewsletter_subscription_confirm_body_text"><s:text name="jpnewsletter.email.body.text" /></label>
			<wpsf:textarea id="jpnewsletter_subscription_confirm_body_text" name="newsletterConfig.subscriptionTextBody" cols="50" rows="4" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter_subscription_confirm_body_html"><s:text name="jpnewsletter.email.body.html" /></label>
			<wpsf:textarea id="jpnewsletter_subscription_confirm_body_html" name="newsletterConfig.subscriptionHtmlBody" cssClass="text" cols="50" rows="4" cssClass="form-control" />
		</div>
			
		<h4><s:text name="jpnewsletter.title.cancellation" /></h4>
		
		<div class="form-group">
			<label for="newsletterConfig_unsubscriptionPageCode"><s:text name="jpnewsletter.email.page" /></label>
			<wpsf:select list="confirmUnsubscriptionPages" name="newsletterConfig.unsubscriptionPageCode" id="newsletterConfig_unsubscriptionPageCode" listKey="code" listValue="getShortFullTitle(currentLang.code)" 
					headerKey="" headerValue="%{getText('label.none')}" cssClass="form-control" />
		</div>
		
		<p><s:text name="jpnewsletter.cancellation.link.help" /></p>
		<div class="form-group">
			<label for="jpnewsletter_subscribers_label_footer_text"><s:text name="jpnewsletter.email.textfooter" /></label>
			<wpsf:textarea id="jpnewsletter_subscribers_label_footer_text" name="newsletterConfig.subscribersTextFooter" cols="50" rows="3" cssClass="form-control" />
		</div>
		<div class="form-group">
			<label for="jpnewsletter_subscribers_label_footer_html"><s:text name="jpnewsletter.email.htmlfooter" /></label>
			<wpsf:textarea id="jpnewsletter_subscribers_label_footer_html" name="newsletterConfig.subscribersHtmlFooter" cols="50" rows="3" cssClass="form-control" />
		</div>
		</div>
		</div>
	</fieldset> 
		
<div class="form-group">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<wpsf:submit action="save" type="button" cssClass="btn btn-primary btn-block">
			<span class="icon fa fa-floppy-o"></span>&#32;
			<s:text name="label.save" />
		</wpsf:submit>
	</div>
</div>

</s:form>