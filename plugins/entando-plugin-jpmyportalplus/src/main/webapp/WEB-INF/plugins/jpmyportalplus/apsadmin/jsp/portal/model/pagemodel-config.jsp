<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns"/></li>
    <li>
        <a href="<s:url action="list" namespace="/do/PageModel"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageModelManagement" />">
            <s:text name="breadcrumb.integrations.components"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="jpmyportalplus.title.pageModel.configurationDetail"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpmyportalplus.title.pageModel.configurationDetail"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="jpmyportalplus.title.pageModel.configurationDetail.help" />"
               data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br/>
<br/>

<div id="main">

    <s:set var="pageModelVar" value="%{getPageModel(code)}"/>
    <div class="table-responsive">
        <table class="table table-bordered table-hover no-mb">
            <tr>
                <th class="text-right col-xs-2">
                    <s:text name="label.description"/>
                </th>
                <td class="col-xs-10">
                    <s:property value="#pageModelVar.description"/>
                </td>
            </tr>
            <tr>
                <th class="text-right col-xs-2">
                    <s:text name="label.code"/>
                </th>
                <td>
                    <code><s:property value="code"/></code>
                </td>
            </tr>
            <tr>
                <th class="text-right col-xs-2">
                    <s:text name="label.pluginCode"/>
                </th>
                <td>
                    <code><s:property value="#pageModelVar.pluginCode"/></code>
                </td>
            </tr>
        </table>
    </div>
    <br>

    <s:form action="save">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>
        <p class="sr-only">
            <wpsf:hidden name="code"/>
        </p>

        <s:set var="modelConfigurationVar" value="#pageModelVar.configuration"/>
        <s:set var="myPortalConfigurationVar" value="%{getConfiguration(code)}"/>
        <s:if test="#modelConfigurationVar.length > 0">

            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <th class="text-center col-sm-1"><s:text name="label.frame"/></th>
                            <th><s:text name="label.description"/></th>
                            <th class="text-center col-sm-1"><s:text name="label.free"/></th>
                            <th class="text-center col-sm-1"><s:text name="label.column"/></th>
                        </tr>
                    </thead>
                    <tbody>
                    <s:set var="myPortalFrameConfigurationVar" value="#myPortalConfigurationVar[#frameVar.pos]"/>
                        <s:iterator value="#modelConfigurationVar" var="frameVar">
                            <tr>
                                <td class="text-center">
                                    <s:property value="#frameVar.pos"/>
                                </td>
                                <td>
                                    <s:property value="#frameVar.description"/>
                                </td>
                                <td class="text-center">
                                    <wpsf:checkbox name="%{'freePosition_' + #frameVar.pos}"
                                                   value="%{!#myPortalFrameConfigurationVar.locked}"/>
                                </td>
                                <td class="text-center">
                                    <wpsf:select name="%{'columnPosition_' + #frameVar.pos}" headerKey="" headerValue=""
                                                 list="#{'1':'1','2':'2','3':'3','4':'4','5':'5'}"
                                                 value="%{#myPortalFrameConfigurationVar.column}"/>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>
        </s:if>
        <s:else>
            <div class="alert alert-info">
                <p>
                    <strong><s:text name="label.whoops"/></strong>&#32;<s:text
                        name="note.page.pagemodelWithoutPositions"/>
                </p>
            </div>
        </s:else>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.save"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>