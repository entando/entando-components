<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:form action="save" >
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
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
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<p class="sr-only">
		<wpsf:hidden name="code" />
	</p>
	
	<s:set var="pageModelVar" value="%{getPageModel(code)}" />
	
	Model CODE <s:property value="code" />
	<br />
	Model DESCRIPTION <s:property value="#pageModelVar.description" />
	<br />
	
	<s:set var="modelConfigurationVar" value="#pageModelVar.configuration" />
	<s:set var="myPortalConfigurationVar" value="%{getConfiguration(code)}" />
	<s:if test="#modelConfigurationVar.length > 0">
		<div class="table-responsive">
			<table class="table table-bordered">
			<tr>
				<th><s:text name="label.frame" /></th>
				<th><s:text name="label.description" /></th>
				<th><s:text name="label.free" /></th>
				<th><s:text name="label.column" /></th>
			</tr>
			<s:iterator value="#modelConfigurationVar" var="frameVar">
			<s:set var="myPortalFrameConfigurationVar" value="#myPortalConfigurationVar[#frameVar.pos]" />
			<tr>
				<td>
					<s:property value="#frameVar.pos" />
				</td>
				<td>
					<s:property value="#frameVar.description" />
				</td>
				<td>
					<wpsf:checkbox name="%{'freePosition_' + #frameVar.pos}" value="%{!#myPortalFrameConfigurationVar.locked}" />
				</td>
				<td>
					<wpsf:select name="%{'columnPosition_' + #frameVar.pos}" headerKey="" headerValue="" 
								 list="#{'1':'1','2':'2','3':'3','4':'4','5':'5'}" value="%{#myPortalFrameConfigurationVar.column}" />
				</td>
			</tr>
			</s:iterator>
			</table>
		</div>
	</s:if>
	<s:else>
		<div class="alert alert-info">
			<p>
				<strong><s:text name="label.whoops" /></strong>&#32;<s:text name="note.page.pagemodelWithoutPositions" />
			</p>
		</div>
	</s:else>
	
    <div class="form-group">
      <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
        <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
          <span class="icon fa fa-floppy-o"></span>&#32;
          <s:text name="label.save" />
        </wpsf:submit>
      </div>
    </div>
</s:form>