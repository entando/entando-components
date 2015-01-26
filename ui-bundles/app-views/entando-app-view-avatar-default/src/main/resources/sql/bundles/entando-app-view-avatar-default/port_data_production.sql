INSERT INTO sysconfig( version, item, descr, config)  VALUES ('production', 'jpavatar_config', 'jpavatar configuration', '<jpavatar>
	<style>local</style>
</jpavatar>');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpavatar_avatar', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Avatar - Show the current user avatar</property>
<property key="it">Avatar - Mostra l''avatar dell''utente corrente</property>
</properties>', NULL, 'jpavatar', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpavatar/Front/Avatar/edit.action</property>
</properties>
', 1);

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_YOU_HAVE_NO_AVATAR', 'en', 'You don''t have an avatar');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_YOU_HAVE_NO_AVATAR', 'it', 'Non hai nessun avatar');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_CURRENT_AVATAR', 'it', 'Avatar corrente');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_CURRENT_AVATAR', 'en', 'Current Avatar');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_CONFIRM_DELETE', 'en', 'Do You really want to remove your avatar?');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_CONFIRM_DELETE', 'it', 'Vuoi davvero eliminare l''avatar?');


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_AVATAR_SAVED', 'en', 'Avatar successfully saved');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_AVATAR_SAVED', 'it', 'Avatar salvato correttamente');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_TITLE', 'it', 'Avatar');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_TITLE', 'en', 'Avatar');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_DELETE', 'it', 'Rimuovi');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_DELETE', 'en', 'Remove');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_UPLOAD', 'it', 'Carica un''immagine');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_UPLOAD', 'en', 'Upload an image');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_GO_UPLOAD', 'it', 'Carica');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_GO_UPLOAD', 'en', 'Upload');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_OK_THANKYOU', 'it', 'Ok');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpavatar_OK_THANKYOU', 'en', 'Ok');
