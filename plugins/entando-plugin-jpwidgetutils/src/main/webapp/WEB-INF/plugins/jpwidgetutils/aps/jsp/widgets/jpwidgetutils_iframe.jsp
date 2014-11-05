<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpwu" uri="/jpwidgetutils-core" %>

<iframe src="<jpwu:iframe />" width="100%" height="<wp:currentWidget param="config" configParam="height" />">
  <p><wp:i18n key="jpwidgetutils_ERROR_NO_IFRAME" /></p>
</iframe>