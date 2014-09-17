<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<fieldset class="margin-large-top">
  <legend class="margin-small-bottom"><s:text name="jpfacetnav.title.contentTypes" /></legend>
  <p class="sr-only">
    <wpsf:hidden name="contentTypesFilter" />
  </p>
  <div class="input-group margin-base-vertical">
    <label for="contentTypeCode" class="sr-only"><s:text name="label.type"/></label>
      <wpsf:select name="contentTypeCode" id="contentTypeCode" list="contentTypes" listKey="code" listValue="descr"  cssClass="form-control "/>
      <span class="input-group-btn">
        <wpsf:submit action="joinContentType" type="button" cssClass="btn btn-default" >
          <span class="icon fa fa-plus-circle"></span>
          <s:text name="label.add" />
        </wpsf:submit>
      </span>
  </div>
  <s:if test="%{contentTypeCodes.size()>0}">
    <s:iterator value="contentTypeCodes" id="currentContentTypeCode" status="rowstatus">
      <wpsa:set name="currentFacet" value="%{getFacet(#currentFacetCode)}" />
      <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
        <wpsa:set name="currentContentType" value="%{getContentType(#currentContentTypeCode)}" />
        <abbr title="<s:property value="#currentContentType.descr"/>">
          <s:property value="#currentContentType.descr" />
        </abbr>&#32;
        <wpsa:actionParam action="removeContentType" var="actionName" >
          <wpsa:actionSubParam name="contentTypeCode" value="%{#currentContentTypeCode}" />
        </wpsa:actionParam>
        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #currentContentTypeCode.defaultFullTitle}" cssClass="btn btn-default btn-xs badge">
           <span class="icon fa fa-times"></span>
           <span class="sr-only">x</span>
        </wpsf:submit>
      </span>
    </s:iterator>
  </s:if>
</fieldset>
