<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app" />
    </li>
    <li>
        <s:text name="breadcrumb.jacms" />
    </li>
    <s:if test="onEditContent">
        <li>
            <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
                <s:text name="breadcrumb.jacms.content.list"/>
            </a>
        </li>
        <li>
            <a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:text name="breadcrumb.jacms.content.edit"/>
            </a>
        </li>
    </s:if>
    <s:else>
        <li>
            <s:text name="breadcrumb.digitalAsset" />
        </li>
    </s:else>
    <li>
        <s:if test="onEditContent">
            <s:url var="archiveUrlVar" action="findResource">
                <s:param name="resourceTypeCode" value="resourceTypeCode" />
                <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
            </s:url>
        </s:if>
        <s:else>
            <s:url var="archiveUrlVar" action="list"><s:param name="resourceTypeCode" value="resourceTypeCode" /></s:url>
        </s:else>
        <a href="<s:property value="#archiveUrlVar" />">
            <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}" />
        </a>
    </li>
    <li class="page-title-container">
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.%{resourceTypeCode}.new" />
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.%{resourceTypeCode}.edit" />
        </s:elseif>
    </li>
</ol>
<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.%{resourceTypeCode}.new" />
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="title.%{resourceTypeCode}.edit" />
    </s:elseif>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
            data-original-title=""
            data-content="<s:property value="%{getText('help.' + resourceTypeCode + '.' + strutsAction + '.info')}" escapeXml="true" />"
            data-placement="left">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields" />
    </div>
</div>
<br />
<s:set var="categoryTreeStyleVar">
    <wp:info key="systemParam" paramName="treeStyle_category" />
