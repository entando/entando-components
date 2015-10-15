<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.messageManagement" />&#32;/&#32;
        <s:text name="title.messageManagement.configuration" />&#32;/&#32;
        <s:text name="title.messagetype" />:&#32;<s:property value="%{messageType.descr}"/>

    </span>
</h1>
<div id="main">

    <s:set var="removeAddressImage" ><wp:resourceURL />administration/common/img/icons/list-remove.png</s:set>
    <s:form action="save">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="true" /></li>
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
                        <li><s:property escape="true" /></li>
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

            <%--
            <p><s:text name="webdynamicform.intro" /></p>
            --%>

            <%--<p><s:text name="webdynamicform.parameters.summary" /></p>--%>

 <%--       --- Da approvare ---

            <div class="panel panel-default">
                <div class="panel-heading"><s:text name="label.senderCode" /></div>
                <div class="panel-body"><s:text name="label.sender.help" /></div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading"><s:text name="label.mailAttrName" /></div>
                <div class="panel-body"><s:text name="label.attribute.email.help" /></div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading"><s:text name="label.subjectModel" /></div>
                <div class="panel-body"><s:text name="label.subjectModel.help"/></div>
            </div>    
            <div class="panel panel-default">
                <div class="panel-heading"><s:text name="label.bodyModel" /></div>
                <div class="panel-body"><s:text name="label.bodyModel.help" /></div>
            </div>   
 --%>                     
            <div class="form-group">
                <div class="checkbox">
                    <wpsf:checkbox id="jpwebdynamicform_store" name="store" cssClass="radiocheck" />&#32;
                    <label for="jpwebdynamicform_store"><s:text name="label.local.message.store" /></label>
                </div>
            </div>

            <div class="form-group">
                <%-- Il mittente è quello di sistema che figurerà nelle mail agli operatori o all'utente
                E' obbligatorio solo se mailAttrName o notifiable sono true --%>
                <label for="jpwebdynamicform_sendercode"><s:text name="label.senderCode" /></label>
                <wpsf:select id="jpwebdynamicform_sendercode" list="senders" name="senderCode" listKey="key" listValue="value" headerKey="" headerValue="%{getText('label.select')}" cssClass="form-control"/>
                <span class="help-block"><s:text name="label.sender.help" /></span>
            </div>

            <div class="form-group">
                <%-- L'attributo dell'entità che contiene l'indirizzo eMail dell'utente del portale.
                Serve se si vuole consentire l'invio di eMail di risposta in back-end --%>
                <label for="jpwebdynamicform_mailattribute">
                    <s:text name="label.mailAttrName" />
                </label>
                <wpsf:select id="jpwebdynamicform_mailattribute" list="textAttributes" name="mailAttrName" listKey="name" listValue="name" headerKey="" headerValue="%{getText('label.select')}" cssClass="form-control" />
                <span class="help-block"><s:text name="label.attribute.email.help" /></span>
            </div>

            <div class="form-group">
                <label for="jpwebdynamicform_subjectmodel">
                    <s:text name="label.subjectModel" />
                </label>
                <wpsf:textfield id="jpwebdynamicform_subjectmodel" name="subjectModel" cssClass="form-control" />
                <span class="help-block"><s:text name="label.subjectModel.help"/></span>
            </div>

            <div class="form-group">
                <label for="jpwebdynamicform_bodymodel">
                    <s:text name="label.bodyModel" />
                </label>
                <wpsf:textarea name="bodyModel" rows="6" cols="60" cssClass="form-control" />
                <span class="help-block"><s:text name="label.bodyModel.help" /></span>
            </div>
        </fieldset>

        <fieldset class="col-xs-12">
            <legend><s:text name="automatic.notification.receipt" /></legend>

            <div class="panel panel-default">
                <div class="panel-body">
                    <s:text name="automatic.notification.receipt.intro" />
                </div>
            </div>

            <%-- Indica se si vuole far notificare vial mail il messaggio agli operatori
            Se true, influenzerà la validazione (obbligatorietà) degli altri campi. --%>
            <div class="form-group">
                <div class="checkbox">
                    <wpsf:checkbox id="jpwebdynamicform_notifiable" name="notifiable" />&#32;
                    <label for="jpwebdynamicform_notifiable"><s:text name="label.automatic.notification.active" /></label>
                </div>
            </div>

