<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>

<div class="ibox float-e-margins">
    <div class="jpcrowdsourcing listIdea">

        <jpcrwsrc:ideaList text="${param.ideaText}" var="currentList" />

        <div class="ibox-title">
            <h5><wp:i18n key="jpcollaboration_SEARCH_RESULT" /></h5>
        </div>
        <div class="ibox-content">
            <c:if test="${empty currentList}">
                <p class="alert alert-warning">
                    <wp:i18n key="jpcollaboration_NOIDEA_FOUND" />
                </p>
            </c:if>
            <c:if test="${!empty currentList}">
                <wp:pager listName="currentList" objectName="groupIdea" pagerIdFromFrame="true" max="5" pagerId="listIdeaPager">

                    <c:set var="group" value="${groupIdea}" scope="request" />
                    <%--<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />--%>

                    <c:forEach var="ideaId" items="${currentList}" begin="${groupIdea.begin}" end="${groupIdea.end}" varStatus="status">
                        <s:set var="idea" value="%{getIdea(#attr.ideaId)}" />
                        <s:set var="maxChars" value="5" />
                        <s:set var="ideaAbstract" value="%{getIdea(#attr.ideaId).descr.trim().substring(0,maxChars)}" />

                        <c:set var="instancecodevar"><s:property value="#idea.instanceCode" /></c:set>
                        <jpcrwsrc:pageWithWidget var="viewIdea_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instancecodevar}" listResult="false"/>

                        <wp:url var="viewIdea_pageUrl" page="${viewIdea_page.code}"><wp:urlPar name="ideaId" ><s:property value="#idea.id"/></wp:urlPar></wp:url>

                                <h3><a href="<c:out value="${viewIdea_pageUrl}" />"><s:property value="#idea.title" /></a></h3>
                            <s:set var="categories" value="%{getIdeaTags(#idea)}" />
                            <s:if test="null != #categories && #categories.size > 0">
                            <p>
                                <i class="fa fa-tag" aria-hidden="true"></i>&#32;<wp:i18n key="jpcollaboration_TAG" />:&#32;
                                <s:iterator value="#categories" var="cat" status="status">
                                    <wp:url var="listIdea_pageUrl" page="${listIdea_page.code}"><wp:urlPar name="ideaTag" ><s:property value="#cat.code" /></wp:urlPar></wp:url>
                                    <a href="<c:out value="${listIdea_pageUrl}" escapeXml="false" />"><s:property value="#cat.title" /></a><s:if test="!#status.last">,&#32;</s:if>
                                </s:iterator>
                            </p>
                        </s:if>

                        <%-- here we need the shortDescr --%>

                        <p><i class="fa fa-info-circle" aria-hidden="true"></i>&#32;
                            <wp:i18n key="jpcollaboration_IDEA_PUBBL" />&#32;
                            <span class="font-italic"><s:property value="#idea.username"/>&#32;</span>
                            <strong>
                                <span title="<s:date name="#idea.pubDate" />">
                                    <s:date name="#idea.pubDate" nice="true"/>.&#32;
                                </span>
                            </strong>
                        <p>
                            <wp:i18n key="jpcollaboration_INSTANCE" />:&#32;
                            <s:property value="#idea.instanceCode" />
                        </p>
                        

                        <form action="<wp:action path="/ExtStr2/do/collaboration/FrontEnd/Idea/Manage/ideaLike.action" />" method="post" class="form-horizontal" >
                            <s:hidden name="_csrf" value="%{csrfToken}"/>
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
                            <s:hidden name="_csrf" value="%{csrfToken}"/>
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
                            <span class="badge badge-danger" title="<s:property value="#idea.voteNegative" /> <wp:i18n key="jpcollaboration_IDEA_VOTE_DISAGREE" />">&#32;
                                <s:property value="#idea.voteNegative" />&#32;
                                <i class="fa fa-thumbs-down" aria-hidden="true"></i>&#32;
                            </span>
                        </form>

                        <p style="margin: 20px 0 0 0">
                            <s:set value="#idea.comments[3]" var="currentComments" />
                            <a href="<c:out value="${viewIdea_pageUrl}" />#jpcrdsrc_comments_<s:property value="#idea.id" />">
                                <i class="fa fa-comment-o" aria-hidden="true"></i>&#32;
                                <s:if test="null == #currentComments || #currentComments.size == 0">0</s:if>
                                <s:else><s:property value="#currentComments.size" /></s:else>&#32;
                                <wp:i18n key="jpcollaboration_IDEA_COMMENTS" />
                            </a>
                        </p>

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

