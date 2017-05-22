<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner"/></li>
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />">
            <s:text name="title.pageManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.configPage"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.configPage"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="jpcontentfeedback.title.configPage.help" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp"/>

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
            <s:param name="selectedNode" value="pageCode"></s:param>
        </s:action>

        <s:form action="saveViewerConfig" namespace="/do/jpcontentfeedback/Page/SpecialWidget/Viewer"
                cssClass="form-horizontal">
            <p class="noscreen">
                <wpsf:hidden name="pageCode"/>
                <wpsf:hidden name="frame"/>
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}"/>
            </p>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp"/>
                </div>
                <div class="panel-body">
                    <legend>
                        <span class="control-label label-group-name">
                            <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                            <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}"/>
                        </span>
                    </legend>

                    <s:if test="hasFieldErrors()">
                        <div class="alert alert-danger alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert"><span
                                    class="icon fa fa-times"></span></button>
                            <h4 class="margin-none"><s:text name="message.title.FieldErrors"/></h4>
                            <ul class="margin-base-vertical">
                                <s:iterator value="fieldErrors">
                                    <s:iterator value="value">
                                        <li><s:property escapeHtml="false"/></li>
                                    </s:iterator>
                                </s:iterator>
                            </ul>
                        </div>
                    </s:if>

                    <s:set var="showletParams" value="showlet.type.parameter"/>
                    <s:if test="showlet.config['contentId'] != null">
                        <fieldset class="form-horizontal">
                            <legend><s:text name="title.configContentViewer.settings"/></legend>

                            <s:property value="#showletParams['contentId'].descr"/>

                            <s:set var="content" value="%{getContentVo(showlet.config['contentId'])}"></s:set>

                            <div class="form-group">
                                <div class="col-sm-12 table-responsive">
                                    <table class="table table-bordered table-hover no-mb">
                                        <tr>
                                            <th class="text-right col-sm-2"><s:text name="label.code"/></th>
                                            <td class="col-sm-10">
                                                <code><s:property value="#content.id"/></code>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="text-right col-sm-2"><s:text name="label.description"/></th>
                                            <td class="col-sm-10"><s:property value="#content.descr"/></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <p class="noscreen clear">
                                <wpsf:hidden name="contentId" value="%{getShowlet().getConfig().get('contentId')}"/>
                            </p>

                        </fieldset>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <div class="col-xs-12">
                                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right"
                                                 action="jpcontentfeedbackSearchContents">
                                        <s:text name="%{getText('label.change')}"/>
                                    </wpsf:submit>
                                </div>
                            </div>
                        </div>
                        <fieldset class="form-horizontal">
                            <legend><s:text name="title.publishingOptions"/></legend>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="modelId">
                                    <s:text name="label.contentModel"/>
                                </label>
                                <div class="col-sm-9">
                                    <wpsf:select id="modelId" name="modelId"
                                                 value="%{getShowlet().getConfig().get('modelId')}"
                                                 list="%{getModelsForContent(showlet.config['contentId'])}" headerKey=""
                                                 headerValue="%{getText('label.default')}" listKey="id"
                                                 listValue="description" cssClass="form-control"/>
                                </div>
                            </div>

                        </fieldset>
                    </s:if>
                    <s:else>
                        <p>
                            <s:text name="note.noContentSet"/>
                        </p>
                        <div class="form-horizontal">
                            <div class="form-group">
                                <div class="col-xs-12">
                                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right"
                                                 action="jpcontentfeedbackSearchContents">
                                        <s:text name="%{getText('label.choose')}"/>
                                    </wpsf:submit>
                                </div>
                            </div>
                        </div>
                    </s:else>

                    <fieldset class="form-horizontal">
                        <legend><s:text name="jpcontentfeedback.title.configuration"/></legend>

                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="jpcontentfeedback_usedComment">
                                <s:text name="jpcontentfeedback.label.commentsOnContent"/>
                            </label>
                            <div class="col-sm-9">
                                <wpsf:checkbox name="usedComment" id="jpcontentfeedback_usedComment"
                                               value="%{getShowlet().getConfig().get('usedComment')}"
                                               data-toggle="toggle" cssClass="radiocheck bootstrap-switch"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="jpcontentfeedback_anonymousComment">
                                <s:text name="jpcontentfeedback.label.anonymousComments"/>
                            </label>
                            <div class="col-sm-9">
                                <wpsf:checkbox name="anonymousComment" id="jpcontentfeedback_anonymousComment"
                                               value="%{getShowlet().getConfig().get('anonymousComment')}"
                                               data-toggle="toggle" cssClass="radiocheck bootstrap-switch"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="jpcontentfeedback_commentsModeration">
                                <s:text name="jpcontentfeedback.label.commentsModeration"/>
                            </label>
                            <div class="col-sm-9">
                                <wpsf:checkbox name="commentValidation" id="jpcontentfeedback_commentsModeration"
                                               value="%{getShowlet().getConfig().get('commentValidation')}"
                                               data-toggle="toggle" cssClass="radiocheck bootstrap-switch"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="jpcontentfeedback_usedContentRating">
                                <s:text name="jpcontentfeedback.label.contentsRating"/>
                            </label>
                            <div class="col-sm-9">
                                <wpsf:checkbox name="usedContentRating" id="jpcontentfeedback_usedContentRating"
                                               value="%{getShowlet().getConfig().get('usedContentRating')}"
                                               data-toggle="toggle" cssClass="radiocheck bootstrap-switch"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="jpcontentfeedback_usedCommentWithRating">
                                <s:text name="jpcontentfeedback.label.commentsRating"/>
                            </label>
                            <div class="col-sm-9">
                                <wpsf:checkbox name="usedCommentWithRating" id="jpcontentfeedback_usedCommentWithRating"
                                               value="%{getShowlet().getConfig().get('usedCommentWithRating')}"
                                               data-toggle="toggle" cssClass="radiocheck bootstrap-switch"/>
                            </div>
                        </div>
                    </fieldset>

                    <div class="form-group">
                        <div class="col-xs-12">
                            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                                <s:text name="%{getText('label.confirm')}"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </div>
        </s:form>

    </div>
</div>
