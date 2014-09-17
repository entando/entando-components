<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set name="currentSurveyId" value="%{surveyId}" />
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="questionnaire">
            <s:text name="title.jpsurvey.survey.main" />&#32;/&#32;
            <s:text name="title.jpsurvey.surveyEditing" />
        </s:if>
        <s:else>
            <s:text name="title.jpsurvey.poll.main" />&#32;/&#32;
            <s:text name="title.jpsurvey.pollEditing" />
        </s:else>
    </span>
</h1>
<div id="main">
    <div class="panel panel-default">
        <div class="panel-body">
            <s:text name="note.survey.youwork" />:&#32;<s:property value="currentSurveyId.descr" /> 
            <s:set name="defaultLanguage" value="defaultLangCode"/>
            <code><s:property value="titles[#defaultLanguage]"/></code>
        </div>
    </div>

    <h3 class="margin-bit-top"><s:text name="title.chooseImage" /></h3>

    <s:form action="search" cssClass="form-horizontal"> 
        <p class="noscreen">
            <input type="hidden" name="surveyId" value="<s:property value="surveyId"/>" />
            <input type="hidden" name="resourceTypeCode" value="Image" />
            <input type="hidden" name="questionnaire" value="<s:property value="questionnaire"/>" />
        </p>

        <div class="form-group">
            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span class="input-group-addon">
                    <span class="icon fa fa-file-text-o fa-lg" 
                          title="<s:text name="label.search.by"/>&#32;<s:text name="label.text" />">
                    </span>
                </span>        
                <wpsf:textfield name="text" id="text" cssClass="form-control input-lg" />
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
                        <label class="control-label col-sm-2 text-right">
                            <s:text name="label.category" />
                        </label>
                        <div class="col-sm-5">
                            <div class="well">
                                <p class="toolClass">
                                    <a href="#" rel="expand" title="Expand All">expand all</a> 
                                    <a href="#" rel="collapse" title="Collapse All">collapse all</a>
                                </p>
                                <ul id="categoryTree" class="icons-ul list-unstyled">
                                    <li class="tree_node_flag">
                                        <span class="icon fa-li fa fa-folder"></span>
                                        <input type="radio" name="categoryCode" id="<s:property value="categoryRoot.code" />" 
                                               value="<s:property value="categoryRoot.code" />" 
                                               class="subTreeToggler" />
                                        <label for="<s:property value="categoryRoot.code" />">
                                            <s:if test="categoryRoot.titles[currentLang.code] == null">
                                                <s:property value="categoryRoot.code" />
                                            </s:if>
                                            <s:else>
                                                <s:property value="categoryRoot.titles[currentLang.code]" />
                                            </s:else>
                                        </label>
                                        <s:if test="categoryRoot.children.size > 0">
                                            <ul class="treeToggler icons-ul" id="tree_root">
                                                <s:set name="currentCategoryRoot" value="categoryRoot" />
                                                <s:include value="/WEB-INF/apsadmin/jsp/resource/categoryTreeBuilder.jsp" />
                                            </ul>
                                        </s:if>
                                    </li>
                                </ul>
                            </div>

                        </div>
                    </div>
                </div>

            </div>
        </div>
    </s:form>

    <div class="subsection-light">
        <s:form action="search">
            <p class="noscreen">
                <input type="hidden" name="questionnaire" value="<s:property value="questionnaire"/>" />
                <input type="hidden" name="text"/>	
                <input type="hidden" name="surveyId" value="<s:property value="surveyId"/>" />
                <input type="hidden" name="categoryCode" value="<s:property value="categoryCode" /> " />
                <input type="hidden" name="resourceTypeCode" value="Image" />
            </p>

            <wpsa:subset source="resources" count="10" objectName="groupResource" advanced="true" offset="5">
                <s:set name="group" value="#groupResource" />

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

                <s:iterator id="resourceid">
                    <s:set name="resource" value="%{loadResource(#resourceid)}"></s:set>
                    <%-- http://www.maxdesign.com.au/presentation/definition/dl-image-gallery.htm --%>

                    <div class="row">
                        <div class="col-sm-4 col-md-3">
                            <div class="panel panel-default text-center">
                                <div>
                                    <img src="<s:property value="%{#resource.getImagePath(1)}"/>" class="margin-small-top" style="height:90px;max-width:130px" alt="<s:property value="#resource.descr"/>"/>
                                </div>
                                <div class="btn-group margin-small-vertical">
                                    <a href="<s:url action="joinImage" namespace="/do/jpsurvey/Survey">
                                           <s:param name="resourceId" value="%{#resourceid}" />
                                           <s:param name="surveyId" value="surveyId" />
                                           <s:param name="questionnaire" value="questionnaire" />
                                       </s:url>" 
                                       title="<s:text name="note.joinThisToThat" />: TITOLO_SONDAGGIO_CORRENTE" 
                                       class="btn btn-default">
                                        <span class="icon fa fa-picture-o"></span> 
                                        <s:text name="label.join" />
                                    </a>
                                    <button type="button" class="btn btn-info" data-toggle="popover" data-title="<s:property value="#resource.descr" />" data-original-title="" title="">
                                        <span class="icon fa fa-info"></span>
                                        <span class="sr-only">Info</span>
                                    </button>
                                </div>
                                <script>
                                    $("[data-toggle=popover]").popover({
                                        html: true,
                                        placement: "top",
                                        content: '<div class="list-group margin-small-top">\n\
                                        <a href="<s:property value="%{#resource.getImagePath(1)}"/>" class="list-group-item text-center">View full size</a>\n\
                                        </div>'
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                </s:iterator>

                <div class="pager clear">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

            </wpsa:subset>
        </s:form>
    </div>
</div>