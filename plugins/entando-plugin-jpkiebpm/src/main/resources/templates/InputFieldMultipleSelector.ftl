<div class="row">
    <div class='col-sm-5'>
        <div class="form-group">
            <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
                <#include "/FieldRequired.ftl">$i18n.getLabel("JPKIE_${field.name}")
                </label>  
           
            <select id="selectpicker_${field.id}" name="Monolist:${field.name}" class="form-control" multiple >
                        <#if field.options??>
                        <#list field.options as option>                    
                        <option value="${option.value}">${option.name}</option>
                        </#list>
                        </#if>
                    </select>
                </div>
        </div>
    <script type="text/javascript">
        
         $('#selectpicker_${field.id}').selectpicker(
          {  size: ${field.maxDropdownElements},
            maxOptions: ${field.maxElementsOnTitle},
            liveSearch: ${field.allowFilter?c},            
            actionsBox: ${field.allowClearSelection?c}
         });
        </script>
    </div>
</div>
