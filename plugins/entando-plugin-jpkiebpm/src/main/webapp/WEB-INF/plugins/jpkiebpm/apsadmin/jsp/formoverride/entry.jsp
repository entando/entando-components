<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li>
        <a href="<s:url action="edit" namespace="/do/jpkiebpm/Config" />" >
            <s:text name="jpkiebpm.admin.menu.config" />
        </a>
    </li>
    <li class="page-title-container">
        <s:if test="%{strutsAction==1}">
            <s:text name="label.new" />
        </s:if>
        <s:else>
            <s:text name="label.edit" />
        </s:else>
    </li>
</ol>

<h1 class="page-title-container">
    <s:if test="%{strutsAction==1}">
        <s:text name="label.new" />
    </s:if>
    <s:else>
        <s:text name="label.edit" />
    </s:else>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.kiebpms.help" />" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields" /></div>
</div>
<br/>

<div class="mb-20">

    <s:form  cssClass="form-horizontal">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />

        <p class="noscreen">
            <wpsf:hidden name="strutsAction"/>
            <wpsf:hidden name="id"/>			
        </p>

        <s:set var="isProcessPathSetted" value="%{processPath != null && processPath != ''}" />
        <!--        <code>
                    @<s:property value="#isProcessPathSetted"/>@
                </code>-->

        <%-- formModel.containerId / processId
        --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['formModel.containerId']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="code">
                <s:text name="formModel.containerId" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-5">
                <s:if test="#isProcessPathSetted">
                    <s:select disabled="true" cssClass="" list="processList" id="processPath" name="processPath"  listKey="%{packageName +'.'+processName + '@' + containerId + '@' + kieSourceId}" listValue="%{processName + ' @ ' + containerId}"  ></s:select>
                    <wpsf:hidden name="processPath"/>			
                </s:if>
                <s:else>
                    <s:select list="processList" id="processPath" cssClass="" name="processPath"  listKey="%{packageName +'.'+processName + '@' + containerId + '@' + kieSourceId}" listValue="%{processName + ' @ ' + containerId}"  ></s:select>
                    <s:if test="#hasFieldErrorVar">
                        <span class="help-block text-danger">
                            <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                            </span>
                    </s:if>
                </s:else>
            </div>

            <s:if test="#isProcessPathSetted">
                <div class="col-sm-2">
                    <wpsf:submit action="changeForm" type="button" cssClass="btn btn-warning pull-right">
                        <s:text name="label.changeForm" />
                    </wpsf:submit>
                </div>
            </s:if>
            <s:else>
                <div class="col-sm-2 ">
                    <wpsf:submit action="chooseForm" type="button" cssClass="btn btn-success pull-right">
                        <s:text name="label.chooseForm" />
                    </wpsf:submit>
                </div>
            </s:else>
        </div>

        <s:if test="#isProcessPathSetted">
            <%-- formModel.field  --%>
            <s:set var="fieldErrorsVar" value="%{fieldErrors['formModel.field']}" />
            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

            <div class="form-group<s:property value="#controlGroupErrorClass" />">
                <label class="col-sm-2 control-label" for="code">
                    <s:text name="formModel.field" />
                    <i class="fa fa-asterisk required-icon"></i>
                </label>
                <s:set var="isFieldSetted" value="%{#isProcessPathSetted && (field != null && fiels != '')}" />
                <s:if test="!#isFieldSetted">
                    <div class="col-sm-5">
                        <s:select  list="fields" id="formModel_field" name="formModel.field"  listKey="%{name}" listValue="%{name}"  ></s:select>
                        <s:if test="#hasFieldErrorVar">
                            <span class="help-block text-danger">
                                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                                </span>
                        </s:if>
                    </div>	
                    <div class="col-sm-2">
                        <wpsf:submit action="chooseField" type="button" cssClass="btn btn-success pull-right">
                            <s:text name="label.chooseField" />
                        </wpsf:submit>
                    </div>
                </s:if>
                <s:else>
                    <div class="col-sm-5">
                        <s:select disabled="true"  list="fields" id="formModel_field" name="formModel.field" listKey="%{name}" listValue="%{name}" ></s:select>
                        <wpsf:hidden name="formModel.field"/>		
                        <wpsf:hidden name="field"/>		
                    </div>	
                    <div class="col-sm-2">
                        <wpsf:submit action="changeField" type="button" cssClass="btn btn-warning pull-right">
                            <s:text name="label.changeField" />
                        </wpsf:submit>
                    </div>
                </s:else>

                <s:if test="#isFieldSetted">
                    <br />
                    <br />
                    <br />
                    <br />
                    <div class="col-xs-12">
                        <legend>
                            <s:text name="label.override" />
                        </legend>
                        <div class="well">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="placeHolderOverride">
                                    <s:text name="placeHolderOverride" />
                                </label>
                                <s:textfield name="placeHolderOverride" />
                            </div>


                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="defaultValueOverride">
                                    <s:text name="defaultValueOverride" />
                                </label>
                                <s:textfield name="defaultValueOverride" />
                            </div>	            
                        </div>
                    </div>
                </s:if>
            </div>

            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-sm-12">
                        <wpsf:submit action="save" type="button" cssClass="btn btn-primary pull-right">
                            <s:text name="label.save" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>

        </s:if>
    </s:form>
</div>
