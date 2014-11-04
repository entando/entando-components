INSERT INTO widgetcatalog ( code, plugincode, titles, parenttypecode, defaultconfig, locked ) VALUES ( 'jpnewsletter_registration', 'jpnewsletter', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Iscrizione Newsletter</property>
<property key="it">Iscrizione Newsletter</property>
</properties>', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpnewsletter/Front/RegSubscriber/entry.action</property>
</properties>', 1 );

INSERT INTO widgetcatalog ( code, plugincode, titles, parenttypecode, defaultconfig, locked ) VALUES ( 'jpnewsletter_subscription', 'jpnewsletter', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Conferma Iscrizione Newsletter</property>
<property key="it">Conferma Iscrizione Newsletter</property>
</properties>', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpnewsletter/Front/RegSubscriber/subscription.action</property>
</properties>', 1 );

INSERT INTO widgetcatalog ( code, plugincode, titles, parenttypecode, defaultconfig, locked ) VALUES ( 'jpnewsletter_unsubscription', 'jpnewsletter', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Cancellazione Iscrizione Newsletter</property>
<property key="it">Cancellazione Iscrizione Newsletter</property>
</properties>', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpnewsletter/Front/RegSubscriber/unsubscriptionEntry.action</property>
</properties>', 1 );

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
