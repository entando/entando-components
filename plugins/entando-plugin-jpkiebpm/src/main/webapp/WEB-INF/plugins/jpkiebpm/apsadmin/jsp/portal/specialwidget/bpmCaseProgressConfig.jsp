<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />:
           <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.configPage"/>
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.configPage"/>
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div class="mb-20" >

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmCaseProgressViewer" class="form-horizontal">
        <p class="noscreen">
            <wpsf:hidden name="pageCode"/>
            <wpsf:hidden name="frame"/>
            <wpsf:hidden name="widgetTypeCode"/>
        </p>

        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp"/>
            </div>
            <div class="panel-body">
                <p class="h5 margin-small-vertical">
                    <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}"/>
                </p>
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <strong><s:text name="message.title.FieldErrors"/></strong>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property/></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <div class="container-fluid">
                    <div class="col-lg-12 col-md-12 col-xs-12">

                        <div class="form-group">
                            <label for="channelPath">Channel</label>
                            <div class="input-group">
                                <wpsf:select list="channels" id="channel" name="channel" cssClass="form-control"></wpsf:select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="progress-bar-type">
                                <s:text name="caseProgressWidget.progressBarType"/>
                            </label>
                            <div class="input-group">
                                <wpsf:select list="progressBarTypes" id="progress-bar-type" name="progressBarType" cssClass="form-control"></wpsf:select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="showMilestones"><s:text name="caseProgressWidget.showMilestones"/></label>
                            <div class="input-group">
                                <wpsf:checkbox id="show-milestones" name="showMilestones"></wpsf:checkbox>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="showNumberOfTasks"><s:text name="caseProgressWidget.showNumberOfTasks"/></label>
                            <div class="input-group">
                                <wpsf:checkbox id="show-number-of-tasks" name="showNumberOfTasks"></wpsf:checkbox>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right"
                                 action="save">
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>

        </div>
    </s:form>
</div>
