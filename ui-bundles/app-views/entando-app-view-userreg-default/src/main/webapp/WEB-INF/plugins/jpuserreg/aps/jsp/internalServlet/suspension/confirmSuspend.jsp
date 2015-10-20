<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h1><wp:i18n key="jpuserreg_SUSPENDING_CONFIRM_MSG"/></h1>
<p><wp:i18n key="jpuserreg_SUSPENDING_CONFIRM_INTRO"/>You will be logged out and redirect to home page</p>

<s:if test="hasFieldErrors()">
    <div class="alert alert-block">
        <h2><wp:i18n key="ERRORS" /></h2>
        <ul>
            <s:iterator value="fieldErrors">
                <s:iterator value="value">
                    <s:set name="label" ><s:property/></s:set>
                    <li><s:property /></li>
                </s:iterator>
            </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasActionErrors()">
    <div class="alert alert-block">
        <h2><wp:i18n key="ERRORS" /></h2>
        <ul>
            <s:iterator value="actionErrors">
                <s:set name="label" ><s:property/></s:set>
                <li><s:property /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>

<form method="post" action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/suspend.action" />"  class="form-horizontal" >
      <div class="control-group">
        <label for="password" class="control-label"><wp:i18n key="jpuserreg_PASSWORD"/>&nbsp;<abbr class="icon icon-asterisk" title="<wp:i18n key="jpuserreg_REQUIRED" />"><span class="noscreen"><wp:i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
        <div class="controls">
            <wpsf:password useTabindexAutoIncrement="true" name="password" required="true" id="password" />
        </div>
    </div>
    <p class="form-actions">
        <input type="submit" value="<wp:i18n key="jpuserreg_SUSPEND" />" cssClass="btn btn-primary"/>
    </p>
</form>