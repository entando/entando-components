<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<s:set var="targetNS" value="%{'/do/collaboration/IdeaInstance'}" />
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block"><s:text name="jpcrowdsourcing.title.ideaInstanceManagement" />
	<s:if test="strutsAction ==  1">
            &#32;/&#32;<s:text name="jpcrowdsourcing.title.ideaInstance.add" />
	</s:if>
        <s:if test="strutsAction ==  2">
            &#32;/&#32;<s:text name="jpcrowdsourcing.title.ideaInstance.edit" />
	</s:if>    
    </span>
</h1>
<div id="main">

	<s:form action="save" cssClass="form-horizontal">
		<s:if test="hasFieldErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                            <ul class="margin-base-vertical">
                                <s:iterator value="fieldErrors">
                                    <s:iterator value="value">
                                        <li><s:property/></li>
                                    </s:iterator>
                                </s:iterator>
                            </ul>
                </div>
		</s:if>
		<s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                            <ul class="margin-base-vertical">
                                <s:iterator value="actionErrors">
                                    <li><s:property/></li>
                                </s:iterator>
				</ul>
                </div>
		</s:if>

			<p class="noscreen">
				<wpsf:hidden name="strutsAction" />
				<%--<wpsf:hidden name="code" />--%>
			</p>

                        <div class="form-group">
                            <div class="col-xs-12">
				<label for="ideaInstance_code"><s:text name="label.code" /></label>
				<s:if test="strutsAction == 1"></s:if>
				<wpsf:textfield name="code" id="ideaInstance_code" cssClass="form-control" readonly="strutsAction != 1" />
                            
                            </div>			</div>
			<s:if test="strutsAction == 2">
                        <div class="form-group">
                            <div class="col-xs-12">
                                <label for="createdat"><s:text name="label.createdat" />:</label>&#32;
                                <code><s:date name="createdat" format="dd/MM/yyyy" /></code>
				<wpsf:hidden name="createdat" />
                            </div>
                        </div>    
			</s:if>

                        <div class="form-group">
                            <div class="col-xs-12">
				<label for="groupName"><s:text name="label.join" />&#32;<s:text name="label.group" /></label>
				<div class="input-group">
                                    <wpsf:select name="groupName" id="groupName" list="systemGroups"
					listKey="name" listValue="descr" cssClass="form-control" />
                                    <span class="input-group-btn">
                                        <wpsf:submit action="joinGroup" type="button" cssClass="btn btn-default">
                                            <span class="icon fa fa-plus"></span>&#32;
                                            <s:text name="%{getText('label.join')}"/>
                                        </wpsf:submit>
                                    </span>
                                </div>   
                            </div>     
			</div>
                        <div class="form-group">
                            <div class="col-xs-12">            
                                <s:if test="null == groups || groups.size==0">
                                    <s:text name="jpcrowdsourcing.ideaInstance.label.noGroups" />
                                </s:if>
                                <s:else>
				<s:iterator value="groups" var="group_name">
					<p class="noscreen">
						<wpsf:hidden name="groups" value="%{#group_name}"  />
					</p>
                                        <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                            <abbr title="%{getSystemGroup(#group_name).getDescr()}">
                                                <s:property value="%{getSystemGroup(#group_name).getDescr()}"/>
                                            </abbr>&#32;
                                            <wpsa:actionParam action="removeGroup" var="actionName" >
                                                <wpsa:actionSubParam name="groupName" value="%{#group_name}" />
                                            </wpsa:actionParam>
                                            <wpsf:submit type="button" cssClass="btn btn-default btn-xs badge" action="%{#actionName}"  value="%{getText('label.remove')}" title="%{getText('label.remove')}">
                                                <span class="icon fa fa-times"></span>
                                                <span class="sr-only">x</span>
                                            </wpsf:submit>    
                                        
                                        </span>    
				</s:iterator>
                                </s:else>
                            </div>
                        </div>    
        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                    <span class="icon fa fa-floppy-o"></span>&#32;
                    <s:text name="%{getText('label.save')}"/>
                </wpsf:submit>
            </div>
        </div>

	</s:form>
</div>
