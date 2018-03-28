<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<s:if test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>

<s:if test="#request['angular']==null">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
    <s:set var="angular" value="true" scope="request"/>
</s:if>

<div class="constainer-fluid">
    <div class="ibox">
        <div class="ibox-title">

            <h2 class="card-pf-title">
                <span>Case Instance File</span>
            </h2>
        </div>
        <div class="ibox float-e-margins">
            <div class="ibox-content">
                <form action="<s:action name="updateData" />" method="post" class="form-horizontal" >
                    <%--<s:form namespace="/ExtStr2/do/bpm/FrontEnd/CaseInstanceFile" action="updateData" method="post" cssClass="form-horizontal">--%>
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

                    <h4>Data to be added (json String, adding the same key will overwrite the existing value)</h4>

                    <%--<s:if test="casefile != null">--%>
                    <s:textarea name="data" id="data" cssClass="form-control" value="%{casefile}" />
                    <%--</s:if>--%>
                    <br />
                    <h4>Data id (key) to be deleted</h4>
                    <s:textfield name="dataId" id="dataId" cssClass="form-control"/>

                    <%--<wpsf:submit type="button" action="updateData" name="updateData" cssClass="btnv btn-sm btn-link" >--%>
                    <!--<small>Edit</small>-->
                    <%--</wpsf:submit>--%>
                    <button type="submit" name="updateData" class="btnv btn-sm btn-link" >Update</button>
                    <button type="submit" name="deleteData" formaction="<s:action name="deleteData" />" class="btnv btn-sm btn-link" >Remove</button>

                    <%--<wpsf:submit type="button" action="deleteData" name="deleteData" ccsClass="btn btn-link"  >--%>
                    <!--<small>Remove</small>-->
                    <%--</wpsf:submit>--%>
                    <%--</s:form>--%>
                </form>

                <!--configuration-->
                <s:if test="frontEndCaseData != null">
                    <s:property value="frontEndCaseData" escapeHtml="false" escapeJavaScript="false"/>
                </s:if>
                <br />
                 <s:if test="frontEndCaseData != null">
                    <s:property value="frontEndCaseData" escapeHtml="false" escapeJavaScript="false"/>
                </s:if>
                <br />
                <!--Milestone data-->
                <s:if test="casefile != null">
                    <s:property value="casefile" escapeHtml="false" escapeJavaScript="false"/>
                </s:if>
                <br />
          
            </div>
        </div>
    </div>
</div>