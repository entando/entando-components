<div class="bpm-forms-container">
<div class="ibox ibox-padding">
    <div class="ibox-title">
        <h3 class="control-label editLabel" id="JPKIE_TITLE_${model.title}">$i18n.getLabel("JPKIE_TITLE_${model.title}")</h3>
    </div>
    <div class="ibox-content">
        <div id="dialog-response-process" title="Response"></div>
        <form id="bpm-form" method="post" novalidate="novalidate" class="ui-dform-form" action="$info.getActionPathUrl('/ExtStr2/do/bpm/FrontEnd/DataTypeForm/save')">
            <input type="hidden" id="processId" name="processId" class="ui-dform-hidden" value="${model.processId}">
            <input type="hidden" id="containerId" name="containerId" class="ui-dform-hidden" value="${model.containerId}">
