<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpnewsletter.title.newsletterManagement" />&#32;/&#32;<s:text name="title.contentList" />
	</span>
</h1>

<div id="main" role="main">

<p><s:text name="jpnewsletter.note.intro" /></p>
<p><s:text name="jpnewsletter.note.intro.bis" /></p>

<s:url action="search" var="formAction" namespace="do/jpnewsletter/Newsletter" />
<s:form action="%{'/' + #formAction}" cssClass="form-horizontal" role="search">

<p class="sr-only">
	<wpsf:hidden name="lastGroupBy" />
	<wpsf:hidden name="lastOrder" />
</p>

<div class="form-group">
	<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<span class="input-group-addon">
			<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.description"/>"></span>
		</span>
		<label for="text" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>
		<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
		<span class="input-group-btn">
			<wpsf:submit action="search" type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
				<span class="sr-only"><s:text name="label.search" /></span>
				<span class="icon fa fa-search"></span>
			</wpsf:submit>
			<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
					<span class="sr-only"><s:text name="title.searchFilters" /></span>
					<span class="caret"></span>
			</button>
		</span>
	</div>
	<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div id="search-advanced" class="collapse well collapse-input-group">
			<div class="form-group">
				<label for="contentType" class="control-label col-sm-2 text-right">
					<s:text name="label.type"/>
				</label>
				<div class="col-sm-5 input-group">
					<wpsf:select cssClass="form-control" name="contentType" id="contentType"
						list="contentTypes" listKey="code" listValue="descr"
						headerKey="" headerValue="%{getText('label.all')}" />
				</div>
			</div>
			<div class="form-group">
			<label for="inQueue" class="control-label col-sm-2 text-right"><s:text name="label.state"/>:</label>
			<div class="col-sm-5">
			<select name="inQueue" id="inQueue" class="form-control" tabindex="<wpsa:counter />">
				<option value=""><s:text name="label.all" /></option>
				<option value="1" <s:if test="inQueue==\"1\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.inQueue" /></option>
				<option value="2" <s:if test="inQueue==\"2\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.notInQueue" /></option>
			</select>
			</div>
			</div>
			<div class="form-group">
			<label for="sent" class="control-label col-sm-2 text-right"><s:text name="jpnewsletter.label.sent"/>:</label>
			<div class="col-sm-5">
			<select name="sent" id="sent" class="form-control" tabindex="<wpsa:counter />">
				<option value=""><s:text name="label.all" /></option>
				<option value="1" <s:if test="sent==\"1\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.sent.yes" /></option>
				<option value="2" <s:if test="sent==\"2\"">selected="selected" </s:if>><s:text name="jpnewsletter.label.sent.no" /></option>
			</select>
			</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-5 col-sm-offset-2">
					<wpsf:submit action="search" type="button" cssClass="btn btn-primary">
						<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
					</wpsf:submit>
				</div>
			</div>
		</div>
	</div>
</div>

<hr />

<p class="help-block text-right">
	<button type="button" data-toggle="collapse" data-target="#search-configure-results" class="btn btn-link">
		<s:text name="title.searchResultOptions" />&#32;<span class="icon-chevron-down"></span>
	</button>
</p>
<div id="search-configure-results" class="collapse">
	<div class="form-group col-sm-12">
		<div class="btn-group" data-toggle="buttons">
			<label class="btn btn-default" for="viewCode">
				<wpsf:checkbox name="viewCode" id="viewCode" />&#32;
				<s:text name="label.code" />
			</label>
			<label class="btn btn-default">
				<wpsf:checkbox name="viewTypeDescr" id="viewTypeDescr" />&#32;
				<s:text name="name.contentType" />
			</label>
			<label class="btn btn-default">
				<wpsf:checkbox name="viewGroup" id="viewGroup" />&#32;
				<s:text name="label.group"/>
			</label>
			<label class="btn btn-default">
				<wpsf:checkbox name="viewCreationDate" id="viewCreationDate" />&#32;
				<s:text name="label.creationDate"/>
			</label>
		</div>
	</div>
	<div class="form-group col-sm-12">
		<wpsf:submit action="search" type="button" cssClass="btn btn-primary">
				<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
		</wpsf:submit>
	</div>
