<div class="container">
    <#include "/modelHeader.ftl">
        <#list sections as k, section>
            <#include "/section.ftl">
        </#list>
    <#include "/submit.ftl">
    <#include "/modelFooter.ftl">
</div>

