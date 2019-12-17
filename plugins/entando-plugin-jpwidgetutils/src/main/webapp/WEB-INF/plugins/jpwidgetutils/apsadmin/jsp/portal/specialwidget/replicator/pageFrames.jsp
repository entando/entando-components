<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li>
        <s:url action="configure" namespace="/do/Page" var="configureURL">
            <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
        </s:url>
        <s:set var="configureTitle">
            <s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />
        </s:set>
        <a href="${configureURL}" title="${configureTitle}"><s:text name="title.configPage" /></a>
    </li>
    <li class="page-title-container"><s:text name="name.widget" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}"/>
<h1 class="page-title-container">
    <s:text name="name.widget" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" data-content="<s:text name="name.widget.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>
            
<!-- Info Details  -->
<div class="button-bar mt-20">
    <s:action namespace="/do/Page" name="printPageDetails"
              executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="currentPage.code"></s:param>
    </s:action>
</div>

<hr />




            <div class="panel panel-default">
        <div class="panel-heading">
            <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
        </div>

        <s:if test="hasActionErrors()">
            <div class="message message_error">
                <h4><s:text name="message.title.ActionErrors" /></h4>
                <ul>
                    <s:iterator value="actionErrors">
                        <li><s:property/></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <div class="panel-body">
            <fieldset class="margin-more-top">
                <legend>Frame</legend>
                <p>
                    <strong><s:text name="note.selectedPage" /></strong>:
                    <s:iterator value="langs" status="rowStatus">
                        <s:if test="#rowStatus.index != 0">, </s:if><span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span> <s:property value="targetPage.getTitles()[code]" />
                    </s:iterator>.
                </p>

                <p>
                    <s:text name="note.selectAFrame.msg" />
                </p>
                <div class="table-responsive2">
                    <table class="table table-bordered">
                        <caption><span><s:text name="label.frames" /></span></caption>
                        <tr>
                            <th><abbr title="<s:text name="name.position" />"><s:text name="name.position.abbr" /></abbr></th>
                            <th><s:text name="label.description" /></th>
                            <th><s:text name="name.widget" /></th>
                        </tr>
                        
                        <s:iterator value="targetPage.widgets" var="showlet" status="rowstatus">
                            <s:set var="frames" value="targetPage.getModel().getFrames()" ></s:set>
                            <s:set var="showletType" value="#showlet.getType()" ></s:set>
                                <tr>
                                    <td class="rightText">
                                    <s:if test="targetPage.getModel().getMainFrame() == #rowstatus.index"><img src="<wp:resourceURL/>administration/img/icons/16x16/emblem-important.png" alt="<s:text name="name.mainFrame" />: " title="<s:text name="name.mainFrame" />" /><s:property value="#rowstatus.index"/></s:if>
                                    <s:else><s:property value="#rowstatus.index"/></s:else>
                                    </td>
                                    <td>
                                        <a href="<s:url action="selectFrame" >
                                           <s:param name="frame" value="frame"/>
                                           <s:param name="pageCode" value="pageCode"/>
                                           <s:param name="widgetTypeCode" value="widgetTypeCode"/>
                                           <s:param name="pageCodeParam" value="pageCodeParam" />
                                           <s:param name="frameIdParam" value="#rowstatus.index" />
                                       </s:url>"><s:property value="targetPage.getModel().getFrames()[#rowstatus.index]"/></a>
                                </td>
                                <td>
                                    <s:if test='%{getTitle(#showletType.getCode(), #showletType.getTitles())!="" }'>
                                        <s:property value="%{getTitle(#showletType.getCode(), #showletType.getTitles())}" />
                                    </s:if>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </div>
            </fieldset>
        </div>
    </div>
