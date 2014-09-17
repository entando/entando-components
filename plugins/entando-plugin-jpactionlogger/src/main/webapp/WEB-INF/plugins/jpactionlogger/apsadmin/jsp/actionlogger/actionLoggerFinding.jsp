<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"> <s:text name="title.actionLogger.management" /></span>
</h1>
<div id="main">
	<s:form action="list" cssClass="form-horizontal">
			<s:if test="hasActionErrors()">
				<div class="alert alert-danger  alert-dismissable fade in">
					<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
					<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
					<ul class="unstyled margin-small-top">
						<s:iterator value="actionErrors">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<%-- A main container --%>
			<div class="form-group">
				<%-- The input-group container --%>
				<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<%-- The addon group --%>
					<span class="input-group-addon">
						<%-- Icon --%>
						<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="actiondate"/>"></span>
						<span class="sr-only"><s:text name="label.search.by" />&#32;<s:text name="actiondate"/></span>
					</span>
					<%-- The main input --%>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="form-control input-lg datepicker" id="jpactionlogger_dateStart_cal" name="start" placeholder="%{getText('actiondate')} (dd/MM/yyyy)" />
					<%-- The search buttons --%>
					<span class="input-group-btn">
						<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" type="button" cssClass="btn btn-primary btn-lg">
							<span class="sr-only"><s:text name="label.search" /></span>
							<span class="icon fa fa-search"></span>
						</wpsf:submit>
						<%-- The collapsible toggler --%>
						<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
							<span class="sr-only"><s:text name="title.searchFilters" /></span>
							<span class="caret"></span>
						</button>
					</span>
				</div>
				<%-- Advanced search --%>
				<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<div id="search-advanced" class="collapse well collapse-input-group">
						<div class="accordion_element">
							<%-- Form Field EndDate --%>
								<div class="form-group">
									<label for="jpactionlogger_dateEnd_cal" class="control-label col-sm-2 text-right"><s:text name="end" /></label>
									<div class="col-sm-5">
										<wpsf:textfield useTabindexAutoIncrement="true"	cssClass="form-control datepicker" id="jpactionlogger_dateEnd_cal" name="end" placeholder="dd/MM/yyyy" />
									</div>
								</div>
							<%--Form Field User--%>
								<div class="form-group">
									<label for="jpactionlogger_username" class="control-label col-sm-2 text-right"><s:text name="username" /></label>
									<div class="col-sm-5">
										<wpsf:textfield useTabindexAutoIncrement="true"	cssClass="form-control" id="jpactionlogger_username" name="username" />
									</div>
								</div>
							<%--Form Field StartDate--%>
								<div class="form-group">
									<label for="jpactionlogger_namespace" class="control-label col-sm-2 text-right"><s:text name="namespace" /></label>
									<div class="col-sm-5">
										<wpsf:textfield useTabindexAutoIncrement="true"	cssClass="form-control" id="jpactionlogger_namespace" name="namespace" />
									</div>
								</div>
							<%--Form Field Action--%>
								<div class="form-group">
									<label for="jpactionlogger_actionName" class="control-label col-sm-2 text-right"><s:text name="actionName" /></label>
									<div class="col-sm-5">
										<wpsf:textfield useTabindexAutoIncrement="true"	cssClass="form-control" id="jpactionlogger_actionName" name="actionName" />
									</div>
								</div>
							<%--Form Field Parameters--%>
								<div class="form-group">
									<label for="jpactionlogger_params" class="control-label col-sm-2 text-right"><s:text name="params" /></label>
									<div class="col-sm-5">
										<wpsf:textfield useTabindexAutoIncrement="true"	cssClass="form-control" id="jpactionlogger_params" name="params" />
									</div>
								</div>
							<%-- Button specific search --%>
								<div class="form-group">
									<div class="col-sm-5 col-sm-offset-2">
										<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" type="button" cssClass="btn btn-primary">
											<span class="icon fa fa-search"></span>&#32;
											<s:text name="label.search" />
										</wpsf:submit>
									</div>
								</div>
						</div>
					</div>
				</div>
			</div>
	</s:form>
	<s:form action="search">
		<p class="sr-only">
			<s:hidden name="username" />
			<s:hidden name="namespace" />
			<s:hidden name="actionName" />
			<s:hidden name="params" />
			<s:hidden name="start" />
			<s:hidden name="end" />
		</p>
		<wpsa:subset source="actionRecords" count="10" objectName="groupActions" advanced="true" offset="5">
			<s:set var="group" value="#groupActions" />
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
			<div class="table-responsive">
				<%-- Results Table --%>
				<table class="table table-bordered" >
					<caption class="sr-only"><s:text name="actionLogger.list" /></caption>
					<tr>
						<th scope="col"><abbr title="<s:text name="label.remove" />">R</abbr></th>
						<th scope="col" class="text-center" ><s:text name="actiondate" /></th>
						<th scope="col"><s:text name="username" /></th>
						<th scope="col"><s:text name="namespace" /></th>
						<th scope="col"><s:text name="actionName" /></th>
						<th scope="col"><s:text name="params" /></th>
					</tr>
					<s:iterator var="id">
						<%-- Date is loaded here, because in this way is ready to be got in input from Action button --%>
						<s:set var="logRecord" value="%{getActionRecord(#id)}"/>
						<tr>
							<%-- Action Column --%>
								<td class="monospace centerText">
									<div class="btn-group btn-group-xs">
										<a class="btn btn-warning" href="<s:url action="delete"> ><s:param name="id" value="#logRecord.id"></s:param></s:url>" title="<s:text name="label.remove" />: <s:date name="#logRecord.actionDate" format="dd/MM/yyyy HH:mm:ss" />"><span class="icon fa fa-times-circle-o"></span>&#32;
											 <span class="sr-only"><s:text name="label.alt.clear" /></span>
										</a>
									</div>
								</td>
							<%-- Date Column --%>
								<td class="text-center text-nowrap"><s:set var="logRecord"	value="%{getActionRecord(#id)}" />
									<code><s:date name="#logRecord.actionDate" format="dd/MM/yyyy HH:mm:ss" /></code>
								</td>
							<%-- Username Column --%>
								<td class="monospace"><s:property value="#logRecord.username"/></td>
							<%-- NameSpace Column --%>
								<td ><s:property value="#logRecord.namespace"/></td>
							<%-- ActionName Column --%>
								<td><s:property value="#logRecord.actionName"/></td>
							<%-- Parameters Column --%>
								<td>
									<%--Shows firts three parameters--%>
									<%-- Like iterator is used logRecord.id--%>
									<s:set var="recordParametersStringVar" value="#logRecord.parameters" scope="page" />
									<c:choose>
										<c:when test="${empty recordParametersStringVar}">
											<span class="sr-only">No parameters.</span>
										</c:when>
										<c:otherwise>
											<%-- Each pair key-value is on a different line--%>
											<c:set var="recordParametersArrayVar" value="${fn:split(recordParametersStringVar, newLineChar )}" />
											<c:forEach items="${recordParametersArrayVar}" var="currentParameter" begin="0" end="2" varStatus="status">
												<c:out value="${currentParameter}"/>
												<c:if test="${not status.last}"><br /></c:if>
											</c:forEach>
											<c:if test="${fn:length(recordParametersArrayVar) > 3}">
												<%-- Rest of hidden Area --%>
													<div id="parameter-item-<c:out value="${logRecord.id}"/>" class="collapse">
															<%-- recordParametersStringVar contains all parameters like a string --%>
															<c:forEach items="${recordParametersArrayVar}" var="currentParameter" begin="3" varStatus="status">
																<c:out value="${currentParameter}"/>
																<c:if test="${not status.last}"><br /></c:if>
															</c:forEach>
													</div>
												<%-- If there are at least three parameters is shown the link &hellip to display more information--%>
													<a class="cursor-pointer" data-toggle="collapse" data-target="#parameter-item-<c:out value='${logRecord.id}'/>" title="All parameters">
														&hellip;
													</a>
											</c:if>
										</c:otherwise>
									</c:choose>
								</td>
						</tr>
					</s:iterator>
				</table>
			</div>
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
	</s:form>
</div>