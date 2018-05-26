<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />

<!-- Content Table -->
<div class="row">
    <div class="container-fluid container-cards-pf">
        <div class="row row-cards-pf">
            <div class="col-xs-12">
                <div class="card-pf card-pf-utilization">
                    <div class="card-pf-heading">
                        <wp:ifauthorized permission="editContents">
                            <span class="card-pf-heading-details">
                                <wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />
                                <span class="btn-group">
                                    <button type="button" class="btn btn-primary dropdown-toggle"
                                            data-toggle="dropdown">
                                        <s:text name="label.add"/>&#32;
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
                                        <s:iterator var="contentTypeVar" value="#contentTypesVar">
                                            <jacmswpsa:contentType
                                                typeCode="%{#contentTypeVar.typeCode}"
                                                property="isAuthToEdit" var="isAuthToEditVar" />
                                            <s:if test="%{#isAuthToEditVar}">
                                                <li>
                                                    <s:url action="createNew" namespace="/do/jacms/Content" var="addContentURL">
                                                        <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                                                    </s:url>
                                                    <a href="${addContentURL}"
                                                       title="<s:property value="%{#contentTypeVar.typeDescr}" />">
                                                        <s:property value="%{#contentTypeVar.typeDescr}" />
                                                    </a>
                                                </li>
                                            </s:if>
                                        </s:iterator>
                                    </ul>
                                </span>
                            </span>
                        </wp:ifauthorized>
                        <h2 class="card-pf-title">
                            <s:text name="dashboard.contentList" />
                        </h2>
                    </div>
                    <div class="card-pf-body" id="content-table">
                        <div class="table-responsive hidden">
                            <table class="table table-striped table-bordered no-mb">
                                <thead>
                                    <tr>
                                        <th><s:text name="dashboard.contents.description" /></th>
                                        <th><s:text name="dashboard.contents.author" /></th>
                                        <th><s:text name="dashboard.contents.type" /></th>
                                        <th class="text-center table-w-10"><s:text name="dashboard.contents.status" /></th>
                                        <th class="text-center w20perc"><s:text name="dashboard.contents.lastModified" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <wp:ifauthorized permission="editContents">
                        <s:url action="list" namespace="/do/jacms/Content" var="contentListURL" />
                        <a href="${contentListURL}"
                           class="bottom-link display-block text-right"
                           title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.contentList" />">
                            <s:text name="dashboard.contentList" />
                        </a>
                    </wp:ifauthorized>
                </div>
            </div>
        </div>
    </div>
</div>
                                                    
                                                    
                                                    
                                                    
                                                    
