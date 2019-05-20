<#include "/RowOpen.ftl">
<div class="col-md-${field.span} col-sm-12">
    <div class="ui-dform-div form-group">
        <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
            <#include "/FieldLabel.ftl">
        </label>
        <div class="input-group">
        <#if field.options??>
            <#list field.options as option>
            <div class="<#include "/RadioGroupInline.ftl">"><input type="${field.typeHTML}" id="${field.id}" name="$data.${field.name}.type:${field.name}" class="ui-widget" aria-required="true" value="${option.value}" <#include "/RadioGroupOptionDefaultValue.ftl">> ${option.name}</div>
            </#list>
        </#if>
        </div>
    </div>
</div>
<#include "/RowClose.ftl">
