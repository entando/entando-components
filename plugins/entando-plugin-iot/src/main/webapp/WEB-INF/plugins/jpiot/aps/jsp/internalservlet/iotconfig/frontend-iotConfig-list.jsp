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
<wp:headInfo type="CSS" info="widgets/iotConfig_list.css" />
--%>

<section class="iotConfig_list">

<h1><wp:i18n key="jpiot_IOTCONFIG_SEARCH_IOTCONFIG" /></h1>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotConfig/search.action" />" method="post" >

  <fieldset>
		<label for="iotConfig_id"><wp:i18n key="jpiot_IOTCONFIG_ID" /></label>
		<input type="text" name="id" id="iotConfig_id" value="<s:property value="id" />" />
		<label for="iotConfig_name"><wp:i18n key="jpiot_IOTCONFIG_NAME" /></label>
		<input type="text" name="name" id="iotConfig_name" value="<s:property value="name" />" />
		<label for="iotConfig_hostname"><wp:i18n key="jpiot_IOTCONFIG_HOSTNAME" /></label>
		<input type="text" name="hostname" id="iotConfig_hostname" value="<s:property value="hostname" />" />
		<label for="iotConfig_port"><wp:i18n key="jpiot_IOTCONFIG_PORT" /></label>
		<input type="text" name="port" id="iotConfig_port" value="<s:property value="port" />" />
		<label for="iotConfig_webapp"><wp:i18n key="jpiot_IOTCONFIG_WEBAPP" /></label>
		<input type="text" name="webapp" id="iotConfig_webapp" value="<s:property value="webapp" />" />
		<label for="iotConfig_username"><wp:i18n key="jpiot_IOTCONFIG_USERNAME" /></label>
		<input type="text" name="username" id="iotConfig_username" value="<s:property value="username" />" />
		<label for="iotConfig_password"><wp:i18n key="jpiot_IOTCONFIG_PASSWORD" /></label>
		<input type="text" name="password" id="iotConfig_password" value="<s:property value="password" />" />
		<label for="iotConfig_token"><wp:i18n key="jpiot_IOTCONFIG_TOKEN" /></label>
		<input type="text" name="token" id="iotConfig_token" value="<s:property value="token" />" />
  </fieldset>

  <button type="submit" class="btn btn-primary">
    <wp:i18n key="SEARCH" />
  </button>

<wpsa:subset source="iotConfigsId" count="10" objectName="groupIotConfig" advanced="true" offset="5">
<s:set var="group" value="#groupIotConfig" />

<div class="margin-medium-vertical text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<p>
  <a href="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotConfig/new.action"></wp:action>" title="<wp:i18n key="NEW" />" class="btn btn-info">
    <span class="icon-plus-sign icon-white"></span>&#32;
    <wp:i18n key="NEW" />
  </a>
</p>

<table class="table table-bordered table-condensed table-striped">
<thead>
<tr>
  <th class="text-right">
    <wp:i18n key="jpiot_IOTCONFIG_ID" />
  </th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTCONFIG_NAME" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTCONFIG_HOSTNAME" /></th>
	<th
         class="text-right"        ><wp:i18n key="jpiot_IOTCONFIG_PORT" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTCONFIG_WEBAPP" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTCONFIG_USERNAME" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTCONFIG_PASSWORD" /></th>
	<th
                 class="text-left"><wp:i18n key="jpiot_IOTCONFIG_TOKEN" /></th>
	<th>
    <wp:i18n key="jpiot_IOTCONFIG_ACTIONS" />
  </th>
</tr>
</thead>
<tbody>
<s:iterator var="iotConfigId">
<tr>
	<s:set var="iotConfig" value="%{getIotConfig(#iotConfigId)}" />
	<td class="text-right">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotConfig/edit.action"><wp:parameter name="id"><s:property value="#iotConfig.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="EDIT" />: <s:property value="#iotConfig.id" />"
      class="label label-info display-block">
    <s:property value="#iotConfig.id" />&#32;
    <span class="icon-edit icon-white"></span>
    </a>
  </td>
	<td
            >
    <s:property value="#iotConfig.name" />  </td>
	<td
            >
    <s:property value="#iotConfig.hostname" />  </td>
	<td
         class="text-right"    >
    <s:property value="#iotConfig.port" />  </td>
	<td
            >
    <s:property value="#iotConfig.webapp" />  </td>
	<td
            >
    <s:property value="#iotConfig.username" />  </td>
	<td
            >
    <s:property value="#iotConfig.password" />  </td>
	<td
            >
    <s:property value="#iotConfig.token" />  </td>
	<td class="text-center">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotConfig/trash.action"><wp:parameter name="id"><s:property value="#iotConfig.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="TRASH" />: <s:property value="#iotConfig.id" />"
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