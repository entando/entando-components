<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="margin-none"><s:text name="title.referencedSurveys" /></h3>
    </div>
    <div class="panel-body">

        <s:if test="references['jpsurveySurveyManagerUtilizers']">
            <s:set var="referencingSurveys" value="references['jpsurveySurveyManagerUtilizers']" />
            <wpsa:subset source="#referencingSurveys" count="10" objectName="surveysReferencesGroup" advanced="true" offset="5" pagerId="referencingSurveysId">
                <s:set name="group" value="#surveysReferencesGroup" />
                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

                <s:set var="canEditSurvey" value="%{false}" />
                <wp:ifauthorized permission="manageSurvey"><s:set var="canEditSurvey" value="%{true}" /></wp:ifauthorized>

                    <table class="table table-bordered" id="surveyListTable">
                        <tr>
                            <th class="text-right"><s:text name="label.code" /></th>
                        <th><s:text name="label.description" /></th>
                    </tr>
                    <s:iterator var="survey" >
                        <tr>
                            <td class="text-right">
                                <s:if test="#canEditSurvey">
                                    <a href="<s:url namespace="/do/jpsurvey/Survey" action="editSurvey"><s:param name="surveyId" value="#survey.id"/><s:param name="questionnaire" value="questionnaire"/></s:url>" title="<s:text name="label.edit" />: <s:property value="#survey.id" />" ><s:property value="#survey.id" /></a>
                                </s:if>
                                <s:else><s:property value="#survey.id" /></s:else>
                                </td>
                                <td>
                                <s:property value="%{getTitle(#survey.id, #survey.titles)}" />
                            </td>
                        </tr>
                    </s:iterator>
                </table>

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
            </wpsa:subset>

        </s:if>
        <s:else>
            <div class="alert alert-info"><s:text name="note.referencedContent.empty" /></div>
        </s:else>
    </div>
</div>