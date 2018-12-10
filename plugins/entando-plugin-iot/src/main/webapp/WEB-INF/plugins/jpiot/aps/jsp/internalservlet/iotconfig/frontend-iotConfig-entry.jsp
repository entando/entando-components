<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

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

<section>
<s:if test="strutsAction==1">
	<h1><wp:i18n key="jpiot_IOTCONFIG_NEW" /></h1>
</s:if>
<s:elseif test="strutsAction==2">
	<h1><wp:i18n key="jpiot_IOTCONFIG_EDIT" /></h1>
</s:elseif>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotConfig/save.action" />" method="post">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-error">
			<h2><s:text name="message.title.FieldErrors" /></h2>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
				<li><s:property/></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
		<div class="alert alert-error">
			<h2><s:text name="message.title.ActionErrors" /></h2>
			<ul>
				<s:iterator value="actionErrors">
				<li><s:property/></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="id" />
	</p>

	<fieldset>
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

	<wpsf:submit type="button" cssClass="btn btn-primary">
		<wp:i18n key="SAVE" />
	</wpsf:submit>

</form>
</section>