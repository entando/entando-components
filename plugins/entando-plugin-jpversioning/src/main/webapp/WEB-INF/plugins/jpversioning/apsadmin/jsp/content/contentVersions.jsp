<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
    <legend><s:text name="jpversioning.label.history"/></legend>
    <s:set var="contentVersionsList" value="%{contentVersions}"/>

    <s:if test="%{#contentVersionsList == null || !#contentVersionsList.size() > 0}">
        <p><s:text name="jpversioning.message.no.previous.revisions"/></p>
    </s:if>
    <s:else>
        <wpsa:subset source="#contentVersionsList" count="5" objectName="groupContent" advanced="true" offset="5">
            <p class="sr-only">
                <wpsf:hidden name="contentId" value="%{content.id}"/>
            </p>

            <s:set var="group" value="#groupContent"/>

            <div class="col-xs-12 no-padding">
                <table class="table table-striped table-bordered table-hover no-mb">
                    <caption class="sr-only"><span><s:text name="title.jpversioning.versionList"/></span></caption>
                    <thead>
                    <tr>
                        <th class="text-center"><s:text name="jpversioning.version.full"/></th>
                        <th><s:text name="jpversioning.label.description"/></th>
                        <th class="text-center"><s:text name="jpversioning.label.lastVersion"/></th>
                        <th class="text-center"><s:text name="jpversioning.label.username"/></th>
                        <th class="text-center"><s:text name="label.actions"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <s:iterator var="versionId">
                        <s:set var="contentVersion" value="%{getContentVersion(#versionId)}"/>
                        <tr>
                            <td class="text-center text-nowrap"><code><s:property
                                    value="#contentVersion.version"/></code></td>
                            <td><s:property value="#contentVersion.descr"/></td>
                            <td class="text-center text-nowrap">
                                <code><s:date name="#contentVersion.versionDate" format="dd/MM/yyyy HH:mm"/></code>
                            </td>
                            <td class="text-center"><s:property value="#contentVersion.username"/></td>
                            <td class="text-center">
                                <div class="dropdown dropdown-kebab-pf">
                                    <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight"
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                        <span class="fa fa-ellipsis-v"></span>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                                        <li>
                                            <a href="<s:url action="preview" namespace="/do/jpversioning/Content/Versioning">
                                               <s:param name="versionId" value="#contentVersion.id" />
                                               <s:param name="contentId" value="%{content.id}" />
                                               <s:param name="fromEdit" value="true" />
                                               <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                                                <s:text name="label.view"/>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="<s:url action="entryRecover" namespace="/do/jpversioning/Content/Versioning" >
                                               <s:param name="versionId" value="#contentVersion.id" />
                                               <s:param name="contentId" value="%{content.id}" />
                                               <s:param name="fromEdit" value="true" />
                                               <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                                                <s:text name="jpversioning.label.restore"/>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </s:iterator>
                    </tbody>
                </table>
            </div>
            <div class="content-view-pf-pagination clearfix">
                <div class="form-group">
                    <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                    <div class="mt-5">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                    </div>
                </div>

            </div>
        </wpsa:subset>
    </s:else>
</fieldset>
