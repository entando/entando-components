<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="jpcontentscheduler.title.management" /></h1>

<div id="main">

<h2>[<s:property value="item" />]</h2>

<s:form action="saveItem">
	
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionErrors">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
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
		<div class="alert alert-success alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

<p>
	<s:hidden name="item" />
</p>
	<p>
		<s:text name="jpcontentscheduler.label.config" />:<br />
		<s:textarea name="config" cols="75" rows="25" />
	</p>
	<p>
		<s:submit value="%{getText('jpcontentscheduler.label.save')}" />
	</p>
</s:form>

<h2><s:text name="jpcontentscheduler.saveItem.instructions" /></h2>


<hr />			<hr />					<hr />

<div class="panel panel-default">
	<div class="panel-body">
		<s:text name="label.jpcontentscheduler.intro" />
	</div>
</div>
    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:form id="configurationForm" name="configurationForm" method="post" action="#" cssClass="form-horizontal">
        <legend><s:text name="legend.contentThreadconfigSettings" /></legend>
        
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="label.active" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="threadConfig.active" id="threadConfig_active" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>


        <%-- threadConfig.siteCode --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.siteCode']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_siteCode">
                <s:text name="label.threadConfig.globalCat" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.siteCode" id="threadConfig_siteCode" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <%-- threadConfig.globalCat --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.globalCat']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_globalCat">
                <s:text name="label.threadConfig.globalCat" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.globalCat" id="threadConfig_globalCat" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>


		<legend><s:text name="legend.content.replace" /></legend>
        <%-- threadConfig.contentIdRepl --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.contentIdRepl']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_contentIdRepl">
                <s:text name="label.threadConfig.contentIdRepl" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.contentIdRepl" id="threadConfig_contentIdRepl" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <%-- threadConfig.contentModelRepl --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.contentModelRepl']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="threadConfig_contentModelRepl">
                <s:text name="label.threadConfig.contentModelRepl" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="threadConfig.contentModelRepl" id="threadConfig_contentModelRepl" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

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
                <wpsf:textfield name="threadConfig.htmlHeader" id="threadConfig_htmlHeader" cssClass="form-control" />
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
                <wpsf:textfield name="threadConfig.htmlFooter" id="threadConfig_htmlFooter" cssClass="form-control" />
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
                <wpsf:textfield name="threadConfig.textHeader" id="threadConfig_textHeader" cssClass="form-control" />
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
                <wpsf:textfield name="threadConfig.textFooter" id="threadConfig_textFooter" cssClass="form-control" />
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
        
        <s:include value="/WEB-INF/plugins/jpcontentscheduler/apsadmin/jsp/config/_users.jsp" />
        <s:include value="/WEB-INF/plugins/jpcontentscheduler/apsadmin/jsp/config/_groups.jsp" />
        <s:include value="/WEB-INF/plugins/jpcontentscheduler/apsadmin/jsp/config/_contentTypes.jsp" />

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


<s:property value="threadConfig"/>

</div>
<h1>AAAAAAAAAAAAAAAAA</h1>