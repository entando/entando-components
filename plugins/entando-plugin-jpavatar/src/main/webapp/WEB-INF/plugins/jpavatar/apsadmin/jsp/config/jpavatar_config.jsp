<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.avatarManagement" />&#32;/&#32;
        <s:text name="label.avatar.config" />
    </span>
</h1>
<div id="main">
    <s:form action="save" >
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
                        <li><s:property escape="false" /></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-success alert-dismissable">
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
            <legend><s:text name="legend.config" /></legend>
            <div class="form-group">
                <span class="important"><s:property value="SYTLE" /></span>
            </div>
            <div class="form-group">
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default <s:if test="%{null == avatarConfig.style || avatarConfig.style == 'default'}"> active</s:if>">
                        <s:text name="label.avatarConfig.style.default"/>
                        <wpsf:radio name="avatarConfig.style" value="default" id="default_avatarConfig_style" checked="%{null == avatarConfig.style || avatarConfig.style == 'default'}" cssClass="radiocheck" />
                    </label>
                    <label class="btn btn-default <s:if test="%{avatarConfig.style == 'local'}"> active</s:if>">
                        <s:text name="label.avatarConfig.style.local"/>
                        <wpsf:radio name="avatarConfig.style" value="local" id="local_avatarConfig_style" checked="%{avatarConfig.style == 'local'}" cssClass="radiocheck" />
                    </label>
                    <label class="btn btn-default <s:if test="%{avatarConfig.style == 'gravatar'}"> active</s:if>">
                        <s:text name="label.avatarConfig.style.gravatar"/>
                        <wpsf:radio name="avatarConfig.style" value="gravatar" id="gravatar_avatarConfig_style" checked="%{avatarConfig.style == 'gravatar'}" cssClass="radiocheck" />
                    </label>
                </div>
            </div>
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