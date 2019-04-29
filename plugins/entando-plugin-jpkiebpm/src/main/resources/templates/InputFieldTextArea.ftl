<div class="row">
<div class="col-md-12">

    <div class="ui-dform-div form-group">
        <label id="JPKIE_${field.name}" for="${field.id}" class="editLabel">
        <#include "/FieldLabel.ftl"></label>
        <textarea rows="4" cols="50" id="${field.id}" name="$data.${field.name}.type:${field.name}" labelkey="JPKIE_${field.name}" class="form-control ui-widget" aria-required="true" <#include "/FieldDisabled.ftl" ><#include "/FieldPlaceHolder.ftl">>${field.value}</textarea>
    </div>

</div>
</div>
