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
			<s:if test="contact.id == null">
				<s:text name="jpaddressbook.title.newContact" />
			</s:if>
			<s:else>
				<s:text name="jpaddressbook.title.edit" />
			</s:else>
	</span>
</h1>
<div id="main">
	<s:form cssClass="form-horizontal">
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
				</ul>
			</div>
		</s:if>

			<s:set var="lang" value="defaultLang" />
			<s:iterator value="contact.attributes" var="attribute">
				<s:if test="#attribute.active">
					<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
					<div class="form-group">
						<div class="col-xs-12">
							<s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
								<label class="display-block">
									<span class="icon fa fa-list" title="<s:text name="label.list" />"></span>
									<s:property value="#attribute.name" />
									&#32;
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
								</label>
							</s:if>
							<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox' || #attribute.type == 'ThreeState'">
								<label class="display-block">
									<s:property value="#attribute.name" />
									&#32;
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
								</label>
							</s:elseif>
							<s:elseif test="#attribute.type == 'CheckBox'">
							<%-- Leave this completely blank --%>
							</s:elseif>
							<s:else>
								<label
									class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
										<s:property value="#attribute.name" />
							 	</label>
							</s:else>
								<s:if test="#attribute.type == 'Boolean'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
								</s:if>
								<s:elseif test="#attribute.type == 'CheckBox'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Date'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Enumerator'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'EnumeratorMap'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Hypertext'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Longtext'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Monotext'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Number'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Text'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'ThreeState'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Timestamp'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Composite'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/compositeAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'List'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
								</s:elseif>
								<s:elseif test="#attribute.type == 'Monolist'">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monolistAttribute.jsp" />
								</s:elseif>
						</div><%-- form group --%>
					</div>
				</s:if>
			</s:iterator>
				<%-- END CICLO ATTRIBUTI --%>
				<div class="form-group">
					<div class="col-xs-12 col-sm-4 col-md-3 margin-base-top">
						<wpsf:submit  value="%{getText('label.save')}" type="button" cssClass="btn btn-primary btn-block" action="save">
							 <span class="icon fa fa-floppy-o">&#32;<s:text name="label.save" /></span>
						 </wpsf:submit>
					</div>
				</div>
		</s:form>
</div>
