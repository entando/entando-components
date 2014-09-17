<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpcw" uri="/jpcontentworkflow-apsadmin-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%-- overriding:
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/entryContent-actions-jack.jsp" />
--%>
<wpcw:contentStep nextStep="false" var="previousStepVar" />
<wpcw:contentStep nextStep="true" var="nextStepVar" />
<s:set var="buttonsNumberVar" value="%{0}" />
<div class="row margin-none margin-large-top">
	<s:if test="%{#previousStepVar != null}">
		<div class="col-xs-12 col-sm-3 col-md-3 margin-small-vertical">
			<wpsf:submit
				type="button"
				cssClass="btn btn-default btn-block"
				action="previousStep"
				title="%{getText('note.button.previousStep')}">
					<span class="icon fa fa-long-arrow-left"></span>&#32;
					<s:property value="%{getText('label.previousStep')}" />
			</wpsf:submit>
		</div>
	</s:if>

	<div class="col-xs-12 col-sm-3 col-md-3 margin-small-vertical">
		<wpsf:submit
			action="save"
			type="button"
			cssClass="btn btn-default btn-block"
			title="%{getText('note.button.saveContent')}">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:property value="%{getText('label.save')}" />
		</wpsf:submit>
	</div>

	<s:if test="%{#nextStepVar != null}">
		<div class="input-group col-xs-12 col-sm-3 col-md-3 margin-small-vertical">
			<wpsf:submit
				action="nextStep"
				type="button"
				cssClass="btn btn-default btn-block"
				title="%{getText('note.button.nextStep')}">
					<span class="icon fa fa-long-arrow-right"></span>
					<s:property value="%{getText('label.nextStep')}" />
			</wpsf:submit>
		</div>
	</s:if>

	<wp:ifauthorized permission="validateContents">
		<%-- save and approve --%>
			<s:if test="%{#nextStepVar == null || #nextStepVar.equals('READY')}">
				<s:if test="content.onLine">
					<div class="clearfix"></div>
				</s:if>
				<div class="input-group col-sm-3 col-md-3 margin-small-vertical">
					<wpsf:submit
						action="saveAndApprove"
						cssClass="btn btn-success btn-block"
						type="button"
						title="%{getText('note.button.saveAndApprove')}">
							<span class="icon fa fa-check"></span>&#32;
							<s:property value="%{getText('label.saveAndApprove')}" />
					</wpsf:submit>
				</div>
			</s:if>
		<%-- suspend --%>
			<s:if test="content.onLine">
				<div class="input-group col-xs-12 col-sm-3 col-md-3 margin-small-vertical">
					<wpsf:submit
						action="suspend"
						cssClass="btn btn-warning btn-block"
						type="button"
						title="%{getText('note.button.suspend')}">
							<span class="icon fa fa-pause"></span>&#32;
							<s:property value="%{getText('label.suspend')}" />
					</wpsf:submit>
				</div>
			</s:if>
	</wp:ifauthorized>
</div>
