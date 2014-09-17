<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpversioning.admin.menu" />&#32;/&#32;<s:text name="title.jpversioning.resources.images" />
    </span>
</h1>
<div id="main">
	<s:form action="search" cssClass="form-horizontal">
		<p class="sr-only">
			<wpsf:hidden name="resourceTypeCode" />
		</p>
		<div class="form-group">
			<div class="input-group col-sm-12">
				<span class="input-group-addon">
					<span class="icon fa fa-file-text-o fa-lg"></span>
				</span>
				<label for="text" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>
				<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
				<div class="input-group-btn">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
						<span class="icon fa fa-search"></span>
						<span class="sr-only"><s:text name="label.search" /></span>
					</wpsf:submit>
				</div>
			</div>
		</div>
	</s:form>
	<div class="subsection-light">
		<s:form action="search">
			<p class="sr-only">
				<wpsf:hidden name="text" />
				<wpsf:hidden name="resourceTypeCode" />
			</p>
		
			<wpsa:subset source="trashedResources" count="10" objectName="groupResource" advanced="true" offset="5">
				<s:set var="group" value="#groupResource" />
				
				<div class="pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>

				<div class="row">
				
				<s:iterator var="resourceid">
					<s:set var="resource" value="%{getTrashedResource(#resourceid)}" />
					<div class="col-sm-4 col-md-3">
						<div class="panel panel-default text-center">

							
						<s:set var="url0" >
						<s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
							<s:param name="resourceId" value="#resourceid"/>
							<s:param name="size" value="0"/>
							<s:param name="langCode" value=""/>
						</s:url>
						</s:set>
						
						<s:set var="url1" >
						<s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
							<s:param name="resourceId" value="#resourceid"/>
							<s:param name="size" value="1"/>
							<s:param name="langCode" value=""/>
						</s:url>
						</s:set>	

						<div class="panel-heading text-right padding-small-vertical padding-small-right">
							<a href="<s:url action="remove" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" class="icon fa fa-times-circle text-warning">
								<span class="sr-only">Delete</span>
							</a>
						</div>

						<div>
							<%-- Dimension forced for img thumbnail --%>
							<img src="<c:out value="${url1}" escapeXml="false"/>" alt=" " style="height:90px;max-width:130px" class="margin-small-top" />
						</div>

						<div class="btn-group margin-small-vertical">
							<a href="<s:url action="restore" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="jpversioning.label.restore" />"
								 class="btn btn-default"
								 title="<s:text name="jpversioning.label.restore" />: <s:property value="#resource.descr" />">
								<span class="icon fa fa-reply"></span>&#32;
								<s:text name="jpversioning.label.restore" />
							</a>

							<button type="button" class="btn btn-info" data-toggle="popover">
								<span class="icon fa fa-info"></span>
								<span class="sr-only"><s:text name="label.info" /></span>
							</button>
						</div>

						<s:set var="fileInfo">
							<s:property value="#resource.descr" />
						</s:set>

						<script>
							$("[data-toggle=popover]").popover({
								html: true,
								placement: "top",
								content: '<s:property value="fileInfo" escape="false" />'
							});
						</script>

						</div>
					</div>
					
				</s:iterator>
				</div>

				<div class="pager clear">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
				
			</wpsa:subset>
		</s:form>
	</div>
</div>