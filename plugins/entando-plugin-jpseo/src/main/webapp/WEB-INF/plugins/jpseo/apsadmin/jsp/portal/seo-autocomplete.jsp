<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsaseo" uri="/jpseo-apsadmin-core" %>

<wpsaseo:seoMetatagCatalogTag var="keysCatalogVar" />

<!--List of all available metatags-->
<script>
    $(function () {
        var availableTags = [
    <s:iterator value="#keysCatalogVar">"<s:property />",</s:iterator>
        ];
        $("#new_metatag").autocomplete({
            source: availableTags
        });
    });
</script>