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

<s:if test="hasFieldErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong>
            <s:text name="message.title.FieldErrors" />
        </strong>
        <ul class="margin-base-top">
            <s:iterator value="fieldErrors">
                <s:iterator value="value">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </s:iterator>
        </ul>
    </div>
</s:if>

<s:property value="renderedForm" escapeHtml="false" />
