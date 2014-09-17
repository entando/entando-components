<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"  %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"  %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"  %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.contactManagement" />
	</span>
</h1>
<div id="main">
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger  alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
			<ul class="unstyled margin-small-top">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:form cssClass="form-horizontal" action="search">
		<s:set var="searcheableAttributes" value="searcheableAttributes" />
		<s:iterator var="attribute" value="#searcheableAttributes">
			<s:set var="currentFieldId">jpaddressbook_contactFinding_<s:property value="#attribute.name" /></s:set>
				<s:if test="#attribute.textAttribute">
					<div class="form-group">
						<div class="col-xs-12">
							<label for="<s:property value="currentFieldId" />">
								<s:property value="#attribute.name" />
							</label>
							<s:set name="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
							<wpsf:textfield id="%{#currentFieldId}" cssClass="form-control" name="%{#textInputFieldName}" value="%{getSerchFormValue(#textInputFieldName)}" /><br />
						</div>
					</div>
				</s:if>
				<s:elseif test="#attribute.type == 'Date'">
					<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
					<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
					<div class="form-group">
						<div class="col-xs-12">
							<label for="<s:property value="currentFieldId" />_start_cal"><s:property value="#attribute.name" /><s:text name="jpaddressbook.label.search.dateStartvalue" /></label>
							<wpsf:textfield id="%{#currentFieldId}_start_cal" cssClass="form-control" name="%{#dateStartInputFieldName}" value="%{getSerchFormValue(#dateStartInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<label for="<s:property value="currentFieldId" />_end_cal"><s:property value="#attribute.name" />&#32;<s:text name="jpaddressbook.label.search.dateEndvalue" />:</label>
							<wpsf:textfield id="%{#currentFieldId}_end_cal" cssClass="form-control" name="%{#dateEndInputFieldName}" value="%{getSerchFormValue(#dateEndInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</div>
					</div>
				</s:elseif>
				<s:elseif test="#attribute.type == 'Number'">
					<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
					<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
					<div class="form-group">
						<div class="col-xs-12">
							<label for="<s:property value="currentFieldId" />_start"><s:property value="#attribute.name" />&#32;<s:text name="jpaddressbook.label.search.numberStartvalue" />:</label>
							<wpsf:textfield  id="%{#currentFieldId}_start" cssClass="text" name="%{#numberStartInputFieldName}" value="%{getSerchFormValue(#numberStartInputFieldName)}" /><br />
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<label for="<s:property value="currentFieldId" />_end"><s:property value="#attribute.name" />&#32;<s:text name="jpaddressbook.label.search.numberEndvalue" />:</label>
							<wpsf:textfield id="%{#currentFieldId}_end" cssClass="text" name="%{#numberEndInputFieldName}" value="%{getSerchFormValue(#numberEndInputFieldName)}" /><br />
						</div>
					</div>
				</s:elseif>
				<s:elseif test="#attribute.type == 'Boolean'">
					<div class="form-group">
						<div class="col-xs-12">
							<wpsf:checkbox name="%{#attribute.name}" id="%{#currentFieldId}"  cssClass="radiocheck"/>&nbsp;<label for="<s:property value="currentFieldId"/>" ><s:property value="#attribute.name" /></label>
						</div>
					</div>
				</s:elseif>
				<s:elseif test="#attribute.type == 'ThreeState'">
					<div class="form-group">
						<div class="col-xs-12">
							<label><s:property value="#attribute.name" /></label>
							<div class="input-group">
								<div class="radiocheck">
									<label for="none_<s:property value="%{#currentFieldId}" />" class="normal" >
										<wpsf:radio name="%{#attribute.name}" id="none_%{#currentFieldId}" value="" checked="%{#attribute.booleanValue == null}" cssClass="radiocheck" />
										<s:text name="label.bothYesAndNo"/>
									</label>
								</div>
								<div class="radiocheck">
									<label for="true_<s:property value="%{#currentFieldId}" />" class="normal" >
										<wpsf:radio name="%{#attribute.name}" id="true_%{#currentFieldId}" value="true" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" cssClass="radiocheck" />
										<s:text name="label.yes"/>
									</label>
								</div>
								<div class="radiocheck">
									<label for="false_<s:property value="%{#currentFieldId}" />" class="normal">
										<wpsf:radio name="%{#attribute.name}" id="false_%{#currentFieldId}" value="false" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" cssClass="radiocheck" />
										<s:text name="label.no"/>
									</label>
								</div>
							</div>
						</div>
					</div>
				</s:elseif>
		</s:iterator>

		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3">
				<wpsf:submit value="%{getText('label.search')}" cssClass="btn btn-primary"  type="button" action="search" >
					<span class="icon fa fa-search"></span>&#32;
					<s:text name="label.search"/>
				</wpsf:submit>
		 </div>
		</div>

	</s:form>

	<hr />

	<div class="row">
		<div class="col-xs-12">
			<%-- Button new --%>
			<a class="btn btn-default" href="<s:url action="new" />"><span class="icon fa fa-plus-circle"></span>
				&#32;<s:text name="label.new" />
			</a>
		</div>
	</div>
<%-- Results Area --%>
	<div class="subsection-light">
		<s:form action="search">
			<p class="sr-only">
				<s:iterator var="attribute" value="#searcheableAttributes">
					<s:if test="#attribute.textAttribute">
							<s:set name="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
							<wpsf:hidden name="%{#textInputFieldName}" value="%{getSerchFormValue(#textInputFieldName)}" />
					</s:if>
					<s:elseif test="#attribute.type == 'Date'">
							<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
							<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
							<wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSerchFormValue(#dateStartInputFieldName)}" />
							<wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSerchFormValue(#dateEndInputFieldName)}" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Number'">
							<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
							<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
							<wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSerchFormValue(#numberStartInputFieldName)}" />
							<wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSerchFormValue(#numberEndInputFieldName)}" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Boolean'">
						<%-- todo --%>
					</s:elseif>
					<s:elseif test="#attribute.type == 'ThreeState'">
						<%-- todo --%>
					</s:elseif>
				</s:iterator>
			</p>
			<s:set var="contactIds" value="searchResult" />
			<%-- <s:if test="#contactIds.isEmpty()">no risultati </s:if> --%>
			<wpsa:subset source="#contactIds" count="10" objectName="groupContact" advanced="true" offset="5">
			<s:set name="group" value="#groupContact" />
				<div class="pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
				<s:set value="userProfilePrototype" var="userProfilePrototype" />
				<%-- TABLE: Results --%>
					<div class="table-responsive">
						<s:set var="contact" value="%{getContact(#contactId)}" />
						<%--CONTROL: table is displayed if  there aren elements --%>
						<s:if test="#group.size >0">
						<table class="table table-bordered">
							<caption class="sr-only"><s:text name="contacts.list" /></caption>
							<tr>
								<th class="text-center padding-large-left padding-large-right"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
								<s:if test="#userProfilePrototype.surnameAttributeName != null"><th><s:text name="jpaddressbook.label.surnameAttribute" /></th></s:if>
								<s:if test="#userProfilePrototype.firstNameAttributeName != null"><th><s:text name="jpaddressbook.label.nameAttribute" /></th></s:if>
								<s:if test="#userProfilePrototype.mailAttributeName != null"><th><s:text name="jpaddressbook.label.mailAttribute" /></th></s:if>
								<th scope="col"><abbr title="<s:text name="jpaddressbook.label.ownerAttributeFull" />"><s:text name="jpaddressbook.label.ownerAttributeShort" /></abbr></th>
								<%--
								<th scope="col" class="text-center" ><abbr title="<s:text name="jpaddressbook.download.vcard" />"><s:text name="jpaddressbook.name.vcard" /></abbr></th>
								--%>
							</tr>
							<s:iterator var="contactId">
								<s:set var="contact" value="%{getContact(#contactId)}" />
								<tr>
									<%-- <td><s:property value="#contactId" /></td> --%>
									<%-- EVENTUALI COLONNE CONFIGURABILI IN BASE AL MODELLO DEL PROFILO --%>
										<td class="text-center text-nowrap">
										 <div class="btn-group btn-group-xs">
										 		<%-- View button --%>
													<a
														class="btn btn-default"
														title="<s:text name="jpaddressbook.label.openDetailsFull" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>"
														href="<s:url action="view" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />"
														>
														<span class="sr-only"><s:text name="label.view" /></span>
														<span class="icon fa fa-info"></span>
													</a>
												<s:if test="#contact.owner.equals(#session.currentUser.username)">
													<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
            								<span class="caret"></span>
          								</button>
          								<ul class="dropdown-menu text-left" role="menu">
          									<li>
															<%-- Edit button --%>
																<a
																	title="<s:text name="jpaddressbook.label.editContact" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>"
																	href="<s:url action="view" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />"
																	>
																	<span class="icon fa fa-pencil-square-o fa-fw"></span>
																	<s:text name="label.edit" />
																</a>
          									</li>
          									<li>
															<%-- download --%>
															<a
																title="<s:text name="jpaddressbook.download.vcard" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>"
																href="<s:url action="downloadvcard" namespace="/do/jpaddressbook/VCard"><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />"
																>
																	<span class="icon fa fa-download fa-fw"></span>
																		Download VCard
																	<span class="sr-only">&#32;
																		<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;
																		<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>
																	</span>
															</a>
          									</li>
          								</ul>
												</s:if>
										 </div>
											<s:if test="#contact.owner.equals(#session.currentUser.username)">
												<div class="btn-group btn-group-xs">
														<%-- Remove button --%>
															<a class="btn btn-warning" title="<s:text name="label.remove" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>" href="<s:url action="trash" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
																<span class="icon fa fa-times-circle-o"></span>&#32;
																<span class="sr-only"><s:text name="label.remove" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/></span>
															</a>
												</div>
											</s:if>
										</td>
									<s:if test="#userProfilePrototype.surnameAttributeName != null">
										<td>
											<a title="<s:text name="jpaddressbook.label.editContact" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>" href="<s:url action="edit" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
												<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>
											</a>
										</td>
									</s:if>
									<s:if test="#userProfilePrototype.firstNameAttributeName != null">
										<td>
											<a title="<s:text name="jpaddressbook.label.editContact" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>" href="<s:url action="edit" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
												<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>
											</a>
										</td>
									</s:if>
									<s:if test="#userProfilePrototype.mailAttributeName != null">
										<td>
											<s:property value="#contact.getValue(#userProfilePrototype.mailAttributeName)"/>
										</td>
									</s:if>
									<td>
										<code><s:property value="#contact.owner" /></code>
									</td>
								</tr>
							</s:iterator>
						</table>
						</s:if>
					</div>
				<div class="pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
			</wpsa:subset>
		</s:form>
	</div>
</div>
