<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpcontentscheduler.integrations"/></li>
    <li><s:text name="jpcontentscheduler.components"/></li>
    <li><s:text name="jpcontentscheduler.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="title.contentList"/>
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
    <s:form id="configurationForm" name="configurationForm" method="post" action="#" cssClass="form-horizontal">
        <legend><s:text name="legend.mail" /></legend>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="label.alsoHtml" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="threadConfig.alsoHtml" id="threadConfig_alsoHtml" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>
        <%-- threadConfig.senderCode --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.senderCode']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_senderCode">
                <s:text name="label.threadConfig.senderCode" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.senderCode" id="threadConfig_senderCode" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.mailAttrName --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.senderCode']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_mailAttrName">
                <s:text name="label.threadConfig.mailAttrName" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.mailAttrName" id="threadConfig_mailAttrName" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        
        <%-- threadConfig.subject --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.subject']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_subject">
                <s:text name="label.threadConfig.subject" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.subject" id="threadConfig_subject" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- threadConfig.htmlHeader --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.htmlHeader']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_htmlHeader">
                <s:text name="label.threadConfig.htmlHeader" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="threadConfig.htmlHeader" id="threadConfig_htmlHeader" cssClass="form-control" cols="50" rows="4"/>
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.htmlFooter --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.htmlFooter']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_htmlFooter">
                <s:text name="label.threadConfig.htmlFooter" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="threadConfig.htmlFooter" id="threadConfig_htmlFooter" cssClass="form-control" cols="50" rows="4"/>
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.htmlSeparator --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.htmlSeparator']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_htmlSeparator">
                <s:text name="label.threadConfig.htmlSeparator" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.htmlSeparator" id="threadConfig_htmlSeparator" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
         <%-- threadConfig.htmlHeaderMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.htmlHeaderMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_htmlHeaderMove">
                <s:text name="label.threadConfig.htmlHeaderMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.htmlHeaderMove" id="threadConfig_htmlHeaderMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.htmlFooterMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.htmlFooterMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_htmlFooterMove">
                <s:text name="label.threadConfig.htmlFooterMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.htmlFooterMove" id="threadConfig_htmlFooterMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.htmlSeparatorMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.htmlSeparatorMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_htmlSeparatorMove">
                <s:text name="label.threadConfig.htmlSeparatorMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.htmlSeparatorMove" id="threadConfig_htmlSeparatorMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- threadConfig.textHeader --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.textHeader']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_textHeader">
                <s:text name="label.threadConfig.textHeader" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="threadConfig.textHeader" id="threadConfig_textHeader" cssClass="form-control" cols="50" rows="3"/>
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.textFooter --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.textFooter']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_textFooter">
                <s:text name="label.threadConfig.textFooter" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textarea name="threadConfig.textFooter" id="threadConfig_textFooter" cssClass="form-control" cols="50" rows="3"/>
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.textSeparator --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.textSeparator']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_textSeparator">
                <s:text name="label.threadConfig.textSeparator" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.textSeparator" id="threadConfig_textSeparator" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.textHeaderMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.textHeaderMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_textHeaderMove">
                <s:text name="label.threadConfig.textHeaderMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.textHeaderMove" id="threadConfig_textHeaderMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.textFooterMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.textFooterMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_textFooterMove">
                <s:text name="label.threadConfig.textFooterMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.textFooterMove" id="threadConfig_textFooterMove" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.textSeparatorMove --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.textSeparatorMove']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_textSeparatorMove">
                <s:text name="label.threadConfig.textSeparatorMove" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.textSeparatorMove" id="threadConfig_textSeparatorMove" cssClass="form-control" />
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
                    <wpsf:submit name="save" type="button" action="#" cssClass="btn btn-primary" >
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
        
    </s:form>
</div>