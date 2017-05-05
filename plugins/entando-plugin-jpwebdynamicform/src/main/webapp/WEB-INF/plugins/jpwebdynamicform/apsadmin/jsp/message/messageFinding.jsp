<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpavatar.admin.menu.integration"/></li>
    <li>
        <s:text name="jpavatar.admin.menu.uxcomponents"/>
    </li>
    <li class="page-title-container">
        <s:text name="breadcrumb.messageManagement"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="title.messageManagement"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="TO be inserted" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />"><s:text
                            name="breadcrumb.messageManagement"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpwebdynamicform/Message/Config" action="list" />"><s:text
                            name="breadcrumb.configuration"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>"><s:text
                            name="breadcrumb.messageType"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main">

    <%--<s:if test="hasActionErrors()">--%>
        <%--<div class="alert alert-danger alert-dismissable fade in">--%>
            <%--<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>--%>
            <%--<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors"/></h2>--%>
            <%--<ul>--%>
                <%--<s:iterator value="actionErrors">--%>
                    <%--<li><s:property escapeHtml="false"/></li>--%>
                <%--</s:iterator>--%>
            <%--</ul>--%>
        <%--</div>--%>
    <%--</s:if>--%>

    <%--<s:if test="hasFieldErrors()">--%>
        <%--<div class="alert alert-danger alert-dismissable">--%>
            <%--<button type="button" class="close" data-dismiss="alert" aria-hidden="true">--%>
                <%--<span class="pficon pficon-close"></span>--%>
            <%--</button>--%>
            <%--<span class="pficon pficon-error-circle-o"></span>--%>
            <%--<strong><s:text name="message.title.FieldErrors"/></strong>.--%>
            <%--<ul>--%>
                <%--<s:iterator value="fieldErrors">--%>
                    <%--<s:iterator value="value">--%>
                        <%--<li><s:property escapeHtml="false"/></li>--%>
                    <%--</s:iterator>--%>
                <%--</s:iterator>--%>
            <%--</ul>--%>
        <%--</div>--%>
    <%--</s:if>--%>
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />

    <s:form action="search" cssClass="form-horizontal" role="search">
        <div class="searchPanel form-group">
            <div class="well col-md-offset-3 col-md-6">
                <p class="search-label col-sm-12"><s:text name="label.search.message.type"/></p>
                <div class="form-group">
                    <%--<s:form action="search" cssClass="search-pf has-button">--%>
                        <div class="col-sm-12 has-clear">
                            <wpsf:select name="entityTypeCode" id="entityTypeCode" list="entityPrototypes"
                                         listKey="typeCode" listValue="typeDescr" headerKey=""
                                         headerValue="%{getText('label.all')}"
                                         cssClass="form-control input-lg"/>
                                         <%--disabled="!(null == entityTypeCode || entityTypeCode == '')"--%>
                            <wpsf:hidden name="entityTypeCode"/>
                        </div>
                    <%--</s:form>--%>
                </div>
                <div class="panel-group" id="accordion-markup">
                    <div class="panel panel-default">
                        <div class="panel-heading" style="padding:0 0 10px;">
                            <p class="panel-title active" style="text-align: end">
                                <a data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne">
                                    <s:text name="label.search.advanced"/>
                                </a>
                            </p>
                        </div>
                            <%--Ricerca avanzata--%>
                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                    <%-- from --%>
                                <div class="form-group">
                                    <label for="jpwebdynamicform_from_cal"
                                           class="control-label col-sm-2 text-right"><s:text name="label.from"/></label>
                                    <div class="col-sm-5">
                                        <wpsf:textfield name="from" id="jpwebdynamicform_from_cal"
                                                        cssClass="form-control"/>
                                    </div>
                                </div>

                                    <%-- to --%>
                                <div class="form-group">
                                    <label for="jpwebdynamicform_to_cal"
                                           class="control-label col-sm-2 text-right"><s:text name="label.to"/></label>
                                    <div class="col-sm-5">
                                        <wpsf:textfield name="to" id="jpwebdynamicform_to_cal" cssClass="form-control"/>
                                    </div>
                                </div>

                                    <%-- status --%>
                                <div class="form-group">
                                    <label for="jpwebdynamicform_status"
                                           class="control-label col-sm-2 text-right"><s:text
                                            name="label.status"/></label>
                                    <div class="col-sm-5">
                                        <select name="answered" id="jpwebdynamicform_status" class="form-control">
                                            <option value=""><s:text name="label.all"/></option>
                                            <option value="0" <s:if test="%{answered==0}">selected="selected" </s:if>>
                                                <s:text name="label.waiting"/></option>
                                            <option value="1" <s:if test="%{answered==1}">selected="selected" </s:if>>
                                                <s:text name="label.answered"/></option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-12">
                    <div class="form-group">
                        <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                            <s:text name="label.search"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>
    </s:form>


    <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">

        <%-- advanced search parameter --%>

        <s:set var="searcheableAttributes" value="searcheableAttributes"/>
        <s:if test="null != #searcheableAttributes && #searcheableAttributes.size() > 0">
            <p class="noscreen"><wpsf:hidden name="entityTypeCode"/></p>
            <s:iterator var="attribute" value="#searcheableAttributes">
                <s:set var="currentFieldId">entityFinding_<s:property value="#attribute.name"/></s:set>
                <s:if test="#attribute.textAttribute">
                    <s:set var="textInputFieldName"><s:property value="#attribute.name"/>_textFieldName</s:set>
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right"
                               for="<s:property value="#currentFieldId" />"><s:property
                                value="#attribute.name"/></label>
                        <div class="col-sm-5">
                            <s:set var="textInputFieldName"><s:property value="#attribute.name"/>_textFieldName</s:set>
                            <wpsf:textfield id="%{#currentFieldId}" cssClass="form-control"
                                            name="%{#textInputFieldName}"
                                            value="%{getSearchFormFieldValue(#textInputFieldName)}"/><br/>
                        </div>
                    </div>
                </s:if>

                <s:elseif test="#attribute.type == 'Date'">
                    <s:set var="dateStartInputFieldName"><s:property
                            value="#attribute.name"/>_dateStartFieldName</s:set>
                    <s:set var="dateEndInputFieldName"><s:property value="#attribute.name"/>_dateEndFieldName</s:set>
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right"
                               for="<s:property value="#currentFieldId" />_start_cal"><s:property
                                value="#attribute.name"/>&#32;<s:text name="Start"/></label>
                        <div class="col-sm-5">
                            <wpsf:textfield id="%{#currentFieldId}_dateStartFieldName_cal"
                                            cssClass="form-control datepicker" name="%{#dateStartInputFieldName}"
                                            value="%{getSearchFormFieldValue(#dateStartInputFieldName)}"/>
                            <span class="help-block">dd/MM/yyyy</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right"
                               for="<s:property value="#currentFieldId" />_end_cal"><s:property
                                value="#attribute.name"/>&#32;<s:text name="End"/></label>
                        <div class="col-sm-5">
                            <wpsf:textfield id="%{#currentFieldId}_end_cal" cssClass="form-control datepicker"
                                            name="%{#dateEndInputFieldName}"
                                            value="%{getSearchFormFieldValue(#dateEndInputFieldName)}"/>
                            <span class="help-block">dd/MM/yyyy</span>
                        </div>
                    </div>
                </s:elseif>

                <s:elseif test="#attribute.type == 'Number'">
                    <s:set var="numberStartInputFieldName"><s:property
                            value="#attribute.name"/>_numberStartFieldName</s:set>
                    <s:set var="numberEndInputFieldName"><s:property
                            value="#attribute.name"/>_numberEndFieldName</s:set>
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right"
                               for="<s:property value="#currentFieldId" />_start"><s:property value="#attribute.name"/>&#32;<s:text
                                name="Start"/></label>
                        <div class="col-sm-5">
                            <wpsf:textfield id="%{#currentFieldId}_start" cssClass="form-control"
                                            name="%{#numberStartInputFieldName}"
                                            value="%{getSearchFormFieldValue(#numberStartInputFieldName)}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right"
                               for="<s:property value="#currentFieldId" />_end"><s:property value="#attribute.name"/>&#32;<s:text
                                name="End"/></label>
                        <div class="col-sm-5">
                            <wpsf:textfield id="%{#currentFieldId}_end" cssClass="form-control"
                                            name="%{#numberEndInputFieldName}"
                                            value="%{getSearchFormFieldValue(#numberEndInputFieldName)}"/>
                        </div>
                    </div>
                </s:elseif>

                <s:elseif test="#attribute.type == 'Boolean'">
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right"
                               for="<s:property value="#currentFieldId"/>"><s:property value="#attribute.name"/></label>&nbsp;
                        <div class="col-sm-5">
                            <div class="checkbox">
                                <wpsf:checkbox name="%{#attribute.name}" id="%{#currentFieldId}"/>
                            </div>
                        </div>
                    </div>
                </s:elseif>

                <s:elseif test="#attribute.type == 'ThreeState'">
                    <div class="form-group">
                        <label class="control-label col-sm-2 text-right"><s:property value="#attribute.name"/></label>
                        <div class="col-sm-5">
                            <label for="none_<s:property value="%{#currentFieldId}" />" class="radio-inline">
                                <wpsf:radio name="%{#attribute.name}" id="none_%{#currentFieldId}" value=""
                                            checked="%{#attribute.booleanValue == null}" type="radio"/>
                                <s:text name="label.bothYesAndNo"/>
                            </label>

                            <label for="true_<s:property value="%{#currentFieldId}" />" class="radio-inline">
                                <wpsf:radio name="%{#attribute.name}" id="true_%{#currentFieldId}" value="true"
                                            checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}"
                                            type="radio"/>
                                <s:text name="label.yes"/>
                            </label>
                            <label for="false_<s:property value="%{#currentFieldId}" />" class="radio-inline">
                                <s:text name="label.no"/>
                                <wpsf:radio name="%{#attribute.name}" id="false_%{#currentFieldId}" value="false"
                                            checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}"
                                            type="radio"/>
                            </label>
                        </div>
                    </div>
                </s:elseif>
            </s:iterator>
        </s:if>
    </div>
<%--BOX CHE NON SI CAPISCE COSA FA--%>
    <s:if test="null == #searcheableAttributes || !#searcheableAttributes.size() > 0">
        <div class="panel panel-default">
            <div class="panel-body">
                <s:text name="search.advanced.choose.type"/>
            </div>
        </div>
    </s:if>
    <div class="subsection-light">
        <s:set var="entityIds" value="searchResult"/>
        <s:if test="%{#entityIds.size()==0}">
            <div class="alert alert-info"><s:text name="note.message.list.none"/></div>
        </s:if>
        <s:else>
            <s:form action="search">
                <p class="noscreen">
                    <wpsf:hidden name="entityTypeCode"/>
                    <wpsf:hidden name="from"/>
                    <wpsf:hidden name="to"/>
                    <wpsf:hidden name="answered"/>
                    <s:iterator var="attribute" value="#searcheableAttributes">
                        <s:if test="#attribute.textAttribute">
                            <s:set var="textInputFieldName"><s:property value="#attribute.name"/>_textFieldName</s:set>
                            <wpsf:hidden name="%{#textInputFieldName}"
                                         value="%{getSearchFormFieldValue(#textInputFieldName)}"/>
                        </s:if>
                        <s:elseif test="#attribute.type == 'Date'">
                            <s:set var="dateStartInputFieldName"><s:property
                                    value="#attribute.name"/>_dateStartFieldName</s:set>
                            <s:set var="dateEndInputFieldName"><s:property
                                    value="#attribute.name"/>_dateEndFieldName</s:set>
                            <wpsf:hidden name="%{#dateStartInputFieldName}"
                                         value="%{getSearchFormFieldValue(#dateStartInputFieldName)}"/>
                            <wpsf:hidden name="%{#dateEndInputFieldName}"
                                         value="%{getSearchFormFieldValue(#dateEndInputFieldName)}"/>
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Number'">
                            <s:set var="numberStartInputFieldName"><s:property value="#attribute.name"/>_numberStartFieldName</s:set>
                            <s:set var="numberEndInputFieldName"><s:property
                                    value="#attribute.name"/>_numberEndFieldName</s:set>
                            <wpsf:hidden name="%{#numberStartInputFieldName}"
                                         value="%{getSearchFormFieldValue(#numberStartInputFieldName)}"/>
                            <wpsf:hidden name="%{#numberEndInputFieldName}"
                                         value="%{getSearchFormFieldValue(#numberEndInputFieldName)}"/>
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Boolean'">
                            <%-- DA FARE --%>
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'ThreeState'">
                            <%-- DA FARE --%>
                        </s:elseif>
                    </s:iterator>
                </p>

                <wpsa:subset source="#entityIds" count="15" objectName="entityGroup" advanced="true" offset="5">
                    <s:set var="group" value="#entityGroup"/>

                    <div class="pager">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp"/>
                    </div>

                    <table class="table table-bordered" id="messageListTable">
                        <tr>
                            <td class="text-center">
                                <div class="btn-group btn-group-xs">
                                    <a class="btn btn-default" href="<s:url action="newAnswer">
                                           <s:param name="id" value="#message.id"/></s:url>" 
                                       title="<s:text name="label.newAnswer.at" />: <s:property value="#message.id" />">
                                        <span class="icon fa fa-envelope"></span>
                                        <span class="sr-only"><s:text name="label.newAnswer" /></span>
                                    </a>
                                    <a class="btn btn-default" href="<s:url action="view">
                                           <s:param name="id" value="#message.id"></s:param></s:url>" 
                                       title="<s:text name="label.view"/>&#32;<s:property value="#message.id"/>">
                                        <span class="icon fa fa-info"></span>
                                    </a>
                                </div>
                                <div class="btn-group btn-group-xs">
                                    <a class="btn btn-warning" href="<s:url action="trash"><s:param name="id" value="#message.id"/></s:url>" 
                                       title="<s:text name="label.remove" />: <s:property value="#message.id" />">
                                        <span class="icon fa fa-times-circle-o"></span>
                                        <span class="sr-only"><s:text name="label.remove" /></span>                                        
                                    </a>
                                </div>
                            </td>
                            <td><s:property value="#message.id"/>&#32;&ndash;&#32;<s:property value="#message.typeDescr"/></td>
                            <td class="text-center"><code><s:date name="#message.creationDate" format="dd/MM/yyyy HH:mm"/></code></td>
                                    <s:if test="%{#answers.size()>0}">
                                        <s:set var="iconImage">icon fa fa-check text-success</s:set>
                                <s:set var="thereIsAnswer" value="%{getText('label.answered')}" />
                            </s:if>
                            <s:else>
                                <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                                <s:set var="thereIsAnswer" value="%{getText('label.waiting')}" />
                            </s:else>
                            <td class="text-center">
                                <span class="<s:property value="iconImage" />"></span>
                                <span class="sr-only"><s:property value="thereIsAnswer" /></span>
                            </td>
                        </tr>
                        <s:iterator var="messageId">
                            <s:set var="message" value="%{getMessage(#messageId)}"/>
                            <s:set var="answers" value="%{getAnswers(#messageId)}"/>
                            <tr>
                                <td class="text-center">
                                    <div class="btn-group btn-group-xs">
                                        <a class="btn btn-default" href="<s:url action="newAnswer">
                                           <s:param name="id" value="#message.id"/></s:url>"
                                           title="<s:text name="label.newAnswer.at" />: <s:property value="#message.id" />">
                                            <span class="icon fa fa-envelope"></span>
                                            <span class="sr-only"><s:text name="label.newAnswer"/></span>
                                        </a>
                                        <a class="btn btn-default" href="<s:url action="view">
                                           <s:param name="id" value="#message.id"></s:param></s:url>"
                                           title="<s:text name="label.view"/>&#32;<s:property value="#message.id"/>">
                                            <span class="icon fa fa-info"></span>
                                        </a>
                                    </div>
                                    <div class="btn-group btn-group-xs">
                                        <a class="btn btn-warning"
                                           href="<s:url action="trash"><s:param name="id" value="#message.id"/></s:url>"
                                           title="<s:text name="label.remove" />: <s:property value="#message.id" />">
                                            <span class="icon fa fa-times-circle-o"></span>
                                            <span class="sr-only"><s:text name="label.remove"/></span>
                                        </a>
                                    </div>
                                </td>
                                <td><s:property value="#message.id"/>&#32;&ndash;&#32;<s:property
                                        value="#message.typeDescr"/></td>
                                <td class="text-center"><code><s:date name="#message.creationDate"
                                                                      format="dd/MM/yyyy HH:mm"/></code></td>
                                <s:if test="%{#answers.size()>0}">
                                    <s:set var="iconImage">icon fa fa-check text-success</s:set>
                                    <s:set var="thereIsAnswer" value="%{getText('label.answered')}"/>
                                </s:if>
                                <s:else>
                                    <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                                    <s:set var="thereIsAnswer" value="%{getText('label.waiting')}"/>
                                </s:else>
                                <td class="text-center">
                                    <span class="<s:property value="iconImage" />"></span>
                                    <span class="sr-only"><s:property value="thereIsAnswer"/></span>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>

                    <div class="pager">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp"/>
                    </div>

                </wpsa:subset>
            </s:form>
        </s:else>
    </div>
</div>
