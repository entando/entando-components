<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="hasActionMessages()">
   <div class="alert alert-success" role="alert">
        <s:iterator value="actionMessages">
            <p>
            <s:property/>
            </p>
        </s:iterator>

   </div>

</s:if>
<s:property value="renderedForm" />
