<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>

<script src="<wp:resourceURL />plugins/jpkiebpm/administration/js/jbpm-component-config.js"></script>-->



<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />:
           <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.configPage"/>
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.configPage"/>
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div class="mb-20" >

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmCaseInstanceSelectorViewer" class="form-horizontal">
        <p class="noscreen">
            <wpsf:hidden name="pageCode"/>
            <wpsf:hidden name="frame"/>
            <wpsf:hidden name="widgetTypeCode"/>
        </p>

        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp"/>
            </div>
            <div class="panel-body">
                <p class="h5 margin-small-vertical">
                    <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}"/>
                </p>
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <strong><s:text name="message.title.FieldErrors"/></strong>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property/></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <s:set var="isknowledgeSourcePathSetted" value="%{knowledgeSourcePath != null && knowledgeSourcePath != ''}"/>
                <s:set var="isProcessPathSetted" value="%{processPath != null && processPath != ''}"/>
                <s:set var="isChannelSetted" value="%{channel != null && channel != ''}"/>


                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-8 col-md-8 col-xs-8">

                            <div class="form-group">
                                <label for="Knowledge Source"><s:text name="Knowledge Source"/></label>
                                <div class="input-group">

                                    <s:select list="knowledgeSource" id="knowledgeSourcePath" name="knowledgeSourcePath"  
                                              listKey="value.id"
                                              listValue="value.name" class="form-control">
                                    </s:select>
                                    <span class="input-group-btn">
                                        <s:if test="#isknowledgeSourcePathSetted">
                                            <wpsf:submit action="changeKnowledgeSourceForm" value="Change Knowledge Source"
                                                         cssClass="btn btn-warning"/>
                                        </s:if>
                                        <s:else>

                                            <wpsf:submit action="chooseKnowledgeSourceForm" value="Choose Knowledge Source"
                                                         cssClass="btn btn-success"/>
                                        </s:else>
                                    </span>
                                </div>
                            </div>
                        </div>

                    </div>

                    <s:if test="#isknowledgeSourcePathSetted">
                        <div class="row">
                            <div class="col-lg-8 col-md-8 col-xs-8">

                                <div class="form-group">
                                    <label for="Deployment Unit"><s:text name="Deployment Unit"/></label>
                                    <div class="input-group">

                                        <s:select list="process" id="container-id" name="processPath"  
                                                  listKey="containerId"
                                                  listValue="containerId" class="form-control">
                                        </s:select>                                        
                                        <span class="input-group-btn">
                                            <s:if test="#isProcessPathSetted">
                                                <wpsf:submit action="changeForm" value="Change Deployment Unit"
                                                             cssClass="btn btn-warning"/>
                                            </s:if>
                                            <s:else>
                                                <wpsf:submit action="chooseForm" value="Choose Deployment Unit"
                                                             cssClass="btn btn-success"/>
                                            </s:else>
                                        </span>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </s:if>

                    <s:if test="#isProcessPathSetted">
                        <div class="row">
                            <div class="col-lg-8 col-md-8 col-xs-8">
                                <div class="form-group">
                                    <label for="channel">Channel</label>
                                    <div class="input-group">
                                        <s:select list="channels" id="channel" name="channel" class="form-control">
                                        </s:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </s:if>

                </div>
            </div>
        </div>
        <s:if test="frontEndCaseData != null">
            <wpsf:hidden name="frontEndCaseData"/>
        </s:if>        
        <s:if test="channel != null">
            <wpsf:hidden name="channel"/>
        </s:if>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:submit disabled="!#isProcessPathSetted" type="button" cssClass="btn btn-primary pull-right"
                                 action="save">
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>

    </s:form>
    <pre style="display:none;">
    <!--Saved Configurations-->
    <br />
    <br />
    <!--kieContainerListJson value-->
    kieContainerListJson value
    <br />
        <s:if test="kieContainerListJson != null">
            <s:property value="kieContainerListJson" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
    <!--/kieContainerListJson value-->
    <br />
    <br />
    <!--knowledgeSourceJson value-->
    knowledgeSourceJson value
    <br />
        <s:if test="knowledgeSourceJson != null">
            <s:property value="knowledgeSourceJson" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
    <!--/knowledgeSourceJson value-->
    </pre>
</div>
