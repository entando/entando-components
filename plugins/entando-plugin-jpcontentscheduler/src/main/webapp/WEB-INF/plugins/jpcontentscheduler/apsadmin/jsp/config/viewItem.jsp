<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpcontentscheduler.integrations"/></li>
    <li><s:text name="jpcontentscheduler.components"/></li>
    <li><s:text name="jpcontentscheduler.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="jpcontentscheduler.admin.general"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpcontentscheduler.admin.menu"/>
                <span class="pull-right">
                    <s:text name="jpcontentscheduler.section.help" var="helpVar"/>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="${helpVar}" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.general"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewEmail"/>">
                        <s:text name="jpcontentscheduler.admin.mail"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewUsers"/>">
                        <s:text name="jpcontentscheduler.admin.users"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewContentTypes"/>">
                        <s:text name="jpcontentscheduler.admin.contentTypes"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>


<div id="main" role="main">

<%-- <h2>[<s:property value="item" />]</h2> --%>

<s:form action="saveItem" class="form-horizontal">
	
	<s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul>
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                </s:iterator>
            </ul>
        </div>
	</s:if>
	
	<s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
            <ul class="margin-base-top">
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </s:iterator>
            </ul>
		</div>
	</s:if>
	
	<s:if test="hasActionMessages()">
        <div class="alert alert-success">
            <span class="pficon pficon-ok"></span>
            <h2 class="h4 margin-none">
                <s:text name="messages.confirm" />
            </h2>
            <ul class="margin-base-top">
                <s:iterator value="actionMessages">
                    <li>
                        <s:property escapeHtml="false" />
                    </li>
                </s:iterator>
            </ul>
        </div>
    </s:if>

    <p>
    	<s:hidden name="item" />
    </p>
    
    <%--
	<p>
		<s:text name="jpcontentscheduler.label.config" />:<br />
		<s:textarea name="config" cols="75" rows="25" />
	</p>
	<p>
		<s:submit value="%{getText('jpcontentscheduler.label.save')}" />
	</p>
     --%>
</s:form>

<%-- <h2><s:text name="jpcontentscheduler.saveItem.instructions" /></h2> --%>
<%--
<div class="panel panel-default">
	<div class="panel-body">
		<s:text name="label.jpcontentscheduler.intro" />
	</div>
</div>
 --%>
    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:form id="configurationForm" name="configurationForm" method="post" action="saveItem" cssClass="form-horizontal">
        <legend><s:text name="legend.contentThreadconfigSettings" /></legend>
        
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="label.active" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="active" id="active" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>

        <%-- siteCode --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['siteCode']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="siteCode">
                <s:text name="label.threadConfig.siteCode" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="siteCode" id="siteCode" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- globalCat --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['globalCat']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="globalCat">
                <s:text name="label.threadConfig.globalCat" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="globalCat" id="globalCat" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>


		<legend><s:text name="legend.content.replace" /></legend>
        <%-- contentIdRepl --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['contentIdRepl']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="contentIdRepl">
                <s:text name="label.threadConfig.contentIdRepl" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="contentIdRepl" id="contentIdRepl" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- contentModelRepl --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['contentModelRepl']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="contentModelRepl">
                <s:text name="label.threadConfig.contentModelRepl" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="contentModelRepl" id="contentModelRepl" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>


        <div class="form-group">
            <div class="col-xs-12">
                <div class="pull-right">
                    <wpsf:submit name="save" type="button" action="saveItem" cssClass="btn btn-primary" >
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>

</s:form>

