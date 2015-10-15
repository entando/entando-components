<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.eMailManagement" />&#32;/&#32;<s:text name="label.smtpConfig" /></span></h1>

<div id="main">
    <div class="panel panel-default">
        <div class="panel-body">
            <s:text name="label.jpmail.intro" />
        </div>
    </div>
    <div id="messages">
        <s:include value="/WEB-INF/plugins/jpmail/apsadmin/jsp/mail/inc/smtp-messages.jsp" />
    </div>
    <s:form id="configurationForm" name="configurationForm" method="post" action="testSmtp">

        <fieldset class="col-xs-12">
            <legend><s:text name="legend.generalSettings" /></legend>
            <div class="form-group">
                <div class="checkbox">
                    <label for="active"><s:text name="label.active" />
                        <wpsf:checkbox name="active" id="active" />
                    </label>
                </div>    
            </div>
            <div class="form-group">
                <div class="checkbox">
                    <label for="debug"><s:text name="label.debug" />
                        <wpsf:checkbox name="debug" id="debug" />
                    </label>
                </div>    
            </div>
        </fieldset> 

        <fieldset class="col-xs-12">
            <legend><s:text name="legend.connection" /></legend>

            <div class="form-group">
                <label for="smtpHost"><s:text name="smtpHost" /></label>
                <wpsf:textfield name="smtpHost" id="smtpHost" cssClass="form-control" />
            </div>
            <div class="form-group">
                <label for="smtpPort"><s:text name="label.smtpPort" /></label>
                <wpsf:textfield name="smtpPort" id="smtpPort" cssClass="form-control" />
            </div>
            <div class="form-group">
                <label for="smtpPort"><s:text name="label.security" /></label>

                <div class="radio">
                    <s:text name="label.smtp.standard"/>
                    <wpsf:radio id="smtpstd" name="smtpProtocol" value="0" checked="%{smtpProtocol == 0}" cssClass="radio" />
                </div>
                <div class="radio">
                    <s:text name="label.smtp.ssl"/>
                    <wpsf:radio id="smtpssl" name="smtpProtocol" value="1" checked="%{smtpProtocol == 1}" cssClass="radio" />
                </div>    
                <div class="radio">
                    <s:text name="label.smtp.tls"/>
                    <wpsf:radio id="smtptls" name="smtpProtocol" value="2" checked="%{smtpProtocol == 2}" cssClass="radio" />    
                </div>
                <div class="form-group">
                    <label for="smtpTimeout"><s:text name="label.smtpTimeout" /></label>
                    <wpsf:textfield name="smtpTimeout" id="smtpTimeout" cssClass="form-control" />
                </div>
            </div>
        </fieldset> 
        <fieldset class="col-xs-12">
            <legend><s:text name="legend.authentication" /></legend> 
            <div class="form-group">
                <label for="smtpUserName"><s:text name="smtpUserName" /></label>
                <wpsf:textfield name="smtpUserName" id="smtpUserName" cssClass="form-control" />
            </div>
            <div class="form-group">
                <label for="smtpPassword"><s:text name="smtpPassword" /></label>
                <wpsf:hidden value="%{getSmtpPassword()}" />
                <wpsf:password  name="smtpPassword" id="smtpPassword" cssClass="form-control" />
            </div>
        </fieldset> 

        <div class="form-horizontal">            
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <wpsf:submit name="save" type="button" action="saveSmtp" cssClass="btn btn-primary btn-block" onclick="overrideSubmit('saveSmtp')">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>    
                </div>    
                <div class="btn-group margin-small-left margin-small-vertical">
                    <sj:submit parentTheme="simple" formIds="configurationForm" value="%{getText('label.testConnection')}" targets="messages" cssClass="btn btn-default"/>
                    <wpsf:submit name="testMail"  value="%{getText('label.sendEmail')}" action="testMail" cssClass="btn btn-default" onclick="overrideSubmit('testMail')"/>
                </div>    
            </div>
        </div>            

    </s:form>
</div>
