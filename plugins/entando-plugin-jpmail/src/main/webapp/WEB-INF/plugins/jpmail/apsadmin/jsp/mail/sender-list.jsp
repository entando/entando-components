<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpmail.admin.menu" /></li>
    <li class="page-title-container"><s:text name="title.eMailManagement.sendersConfig"/></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <h1 class="page-title-container">
                <s:text name="jpmail.admin.menu" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="title.eMailManagement.help"/> data-placement="left" data-original-title="">
                       <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-12 col-md-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url namespace="/do/jpmail/MailConfig" action="viewSenders" />"><s:text name="title.eMailManagement.sendersConfig"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpmail/MailConfig" action="editSmtp" />"><s:text name="jpmail.admin.menu.smtp"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>

<br/>

<div class="mb-20">
    <div class="row">
        <div class="col-sm-12">
            <a href="<s:url action="newSender" />" class="btn btn-primary pull-right" style="margin-bottom: 5px">
                <s:text name="label.add"/>
            </a>
        </div>
    </div>

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:if test="%{senderCodes.size()==0}">
        <p><s:text name="label.senders.none" /></p>
    </s:if>
    <s:else>
        <div class="table-responsive overflow-visible">
            <table class="table table-striped table-bordered table-hover no-mb">
                <thead>
                    <tr>
                        <th class="col-sm-5"><s:text name="code" /></th>
                        <th class="col-sm-6"><s:text name="mail" /></th>
                        <th class="text-center table-w-5"><s:text name="label.actions"/></th>
                    </tr>
                </thead>

                <tbody>
                    <s:iterator value="%{config.senders.entrySet()}" var="sender">
                        <tr>
                            <td><s:property value="#sender.key"/></td>
                            <td><s:property value="#sender.value" /></td>
                            <td class="text-center table-view-pf-actions">
                                <div class="dropdown dropdown-kebab-pf">
                                    <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span class="fa fa-ellipsis-v"></span>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-right">
                                        <li><li>
                                            <a href="<s:url action="editSender" > <s:param name="code" value="#sender.key" /></s:url>"
                                               title="<s:text name="label.edit" />">
                                                <s:text name="label.edit"/>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="<s:url action="trashSender" > <s:param name="code" value="#sender.key" /></s:url>"
                                               title="<s:text name="label.delete" />">
                                                <s:text name="label.delete"/>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </div>
    </s:else>
</div>
