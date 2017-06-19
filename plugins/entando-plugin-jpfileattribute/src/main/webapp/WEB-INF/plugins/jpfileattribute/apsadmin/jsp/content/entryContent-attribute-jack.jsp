<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.type == 'File'">

    <div class="form-group">
        <label class="col-sm-2 control-label">
            <s:property value="#attribute.name" />
            <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
        </label>
        <div class="col-sm-10">
            <s:include value="/WEB-INF/plugins/jpfileattribute/apsadmin/jsp/entity/modules/fileAttribute.jsp" />
        </div>
    </div>
</s:if>

