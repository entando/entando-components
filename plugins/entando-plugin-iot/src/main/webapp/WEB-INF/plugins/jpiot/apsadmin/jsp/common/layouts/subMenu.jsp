<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li><a href="<s:url namespace="/do/jpiot/IotConfig" action="list" />" ><s:text name="jpiot.title.iotConfigManagement" /></a></li>
	<li><a href="<s:url namespace="/do/jpiot/IotListDevices" action="list" />" ><s:text name="jpiot.title.iotListDevicesManagement" /></a></li>
</wp:ifauthorized>
