<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<s:set var="currentLang" value="%{getCurrentLang().code}" />
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.surveyList" /></li>
    <li>
        <a href="<s:url action="listSurveys" >
               <s:param name="questionnaire" value="questionnaire"></s:param>
           </s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />">
            <s:text name="title.jpsurvey.survey.main" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="jpsurvey_freeText_results" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="jpsurvey_freeText_results" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                       title="" data-content="<s:text name="jpsurvey_freeText_results.help" />"
                       data-placement="left" data-original-title=""> 
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <s:if test="questionnaire">
                        <li class="active">
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
                                <s:text name="title.jpsurvey.survey.main" />
                            </a>
                        </li>
                    </s:if>
                    <s:else>
                        <li>
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
                                <s:text name="title.jpsurvey.survey.main" />
                            </a>
                        </li>
                    </s:else>
                    <s:if test="questionnaire">
                        <li>
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
                                <s:text name="title.jpsurvey.poll.main" />
                            </a>
                        </li>
                    </s:if>
                    <s:else>
                        <li class="active">
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
                                <s:text name="title.jpsurvey.poll.main" />
                            </a>
                        </li>
                    </s:else>
                </ul>
            </div>
        </wp:ifauthorized>
    </div>
</div>
<br>

<div class="mb-20">
    <s:if test="%{freeTextMap != NULL && freeTextMap.size() > 0}">
        <table class="table table-bordered table-hover no-mb">
            <tr>
                <th class="text-right col-sm-2">
                    <s:text name="jpsurvey_freeText_answers" /></th>
                <th class="text-right col-sm-2">
                    <s:text  name="jpsurvey_freeText_occurences" /></th>
            </tr>
            <s:iterator var="currentFreeText" value="freeTextMap">
                <tr>
                    <td class="col-sm-10"><s:property value="#currentFreeText.key" /></td>
                    <td class="col-sm-10"><s:property value="#currentFreeText.value" /></td>
                </tr>
            </s:iterator>
        </table>
    </s:if>
    <s:else>
        <p>
            <s:text name="jpsurvey_freeText_noAnswers" />
        </p>
    </s:else>
</div>
