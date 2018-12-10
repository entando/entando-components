<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<%--CAL START --%>

<wp:info key="currentLang" var="currentLang" />

<c:set var="js_for_datepicker">
/* Italian initialisation for the jQuery UI date picker plugin. */
/* Written by Antonello Pasella (antonello.pasella@gmail.com). */
jQuery(function($){
$.datepicker.regional['it'] = {
closeText: 'Chiudi',
prevText: '&#x3c;Prec',
nextText: 'Succ&#x3e;',
currentText: 'Oggi',
monthNames: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno',
'Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'],
monthNamesShort: ['Gen','Feb','Mar','Apr','Mag','Giu',
'Lug','Ago','Set','Ott','Nov','Dic'],
dayNames: ['Domenica','Luned&#236','Marted&#236','Mercoled&#236','Gioved&#236','Venerd&#236','Sabato'],
dayNamesShort: ['Dom','Lun','Mar','Mer','Gio','Ven','Sab'],
dayNamesMin: ['Do','Lu','Ma','Me','Gi','Ve','Sa'],
weekHeader: 'Sm',
dateFormat: 'dd/mm/yy',
firstDay: 1,
isRTL: false,
showMonthAfterYear: false,
yearSuffix: ''};
});

jQuery(function($){
if (Modernizr.touch && Modernizr.inputtypes.date) {
$.each( $("input[data-isdate=true]"), function(index, item) {
item.type = 'date';
});
} else {
$.datepicker.setDefaults( $.datepicker.regional[ "<c:out value="${currentLang}" />" ] );
$("input[data-isdate=true]").datepicker({
       changeMonth: true,
       changeYear: true,
       dateFormat: "dd/mm/yy"
     });
}
});
</c:set>
<wp:headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.1/jquery-ui.js" />
<wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<wp:headInfo type="JS_RAW" info="${js_for_datepicker}" />

<%--CAL END --%>

<%--
optional CSS
<wp:headInfo type="CSS" info="widgets/iotListDevices_list.css" />
--%>

<section class="iotListDevices_list">

<h1><wp:i18n key="jpiot_IOTLISTDEVICES_SEARCH_IOTLISTDEVICES" /></h1>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotListDevices/search.action" />" method="post" >

  <fieldset>
		<label for="iotListDevices_id"><wp:i18n key="jpiot_IOTLISTDEVICES_ID" /></label>
		<input type="text" name="id" id="iotListDevices_id" value="<s:property value="id" />" />
		<label for="iotListDevices_widgetTitle"><wp:i18n key="jpiot_IOTLISTDEVICES_WIDGETTITLE" /></label>
		<input type="text" name="widgetTitle" id="iotListDevices_widgetTitle" value="<s:property value="widgetTitle" />" />
		<label for="iotListDevices_datasource"><wp:i18n key="jpiot_IOTLISTDEVICES_DATASOURCE" /></label>
		<input type="text" name="datasource" id="iotListDevices_datasource" value="<s:property value="datasource" />" />
		<label for="iotListDevices_context"><wp:i18n key="jpiot_IOTLISTDEVICES_CONTEXT" /></label>
		<input type="text" name="context" id="iotListDevices_context" value="<s:property value="context" />" />
		<label for="iotListDevices_download"><wp:i18n key="jpiot_IOTLISTDEVICES_DOWNLOAD" /></label>
		<input type="text" name="download" id="iotListDevices_download" value="<s:property value="download" />" />
		<label for="iotListDevices_filter"><wp:i18n key="jpiot_IOTLISTDEVICES_FILTER" /></label>
		<input type="text" name="filter" id="iotListDevices_filter" value="<s:property value="filter" />" />
		<label for="iotListDevices_allColumns"><wp:i18n key="jpiot_IOTLISTDEVICES_ALLCOLUMNS" /></label>
		<input type="text" name="allColumns" id="iotListDevices_allColumns" value="<s:property value="allColumns" />" />
		<label for="iotListDevices_columns"><wp:i18n key="jpiot_IOTLISTDEVICES_COLUMNS" /></label>
		<input type="text" name="columns" id="iotListDevices_columns" value="<s:property value="columns" />" />
  </fieldset>

  <button type="submit" class="btn btn-primary">
    <wp:i18n key="SEARCH" />
  </button>

<wpsa:subset source="iotListDevicessId" count="10" objectName="groupIotListDevices" advanced="true" offset="5">
<s:set var="group" value="#groupIotListDevices" />

<div class="margin-medium-vertical text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<p>
  <a href="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotListDevices/new.action"></wp:action>" title="<wp:i18n key="NEW" />" class="btn btn-info">
    <span class="icon-plus-sign icon-white"></span>&#32;
    <wp:i18n key="NEW" />
  </a>
</p>

<table class="table table-bordered table-condensed table-striped">
<thead>
<tr>
  <th class="text-right">
    <wp:i18n key="jpiot_IOTLISTDEVICES_ID" />
  </th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTLISTDEVICES_WIDGETTITLE" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTLISTDEVICES_DATASOURCE" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTLISTDEVICES_CONTEXT" /></th>
	<th
         class="text-right"        ><wp:i18n key="jpiot_IOTLISTDEVICES_DOWNLOAD" /></th>
	<th
         class="text-right"        ><wp:i18n key="jpiot_IOTLISTDEVICES_FILTER" /></th>
	<th
         class="text-right"        ><wp:i18n key="jpiot_IOTLISTDEVICES_ALLCOLUMNS" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTLISTDEVICES_COLUMNS" /></th>
	<th>
    <wp:i18n key="jpiot_IOTLISTDEVICES_ACTIONS" />
  </th>
</tr>
</thead>
<tbody>
<s:iterator var="iotListDevicesId">
<tr>
	<s:set var="iotListDevices" value="%{getIotListDevices(#iotListDevicesId)}" />
	<td class="text-right">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotListDevices/edit.action"><wp:parameter name="id"><s:property value="#iotListDevices.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="EDIT" />: <s:property value="#iotListDevices.id" />"
      class="label label-info display-block">
    <s:property value="#iotListDevices.id" />&#32;
    <span class="icon-edit icon-white"></span>
    </a>
  </td>
	<td
            >
    <s:property value="#iotListDevices.widgetTitle" />  </td>
	<td
            >
    <s:property value="#iotListDevices.datasource" />  </td>
	<td
            >
    <s:property value="#iotListDevices.context" />  </td>
	<td
         class="text-right"    >
    <s:property value="#iotListDevices.download" />  </td>
	<td
         class="text-right"    >
    <s:property value="#iotListDevices.filter" />  </td>
	<td
         class="text-right"    >
    <s:property value="#iotListDevices.allColumns" />  </td>
	<td
            >
    <s:property value="#iotListDevices.columns" />  </td>
	<td class="text-center">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotListDevices/trash.action"><wp:parameter name="id"><s:property value="#iotListDevices.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="TRASH" />: <s:property value="#iotListDevices.id" />"
      class="btn btn-warning btn-small">
      <span class="icon-trash icon-white"></span>&#32;
      <wp:i18n key="TRASH" />
    </a>
  </td>
</tr>
</s:iterator>
</tbody>
</table>

<div class="margin-medium-vertical text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</form>
</section>