<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpcontentscheduler.integrations"/></li>
    <li><s:text name="jpcontentscheduler.components"/></li>
    <li><s:text name="jpcontentscheduler.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="jpcontentscheduler.admin.mail"/>
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
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.general"/>
                    </a>
                </li>
                <li class="active">
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

    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:form id="configurationForm" name="configurationForm" method="post" action="saveEmail" cssClass="form-horizontal">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <legend><s:text name="legend.mail" /></legend>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="label.threadConfig.alsoHtml" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="alsoHtml" id="alsoHtml" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>
        <%-- senderCode --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['senderCode']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="senderCode">
                <s:text name="label.threadConfig.senderCode" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="senderCode" id="senderCode" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- mailAttrName --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['senderCode']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="mailAttrName">
                <s:text name="label.threadConfig.mailAttrName" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="mailAttrName" id="mailAttrName" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- subject --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['subject']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="subject">
                <s:text name="label.threadConfig.subject" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="subject" id="subject" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- htmlHeader --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['htmlHeader']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="htmlHeader">
                <s:text name="label.threadConfig.htmlHeader" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="htmlHeader" id="htmlHeader" cssClass="form-control" cols="50" rows="4"/>
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- htmlFooter --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['htmlFooter']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="htmlFooter">
                <s:text name="label.threadConfig.htmlFooter" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="htmlFooter" id="htmlFooter" cssClass="form-control" cols="50" rows="4"/>
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- htmlSeparator --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['htmlSeparator']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="htmlSeparator">
                <s:text name="label.threadConfig.htmlSeparator" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="htmlSeparator" id="htmlSeparator" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- htmlHeaderMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['htmlHeaderMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="htmlHeaderMove">
                <s:text name="label.threadConfig.htmlHeaderMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="htmlHeaderMove" id="htmlHeaderMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- htmlFooterMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['htmlFooterMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="htmlFooterMove">
                <s:text name="label.threadConfig.htmlFooterMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="htmlFooterMove" id="htmlFooterMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- htmlSeparatorMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['htmlSeparatorMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="htmlSeparatorMove">
                <s:text name="label.threadConfig.htmlSeparatorMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="htmlSeparatorMove" id="htmlSeparatorMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- textHeader --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['textHeader']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="textHeader">
                <s:text name="label.threadConfig.textHeader" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="textHeader" id="textHeader" cssClass="form-control" cols="50" rows="3"/>
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- textFooter --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['textFooter']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="textFooter">
                <s:text name="label.threadConfig.textFooter" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="textFooter" id="textFooter" cssClass="form-control" cols="50" rows="3"/>
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- textSeparator --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['textSeparator']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="textSeparator">
                <s:text name="label.threadConfig.textSeparator" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="textSeparator" id="textSeparator" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- textHeaderMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['textHeaderMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="textHeaderMove">
                <s:text name="label.threadConfig.textHeaderMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="textHeaderMove" id="textHeaderMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- textFooterMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['textFooterMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="textFooterMove">
                <s:text name="label.threadConfig.textFooterMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="textFooterMove" id="textFooterMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- textSeparatorMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['textSeparatorMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="textSeparatorMove">
                <s:text name="label.threadConfig.textSeparatorMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="textSeparatorMove" id="textSeparatorMove" cssClass="form-control" />
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
                    <wpsf:submit name="save" type="button" action="saveEmail" cssClass="btn btn-primary" >
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>

    </s:form>
</div>