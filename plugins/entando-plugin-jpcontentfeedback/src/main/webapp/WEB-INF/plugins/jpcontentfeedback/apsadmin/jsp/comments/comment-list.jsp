<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpcontentfeedback.title.comment.list"/></li>
    <li class="page-title-container"><s:text name="jpcontentfeedback.admin.menu.contentFeedback"/></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="jpcontentfeedback.title.comment.list"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpcontentfeedback.title.commentsManager.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>

                </span>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <li class="active">
                        <a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />">
                            <s:text name="jpcontentfeedback.admin.menu.contentFeedback"/>
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="edit" namespace="/do/jpcontentfeedback/Config" />">
                            <s:text name="jpcontentfeedback.admin.menu.contentFeedback.edit"/>
                        </a>
                    </li>
                </ul>
            </div>
        </wp:ifauthorized>
    </div>
</div>
<br>


<div id="main">

    <s:form action="search" cssClass="form-horizontal">
        <s:if test="hasActionMessages()">
            <div class="alert alert-info alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="messages.confirm"/></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionMessages">
                        <li><s:property escapeHtml="false"/></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors"/></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false"/></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors"/></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property/></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <div class="form-group">
            <div class="col-xs-12  ">
                <div class="well col-md-offset-3 col-md-6  ">
                    <p class="search-label"><s:text name="jpcontentfeedback.label.search"/></p>
                    <s:form action="search" class="search-pf has-button " cssClass="form-horizontal">
                        <div class="form-group">

                            <label class="col-sm-2 control-label" for="text" class="sr-only">
                                <s:text name="jpcontentfeedback.label.search.by"/>
                            </label>
                            <div class="col-sm-9">
                                <s:set var="allStatus" value="%{getAllStatus()}"/>
                                <wpsf:select cssClass="form-control " list="#allStatus" name="status" id="status"  listKey="key" listValue="value" headerKey="" headerValue="%{getText('label.all')}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2"></label>
                            <div class="col-sm-9">
                                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                                    <s:text name="label.search"/>
                                </wpsf:submit>
                            </div>
                        </div>
                    </s:form>
                </div>
            </div>
            <br>
            <br>
            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div id="search-advanced" class="collapse well collapse-input-group">
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right" for="comment" class="">
                            <s:text name="jpcontentfeedback.comment"/>
                        </label>
                        <div class="col-sm-5" id="content_list-changeContentType">
                            <wpsf:textfield name="text" id="comment" cssClass="form-control"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="author" class="control-label col-sm-2 text-right">
                            <s:text name="jpcontentfeedback.author"/>
                        </label>
                        <div class="col-sm-5" id="content_list-changeContentType">
                            <wpsf:textfield name="author" id="author" cssClass="form-control"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="from_cal" class="control-label col-sm-2 text-right">
                            <s:text name="jpcontentfeedback.date.from"/>
                        </label>
                        <div class="col-sm-5" id="content_list-changeContentType">
                            <wpsf:textfield name="from" id="from_cal" value="%{from}"
                                            cssClass="form-control datepicker"/>
                            <span class="help-block">dd/MM/yyyy</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="to_cal" class="control-label col-sm-2 text-right">
                            <s:text name="jpcontentfeedback.date.to"/></label>
                        <div class="col-sm-5" id="content_list-changeContentType">
                            <wpsf:textfield name="to" id="to_cal" value="%{to}" cssClass="form-control datepicker"/>
                            <span class="help-block">dd/MM/yyyy</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-5 col-sm-offset-2">
                            <wpsf:submit type="button" cssClass="btn btn-primary">
                                <s:text name="%{getText('label.search')}"/>
                                <span class="icon fa fa-search"></span>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="subsection-light">

            <wpsa:subset source="commentIds" count="10" objectName="groupComment" advanced="true" offset="5">
                <s:set var="group" value="#groupComment"/>
                <s:set var="lista" value="commentIds"/>
                <s:if test="!#lista.empty">
                    <table class="table table-striped table-bordered table-hover no-mb" id="commentTable">
                        <tr>
                            <th><s:text name="jpcontentfeedback.author"/></th>
                            <th class="text-center"><s:text name="jpcontentfeedback.date.creation"/></th>
                            <th><s:text name="jpcontentfeedback.status"/></th>
                            <th class="text-center table-w-5"><s:text name="label.actions"/></th>
                        </tr>
                        <s:iterator var="commentoId">
                            <tr>
                                <s:set var="commento" value="%{getComment(#commentoId)}"/>
                                <td><s:property value="#commento.username"/></td>
                                <td class="text-center"><s:date name="#commento.creationDate"
                                        format="dd/MM/yyyy HH:mm"/></td>
                                <td><s:text name="%{'jpcontentfeedback.label.' + #commento.status}"/></td>
                                <td class="table-view-pf-actions text-center">
                                    <div class="dropdown dropdown-kebab-pf">
                                        <p class="sr-only"><s:text name="label.actions"/></p>
                                        <span class="btn btn-menu-right dropdown-toggle" type="button"
                                              data-toggle="dropdown" aria-haspopup="true"
                                              aria-expanded="false">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </span>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <li>
                                                <a href="<s:url action="view"><s:param name="selectedComment" value="#commentoId" /></s:url>"
                                                   title="<s:text name="label.edit" />:&#32;<s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" />">
                                                    <s:text name="label.edit"/>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="<s:url action="trash"><s:param name="selectedComment" value="#commentoId" /></s:url>"
                                                   title="<s:text name="label.remove" />:&#32;<s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" />">
                                                    <s:text name="label.remove"/>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                    <div class="content-view-pf-pagination clearfix">
                        <div class="form-group">
                            <span>
                                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
                            </span>
                            <div class="mt-5">
                                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp"/>
                            </div>
                        </div>
                    </div>
                </s:if>
                <s:else><p><s:text name="jpcontentfeedback.note.list.empty"/></p></s:else>
                </div>

                <div class="pager">
                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp"/>
            </div>

        </wpsa:subset>
    </s:form>
</div>
