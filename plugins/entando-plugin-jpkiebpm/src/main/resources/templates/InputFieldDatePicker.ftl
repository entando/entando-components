<#include "/RowOpen.ftl">
<div class="col-md-${field.span} col-sm-12">
    <div class="form-group">
        <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
            <#include "/FieldLabel.ftl">
        </label>

        <div class="input-group" id="datepicker_${field.id}">
            <input type="${field.typeHTML}" id="${field.id}" name="$data.${field.name}.type:${field.name}" labelkey="JPKIE_${field.name}" class="form-control date-picker" aria-required="true" <#include "/FieldDisabled.ftl" ><#include "/FieldPlaceHolder.ftl">value="${field.value}">
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
    
    <script type="text/javascript">
        $("#datepicker_${field.id}").datetimepicker({
            <#if field.showTime == true>
            format: 'YYYY-MM-DD hh:mm',
            <#else>
            format: 'YYYY-MM-DD',
            </#if>
            allowInputToggle: true,
            showTodayButton: true
        });
    </script>
</div>
<#include "/RowClose.ftl">
