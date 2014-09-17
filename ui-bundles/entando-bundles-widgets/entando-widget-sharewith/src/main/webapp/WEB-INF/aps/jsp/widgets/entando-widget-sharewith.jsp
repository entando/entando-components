<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="CSS" info="widgets/entando-widget-sharewith/entando-widget-sharewith.css" />
<c:set var="imageUrlPath_jscode">
	window.entando_widget_sharewith_imgURL= '<wp:resourceURL />static/img/entando-widget-sharewith/';
</c:set>
<wp:headInfo type="JS_VARS" info="${imageUrlPath_jscode}" />

<wp:headInfo type="JS" info="entando-widget-sharewith/sharewith_buttons.js" />
<wp:headInfo type="JS" info="entando-widget-sharewith/sharewith.js" />

<div class="entando-widget-sharewith">
	<p class="noscreen"><wp:i18n key="ESSW_TITLE" /></p>
	<p class="entando-widget-sharewith-buttons">
		<noscript><wp:i18n key="ESSW_NOSCRIPT" /></noscript>
	</p>
</div>