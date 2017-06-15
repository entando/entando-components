<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.surveyList" /></li>
        <s:if test="questionnaire">
        <li class="page-title-container"><s:text
                name="title.jpsurvey.survey.main" /></li>
        </s:if>
        <s:else>
        <li class="page-title-container"><s:text
                name="title.jpsurvey.poll.main" /></li>
        </s:else>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="title.surveyList" />
                <span class="pull-right"> <a tabindex="0" role="button"
                                             data-toggle="popover" data-trigger="focus" data-html="true"
                                             title=""
                                             data-content="<s:text name="title.jpsurvey.survey.main.help" />"
                                             data-placement="left" data-original-title=""> <i
                            class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <s:if test="questionnaire">
                        <li class="active"><a
                                href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
                                <s:text name="jpsurvey.label.questionnaires" />
                            </a></li>
                        </s:if>
                        <s:else>
                        <li><a
                                href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
                                <s:text name="jpsurvey.label.questionnaires" />
                            </a></li>
                        </s:else>
                        <s:if test="questionnaire">
                        <li><a
                                href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
                                <s:text name="jpsurvey.label.polls" />
                            </a></li>
                        </s:if>
                        <s:else>
                        <li class="active"><a
                                href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
                                <s:text name="jpsurvey.label.polls" />
                            </a></li>
                        </s:else>
                </ul>
            </div>
        </wp:ifauthorized>
    </div>
