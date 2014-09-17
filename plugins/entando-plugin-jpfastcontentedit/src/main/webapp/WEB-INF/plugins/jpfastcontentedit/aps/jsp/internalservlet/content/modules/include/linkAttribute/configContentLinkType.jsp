<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1><wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE" /></h1>
<s:include value="linkAttributeConfigIntro.jsp"></s:include>
<h3><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_2_OF_2" /></h3>
<s:include value="linkAttributeConfigReminder.jsp" />
<p><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_TO_CONTENT" /></p>

<form 
	class="form-horizontal"
	action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/search.action" />" 
	method="post">

		<s:if test="hasFieldErrors()">
			<div class="alert alert-block">
				<p><strong><wp:i18n key="jpfastcontentedit_ERRORS" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="fieldErrors">
							<s:iterator value="value">
									<li><s:property escape="false" /></li>
							</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		
		<p class="noscreen hide">
			<wpsf:hidden name="contentOnSessionMarker" />
		</p>

		<wp:i18n key="jpfastcontentedit_SELECT_ALL" var="jpfastcontentedit_SELECT_ALL" /> 
		<s:set var="select_all_label" value="#attr.jpfastcontentedit_SELECT_ALL" />
		
		<div class="control-group">
			<label class="control-label" for="jpfastcontentedit-configurelink-search-text"><wp:i18n key="jpfastcontentedit_SEARCH_FOR_DESCR" /></label>
			<div class="controls">
				<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="jpfastcontentedit-configurelink-search-text" cssClass="text" />
			</div>
		</div>
		<fieldset>
			<legend class="accordion_toggler"><wp:i18n key="jpfastcontentedit_SEARCH_FILTERS" /></legend>
			<div class="accordion_element">
				<div class="control-group">
					<label class="control-label" for="jpfastcontentedit-configurelink-search-contentType"><wp:i18n key="jpfastcontentedit_TYPE" /></label>
					<div class="controls">
						<wpsf:select 
							useTabindexAutoIncrement="true" 
							name="contentType" 
							id="jpfastcontentedit-configurelink-search-contentType" 
							list="contentTypes" 
							listKey="code" 
							listValue="descr" 
							headerKey="" 
							headerValue="%{#select_all_label}" 
							/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="jpfastcontentedit-configurelink-search-state"><wp:i18n key="jpfastcontentedit_STATUS" /></label>
					<div class="controls">
						<wpsf:select useTabindexAutoIncrement="true" 
							name="state" 
							id="jpfastcontentedit-configurelink-search-state" 
							list="avalaibleStatus" 
							headerKey="" 
							headerValue="%{#select_all_label}" 
							listKey="key" 
							listValue="%{getText(value)}" />	
					</div>
				</div>
			</div>
		</fieldset>
		<p class="form-actions">
			<wp:i18n key="jpfastcontentedit_SEARCH" var="jpfastcontentedit_SEARCH" />
			<s:set var="search_label" value="#attr.jpfastcontentedit_SEARCH" />
			<wpsf:submit useTabindexAutoIncrement="true" value="%{#search_label}" cssClass="btn" />
		</p>
</form>

