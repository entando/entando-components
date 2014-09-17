<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="questionnaire"><s:text name="title.jpsurvey.survey.main" /></s:if>
        <s:else><s:text name="title.jpsurvey.poll.main" /></s:else>
        </span> 
    </h1>
    <div id="main">
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasActionMessages()">
        <div class="alert alert-success alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>	
            <ul class="margin-base-vertical">
                <s:iterator value="actionMessages">
                    <li><s:property escape="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:form action="searchSurvey" cssClass="form-horizontal">
        <p class="noscreen">
            <s:hidden name="questionnaire" />
        </p>

        <div class="form-group">
            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span class="input-group-addon">
                    <span class="icon fa fa-file-text-o fa-lg" 
                          title="<s:text name="label.search.by" />&#32;<s:text name="jpsurvey_description" />">
                    </span>
                </span>        

                <wpsf:textfield name="description" id="description" cssClass="form-control input-lg" />
                <span class="input-group-btn">
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
                        <span class="sr-only"><s:text name="%{getText('label.search')}" /></span>
                        <span class="icon fa fa-search"></span>
                    </wpsf:submit>

                    <button type="button" class="btn btn-primary btn-lg dropdown-toggle" 
                            data-toggle="collapse" 
                            data-target="#search-advanced" title="Refine your search">
                        <span class="sr-only"><s:text name="title.searchFilters" /></span>
                        <span class="caret"></span>
                    </button>    
                </span>
            </div>

            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div id="search-advanced" class="collapse well collapse-input-group">
                    <div class="form-group">
                        <label for="title" class="control-label col-sm-2 text-right"><s:text name="jpsurvey_title" /></label>
                        <div class="col-sm-5"> 
                            <wpsf:textfield name="title" id="title" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="group" class="control-label col-sm-2 text-right"><s:text name="jpsurvey_group" /></label>
                        <div class="col-sm-5">
                            <wpsf:select name="group" id="group" list="groups" listKey="name" listValue="descr" 
                                         headerKey="" headerValue="%{getText('jpsurvey_indifferent')}" cssClass="form-control" />
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-5 col-sm-offset-2">
                            <wpsf:submit type="button" cssClass="btn btn-primary">
                                <s:text name="%{getText('label.search')}" />
                                <span class="icon fa fa-search"></span>
                            </wpsf:submit>    
                        </div>    
                    </div>

                </div>
            </div>
        </div>
        <div class="subsection-light">
            <%//TODO verificare posizione new %>
            <p>
                <a class="btn btn-default" href="<s:url action="newSurvey" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="questionnaire"/></s:url>" tabindex="<wpsa:counter />">
                        <span class="icon fa fa-plus-circle"></span>
                    <s:if test="questionnaire"><s:text name="jpsurvey_new_survey" /></s:if>
                    <s:else><s:text name="jpsurvey_new_poll" /></s:else>
                    </a>
                </p>
            <s:if test="%{surveysIds.size()==0}">
                <s:if test="questionnaire">
                    <div class="alert alert-info"><s:text name="jpsurvey.noSurveys" /></div>
                </s:if>
                <s:else>
                    <div class="alert alert-info"><s:text name="jpsurvey.noPools" /></div>
                </s:else>
            </s:if>
            <s:else>
                <wpsa:subset source="surveysIds" count="10" objectName="groupSurveyIds" advanced="true" offset="5">
                    <s:set name="group" value="#groupSurveyIds" />
                    <div class="pager">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                    </div>
                    <table class="table table-bordered">
                        <tr>
                            <th class="text-center"><abbr title="<s:text name="jpsurvey_actions" />">-</abbr></th>
                            <th><s:text name="jpsurvey_title" /></th>
                            <th class="text-center"><s:text name="jpsurvey_start_date" /></th>
                            <th class="text-right"><s:text name="jpsurvey_questions_number" /></th>
                            <th class="text-center"><abbr title="<s:text name="jpsurvey_published" />">P</abbr></th>
                        </tr>
                        <s:iterator id="surveyId">
                            <s:set name="survey" value="%{getSurvey(#surveyId)}" />
                            <%--LINK ALL'AZIONE DI EDIT, DIFFERENZIAMO PER TIPO DI SURVEY --%>
                            <tr>
                                <td class="text-center">
                                    <div class="btn-group btn-group-xs">
                                        <s:if test="%{#survey.active}">
                                            <a class="btn btn-default" 
                                               href="<s:url action="retireSurvey">
                                                   <s:param name="surveyId" value="#survey.id"/>
                                                   <s:param name="questionnaire" value="questionnaire" />
                                               </s:url>" title="<s:text name="jpsurvey_exitPublish" />:&#32;<s:property value="%{getLabel(#survey.titles)}"/>">
                                                <span class="icon fa fa-pause"></span>
                                                <span class="sr-only"><s:text name="jpsurvey_exitPublish" /></span>
                                            </a>
                                        </s:if>
                                        <s:else>
                                            <a  class="btn btn-default" 
                                                href="<s:url action="publishSurvey">
                                                    <s:param name="surveyId" value="#survey.id"/>
                                                    <s:param name="questionnaire" value="questionnaire"/>
                                                </s:url>" title="<s:text name="jpsurvey_publish" />:&#32;<s:property value="%{getLabel(#survey.titles)}"/>">
                                                <span class="icon fa fa-check"></span>
                                                <span class="sr-only"><s:text name="jpsurvey_publish" /></span>
                                            </a>
                                        </s:else>
                                        <a class="btn btn-default" 
                                           href="<s:url action="editSurvey">
                                               <s:param name="surveyId" value="#survey.id"/>
                                               <s:param name="questionnaire" value="questionnaire"/>
                                           </s:url>" title="<s:text name="label.edit" />:&#32;<s:property value="%{getLabel(#survey.titles)}" />">
                                            <span class="icon fa fa-pencil-square-o"></span>
                                            <span class="sr-only"><s:text name="label.edit" /></span>
                                        </a>
                                        <a class="btn btn-default"
                                           href="<s:url action="view">
                                               <s:param name="surveyId" value="#survey.id"/>
                                               <s:param name="questionnaire" value="questionnaire" />
                                           </s:url>" title="<s:text name="jpsurvey_results" />:&#32;<s:property value="%{getLabel(#survey.titles)}"/>">
                                            <span class="icon fa fa-info"></span>
                                            <span class="sr-only"><s:text name="jpsurvey_results" /></span>                                            
                                        </a>
                                    </div>
                                    <div class="btn-group btn-group-xs">
                                        <a class="btn btn-warning btn-xs" 
                                           href="<s:url action="trashSurvey">
                                               <s:param name="surveyId" value="#survey.id"/>
                                               <s:param name="deletingElement" value="1"/>
                                               <s:param name="questionnaire" value="questionnaire"/>
                                               <s:param name="deleteInfo" value="%{getLabel(#survey.titles)}"/>
                                           </s:url>" title="<s:text name="label.remove" />: <s:property value="%{getLabel(#survey.titles)}"/>">
                                            <span class="sr-only">Delete</span>
                                            <span class="icon fa fa-times-circle-o"></span>
                                        </a>
                                    </div>
                                </td>
                                <td>
                                    <s:property value="%{getLabel(#survey.titles)}"/>
                                </td>
                                <td class="text-center"><code><s:date name="#survey.startDate" format="dd/MM/yyyy" /></code></td> 
                                        <s:if test="%{#survey.active}">
                                            <wpsa:set name="iconImage" id="iconImage">icon fa fa-check text-success</wpsa:set>
                                    <s:set name="isOnlineStatus" value="%{getText('jpsurvey_published_yes')}" />
                                </s:if>
                                <s:elseif test="%{#survey.publishable}">
                                    <wpsa:set name="iconImage" id="iconImage">icon fa fa-pause text-warning</wpsa:set>
                                    <s:set name="isOnlineStatus" value="%{getText('jpsurvey_published_not_but_ready')}" />
                                </s:elseif>
                                <s:else>
                                    <wpsa:set name="iconImage" id="iconImage">icon fa fa-pause text-adjust</wpsa:set>
                                    <s:set name="isOnlineStatus" value="%{getText('jpsurvey_published_not_ready')}" />
                                </s:else>
                                <td class="text-right"><s:property value="#survey.questionsNumber"/></td>
                                <td class="text-center"><span class="<s:property value="iconImage" />" alt="<s:property value="isOnlineStatus" />" title="<s:property value="isOnlineStatus" />" /></td>
                            </tr>	
                        </s:iterator>
                    </table>
                    <div class="pager">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                    </div>
                </wpsa:subset>
            </s:else>
        </div>
    </s:form>
</div>