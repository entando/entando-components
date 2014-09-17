<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.contentfeedbackManagement" />&#32;/&#32;
        <s:text name="title.contentfeedbackSettings" />
    </span>
</h1>

<div id="main">
	<s:form action="update">
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

		<s:if test="hasActionMessages()">
                <div class="alert alert-info alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
				<ul class="margin-base-vertical">
					<s:iterator value="actionMessages">
						<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<fieldset class="col-xs-12">
			<legend><s:text name="jpcontentfeedback.comments" /></legend>
                        <div class="form-group">
                            <div class="checkbox">
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_comment"
					name="config.comment" 
					value="true" <s:if test="config.comment"> checked="checked"</s:if>  
				/>
				<label for="jpcontentfeedback_comment"><s:text name="jpcontentfeedback.label.commentsOnContent" /></label>
                            </div>
                            </div>
			<div class="form-group">
                            <div class="checkbox">
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_anonymousComment"
					name="config.anonymousComment" 
					value="true" <s:if test="config.anonymousComment"> checked="checked"</s:if>
				/>
				<label for="jpcontentfeedback_anonymousComment"><s:text name="jpcontentfeedback.label.anonymousComments"/></label>
				<span class="inline"><s:text name="jpcontentfeedback.note.anonymousComments" /></span>
                            </div>
			</div>
			<div class="form-group">
                            <div class="checkbox">
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_moderatedComment"
					name="config.moderatedComment" 
					value="true" <s:if test="config.moderatedComment"> checked="checked"</s:if>
				/>
				<label for="jpcontentfeedback_moderatedComment"><s:text name="jpcontentfeedback.label.commentsModeration"/></label>
				<span class="inline"><s:text name="jpcontentfeedback.note.commentsModeration" /></span>
                            </div>
                            </div>
			
			<div class="form-group">
                            <div class="checkbox">
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_rateComment"
					name="config.rateComment" 
					value="true" <s:if test="config.rateComment"> checked="checked"</s:if>
				/>
				<label for="jpcontentfeedback_rateComment"><s:text name="jpcontentfeedback.label.commentsRating" /></label>
                            </div>
                            </div>
		
		</fieldset>
		<fieldset class="col-xs-12">
			<legend><s:text name="jpcontentfeedback.contents" /></legend>
                        <div class="form-group">
                            <div class="checkbox">
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_rateContent"
					name="config.rateContent" 
					value="true" <s:if test="config.rateContent"> checked="checked"</s:if> 
				/>
				<label for="jpcontentfeedback_rateContent"><s:text name="jpcontentfeedback.label.contentsRating" /></label>
                            </div>
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