</div>
<br>
<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
    <s:form action="searchSurvey" cssClass="form-horizontal">
        <div class="searchPanel form-group">
            <div class="well col-md-offset-3 col-md-6">
                <p class="search-label">
                    <s:text name="label.search.label" />
                </p>
                <p class="noscreen">
                    <s:hidden name="questionnaire" />
                </p>

                <div class="form-group">
                    <label for="title" class="control-label col-sm-2"><s:text
                            name="label.code" /></label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="description" id="description"
                                        cssClass="form-control input-lg" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="title" class="control-label col-sm-2"><s:text
                            name="jpsurvey_title" /></label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="title" id="title"
                                        cssClass="form-control input-lg" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="group" class="control-label col-sm-2"><s:text
                            name="jpsurvey_group" /></label>
                    <div class="col-sm-9">
                        <wpsf:select name="group" id="group" list="groups" listKey="name"
                                     listValue="descr" headerKey=""
                                     headerValue="%{getText('jpsurvey_indifferent')}"
                                     cssClass="form-control input-lg" />
                    </div>
                </div>

                <div class="col-sm-12">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="%{getText('label.search')}" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>

    <div class="subsection-light">
        <p>
            <a class="btn btn-primary pull-right"
               href="<s:url action="newSurvey" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="questionnaire"/></s:url>"
               tabindex="<wpsa:counter />"><s:if test="questionnaire">
                    <s:text name="jpsurvey_new_survey" />
                </s:if> <s:else>
                    <s:text name="jpsurvey_new_poll" />
                </s:else> </a>
        </p>
        <br> <br>
        <s:if test="%{surveysIds.size()==0}">
            <s:if test="questionnaire">
                <div class="alert alert-info">
                    <s:text name="jpsurvey.noSurveys" />
                </div>
            </s:if>
            <s:else>
                <div class="alert alert-info">
                    <s:text name="jpsurvey.noPools" />
                </div>
            </s:else>
        </s:if>
        <s:else>
            <wpsa:subset source="surveysIds" count="10"
                         objectName="groupSurveyIds" advanced="true" offset="5">
                <s:set var="group" value="#groupSurveyIds" />
                <table class="table table-striped table-bordered table-hover no-mb">
                    <thead>
                        <tr>
                            <th class="text-center"><s:text name="jpsurvey_title" /></th>
                            <th class="text-center"><s:text name="jpsurvey_start_date" /></th>
                            <th class="text-center"><s:text
                                    name="jpsurvey_questions_number" /></th>
                            <th class="text-center"><s:text name="jpsurvey_published" /></th>
                            <th class="text-center"><s:text name="jpsurvey_actions" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator var="surveyId">
                            <s:set var="survey" value="%{getSurvey(#surveyId)}" />
                            <tr class="text-center">
                                <td><s:property value="%{getLabel(#survey.titles)}" /></td>
                                <td><code>
                                        <s:date name="#survey.startDate" format="dd/MM/yyyy" />
                                    </code></td>
                                    <s:if test="%{#survey.active}">
                                        <wpsa:set name="iconImage" id="iconImage">icon fa fa-check text-success</wpsa:set>
                                    <s:set var="isOnlineStatus"
                                           value="%{getText('jpsurvey_published_yes')}" />
                                </s:if>
                                <s:elseif test="%{#survey.publishable}">
                                    <wpsa:set name="iconImage" id="iconImage">icon fa fa-pause text-warning</wpsa:set>
                                    <s:set var="isOnlineStatus"
                                           value="%{getText('jpsurvey_published_not_but_ready')}" />
                                </s:elseif>
                                <s:else>
                                    <wpsa:set name="iconImage" id="iconImage">icon fa fa-pause text-adjust</wpsa:set>
                                    <s:set var="isOnlineStatus"
                                           value="%{getText('jpsurvey_published_not_ready')}" />
                                </s:else>
                                <td><s:property value="#survey.questionsNumber" /></td>
                                <td><span class="<s:property value="iconImage" />"
                                          alt="<s:property value="isOnlineStatus" />"
                                          title="<s:property value="isOnlineStatus" />"></span></td>
                                <td class="table-view-pf-actions">
                                    <div class="dropdown dropdown-kebab-pf">
                                        <button class="btn btn-menu-right dropdown-toggle"
                                                type="button" data-toggle="dropdown" aria-haspopup="true"
                                                aria-expanded="false">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <s:if test="%{#survey.active}">
                                                <li><a
                                                        href="<s:url action="retireSurvey">
                                                            <s:param name="surveyId" value="#survey.id"/>
                                                            <s:param name="questionnaire" value="questionnaire" />
                                                        </s:url>"
                                                        title="<s:text name="jpsurvey_exitPublish" />:&#32;<s:property value="%{getLabel(#survey.titles)}"/>">
                                                        <s:text name="jpsurvey_exitPublish" />
                                                    </a></li>
                                                </s:if>
                                                <s:else>
                                                <li><a
                                                        href="<s:url action="publishSurvey">
                                                            <s:param name="surveyId" value="#survey.id"/>
                                                            <s:param name="questionnaire" value="questionnaire"/>
                                                        </s:url>"
                                                        title="<s:text name="jpsurvey_publish" />:&#32;<s:property value="%{getLabel(#survey.titles)}"/>">
                                                        <s:text name="jpsurvey_publish" />
                                                    </a></li>
                                                </s:else>
                                            <li><a
                                                    href="<s:url action="editSurvey">
                                                        <s:param name="surveyId" value="#survey.id"/>
                                                        <s:param name="questionnaire" value="questionnaire"/>
                                                    </s:url>"
                                                    title="<s:text name="label.edit" />:&#32;<s:property value="%{getLabel(#survey.titles)}" />">
                                                    <s:text name="label.edit" />
                                                </a></li>
                                            <li><a
                                                    href="<s:url action="view">
                                                        <s:param name="surveyId" value="#survey.id"/>
                                                        <s:param name="questionnaire" value="questionnaire" />
                                                    </s:url>"
                                                    title="<s:text name="jpsurvey_results" />:&#32;<s:property value="%{getLabel(#survey.titles)}"/>">
                                                    <s:text name="jpsurvey_results" />
                                                </a></li>
                                            <li><a
                                                    href="<s:url action="trashSurvey">
                                                        <s:param name="surveyId" value="#survey.id"/>
                                                        <s:param name="deletingElement" value="1"/>
                                                        <s:param name="questionnaire" value="questionnaire"/>
                                                        <s:param name="deleteInfo" value="%{getLabel(#survey.titles)}"/>
                                                    </s:url>"
                                                    title="<s:text name="label.remove" />: <s:property value="%{getLabel(#survey.titles)}"/>">
                                                    <span class="sr-only"><s:text name="label.delete" /></span>
                                                </a></li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
                <div class="pager">
                    <s:include
                        value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
            </wpsa:subset>
        </s:else>
    </div>
</div>
