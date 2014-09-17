<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ page contentType="charset=UTF-8" %>
<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>
<p class="noscreen"><a href="#editFrame"><s:text name="note.goToEditFrame" /></a></p>

<div id="main">
	<h2><s:text name="title.configPage" /></h2>

	<s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
	<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

	<div class="subsection-light">
		<h3><s:text name="title.configPage.youAreDoing" /></h3>
		<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />

		<s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
		<h3 class="margin-more-top"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(#showletType.code, #showletType.titles)}" /></h3>

		<s:form>
			<s:if test="hasActionErrors()">
				<div class="message message_error">
				<h4><s:text name="message.title.ActionErrors" /></h4>
					<ul>
					<s:iterator value="actionErrors">
						<li><s:property/></li>
					</s:iterator>
					</ul>
				</div>
			</s:if>
			<fieldset class="margin-more-top">
				<legend><s:text name="label.info" /></legend>
				<p class="noscreen">
					<s:hidden name="pageCode" />
					<s:hidden name="frame" />
					<s:hidden name="widgetTypeCode" />
					<s:hidden name="pageCodeParam" />
				</p>

				<p>
					<s:text name="note.selectedPage" />:
					<s:iterator value="langs" status="rowStatus">
						<s:if test="#rowStatus.index != 0">, </s:if><span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span> <s:property value="targetPage.getTitles()[code]" />
					</s:iterator>.
				</p>

				<p>
					<s:text name="note.selectAFrame.msg" />
				</p>

				<table class="generic">
					<caption><span><s:text name="label.frames" /></span></caption>
					<tr>
						<th><abbr title="<s:text name="name.position" />"><s:text name="name.position.abbr" /></abbr></th>
						<th><s:text name="label.description" /></th>
						<th><s:text name="name.widget" /></th>
					</tr>
					<s:iterator value="targetPage.showlets" id="showlet" status="rowstatus">
						<s:set var="frames" value="targetPage.getModel().getFrames()" ></s:set>
						<s:set var="showletType" value="#showlet.getType()" ></s:set>
						<tr>
							<td class="rightText">
								<s:if test="targetPage.getModel().getMainFrame() == #rowstatus.index"><img src="<wp:resourceURL/>administration/img/icons/16x16/emblem-important.png" alt="<s:text name="name.mainFrame" />: " title="<s:text name="name.mainFrame" />" /><s:property value="#rowstatus.index"/></s:if>
								<s:else><s:property value="#rowstatus.index"/></s:else>
							</td>
							<td>
								<a href="<s:url action="selectFrame" >
									<s:param name="frame" value="frame"/>
									<s:param name="pageCode" value="pageCode"/>
									<s:param name="widgetTypeCode" value="widgetTypeCode"/>
									<s:param name="pageCodeParam" value="pageCodeParam" />
									<s:param name="frameIdParam" value="#rowstatus.index" />
								</s:url>"><s:property value="targetPage.getModel().getFrames()[#rowstatus.index]"/></a>
							</td>
							<td>
								<s:if test='%{getTitle(#showletType.getCode(), #showletType.getTitles())!="" }'>
									<s:property value="%{getTitle(#showletType.getCode(), #showletType.getTitles())}" />
								</s:if>
							</td>
						</tr>
					</s:iterator>
				</table>
			</fieldset>
		</s:form>
	</div>
</div>