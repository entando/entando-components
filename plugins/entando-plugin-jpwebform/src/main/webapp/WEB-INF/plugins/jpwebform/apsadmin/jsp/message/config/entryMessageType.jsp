<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.emailNotifier.config" />&#32;/&#32;
        <s:text name="title.messagetype" />:&#32;<s:property value="%{messageType.descr}"/>
    </span>
</h1>
<div id="main">

    <s:form action="save">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="true" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="true" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <p class="noscreen">
            <wpsf:hidden name="typeCode" />
            <s:iterator value="recipientsTo" var="recipient" status="rowstatus">
                <wpsf:hidden name="recipientsTo" value="%{#recipient}" id="recipientsTo-%{#rowstatus.index}" />
            </s:iterator>
            <s:iterator value="recipientsCc" var="recipient" status="rowstatus">
                <wpsf:hidden name="recipientsCc" value="%{#recipient}" id="recipientsCc-%{#rowstatus.index}" />
            </s:iterator>
            <s:iterator value="recipientsBcc" var="recipient" status="rowstatus">
                <wpsf:hidden name="recipientsBcc" value="%{#recipient}" id="recipientsBcc-%{#rowstatus.index}" />
            </s:iterator>
        </p>

        <fieldset class="col-xs-12">
            <legend><s:text name="webdynamicform" /></legend>

            <div class="form-group">
                <div class="checkbox">
                    <wpsf:checkbox id="jpwebform_notifiable" cssClass="radiocheck" name="notifiable" />
                    <label for="jpwebform_notifiable"><s:text name="label.automatic.notification.active" /></label>
                </div>
                <div class="help-block">
                    <s:text name="automatic.notification.receipt.intro" />
                </div>
            </div>

            <%--
                                    <p>
                                            <wpsf:checkbox id="jpwebform_store" name="store" cssClass="radiocheck" />&#32;
                                            <label for="jpwebform_store"><s:text name="label.local.message.store" /></label>
                                    </p>
            --%>
            <div class="form-group">
                <label for="jpwebform_sendercode"><s:text name="label.senderCode" /></label>
                <wpsf:select id="jpwebform_sendercode" list="senders" name="senderCode" listKey="key" listValue="value" headerKey="" headerValue="%{getText('label.select')}" cssClass="form-control"/>
                <div class="help-block">
                    <s:text name="label.sender.help" />
                </div>
            </div>
            <%--
                                    <p class="margin-more-bottom">
                                            <s:text name="label.attribute.email.help" /><br /><br />
                                            <label for="jpwebform_mailattribute"><s:text name="label.mailAttrName" />:</label>
                                            <wpsf:select id="jpwebform_mailattribute" list="textAttributes" name="mailAttrName" listKey="name" listValue="name" headerKey="" headerValue="%{getText('label.select')}" />
                                    </p>
            --%>
        </fieldset>

        <fieldset class="col-xs-12">
            <legend><s:text name="label.configured.recipients" /></legend>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="margin-none">
                        <s:text name="label.configured.recipients" />
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <div class="margin-base-vertical">
                            <span class="important">
                                <s:text name="label.recipientsTo" />
                            </span>
                        </div>                        
                        <s:if test="%{recipientsTo == null || recipientsTo.size() == 0}">
                            <div class="alert alert-info"><s:text name="label.no.configured.recipients" /></div>
                        </s:if>
                        <s:else>
                            <div class="clearfix">
                                <s:iterator value="recipientsTo" var="recipient" >
                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                        <s:property value="#recipient"/>&#32; 
                                        <wpsa:actionParam action="removeAddress" var="actionName" >
                                            <wpsa:actionSubParam name="recipientType" value="1" />
                                            <wpsa:actionSubParam name="address" value="%{#recipient}" />
                                        </wpsa:actionParam>
                                        <wpsf:submit
                                            type="button"
                                            action="%{#actionName}"
                                            title="%{getText('label.remove')}: %{#recipient}"
                                            cssClass="btn btn-default btn-xs badge">
                                            <span class="icon fa fa-times"></span>
                                            <span class="sr-only">x</span>
                                        </wpsf:submit>
                                    </span>                                
                                </s:iterator>
                            </div>
                        </s:else>
                    </div>

                    <div class="form-group">
                        <div class="margin-base-vertical">
                            <span class="important">
                                <s:text name="label.recipientsCc" />
                            </span>
                        </div>
                        <s:if test="%{recipientsCc == null || recipientsCc.size() == 0}">
                            <div class="alert alert-info"><s:text name="label.no.configured.recipients" /></div>
                        </s:if>
                        <s:else>
                            <div class="clearfix">
                                <s:iterator value="recipientsCc" var="recipient" >
                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                        <s:property value="#recipient"/>&#32;  
                                    <wpsa:actionParam action="removeAddress" var="actionName" >
                                        <wpsa:actionSubParam name="recipientType" value="2" />
                                        <wpsa:actionSubParam name="address" value="%{#recipient}" />
                                    </wpsa:actionParam>
                                    <wpsf:submit
                                        type="button"
                                        action="%{#actionName}"
                                        title="%{getText('label.remove')}: %{#recipient}"
                                        cssClass="btn btn-default btn-xs badge">
                                        <span class="icon fa fa-times"></span>
                                        <span class="sr-only">x</span>
                                    </wpsf:submit>
                                    </span>
                                </s:iterator>
                            </div>
                        </s:else>
                    </div>

                    <div class="form-group">
                        <div class="margin-base-vertical">
                            <span class="important">
                                <s:text name="label.recipientsBcc" />
                            </span>
                        </div>
                        <s:if test="%{recipientsBcc == null || recipientsBcc.size() == 0}">
                            <div class="alert alert-info"><s:text name="label.no.configured.recipients" /></div>
                        </s:if>
                        <s:else>
                            <div class="clearfix">
                                <s:iterator value="recipientsBcc" var="recipient" >
                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                        <s:property value="#recipient"/>&#32;   
                                    <wpsa:actionParam action="removeAddress" var="actionName" >
                                        <wpsa:actionSubParam name="recipientType" value="3" />
                                        <wpsa:actionSubParam name="address" value="%{#recipient}" />
                                    </wpsa:actionParam>
                                    <wpsf:submit
                                        type="button"
                                        action="%{#actionName}"
                                        title="%{getText('label.remove')}: %{#recipient}"
                                        cssClass="btn btn-default btn-xs badge">
                                        <span class="icon fa fa-times"></span>
                                        <span class="sr-only">x</span>
                                    </wpsf:submit>                                    
                                    </span>                        
                                </s:iterator>
                            </div>
                        </s:else>
                    </div>
                </div>                            

            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="margin-none">
                        <s:text name="addrecipent" />
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="jpwebform_addrectype"><s:text name="label.recipientType" /></label>
                        <wpsf:select list="#{1: getText('label.recipient.to'), 2: getText('label.recipient.cc'), 3: getText('label.recipient.bcc')}" name="recipientType" id="jpwebform_addrectype" cssClass="form-control"/>
                    </div>
                    <div class="form-group">
                        <label for="jpwebform_addrecaddress"><s:text name="label.address" /></label>
                        <wpsf:textfield cssClass="form-control" id="jpwebform_addrecaddress" name="address" />
                    </div>
                    <div class="form-group">
                        <wpsf:submit type="button" cssClass="btn btn-default" action="addAddress">
                            <span class="icon fa fa-plus-square"></span>&#32;
                            <s:text name="%{getText('label.addAddress')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="col-xs-12">
            <legend>Template of the administration's email</legend>
            <div class="panel panel-default">
                <div class="panel-body">
                    Here are defined the subject and the body of automatic emails sent to the recipients every time a form is compiled.
                </div>
            </div>

            <div class="form-group">
                <label for="jpwebform_subjectmodel"><s:text name="label.subjectModel" /></label>
                <wpsf:textfield cssClass="form-control" id="jpwebform_subjectmodel" name="subjectModel" />
                <div class="help-block"><s:text name="label.subjectModel.help" /></div>
            </div>

            <div class="form-group">
                <label for="jpwebform_bodymodel"><s:text name="label.bodyModel" />:</label>
                <wpsf:textarea cssClass="form-control" name="bodyModel" rows="6" cols="60" />
                <div class="help-block"><s:text name="label.bodyModel.help" /></div>
            </div>
        </fieldset>
            
        <fieldset class="col-xs-12">
            <legend>Template of the user's email</legend>
            <div class="panel panel-default">
                <div class="panel-body">
                    Here are defined the subject and the body of the email sent to the portal users after  successfully compiling the form.
                </div>
            </div>            
            <div class="form-group">
                <label for="jpwebform_subjectmodel"><s:text name="label.subjectModel" />:</label>
                <wpsf:textfield cssClass="form-control" id="jpwebform_subjectmodelResp" name="subjectModelResp" />
                <div class="help-block">It's a short text used as the subject, usually It's something like: 'myportal.com - Form XYZ'</div>
            </div>

            <div class="form-group">
                <label for="jpwebform_bodymodelResp"><s:text name="label.bodyModel" /></label>
                <wpsf:textarea cssClass="form-control" id="jpwebform_bodymodelResp" name="bodyModelResp" rows="6" cols="60" />
                <div class="help-block">
                    The body of the email usually gives the user the summary of the data sent; It is a Velocity Template and you have two objects: $i18n (for i18n labels) and $message (the Message Object).<br /><br />
                </div>
            </div>
        </fieldset>
                
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <s:submit type="button" cssClass="btn btn-primary btn-block">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="%{getText('label.save')}" />
                    </s:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>