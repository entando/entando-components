<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jacmsapsadmin" uri="/jacms-apsadmin-core"%>


<s:form action="entandoResourceSearch" cssClass="form-horizontal" role="search">
    
    <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/info-prev-value.jsp" />
    
    <p class="sr-only"><s:text name="note.chooseResourceToLink" />.</p>
    
    <p class="sr-only">
        <wpsf:hidden name="prevCode" value="%{#prevResourceVar.id}" />
        <wpsf:hidden name="activeTab" value="3" />
        <wpsf:hidden name="internalResourceActionName" value="entandoResourceSearch" />
        <wpsf:hidden name="contentOnSessionMarker" />
        <wpsf:hidden name="linkTypeVar" value="5" />
        
    </p>
    <div class="col-xs-12">
        <div class="well">
            <p class="search-label">
                <s:text name="label.search.label" />
            </p>
            <!-- Description -->
            <div class="form-group">
                <label for="text" class="control-label col-sm-2 text-right" for="text">
                    <s:text name="label.description" />
                </label>
                <div class="col-sm-9">
                    <wpsf:textfield name="text" id="text" cssClass="form-control"
                                    placeholder="%{getText('label.description')}"
                                    title="%{getText('label.search.by')+' '+getText('label.description')}" />
                </div>
            </div>
            <!-- Resource type -->
            <div class="form-group">
                <label class="control-label col-sm-2 text-right" for="resourceTypeCode">
                    <s:text name="label.type"/>
                </label>
                <div class="col-sm-9">
                    <wpsf:select name="resourceTypeCode" id="resourceTypeCode"
                                 list="resourceTypeCodes" headerKey="" 
                                 headerValue="%{getText('note.choose')}" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-9">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.search" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </div>
</s:form>

<s:form action="entandoResourceSearch" >
    <div class="col-xs-12">
        <p class="sr-only">
            <s:if test="%{null != text && !text.equals('')}">
                <wpsf:hidden name="text" />
            </s:if>
            <s:if test="%{null != resourceTypeCode && !resourceTypeCode.equals('')}">
                <wpsf:hidden name="resourceTypeCode" />
            </s:if>
            <wpsf:hidden name="activeTab" value="3" />
            <wpsf:hidden name="internalResourceActionName" value="entandoResourceSearch" />
            <wpsf:hidden name="contentOnSessionMarker" />
            <wpsf:hidden name="linkTypeVar" value="5" />
            Previous Value selected: <s:property value="#prevContentVoVar.id"/> - <s:property value="#prevContentVoVar.descr"/> 
        </p>
        
        <s:set var="maxSizeVar" value="10" />
        <s:set var="paginatedResourcesIdsVar" value="%{getPaginatedResourcesId(#maxSizeVar)}" />
        <s:set var="resourcesIdsVar" value="#paginatedResourcesIdsVar.list" />
        
        <jacmsapsadmin:cmssubset pagerId="%{getPagerId()}" total="%{#paginatedResourcesIdsVar.count}" maxSize="#maxSizeVar" objectName="groupContent" offset="5" >
            <s:set var="group" value="#groupContent" />
            <s:if test="%{#resourcesIdsVar.size() > 0}">
                <div class="table-responsive no-mb">
                    <table class="table table-striped table-bordered table-hover no-mb" id="resourceListTable">
                        <thead>
                            <tr>
                                <th class="text-center w2perc"><input type="radio" disabled /></th>
                                <!-- description -->
                                <th><s:text name="label.description" /></th>
                                <!-- key -->
                                <th class="text-left w20perc"><s:text name="label.code" /></th>
                                <!-- type code -->
                                <th class="text-center w20perc"><s:text name="label.typeCode" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator var="resourceIdVar" value="#resourcesIdsVar" >
                                <s:set var="resourceVar" value="%{loadResource(#resourceIdVar)}" />
                                <tr>
                                    <td class="text-center">
                                        <input type="radio" name="resourceId" 
                                               id="resourceId_<s:property value="#resourceIdVar"/>" 
                                               value="<s:property value="#resourceIdVar"/>" />
                                    </td>
                                    <td><s:property value="#resourceVar.descr" /></td>
                                    <td class="text-left"><s:property value="#resourceIdVar"/></td>
                                    <td class="text-center"><s:property value="#resourceVar.type" /></td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                </div>
                <div class="content-view-pf-pagination table-view-pf-pagination clearfix">
                    <div class="form-group">
                        <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                        <div class="mt-5">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                        </div>
                    </div>
                </div>
                
                <!-- Link attributes -->
                <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/entando-link-attributes.jsp" />
                
                <div class="form-group mt-20">
                    <button type="submit" id="button_resourceLink" name="button_resourceLink" 
                            class="btn btn-primary pull-right">
                        <s:text name="label.save" />
                    </button>
                </div>
            </s:if>
            <s:else>
                <div class="alert alert-info">
                    <span class="pficon pficon-info"></span>
                    <strong><s:text name="label.listEmpty" /></strong>&#32;
                    <s:text name="label.noResourceFound" />
                </div>
            </s:else>
        </jacmsapsadmin:cmssubset>    
    </div>
</s:form>
