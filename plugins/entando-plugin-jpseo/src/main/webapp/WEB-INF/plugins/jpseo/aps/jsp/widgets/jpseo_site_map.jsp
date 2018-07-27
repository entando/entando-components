<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpseo" uri="/jpseo-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<?xml version="1.0" encoding="UTF-8"?>
<urlset
      xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9
            http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">
<wp:currentWidget param="config" configParam="rootPage" var="rootPage" /><jpseo:siteMap var="pages" rootPage="${rootPage}"  /><c:forEach var="page" items="${pages}" ><url><loc><c:out value="${page}" /></loc></url></c:forEach></urlset>