</div>

</s:form>

<div class="subsection-light">

<s:form action="search" >
<p class="noscreen">
	<wpsf:hidden name="text" />
	<wpsf:hidden name="contentType" />
	<wpsf:hidden name="inQueue" />
	<wpsf:hidden name="sent" />
	<wpsf:hidden name="viewCode" />
	<wpsf:hidden name="viewTypeDescr" />
	<wpsf:hidden name="viewGroup" />
	<wpsf:hidden name="viewCreationDate" />
	<wpsf:hidden name="lastGroupBy" />
	<wpsf:hidden name="lastOrder" />
</p>

<s:if test="hasActionErrors()">
<div class="alert alert-danger alert-dismissable fade in margin-base-top">
	<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
	<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
	<ul class="margin-base-top">
	<s:iterator value="ActionErrors">
		<li><s:property escape="false" /></li>
	</s:iterator>
	</ul>
</div>
</s:if>
<s:if test="hasActionMessages()">
<div class="alert alert-success alert-dismissable fade in">
	<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
	<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
	<ul class="margin-base-top">
		<s:iterator value="actionMessages">
			<li><s:property escape="false" /></li>
		</s:iterator>
	</ul>
</div>
</s:if>

<s:set var="contentIdsVar" value="contents" />

<wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
<s:set var="group" value="#groupContent" />

