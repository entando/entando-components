<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li class="page-title-container">
        <s:text name="jpuserreg.title.config"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpuserreg.title.config"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main">
    <s:form action="save" class="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors"/></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property/></li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors"/></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property/></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <legend>
            <span class="control-label label-group-name"><s:text name="label.info"/></span>
        </legend>
        <p>
            <s:iterator var="defaultAuthVar" value="config.defaultCsvAuthorizations">
                <wpsf:hidden name="config.defaultCsvAuthorizations" value="%{#defaultAuthVar}"/>
            </s:iterator>
        </p>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="tokenValidityMinutes">
                <s:text name="jpuserreg.tokenValidityMinutes"/>
            </label>
            <div class="col-sm-10">
                <wpsf:select id="tokenValidityMinutes" name="config.tokenValidityMinutes"
                             list="#{
                                1440: '1 '+getText('day'),
                                2880: '2 '+getText('days'),
                                4320: '3 '+getText('days'),
                                5760: '4 '+getText('days'),
                                7200: '5 '+getText('days'),
                                8640: '6 '+getText('days'),
                                10080: '7 '+getText('days'),
                                20160: '14 '+getText('days'),
                                43200: '30 '+getText('days')
                             }" cssClass="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="eMailSenderCode">
                <s:text name="jpuserreg.eMailSenderCode"/>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="config.eMailSenderCode" id="eMailSenderCode"
                             list="mailSenders" listKey="key"
                             listValue="value"
                             cssClass="form-control">

                </wpsf:select>
            </div>
        </div>

        <legend>
            <span class="control-label label-group-name"><s:text name="jpuserreg.defaultAuthorizations"/></span>
        </legend>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="userGroup">
                <s:text name="jpuserreg.label.userGroup"/>
            </label>
            <div class="col-sm-10">
                <wpsf:select id="userGroup" name="groupName" list="systemGroups" listKey="name" listValue="description"
                             cssClass="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="userRole">
                <s:text name="jpuserreg.label.userRole"/>
            </label>
            <div class="col-sm-10">
                <div class="input-group">
                    <wpsf:select id="userRole" name="roleName" list="systemRoles" headerKey="" headerValue=""
                                 listKey="name" listValue="description" cssClass="combobox form-control"/>
                    <span class="input-group-btn">
                        <wpsf:submit type="button" action="addAuthorization" cssClass="btn btn-default">
                            <span class="icon fa fa-plus"></span>
                            <%--<s:text name="jpuserreg.label.addAuthorization"></s:text>--%>
                        </wpsf:submit>
                    </span>
                </div>
                <br/>
                <p><s:text name="jpuserreg.note.authorizationsList" /></p>
                <s:set var="userCvsAuthorizationsVar" value="%{config.defaultCsvAuthorizations}" />

                <s:if test="%{#userCvsAuthorizationsVar.size()>0}">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" >
                            <thead>
                                <tr>
                                    <th><s:text name="jpuserreg.label.userGroup" /></th>
                                    <th><s:text name="jpuserreg.label.userRole" /></th>
                                    <th class="text-center text-nowrap col-sm-1"><s:text name="label.actions" /></th>
                                </tr>
                            </thead>
                            <tbody>
                            <s:iterator value="#userCvsAuthorizationsVar" var="userCvsAuthorizationVar" status="elementStatus">
                                <s:set var="userCvsAuthorizationParamsVar" value="%{#userCvsAuthorizationVar.split(',')}" />
                                <tr>
                                    <td>
                                        <s:set var="groupVar" value="%{getGroup(#userCvsAuthorizationParamsVar[0])}" />
                                        <s:if test="null != #groupVar">
                                            <s:property value="#groupVar.description" />&#32;<code><s:property value="#groupVar.name" /></code>
                                        </s:if>
                                        <s:else><code>&ndash;</code></s:else>
                                    </td>
                                    <td>
                                        <s:set var="roleVar" value="%{getRole(#userCvsAuthorizationParamsVar[1])}" />
                                        <s:if test="null != #roleVar">
                                            <s:property value="#roleVar.description" />&#32;<code><s:property value="#roleVar.name" /></code>
                                        </s:if>
                                        <s:else><code>&ndash;</code></s:else>
                                    </td>
                                    <td class="text-center text-nowrap">
                                        <s:set var="elementIndexVar" value="#elementStatus.index" />
                                        <div class="btn-group btn-group-xs">
                                            <wpsa:actionParam action="removeAuthorization" var="actionName" >
                                                <wpsa:actionSubParam name="csvAuthorization" value="%{#userCvsAuthorizationVar}" />
                                            </wpsa:actionParam>
                                            <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-menu-right">
                                                <span class="icon fa fa-trash-o fa-lg"></span>
                                            </wpsf:submit>
                                        </div>
                                    </td>
                                </tr>
                            </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </s:if>
                <s:else>
                    <p><s:text name="jpuserreg.note.userAuthorizations.empty" /></p>
                </s:else>

            </div>
        </div>
        <legend>
            <span class="control-label label-group-name"><s:text name="jpuserreg.activation"/></span>
        </legend>
        <p><s:property value="jpuserreg.parameters.summary" /></p>
        <div class="form-group">
            <div class="col-sm-12 table-responsive">
                <table class="table table-bordered table-hover content-list no-mb">
                    <tr>
                        <th class="text-right col-sm-2"><s:text name="jpuserreg.activationPageCode"/></th>
                        <td class="col-sm-10"><s:property value="%{getText('jpuserreg.activationPageCode.help')}"
                                                          escapeHtml="false"/></td>
                    </tr>
                    <tr>
                        <th class="text-right col-sm-2"><s:text name="jpuserreg.label.subject"/></th>
                        <td class="col-sm-10"><s:property value='%{getText("jpuserreg.label.activation.subject.help")}'
                                                          escapeHtml="false"/></td>
                    </tr>
                    <tr>
                        <th class="text-right col-sm-2"><s:text name="jpuserreg.label.body"/></th>
                        <td class="col-sm-10"><s:property value='%{getText("jpuserreg.label.activation.body.help")}'
                                                          escapeHtml="false"/></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="activationPageCode">
                <s:text name="jpuserreg.activationPageCode"/>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="config.activationPageCode" id="activationPageCode" list="%{activationPages}"
                             listKey="code" listValue="%{getShortFullTitle(currentLang.code)}" cssClass="form-control"/>
            </div>
        </div>

        <s:iterator var="langVar" value="langs">
            <s:set var="template" value="%{config.activationTemplates.get(#langVar.code)}"/>
            <div class="form-group">
                <label class="col-sm-2 control-label"
                       for="config_activationTemplates_subject_<s:property value="#langVar.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code"/></code>&#32;
                    <s:text name="jpuserreg.label.subject"/>
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield value="%{#template.subject}"
                                    name="config.activationTemplates['%{#langVar.code}'].subject"
                                    id="config_activationTemplates_subject_%{#langVar.code}" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"
                       for="config_activationTemplates_body_<s:property value="#langVar.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code"/></code>&#32;
                    <s:text name="jpuserreg.label.body"/>
                </label>
                <div class="col-sm-10">
                    <wpsf:textarea value="%{#template.body}" name="config.activationTemplates['%{#langVar.code}'].body"
                                   id="config_activationTemplates_body_%{#langVar.code}" cssClass="form-control"
                                   rows="8" cols="50"/>
                </div>
            </div>
        </s:iterator>




        <legend>
            <span class="control-label label-group-name"><s:text name="jpuserreg.reactivation" /></span>
        </legend>
        <p>
            <s:property value="jpuserreg.parameters.summary" />
        </p>
        <div class="form-group">
            <div class="col-sm-12 table-responsive">
                <table class="table table-bordered table-hover content-list no-mb">
                    <tr>
                        <th class="text-right col-sm-2"><s:text name="jpuserreg.reactivationPageCode"/></th>
                        <td class="col-sm-10"><s:property value="%{getText('jpuserreg.reactivationPageCode.help')}" escapeHtml="false" /></td>
                    </tr>
                    <tr>
                        <th class="text-right col-sm-2"><s:text name="jpuserreg.label.subject"/></th>
                        <td class="col-sm-10"><s:property value='%{getText("jpuserreg.label.reactivation.subject.help")}' escapeHtml="false" /></td>
                    </tr>
                    <tr>
                        <th class="text-right col-sm-2"><s:text name="jpuserreg.label.body"/></th>
                        <td><s:property value='%{getText("jpuserreg.label.reactivation.body.help")}' escapeHtml="false"  /></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="reactivationPageCode">
                <s:text name="jpuserreg.reactivationPageCode" />
            </label>
            <div class="col-sm-10">
                <wpsf:select name="config.reactivationPageCode" id="reactivationPageCode" list="%{reactivationPages}" listKey="code" listValue="%{getShortFullTitle(currentLang.code)}" cssClass="form-control" />
            </div>
        </div>

        <s:iterator var="lang" value="langs">
            <s:set var="template" value="%{config.reactivationTemplates.get(#lang.code)}" />
            <div class="form-group">
                <label class="col-sm-2 control-label"
                       for="config_reactivationTemplates_subject_<s:property value="#lang.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code"/></code>&#32;
                    <s:text name="jpuserreg.label.subject"/>
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield value="%{#template.subject}" name="config.reactivationTemplates['%{#lang.code}'].subject" id="config_reactivationTemplates_subject_%{#lang.code}" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"
                       for="config_activationTemplates_body_<s:property value="#langVar.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code"/></code>&#32;
                    <s:text name="jpuserreg.label.body"/>
                </label>
                <div class="col-sm-10">
                    <wpsf:textarea value="%{#template.body}" name="config.reactivationTemplates['%{#lang.code}'].body" id="config_reactivationTemplates_body_%{#lang.code}" cssClass="form-control" rows="8" cols="50" />
                </div>
            </div>
        </s:iterator>

		<div class="form-horizontal">
			<div class="form-group">
				<div class="col-sm-12 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
						<s:text name="%{getText('label.save')}"/>
					</wpsf:submit>
				</div>
			</div>
		</div>
	</s:form>
</div>