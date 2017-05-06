<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.uxcomponents"/></li>
    <li><s:text name="jprss.title.rssManagement" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="jprss.title.rssManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button"
           data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="TO be inserted" data-placement="left"
           data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
	    </a>
	</span>
</h1>

<!-- Default separator -->
<div class="text-right">
    <div class="form-group-separator"></div>
</div>

<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>

<s:set var="channelsVar" value="channels" />

<s:if test="%{#channelsVar.size > 0}" >
    <div class="mt-20">
        <table class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th><s:text name="title" /></th>
                <th><s:text name="description" /></th>
                <th style="width: 40px;"><s:text name="status" /></th>
                <th style="width: 40px;"><s:text name="active" /></th>
                <th style="width: 110px;"><s:text name="contentType" /></th>
                <th style="width: 80px;"><s:text name="feedType" /></th>
                <th class="text-center" style="width: 50px"><s:text name="label.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="#channelsVar" var="channelVar">
                <tr class="dl-horizontal dl-striped panel padding-base-top padding-base-bottom">
                    <td><s:property value="#channelVar.title" /></td>
                    <td><s:property value="#channelVar.description" /></td>
                    <td class="text-center">
                        <s:set var="rssMappingChannelVar" value="%{getContentMapping(#channelVar.contentType)}" ></s:set>
                        <s:if test="null == #rssMappingChannelVar">
                            <span class="icon fa fa-exclamation text-warning" title="<s:text name="label.status.invalid" />"> </span>
                        </s:if>
                        <s:else>
                            <span class="icon fa fa-check text-success" title="<s:text name="label.status.ok" />"> </span>
                        </s:else>
                    </td>
                    <td class="text-center">
                        <s:if test="#channelVar.active">
                            <span class="icon fa fa-check-square-o" title="<s:text name="label.yes" />"></span>
                        </s:if>
                        <s:else>
                            <span class="icon fa fa-square-o" title="<s:text name="label.no" />"></span>
                        </s:else>
                    </td>
                    <td>
                        <s:property value="%{getSmallContentType(#channelVar.contentType).descr}" />
                    </td>
                    <td><s:property value="%{getAvailableFeedTypes()[#channelVar.feedType]}" /></td>
                    <td class="table-view-pf-actions">
                        <div class="dropdown dropdown-kebab-pf">
                            <p class="sr-only"><s:text name="label.actions"/></p>
                            <span class="btn btn-menu-right dropdown-toggle" type="button"
                                  data-toggle="dropdown" aria-haspopup="true"
                                  aria-expanded="false">
                                    <span class="fa fa-ellipsis-v"></span>
                            </span>
                            <ul class="dropdown-menu dropdown-menu-right">
                                <li>
                                    <a href="<s:url action="edit"><s:param name="id" value="#channelVar.id"/></s:url>"
                                       title="<s:text name="label.edit" />: <s:property value="#channelVar.title" />">
                                        <s:text name="label.edit" />
                                    </a>
                                </li>
                                <li>
                                    <a href="<s:url action="trash"><s:param name="id" value="#channelVar.id"/></s:url>"
                                       title="<s:text name="label.remove" />: <s:property value="#channelVar.title" />">
                                        <s:text name="label.remove" />
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </div>
</s:if>
<s:else>
    <br/>
    <p><s:text name="note.channels.noChannels" /></p>
</s:else>

<a href="<s:url namespace="/do/jprss/Rss" action="new" />"
   class="btn btn-primary pull-right"
   style="margin-bottom: 5px">
    <s:text name="jprss.options.add" />
</a>

<s:set var="contentTypesVar" value="contentTypes" />
<s:if test="%{#contentTypesVar.size > 0}">
    <wp:ifauthorized permission="superuser" var="isSuperuser" />
    <s:set var="isSuperuserVar">${isSuperuser}</s:set>
    <div class="col-xs-12 no-padding">
        <div class="mt-20">
            <table class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th><s:text name="contentType" /></th>
                    <th><s:text name="label.status.info" /></th>
                    <th class="text-center" style="width: 50px"><s:text name="label.actions"/></th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="#contentTypesVar" var="contentTypeVar" status="counter">
                    <s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}" />
                    <tr class="dl-horizontal dl-striped panel padding-base-top padding-base-bottom">
                        <td class="col-md-5">
                            <s:property value="#contentTypeVar.typeDescr" />&#32;<code><s:property value="#contentTypeVar.typeCode" /></code>
                        </td>
                        <td class="col-md-6">
                            <s:set var="rssMappingVar" value="%{getContentMapping(#contentTypeVar.typeCode)}" />
                            <s:if test="null == #rssMappingVar">
                                <s:text name="contenttype.not.configurable" />
                            </s:if>
                            <s:else>
                                <s:text name="title" />:&#32;<code><s:property value="#rssMappingVar.titleAttributeName" /></code>
                                <s:if test="null != #rssMappingVar.descriptionAttributeName">,
                                    <s:text name="description" />:&#32;<code><s:property value="#rssMappingVar.descriptionAttributeName" /></code>
                                </s:if>
                            </s:else>
                        </td>
                        <td class="text-center text-nowrap">
                            <div class="btn-group btn-group-xs">
                                <s:if test="#isSuperuserVar == 'true'">
                                    <a class="btn btn-menu-right btn-sm margin-small-vertical"
                                       id="<s:property value="#entityAnchor" />"
                                       href="<s:url namespace="/do/Entity" action="initEditEntityType">
					                        <s:param name="entityManagerName">jacmsContentManager</s:param>
					                        <s:param name="entityTypeCode"><s:property value="#contentTypeVar.typeCode" /></s:param>
				                            </s:url>"
                                       title="<s:text name="label.edit" />: <s:property value="#contentTypeVar.typeDescr" />">
                                        <span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#contentTypeVar.typeDescr" /></span>
                                        <span class="icon fa fa-lg fa-pencil-square-o"></span>
                                    </a>
                                </s:if>
                                <s:else>&ndash;</s:else>
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
    <br/>
    <p><s:text name="no.contentypes" /></p>
</s:else>