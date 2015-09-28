<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.messageManagement" />
    </span>
</h1>
<div id="main">
    
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property escape="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:form action="search" cssClass="form-horizontal">

        <div class="form-group">
            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span class="input-group-addon">
                    <span class="icon fa fa-file-text-o fa-lg" 
                          title="<s:text name="label.search.message.type" />">
                    </span>
                </span>        

                <wpsf:select name="entityTypeCode" id="entityTypeCode" list="entityPrototypes" listKey="typeCode" listValue="typeDescr" headerKey="" headerValue="%{getText('label.all')}" disabled="!(null == entityTypeCode || entityTypeCode == '')" cssClass="form-control input-lg"/>
                <span class="input-group-btn">
                    <s:if test="!(null == entityTypeCode || entityTypeCode == '')">
                        <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" action="changeEntityType">
                            <s:text name="%{getText('label.change')}" />
                        </wpsf:submit>
                    </s:if>
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
                        <span class="sr-only"><s:text name="%{getText('label.search')}" /></span>
                        <span class="icon fa fa-search"></span>
                    </wpsf:submit>

                    <button type="button" class="btn btn-primary btn-lg dropdown-toggle" 
                            data-toggle="collapse" 
                            data-target="#search-advanced" title="Refine your search">
                        <span class="sr-only"><s:text name="title.searchFilters" /></span>
                        <span class="caret"></span>
                    </button>    
                </span>
            </div>

            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div id="search-advanced" class="collapse well collapse-input-group">
                    <div class="form-group">
                        <label for="jpwebdynamicform_from_cal" class="control-label col-sm-2 text-right"><s:text name="label.from"/></label>
                        <div class="col-sm-5">
                            <wpsf:textfield name="from" id="jpwebdynamicform_from_cal" cssClass="form-control" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="jpwebdynamicform_to_cal" class="control-label col-sm-2 text-right"><s:text name="label.to"/></label>
                        <div class="col-sm-5">
                            <wpsf:textfield name="to" id="jpwebdynamicform_to_cal" cssClass="form-control" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="jpwebdynamicform_status" class="control-label col-sm-2 text-right"><s:text name="label.status"/></label>
                        <div class="col-sm-5">
                            <select name="answered" id="jpwebdynamicform_status" class="form-control">
                                <option value="" ><s:text name="label.all" /></option>
                                <option value="0" <s:if test="%{answered==0}">selected="selected" </s:if>><s:text name="label.waiting" /></option>
                                <option value="1" <s:if test="%{answered==1}">selected="selected" </s:if>><s:text name="label.answered" /></option>
                                </select>
                            </div>
                        </div> 

                    <%-- advanced search parameter --%>

                    <s:set var="searcheableAttributes" value="searcheableAttributes" /> 
                    <s:if test="null != #searcheableAttributes && #searcheableAttributes.size() > 0">
                        <p class="noscreen"><wpsf:hidden name="entityTypeCode" /></p>
                        <s:iterator var="attribute" value="#searcheableAttributes">
                            <s:set var="currentFieldId">entityFinding_<s:property value="#attribute.name" /></s:set>
                            <s:if test="#attribute.textAttribute">
                                <s:set name="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2 text-right" for="<s:property value="#currentFieldId" />"><s:property value="#attribute.name" /></label>
                                    <div class="col-sm-5">
                                        <s:set name="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
                                        <wpsf:textfield id="%{#currentFieldId}" cssClass="form-control" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" /><br />
                                    </div>
                                </div>
                            </s:if>

                            <s:elseif test="#attribute.type == 'Date'">
                                <s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
                                <s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2 text-right" for="<s:property value="#currentFieldId" />_start_cal"><s:property value="#attribute.name" />&#32;<s:text name="Start"/></label>
                                    <div class="col-sm-5">
                                        <wpsf:textfield id="%{#currentFieldId}_dateStartFieldName_cal" cssClass="form-control datepicker" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
                                        <span class="help-block">dd/MM/yyyy</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 text-right" for="<s:property value="#currentFieldId" />_end_cal"><s:property value="#attribute.name" />&#32;<s:text name="End"/></label>
                                    <div class="col-sm-5">
                                        <wpsf:textfield id="%{#currentFieldId}_end_cal" cssClass="form-control datepicker" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
                                        <span class="help-block">dd/MM/yyyy</span>
                                    </div>
                                </div>
                            </s:elseif>

                            <s:elseif test="#attribute.type == 'Number'">
                                <s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
                                <s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2 text-right" for="<s:property value="#currentFieldId" />_start"><s:property value="#attribute.name" />&#32;<s:text name="Start"/></label>
                                    <div class="col-sm-5">
                                        <wpsf:textfield id="%{#currentFieldId}_start" cssClass="form-control" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2 text-right" for="<s:property value="#currentFieldId" />_end"><s:property value="#attribute.name" />&#32;<s:text name="End"/></label>
                                    <div class="col-sm-5">
                                        <wpsf:textfield id="%{#currentFieldId}_end" cssClass="form-control" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
                                    </div>
                                </div>
                            </s:elseif>

                            <s:elseif test="#attribute.type == 'Boolean'">
                                <div class="form-group">
                                    <label class="control-label col-sm-2 text-right" for="<s:property value="#currentFieldId"/>" ><s:property value="#attribute.name" /></label>&nbsp;
                                    <div class="col-sm-5">
                                        <div class="checkbox">
                                            <wpsf:checkbox name="%{#attribute.name}" id="%{#currentFieldId}" />
                                        </div>
                                    </div>
                                </div>
                            </s:elseif>

                            <s:elseif test="#attribute.type == 'ThreeState'">
                                <div class="form-group">
                                    <label class="control-label col-sm-2 text-right"><s:property value="#attribute.name" /></label>
                                    <div class="col-sm-5">
                                        <label for="none_<s:property value="%{#currentFieldId}" />" class="radio-inline" >
                                            <wpsf:radio name="%{#attribute.name}" id="none_%{#currentFieldId}" value="" checked="%{#attribute.booleanValue == null}" type="radio" />
                                            <s:text name="label.bothYesAndNo"/>
                                        </label>

                                        <label for="true_<s:property value="%{#currentFieldId}" />" class="radio-inline" >
                                            <wpsf:radio name="%{#attribute.name}" id="true_%{#currentFieldId}" value="true" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" type="radio" />
                                            <s:text name="label.yes"/>
                                        </label>
                                        <label for="false_<s:property value="%{#currentFieldId}" />" class="radio-inline">
                                            <s:text name="label.no"/>
                                            <wpsf:radio name="%{#attribute.name}" id="false_%{#currentFieldId}" value="false" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" type="radio" />
                                        </label>
                                    </div>
                                </div>
                            </s:elseif>
                        </s:iterator>
                    </s:if>

                    <div class="form-group">
                        <div class="col-sm-5 col-sm-offset-2">
                            <wpsf:submit type="button" cssClass="btn btn-primary">
                                <s:text name="%{getText('label.search')}" />
                                <span class="icon fa fa-search"></span>
                            </wpsf:submit>    
                        </div>    
                    </div>                                
                </div>
            </div>
        </div>
    </s:form>                    

    <s:if test="null == #searcheableAttributes || !#searcheableAttributes.size() > 0">
        <div class="panel panel-default">
            <div class="panel-body">
                <s:text name="search.advanced.choose.type" />
            </div>
        </div>
    </s:if>

    <div class="subsection-light">
        <s:set var="entityIds" value="searchResult" />
        <s:if test="%{#entityIds.size()==0}">
            <div class="alert alert-info"><s:text name="note.message.list.none" /></div>
        </s:if>
        <s:else>
            <s:form action="search" >
            <p class="noscreen">
                <wpsf:hidden name="entityTypeCode" />
                <wpsf:hidden name="from" />
                <wpsf:hidden name="to" />
                <wpsf:hidden name="answered"/>
                <s:iterator var="attribute" value="#searcheableAttributes">
                    <s:if test="#attribute.textAttribute">
                        <s:set name="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
                        <wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
                    </s:if>
                    <s:elseif test="#attribute.type == 'Date'">
                        <s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
                        <s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
                        <wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
                        <wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
                    </s:elseif>
                    <s:elseif test="#attribute.type == 'Number'">
                        <s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
                        <s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
                        <wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
                        <wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
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
                <s:set name="group" value="#entityGroup" />

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

                <table class="table table-bordered" id="messageListTable">
                    <tr>
                        <th class="text-center"><abbr title="<s:text name="label.operation" />">&ndash;</abbr></th>
                        <th><s:text name="label.request" /></th>
                        <th class="text-center"><s:text name="label.creationDate" /></th>
                        <th class="text-center"><s:text name="label.status"/></th>
                    </tr>
                    <s:iterator var="messageId">
                        <s:set name="message" value="%{getMessage(#messageId)}" />
                        <s:set name="answers" value="%{getAnswers(#messageId)}" />
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
                                        <s:set name="iconImage" id="iconImage">icon fa fa-check text-success</s:set>
                                <s:set name="thereIsAnswer" value="%{getText('label.answered')}" />
                            </s:if>
                            <s:else>
                                <s:set name="iconImage" id="iconImage">icon fa fa-pause text-warning</s:set>
                                <s:set name="thereIsAnswer" value="%{getText('label.waiting')}" />
                            </s:else>
                            <td class="text-center">
                                <span class="<s:property value="iconImage" />"></span>
                                <span class="sr-only"><s:property value="thereIsAnswer" /></span>
                            </td>
                        </tr>
                    </s:iterator>
                </table>

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

            </wpsa:subset>
            </s:form>
        </s:else>
    </div>
</div>