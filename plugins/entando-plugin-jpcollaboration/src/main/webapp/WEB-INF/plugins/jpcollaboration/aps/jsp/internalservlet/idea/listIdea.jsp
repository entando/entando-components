<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<div class="jpcrowdsourcing listIdea">
    <div class="ibox float-e-margins">
        <wp:info key="currentLang" var="currentLang" />
        <s:set var="ideaListDate" value="%{getIdeas(0)}"  />
        <s:set var="ideaListVote" value="%{getIdeas(1)}" />
        <jpcrwsrc:pageWithWidget var="viewIdea_page" widgetTypeCode="jpcollaboration_idea" listResult="false"/>
        <jpcrwsrc:pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaList" listResult="false"/>
        <jpcrwsrc:ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>

        <wp:url var="currentCategoryUrl" page="${listIdea_page.code}" paramRepeat="false" />
        <c:if test="${!empty categoryInfoList}">
            <c:forEach items="${categoryInfoList}" var="categoryInfo" varStatus="status">
                <c:if test="${!empty param.ideaTag && param.ideaTag==categoryInfo.category.code}">
                    <wp:url var="currentCategoryUrl" paramRepeat="false" page="${listIdea_page.code}">
                        <wp:urlPar name="ideaTag">
                            <c:out value="${categoryInfo.category.code}" />
                        </wp:urlPar>
                    </wp:url>
                    <c:set var="currentCategoryCode" value="${categoryInfo.category.code}"></c:set>
                    <c:set var="currentCategoryTitle" value="${categoryInfo.title}"></c:set>
                </c:if>
            </c:forEach>
        </c:if>
        <div class="ibox-title">
            <h5><wp:i18n key="jpcollaboration_LIST_IDEA_TITLE" /></h5>
        </div>
        <div class="ibox-content">
            <c:if test="${(!empty currentCategoryTitle) || (not empty param.ideaText)}">
                <p>
                    <c:if test="${!empty currentCategoryTitle}">
                        <span class="label label-info">
                            <span class="icon-tags icon-white" title="<wp:i18n key="jpcollaboration_LIST_IDEA_FILTERED_BY" />: <c:out value="${currentCategoryTitle}" />">
                            </span>&nbsp;<c:out value="${currentCategoryTitle}" />
                            &nbsp;&nbsp;&nbsp;&nbsp;<a href="<wp:url page="${listIdea_page.code}" />" title="<wp:i18n key="jpcollaboration_LIST_IDEA_ALL" />">
                                <span class="icon-remove icon-white"></span>
                            </a>

                        </span>
                    </c:if>

                    <c:if test="${not empty param.ideaText}">
                        <span class="label label-info">
                            <span class="icon-search icon-white" title="<wp:i18n key="SEARCHED_FOR" />: <c:out value="${param.ideaText}" />">
                            </span>&nbsp;
                            <c:out value="${param.ideaText}" />&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="<c:out value="${currentCategoryUrl}" />" title="<wp:i18n key="jpcollaboration_SEARCH_REMOVEFILTER" />">
                                <span class="icon-remove icon-white"></span>
                            </a>

                        </span>
                    </c:if>
                </p>
            </c:if>

            <s:if test="null != #parameters.listIdea_form">
                <s:if test="hasActionErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                        <wp:i18n key="ERRORS" />
                        <ul>
                            <s:iterator value="actionErrors">
                                <li><s:property escapeHtml="false" /></li>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>
                <s:if test="hasActionMessages()">
                    <div class="alert alert-danger alert-dismissable">
                        <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                        <wp:i18n key="MESSAGES" />
                        <ul>
                            <s:iterator value="actionMessages">
                                <li><s:property /></li>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-error">
                        <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                        <p class="alert-heading"><wp:i18n key="ERRORS" /></p>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>
            </s:if>

            <c:choose>
                <c:when test="${!empty param.type && param.type=='vote'}">
                    <c:set var="titleLabel" value="jpcrowdsourcing_TYPE_VOTE" />
                    <s:set var="currentList" value="#ideaListVote" scope="page" />
                </c:when>
                <c:otherwise>
                    <c:set var="titleLabel" value="jpcrowdsourcing_TYPE_DATE" />
                    <s:set var="currentList" value="#ideaListDate" scope="page" />
                </c:otherwise>
            </c:choose>

            <ul class="nav nav-tabs">

                <li<c:if test="${empty param.type}"> class="active"</c:if>>
                    <a href="<c:out value="${currentCategoryUrl}" escapeXml="false" />"><wp:i18n key="jpcollaboration_TAGS_ALL" /></a>
                </li>

                <wp:url var="linkTypeListDate"><wp:urlPar name="type">date</wp:urlPar><wp:urlPar name="ideaTag"><c:out value="${param.ideaTag}" /></wp:urlPar></wp:url>
                <li<c:if test="${!empty param.type && param.type=='date'}"> class="active"</c:if>>
                    <a href="<c:out value="${linkTypeListDate}" escapeXml="false" />">
                        <wp:i18n key="jpcollaboration_IDEA_MORE_RECENT" />
                        (<s:property value="#ideaListDate.size"/>)</a>
                </li>

                <wp:url var="linkTypeListVote"><wp:urlPar name="type">vote</wp:urlPar><wp:urlPar name="ideaTag"><c:out value="${param.ideaTag}" /></wp:urlPar></wp:url>
                <li<c:if test="${!empty param.type && param.type=='vote'}"> class="active"</c:if>>
                    <a href="<c:out value="${linkTypeListVote}" escapeXml="false" />">
                        <wp:i18n key="jpcollaboration_IDEA_MORE_VOTED" />
                        (<s:property value="#ideaListVote.size"/>)</a>
                </li>

            </ul>
            <div class="tab-content" style="padding: 1em">

                <c:if test="${empty currentList}">
                    <p class="alert alert-warning">
                        <wp:i18n key="jpcollaboration_NOIDEA_FOUND" />
                    </p>
                </c:if>
                <c:if test="${!empty currentList}">
                    <wp:pager listName="currentList" objectName="groupIdea" pagerIdFromFrame="true" max="5" pagerId="listIdeaPager">

                        <c:set var="group" value="${groupIdea}" scope="request" />
                        <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />

                        <c:forEach var="ideaId" items="${currentList}" begin="${groupIdea.begin}" end="${groupIdea.end}" varStatus="status">
                            <s:set var="idea" value="%{getIdea(#attr.ideaId)}" />
                            <s:set var="maxChars" value="5" />
                            <s:set var="ideaAbstract" value="%{getIdea(#attr.ideaId).descr.trim().substring(0,maxChars)}" />

                            <wp:url var="viewIdea_pageUrl" page="${viewIdea_page.code}"><wp:urlPar name="ideaId" ><s:property value="#idea.id"/></wp:urlPar></wp:url>

                                    <p style="font-size: 1.5em">
                                        <a href="<c:out value="${viewIdea_pageUrl}" />">
                                    <s:property value="#idea.title" />
                                </a>
                            </p>


                            <s:set var="categories" value="%{getIdeaTags(#idea)}" />
                            <s:if test="null != #categories && #categories.size > 0">
                                <p class="mt-20"style="margin: 20px 0 20px 0">
                                    <i class="fa fa-tag" aria-hidden="true"></i>&#32;<wp:i18n key="jpcollaboration_TAG" />:&#32;
                                    <s:iterator value="#categories" var="cat" status="status">
                                        <wp:url var="listIdea_pageUrl" page="${listIdea_page.code}"><wp:urlPar name="ideaTag" ><s:property value="#cat.code" /></wp:urlPar></wp:url>
                                        <a href="<c:out value="${listIdea_pageUrl}" escapeXml="false" />"><s:property value="#cat.title" /></a><s:if test="!#status.last">,&#32;</s:if>
                                    </s:iterator>
                                </p>
                            </s:if>

                            <%-- here we need the shortDescr --%>

                            <s:set var="avatarUsername" value="#idea.username" scope="request"/>
                            <jpavatar:avatar username="${avatarUsername}" var="currentAvatar" />
                            <div class="dropdown-messages-box">
                                <img src="<c:out value="${currentAvatar}" />" class="img-polaroid"  title="" />&#32;

                                <div class="media-body ">
                                    <div>
                                        <wp:i18n key="jpcollaboration_IDEA_PUBBL" />&#32;
                                        <span class="font-italic"><s:property value="#idea.username"/></span>
                                        <strong><s:date name="#idea.pubDate" /></strong>
                                    </div>
                                </div>


                                <span class="font-italic"><s:property value="#idea.username"/>&#32;</span>
                                <span title="<s:date name="#idea.pubDate" />">
                                    <strong> <s:date name="#idea.pubDate" nice="true"/></strong>
                                </span>
                            </div>

                            <form action="<wp:action path="/ExtStr2/do/collaboration/FrontEnd/Idea/Manage/ideaLike.action" />" method="post" class="form-inline display-inline">
                                <p class="noscreen">
                                    <s:if test="%{null != #parameters['type']}"><wpsf:hidden name="type" value="%{#parameters['type']}" /></s:if>
                                    <s:if test="%{null != #parameters['ideaTag']}"><wpsf:hidden name="ideaTag" value="%{#parameters['ideaTag']}" /></s:if>
                                    <s:if test="%{null != #parameters[#pagerIdNameVar + '_item']}"><wpsf:hidden name="%{#pagerIdNameVar + '_item'}" value="%{#parameters[#pagerIdNameVar + '_item']}" /></s:if>
                                    <wpsf:hidden name="ideaId" value="%{#idea.id}" />
                                    <wpsf:hidden name="listIdea_form" value="listIdea_form" />
                                    <input type="hidden" name="userAction" value="like" />
                                </p>

                                <s:token name="listIdea" />
                                <s:set var="labelSubmit"><wp:i18n key="jpcollaboration_IDEA_LIKE_IT" escapeXml="false" /></s:set>
                                <wpsf:submit value="%{#labelSubmit}" cssClass="btn btn-xs btn-success" />
                                <span class="badge badge-success" title="<s:property value="#idea.votePositive" /> <wp:i18n key="jpcollaboration_IDEA_VOTE_AGREE" />">&#32;
                                    <i class="fa fa-thumbs-up" aria-hidden="true"></i>&#32;
                                    <s:property value="#idea.votePositive" />
                                </span>
                            </form>

                            <form action="<wp:action path="/ExtStr2/do/collaboration/FrontEnd/Idea/Manage/ideaUnlike.action" />" method="post" class="form-inline display-inline">
                                <p class="noscreen">
                                    <s:if test="%{null != #parameters['type']}"><wpsf:hidden name="type" value="%{#parameters['type']}" /></s:if>
                                    <s:if test="%{null != #parameters['ideaTag']}"><wpsf:hidden name="ideaTag" value="%{#parameters['ideaTag']}" /></s:if>
                                    <s:set var="pagerIdNameVar" ><c:out value="${groupIdea.pagerId}" /></s:set>
                                    <s:if test="%{null != #parameters[#pagerIdNameVar + '_item']}"><wpsf:hidden name="%{#pagerIdNameVar + '_item'}" value="%{#parameters[#pagerIdNameVar + '_item']}" /></s:if>
                                    <wpsf:hidden name="ideaId" value="%{#idea.id}" />
                                    <wpsf:hidden name="listIdea_form" value="listIdea_form" />
                                    <input type="hidden" name="userAction" value="unlike" />
                                </p>

                                <s:token name="listIdea" />
                                <s:set var="labelSubmit"><wp:i18n key="jpcollaboration_IDEA_NOT_LIKE_IT" escapeXml="false" /></s:set>
                                <wpsf:submit value="%{#labelSubmit}" cssClass="btn btn-xs btn-danger" />
                                <span class="badge badge-danger" title="<s:property value="#idea.voteNegative" /> 
                                      <wp:i18n key="jpcollaboration_IDEA_VOTE_DISAGREE" />">&#32;
                                    <s:property value="#idea.voteNegative" />&#32;
                                    <i class="fa fa-thumbs-down" aria-hidden="true"></i>
                                </span>&#32;
                            </form>
                            <p class="divider"></p>
                            <p class="margin-large-top">
                                <s:set value="#idea.comments[3]" var="currentComments" />
                                <a href="<c:out value="${viewIdea_pageUrl}" />#jpcrdsrc_comments_<s:property value="#idea.id" />">
                                    <i class="fa fa-comment-o" aria-hidden="true"></i>&#32;
                                    <s:if test="null == #currentComments || #currentComments.size == 0">0</s:if>
                                    <s:else><s:property value="#currentComments.size" /></s:else>&#32;
                                    <wp:i18n key="jpcollaboration_IDEA_COMMENTS" />
                                </a>
                            </p>
                            <hr>
                        </c:forEach>

                        <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />

                    </wp:pager>

                    <%-- Important: reset variables --%>
                    <c:set var="currentList" value="${null}" scope="request" />
                    <c:set var="group" value="${null}" scope="request" />

                </c:if>
            </div>
        </div>
    </div>
</div>

