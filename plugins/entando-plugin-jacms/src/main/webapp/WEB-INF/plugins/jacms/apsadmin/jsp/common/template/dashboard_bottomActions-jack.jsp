<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />

<wp:ifauthorized permission="editContents">
    <div class="btn-group dropup w100perc mt-10 mb-10">
        <button type="button" class="btn btn-primary dropdown-toggle btn-block"
                data-toggle="dropdown">
            <s:text name="dashboard.addContent"/>&#32;
            <span class="caret dashboard-caret-right"></span>
        </button>
        <ul class="dropdown-menu w100perc" role="menu">
            <s:iterator var="contentTypeVar" value="#contentTypesVar">
                <jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}" property="isAuthToEdit" var="isAuthToEditVar" />
                <s:if test="%{#isAuthToEditVar}">
                    <li>
                        <s:url action="createNew" namespace="/do/jacms/Content" var="newContentURL">
                            <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                        </s:url>
                        <a href="${newContentURL}"
                           title="<s:text name="label.add" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" />">
                            <s:property value="%{#contentTypeVar.typeDescr}" />
                        </a>
                    </li>
                </s:if>
            </s:iterator>
        </ul>
    </div>
</wp:ifauthorized>

<wp:ifauthorized permission="manageResources">
    <div class="btn-group dropup w100perc mt-10 mb-10">
        <button type="button" class="btn btn-primary dropdown-toggle btn-block"
                data-toggle="dropdown">
            <s:text name="dashboard.addAsset"/>&#32;
            <span class="caret dashboard-caret-right"></span>
        </button>
        <ul class="dropdown-menu w100perc" role="menu">
            <li>
                <s:url namespace="/do/jacms/Resource" action="new" var="addImageURL">
                    <s:param name="resourceTypeCode" value="'Image'" />
                </s:url>
                <a href="${addImageURL}" title="<s:text name="label.new" />&#32;<s:text name="label.image"/>">
                    <s:text name="label.image"/>
                </a>
            </li>
            <li>
                <s:url namespace="/do/jacms/Resource" action="new" var="addAttachURL">
                    <s:param name="resourceTypeCode" value="'Attach'" />
                </s:url>
                <a href="${addAttachURL}" title="<s:text name="label.new" />&#32;<s:text name="label.attach"/>">
                    <s:text name="label.attach"/>
                </a>
            </li>
        </ul>
    </div>
</wp:ifauthorized>
