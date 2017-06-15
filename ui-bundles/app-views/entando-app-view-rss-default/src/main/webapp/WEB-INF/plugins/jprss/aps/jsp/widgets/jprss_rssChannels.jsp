<%@ taglib prefix="jprss" uri="/jprss-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="<wp:resourceURL />plugins/jprss/administration/common/css/jprss-administration.css">

<jprss:rssList listName="rssList" />
<wp:info key="langs" var="langs" />

<p class="rssChannelTitle"><wp:i18n key="jprss_FEED_LIST"/></p>
<dl class="dl-horizontal">
    <c:if test="${!(empty rssList)}">
        <c:forEach var="channel" items="${rssList}">
            <p class="rssTitle"><c:out value="${channel.title}"/></p>
            <p class="rssChannelDescr"><c:out value="${channel.description}"/></p>
            <ul class="unstyled inline margin-medium-bottom">
                <c:forEach var="channelLang" items="${langs}">
                    <li class="inline rss">
                        <a href="<wp:info key="systemParam" paramName="applicationBaseURL"/>do/jprss/Rss/Feed/show.action?id=<c:out value="${channel.id}"/>&amp;lang=<c:out value="${channelLang.code}"/>"
                            class="badge">
                            <c:out value="${channelLang.descr}"/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </c:forEach>
    </c:if>

