<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="admin.jpblog.title.config" />
    </span>
</h1>

<div id="main">

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none">
                <s:text name="message.title.ActionErrors" />
            </h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property escape="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none">
                <s:text name="message.title.FieldErrors" />
            </h2>
            <ul class="margin-base-vertical">
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:if test="hasActionMessages()">
        <div class="alert alert-success alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none">
                <s:text name="messages.confirm" />
            </h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionMessages">
                    <li><s:property /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:form action="update" cssClass="form-horizontal">
        <s:if test="%{categories.size() == 0}">
            <div class="form-group">
                <div class="col-xs-12">
                    <label for="category"><s:text name="label.categories" /></label>
                    <div class="input-group">
                        <wpsf:select name="catCode" id="category" list="systemCategories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
                        <div class="input-group-btn">

                            <wpsf:submit type="button" action="addCategory" cssClass="btn btn-default">
                                <span class="icon fa fa-plus"></span>&#32;
                                <s:text name="%{getText('label.join')}"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </div>
        </s:if>
        <s:else>
            <div class="clearfix">
            <s:iterator value="categories" var="category" >
                <s:if test="#category!='categories'">
                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                        <span class="icon fa fa-tag"></span>&#32;
                        <s:property value="#category"/>&#32;
                        <wpsf:hidden name="categories" value="%{#category}"/>
                        <wpsa:actionParam action="removeCategory" var="removeCategoryAction">
                            <wpsa:actionSubParam name="catCode" value="%{#category}" />
                        </wpsa:actionParam>
                        <wpsf:submit type="button" title="%{getText('label.remove') +' ' + #category}" action="%{#removeCategoryAction}" cssClass="btn btn-default btn-xs badge">
                            <span class="icon fa fa-times"></span>
                            <span class="sr-only">x</span>
                        </wpsf:submit>
                    </span>                        
                </s:if>
            </s:iterator>
            </div>
        </s:else>
        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" action="update" cssClass="btn btn-primary btn-block">
                    <span class="icon fa fa-floppy-o"></span>&#32;
                    <s:text name="%{getText('label.save')}" />
                </wpsf:submit>
            </div>
        </div>                
    </s:form>
</div>