<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner"/></li>
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />">
            <s:text name="title.pageManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.configPage"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.configPage"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp"/>

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>

    <s:form namespace="/do/jacms/Page/SpecialWidget/ListViewer" cssClass="form-horizontal">
        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp"/>
            </div>

            <div class="panel-body">

                <s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
                <legend>
                    <label class="sr-only"><s:text name="name.widget"/></label>
                    <span class="control-label label-group-name">
						<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>
						<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}"/>
					</span>
                </legend>

                <p class="sr-only">
                    <wpsf:hidden name="pageCode"/>
                    <wpsf:hidden name="frame"/>
                    <wpsf:hidden name="widgetTypeCode"/>
                </p>

                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h3 class="h4 margin-none"><s:text name="message.title.FieldErrors"/></h3>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false"/></li>
                                </s:iterator>
                            </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <p class="sr-only">
                    <wpsf:hidden name="contentType"/>
                    <wpsf:hidden name="categories" value="%{#parameters['categories']}"/>
                    <wpsf:hidden name="orClauseCategoryFilter" value="%{#parameters['orClauseCategoryFilter']}"/>
                    <wpsf:hidden name="userFilters" value="%{#parameters['userFilters']}"/>
                    <wpsf:hidden name="filters"/>
                    <wpsf:hidden name="modelId"/>
                    <wpsf:hidden name="maxElemForItem"/>
                    <wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}"/>
                    <s:iterator var="lang" value="langs">
                        <wpsf:hidden name="%{'linkDescr_' + #lang.code}"
                                     value="%{#parameters['linkDescr_' + #lang.code]}"/>
                        <wpsf:hidden name="%{'title_' + #lang.code}" value="%{#parameters['title_' + #lang.code]}"/>
                    </s:iterator>
                    <wpsf:hidden name="userFilterKey" value="category"/>
                </p>

                <div class="form-group">
                    <label for="userFilterCategoryCode" class="col-xs-2 control-label">
                        <s:text name="label.userFilterCategory"/>
                    </label>
                    <div class="col-xs-10">
                        <wpsf:select name="userFilterCategoryCode" id="userFilterCategoryCode" list="categories"
                                     listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey=""
                                     headerValue="%{getText('label.all')}" cssClass="form-control"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit action="addUserFilter" type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save"/>
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
