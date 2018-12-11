<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String cId = java.util.UUID.randomUUID().toString();
%>

<s:if test="#request['svgfix']==null">
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function () {
            var svgList = document.getElementsByTagName("svg");
            for (i = 0; i < svgList.length; i++) {
                var svg = svgList[i];
                var h = svg.getAttribute("height")
                var w = svg.getAttribute("width")
                svg.removeAttribute("height")
                svg.removeAttribute("width")
                svg.setAttribute("viewBox", "0 0 " + w + " " + h);
            }
        });
    </script>
    <s:set var="svgfix" value="true" scope="request"/>
</s:if>

<div  class="ibox float-e-margins">
    <div class="ibox-title">
        <h5>Case Instance Diagram</h5>
        <div class="ibox-tools">
            <a class="collapse-link">
                <i class="fa fa-chevron-up"></i>
            </a>
            <a class="close-link"> <i class="fa fa-times"></i> </a>
        </div>
    </div>

    <s:if test="%{null != diagram}">
        <div class="ibox-content">
            <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/ProcessDiagram/view.action"/>" method="post" class="form-horizontal" >
                <s:if test="casePath != null">
                    <s:hidden name="casePath" escapeHtml="false" escapeJavaScript="false"/>
                </s:if>
                <s:if test="knowledgeSourceId != null">
                    <s:hidden name="knowledgeSourceId" escapeHtml="false" escapeJavaScript="false"/>
                </s:if>
                <s:if test="containerid != null">
                    <s:hidden name="containerid" escapeHtml="false" escapeJavaScript="false"/>
                </s:if>
                <s:if test="channelPath != null">
                    <s:hidden name="channelPath" escapeHtml="false" escapeJavaScript="false"/>
                </s:if>
            </form>
            <s:property value="diagram" escapeHtml="false" escapeJavaScript="false"/>
        </div>
    </s:if>

</div>
