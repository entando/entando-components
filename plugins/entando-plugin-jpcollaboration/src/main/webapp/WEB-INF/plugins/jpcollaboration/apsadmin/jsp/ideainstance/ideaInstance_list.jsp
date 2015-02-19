<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<s:set var="targetNS" value="%{'/do/collaboration/IdeaInstance'}" />
<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.title.ideaInstanceManagement" /></span></h1>

<p>
    <a class="btn btn-default margin-base-bottom" href="<s:url action="new" />">
        <span class="icon fa fa-plus-circle"> 
            <s:text name="label.new" />
        </span>
    </a>
</p>

<div id="main">
	<s:form action="list" cssClass="form-horizontal">
		<s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                            <ul class="margin-base-vertical">
                                <s:iterator value="actionErrors">
                                    <li><s:property escape="false" /></li>
                                </s:iterator>
                            </ul>
                </div>
		</s:if>  
                    
                <div class="form-group">
                    <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <span class="input-group-addon">
                            <span class="icon fa fa-file-text-o fa-lg" 
                                title="<s:text name="label.search.by"/>&#32;<s:text name="label.code" />">
                            </span>
                        </span>
                            <c:set var="pholder"><s:text name="label.code" /></c:set>    
			<wpsf:textfield name="code" id="code" cssClass="form-control input-lg" placeholder="${pholder}"/>
                        <span class="input-group-btn">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
                                <span class="sr-only"><s:text name="%{getText('label.search')}" /></span>
                                <span class="icon fa fa-search"></span>
                            </wpsf:submit>
                                
                            <button type="button" class="btn btn-primary btn-lg dropdown-toggle" 
                                data-toggle="collapse" 
                                data-target="#search-advanced" title="Refine your search">
                                <span class="sr-only"><s:text name="title.searchFilters" /></span>
                                <span class="caret"></span>
                            </button>    
                        </span>
                    </div>

                    <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <div id="search-advanced" class="collapse well collapse-input-group">
                            <div class="form-group">
                                <label for="groupName" class="control-label col-sm-2 text-right"><s:text name="label.group.search" /></label>
                                <div class="col-sm-5">    
                                    <wpsf:select name="groupName" id="groupName" list="systemGroups"
                                        listKey="name" listValue="descr" cssClass="form-control" headerKey="" headerValue="%{getText('label.all')}" />    
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-5 col-sm-offset-2">  
                                    <wpsf:submit type="button" cssClass="btn btn-primary">
                                        <s:text name="%{getText('label.search')}" />
                                        <span class="icon fa fa-search"></span>
                                    </wpsf:submit>
                                </div>
                            </div>   
                        </div>
                    </div>            
                </div>
		</s:form>
		<s:form action="search">
			<div class="subsection-light">
				<p class="noscreen">
					<s:hidden name="groupName" />
				</p>

				<wpsa:subset source="ideaInstancesId" count="10" objectName="groupIdeaInstances" advanced="true" offset="5">
					<s:set name="group" value="#groupIdeaInstances" />

					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>

					<table class="table table-bordered">
                                            <tr>
						<th scope="col" class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
                                                <th scope="col"><s:text name="label.code" /></th>
                                                <th scope="col" class="text-right"><s:text name="label.idea.count" /></th>
						<th scope="col" class="text-center"><s:text name="label.createdat" /></th>
                                            </tr>
                                            <s:iterator var="code">
                                            <s:set name="ideaInstance_var" value="%{getIdeaInstance(#code)}" />
                                            <tr>
                                                <td class="text-center text-nowrap">
                                                    <div class="btn-group btn-group-xs">
                                                        <a class="btn btn-default" 
                                                           title="<s:text name="label.edit" />: <s:property value="#ideaInstance_var.code" />" 
                                                           href="<s:url action="edit"><s:param name="code" value="#ideaInstance_var.code"/></s:url>">
                                                            <span class="icon fa fa-pencil-square-o"></span>
                                                            <span class="sr-only"><s:text name="label.edit" />: <s:property value="#ideaInstance_var.code" /></span>
                                                        </a>
                                                    </div>
                                                    <div class="btn-group btn-group-xs">   
                                                        <a class="btn btn-warning btn-xs" href="<s:url action="trash"><s:param name="code" value="#ideaInstance_var.code"></s:param></s:url>" title="<s:text name="label.remove" />: <s:property value="#ideaInstance_var.code"/>">
                                                            <span class="sr-only"></span>
                                                            <span class="icon fa fa-times-circle-o"></span>
                                                        </a>
                                                    </div>        
                                                </td>
                                                <td class="text-nowrap"><code><s:property value="#ideaInstance_var.code"/></code></td>
						<td class="text-right">
                                                    <s:url action="list" namespace="/do/collaboration/Idea" var="link_to_idea_list">
                                                        <s:param name="ideaInstance" value="#ideaInstance_var.code" />
                                                    </s:url>
                                                    <a href="<s:property value="#link_to_idea_list"/>"><s:property value="#ideaInstance_var.children.size"/></a>
						</td>
                                                <td class="text-center text-nowrap"><code><s:date name="#ideaInstance_var.createdat" format="dd/MM/yyyy"/></code></td>
                                            </tr>
                                            </s:iterator>
					</table>

					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>

				</wpsa:subset>
			</div>
	</s:form>
</div>