<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
    <a href="<s:url action="list" />" 
       title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />">
        <s:text name="title.messageManagement" />
    </a>&#32;/&#32;
    <s:text name="title.messageManagement.newAnswer" />
    </span>
</h1>
        
<div id="main2">
	<s:set var="id" value="message.id" />
        <s:set var="typeCode" value="message.typeCode" />
        <div class="panel panel-default">
            <div class="panel-body">
                <s:text name="title.messageManagement.newAnswer.info" />:&#32;<em><s:property value="message.typeDescr"/></em>&#32;(<code><s:property value="%{#id}"/></code>)
            </div>
        </div>
	<s:form action="sendAnswer" enctype="multipart/form-data">
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
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
			
		<div>
			<s:include value="inc/include_messageDetails.jsp" />
		</div>
		
		<fieldset class="col-xs-12">
			<legend><s:text name="label.answer" /></legend>
			<p class="noscreen">
				<wpsf:hidden name="id" />
			</p>
			<div class="form-group">
				<label for="text"><s:text name="text" /></label>
				<wpsf:textarea name="text" id="text" cssClass="form-control" cols="60" rows="20" />
			</div>
			
			<div class="form-group">
				<label for="attachment"><s:text name="attachment" /></label>
				<input type="file" id="attachment" name="attachment" value="%{getText('label.browse')}" />
			</div>
		</fieldset>

      <div class="form-horizontal">
        <div class="form-group">
          <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
            <s:submit type="button" cssClass="btn btn-primary btn-block">
              <s:text name="%{getText('label.send')}" />
            </s:submit>
          </div>
        </div>
      </div>                                
	</s:form>
</div>