<%--                
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="margin-none">
                        <s:text name="label.configured.recipients" />
                    </h3>
                </div>
--%>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="margin-none">
                        <s:text name="label.configured.recipients" />
                    </h3>
                </div>
                <div class="panel-body">

                    <div class="form-group">

                        <div class="margin-base-vertical">
                            <s:text name="label.recipientsTo" />
                        </div>
                        <s:if test="%{recipientsTo == null || recipientsTo.size() == 0}">
                            <div class="alert alert-info"><s:text name="label.no.configured.recipients" /></div>
                        </s:if>
                        <s:else>
                            <div class="clearfix">
                                <s:iterator value="recipientsTo" var="recipient" >
                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                        <abbr title="<s:property value="#recipient"/>">
                                            <s:property value="#recipient"/>
                                        </abbr>&#32;
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
                            <s:text name="label.recipientsCc" />
                        </div>
                        <s:if test="%{recipientsCc == null || recipientsCc.size() == 0}">
                            <div class="alert alert-info"><s:text name="label.no.configured.recipients" /></div>
                        </s:if>
                        <s:else>
                            <div class="clearfix">
                                <s:iterator value="recipientsCc" var="recipient" >
                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                        <abbr title="<s:property value="#recipient"/>">
                                            <s:property value="#recipient"/>
                                        </abbr>&#32;
                                        <wpsa:actionParam action="removeAddress" var="actionName" >
                                            <wpsa:actionSubParam name="recipientType" value="2" />
                                            <wpsa:actionSubParam name="address" value="%{#recipient}" />
                                        </wpsa:actionParam>
                                        <wpsf:submit type="button" action="%{#actionName}"mtitle="%{getText('label.remove')}: %{#recipient}" cssClass="btn btn-default btn-xs badge">
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
                            <s:text name="label.recipientsBcc" />
                        </div>
                        <s:if test="%{recipientsBcc == null || recipientsBcc.size() == 0}">
                            <div class="alert alert-info"><s:text name="label.no.configured.recipients" /></div>
                        </s:if>
                        <s:else>
                            <div class="clearfix">
                                <s:iterator value="recipientsBcc" var="recipient" >
                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                        <abbr title="<s:property value="#recipient"/>">
                                            <s:property value="#recipient"/>
                                        </abbr>&#32;
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
<%--                        
                        
                        <label for="jpwebdynamicform_addrectype"><s:text name="label.recipientType" /></label>
                    </div>
                    <div class="form-group">
                        <label for="jpwebdynamicform_addrecaddress"><s:text name="label.address" /></label>
                    </div>
                    <div class="form-group">
                    </div>                        
                        
                </div>
            </div>
--%>                        
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="margin-none">
                        <s:text name="addrecipent" />
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="jpwebdynamicform_addrectype"><s:text name="label.recipientType" /></label>
                        <wpsf:select list="#{1: getText('label.recipient.to'), 2: getText('label.recipient.cc'), 3: getText('label.recipient.bcc')}" name="recipientType" id="jpwebdynamicform_addrectype" cssClass="form-control" />
                    </div>
                    <div class="form-group">
                        <label for="jpwebdynamicform_addrecaddress"><s:text name="label.address" /></label>
                        <wpsf:textfield cssClass="form-control" id="jpwebdynamicform_addrecaddress" name="address" />
                    </div>
                    <div class="form-group">
                        <wpsf:submit type="button" cssClass="btn btn-default" action="addAddress" >
                            <span class="icon fa fa-plus-square"></span>&#32;
                            <s:text name="%{getText('label.addAddress')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>                    
        </fieldset>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="%{getText('label.save')}" />
                    </wpsf:submit>
                </div>
            </div>
        </div>	
    </s:form>
</div>