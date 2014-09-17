<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="targetNS" value="%{'/do/jprss/Rss'}" />
<h1><s:text name="jprss.title.rssManagement" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">
	<s:if test="strutsAction ==  1">
		<h2><s:text name="jprss.title.addChannel" /></h2>
	</s:if>
	<s:if test="strutsAction ==  2">
		<h2><s:text name="jprss.title.editChannel" /></h2>
	</s:if>
	
	<s:form action="save">
		<s:if test="hasFieldErrors()">
			<div class="message message_error">	
				<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
				            <li><s:property/></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="message message_error">	
				<h3><s:text name="message.title.ActionErrors" /></h3>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
	
			<p class="noscreen">
				<wpsf:hidden name="strutsAction" />
				<wpsf:hidden name="id" />
			</p>
		
			<s:if test="null == contentType || contentType==''">
				<fieldset class="margin-more-top">
					<legend><s:text name="label.info" /></legend>
					<p>
						<label for="rss_chn_contenttype" class="basic-mint-label"><s:text name="contentType" />:</label>
						<s:select list="availableContentTypes" name="contentType" id="rss_chn_contenttype" cssClass="text" />
					</p>
				</fieldset>
				<p>
					<wpsf:submit useTabindexAutoIncrement="true" action="selectContentType" cssClass="button" value="%{getText('label.continue')}" />
				</p>
			</s:if>
			<s:else>
				<fieldset class="margin-more-top">
					<legend><s:text name="label.info" /></legend>
					<p>
						<label for="rss_chn_contenttype" class="basic-mint-label"><s:text name="contentType" />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" name="dummy_contentType" id="dummy_rss_chn_contenttype" cssClass="text" value="%{getSmallContentType(contentType).descr}" disabled="true" />
						<wpsf:hidden name="contentType" />
					</p>	
					<p>
						<label for="rss_chn_title" class="basic-mint-label"><s:text name="title" />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" name="title" id="rss_chn_title" cssClass="text" />
					</p>
					<p>
						<label for="rss_chn_description" class="basic-mint-label"><s:text name="description" />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" name="description" id="rss_chn_description" cssClass="text" />
					</p>
					<p>
						<wpsf:checkbox useTabindexAutoIncrement="true" name="active" id="rss_chn_active" cssClass="radiocheck" />&#32;<label for="rss_chn_active"><s:text name="active" /></label>
					</p>
					<p>
						<label for="rss_chn_feedtype" class="basic-mint-label"><s:text name="feedType" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" list="availableFeedTypes" name="feedType" id="rss_chn_feedtype" cssClass="monospace text" />
					</p>
					<p>
						<label for="rss_chn_category" class="basic-mint-label"><s:text name="category" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" cssClass="text" id="rss_chn_category" headerKey="" headerValue="%{getText('jprss.label.selectCategory')}" 
							list="availableCategories" listKey="code" listValue="%{getShortFullTitle(currentLang.code)}" name="category" />
					</p>
					<p>
						<label for="maxContentsSize" class="basic-mint-label"><s:text name="label.max.items" /></label>
						<wpsf:select useTabindexAutoIncrement="true" name="maxContentsSize" id="maxContentsSize" value="maxContentsSize" 
							headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20,30:30,50:50}" cssClass="text" />
					</p>
				</fieldset>
				<fieldset>
					<legend><s:text name="jprss.label.filters" /></legend>
					<p>
						<label for="filterKey" class="basic-mint-label"><s:text name="label.filter" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="filterKey" id="filterKey" list="allowedFilterTypes" listKey="key" listValue="value" cssClass="text" />
						<wpsf:submit useTabindexAutoIncrement="true" action="setFilterType" value="%{getText('label.add')}" cssClass="button" />
					</p>
					<p class="noscreen">
						<wpsf:hidden name="filters" />
					</p>
					<s:if test="null != filtersProperties && filtersProperties.size()>0" >
						<table class="generic" summary="TODO">
							<caption><span><s:text name="jprss.configured.filters" /></span></caption>
							<tr>			
								<th><abbr title="<s:text name="label.number" />">N</abbr></th>
								<th><s:text name="name.filterDescription" /></th>
								<th><s:text name="label.order" /></th>
								<th class="icon" colspan="3"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
							</tr>
							<s:iterator value="filtersProperties" id="filter" status="rowstatus">	
							<tr>
								<td class="rightText"><s:property value="#rowstatus.index+1"/></td>
								<td>
									<s:text name="label.filterBy" /><strong>
										<s:if test="#filter['key'] == 'created'">
											<s:text name="label.creationDate" />
										</s:if>
										<s:elseif test="#filter['key'] == 'modified'">
											<s:text name="label.lastModifyDate" />			
										</s:elseif>
										<s:else>
											<s:property value="#filter['key']" />
										</s:else>
									</strong><s:if test="(#filter['start'] != null) || (#filter['end'] != null) || (#filter['value'] != null)">,
									<s:if test="#filter['start'] != null">
										<s:text name="label.filterFrom" /><strong>
											<s:if test="#filter['start'] == 'today'">
												<s:text name="label.today" />
											</s:if>
											<s:else>
												<s:property value="#filter['start']" />
											</s:else>
										</strong>
										<s:if test="#filter['startDateDelay'] != null" >
											<s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['startDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />&nbsp;
										</s:if>		
									</s:if>
									<s:if test="#filter['end'] != null">
										<s:text name="label.filterTo" /><strong>
											<s:if test="#filter['end'] == 'today'">
												<s:text name="label.today" />
											</s:if>
											<s:else>
												<s:property value="#filter['end']" />
											</s:else>
										</strong>
										<s:if test="#filter['endDateDelay'] != null" >
											<s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['endDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
										</s:if>
									</s:if>
									<s:if test="#filter['value'] != null">
										<s:text name="label.filterValue" />:<strong> <s:property value="#filter['value']" /></strong> 
											<s:if test="#filter['likeOption'] == 'true'">
												<em>(<s:text name="label.filterValue.isLike" />)</em> 
											</s:if>
									</s:if>
									<s:if test="#filter['valueDateDelay'] != null" >
										<s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['valueDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
									</s:if>
									</s:if>
									<s:if test="#filter['nullValue'] != null" >
										&nbsp;<s:text name="label.filterNoValue" />
									</s:if>
								</td>
								<td>
								<s:if test="#filter['order'] == 'ASC'"><s:text name="label.order.ascendant" /></s:if>
								<s:if test="#filter['order'] == 'DESC'"><s:text name="label.order.descendant" /></s:if>
								</td>
								<td class="icon">
									<wpsa:actionParam action="moveFilter" var="actionName" >
										<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
										<wpsa:actionSubParam name="movement" value="UP" />
									</wpsa:actionParam>
									<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up.png</s:set>		
									<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')}" />
								</td>
								<td class="icon">	
									<wpsa:actionParam action="moveFilter" var="actionName" >
										<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
										<wpsa:actionSubParam name="movement" value="DOWN" />
									</wpsa:actionParam>
									<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down.png</s:set>
									<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')}" />
								</td>
								<td class="icon">	
									<wpsa:actionParam action="removeFilter" var="actionName" >
										<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
									</wpsa:actionParam>
									<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
									<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image"  src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />
								</td>	
							</tr>
							</s:iterator>
						</table>
					</s:if>
					<s:else>
						<p><s:text name="note.filters.none" /></p>		
					</s:else>
					<%--
					<p>
						<wpsf:hidden name="filters" />
						<wpsf:submit useTabindexAutoIncrement="true" action="newFilter" value="%{getText('label.add')}" cssClass="button" />
					</p>
					--%>
				</fieldset>
		
				<p class="centerText">
					<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.save')}" />
				</p>
		</s:else>
	</s:form>

</div>

