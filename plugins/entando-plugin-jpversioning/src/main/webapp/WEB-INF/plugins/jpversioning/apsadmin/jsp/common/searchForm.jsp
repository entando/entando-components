<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<div class="searchPanel form-group">
    <label for="text" class="sr-only">
        <s:text name="label.search.by"/>&#32;<s:text name="label.description"/>
    </label>

    <div class="well col-md-offset-3 col-md-6">
        <p class="search-label col-sm-12"><s:text name="label.search.label"/></p>
        <div class="form-group">
            <s:form action="search" cssClass="search-pf has-button">
                <div class="col-sm-12 has-clear">
                    <wpsf:textfield id="text"
                                    name="text"
                                    cssClass="form-control input-lg"
                                    title="%{getText('label.search.by')+' '+getText('label.description')}"
                                    placeholder="%{getText('label.search.label')}"/>
                </div>
            </s:form>
        </div>
        <div class="panel-group" id="accordion-markup" >
            <div class="panel panel-default">
                <div class="panel-heading" style="padding:0 0 10px;">
                    <p class="panel-title active" style="text-align: end">
                        <a data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne">
                            <s:text name="label.search.advanced" />
                        </a>
                    </p>
                </div>
                <s.if test="%{hasAdvancedFilters}">
                <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <%-- Type --%>
                        <div class="form-group">
                            <label for="contentType" class="control-label col-sm-2" ><s:text name="label.type" /></label>
                            <div class="col-sm-9" >
                                <wpsf:select name="contentType"
                                             id="contentType"
                                             list="contentTypes"
                                             listKey="code"
                                             listValue="descr"
                                             headerKey=""
                                             headerValue="%{getText('label.all')}"
                                             cssClass="form-control" >
                                </wpsf:select>
                            </div>
                        </div>
                    </div>
                </div>
                </s.if>
            </div>
        </div>
        <div class="col-sm-12">
            <div class="form-group">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.search" />
                </wpsf:submit>
            </div>
        </div>
    </div>
</div>