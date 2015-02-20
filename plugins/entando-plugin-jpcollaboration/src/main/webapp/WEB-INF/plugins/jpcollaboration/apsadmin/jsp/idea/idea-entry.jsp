<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpcrowdsourcing.admin.title" />&#32;/&#32;
        <s:text name="jpcrowdsourcing.title.ideaEntry" />
    </span>
</h1>
<div id="main">

    <s:form action="save">

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
                    <s:iterator value="ActionErrors">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
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
        <fieldset class="col-xs-12">
            <legend><s:text name="label.info" /></legend>

            <p class="noscreen">
                <wpsf:hidden name="idea.id" value="%{idea.id}" />
            </p>
            <div class="form-group">
                <label for="idea_instanceCode"><s:text name="jpcrowdsourcing.label.instance"/></label>
                <wpsf:select name="idea.instanceCode" id="idea_instanceCode" list="ideaInstances" listKey="code" listValue="code" cssClass="form-control" />
            </div>
            <div class="form-group">
                <label for="idea.title"><s:text name="jpcrowdsourcing.label.title" /></label>
                <wpsf:textfield id="idea.title" name="idea.title" value="%{idea.title}" cssClass="form-control"/>
            </div>
            <div class="form-group">
                <label for="idea.descr" class="alignTop"><s:text name="jpcrowdsourcing.label.description" /></label>
                <wpsf:textarea  id="idea.descr" name="idea.descr" value="%{idea.descr}" cols="50" rows="3" cssClass="form-control" />
            </div>
            <div class="form-group">
                <s:set var="statusMap" value="#{
                       1:'jpcrowdsourcing.label.status_not_approved.singular',
                           3:'jpcrowdsourcing.label.status_approved.singular'
                       }" />
                <div class="btn-group" data-toggle="buttons">
                    <label for="idea.status.1" class="btn btn-default active">
                        <wpsf:radio name="idea.status" value="1" id="idea.status.1" checked="idea.status==1||idea.status==2" />
                        <s:text name="jpcrowdsourcing.label.status_not_approved.singular" />
                    </label>
                    <label for="idea.status.3" class="btn btn-default">
                        <wpsf:radio name="idea.status" value="3" id="idea.status.3" checked="idea.status==3" />
                        <s:text name="jpcrowdsourcing.label.status_approved.singular" />
                    </label>
                </div>                        
            </div>

            <s:if test="idea.id!=null">
                <p>
                    <s:if test="%{idea.comments.size()>0}">
                    <div class="panel panel-default panel-body"><s:property value="idea.comments.size"/>&#32;<s:text name="jpcrowdsourcing.label.comments" /></div>
                    </s:if>
                    <s:else>
                    <div class="alert alert-info"><s:text name="jpcrowdsourcing.note.nocomments" /></div>
                    </s:else>
                </p>
            </s:if>

        </fieldset>

        <fieldset class="col-xs-12">
            <legend><s:text name="jpcrowdsourcing.title.tag" /></legend>
            <div class="form-group">
                <label for="tag"><s:text name="jpcrowdsourcing.label.tagSet" /></label>
                <div class="input-group">
                    <wpsf:select list="%{getIdeaTags(false)}" name="tag" id="tag" listKey="code" listValue="title" cssClass="form-control"/>
                    <span class="input-group-btn">
                        <wpsf:submit type="button" action="joinCategory" cssClass="btn btn-default">
                            <span class="icon fa fa-plus"></span>&#32;
                            <s:text name="%{getText('label.add')}"/>
                        </wpsf:submit>
                    </span>   
                </div>
            </div>

            <s:set var="ideaCategories" value="idea.tags" />
            <s:if test="#ideaCategories != null && #ideaCategories.size() > 0">
                <s:iterator value="#ideaCategories" var="ideaCategoryCode">
                    <p class="noscreen">
                        <wpsf:hidden name="idea.tags" value="%{#ideaCategoryCode}" />
                    </p>
                </s:iterator>

                <s:iterator value="#ideaCategories" var="ideaCategoryCode">
                    <s:set var="ideaCategory" value="%{getCategory(#ideaCategoryCode)}" />
                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                        <span class="icon fa fa-tag"></span>&#32;
                        <abbr title="<s:property value="#ideaCategory.getFullTitle(currentLang.code)"/>">
                            <s:property value="#ideaCategory.getShortFullTitle(currentLang.code)" />
                        </abbr>&#32;
                        <wpsa:actionParam action="removeCategory" var="actionName" >
                            <wpsa:actionSubParam name="tag" value="%{#ideaCategory.code}" />
                        </wpsa:actionParam>
                        <!--                                        <div class="btn-group btn-group-xs margin-small-left">-->
                        <wpsf:submit cssClass="btn btn-default btn-xs badge" type="button" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ' ' + #ideaCategory.defaultFullTitle}">
                            <span class="fa fa-times-circle-o"></span>        
                        </wpsf:submit>    
                    </span>

                </s:iterator>

            </s:if>
        </fieldset>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-block" >
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>        

    </s:form>
</div>