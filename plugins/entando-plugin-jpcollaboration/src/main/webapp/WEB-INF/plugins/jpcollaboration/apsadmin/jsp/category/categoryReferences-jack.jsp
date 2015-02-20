<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jpcrowdsourcing-apsadmin-core" %>


<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="margin-none"><s:text name="title.category.ideaReferenced" /></h3>
    </div>
    <div class="panel-body">

        <s:if test="null != references['jpcrowdsourcingIdeaManagerUtilizers']">
            <wpsa:subset source="references['jpcrowdsourcingIdeaManagerUtilizers']" count="10" objectName="contentReferences" advanced="true" offset="5" pagerId="contentManagerReferences">
                <s:set name="group" value="#contentReferences" />

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

                <table class="table table-bordered" id="contentListTable">
                    <tr>
                        <th class="text-center"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
                        <th><s:text name="jpcrowdsourcing.label.title" /></th>
                        <th><s:text name="jpcrowdsourcing.label.author" /></th>
                        <th class="text-center"><s:text name="jpcrowdsourcing.label.date" /></th>
                        <th class="text-right"><s:text name="jpcrowdsourcing.label.comments" /></th>
                        <th class="text-center"><abbr title="<s:text name="jpcrowdsourcing.label.approved" />"><s:text name="jpcrowdsourcing.label.approved.short" /></abbr></th>
                    </tr>
                    <s:iterator var="idea">
                        <tr>
                            <s:url action="edit" var="editAction" namespace="/do/collaboration/Idea" >
                                <s:param name="ideaId" value="#idea.id" />
                            </s:url>
                            <td class="text-center">
                                <s:url action="trashReference" var="trashAction" namespace="/do/collaboration/Idea" escapeAmp="false">
                                    <s:param name="ideaId" value="#idea.id" />
                                    <s:param name="selectedNode" value="#request['selectedNode']" />
                                </s:url>
                                <div class="btn-group btn-group-xs">
                                    <a class="btn btn-default" title="<s:text name="label.edit" />: <s:property value="#idea.title" />" href="<s:property value="#editAction"/>" title="<s:text name="label.edit" />">
                                        <span class="icon fa fa-pencil-square-o"></span>
                                        <span class="sr-only"><s:text name="label.edit" />: <s:property value="#idea.title" /></span>
                                    </a>
                                </div>            
                                <div class="btn-group btn-group-xs">   
                                    <a class="btn btn-warning btn-xs" href="<s:property value="#trashAction" />" title="<s:text name="label.delete" />">
                                        <span class="sr-only"></span>
                                        <span class="icon fa fa-times-circle-o"></span>
                                    </a>
                                </div>
                            </td>
                            <td>
                                <s:property value="#idea.title"/>
                            </td> 
                            <td>
                                <code><s:property value="#idea.username"/></code>
                            </td>
                            <td class="text-center">
                                <code><s:date name="#idea.pubDate" format="dd/MM/yyyy" /></code>
                            </td>
                            <td class="text-right">
                                <s:if test="#idea.comments.size > 0">
                                    <s:url var="commentListAction" action="list" namespace="/do/collaboration/Idea/Comment">
                                        <s:param name="ideaId" value="#idea.id" />
                                    </s:url>
                                    <span class="monospace"><a href="<s:property value="#commentListAction" />" title="<s:text name="jpcrowdsourcing.label.goToComment" />"><s:property value="#idea.comments.size"/></a></span>
                                    </s:if>
                                    <s:else>
                                    <span class="monospace"><s:text name="jpcrowdsourcing.label.zero" /></span>
                                </s:else>		
                            </td>
                            <td class="text-center">
                                <s:if test="#idea.status == 3">
                                    <s:set name="iconImage" id="iconImage"><wp:resourceURL/>icon fa fa-check text-success</s:set>
                                    <s:set name="isOnlineStatus" value="%{getText('label.yes')}" />
                                </s:if>
                                <s:if test="#idea.status == 2">
                                    <s:set name="iconImage" id="iconImage"><wp:resourceURL/>icon fa fa-pause text-warning</s:set>
                                    <s:set name="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
                                </s:if>
                                <s:if test="#idea.status == 1">
                                    <s:set name="iconImage" id="iconImage"><wp:resourceURL/>icon fa fa-pause text-warning</s:set>
                                    <s:set name="isOnlineStatus" value="%{getText('label.no')}" />
                                </s:if>		
                                <span class="<s:property value="iconImage" />" title="<s:property value="isOnlineStatus" />"></span>
                            </td>
                        </tr>
                    </s:iterator>
                </table>

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

            </wpsa:subset>
        </s:if>
        <s:else>
            <div class="alert alert-info"><s:text name="note.category.referencedContents.empty" /></div>
        </s:else>
    </div>        
</div>
