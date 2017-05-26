<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li>
        <a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
            <s:text name="jpversioning.admin.menu" /></a>
    </li>
    <li class="page-title-container"><s:text name="title.jpversioning.recover" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="page-title-container">
                <s:text name="jpversioning.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpversioning.admin.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>

<br/>

<div id="main">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th class="text-center"><s:text name="jpversioning.label.id" /></th>
            <th><s:text name="jpversioning.label.description" /></th>
            <th class="text-center"><s:text name="jpversioning.label.lastVersion" /></th>
            <th class="text-center"><s:text name="jpversioning.label.version" /></th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="text-center"><s:property value="contentVersion.contentId" /></td>
            <td><s:property value="contentVersion.descr" /></td>
            <td class="text-center"><s:date name="contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></td>
            <td class="text-center"><s:property value="contentVersion.version" /></td>
        </tr>
        </tbody>
    </table>

    <s:if test="%{trashedResources == null || trashedResources.size() == 0 }"></s:if>
    <s:else>
        <table class="generic">
            <caption><span><s:text name="title.versioning.resource.image.trashed" /></span></caption>
            <thead>
            <tr>
                <th class="text-center"><s:text name="versioning.resource.id"/></th>
                <th class="text-center"><s:text name="label.type"/></th>
                <th><s:text name="label.description"/></th>
                <th class="text-center"><s:text name="label.ownerGroup"/></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator var="id" value="trashedResources">
                <s:set var="resourceItem" value="getTrashedResource(#id)" />
                <tr>
                    <td class="text-center"><s:property value="#resourceItem.id" /></td>
                    <td><s:property value="#resourceItem.type" /></td>
                    <td class="text-center"><s:property value="#resourceItem.descr" /></td>
                    <td class="text-center"><s:property value="#resourceItem.mainGroup" /></td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </s:else>

    <s:if test="%{trashRemovedResources == null || trashRemovedResources.size() == 0 }"></s:if>
    <s:else>
        <table class="generic">
            <caption><span><s:text name="title.versioning.resource.attach.trashed" /></span></caption>
            <thead>
            <tr>
                <th class="text-center"><s:text name="versioning.resource.id"/></th>
                <th><s:text name="label.messages"/></th>
            </tr>
            </thead>
            <tbody>
            <s:iterator var="id" value="trashRemovedResources">
                <tr>
                    <td class="text-center"><s:property value="#id" /></td>
                    <td><s:text name="message.versioning.trashRemovedResource" /></td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </s:else>
    <div class="subsection-light">
        <s:form action="recover" >
            <p class="sr-only">
                <wpsf:hidden name="versionId" />
                <wpsf:hidden name="contentId" />
                <wpsf:hidden name="contentOnSessionMarker" />
            </p>
            <div class="alert alert-info">
                <s:text name="jpversioning.confirmRestore.info" />
            </div>
            <div class="text-center margin-large-top pull-right">
                <wpsf:submit type="button" cssClass="btn btn-primary" >
                    <s:text name="label.confirm" />
                </wpsf:submit>
            </div>
        </s:form>
    </div>
</div>
