<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner"/></li>
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.configPage" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.configPage" />
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


<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
            <s:param name="selectedNode" value="pageCode"></s:param>
        </s:action>

        <s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>

        <s:form action="saveConfig" namespace="/do/jpuserreg/Page/SpecialWidget/UserReg" cssClass="form-horizontal">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span>
                    </button>
                    <h4 class="margin-none"><s:text name="message.title.ActionErrors"/></h4>
                    <ul class="margin-base-vertical">
                        <s:iterator value="actionErrors">
                            <li><s:property escapeHtml="false"/></li>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>
            <s:if test="hasFieldErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span>
                    </button>
                    <h4 class="margin-none"><s:text name="message.title.FieldErrors"/></h4>
                    <ul class="margin-base-vertical">
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                                <li><s:property escapeHtml="false"/></li>
                            </s:iterator>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>

            <p class="noscreen">
                <wpsf:hidden name="pageCode"/>
                <wpsf:hidden name="frame"/>
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}"/>
            </p>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp"/>
                </div>
                <div class="panel-body">
                    <legend>
                        <span class="control-label label-group-name">
                            <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                            <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}"/>
                        </span>
                    </legend>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="jpuserreg_typecode">
                            <s:text name="jpuserreg.label.typeCode"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:select id="jpuserreg_typecode" name="typeCode" list="profileTypes" listKey="typeCode" listValue="typeDescr" cssClass="form-control"/>
                        </div>
                    </div>

                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-sm-12 margin-small-vertical">
                        <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                            <s:text name="%{getText('label.save')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </s:form>
    </div>
</div>