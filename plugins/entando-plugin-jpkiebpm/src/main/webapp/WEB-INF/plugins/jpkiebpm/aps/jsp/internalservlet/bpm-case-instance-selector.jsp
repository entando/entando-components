<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:if test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>

<form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceSelector/selectCaseInstance.action"/>" method="post" class="form-horizontal">
    <s:if test="knowledgeSourceId != null">
        <s:hidden name="knowledgeSourceId" escapeHtml="false" escapeJavaScript="false"/>
    </s:if>
    <s:if test="containerid != null">
        <s:hidden name="containerid" escapeHtml="false" escapeJavaScript="false"/>
    </s:if>
    <s:if test="channelPath != null">
        <s:hidden name="channelPath" escapeHtml="false" escapeJavaScript="false"/>
    </s:if>
    <div  class="ibox float-e-margins">
        <div class="ibox-title">
            <h5>Case selector</h5>
            <div class="ibox-tools">
                <a class="collapse-link"> 
                    <i class="fa fa-chevron-up"></i> 
                </a> 
                <a class="close-link"> <i class="fa fa-times"></i> </a> 
            </div>
        </div>
        <div class="ibox-content">
            <div class="panel-body">
                <div class="row">
                    <label class="control-label" for="processPath">List of case instances</label>
                    <s:select
                        list="cases" 
                        id="casePath" 
                        name="casePath" 
                        cssClass="form-control"
                        >
                    </s:select>
                    <s:if test="%{errorCode == 1}">No Kie server configuration found</s:if>
                    <s:if test="%{errorCode == 2}">No instaces availables - check the configuration</s:if>
                    <br/>
                    <wpsf:submit 
                        type="button" 
                        value="Select"
                        cssClass="btn btn-primary btn-sm  pull-right" 
                        />
                </div>
            </div>
        </div>
    </div>
</form>
