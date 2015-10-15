INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked )
	VALUES ( 'jpwebdynamicform_message_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Dynamic Web Forms - Publish the form for a Message Type</property>
<property key="it">Dynamic Web Forms - Pubblica il form di un tipo di Messaggio</property>
</properties>', '<config>
	<parameter name="typeCode">Code of the Message Type</parameter>
	<parameter name="formProtectionType">Protection type of the form</parameter>
	<action name="webdynamicformConfig"/>
</config>', 'jpwebdynamicform', NULL, NULL, 1 );


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked )
	VALUES ( 'jpwebdynamicform_message_choice', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Dynamic Web Forms - Choice of a type of Message</property>
<property key="it">Dynamic Web Forms - Scelta tipo di Messaggio</property>
</properties>', NULL, 'jpwebdynamicform', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpwebdynamicform/Message/User/listTypes</property>
</properties>', 1 );


INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INFO', 'en', 'The fields marked with * are required.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INFO', 'it', 'I campi contrassegnati dal simbolo * sono obbligatori.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_FULL', 'en', 'Mandatory');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_FULL', 'it', 'Obbligatorio');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_SHORT', 'en', '*');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_SHORT', 'it', '*');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL', 'en', 'Max Length');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL', 'it', 'Lunghezza massima');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT', 'en', 'max');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT', 'it', 'max');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_FULL', 'en', 'Min Length');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_FULL', 'it', 'Lunghezza minima');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT', 'en', 'min');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT', 'it', 'min');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INVIA', 'en', 'Send');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INVIA', 'it', 'Invia');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_SAVE_CONFIRMATION', 'it', 'Il messaggio è stato inviato correttamente. Se vuoi inviaci un''altra');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_SAVE_CONFIRMATION', 'en', 'The message has been sent successfully. If you need, send us another');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_REQUEST_LINK', 'en', 'request');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_REQUEST_LINK', 'it', 'richiesta');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_SELECT', 'en', 'Select');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_SELECT', 'it', 'Seleziona');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGETYPE', 'it', 'Scegliere un tipo di messaggio');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGETYPE', 'en', 'Choose a message type');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_CHOOSE_TYPE', 'it', 'Scegli e continua');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_CHOOSE_TYPE', 'en', 'Select and continue');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS', 'it', 'Errori durante la compilazione del modulo');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS', 'en', 'Errors occured');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_YES', 'it', 'Si');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_YES', 'en', 'Yes');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_NO', 'it', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_NO', 'en', 'No');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_NONE', 'it', 'Indifferente');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_NONE', 'en', 'None');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS_HAPPENED', 'it', 'Si sono verificati degli errori!');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS_HAPPENED', 'en', 'An error has happened!');




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front_attributeInfo-help-block', NULL, 'jpwebdynamicform', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>

<@s.set var="validationRules" value="#attribute.validationRules.ognlValidationRule" />
<@s.set var="hasValidationRulesVar" value="%{#validationRules != null && #validationRules.expression != null}" />

<@s.if test="%{#hasValidationRulesVar || #attribute.type == ''Date'' || (#attribute.textAttribute && (#attribute.minLength != -1 || #attribute.maxLength != -1))}">
		<span class="help-block">
		<@s.if test="#attribute.type == ''Date''">dd/MM/yyyy&#32;</@s.if>
		<@s.if test="%{#validationRules.helpMessageKey != null}">
			<@s.set var="label" scope="page" value="#validationRules.helpMessageKey" /><@wp.i18n key="${label}" />
		</@s.if>
		<@s.elseif test="%{#validationRules.helpMessage != null}">
			<@s.property value="#validationRules.helpMessage" />
		</@s.elseif>
		<@s.if test="#attribute.minLength != -1">
			&#32;
			<abbr title="<@wp.i18n key="jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_FULL" />&#32;<@s.property value="#attribute.minLength" />">
				<@wp.i18n key="jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT" />:&#32;<@s.property value="#attribute.minLength" />
			</abbr>
		</@s.if>
		<@s.if test="#attribute.maxLength != -1">
			&#32;
			<abbr title="<@wp.i18n key="jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL" />&#32;<@s.property value="#attribute.maxLength" />">
				<@wp.i18n key="jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT" />:&#32;<@s.property value="#attribute.maxLength" />
			</abbr>
		</@s.if>
	</span>
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front_AttributeInfo', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<@s.if test="#attribute.required">
	<abbr class="icon icon-asterisk" title="<@wp.i18n key="jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_FULL" />"><span class="noscreen"><@wp.i18n key="jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_SHORT" /></span></abbr>
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-BooleanAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<label class="radio inline" for="<@s.property value="%{#attribute_id + ''-true''}" />">
	<@wpsf.radio 
		useTabindexAutoIncrement=true 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + ''-true''}" 
		value="true"
		checked="%{#attribute.value == true}" 
		cssClass="radio" />
		<@wp.i18n key="jpwebdynamicform_YES" />
