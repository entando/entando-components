<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="random"><c:out value="${random}" /></s:set>
<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-contentFinding">
	<h2><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h2>
	<div class="subsection-light">
		<h3 class="margin-more-top margin-bit-bottom">
			<s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
			<s:text name="name.widget" />:&#32;<s:property value="%{getTitle(#showletType.code, #showletType.titles)}" />
		</h3>
		<s:form cssClass="form-inline" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Viewer" action="searchContents" id="formform" theme="simple">
			<p class="noscreen">
				<wpsf:hidden name="pageCode" />
				<wpsf:hidden name="frame" />
				<wpsf:hidden name="widgetTypeCode" />
				<wpsf:hidden name="modelId" />
			</p>
			<s:if test="hasFieldErrors()">
				<div class="alert">
				<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
					<ul class="unstyled">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
						<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
					</ul>
				</div>
			</s:if>
			<fieldset class="well well-small form-horizontal">
				<legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
				<div class="accordion_element">
					<div class="control-group">
						<label class="control-label" for="<c:out value="text${random}" />"><s:text name="label.search.by"/>&#32;
						<s:text name="label.description"/></label>
						<div class="controls">
							<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="%{'text'+#random}" cssClass="text" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="<c:out value="contentIdToken${random}" />"><s:text name="label.code"/></label>
						<div class="controls">
							<wpsf:textfield useTabindexAutoIncrement="true" name="contentIdToken" id="%{'contentIdToken'+#random}" cssClass="text" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="<c:out value="contentType${random}" />"><s:text name="label.type"/></label>
						<div class="controls">
							<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="%{'contentType'+#random}" 
								list="contentTypes" listKey="code" listValue="descr" 
								headerKey="" headerValue="%{getText('label.all')}" cssClass="text"></wpsf:select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="<c:out value="state${random}" />"><s:text name="label.state"/></label>
						<div class="controls">
							<wpsf:select useTabindexAutoIncrement="true" name="state" id="%{'state'+#random}" list="avalaibleStatus" 
								headerKey="" headerValue="%{getText('label.all')}" cssClass="text" listKey="key" listValue="%{getText(value)}" />
						</div>
					</div>
					<p>
						<s:url var="searchContentsUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Viewer" action="searchContents" />
						<sj:submit targets="form-container" href="%{#searchContentsUrlVar}" value="%{getText('label.search')}" indicator="indicator" button="true" cssClass="button" />
					</p>
				</div>
			</fieldset>
			<wpfssa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
				<s:set name="group" value="#groupContent" />
				<s:set var="pagerSubmitActionNameVar" value="'searchContents'" />
				<div class="archive-pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/include/pager_formBlock.jsp" />
				</div>
				<p class="noscreen">
					<wpsf:hidden name="lastGroupBy" />
					<wpsf:hidden name="lastOrder" />
				</p>
				<s:if test="%{#group.size>0}">
				<table class="table table-striped table-bordered" summary="<s:text name="note.page.contentViewer.summary" />">
					<caption><span><s:text name="title.contentList" /></span></caption>
					<tr>
						<th>
							<a href="
								<s:url action="changeContentListOrder">
									<s:param name="text">
										<s:property value="#request.text"/>
									</s:param>
									<s:param name="contentIdToken">
										<s:property value="#request.contentIdToken"/>
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
									<s:param name="pageCode">
										<s:property value="#request.pageCode"/>
									</s:param>
									<s:param name="frame">
										<s:property value="#request.frame"/>
									</s:param>
									<s:param name="modelId">
										<s:property value="#request.modelId"/>
									</s:param>
									<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
									<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
									<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
									<s:param name="groupBy">descr</s:param>
								</s:url>
							">
								<s:text name="label.description" />
							</a>
						</th>
						<th>
							<a href="
								<s:url action="changeContentListOrder">
									<s:param name="text">
										<s:property value="#request.text"/>
									</s:param>
									<s:param name="contentIdToken">
										<s:property value="#request.contentIdToken"/>
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
									<s:param name="pageCode">
										<s:property value="#request.pageCode"/>
									</s:param>
									<s:param name="frame">
										<s:property value="#request.frame"/>
									</s:param>
									<s:param name="modelId">
										<s:property value="#request.modelId"/>
									</s:param>
									<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
									<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
									<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
									<s:param name="groupBy">code</s:param>
								</s:url>
							">
								<s:text name="label.code" />
							</a>
						</th>
						<th><s:text name="label.group" /></th>
						<%--
						<th>
							<a href="
								<s:url action="changeContentListOrder">
									<s:param name="text">
										<s:property value="#request.text"/>
									</s:param>
									<s:param name="contentIdToken">
										<s:property value="#request.contentIdToken"/>
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
									<s:param name="pageCode">
										<s:property value="#request.pageCode"/>
									</s:param>
									<s:param name="frame">
										<s:property value="#request.frame"/>
									</s:param>
									<s:param name="modelId">
										<s:property value="#request.modelId"/>
									</s:param>
									<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
									<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
									<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
									<s:param name="groupBy">created</s:param>
								</s:url>
							">
								<s:text name="label.creationDate" />
							</a>
						</th>
						--%>
						<th>
							<a href="
								<s:url action="changeContentListOrder">
									<s:param name="text">
										<s:property value="#request.text"/>
									</s:param>
									<s:param name="contentIdToken">
										<s:property value="#request.contentIdToken"/>
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
									<s:param name="pageCode">
										<s:property value="#request.pageCode"/>
									</s:param>
									<s:param name="frame">
										<s:property value="#request.frame"/>
									</s:param>
									<s:param name="modelId">
										<s:property value="#request.modelId"/>
									</s:param>
									<s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
									<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
									<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
									<s:param name="groupBy">lastModified</s:param>
								</s:url>
							">
								<s:text name="label.lastEdit" />
							</a>
						</th>
					</tr>
					<s:iterator var="contentId">
						<s:set name="content" value="%{getContentVo(#contentId)}"></s:set>
						<tr>
							<td>
								<label class="radio" for="contentId_<s:property value="#content.id+#random"/>">
									<input type="radio" name="contentId" id="contentId_<s:property value="#content.id+#random"/>" value="<s:property value="#content.id"/>" />
								<s:property value="#content.descr" /></label>
							</td>
							<td><span class="monospace"><s:property value="#content.id" /></span></td>
							<td><s:property value="%{getGroup(#content.mainGroupCode).descr}" /></td>
							<%--
							<td><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></td>
							--%>
							<td><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></td>
						</tr>
					</s:iterator>
				</table>
				<div class="archive-pager">
					<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/include/pager_formBlock.jsp" />
				</div>
				</s:if>
			</wpfssa:subset>
			<p class="centerText margin-more-top">
				<s:url var="executeJoinContentUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/Viewer" action="executeJoinContent" />
				<sj:submit targets="form-container" href="%{#executeJoinContentUrlVar}" value="%{getText('label.confirm')}" indicator="indicator" button="true" cssClass="button" />
			</p>
		</s:form>
	</div>
</div>