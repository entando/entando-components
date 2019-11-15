<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li class="page-title-container"><s:text name="admin.jpblog.title.config"/></li>
</ol>

<h1 class="page-title-container">
    <s:text name="admin.jpblog.title.config" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="admin.jpblog.title.config.help" />" data-placement="left"
           data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="col-xs-12">
    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:form action="update" cssClass="form-horizontal">
        <s:if test="%{categories.size() == 0}">
            <div class="form-group">
                <div class="col-xs-12">
                    <label class="col-sm-2 control-label" for="category">
                        <s:text name="label.categories" />
                    </label>
                    <div class="col-sm-10 input-group">
                        <select name="catCode" id="catCode" class="form-control">
                            <option value=""><s:text name="label.all" /></option>
                            <s:iterator value="systemCategories" var="categoryVar">
                                <option <s:if test="%{catCode == #categoryVar.code}">selected="selected"</s:if> 
                                    value="<s:property value="#categoryVar.code"/>"><s:property value="%{getShortFullTitle(#categoryVar, currentLang.code)}"/></option>
                            </s:iterator>
                        </select>
                        <div class="input-group-btn">
                            <wpsf:submit type="button" action="addCategory" cssClass="btn btn-primary">
                                <span class="icon fa fa-plus"></span>&#32;
                                <s:property value="label.join" />
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </div>
        </s:if>
        <s:else>
            <div class="clearfix">
                <s:iterator value="categories" var="category">
                    <s:if test="#category!='categories'">
                        <div class="label label-default label-tag label-sm">
                            <span class="icon" />
                            <s:property value="#category"/>&#32;
                            <wpsf:hidden name="categories" value="%{#category}"/>
                            <wpsa:actionParam action="removeCategory" var="removeCategoryAction">
                                <wpsa:actionSubParam name="catCode" value="%{#category}" />
                            </wpsa:actionParam>
                            <wpsf:submit type="button" title="%{getText('label.remove') +' ' + #category}" action="%{#removeCategoryAction}" cssClass="btn btn-tag btn-link">
                                <span class="icon fa fa-times"></span>
                                <span class="sr-only">x</span>
                            </wpsf:submit>
                        </div>
                    </s:if>
                </s:iterator>
            </div>
        </s:else>
        <div class="form-group pull-right">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" action="update" cssClass="btn btn-primary">
                    <s:text name="%{getText('label.save')}" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
