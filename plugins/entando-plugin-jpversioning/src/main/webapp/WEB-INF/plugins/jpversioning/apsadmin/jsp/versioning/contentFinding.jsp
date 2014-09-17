<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpversioning.admin.menu" />&#32;/&#32;<s:text name="title.jpversioning.content" />
    </span>
</h1>
<div id="main">
<s:form class="form-horizontal" action="search">
  	<div class="form-group">
  	    <label for="text" class="sr-only">
  	      <s:text name="label.search.by"/>&#32;<s:text name="label.description"/>
  	    </label>
  	    <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
  	    	<span class="input-group-addon">
  	        <!-- icon -->
  	        <span class="icon fa fa-file-text-o fa-lg"
  	          title="<s:text name="label.search.by"/>&#32;<s:text name="label.description"/>">
  	        </span>
  	        </span>

  	        <wpsf:textfield cssClass="form-control input-lg" name="descr" id="descr" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}"  />

  	        <!-- the search buttons -->
  	        <span class="input-group-btn">
  	          <!-- the main button -->
  	          <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="Search" >
  	            <span class="sr-only"><s:text name="label.search" /></span>
  	            <span class="icon fa fa-search"></span>
  	          </wpsf:submit>
  	          <!-- the collapsible toggler -->
  	          <button type="button" class="btn btn-primary btn-lg dropdown-toggle"
  	            data-toggle="collapse"
  	            data-target="#COLLAPSIBLE-CONTAINER-ID" title="Refine your search">
  	              <span class="sr-only"><s:text name="title.searchFilters" /></span>
  	              <span class="caret"></span>
  	          </button>
  	        </span>
  	      </span><!-- end input-group-addon -->
  	  	</div><!-- input-group -->

  	  	<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
    	    <div id="COLLAPSIBLE-CONTAINER-ID" class="collapse well collapse-input-group">
    	    	<div class="form-group">
    			       <label for="contentType" class="control-label col-sm-2 text-right"><s:text name="label.type"/></label>
    			          <div class="col-sm-5 input-group">
                  			<wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" ></wpsf:select>
    			          </div>
            </div>
    	    </div>
  	    </div>
  	</div>
		<div class="subsection-light">
			<s:if test="%{null == latestVersions || latestVersions.size == 0 }">
				<div class="alert alert-info">
					<s:text name="message.jpversioning.noModifiedContents"></s:text>
				</div>
			</s:if>

			<s:else>
				<wpsa:subset source="latestVersions" count="10" objectName="groupContent" advanced="true" offset="5">
					<s:set var="group" value="#groupContent" />

					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
    		<div class="table-responsive">
    			<table class="table table-bordered">
    						<caption class="sr-only"><span><s:text name="title.jpversioning.contentList" /></span></caption>
    						<tr>
    							<th><s:text name="jpversioning.label.description" /></th>
    							<th class="text-center"><s:text name="jpversioning.label.id" /></th>
    							<th><s:text name="jpversioning.label.contentType" /></th>
    							<th><s:text name="jpversioning.label.username" /></th>
    							<th class="text-center"><s:text name="jpversioning.label.lastVersion" /></th>
                  <th class="text-center">
                    <abbr title="<s:text name="name.onLine" />">P</abbr>
                  </th>
                </tr>
    						<s:iterator var="id">
    							<s:set var="contentVersion" value="%{getContentVersion(#id)}" />
    							<tr>
    								<td>
    									<a href="<s:url action="history" ><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="versionId" value="#contentVersion.id" /></s:url>" title="<s:text name="jpversioning.label.detailOf" />&#32;<s:property value="#contentVersion.descr" />"><s:property value="#contentVersion.descr" /></a>
    								</td>
    								<td class="text-center text-nowrap"><code><s:property value="#contentVersion.contentId" /></code></td>
    								<td><s:property value="%{getSmallContentType(#contentVersion.contentType).descr}" /></td>
    								<td><s:property value="#contentVersion.username" /></td>
    								<td class="text-center text-nowrap"><code><s:property value="#contentVersion.version" />&#32;(<s:date name="#contentVersion.versionDate" format="dd/MM/yyyy" />)</code></td>

                    <s:if test="(#contentVersion.status == 'PUBLIC')">
                      <s:set var="iconName">check</s:set>
                      <s:set var="textVariant">success</s:set>
                      <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                    </s:if>
                    <s:if test="(#contentVersion.status != 'PUBLIC')">
                      <s:set var="iconName">pause</s:set>
                      <s:set var="textVariant">warning</s:set>
                      <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                    </s:if>
                    <td class="text-center">
                      <span class="icon fa fa-<s:property value="iconName" /> text-<s:property value="textVariant" />" title="<s:property value="isOnlineStatus" />"></span>
                      <span class="sr-only"><s:property value="isOnlineStatus" /></span>
                    </td>
    							</tr>
    						</s:iterator>
    				</table>
    			</div>
    			<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				</wpsa:subset>
			</s:else>
		</div>
	</s:form>
</div>
