<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.eMailManagement" />&#32;/&#32;
        <s:if test="%{strutsAction==1}" ><s:text name="title.eMailManagement.newSender" /></s:if>
        <s:else><s:text name="title.eMailManagement.editSender" />:&nbsp;<s:property value="code"/></s:else>
    </span>
</h1>
    
<div id="main">
    
	<s:form action="saveSender" >
		<s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<ul class="margin-base-vertical">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escapeHtml="false" /></li>
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
						<li><s:property escapeHtml="false" /></li>
					</s:iterator>
				</ul>
                    </div>
		</s:if>
		
		<p class="noscreen">	
			<wpsf:hidden name="strutsAction"/>
			<s:if test="%{strutsAction==2}" ><wpsf:hidden name="code"/></s:if>
		</p>
		
		<fieldset class="col-xs-12"> 
			<legend><s:text name="label.info" /></legend> 
			<div class="form-group">
				<label for="code"><s:text name="code" /></label>
				<wpsf:textfield name="code" id="code" disabled="%{strutsAction==2}" cssClass="form-control" />
			</div>
			<div class="form-group">
				<label for="mail"><s:text name="mail" /></label>
				<wpsf:textfield name="mail" id="mail" cssClass="form-control" />
			</div>
		</fieldset>
		
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" cssClass="btn btn-primary btn-block" >
                            <span class="icon fa fa-floppy-o"></span>&#32;
                            <s:text name="%{getText('label.save')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
	</s:form>	
</div>