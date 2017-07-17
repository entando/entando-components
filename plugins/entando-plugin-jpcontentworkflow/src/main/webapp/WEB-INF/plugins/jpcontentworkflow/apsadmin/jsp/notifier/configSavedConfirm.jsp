<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jpcontentworkflow/Workflow" />"><s:text name="jpcontentworkflow.menu.workflowAdmin"/></a>
    </li>
    <li class="page-title-container">
        <s:text name="jpcontentworkflow.menu.notifier"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpcontentworkflow.menu.workflowAdmin"/>
        <span class="pull-right"> 
            <a tabindex="0" role="button"data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="jpcontentworkflow.title.general.help" />" data-placement="left"
               data-original-title=""> 
                <i class="fa fa-question-circle-o"aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div class="alert alert-success">
    <span class="pficon pficon-ok"></span>
    <strong><s:text name="note.workflowNotifierManagement.savedConfirm" /></strong>
</div>
