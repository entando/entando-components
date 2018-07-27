<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Contents -->
<div class="col-xs-12 col-lg-8">
    <div class="card-pf card-pf-utilization card-pf-accented card-pf-aggregate-status">
        <h2 class="card-pf-title no-mb text-left bold">
            <s:text name="dashboard.contentStatus" />
        </h2>
        <div class="text-left"><span id="lastUpdate-contents"></span></div>
        <div class="card-pf-body" id="content-status">
            <div id="contents-donut-chart"
                 class="example-donut-chart-right-legend">
                <div class="spinner spinner-lg"></div>
            </div>
        </div>
        <wp:ifauthorized permission="manageContents">
            <s:url action="results" namespace="/do/jacms/Content" var="contentListURL" />
            <a href="${contentListURL}" class="bottom-link display-block text-right" title="<s:text name="note.goToSomewhere" />:
               <s:text name="dashboard.contents.contentList" />">
                <s:text name="dashboard.contents.contentList" />
            </a>
        </wp:ifauthorized>
    </div>
</div>
