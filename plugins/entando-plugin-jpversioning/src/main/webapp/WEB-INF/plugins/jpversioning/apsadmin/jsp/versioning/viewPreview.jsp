<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li>
        <a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
            <s:text name="jpversioning.admin.menu" /></a>
    </li>
    <li><s:text name="title.jpversioning.content" /></li>
    <li class="page-title-container"><s:text name="jpversioning.label.detail" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="page-title-container">
                <s:text name="jpversioning.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="TO be inserted" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>

<div id="main">
    <p class="sr-only"><s:text name="note.workingOn" />:&#32;<em class="important"><s:property value="content.descr"/></em> (<s:property value="content.typeDescr"/>)</p>
    <h3 class="sr-only"><s:text name="title.quickMenu" /></h3>
    <ul class="sr-only">
        <li><a href="#jpcontentinspection_metadata"><abbr title="<s:text name="metadata.full" />"><s:text name="metadata" /></abbr></a></li>
        <li><a href="#jpcontentinspection_referral_contents"><s:text name="title.referencingContents" /></a></li>
        <li><a href="#jpcontentinspection_referring_conts"><s:text name="title.referencedContents" /></a></li>
        <li><a href="#jpcontentinspection_pages"><s:text name="title.referencedPages" /></a></li>
        <li><a href="#jpcontentinspection_contents"><s:text name="title.content" /></a></li>
        <li><a href="#jpcontentinspection_referencing_pages"><s:text name="title.referencingPages" /></a></li>
    </ul>
    <%-- metadata --%>
    <p class="margin-base-vertical text-right">
        <button type="button" data-toggle="collapse" data-target="#jpcontentinspection_info" class="btn btn-link">
            <s:text name="label.info" />
            <span class="icon-chevron-down"></span>
        </button>
    </p>
    <div class="collapse" id="jpcontentinspection_info">
        <table class="table table-striped table-bordered table-hover no-mb">
            <s:set var="contentGroup" value="%{getGroup(content.getMainGroup())}" />
            <thead>
            <tr>
                <th><s:text name="label.description" /></th>
                <th class="text-center"><s:text name="jpversioning.label.version.date" /></th>
                <th class="text-center"><s:text name="jpversioning.label.author" /></th>
                <th class="text-center"><s:text name="jpversioning.label.version" /></th>
                <th class="text-center"><s:text name="jpversioning.label.status" /></th>
                <th class="text-center"><s:text name="label.ownerGroup" /></th>
                <th class="text-center"><s:text name="label.viewgroups" /></th>
                <th class="text-center"><s:text name="label.categories" /></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><s:property value="contentVersion.descr" /></td>
                <td class="text-center"><code><s:date name="contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></code></td>
                <td class="text-center"><code><s:property value="%{contentVersion.username}" /></code></td>
                <td class="text-center"><s:property value="contentVersion.version" /></td>
                <td class="text-center"><s:property value="%{getText(contentVersion.status)}" /></td>
                <td><s:property value="%{#contentGroup.descr}" /></td>
                <td class="text-center">
                    <s:if test="%{content.groups.size() <= 0}">
                    <span class="text-muted"><s:text name="noExtraGroup" /></span>
                    </s:if>
                    <s:if test="%{content.groups.size() > 0}">
                    <s:iterator value="content.groups" var="groupName">
                        <s:property value="%{getGroupsMap()[#groupName].getDescr()}"/>&#32;
                    </s:iterator>
                    </s:if>
                <td class="text-center">
                    <s:if test="%{content.categories.size() <=  0}">
                        <span class="text-muted"><s:text name="category.no.category" /></span>
                    </s:if>
                    <s:if test="%{content.categories.size() > 0}">
                        <s:iterator value="content.categories" var="contentCategory">
                            <s:property value="#contentCategory.defaultFullTitle"/>&#32;
                        </s:iterator>
                    </s:if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <br/>
    <s:include value="/WEB-INF/plugins/jpversioning/apsadmin/jsp/versioning/inc/contentView.jsp" />
</div>
