<div class="container">
<#include "/modelHeader.ftl">
<#include "/sectionHeader.ftl">
<#list fields as field>
    <div class="fields">
        <#switch field.typePAM>
        <#case "TextBox">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "TextArea">
            <#include "/inputFieldTextArea.ftl">
            <#break>
        <#case "DatePicker">
            <#include "/inputFieldDatePicker.ftl">
            <#break>
        <#case "IntegerBox">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "DecimalBox">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "CheckBox">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "Slider">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "RadioGroup">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "MultipleSelector">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "MultipleInput">
            <#include "/inputFieldTextBox.ftl">
            <#break>
        <#case "Document">
            <#include "/inputFieldTextBox.ftl">
            <#break>      
        <#default>
            <#include "/inputFieldTextBox.ftl">
            <#break>
        </#switch>
    </div>
</#list>
<#include "/sectionFooter.ftl">
<#include "/submit.ftl">
<#include "/modelFooter.ftl">
</div>