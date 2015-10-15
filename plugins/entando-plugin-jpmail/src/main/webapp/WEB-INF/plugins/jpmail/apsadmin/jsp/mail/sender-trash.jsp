<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.eMailManagement" />&#32;/&#32;<s:text name="title.eMailManagement.sendersConfig.trashSender" /></span></h1>
<div id="main">
    <s:form action="deleteSender" cssClass="form-horizontal">
        <p class="noscreen"><wpsf:hidden name="code"/></p>
        <div class="alert alert-warning">
            <s:text name="note.sendersConfig.trash" />&#32;<code><s:property value="code" /></code>?&#32;
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;
                    <s:text name="%{getText('label.remove')}"/>
                </wpsf:submit>
            </div>           
            <p class="text-center margin-small-top"><s:text name="note.sendersConfig.trash.goBack" />&nbsp;<a class="btn btn-link" href="<s:url action="viewSenders"/>"><s:text name="title.eMailManagement.sendersConfig" /></a></p>
        </div>
    </s:form>
</div> 