</s:set>
<s:set var="lockGroupSelect" value="%{resourceId != null && resourceId != 0}"></s:set>
<s:form action="save" method="post" enctype="multipart/form-data" cssClass="form-horizontal">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/inc_fullErrors.jsp" />
    <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <wpsf:hidden name="resourceTypeCode" />
        <wpsf:hidden name="contentOnSessionMarker" />
        <s:if test="strutsAction != 1">
            <wpsf:hidden name="resourceId" />
        </s:if>
        <s:if test="%{lockGroupSelect}">
            <wpsf:hidden name="mainGroup" />
        </s:if>
    </p>
    <%-- descr --%>
    <s:set var="fieldErrorsVar" value="%{fieldErrors['descr']}" />
    <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <label class="col-sm-2 control-label" for="descr">
            <s:text name="label.description" />
            <i class="fa fa-asterisk required-icon"></i>
        </label>
        <div class="col-sm-10">
            <wpsf:textfield name="descr" id="descr" cssClass="form-control" />
            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldErrorsVar}">
                        <s:property escapeHtml="false" />
                        &#32;
                    </s:iterator>
                </span>
            </s:if>
        </div>
    </div>
    <%-- mainGroup --%>
    <s:set var="fieldErrorsVar" value="%{fieldErrors['mainGroup']}" />
    <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <s:set var="resourceCategory" value="%{getCategory(#categoryCode)}"></s:set>
        <label class="col-sm-2 control-label" for="mainGroup">
            <s:text name="label.group" />
            <i class="fa fa-asterisk required-icon"></i>
        </label>
        <div class="col-sm-10">
            <wpsf:select name="mainGroup" id="mainGroup" list="allowedGroups" listKey="name" listValue="description"
                disabled="%{lockGroupSelect}" cssClass="combobox form-control"></wpsf:select>
            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldErrorsVar}">
                        <s:property escapeHtml="false" />
                        &#32;
                    </s:iterator>
                </span>
            </s:if>
        </div>
    </div>
    <%-- upload --%>
    <s:set var="uploadFieldErrorsVar" value="%{fieldErrors['upload']}" />
    <s:set var="fileNameFieldErrorsVar" value="%{fieldErrors['fileName']}" />
    <s:set var="hasFieldErrorVar"
        value="(#uploadFieldErrorsVar != null && !#uploadFieldErrorsVar.isEmpty()) || (#fileNameFieldErrorsVar != null && !#fileNameFieldErrorsVar.isEmpty())" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <label class="col-sm-2 control-label" for="upload">
            <s:text name="label.file" />
            <s:if test="%{resourceTypeCode == 'Image'}">
                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                    data-placement="top" data-content="<s:text name="title.resourceManagement.help" />"
                    data-original-title="" style="position: absolute; right: 8px;">
                    <span class="fa fa-info-circle"></span>
                </a>
            </s:if>
            <s:elseif test="%{resourceTypeCode == 'Attach'}">
                <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                    data-placement="bottom" data-content="<s:text name="title.resourceAttach.help" />"
                    data-original-title="" style="position: absolute; right: 8px;">
                    <span class="fa fa-info-circle"></span>
                </a>
            </s:elseif>
        </label>
        <div class="col-sm-10">
            <s:file name="upload" id="upload" label="label.file" />
            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#uploadFieldErrorsVar}">
                        <s:property escapeHtml="false" />
                        &#32;
                    </s:iterator>
                    <s:iterator value="%{#fileNameFieldErrorsVar}">
                        <s:property escapeHtml="false" />
                        &#32;
                    </s:iterator>
                </span>
            </s:if>
        </div>
    </div>
    <fieldset class="margin-base-vertical" id="category-content-block">
        <div class="form-group<s:property value="controlGroupErrorClassVar" />">
            <div class="col-xs-2 control-label">
                <label>
                    <s:text name="title.categoriesManagement" />
                </label>
            </div>
            <div class="col-xs-10">
                <script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>
                <s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/category/categoryTree-extra.jsp"/>

                <s:set var="useAjax" value="true" />
                <s:set var="selectedTreeNode" value="selectedNode"/>
                <s:set var="currentRoot" value="categoryRoot"/>
                <s:set var="joinCategoryEndpoint" value="'joinCategory'"/>
                <s:set var="loadTreeActionName" value="''"/>
                <s:set var="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'"/>
                <s:set var="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'"/>
                <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/categoryTreeTable.jsp" />

                <s:if test="extraGroups.size() != 0">
                    <s:iterator value="extraGroups" var="groupName">
                        <wpsa:actionParam action="removeExtraGroup" var="actionName">
                            <wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
                        </wpsa:actionParam>
                        <div class="label label-default label-tag label-sm">
                            <s:property value="%{getSystemGroups()[#groupName].getDescr()}" />
                            &#32;
                            <wpsf:submit type="button" action="%{#actionName}" value="%{getText('label.remove')}"
                                title="%{getText('label.remove')}" cssClass="btn btn-tag">
                                <span class="icon fa fa-times"></span>
                                <span class="sr-only">x</span>
                            </wpsf:submit>
                        </div>
                    </s:iterator>
                </s:if>
                
                <span id="categoryList">

                    <p class="sr-only">
                        <s:iterator value="categoryCodes" var="categoryCodeVar" status="rowstatus">
                            <input type="hidden" name="categoryCodes" value="<s:property value="#categoryCodeVar" />"
                                   id="categoryCodes-<s:property value="#rowstatus.index" />"/>
                        </s:iterator>
                    </p>

                    <s:if test="%{categoryCodes != null && !categoryCodes.empty}">
                        <ul class="list-inline mt-20">
                            <s:iterator value="categoryCodes" var="categoryCodeVar">
                                <s:set var="resourceCategory" value="%{getCategory(#categoryCodeVar)}"></s:set>
                                    <li>
                                        <span class="label label-info">
                                            <span class="icon fa fa-tag"></span>
                                            &#32;
                                            <abbr title="<s:property value="#resourceCategory.getFullTitle(currentLang.code)"/>">
                                            <s:property value="#resourceCategory.getShortFullTitle(currentLang.code)"/>
                                        </abbr>
                                        &#32;
                                        <button type="button" class="btn btn-link"
                                                onclick="categoriesAjax.removeCategory('removeCategory', '<s:property value="#resourceCategory.code"/>')"
                                                title="<s:property value="%{getText('label.remove') + ' ' + #resourceCategory.defaultFullTitle}" />">
                                            <span class="pficon pficon-close white"></span>
                                            <span class="sr-only">x</span>
                                        </button>
                                    </span>
                                </li>
                            </s:iterator>
                        </ul>
                    </s:if>
                </span>

            </div>
        </div>
    </fieldset>
    <br>
    <div class="form-horizontal">
        <div class="form-group">
            <div class="col-sm-12 margin-small-vertical">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </div>
    
</s:form>

<%-- TODO: refactor resource detail
<s:form action="edit" method="post" cssClass="form-horizontal">
    <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <wpsf:hidden name="resourceTypeCode" />
        <wpsf:hidden name="contentOnSessionMarker" />
        <s:if test="strutsAction != 1">
            <wpsf:hidden name="resourceId" />
        </s:if>
        <s:if test="#categoryTreeStyleVar == 'request'">
            <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
            </s:iterator>
        </s:if>
        <s:if test="%{lockGroupSelect}">
            <wpsf:hidden name="mainGroup" />
        </s:if>
    </p>
    <s:if test="strutsAction == 2">
        <s:set var="referencingContentsId" value="references['jacmsContentManagerUtilizers']" />
        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/portal/include/referencingContents.jsp" />
    </s:if>
</s:form>
 --%>