<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<s:if test="%{#contentIdsVar.size() > 0}">
	
	<div class="table-responsive">
		<table class="table table-bordered" id="contentListTable">
		<caption class="sr-only"><s:text name="title.contentList" /></caption>
		<tr>
			<th>
			<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
				<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
				<s:param name="lastOrder" ><s:property value="lastOrder" /></s:param>
				<s:param name="groupBy">descr</s:param>
				<s:param name="inQueue"><s:property value="inQueue"/></s:param>
				<s:param name="sent"><s:property value="sent"/></s:param>
				<s:param name="entandoaction:changeOrder">changeOrder</s:param>
				</s:url>"><s:text name="label.description" /></a>
			</th>
		<s:if test="viewCode">
			<th>
			<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
				<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
				<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
				<s:param name="groupBy">code</s:param>
				<s:param name="inQueue"><s:property value="inQueue"/></s:param>
				<s:param name="sent"><s:property value="sent"/></s:param>
				<s:param name="entandoaction:changeOrder">changeOrder</s:param>
				</s:url>"><s:text name="label.code" /></a>
			</th>
		</s:if>
		<s:if test="viewTypeDescr"><th><s:text name="label.type" /></th></s:if>
		<s:if test="viewGroup"><th><s:text name="label.group" /></th></s:if>
		<s:if test="viewCreationDate">
			<th class="text-center">
			<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
				<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
				<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
				<s:param name="groupBy">created</s:param>
				<s:param name="inQueue"><s:property value="inQueue"/></s:param>
				<s:param name="sent"><s:property value="sent"/></s:param>
				<s:param name="entandoaction:changeOrder">changeOrder</s:param>
				</s:url>"><s:text name="label.creationDate" /></a>
			</th>
		</s:if>
			<th class="text-center">
			<a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
				   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
				<s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
				<s:param name="groupBy">lastModified</s:param>
				<s:param name="inQueue"><s:property value="inQueue"/></s:param>
				<s:param name="sent"><s:property value="sent"/></s:param>
				<s:param name="entandoaction:changeOrder">changeOrder</s:param>
				</s:url>"><s:text name="label.lastEdit" /></a>
			</th>
			<th class="text-center"><s:text name="jpnewsletter.label.sendDate" /></th>
			<th class="text-center"><abbr title="<s:text name="label.state" />">S</abbr></th>
			<th class="text-center"><abbr title="<s:text name="jpnewsletter.label.details" />">D</abbr></th>
		</tr>
		<s:iterator var="contentId">
		<s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
		<s:set name="contentReport" value="%{getContentReport(#contentId)}" />
		<tr>
		<td>
			<label>
				<input type="checkbox" name="contentIds" id="content_<s:property value="#content.id" />" value="<s:property value="#content.id" />" />
				<s:property value="#content.descr" />
			</label>
		</td>
		<s:if test="viewCode">
			<td>
				<code><s:property value="#content.id" /></code>
			</td>
		</s:if>
		<s:if test="viewTypeDescr">
			<td>
				<s:property value="%{getSmallContentType(#content.typeCode).description}" />
			</td>
		</s:if>
		<s:if test="viewGroup">
			<td>
				<s:property value="%{getGroup(#content.mainGroupCode).description}" />
			</td>
		</s:if>
		<s:if test="viewCreationDate">
			<td class="text-center text-nowrap">
				<code><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></code>
			</td>
		</s:if>
		<td class="text-center text-nowrap">
			<code><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></code>
		</td>

		<s:if test="#content.onLine && #content.sync">
			<s:set var="iconName">check</s:set>
			<s:set var="textVariant">success</s:set>
			<s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
		</s:if>
		<s:if test="#content.onLine && !(#content.sync)">
			<s:set var="iconName">adjust</s:set>
			<s:set var="textVariant">info</s:set>
			<s:set var="isOnlineStatus" value="%{getText('label.yes') + ', ' + getText('note.notSynched')}" />
		</s:if>
		<s:if test="!(#content.onLine)">
			<s:set var="iconName">pause</s:set>
			<s:set var="textVariant">warning</s:set>
			<s:set var="isOnlineStatus" value="%{getText('label.no')}" />
		</s:if>
		<td class="text-center">
		<s:if test="%{#contentReport!=null}"><span class="monospace"><s:date name="#contentReport.sendDate" format="dd/MM/yyyy HH:mm" /></span></s:if>
		<s:else><abbr title="<s:text name="neverSentNewsletter" />">-</abbr></s:else>
		</td>
		<s:if test="isContentInQueue(#contentId)">
			<s:set var="iconName">adjust</s:set>
			<s:set var="textVariant">info</s:set>
			<s:set var="statusVar" value="%{getText('jpnewsletter.label.inQueue')}" />
		</s:if>
		<s:else>
			<s:set var="iconName">pause</s:set>
			<s:set var="textVariant">warning</s:set>
			<s:set var="statusVar" value="%{getText('jpnewsletter.label.notInQueue')}" />
		</s:else>
		<td class="text-center">
			<span class="icon fa fa-<s:property value="#iconName" /> text-<s:property value="#textVariant" />" title="<s:property value="#statusVar" />"></span>
			<span class="sr-only"><s:property value="#statusVar" /></span>
		</td>
		<td class="text-center">
		<a href="<s:url action="entry" ><s:param name="contentId" value="#content.id" /></s:url>" title="<s:text name="jpnewsletter.label.detailOf" />: <s:property value="#content.descr" />">
			<img src="<wp:resourceURL />plugins/jpnewsletter/administration/img/icons/system-search.png" alt="<s:text name="label.view" />" />
		</a>
		</td>
		</tr>
		</s:iterator>
		</table>
	</div>
		
<div class="row margin-none margin-large-top">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<wpsf:submit action="addToQueue" type="button" cssClass="btn btn-success btn-block" title="%{getText('jpnewsletter.label.insertInQueue')}" >
			<span class="icon fa fa-check"></span>&#32;
			<s:text name="jpnewsletter.label.insertInQueue" />
		</wpsf:submit>
	</div>
</div>

</s:if>

<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</s:form>
</div>


</div>