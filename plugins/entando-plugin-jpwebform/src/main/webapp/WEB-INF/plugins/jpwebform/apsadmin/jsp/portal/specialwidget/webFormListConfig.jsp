<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="viewTree" namespace="/do/Page" />" 
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>&#32;/&#32;
        <s:text name="title.configPage" />
    </span>
</h1>

<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="saveListConfig" namespace="/do/jpwebform/Page/SpecialWidget/Webform">
            <p class="noscreen"> 
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
            </p>

            <s:if test="hasFieldErrors()">
                <div class="message message_error">
                    <h4><s:text name="message.title.FieldErrors" /></h4>	
                    <ul>
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                                <li><s:property escapeHtml="false" /></li>
                                </s:iterator>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
                </div>
                <div class="panel-body">
                    <h2 class="h5 margin-small-vertical">
                        <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                        <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                    </h2>
                    <fieldset class="col-xs-12"><legend><s:text name="jpwebform.specialwidget.title.formStatus" /></legend>
                        <div class="form-group">
                            <div class="btn-group" data-toggle="buttons">
                                <label for="all_status" class="btn btn-default active">
                                    <wpsf:radio 
                                        name="status"
                                        id="all_status"
                                        value="" 
                                        checked="%{null eq status}"
                                        cssClass="radiocheck" />&#32;
                                    <s:text name="jpwebform.specialwidget.label.status.all" />
                                </label>

                                <label for="complete_status" class="btn btn-default">
                                    <wpsf:radio
                                        name="status"
                                        id="complete_status"
                                        value="true" 
                                        checked="%{status eq true}" />
                                    &#32;
                                    <s:text name="jpwebform.specialwidget.label.status.completed" />
                                </label>
                                <label for="incomplete_status" class="btn btn-default">
                                    <wpsf:radio
                                        name="status"
                                        id="incomplete_status"
                                        value="false" 
                                        checked="%{status eq false}" />
                                    &#32;
                                    <s:text name="jpwebform.specialwidget.label.status.incomplete" />
                                </label>
                            </div>                    
                        </div>
                    </fieldset>
                    <fieldset class="col-xs-12">
                        <legend class="accordion_toggler"><s:text name="jpwebform.specialwidget.title.extraOption" /></legend>
                        <div class="accordion_element">
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    <s:text name="jpwebform.specialwidget.note.extraOption.intro" />
                                </div>
                            </div>
                            <s:iterator var="lang" value="langs">
                                <div class="form-group">
                                    <label for="title_<s:property value="#lang.code" />" class="control-label">
                                        <code class="label label-info"><s:property value="#lang.code" /></code>&#32;<s:text name="label.title" />
                                    </label>
                                    <wpsf:textfield name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{showlet.config.get('title_' + #lang.code)}" cssClass="form-control" />
                                </div>
                            </s:iterator>
                            <div class="form-group">
                                <label for="pageLink" ><s:text name="jpwebform.specialwidget.label.link.page" /></label>
                                <select name="pageLink" id="pageLink" class="form-control">
                                    <s:iterator var="pages" value="pageVar">
                                        <option value=""><s:text name="label.select" /></option>
                                        <option <s:if test="%{widget.config.get('pageLink') == #pageVar.code}">selected="selected"</s:if> 
                                            value="<s:property value="#pageVar.code"/>"><s:property value="%{getShortFullTitle(#pageVar, currentLang.code)}"/></option>
                                    </s:iterator>
                                </select>
                            </div>
                            <s:iterator var="lang" value="langs">
                                <div class="form-group">
                                    <label for="linkDescr_<s:property value="#lang.code" />" class="control-label">
                                        <code class="label label-info"><s:property value="#lang.code" /></code>&#32;<s:text name="jpwebform.specialwidget.label.link.descr"/></label>
                                        <wpsf:textfield name="linkDescr_%{#lang.code}" id="linkDescr_%{#lang.code}" value="%{showlet.config.get('linkDescr_' + #lang.code)}" cssClass="form-control" />
                                </div>
                            </s:iterator>
                        </div>
                    </fieldset>
                    <fieldset class="col-xs-12">
                        <legend><s:text name="jpwebform.specialwidget.title.publishingOptions" /></legend>
                        <div class="form-group">
                            <label for="maxElemForItem"><s:text name="jpwebform.specialwidget.label.maxElementsForItem" /></label>
                            <wpsf:select name="maxElemForItem" id="maxElemForItem" value="%{getShowlet().getConfig().get('maxElemForItem')}" 
                                         headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="form-control" />
                        </div>
                        <div class="form-group">
                            <label for="maxElements"><s:text name="jpwebform.specialwidget.label.maxElements" /></label>
                            <wpsf:select name="maxElements" id="maxElements" value="%{getShowlet().getConfig().get('maxElements')}" 
                                         headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="form-control" />
                        </div>
                    </fieldset>
                    <div class="form-horizontal">        
                        <div class="form-group">
                            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                                <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="saveListConfig">
                                    <span class="icon fa fa-floppy-o"></span>&#32;
                                    <s:text name="%{getText('label.save')}"/>
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>  
                </div>
            </div>
        </s:form>
    </div>
</div>