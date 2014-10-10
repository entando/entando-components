<%@ taglib prefix="wpfp" uri="/jpfacetnav-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<wp:headInfo type="CSS" info="../../plugins/jpfacetnav/static/css/jpfacetnav.css"/>
<div class="jpfacetnav">
	<h2 class="title"><wp:i18n key="jpfacetnav_TITLE_TREE" /></h2>
	<wpfp:facetNavTree requiredFacetsParamName="requiredFacets" facetsTreeParamName="facetsForTree" />
	<c:set var="occurrences" value="${occurrences}" scope="request" />
	<c:forEach var="facetRoot" items="${facetsForTree}">
		<h3><wpfp:facetNodeTitle facetNodeCode="${facetRoot.code}" /></h3>
		<c:choose>
			<c:when test="${occurrences[facetRoot.code] != null && !(empty occurrences[facetRoot.code])}">
				<ul>
					<c:forEach var="facetNode" items="${facetRoot.children}">
						<c:if test="${occurrences[facetNode.code] != null}">
							<li>
								<a href="<wp:url><wp:parameter name="selectedNode" ><c:out value="${facetNode.code}" /></wp:parameter>
									<c:forEach var="requiredFacet" items="${requiredFacets}" varStatus="status">
										<wpfp:urlPar name="facetNode_${status.count}" ><c:out value="${requiredFacet}" /></wpfp:urlPar>
									</c:forEach>
									</wp:url>"><wpfp:facetNodeTitle facetNodeCode="${facetNode.code}" /></a>&#32;<abbr class="jpfacetnav_tree_occurences" title="<wp:i18n key="jpfacetnav_OCCURRENCES_FOR" />&#32;<wpfp:facetNodeTitle facetNodeCode="${facetNode.code}" />:&#32;<c:out value="${occurrences[facetNode.code]}" />"><c:out value="${occurrences[facetNode.code]}" /></abbr>
								<wpfp:hasToOpenFacetNode facetNodeCode="${facetNode.code}" requiredFacets="${requiredFacets}" occurrences="${occurrences}" >
									<c:set var="currFacetRoot" value="${facetNode}" scope="request" />
									<c:import url="/WEB-INF/plugins/jpfacetnav/aps/jsp/widgets/inc/inc_facetNavTree.jsp" />
								</wpfp:hasToOpenFacetNode>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</c:when>
			<c:otherwise>
				<p><abbr title="<wp:i18n key="jpfacetnav_EMPTY_TAG" />:&#32;<wpfp:facetNodeTitle facetNodeCode="${facetRoot.code}" escapeXml="true" />">&ndash;</abbr></p>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</div>