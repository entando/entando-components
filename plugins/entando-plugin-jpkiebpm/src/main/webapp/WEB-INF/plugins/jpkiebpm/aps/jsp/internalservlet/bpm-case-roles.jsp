<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%
String cId = java.util.UUID.randomUUID().toString();
%>


<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<s:if test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>

<s:if test="#request['angular']==null">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
    <s:set var="angular" value="true" scope="request"/>
</s:if>

<s:if test="#request['bpmRoles']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-roles.js"></script>
    <s:set var="bpmRoles" value="true" scope="request"/>
</s:if>

<div  class="ibox float-e-margins"  id="<%=cId%>" ng-controller="RolesController as vm">
    <div class="ibox-title">
        <h5>Roles list</h5>
        <div class="ibox-tools">
            <a class="collapse-link"> 
                <i class="fa fa-chevron-up"></i> 
            </a> 
            <a class="close-link"> <i class="fa fa-times"></i> </a> 
        </div>
    </div>
    <s:if test="%{null != roles}">
    <div class="ibox-content">
        <div class="row">
            <div class="col-sm-6 m-b-xs">
                <div data-toggle="buttons" class="btn-group">
                    <label class="btn btn-sm btn-white" ng-class="{'active':vm.ui.rolesFilter == 'all'}" ng-click="vm.ui.setFilter('all')">
                        All </label>
                    <label class="btn btn-sm btn-white" ng-class="{'active':vm.ui.rolesFilter == 'unassigned'}" ng-click="vm.ui.setFilter('unassigned')">
                        Unassigned </label>
                    <label class="btn btn-sm btn-white" ng-class="{'active':vm.ui.rolesFilter == 'assigned'}" ng-click="vm.ui.setFilter('assigned')" >
                        Assigned </label>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="input-group">
                    <input type="text" placeholder="Search" class="input-sm form-control" ng-model="vm.ui.searchText">
                    <span  class="input-group-btn"> <button class="btn btn-primary btn-sm" type="button"><i class="fa fa-search"></i></button></span>
                </div>
            </div>
        </div>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Role</th>
                        <th>User</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="role in vm.mod.roles| rolesFilter:vm.ui.rolesFilter | filter:vm.ui.searchText">
                        <td>{{role.name}}
                        </td>
                        <td>
                            <span ng-repeat="user in role.users">
                                <i class="fa fa-user"></i> {{user}}
                            </span>
                            <span ng-repeat="group in role.groups">
                                <i class="fa fa-users"></i> {{group}}
                            </span>
                        </td>
                        <td>
                            <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceRoles/addRole.action"/>" method="post" class="form-horizontal" >
                                <s:if test="casePath != null">
                                    <s:hidden name="casePath" escapeHtml="false" escapeJavaScript="false"/>
                                </s:if>
                                <s:if test="knowledgeSourceId != null">
                                    <s:hidden name="knowledgeSourceId" escapeHtml="false" escapeJavaScript="false"/>
                                </s:if>
                                <s:if test="containerid != null">
                                    <s:hidden name="containerid" escapeHtml="false" escapeJavaScript="false"/>
                                </s:if>
                                <s:if test="channelPath != null">
                                    <s:hidden name="channelPath" escapeHtml="false" escapeJavaScript="false"/>
                                </s:if>

                                <s:hidden name="caseRoleName" escapeHtml="false" escapeJavaScript="false" value="{{::role.name}}"/>

                                <div class="form-group" ng-show="role.edit">
                                    <label><i class="fa fa-user"></i> User
                                        <s:textfield name="user" cssClass="form-control" value="{{vm.ui.getUser(role)}}"/>

                                </div>
                                <div class="form-group" ng-show="role.edit">
                                    <label><i class="fa fa-users"></i> Group
                                        <s:textfield name="group" cssClass="form-control" value="{{vm.ui.getGroup(role)}}"/>
                                    </label>
                                </div>
                                <button type="button" class="btn btn-primary btn-sm" ng-click="role.edit = true" ng-hide="role.edit">Edit</button>
                                <wpsf:submit type="button" action="deleteRole" name="deleteRole" value="Remove Assignments" ng-hide="role.edit" cssClass="btn btn-default btn-sm" ng-disabled="role.users.length===0 && role.groups.length===0"/>

                                <button type="button" class="btn btn-primary btn-sm" ng-click="role.edit = false" ng-show="role.edit">Cancel</button>
                                <wpsf:submit type="button" action="addRole" name="addRole" value="Save" cssClass="btn btn-primary btn-sm" ng-show="role.edit" />

                            </form>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    </s:if>
</div>

<script type="text/javascript">

    (function () {
    <s:if test="roles != null">
        bootBpmRolesComponents('<%=cId%>',<s:property value="roles" escapeHtml="false" escapeJavaScript="false"/>);
        angular.element(document).ready(function () {
            angular.bootstrap(document.getElementById('<%=cId%>'), ['<%=cId%>']);
        });
    </s:if>
    })();
</script>