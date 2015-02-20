<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity">
               <s:param name="entityManagerName">
                   <s:property value="entityManagerName" />
               </s:param>
           </s:url>" 
           title="<s:text name="note.goToSomewhere" />:&#32;<s:text name="title.entityManagement" />">
            <s:text name="jpwebform.title.formsManagement" />
        </a>&#32;/&#32;
        <s:set var="stepsConfigVar" value="stepsConfig" />
        <s:set var="entityPrototypeVar" value="%{getEntityPrototype(entityTypeCode)}" />
        <s:set var="stepCode" value="%{stepCode}" />
        <s:set var="stepVar" value="%{#stepsConfigVar.getStep(#stepCode)}" />
        <s:text name="label.configure" />:&#32;<s:property value=" #entityPrototypeVar.typeDescr" />&#32;&ndash;&#32;
        <s:text name="jpwebform.gui.configure.long" />&#32;/&#32;
        <s:property value="#stepVar.code" />
        <s:if test="%{#stepVar == null && #stepCode eq 'confirm'}">
            <s:text name="jpwebform.label.confirmStep" />
        </s:if>	
        <s:elseif test="%{#stepVar == null && #stepCode eq 'ending'}">
            <s:text name="jpwebform.endpoint.step" />
        </s:elseif>
    </span>
