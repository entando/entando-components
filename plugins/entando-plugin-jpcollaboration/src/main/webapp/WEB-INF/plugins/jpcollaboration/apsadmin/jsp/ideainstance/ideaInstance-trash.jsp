<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

<s:set var="targetNS" value="%{'/do/collaboration/IdeaInstance'}" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpcrowdsourcing.admin.title" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />">
            <s:text name="jpcrowdsourcing.title.ideaInstanceManagement" />
        </a>
    </li>
    <li class="page-title-container"><s:text name="label.delete" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpcrowdsourcing.admin.menu"/>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>

<br>

<div class="mb-20">
    <s:form action="delete">
        <p class="noscreen">
            <wpsf:hidden name="strutsAction" />
            <wpsf:hidden name="code" />
        </p>

        <div class="text-center mt-20">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline">
                <s:text name="label.delete" />
            </p>
            <p class="esclamation-underline-text">
                <s:text name="note.deleteIdeaInstance.areYouSure" />
                &#32;
                <em><s:property value="code" /></em>?
            </p>
            <a class="btn btn-default button-fixed-width"
               href="<s:url action="list" />">
                <s:text name="%{getText('label.back')}" />
            </a>
            <wpsf:submit type="button"
                         cssClass="btn btn-danger button-fixed-width">
                <s:text name="%{getText('label.remove')}" />
            </wpsf:submit>
        </div>

        <s:set var="ideaInstance_var" value="%{getIdeaInstance(code)}" />
        <s:if test="#ideaInstance_var.children.size > 0">
            <legend>
                <s:text name="jpcrowdsourcing.label.delete.reference" />
            </legend>
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover no-mb">
                    <thead>
                        <tr>
                            <th>
                                <s:text name="jpcrowdsourcing.label.title" />
                            </th>
                            <th class="table-w-20">
                                <s:text name="jpcrowdsourcing.label.instance" />
                            </th>
                            <th class="table-w-20">
                                <s:text name="jpcrowdsourcing.label.author" />
                            </th>
                            <th class="text-center table-w-10">
                                <s:text name="jpcrowdsourcing.label.date" />
                            </th>
                            <th class="text-center table-w-10">
                                <s:text name="jpcrowdsourcing.label.comments" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="#ideaInstance_var.children" var="ideaId">
                            <tr>
                                <s:set var="idea" value="%{getIdea(#ideaId)}" />
                                <s:url action="edit" namespace="/do/collaboration/Idea" var="editAction">
                                    <s:param name="ideaId" value="#idea.id" />
                                </s:url>
                                <td>
                                    <a href="<s:property value="#editAction"/>"
                                       title="<s:text name="label.edit" />">
                                        <s:property value="#idea.title" />
                                    </a>
                                </td>
                                <td>
                                    <s:url action="list" namespace="/do/collaboration/IdeaInstance"
                                           var="link_to_instancelist">
                                        <s:param name="code" value="#idea.instanceCode" />
                                    </s:url>
                                    <a href="<s:property value="#link_to_instancelist" />">
                                        <s:property value="#idea.instanceCode" />
                                    </a>
                                </td>
                                <td>
                                    <s:property value="#idea.username" />
                                </td>
                                <td class="text-center">
                                    <s:date name="#idea.pubDate" format="dd/MM/yyyy HH:mm" />
                                </td>
                                <td class="text-center">
                                    <s:if test="#idea.comments.size > 0">
                                        <s:url var="commentListAction" action="list"
                                               namespace="/do/collaboration/Idea/Comment">
                                            <s:param name="ideaId" value="#idea.id" />
                                        </s:url>
                                        <span class="monospace">
                                            <a href="<s:property value="#commentListAction" />"
                                               title="<s:text name="jpcrowdsourcing.label.goToComment" />">
                                                <s:property value="#idea.comments.size" />
                                            </a>
                                        </span>
                                    </s:if>
                                    <s:else>
                                        <span class="monospace">
                                            <s:text name="jpcrowdsourcing.label.zero" />
                                        </span>
                                    </s:else>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>
        </s:if>
    </s:form>
</div>
