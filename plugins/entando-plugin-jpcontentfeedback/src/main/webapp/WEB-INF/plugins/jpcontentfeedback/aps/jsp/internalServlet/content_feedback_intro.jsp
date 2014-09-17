<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpcf" uri="/jpcontentfeedback-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
<wp:headInfo type="CSS" info="../../plugins/jpcontentfeedback/static/css/jpcontentfeedback.css"/>
--%>
<s:set var="htmlIdPrefix">jpfeedback_<wp:currentPage param="code" /><wp:currentWidget param="code"/>_</s:set>
<div class="jpcontentfeedback container">
  <s:if test="hasActionErrors()">
  <div class="row-fluid">
    <div class="alert alert-error span12">
      <button type="button" class="close" data-dismiss="alert"><i class="fa fa-times"></i></button>
      <p class="alert-heading"><wp:i18n key="ERRORS" /></p>
      <ul>
      <s:iterator value="actionErrors">
        <li><s:property escape="false" /></li>
      </s:iterator>
      </ul>
    </div>
  </div>
  </s:if>
  <s:if test="hasActionMessages()">
  <div class="row-fluid">
    <div class="alert alert-info span12">
      <button type="button" class="close" data-dismiss="alert"><i class="fa fa-times"></i></button>
      <p class="alert-heading"><wp:i18n key="MESSAGES" /></p>
      <ul>
        <s:iterator value="actionMessages">
          <li><s:property /></li>
        </s:iterator>
      </ul>
    </div>
  </div>
  </s:if>

  <%--INIZIO BLOCCO VOTO SU COMMENTO --%>
  <jpcf:ifViewContentOption param="usedContentRating">
    <div class="content_rating row-fluid">
      <div class="span12">
      <s:set var="rating" value="%{getContentRating()}" />
      <h2><wp:i18n key="jpcontentfeedback_CONTENT_RATING" /><s:if test="#rating != null">:&#32;<fmt:formatNumber value="${rating.average}" pattern="#0.00" />,&#32;<s:property value="#rating.voters" />&#32;<wp:i18n key="jpcontentfeedback_VOTERS_NUM" />
      </s:if>
      </h2>
      <s:else>
        <p class="alert alert-info"><wp:i18n key="jpcontentfeedback_VOTERS_NULL" /></p>
      </s:else>

      <%--INIZIO FORM --%>
      <c:set var="showRateContentForm" value="${sessionScope.currentUser != 'guest'}" />
      <c:if test="${showRateContentForm}">


        <s:set var="redirContent" value="currentContentId" scope="request" />
        <wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/insertVote.action" var="insertVoteAction" >
			<wp:parameter name="contentId" value="${redirContent}" />
             <wp:parameter  name="entaredir_contentId" value="${redirContent}" />
              <s:if test="null != extraParamNames">
                <s:iterator var="extraP" value="extraParamNames">
                  <s:set var="req_extraP" value="#extraP" scope="request" />
                  <c:if test="${!empty param[req_extraP]}">
                  	<wp:parameter  name="entaredir_${req_extraP}" value="${param[req_extraP]}" />
                  </c:if>
                </s:iterator>
              </s:if>
		</wp:action>
        <form action="<c:out value="${insertVoteAction}" escapeXml="false" />" method="post" class="form-inline">
          <p class="noscreen">
			<input type="hidden" name="entaredir_commentsPagerId_item" value="${param.commentsPagerId_item}" />
          </p>
          <s:iterator value="votes" var="voteItem">
            <s:set var="currentId" value="%{#htmlIdPrefix+'_vote_'+#voteItem.key}" />
            <label class="radio" for="<s:property value="#currentId" />" title="<wp:i18n key="jpcontentfeedback_VOTE" />:&#32;<s:property value="#voteItem.key" />">
            <input type="radio" name="vote" id="<s:property value="#currentId" />" value="<s:property value="#voteItem.key" />" />&#32;<s:property value="#voteItem.value" /></label>
          </s:iterator>
            <s:set name="labelSubmit"><wp:i18n key="jpcontentfeedback_VOTE" /></s:set>
            <wpsf:submit useTabindexAutoIncrement="true" value="%{#labelSubmit}" cssClass="btn" />
        </form>
      </c:if>
      <%--FINE FORM --%>
      </div>
    </div>
  </jpcf:ifViewContentOption>
  <%--FINE BLOCCO VOTO SU COMMENTO --%>

  <%-- Comments --%>
  <jpcf:ifViewContentOption param="usedCommentWithRating" var="isUsedCommentWithRating" />
  <jpcf:ifViewContentOption param="usedComment">
    <div class="content_comments row-fluid">
      <div class="span12">
      <h2><wp:i18n key="jpcontentfeedback_COMMENTS" /></h2>
      <s:set var="contentCommentIdsVar" value="%{contentCommentIds}"/>
      <s:if test="#contentCommentIdsVar.size > 0 ">
        <wp:pager pagerId="commentsPagerId" listName="contentCommentIdsVar" objectName="groupComment" pagerIdFromFrame="true" max="5" >

        <c:set var="group" value="${groupComment}" scope="request" />
        <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />

          <ol class="unstyled">
          <c:forEach var="commentId" items="${contentCommentIdsVar}" begin="${groupComment.begin}" end="${groupComment.end}" varStatus="status">
            <li class="margin-medium-vertical padding-medium-bottom">
              <jpcf:contentCommentViewer commentId="${commentId}" commentName="comment"/>
              <div class="comment_info">
                <%-- comment info --%>

                  <s:set var="ccc"><c:out value="${comment.comment}" /></s:set>
                  <blockquote>
                    <p>
                      <s:property value='#ccc.replaceAll("\r", "").replaceAll("\n\n+", "<br />\n").replaceAll("\n+", "<br />")' escapeHtml="false" />
                    </p>
                    <small><c:out value="${comment.username}" />,&#32;<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${comment.creationDate}" /></small>
                  </blockquote>
              </div>
              <c:if test="${isUsedCommentWithRating}" >
                <div class="comment_rating">
                  <h3><wp:i18n key="jpcontentfeedback_COMMENT_RATING" />:&#32;
                  <s:set var="commentIdvalue"><c:out value="${commentId}" /></s:set>
                  <s:set var="commentRating" value="%{getCommentRating(#commentIdvalue)}"></s:set>
                  <s:if test="#commentRating != null">
                      <fmt:formatNumber value="${commentRating.average}" pattern="#0.00" />,&#32;<c:out value="${commentRating.voters}" />&#32;<wp:i18n key="jpcontentfeedback_VOTERS_NUM" />
                  </s:if>
                  <s:else>
                    <abbr title="<wp:i18n key="jpcontentfeedback_COMMENT_NORATING" />">&ndash;</abbr>
                  </s:else>
                  </h3>

                  <c:set var="showRateCommentForm" value="${sessionScope.currentUser != 'guest'}" />
                  <c:if test="${showRateCommentForm}">


		          	<s:set var="redirContent" value="currentContentId" scope="request" />
			        <wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/insertCommentVote.action" var="insertCommentVote" >
						<wp:parameter name="selectedComment" value="${commentId}" />
						<wp:parameter name="contentId" value="${comment.contentId}" />
			             <wp:parameter  name="entaredir_contentId" value="${redirContent}" />
			              <s:if test="null != extraParamNames">
			                <s:iterator var="extraP" value="extraParamNames">
			                  <s:set var="req_extraP" value="#extraP" scope="request" />
			                  <c:if test="${!empty param[req_extraP]}">
			                  	<wp:parameter  name="entaredir_${req_extraP}" value="${param[req_extraP]}" />
			                  </c:if>
			                </s:iterator>
			              </s:if>
					</wp:action>
                    <form action="<c:out value="${insertCommentVote}" escapeXml="false" />" method="post" class="form-inline">
                      <p class="noscreen">
							<input type="hidden" name="entaredir_commentsPagerId_item" value="${param.commentsPagerId_item}" />
                      </p>

                        <s:set var="htmt_vode_id">votecomment_<c:out value="${comment.id}" /><c:out value="${comment.contentId}" /></s:set>
                        <s:set name="labelSubmit"><wp:i18n key="jpcontentfeedback_SEND" /></s:set>
                        <label for="<s:property value="#htmt_vode_id" />"><wp:i18n key="jpcontentfeedback_LABEL_RATING" /></label>
                        <select name="vote" id="<s:property value="#htmt_vode_id" />" class="span1">
                          <s:iterator value="votes" var="voteItem">
                              <s:set var="currentId" value="%{#htmlIdPrefix+'_vote_'+#voteItem.key}" />
                              <option value="<s:property value="#voteItem.key" />"><s:property value="#voteItem.value" /></option>
                          </s:iterator>
                        </select>
                        <wpsf:submit useTabindexAutoIncrement="true" value="%{#labelSubmit}" cssClass="btn" />
                    </form>
                  </c:if>
                </div>
              </c:if>
              <%-- remove --%>
              <c:if test="${comment.username == sessionScope.currentUser}" >
                <wp:ifauthorized permission="jpcontentfeedback_comment_edit">
                  <p class="comment_actions">

		          	<s:set var="redirContent" value="currentContentId" scope="request" />
			        <wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/delete.action" var="deleteAction" >
						<wp:parameter name="selectedComment"><c:out value="${commentId}" /></wp:parameter>
						<wp:parameter name="contentId" value="${comment.contentId}" />
			            <wp:parameter name="entaredir_contentId" value="${redirContent}" />
			            <wp:parameter name="entaredir_commentsPagerId_item" value="${param.commentsPagerId_item}" />
			              <s:if test="null != extraParamNames">
			                <s:iterator var="extraP" value="extraParamNames">
			                  <s:set var="req_extraP" value="#extraP" scope="request" />
			                  <c:if test="${!empty param[req_extraP]}">
			                  	<wp:parameter  name="entaredir_${req_extraP}" value="${param[req_extraP]}" />
			                  </c:if>
			                </s:iterator>
			              </s:if>
					</wp:action>

					<a href="<c:out value="${deleteAction}" escapeXml="false" />" class="btn btn-danger"><i class="icon-remove icon-white"></i>&#32;<wp:i18n key="jpcontentfeedback_DELETE" /></a>
                  </p>
                </wp:ifauthorized>
              </c:if>
            </li>
          </c:forEach>

          </ol>
          <%--  --%>

          <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />

        </wp:pager>

        <%-- Important: reset variables --%>
        <c:set var="group" value="${null}" scope="request" />

      </s:if>
      <s:else>
        <p class="alert alert-info"><wp:i18n key="jpcontentfeedback_COMMENTS_NULL" /></p>
      </s:else>

      <%--INIZIO BLOCCO FORM AGGIUNTA COMMENTO --%>
      <c:set var="showCommentForm" value="${true}" />
      <jpcf:ifViewContentOption param="anonymousComment" var="allowAnonymousComment" />
      <c:if test="${sessionScope.currentUser == 'guest'}">
        <c:set var="showCommentForm" value="${allowAnonymousComment}" />
      </c:if>
      <c:if test="${showCommentForm}">
        <div class="comment_form">
          <h3 class="jpcontentfeedback_title"><span><wp:i18n key="jpcontentfeedback_COMMENT_THE_CONTENT" /></span></h3>
          <%-- Messaggi di errore utente/validazione --%>
          <s:if test="hasFieldErrors()">
            <div class="alert alert-error">
            <button type="button" class="close" data-dismiss="alert"><i class="fa fa-times"></i></button>
            <p class="alert-heading"><wp:i18n key="ERRORS" /></p>
            <ul>
              <s:iterator value="fieldErrors">
                <s:iterator value="value">
                  <li><s:property escape="false" /></li>
                </s:iterator>
              </s:iterator>
            </ul>
            </div>
          </s:if>

          <s:set var="redirContent" value="currentContentId" scope="request" />
          <wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/insert.action" var="insertAction" >
             <wp:parameter  name="entaredir_contentId" value="${redirContent}" />
              <s:if test="null != extraParamNames">
                <s:iterator var="extraP" value="extraParamNames">
                  <s:set var="req_extraP" value="#extraP" scope="request" />
                  <c:if test="${!empty param[req_extraP]}">
                  	<wp:parameter  name="entaredir_${req_extraP}" value="${param[req_extraP]}" />
                  </c:if>
                </s:iterator>
              </s:if>
          </wp:action>

          <form action="<c:out value="${insertAction}" escapeXml="false" />" method="post">
            <p class="noscreen">
              <c:choose>
                <c:when test="${groupComment.size > 0 && groupComment.size >= groupComment.max}">
                  <c:set var="x" value="0" />
                  <c:if test="${((groupComment.size + 1) / groupComment.max)  > groupComment.maxItem }" >
                    <c:set var="x" value="1" />
                  </c:if>
                  <c:set var="pag_value" value="${groupComment.maxItem + 1}" />
                </c:when>
                <c:otherwise>
                  <c:set var="pag_value" value="${groupComment.currItem}" />
                </c:otherwise>
              </c:choose>
              <input type="hidden" name="contentId" value="<s:property value="currentContentId"/>">
              <input type="hidden" name="entaredir_commentsPagerId_item" value="${param.commentsPagerId_item}" />
              <s:token />
            </p>
            <p>
              <label for="commentText"><wp:i18n key="jpcontentfeedback_LABEL_COMMENTTEXT" /></label>
              <wpsf:textarea useTabindexAutoIncrement="true" name="commentText" id="commentText" value="" cssClass="span6" cols="40" rows="3" />
            </p>
            <p>
              <s:set name="labelSubmit"><wp:i18n key="jpcontentfeedback_SEND" /></s:set>
              <wpsf:submit useTabindexAutoIncrement="true" value="%{#labelSubmit}" cssClass="btn btn-primary" />
            </p>
          </form>
        </div>
      </c:if>
      <c:if test="${!showCommentForm}">
        <div class="alert alert-warning">
          <p><wp:i18n key="jpcontentfeedback_LOGIN_REQUIRED" /></p>
        </div>
      </c:if>
      <%--FINE BLOCCO FORM AGGIUNTA COMMENTO --%>
      </div>
    </div>
  </jpcf:ifViewContentOption>
</div>
