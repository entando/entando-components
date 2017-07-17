<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpnewsletter.integrations"/></li>
    <li><s:text name="jpnewsletter.components"/></li>
    <li><s:text name="jpnewsletter.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="title.contentList"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpnewsletter.admin.menu"/>
                <span class="pull-right">
                    <s:text name="jpnewsletter.section.help" var="helpVar"/>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpnewsletter.section.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />">
                        <s:text name="title.contentList"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />">
                        <s:text name="jpnewsletter.admin.queue"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Subscriber" />">
                        <s:text name="jpnewsletter.admin.subscribersList"/>
                    </a>
                </li>
                <wp:ifauthorized permission="jpnewsletter_config">
                    <li>
                        <a href="<s:url action="edit" namespace="/do/jpnewsletter/Newsletter/Config" />">
                            <s:text name="jpnewsletter.admin.config"/>
                        </a>
                    </li>
                </wp:ifauthorized>
            </ul>
        </div>
    </div>
</div>
<br>


<div class="mb-20">

    <div class="alert alert-info">
        <span class="pficon pficon-info"></span>
        <p><s:text name="jpnewsletter.note.intro"/></p>
        <p><s:text name="jpnewsletter.note.intro.bis"/></p>
    </div>

    <!-- Search Form -->
    <s:url action="search" var="formAction" namespace="do/jpnewsletter/Newsletter"/>
    <s:form action="%{'/' + #formAction}" cssClass="form-horizontal" role="search">
        <p class="sr-only">
            <wpsf:hidden name="lastGroupBy"/>
            <wpsf:hidden name="lastOrder"/>
        </p>

        <div class="searchPanel form-group">
            <div class="well col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-10">
                <p class="search-label">
                    <s:text name="label.search"/>
                </p>

                <div class="form-group">
                    <label class="control-label col-sm-2" for="text">
                        <s:text name="label.description"/>
                    </label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="text" id="text" cssClass="form-control"
                                        placeholder="%{getText('label.description')}"
                                        title="%{getText('label.search.by')} %{getText('label.description')}"/>
                    </div>
                </div>

                <div class="panel-group" id="accordion-markup">
                    <div class="panel panel-default">
                        <div class="panel-heading pb-10">
                            <p class="panel-title active text-right">
                                <a data-toggle="collapse" data-parent="#accordion-markup"
                                   href="#collapseContentsSearch">
                                    <s:text name="label.search.advanced"/>
                                </a>
                            </p>
                        </div>
                        <div id="collapseContentsSearch" class="panel-collapse collapse form-horizontal">
                            <div class="panel-body">
                                <div class="form-group">
                                    <label for="contentType" class="control-label col-sm-2">
                                        <s:text name="label.type"/>
                                    </label>
                                    <div class="col-sm-9">
                                        <wpsf:select cssClass="form-control" name="contentType" id="contentType"
                                                     list="contentTypes" listKey="code" listValue="descr"
                                                     headerKey="" headerValue="%{getText('label.all')}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="inQueue" class="control-label col-sm-2 "><s:text
                                            name="label.state"/>:</label>
                                    <div class="col-sm-9">
                                        <select name="inQueue" id="inQueue" class="form-control"
                                                tabindex="<wpsa:counter />">
                                            <option value=""><s:text name="label.all"/></option>
                                            <option value="1" <s:if test="inQueue==\"1\"">selected="selected" </s:if>>
                                                <s:text
                                                    name="jpnewsletter.label.inQueue"/></option>
                                            <option value="2" <s:if test="inQueue==\"2\"">selected="selected" </s:if>>
                                                <s:text
                                                    name="jpnewsletter.label.notInQueue"/></option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="sent" class="control-label col-sm-2"><s:text
                                            name="jpnewsletter.label.sent"/>:</label>
                                    <div class="col-sm-9">
                                        <select name="sent" id="sent" class="form-control" tabindex="<wpsa:counter />">
                                            <option value=""><s:text name="label.all"/></option>
                                            <option value="1" <s:if test="sent==\"1\"">selected="selected" </s:if>>
                                                <s:text
                                                    name="jpnewsletter.label.sent.yes"/></option>
                                            <option value="2" <s:if test="sent==\"2\"">selected="selected" </s:if>>
                                                <s:text
                                                    name="jpnewsletter.label.sent.no"/></option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-12">
                    <div class="form-group">
                        <wpsf:submit action="search" type="button" cssClass="btn btn-primary pull-right"
                                     title="%{getText('label.search')}">
                            <s:text name="label.search"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>
    </s:form>

    <!-- Result list -->
    <div class="subsection-light">

        <s:form action="search">
            <p class="noscreen">
                <wpsf:hidden name="text"/>
                <wpsf:hidden name="contentType"/>
                <wpsf:hidden name="inQueue"/>
                <wpsf:hidden name="sent"/>
                <wpsf:hidden name="viewCode"/>
                <wpsf:hidden name="viewTypeDescr"/>
                <wpsf:hidden name="viewGroup"/>
                <wpsf:hidden name="viewCreationDate"/>
                <wpsf:hidden name="lastGroupBy"/>
                <wpsf:hidden name="lastOrder"/>
            </p>

            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>

            <s:set var="contentIdsVar" value="contents"/>
            <wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
                <s:set var="group" value="#groupContent"/>

                <s:if test="%{#contentIdsVar.size() > 0}">
                    <div class="col-xs-12 no-padding">
                        <table class="table table-striped table-bordered table-hover content-list no-mb" id="contentListTable">
                            <thead>
                                <tr>
                                    <th class="text-center">
                                        <label class="sr-only" for="selectAll"><s:text name="label.selectAll" /></label>
                                        <input type="checkbox" class="js_selectAll">
                                    </th>
                                    <th>
                                        <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                                               <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                               <s:param name="lastOrder" ><s:property value="lastOrder" /></s:param>
                                               <s:param name="groupBy">descr</s:param>
                                               <s:param name="inQueue"><s:property value="inQueue"/></s:param>
                                               <s:param name="sent"><s:property value="sent"/></s:param>
                                               <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                           </s:url>"><s:text name="label.description"/></a>
                                    </th>
                                    <th>
                                        <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                                               <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                               <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                               <s:param name="groupBy">code</s:param>
                                               <s:param name="inQueue"><s:property value="inQueue"/></s:param>
                                               <s:param name="sent"><s:property value="sent"/></s:param>
                                               <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                           </s:url>"><s:text name="label.code"/></a>
                                    </th>
                                    <th><s:text name="label.type"/></th>
                                    <th><s:text name="label.group"/></th>
                                    <th class="text-center">
                                        <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                                               <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                               <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                               <s:param name="groupBy">created</s:param>
                                               <s:param name="inQueue"><s:property value="inQueue"/></s:param>
                                               <s:param name="sent"><s:property value="sent"/></s:param>
                                               <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                           </s:url>"><s:text name="label.creationDate"/></a>
                                    </th>
                                    <th class="text-center">
                                        <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                                               <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                               <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                               <s:param name="groupBy">lastModified</s:param>
                                               <s:param name="inQueue"><s:property value="inQueue"/></s:param>
                                               <s:param name="sent"><s:property value="sent"/></s:param>
                                               <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                           </s:url>"><s:text name="label.lastEdit"/></a>
                                    </th>
                                    <th class="text-center"><s:text name="jpnewsletter.label.sendDate"/></th>
                                    <th class="text-center"><s:text name="label.state" /></th>
                                    <th class="text-center table-w-5"><s:text name="label.actions" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator var="contentId">
                                    <s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
                                    <s:set var="contentReport" value="%{getContentReport(#contentId)}"/>
                                    <tr>
                                        <td class="text-center">
                                            <input type="checkbox" name="contentIds" 
                                                   id="content_<s:property value="#content.id" />" 
                                                   value="<s:property value="#content.id" />"/>
                                        </td>
                                        <td><s:property value="#content.description"/></td>
                                        <td>
                                            <s:property value="#content.id"/>
                                        </td>
                                        <td>
                                            <s:property value="%{getSmallContentType(#content.typeCode).description}"/>
                                        </td>
                                        <td>
                                            <s:property value="%{getGroup(#content.mainGroupCode).description}"/>
                                        </td>
                                        <td class="text-center text-nowrap">
                                            <s:date name="#content.create" format="dd/MM/yyyy HH:mm"/>
                                        </td>
                                        <td class="text-center text-nowrap">
                                            <s:date name="#content.modify" format="dd/MM/yyyy HH:mm"/>
                                        </td>

                                        <s:if test="#content.onLine && #content.sync">
                                            <s:set var="iconName">check</s:set>
                                            <s:set var="textVariant">success</s:set>
                                            <s:set var="isOnlineStatus" value="%{getText('label.yes')}"/>
                                        </s:if>
                                        <s:if test="#content.onLine && !(#content.sync)">
                                            <s:set var="iconName">adjust</s:set>
                                            <s:set var="textVariant">info</s:set>
                                            <s:set var="isOnlineStatus"
                                                   value="%{getText('label.yes') + ', ' + getText('note.notSynched')}"/>
                                        </s:if>
                                        <s:if test="!(#content.onLine)">
                                            <s:set var="iconName">pause</s:set>
                                            <s:set var="textVariant">warning</s:set>
                                            <s:set var="isOnlineStatus" value="%{getText('label.no')}"/>
                                        </s:if>
                                        <td class="text-center">
                                            <s:if test="%{#contentReport!=null}"><span class="monospace"><s:date
                                                        name="#contentReport.sendDate" format="dd/MM/yyyy HH:mm"/></span></s:if>
                                            <s:else><abbr title="<s:text name="neverSentNewsletter" />">-</abbr></s:else>
                                            </td>
                                        <s:if test="isContentInQueue(#contentId)">
                                            <s:set var="iconName">adjust</s:set>
                                            <s:set var="textVariant">info</s:set>
                                            <s:set var="statusVar" value="%{getText('jpnewsletter.label.inQueue')}"/>
                                        </s:if>
                                        <s:else>
                                            <s:set var="iconName">pause</s:set>
                                            <s:set var="textVariant">warning</s:set>
                                            <s:set var="statusVar" value="%{getText('jpnewsletter.label.notInQueue')}"/>
                                        </s:else>
                                        <td class="text-center">
                                            <span class="icon fa fa-<s:property value="#iconName" /> text-<s:property value="#textVariant" />"
                                                  title="<s:property value="#statusVar" />"></span>
                                            <span class="sr-only"><s:property value="#statusVar"/></span>
                                        </td>

                                        <td class="table-view-pf-actions text-center">
                                            <div class="dropdown dropdown-kebab-pf">
                                                <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown">
                                                    <span class="fa fa-ellipsis-v"></span>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <li>
                                                        <a title="Newsletter&nbsp;<s:text name="jpnewsletter.label.detailOf" />: <s:property value="#content.description" />"
                                                           href="<s:url action="entry" ><s:param name="contentId" value="#content.id" /></s:url>">
                                                            <s:text name="label.detail" />
                                                            <span class="sr-only">Newsletter&nbsp;<s:text
                                                                    name="jpnewsletter.label.detailOf"/>: <s:property
                                                                    value="#content.description"/>
                                                            </span>
                                                        </a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
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
                    </div>
                    <div class="col-xs-12 mt-20 no-padding">
                        <wpsf:submit action="addToQueue" type="button" cssClass="btn btn-primary pull-right"
                                     title="%{getText('jpnewsletter.label.insertInQueue')}">
                            <s:text name="jpnewsletter.label.insertInQueue"/>
                        </wpsf:submit>
                    </div>
                </s:if>
            </wpsa:subset>
        </s:form>
    </div>
</div>
