<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jprss/Rss" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="jprss.title.rssManagement" />">
            <s:text name="jprss.title.rssManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:if test="strutsAction ==  1">
            <s:text name="label.add" />
        </s:if>
        <s:if test="strutsAction ==  2">
            <s:text name="label.edit" />
        </s:if>
    </li>
</ol>

<h1 class="page-title-container">
    <s:if test="strutsAction ==  1">
        <s:text name="label.add" />
    </s:if>
    <s:if test="strutsAction ==  2">
        <s:text name="label.edit" />
    </s:if>

    <span class="pull-right">
        <a tabindex="0" role="button"
           data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="jprss.title.rssManagement.help" />" data-placement="left"
           data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<!-- Default separator -->
<div class="text-right">
    <div class="form-group-separator"></div>
</div>

<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>

<s:form action="save" cssClass="form-horizontal">
    <p class="noscreen">
        <wpsf:hidden name="strutsAction" />
        <wpsf:hidden name="id" />
    </p>

    <p>
        <s:text name="note.channels.instructions" />
    </p>

    <s:if test="null == contentType || contentType==''">
        <fieldset class="col-xs-12">
            <legend><s:text name="label.info" /></legend>
            <div class="input-group">
                <wpsf:select name="contentType" id="rss_chn_contenttype" list="availableContentTypes" cssClass="form-control" />
                <div class="input-group-btn">
                    <wpsf:submit type="button" action="selectContentType" cssClass="btn btn-primary">
                        <s:text name="label.confirm" />
                    </wpsf:submit>
                </div>
            </div>
        </fieldset>
    </s:if>

    <s:else>
        <div >
            <legend><s:text name="jprss.label.filters" /></legend>

            <div class="col-sm-12 mb-10 ">
                <div class="input-group">
                    <wpsf:select name="filterKey" id="filterKey" list="allowedFilterTypes" listKey="key" listValue="value" cssClass="form-control" />
                    <span class="input-group-btn">
                        <wpsf:submit type="button" action="setFilterType" cssClass="btn btn-primary">
                            <s:text name="label.add" />
                        </wpsf:submit>
                    </span>
                </div>
            </div>


            <p class="sr-only">
                <wpsf:hidden name="filters" />
            </p>

            <s:if test="null != filtersProperties && filtersProperties.size()>0" >
                <div class="col-sm-12 mb-10 ">
                    <ol class="list-group">
                        <s:iterator value="filtersProperties" var="filter" status="rowstatus">
                            <li class="list-group-item">
                                <s:text name="label.filterBy" /><strong>
                                    <s:if test="#filter['key'] == 'created'">
                                        <s:text name="label.creationDate" />
                                    </s:if>
                                    <s:elseif test="#filter['key'] == 'modified'">
                                        <s:text name="label.lastModifyDate" />
                                    </s:elseif>
                                    <s:else>
                                        <s:property value="#filter['key']" />
                                    </s:else>
                                </strong><s:if test="(#filter['start'] != null) || (#filter['end'] != null) || (#filter['value'] != null)">,
                                    <s:if test="#filter['start'] != null">
                                        <s:text name="label.filterFrom" /><strong>
                                            <s:if test="#filter['start'] == 'today'">
                                                <s:text name="label.today" />
                                            </s:if>
                                            <s:else>
                                                <s:property value="#filter['start']" />
                                            </s:else>
                                        </strong>
                                        <s:if test="#filter['startDateDelay'] != null" >
                                            <s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['startDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />&nbsp;
                                        </s:if>
                                    </s:if>
                                    <s:if test="#filter['end'] != null">
                                        <s:text name="label.filterTo" /><strong>
                                            <s:if test="#filter['end'] == 'today'">
                                                <s:text name="label.today" />
                                            </s:if>
                                            <s:else>
                                                <s:property value="#filter['end']" />
                                            </s:else>
                                        </strong>
                                        <s:if test="#filter['endDateDelay'] != null" >
                                            <s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['endDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
                                        </s:if>
                                    </s:if>
                                    <s:if test="#filter['value'] != null">
                                        <s:text name="label.filterValue" />:<strong> <s:property value="#filter['value']" /></strong>
                                        <s:if test="#filter['likeOption'] == 'true'">
                                            <em>(<s:text name="label.filterValue.isLike" />)</em>
                                        </s:if>
                                    </s:if>
                                    <s:if test="#filter['valueDateDelay'] != null" >
                                        <s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['valueDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
                                    </s:if>
                                </s:if>
                                <s:if test="#filter['nullValue'] != null" >
                                    &nbsp;<s:text name="label.filterNoValue" />
                                </s:if>
                                &middot;
                                <s:if test="#filter['order'] == 'ASC'"><s:text name="label.order.ascendant" /></s:if>
                                <s:if test="#filter['order'] == 'DESC'"><s:text name="label.order.descendant" /></s:if>

                                    <div class="btn-toolbar pull-right">
                                        <div class="btn-group btn-group-sm">
                                        <wpsa:actionParam action="moveFilter" var="actionName" >
                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                            <wpsa:actionSubParam name="movement" value="UP" />
                                        </wpsa:actionParam>
                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
                                            <span class="sr-only"><s:text name="label.moveUp" /></span>
                                            <span class="icon fa fa-sort-asc"></span>
                                        </wpsf:submit>

                                        <wpsa:actionParam action="moveFilter" var="actionName" >
                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                            <wpsa:actionSubParam name="movement" value="DOWN" />
                                        </wpsa:actionParam>
                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
                                            <span class="sr-only"><s:text name="label.moveDown" /></span>
                                            <span class="icon fa fa-sort-desc"></span>
                                        </wpsf:submit>

                                        <wpsa:actionParam action="removeFilter" var="actionName" >
                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                        </wpsa:actionParam>
                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}" cssClass="btn btn-primary">
                                            <span class="sr-only"><s:text name="label.remove" /></span>
                                            <span class="fa fa-trash-o fa-lg"></span>
                                        </wpsf:submit>
                                    </div>
                                </div>
                                <span class="clearfix"></span>
                            </li>
                        </s:iterator>
                    </ol>
                </div>
            </s:if>
        </div>

        <div>

            <legend><s:text name="label.info" /></legend>


            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <s:text name="contentType" />
                </div>
                <div class="col-sm-10">
                    <wpsf:textfield name="dummy_contentType" id="dummy_rss_chn_contenttype" cssClass="form-control"
                                    value="%{getSmallContentType(contentType).descr}" disabled="true" />
                </div>
            </div>

            <p class="sr-only">
                <wpsf:hidden name="contentType" />
            </p>

            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <s:text name="title" />
                </div>
                <div class="col-sm-10">
                    <wpsf:textfield name="title" id="rss_chn_title" cssClass="form-control" />
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <s:text name="description" />
                </div>
                <div class="col-sm-10">
                    <wpsf:textfield name="description" id="rss_chn_description" cssClass="form-control" />
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <s:text name="active" />
                </div>
                <div class="col-sm-10">
                    <div class="checkbox">
                        <wpsf:checkbox name="active" id="rss_chn_active" cssClass="bootstrap-switch" />&#32;
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <s:text name="feedType" />
                </div>
                <div class="col-sm-10">
                    <wpsf:select list="availableFeedTypes" name="feedType" id="rss_chn_feedtype" cssClass="form-control" />
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <s:text name="category" />
                </div>
                <div class="col-sm-10">
                    <select name="category" id="rss_chn_category" class="form-control">
                        <option value=""><s:text name="jprss.label.selectCategory" /></option>
                        <s:iterator value="availableCategories" var="categoryVar">
                            <option <s:if test="%{category == #categoryVar.code}">selected="selected"</s:if> 
                                value="<s:property value="#categoryVar.code"/>"><s:property value="%{getShortFullTitle(#categoryVar, currentLang.code)}"/></option>
                        </s:iterator>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-2 control-label">
                    <s:text name="label.max.items" />
                </div>
                <div class="col-sm-10">
                    <wpsf:select id="maxContentsSize"
                                 name="maxContentsSize"
                                 headerKey=""
                                 headerValue="%{getText('label.all')}"
                                 list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}"
                                 cssClass="form-control" />
                </div>
            </div>


        </div>

        <div class="col-xs-12">
            <div class="form-group pull-right">
                <wpsf:submit type="button" cssClass="btn btn-primary">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>

    </s:else>
</s:form>
