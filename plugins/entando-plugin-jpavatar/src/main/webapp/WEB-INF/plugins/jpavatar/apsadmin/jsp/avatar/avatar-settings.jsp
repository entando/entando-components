<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpavatar.admin.menu.integration"/></li>
    <li>
        <s:text name="breadcrumb.integrations.components"/>
    </li>
    <li class="page-title-container">
        <s:text name="title.avatarManagement"/>
    </li>
    <li class="page-title-container">
        <s:text name="title.avatar.settings"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="title.avatar.settings"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="title.avatarManagement.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url namespace="/do/jpavatar/Config" action="management" />"><s:text name="title.avatarManagement"/></a>
                </li>
                <li class="active">
                    <a href="<s:url namespace="/do/jpavatar/Config" action="edit" />"><s:text name="title.avatar.settings"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>
<div class="col-xs-12">
    <s:form cssClass="form-horizontal" namespace="/do/jpavatar/Avatar" action="saveConfig" method="post" enctype="multipart/form-data">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <fieldset class="form-group">
            <div class="col-xs-2 control-label">
                <span class="display-block"><s:text name="label.chooseYourEditor"/></span>
            </div>
            <div class="col-xs-10 ">
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default <c:if test="${style == 'local'}"> active </c:if>">
                            <input type="radio" class="radiocheck"
                                   name="avatarConfig.style" id="local_avatarConfig_style" value="local" />
                        <%--<s:if test="systemParams['hypertextEditor'] == 'none'">checked="checked"</s:if> />--%>
                        <c:if test="${style == 'local'}"> checked="checked" </c:if>
                        <s:text name="label.avatarConfig.style.local"/>
                    </label>
                    <label class="btn btn-default <c:if test="${style == 'gravatar'}"> active </c:if>">
                            <input type="radio" class="radiocheck"
                                   name="avatarConfig.style" id="local_avatarConfig_style" value="local" />
                        <%--<s:if test="systemParams['hypertextEditor'] == 'fckeditor'">checked="checked"</s:if> />--%>
                        <c:if test="${style == 'gravatar'}"> checked="checked" </c:if>
                        <s:text name="label.avatarConfig.style.gravatar"/>
                    </label>
                </div>
            </div>
        </fieldset>

        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                    <span class="icon fa fa-floppy-o"></span>&#32;
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
