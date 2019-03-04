<div class="ui-dform-div">
    <fieldset class="ui-dform-fieldset">
        <#if section.name??>
        <legend class="control-label editLabel ui-dform-legend">
            $i18n.getLabel("JPKIE_FORM_${section.name}")
            </legend>
        <#else> 
        </#if>
        <div class="fields">

        <#if section.fields??>
            <#list section.fields as field>
                <#switch field.typePAM>
                <#case "TextBox">
                    <#include "/InputFieldTextBox.ftl">
                    <#break>
                <#case "TextArea">
                    <#include "/InputFieldTextArea.ftl">
                    <#break>
                <#case "DatePicker">
                    <#include "/InputFieldDatePicker.ftl">
                    <#break>
                <#case "IntegerBox">
                    <#include "/InputFieldTextBox.ftl">
                    <#break>
                <#case "DecimalBox">
                    <#include "/InputFieldTextBox.ftl">
                    <#break>
                <#case "CheckBox">
                    <#include "/InputFieldCheckBox.ftl">
                    <#break>
                <#case "Slider">
                    <#include "/InputFieldTextBox.ftl">
                    <#break>
                <#case "RadioGroup">
                    <#include "/InputFieldRadioGroup.ftl">
                    <#break>
                <#case "ListBox">
                    <#include "/InputFieldListBox.ftl">
                    <#break>
                <#case "MultipleSelector">
                    <#include "/InputFieldMultipleSelector.ftl">
                    <#break>
                <#case "MultipleInput">
                    <#include "/InputFieldTextBox.ftl">
                    <#break>
                <#case "Document">
                    <#include "/InputFieldTextBox.ftl">
                    <#break>      
                <#default>
                    <#include "/InputFieldTextBox.ftl">
                    <#break>
                </#switch>
            </#list>
        </#if>
        </div>
        </fieldset>
    </div>
