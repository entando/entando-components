<div class="row">
    <div class="col-md-5">
        <div class="ui-dform-div form-group">
            <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
                <#include "/FieldRequired.ftl">$i18n.getLabel("JPKIE_${field.name}")
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
</div>
