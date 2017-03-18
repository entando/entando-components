<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityManagement" />"><s:text name="title.entityAdmin.manage" />
                </a>&#32/&#32
        <s:set var="stepsConfigVar" value="stepsConfig" />
        <s:set var="entityTypeCode" value="entityTypeCode" />
        <s:set var="entityPrototypeVar" value="%{getEntityPrototype(#entityTypeCode)}" />
        <s:set var="firstStepCodeVar" value="%{getStepsConfig().getFirstStep().getCode()}" />
        <s:set var="isGuiBuiltVar" value="%{isBuiltEveryStepGui()}" />
        <s:text name="jpwebform.title.publish" />:&#32;<s:property value="%{#entityPrototypeVar.typeDescr}" />
    </span>    
</h1>
<div id="main">

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

    <s:form action="publishPreview" namespace="/do/jpwebform/Config/Gui" >
		<%--
        <s:if test="#isGuiBuiltVar" >
            <s:set var="showingPageSelectItems" value="showingPageSelectItems"></s:set>
			<s:if test="!#showingPageSelectItems.isEmpty()">
				<div class="form-group">
					<label for="jpwebformStepPreviewActionPageCode"><s:text name="name.preview.page" /></label>
					<div class="input-group">
					<wpsf:select name="jpwebformStepPreviewActionPageCode" id="%{'jpwebformStepPreviewActionPageCode'}" list="#showingPageSelectItems" listKey="key" listValue="value" cssClass="form-control"/>
					<div class="input-group-btn">
					<wpsf:submit type="button" cssClass="btn btn-default" title="%{getText('note.button.previewContent')}">
						<s:text name="%{getText('label.preview')}"/>
					</wpsf:submit>
					</div>    
					</div>
				</div>
			</s:if>
			<s:else>
				<div class="alert alert-info"><s:text name="label.preview.noPreviewPages" /></div>
				<div class="form-horizontal">
				<div class="form-group">
					<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
						<wpsf:submit type="button" cssClass="btn btn-default btn-block" disabled="true" action="%{#previewActionName}" title="%{getText('note.button.previewContent')}">
							<s:text name="%{getText('label.preview')}"/>
						</wpsf:submit>
					</div>
				</div>
				</div>
			</s:else>
        </s:if>
		--%>
        <p class="noscreen">	
            <wpsf:hidden name="entityTypeCode" />
            <wpsf:hidden name="stepCode" value="%{firstStepCodeVar}"/>
        </p>

        <fieldset class="col-xs-12">
            <legend><s:text name="jpwebform.label.steps.configuraton" /></legend>
            <s:iterator value="#stepsConfigVar.steps" var="stepVar" >
                <s:set var="stepCode" value="#stepVar.code" />
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span><s:property value="#stepVar.code" /> (<s:text name="jpwebform.label.steps.gui.ready" />:&#32;<s:property value="%{#stepVar.builtGui ? getText('label.yes') : getText('label.no')}" />)</span>
                        <%-- ORDER <s:property value="#stepVar.order" />  - TYPE <s:property value="#stepVar.type" /> --%> 
                    </div>
                    <div class="panel-body">
                        
                <table class="table table-bordered">
                    <tr>
                        <th><s:text name="jpwebform.label.attribute.name" /></th>
                        <th><s:text name="jpwebform.label.attribute.type" /></th>
                        <th class="icon"><s:text name="jpwebform.steps.attribute.viewOnly" /></th>
                    </tr>
                    <s:iterator var="attributeNameVar" value="#stepVar.attributeOrder" >
                        <s:set var="attributeConfigVar" value="%{#stepVar.attributeConfigs[#attributeNameVar]}" />
                        <s:set var="attributeVar" value="%{#entityPrototypeVar.getAttribute(#attributeNameVar)}" />
                        <tr>
                            <td><code><s:property value="#attributeConfigVar.name" /></code></td>
                            <td>
                                <s:if test="null == #attributeVar" ><s:text name="jpwebform.error.attrbute.not.found.in.step" /></s:if>
                                <s:else><s:property value="#attributeVar.type" /></s:else>
                                </td>
                                <td class="icon">
                                <s:if test="#attributeConfigVar.view">
                                    <s:text name="label.yes" />
                                </s:if>
                                <s:else>
                                    <s:text name="label.no" />
                                </s:else>
                            </td>
                        </tr>
                    </s:iterator>
                </table>
                    </div>
                </div>
            </s:iterator>
        </fieldset>

        <fieldset class="col-xs-12">
            <legend><s:text name="jpwebform.label.confirmStep" /></legend>
            <div class="form-group">
                <s:text name="jpwebform.label.enabled" />: 
                <span class="important">
                    <s:if test="#stepsConfigVar.confirmGui"><s:text name="label.yes" /></s:if>
                    <s:else><s:text name="label.no" /></s:else>
                    </span>
                </div>
                <div class="form-group">
                <s:text name="jpwebform.label.steps.gui.ready" />:
                <span class="important">
                    <s:if test="#stepsConfigVar.builtConfirmGui"><s:text name="label.yes" /></s:if>
                    <s:else><s:text name="label.no" /></s:else>
                    </span>
                </div>
            </fieldset>

            <fieldset class="col-xs-12">
                <legend><s:text name="jpwebform.endpoint.step" /></legend>
            <div class="form-group">
                <s:text name="jpwebform.label.steps.gui.ready" />:
                <span class="important">
                    <s:if test="#stepsConfigVar.builtEndPointGui"><s:text name="label.yes" /></s:if>
                    <s:else><s:text name="label.no" /></s:else>
                    </span>
                </div>
            </fieldset>
    </s:form>

    <s:if test="#isGuiBuiltVar">
        <s:form action="publish" namespace="/do/jpwebform/Publish" cssClass="form-horizontal">
            <wpsf:hidden name="entityTypeCode" />
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <s:submit type="button" action="publish" cssClass="btn btn-primary btn-block">
                        <s:text name="%{getText('jpwebform.label.publish')}" />
                    </s:submit>
                </div>
            </div>                        
        </s:form>
    </s:if>
</div>