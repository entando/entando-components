<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="jpcontentscheduler.integrations" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.components" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.admin.menu" />
    </li>
    <li class="page-title-container">
        <s:text name="title.contentList" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpcontentscheduler.admin.menu" />
                <span class="pull-right">
                    <s:text name="jpcontentscheduler.section.help" var="helpVar" />
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="${helpVar}" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.general" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.mail" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewUsers"/>">
                        <s:text name="jpcontentscheduler.admin.users" />
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="viewContentTypes"/>">
                        <s:text name="jpcontentscheduler.admin.contentTypes" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>
<div id="main" role="main">
    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>
    
    <s:form id="configurationForm" name="configurationForm" method="post" action="addContentType" cssClass="form-horizontal">
        <legend>
            <s:text name="jpcontentscheduler.label.addContentType" />
        </legend>
        
        <p class="sr-only">
            <wpsf:hidden name="idsCategories"/>
        </p>
        
        <!-- Content Type -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.contentType" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['contentTypeElem.contentType']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:select name="contentTypeElem.contentType" id="contentTypeElem_contentType"
                    list="%{getContentTypes()}" listKey="code" listValue="description" headerKey=""
                    headerValue="%{getText('note.choose')}" cssClass="form-control" />
            </div>
        </div>
        <!-- dateStart attribute -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.startDateAttribute" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['contentTypeElem.startDateAttr']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="contentTypeElem.startDateAttr" id="contentTypeElem_startAttr"
                    placeholder="%{getText('jpcontentscheduler.label.startDateAttribute')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
        <!-- dateEnd attribute -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.endDateAttribute" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['contentTypeElem.endDateAttro']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="contentTypeElem.endDateAttro" id="contentTypeElem_endAttro"
                    placeholder="%{getText('jpcontentscheduler.label.endDateAttribute')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
        <!-- idContentReplace -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.contentReplaceId" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['contentTypeElem.idContentReplace']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="contentTypeElem.idContentReplace" id="contentTypeElem_idContentReplace"
                    placeholder="%{getText('jpcontentscheduler.label.contentReplaceId')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
<!--         modelIdContentReplace -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.contentReplaceModelId" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['contentTypeElem.modelIdContentReplace']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="contentTypeElem.modelIdContentReplace" id="contentTypeElem_modelIdContentReplace"
                    placeholder="%{getText('jpcontentscheduler.label.contentReplaceModelId')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.suspend" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="contentTypeElem.suspend" id="contentTypeElem_suspend" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>
        
        <!-- Categories -->
        <s:set var="categoryTreeStyleVar">
            <wp:info key="systemParam" paramName="treeStyle_category" />
        </s:set>
        <fieldset class="margin-base-vertical" id="category-content-block">
        <div class="form-group<s:property value="controlGroupErrorClassVar" />">
            <div class="col-xs-2 control-label">
                <label>
                    <s:text name="title.categoriesManagement" />
                </label>
            </div>
            <div class="col-xs-10">
                <s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/category/categoryTree-extra.jsp" />
                <table id="categoryTree" class="table table-bordered table-hover table-treegrid ${categoryTreeStyleVar}">
                    <thead>
                        <tr>
                            <th>
                                <s:text name="label.category.tree" />
                                <s:if test="#categoryTreeStyleVar == 'classic'">
                                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                                        <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>
                                        &#32;
                                        <s:text name="label.category.expandAll" />
                                    </button>
                                    <button type="button" class="btn-no-button" id="collapseAll">
                                        <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>
                                        &#32;
                                        <s:text name="label.category.collapseAll" />
                                    </button>
                                </s:if>
                            </th>
                            <th class="text-center table-w-10">
                                <s:text name="label.category.join" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:set var="selectedTreeNode" value="selectedNode" />
                        <s:set var="inputFieldName" value="'categoryCode'" />
                        <s:set var="selectedTreeNode" value="categoryCode" />
                        <s:set var="liClassName" value="'category'" />
                        <s:set var="treeItemIconName" value="'fa-folder'" />
                        <s:if test="#categoryTreeStyleVar == 'classic'">
                            <s:set var="currentRoot" value="categoryRoot" />
                            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilderCategoriesJoin.jsp" />
                        </s:if>
                        <s:elseif test="#categoryTreeStyleVar == 'request'">
                            <s:set var="currentRoot" value="showableTree" />
                            <s:set var="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
                            <s:set var="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
                            <s:include
                                value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilder-request-categories.jsp" />
                        </s:elseif>
                    </tbody>
                </table>
                <s:if test="categoryCodes != null && categoryCodes.size() > 0">
                    <ul class="list-inline mt-20">
                        <s:iterator value="categoryCodes" var="categoryCode">
                            <s:set var="contentTypeCategory" value="%{getCategory(#categoryCode)}"></s:set>
                            <li>
                                <span class="label label-info">
                                    <span class="icon fa fa-tag"></span>
                                    &#32;
                                    <abbr title="<s:property value="%{getFullTitle(#contentTypeCategory, currentLang.code)}"/>">
                                        <s:property value="%{getShortFullTitle(#contentTypeCategory, currentLang.code)}" />
                                    </abbr>
                                    &#32;
                                    <wpsf:hidden name="categoryCodes" value="%{#contentTypeCategory.code}"/>
                                    <wpsa:actionParam action="removeCategory" var="actionName">
                                        <wpsa:actionSubParam name="categoryCode" value="%{#contentTypeCategory.code}" />
                                    </wpsa:actionParam>
                                    <wpsf:submit type="button" action="%{#actionName}"
                                        title="%{getText('label.remove') + ' ' + getDefaultFullTitle(#contentTypeCategory)}"
                                        cssClass="btn btn-link">
                                        <span class="pficon pficon-close white"></span>
                                        <span class="sr-only">x</span>
                                    </wpsf:submit>
                                </span>
                            </li>
                        </s:iterator>
                    </ul>
                </s:if>
            </div>
        </div>
    </fieldset>
        
        <div class="form-group">
            <div class="col-xs-12">
                <div class="pull-right">
                    <wpsf:submit name="save" type="button" cssClass="btn btn-primary">
                        <s:text name="%{getText('label.add')}" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>