</label>
&#32;
<label class="radio inline" for="<@s.property value="%{#attribute_id+''-false''}" />">
	<@wpsf.radio 
		useTabindexAutoIncrement=true 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + ''-false''}" 
		value="false" 
		checked="%{#attribute.value == false}" 
		cssClass="radio" />
		<@wp.i18n key="jpwebdynamicform_NO" />
</label>
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-CheckboxAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<@s.set name="checkedValueVar" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
<@wpsf.checkbox useTabindexAutoIncrement=true 
	name="%{#attributeTracer.getFormFieldName(#attribute)}" 
	id="%{attribute_id}" value="#checkedValueVar" />
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_IteratorAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>

<div class="control-group <@s.property value="%{'' attribute-type-''+#attribute.type+'' ''}" /> <@s.property value="#fieldErrorClass" />">
	<label class="control-label" for="<@s.property value="#attribute_id" />">
		<@wp.i18n key="${i18n_attribute_name}" />
		<@wp.fragment code="jpwebdynform_is_front_AttributeInfo" escapeXml=false />
	</label>
	<div class="controls">
		<@s.if test="#attribute.type == ''Boolean''">
			<@wp.fragment code="jpwebdynform_is_front-BooleanAttribute" escapeXml=false />
		</@s.if>
		<@s.elseif test="#attribute.type == ''CheckBox''">
			<@wp.fragment code="jpwebdynform_is_front-CheckboxAttribute" escapeXml=false />
		</@s.elseif>
		<@s.elseif test="#attribute.type == ''Date''">
			<@wp.fragment code="jpwebdynform_is_front-DateAttribute" escapeXml=false />
		</@s.elseif>
		<@s.elseif test="#attribute.type == ''Enumerator''">
			<@wp.fragment code="jpwebdynform_is_front-EnumeratorAttribute" escapeXml=false />
		</@s.elseif>
		<@s.elseif test="#attribute.type == ''EnumeratorMap''">
			<@wp.fragment code="jpwebdynform_is_front-EnumeratorMapAttribute" escapeXml=false />
		</@s.elseif>
		<@s.elseif test="#attribute.type == ''Longtext''">
			<@wp.fragment code="jpwebdynform_is_front-LongtextAttribute" escapeXml=false /> 
		</@s.elseif>
		<@s.elseif test="#attribute.type == ''Number''">
			<@wp.fragment code="jpwebdynform_is_front-NumberAttribute" escapeXml=false />
		</@s.elseif>
		<@s.elseif test="#attribute.type == ''Monotext'' || #attribute.type == ''Text''">
			<@wp.fragment code="jpwebdynform_is_front-MonotextAttribute" escapeXml=false />
		</@s.elseif>
		<@s.elseif test="#attribute.type == ''ThreeState''">
			<@wp.fragment code="jpwebdynform_is_front-ThreeStateAttribute" escapeXml=false />
		</@s.elseif>
		<@s.else>
			<@wp.fragment code="jpwebdynform_is_front-MonotextAttribute" escapeXml=false />
		</@s.else>
		<@wp.fragment code="jpwebdynform_is_front_attributeInfo-help-block" escapeXml=false />
	</div>
</div>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-CompositeAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>

<@s.set var="i18n_parent_attribute_name" value="#attribute.name" />
<@s.set name="masterCompositeAttributeTracer" value="#attributeTracer" />
<@s.set name="masterCompositeAttribute" value="#attribute" />

<@s.iterator value="#attribute.attributes" var="attribute">
	<@s.set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)" />
	<@s.set name="parentAttribute" value="#masterCompositeAttribute" />
	<@s.set var="i18n_attribute_name" value="%{''jpwebdynamicform_''+ #typeCodeKey +''_''+ #attribute.name}" scope="request" />
	<@s.set var="attribute_id" value="%{''jpwebdynamicform_''+ #typeCodeKey +''_''+ #attributeTracer.getFormFieldName(#attribute)}" />
	<@wp.fragment code="jpwebdynform_is_IteratorAttribute" escapeXml=false />
