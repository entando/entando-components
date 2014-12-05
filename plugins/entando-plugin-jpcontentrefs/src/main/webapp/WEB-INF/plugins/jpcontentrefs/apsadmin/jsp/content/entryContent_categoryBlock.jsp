<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<fieldset>
	<legend><s:text name="title.categoriesManagement"/></legend>
	<div class="input-group margin-base-vertical">
			<label for="contentTypeCode" class="sr-only"><s:text name="label.type"/></label>
				<wpsf:select name="categoryCode" id="categoryCode" list="%{getCategories(content.typeCode)}" listKey="code" listValue="getFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.select')}"	value="%{getShowlet().getConfig().get('category')}" cssClass="form-control" />
				<span class="input-group-btn">
					<wpsf:submit action="joinPrivateCategory" type="button" cssClass="btn btn-default" >
						<span class="icon fa fa-plus"></span>
						<s:text name="label.join" />
					</wpsf:submit>
				</span>
	</div>
<s:set var="contentCategories" value="content.categories" />
<s:if test="#contentCategories != null && #contentCategories.size() > 0">
	<%-- Categories table --%>
		<table class="table table-bordered" summary="<s:text name="note.contentCategories.summary"/>: <s:property value="content.descr" />">
			<caption class="sr-only"><span><s:text name="title.contentCategories.list"/></span></caption>
			<thead class="sr-only">
				<tr>
					 <th class="text-center padding-large-left padding-large-right col-xs-1 col-sm-1 col-md-1 col-lg-1"><abbr title="<s:text name="label.remove" />">R</abbr></th>
					<th><s:text name="label.category"/></th>
				</tr>
			</thead>
			<s:iterator value="#contentCategories" id="contentCategory">
			<tr>
				<td class="text-center padding-large-left padding-large-right col-xs-1 col-sm-1 col-md-1 col-lg-1">
					<div class="btn-group btn-group-xs">
						<wpsa:actionParam action="removePrivateCategory" var="actionName" >
							<wpsa:actionSubParam name="categoryCode" value="%{#contentCategory.code}" />
						</wpsa:actionParam>
						<%--<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>--%>
						<wpsf:submit  type="button" cssClass="btn btn-warning"  action="%{#actionName}" title="%{getText('label.remove') + ' ' + #contentCategory.defaultFullTitle}" >
							<span class="icon fa fa-times-circle-o"></span>
							<span class="sr-only"><s:text name="label.remove" /></span>
						</wpsf:submit>
					</div>
				</td>
				<td><s:property value="#contentCategory.getFullTitle(currentLang.code)"/></td>
			</tr>
		<%-- Labels sostitution to table --%>
		<%--<wpsa:set name="currentCat" value="%{getFacet(#contentCategory)}" />
			<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
				<span class="icon fa fa-tag"></span>&#32;
				<abbr title="<s:property value="#currentCat.defaultFullTitle"/>">
					 <s:property value="#currentCat.defaultFullTitle"/>
				</abbr>&#32;
				<wpsa:actionParam action="removePrivateCategory" var="actionName" >
					<wpsa:actionSubParam name="facetCode" value="%{#contentCategory}" />
				</wpsa:actionParam>
				<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #contentCategory.defaultFullTitle}" cssClass="btn btn-default btn-xs badge">
					 <span class="icon fa fa-times"></span>
						<span class="sr-only">x</span>
				</wpsf:submit>
			</span>--%>
		 </s:iterator>
		</table>
</s:if>
</fieldset>

