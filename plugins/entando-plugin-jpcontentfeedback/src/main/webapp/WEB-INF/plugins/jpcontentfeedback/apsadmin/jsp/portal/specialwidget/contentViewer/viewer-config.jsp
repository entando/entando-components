<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="viewTree" namespace="/do/Page" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>&#32;/&#32;<s:text name="title.configPage" />
    </span>
</h1>

<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="saveViewerConfig" namespace="/do/jpcontentfeedback/Page/SpecialWidget/Viewer">
            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
            </p>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
                </div>
                <div class="panel-body">
                    <h2 class="h5 margin-small-vertical">
                        <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                        <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                    </h2>

            <s:if test="hasFieldErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h4 class="margin-none"><s:text name="message.title.FieldErrors" /></h4>
                    <ul class="margin-base-vertical">
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                                <li><s:property escape="false" /></li>
                                </s:iterator>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>


            <s:set name="showletParams" value="showlet.type.parameter" />
                <fieldset class="col-xs-12">
                    <legend><s:text name="title.configContentViewer.settings" /></legend>

            <s:property value="#showletParams['contentId'].descr" />

            <s:if test="showlet.config['contentId'] != null">
                <s:set name="content" value="%{getContentVo(showlet.config['contentId'])}"></s:set>
                <div class="form-group">
                    <table class="table table-bordered">
                        <tr>
                            <th class="text-right"><s:text name="label.code" /></th>
                        <td><code><s:property value="#content.id" /></code></td>
                        </tr>
                        <tr>
                        <th class="text-right"><s:text name="label.description" /></th>
                        <td><s:property value="#content.descr" /></td>
                        </tr>
                    </table>
                </div>
                <p class="noscreen clear">
                    <wpsf:hidden name="contentId" value="%{getShowlet().getConfig().get('contentId')}" />
                </p>

                </fieldset>
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="jpcontentfeedbackSearchContents">
                                <s:text name="%{getText('label.change')}"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>

                <fieldset class="col-xs-12">
                    <legend><s:text name="title.publishingOptions" /></legend>
                    <div class="form-group">
                        <label for="modelId"><s:text name="label.contentModel" /></label>
                        <wpsf:select  id="modelId" name="modelId" value="%{getShowlet().getConfig().get('modelId')}"
                                     list="%{getModelsForContent(showlet.config['contentId'])}" headerKey=""
                                     headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="form-control" />
                    </div>
                </fieldset>
            </s:if>
            <s:else>
                <p>
                    <s:text name="note.noContentSet" />
                </p>
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="jpcontentfeedbackSearchContents">
                                <s:text name="%{getText('label.choose')}"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </s:else>

            <%-- --%>
            <fieldset class="col-xs-12">
                <legend><s:text name="jpcontentfeedback.title.configuration" /></legend>
                <div class="form-group">
                    <div class="checkbox">
                        <wpsf:checkbox  name="usedComment" id="jpcontentfeedback_usedComment"  value="%{getShowlet().getConfig().get('usedComment')}" cssClass="radiocheck"/>
                        <label for="jpcontentfeedback_usedComment"><s:text name="jpcontentfeedback.label.commentsOnContent" /></label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="checkbox">
                        <wpsf:checkbox  name="anonymousComment" id="jpcontentfeedback_anonymousComment"  value="%{getShowlet().getConfig().get('anonymousComment')}" cssClass="radiocheck"/>
                        <label for="jpcontentfeedback_anonymousComment"><s:text name="jpcontentfeedback.label.anonymousComments" /></label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="checkbox">
                        <wpsf:checkbox  name="commentValidation" id="jpcontentfeedback_commentsModeration" value="%{getShowlet().getConfig().get('commentValidation')}" cssClass="radiocheck" />
                        <label for="jpcontentfeedback_commentsModeration"><s:text name="jpcontentfeedback.label.commentsModeration" /></label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="checkbox">
                        <wpsf:checkbox  name="usedContentRating" id="jpcontentfeedback_usedContentRating"  value="%{getShowlet().getConfig().get('usedContentRating')}" cssClass="radiocheck" />
                        <label for="jpcontentfeedback_usedContentRating"><s:text name="jpcontentfeedback.label.contentsRating" /></label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="checkbox">
                        <wpsf:checkbox  name="usedCommentWithRating" id="jpcontentfeedback_usedCommentWithRating"  value="%{getShowlet().getConfig().get('usedCommentWithRating')}" cssClass="radiocheck" />
                        <label for="jpcontentfeedback_usedCommentWithRating"><s:text name="jpcontentfeedback.label.commentsRating" /></label>
                    </div>
                </div>
            </fieldset>
            <%-- --%>

                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                                <s:text name="%{getText('label.confirm')}"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
                </div>
            </div>
        </s:form>

    </div>
</div>