</@s.iterator>
<@s.set name="attributeTracer" value="#masterCompositeAttributeTracer" />
<@s.set name="attribute" value="#masterCompositeAttribute" />
<@s.set name="parentAttribute" value=""></@s.set>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-DateAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign currentLangVar ><@wp.info key="currentLang" /></#assign>
<@s.if test="#attribute.failedDateString == null">
	<@s.set name="dateAttributeValue" value="#attribute.getFormattedDate(''dd/MM/yyyy'')" />
</@s.if>
<@s.else>
	<@s.set name="dateAttributeValue" value="#attribute.failedDateString" />
</@s.else>
<@wpsf.textfield 
useTabindexAutoIncrement=true id="%{attribute_id}" 
name="%{#attributeTracer.getFormFieldName(#attribute)}" 
value="%{#dateAttributeValue}" maxlength="10" cssClass="text userprofile-date" />
  &#32;
<#assign js_for_datepicker="jQuery(function($){
	$.datepicker.regional[''it''] = {
		closeText: ''Chiudi'',
		prevText: ''&#x3c;Prec'',
		nextText: ''Succ&#x3e;'',
		currentText: ''Oggi'',
		monthNames: [''Gennaio'',''Febbraio'',''Marzo'',''Aprile'',''Maggio'',''Giugno'',
			''Luglio'',''Agosto'',''Settembre'',''Ottobre'',''Novembre'',''Dicembre''],
		monthNamesShort: [''Gen'',''Feb'',''Mar'',''Apr'',''Mag'',''Giu'',
			''Lug'',''Ago'',''Set'',''Ott'',''Nov'',''Dic''],
		dayNames: [''Domenica'',''Luned&#236'',''Marted&#236'',''Mercoled&#236'',''Gioved&#236'',''Venerd&#236'',''Sabato''],
		dayNamesShort: [''Dom'',''Lun'',''Mar'',''Mer'',''Gio'',''Ven'',''Sab''],
		dayNamesMin: [''Do'',''Lu'',''Ma'',''Me'',''Gi'',''Ve'',''Sa''],
		weekHeader: ''Sm'',
		dateFormat: ''dd/mm/yy'',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''''};
});

jQuery(function($){
	if (Modernizr.touch && Modernizr.inputtypes.date) {
		$.each(	$(''input.jpwebdynamicform-date''), function(index, item) {
			item.type = ''date'';
		});
	} else {
		$.datepicker.setDefaults( $.datepicker.regional[''${currentLangVar}''] );
		$(''input.jpwebdynamicform-date'').datepicker({
      			changeMonth: true,
      			changeYear: true,
      			dateFormat: ''dd/mm/yy''
    		});
	}
});" >
<@wp.headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
<@wp.headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.0/jquery-ui.min.js" />
<@wp.headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.min.css" />
<@wp.headInfo type="JS_RAW" info="${js_for_datepicker}" />', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-EnumeratorAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<@wp.i18n key="jpwebdynamicform_SELECT" var="labelSelectVar" />
<@wpsf.select useTabindexAutoIncrement=true name="%{#attributeTracer.getFormFieldName(#attribute)}" id="%{attribute_id}" headerKey="" headerValue="%{#labelSelectVar}" list="#attribute.items" value="%{#attribute.getText()}" />
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-EnumeratorMapAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<@wp.i18n key="jpwebdynamicform_SELECT" var="labelSelectVar" />
<@wpsf.select useTabindexAutoIncrement=true name="%{#attributeTracer.getFormFieldName(#attribute)}" 
id="%{attribute_id}" headerKey="" headerValue="%{#labelSelectVar}" 
list="#attribute.mapItems" value="%{#attribute.getText()}" listKey="key" listValue="value" />
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-LongtextAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<@wpsf.textarea useTabindexAutoIncrement=true cols="30" rows="5" id="%{attribute_id}" name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.getTextForLang(#lang.code)}" />
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-MonotextAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<@wpsf.textfield useTabindexAutoIncrement=true id="%{attribute_id}" name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.getTextForLang(#lang.code)}" maxlength="254" />
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-NumberAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<@s.if test="#attribute.failedNumberString == null"><@s.set name="numberAttributeValue" value="#attribute.value"></@s.set></@s.if>
<@s.else><@s.set name="numberAttributeValue" value="#attribute.failedNumberString"></@s.set></@s.else>
<@wpsf.textfield useTabindexAutoIncrement=true id="%{#attribute_id}" name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#numberAttributeValue}" maxlength="254" />
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynform_is_front-ThreeStateAttribute', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="#lang.default">
<label class="radio inline" for="<@s.property value="%{#attribute_id + ''-none''}" />">
	<@wpsf.radio 
		useTabindexAutoIncrement=true  
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + ''-none''}" 
		value="" 
		checked="%{#attribute.booleanValue == null}" 
		cssClass="radio" />
		<@wp.i18n key="jpwebdynamicform_NONE" />
</label>
&#32;
<label class="radio inline" for="<@s.property value="%{#attribute_id + ''-true''}" />">
	<@wpsf.radio 
		useTabindexAutoIncrement=true  
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + ''-true''}" 
		value="true" 
		checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}"
		cssClass="radio" />
		<@wp.i18n key="jpwebdynamicform_YES" />
</label>
&#32;
<label class="radio inline" for="<@s.property value="%{#attribute_id + ''-false''}" />">
	<@wpsf.radio 
		useTabindexAutoIncrement=true  
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id + ''-false''}" 
		value="false" 
		checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" 
		cssClass="radio" />
		<@wp.i18n key="jpwebdynamicform_NO" />
</label>
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynamicform_is_messageTypeFinding', 'jpwebdynamicform_message_choice', 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>

<@wp.info key="currentLang" var="currentLang" />
<form class="form-horizontal jpwebdynamicform-choose-type" 
	  action="<@wp.action path="/ExtStr2/do/jpwebdynamicform/Message/User/new.action"/>" 
	  method="post">
	<div class="control-group">
		<label class="control-label" for="jpwebdynamicform_typecode"><@wp.i18n key="jpwebdynamicform_MESSAGETYPE" /></label>
		<div class="controls">
			<select name="typeCode" tabindex="<@wpsa.counter/>" id="jpwebdynamicform_typecode" class="text">
				<@s.iterator value="messageTypes" var="messageType" >
					<@s.set name="optionDescr">jpwebdynamicform_TITLE_<@s.property value="#messageType.code"/></@s.set>
					<option value="<@s.property value="#messageType.code"/>"><@wp.i18n key="${optionDescr}" /></option>
				</@s.iterator>
			</select>
		</div>
	</div>
	<p class="form-actions">
		<@wp.i18n key="jpwebdynamicform_CHOOSE_TYPE" var="jpwebdynamicform_CHOOSE_TYPE" />
		<@wpsf.submit 
			useTabindexAutoIncrement=true 
			value="%{#attr.jpwebdynamicform_CHOOSE_TYPE}" 
			cssClass="btn btn-primary" 
			/>
	</p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynamicform_message_form', 'jpwebdynamicform_message_form', 'jpwebdynamicform', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.internalServlet actionPath="/ExtStr2/do/jpwebdynamicform/Message/User/new" />', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynamicform_is_entryMessage', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>

<@s.set var="titleKey">jpwebdynamicform_TITLE_<@s.property value="typeCode"/></@s.set>
<@s.set var="typeCodeKey" value="typeCode" />
<@s.set var="lang" value="defaultLang" />
<h1><@wp.i18n key="${titleKey}" /></h1>
<form class="form-horizontal jpwebdynamicform-<@s.property value="#typeCodeKey" />" action="<@wp.action path="/ExtStr2/do/jpwebdynamicform/Message/User/send.action"/>" method="post">
	<@s.if test="hasFieldErrors()">
		<div class="alert alert-block">
			<p><strong><@wp.i18n key="ERRORS"/></strong></p>
			<ul class="unstyled">
				<@s.iterator value="fieldErrors">
					<@s.iterator value="value">
						<li><@s.property escape=false /></li>
					</@s.iterator>
				</@s.iterator>
			</ul>
		</div>
	</@s.if>
	<@s.if test="hasActionErrors()">
		<div class="alert alert-block">
			<p><strong><@wp.i18n key="ERRORS"/></strong></p>
			<ul class="unstyled">
				<@s.iterator value="actionErrors">
					<li><@s.property escape=false /></li>
				</@s.iterator>
			</ul>
		</div>
	</@s.if>
	<@s.set var="hasFieldErrors" value="%{hasFieldErrors()}" />
	<@s.set var="fieldErrors" value="%{fieldErrors}" />
	<p class="noscreen hide">
		<@wpsf.hidden name="typeCode" />
	</p>
	<@s.if test="honeypotEnabled">
	<p class="noscreen">
		<@wp.i18n key="jpwebdynamicform_${honeypotParamName}" /><br />
		<@wpsf.textfield name="%{honeypotParamName}" id="" maxlength=254 />
	</p>
	</@s.if>
	<p>
		<@wp.i18n key="jpwebdynamicform_INFO" />
	</p>
	<@s.iterator value="message.attributeList" var="attribute">
		<@wpsa.tracerFactory var="attributeTracer" lang="%{#lang.code}" />
		<@s.set var="i18n_attribute_name" value="%{''jpwebdynamicform_''+ #typeCodeKey +''_''+ #attribute.name}" scope="request" />
		<@s.set var="attribute_id" value="%{''jpwebdynamicform_''+ #typeCodeKey +''_''+ #attributeTracer.getFormFieldName(#attribute)}" />
		<@s.set var="fieldErrorClass" value="%{#fieldErrors.containsKey(#attributeTracer.getFormFieldName(#attribute)) ? '' error '' : '' ''  }" />
		<@s.if test="#attribute.type != ''Composite''">
			<div class="control-group <@s.property value="%{'' attribute-type-''+#attribute.type+'' ''}" /> <@s.property value="#fieldErrorClass" />">
				<label class="control-label" for="<@s.property value="#attribute_id" />">
					<@wp.i18n key="${i18n_attribute_name}" />
					<@wp.fragment code="jpwebdynform_is_front_AttributeInfo" escapeXml=false />
				</label>
				<div class="controls">
					<@s.if test="#attribute.type == ''Boolean''">
						<@wp.fragment code="jpwebdynform_is_front-BooleanAttribute" escapeXml=false />
					</@s.if>
					<@s.elseif test="#attribute.type == ''CheckBox''">
						<@wp.fragment code="jpwebdynform_is_front-CheckboxAttribute" escapeXml=false />
					</@s.elseif>
					<@s.elseif test="#attribute.type == ''Date''">
						<@wp.fragment code="jpwebdynform_is_front-DateAttribute" escapeXml=false />
					</@s.elseif>
					<@s.elseif test="#attribute.type == ''Enumerator''">
						<@wp.fragment code="jpwebdynform_is_front-EnumeratorAttribute" escapeXml=false />
					</@s.elseif>
					<@s.elseif test="#attribute.type == ''EnumeratorMap''">
						<@wp.fragment code="jpwebdynform_is_front-EnumeratorMapAttribute" escapeXml=false />
					</@s.elseif>
					<@s.elseif test="#attribute.type == ''Longtext''">
						<@wp.fragment code="jpwebdynform_is_front-LongtextAttribute" escapeXml=false />
					</@s.elseif>
					<@s.elseif test="#attribute.type == ''Number''">
						<@wp.fragment code="jpwebdynform_is_front-NumberAttribute" escapeXml=false />
					</@s.elseif>
					<@s.elseif test="#attribute.type == ''Monotext'' || #attribute.type == ''Text''">
						<@wp.fragment code="jpwebdynform_is_front-MonotextAttribute" escapeXml=false />
					</@s.elseif>
					<@s.elseif test="#attribute.type == ''ThreeState''">
						<@wp.fragment code="jpwebdynform_is_front-ThreeStateAttribute" escapeXml=false />
					</@s.elseif>
					<@s.else>
						<@wp.fragment code="jpwebdynform_is_front-MonotextAttribute" escapeXml=false />
					</@s.else>
					<@wp.fragment code="jpwebdynform_is_front_attributeInfo-help-block" escapeXml=false />
				</div>
			</div>
		</@s.if>
		<@s.else>
			<div class="well well-small">
				<fieldset class=" <@s.property value="%{'' attribute-type-''+#attribute.type+'' ''}" /> ">
					<legend class="margin-medium-top"><@wp.i18n key="${i18n_attribute_name}" />
					<@wp.fragment code="jpwebdynform_is_front_AttributeInfo" escapeXml=false />
					<@wp.fragment code="jpwebdynform_is_front_attributeInfo-help-block" escapeXml=false />
					<@wp.fragment code="jpwebdynform_is_front-CompositeAttribute" escapeXml=false />
				</fieldset>
			</div>
		</@s.else>
	</@s.iterator>
	<@s.if test="recaptchaEnabled">
		<script type="text/javascript" src="http://api.recaptcha.net/challenge?k=<@wp.info key="systemParam" paramName="jpwebdynamicform_recaptcha_publickey" />"></script>
		<noscript>
		<iframe src="http://api.recaptcha.net/noscript?k=<@wp.info key="systemParam" paramName="jpwebdynamicform_recaptcha_publickey" />"
				height="300" width="500" frameborder="0"></iframe><br />
<@s.textarea name="recaptcha_challenge_field" rows="3" cols="40" />
<@s.hidden name="recaptcha_response_field" value="manual_challenge" />
		</noscript>
	</@s.if>
	<p class="form-actions">
		<@wp.i18n key="jpwebdynamicform_INVIA" var="labelSubmit" />
		<@wpsf.submit 
			cssClass="btn btn-primary"
			useTabindexAutoIncrement=true 
			value="%{#attr.labelSubmit}" />
	</p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynamicform_is_captchaPage', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>

<@s.set var="titleKey">jpwebdynamicform_TITLE_<@s.property value="typeCode"/></@s.set>
<@s.set var="subtitleKey">jpwebdynamicform_SUBTITLE_<@s.property value="typeCode"/></@s.set>
<@s.set var="typeCodeKey" value="typeCode" />
<@s.set var="myCurrentPage"><@wp.currentPage param="code"/></@s.set>
<h2 class="title-divider"><span><@wp.i18n key="${titleKey}" /></span>
<small><@wp.i18n key="${subtitleKey}" /></small></h2>

<form action="<@wp.action path="/ExtStr2/do/jpwebdynamicform/Message/User/captchaConfirm.action"/>" method="post" id="formContact" >
	<@s.if test="hasFieldErrors()">
	<div class="alert alert-error">
		<ul>
		<@s.iterator value="fieldErrors">
			<@s.iterator value="value">
				<li><@s.property escape="false" /></li>
			</@s.iterator>
		</@s.iterator>
		</ul>
	</div>
	</@s.if>
	<@s.if test="hasActionErrors()">
	<div class="alert alert-error">
		<ul>
			<@s.iterator value="actionErrors">
				<li><@s.property escape="false" /></li>
			</@s.iterator>
		</ul>
	</div>
	</@s.if>
<@s.if test="recaptchaAfterEnabled">
<script type="text/javascript" src="http://api.recaptcha.net/challenge?k=<@wp.info key="systemParam" paramName="jpwebdynamicform_recaptcha_publickey" />"></script>
<noscript>
<iframe src="http://api.recaptcha.net/noscript?k=<@wp.info key="systemParam" paramName="jpwebdynamicform_recaptcha_publickey" />"
 height="300" width="500" frameborder="0"></iframe><br />
<@s.textarea name="recaptcha_challenge_field" rows="3" cols="40" />
<@s.hidden name="recaptcha_response_field" value="manual_challenge" />
</noscript>
</@s.if>
<p>
	<@s.set name="labelSubmit"><@wp.i18n key="jpwebdynamicform_INVIA" /></@s.set>
	<@wpsf.submit useTabindexAutoIncrement=true value="%{#labelSubmit}" cssClass="btn btn-inverse"/>
</p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpwebdynamicform_is_messageSaveConfirmed', NULL, 'jpwebdynamicform', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<@s.set var="titleKey">jpwebdynamicform_TITLE_<@s.property value="typeCode"/></@s.set>
<h1><@wp.i18n key="${titleKey}" /></h1>
<p class="alert alert-success">
	<strong><@wp.i18n key="jpwebdynamicform_MESSAGE_SAVE_CONFIRMATION" /></strong>
	<a href="<@wp.url />">&#32;<@wp.i18n key="jpwebdynamicform_MESSAGE_REQUEST_LINK" /></a>
</p>', 1);