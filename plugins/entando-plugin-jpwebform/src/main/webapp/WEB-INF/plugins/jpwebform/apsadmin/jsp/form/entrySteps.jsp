<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="initViewEntityTypes" 
               namespace="/do/Entity">
               <s:param name="entityManagerName">
                   <s:property value="entityManagerName" />
               </s:param>
           </s:url>" 
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityManagement" />">
            <s:set var="entityPrototypeVar" value="%{getEntityPrototype(entityTypeCode)}" />
            <s:text name="title.entityAdmin.manage" />
        </a>&#32;/&#32;
        <s:text name="label.configure" />:&#32;
        <s:property value="#entityPrototypeVar.typeDescr" />&#32;&ndash;&#32;<s:text name="jpwebform.steps.configure.long" />
    </span>
</h1>
<div id="main">
    <s:form action="save">
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
        <s:set var="stepsConfigVar" value="stepsConfig" />
        <p class="noscreen">
            <wpsf:hidden name="entityTypeCode" />
        </p>
        <div class="form-group">
            <label for="jpwebform_newstepcode"><s:text name="jpwebform.label.newStepCode" /></label>
            <div class="input-group">
                <wpsf:textfield cssClass="form-control" id="jpwebform_newstepcode" name="newStepCode" />
                <wpsa:actionParam action="addStep" var="actionName"></wpsa:actionParam>
                    <span class="input-group-btn">
                    <wpsf:submit 
                        disabled="%{#operationButtonDisabled}"
                        type="button"
                        action="%{#actionName}" cssClass="btn btn-default">
                        <span class="icon fa fa-plus-square"></span>&#32;
                        <s:text name="jpwebform.label.addNewStep"/>
                    </wpsf:submit>
                </span>
            </div>
        </div>
        <s:iterator value="#stepsConfigVar.steps" var="stepVar" status="iteratorStatusStep">
            <fieldset class="col-xs-12">
				<s:text name="jpwebform.name.ognlCodes" />
				<wpsf:textfield name="%{'stepsConfig.steps[' +#iteratorStatusStep.index + '].ognlExpression'}" value="%{#stepVar.ognlExpression}" />
				<%--
				<wpsa:actionParam action="verifyOgnlExpression" var="actionName">
					<wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
				</wpsa:actionParam>
				<wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-default">
					<!-- <s:text name="%{getText('jpwebform.label.addAttribute')}"/> -->
					<s:text name="Verify"/> 
				</wpsf:submit>
				--%>
                <legend><s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/form/include_operationStep.jsp" />&#32;<s:property value="#stepVar.code" /></legend>
                <s:set var="entityAttributeNameFieldNameVar" value="%{'entityAttributeName_'+#stepVar.code}" />
                <div class="form-group">
                    <s:if test="#entityPrototypeVar.attributeList.isEmpty eq null || #entityPrototypeVar.attributeList.isEmpty">
                        <%--
                        <s:text name="jpwebform.steps.emptyAttributes" />.&#32;
                        <a href="<s:url action="initEditEntityType" namespace="/do/Entity" ><s:param name="entityManagerName" value="%{entityManagerName}"/><s:param name="entityTypeCode" value="entityTypeCode"/></s:url>">
                                <s:text name="label.add" />
                        </a>
                        --%>
                    </s:if>
                    <s:else>
                        <s:if test="%{#stepVar.attributeOrder.size() == #entityPrototypeVar.attributeList.size()}">
                            <%--
                            <s:text name="jpwebform.steps.usedAttributes" />.&#32;
                            <a href="<s:url action="initEditEntityType" namespace="/do/Entity" ><s:param name="entityManagerName" value="%{entityManagerName}"/><s:param name="entityTypeCode" value="entityTypeCode"/></s:url>" >
                                    <s:text name="label.add" />
                            </a>
                            --%>
                        </s:if>
                        <s:else>
                            <label for="<s:property value="#entityAttributeNameFieldNameVar" />">
                                <s:text name="jpwebform.label.attribute" />
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon">
                                    <wpsf:checkbox name="%{'onlyViewAttribute_' + #stepVar.code}" id="%{'view-only-'+#iteratorStatusStep.count}" />&#32;
                                    <%--<label for="view-only-<s:property value="#iteratorStatusStep.count" />"><s:text name="jpwebform.steps.attribute.viewOnly" /></label>--%>
                                    <s:text name="jpwebform.steps.attribute.viewOnly" />

                                </span>
                                <select name="<s:property value="#entityAttributeNameFieldNameVar" />" tabindex="<wpsa:counter />" id="<s:property value="#entityAttributeNameFieldNameVar" />" class="form-control">
                                    <s:iterator value="#entityPrototypeVar.attributeList" var="attributeVar">
                                        <s:if test="%{!(#stepVar.attributeOrder.contains(#attributeVar.name))}">
                                            <option value="<s:property value="#attributeVar.name" />">
                                                <s:property value="#attributeVar.name" /> - <s:property value="#attributeVar.type" />
                                            </option>
                                        </s:if>
                                    </s:iterator>
                                </select>
                                <div class="input-group-btn">
                                    <wpsa:actionParam action="addStepAttribute" var="actionName" >
                                        <wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
                                    </wpsa:actionParam>
                                    <wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-default">
                                        <span class="icon fa fa-plus"></span>&#32;
                                        <s:text name="jpwebform.label.addAttribute"/>
                                    </wpsf:submit>
                                </div>
                            </div>
                        </s:else>
                    </s:else>
                </div>
                <div class="form-group">
                <ul class="list-unstyled">
                    <s:iterator value="#stepVar.attributeOrder" var="attributeNameVar" status="iteratorStatusStepsAttribute">
                        <s:set var="attributeConfigVar" value="%{#stepVar.attributeConfigs[#attributeNameVar]}" />
                        <s:set var="attributeVar" value="%{#entityPrototypeVar.getAttribute(#attributeNameVar)}" />
                        <li class="margin-small-vertical">
                            <span class="important"><s:property value="#iteratorStatusStepsAttribute.index + 1" /></span>
                            <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/form/include_operationModule.jsp" />
                            <s:property value="#attributeConfigVar.name" />
                            (<s:property value="#attributeVar.type" />
                            <s:if test="#attributeConfigVar.view">,&#32;<s:text name="jpwebform.steps.attribute.viewOnly" /></s:if>
                            <s:if test="#attributeVar.required">,&#32;<s:text name="Entity.attribute.flag.mandatory.full" /></s:if>)	
                            </li>
                    </s:iterator>
                </ul>
                </div>
            </fieldset>
        </s:iterator>
        <fieldset class="col-xs-12">
            <legend><s:text name="jpwebform.label.confirmStep" /></legend>
            <div class="form-group">
                <div class="checkbox">
                    <wpsf:checkbox name="confirmGui" fieldValue="true" id="confirmGui" value="%{#stepsConfigVar.isConfirmGui()}"  />&#32;
                    <label for="confirmGui"><s:text name="label.active" /></label>
                </div>
            </div>
        </fieldset>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <s:submit action="save" type="button" cssClass="btn btn-primary btn-block">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="label.save" />
                    </s:submit>
                </div>
            </div>
        </div>                        
    </s:form>
</div>