<form 
	class="form-horizontal"
	action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/joinContentLink.action" />" 
	method="post">

	<wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
			<s:set var="group" value="#groupContent" />

			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>

			<p class="noscreen hide">
				<wpsf:hidden name="contentOnSessionMarker" />
				<wpsf:hidden name="lastGroupBy" />
				<wpsf:hidden name="lastOrder" />
			</p>

			<table 
				class="table table-striped table-condensed" 
				summary="<wp:i18n key="jpfastcontentedit_LINKATTRIBUTE_CONTENT_SUMMARY" />">
					<caption><wp:i18n key="jpfastcontentedit_LINKATTRIBUTE_CONTENT_CAPTION" /></caption>
					<tr>
					<%-- <th><s:text name="note.choice" /></th>  --%>
					<%-- <th><a href="
						<s:url action="changeOrder">
							<s:param name="text">
								<s:property value="#request.text"/>
							</s:param>
							<s:param name="contentType">
								<s:property value="#request.contentType"/>
							</s:param>
							<s:param name="state">
								<s:property value="#request.state"/>
							</s:param>
							<s:param name="pagerItem">
								<s:property value="#groupContent.currItem"/>
							</s:param>
							<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
							<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
							<s:param name="groupBy">code</s:param>
						</s:url>
					" tabindex="<wpsa:counter />"><s:text name="label.code" /></a></th>
					  --%>
						<th>
							<c:set var="descriptionLink">/ExtStr2/do/jpfastcontentedit/Content/Link/changeOrder.action?text=<s:property value="#request.text"/>&amp;contentType=<s:property value="#request.contentType"/>&amp;state=<s:property value="#request.state"/>&amp;pagerItem=<s:property value="#groupContent.currItem"/>&amp;lastGroupBy=<s:property value="lastGroupBy"/>&amp;lastOrder=<s:property value="lastOrder"/>&amp;groupBy=descr</c:set>
							<a href="<wp:action path="${descriptionLink}" />" tabindex="<wpsa:counter />"><wp:i18n key="jpfastcontentedit_DESCRIPTION" /></a>
							<%--
							<a href="
							<s:url action="changeOrder">
								<s:param name="text"> 
									<s:property value="#request.text"/>
								</s:param>
								<s:param name="contentType">
									<s:property value="#request.contentType"/>
								</s:param>
								<s:param name="state">
									<s:property value="#request.state"/>
								</s:param>
								<s:param name="pagerItem">
									<s:property value="#groupContent.currItem"/>
								</s:param>
								<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
								<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
								<s:param name="groupBy">descr</s:param>
							</s:url>
						" tabindex="<wpsa:counter />"><s:text name="label.description" /></a>
						--%>
						</th>
						<th><wp:i18n key="jpfastcontentedit_GROUP" /></th>
						<th>
							<c:set var="creationDateLink">/ExtStr2/do/jpfastcontentedit/Content/Link/changeOrder.action?text=<s:property value="#request.text"/>&amp;contentType=<s:property value="#request.contentType"/>&amp;state=<s:property value="#request.state"/>&amp;pagerItem=<s:property value="#groupContent.currItem"/>&amp;lastGroupBy=<s:property value="lastGroupBy"/>&amp;lastOrder=<s:property value="lastOrder"/>&amp;groupBy=created</c:set>
							<a href="<wp:action path="${creationDateLink}" />" tabindex="<wpsa:counter />"><wp:i18n key="jpfastcontentedit_CREATION_DATE" /></a>	
							<%--
							<a href="
							<s:url action="changeOrder">
								<s:param name="text">
									<s:property value="#request.text"/>
								</s:param>
								<s:param name="contentType">
									<s:property value="#request.contentType"/>
								</s:param>
								<s:param name="state">
									<s:property value="#request.state"/>
								</s:param>
								<s:param name="pagerItem">
									<s:property value="#groupContent.currItem"/>
								</s:param>
								<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
								<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
								<s:param name="groupBy">created</s:param>
							</s:url>
						" tabindex="<wpsa:counter />"><s:text name="label.creationDate" /></a>
							--%>
						</th>
						<th>
							<c:set var="lastEditLink">/ExtStr2/do/jpfastcontentedit/Content/Link/changeOrder.action?text=<s:property value="#request.text"/>&amp;contentType=<s:property value="#request.contentType"/>&amp;state=<s:property value="#request.state"/>&amp;pagerItem=<s:property value="#groupContent.currItem"/>&amp;lastGroupBy=<s:property value="lastGroupBy"/>&amp;lastOrder=<s:property value="lastOrder"/>&amp;groupBy=lastModified</c:set>
							<a href="<wp:action path="${lastEditLink}" />" tabindex="<wpsa:counter />"><wp:i18n key="jpfastcontentedit_LAST_EDIT" /></a>
							<%--
								<a href="
								<s:url action="changeOrder">
									<s:param name="text">
										<s:property value="#request.text"/>
									</s:param>
									<s:param name="contentType">
										<s:property value="#request.contentType"/>
									</s:param>
									<s:param name="state">
										<s:property value="#request.state"/>
									</s:param>
									<s:param name="pagerItem">
										<s:property value="#groupContent.currItem"/>
									</s:param>
									<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
									<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
									<s:param name="groupBy">lastModified</s:param>
								</s:url>
								" tabindex="<wpsa:counter />"><s:text name="label.lastEdit" /></a>
							--%>
						</th>
					</tr>
				<s:iterator var="contentId">
					<s:set var="content" value="%{getContentVo(#contentId)}" />
					<tr>
						<%-- <td><s:property value="#content.id" /></td>  --%>
						<td>
							<label class="radio" for="contentId_<s:property value="#content.id"/>">
								<input type="radio" name="contentId" id="contentId_<s:property value="#content.id"/>" value="<s:property value="#content.id"/>" />
								<s:property value="#content.descr" />
							</label>
						</td>
						<td><s:property value="groupsMap[#content.mainGroupCode].descr" /></td>
						<td><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></td>
						<td><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></td>
					</tr>
				</s:iterator>
		</table>
		<div class="pager">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>
	</wpsa:subset>
	<p>
		<label class="checkbox" for="contentOnPageType">
			<wpsf:checkbox useTabindexAutoIncrement="true" name="contentOnPageType" id="contentOnPageType" />
			<wp:i18n key="jpfastcontentedit_MAKE_CONTENT_ON_PAGE" />
		</label>
	</p>

	<p class="form-actions">
		<wp:i18n key="jpfastcontentedit_CONFIRM" var="jpfastcontentedit_CONFIRM" />
		<wpsf:submit 
			useTabindexAutoIncrement="true" 
			value="%{#attr.jpfastcontentedit_CONFIRM}" 
			title="%{#attr.jpfastcontentedit_CONFIRM}" 
			cssClass="btn btn-primary" />
		&emsp;
		<a class="btn btn-link"
			href="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/backToEntryContent.action">
			<wp:parameter name="contentOnSessionMarker"><s:property value="%{contentOnSessionMarker}" /></wp:parameter>
			</wp:action>
			" >
			<wp:i18n key="jpfastcontentedit_BACK_TO_CONTENT" />
		</a>
	</p>

</form>