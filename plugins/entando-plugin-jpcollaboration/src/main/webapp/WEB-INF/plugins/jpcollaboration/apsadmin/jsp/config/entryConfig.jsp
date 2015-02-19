<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.admin.title" />&#32;/&#32;<s:text name="jpcrowdsourcing.title.configure" /></span></h1>

<div id="main">

<s:form action="saveConfig">

	<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>	
                    <ul class="margin-base-vertical">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property escape="false" /></li>
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
			<s:iterator value="ActionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
                    </ul>
	</div>
	</s:if>
	<s:if test="hasActionMessages()">
	<div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>	
                    <ul class="margin-base-vertical">
			<s:iterator value="actionMessages">
				<li><s:property escape="false" /></li>
			</s:iterator>
                    </ul>
	</div>
	</s:if>

<fieldset class="col-xs-12">
<legend><s:text name="jpcrowdsourcing.title.moderation" /></legend>
<p><s:text name="jpcrowdsourcing.note.moderation" /></p>

<div class="form-group">
    <div class="checkbox">
        <label for="moderateEntries"><s:text name="jpcrowdsourcing.label.idea.moderation" />
            <wpsf:checkbox name="moderateEntries" id="moderateEntries" value="%{moderateEntries}" cssClass="radiocheck"/>
        </label>
    </div>

    <div class="checkbox">
        <label for="moderateComments"><s:text name="jpcrowdsourcing.label.comment.moderation" />
            <wpsf:checkbox name="moderateComments" id="moderateComments" value="%{moderateComments}" cssClass="radiocheck"/>
        </label>
    </div>
</div>    
</fieldset>
        
<div class="form-horizontal">            
    <div class="form-group">
        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
            <s:submit type="button" cssClass="btn btn-primary btn-block" >
                <span class="icon fa fa-floppy-o"></span>&#32;
                <s:text name="%{getText('label.save')}"/>
            </s:submit>   
        </div>
    </div>
</div>

</s:form>
</div>