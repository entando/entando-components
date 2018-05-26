<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%-- referenced contents --%>
<div id="contents" class="tab-pane fade">
    <s:form action="detail" cssClass="form-horizontal">
        <s:hidden name="name" />
        <s:if test="null != references['jacmsContentManagerUtilizers']">
            <wpsa:subset source="references['jacmsContentManagerUtilizers']"
                         count="10" objectName="contentReferences" advanced="true"
                         offset="5" pagerId="contentManagerReferences">
                <s:set var="group" value="#contentReferences" />
                <div class="col-xs-12 no-padding">
                    <table class="table table-striped table-bordered table-hover no-mb"
                           id="contentListTable">
                        <thead>
                            <tr>
                                <th class="col-xs-7 col-sm-7 col-md-7 col-lg-7">
                                    <s:text name="label.description" />
                                </th>
                                <th class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1">
                                    <s:text name="label.code" />
                                </th>
                                <th class="text-center"><s:text name="label.type" /></th>
                                <th class="text-center"><s:text name="label.lastEdit" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator var="currentContentIdVar">
                            <jacmswpsa:content contentId="%{#currentContentIdVar}"
                                               var="currentContentRecordVar" record="true" />
                            <jacmswpsa:content contentId="%{#currentContentRecordVar.id}"
                                               var="currentContentVar" authToEditVar="isAuthToEditVar"
                                               workVersion="true" />
                            <tr>
                                <td>
                                    <s:if test="#isAuthToEditVar">
                                        <a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#currentContentVar.id" /></s:url>"
                                           title="<s:text name="label.edit" />:&#32;<s:property value="#currentContentVar.descr"/>">
                                            <s:property value="#currentContentVar.descr" />
                                        </a>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#currentContentVar.descr" />
                                    </s:else>
                                </td>
                                <td class="text-center">
                                    <code>
                                        <s:property value="#currentContentVar.id" />
                                    </code>
                                </td>
                                <td class="text-center">
                                    <s:property value="#currentContentVar.typeDescr" />
                                </td>
                                <td class="icon text-center">
                                    <code>
                                        <s:date name="#currentContentRecordVar.modify"
                                                format="dd/MM/yyyy" />
                                    </code>
                                </td>
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                </div>
                <div class="content-view-pf-pagination clearfix">
                    <div class="form-group">
                        <span>
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                        <div class="mt-5">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                        </div>
                    </div>
                </div>
            </wpsa:subset>
        </s:if>
        <s:else>
            <p class="margin-none text-center">
                <br>
                <s:text name="note.group.referencedContents.empty" />
            </p>
        </s:else>
    </s:form>
</div>
<div id="resources" class="tab-pane fade">
    <s:form action="detail" cssClass="form-horizontal">
        <s:hidden name="name" />
        <s:if test="null != references['jacmsResourceManagerUtilizers']">
            <wpsa:subset source="references['jacmsResourceManagerUtilizers']"
                         count="10" objectName="resourceReferences" advanced="true"
                         offset="5" pagerId="resourceManagerReferences">
                <s:set var="group" value="#resourceReferences" />
                <div class="col-xs-12 no-padding">
                    <table class="table table-striped table-bordered table-hover no-mb"
                           id="resourceListTable">
                        <thead>
                            <tr>
                                <th><s:text name="label.description" /></th>
                                <th><s:text name="label.type" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator var="currentResourceIdVar">
                            <jacmswpsa:resource resourceId="%{#currentResourceIdVar}"
                                                var="currentResourceVar" />
                            <tr>
                                <s:set var="canEditCurrentResource" value="%{false}" />
                                <c:set var="currentResourceGroup">
                                    <s:property value="#currentResourceVar.mainGroup"
                                                escapeHtml="false" />
                                </c:set>
                                <td>
                                    <wp:ifauthorized groupName="${currentResourceGroup}"
                                                     permission="manageResources">
                                        <s:set var="canEditCurrentResource" value="%{true}" />
                                    </wp:ifauthorized>
                                    <s:if test="#canEditCurrentResource">
                                        <a href="<s:url action="edit" namespace="/do/jacms/Resource"><s:param name="resourceId" value="#currentResourceVar.id" /></s:url>"
                                           title="<s:text name="label.edit" />:&#32;<s:property value="#currentResourceVar.descr"/>">
                                            <s:property value="#currentResourceVar.descr" />
                                        </a>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#currentResourceVar.descr" />
                                    </s:else>
                                </td>
                                <td>
                                    <s:property value="#currentResourceVar.type" />
                                </td>
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                </div>
                <div class="content-view-pf-pagination clearfix">
                    <div class="form-group">
                        <span>
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        </span>
                        <div class="mt-5">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                        </div>
                    </div>
                </div>
            </wpsa:subset>
        </s:if>
        <s:else>
            <p class="margin-none text-center">
                <br>
                <s:text name="note.group.referencedResources.empty" />
            </p>
        </s:else>
    </s:form>
</div>