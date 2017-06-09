<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpwebdynamicform.menu.integration"/></li>
    <li>
        <s:text name="jpwebdynamicform.menu.uxcomponents"/>
    </li>
    <li class="page-title-container">
        <s:text name="breadcrumb.messageList"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="title.message.original"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpwebdynamicform.menu.messages.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />">
                        <s:text name="breadcrumb.messageList"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpwebdynamicform/Message/Config" action="list" />">
                        <s:text name="breadcrumb.configuration"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpwebdynamicform/Entity" action="viewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>">
                        <s:text name="breadcrumb.messageType"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>

    <s:form action="search" cssClass="form-horizontal" role="search">
        <div class="searchPanel form-group">
            <div class="well col-md-offset-3 col-md-6">
                <p class="search-label col-sm-12"><s:text name="label.search.message.type"/></p>
                <div class="form-group">
                    <label class="control-label col-sm-2 text-right" for="entityTypeCode">
                        <s:text name="label.type"/>
                    </label>
                    <div class="col-sm-9 input-group input-20px-leftRight">
                        <wpsf:select cssClass="form-control" name="entityTypeCode"
                                     id="entityTypeCode" list="entityPrototypes" listKey="typeCode"
                                     listValue="typeDescr" headerKey=""
                                     headerValue="%{getText('label.all')}"/>
                                     
                        <span class="input-group-btn">
                            <wpsf:submit cssClass="btn btn-primary" value="%{getText('label.set')}"/>
                        </span>
                    </div>
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
                                    <div class="col-sm-9">
                                        <wpsf:textfield name="from" id="jpwebdynamicform_from_cal" cssClass="form-control"/>
                                    </div>
                                </div>

                                    <%-- to --%>
                                <div class="form-group">
                                    <label for="jpwebdynamicform_to_cal"
                                           class="control-label col-sm-2 text-right"><s:text name="label.to"/></label>
                                    <div class="col-sm-9">
                                        <wpsf:textfield name="to" id="jpwebdynamicform_to_cal" cssClass="form-control"/>
                                    </div>
                                </div>

                                    <%-- status --%>
                                <div class="form-group">
                                    <label for="jpwebdynamicform_status"
                                           class="control-label col-sm-2 text-right"><s:text
                                            name="label.status"/></label>
                                    <div class="col-sm-9">
                                        <select name="answered" id="jpwebdynamicform_status" class="form-control">
                                            <option value=""><s:text name="label.all"/></option>
                                            <option value="0" <s:if test="%{answered==0}">selected="selected" </s:if>>
                                                <s:text name="label.waiting"/></option>
                                            <option value="1" <s:if test="%{answered==1}">selected="selected" </s:if>>
                                                <s:text name="label.answered"/></option>
                                        </select>
                                    </div>
                                </div>

                                <!-- Filter by attribute -->
                                <s:set var="searcheableAttributes" value="searcheableAttributes"/>
                                <s:if test="null != #searcheableAttributes && #searcheableAttributes.size() > 0">
                                    <s:iterator var="attribute" value="#searcheableAttributes">
                                        <s:set var="currentFieldId">entityFinding_<s:property value="#attribute.name"/></s:set>
                                        <s:if test="#attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Monotext'">
                                            <s:set var="textInputFieldName"><s:property value="#attribute.name"/>_textFieldName</s:set>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right"
                                                       for="<s:property value="#currentFieldId" />"><s:property
                                                        value="#attribute.name"/></label>
                                                <div class="col-sm-9">
                                                    <s:set var="textInputFieldName"><s:property
                                                            value="#attribute.name"/>_textFieldName</s:set>
                                                    <wpsf:textfield id="%{#currentFieldId}" cssClass="form-control"
                                                                    name="%{#textInputFieldName}"
                                                                    value="%{getSearchFormFieldValue(#textInputFieldName)}"/><br/>
                                                </div>
                                            </div>
                                        </s:if>
                                        <s:elseif test="#attribute.type == 'Enumerator'">
                                            <s:set var="enumeratorFieldName"><s:property value="#attribute.name"/>_textFieldName</s:set>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right" 
                                                       for="${currentFieldId}">
                                                       <s:property value="#attribute.name"/>
                                                </label>
                                                <div class="col-sm-9">
                                                <wpsf:select name="%{#enumeratorFieldName}" 
                                                    id="%{#currentFieldId}" 
                                                    headerKey="" 
                                                    headerValue="%{getText('label.none')}" 
                                                    list="#attribute.items" 
                                                    value="%{getSearchFormFieldValue(#enumeratorFieldName)}" 
                                                    cssClass="form-control" />
                                                </div>
                                            </div>
                                        </s:elseif>

                                        <s:elseif test="#attribute.type == 'EnumeratorMap'">
                                            <s:set var="enumeratorMapFieldName"><s:property value="#attribute.name"/>_textFieldName</s:set>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right" 
                                                       for="${currentFieldId}">
                                                       <s:property value="#attribute.name"/>
                                                </label>
                                                <div class="col-sm-9">
                                                    <wpsf:select name="%{#enumeratorMapFieldName}" 
                                                        id="%{#currentFieldId}" 
                                                         headerKey="" headerValue="%{getText('label.none')}" 
                                                         list="#attribute.mapItems" listKey="key" listValue="value" 
                                                         value="%{getSearchFormFieldValue(#enumeratorMapFieldName)}" cssClass="form-control" />
                                                </div>
                                            </div>
                                        </s:elseif>

                                        <s:elseif test="#attribute.type == 'Date'">
                                            <s:set var="dateStartInputFieldName"><s:property
                                                    value="#attribute.name"/>_dateStartFieldName</s:set>
                                            <s:set var="dateEndInputFieldName"><s:property value="#attribute.name"/>_dateEndFieldName</s:set>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right"
                                                       for="<s:property value="#currentFieldId" />_start_cal"><s:property
                                                        value="#attribute.name"/>&#32;<s:text name="Start"/></label>
                                                <div class="col-sm-9">
                                                    <wpsf:textfield id="%{#currentFieldId}_dateStartFieldName_cal"
                                                                    cssClass="form-control datepicker"
                                                                    name="%{#dateStartInputFieldName}"
                                                                    value="%{getSearchFormFieldValue(#dateStartInputFieldName)}"/>
                                                    <span class="help-block">dd/MM/yyyy</span>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right"
                                                       for="<s:property value="#currentFieldId" />_end_cal"><s:property
                                                        value="#attribute.name"/>&#32;<s:text name="End"/></label>
                                                <div class="col-sm-9">
                                                    <wpsf:textfield id="%{#currentFieldId}_end_cal"
                                                                    cssClass="form-control datepicker"
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
                                                       for="<s:property value="#currentFieldId" />_start"><s:property
                                                        value="#attribute.name"/>&#32;<s:text
                                                        name="Start"/></label>
                                                <div class="col-sm-9">
                                                    <wpsf:textfield id="%{#currentFieldId}_start"
                                                                    cssClass="form-control"
                                                                    name="%{#numberStartInputFieldName}"
                                                                    value="%{getSearchFormFieldValue(#numberStartInputFieldName)}"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right"
                                                       for="<s:property value="#currentFieldId" />_end"><s:property
                                                        value="#attribute.name"/>&#32;<s:text
                                                        name="End"/></label>
                                                <div class="col-sm-9">
                                                    <wpsf:textfield id="%{#currentFieldId}_end" cssClass="form-control"
                                                                    name="%{#numberEndInputFieldName}"
                                                                    value="%{getSearchFormFieldValue(#numberEndInputFieldName)}"/>
                                                </div>
                                            </div>
                                        </s:elseif>

                                        <s:elseif test="#attribute.type == 'Boolean'">
                                            <s:set var="booleanInputFieldName" >
                                                <s:property value="#attribute.name" />_booleanFieldName
                                            </s:set>
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right"
                                                       for="<s:property value="#currentFieldId"/>"><s:property
                                                        value="#attribute.name"/></label>&nbsp;
                                                <div class="col-sm-9">
                                                        <s:if test="%{getSearchFormFieldValue(#booleanInputFieldName) != null}">
                                                            <wpsf:checkbox name="%{#booleanInputFieldName}" id="%{#currentFieldId}" checked="true" />
                                                        </s:if>
                                                        <s:else>
                                                            <wpsf:checkbox name="%{#booleanInputFieldName}" id="%{#currentFieldId}" />
                                                        </s:else>
                                                </div>
                                            </div>
                                        </s:elseif>

                                        <s:elseif test="#attribute.type == 'ThreeState'">
                                            <div class="form-group">
                                                <label class="control-label col-sm-2 text-right"><s:property
                                                        value="#attribute.name"/></label>
                                                <div class="col-sm-9">
                                                    <label for="none_<s:property value="%{#currentFieldId}" />"
                                                           class="radio-inline">
                                                        <wpsf:radio name="%{#attribute.name}"
                                                                    id="none_%{#currentFieldId}" value=""
                                                                    checked="%{getSearchFormFieldValue(#attribute.name) == null}"
                                                                    type="radio"/>
                                                        <s:text name="label.bothYesAndNo"/>
                                                    </label>
                                                    <label for="true_<s:property value="%{#currentFieldId}" />"
                                                           class="radio-inline">
                                                        <wpsf:radio name="%{#attribute.name}"
                                                                    id="true_%{#currentFieldId}" value="true"
                                                                    checked="%{(getSearchFormFieldValue(#attribute.name) != null) && (getSearchFormFieldValue(#attribute.name) == 'true')}"
                                                                    type="radio"/>
                                                        <s:text name="label.yes"/>
                                                    </label>
                                                    <label for="false_<s:property value="%{#currentFieldId}" />"
                                                           class="radio-inline">
                                                        <wpsf:radio name="%{#attribute.name}"
                                                                    id="false_%{#currentFieldId}" value="false"
                                                                    checked="%{(getSearchFormFieldValue(#attribute.name) != null) && (getSearchFormFieldValue(#attribute.name) == 'false')}"
                                                                    type="radio"/>
                                                        <s:text name="label.no"/>
                                                    </label>
                                                </div>
                                            </div>
                                        </s:elseif>
                                    </s:iterator>
                                </s:if>
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
        <div class="alert alert-info">
            <span class="pficon pficon-info"></span>
                <s:text name="note.message.list.none"/>
            </div>
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
                    <table class="table table-striped table-bordered table-hover no-mb" id="messageListTable">
                        <thead>
                        <tr>
                            <th class="col-xs-6"><s:text name="label.request"/></th>
                            <th class="col-xs-4 text-center"><s:text name="label.creationDate"/></th>
                            <th class="col-xs-1 text-center"><s:text name="label.status"/></th>
                            <th class="col-xs-1 text-center"><s:text name="label.actions"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <s:iterator var="messageId">
                            <s:set var="message" value="%{getMessage(#messageId)}"/>
                            <s:set var="answers" value="%{getAnswers(#messageId)}"/>
                            <tr>
                                <td>
                                    <s:property value="#message.id"/>&#32;&ndash;&#32;<s:property
                                        value="#message.typeDescr"/>
                                </td>
                                <td class="text-center">
                                    <s:date name="#message.creationDate" format="dd/MM/yyyy HH:mm"/>
                                </td>
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
                                <td class="text-center">
                                    <div class="dropdown dropdown-kebab-pf">
                                        <p class="sr-only"><s:text name="label.actions"/></p>
                                        <span class="btn btn-menu-right dropdown-toggle" type="button"
                                              data-toggle="dropdown" aria-haspopup="true"
                                              aria-expanded="false">
                                                                <span class="fa fa-ellipsis-v"></span>
                                                            </span>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <li>
                                                <a href="<s:url action="newAnswer"><s:param name="id" value="#message.id"/></s:url>"
                                                   title="<s:text name="label.newAnswer.at" />:&#32;<s:property value="#message.id" />">
                                                    <s:text name="label.newAnswer"/>
                                                    <span class="sr-only"><s:text name="label.newAnswer"/></span>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="<s:url action="view"><s:param name="id" value="#message.id"></s:param></s:url>"
                                                   title="<s:text name="label.view"/>&#32;<s:property value="#message.id"/>">
                                                    <s:text name="label.view"/>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="<s:url action="trash"><s:param name="id" value="#message.id"/></s:url>"
                                                   title="<s:text name="label.remove" />: <s:property value="#message.id" />">
                                                    <s:text name="label.remove"/>
                                                    <span class="sr-only"><s:text name="label.remove"/></span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                    <div class="content-view-pf-pagination table-view-pf-pagination clearfix">
                        <div class="form-group">
                                <span><s:include
                                        value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/></span>
                            <div class="mt-5">
                                <s:include
                                        value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp"/>
                            </div>
                        </div>
                    </div>
                    <br>
                </wpsa:subset>
            </s:form>
        </s:else>
    </div>
</div>
