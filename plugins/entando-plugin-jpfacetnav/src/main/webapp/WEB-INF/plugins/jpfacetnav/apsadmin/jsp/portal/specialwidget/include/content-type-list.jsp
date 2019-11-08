<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<fieldset class="col-xs-12 no-padding">
    <legend>
        <s:text name="jpfacetnav.title.contentTypes" />
    </legend>
    <p class="sr-only">
        <wpsf:hidden name="contentTypesFilter" />
    </p>
    <div class="form-group">
        <label for="contentType" class="col-sm-2 control-label">
            <s:text name="label.type" />
        </label>
        <div class="col-sm-10">
            <div class="input-group mb-20">
                <wpsf:select name="contentTypeCode" id="contentTypeCode" list="contentTypes" listKey="code"
                    listValue="descr" cssClass="form-control " />
                <span class="input-group-btn">
                    <wpsf:submit action="joinContentType" type="button" cssClass="btn btn-primary">
                        <s:text name="label.add" />
                    </wpsf:submit>
                </span>
            </div>
            <s:if test="%{contentTypeCodes.size()>0}">
                <h3 class="sr-only">
                    <s:text name="title.contentType.list" />
                </h3>
                <h4>
                    <s:text name="jpfacetnav.title.contentTypes.associated" />
                </h4>
                <ul class="list-inline">
                    <s:iterator value="contentTypeCodes" var="currentContentTypeCode" status="rowstatus">
                        <wpsa:set name="currentFacet" value="%{getFacet(#currentFacetCode)}" />
                        <li>
                            <span class="label label-tag">
                                <wpsa:set name="currentContentType" value="%{getContentType(#currentContentTypeCode)}" />
                                <abbr title="<s:property value="#currentContentType.descr"/>">
                                    <s:property value="#currentContentType.descr" />
                                </abbr>
                                &#32; &#32;
                                <wpsa:actionParam action="removeContentType" var="actionName">
                                    <wpsa:actionSubParam name="contentTypeCode" value="%{#currentContentTypeCode}" />
                                </wpsa:actionParam>
                                <wpsf:submit type="button" action="%{#actionName}" 
                                    title="%{getText('label.remove') + ' ' + getDefaultFullTitle(#currentContentTypeCode)}"
                                    cssClass="btn btn-link">
                                    <span class="pficon pficon-close white"></span>
                                    <span class="sr-only">x</span>
                                </wpsf:submit>
                            </span>
                        </li>
                    </s:iterator>
                </ul>
            </s:if>
        </div>
    </div>
</fieldset>
