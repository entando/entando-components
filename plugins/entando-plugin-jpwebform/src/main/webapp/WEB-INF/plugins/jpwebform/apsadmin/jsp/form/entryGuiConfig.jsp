<%-- should we delete this? --%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1><a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityManagement" />"><s:text name="jpwebform.title.formsManagement" /></a></h1>
<div id="main">
	<s:if test="hasActionErrors()">
		<div class="message message_error">
			<h2><s:text name="message.title.ActionErrors" /></h2>
			<ul>
				<s:iterator value="actionErrors">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
			<h3><s:text name="message.title.FieldErrors" /></h3>	
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escapeHtml="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>

	<s:set var="stepsConfigVar" value="stepsConfig" />
	<s:set var="entityTypeCode" value="entityTypeCode" />
	<s:set var="entityPrototypeVar" value="%{getEntityPrototype(#entityTypeCode)}" />

	<s:form action="save" namespace="/do/jpwebform/Config/Gui/">
		<p class="noscreen">
			<wpsf:hidden name="entityTypeCode" />
		</p>

		<s:if test="#stepsConfigVar==null || #stepsConfigVar.steps == null || #stepsConfigVar.steps.size() == 0">
			<fieldset>
				<legend><s:text name="jpwebform.label.steps.configuraton" /></legend>	
				<p>
					<s:url namespace="/do/jpwebform/Config/Step" action="entry" var="addStepAction"><s:param name="entityTypeCode" value="#entityTypeCode" /></s:url>
					No Steps configured. <a href="<s:property value="#addStepAction" />">Add one step&#32;<img src="<wp:resourceURL />administration/common/img/icons/next.png" alt="<s:text name="jpwebform.steps.configure.long" />" /></a>
					
				</p>
			</fieldset>
		</s:if>
		<s:else>
			<%--
			<s:iterator var="stepVar" value="#stepsConfigVar.steps">
				<fieldset>
					<legend><s:property value="#stepVar.code" /></legend>
						<p>
							<s:text name="jpwebform.label.steps.gui.ready" />:
							<span class="important">
								<s:if test="#stepsConfigVar.builtGui"><s:text name="label.yes" /></s:if>
								<s:else><s:text name="label.no" /></s:else>
							</span>
						</p>
						<ul>
							<s:iterator var="attributeNameVar" value="#stepVar.attributeOrder" >
								<s:set var="attributeConfigVar" value="%{#stepVar.attributeConfigs[#attributeNameVar]}" />
								<s:set var="attributeVar" value="%{#entityPrototypeVar.getAttribute(#attributeNameVar)}" />
								<li>
									<s:property value="#attributeConfigVar.name" /> - 
									<s:property value="#attributeConfigVar.view" /> -
									<s:if test="null == #attributeVar" >
										ATTRIBUTE ISN'T INTO MODEL
									</s:if>
									<s:else>
										TYPE <s:property value="#attributeVar.type" />
									</s:else>
								</li>
							</s:iterator>
						</ul>
						<p>
							<wpsa:actionParam action="editGui" var="actionName" >
								<wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
							</wpsa:actionParam>
							<wpsf:submit 
								useTabindexAutoIncrement="true" 
								action="%{#actionName}" 
								value="%{getText('label.editGui')}" 
								title="%{getText('label.editGui')}" 
							/>
						</p>
				</fieldset>
			</s:iterator>
			--%>
			<s:iterator value="#stepsConfigVar.steps" var="stepVar" >
				<s:set name="stepCode" value="#stepVar.code" />
				<fieldset>
					<legend>
						<span><s:property value="#stepVar.code" /> (<s:text name="jpwebform.label.steps.gui.ready" />:&#32;<s:property value="%{#stepVar.builtGui ? getText('label.yes') : getText('label.no')}" />)</span>
					</legend>

					<table class="generic">
						<caption>
							<span>Attribute List</span>
							<%-- ORDER <s:property value="#stepVar.order" />  - TYPE <s:property value="#stepVar.type" /> --%> 
						</caption>
						<tr>
							<th><s:text name="jpwebform.label.attribute.name" /></th>
							<th><s:text name="jpwebform.label.attribute.type" /></th>
							<th class="icon"><s:text name="jpwebform.steps.attribute.viewOnly" /></th>
						</tr>
						<s:iterator var="attributeNameVar" value="#stepVar.attributeOrder" >
							<s:set var="attributeConfigVar" value="%{#stepVar.attributeConfigs[#attributeNameVar]}" />
							<s:set var="attributeVar" value="%{#entityPrototypeVar.getAttribute(#attributeNameVar)}" />
							<tr>
								<td><s:property value="#attributeConfigVar.name" /></td>
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
					<p>
						<wpsa:actionParam action="editGui" var="actionName" >
							<wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
						</wpsa:actionParam>
						<wpsf:submit 
							useTabindexAutoIncrement="true" 
							action="%{#actionName}" 
							value="%{getText('label.editGui')}" 
							title="%{getText('label.editGui')}" 
						/>
					</p>
				</fieldset>
			</s:iterator>
		</s:else>
		
		<fieldset>
			<legend><s:text name="jpwebform.label.confirmStep" /></legend>
			<p>
				<s:text name="jpwebform.label.steps.gui.ready" />:
				<span class="important">
					<s:if test="#stepsConfigVar.builtConfirmGui"><s:text name="label.yes" /></s:if>
					<s:else><s:text name="label.no" /></s:else>
				</span>
				&#32;
				<wpsa:actionParam action="editGui" var="actionName" >
					<wpsa:actionSubParam name="stepCode" value="confirm" />
				</wpsa:actionParam>
				<wpsf:submit 
					cssClass="button"
					useTabindexAutoIncrement="true" 
					action="%{#actionName}" 
					value="%{getText('jpwebform.label.editgui')}" 
					/>
			</p>
		</fieldset>
		
		<fieldset>
			<legend><s:text name="jpwebform.endpoint.step" /></legend>
			<p>
				<s:text name="jpwebform.label.steps.gui.ready" />:
				<span class="important">
					<s:if test="#stepsConfigVar.builtEndPointGui"><s:text name="label.yes" /></s:if>
					<s:else><s:text name="label.no" /></s:else>
				</span>
				<wpsa:actionParam action="editGui" var="actionName" >
					<wpsa:actionSubParam name="stepCode" value="ending" />
				</wpsa:actionParam>
				<wpsf:submit 
					cssClass="button"
					useTabindexAutoIncrement="true" 
					action="%{#actionName}" 
					value="%{getText('jpwebform.label.editgui')}" 
					/>
			</p>
		</fieldset>

	</s:form>
</div>