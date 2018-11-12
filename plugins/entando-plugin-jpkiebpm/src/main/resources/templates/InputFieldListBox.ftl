<div class="row">
    <div class='col-sm-5'>
        <div class="form-group">
            <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
                <#include "/FieldRequired.ftl">$i18n.getLabel("JPKIE_${field.name}")
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
</div>
