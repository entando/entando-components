<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"  %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"  %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"  %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />">
			<s:text name="title.contactManagement" />
		</a>
		&#32;/&#32;
		<s:text name="jpaddressbook.title.details" />
	</span>
</h1>
<div id="main">
	<%--<legend class="accordion_toggler"><s:text name="jpaddressbook.title.details" /></legend>--%>
	<div class="panel panel-default">
		<div class="panel-body">
			<s:set name="lang" value="defaultLang" />
				<s:iterator value="contact.attributes" var="attribute" status="attributeListStatus"><%-- content.attributeList iterator --%>
					<s:if test="#attribute.active">
						<div class="form-group"><%-- form-group --%>
							<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" /><%-- tracer init --%>
							<s:if test="null != #attribute.description"><s:set var="attributeLabelVar" value="#attribute.description" /></s:if>
							<s:else><s:set var="attributeLabelVar" value="#attribute.name" /></s:else>
							<label><s:property value="#attributeLabelVar" /></label>
						 	<div class="input-group"><%--input-group --%>
								<s:if test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Enumerator'">
									<s:if test="#lang.default">
										<div>
											<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
										</div>
									</s:if>
									<s:else>
										<s:if test="%{(#attribute.isMultilingual()) && (#attribute.getTextForLang(#lang.code)!=null)}">
												<div>
													<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
												</div>
										</s:if>
										<s:else>
												<div class="text-muted">
													<s:property value="#attribute.getText()" />
											</div>
										</s:else>
									</s:else>
								</s:if>
								<s:elseif test="#attribute.type == 'Attach'">
									<p><s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/attachAttribute.jsp" /></p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Date'">
									<p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" /></p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Enumerator'">
									<p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" /></p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'EnumeratorMap'">
									<p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorMapAttribute.jsp" /></p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Hypertext'">
									<div><s:include value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" /></div>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Number'">
									<p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" /></p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'ThreeState'">
									<p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" /></p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Timestamp'">
									<p><s:include value="/WEB-INF/apsadmin/jsp/entity/view/timestampAttribute.jsp" /></p>
								</s:elseif>
								<s:elseif test="#attribute.type == 'Composite'">
									<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/compositeAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'List'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Monolist'">
									<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/view/monolistAttribute.jsp" />
								</s:elseif>
							</div><%--input-group --%>
						</div><%-- form-group --%>
					</s:if>
					<s:if test="!#attributeListStatus.last"><hr /></s:if>
				</s:iterator><%-- content.attributeList iterator --%>
		</div>
	</div>
	<s:set var="contact" value="%{contact}" />
	<s:set value="userProfilePrototype" var="%{getUserProfilePrototype()}" />
	<a
		href="<s:url action="downloadvcard" namespace="/do/jpaddressbook/VCard">
			<s:param name="entityId" value="%{#contact.id}"></s:param>
		</s:url>"
		title="<s:text name="jpaddressbook.download.vcard" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>"
		class="btn btn-default"
		>
		<span class="icon fa fa-download"></span>&#32;Download VCard
	</a>
	<%-- END CICLO ATTRIBUTI --%>
</div>
