<div class="row">
<<<<<<< HEAD
    <div class='col-sm-5'>
        <div class="form-group">
            <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
                <#include "/FieldLabel.ftl">
            </label>   
            <select id="${field.id}" name="$data.${field.name}.type:${field.name}" class="form-control" >
                <#if field.addEmptyOption??>
=======
        <div class='col-sm-12'>
            <div class="form-group">
                <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
                    <#include "/FieldLabel.ftl">
                </label>  
                <select id="${field.id}" name="$data.${field.name}.type:${field.name}" class="form-control" >
                    <#if field.addEmptyOption??>
>>>>>>> 0ef8f8eff... New Task Form Widget
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
