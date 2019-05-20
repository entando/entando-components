<#include "/RowOpen.ftl">
<div class="col-md-${field.span} col-sm-12">
    <div class="form-group">
        <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
            <#include "/FieldLabel.ftl">
        </label>
        <select id="${field.id}" name="$data.${field.name}.type:${field.name}" class="form-control" >
            <#if field.addEmptyOption??>
            <option></option>
            </#if>
            <#if field.options??>
            <#list field.options as option>
            <option <#include "/ListBoxOptionDefaultValue.ftl">value="${option.value}">${option.name}</option>
            </#list>
            </#if>
        </select>
    </div>
</div>
<#include "/RowClose.ftl">
