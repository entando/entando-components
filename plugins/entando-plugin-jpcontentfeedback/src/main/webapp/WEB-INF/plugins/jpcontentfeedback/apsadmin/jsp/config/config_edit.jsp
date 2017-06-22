<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.contentfeedbackManagement"/></li>
    <li class="page-title-container"><s:text name="jpcontentfeedback.admin.menu.contentFeedback.edit"/></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="title.contentfeedbackManagement"/>
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
                    <li>
                        <a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />">
                            <s:text name="jpcontentfeedback.admin.menu.contentFeedback"/>
                        </a>
                    </li>
                    <li class="active">
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
    <s:form action="update" class="form-horizontal">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>

        <fieldset class="margin-large-top">
            <legend>
                <s:text name="jpcontentfeedback.comments"/>
            </legend>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="jpcontentfeedback_comment">
                    <s:text name="jpcontentfeedback.label.commentsOnContent"/>
                </label>
                <div class="col-sm-9">
                    <input type="checkbox" class="radiocheck bootstrap-switch" id="jpcontentfeedback_comment"
                           name="config.comment" data-toggle="toggle" value="true" <s:if
                               test="config.comment"> checked="checked"</s:if>/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="jpcontentfeedback_anonymousComment">
                    <s:text name="jpcontentfeedback.label.anonymousComments"/>
                    <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-placement="top" data-content="<s:text name="jpcontentfeedback.note.anonymousComments"/>"
                       data-original-title="">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-9">
                    <input type="checkbox" class="radiocheck bootstrap-switch" id="jpcontentfeedback_anonymousComment"
                           name="config.anonymousComment" data-toggle="toggle" value="true" <s:if
                               test="config.anonymousComment"> checked="checked"</s:if>/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="jpcontentfeedback_moderatedComment">
                    <s:text name="jpcontentfeedback.label.commentsModeration"/>
                    <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-placement="top" data-content="<s:text name="jpcontentfeedback.note.commentsModeration"/>"
                       data-original-title="">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-9">
                    <input type="checkbox" class="radiocheck bootstrap-switch" id="jpcontentfeedback_moderatedComment"
                           name="config.moderatedComment" data-toggle="toggle" value="true" <s:if
                               test="config.moderatedComment"> checked="checked"</s:if>/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="jpcontentfeedback_moderatedComment">
                    <s:text name="jpcontentfeedback.label.commentsRating"/>
                </label>
                <div class="col-sm-9">
                    <input type="checkbox" class="radiocheck bootstrap-switch" id="jpcontentfeedback_rateComment"
                           name="config.rateComment" data-toggle="toggle" value="true" <s:if
                               test="config.rateComment"> checked="checked"</s:if>/>
                    </div>
                </div>
            </fieldset>

            <fieldset class="margin-large-top">
                <legend>
                <s:text name="jpcontentfeedback.contents"/>
            </legend>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="jpcontentfeedback_rateContent">
                    <s:text name="jpcontentfeedback.label.contentsRating"/>
                </label>
                <div class="col-sm-9">
                    <input type="checkbox" class="radiocheck bootstrap-switch" id="jpcontentfeedback_rateContent"
                           name="config.rateContent" data-toggle="toggle" value="true" <s:if
                               test="config.rateContent"> checked="checked"</s:if>/>
                    </div>
                </div>
            </fieldset>
            <div class="form-group">
                <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="%{getText('label.save')}"/>
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