</h1>
<div id="main">
    <s:set var="isGuiBuilt" value="(#stepsConfigVar.builtConfirmGui && #stepCode eq 'confirm') ||  (#stepsConfigVar.builtEndPointGui && #stepCode eq 'ending') || #stepVar.builtGui" />

    <s:form action="saveGui" namespace="/do/jpwebform/Config/Gui/" method="post"> 
        <p class="noscreen">
            <wpsf:hidden name="entityTypeCode" />
            <wpsf:hidden name="stepCode" />
        </p>

        <s:if test="%{#isGuiBuilt}" >
            <%-- preview begin --%>
			<%--
            <s:set var="showingPageSelectItems" value="showingPageSelectItems"></s:set>
                <div class="centerText margin-more-top">
                <s:if test="!#showingPageSelectItems.isEmpty()">
                    <div class="form-group">
                        <label for="jpwebformStepPreviewActionPageCode"><s:text name="name.preview.page" /></label>
                        <div class="input-group">
                            <wpsf:select 
                                cssClass="form-control"
                                name="jpwebformStepPreviewActionPageCode" 
                                id="jpwebformStepPreviewActionPageCode" 
                                list="#showingPageSelectItems" 
                                listKey="key" 
                                listValue="value" />
                            <div class="input-group-btn">
                                <wpsf:submit 
                                    type="button"
                                    cssClass="btn btn-default" 
                                    action="preview">
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
                                <wpsf:submit 
                                    cssClass="btn btn-default btn-block" 
                                    disabled="true" 
                                    value="%{getText('label.preview')}" 
                                    title="%{getText('note.button.previewContent')}" />
                            </div>
                        </div>
                    </div>
                </s:else>
            </div>
			--%>
            <%-- preview end --%>

            <fieldset class="col-xs-12">
                <legend class="accordion_toggler"><s:text name="jpwebform.note.attributes.available" /></legend>			
                <div class="accordion_element">
                    <s:if test="!#stepVar.attributeOrder.isEmpty()">
                        <table class="table table-bordered">
                            <tr>
                                <th><s:text name="label.code" /></th>
                                <th><s:text name="label.type" /></th>
                                <th><s:text name="jpwebform.steps.attribute.viewOnly" /></th>
                                <th><abbr title="<s:text name="Entity.attribute.flag.mandatory.full" />"><s:text name="Entity.attribute.flag.mandatory.short" /></abbr></th>
                                <th><s:text name="name.ognlCodes" /></th>
                                <th><s:text name="label.settings" /></th>
                            </tr>
                            <s:iterator value="#stepVar.attributeOrder" var="attributeNameVar" status="status">
                                <s:set var="attributeConfigVar" value="%{#stepVar.attributeConfigs[#attributeNameVar]}" />
                                <s:set var="attributeVar" value="%{#entityPrototypeVar.getAttribute(#attributeNameVar)}" />
                                <tr>
                                    <td><span class="monospace"><code><s:property value="#attributeVar.name" /></code></span></td>
                                    <td><s:property value="#attributeVar.type" /></td>
                                    <td>
                                        <s:if test="#attributeConfigVar.view">
                                            <s:text name="label.no" />
                                        </s:if>
                                        <s:else>
                                            <span class="noscreen"><s:text name="label.yes" /></span>
                                        </s:else>
                                    </td>
                                    <td>
                                        <s:if test="#attributeVar.required">
                                            <s:text name="label.yes" />
                                        </s:if>
                                        <s:else>
                                            <span class="noscreen"><s:text name="label.no" /></span>
                                        </s:else>
                                    </td>
                                    <td>
                                        <s:set var="validationRules" value="#attributeVar.validationRules" />
                                        <s:if test="#validationRules != null && #validationRules.ognlValidationRule != null && #validationRules.ognlValidationRule.expression != null">
                                            <s:if test="#validationRules.ognlValidationRule.helpMessageKey != null">
                                                <s:set var="labelKey" value="#validationRules.ognlValidationRule.helpMessageKey" scope="page" />
                                                <s:set var="langCode" value="currentLang.code" scope="page" /><wp:i18n var="helpMessageFullVar" key="${attributeLabelKeyVar}" lang="${langCode}" />
                                            </s:if>
                                            <s:else><s:set var="helpMessageFullVar" value="#validationRules.ognlValidationRule.helpMessage" scope="page" /></s:else>
                                            <span title="OGNL: <s:property value="#validationRules.ognlValidationRule.expression" />"><s:property value="#attr.helpMessageFullVar" /></span>
                                        </s:if>
                                        <s:else>
                                            <span class="noscreen">&ndash;</span>
                                        </s:else>
                                    </td>
                                    <td>
                                        <s:if test="#attributeVar.textAttribute && (#attributeVar.minLength != -1 || #attributeVar.maxLength != -1)">
                                            <s:if test="#attributeVar.minLength != -1">&#32;<abbr title="<s:text name="Entity.attribute.flag.minLength.full" />" ><s:text name="Entity.attribute.flag.minLength.short" /></abbr>:<s:property value="#attributeVar.minLength" /></s:if>
                                            <s:if test="#attributeVar.maxLength != -1">&#32;<abbr title="<s:text name="Entity.attribute.flag.maxLength.full" />" ><s:text name="Entity.attribute.flag.maxLength.short" /></abbr>:<s:property value="#attributeVar.maxLength" /></s:if>
                                        </s:if>
                                        <s:elseif test="#attributeVar.type eq 'Number' && #attributeVar.validationRules !=null">
                                            <s:if test="#attributeVar.validationRules.rangeStart!=null"><s:text name="note.range.from" />:<s:property value="#attributeVar.validationRules.rangeStart" /></s:if>
                                            <s:if test="#attributeVar.validationRules.rangeEnd!=null">&#32;<s:text name="note.range.to" />:<s:property value="#attributeVar.validationRules.rangeEnd" /></s:if>
                                        </s:elseif>
                                        <s:else>
                                            <span class="noscreen">&ndash;</span>
                                        </s:else>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </s:if>
                    <s:else>
                        <div class="alert alert-info"><s:text name="jpwebform.note.no.attributes.within.step" /></div>
                    </s:else>
                </div>
            </fieldset>

            <fieldset class="col-xs-12">
                <legend><s:text name="jpwebform.gui.configure.long" /></legend>
                <div class="form-group">
                    <label class=" alignTop" for="gui-html-code">HTML</label>
                    <wpsf:textarea name="userGui" rows="15" cols="50" cssClass="form-control" id="gui-html-code" />
                </div>
                <div class="form-group">
                    <label class=" alignTop" for="gui-css-code">CSS</label>
                    <wpsf:textarea name="userCss" rows="15" cols="50" cssClass="form-control" id="gui-css-code" />
                </div>
            </fieldset>

            <wpsa:actionParam action="buildGui" var="actionName" >
                <wpsa:actionSubParam name="overwriteStepGui" value="false" />
            </wpsa:actionParam>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                        <wpsf:submit 
                            type="button"    
                            action="%{#actionName}" 
                            title="%{getText('jpwebform.label.resetGui')}" 
                            cssClass="btn btn-default">
                            <s:text name="%{getText('jpwebform.label.resetGui')}"/>
                        </wpsf:submit>
                        <wpsf:submit 
                            type="button"
                            cssClass="btn btn-primary" >
                            <span class="icon fa fa-floppy-o"></span>&#32;
                            <s:text name="%{getText('label.save')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </s:if>
        <s:else>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                        <wpsa:actionParam action="buildGui" var="actionName" >
                            <wpsa:actionSubParam name="overwriteStepGui" value="true" />
                        </wpsa:actionParam>
                        <wpsf:submit 
                            type="button"
                            action="%{#actionName}" 
                            title="%{getText('jpwebform.label.buildGui')}" 
                            cssClass="btn btn-primary btn-block">
                            <s:text name="%{getText('jpwebform.label.buildGui')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </s:else>
    </s:form>
    <div id="help" style="display: none;">
        The tag which always exist are:

        [[#title#]] displays the title of the form
        [[#form-start#]] starts the HTML form 
        [[#form-submit#]] displays the form buttons
        [[#form-end#]] ends the HTML form
        [[#inputmail#]] is valid for the Confirm step only, and will display the input field where users can insert their email.
        If the Manage User Profile plugin is installed the email field will be populated with the mail found in the profile, if present. Of course the user can change the value presented.

        Custom generated tags have this form:

        [[#fieldName=ATTRIBUTE_NAME;type=label#]] displays the automatically generated label for the attribute's label
        [[#fieldName=ATTRIBUTE_NAME;type=info#]] displays messages regarding the the attribute (if any)
        [[#fieldName=ATTRIBUTE_NAME;type=input#]] displays the input field of the attribute
    </div>
</div>