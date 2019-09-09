<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="hasActionMessages()">
    <div class="alert alert-success alert-success-bpm" role="alert">
        <s:iterator value="actionMessages">
            <p>
                <i class="fa fa-check" aria-hidden="true"></i> &nbsp;&nbsp;<s:property/>
            </p>
        </s:iterator>

    </div>

</s:if>

