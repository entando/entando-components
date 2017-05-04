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

<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
        <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
        <ul class="margin-base-vertical">
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>

<s:set var="channelsVar" value="channels" />

<s:if test="%{#channelsVar.size > 0}" >
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover content-list no-mb">
            <tr>
                <th><s:text name="title" /></th>
                <th><s:text name="description" /></th>
                <th><s:text name="status" /></th>
                <th><s:text name="active" /></th>
                <th><s:text name="contentType" /></th>
                <th><s:text name="feedType" /></th>
                <th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
            </tr>
            <s:iterator value="#channelsVar" var="channelVar">
                <tr class="dl-horizontal dl-striped panel padding-base-top padding-base-bottom">
                    <td class="col-md-5"><s:property value="#channelVar.title" /></td>
                    <td class="col-md-6"><s:property value="#channelVar.description" /></td>
                    <td class="col-md-6">
                        <s:set var="rssMappingChannelVar" value="%{getContentMapping(#channelVar.contentType)}" ></s:set>
                        <s:if test="null == #rssMappingChannelVar">
                            <s:text name="label.status.invalid" />
                        </s:if>
                        <s:else>
                            <s:text name="label.status.ok" />
                        </s:else>
                    </td>
                    <td class="icon">
                        <s:if test="#channelVar.active"><s:text name="label.yes" /></s:if>
                        <s:else><s:text name="label.no" /></s:else>
                    </td>
                    <td class="col-md-6">
                        <s:property value="%{getSmallContentType(#channelVar.contentType).descr}" />
                    </td>
                    <td class="col-md-6"><s:property value="%{getAvailableFeedTypes()[#channelVar.feedType]}" /></td>
                    <td class="col-md-1 text-center">
                        <div class="dropdown dropdown-kebab-pf">
                            <p class="sr-only"><s:text name="label.actions"/></p>
                            <span class="btn btn-menu-right dropdown-toggle" type="button"
                                  data-toggle="dropdown" aria-haspopup="true"
                                  aria-expanded="false">
                                    <span class="fa fa-ellipsis-v"></span>
                            </span>
                            <ul class="dropdown-menu dropdown-menu-right">
                                <li>
                                        <%-- edit --%>
                                    <a href="<s:url action="edit"><s:param name="id" value="#channelVar.id"/></s:url>"
                                       title="<s:text name="label.edit" />: <s:property value="#channelVar.title" />"
                                       class="btn btn-default" >
                                        <span class="sr-only"><s:text name="label.edit" />: <s:property value="#channelVar.title" /></span>
                                        <span class="icon fa fa-pencil-square-o"></span>
                                    </a>
                                </li>
                                <li>
                                        <%-- remove --%>
                                    <a href="<s:url action="trash"><s:param name="id" value="#channelVar.id"/></s:url>"
                                       title="<s:text name="label.remove" />: <s:property value="#channelVar.title" />"
                                       class="btn btn-warning">
                                        <span class="icon fa fa-times-circle-o"></span>&#32;
                                        <span class="sr-only"><s:text name="label.alt.clear" /></span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
<s:else>
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
            <table class="table table-striped table-bordered table-hover content-list no-mb">
                <thead>
                <tr>
                    <th><s:text name="contentType" /></th>
                    <th><s:text name="label.status.info" /></th>
                    <th class="text-center" style="width: 20px"><s:text name="label.actions"/></th>
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
    <p><s:text name="no.contentypes" /></p>
</s:else>