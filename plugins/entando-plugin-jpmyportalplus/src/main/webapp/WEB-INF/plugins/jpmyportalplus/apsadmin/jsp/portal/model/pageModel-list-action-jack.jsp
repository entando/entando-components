<%@ taglib prefix="s" uri="/struts-tags" %>
<a href="<s:url namespace="/do/jpmyportalplus/modelconfig" action="edit"><s:param name="code" value="#pageModelVar.code"/></s:url>"
   title="<s:text name="jpmyportalplus.label.editPageModel" />:&#32;<s:property value="#pageModelVar.description" />&#32;(<s:property value="#pageModelVar.code" />)">
    <s:text name="jpmyportalplus.label.editPageModel"/>:&#32;<s:property value="#pageModelVar.description"/>
</a>
