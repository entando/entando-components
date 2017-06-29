<%@ taglib prefix="s" uri="/struts-tags" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li class="page-title-container">
        <s:text name="jpuserreg.title.config" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpuserreg.title.config" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                data-content="<s:text name="jpuserreg.menu.userregAdmin.help"/>" data-placement="left"
                data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields" />
    </div>
</div>
<br>

<div id="main">
    <div class="alert alert-success">
        <span class="pficon pficon-ok"></span>
        <strong><s:text name="messages.confirm" /></strong>&nbsp;<s:text name="jpuserreg.note.configuration.saved" />.
    </div>
</div>
