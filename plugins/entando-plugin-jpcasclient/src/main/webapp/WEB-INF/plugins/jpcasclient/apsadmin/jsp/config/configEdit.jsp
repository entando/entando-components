<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li class="page-title-container"><s:text name="jpcasclient.admin.menu" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpcasclient.admin.menu" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="jpcasclient.title.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<!-- Default separator -->
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div class="mb-20">
    <p>
        <s:text name="jpcasclient.label.casclientConfig.intro" />
    </p>

    <s:form action="save" cssClass="form-horizontal">
        <div class="col-md-12">
            <s:if test="hasActionMessages()">
                <div class="alert alert-success">
                    <span class="pficon pficon-ok"></span>
                    <strong><s:text name="messages.confirm" /></strong>
                    <ul>
                        <s:iterator value="actionMessages">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>
            <s:if test="hasFieldErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="message.title.FieldErrors" /></strong>
                    <ul>
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                                <li>
                                    <s:property escapeHtml="false" />
                                </li>
                            </s:iterator>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="message.title.ActionErrors" /></strong>
                    <ul>
                        <s:iterator value="actionErrors">
                            <li>
                                <s:property escapeHtml="false" />
                            </li>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>

            <legend style="text-transform: capitalize">
                <s:text name="jpcasclient.legend.status" />
            </legend>

            <div class="form-group">
                <label class="col-sm-2 control-label">
                    <s:text name="label.active" />
                </label>
                <div class="col-sm-10">
                    <input type="hidden" value="<s:property value="config.active" />"  id="active" name="config.active">
                    <input type="checkbox" value="<s:property value="config.active" />" id="ch_active" class="bootstrap-switch"
                           <s:if test="config.active">checked="checked"</s:if> >
                    </div>
                </div>

                <legend>
                <s:text name="jpcasclient.legend.configuration" />
            </legend>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="casLoginURL">
                    <s:text name="jpcasclient.label.casLoginURL" />:&nbsp;
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield useTabindexAutoIncrement="true" name="config.casLoginURL" id="casLoginURL" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="casLogoutURL" class="col-sm-2 control-label"><s:text name="jpcasclient.label.casLogoutURL" />:</label>
                <div class="col-sm-10">
                    <wpsf:textfield useTabindexAutoIncrement="true" name="config.casLogoutURL" id="casLogoutURL" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="casValidateURL" class="col-sm-2 control-label"><s:text name="jpcasclient.label.casValidateURL" />:</label>
                <div class="col-sm-10">
                    <wpsf:textfield useTabindexAutoIncrement="true" name="config.casValidateURL" id="casValidateURL" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="serverBaseURL" class="col-sm-2 control-label"><s:text name="jpcasclient.label.serverBaseURL" />:</label>
                <div class="col-sm-10">
                    <wpsf:textfield useTabindexAutoIncrement="true" name="config.serverBaseURL" id="serverBaseURL" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="notAuthPage" class="col-sm-2 control-label"><s:text name="jpcasclient.label.notAuthPage" />:</label>
                <div class="col-sm-10">
                    <wpsf:textfield useTabindexAutoIncrement="true" name="config.notAuthPage" id="notAuthPage" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label for="realm" class="col-sm-2 control-label"><s:text name="jpcasclient.label.realm" />:</label>
                <div class="col-sm-10">
                    <wpsf:textfield useTabindexAutoIncrement="true" name="config.realm" id="realm" cssClass="form-control" />
                </div>
            </div>

            <div class="col-md-12">
                <div class="form-group pull-right ">
                    <div class="btn-group">
                        <wpsf:submit type="button" action="save" cssClass="btn btn-primary ">
                            <s:text name="label.save" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>
    </s:form>
</div>

<script type="application/javascript" >
    $('input[type="checkbox"][id^="ch_"]').on('switchChange.bootstrapSwitch', function (ev, data) {
    var id = ev.target.id.substring(3);
    console.log("id", id);
    var $element = $('#'+id);
    $element.attr('value', ''+data);
    });
</script>
