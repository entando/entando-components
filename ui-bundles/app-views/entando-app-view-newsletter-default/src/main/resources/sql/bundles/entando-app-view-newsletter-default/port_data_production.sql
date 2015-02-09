INSERT INTO widgetcatalog ( code, plugincode, titles, parenttypecode, defaultconfig, locked ) VALUES ( 'jpnewsletter_registration', 'jpnewsletter', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Newsletter subscription</property>
<property key="it">Iscrizione Newsletter</property>
</properties>', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpnewsletter/Front/RegSubscriber/entry.action</property>
</properties>', 1 );

INSERT INTO widgetcatalog ( code, plugincode, titles, parenttypecode, defaultconfig, locked ) VALUES ( 'jpnewsletter_subscription', 'jpnewsletter', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Conferma Iscrizione Newsletter</property>
<property key="en">Newsletter subscription confirm</property>
</properties>', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpnewsletter/Front/RegSubscriber/subscription.action</property>
</properties>', 1 );

INSERT INTO widgetcatalog ( code, plugincode, titles, parenttypecode, defaultconfig, locked ) VALUES ( 'jpnewsletter_unsubscription', 'jpnewsletter', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Newsletter unsubscription</property>
<property key="it">Cancellazione Iscrizione Newsletter</property>
</properties>', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpnewsletter/Front/RegSubscriber/unsubscriptionEntry.action</property>
</properties>', 1 );




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnewsletter_is_subscription_form', 'jpnewsletter_registration', 'jpnewsletter', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<form class="pluginsForm" action="<@wp.action path="/ExtStr2/do/jpnewsletter/Front/RegSubscriber/addSubscription.action" />" method="post" >
<@s.if test="hasFieldErrors()">
	<div>
	<h3><@s.text name="message.title.FieldErrors" /></h3>
	<ul>
		<@s.iterator value="fieldErrors">
			<@s.iterator value="value">
	            <li><@s.property escape=false /></li>
			</@s.iterator>
		</@s.iterator>
	</ul>
	</div>
</@s.if>
	<p>
		<label for="jpnewsletter_email"><@wp.i18n key="jpnewsletter_LABEL_EMAIL" /></label><br />
		<@wpsf.textfield id="jpnewsletter_email" name="mailAddress" cssClass="text" /> 
	</p>
	<p>
		<@s.set var="jpnewsletter_REGISTER"><@wp.i18n key="jpnewsletter_REGISTER" /></@s.set>
		<@wpsf.submit action="addSubscription" value="%{#jpnewsletter_REGISTER}" cssClass="button" />
	</p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnewsletter_is_subscription_sent_mail', 'jpnewsletter_registration', 'jpnewsletter', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<p>
	<@wp.i18n key="jpnewsletter_SENT_MAIL" />
</p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnewsletter_is_subscription_activation_failed', 'jpnewsletter_subscription', 'jpnewsletter', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<p>
	<@wp.i18n key="jpnewsletter_SUBSCRIPTION_NOT_OK" />
</p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnewsletter_is_subscription_activated', 'jpnewsletter_subscription', 'jpnewsletter', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<p>
	<@wp.i18n key="jpnewsletter_SUBSCRIPTION_OK" />
</p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnewsletter_is_unsubscription_confirm', 'jpnewsletter_unsubscription', 'jpnewsletter', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<@s.if test="%{!mailAddress.equalsIgnoreCase('''')}">
	<form class="pluginsForm" action="<@wp.action path="/ExtStr2/do/jpnewsletter/Front/RegSubscriber/unsubscription.action"/>" method="post">
		<p>
			<@wpsf.hidden name="mailAddress" />
			<@wp.i18n key="jpnewsletter_UNSUB_CONFIRM" />
		</p>
		<p>
			<@s.set var="jpnewsletter_CONFIRM_REMOVE"><@wp.i18n key="jpnewsletter_CONFIRM_REMOVE" /></@s.set>
			<@wpsf.submit cssClass="button" value="%{#jpnewsletter_CONFIRM_REMOVE}" action="unsubscription" />
		</p>
	</form>
</@s.if>
<@s.else>
	<p>
		<@wp.i18n key="jpnewsletter_UNSUB_NOMAIL" />
	</p>
</@s.else>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnewsletter_is_unsubscription_error', 'jpnewsletter_unsubscription', 'jpnewsletter', NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<@s.if test="hasActionErrors()">
<div class="message message_error">
<h2><@s.text name="message.title.ActionErrors" /></h2>	
	<ul>
	<@s.iterator value="actionErrors">
		<li><@s.property escape=false /></li>
	</@s.iterator>
	</ul>
</div>
</@s.if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnewsletter_is_unsubscription_done', 'jpnewsletter_unsubscription', 'jpnewsletter', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<p>
	<@wp.i18n key="jpnewsletter_UNSUB_OK" />
</p>', 1);




INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_CONFIRM_REMOVE', 'it', 'Rimuovi' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_LABEL_EMAIL', 'it', 'Indirizzo email' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_REGISTER', 'it', 'Iscriviti alla newsletter' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_SENT_MAIL', 'it', 'Ti è stata inviata una email al tuo indirizzo di posta con il link per confermare la registrazione.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_SUBSCRIPTION_OK', 'it', 'La registrazione alla newsletter è avvenuta correttamente. Grazie.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_UNSUB_CONFIRM', 'it', 'Confermi la rimozione dalla newsletter?' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_UNSUB_NOMAIL', 'it', 'Nessuna email specificata.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_UNSUB_OK', 'it', 'Rimozione effettuata correttamente.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_SUBSCRIPTION_NOT_OK', 'it', 'Si è verificato un errore in fase di registrazione alla newsletter.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_CONFIRM_REMOVE', 'en', 'Remove' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_LABEL_EMAIL', 'en', 'Email address' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_REGISTER', 'en', 'Subscribe to newsletter' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_SENT_MAIL', 'en', 'We sent you an email with a link to confirm your subscription.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_SUBSCRIPTION_OK', 'en', 'Newsletter''s subscription successfully happened. Thanks.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_UNSUB_CONFIRM', 'en', 'Confirm unsubscription from the newsletter?' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_UNSUB_NOMAIL', 'en', 'None specified email.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_UNSUB_OK', 'en', 'Unsubscription successfully happened.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpnewsletter_SUBSCRIPTION_NOT_OK', 'en', 'An error is happened during newsletter subscription.' );
