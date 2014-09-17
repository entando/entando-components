<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsfcs" uri="/jpfrontshortcut-apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-navigatorConfig"> 
	<h2 class="margin-more-top"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h2>
	<h3><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h3>
	<s:form cssClass="form-inline" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Navigator" action="saveNavigatorConfig" id="formform" theme="simple">
		<s:if test="hasActionErrors()">
			<div class="alert">
				<p><strong><s:text name="message.title.ActionErrors" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="alert">
				<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
						<li><s:property/></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<p class="noscreen">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
			<wpsf:hidden name="navSpec" />
		</p>
		<fieldset>
			<legend><s:text name="widget.configNavigator.expressionList" /></legend>
		<s:if test="expressions.size != 0">
			<table class="table" summary="<s:text name="note.page.navigator.summary" />">
				<%--
				<caption><span><s:text name="widget.configNavigator.expressionList" /></span></caption>
				--%>
				<tr>
					<th><abbr title="<s:text name="label.number" />">N</abbr></th>
					<th><s:text name="widget.configNavigator.navSpec" /></th> 
					<th><s:text name="widget.configNavigator.operator" /></th>
					<th class="icon" colspan="3"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
				</tr>
				<s:iterator value="expressions" var="expression" status="rowstatus">
					<tr>
						<td class="rightText"><s:property value="#rowstatus.index + 1"/></td>
						<td>
							<s:if test="#expression.specId == 1"><s:text name="widget.configNavigator.currentPage" /></s:if>
							<s:elseif test="#expression.specId == 2"><s:text name="widget.configNavigator.parentCurrent" /></s:elseif>
							<s:elseif test="#expression.specId == 3"><s:text name="widget.configNavigator.parentFromCurrent" />: <s:property value="specSuperLevel" /></s:elseif>
							<s:elseif test="#expression.specId == 4"><s:text name="widget.configNavigator.parentFromRoot" />: <s:property value="specAbsLevel" /></s:elseif>
							<s:elseif test="#expression.specId == 5"><s:text name="widget.configNavigator.specifiedPage" />: 
								<s:set var="specPageVar" value="%{getPage(specCode)}" ></s:set>
								<s:property value="#specPageVar.getFullTitle(currentLang.code)" /><s:if test="!#specPageVar.showable"> [i]</s:if>
								<s:if test="null == #specPageVar" ><s:text name="note.noPageSet" /></s:if>
							</s:elseif>
							<s:else><span class="label label-warning">error</span></s:else>
						</td>
						<td>
							<s:if test="#expression.operatorId == -1"><s:text name="widget.configNavigator.none" /></s:if>
							<s:elseif test="#expression.operatorId == 1"><s:text name="widget.configNavigator.allChildren" /></s:elseif>
							<s:elseif test="#expression.operatorId == 2"><s:text name="widget.configNavigator.allNodes" /></s:elseif>
							<s:elseif test="#expression.operatorId == 3"><abbr title="<s:text name="widget.configNavigator.levelOfNodesTothisLevel" />"><s:text name="widget.configNavigator.nodesTothisLevel" /></abbr>: <s:property value="operatorSubtreeLevel" /></s:elseif>
							<s:else><span class="label label-warning">error</span></s:else>
						</td>
						<td class="icon">
							<wpsfcs:actionParam var="moveExpressionUpActionVar" action="moveExpression" >
								<wpsfcs:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
								<wpsfcs:actionSubParam name="movement" value="UP" />
							</wpsfcs:actionParam>
							<s:url var="moveExpressionUpUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Navigator" action="%{moveExpressionUpActionVar}" />
							<s:set name="iconImagePath" var="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up.png</s:set>
							<sj:submit cssClass="btn btn-small" targets="form-container" href="%{moveExpressionUpUrlVar}" indicator="indicator" type="image" src="%{#iconImagePath}" value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')}" button="false" />
						</td>
						<td class="icon">
							<wpsfcs:actionParam var="moveExpressionDownActionVar" action="moveExpression" >
								<wpsfcs:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
								<wpsfcs:actionSubParam name="movement" value="DOWN" />
							</wpsfcs:actionParam>
							<s:url var="moveExpressionDownUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Navigator" action="%{moveExpressionDownActionVar}" />
							<s:set name="iconImagePath" var="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down.png</s:set>
							<sj:submit cssClass="btn btn-small" targets="form-container" href="%{moveExpressionDownUrlVar}" indicator="indicator" type="image" src="%{#iconImagePath}" value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')}" button="false" />
						</td>
						<td class="icon">
							<wpsfcs:actionParam var="removeExpressionActionVar" action="removeExpression" >
								<wpsfcs:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
							</wpsfcs:actionParam>
							<s:url var="removeExpressionUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Navigator" action="%{removeExpressionActionVar}" />
							<s:set name="iconImagePath" var="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
							<sj:submit cssClass="btn btn-small" targets="form-container" href="%{removeExpressionUrlVar}" indicator="indicator" type="image" src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" button="false" />
						</td>
					</tr>
				</s:iterator>
			</table>
		</s:if>
		<s:else>
			<p><s:text name="note.noRuleSet" /></p>
		</s:else>
		<hr />
		<h4 class="margin-more-bottom"></h4>
		<fieldset>
			<legend><s:text name="widget.configNavigator.navSpec" /></legend>
			<ul class="unstyled">
				<li>
					<label class="checkbox" for="specId1"><input type="radio" name="specId" id="specId1" value="1" /><s:text name="widget.configNavigator.currentPage" /></label></li>
				<li>
					<label class="checkbox" for="specId2">
						<input type="radio" name="specId" id="specId2" value="2" /> 
						<s:text name="widget.configNavigator.parentCurrent" />
					</label>
				</li>
				<li>
					<label class="checkbox" for="specId3">
						<input type="radio" name="specId" id="specId3" value="3" />
						<s:text name="widget.configNavigator.parentFromCurrent" />
					</label>
					<wpsf:select useTabindexAutoIncrement="true" name="specSuperLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" /></li>
				<li>
					<label class="checkbox" for="specId4">
						<input type="radio" name="specId" id="specId4" value="4" />
						<s:text name="widget.configNavigator.parentFromRoot" />
					</label>
					<wpsf:select useTabindexAutoIncrement="true" name="specAbsLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" /></li>
				<li>
					<label class="checkbox inline" for="specId5">
						<input type="radio" name="specId" id="specId5" value="5" />
						<s:text name="widget.configNavigator.specifiedPage" />
					</label> 
					<select class="inline" name="specCode">
						<s:iterator var="page" value="pages">
							<option value="<s:property value="#page.code"/>"><s:property value="#page.getShortFullTitle(currentLang.code)"/><s:if test="!#page.showable"> [i]</s:if></option>
						</s:iterator>
					</select>
				</li>
			</ul>
		</fieldset>
		<fieldset>
			<legend><s:text name="widget.configNavigator.operator" /></legend>
			<p>
				<label for="operatorId" class="basic-mint-label"><s:text name="widget.configNavigator.operatorType" /></label>
				<select name="operatorId" id="operatorId">
					<option value="0"><s:text name="widget.configNavigator.none" /></option>
					<option value="1"><s:text name="widget.configNavigator.allChildren" /></option>
					<option value="2"><s:text name="widget.configNavigator.allNodes" /></option>
					<option value="3"><s:text name="widget.configNavigator.nodesTothisLevel" /></option>
				</select>
			</p>
			<p>
				<label for="operatorSubtreeLevel" class="basic-mint-label"><s:text name="widget.configNavigator.levelOfNodesTothisLevel" /></label>
				<wpsf:select useTabindexAutoIncrement="true" name="operatorSubtreeLevel" id="operatorSubtreeLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" />
			</p>
		</fieldset>
		<p>
			<s:url var="addExpressionUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Navigator" action="addExpression" />
			<sj:submit targets="form-container" href="%{addExpressionUrlVar}" value="%{getText('label.add')}" indicator="indicator" button="true" cssClass="button" />
		</p>
		<p class="save-action">
			<sj:submit targets="form-container" value="%{getText('label.save')}" indicator="indicator" button="true" cssClass="button" />
		</p>
	</s:form>
</div>