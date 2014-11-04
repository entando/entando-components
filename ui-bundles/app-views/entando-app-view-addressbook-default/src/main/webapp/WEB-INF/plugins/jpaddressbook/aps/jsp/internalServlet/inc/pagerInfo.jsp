<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %> 
<p class="paginazione"><wp:i18n key="jpaddressbook_SEARCH_RESULTS_INTRO" />&#32;<s:property value="#group.size" />&#32;<wp:i18n key="jpaddressbook_SEARCH_RESULTS_OUTRO" />.<br />
<wp:i18n key="jpaddressbook_PAGE" />: [<s:property value="#group.currItem" />/<s:property value="#group.maxItem" />].</p>  