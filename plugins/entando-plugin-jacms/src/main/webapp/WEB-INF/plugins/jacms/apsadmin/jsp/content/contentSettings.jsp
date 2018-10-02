<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app"/>
    </li>
    <li>
        <s:text name="breadcrumb.jacms"/>
    </li>
    <li class="page-title-container">
        <s:text name="menu.contents.settings" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="menu.contents.settings" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="note.reload.contentReferences.help" />" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:form class="form-horizontal" action="updateSystemParams">
        <s:if test="hasActionMessages()">
            <div class="alert alert-success alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-ok"></span>
                <strong><s:text name="messages.confirm"/></strong>
                <s:iterator value="actionMessages">
                    <span><s:property escapeHtml="false"/></span>
                </s:iterator>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.ActionErrors" /></strong>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <div class="form-group">
            <div class="col-xs-2 control-label">
                <span class="display-block"><s:text name="note.reload.contentReferences.start"/></span>
            </div>
            <div class="col-xs-10 ">
                <div class="btn-group">
                    <s:if test="contentManagerStatus == 1">
                        <p class="text-info">
                            <a class="btn btn-primary" href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" title="<s:text name="note.reload.contentReferences.refresh" />">
                                <s:text name="label.refresh" />
                            </a>
                            &#32;(<s:text name="note.reload.contentReferences.status.working" />)
                        </p>
                    </s:if>
                    <s:else>
                        <p>
                            <a class="btn btn-primary" href="<s:url action="reloadContentsReference" namespace="/do/jacms/Content/Admin" />">
                                <s:text name="note.reload.contentReferences.start" />
                            </a>
                            &#32;(
                            <s:if test="contentManagerStatus == 2">
                                <span class="text-info"><s:text name="note.reload.contentReferences.status.needToReload" /></span>
                            </s:if>
                            <s:elseif test="contentManagerStatus == 0">
                                <s:text name="note.reload.contentReferences.status.ready" />
                            </s:elseif>
                            )
                        </p>
                    </s:else>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-2 control-label">
                <span class="display-block"><s:text name="title.reload.contentIndexes"/></span>
            </div>
            <div class="col-xs-10 ">
                <div class="btn-group">
                    <s:if test="searcherManagerStatus == 1">
                        <p class="text-info">
                            <a class="btn btn-primary" href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" title="<s:text name="note.reload.contentIndexes.refresh" />">
                                <s:text name="label.refresh" />
                            </a>
                            &#32;(<s:text name="note.reload.contentIndexes.status.working" />)
                        </p>
                    </s:if>
                    <s:else>
                        <p>
                            <a class="btn btn-primary" href="<s:url action="reloadContentsIndex" namespace="/do/jacms/Content/Admin" />">
                                <s:text name="note.reload.contentIndexes.start" />
                            </a>
                            &#32;(
                            <s:if test="searcherManagerStatus == 2">
                                <span class="text-warning"><s:text name="note.reload.contentIndexes.status.needToReload" /></span>
                            </s:if>
                            <s:elseif test="searcherManagerStatus == 0">
                                <span><s:text name="note.reload.contentIndexes.status.ready" /></span>
                            </s:elseif>
                            )
                        </p>
                    </s:else>
                    <s:if test="lastReloadInfo != null">
                        <p class="text-info">
                            <s:text name="note.reload.contentIndexes.lastOn.intro" />&#32;<span class="important"><s:date name="lastReloadInfo.date" format="dd/MM/yyyy HH:mm" /></span>,
                            <s:if test="lastReloadInfo.result == 0">
                                <span class="text-error"><s:text name="note.reload.contentIndexes.lastOn.ko" /></span>.
                            </s:if>
                            <s:else>
                                <s:text name="note.reload.contentIndexes.lastOn.ok" />.
                            </s:else>
                        </p>
                    </s:if>
                </div>
            </div>
        </div>
            
        <fieldset class="form-group">
            <div class="col-xs-2 control-label">
                <span class="display-block"><s:text name="label.chooseYourEditor"/></span>
            </div>
            <div class="col-xs-10 ">
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default <s:if test="systemParams['hypertextEditor'] == 'none'"> active</s:if>">
                            <input type="radio" class="radiocheck" id="admin-settings-area-hypertextEditor_none"
                                   name="hypertextEditor"
                                   value="none"
                            <s:if test="systemParams['hypertextEditor'] == 'none'">checked="checked"</s:if> />
                        <s:text name="label.none"/>
                    </label>
                    <label class="btn btn-default <s:if test="systemParams['hypertextEditor'] == 'fckeditor'"> active</s:if>">
                            <input type="radio" class="radiocheck"
                                   id="admin-settings-area-hypertextEditor_fckeditor" name="hypertextEditor"
                                   value="fckeditor"
                            <s:if test="systemParams['hypertextEditor'] == 'fckeditor'">checked="checked"</s:if> />
                        <s:text name="name.editor.ckeditor"/>
                    </label>
                </div>
            </div>
        </fieldset>
        <!-- alt, description, legend, and title -->
        <fieldset class="col-xs-12 settings-form">
            <h2>
                <s:text name="jacms.menu.resourceMetadataMapping" />
            </h2>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 control-label">
                        <span for="resourceAltMapping"><s:text name="jacms.label.resourceAltMapping" /></span>
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="jacms.help.resourceAltMapping" />" data-placement="right">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </div>
                    <div class="col-xs-10">
                        <wpsf:textfield name="resourceAltMapping" id="resourceAltMapping"  cssClass="form-control" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 control-label">
                        <span for="resourceDescriptionMapping"><s:text name="jacms.label.resourceDescriptionMapping" /></span>
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="jacms.help.resourceDescriptionMapping" />" data-placement="right">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </div>
                    <div class="col-xs-10">
                        <wpsf:textfield name="resourceDescriptionMapping" id="resourceDescriptionMapping"  cssClass="form-control" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 control-label">
                        <span for="resourceLegendMapping"><s:text name="jacms.label.resourceLegendMapping" /></span>
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="jacms.help.resourceLegendMapping" />" data-placement="right">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </div>
                    <div class="col-xs-10">
                        <wpsf:textfield name="resourceLegendMapping" id="resourceLegendMapping"  cssClass="form-control" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 control-label">
                        <span for="resourceTitleMapping"><s:text name="jacms.label.resourceTitleMapping" /></span>
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="jacms.help.resourceTitleMapping" />" data-placement="right">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </div>
                    <div class="col-xs-10">
                        <wpsf:textfield name="resourceTitleMapping" id="resourceTitleMapping"  cssClass="form-control" />
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="col-xs-12">

            <h2>
                <s:text name="jacms.title.contentSetting.cropImage"/>
            </h2>
            <div class="form-group">
                <div class="col-sm-5 col-sm-offset-2">
                    <div id="add-crop-dim-button">
                        <button type="button" id="add-crop-dim" class="btn btn-primary">
                            <s:text name="label.add" />
                        </button>
                    </div>
                </div>
            </div>

            <s:set var="aspectRatioListVar" value="ratio" />
            <s:iterator var="cropDimVar" value="#aspectRatioListVar" status="status">
                <div class="form-group">
                    <div class="col-sm-2 col-sm-offset-2">
                        <wpsf:textfield name="ratio" maxlength="40" id="ratio_%{#status.count - 1}" cssClass="form-control ratio-conf" value="%{#cropDimVar}" />
                    </div>
                    <button type="button" class="btn-danger delete-fields" title="<s:text name="label.delete" />">
                        <span class="pficon pficon-delete"></span>
                    </button>
                </div>
            </s:iterator>
            <div id="fields-container" >
            </div>
        </fieldset>

        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save"/>
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>

<template id="hidden-fields-template">
    <div class="form-group">
        <div class="col-sm-2 col-sm-offset-2">
            <wpsf:textfield name="ratio" maxlength="250" id="newRatio" cssClass="form-control ratio-conf" />
        </div>

        <button type="button" class="btn-danger delete-fields" title="<s:text name="label.delete" />">
            <span class="pficon pficon-delete"></span>
        </button>
    </div>
</template>

<script type="text/javascript">
    $(document).ready(function () {

        $('#add-crop-dim').click(function (e) {
            e.preventDefault();
            var numItems = $('.ratio-conf').length;

            var template = $('#hidden-fields-template').html();

            $('#fields-container').append(template);

            var newId = parseInt(numItems);
            $('#newRatio').val("");
            $('#newRatio').attr("id", "ratio_" + newId);


        });
        $('.delete-fields').click(function (e) {
            e.preventDefault();
            $(this).parent('div').remove();
        });

        $('#fields-container').on("click", ".delete-fields", function (e) {
            e.preventDefault();
            $(this).parent('div').remove();
        })
    });
</script>
