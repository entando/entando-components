<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />
<wp:headInfo type="JS" info="entando-misc-bootstrap/bootstrap.min.js" />
<wp:info key="langs" var="langs" />
<wp:info key="currentLang" var="currentLang" />

<a data-toggle="dropdown" class="dropdown-toggle" href="#"  title="<wp:i18n key="ESLC_LANGUAGE" />">     
    <c:choose>
        <c:when test="${sessionScope.currentUser != 'guest'}">
            <span class="block m-t-xs"> 
                <strong class="font-bold">
                    ${Session.currentUser}
                </strong>
            </span>
        </c:when>
        <c:otherwise>
            <span class="block m-t-xs"> 
                <strong class="font-bold">
                    <wp:i18n key="ESLF_SIGNIN" />
                </strong>
            </span>
        </c:otherwise>
        <span class="text-muted text-xs block">
            <wp:i18n key="ESLC_LANGUAGE" />
            <b class="caret"></b>
        </span>
    </a>
    <ul class="dropdown-menu animated fadeInRight m-t-xs">
        <c:forEach var="iteratorLang" items="${langs}" varStatus="status">
            <li<c:if test="${iteratorLang.code==currentLang}"> class="active"</c:if>>
                <a href="<wp:url lang="${iteratorLang.code}" paramRepeat="true" />">
                    <wp:i18n key="ESLC_LANG_${iteratorLang.code}" />
                </a>
            </li>
        </c:forEach>
    </ul>

