<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>

<div class="jpcrowdsourcing viewIdea">
    <div class="ibox float-e-margins">

        <s:set var="idea" value="%{getIdea(ideaId)}" />
        <s:if test="#idea!=null">

            <%-- //back to list//
                    the page is the one that contains the widget jpcrowdsourcing_ideaInstance configured with the same instancecode of this idea
            --%>
            <c:set var="instanceCodeVar"><s:property value="#idea.instanceCode" /></c:set>
            <jpcrwsrc:pageWithWidget var="ideaList_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceCodeVar}" listResult="false"/>
            <wp:url page="${ideaList_page.code}" var="listPage"/>

<!--            <form action="${listPage}" method="post">
            <s:set var="labelList"><wp:i18n key="jpcollaboration_BACK_TO_LIST" escapeXml="false" /></s:set>
            <p><wpsf:submit value="%{#labelList}" cssClass="btn btn-default" /></p>
        </form>-->

            <%-- //back to list// --%>

            <div class="ibox-title">
                <h5><s:property value="#idea.title" /></h5>
            </div>
            <div class="ibox-content">
                <s:set var="categories" value="#idea.tags" />
                <s:if test="null != #categories && #categories.size > 0">
                    <p>
                        <i class="fa fa-tag" aria-hidden="true"></i>&#32;<wp:i18n key="jpcollaboration_TAG" />:&#32;
                        <s:iterator value="#categories" var="cat" status="status">
                            <wp:url var="ideaList_pageUrl" page="${ideaList_page.code}"><wp:urlPar name="ideaTag" ><s:property value="#cat" /></wp:urlPar></wp:url>
                            <a href="<c:out value="${ideaList_pageUrl}" escapeXml="false" />"><s:property value="%{getCategory(#cat).getTitle()}" /></a><s:if test="!#status.last">,&#32;</s:if>
                        </s:iterator>
                    </p>
                </s:if>

                <p class="p-xs bg-muted">
                    <s:property value='#idea.descr.trim().replaceAll("\r", "").replaceAll("\n\n+", "</p><p>").replaceAll("\n", "</p><p>")' escapeHtml="false" />
                </p>

                <p><i class="fa fa-info-circle" aria-hidden="true"></i>&#32;
                    <wp:i18n key="jpcollaboration_IDEA_PUBBL" />&#32;
                    <span class="font-italic"><s:property value="#idea.username"/>&#32;</span>
                    <strong title="<s:date name="#idea.pubDate" />">
                        <s:date name="#idea.pubDate" nice="true"/>
                    </strong>
                </p>

                <s:if test="null == #parameters.comment_form && null != #parameters.userAction">
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
                        <div class="alert alert-danger">
                            <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                                <wp:i18n key="ERRORS" />
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

                <form action="<wp:action path="/ExtStr2/do/collaboration/FrontEnd/Idea/ideaLike.action"/>" method="post" class="form-inline display-inline">
                    <s:hidden name="_csrf" value="%{csrfToken}"/>
                    <p class="noscreen">
                        <wpsf:hidden name="ideaId" value="%{ideaId}" />
                        <input type="hidden" name="userAction" value="like" />
                    </p>

                    <s:token name="listIdea" />
                    <s:set var="labelSubmit"><wp:i18n key="jpcollaboration_IDEA_LIKE_IT" escapeXml="false" /></s:set>
                    <wpsf:submit value="%{#labelSubmit}" cssClass="btn btn-xs btn-success" />
                    <span class="badge badge-success" title="<s:property value="#idea.votePositive" /> 
                          <wp:i18n key="jpcollaboration_IDEA_VOTE_AGREE" />">&#32;
                        <i class="fa fa-thumbs-up" aria-hidden="true"></i>&#32;
                        <s:property value="#idea.votePositive" /></span>

                </form>

                <form action="<wp:action path="/ExtStr2/do/collaboration/FrontEnd/Idea/ideaUnlike.action"/>" method="post" class="form-inline display-inline">
                    <s:hidden name="_csrf" value="%{csrfToken}"/>
                    <p class="noscreen">
                        <wpsf:hidden name="ideaId" value="%{ideaId}" />
                        <input type="hidden" name="userAction" value="dislike" />
                    </p>

                    <s:token name="listIdea" />
                    <s:set var="labelSubmit"><wp:i18n key="jpcollaboration_IDEA_NOT_LIKE_IT" escapeXml="false" /></s:set>
                    <wpsf:submit value="%{#labelSubmit}" cssClass="btn btn-xs btn-danger" />
                    <span class="badge badge-danger" title="<s:property value="#idea.voteNegative" /> 
                          <wp:i18n key="jpcollaboration_IDEA_VOTE_DISAGREE" />">&#32;<s:property value="#idea.voteNegative" />&#32;
                        <i class="fa fa-thumbs-down" aria-hidden="true"></i>
                    </span>&#32;
                </form>

                <s:set value="#idea.comments[3]" var="currentComments" />
                <legend  style="margin: 20px 0 0 0;" id="jpcrdsrc_comments_<s:property value="#idea.id" />"><wp:i18n key="jpcollaboration_IDEA_COMMENTS" /></legend>
                <p style="margin: 20px 0 0 0;">
                    <i class="fa fa-comment-o" aria-hidden="true"></i>&#32;
                    <s:if test="null == #currentComments || #currentComments.size == 0">0</s:if>
                    <s:else><s:property value="#currentComments.size" /></s:else>&#32;
                    <wp:i18n key="jpcollaboration_IDEA_COMMENTS" />
                </p>

                <c:choose>
                    <c:when test="${sessionScope.currentUser != 'guest'}">
                        <form action="<wp:action path="/ExtStr2/do/collaboration/FrontEnd/Idea/saveComment.action"/>" method="post" accept-charset="UTF-8">
                            <s:hidden name="_csrf" value="%{csrfToken}"/>
                            <s:if test="null != #parameters.comment_form">
                                <s:if test="hasActionErrors()">
                                    <div class="alert alert-danger">
                                        <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                                            <wp:i18n key="ERRORS" />
                                        <ul>
                                            <s:iterator value="actionErrors">
                                                <li><s:property escapeHtml="false" /></li>
                                                </s:iterator>
                                        </ul>
                                    </div>
                                </s:if>
                                <s:if test="hasActionMessages()">
                                    <div class="alert alert-info">
                                        <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                                            <wp:i18n key="MESSAGES" />
                                        <ul>
                                            <s:iterator value="actionMessages">
                                                <li><s:property /></li>
                                                </s:iterator>
                                        </ul>
                                    </div>
                                </s:if>
                                <s:if test="hasFieldErrors()">
                                    <div class="alert alert-danger alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                                            <wp:i18n key="ERRORS" />
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

                            <p class="noscreen">
                                <wpsf:hidden name="comment_form" value="comment_form" />
                                <wpsf:hidden name="ideaId" value="%{ideaId}" />
                                <wpsf:hidden name="ideaComment.id" value="%{ideaComment.id}" />
                                <wpsf:hidden name="ideaComment.ideaId" value="%{ideaId}" />
                            </p>

                            <s:token name="saveComment"/>
                            <p>
                                <label class="control-label" for="ideaComment_comment"><wp:i18n key="jpcollaboration_COMMENT" /></label>
                                <wpsf:textarea id="ideaComment_comment" name="ideaComment.comment" cols="40" rows="5" cssClass="form-control" />
                            </p>

                            <s:set var="labelSave"><wp:i18n key="jpcollaboration_SAVE_COMMENT" escapeXml="false" /></s:set>
                                <div>
                                <wpsf:submit value="%{#labelSave}" cssClass="btn btn-success pull-right" />
                            </div>

                        </form>

                    </c:when>
                    <c:otherwise>
                        <p class="alert alert-warning"><wp:i18n key="jpcollaboration_DO_LOGIN" /></p>
                    </c:otherwise>
                </c:choose>

                <s:if test="#currentComments.size > 0">
                    <div style="margin: 60px 0 0 0">
                        <s:iterator value="#currentComments" var="currentCommentId">
                            <s:set var="currentComment" value="%{getComment(#currentCommentId)}" />
                            <p>
                                <i class="fa fa-comment-o" aria-hidden="true"></i>&#32;
                                <strong title="<s:date name="#currentComment.creationDate" />">
                                    <s:date name="#currentComment.creationDate" nice="true" />
                                </strong>&#32;
                                <em><s:property value="#currentComment.username" /></em>&#32;
                                <wp:i18n key="jpcollaboration_COMMENT_SAID" />:
                            </p>
                            <blockquote>
                                <p>
                                    <s:property value='#currentComment.comment.trim().replaceAll("\r", "").replaceAll("\n\n+", "</p><p>").replaceAll("\n", "</p><p>")' escapeHtml="false" />
                                </p>
                            </blockquote>
                        </s:iterator>
                    </div>
                </s:if>


                <%-- //back to list// ${listPage} setted on top --%>
                <form action="${listPage}" method="post" class="form-inline display-inline">
                    <s:hidden name="_csrf" value="%{csrfToken}"/>
                    <s:set var="labelList"><wp:i18n key="jpcollaboration_BACK_TO_LIST" escapeXml="false" /></s:set>
                        <p>
                        <wpsf:submit value="%{#labelList}" cssClass="btn btn-default" />
                    </p>
                </form>
                <%-- //back to list// --%>

            </s:if>
            <s:else>
                <div class="alert alert-danger">
                    <wp:i18n key="jpcollaboration_IDEA_DETAILS" />
                    <p>
                        <wp:i18n key="jpcollaboration_IDEA_NOT_FOUND" />
                    </p>
                </div>
            </s:else>
        </div>
    </div>
</div>
