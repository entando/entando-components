<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ page contentType="charset=UTF-8" %>
<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.jpmyportalplus" /></span></h1>
<div id="main">
<s:form action="save">

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

	<s:if test="hasActionMessages()">
	<div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
                    <ul class="margin-base-vertical">
                        <s:iterator value="actionMessages">
                            <li><s:property escape="false" /></li>
                        </s:iterator>
                    </ul>
	</div>
	</s:if>


	<s:set var="showletTypeCodes" value="showletTypeCodes" />
	<p class="noscreen">
	<s:iterator var="showletCode" value="showletTypeCodes" status="rowstatus">
		<wpsf:hidden id="showlets-%{#rowstatus.index}" name="showletTypeCodes" value="%{#showletCode}" />
	</s:iterator>
	</p>

	<fieldset class="col-xs-12">
		<legend><s:text name="jpmyportalplus.label.configWidget" /></legend>

		<div class="form-group">
                    <label for="showletCode"><s:text name="jpmyportalplus.label.addWidget" /></label>
                    <div class="input-group">
			<select name="showletCode" tabindex="<wpsa:counter />" id="showletCode" class="form-control">
			<s:iterator var="showletFlavour" value="showletFlavours">
				<wpsa:set var="tmpShowletType">tmpShowletTypeValue</wpsa:set>
				<s:iterator var="showletType" value="#showletFlavour" >
					<s:if test="#showletType.optgroup != #tmpShowletType">
						<s:if test="#showletType.optgroup == 'stockShowletCode'">
							<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.stock" /></wpsa:set>
						</s:if>
						<s:elseif test="#showletType.optgroup == 'customShowletCode'">
							<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.custom" /></wpsa:set>
						</s:elseif>
						<s:elseif test="#showletType.optgroup == 'userShowletCode'">
							<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.user" /></wpsa:set>
						</s:elseif>
						<s:else>
							<wpsa:set var="pluginPropertyName" value="%{getText(#showletType.optgroup + '.name')}" />
							<wpsa:set var="pluginPropertyCode" value="%{getText(#showletType.optgroup + '.code')}" />
							<wpsa:set var="optgroupLabel"><s:text name="#pluginPropertyName" /></wpsa:set>
						</s:else>
					<optgroup label="<s:property value="#optgroupLabel" />">
					</s:if>
						<s:if test="!#showletTypeCodes.contains(#showletType.key)">
							<option value="<s:property value="#showletType.key" />">
							<s:property value="#showletType.value" /></option>
						</s:if>
					<wpsa:set var="tmpShowletType"><s:property value="#showletType.optgroup" /></wpsa:set>
				</s:iterator>
					</optgroup>
			</s:iterator>
			</select>
                        <span class="input-group-btn">
                            <wpsf:submit action="addWidget" value="%{getText('label.add')}" cssClass="btn btn-warning" />
                        </span>
                    </div>
		</div>

		<s:if test="%{showletTypeCodes.size > 0}">
			<table class="table table-bordered">
				<tr>
                                    <th class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3" scope="col"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
                                    <th scope="col">Widget</th>
				</tr>

				<s:iterator var="showletCode" value="#showletTypeCodes" status="rowstatus">
                                    <tr>
                                        <td class="text-center text-nowrap">
                                            <wpsa:actionParam action="removeWidget" var="actionName" ><wpsa:actionSubParam name="showletCode" value="%{#showletCode}" /></wpsa:actionParam>
                                            <wpsf:submit type="button" src="%{#iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ': ' + getTitle(#showletCode, #showletType.getTitles())}" cssClass="btn btn-warning btn-xs">
                                                <span class="icon fa fa-times-circle-o"></span>
                                            </wpsf:submit>
                                        </td>
                                        <td>
                                            <wpsa:set var="showletType" value="%{getShowletType(#showletCode)}" />
                                            <s:property value="%{getTitle(#showletCode, #showletType.getTitles())}" />
					</td>
                                    </tr>
				</s:iterator>
			</table>
		</s:if>
	</fieldset>
    <div class="form-horizontal">
        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<wpsf:submit type="button" value="%{getText('label.save')}" cssClass="btn btn-primary btn-block">
                    <span class="icon fa fa-floppy-o"></span>&#32;
                    <s:text name="%{getText('label.save')}"/>
                </wpsf:submit>
            </div>
	</div>
    </div>
</s:form>
</div>
