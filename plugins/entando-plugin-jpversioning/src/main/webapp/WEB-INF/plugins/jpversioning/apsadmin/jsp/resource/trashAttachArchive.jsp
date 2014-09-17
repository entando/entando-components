<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpversioning.admin.menu" />&#32;/&#32;<s:text name="title.jpversioning.resources.attaches" />
    </span>
</h1>
<div id="main">
	
	<s:form action="search" cssClass="form-horizontal">
		<p class="sr-only"><wpsf:hidden name="resourceTypeCode" /></p>
		
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
				<ul>
				<s:iterator var="resourceid">
					<li class="list-group-item" >
					<s:set var="resource" value="%{getTrashedResource(#resourceid)}"></s:set>
					<s:set var="resourceInstance" value="%{#resource.getInstance()}"></s:set>
					
					<s:set var="url" >
						<s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
							<s:param name="resourceId" value="#resourceid"/>
							<s:param name="size" value="0"/>
							<s:param name="langCode" value=""/>
						</s:url>
					</s:set>

					<div class="row">
					<div class="col-lg-12">

						<div class="text-right no-margin">
							<a href="<s:url action="remove" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="label.remove" />: <s:property value="#resource.descr" /> "><span class="icon fa fa-times-circle text-warning"></span></a>
						</div>
						<a href="<s:url action="restore" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="jpversioning.label.restore" />" ><span class="icon fa fa-reply"></span></a>

						<s:set var="fileDescr" value="#resource.descr" />
						<s:if test='%{#fileDescr.length()>90}'>
							<s:set var="fileDescr" value='%{#fileDescr.substring(0,30)+"..."+#fileDescr.substring(#fileDescr.length()-30)}' />
							<abbr title="<s:property value="#resource.descr" />">
							<s:property value="#fileDescr" /></abbr>
						</s:if>
						<s:else>
							<s:property value="#resource.descr" />
						</s:else>
						<s:set var="fileName" value="#resourceInstance.fileName" />
						<a href="<c:out value="${url}" escapeXml="false"/>" title="<s:text name="label.download" />">
						<s:if test='%{#fileName.length()>25}'>
							<s:set var="fileName" value='%{#fileName.substring(0,10)+"..."+#fileName.substring(#fileName.length()-10)}' />
							<code class="margin-small-bottom"><abbr title="<s:property value="#resourceInstance.fileName" />"><s:property value="#fileName" /></abbr></code>
						</s:if>
						<s:else>
							<code><s:property value="#fileName" /></code>
						</s:else>
						</a>
						<span class="badge">
						<s:property value="%{#resourceInstance.fileLength.replaceAll(' ', '&nbsp;')}"  escapeXml="false" escapeHtml="false" escapeJavaScript="false" />
						</span>
					</div>
					</div>
					</li>
				</s:iterator>
				</ul>
				<div class="pager clear">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
			</wpsa:subset>
			
		</s:form>
	</div>
</div>