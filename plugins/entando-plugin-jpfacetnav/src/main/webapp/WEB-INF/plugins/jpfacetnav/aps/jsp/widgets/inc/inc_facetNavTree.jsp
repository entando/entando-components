<%@ taglib prefix="wpfp" uri="/jpfacetnav-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<ul>
	<c:forEach var="facetNode" items="${currFacetRoot.children}">
		<wpfp:hasToViewFacetNode facetNodeCode="${facetNode.code}" requiredFacets="${requiredFacets}">
			<c:if test="${occurrences[facetNode.code] != null}">
				<li>
				<a href="<wp:url><wpfp:urlPar name="selectedNode" ><c:out value="${facetNode.code}" /></wpfp:urlPar>
					<c:forEach var="requiredFacet" items="${requiredFacets}" varStatus="status">
						<wpfp:urlPar name="facetNode_${status.count}" ><c:out value="${requiredFacet}" /></wpfp:urlPar>
					</c:forEach>
					</wp:url>"><wpfp:facetNodeTitle escapeXml="true" facetNodeCode="${facetNode.code}" /></a>&#32;<abbr class="jpfacetnav_tree_occurences" title="<wp:i18n key="jpfacetnav_OCCURRENCES_FOR" />&#32;<wpfp:facetNodeTitle escapeXml="true" facetNodeCode="${facetNode.code}" />:&#32;<c:out value="${occurrences[facetNode.code]}" />"><c:out value="${occurrences[facetNode.code]}" /></abbr>
				<wpfp:hasToOpenFacetNode facetNodeCode="${facetNode.code}" requiredFacets="${requiredFacets}" occurrences="${occurrences}" >
					<c:set var="currFacetRoot" value="${facetNode}" scope="request" />
					<c:import url="/WEB-INF/plugins/jpfacetnav/aps/jsp/widgets/inc/inc_facetNavTree.jsp" />
				</wpfp:hasToOpenFacetNode>
				</li>
			</c:if>
		</wpfp:hasToViewFacetNode>
	</c:forEach>
</ul>