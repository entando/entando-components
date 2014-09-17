<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpcontentfeedback.title.commentsManager" />&#32;/&#32;
        <s:text name="jpcontentfeedback.title.comment.list" />
    </span>
</h1>
<div id ="main">

    <s:form action="search" cssClass="form-horizontal">
        <s:if test="hasActionMessages()">
            <div class="alert alert-info alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionMessages">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property/></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <div class="form-group">
            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span class="input-group-addon">
                    <span class="icon fa fa-file-text-o fa-lg" 
                          title="<s:text name="label.search.by" />&#32;<s:text name="jpcontentfeedback.status" />">
                    </span>
                </span>  

                <s:set var="allStatus" value="%{getAllStatus()}" />
                <wpsf:select cssClass="form-control input-lg" 
                             list="#allStatus" name="status" id="status" 
                             listKey="key" listValue="value" headerKey="" 
                             headerValue="%{getText('label.all')}" />
                <span class="input-group-btn">
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
                        <label class="control-label col-sm-2 text-right" for="comment" class=""><s:text name="jpcontentfeedback.comment" /></label>
                        <div class="col-sm-5" id="content_list-changeContentType">                 
                            <wpsf:textfield name="text" id="comment" cssClass="form-control"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="author" class="control-label col-sm-2 text-right"><s:text name="jpcontentfeedback.author" /></label>
                        <div class="col-sm-5" id="content_list-changeContentType">                 
                            <wpsf:textfield name="author" id="author" cssClass="form-control"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="from_cal" class="control-label col-sm-2 text-right"><s:text name="jpcontentfeedback.date.from" /></label>
                        <div class="col-sm-5" id="content_list-changeContentType">                 
                            <wpsf:textfield name="from" id="from_cal" value="%{from}" cssClass="form-control datepicker"/>
                            <span class="help-block">dd/MM/yyyy</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="to_cal" class="control-label col-sm-2 text-right"><s:text name="jpcontentfeedback.date.to" /></label>
                        <div class="col-sm-5" id="content_list-changeContentType">                 
                            <wpsf:textfield name="to" id="to_cal" value="%{to}" cssClass="form-control datepicker"/>
                            <span class="help-block">dd/MM/yyyy</span>
                        </div>
                    </div>    


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

        <div class="subsection-light">

            <wpsa:subset source="commentIds" count="10" objectName="groupComment" advanced="true" offset="5">
                <s:set name="group" value="#groupComment" />

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

                <s:set var="lista" value="commentIds" />
                <s:if test="!#lista.empty">
                    <table class="table table-bordered">
                        <tr>
                            <th class="text-center"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
                            <th><s:text name="jpcontentfeedback.author" /></th>
                            <th class="text-center"><s:text name="jpcontentfeedback.date.creation" /></th>
                            <th><s:text name="jpcontentfeedback.status" /></th>
                        </tr>
                        <s:iterator var="commentoId">
                            <tr>
                                <s:set var="commento" value="%{getComment(#commentoId)}" />
                                <td class="text-center">
                                    <div class="btn-group btn-group-xs">
                                        <a class="btn btn-default" 
                                           title="<s:text name="label.edit" />:&#32;<s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" />" 
                                           href="<s:url action="view"><s:param name="selectedComment" value="#commentoId" /></s:url>">
                                            <span class="icon fa fa-pencil-square-o"></span>
                                            <span class="sr-only"><s:text name="label.edit" />: <s:property value="#ideaInstance_var.code" /></span>
                                        </a>
                                    </div>
                                    <div class="btn-group btn-group-xs margin-small-left">   
                                        <a class="btn btn-warning btn-xs" 
                                           href="<s:url action="trash"><s:param name="selectedComment" value="#commentoId" /></s:url>" 
                                           title="<s:text name="label.remove" />:&#32;<s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" />">
                                            <span class="sr-only"></span>
                                            <span class="icon fa fa-times-circle-o"></span>
                                        </a>
                                    </div> 
                                </td>
                                    <td><code><s:property value="#commento.username"/></code></td>
                                <td class="text-center"><code><s:date name="#commento.creationDate" format="dd/MM/yyyy HH:mm" /></code></td>
                                <td><s:text name="%{'jpcontentfeedback.label.' + #commento.status}" /></td>
                                </tr>
                        </s:iterator>
                    </table>
                </s:if>
                <s:else><p><s:text name="jpcontentfeedback.note.list.empty" /></p></s:else>
                </div>

                <div class="pager">
                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
            </div>

        </wpsa:subset>
    </s:form>
</div>