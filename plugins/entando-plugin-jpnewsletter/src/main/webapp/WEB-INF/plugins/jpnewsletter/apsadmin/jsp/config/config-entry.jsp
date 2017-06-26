<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpnewsletter.integrations"/></li>
    <li><s:text name="jpnewsletter.components"/></li>
    <li><s:text name="jpnewsletter.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="jpnewsletter.title.newsletterConfig"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpnewsletter.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpnewsletter.section.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />">
                        <s:text name="jpnewsletter.admin.list"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />">
                        <s:text name="jpnewsletter.admin.queue"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Subscriber" />">
                        <s:text name="jpnewsletter.admin.subscribersList"/>
                    </a>
                </li>
                <wp:ifauthorized permission="jpnewsletter_config">
                    <li class="active">
                        <a href="<s:url action="edit" namespace="/do/jpnewsletter/Newsletter/Config" />">
                            <s:text name="jpnewsletter.title.newsletterConfig"/>
                        </a>
                    </li>
                </wp:ifauthorized>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main">
    <s:form class="form-horizontal">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>

        <fieldset class="margin-large-top">
            <legend>
                <span><s:text name="jpnewsletter.title.basic"/></span>
            </legend>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="jpnewsletter_active">
                    <s:text name="jpnewsletter.active"/>
                </label>
                <div class="col-sm-10">
                    <wpsf:checkbox id="jpnewsletter_active" data-toggle="toggle" name="activeService"
                                   cssClass="radiocheck bootstrap-switch"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="jpnewsletter_day_cal">
                    <s:text name="jpnewsletter.startday"/>
                    <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-placement="top"
                       data-content="<s:text name="jpnewsletter.time.format"/>"
                       data-original-title="">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-10">
                    <div class="row">
                        <div class="col-xs-3">

                            <s:set var="startSchedulerDateValue">
                                <s:if test="null != newsletterConfig.startScheduler">
                                    <s:date name="newsletterConfig.startScheduler" format="dd/MM/yyyy"/>
                                </s:if>
                            </s:set>
                            <div class="input-group date" data-provide="datepicker">
                                <wpsf:textfield id="jpnewsletter_day_cal" name="newsletterConfig.startScheduler"
                                                value="%{#startSchedulerDateValue}" maxlength="254"
                                                cssClass="form-control" data-date-format="dd/mm/yyyy"/>

                            </div>

                        </div>
                        <div class="col-xs-3">
                            <wpsf:select id="jpnewsletter_hour" name="startSchedulerHour"
                                         list="#{0:0,1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,11:11,12:12,13:13,14:14,15:15,16:16,17:17,18:18,19:19,20:20,21:21,22:22,23:23}"
                                         cssClass="form-control"/>
                            <span for="jpnewsletter_hour" class="help help-block"><s:text
                                    name="jpnewsletter.hour"/></span>
                        </div>
                        <div class="col-xs-3">
                            <wpsf:select id="jpnewsletter_min" name="startSchedulerMinute"
                                         list="#{0:00,5:05,10:10,15:15,20:20,25:25,30:30,35:35,40:40,45:45,50:50,55:55}"
                                         cssClass="form-control"/>
                            <span for="jpnewsletter_min" class="help help-block"><s:text
                                    name="jpnewsletter.minute"/></span>
                        </div>
                        <div class="col-xs-3">
                            <wpsf:select id="jpnewsletter_delay" name="newsletterConfig.hoursDelay" list="#{
                                         24: getText('delay.Oneday'),
                                             48: getText('delay.Two.days'),
                                             96: getText('delay.Four.days'),
                                             168: getText('delay.One.Week'),
                                             336: getText('delay.Two.weeks'),
                                             672: getText('delay.Four.weeks')
                                         }" cssClass="form-control"/>
                            <span for="jpnewsletter_delay" class="help help-block"><s:text
                                    name="jpnewsletter.delay"/></span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="jpnewsletter_addcontenttype">
                    <s:text name="jpnewsletter.addContentType"/>
                </label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <wpsf:select id="jpnewsletter_addcontenttype" name="contentTypeCode" list="contentTypes"
                                     listKey="code"
                                     listValue="descr" cssClass="form-control"/>
                        <span class="input-group-btn">
                            <wpsf:submit type="button" action="addContentType" cssClass="btn btn-primary">
                                <s:text name="label.add"/>
                            </wpsf:submit>
                        </span>
                    </div>
                </div>
            </div>

            <s:if test="%{newsletterConfig.contentTypes.values().size() > 0}">
                <div class="form-group">
                    <div class="col-sm-10 col-sm-offset-2 table-responsive">

                        <table class="table table-striped table-bordered table-hover no-mb">
                            <thead>
                                <tr>
                                    <th><s:text name="jpnewsletter.contentType"/></th>
                                    <th><s:text name="jpnewsletter.plainModel"/></th>
                                    <th><s:text name="jpnewsletter.htmlModel"/></th>
                                    <th class="text-center col-xs-1">
                                        <s:text name="label.actions"/>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="newsletterConfig.contentTypes.values()" var="contentTypeConfig">
                                    <tr>
                                        <td>
                                            <s:property
                                                value="getSmallContentType(#contentTypeConfig.contentTypeCode).descr"/>
                                        </td>
                                        <td>
                                            <s:property
                                                value="getContentModel(#contentTypeConfig.simpleTextModel).description"/>
                                        </td>
                                        <td>
                                            <s:set var="htmlModelVar"
                                                   value="%{getContentModel(#contentTypeConfig.htmlModel).description}"/>
                                            <s:if test="null != #htmlModelVar">
                                                <s:property value="#htmlModelVar"/>
                                            </s:if>
                                            <s:else>&ndash;</s:else>
                                            </td>
                                            <td class="text-center text-nowrap">
                                                <div class="btn-group btn-group-xs">
                                                <wpsa:actionParam action="removeContentType" var="actionName">
                                                    <wpsa:actionSubParam name="contentTypeCode"
                                                                         value="%{#contentTypeConfig.contentTypeCode}"/>
                                                </wpsa:actionParam>
                                                <wpsf:submit type="button" action="%{#actionName}"
                                                             title="%{getText('label.remove')}: %{getSmallContentType(#contentTypeConfig.contentTypeCode).descr}"
                                                             cssClass="btn btn-sm btn-link">
                                                    <span class="icon fa fa-trash-o fa-lg"></span>
                                                </wpsf:submit>
                                            </div>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </div>
            </s:if>
            <s:else>
                <div class="row">
                    <div class="col-sm-offset-2 col-sm-10">
                        <div class="alert alert-info">
                            <span class="pficon pficon-info"></span>
                            <s:text name="jpnewsletter.contentTypes.empty"/>
                        </div>
                    </div>
                </div>
            </s:else>
        </fieldset>

        <!-- Messages -->
        <fieldset class="margin-large-top">
            <legend data-toggle="collapse" data-target="#mail-configuration">
                <s:text name="jpnewsletter.title.email"/>&#32;<span class="icon fa fa-chevron-down"></span>
            </legend>

            <div class="collapse" id="mail-configuration">

                <div class="row">
                    <div class="col-sm-offset-2 col-sm-10">
                        <div class="alert alert-info">
                            <span class="pficon pficon-info"></span>
                            <s:text name="jpnewsletter.config.help"/>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label" for="jpnewsletter_sender">
                        <s:text name="jpnewsletter.email.sender"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:select id="jpnewsletter_sender" name="newsletterConfig.senderCode" list="mailSenders"
                                     listKey="key" listValue="value" cssClass="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="jpnewsletter_subject">
                        <s:text name="jpnewsletter.email.subject"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textfield id="jpnewsletter_subject" name="newsletterConfig.subject"
                                        cssClass="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="jpnewsletter_textheader">
                        <s:text name="jpnewsletter.email.textheader"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textarea id="jpnewsletter_textheader" name="newsletterConfig.textHeader" cols="50"
                                       rows="3" cssClass="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="jpnewsletter_textfooter">
                        <s:text name="jpnewsletter.email.textfooter"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textarea id="jpnewsletter_textfooter" name="newsletterConfig.textFooter" cols="50"
                                       rows="3" cssClass="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="jpnewsletter_alsoHtml">
                        <s:text name="jpnewsletter.email.alsoHtml"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:checkbox id="jpnewsletter_alsoHtml" data-toggle="toggle" name="alsoHtml"
                                       cssClass="radiocheck bootstrap-switch"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="jpnewsletter_htmlheader">
                        <s:text name="jpnewsletter.email.htmlheader"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textarea id="jpnewsletter_htmlheader" name="newsletterConfig.htmlHeader" cols="50"
                                       rows="3" cssClass="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="jpnewsletter_htmlfooter">
                        <s:text name="jpnewsletter.email.htmlfooter"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textarea id="jpnewsletter_htmlfooter" name="newsletterConfig.htmlFooter" cols="50"
                                       rows="3" cssClass="form-control"/>
                    </div>
                </div>
            </div>
        </fieldset>

        <!-- Subscription -->
        <fieldset class="margin-large-top">

            <legend data-toggle="collapse" data-target="#subscription">
                <s:text name="jpnewsletter.title.signup"/>&#32;<span class="icon fa fa-chevron-down"></span>
            </legend>
            <div class="accordion_element collapse" id="subscription">
                <div class="alert alert-info">
                    <span class="pficon pficon-info"></span>
                    <s:text name="jpnewsletter.subscription.help" />
                </div>

                <!-- Users -->
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="no-mt no-mb">
                            <s:text name="jpnewsletter.subscription.users" />
                        </h3>
                    </div>
                    <div class="panel-body">
                        <s:set var="booleanProfileAttributesVar" value="booleanProfileAttributes" />
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter_subscription_attr">
                                <s:text name="jpnewsletter.mapping.subscription.preference" />
                                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                                   data-html="true" title="" data-placement="top"
                                   data-content="<s:text name="jpnewsletter.subscription.help1"/>"
                                   data-original-title="">
                                    <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                            <div class="col-sm-10">
                                <wpsf:select id="jpnewsletter_subscription_attr"
                                             name="newsletterConfig.allContentsAttributeName" list="#booleanProfileAttributesVar"
                                             listKey="name" listValue="name" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter_catlist">
                                <s:text name="jpnewsletter.config.categories" />
                                &#32;
                                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                                   data-html="true" title="" data-placement="top"
                                   data-content="Open this section to create tematic newsletter" data-original-title="">
                                    <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-xs-5">
                                        <wpsf:select id="jpnewsletter_catlist" name="categoryCode" list="categories"
                                                     listKey="code" listValue="getFullTitle(currentLang.code)"
                                                     cssClass="form-control" />
                                        <span for="jpnewsletter_hour" class="help help-block">
                                            <s:text name="jpnewsletter.category" />
                                        </span>
                                    </div>
                                    <div class="col-xs-5">
                                        <wpsf:select id="jpnewsletter_catattr" name="attributeName"
                                                     list="booleanProfileAttributes" listKey="name" listValue="name"
                                                     cssClass="form-control" />
                                        <span for="jpnewsletter_hour" class="help help-block">
                                            <s:text name="jpnewsletter.category.profileAttribute" />
                                        </span>
                                    </div>
                                    <div class="col-xs-2">
                                        <span class="text-left">
                                            <wpsf:submit type="button" action="addCategoryMapping"
                                                         cssClass="btn btn-primary">
                                                <s:text name="label.add"></s:text>
                                            </wpsf:submit>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <s:set var="categoryMappingKeys" value="newsletterConfig.subscriptions.keySet()" />
                        <s:if test="%{#categoryMappingKeys.size() > 0}">
                            <div class="row">
                                <div class="col-sm-10 col-sm-offset-2 table-responsive">
                                    <table class="table table-striped table-bordered table-hover no-mb">
                                        <thead>
                                            <tr>
                                                <th>
                                                    <s:text name="jpnewsletter.description" />
                                                </th>
                                                <th>
                                                    <s:text name="jpnewsletter.code" />
                                                </th>
                                                <th>
                                                    <s:text name="jpnewsletter.profileAttribute" />
                                                </th>
                                                <th class="text-center col-xs-1">
                                                    <s:text name="label.actions" />
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <s:iterator value="#categoryMappingKeys" var="categoryMappingKey">
                                                <tr>
                                                    <td>
                                                        <s:property
                                                            value="getTitle(#categoryMappingKey, getCategory(#categoryMappingKey).titles)" />
                                                    </td>
                                                    <td>
                                                        <s:property value="#categoryMappingKey" />
                                                    </td>
                                                    <td>
                                                        <s:property
                                                            value="%{newsletterConfig.subscriptions[#categoryMappingKey]}" />
                                                    </td>
                                                    <td class="text-center text-nowrap">
                                                        <div class="btn-group btn-group-xs">
                                                            <wpsa:actionParam action="removeCategoryMapping"
                                                                              var="actionName">
                                                                <wpsa:actionSubParam name="categoryCode"
                                                                                     value="%{#categoryMappingKey}" />
                                                            </wpsa:actionParam>
                                                            <wpsf:submit type="button" action="%{#actionName}"
                                                                         title="%{getText('label.remove')}"
                                                                         cssClass="btn btn-sm btn-link">
                                                                <span class="icon fa fa-trash-o fa-lg"></span>
                                                            </wpsf:submit>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </s:iterator>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </s:if>
                        <s:else>
                            <div class="row">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <div class="alert alert-info">
                                        <span class="pficon pficon-info"></span>
                                        <s:text name="jpnewsletter.category.empty" />
                                    </div>
                                </div>
                            </div>
                        </s:else>
                    </div>
                </div>

                <!-- Guests -->
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="no-mt no-mb">
                            <s:text name="jpnewsletter.subscription.guests" />
                        </h3>
                    </div>
                    <div class="panel-body">
                        <legend>
                            <s:text name="jpnewsletter.title.signup" />
                        </legend>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="newsletterConfig_subscriptionPageCode">
                                <s:text name="jpnewsletter.email.page" />
                            </label>
                            <div class="col-sm-10">
                                <wpsf:select list="confirmSubscriptionPages"
                                             name="newsletterConfig.subscriptionPageCode"
                                             id="newsletterConfig_subscriptionPageCode" listKey="code"
                                             listValue="getShortFullTitle(currentLang.code)" headerKey=""
                                             headerValue="%{getText('label.none')}" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter.subscriptors_confirm_tokenValidity">
                                <s:text name="jpnewsletter.email.tokenValidity" />
                            </label>
                            <div class="col-sm-10">
                                <wpsf:select id="jpnewsletter.subscriptors_confirm_tokenValidity"
                                             name="newsletterConfig.subscriptionTokenValidityDays"
                                             list="#{1:1,2:2,5:5,10:10,15:15,20:20}" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter.subscriptors_confirm_subject">
                                <s:text name="jpnewsletter.email.subject" />
                            </label>
                            <div class="col-sm-10">
                                <wpsf:textfield id="jpnewsletter.subscriptors_confirm_subject"
                                                name="newsletterConfig.subscriptionSubject" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter_subscription_confirm_body_text">
                                <s:text name="jpnewsletter.email.body.text" />
                                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                                   data-html="true" title="" data-placement="top"
                                   data-content="<s:text name="jpnewsletter.subscription.link.help"/>"
                                   data-original-title="">
                                    <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                            <div class="col-sm-10">
                                <wpsf:textarea id="jpnewsletter_subscription_confirm_body_text"
                                               name="newsletterConfig.subscriptionTextBody" cols="50" rows="4"
                                               cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter_subscription_confirm_body_html">
                                <s:text name="jpnewsletter.email.body.html" />
                                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                                   data-html="true" title="" data-placement="top"
                                   data-content="<s:text name="jpnewsletter.subscription.link.help"/>"
                                   data-original-title="">
                                    <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                            <div class="col-sm-10">
                                <wpsf:textarea id="jpnewsletter_subscription_confirm_body_html"
                                               name="newsletterConfig.subscriptionHtmlBody" cols="50" rows="4"
                                               cssClass="form-control" />
                            </div>
                        </div>
                        <legend>
                            <s:text name="jpnewsletter.title.cancellation" />
                        </legend>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="newsletterConfig_unsubscriptionPageCode">
                                <s:text name="jpnewsletter.email.page" />
                            </label>
                            <div class="col-sm-10">
                                <wpsf:select list="confirmUnsubscriptionPages"
                                             name="newsletterConfig.unsubscriptionPageCode"
                                             id="newsletterConfig_unsubscriptionPageCode" listKey="code"
                                             listValue="getShortFullTitle(currentLang.code)" headerKey=""
                                             headerValue="%{getText('label.none')}" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter_subscribers_label_footer_text">
                                <s:text name="jpnewsletter.email.textfooter" />
                                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                                   data-html="true" title="" data-placement="top"
                                   data-content="<s:text name="jpnewsletter.cancellation.link.help"/>"
                                   data-original-title="">
                                    <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                            <div class="col-sm-10">
                                <wpsf:textarea id="jpnewsletter_subscribers_label_footer_text"
                                               name="newsletterConfig.subscribersTextFooter" cols="50" rows="3"
                                               cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="jpnewsletter_subscribers_label_footer_html">
                                <s:text name="jpnewsletter.email.htmlfooter" />
                                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus"
                                   data-html="true" title="" data-placement="top"
                                   data-content="<s:text name="jpnewsletter.cancellation.link.help"/>"
                                   data-original-title="">
                                    <span class="fa fa-info-circle"></span>
                                </a>
                            </label>
                            <div class="col-sm-10">
                                <wpsf:textarea id="jpnewsletter_subscribers_label_footer_html"
                                               name="newsletterConfig.subscribersHtmlFooter" cols="50" rows="3"
                                               cssClass="form-control" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit action="save" type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save"/>
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
<br>
